
public class Circle {
	int x,y,radius,no,capa;
	public Circle(int _x, int _y, int _r,int _n, int _capa){
		x = _x; y = _y; radius = _r;no=_n;capa = _capa;
	}
	public static void main(String[] args){
		for(int i = 0; i < 1000;i++)
			System.out.println((int)(2*Math.random())*2-1);
	}
}
