import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.directory.model.DeleteSnapshotResult;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.*;

public class EC2related extends mytools{
	
	private AmazonEC2 ec2client = null;
	private AWSCredentials cre = null;
	public EC2related(AmazonEC2 _client, AWSCredentials _cre){
		ec2client = _client;
		cre = _cre;
	}
	
	public List<Image> getAllImages(){
		
        DescribeImagesRequest req = new DescribeImagesRequest();
        ArrayList<String> us = new ArrayList<String>();
        us.add("self");
        req.setOwners(us);
        List<Image> images = ec2client.describeImages(req).getImages();
		
        return images;
	}
	
	public Instance runInstanceWithAMI_block(Image ami, Subnet sbn){
		Instance ins = runInstanceWithAMI(ami,sbn);
		waitFor(ins,"running");
		return ins;
	}
	
	public Instance runInstanceWithAMI(Image ami, Subnet sbn){
		
		RunInstancesRequest rReq = new RunInstancesRequest();
        rReq.setImageId(ami.getImageId());
        rReq.setMaxCount(1);
        rReq.setMinCount(1);// Are above 2 lines needed?
        rReq.setInstanceType("t2.micro");
        rReq.setSubnetId(sbn.getSubnetId());
        RunInstancesResult res = null;
        try{
        	res = ec2client.runInstances(rReq);
        }
        catch(AmazonServiceException e){
        	if(e.getErrorCode().compareTo("InstanceLimitExceeded")==0){
        		ll("Instance full.");
        		return null;
        	}
        	else e.printStackTrace();
        }

        return res.getReservation().getInstances().get(0);
	}
	
	public void StopInstanceWithBlock(Instance ins){
		ec2client.stopInstances(new StopInstancesRequest().withInstanceIds(ins.getInstanceId()));
		waitFor(ins,"stopped");
	}
	
	public void StartInstanceWithBlock(Instance ins){
		ec2client.startInstances(new StartInstancesRequest().withInstanceIds(ins.getInstanceId()));
		waitFor(ins,"running");
	}
	
	public void waitFor(Instance ins, String expected){
		String state = ec2client.describeInstances(new DescribeInstancesRequest()
				.withInstanceIds(ins.getInstanceId()))
				.getReservations().get(0).getInstances().get(0).getState().getName();
		
		while(state.compareTo(expected) != 0){
			//ll(state);
			state = ec2client.describeInstances(new DescribeInstancesRequest()
						.withInstanceIds(ins.getInstanceId()))
						.getReservations().get(0).getInstances().get(0).getState().getName();
		}
	}
	
	public void terminateInstances(List<String> bootID){
		
		if(bootID.isEmpty()) return;
        TerminateInstancesRequest tReq = new TerminateInstancesRequest();
        tReq.setInstanceIds(bootID);
        ec2client.terminateInstances(tReq);
       
	}
	
	public List<Reservation> givenInstances(){
		
		Filter f = new Filter().withName("instance-state-name").withValues("running");
		DescribeInstancesRequest dr = new DescribeInstancesRequest().withFilters(f);
		DescribeInstancesResult ds = ec2client.describeInstances(dr);
		return ds.getReservations();
	}
	
	//Return instances including dead shit.
	public List<Reservation> givenInstancesAll(){
		
		DescribeInstancesRequest dr = new DescribeInstancesRequest();
		DescribeInstancesResult ds = ec2client.describeInstances(dr);
		return ds.getReservations();
	}
	
	public Snapshot TakeSS(Instance ins){
		
		Filter f = new Filter().withName("attachment.instance-id").withValues(ins.getInstanceId());
		String volumeId = ec2client.describeVolumes(new DescribeVolumesRequest().withFilters(f)).getVolumes().get(0).getVolumeId();
		CreateSnapshotResult csr = ec2client.createSnapshot(new CreateSnapshotRequest().withVolumeId(volumeId));
		
		return csr.getSnapshot();
		
	}
	
	public void deleteSS(){
		Filter f_all = new Filter().withName("owner-id").withValues("521467170983");
		
			for(Snapshot s: ec2client.describeSnapshots(new DescribeSnapshotsRequest()
					.withFilters(f_all)).getSnapshots()){
				try{ec2client.deleteSnapshot(new DeleteSnapshotRequest().withSnapshotId(s.getSnapshotId()));}
				catch(AmazonServiceException ase){
					if(ase.getErrorCode().compareTo("InvalidSnapshot.InUse")==0){
						ll("InUse.Jumps for the next one.");
						continue;
					}
				}
				ll(s.getSnapshotId()+" is fucked off.");
			}
			ll("Everything fucked off already.");
			return;
	}
	
	public Instance moveFromTo(Instance ins, Subnet s1, Subnet s2) throws InterruptedException{
		
		ll("-----Moving instance "+ins.getInstanceId()+" from subnet "+s1.getCidrBlock()+" to subnet "+s2.getCidrBlock());
		
		ll("Lauching new one in subnet (Pipelining)" + s2.getCidrBlock());
		Instance newins = this.runInstanceWithAMI(this.getAllImages().get(0), s2);
		
		ll("Stop instance " + ins.getInstanceId()+".........");
		this.StopInstanceWithBlock(ins);

		Filter vf = new Filter().withName("attachment.instance-id").withValues(ins.getInstanceId());
		DescribeVolumesResult dvr = ec2client.describeVolumes(new DescribeVolumesRequest().withFilters(vf));
		List<Volume> vlist = dvr.getVolumes();
		Volume backup = vlist.get(0);
		String vid = backup.getVolumeId();//Choose the first volume id to be removed because there is only one volume.
		
		ll("Detaching Volume " +vid+".....");
		
		ec2client.detachVolume(new DetachVolumeRequest().withVolumeId(vid));
		
		ll("Stop.....");
		this.StopInstanceWithBlock(newins);
		
		ll("Detach.....");
		Filter uselessvf = new Filter().withName("attachment.instance-id").withValues(newins.getInstanceId());
		dvr = ec2client.describeVolumes(new DescribeVolumesRequest().withFilters(uselessvf));
		List<Volume> uvlist = dvr.getVolumes();
		String uselessvid = uvlist.get(0).getVolumeId();
		ec2client.detachVolume(new DetachVolumeRequest().withVolumeId(uselessvid));
		
		ll("Attach......");
		ec2client.attachVolume(new AttachVolumeRequest()
				.withVolumeId(backup.getVolumeId())
				.withInstanceId(newins.getInstanceId())
				.withDevice("/dev/xvda"));
		
		ll("Starting........");
		ec2client.startInstances(new StartInstancesRequest().withInstanceIds(newins.getInstanceId()));
		
		ll("Terminating old one........");
		ec2client.terminateInstances(new TerminateInstancesRequest().withInstanceIds(ins.getInstanceId()));
		
		
		return null;
	}
}
	
