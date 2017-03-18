package sleep;

import org.openqa.selenium.WebDriver;



public class WaitWebLoad {
	static final int MAX_REFRESH_CNT = 3;
	static final int MAX_WAIT_TIME = 10000;
	
	public static boolean load(WebDriver driver, WebLoad webload) {
		try {	
			int refresh_cnt = 0;
			while (refresh_cnt < MAX_REFRESH_CNT) {
				int wait_time = 0;
				while (wait_time < MAX_WAIT_TIME) {
					Thread.sleep(1000);
					wait_time += 1000;
					if (webload.is_success(driver)) {
						return true;
					}
				}
				refresh_cnt++;
				driver.navigate().refresh();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}




