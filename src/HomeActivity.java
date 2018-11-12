import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class LuongXuLiMotKetNoiDen extends Thread {
	Socket soc;
	int thuTu;
	JFrame frame;
	JTextField field = new JTextField();
	JTextArea textArea = new JTextArea(10, 40);

	DataInputStream in;
	DataOutputStream out;
	String tenKhachHang;
	String tenTrungTam;
	
	public LuongXuLiMotKetNoiDen(Socket soc, int thuTu) {
		this.soc = soc;
		this.thuTu = thuTu;
		this.tenKhachHang = "CLIENT #"+thuTu+" :  ";
		this.tenTrungTam = "TRUNG TÂM :  ";
		
		//start 1 Jframe
		frame = new JFrame("Server -> Client #"+thuTu);
		frame.setSize(400, 400);
		
        frame.setLayout(new BorderLayout());
        
        field.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String msgGui = field.getText().toString().trim();
				if( msgGui.equals("") || msgGui == null ) {
					
				}			
				else {
					if( msgGui.equals(".") ) {
						textArea.append("Thông báo: Bạn đã ngắt cuộc tư vấn! \n");
						JOptionPane.showMessageDialog(frame, "Thông báo: Bạn đã ngắt cuộc tư vấn!");
						frame.setVisible(false);
					}
					else {
						textArea.append(tenTrungTam + msgGui + "\n");
					}				
					try {
						out.writeUTF(msgGui);
						field.setText("");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
			}
		} );
		
        textArea.append("Chú ý: Bạn có thể nhập . để kết thúc cuộc tư vấn! \n");
        textArea.setLineWrap(true);
		frame.add(field, BorderLayout.NORTH);
		frame.add(textArea, BorderLayout.CENTER);
		
		frame.setDefaultCloseOperation(3);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	@Override
	public void run() {
		if(soc != null) {
			try {				
				in = new DataInputStream( soc.getInputStream() );
				out =  new DataOutputStream( soc.getOutputStream() );
				
				out.writeUTF("( Bạn có thể nhấn . để kết thúc cuộc tư vấn! )");
				out.writeUTF("Xin chao Client #"+thuTu+ " .Bạn cần giúp gì? ");
				
				
				while(true) {
					//System.out.println(tenKhachHang + "van dang trong ngóng msgNhan");
					String msgNhan = in.readUTF().toString().trim();
					if( msgNhan.equals(".") || msgNhan == ".") {
						textArea.append("Thông báo: Khách hàng đã dừng cuộc tư vấn! \n");
						JOptionPane.showMessageDialog(frame, "Thông báo: Khách hàng đã dừng cuộc tư vấn!");
						frame.setVisible(false);
						break;
					}
					else textArea.append( tenKhachHang + msgNhan + "\n");
					
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
		
	
}

class LuongLangNgheCacClient extends Thread{
	ServerSocket server;
	public LuongLangNgheCacClient(ServerSocket server) {
		this.server = server;
	}
	
	public void tatServer() {
		try {
			this.server.close(); this.server = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run() {		
			int thuTu = 0;
			
			while(true) {	
				System.out.println("Dang lang nhe ccac client ket noi den..");
				if(server == null) break;
				Socket soc;
				try {
					soc = server.accept();
					System.out.println("Da co ket noi Client tai"+soc.getInetAddress().toString());
					new LuongXuLiMotKetNoiDen(soc, thuTu++).start();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
	
	}
}



public class HomeActivity extends JFrame{
	JButton btnBatServerTuVan, btnTatServerTuVan;
    static ServerSocket server=null;
    JFrame jFrame = new JFrame("Trang chu");
    JTextArea textArea;
    LuongLangNgheCacClient luongLangNgheCacClient=null;
	
	public HomeActivity() {
		jFrame.setSize(1000, 600);
		
		textArea = new JTextArea(20, 80);
		textArea.setEditable(false);
		
		btnBatServerTuVan = new JButton("Bat Server Tu Van");
		btnBatServerTuVan.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(server == null) {
						int port=2000;
						server = new ServerSocket(port);
						JOptionPane.showMessageDialog(jFrame, "Đã bật server tư vấn, đang chờ các Client kêt nối");
					    Date date = new Date();
						textArea.append(date.toString()+"  :"+"Server tư vấn đang chạy tại cổng :"+port+"\n");
						LangNgheClient();
						
					}else {
						JOptionPane.showMessageDialog(jFrame, "Server đã bật!");
					}
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnTatServerTuVan = new JButton("Tắt server tư vấn");
		btnTatServerTuVan.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				if(server != null) {
					try {
						server.close();server = null;
						luongLangNgheCacClient.tatServer();
						JOptionPane.showMessageDialog(jFrame, "Đã tắt server");
						Date date = new Date();
						textArea.append(date.toString()+"  :"+"Server tư vấn đã tắt..\n");
						
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}else {
					JOptionPane.showMessageDialog(jFrame, "Server chưa bật");
				}
				
			}
		});
		
		
		
		jFrame.setLayout(new FlowLayout());
		jFrame.add(btnBatServerTuVan);
		jFrame.add(btnTatServerTuVan);
		jFrame.add(textArea);
		
		jFrame.setDefaultCloseOperation(3);
		jFrame.setLocationRelativeTo(null);
		jFrame.setVisible(true);
				
	}
	
	public void LangNgheClient() {
		if(server != null) {
			luongLangNgheCacClient = new LuongLangNgheCacClient(server);
			luongLangNgheCacClient.start();
		}
	}
	
		
}
