package analyze;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import mongodb.Jdbc;

import org.bson.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import cookie.CookieUtil;
import sinaaccount.LoginUsersManage;
import sinauser.User;
import sleep.Sleep;
import tools.Login;
import tools.ProgramInfoUtil;
import tools.SeleniumUtil;
import tools.UrlUtil;

public class AnaylzeTvProgram {

	static String [] program_name = {
			"starroad_user",//0
			"happycamp_user",//1
			"sanshensanshi_user",//2
			"xingjingduizhang_user",//3
			"我是歌手",//4
			"NBA",//5
			"非诚勿扰",//6
			"军事记实",//7
			"中国之星",//8
			"随机抽样",//9
	};
	
	static String [] program_url = {
			"http://s.weibo.com/weibo/%25E6%2598%259F%25E5%2585%2589%25E5%25A4%25A7%25E9%2581%2593&region=custom:34:1000&typeall=1&suball=1&page=",
			"http://s.weibo.com/weibo/%25E5%25BF%25AB%25E4%25B9%2590%25E5%25A4%25A7%25E6%259C%25AC%25E8%2590%25A5&region=custom:34:1&typeall=1&suball=1&page=",
			"http://s.weibo.com/weibo/%25E4%25B8%2589%25E7%2594%259F%25E4%25B8%2589%25E4%25B8%2596&region=custom:34:1&typeall=1&suball=1&timescope=custom:2016-02-01-0:2017-02-27-4&page=",
			"http://s.weibo.com/weibo/%25E5%2588%2591%25E8%25AD%25A6%25E9%2598%259F%25E9%2595%25BF&b=1&page=",
			"http://s.weibo.com/weibo/%25E6%2588%2591%25E6%2598%25AF%25E6%25AD%258C%25E6%2589%258B&region=custom:34:1&typeall=1&suball=1&timescope=custom:2016-02-02-1:2017-02-28-0&page=",
			"http://s.weibo.com/weibo/NBA&region=custom:34:1&typeall=1&suball=1&timescope=custom:2016-01-06-4:2017-02-28-0&page=",
			"http://s.weibo.com/weibo/%25E9%259D%259E%25E8%25AF%259A%25E5%258B%25BF%25E6%2589%25B0&region=custom:34:1&typeall=1&suball=1&page=",
			"http://s.weibo.com/weibo/%25E5%2586%259B%25E4%25BA%258B%25E7%25BA%25AA%25E5%25AE%259E&typeall=1&suball=1&timescope=custom:2015-01-01-0:2017-01-05-0&page=",
			"http://s.weibo.com/weibo/%25E4%25B8%25AD%25E5%259B%25BD%25E4%25B9%258B%25E6%2598%259F&typeall=1&suball=1&timescope=custom:2016-01-01-0:2016-03-01-0&page=",
			"http://s.weibo.com/weibo/%25E4%25BA%2586&region=custom:34:1000&typeall=1&suball=1&timescope=custom:2016-03-15-8:2016-03-15-23&page="
	};
	
	static Map<String, String> id_link_program;
	
	static Set<String> program_set;
	static int now_index = 0;
	/**
	 * 0 是 analyzer program_id的用户
	 * 1 是得到program_id的用户并写入mongodb
	 * 2 是写cookie
	 * 3读取数据库中的节目，然后获取用户信息
	 */
	static int FLAG_ANALYZER_OR_GETUSER = 0;
	static int program_id = 9;
	static int begin_index = 0;
	static int end_index = 3;
	
	public static void main(String[] args) {
		getAlreadyProgram();
		if (FLAG_ANALYZER_OR_GETUSER == 1) {
			int day = 1;
			WebDriver driver = SeleniumUtil.getDriver();
			
			getIdLinkProgram();
			
			test_login(driver, begin_index, end_index);
			while (true ) {
				change_url(day);
				day++;
				if (day > 28) {
					break;
				}
				getProgramUser(driver, begin_index, end_index, 50 ,2, program_url[program_id], program_name[program_id]);
				driver.close();
				driver = SeleniumUtil.getDriver();
			}
		} else if (FLAG_ANALYZER_OR_GETUSER == 0) {
			analyzer();
		} else if (FLAG_ANALYZER_OR_GETUSER == 2) {
			//while (true) {
				Jdbc.deleteCollection("cookie");
				WebDriver driver = SeleniumUtil.getDriver();
				SeleniumUtil.getAndSaveUserCookie(driver, begin_index, end_index);
				driver.close();
			//}
		} else if (FLAG_ANALYZER_OR_GETUSER == 3) {
			WebDriver driver = SeleniumUtil.getDriver();
			getIdLinkProgram();
			test_login(driver, begin_index, end_index);
			//测试完之后变为
			now_index = end_index;
			GetMultilProgram(driver, begin_index, end_index);
		}
		System.exit(0);
	}
	
	private static void getAlreadyProgram() {
		program_set = new HashSet<String>();
		List<Document> docs = Jdbc.find("user_program_data");
		for (int i = 0; i < docs.size(); i++) {
			String uid = docs.get(i).getString("id");
			if (!program_set.contains(uid)) {
				program_set.add(uid);
			}
		}
	}

	private static void GetMultilProgram(WebDriver driver, int begin_index2,
			int end_index2) {
		List<String> programs = getProgram();
		for (int i = 0; i < programs.size(); i++) {
			//用一次刷新确定，时间区间，也就是确定url
			String p_name = programs.get(i);
			System.out.println("now program is :" + p_name);
			String p_url = "http://s.weibo.com/weibo/" + 
							UrlUtil.getURLEncoderString(UrlUtil.getURLEncoderString(p_name)) +
							"&typeall=1&suball=1&timescope=custom:2016-02-02-1:2017-03-08-2&page=";
			driver.get(p_url + String.valueOf(1));
			Sleep.sleep(2000);
			List<WebElement> div_pages = driver.findElements(By.xpath("//*[@class='layer_menu_list W_scroll']"));
			int page_num = 0;
			if (div_pages.size() != 0) {
				WebElement div_page = div_pages.get(0);
				List<WebElement> lists = div_page.findElements(By.tagName("li"));
				System.out.println("list size = " + lists.size());
				page_num = lists.size();
			} else {
				//如果
				//continue;
				page_num = 10;
				System.out.println(p_name + "第一页没有加载出来");
			}
			
			int one_step = (page_num > 10) ? page_num / 10 : 1;
			if (one_step == 1) {
				page_num = Math.min(page_num, 10);
			}
			getProgramUser(driver, begin_index2, end_index2, page_num, one_step, p_url, p_name);
			driver.manage().timeouts().pageLoadTimeout(10000, TimeUnit.SECONDS);
			//driver.close();
			//Sleep.sleep(500);
			//driver = SeleniumUtil.getDriver();
		}
	}

	/**
	 * 得到需要抓取的电视节目
	 * @return
	 */
	private static List<String> getProgram() {
		List<Document> docs = ProgramInfoUtil.getToFile();
		//List<Document> docs = Jdbc.find("program_info");
		List<String> programs = new ArrayList<String>();
		for (int i = 166; i < 200; i++) {
			programs.add(docs.get(i).getString("program_name"));
			System.out.println("getProgram " + i + ":" + docs.get(i).getString("program_name"));
		}
		return programs;
	}

	/**
	 * 加载uid 与  program_name
	 */
	private static void getIdLinkProgram() {
		id_link_program = new HashMap<String, String>();
		//List<Document> docs = Jdbc.find("program_info");
		List<Document> docs = ProgramInfoUtil.getToFile();
		for (int i = 0; i < docs.size(); i++) {
			Document doc = docs.get(i);
			String program_name = doc.getString("program_name");
			String uid = doc.getString("uid");
			id_link_program.put(uid, program_name);
		}
	}

	private static void change_url(int day) {
		// TODO Auto-generated method stub
		String str_day = null;
		if (day < 10) {
			str_day = "0" + String.valueOf(day);
		} else {
			str_day = String.valueOf(day);
		}
		String str = "http://s.weibo.com/weibo/%25E6%2598%25AF&region=custom:34:1&typeall=1&suball=1&timescope=custom:2016-03-" +
					 str_day + "-8:2016-03-" +
					 str_day + "-23&page=";
		System.out.println(str);
		program_url[program_id] = str;
	}

	private static void analyzer() {

		//List<Document> docs = Jdbc.find(program_name[program_id]);
		List<Document> docs = Jdbc.find("user_program_data");
		List<User> users = new ArrayList<User>();
		for (int i = 0; i < docs.size(); i++) {
			User user = new User();
			user.docToUser(docs.get(i));
			users.add(user);
		}
		Set<String> user_set = new HashSet<String>();
		
		Map<String, Integer> tags_count = new HashMap<String, Integer>();
		
		for (int i = 0; i < users.size(); i++) {
			User user = users.get(i);
			String id = user.getUser_id();
			if (user_set.contains(id)) {
				continue;
			}
			user_set.add(id);
			
			System.out.println(user.getUser_name());
			System.out.println(user.getUser_summary());
			System.out.println(user.getBirthday());
			for (int j = 0; j < user.getTags().size(); j++) {
				String tag = user.getTags().get(j);
				System.out.print(user.getTags().get(j) + "|");
				if (tags_count.containsKey(tag)){
					int tmp_num = tags_count.get(tag);
					tags_count.remove(tag);
					tags_count.put(tag, tmp_num + 1);
				} else {
					tags_count.put(tag, 1);
				}
			}
			System.out.println("\n --------------------------\n");
		}
		
		List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>();
		list.addAll(tags_count.entrySet());
		if (list.size() == 0) {
			return ;
		}
		
		ValueCmp cmp = new ValueCmp();
		Collections.sort(list, cmp);
		for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, Integer> now = it.next();
			System.out.println(now.getKey() + " " + now.getValue());
		}
		
	}
	
	public static void getProgramUser(WebDriver driver, int begin_index2,
									  int end_index2, int page_num, int one_step,
									  String p_url, String p_name) {
		Login login = new Login();
		int tmp_cnt = 0;
		for (int i = 2; i <= page_num; i += one_step) {
			System.out.println("\n------------------------\npage=" + i + "\n----------------------\n");
			tmp_cnt++;
			if (tmp_cnt % 5 == 0) {
				now_index++;
				if (now_index > end_index2) {
					now_index = begin_index2;
				}
				login.switchLogin(driver, String.valueOf(now_index));
			}
			
			Sleep.sleep(10000);
			
			if (getOnePageUser(i, driver, p_url, p_name) == false) {//在最后一个的时候完全加载。
				break;
			}
		}
	}
	
	public static boolean getOnePageUser(int page_id, WebDriver driver, String program_url, String program_name) {
		String src = program_url;
		src += String.valueOf(page_id);
		driver.manage().timeouts().pageLoadTimeout(10000, TimeUnit.SECONDS);
		int try_cnt = 0;

		driver.get(src);
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<WebElement> users = driver.findElements(By.className("W_face_radius"));
		System.out.println("users.size() = " + users.size());
		List<String> id_users = new ArrayList<String> ();
		if (users.size() == 0) {
			driver.get(src);
			Sleep.sleep(10000);
			users = driver.findElements(By.className("W_face_radius"));
		}
		for (int i = 0; i < users.size(); i++) {
			//System.out.println(users.get(i).getAttribute("usercard"));
			String usercard = users.get(i).getAttribute("usercard");
			if (null == usercard || usercard.indexOf("=") == -1 || usercard.indexOf("&") == -1) {
				continue;
			}
			
			Sleep.sleep(5000);
			
			String user_id = usercard.substring(usercard.indexOf("=") + 1, usercard.indexOf("&"));
			System.out.println(user_id);
			if (program_set.contains(user_id)) {
				continue;
			}
			program_set.add(user_id);
			id_users.add(user_id);
		}
		
		for (int i = 0; i < id_users.size(); i++) {
			String url = "http://weibo.com/p/100505" + id_users.get(i) + "/info?mod=pedit_more";
			User user = new User();
			boolean flag_last = false;
			if (i == id_users.size() - 1) {
				flag_last = true;
			}
			user.SetUserByPage(driver, url, id_users.get(i), flag_last, program_name, id_link_program);
			
			Jdbc.insert_doc("user_program_data", user.getUserDoc(program_name));
		}
		
		return true;
	}
	
	private static void test_login(WebDriver driver, int b, int d) {
		LoginUsersManage manage = new LoginUsersManage();
		Login login = new Login();
		driver.get("http://weibo.com/login.php");
		
		for (int i = b; i <= d; i++) {
			if (login.switchLogin(driver, String.valueOf(i)) ) {
				System.out.println("切换用户" + String.valueOf(i) + "成功");
			}
		}
	}

	private static void sleep(int i) {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

class ValueCmp implements 
	Comparator<Map.Entry<String, Integer>> {
	
	@Override
	public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
		return o1.getValue().compareTo(o2.getValue());
	}
}
