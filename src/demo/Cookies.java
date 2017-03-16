package demo;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.os.WindowsUtils;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Cookies {
	
	/**
     * @author Young
     * 
     */
    public static void addCookies() {
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
		ChromeOptions option = new ChromeOptions(); 
    	option.addArguments("-test-type"); //测试模式，顶部警告忽略
    	option.addArguments("-start-maximized");//最大化
        WebDriver driver = new ChromeDriver(option);
        
        driver.get("http://www.zhihu.com/#signin");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        WebElement user = driver
                .findElement(By.xpath("//input[@name='account']"));
        user.clear();
        user.sendKeys("690707520@qq.com");
        WebElement password = driver.findElement(By
                .xpath("//input[@name='password']"));
        password.clear();
        password.sendKeys("7115135");

        WebElement submit = driver.findElement(By
                .xpath("//button[@class='sign-button submit']"));
        submit.submit();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        File file = new File("broswer.data");
        try {
            // delete file if exists
            file.delete();
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            for (Cookie ck : driver.manage().getCookies()) {
                bw.write(ck.getName() + ";" + ck.getValue() + ";"
                        + ck.getDomain() + ";" + ck.getPath() + ";"
                        + ck.getExpiry() + ";" + ck.isSecure());
                bw.newLine();
            }
            bw.flush();
            bw.close();
            fw.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("cookie write to file");
        }
    }	
	
    /**
     * 得到WebDriver
     * @return
     */
    public static WebDriver create() {
        
        // TODO Auto-generated method stub
        String chromdriver="C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", chromdriver);
        ChromeOptions options = new ChromeOptions();
        
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability("chrome.switches",
                Arrays.asList("--start-maximized"));
        options.addArguments("--test-type", "--start-maximized");
        WebDriver driver=new ChromeDriver(options);
        return driver;
    }
    
    public static void main(String[] args) {
		addCookies();
		//WindowsUtils.tryToKillByName("chrome.exe");//关闭浏览器
        //WindowsUtils.getProgramFilesPath();
        WebDriver driver = create();
        
        driver.get("http://www.zhihu.com/");
        try 
        {
            File file=new File("broswer.data");
            FileReader fr=new FileReader(file);
            BufferedReader br=new BufferedReader(fr);
            String line;
            while((line=br.readLine())!= null)
            {
                StringTokenizer str=new StringTokenizer(line,";");
                while(str.hasMoreTokens())
                {
                    String name=str.nextToken();
                    String value=str.nextToken();
                    String domain=str.nextToken();
                    String path=str.nextToken();
                    Date expiry=null;
                    String dt;
                    if(!(dt=str.nextToken()).equals(null))
                    {
                        //expiry=new Date(dt);
                        //System.out.println(expiry);
                        System.out.println(dt);
                    }
                    boolean isSecure=new Boolean(str.nextToken()).booleanValue();
                    Cookie ck=new Cookie(name,value,domain,path,expiry,isSecure);
                    
                    driver.manage().addCookie(ck);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        driver.get("http://www.zhihu.com/");
	}
    //关于COOKIES的读写 封装一下
}
