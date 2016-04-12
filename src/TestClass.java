import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.ec2.model.*;

public class TestClass extends mytools{
	public static void main(String[] args) throws InterruptedException {
		
		SeriousConsole sc = new SeriousConsole();
		
		Image myAMI = sc.ec2.getAllImages().get(0);
		//sc.ec2.runInstanceWithAMI(myAMI, sc.vpc.getSubnet().get(0));
		List<Reservation> ilist = sc.ec2.givenInstances();
		
		while(ilist.isEmpty()){
			ilist = sc.ec2.givenInstances();
		}
		
		Instance ins = ilist.get(0).getInstances().get(0);
		ll("Instance "+ins.getInstanceId()+" created.");
		
		Subnet s1 = sc.vpc.getSubnet().get(0);
		Subnet s2 = sc.vpc.getSubnet().get(1);
		
		sc.ec2.moveFromTo(ins, s2, s1);
	}
}
