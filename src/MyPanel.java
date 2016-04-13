import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

public class MyPanel extends JPanel {
	
	ArrayList<Circle> clist;
	ArrayList<STA> slist;
	
	static enum state{AP,STA,LINE,ALL,NONE};
	
	state cur = state.NONE;
	int[][] matrix = new int[99][99];
	int[][] conn = new int[99][99];
	int[][] prev = new int[99][99];
	
    MyPanel()
    {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(250,250));
        clist = new ArrayList<Circle>();
        slist = new ArrayList<STA>();
    }

    public void paintComponent(Graphics page)
    {
        super.paintComponent(page);
        
        	for(int i = 0; i < slist.size();i++){
        		for(int j = 0; j < clist.size();j++){
        			if(conn[i][j] == 1){
        				STA s = slist.get(i);
        				Circle c = clist.get(j);
        				page.setColor(Color.RED);
        				page.drawLine(s.x+5, s.y+5, c.x+c.radius, c.y+c.radius);
        			}
        		}
        	}

        page.setColor(Color.BLACK);
        for(Circle c : clist){
        	page.drawString("AP"+c.no, c.x+c.radius-10, c.y+c.radius+3);
        	page.drawOval(c.x, c.y,2*c.radius, 2*c.radius);
        }
        
        for(STA s : slist){
        	Color p = new Color((int)s.bw+80,57,0);
        	page.setColor(p);
        	page.fillOval(s.x, s.y, 10, 10);
        }

    }

    
	public void doshit() {
		//cleanmatrix();
		
		for(STA s : slist){
			//ArrayList<Integer> res = new ArrayList<Integer>();
			//System.out.print("STA "+ s.no + " can connect to AP");
			for(Circle c : clist){
				if(checkyoupriv(c, s)) {
				conn[s.no-1][c.no-1] = 1;
				//System.out.print(" "+c.no);
				}
				else{
				conn[s.no-1][c.no-1] = 0;
				}
			}
		}
		
		if(isstateChanged()) System.out.println("Changed");
		
		repaint();
}

	private boolean isstateChanged() {
		boolean changed = false;
		for(int i = 0 ; i < slist.size();i++){
			for(int j = 0; j < clist.size();j++){
				if(conn[i][j] != prev[i][j]) changed = true;
				prev[i][j] = conn[i][j];
			}
		}
		return changed;
	}

	private void cleanmatrix() {
		for(int i = 0; i < slist.size();i++){
			for(int j = 0; j < clist.size();j++) matrix[i][j] = 0;
		}
		
	}

	private boolean checkyoupriv(Circle c, STA s){
		int cx = c.x + c.radius;
		int cy = c.y + c.radius;
		int px = s.x;
		int py = s.y;
		if((Math.pow(py-cy,2) + Math.pow(px-cx,2)) > Math.pow(c.radius,2)) return false;
		return true;
	}
}
