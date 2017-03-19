package sinaaccount;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mongodb.Jdbc;

import org.bson.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import sleep.Sleep;
import tools.ComUtil;
import tools.LogUtil;
import tools.Login;
import tools.PrintClass;
import tools.SeleniumUtil;
import tvprograms.Program;
import tvprograms.ProgramsUtil;

public class LoginUsersManage {
	List<LoginUser> users;
	int now_index;
	int all_num;
	
	public int getAll_num() {
		return all_num;
	}

	public void setAll_num(int all_num) {
		this.all_num = all_num;
	}

	public LoginUsersManage() {
		users = new ArrayList<LoginUser>();
		now_index = -1;
		//在mongodb中找出登录账号与密码
		List<Document> docs = Jdbc.find("loginuser");
		for (int i = 0; i < docs.size(); i++) {
			Document doc = docs.get(i);
            LoginUser user = new LoginUser();
			user.setId(doc.getInteger("id"));
            user.setName(doc.getString("name"));
			user.setPassword(doc.getString("password"));
			user.setSafe_level(doc.getInteger("safelevel"));
			user.setCare_enough(doc.getString("careenough"));
			user.setToday_care_cnt(doc.getInteger("today_care_cnt", 0));
			Integer tmp_num = doc.getInteger("total_need_care_cnt");
			if (tmp_num != null) {
				user.setTotal_need_care_cnt(tmp_num);
			}
			String str_need_care_program_set = doc.getString("str_need_care_program_set");
			if (str_need_care_program_set != null && 
				(!str_need_care_program_set.equals(""))){
				String[]str_set = str_need_care_program_set.split(ComUtil.SPLITFLAG);
				Set<String> tmp_set = new HashSet<String>();
				for (String  str : str_set){
					if (str.equals("")) {
						continue;
					}
					tmp_set.add(str);
				}
				user.setNeed_care_program_set(tmp_set);
			}
			users.add(user);
            //System.out.println(mongoCursor.next());  
			//System.out.println(user);
		}
		all_num = users.size();
	}
	
	/**
	 * 得到当前用户
	 * @return
	 */
	public LoginUser getNowUser() {
		return users.get(now_index);
	}
	
	
	/**
	 * 得到下一个登录用户
	 * @return
	 */
	public LoginUser next() {
		now_index++;
		return users.get(now_index);
	}
	
	/**
	 * 判断是否还有下一个用户
	 * @return
	 */
	public boolean hasNext() {
		if (now_index < users.size() - 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * 得到users[id]
	 * @param id
	 * @return
	 */
	public LoginUser getUserByIndex(int id) {
		return users.get(id);
	}
	
	public List<LoginUser> getUsersBySafeLevel(int... safe_level) {
		List<LoginUser> safe_level_users = new ArrayList<LoginUser>();
		for(LoginUser user : users) {
			for (int level: safe_level){
				if (level == user.getSafe_level()) {
					safe_level_users.add(user);
					break;
				}
			}
		}
		return safe_level_users;
	}
	
	private static void FIRST_INIT_GET_USER_CARE_PROGRAM(int safe_level) {
		//get tv program
		List<Program> programs = ProgramsUtil.getProgramList();
		Set<String> need_care_program_sina_id = new HashSet<String>();
		for (Program program : programs) {
			need_care_program_sina_id.add(program.getSina_id());
		}
		LoginUsersManage manage = new LoginUsersManage();
		List<LoginUser> users = manage.getUsersBySafeLevel(safe_level);
		Login login = new Login();
		WebDriver driver = SeleniumUtil.getDriver();
		for (LoginUser user : users) {
			//debug
//			if (user.getId() < 25) {
//				continue;
//			}
			if (user.getTotal_need_care_cnt() == 0) {
				continue;
			}
			Set<String> havecare = new HashSet<String>();
			driver.manage().deleteAllCookies();
			login.login(user, driver);
			
			Sleep.sleep(100000);
			List<WebElement> divs = driver.findElements(By.xpath("//*[@node-type='follow']"));
			if (divs.size() != 0) {
				WebElement div = divs.get(0);
				
				try {
					div.click();//
				} catch(Exception e) {
					System.out.println("can not click " + user.getId());
					continue;
				}
				
				Sleep.sleep(1000);
				while (true) {
				
					List<WebElement> care_programs = driver.findElements(By.xpath("//*[@class='member_wrap clearfix']"));
					for (int i = 0; i < care_programs.size(); i++) {
						String sina_name = null;
						String sina_id = null;
						WebElement care_program = care_programs.get(i);
						List<WebElement> ele_care_program_names = care_program.findElements(By.className("S_txt1"));
						if (ele_care_program_names.size() != 0) {
							WebElement ele_care_program_name = ele_care_program_names.get(0);
							if (ele_care_program_name == null) {
								continue;
							}
							String usercard = ele_care_program_name.getAttribute("usercard");
							
							sina_name = ele_care_program_name.getText();
							if (null == usercard) {
								continue;
							}
							sina_id = usercard.substring(usercard.indexOf("=") + 1);
							//System.out.println(program_name + " " + program_id);
							if (need_care_program_sina_id.contains(sina_id)) {
								havecare.add(sina_id);
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
			}
			
			Set<String> need_care = new HashSet<String>();
			int total_need_care_cnt = 0;
			for (Program program : programs) {
				if (!havecare.contains(program.getSina_id())) {
					need_care.add(program.getTv_name());
					total_need_care_cnt++;
				}
			}
			user.setNeed_care_program_set(need_care);
			user.setTotal_need_care_cnt(total_need_care_cnt);
			user.setTotal_need_care_cnt(need_care.size());
			Document filter = new Document().append("name", user.getName());
			Jdbc.UpdateOneByKey_Vaule("loginuser", filter, user.toDoc());
			System.out.println(user.getId() + "get care program set!");
			//System.out.println(PrintClass.outObjPropertyString(user));
			//LogUtil.debug(PrintClass.outObjPropertyString(user));
		}
		System.exit(0);
	}
	
	public static void main(String args[]) throws InterruptedException {
		FIRST_INIT_GET_USER_CARE_PROGRAM(1);
	}
}

