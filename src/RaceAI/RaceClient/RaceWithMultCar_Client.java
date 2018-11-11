package RaceAI.RaceClient;

import java.awt.Graphics2D;
import java.util.Vector;

import _LoadResource.LoadResource;

public class RaceWithMultCar_Client {
	Race race;
	Vector<Car> cars;
	Car Ccar;
	private int wd_h, wd_w;

	// Tat ca thong tin ban do duoc luu trong msg
	public void Init(String msg) {
		String[] temp = msg.split(":");

		int count = 0;
		String[] map = new String[Integer.parseInt(temp[count++])];
		for (int i = 0; i < map.length; i++) {
			map[i] = temp[count++];
		}

		int size = Integer.parseInt(temp[count++]);
		int b_no = Integer.parseInt(temp[count++]);
		int num_car = Integer.parseInt(temp[count++]);
		double car_s_x = Double.parseDouble(temp[count++]);
		double car_s_y = Double.parseDouble(temp[count++]);
		int CcarID = Integer.parseInt(temp[count++]);
		this.race = new Race(map, size, b_no);
		cars = new Vector<Car>();
		for (int k = 0; k < num_car; k++) {
			Car car = new Car(0, 0, 0, car_s_x, car_s_y);
			this.cars.add(car);
			car.SetOffset(this.race.offset);
			if (k == CcarID)
				Ccar = car;
		}
	}

	public void setWindowSize(int wd_h, int wd_w) {
		this.wd_w = wd_w;
		this.wd_h = wd_h;
	}

	public RaceWithMultCar_Client(String msg, int wd_w, int wd_h) {
		this.wd_h = wd_h;
		this.wd_w = wd_w;
		this.Init(msg);
		this.UpdateOffset();
	}

	public synchronized void UpdateCarPos(String msg) {
		String[] temp = msg.split(":");
		//System.out.println("--ee---"+temp[0]+","+temp[1]+","+temp[2]+","+temp[3]);
		int count = 0;
		for (int i = 0; i < this.cars.size(); i++) {
			this.cars.get(i).Update(Double.parseDouble(temp[count++]),
					Double.parseDouble(temp[count++]),
					Double.parseDouble(temp[count++]),
					Boolean.parseBoolean(temp[count++]));
		}
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				int x = (int) (this.Ccar.x / (this.race.b_no * this.race.size))
						+ i;
				int y = (int) (this.Ccar.y / (this.race.b_no * this.race.size))
						+ j;

				if (y >= 0 && y < this.race.map.length && x >= 0
						&& x < this.race.map[y].length()) {
					String s1 = this.race.map[y].substring(0, x);
					String s2 = this.race.map[y].substring(x + 1);
					String c = temp[count++];
					this.race.map[y] = s1 + c + s2;
				} else
					count++;
			}
		}

		this.UpdateOffset();
	}

	public void paint(Graphics2D figure) {
		this.race.paint(figure, this.wd_h, this.wd_w);
		for (int i = 0; i < this.cars.size(); i++) {
			this.cars.get(i).paint(figure);
			if (this.cars.get(i) == this.Ccar)
				figure.drawString("You", (int) (this.Ccar.x
						+ this.Ccar.offset.x - 10), (int) (this.Ccar.y
						+ this.Ccar.offset.y - this.Ccar.s_y));
		}
	}

	public void UpdateOffset() {
		this.race.offset.x = this.wd_w / 2 - this.Ccar.x;
		if (this.race.offset.x > 0)
			this.race.offset.x = 0;
		if (this.race.offset.x + this.race.map[0].length() * this.race.size
				* this.race.b_no < this.wd_w)
			this.race.offset.x = this.wd_w - this.race.map[0].length()
					* this.race.size * this.race.b_no;

		this.race.offset.y = this.wd_h / 2 - this.Ccar.y;
		if (this.race.offset.y > 0)
			this.race.offset.y = 0;
		if (this.race.offset.y + this.race.map.length * this.race.size
				* this.race.b_no < this.wd_h)
			this.race.offset.y = this.wd_h - this.race.map.length
					* this.race.size * this.race.b_no;

	}

}
