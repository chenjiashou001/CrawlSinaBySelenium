package test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import sinaaccount.LoginUsersManage;
import tools.Login;

public class TestLogin {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
		ChromeOptions option = new ChromeOptions(); 
    	option.addArguments("-test-type"); //����ģʽ�������������
    	option.addArguments("-start-maximized");//���
        WebDriver driver = new ChromeDriver(option);
        LoginUsersManage manage = new LoginUsersManage();
        Login login = new Login();
			
    	if (login.login(manage.getUserByIndex(1).getName(), 
					manage.getUserByIndex(1).getPassword(), 
					manage.getUserByIndex(1).getSafe_level(), driver)) {
    		System.out.println("��¼�ɹ�");
    	} else {
    		System.out.println("��¼ʧ��");
    	}
    	
        driver.close();
	}

}
