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
    	option.addArguments("-test-type"); //����ģʽ�������������
    	option.addArguments("-start-maximized");//���
    	//��ֹ����ͼƬ
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
				System.out.println("deleteAllCookies���쳣");
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
				System.out.println("�û�" + i + "��¼�ɹ�");
			} else {
				System.out.println("�û�" + i + "��¼ʧ��");
			}
			Sleep.sleep(60000);
			//����������cookie��driver
			if (CookieUtil.save(String.valueOf(i), driver) == false) {
				i--;
				continue;//������
			}
		}
		//�������cookie
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
