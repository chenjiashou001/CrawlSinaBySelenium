package etc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import sinaaccount.LoginUsersManage;
import sleep.WaitWebLoad;
import sleep.WebLoad;

public class ClawerByUserId {
	
	static final int MAX_TRY_TIME = 3;
	/**
	 * 微博登录，获取cookies
	 * @return 带有登录信息cookies的WebDriver
	 */
	public WebDriver login(String username, String password, int flag_validate) {
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
		ChromeOptions option = new ChromeOptions(); 
    	option.addArguments("-test-type"); //测试模式，顶部警告忽略
    	option.addArguments("-start-maximized");//最大化
    	//禁止加载图片
//    	Map<String, Object> prefs = new HashMap<String, Object>();
//    	prefs.put("profile.managed_default_content_settings.images", 2);
//    	option.setExperimentalOption("prefs", prefs);
    	
    	WebDriver driver = new ChromeDriver(option);
        driver.get("http://weibo.com/login.php");
        if (!WaitWebLoad.load(driver, new WaitLogin())) {
        	return null;
        }
        WebElement list = driver.findElement(By.name("username"));
        list.sendKeys(username);
        sleep(500);
        WebElement psw = driver.findElement(By.name("password"));
        psw.sendKeys(password);
        List<WebElement> elms = driver.findElements(By.xpath("//*[@class='W_btn_a btn_32px']"));
        System.out.println(elms.size());
        sleep(1000 + flag_validate * 20000);
        //elms.get(0).click();
        sleep(1000);
        return driver;
	}
	
	/**
	 * 得到一组用户的信息,并存入mongodb
	 * @param userids
	 * @return
	 */
	public boolean clawer_users(List<String> userids) {
		LoginUsersManage login_manage = new LoginUsersManage();
		WebDriver driver = login(login_manage.getUserByIndex(2).getName(), login_manage.getUserByIndex(2).getPassword(), 1);
		
		if (null == driver) {
			return false;
		}
		int unsuccess_cnt = 0;
		for (int i = 0;i < userids.size(); i++) {
			String id = userids.get(i);
			Document document = new Document("id", id);
			String url = "http://weibo.cn/account/privacy/tags/?uid="+userids.get(i)+"&st=dd972e";
			driver.get(url);
			if (!WaitWebLoad.load(driver, new WaitTag())) {
	        	unsuccess_cnt++;
	        	continue;
			}
			List<WebElement> tags = driver.findElements(By.xpath("/html/body/div[6]/a"));
			String str_tag="";
			for (int j = 0; j < tags.size(); j++) {
				System.out.println(tags.get(j).getText());
				str_tag += tags.get(j).getText()+",";
			}
			document.append("tags", str_tag);
			rand_sleep(2000,2000);
			//然后就是存储微博内容
			url = "http://m.weibo.cn/u/" + id + "?uid=" + id + "&luicode=10000012&lfid=1005052644015861_-_FANS";
			driver.get(url);
			rand_sleep(5000,10000);
			if (!WaitWebLoad.load(driver, new WaitContent())) {
				unsuccess_cnt++;
				continue;
			}
			int pre = -1;
			while (true) {
				List<WebElement> contents = driver.findElements(By.className("time"));
				if (contents.size() == 0 || contents.size() == pre || contents.size() > 100) {
					break;
				}
				pre = contents.size();//大小没有变也跳出
				String time = contents.get(pre - 1).getText();
				System.out.println("time: " + time);
				if (time.length() > 3 && time.substring(0, 2).equals("20")) {
					break;
				}
				((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
				sleep(1500);
				rand_sleep(3000, 3000);
			}
			List<WebElement> allcontents = driver.findElements(By.xpath("//*[@class='card m-panel card9']"));
			List<Document> documents = new ArrayList<Document>();
			for (int j = 0; j < allcontents.size(); j++) {
				Document tmp_weibo = new Document();
				List<WebElement> times = allcontents.get(j).findElements(By.className("time"));
				if (times.size() != 0) {
					tmp_weibo.append("time", times.get(0).getText());
				}
				List<WebElement> weibo_text = allcontents.get(j).findElements(By.className("weibo-text"));
				StringBuffer str_weibo = new StringBuffer();
				for (int k = 0; k < weibo_text.size(); k++) {
					//System.out.println(weibo_text.get(k).getText());
					str_weibo.append(weibo_text.get(k).getText() + ",#|#,");
				}
				tmp_weibo.append("weibo_text", str_weibo.toString());
				documents.add(tmp_weibo);
			}
			document.append("content", documents);
			savetomongodb(document);
		}
		return true;
	}
	
	private void rand_sleep(int i, int j) {
		// TODO Auto-generated method stub
		sleep(i + (int)(1+Math.random()*(j)));
	}

	private void savetomongodb(Document document) {
		// 连接到 mongodb 服务
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        
        // 连接到数据库
        MongoDatabase mongoDatabase = mongoClient.getDatabase("sina");  
        System.out.println("Connect to database successfully");
        
        MongoCollection<Document> collection = mongoDatabase.getCollection("userdata");
        System.out.println("集合 userdata 选择成功");
        
        collection.insertOne(document);
	}

	/**
	 * 线程休眠s秒
	 * @param s
	 */
	private void sleep(int s) {
		try {
			Thread.sleep(s);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

/**
 * 
 * @author chenjiashou
 *
 */
class WaitLogin extends	WebLoad {

	@Override
	public boolean is_success(WebDriver driver) {
		WebElement list = driver.findElement(By.name("username"));
		WebElement psw = driver.findElement(By.name("password"));
		List<WebElement> elms = driver.findElements(By.xpath("//*[@class='W_btn_a btn_32px']"));
		//System.out.println("elms size:" + elms.size());
		if (list == null || psw == null || elms.size() == 0) {
			return false;
		}
		return true;
	}
}

/**
 * 
 * @author chenjiashou
 *
 */
class WaitTag extends WebLoad {

	@Override
	public boolean is_success(WebDriver driver) {
		List<WebElement> elms = driver.findElements(By.className("c"));
		//System.out.println("elms size:" + elms.size());
		if (elms.size() == 0) {
			return false;
		}
		return true;
	}
}

/**
 * 
 * @author chenjiashou
 *
 */
class WaitContent extends WebLoad {

	@Override
	public boolean is_success(WebDriver driver) {
		List<WebElement> elms  = driver.findElements(By.className("txt-shadow"));
		if (elms.size() == 0) {
			return false;
		}
		return true;
	}
	
}

