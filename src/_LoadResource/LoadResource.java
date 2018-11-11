package _LoadResource;

import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;

import javax.swing.ImageIcon;

public class LoadResource {
	public static Image ImgBGLogin, ImgLoginFrame, ImgBGWaitRoom, ImgRoadNum,
			ImgMarkRoad, ImgVaoBan, ImgVaoBanPress, ImgVaoBanNone,
			ImgBGReadyRoom, ImgUnderTab, ImgReady, ImgReadyPressed,
			ImgReadyPicture, ImgRoadNgang, ImgRoadDoc, ImgNga4, ImgNga3Tren,
			ImgNga3Duoi, ImgNga3Trai, ImgNga3Phai, ImgGocTrenPhai,
			ImgGocTrenTrai, ImgGocDuoiPhai, ImgGocDuoiTrai, ImgNum3, ImgNum2,
			ImgNum1, ImgNum0, btnExit, btnExitNone, btnJoin, btnJoinNone,
			btnReady, btnReadyNone, btnCancel, btnCancelNone, btnBack,
			btnBackNone, Efslow, Efslide, ImgGhep1, ImgGhep2, ImgLock,
			ImgStartRace, ImgFinishRace, ImgNoteFinish, ImgHieuUngFinish,
			Imglevelup, ImgOK, ImgOKpress,Imgplaying,ImgsoundON,ImgsoundOFF;
	public static Image ImgLoading[];
	public static Point[] PosRoadNum;
	public static Point[] PosTableInfo;
	public static Point[] PosFinish;

	public LoadResource() {
		LoadImage();
		LoadPos();
	}

	public static void LoadPos() {
		PosRoadNum = new Point[6];
		int x, y;
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 3; j++) {
				x = 80 + j * 247;
				y = 190 + i * 180;
				PosRoadNum[i * 3 + j] = new Point(x, y);
			}
		PosTableInfo = new Point[10];
		x = 10;
		y = 120;
		for (int i = 0; i < 2; i++) {
			x = x + 495 * i;
			y = 120;
			for (int j = 0; j < 5; j++) {
				PosTableInfo[i * 5 + j] = new Point(x, y);
				y += 76;
			}
		}

		PosFinish = new Point[10];
		x = 275;
		y = 215;
		for (int i = 0; i < 10; i++) {
			PosFinish[i] = new Point(x, y);
			y += 25;
		}
	}

	public static void LoadImage() {
		ImgBGLogin = new ImageIcon("src/_IMAGE/BGLogin.png").getImage();
		ImgLoginFrame = new ImageIcon("src/_IMAGE/loginFrame.png").getImage();
		ImgBGWaitRoom = new ImageIcon("src/_IMAGE/BGWaitRoom.png").getImage();
		ImgRoadNum = new ImageIcon("src/_IMAGE/Road_Num.png").getImage();
		ImgMarkRoad = new ImageIcon("src/_IMAGE/markRoad.png").getImage();
		ImgVaoBan = new ImageIcon("src/_IMAGE/btn_vaoban.png").getImage();
		ImgVaoBanPress = new ImageIcon("src/_IMAGE/btn_vaobanpress.png")
				.getImage();
		ImgVaoBanNone = new ImageIcon("src/_IMAGE/btn_vaoban_none.png")
				.getImage();
		ImgBGReadyRoom = new ImageIcon("src/_IMAGE/ImgGBReadyRoom.png")
				.getImage();
		ImgUnderTab = new ImageIcon("src/_IMAGE/ImgUnderTab.png").getImage();
		ImgReady = new ImageIcon("src/_IMAGE/ImgReady.png").getImage();
		ImgReadyPressed = new ImageIcon("src/_IMAGE/ImgReadyPressed.png")
				.getImage();
		ImgReadyPicture = new ImageIcon("src/_IMAGE/ImgReadyPicture.png")
				.getImage();
		ImgRoadNgang = new ImageIcon("src/_IMAGE/road_ngang.png").getImage();
		ImgRoadDoc = new ImageIcon("src/_IMAGE/road_doc.png").getImage();
		ImgNga4 = new ImageIcon("src/_IMAGE/nga4.png").getImage();
		ImgNga3Duoi = new ImageIcon("src/_IMAGE/nga3_duoi.png").getImage();
		ImgNga3Tren = new ImageIcon("src/_IMAGE/nga3_tren.png").getImage();
		ImgNga3Phai = new ImageIcon("src/_IMAGE/nga3_phai.png").getImage();
		ImgNga3Trai = new ImageIcon("src/_IMAGE/nga3_trai.png").getImage();
		ImgGocTrenPhai = new ImageIcon("src/_IMAGE/goc_trenphai.png")
				.getImage();
		ImgGocTrenTrai = new ImageIcon("src/_IMAGE/goc_trentrai.png")
				.getImage();
		ImgGocDuoiPhai = new ImageIcon("src/_IMAGE/goc_duoiphai.png")
				.getImage();
		ImgGocDuoiTrai = new ImageIcon("src/_IMAGE/goc_duoitrai.png")
				.getImage();
		ImgNum3 = new ImageIcon("src/_IMAGE/num3.png").getImage();
		ImgNum2 = new ImageIcon("src/_IMAGE/num2.png").getImage();
		ImgNum1 = new ImageIcon("src/_IMAGE/num1.png").getImage();
		ImgNum0 = new ImageIcon("src/_IMAGE/num0.png").getImage();
		btnExit = new ImageIcon("src/_IMAGE/btn_exit.png").getImage();
		btnExitNone = new ImageIcon("src/_IMAGE/btn_exitNone.png").getImage();
		btnJoin = new ImageIcon("src/_IMAGE/btn_join.png").getImage();
		btnJoinNone = new ImageIcon("src/_IMAGE/btn_joinNone.png").getImage();
		btnReady = new ImageIcon("src/_IMAGE/btn_ready.png").getImage();
		btnReadyNone = new ImageIcon("src/_IMAGE/btn_readyNone.png")
				.getImage();
		btnCancel = new ImageIcon("src/_IMAGE/btn_cancel.png").getImage();
		btnCancelNone = new ImageIcon("src/_IMAGE/btn_cancelNone.png")
				.getImage();
		btnBack = new ImageIcon("src/_IMAGE/btn_back.png").getImage();
		btnBackNone = new ImageIcon("src/_IMAGE/btn_backNone.png").getImage();
		Efslow = new ImageIcon("src/_IMAGE/hieuungcham.png").getImage();
		Efslide = new ImageIcon("src/_IMAGE/hieuungbang.png").getImage();
		ImgGhep1 = new ImageIcon("src/_IMAGE/ImgGhep1.png").getImage();
		ImgGhep2 = new ImageIcon("src/_IMAGE/ImgGhep2.png").getImage();
		ImgLock = new ImageIcon("src/_IMAGE/locked.png").getImage();
		ImgStartRace = new ImageIcon("src/_IMAGE/ImgStartRace.png")
				.getImage();
		ImgFinishRace = new ImageIcon("src/_IMAGE/ImgFinishRace.png")
				.getImage();
		ImgNoteFinish = new ImageIcon("src/_IMAGE/ImgNoteFinish.png")
				.getImage();
		ImgHieuUngFinish = new ImageIcon("src/_IMAGE/ImgHieuUngFinish.png")
				.getImage();
		Imglevelup = new ImageIcon("src/_IMAGE/Imglevelup.png").getImage();
		ImgOK = new ImageIcon("src/_IMAGE/ImgOK.png").getImage();
		ImgOKpress = new ImageIcon("src/_IMAGE/ImgOKpress.png").getImage();
		Imgplaying = new ImageIcon("src/_IMAGE/Imgplaying.png").getImage();
		ImgsoundON = new ImageIcon("src/_IMAGE/soundON.png").getImage();
		ImgsoundOFF = new ImageIcon("src/_IMAGE/soundOFF.png").getImage();

		ImgLoading = new Image[12];
		for (int i = 0; i < 12; i++) {
			ImgLoading[i] = new ImageIcon("src/_IMAGE/loading/spinner_" + i
					+ ".png").getImage();
		}
	}
}
