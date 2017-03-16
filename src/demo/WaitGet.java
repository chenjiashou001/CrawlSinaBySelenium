package demo;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import tools.SeleniumUtil;

public class WaitGet {

	public static void main(String[] args) {
		SeleniumUtil su = new SeleniumUtil();
		WebDriver driver = su.getDriver();
		driver.get("weibo.com");
		WebDriverWait wait = new WebDriverWait(driver, 20);
		
	}

}
