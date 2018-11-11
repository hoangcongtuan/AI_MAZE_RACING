package RaceAI.RaceClient;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


public class Car {
	
	//Coordinate
	double x,y;
	//Direction
	double alpha;
	//Size
	double s_x,s_y;
	BufferedImage img = null,imgflur = null;
	public boolean flur;
	Point2D.Double offset=new Point2D.Double(0,0);
	
	public Car(double x, double y, double alpha, double s_x, double s_y){
		this.s_x=s_x;
		this.s_y=s_y;
		Update(alpha,x,y,false);
	}
	
	public double getx(){
		return x;
	}
	
	public double gety(){
		return y;
	}
	
	public double getalpha(){
		return alpha;
	}
	public double getsizex(){
		return s_x;
	}
	public double getsizey(){
		return s_y;
	}
	
	void Update(double alpha, double x, double y,boolean flur){
		this.alpha=alpha;
		this.x=x;
		this.y=y;
		this.flur = flur;
	}
	
	void SetImage(String fname){
		try {
		    this.img = ImageIO.read(new File(fname+".png"));
		    s_x=this.img.getWidth()*s_y/this.img.getHeight();
		    this.imgflur = ImageIO.read(new File(fname+"flur.png"));
		    s_x=this.imgflur.getWidth()*s_y/this.imgflur.getHeight();
		} catch (Exception e) {}
	}
	
	void SetOffset(Point2D.Double offset){
		this.offset = offset;
	}

	Point2D[] GetAllPoint(){
		AffineTransform at = new AffineTransform();
		at.setToRotation(Math.toRadians(alpha), x, y );
		
		return new Point2D[]{at.transform(new Point2D.Double(x-s_x/2,y-s_y/2), new Point2D.Double()),
				at.transform(new Point2D.Double(x-s_x/2,y+s_y/2), new Point2D.Double()),
				at.transform(new Point2D.Double(x+s_x/2,y-s_y/2), new Point2D.Double()),
				at.transform(new Point2D.Double(x+s_x/2,y+s_y/2), new Point2D.Double())};
	}
	
	void paint(Graphics2D figure){
		
		AffineTransform at = new AffineTransform();
		at.setToRotation(Math.toRadians(alpha), x+offset.x, y+offset.y );
		figure.setTransform(at);
		
		
		figure.setColor(Color.GREEN);
		if (img==null)
			figure.fillRect((int)(x+offset.x-s_x/2), (int)(y+offset.y-s_y/2), (int)s_x, (int)s_y);
		else 
			if (!flur)
			figure.drawImage(img, (int)(x+offset.x-s_x/2), (int)(y+offset.y-s_y/2), (int)s_x, (int)s_y, null);
			else 
			figure.drawImage(imgflur, (int)(x+offset.x-s_x/2), (int)(y+offset.y-s_y/2), (int)s_x, (int)s_y, null);
		
		figure.setTransform(new AffineTransform());
	}

}
