import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class MyPanel extends JPanel {
    MyPanel()
    {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(250,250));
    }

    public void paintComponent(Graphics page)
    {
        super.paintComponent(page);
        page.drawOval(0, 0, 100, 100);
    }

}
