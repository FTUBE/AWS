import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSpinner;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class window {

	private JFrame frame;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window w = new window();
					w.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public window() {
		initialize();
	}
	//Circle shit.
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 1024, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblAp = new JLabel("AP");
		MyPanel mypanel = new MyPanel();
		
		JSpinner APx = new JSpinner();
		APx.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(mypanel.clist.isEmpty()) return;
				mypanel.clist.get(mypanel.clist.size()-1).x = (int)APx.getValue();
				mypanel.repaint();
			}
		});
		
		JSpinner APy = new JSpinner();
		APy.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(mypanel.clist.isEmpty()) return;
				mypanel.clist.get(mypanel.clist.size()-1).y = (int)APy.getValue();
				mypanel.repaint();
			}
		});
		
		JSpinner APrad = new JSpinner();
		APrad.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(mypanel.clist.isEmpty()) return;
				mypanel.clist.get(mypanel.clist.size()-1).radius = (int)APrad.getValue();
				mypanel.repaint();
			}
		});
		JButton btnNewButton = new JButton("AP");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblAp.setText("AP");
				mypanel.clist.add(new Circle(0,0,50,mypanel.clist.size()+1));
				APx.setValue(0);
				APy.setValue(0);
				APrad.setValue(50);
				mypanel.repaint();
			}
		});
		
		JLabel lblApx = new JLabel("APx:");
		
		JLabel lblApy = new JLabel("APy:");
		
		JLabel lblAprad = new JLabel("APrad:");
		
		JSpinner STAbw = new JSpinner();
		STAbw.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(mypanel.slist.isEmpty()) return;
				STA s = mypanel.slist.get(mypanel.slist.size()-1);
				s.bw = (int) STAbw.getValue();
				mypanel.repaint();
			}
		});
		
		JSpinner STAy = new JSpinner();
		STAy.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(mypanel.slist.isEmpty()) return;
				STA s = mypanel.slist.get(mypanel.slist.size()-1);
				s.y = (int) STAy.getValue();
				mypanel.repaint();
			}
		});
		
		JSpinner STAx = new JSpinner();
		STAx.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(mypanel.slist.isEmpty()) return;
				STA s = mypanel.slist.get(mypanel.slist.size()-1);
				s.x = (int) STAx.getValue();
				mypanel.repaint();
			}
		});
		
		JButton btnSta = new JButton("STA");
		btnSta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblAp.setText("STA");
				mypanel.slist.add(new STA(mypanel.slist.size()-1, 0, 0, 0));
				mypanel.repaint();
			}
		});
		
		JLabel lblStax = new JLabel("STAx:");
		
		JLabel lblStay = new JLabel("STAy:");
		
		JLabel lblStabw = new JLabel("STAbw:");
		
		
		//Layout shit.
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(40)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblAp)
							.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(mypanel, GroupLayout.PREFERRED_SIZE, 450, GroupLayout.PREFERRED_SIZE)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED, 458, Short.MAX_VALUE)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(btnSta)
										.addComponent(btnNewButton))
									.addGap(15))
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(50)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(lblApx)
										.addComponent(lblApy, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblAprad, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE))
									.addGap(47)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
										.addComponent(APrad, GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
										.addComponent(APy)
										.addComponent(APx))
									.addGap(74)
									.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addComponent(lblStax)
										.addComponent(lblStay)
										.addComponent(lblStabw))
									.addGap(18)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
										.addComponent(STAbw, GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
										.addComponent(STAy)
										.addComponent(STAx))
									.addGap(153))))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(15)
							.addComponent(mypanel, GroupLayout.PREFERRED_SIZE, 450, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnNewButton)
							.addGap(18)
							.addComponent(btnSta)
							.addGap(19)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblApx)
								.addComponent(APx, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(STAx, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblStax))
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblApy)
								.addComponent(APy, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(STAy, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblStay))
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblAprad)
								.addComponent(APrad, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(STAbw, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblStabw))))
					.addPreferredGap(ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
					.addComponent(lblAp)
					.addGap(35))
		);
		frame.getContentPane().setLayout(groupLayout);
	}
}
