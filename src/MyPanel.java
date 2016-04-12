import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class MyPanel extends JPanel {
	
	ArrayList<Circle> clist;
	
    MyPanel()
    {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(250,250));
        clist = new ArrayList<Circle>();
    }

    public void paintComponent(Graphics page)
    {
        super.paintComponent(page);
        for(Circle c : clist)
        	page.drawOval(c.x, c.y,2*c.radius, 2*c.radius);
    }

}
