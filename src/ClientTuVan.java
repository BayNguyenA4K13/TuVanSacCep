import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.AttributedString;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

public class ClientTuVan {
	JFrame frame = new JFrame("Client->Server Tu van");
	JTextField field = new JTextField();
	JTextArea textArea = new JTextArea(10, 40);
	static Socket soc=null;
	DataInputStream in;
	DataOutputStream out;
	String tenTrungTam;
	String tenKhachHang;
	
	public ClientTuVan() {
		this.tenTrungTam = "TRUNG TÂM :  ";
		this.tenKhachHang = "BẠN :  ";
		
		frame.setSize(400, 400);
		
		frame.setLayout(new BorderLayout());
		
		field.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String msgGui = field.getText().toString().trim();
					if( msgGui.equals("") || msgGui == null) {
						
					}else {
						if( msgGui.equals(".") ) {
							textArea.append("Thông báo: Bạn đã ngắt cuộc tư vấn!");
							JOptionPane.showMessageDialog(frame, "Thông báo: Bạn đã ngắt cuộc tư vấn!");
							frame.setVisible(false);
						}else {
							textArea.append(tenKhachHang + msgGui + "\n" );
						}
					}
										
					out.writeUTF( field.getText().toString() );
					field.setText("");
					
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		frame.add(field, BorderLayout.NORTH);
		frame.add(textArea, BorderLayout.CENTER);
		
		frame.setDefaultCloseOperation(3);
		frame.setLocationRelativeTo(null);
	}
	
	public void ketNoiDenServer() {
	
		try {
			soc = new Socket("localhost", 2000);
			
			in = new DataInputStream( soc.getInputStream() );
			out = new DataOutputStream( soc.getOutputStream() );
	
			textArea.append(tenTrungTam + in.readUTF() + "\n" );
			textArea.append(tenTrungTam + in.readUTF() + "\n" );
			
			while(true) {
				//System.out.println(tenKhachHang + "van dang trong ngóng msgNhan");
				String msgNhan = in.readUTF();
				if( msgNhan.equals(".") || msgNhan == ".") {
					textArea.append("Thông báo: Trung tâm đã dừng cuộc tư vấn! \n");
					JOptionPane.showMessageDialog(frame, "Thông báo: Trung tâm đã dừng cuộc tư vấn!");
					frame.setVisible(false);
					break;
				}
				
				textArea.append(tenTrungTam + msgNhan + "\n"); 
				
			}
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ClientTuVan client = new ClientTuVan();
		client.frame.setVisible(true);
		client.ketNoiDenServer();
				
	}
	
	
}
