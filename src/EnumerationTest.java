import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EnumerationTest {
	
	int[] APs,STAs;
	ArrayList<int[]> conn;
	int[][] matrix;
	ArrayList<Integer> stddev = new ArrayList<Integer>();
	int apN,staN;
	Map<Double,Integer> result = new HashMap<Double,Integer>();
	
	public EnumerationTest(){
		APs = new int[]{10,11,15,19,11,10,11,15,19,11,10,11};
		STAs = new int[]{5,3,6,8,6,9,2,7,6,5};
		conn = new ArrayList<int[]>();
		
		int[] poss = new int[APs.length];
		for(int i = 1; i <= APs.length;i++){
			poss[i-1] = i;
		}
		
		for(int i = 0;i < STAs.length;i++){
			conn.add(poss);
		}
		
		apN = APs.length;
		staN = STAs.length;
		matrix = new int[staN][apN];
	}
	public void printM(int[][] matrix){
		for(int i = 0; i < matrix.length;i++){
			for(int n = 0; n < matrix[i].length;n++){
				if(matrix[i][n]==0) {System.out.print("  ");continue;}
				System.out.print(matrix[i][n]+" ");
			}
			System.out.println();
		}
	}
	
	public void traverse(int row, int stand, ArrayList<Integer> record,Double thresh){
		
		if(row==staN){
			stddev.clear();
			
			for(int i = 0; i < record.size();i++){
				int col = record.get(i)-1;
				matrix[i][col] = STAs[i];
			}
			
			for(int ap = 0; ap < apN; ap++){
				int sum = 0;
				for(int sta = 0; sta < staN;sta++){
					sum += matrix[sta][ap];
				}
				stddev.add(sum);
				
				if(sum > APs[ap]*thresh){
					//System.out.println("Nonsense.");
					for(int i = 0; i < record.size();i++){
						matrix[i][record.get(i)-1] = 0;
					}
					return;
				}
			}
			double total=0;
			double dev = 0;
			for(int i = 0; i < apN;i++) total += stddev.get(i);
			double avg = total/(double)apN;
			for(int i = 0; i < apN;i++) dev += Math.pow((stddev.get(i)-avg),2);
			result.put(dev, 0);
			if(dev<500){
				System.out.println("DEV = "+dev);
				printM(matrix);
				System.out.println("============");
			}
			for(int i = 0; i < record.size();i++){
				matrix[i][record.get(i)-1] = 0;
			}
			return;
		}
		
		for(int i = stand; i < conn.get(row).length;i++){
			//matrix[row][stand] = x;
			record.add(conn.get(row)[i]);
			traverse(row+1,0,record,0.6);
			record.remove(record.size()-1);
		}
		
	}
	public static void main(String[] args){
		
		EnumerationTest et = new EnumerationTest();
		ArrayList<Integer> li = new ArrayList<Integer>();
		et.traverse(0,0,li,0.6);
		double min= Double.MAX_VALUE;
		for(Double dev : et.result.keySet()){
			min = Math.min(min, dev);
		}
		System.out.println("min value = "+min);
		
	}
}
