import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.ec2.*;
import com.amazonaws.services.ec2.model.*;

public class VPCrelated extends mytools{
	
	private AmazonEC2 ec2client = null;
	
	public VPCrelated(AmazonEC2 _ec2){
		ec2client = _ec2;
	}
	//Create a VPC with random CIDR
	public Vpc randomlyVPC(int prefix) throws prefException {
		
		if(prefix > 28 || prefix < 16) {throw new prefException("Prefix Range Error.[18>=prefix>=4]");}
		
		CreateVpcRequest vr = new CreateVpcRequest(rdmCIDR(prefix));
		
        CreateVpcResult res = null;
        
        try{
        	res = ec2client.createVpc(vr);
        }
        catch(Exception e){
        	System.out.println("=====\n"+e.toString()+"\n=====");
        	return null;
        }
        Vpc vpc = res.getVpc();
        System.out.println("VPC_id = "+vpc.getVpcId()+"\nCIDR = "+vpc.getCidrBlock()+"\nCreated.");
        return vpc;
	}
	
	public String rdmCIDR(int prefix){
		
		int strint[] = new int[]{0,0,0,0};
		while(true){
			strint[0] = 0;strint[1] = 0;strint[2] = 0;strint[3] = 0;
			for(int i = 0; i < prefix;i++){
				strint[i/8] += ((int)(Math.random()*2))*(int)Math.pow(2, 7-(i%8));
			}
			if(strint[0]<=223) break; 
		}
	
		
		String toret = Integer.toString(strint[0]);
		for(int i = 1; i < 4;i++){
			toret += ("."+Integer.toString(strint[i]));
		}
		toret += ("/"+prefix);

		return toret;
	}
	
	public Subnet subnetVPC(Vpc vpc, int num){

		String comb[] = vpc.getCidrBlock().split("/");
		int place = 0, base = 1;
		
		while(base < num){
			place++;
			base *= 2;
		}
		String bin = "";
		String[] parts = comb[0].split("\\.");
		for(String part : parts){
			bin += String.format("%8s",Integer.toBinaryString(Integer.valueOf(part))).replace(' ', '0');
		}
		
		List<String> cidrs = new ArrayList<String>();
		for(int i = 0 ; i < base;i++){
			
			String binnum = Integer.toBinaryString(Integer.valueOf(i));
			String finalbin = bin.substring(0, Integer.valueOf(comb[1]))+String.format("%"+place+"s", binnum).replace(' ', '0');
			finalbin = String.format("%-32s",finalbin).replace(' ', '0');
			String finaldec = "";
			String[] part = new String[4];
			
			for(int p = 0,c=0; p < 32;p+=8){
				part[c++] = Integer.toString(Integer.valueOf(finalbin.substring(p, p+8),2));
			}
			
			finaldec = part[0]+"."+part[1]+"."+part[2]+"."+part[3]+"/"+(Integer.valueOf(comb[1])+place);
			
			cidrs.add(finaldec);
		}
		Subnet toret = createSubnetbySet(cidrs,vpc);
		ll("Done.");
		return toret;
		
		
		/*
		CreateSubnetRequest csbnReq = new CreateSubnetRequest();
		csbnReq.setVpcId(vpc.getVpcId());
		*/ //csbnReq.setCidrBlock();
	}
	
	public List<Vpc> getVPC(){
		DescribeVpcsResult dr = ec2client.describeVpcs();
		return dr.getVpcs();
	}
	
	public List<Subnet> getSubnet(){
		return ec2client.describeSubnets().getSubnets();
	}
	
	public Subnet createSubnetbySet(List<String> set,Vpc vpc){
		
		CreateSubnetRequest csbnReq = new CreateSubnetRequest();
		csbnReq.setVpcId(vpc.getVpcId());
		Subnet toret = null;
			for(String cidr : set){
				csbnReq.setCidrBlock(cidr);
				CreateSubnetResult csr = ec2client.createSubnet(csbnReq);
				toret = csr.getSubnet();
				ll("Done for "+cidr);
			}
			return toret;
	}
	
}
