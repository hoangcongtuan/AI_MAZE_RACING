package RaceAI.RaceClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.sound.sampled.Clip;

public class Client extends Thread {
	Socket socket;
	DataInputStream _dis;
	DataOutputStream _dos;
	String Name, Pass, Group, AIname;
	String Msg = "";
	Boolean Stop = false;
	public MainFrame main;
	public ClientWaitRoom waitroom;
	public ReadyRoom readyroom;
	public AIRaceClient raceclient;

	public Client(String IP, int Port, String Name, String Pass, String Group,
			String AIname, MainFrame main) {
		try {
			this.main = main;
			this.Name = Name;
			this.Pass = Pass;
			this.Group = Group;
			this.AIname = AIname;
			this.socket = new Socket(IP, Port);
			this._dis = new DataInputStream(this.socket.getInputStream());
			this._dos = new DataOutputStream(this.socket.getOutputStream());
			this.start();
		} catch (Exception e) {
			this.Msg = "Connection Error";
		}
	}
	
	public synchronized boolean Send2Server(String str){
		try {
			this._dos.writeUTF(str);
			return true;
		} catch (IOException e) {
			System.out.println("Error! Can not send command to Server");
			return false;
		}
	}

	public void run() {
		try {
			this.Send2Server("Client_login;" + Name + ";" + Pass + ";" + Group
					+ ";" + AIname + ";");
			while (!Stop) {
				String re = this._dis.readUTF();
				String[] temp = re.split(";");

				if (temp[0].compareTo("Server_login") == 0) {
					if (temp[1].compareTo("LoginNO") == 0) {
						this.main.resetField();
						main.lbThongBao
								.setText("<html><font color='red'>The account or password is incorrect!!!</font></html>");
					} else if (temp[1].compareTo("Loginning") == 0) {
						this.main.resetField();
						main.lbThongBao
								.setText("<html><font color='red'>The account is logged in to another computer!!!</font></html>");
					} else if (temp[1].compareTo("LoginOK") == 0) {
						Thread.sleep(1500);
						main.IFlogin.stopLoading();
						main.frame.setVisible(false);
						waitroom = new ClientWaitRoom(socket, this);
					}
					Thread.sleep(100);
				} else if (temp[0].compareTo("Exit") == 0) {
					this.Msg = "Exit";
					this.Stop = true;
					System.exit(0);
				} else if (temp[0].compareTo("Server_OKjointRoom") == 0) {
					int numbroad = Integer.parseInt(temp[1]);
					this.readyroom = new ReadyRoom(this, socket, numbroad);
					this.waitroom.setVisible(false);
				} else if (temp[0].compareTo("Server_infoRoom") == 0) {
					int size = Integer.parseInt(temp[1]);
					for (int i = 0; i < size; i++) {
						Room tmproom = this.waitroom.roompanel
								.Find(temp[i * 6 + 2]);
						if (tmproom != null) {
							tmproom.num_player = Integer
									.parseInt(temp[i * 6 + 3]);
							tmproom.playing = Boolean.parseBoolean(temp[i * 6 + 7]);
						} else {
							Room newroom = new Room(temp[i * 6 + 2],
									Integer.parseInt(temp[i * 6 + 3]),
									Integer.parseInt(temp[i * 6 + 4]),
									Integer.parseInt(temp[i * 6 + 5]),
									temp[i * 6 + 6],
									Boolean.parseBoolean(temp[i * 6 + 7]));
							this.waitroom.roompanel.room.add(newroom);
						}
					}
					this.waitroom.roompanel.repaint();
				} else if (temp[0].compareTo("Server_infouser") == 0) {
					this.waitroom.roompanel.nameUser = temp[1];
					this.waitroom.roompanel.levelUser = temp[2];
					this.waitroom.roompanel.repaint();
				} else if (temp[0].compareTo("Server_infoPlayer") == 0) {
					int tam = Integer.parseInt(temp[1]);
					this.readyroom.readypanel.numbplayer = tam;
					this.readyroom.readypanel.Player_Name = new String[tam];
					this.readyroom.readypanel.Player_Level = new String[tam];
					this.readyroom.readypanel.Player_Group = new String[tam];
					this.readyroom.readypanel.AreYouReady = new boolean[tam];
					for (int i = 0; i < tam; i++) {
						this.readyroom.readypanel.Player_Name[i] = temp[i * 4 + 2];
						this.readyroom.readypanel.Player_Level[i] = temp[i * 4 + 3];
						this.readyroom.readypanel.Player_Group[i] = temp[i * 4 + 4];
						this.readyroom.readypanel.AreYouReady[i] = Boolean
								.parseBoolean(temp[i * 4 + 5]);
						this.readyroom.readypanel.repaint();
					}
				} else if (temp[0].compareTo("Server_StartGame") == 0) {
					raceclient = new AIRaceClient(this,Boolean.parseBoolean(temp[1]));
					readyroom.setVisible(false);
				} else if (temp[0].compareTo("Server_InitGame") == 0) {
					this.raceclient.initgame(temp[1]);
				} else if (temp[0].compareTo("Server_Control") == 0) {
					this.raceclient.RwC.UpdateCarPos(temp[1]);
					this.raceclient.repaint();
				} else if (temp[0].compareTo("Server_removeRoom") == 0) {
					this.waitroom.roompanel.room.remove(this.waitroom.roompanel
							.Find(temp[1]));
					this.waitroom.roompanel.repaint();
				}// Server_paintGI
				else if (temp[0].compareTo("Server_paintGI") == 0) {
					if (temp[1].compareTo("finish") == 0) {
						this.raceclient.count = 0;
						//if (this.waitroom.roompanel.SoundState)
						//	SoundManager.MGOING.play(Clip.LOOP_CONTINUOUSLY);
						this.raceclient.repaint();
					} else {
						this.raceclient.count = Integer.parseInt(temp[1]);
						switch(this.raceclient.count){
						case 4:
							if (this.waitroom.roompanel.SoundState)
								SoundManager.MTHREE.play(0);
							break;
						case 3:
							if (this.waitroom.roompanel.SoundState)
								SoundManager.MTWO.play(0);
							break;
						case 2:
							if (this.waitroom.roompanel.SoundState)
								SoundManager.MONE.play(0);
							break;
						case 1:
							if (this.waitroom.roompanel.SoundState)
								SoundManager.MSTART.play(0);
						}
						this.raceclient.repaint();
					}
				} else if (temp[0].compareTo("Server_SendMessage") == 0) {
					this.waitroom.roompanel.contentmes = temp[1];
					System.out.println("nhan duoc");
				} else if (temp[0].compareTo("Server_winerlist") == 0) {
					if (this.waitroom.roompanel.SoundState)
					{
						SoundManager.MGOING.stop();
						SoundManager.MSTOP.play(0);
					}
					new HieuUngFinish(raceclient).start();
					raceclient.value = Integer.parseInt(temp[1]);
					raceclient.Name = new String[raceclient.value];
					raceclient.Group = new String[raceclient.value];
					raceclient.Level = new String[raceclient.value];
					raceclient.Time = new String[raceclient.value];
					for (int i = 0; i < raceclient.value; i++) {
						raceclient.Name[i] = temp[i * 4 + 2];
						raceclient.Group[i] = temp[i * 4 + 3];
						raceclient.Level[i] = temp[i * 4 + 4];
						raceclient.Time[i] = temp[i * 4 + 5];
					}
					raceclient.indexlevelup = Integer
							.parseInt(temp[raceclient.value * 4 + 2]);
					raceclient.repaint();
				}
			}
		} catch (Exception e) {
			this.Msg = "Connection Error";
		}
	}
}
