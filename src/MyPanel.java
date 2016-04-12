import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class MyPanel extends JPanel {
	
	ArrayList<Circle> clist;
	ArrayList<STA> slist;
	
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
        for(Circle c : clist){
        	page.drawString("AP"+c.no, c.x+c.radius-10, c.y+c.radius+3);
        	page.drawOval(c.x, c.y,2*c.radius, 2*c.radius);
        }
        
        for(STA s : slist){
        	Color p = new Color((int)s.bw+80,57,0);
        	page.setColor(p);
        	page.fillOval(s.x, s.y, 5, 5);
        }
    }

}
