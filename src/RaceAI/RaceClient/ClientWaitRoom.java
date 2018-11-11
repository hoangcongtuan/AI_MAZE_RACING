package RaceAI.RaceClient;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import _LoadResource.LoadResource;

public class ClientWaitRoom extends JFrame {
	private int S_WIDTH = 1000;
	private int S_HEIGHT = 600;
	public WaitRoomPanel roompanel;
	public JFrame frame;

	public ClientWaitRoom(Socket socket, final Client cl) {
		frame = this;
		this.setTitle("Car Racing - WAITROOM");
		roompanel = new WaitRoomPanel(socket, cl);
		this.add(roompanel);
		setResizable(false);
		pack();
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				int response = JOptionPane.showConfirmDialog(frame,
						"Are you sure to close this window?",
						"Really Closing?", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.YES_OPTION) {
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

class WaitRoomPanel extends JPanel implements MouseListener,
		MouseMotionListener {
	private int S_HEIGHT = 600;
	private int S_WIDTH = 1000;
	public boolean[] mark;
	public Vector<Room> room;
	public String nameUser = "_user_", levelUser = "0",
			contentmes = "Racing AI Coding!";
	public Socket socket;
	public DataOutputStream dos;
	public int btjoin, btexit;
	public Client cl;
	public int toadotbX, toadotbY = 135;
	public boolean SoundState = true;

	//
	// File f = new File("D:/Songs/preview.mp3");
	// MediaLocator ml = new MediaLocator(f.toURL());
	// Player p = Manager.createPlayer(ml);
	// p.start();

	public WaitRoomPanel(Socket socket, Client cl) {
		this.socket = socket;
		this.cl = cl;
		this.setSize(S_WIDTH, S_HEIGHT);
		addMouseListener(this);
		addMouseMotionListener(this);
		init();
	}

	public void init() {
		mark = new boolean[6];
		room = new Vector<Room>();
		btjoin = 0;
		btexit = 0;
		for (int i = 0; i < 6; i++) {
			mark[i] = false;
		}
		try {
			this.dos = new DataOutputStream(this.socket.getOutputStream());
			this.dos.writeUTF("Client_infoRoom;");
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
		new ShowMessage(this).start();
		SoundManager.init();
		SoundManager.MNEN.play(Clip.LOOP_CONTINUOUSLY);
		;
	}

	public void paintComponent(Graphics g) {
		g.drawImage(LoadResource.ImgBGWaitRoom, 0, 0,
				LoadResource.ImgBGWaitRoom.getWidth(null),
				LoadResource.ImgBGWaitRoom.getHeight(null), null);

		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 17));
		g.drawString(contentmes, toadotbX, toadotbY);
		g.drawImage(LoadResource.ImgGhep1, 0, 108,
				LoadResource.ImgGhep1.getWidth(null),
				LoadResource.ImgGhep1.getHeight(null), null);
		g.drawImage(LoadResource.ImgGhep2, 724, 114,
				LoadResource.ImgGhep2.getWidth(null),
				LoadResource.ImgGhep2.getHeight(null), null);
		// xuat thong tin nguoi choi: ten,level
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.drawString("User Information", 750, 80);
		g.setFont(new Font("Arial", Font.BOLD, 17));
		g.drawString("User Name: " + nameUser, 790, 135);
		g.drawString("Level: " + levelUser, 835, 160);
		// sound
		if (SoundState)
			g.drawImage(LoadResource.ImgsoundON, 930, 10,
					LoadResource.ImgsoundON.getWidth(null),
					LoadResource.ImgsoundON.getHeight(null), null);
		else
			g.drawImage(LoadResource.ImgsoundOFF, 930, 10,
					LoadResource.ImgsoundOFF.getWidth(null),
					LoadResource.ImgsoundOFF.getHeight(null), null);

		// xuat thong tin duong dua
		for (int i = 0; i < room.size(); i++) {
			g.setColor(Color.RED);
			g.setFont(new Font("Arial", Font.BOLD, 20));
			int nameVal = Integer.parseInt(room.get(i).NameRoom);
			g.drawImage(LoadResource.ImgRoadNum,
					LoadResource.PosRoadNum[nameVal].x,
					LoadResource.PosRoadNum[nameVal].y,
					LoadResource.ImgRoadNum.getWidth(null),
					LoadResource.ImgRoadNum.getHeight(null), null);
			if (mark[nameVal] == true) {
				g.drawImage(LoadResource.ImgMarkRoad,
						LoadResource.PosRoadNum[nameVal].x,
						LoadResource.PosRoadNum[nameVal].y,
						LoadResource.ImgMarkRoad.getWidth(null),
						LoadResource.ImgMarkRoad.getHeight(null), null);
			}
			if (room.get(i).playing)
				g.drawImage(LoadResource.Imgplaying,
						LoadResource.PosRoadNum[nameVal].x - 10,
						LoadResource.PosRoadNum[nameVal].y + 15,
						LoadResource.Imgplaying.getWidth(null),
						LoadResource.Imgplaying.getHeight(null), null);
			g.drawString("Road Number " + nameVal,
					LoadResource.PosRoadNum[nameVal].x - 30,
					LoadResource.PosRoadNum[nameVal].y - 10);
			g.setColor(Color.BLACK);
			g.setFont(new Font("Arial", Font.ITALIC, 10));
			g.drawString("Number player: (" + room.get(i).num_player + "/"
					+ room.get(i).max_player + ")",
					LoadResource.PosRoadNum[nameVal].x,
					LoadResource.PosRoadNum[nameVal].y + 100);
			g.drawString("Request min level: " + room.get(i).min_lever,
					LoadResource.PosRoadNum[nameVal].x,
					LoadResource.PosRoadNum[nameVal].y + 115);
			g.drawString("Name map: " + room.get(i).NameMap,
					LoadResource.PosRoadNum[nameVal].x,
					LoadResource.PosRoadNum[nameVal].y + 130);
		}

		for (int i = 0; i < 6; i++) {
			if (this.Find(i + "") == null) {
				g.drawImage(LoadResource.ImgLock, LoadResource.PosRoadNum[i].x,
						LoadResource.PosRoadNum[i].y,
						LoadResource.ImgLock.getWidth(null),
						LoadResource.ImgLock.getHeight(null), null);
			}
		}
		// button
		if (btjoin == 0)
			g.drawImage(LoadResource.btnJoin, 297, 520,
					LoadResource.btnJoin.getWidth(null),
					LoadResource.btnJoin.getHeight(null), null);
		else if (btjoin == 1)
			g.drawImage(LoadResource.btnJoinNone, 297, 520,
					LoadResource.btnJoinNone.getWidth(null),
					LoadResource.btnJoinNone.getHeight(null), null);

		if (btexit == 0)
			g.drawImage(LoadResource.btnExit, 544, 520,
					LoadResource.btnExit.getWidth(null),
					LoadResource.btnExit.getHeight(null), null);
		else if (btexit == 1)
			g.drawImage(LoadResource.btnExitNone, 544, 520,
					LoadResource.btnExitNone.getWidth(null),
					LoadResource.btnExitNone.getHeight(null), null);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		if (297 <= e.getX() && e.getX() <= 439 && 520 <= e.getY()
				&& e.getY() <= 560) {
			btjoin = 1;
			btexit = 0;
		} else if (544 <= e.getX() && e.getX() <= 686 && 520 <= e.getY()
				&& e.getY() <= 560) {
			btjoin = 0;
			btexit = 1;
		} else {
			btjoin = 0;
			btexit = 0;
		}
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
		if (297 <= e.getX() && e.getX() <= 439 && 520 <= e.getY()
				&& e.getY() <= 560) {
			btjoin = 1;
			btexit = 0;
			if (this.SoundState)
				SoundManager.MCLICK.play(0);
		} else if (544 <= e.getX() && e.getX() <= 686 && 520 <= e.getY()
				&& e.getY() <= 560) {
			btjoin = 0;
			btexit = 1;
			if (this.SoundState)
				SoundManager.MCLICK.play(0);
		} else if (930 <= e.getX() && e.getX() <= 980 && 10 <= e.getY()
				&& e.getY() <= 50) {
			if (this.SoundState) {
				this.SoundState = false;
				SoundManager.MNEN.stop();
			} else {
				this.SoundState = true;
				SoundManager.MNEN.play(Clip.LOOP_CONTINUOUSLY);
			}
		} else {
			for (int k = 0; k < 6; k++) {
				mark[k] = false;
			}
			btjoin = 0;
			for (int i = 0; i < this.room.size(); i++) {
				int nameVal = Integer.parseInt(this.room.get(i).NameRoom);
				if (LoadResource.PosRoadNum[nameVal].x <= e.getX()
						&& e.getX() < LoadResource.PosRoadNum[nameVal].x + 86
						&& LoadResource.PosRoadNum[nameVal].y <= e.getY()
						&& e.getY() < LoadResource.PosRoadNum[nameVal].y + 86) {
					mark[nameVal] = true;
				}
			}
		}
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		btjoin = 0;
		btexit = 0;
		if (297 <= e.getX() && e.getX() <= 439 && 520 <= e.getY()
				&& e.getY() <= 560) {
			// thuc hien
			boolean check = false;
			int numbroad = 0;
			for (int i = 0; i < 6; i++) {
				if (mark[i] == true) {
					check = true;
					numbroad = i;
					break;
				}
			}
			if (check) {
				cl.Send2Server("Client_jointRoom;" + numbroad + ";");
			}
		} else if (544 <= e.getX() && e.getX() <= 686 && 520 <= e.getY()
				&& e.getY() <= 560) {
			cl.Send2Server("Client_exit;");
		}
		repaint();
	}

	public Room Find(String NameRoom) {
		int i = 0;
		for (i = 0; i < this.room.size(); i++) {
			Room r = this.room.get(i);
			if (r.NameRoom.compareTo(NameRoom) == 0)
				break;
		}
		if (i < this.room.size())
			return this.room.get(i);
		return null;
	}
}

class Room {
	public String NameRoom, NameMap;
	public int num_player, max_player, min_lever;
	public boolean playing;

	public Room(String NameRoom, int num_player, int max_player, int min_level,
			String NameMap, boolean playing) {
		this.NameRoom = NameRoom;
		this.num_player = num_player;
		this.max_player = max_player;
		this.min_lever = min_level;
		this.NameMap = NameMap;
		this.playing = playing;
	}
}

class ShowMessage extends Thread {
	WaitRoomPanel wrp;
	int i = 0;

	public ShowMessage(WaitRoomPanel wrp) {
		this.wrp = wrp;
	}

	public void run() {
		while (true) {
			if (0 <= i && i <= 30)
				this.wrp.toadotbX = 1000;
			else if (31 <= i && i < 431)
				this.wrp.toadotbX -= 4;
			else
				resetshow();
			this.wrp.repaint();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
	}

	public void resetshow() {
		i = 0;
	}
}
