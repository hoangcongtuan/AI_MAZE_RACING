package RaceAI.RaceClient;

import java.awt.*;
import java.awt.geom.Point2D;

import _LoadResource.LoadResource;

public class Race {
	String[] map = null;
	int size, b_no;
	Point2D.Double offset = new Point2D.Double(0, 0);

	// double offset_x=0, offset_y=0;
	public Race(int size, int b_no) {
		this.size = size;
		this.b_no = b_no;
	}

	public Race(String[] map, int size, int b_no) {
		this.map = map;
		this.size = size;
		this.b_no = b_no;
	}
	
	public int BlockSize(){
		return this.size*this.b_no;
	}
	public char BlockKind(int x, int y){
		if (y<0 || y>=map.length) return 'e';
		if (x<0 || x>=map[y].length()) return 'e';
		return map[y].charAt(x);
	}
	
	public int BlockColumn(){
		if (map.length>0)
			return map[0].length();
		return 0;
	}
	
	public int BlockRow(){
		return map.length;
	}

	void SetOffset(Point2D.Double offset) {
		this.offset = offset;
	}

	private void paintBlock(Graphics2D figure, int x, int y, Color c1, Color c2) {
		for (int i = 0; i < this.b_no; i++) {
			for (int j = 0; j < this.b_no; j++) {
				figure.setColor(c1);
				figure.fillRect(x + j * this.size, y + i * this.size,
						this.size, this.size);
				figure.setColor(c2);
				figure.drawRect(x + j * this.size + 1, y + i * this.size + 1,
						this.size - 2, this.size - 2);
			}
		}
	}

	private void paintRoad(Graphics2D figure, int x, int y, int i, int j) {
		int tmp = 0;
		if (this.map[i].charAt(j - 1) == '0'
				|| this.map[i].charAt(j - 1) == '2'
				|| this.map[i].charAt(j - 1) == '3'
				|| this.map[i].charAt(j - 1) == 'G'
				|| this.map[i].charAt(j - 1) == 'S')
			tmp = tmp | 8;// trai
		if (this.map[i].charAt(j + 1) == '0'
				|| this.map[i].charAt(j + 1) == '2'
				|| this.map[i].charAt(j + 1) == '3'
				|| this.map[i].charAt(j + 1) == 'G'
				|| this.map[i].charAt(j + 1) == 'S')
			tmp = tmp | 4;// phai
		if (this.map[i - 1].charAt(j) == '0'
				|| this.map[i - 1].charAt(j) == '2'
				|| this.map[i - 1].charAt(j) == '3'
				|| this.map[i - 1].charAt(j) == 'G'
				|| this.map[i - 1].charAt(j) == 'S')
			tmp = tmp | 2;// tren
		if (this.map[i + 1].charAt(j) == '0'
				|| this.map[i + 1].charAt(j) == '2'
				|| this.map[i + 1].charAt(j) == '3'
				|| this.map[i + 1].charAt(j) == 'G'
				|| this.map[i + 1].charAt(j) == 'S')
			tmp = tmp | 1;// duoi
		switch (tmp) {
		case 2:
			figure.drawImage(LoadResource.ImgRoadDoc, x, y, this.b_no
					* this.size, this.b_no * this.size, null);
			break;
		case 4:
			figure.drawImage(LoadResource.ImgRoadNgang, x, y, this.b_no
					* this.size, this.b_no * this.size, null);
			break;
		case 1:
			figure.drawImage(LoadResource.ImgRoadDoc, x, y, this.b_no
					* this.size, this.b_no * this.size, null);
			break;
		case 8:
			figure.drawImage(LoadResource.ImgRoadNgang, x, y, this.b_no
					* this.size, this.b_no * this.size, null);
			break;
		case 3:
			figure.drawImage(LoadResource.ImgRoadDoc, x, y, this.b_no
					* this.size, this.b_no * this.size, null);
			break;
		case 12:
			figure.drawImage(LoadResource.ImgRoadNgang, x, y, this.b_no
					* this.size, this.b_no * this.size, null);
			break;

		case 6:
			figure.drawImage(LoadResource.ImgGocDuoiTrai, x, y, this.b_no
					* this.size, this.b_no * this.size, null);
			break;
		case 5:
			figure.drawImage(LoadResource.ImgGocTrenTrai, x, y, this.b_no
					* this.size, this.b_no * this.size, null);
			break;
		case 9:
			figure.drawImage(LoadResource.ImgGocTrenPhai, x, y, this.b_no
					* this.size, this.b_no * this.size, null);
			break;
		case 10:
			figure.drawImage(LoadResource.ImgGocDuoiPhai, x, y, this.b_no
					* this.size, this.b_no * this.size, null);
			break;

		case 14:
			figure.drawImage(LoadResource.ImgNga3Tren, x, y, this.b_no
					* this.size, this.b_no * this.size, null);
			break;
		case 7:
			figure.drawImage(LoadResource.ImgNga3Phai, x, y, this.b_no
					* this.size, this.b_no * this.size, null);
			break;
		case 11:
			figure.drawImage(LoadResource.ImgNga3Trai, x, y, this.b_no
					* this.size, this.b_no * this.size, null);
			break;
		case 13:
			figure.drawImage(LoadResource.ImgNga3Duoi, x, y, this.b_no
					* this.size, this.b_no * this.size, null);
			break;

		case 15:
			figure.drawImage(LoadResource.ImgNga4, x, y, this.b_no * this.size,
					this.b_no * this.size, null);
			break;
		default:
			paintBlock(figure, x, y, Color.GRAY, Color.GRAY);
		}
		if (this.map[i].charAt(j)=='2')
			figure.drawImage(LoadResource.Efslow, x, y, this.b_no * this.size,
					this.b_no * this.size, null);
		else
			if (this.map[i].charAt(j)=='3') 
				figure.drawImage(LoadResource.Efslide, x, y, this.b_no * this.size,
						this.b_no * this.size, null);

	}

	private boolean inside(double x, double y, double wd_h, double wd_w){
		if (x>=0 && x<wd_w && y>=0 && y<wd_h) return true;
		return false;
	}
	
	void paint(Graphics2D figure,int wd_h, int wd_w) {
		double blS=this.BlockSize();
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length(); j++) {
				char cmap = map[i].charAt(j);
				int x = (int) (j * blS + offset.x);
				int y = (int) (i * blS + offset.y);
				
				if (!inside(x,y,wd_h,wd_w) && !inside(x+blS,y,wd_h,wd_w) && 
						!inside(x,y+blS,wd_h,wd_w) && !inside(x+blS,y+blS,wd_h,wd_w)) continue;
				
				// if (cmap=='0') paintBlock(figure,x,y,Color.GRAY,Color.GRAY);
				if (cmap == '0' || cmap == '2' || cmap == '3')
					paintRoad(figure, x, y, i, j);
				else if (cmap == '1')
					paintBlock(figure, x, y, Color.DARK_GRAY, Color.LIGHT_GRAY);
				else if (cmap == 'S')
					figure.drawImage(LoadResource.ImgStartRace, x, y, this.b_no
							* this.size, this.b_no * this.size, null);
					//paintBlock(figure, x, y, Color.BLUE, Color.BLUE);
				else if (cmap == 'G')
					figure.drawImage(LoadResource.ImgFinishRace, x, y, this.b_no
							* this.size, this.b_no * this.size, null);
					//paintBlock(figure, x, y, Color.RED, Color.RED);
				else
					paintBlock(figure, x, y, Color.BLACK, Color.BLACK);
			}
		}
	}
}
