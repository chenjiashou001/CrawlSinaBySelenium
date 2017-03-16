package test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import sinaaccount.LoginUsersManage;
import cookie.CookieUtil;

public class TestCookie {

	static void testSave(WebDriver driver) {
		driver.get("http://weibo.com/login.php");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        LoginUsersManage manage = new LoginUsersManage();
        WebElement user = driver.findElement(By.name("username"));
        user.clear();
        user.sendKeys(manage.getUserByIndex(0).getName());
        WebElement password = driver.findElement(By.name("password"));
        
        password.clear();
        password.sendKeys(manage.getUserByIndex(0).getPassword());
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        List<WebElement> elms = driver.findElements(By.xpath("//*[@class='W_btn_a btn_32px']"));
        elms.get(0).click();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        CookieUtil.save("0", driver);
	}
	
	static void testRead(WebDriver driver) {
		driver.get("http://weibo.com/");
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		CookieUtil.get("0", driver);
		driver.get("http://weibo.com/");
	}
	
	public static void main(String[] args) {
		
		
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
		ChromeOptions option = new ChromeOptions(); 
    	option.addArguments("-test-type"); //测试模式，顶部警告忽略
    	option.addArguments("-start-maximized");//最大化
        WebDriver driver = new ChromeDriver(option);
        //testSave(driver);
        testRead(driver);
        
	}

}
