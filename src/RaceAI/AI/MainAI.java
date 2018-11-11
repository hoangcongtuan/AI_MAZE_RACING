package RaceAI.AI;

import java.awt.Point;
import java.util.Vector;

import RaceAI.RaceClient.Car;
import RaceAI.RaceClient.Race;

public class MainAI {
    private Race race;
    private Vector<Car> All_cars;
    private Car Mycar;
    public String key = "0000"; // Go-Back-Left-Right (Up - Down - Left - Right)

    //my variable
    private Point nextBlock;

    /**
     * bản đồ đánh dấu các điểm đã đi qua
     * 0: chưa đi qua
     * -1: đi qua 1 lần
     * -2: đi qua 2 lần -> không đi qua nữa
     * 3, 4: ngã 3, ngã 4
     */
    private int[][] map;

    /**
     * bản đồ đánh dấu các điểm đã đi qua của các xe khác
     * 0: chưa đi qua
     * 1: đã đi qua
     */
    private int[][][] mapOther;

    private int[] ix = {1, 0, 0, -1};
    private int[] iy = {0, 1, -1, 0};

    //last position
    private double lastX = 0;
    private double lastY = 0;

    // last speed
    private double speed = 0;
    private final double SPEED_MAX = 0.0125;

    //dieu khien xe chay
    //neu timer1%TURN_VAL == 1 thi xe vua thang vua TURN_VAL, khong thi xe chi TURN_VAL (dua vao gia toc truoc do)
    /**
     * các timer điều khiển cua xe ở các góc
     */
    private int timer1 = 0, timer2 = 0;
    private final int TURN_VAL = 3; // ở mỗi khúc cua, sau TURN_VAL lần đi thẳng thì có 1 lần vừa đi thẳng rẽ
    private final int TURN_THRESHOLD = 63; //sau TURN_THRESHOLD lần quay xe thì bắt đầu vừa đi thẳng vửa rẽ xe

    /**
     * đã có ai về đích chưa
     */
    private boolean isOtherFinish = false;

    //cac bien dieu khien xe crash_timer
    /**
     * các biến điều khiển lui xe khi bị crash
     */
    private int turn_back_count = 0;    //số lần lui xe
    private int random_timer = 0;        //timer random việc lựa chọn cách lui xe
    private boolean turn_back_left, turn_back; //phương án lui xe
    private int crash_timer = 0;        //timer để check xem xe có bị crash hay không
    private double xt = 0, yt = 0;        //lưu vị trí xe để phát hiện crash
    private int CRASH_CHECK_INTERVAL = 300;    //sau mỗi CRASH_CHECK_INTERVAL thì kiểm tra crash
    private int RANDOM_TIMER_LIMIT = 1000;    //limit của timer

    public MainAI(Race race, Vector<Car> cars, Car Mycar) {
        this.race = race;
        this.Mycar = Mycar;
        this.All_cars = cars;

        initMyVariable();
    }

    private void initMyVariable() {
        map = new int[this.race.BlockColumn() + 1]
                [this.race.BlockRow() + 1];

        mapOther = new int[All_cars.size()]
                [this.race.BlockColumn() + 1]
                [this.race.BlockRow() + 1];

        for (int i = 1; i <= this.race.BlockColumn(); i++) {
            for (int j = 1; j <= this.race.BlockRow(); j++) {
                map[i][j] = 0;
                for (int k = 0; k < All_cars.size(); k++)
                    mapOther[k][i][j] = 0;
            }
        }
    }

    /**
     * Tìm block tiếp theo
     *
     * @param currentBlockX Block hiện tại X
     * @param currentBlockY Block hiện tại Y
     * @return Block tiếp theo
     */
    private Point findNextBlock(int currentBlockX, int currentBlockY) {
        Point nextBlock = new Point();
        int soNgaDuong = 0;    //số ngã đường
        boolean canGo = false;    //có đường đi tiếp hay không
        boolean newWay = false;    //có đường chưa đi qua hay không

        for (int i = 0; i <= 3; i++) {
            if (this.race.BlockKind(currentBlockX + ix[i], currentBlockY + iy[i]) != '1')
                soNgaDuong++;

            if ((map[currentBlockX + ix[i]][currentBlockY + iy[i]] >= 0)
                    && (this.race.BlockKind(currentBlockX + ix[i], currentBlockY + iy[i]) != '1'))
                canGo = true;
            if ((map[currentBlockX + ix[i]][currentBlockY + iy[i]] == 0)
                    && (this.race.BlockKind(currentBlockX + ix[i], currentBlockY + iy[i]) != '1'))
                newWay = true;
        }

        //là ngã 3, ngã tư
        if (soNgaDuong > 2)
            map[currentBlockX][currentBlockY] = soNgaDuong;

        //dò hết 4 block xung quanh
        for (int i = 0; i <= 3; i++) {
            if (canGo && newWay) {
                if ((this.race.BlockKind(currentBlockX + ix[i], currentBlockY + iy[i]) != '1')
                        && (map[currentBlockX + ix[i]][currentBlockY + iy[i]] == 0)) {
                    nextBlock = new Point(currentBlockX + ix[i], currentBlockY + iy[i]);
                    map[currentBlockX][currentBlockY] = -1;
                    break;
                }
            } else if (canGo) {
                if ((this.race.BlockKind(currentBlockX + ix[i], currentBlockY + iy[i]) != '1')
                        && (map[currentBlockX + ix[i]][currentBlockY + iy[i]] > 0)) {
                    nextBlock = new Point(currentBlockX + ix[i], currentBlockY + iy[i]);

                    if (map[currentBlockX][currentBlockY] == 0)
                        map[currentBlockX][currentBlockY] = -1;
                    break;
                }
            } else {
                if (map[currentBlockX + ix[i]][currentBlockY + iy[i]] == -1) {
                    nextBlock = new Point(currentBlockX + ix[i], currentBlockY + iy[i]);
                    map[currentBlockX][currentBlockY] = -2;
                    break;
                }
            }
        }

        return nextBlock;
    }

    public void AI() {
        int currentBlockX = (int) (this.Mycar.getx() / this.race.BlockSize());
        int currentBlockY = (int) (this.Mycar.gety() / this.race.BlockSize());
        if (currentBlockX == this.race.BlockColumn() - 2 && currentBlockY == this.race.BlockRow() - 2) {
            //goal!, go straight ahead
            this.key = "1000";
            return;
        }

        if (this.nextBlock == null)
            this.nextBlock = findNextBlock(currentBlockX, currentBlockY);

        if (timer1 > 1000)
            timer1 = 0;
        //speed
        double speed_now = Math.sqrt(
                (this.Mycar.getx() - lastX) * (this.Mycar.getx() - lastX)
                        + (this.Mycar.gety() - lastY) * (this.Mycar.gety() - lastY));
        speed = (speed * 2 + speed_now) / 3;
        lastX = this.Mycar.getx();
        lastY = this.Mycar.gety();

        //Car's Direction
        double v_x = Math.cos(this.Mycar.getalpha() * Math.PI / 180);
        double v_y = Math.sin(this.Mycar.getalpha() * Math.PI / 180);

        //speed limit
        if (speed > SPEED_MAX * this.race.BlockSize()) {
            this.key = "0000"; //stop
            return;
        }

        //turn back action after crash
        if (--turn_back_count > 0) {
            if (turn_back) {
                this.key = "0100";
//				System.out.println("back");
            } else {
                if (turn_back_left) {
                    this.key = "0110";
//					System.out.println("back left");
                } else {
                    this.key = "0101";
//					System.out.println("back right");
                }
            }
            return;
        }

        int n = All_cars.size();
        for (int i = 0; i < n; i++) {
            //vi tri cua xe dich
            Car others = All_cars.get(i);
            int other_x = (int) (others.getx() / this.race.BlockSize());
            int other_y = (int) (others.gety() / this.race.BlockSize());

            //nếu có 1 xe địch về đích thì xóa tất cả các vị trí mà xe địch chưa đi qua
            if (other_x == this.race.BlockColumn() - 2 && other_y == this.race.BlockRow() - 2 && !isOtherFinish) {
                mapOther[i][other_x][other_y] = 1;
                for (int j = 1; j <= this.race.BlockColumn(); j++)
                    for (int k = 1; k <= this.race.BlockRow(); k++) {
                        if (mapOther[i][j][k] == 0 && map[j][k] == 0)
                            map[j][k] = -2;
                    }
                isOtherFinish = true;
            }

            //chạy tới nextblock
            //block_center
            double next_bl_center_x = (this.nextBlock.x + 0.5) * this.race.BlockSize();// + ixx[huong]*xeta;
            double next_bl_center_y = (this.nextBlock.y + 0.5) * this.race.BlockSize();// + iyy[huong]*xeta;

            //Vector to Next Block Center from Car's position
            double c_x = next_bl_center_x - this.Mycar.getx();
            double c_y = next_bl_center_y - this.Mycar.gety();
            double distance2center = Math.sqrt(c_x * c_x + c_y * c_y);
            if (distance2center != 0) {
                //vector normalization
                c_x /= distance2center;
                c_y /= distance2center;
            }

            /**
             * sau mỗi CRASH_CHECK_INTERVAL thì kiểm tra crash
             */
            if (crash_timer % CRASH_CHECK_INTERVAL == 0) {
                if (xt == this.Mycar.getx() && yt == this.Mycar.gety() && (currentBlockX != 1 || currentBlockY != 1)) {

                    turn_back_count = 150;
                    crash_timer = 1;
                    turn_back_left = false;
                    turn_back = false;
                    random_timer++;
                    if (random_timer > RANDOM_TIMER_LIMIT)
                        random_timer = 0;

                    if (random_timer % 3 == 0) {
                        turn_back_left = false;
                    } else if (random_timer % 3 == 1) {
                        turn_back_left = true;
                    } else {
                        turn_back = true;
                    }
                } else {
                    xt = this.Mycar.getx();
                    yt = this.Mycar.gety();
                }
            }
            crash_timer++;

            if (currentBlockX == 1 && currentBlockY == 1)
                timer2 = 0;
            if (distance2center < this.race.BlockSize() * 0.5) {
                this.nextBlock = findNextBlock(currentBlockX, currentBlockY);
                timer2 = 0;
            } else {
                // Go to nextBlock block center
                double inner = v_x * c_x + v_y * c_y;
                double outer = v_x * c_y - v_y * c_x;

                if (inner > 0.995) {
                    this.key = "1000"; //go
                } else //inner < 0.995{
                    if (inner < 0) {
//					System.out.println("Back");
                        if (timer1 % 2 == 0) {
                            this.key = "0001"; //turn right
//						System.out.println("back right");
                        } else {
                            this.key = "0010";        //turn left
//						System.out.println("back left");
                        }
                    } else //iner >= 0
                    {
                        if (this.race.BlockKind(currentBlockX, currentBlockY) != '3') {
                            if (outer > 0)    //0001 turn right
                            {
                                if (timer2++ > TURN_THRESHOLD) {
//								System.out.println(timer1);
                                    if ((timer1++ % TURN_VAL) == 0)
                                        this.key = "0001";
                                    else
                                        this.key = "1001";
                                } else
                                    this.key = "0001";
                            } else        //"0010" turn left
                            {
                                if (timer2++ > TURN_THRESHOLD) {
//								System.out.println(timer1);
                                    if ((timer1++ % TURN_VAL) == 0)
                                        this.key = "0010";
                                    else
                                        this.key = "1010";
                                } else
                                    this.key = "0010";
                            }
                        } else {
                            if (outer > 0)//	  "0010" turn right
                            {
                                if (timer2++ > TURN_THRESHOLD) {
                                    if ((timer1++ % TURN_VAL) == 0)
                                        this.key = "0010";
                                    else this.key = "1010";
                                } else
                                    this.key = "0010";
                            } else        //"0001" turn left
                            {
                                if (timer2++ > TURN_THRESHOLD) {
                                    if ((timer1++ % TURN_VAL) == 0)
                                        this.key = "0001";
                                    else this.key = "1001";
                                } else
                                    this.key = "0001";
                            }
                        }
                    }
            }
        }
    }
}