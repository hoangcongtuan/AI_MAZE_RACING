package RaceAI.RaceClient;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;




import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import RaceAI.AI.MainAI;
import RaceAI.AI.MainAI_2;
import _LoadResource.LoadResource;

@SuppressWarnings("serial")
public class AIRaceClient extends JFrame implements Runnable, KeyListener,
		MouseListener {

	private boolean[] keys = new boolean[526];
	private Image offScreen;
	private Graphics2D figure;
	int count = 0;

	RaceWithMultCar_Client RwC;
	int modedisplay;// winning - true or playing - false
	String[] Name, Group, Level, Time;
	int indexlevelup, value;
	private boolean btnOK, modeAI;
	public Client cl;

//	MainAI_2 ai;
	MainAI ai;

	public AIRaceClient(final Client cl, boolean modeAI) {
		this.cl = cl;
		this.modeAI = modeAI;
		this.setSize(1000, 600);
		this.setTitle("AI Race Contest!!!!");
		this.setVisible(true);
		this.setDefaultCloseOperation(3);
		this.offScreen = this.createImage(this.getWidth(), this.getHeight());
		this.figure = (Graphics2D) this.offScreen.getGraphics();
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (JOptionPane.showConfirmDialog(null,
						"Do you want to get back?",
						"Really backing?", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					cl.Send2Server("Client_BackRoom;");
					modedisplay=-1;
					dispose();
					cl.waitroom.setVisible(true);
				}
			}
		});

		modedisplay = 0;
		value = 0;
		indexlevelup = 0;
		btnOK = false;
		SoundManager.MNEN.stop();
	}

	public void initgame(String str) {

		RwC = new RaceWithMultCar_Client(str, this.getWidth(), this.getHeight());
		for (int i = 0; i < RwC.cars.size(); i++) {
			RwC.cars.get(i).SetImage("src/_IMAGE/carr" + (i % 5));
		}
		this.addKeyListener(this);
		addMouseListener(this);
		this.addComponentListener(new MyComponentListener(this));
		for (int i = 0; i < keys.length; i++)
			keys[i] = false;
		(new Thread(this)).start();
		if (modeAI)
//			ai = new MainAI_2(RwC.race, RwC.cars, RwC.Ccar);
			ai = new MainAI(RwC.race, RwC.cars, RwC.Ccar);
	}

	class MyComponentListener implements ComponentListener {
		AIRaceClient f;

		public MyComponentListener(AIRaceClient f) {
			this.f = f;
		}

		public void componentResized(ComponentEvent e) {
			f.offScreen = f.createImage(f.getWidth(), f.getHeight());
			f.figure = (Graphics2D) f.offScreen.getGraphics();
			f.RwC.setWindowSize(f.getHeight(), f.getWidth());
			f.repaint();
		}

		public void componentHidden(ComponentEvent arg0) {
		}

		public void componentMoved(ComponentEvent arg0) {
		}

		public void componentShown(ComponentEvent arg0) {
		}
	}

	public void paint(Graphics g) {

		// this.figure.setColor(getBackground());

		if (RwC != null && modedisplay == 0) {
			RwC.paint(figure);

			if (this.count != 0) {
				switch (this.count) {
				case 4:
					figure.drawImage(LoadResource.ImgNum3,
							(1000 - LoadResource.ImgNum3.getWidth(null)) / 2,
							(600 - LoadResource.ImgNum3.getHeight(null)) / 2,
							LoadResource.ImgNum3.getWidth(null),
							LoadResource.ImgNum3.getHeight(null), null);
					break;
				case 3:
					figure.drawImage(LoadResource.ImgNum2,
							(1000 - LoadResource.ImgNum2.getWidth(null)) / 2,
							(600 - LoadResource.ImgNum2.getHeight(null)) / 2,
							LoadResource.ImgNum2.getWidth(null),
							LoadResource.ImgNum2.getHeight(null), null);
					break;
				case 2:
					figure.drawImage(LoadResource.ImgNum1,
							(1000 - LoadResource.ImgNum1.getWidth(null)) / 2,
							(600 - LoadResource.ImgNum1.getHeight(null)) / 2,
							LoadResource.ImgNum1.getWidth(null),
							LoadResource.ImgNum1.getHeight(null), null);
					break;
				case 1:
					figure.drawImage(LoadResource.ImgNum0,
							(1000 - LoadResource.ImgNum0.getWidth(null)) / 2,
							(600 - LoadResource.ImgNum0.getHeight(null)) / 2,
							LoadResource.ImgNum0.getWidth(null),
							LoadResource.ImgNum0.getHeight(null), null);
					break;
				}
			}

		} else if (RwC != null && modedisplay == 1) {
			RwC.paint(figure);
			figure.drawImage(LoadResource.ImgHieuUngFinish, 0, 0,
					LoadResource.ImgHieuUngFinish.getWidth(null),
					LoadResource.ImgHieuUngFinish.getHeight(null), null);
		} else if (RwC != null && modedisplay == 2) {
			figure.drawImage(LoadResource.ImgNoteFinish, 0, 0,
					LoadResource.ImgNoteFinish.getWidth(null),
					LoadResource.ImgNoteFinish.getHeight(null), null);
			figure.setColor(Color.WHITE);
			figure.setFont(new Font("Arial", Font.BOLD, 12));
			for (int i = 0; i < this.value; i++) {
				if (i == this.value - 1) {
					figure.setColor(Color.RED);
					figure.setFont(new Font("Arial", Font.BOLD, 13));
				}
				figure.drawString((i + 1) + "", LoadResource.PosFinish[i].x-5,
						LoadResource.PosFinish[i].y);
				figure.drawString(Name[i], LoadResource.PosFinish[i].x + 70,
						LoadResource.PosFinish[i].y);
				figure.drawString(Group[i], LoadResource.PosFinish[i].x + 150,
						LoadResource.PosFinish[i].y);
				figure.drawString(Level[i], LoadResource.PosFinish[i].x + 237,
						LoadResource.PosFinish[i].y);
				figure.drawString(Time[i], LoadResource.PosFinish[i].x + 305,
						LoadResource.PosFinish[i].y);
				if (i < this.indexlevelup)
					figure.drawImage(LoadResource.Imglevelup,
							LoadResource.PosFinish[i].x + 430,
							LoadResource.PosFinish[i].y - 15,
							LoadResource.Imglevelup.getWidth(null),
							LoadResource.Imglevelup.getHeight(null), null);
			}
			if (!btnOK)
				figure.drawImage(LoadResource.ImgOK, 429, 530,
						LoadResource.ImgOK.getWidth(null),
						LoadResource.ImgOK.getHeight(null), null);
			else
				figure.drawImage(LoadResource.ImgOKpress, 429, 530,
						LoadResource.ImgOKpress.getWidth(null),
						LoadResource.ImgOKpress.getHeight(null), null);
		}

		g.drawImage(offScreen, 0, 0, this);
	}

	public void update(Graphics g) {
		this.paint(g);
	}

	@Override
	public void run() {
		while (modedisplay == 0) {
			String cmd = "Client_sendkey;";

			if (modeAI && ai!=null){
				ai.AI();
				cmd += ai.key;
				this.cl.Send2Server(cmd + ";");
			}
			else
				{
					if (this.keys[KeyEvent.VK_UP])
						cmd += "1";
					else
						cmd += "0";
					if (this.keys[KeyEvent.VK_DOWN])
						cmd += "1";
					else
						cmd += "0";
					if (this.keys[KeyEvent.VK_LEFT])
						cmd += "1";
					else
						cmd += "0";
					if (this.keys[KeyEvent.VK_RIGHT])
						cmd += "1";
					else
						cmd += "0";
					this.cl.Send2Server(cmd + ";");
				}
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		this.keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		this.keys[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
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
		if (modedisplay == 2 && 429 <= e.getX() && e.getX() <= 571
				&& 530 <= e.getY() && e.getY() <= 570) {
			btnOK = true;
			this.repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (modedisplay == 2) {
			btnOK = false;
			this.repaint();
			if (429 <= e.getX() && e.getX() <= 571 && 530 <= e.getY()
					&& e.getY() <= 570) {
				modedisplay = -1;
				this.dispose();
				this.cl.waitroom.setVisible(true);
				if (cl.waitroom.roompanel.SoundState)
					SoundManager.MNEN.play(Clip.LOOP_CONTINUOUSLY);
			}
		}
	}
}

class HieuUngFinish extends Thread {
	AIRaceClient ai;

	public HieuUngFinish(AIRaceClient ai) {
		this.ai = ai;
		ai.modedisplay = 1;
	}

	public void run() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ai.modedisplay = 2;
		ai.repaint();
	}
}
