import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class FlashActivity {
	JButton btnBegin;
	JFrame frame = new JFrame("Tu Van Sac dep");
	
	public FlashActivity() {
		frame.setTitle("Tu Van Sac Dep");
		frame.setSize(300, 300);
		
		btnBegin = new JButton("Bat Dau");
		btnBegin.addActionListener(new ActionListener() {
			 
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				new HomeActivity();
			}
		});
		
		frame.setLayout(new FlowLayout());
		frame.add(btnBegin);
		
		frame.setDefaultCloseOperation(3);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	
	public static void main(String[] args) {
		new FlashActivity();
	}
}
