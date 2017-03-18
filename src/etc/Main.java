package etc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.openqa.selenium.By;  
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;  
import org.openqa.selenium.WebDriver;  
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;  
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import test.TestFile;

import com.thoughtworks.selenium.Selenium;

public class Main {  
	static int BASETIME = 2000;
	private static WebDriver new_driver;
	static HashSet<String> saveuser = new HashSet<String>();
	public static void rand_sleep(Random random_class) {
		int t = random_class.nextInt(2000);
		try {
			Thread.sleep(BASETIME + t);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void add_to_users(String attribute) {
		// TODO Auto-generated method stub
		if (saveuser.contains(attribute)) {
			return ;
		}
		saveuser.add(attribute);
		savetofile(attribute);
	}  
	
    private static void savetofile(String userid) {
		// TODO Auto-generated method stub
		TestFile.writeLog("C:\\Users\\chenjiashou\\Desktop\\SAVE_USER\\USERS.txt", userid);
	}

	public static void main(String[] args) throws InterruptedException {  
    	Random random_class = new Random();
    	
    	/*
    	System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
    	WebDriver dr = new ChromeDriver();  
        dr.get("http://www.baidu.com"); //打开首页  
        //dr.manage().window().maximize();    //最大化  
        //Thread.sleep(3000);  
        //dr.quit();
        System.out.println("page is " + dr.getTitle());
     // 通过 id 找到 input 的 DOM  
        WebElement element =dr.findElement(By.id("s_ipt"));  
        // 输入关键字  
        element.sendKeys("zTree");  
        // 提交 input 所在的 form  
        element.submit();  

        // 显示搜索结果页面的 title  
        System.out.println(" Page title is: " +dr.getTitle());  
        // 关闭浏览器  
        dr.quit();  
        // 关闭 ChromeDriver 接口
         */  
    	System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
    	ChromeOptions option = new ChromeOptions(); 
    	option.addArguments("-test-type"); 
    	option.addArguments("-start-maximized");
    	//禁止加载图片
    	Map<String, Object> prefs = new HashMap<String, Object>();
    	prefs.put("profile.managed_default_content_settings.images", 2);
    	option.setExperimentalOption("prefs", prefs);
    	
    	
        WebDriver driver = new ChromeDriver(option);
        driver.get("http://weibo.com/login.php");
        
        rand_sleep(random_class);
        
        WebElement list = driver.findElement(By.name("username"));
        list.sendKeys("690707520@qq.com");
        Thread.sleep(100);
//        List<WebElement> lists = driver.findElements(By.xpath("//*[@name='username']"));
//        System.out.println(lists.size());
//        lists.get(0).sendKeys("690707520@qq.com");
        WebElement psw = driver.findElement(By.name("password"));
        psw.sendKeys("chenjiashuo001");
        List<WebElement> elms = driver.findElements(By.xpath("//*[@class='W_btn_a btn_32px']"));
        System.out.println(elms.size());
        Thread.sleep(1000);
        elms.get(0).click();
//        elms.get(0).click();
        
        rand_sleep(random_class);
        
        //到weibo.cn中
        //driver.get("http://weibo.cn");
        
        //获取一堆活跃的用户
        //driver.get("http://weibo.com/1291477752/BDp8kFbfW?type=comment#_rnd1486711524693");
        
        //driver.get("http://weibo.com/1291477752/E1qYW4dds?type=comment");
        driver.get("http://weibo.com/1259110474/DaOQmzyuG?type=comment#_rnd1486719451347");
        rand_sleep(random_class);
        
        //找评论粉丝
        while (true) {
	        List<WebElement> comment_users = driver.findElements(By.xpath("//*[@ucardconf='type=1']"));
	        System.out.println(comment_users.size());
	        for (int i = 0; i < comment_users.size(); i++) {
	        	add_to_users(comment_users.get(i).getAttribute("usercard"));
	        	System.out.println(comment_users.get(i).getAttribute("usercard"));
	        }
	        List<WebElement> next_btn = driver.findElements(By.xpath("//*[@class='page next S_txt1 S_line1']"));
	        if (next_btn.size() == 0) {
	        	System.out.println("没有翻页，跳出!");
	        	break;
	        }
	        next_btn.get(0).click();
	        rand_sleep(random_class);
	    }
        //fans.get(4).click();
        //rand_sleep(random_class);
        
        
        //找好友
        List<WebElement> fans = driver.findElements(By.xpath("//*[@bpfilter='page_frame']"));
        System.out.println(fans.size());
        fans.get(4).click();
        rand_sleep(random_class);
        
        String currentWindow = driver.getWindowHandle();// 获取当前窗口句柄  
        System.out.println("curent = " + currentWindow);
        
        //进入好友页面
        List<WebElement> choicefan = driver.findElements(By.xpath("//*[@class='mod_pic']"));
        System.out.println(choicefan.size());    
        choicefan.get(0).click();
        rand_sleep(random_class);
        
        //窗口问题
        
        WebDriver new_dirver = null;
        Set<String> handles = driver.getWindowHandles();// 获取所有窗口句柄
        List<String> it = new ArrayList<String>(handles);
        
        System.out.println("set siez = " + handles.size());
        //Iterator<String> it = handles.iterator();  
        driver.switchTo().window(it.get(1));
        System.out.println("driver title: " + driver.getTitle());
        
        rand_sleep(random_class);
//        while (it.hasNext()) {
//        	System.out.println("now = " + it.next());
//            if (currentWindow == it.next()) {  
//                continue;// 跳出本次循环，继续下个循环  
//            }  
//            try {  
//                new_driver = driver.switchTo().window(it.next());// 切换到新窗口  
//                
//            } catch (Exception e) {  
//                  
//            }  
//            // window.close();//关闭当前焦点所在的窗口  
//        } 
        
        //点击查看更多， 查看标签
        List<WebElement> chickmore = driver.findElements(By.xpath("//*[@class='WB_cardmore S_txt1 S_line1 clearfix']"));
        System.out.println(chickmore.size());
        if (chickmore.size() != 0) {
        	chickmore.get(0).click();
        } else {
        	System.out.println("don't have!");
        	return ;
        }
        rand_sleep(random_class);
        
        //打印标签
        List<WebElement> tags = driver.findElements(By.xpath("//*[@node-type='tag']"));
        System.out.println(tags.size());    
        for (int i = 0; i < tags.size(); i++) {
        	System.out.println(tags.get(i).getText());
        }

        
        //choicefan.get(1).click();
        
        
        /**
        //网址（手机版的新浪微博，因为这个网站登录不要验证码）
        String baseUrl = "https://passport.weibo.cn/signin/login?entry=mweibo&res=wel&wm=3349&r=http%3A%2F%2Fm.weibo.cn%2F";
        driver.get(baseUrl);
        Thread.sleep(3000);
        //WebElement link1 = driver.findElement(By.linkText("登录"));
        //link1.click();
        //WebElement list = driver.findElement(By.name("username"));
        //list.sendKeys("690707520@qq.com");
        //WebElement psw = driver.findElement(By.name("password"));
        //psw.sendKeys("chenjiashuo001");
        //WebElement submitbut = driver.findElement(By.className("W_btn_a btn_32px"));
        //if (submitbut != null) {
        //	//submitbut.click();
        //}
        //driver.close();
        //WebElement login = driver.findElement(By.linkText("登录"));
        //login.click();
        driver.findElement(By.id("loginName")).clear();
        driver.findElement(By.id("loginName")).sendKeys("690707520@qq.com");

        //找到名为"loginPassword"的元素，填写密码
        driver.findElement(By.id("loginPassword")).clear();
        driver.findElement(By.id("loginPassword")).sendKeys("chenjiashuo001");

        //找到登录按钮，点击
        driver.findElement(By.id("loginAction")).click();
        Thread.sleep(2000);
        
        //然后就是找好友
        
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
        
        //获取cookies
        Set<Cookie> cookies = driver.manage().getCookies();
        String cookieStr = "";
        for (Cookie cookie : cookies) {
        	cookieStr += cookie.getName() + "=" + cookie.getValue() + "; ";
        }
        //不过一个WebDriver在登录后自带了Cookies了，直接打开其他地方也是可以的
        **/
    }

    
    
}  

