package test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class TestMsina {
	static int BASETIME = 5000;
	static String users[] = {
		"http://m.weibo.cn/u/2284721054?uid=2284721054&luicode=10000012&lfid=1005052644015861_-_FANS",
		"http://m.weibo.cn/u/3457655792?uid=3457655792&luicode=10000012&lfid=1005052644015861_-_FANS",
		"http://m.weibo.cn/u/3251296042?uid=3251296042&luicode=10000012&lfid=1005052644015861_-_FANS",
		"http://m.weibo.cn/u/3700732594?uid=3700732594&luicode=10000012&lfid=1005052644015861_-_FANS",
		"http://m.weibo.cn/u/2397457807?uid=2397457807&luicode=10000012&lfid=1005052644015861_-_FANS",
		"http://m.weibo.cn/u/2817359962?uid=2817359962&luicode=10000012&lfid=1005052644015861_-_FANS",
		"http://m.weibo.cn/u/2828261947?uid=2828261947&luicode=10000012&lfid=1005052644015861_-_FANS",
		null
	};
	
	static String sina_account[] = {
		"690707520@qq.com",
		"13048288579",
		"18038548714",
		"15322779609"
	};
	
	static String sina_password[] = {
		"chenjiashuo001",
		"q52000",
		"q52000",
		"q52000"
	};
	
	public static void main(String[] args) throws InterruptedException {  
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
    	ChromeOptions option = new ChromeOptions(); 
    	option.addArguments("-test-type"); 
    	option.addArguments("-start-maximized");
    	//禁止加载图片
    	Map<String, Object> prefs = new HashMap<String, Object>();
    	prefs.put("profile.managed_default_content_settings.images", 2);
    	option.setExperimentalOption("prefs", prefs);
        WebDriver driver = new ChromeDriver(option);
        for (int i = 0; i < 4; i++) {
        
        driver.get("https://passport.weibo.cn/signin/login?entry=mweibo&res=wel&wm=3349&r=http%3A%2F%2Fm.weibo.cn%2F");
        Thread.sleep(3000);
        driver.findElement(By.id("loginName")).clear();
        driver.findElement(By.id("loginName")).sendKeys(sina_account[i]);

        //找到名为"loginPassword"的元素，填写密码
        driver.findElement(By.id("loginPassword")).clear();
        driver.findElement(By.id("loginPassword")).sendKeys(sina_password[i]);
        //找到登录按钮，点击
        driver.findElement(By.id("loginAction")).click();
        Thread.sleep(20000);
        
        }
//        for (int i = 0; users[i] != null; i++) {
//        	driver.get(users[i]);
//        	Thread.sleep(1000);
//        	if (i%5 == 0) i = 0;
//        }
        long id = 2284721054L;
        while(true)
        {
        	String up = "http://m.weibo.cn/u/"+id+"?uid="+id+"&luicode=10000012&lfid=1005052644015861_-_FANS";
        	System.out.println(up);
        	driver.get(up);
        	Thread.sleep(1000);
        	id++;
        }
        
        //然后就是找好友
        /*
        WebElement logDiv = driver.findElement(By.xpath("//*[@data-text='我']"));
        logDiv.click();
        Thread.sleep(BASETIME);
        
        List<WebElement> fans = driver.findElements(By.xpath("//*[@class='box-col line-separate']"));
        //System.out.println(fans.size());
        WebElement fanbtn = fans.get(2);
        fanbtn.click();
        
        
        //进入好友页面
        Thread.sleep(BASETIME);
        List<WebElement> friends = driver.findElements(By.xpath("//*[@class='card m-panel card28 m-avatar-box']"));
        System.out.println(friends.size());
        WebElement friendbtn = friends.get(0);
        friendbtn.click();
        //获取微博内容
        Thread.sleep(BASETIME);
        List<WebElement> texts = driver.findElements(By.xpath("//*[@class='weibo-text']"));
        for (int i = 0; i < texts.size(); i++) {
        	System.out.println(texts.get(i).getText());
        }
        */
        
        //获取cookies
//        Set<Cookie> cookies = driver.manage().getCookies();
//        String cookieStr = "";
//        for (Cookie cookie : cookies) {
//        	cookieStr += cookie.getName() + "=" + cookie.getValue() + "; ";
//        }
//        System.out.println(cookieStr);
        //不过一个WebDriver在登录后自带了Cookies了，直接打开其他地方也是可以的
	}
}
