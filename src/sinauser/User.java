package sinauser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.bson.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import sleep.Sleep;
import sleep.WebLoad;


public class User {
	
	final String SPLITFLAG = "###";
	String user_id;
	String user_name;
	String user_summary;
	
	String residence;
	String birthday;
	String register_time;
	
	String education_info;
	
	ArrayList<String> tags;
	
	String level;
	int sex;
	
	int fans_num;
	int weibo_num;
	int attention_num;
	
	boolean is_master;
	ArrayList<String> master_tags;
	ArrayList<String> care_programs;
	public void docToUser(Document userdoc) {
		user_id = userdoc.getString("id");
		user_name = userdoc.getString("name");
		user_summary = userdoc.getString("summary");
		residence = userdoc.getString("residence");
		birthday = userdoc.getString("birthday");
		sex = userdoc.getInteger("sex", -1);
		level = userdoc.getString("level");
		fans_num = userdoc.getInteger("fans_num", -1);
		weibo_num = userdoc.getInteger("weibo_num", -1);
		attention_num = userdoc.getInteger("attention_num", -1);
		String str_tags = userdoc.getString("tags");
		String [] tmp_tags = str_tags.split(SPLITFLAG);
		for (int i = 0; i < tmp_tags.length; i++) {
			tags.add(tmp_tags[i]);
		}
	}
	
	/**
	 * 将user的信息转化为Document文档
	 * @return
	 */
	public Document getUserDoc(String come_from) {
		Document user_doc = new Document();
		user_doc.append("come_from", come_from);
		user_doc.append("id", user_id);
		user_doc.append("name", user_name);
		user_doc.append("summary", user_summary);
		user_doc.append("residence", residence);
		user_doc.append("birthday", birthday);
		//user_doc.append("education_info", education_info);
		user_doc.append("level", level);
		user_doc.append("sex", sex);
		user_doc.append("fans_num", fans_num);
		user_doc.append("weibo_num", weibo_num);
		user_doc.append("attention_num", attention_num);
		String str_tags = "";
		for (int i = 0; i < tags.size(); i++) {
			str_tags += tags.get(i) + SPLITFLAG;
		}
		user_doc.append("tags", str_tags);
		String str_care_programs = "";
		for (int i = 0; i < care_programs.size(); i++) {
			str_care_programs += care_programs.get(i) + SPLITFLAG;
		}
		user_doc.append("care_programs", str_care_programs);
		return user_doc;
	}
	
	public User() {
		tags = new ArrayList<String>();
		care_programs = new ArrayList<String>();
		residence = null;
		birthday = null;
		register_time = null;
		sex = -1;//表示没有
		is_master = false;
	}
	
	/**
	 * 将page上的信息填充到用户的信息中去
	 * @param driver
	 * @param flag_last 
	 * @param program_name 
	 * @param id_link_program 
	 * @return
	 */
	public boolean SetUserByPage(WebDriver driver, String src, String id, boolean flag_last, String program_name, Map<String, String> id_link_program) {
		//TODO  要重新写下判断的方法
		int wait_time = 6;
		if (flag_last) {
			wait_time = 300;
		}
		driver.manage().timeouts().pageLoadTimeout(wait_time, TimeUnit.SECONDS);//
		try{
			driver.get(src);
		} catch(Exception e) {
			System.out.println("详细信息页面" + id + "加载时间过长");
		}
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//if (!WaitWebLoad.load(driver, new WaitUserInfoPage())) {
        //	return false;
        //}
		try {
			
		user_id = id;
		WebElement e_pf_username = (driver.findElement(By.className("pf_username")));
		if(null == e_pf_username) {
			return false;
		}
		WebElement e_username = e_pf_username.findElement(By.className("username"));
		if (e_username != null) {
			user_name = e_username.getText();
		}
		WebElement e_usersummary = driver.findElement(By.className("pf_intro"));
		if (e_usersummary != null) {
			user_summary = e_usersummary.getAttribute("title");
		}
		
		List<WebElement> e_nums = driver.findElements(By.className("W_f18"));
		for (int i = 0; i < e_nums.size(); i++) {
			if (1 == i) {
				fans_num = Integer.parseInt(e_nums.get(i).getText());
			} 
			if (2 == i) {
				weibo_num = Integer.parseInt(e_nums.get(i).getText());
			} 
			if (0 == i) {
				attention_num = Integer.parseInt(e_nums.get(i).getText());
			} 
		}
		
		//等级信息
		List<WebElement> e_tmp_levels = driver.findElements(By.xpath("//*[@class='level_box S_txt2']"));
		String str_level = null;
		if (e_tmp_levels.size() != 0) {
			str_level = e_tmp_levels.get(0).getText();
			//System.out.println(str_level);
			str_level = str_level.substring(0, 5);
		}
		
		if(null != str_level) {
			level = str_level;
		}
		List<WebElement> e_divs = driver.findElements(By.xpath("//*[@class='PCD_text_b PCD_text_b2']"));
		for (int i = 0; i < e_divs.size(); i++) {
			WebElement tmpe = e_divs.get(i);
			List<WebElement> e_tmp_names = tmpe.findElements(By.className("obj_name"));//不能含空格。？
			String tmp_name = null;
			if (e_tmp_names.size() > 0) {
				tmp_name = e_tmp_names.get(0).getText();
			}
			//System.out.println(tmp_name);
			if (tmp_name.equals("基本信息")) {
			
			} else if (tmp_name.equals("标签信息")) {
				
			} else if (tmp_name.equals("工作信息")) {
				
			} else if (tmp_name.equals("教育信息")) {
				
			}
		}
		List<WebElement> e_quotes = driver.findElements(By.xpath("//*[@class='li_1 clearfix']"));
		for (int i = 0; i < e_quotes.size(); i++) {
			String little_info = e_quotes.get(i).getText();
			//System.out.println(little_info);
			String title = little_info.substring(0, little_info.indexOf("："));
			String testquote = "：";
			//System.out.println(testquote.length());
			String content = little_info.substring(little_info.indexOf("：") + testquote.length());
			//System.out.println(title);
			//System.out.println("|| " + content);
			
			if (title.equals("所在地")) {
				residence = content;
			}
			if (title.equals("生日")) {
				birthday = content;
			}
			if (title.equals("注册时间")) {
				register_time = content;
			}
			if (title.equals("标签")) {
				String[] tmp_tags = content.split("\n");
				for (int j = 1; j < tmp_tags.length; j++) {
					tags.add(tmp_tags[j]);
				}
			}
			if (title.equals("性别")) {
				if (content.indexOf("男") != -1) {
					sex = 1;
				}
				if (content.indexOf("女") != -1) {
					sex = 0;
				}
			}
		}
		
		} catch(Exception e) {
			return false;
		}
		
		setUserCareProgram(driver, program_name, id, id_link_program);
		return false;
	}
	
	/**
	 * 读取用户关注用户信息
	 * @param driver
	 * @param program_name
	 * @param id
	 * @param id_link_program
	 */
	private void setUserCareProgram(WebDriver driver, String program_name,
			String id, Map<String, String> id_link_program) {
		String url = "http://weibo.com/p/100505" +
					 id + 
					 "/follow?relate=same_follow&from=rel&wvr=5&loc=bothfollow#place";
		driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
		try {
			driver.get(url);
		} catch(Exception e) {
		}
		
		//不中断
		Sleep.sleep(2000);
		care_programs.add(program_name);
		List<WebElement> follows = driver.findElements(By.xpath("//*[@class='follow_item S_line2']"));
		for (int i = 0; i < follows.size(); i++) {
			WebElement follow = follows.get(i);
			String action_data = follow.getAttribute("action-data");
			String uid = action_data.substring(action_data.indexOf("=") + 1,action_data.indexOf("&"));
			//System.out.println("setUserCareProgram: " + uid);			
			if (id_link_program.containsKey(uid)) {
				if (!care_programs.contains(id_link_program.get(uid))) {
					care_programs.add(id_link_program.get(uid));
				}
			}
		}
	}
	
	/**
	 * 
	 * @author chenjiashou
	 *
	 */
	class WaitUserInfoPage extends	WebLoad {

		@Override
		public boolean is_success(WebDriver driver) {
			List<WebElement> elms = driver.findElements(By.xpath("//*[@class='main_title W_fb W_f14']"));
			//System.out.println("\nlalala:" + elms.size());
			if (elms.size() < 2) {
				return false;
			}
			return true;
		}
	}
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_summary() {
		return user_summary;
	}
	public void setUser_summary(String user_summary) {
		this.user_summary = user_summary;
	}
	public String getResidence() {
		return residence;
	}
	public void setResidence(String residence) {
		this.residence = residence;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getRegister_time() {
		return register_time;
	}
	public void setRegister_time(String register_time) {
		this.register_time = register_time;
	}
	public String getEducation_info() {
		return education_info;
	}
	public void setEducation_info(String education_info) {
		this.education_info = education_info;
	}
	public ArrayList<String> getTags() {
		return tags;
	}
	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getFans_num() {
		return fans_num;
	}
	public void setFans_num(int fans_num) {
		this.fans_num = fans_num;
	}
	public int getWeibo_num() {
		return weibo_num;
	}
	public void setWeibo_num(int weibo_num) {
		this.weibo_num = weibo_num;
	}
	public int getAttention_num() {
		return attention_num;
	}
	public void setAttention_num(int attention_num) {
		this.attention_num = attention_num;
	}
	public boolean isIs_master() {
		return is_master;
	}
	public void setIs_master(boolean is_master) {
		this.is_master = is_master;
	}
	public ArrayList<String> getMaster_tags() {
		return master_tags;
	}
	public void setMaster_tags(ArrayList<String> master_tags) {
		this.master_tags = master_tags;
	}
	
}

