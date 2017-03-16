package tools;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import sinaaccount.LoginUsersManage;
import sleep.Sleep;
import cookie.CookieUtil;

public class SeleniumUtil {
	
	public static WebDriver getDriver() {
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\chenhuan001\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver.exe");
		ChromeOptions option = new ChromeOptions(); 
    	option.addArguments("-test-type"); //测试模式，顶部警告忽略
    	option.addArguments("-start-maximized");//最大化
    	//禁止加载图片
//    	Map<String, Object> prefs = new HashMap<String, Object>();
//    	prefs.put("profile.managed_default_content_settings.images", 2);
//    	option.setExperimentalOption("prefs", prefs);
    	
        WebDriver driver = new ChromeDriver(option);
        return driver;
	}
	
	public static void getAndSaveUserCookie(WebDriver driver, int begin_index2, int end_index2) {
		LoginUsersManage manage = new LoginUsersManage();
		Login login = new Login();
		for (int i = begin_index2; i <= end_index2; i++) {
			//sleep(500);
			//sleep(20000);
			try{
				driver.manage().deleteAllCookies();
			} catch(Exception e) {
				System.out.println("deleteAllCookies有异常");
				driver.close();
				driver = SeleniumUtil.getDriver();
				i--;
				continue;
			}
			//sleep(500);
			if (login.login(manage.getUserByIndex(i).getName(), 
						manage.getUserByIndex(i).getPassword(), 
						manage.getUserByIndex(i).getSafe_level(), 
						driver) ) {
				System.out.println("用户" + i + "登录成功");
			} else {
				System.out.println("用户" + i + "登录失败");
			}
			Sleep.sleep(60000);
			//现在是有了cookie的driver
			if (CookieUtil.save(String.valueOf(i), driver) == false) {
				i--;
				continue;//重新来
			}
		}
		//保存好了cookie
	}

	private static void sleep(int i) {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
