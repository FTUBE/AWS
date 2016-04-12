import java.util.ArrayList;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.*;

public class SeriousConsole extends mytools{
	
	EC2related ec2;
	VPCrelated vpc;
	AmazonEC2 ec2client;
	
	public SeriousConsole(){
		AWSCredentials cre = new ProfileCredentialsProvider("default").getCredentials();
		ec2client = new AmazonEC2Client(cre);
		ec2 = new EC2related(ec2client,cre);
		vpc = new VPCrelated(ec2client);
	}
	
	public void ca(){
		ll("Terminating all instances.....");
		List<String> wtk = new ArrayList<String>();
		for(Reservation s: ec2.givenInstancesAll()) wtk.add(s.getInstances().get(0).getInstanceId());
		ec2.terminateInstances(wtk);
		ll("Done.");
		ll("Removing Network Interfaces.....");
		List<NetworkInterface> nilist = ec2client.describeNetworkInterfaces().getNetworkInterfaces();
		while(!nilist.isEmpty()){
			for(NetworkInterface ni : ec2client.describeNetworkInterfaces().getNetworkInterfaces()){
				if(ni.getStatus().compareTo(NetworkInterfaceStatus.InUse.toString())==0) continue;
				try{ec2client.deleteNetworkInterface(new DeleteNetworkInterfaceRequest().withNetworkInterfaceId(ni.getNetworkInterfaceId()));
				}
				catch(Exception e){e.printStackTrace();continue;}
			}
			nilist = ec2client.describeNetworkInterfaces().getNetworkInterfaces();
		}
		ll("Done.");
		ll("Removing Subnets.....");
		for(Subnet s : vpc.getSubnet()){
			try{
			ec2client.deleteSubnet(new DeleteSubnetRequest().withSubnetId(s.getSubnetId()));}
				catch(Exception e){
					e.printStackTrace();
					continue;
				}
		}
		ll("Done.");
		ll("Removing VPCs.........");

		for(Vpc v : vpc.getVPC()){
			if(v.isDefault()) continue;
			try{ec2client.deleteVpc(new DeleteVpcRequest().withVpcId(v.getVpcId()));}
			catch(Exception e){
				e.printStackTrace();
				continue;
			}
		
		}
		ll("Done.");
		ll("Removing Snapshots.........");
		ec2.deleteSS();
		ll("Done.");
	}
	
	public void listAll(){
		
		ll("-----VPC : ");
		for(Vpc v : vpc.getVPC()){
			ll("vpcid = "+v.getVpcId()+" CIDR = "+v.getCidrBlock());
		}
		
		ll("-----EC2 Instances : ");
		for(Reservation res : ec2.givenInstancesAll()){
			Instance ins = res.getInstances().get(0);
			ll("id = "+ins.getInstanceId());
			ll("state = "+ins.getState().getName());
			ll("subnet = "+ins.getSubnetId());
			ll("vpc = "+ins.getVpcId());
			List<InstanceNetworkInterface> nlist = ins.getNetworkInterfaces();
			for(InstanceNetworkInterface i : nlist){
				ll("networkI.id = "+i.getNetworkInterfaceId());
			}
			Filter f = new Filter().withName("attachment.instance-id").withValues(ins.getInstanceId());
			List<Volume> vlist = ec2client.describeVolumes(new DescribeVolumesRequest()
					.withFilters(f)).getVolumes();
			
			for(Volume v : vlist){
				ll("volume id = "+v.getVolumeId());
				ll("snapshot id = "+v.getSnapshotId());
				ll("state = "+v.getState());
			}
		}
		
		ll("-----Network Interface : ");
		List<NetworkInterface> nilist = ec2client.describeNetworkInterfaces().getNetworkInterfaces();
		
		for(NetworkInterface ni : nilist){
			ll("id = "+ni.getNetworkInterfaceId());
			ll("status = "+ni.getStatus());
			ll("subnet = "+ni.getSubnetId());
			ll("vpc = "+ni.getVpcId());			
		}
		
		ll("-----Snapshots : ");
		Filter sf = new Filter().withName("owner-id").withValues("521467170983");
		List<Snapshot> slist = ec2client.describeSnapshots(new DescribeSnapshotsRequest().withFilters(sf)).getSnapshots();
		for(Snapshot sn : slist){
			ll("id = "+ sn.getSnapshotId());
			ll("volume = "+ sn.getVolumeId());
			ll("state = "+sn.getState());
		}
		ll("-----Subnet : ");
		List<Subnet> snlist  = vpc.getSubnet();
		for(Subnet sn: snlist){
			ll("id = "+sn.getSubnetId());
			ll("CIDR = "+sn.getCidrBlock());
		}	
		
	}
}

