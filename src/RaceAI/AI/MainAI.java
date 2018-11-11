package RaceAI.AI;

import java.awt.Point;
import java.util.Random;
import java.util.Vector;

import RaceAI.RaceClient.Car;
import RaceAI.RaceClient.Race;

public class MainAI {
    Race race;
    Vector<Car> All_cars;
    Car Mycar;

    public String key = "0000"; // Go-Back-Left-Right (Up - Down - Left - Right)

    public MainAI(Race race, Vector<Car> cars, Car Mycar){
        this.race = race;
        this.Mycar = Mycar;
        this.All_cars = cars;

        map = new int[this.race.BlockColumn()+1][this.race.BlockRow() +1];
        mapdich = new int[All_cars.size()][this.race.BlockColumn()+1][this.race.BlockRow() +1];

        for(int i=1;i<=this.race.BlockColumn();i++)
            for(int j=1;j<=this.race.BlockRow();j++)
            {
                map[i][j]=0;
                for(int k=0;k<All_cars.size();k++)
                    mapdich[k][i][j]=0;
            }



        this.xeta= (this.race.BlockSize() - this.Mycar.getsizex() - this.Mycar.getsizey()*1.5)/2;
        if(this.xeta - this.Mycar.getsizex()/2 - this.Mycar.getsizey()/2 > 0)
            this.xedich=this.xeta - this.Mycar.getsizex()/2 - this.Mycar.getsizey()/2;
        else
            this.xedich=0;
        System.out.println(this.Mycar.getsizex() + "__"  +this.Mycar.getsizey()+"__" + this.xeta + "__" + this.xedich + "___"+ this.race.BlockSize());

    }

    Point next;

    int[][] map;
    /* ban do dua
     * gia tri ban dau la 0 (xe chua di qua)
     * xe di qua mot lan, gia tri la -1
     * xe di qua lan 2, gia tri la -2, xe se khong di qua day nua
     * cac diem nga 3,4 co gia tri la 3 4
     */
    int [][][] mapdich;
    /*
     * ban do cua dich
     * [] dau tien luu so thu tu cua dich trong Vecto
     * neu dich di qua thi luu la 1
     * neu chua di qua thi luu la 2
     */

    //down right left up
    int[] ix = {0, 1, -1, 0};
    int[] iy = {1, 0, 0, -1};

    //phai xuong trai len (mang dieu chinh tranh xe)
    int[] ixx = {1, 0, -1, 0, 0};
    int[] iyy = {0, 1, 0, -1, 0};

    //last position
    double lx=0,ly=0;

    // last speed
    double speed = 0;
    final double speedmax = 0.0125;//0.0105 ket hop voi 5

    //dieu khien xe chay
    //neu tim%re == 1 thi xe vua thang vua re, khong thi xe chi re (dua vao gia toc truoc do)
    int tim=0, tim2=0;
    final int re = 3;
    final int a=63; //sau a lan goi thi xe bat dau vua thang vua re

    //dieu khien tranh xe dich
    double xedich;			 //khoang cach tu tam ra 2 ben
    double xeta;
    final double khoangcach=3;	//khoang cach cua xe ta va dich khi bat dau tranh
    int dem=1; 				//xu ly dung xe
    final int sodem=1000;	//dem%sodem==0
    double xdung=0, ydung=0;
    boolean vedich=false;

    //cac bien dieu khien xe dung
    int quay=0;
    int r=0;		//xe lui trai, lui phai, lui
    boolean queotrai,lui;
    public int dung=0;
    double xt=0,yt=0;



    //ham tim diem tiep theo
    public Point tiep(int x, int y)
    {
        Point sau=new Point();
        int nga=0;
        boolean tiep = false; 	//co loi di tiep khong
        boolean cok = false;	//co duong chua di qua hay khong
        for(int i=0;i<=3;i++)
        {
            if(this.race.BlockKind(x+ix[i], y+iy[i]) !='1')
                nga++;
            if((map[x+ix[i]][y+iy[i]]>=0)&&(this.race.BlockKind(x+ix[i], y+iy[i]) !='1'))	tiep = true;
            if((map[x+ix[i]][y+iy[i]]==0)&&(this.race.BlockKind(x+ix[i], y+iy[i]) !='1'))	cok=true;
        }

        if(nga>2)	map[x][y]=nga;
        for(int i=0;i<=3;i++)
        {
            if (tiep && cok)
            {
                if( (this.race.BlockKind(x+ix[i], y+iy[i])!='1') && (map[x+ix[i]][y+iy[i]]==0))
                {
                    sau = new Point(x+ix[i], y+iy[i]);
                    map[x][y]=-1;
                    break;
                }
            }
            else if(tiep)
            {
                if( (this.race.BlockKind(x+ix[i], y+iy[i])!='1') && (map[x+ix[i]][y+iy[i]]>0))
                {
                    sau = new Point(x+ix[i], y+iy[i]);
                    if(map[x][y]==0)	map[x][y]=-1;
                    break;
                }
            }
            else
            {
                if(map[x+ix[i]][y+iy[i]]==-1)
                {
                    sau = new Point(x+ix[i], y+iy[i]);
                    map[x][y]=-2;
                    break;
                }
            }
        }

        return sau;
    }

    public void AI(){

        //toa do block
        int x = (int) (this.Mycar.getx() / this.race.BlockSize());
        int y = (int) (this.Mycar.gety() / this.race.BlockSize());
        if(x==this.race.BlockColumn()-2 && y==this.race.BlockRow()-2)
        {
            this.key="1000";
            return;
        }
        if(this.next==null)	this.next=tiep(x,y);
        if(tim>1000) tim=0;
        //speed
        double speed_now = Math.sqrt((this.Mycar.getx()-lx)*(this.Mycar.getx()-lx)+(this.Mycar.gety()-ly)*(this.Mycar.gety()-ly));
        speed = (speed*2+speed_now)/3;
        lx=this.Mycar.getx();
        ly=this.Mycar.gety();
        //Car's Direction
        double v_x = Math.cos(this.Mycar.getalpha() * Math.PI/180);
        double v_y = Math.sin(this.Mycar.getalpha() * Math.PI/180);

        //han che toc do
        if (speed>speedmax*this.race.BlockSize()) {
            this.key = "0000"; //stop
            return;
        }

        //lui khi dung lai
        if(--quay>0)
        {
            if(lui)
            {
                this.key="0100";
            }
            else
            {
                if(queotrai)	this.key="0110";
                else	this.key="0101";
            }
            return;
        }








        //tranh xe dich
        int n=All_cars.size();
        int huong=4;
        for(int i=0;i<n;i++)
        {
            //vi tri cua xe dich
            Car dich = All_cars.get(i);
            int xdich = (int) (dich.getx() / this.race.BlockSize());
            int ydich = (int) (dich.gety() / this.race.BlockSize());

            //neu co mot xe ve dich thi xoa tat cac cac duong ma xe do chua di qua
            if(xdich==this.race.BlockColumn()-2 && ydich==this.race.BlockRow()-2 && !vedich)
            {
                mapdich[i][xdich][ydich]=1;
                System.out.println("bingo");
                for(int a=1;a<=this.race.BlockColumn();a++)
                    for(int b=1;b<=this.race.BlockRow();b++)
                    {
                        if( mapdich[i][a][b] == 0 && map[a][b]==0)
                            map[a][b]=-2;
                    }
                vedich=true;
            }


            if(dich != Mycar && (xdich!=this.race.BlockColumn()-2 || ydich!=this.race.BlockRow()-2  ))
            {
                if(!vedich)
                    mapdich[i][xdich][ydich]=1;

                //khoang cach giua dich va ta
                double kcx = dich.getx() - this.Mycar.getx();
                double kcy = dich.gety() - this.Mycar.gety();
                double distance2car = Math.sqrt(kcx*kcx+kcy*kcy);

                //kiem tra 2 xe co bi dung nhau hay khong
                if(distance2car<khoangcach*this.Mycar.getsizex())
                {
                    if(this.Mycar.getx()== xdung && this.Mycar.gety()== ydung  && dem%sodem!=0)
                    {
                        dung=1;
                        dem++;
                    }
                    else
                    {
                        xdung=this.Mycar.getx();
                        ydung=this.Mycar.gety();
                        dem=1;
                    }
                }


                if(distance2car != 0 )
                {
                    kcx/=distance2car;
                    kcy/=distance2car;
                }

                //goc giua vecto 2 xe va huong cua xe
                double goc = kcx*v_x + kcy*v_y;
                if(distance2car < this.race.BlockSize()*2)
                {
                    tim2=0;
                }

                int alpha = (int)this.Mycar.getalpha();
                alpha=(alpha%360 + 360)%360;
                //tranh xe dich
                if( (distance2car < this.race.BlockSize()*1.5 && goc>0.6) || distance2car<this.race.BlockSize() )
                {
                    if( ( 45<alpha && 135>alpha   ) || ( 225<alpha  &&  315>alpha )   )  //xe ta nam theo chieu doc
                    {
                        if( dich.getx() > ((this.next.x + 0.5)*this.race.BlockSize() + xedich))
                            huong=2;	//qua trai
                        else if(dich.getx() > ((this.next.x + 0.5)*this.race.BlockSize()-xedich))
                        {
                            if( this.next.x < dich.getx() )
                                huong=2;	//be sang trai
                            else	huong=0;	//sang phai
                        }
                        else
                            huong=0; //phai
                    }
                    else
                    {
                        if( dich.gety() > ((this.next.y + 0.5)*this.race.BlockSize()+xedich))
                            huong=3;	//huong len
                        else if( dich.gety() > ((this.next.y + 0.5)*this.race.BlockSize()-xedich) )
                        {
                            if( this.next.y < dich.gety() )
                                huong=3;	//huong len
                            else
                                huong=1;	//huong xuong
                        }
                        else
                            huong=1;	//xuong
                    }
                }
            }
        }

        //block_center
        double block_center_x= (this.next.x + 0.5) * this.race.BlockSize() + ixx[huong]*xeta;
        double block_center_y= (this.next.y + 0.5) * this.race.BlockSize() + iyy[huong]*xeta;

        //Vector to Next Block Center from Car's position
        double c_x = block_center_x - this.Mycar.getx();
        double c_y = block_center_y - this.Mycar.gety();
        double distance2center = Math.sqrt(c_x*c_x+c_y*c_y);
        if (distance2center!=0) {
            //vector normalization
            c_x/=distance2center;
            c_y/=distance2center;
        }


        /*
         * xu ly khi bi dung tuong
         * sau mot khoang thoi gian se kiem tra xem dung tai vi tri cu hay khong
         * neu van dung nguyen thi se cho lui lai, lui phai, lui trai
         */
        if(dung%300==0)
        {
            if(xt==this.Mycar.getx() && yt==this.Mycar.gety() && (x!=1 || y!=1))
            {
                quay=150;
                dung=1;
                queotrai=false;
                lui=false;
                r++;
                if(r>1000)	r=0;
                if(r%3==0)
                {
                    System.out.println("queo phai");
                    queotrai=false;
                }
                else if(r%3==1)
                {
                    System.out.println("queo trai");
                    queotrai=true;
                }
                else
                {
                    System.out.println("lui");
                    lui=true;
                }
            }
            else
            {
                xt=this.Mycar.getx();
                yt=this.Mycar.gety();
            }
        }
        dung++;

        if(x==1 && y==1)	tim2=0;
        if (distance2center<this.race.BlockSize()*0.5)
        {
            this.next=tiep(x,y);
            tim2=0;
        }
        else
        {
            // Go to next block center
            double inner = v_x*c_x + v_y*c_y;
            double outer = v_x*c_y - v_y*c_x;

            if (inner > 0.995){
                this.key = "1000"; //go
            }
            else
            {
                if (inner < 0)
                {
                    if(tim%2==0) this.key = "0001"; //turn right
                    else	this.key = "0010"; 		//turn left
                }
                else
                {
                    if (this.race.BlockKind(x, y)!='3')
                    {
                        if (outer > 0)	//0001 turn right
                        {
                            if( tim2++ > a)
                            {if((tim++ % re)==0) this.key="0001"; else this.key="1001";}
                            else	this.key="0001";
                        }
                        else 		//"0010" turn left
                        {
                            if( tim2++ > a)
                            {if((tim++ % re)==0) this.key="0010"; else this.key="1010";}
                            else	this.key="0010";
                        }
                    }
                    else
                    {
                        if (outer > 0)//	  "0010" turn right
                        {
                            if( tim2++ > a)
                            {if((tim++ % re)==0) this.key="0010"; else this.key="1010";}
                            else	this.key="0010";
                        }
                        else		//"0001" turn left
                        {
                            if( tim2++ > a)
                            {if((tim++ % re)==0) this.key="0001"; else this.key="1001";}
                            else	this.key="0001";
                        }
                    }
                }
            }
        }
    }
}


