package RaceAI.RaceClient;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import _LoadResource.LoadResource;

public class MainFrame extends JFrame implements ActionListener, KeyListener {
	public JLabel lbID, lbPass, lbIP, lbPort, lbGroup, lbAIName, lbThongBao;
	public JTextField tfID, tfIP, tfPort, tfGroup, tfAIName;
	public JPasswordField tfPass;
	public JButton btLogin;
	public JFrame frame;
	public panelLogin IFlogin;
	public Client client;

	public MainFrame() {
		new LoadResource();
		frame = new JFrame();
		frame.setLayout(null);
		frame.setTitle("Car Racing");
		IFlogin = new panelLogin(this);
		IFlogin.setBounds(0, 0, 500, 500);
		IFlogin.setLayout(null);
		// ------
		lbID = new JLabel("<html><font color='white'>User:</font></html>");
		lbID.setBounds(70, 260, 60, 30);

		tfID = new JTextField("bs1");
		tfID.setEditable(true);
		tfID.addKeyListener(this);
		tfID.setBounds(135, 260, 120, 30);
		// ------
		lbPass = new JLabel("<html><font color='white'>Password:</font></html>");
		lbPass.setBounds(70, 300, 60, 30);

		tfPass = new JPasswordField("123");
		tfPass.setEditable(true);
		tfPass.addKeyListener(this);
		tfPass.setBounds(135, 300, 120, 30);
		// ---------
		lbIP = new JLabel("<html><font color='white'>IP:</font></html>");
		lbIP.setBounds(260, 260, 40, 30);

		tfIP = new JTextField("localhost");
		tfIP.setEditable(true);
		tfIP.addKeyListener(this);
		tfIP.setBounds(300, 260, 120, 30);
		// ------
		lbPort = new JLabel("<html><font color='white'>Port:</font></html>");
		lbPort.setBounds(260, 300, 40, 30);

		tfPort = new JTextField("2500");
		tfPort.setEditable(true);
		tfPort.addKeyListener(this);
		tfPort.setBounds(300, 300, 120, 30);

		// ------
		lbGroup = new JLabel(
				"<html><font color='white'>Name Group:</font></html>");
		lbGroup.setBounds(70, 340, 70, 30);

		tfGroup = new JTextField("VN");
		tfGroup.setEditable(true);
		tfGroup.addKeyListener(this);
		tfGroup.setBounds(135, 340, 120, 30);
		// ------
		lbAIName = new JLabel(
				"<html><font color='white'>Name AI:</font></html>");
		lbAIName.setBounds(260, 340, 40, 30);

		tfAIName = new JTextField("car");
		tfAIName.setEditable(true);
		tfAIName.addKeyListener(this);
		tfAIName.setBounds(300, 340, 120, 30);

		btLogin = new JButton("Login");
		btLogin.addActionListener(this);
		btLogin.setBounds(180, 375, 140, 30);

		lbThongBao = new JLabel("");
		lbThongBao.setBounds(100, 403, 400, 20);

		IFlogin.add(lbID);
		IFlogin.add(tfID);
		IFlogin.add(lbPass);
		IFlogin.add(tfPass);
		IFlogin.add(lbIP);
		IFlogin.add(tfIP);
		IFlogin.add(lbPort);
		IFlogin.add(tfPort);
		IFlogin.add(lbGroup);
		IFlogin.add(tfGroup);
		IFlogin.add(lbAIName);
		IFlogin.add(tfAIName);
		IFlogin.add(btLogin);
		IFlogin.add(lbThongBao);

		frame.add(IFlogin);
		frame.setResizable(false);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.setLocationRelativeTo(null);
		frame.setSize(500, 500);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		frame.setLocation(((int) toolkit.getScreenSize().getWidth() - 500) / 2,
				((int) toolkit.getScreenSize().getHeight() - 500) / 2);
		frame.setVisible(true);
	}
	
	public void resetField() {
		this.btLogin.setEnabled(true);
		this.tfAIName.setEditable(true);
		this.tfGroup.setEditable(true);
		this.tfID.setEditable(true);
		this.tfPass.setEditable(true);
		this.tfIP.setEditable(true);
		this.tfPort.setEditable(true);
		this.IFlogin.stopLoading();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btLogin) {
			btLogin.setEnabled(false);
			tfAIName.setEditable(false);
			tfGroup.setEditable(false);
			tfID.setEditable(false);
			tfPass.setEditable(false);
			tfIP.setEditable(false);
			tfPort.setEditable(false);
			lbThongBao.setText("");
			this.IFlogin.startLoading();
			if (client != null) {
				client.Stop = true;
			}
			client = new Client(tfIP.getText(), Integer.parseInt(tfPort
					.getText()), tfID.getText(), tfPass.getText(),
					tfGroup.getText(), tfAIName.getText(), this);

		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		// if (e.getKeyCode() == e.VK_ENTER) {
		// game.sendMessage(jtfchat.getText());
		// jtfchat.setText("");
		// }
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		new MainFrame();
	}
}

class panelLogin extends JPanel {
	public int count;
	public boolean running;
	public MainFrame main;

	public panelLogin(MainFrame main) {
		this.main = main;
		this.running = false;
		this.count = 0;
	}

	public void startLoading() {
		this.running = true;
		new Loading(this).start();
	}

	public void stopLoading() {
		this.running = false;
		this.repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(LoadResource.ImgBGLogin, 0, 0,
				LoadResource.ImgBGLogin.getWidth(null),
				LoadResource.ImgBGLogin.getHeight(null), this);
		g.drawImage(LoadResource.ImgLoginFrame, 52, 165,
				LoadResource.ImgLoginFrame.getWidth(null),
				LoadResource.ImgLoginFrame.getHeight(null), this);
		if (this.running)
			g.drawImage(LoadResource.ImgLoading[this.count % 11], 234, 200,
					LoadResource.ImgLoading[this.count % 11].getWidth(null),
					LoadResource.ImgLoading[this.count % 11].getHeight(null),
					this);
	}
}

class Loading extends Thread {
	public panelLogin IFlogin;

	public Loading(panelLogin IFlogin) {
		this.IFlogin = IFlogin;
		this.IFlogin.count = 0;
	}

	public void run() {
		while (IFlogin.running) {
			IFlogin.count++;
			IFlogin.repaint();
			try {
				this.sleep(120);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (this.IFlogin.count >= 20) {
				this.IFlogin.main.resetField();
				this.IFlogin.main.lbThongBao
						.setText("<html><font color='red'>Can not connect to Server!!!</font></html>");
			}
		}
	}
}
