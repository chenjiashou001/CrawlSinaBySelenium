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
		
		//�ƶ���Ԫ��element����ġ����ˡ��뵱ǰ���ڵġ�����������
		//((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
		//((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

		//�ƶ���Ԫ��element����ġ��׶ˡ��뵱ǰ���ڵġ��ײ�������
		//((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", element);

		//�ƶ���ҳ����ײ�
		//((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
		
		Thread.sleep(3000);
		
		//�ƶ���ָ��������(��Ե�ǰ�������ƶ�)
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 700)");
		Thread.sleep(3000);
		//��������scrollBy��䣬�൱���ƶ���700+800=1600����λ��
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 800)");
		
		Thread.sleep(3000);
		//�ƶ������ھ���λ�����꣬�����ƶ���������1600����λ��
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 100)");
		Thread.sleep(3000);
		//��������scrollTo��䣬��Ȼ�ƶ���������1200����λ��
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 1200)");
		
		driver.close();
	}

}
