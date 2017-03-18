package demo;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class MoveScroll {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		//get driver
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
    	ChromeOptions option = new ChromeOptions(); 
    	WebDriver driver = new ChromeDriver(option);
        driver.get("https://www.hao123.com/");
		
		//移动到元素element对象的“顶端”与当前窗口的“顶部”对齐
		//((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
		//((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

		//移动到元素element对象的“底端”与当前窗口的“底部”对齐
		//((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", element);

		//移动到页面最底部
		//((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
		
		Thread.sleep(3000);
		
		//移动到指定的坐标(相对当前的坐标移动)
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 700)");
		Thread.sleep(3000);
		//结合上面的scrollBy语句，相当于移动到700+800=1600像素位置
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 800)");
		
		Thread.sleep(3000);
		//移动到窗口绝对位置坐标，如下移动到纵坐标1600像素位置
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 100)");
		Thread.sleep(3000);
		//结合上面的scrollTo语句，仍然移动到纵坐标1200像素位置
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 1200)");
		
		driver.close();
	}

}

