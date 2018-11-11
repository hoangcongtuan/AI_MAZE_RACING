package RaceAI.AI;

import java.awt.Point;
import java.util.Random;
import java.util.Vector;

import RaceAI.RaceClient.Car;
import RaceAI.RaceClient.Race;

public class MainAI_2{
    Race race;
    Vector<Car> All_cars;
    Car Mycar;

    public String key = "0000"; // Go-Back-Left-Right (Up - Down - Left - Right)

    public MainAI_2(Race race, Vector<Car> cars, Car Mycar){
        this.race = race;
        this.Mycar = Mycar;
        this.All_cars = cars;
    }

    /// Write your AI here ...
    // your variants
    Point last,now,next;
    Random rand = new Random();
    int[] ix = {0, 1, 0, -1};
    int[] iy = {1, 0, -1, 0};

    //last position
    double lx=0,ly=0;
    // last speed
    double speed = 0;
    // your AI
    public void AI(){
        // AI example
        //Block Index
        int x = (int) (this.Mycar.getx() / this.race.BlockSize());
        int y = (int) (this.Mycar.gety() / this.race.BlockSize());

        double speed_now = Math.sqrt((this.Mycar.getx()-lx)*(this.Mycar.getx()-lx)+(this.Mycar.gety()-ly)*(this.Mycar.gety()-ly));
        speed = (speed*2+speed_now)/3;
        lx=this.Mycar.getx();
        ly=this.Mycar.gety();
        //System.out.println(speed+ ", "+this.race.BlockSize()*0.01);
        if (speed>this.race.BlockSize()*0.01) {
            this.key = "0000"; //stop
            return;
        }
        else
        if (speed>this.race.BlockSize()*0.02) {
            this.key = "0100"; //break
            return;
        }

        this.now = new Point(x,y);
        if (this.next==null) this.next = this.now;
        if (this.last==null) this.last = this.now;

        //Next Block Center Coordinate
        double block_center_x= (this.next.x + 0.5) * this.race.BlockSize();
        double block_center_y= (this.next.y + 0.5) * this.race.BlockSize();

        //Car's Direction
        double v_x = Math.cos(this.Mycar.getalpha() * Math.PI/180);
        double v_y = Math.sin(this.Mycar.getalpha() * Math.PI/180);

        //Vector to Next Block Center from Car's position
        double c_x = block_center_x - this.Mycar.getx();
        double c_y = block_center_y - this.Mycar.gety();
        double distance2center = Math.sqrt(c_x*c_x+c_y*c_y);
        if (distance2center!=0) {
            //vector normalization
            c_x/=distance2center;
            c_y/=distance2center;
        }


        if (distance2center<this.race.BlockSize()*0.2){
            this.key = "0000"; //stop
            // find new nextBlock block
            boolean find=false;
            {
                int temp;
                int i1=rand.nextInt(4);
                int i2=rand.nextInt(4);
                temp = ix[i1];
                ix[i1] = ix[i2];
                ix[i2] = temp;
                temp = iy[i1];
                iy[i1] = iy[i2];
                iy[i2] = temp;

            }
            int i;
            for (i=0;i<4;i++)
                if ((last.x!=x+ix[i] || last.y!=y+iy[i]) && this.race.BlockKind(x+ix[i], y+iy[i]) !='1'){
                    find = true;
                    break;
                }
            if (find) this.next = new Point(x+ix[i], y+iy[i]);
            else this.next = this.last;
            this.last = this.now;
        }
        else {
            // Go to nextBlock block center
            double inner = v_x*c_x + v_y*c_y;
            double outer = v_x*c_y - v_y*c_x;
            if (inner > 0.995){
                this.key = "1000"; //go
            } else {
                if (inner < 0){
                    this.key = "0001"; //turn right
                    System.out.println("turn right");
                }
                else {
                    if (this.race.BlockKind(x, y)!='3')
                        if (outer > 0) {
                            this.key = "0001"; //turn right
                            System.out.println("turn right");
                        }
                        else {
                            this.key = "0010"; //turn left
                            System.out.println("turn left");
                        }
                    else
                    if (outer > 0) {
                        System.out.println("turn right");
                        this.key = "0010"; //turn right
                    }
                    else {
                        System.out.println("turn left");
                        this.key = "0001"; //turn left
                    }
                }
            }
        }
    }
}
