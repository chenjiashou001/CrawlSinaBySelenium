package tools;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import cn.edu.hfut.dmic.webcollector.net.HttpRequest;
import cn.edu.hfut.dmic.webcollector.net.HttpResponse;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.Cookie;

import cookie.CookieUtil;
import sinaaccount.LoginUsersManage;
import sleep.WaitWebLoad;
import sleep.WebLoad;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
/**
 * �����¼����
 * @author chenjiashou
 *
 */
public class Login {
	final int MAX_TRY_TIME = 10;
	
	/**
	 * �����¼�û���Ϣ��û��cookie��driver
	 * @param username
	 * @param password
	 * @param safe_level
	 * @param driver
	 * @return ���ش���cookie��Ϣ��driver
	 */
	public boolean login(String username, String password, int safe_level, WebDriver driver) {
		int try_cnt = 0;
		driver.manage().timeouts().pageLoadTimeout(1000, TimeUnit.SECONDS);
		while(try_cnt < MAX_TRY_TIME) {
			try_cnt++;
//			System.out.println("logn:" + try_cnt);
			sleep(500);
			String tmpcookie = null;
			boolean exception_flag = false;
			try{
				tmpcookie = concatCookie(driver);
			} catch(Exception e) {
//				System.out.println("concatCookie�쳣");
				exception_flag = true;
				//continue;
			}
			
			if (exception_flag) {
				
				try{
					driver.manage().deleteAllCookies();
					//driver.get("http://weibo.com/login.php");
				} catch(Exception e) {
					//System.out.println("concatcookie�쳣����login��ʱ");
					driver.close();
					driver = SeleniumUtil.getDriver();
//					System.out.println("concatCookie�쳣 ���س�ʱ");
				}
				continue;
			}
			
//			System.out.println("cookie = " + tmpcookie);
			if (tmpcookie != null && tmpcookie.indexOf("SSOLoginState") != -1) {
				return true;//�ɹ���¼
			}
				//if (!WaitWebLoad.load(driver, new WaitLogin())) {
		        //	return false;
		        //}
				try{				//driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
					
					driver.get("http://weibo.com/login.php");
				} catch(Exception e) {
//					System.out.println("pro1");
				}
				sleep(1000);
				
				List<WebElement> elms = null;
				List<WebElement> verify_img = null;
		        try{
					List<WebElement> lists = driver.findElements(By.xpath("//*[@name='username']"));
			        if (lists.size() == 0) {
			        	continue;	
			        }
			        lists.get(0).sendKeys(username);
			        sleep(500);
			        WebElement psw = driver.findElement(By.name("password"));
			        if (psw == null) {
			        	continue;
			        }
			        psw.sendKeys(password);
			        sleep(1500);
			        elms = driver.findElements(By.xpath("//*[@class='W_btn_a btn_32px']"));
			        verify_img = driver.findElements(By.xpath("//*[@node-type='verifycode_image']"));
		        } catch(Exception e) {
//		        	System.out.println("ҳ��Ԫ�ض�λ�쳣");
		        	continue;
		        }
	        	if (verify_img.size() != 0 && !verify_img.get(0).getAttribute("src").equals("about:blank")) {//�ж���֤���Ƿ�ˢ����
	        		String src = verify_img.get(0).getAttribute("src");
	        		try {
	        		String cookie = concatCookie(driver);
	        		System.out.println(src);
	        		HttpRequest request = new HttpRequest(src);
        	        request.setCookie(cookie);
        	        
					HttpResponse response = request.getResponse();
        	        ByteArrayInputStream is = new ByteArrayInputStream(response.getContent());
        	        BufferedImage img = ImageIO.read(is);
        	        is.close();
	        		String userInput = new CaptchaFrame(img).getUserInput();
	        		List<WebElement> verify_input = driver.findElements(By.xpath("//*[@node-type='verifycode']"));
	        		if (verify_input.size() == 0) {
//	        			System.out.println("û���ҵ���֤�������");
	        			continue;
	        		}
	                verify_input.get(0).sendKeys(userInput);
	                } catch (Exception e) {
//	                	System.out.println("pro2");
	                }
	        	}
	        	if (elms.size() == 0) {
//	        		System.out.println("��¼��ťû�м��س���");
	        		continue;
	        	}
	        	sleep(500);
	       try{
	        	elms.get(0).click();
	        	sleep(4000);
	        	//if (!WaitWebLoad.load(driver, new WaitSuccessLogin())) {
		        //	continue;
		        //} else {
		        	//return true;
		        //}
	        } catch (Exception e) {
//				System.out.println("pro3");
				//e.printStackTrace();
			}
        }
		return false;
	}
	
    public static String concatCookie(WebDriver driver) throws Exception {
    	String result = null;

        Set<Cookie> cookieSet = driver.manage().getCookies();
        StringBuilder sb = new StringBuilder();
        for (Cookie cookie : cookieSet) {
            sb.append(cookie.getName() + "=" + cookie.getValue() + ";");
        }
        result = sb.toString();

        return result;
    }
	
	public static class CaptchaFrame {

        JFrame frame;
        JPanel panel;
        JTextField input;
        int inputWidth = 100;
        BufferedImage img;
        String userInput = null;

        public CaptchaFrame(BufferedImage img) {
            this.img = img;
        }

        public String getUserInput() {
            frame = new JFrame("������֤��");
            final int imgWidth = img.getWidth();
            final int imgHeight = img.getHeight();
            int width = imgWidth * 2 + inputWidth * 2;
            int height = imgHeight * 2+50;
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            int startx = (dim.width - width) / 2;
            int starty = (dim.height - height) / 2;
            frame.setBounds(startx, starty, width, height);
            Container container = frame.getContentPane();
            container.setLayout(new BorderLayout());
            panel = new JPanel() {
                @Override
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(img, 0, 0, imgWidth * 2, imgHeight * 2, null);
                }
            };
            panel.setLayout(null);
            container.add(panel);
            input = new JTextField(6);
            input.setBounds(imgWidth * 2, 0, inputWidth, imgHeight * 2);
            panel.add(input);
            JButton btn = new JButton("��¼");
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    userInput = input.getText().trim();
                    synchronized (CaptchaFrame.this) {
                        CaptchaFrame.this.notify();
                    }
                }
            });
            btn.setBounds(imgWidth * 2 + inputWidth, 0, inputWidth, imgHeight * 2);
            panel.add(btn);
            frame.setVisible(true);
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            frame.dispose();
            return userInput;
        }
    }
	
	
	/**
	 * ����cookie������£��л���¼�û�����ֹ�˺ű��⡣
	 * @param driver
	 * @param login_name
	 * @return
	 */
	public boolean switchLogin(WebDriver driver, String login_name) {
		driver.manage().deleteAllCookies();	
		driver.manage().timeouts().pageLoadTimeout(200, TimeUnit.SECONDS);
		while (true) {
			try {
				driver.get("http://weibo.com/login.php");
				break;
			} catch (Exception e) {
				System.out.println("loginҳ�����ʱ���");
			}
		}
		//if (!WaitWebLoad.load(driver, new WaitLogin())) {
        //	return false;
        //}
		
		
		int try_cnt = 0;
		while(try_cnt < MAX_TRY_TIME) {
			try_cnt++;
			try {
				CookieUtil.get(login_name, driver);
				driver.get("http://weibo.com/login.php");
				return true;
			} catch (Exception e) {
				System.out.println("����ʱ���");
				e.printStackTrace();
				//if (!WaitWebLoad.load(driver, new WaitSuccessLogin())) {
		        //	continue;
		        //} else {
		        //	return true;
		        //}
				return true;
			}
		}
		return true;
	}
	
	/**
	 * �߳�����s��
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
	class WaitSuccessLogin extends	WebLoad {

		@Override
		public boolean is_success(WebDriver driver) {
			List<WebElement> elms = driver.findElements(By.xpath("//*[@class='W_ficon ficon_set S_ficon']"));
			//System.out.println("\nlalala:" + elms.size());
			if (elms.size() == 0) {
				return false;
			}
			return true;
		}
	}
}
