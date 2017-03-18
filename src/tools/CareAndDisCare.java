package tools;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mongodb.Jdbc;

import org.bson.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.server.SeleniumDriverResourceHandler;

import sleep.Sleep;

public class CareAndDisCare {
	static Map<String, String> id_link_program;
	
	/**
	 * 加载uid 与  program_name
	 */
	private static void getIdLinkProgram() {
		id_link_program = new HashMap<String, String>();
		List<Document> docs = Jdbc.find("program_info");
		for (int i = 0; i < docs.size(); i++) {
			Document doc = docs.get(i);
			String program_name = doc.getString("program_name");
			String uid = doc.getString("uid");
			id_link_program.put(uid, program_name);
		}
	}
	
	static void disCare(String user_name) {
		getIdLinkProgram();
		
		WebDriver driver = SeleniumUtil.getDriver();
		Login login = new Login();
		List<Document> docs = Jdbc.find("loginuser", new Document().append("name", user_name));
		if (docs.size() != 0) {
			Document doc = docs.get(0);
			String psw = doc.getString("password");
			login.login(user_name, psw, 0, driver);
			Sleep.sleep(1000);
			List<WebElement> divs = driver.findElements(By.xpath("//*[@node-type='follow']"));
			if (divs.size() != 0) {
				
				WebElement div = divs.get(0);
				div.click();
				Sleep.sleep(1000);
				while (true) {
					
					List<WebElement> care_programs = driver.findElements(By.xpath("//*[@class='member_wrap clearfix']"));
					for (int i = 0; i < care_programs.size(); i++) {
						String program_name = null;
						String program_id = null;
						WebElement care_program = care_programs.get(i);
						List<WebElement> ele_care_program_names = care_program.findElements(By.className("S_txt1"));
						if (ele_care_program_names.size() != 0) {
							WebElement ele_care_program_name = ele_care_program_names.get(0);
							program_name = ele_care_program_name.getText();
							String usercard = ele_care_program_name.getAttribute("usercard");
							program_id = usercard.substring(usercard.indexOf("=") + 1);
							//System.out.println(program_name + " " + program_id);
							//然后把不在数据库内的数据记录并取消关注
							if (!id_link_program.containsKey(program_id)) {
								//System.out.println("not in" + program_name + " " + program_id);
								Document need_discare = new Document();
								need_discare.append("user_name", user_name);
								need_discare.append("program_id", program_id);
								need_discare.append("prgoram_name", program_name);
								//Jdbc.insert_doc("SAVEFRIENDINFO", need_discare);
								//然后取消关注
								
							}
						}
					}
					//break;
					
					List<WebElement> nexts = driver.findElements(By.xpath("//*[@class='page next S_txt1 S_line1']"));
					if (nexts.size() != 0) {
						nexts.get(0).click();
						Sleep.sleep(500);
					} else {
						break;	
					}
					
				}
				
				//然后就是取消关注
				/*
				while (true) {
					int flag = 0;
					List<WebElement> batsselects = driver.findElements(By.xpath("//*[@action-type='batselect']"));
					if (batsselects.size() != 0) {
						WebElement batsselect = batsselects.get(0);
						batsselect.click();
						Sleep.sleep(500);
						List<WebElement> care_programs = driver.findElements(By.xpath("//*[@class='member_li S_bg1']"));
						for (int i = 0; i < care_programs.size(); i++) {
							String program_name = null;
							String program_id = null;
							WebElement care_program = care_programs.get(i);
							List<WebElement> ele_care_program_names = care_program.findElements(By.className("S_txt1"));
							if (ele_care_program_names.size() != 0) {
								WebElement ele_care_program_name = ele_care_program_names.get(0);
								program_name = ele_care_program_name.getText();
								String usercard = ele_care_program_name.getAttribute("usercard");
								program_id = usercard.substring(usercard.indexOf("=") + 1);
								Document find_doc = new Document();
								find_doc.append("user_name", user_name);
								find_doc.append("program_id", program_id);
								List<Document> find_docs = Jdbc.find("SAVEFRIENDINFO", find_doc);
								if (find_docs.size() != 0) {
									//需要删除
									System.out.println(program_name + " " + program_id);
									WebElement markbtn = care_program.findElement(By.className("markup_choose"));
									if (markbtn != null) {
										markbtn.click();
										flag = 1;
										Sleep.sleep(200);
									}
								}
							}
						}
						while (true) {
							List<WebElement> discare_btns = driver.findElements(By.xpath("//*[@node-type='cancelFollowBtn']"));
							if (discare_btns.size() != 0) {
								discare_btns.get(0).click();
								List<WebElement> oks = driver.findElements(By.xpath("//*[@class='W_btn_a btn_34px']"));
								if (oks.size() != 0) {
									oks.get(oks.size() - 1).click();
									Sleep.sleep(500);
								} else {
									break;
								}
								
							}
						}
					} else {
						break;
					}
					if (flag == 1) {
						continue;
					}
					List<WebElement> nexts = driver.findElements(By.xpath("//*[@class='page next S_txt1 S_line1']"));
					if (nexts.size() != 0) {
						nexts.get(0).click();
						Sleep.sleep(500);
					} else {
						break;	
					}
				}
				*/
			}
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		disCare("18654689276");
		System.exit(0);
	}

}

