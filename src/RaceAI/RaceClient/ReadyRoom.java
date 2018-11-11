package RaceAI.RaceClient;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import _LoadResource.LoadResource;

public class ReadyRoom extends JFrame {
	private int S_WIDTH = 1000;
	private int S_HEIGHT = 600;
	public readyPanel readypanel;
	public Client cl;

	public ReadyRoom(final Client cl, Socket socket, int numbroad) {
		this.cl = cl;
		this.setTitle("Car Racing - READYROOM");
		readypanel = new readyPanel(this, socket, numbroad);
		this.add(readypanel);
		setResizable(false);
		pack();
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(null, 
		            "Are you sure to close this window? ", "Really Closing?", 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
						cl.Send2Server("Client_exit;");
		        }
		    }
		});
		setLocationRelativeTo(null);
		setSize(S_WIDTH, S_HEIGHT);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		this.setLocation(
				((int) toolkit.getScreenSize().getWidth() - S_WIDTH) / 2,
				((int) toolkit.getScreenSize().getHeight() - S_HEIGHT) / 2);
		this.setVisible(true);
	}
}

class readyPanel extends JPanel implements MouseListener, MouseMotionListener {
	private int S_HEIGHT = 600;
	private int S_WIDTH = 1000;
	public Socket socket;
	public DataOutputStream dos;
	public int numbroad, numbplayer = 0;
	public String Player_Name[], Player_Level[], Player_Group[];
	public boolean AreYouReady[];
	public boolean myReady, btback;
	public ReadyRoom rr;

	public readyPanel(ReadyRoom rr, Socket socket, int numbroad) {
		this.rr = rr;
		this.socket = socket;
		this.numbroad = numbroad;
		this.setSize(S_WIDTH, S_HEIGHT);
		addMouseListener(this);
		addMouseMotionListener(this);
		init();
	}

	public void init() {
		try {
			this.dos = new DataOutputStream(this.socket.getOutputStream());
			this.dos.writeUTF("Client_inforoom;");
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.dos.writeUTF("Client_infouser;");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myReady = false;
		btback = false;
	}

	public void paintComponent(Graphics g) {
		g.drawImage(LoadResource.ImgBGReadyRoom, 0, 0,
				LoadResource.ImgBGReadyRoom.getWidth(null),
				LoadResource.ImgBGReadyRoom.getHeight(null), null);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 53));
		g.drawString(this.numbroad + "", 410, 80);
		if (numbplayer != 0)
			for (int i = 0; i < numbplayer; i++) {
				g.drawImage(LoadResource.ImgUnderTab,
						LoadResource.PosTableInfo[i].x,
						LoadResource.PosTableInfo[i].y,
						LoadResource.ImgUnderTab.getWidth(null),
						LoadResource.ImgUnderTab.getHeight(null), null);
				g.setFont(new Font("Arial", Font.BOLD, 18));
				g.drawString("UserName: " + this.Player_Name[i],
						LoadResource.PosTableInfo[i].x + 20,
						LoadResource.PosTableInfo[i].y + 20);
				g.setFont(new Font("Arial", Font.ITALIC, 15));
				g.drawString("Level: " + this.Player_Level[i],
						LoadResource.PosTableInfo[i].x + 20,
						LoadResource.PosTableInfo[i].y + 40);
				g.setFont(new Font("Arial", Font.ITALIC, 15));
				g.drawString("Group: " + this.Player_Group[i],
						LoadResource.PosTableInfo[i].x + 20,
						LoadResource.PosTableInfo[i].y + 60);
				if (this.AreYouReady[i])
					g.drawImage(LoadResource.ImgReadyPicture,
							LoadResource.PosTableInfo[i].x,
							LoadResource.PosTableInfo[i].y,
							LoadResource.ImgReadyPicture.getWidth(null),
							LoadResource.ImgReadyPicture.getHeight(null), null);
			}
		if (!this.myReady)
			g.drawImage(LoadResource.btnReady, 832, 520,
					LoadResource.btnReady.getWidth(null),
					LoadResource.btnReady.getHeight(null), null);
		else
			g.drawImage(LoadResource.btnCancel, 832, 520,
					LoadResource.btnCancel.getWidth(null),
					LoadResource.btnCancel.getHeight(null), null);

		if (!this.btback)
			g.drawImage(LoadResource.btnBack, 662, 520,
					LoadResource.btnBack.getWidth(null),
					LoadResource.btnBack.getHeight(null), null);
		else
			g.drawImage(LoadResource.btnBackNone, 662, 520,
					LoadResource.btnBackNone.getWidth(null),
					LoadResource.btnBackNone.getHeight(null), null);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		if (662 <= e.getX() && e.getX() <= 804 && 520 <= e.getY()
				&& e.getY() <= 560)
			btback = true;
		else
			btback = false;
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if (662 <= e.getX() && e.getX() <= 804 && 520 <= e.getY()
				&& e.getY() <= 560) {
			btback = true;
			if (rr.cl.waitroom.roompanel.SoundState)
				SoundManager.MCLICK.play(0);
		} else
			btback = false;
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		btback = false;
		if (832 <= e.getX() && e.getX() <= 974 && 520 <= e.getY()
				&& e.getY() <= 560) {
			if (myReady == false) {
				myReady = true;
				try {
					this.dos.writeUTF("Client_ReadyPlay;Ready;");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
				myReady = false;
				try {
					this.dos.writeUTF("Client_ReadyPlay;NoneReady;");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} else if (662 <= e.getX() && e.getX() <= 804 && 520 <= e.getY()
				&& e.getY() <= 560) {
			if (!myReady) {
				try {
					this.dos.writeUTF("Client_BackRoom;");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				this.rr.dispose();
				this.rr.cl.waitroom.setVisible(true);
			}
		}
		repaint();
	}
}