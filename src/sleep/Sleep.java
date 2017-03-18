package sleep;

public class Sleep {
	
	public static void sleep(int t) {
		try {
			Thread.sleep(t);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void rand_time(int base_time,int rand_time) {
		sleep(base_time + (int)(1+Math.random()*(rand_time)));
	}
	
}




