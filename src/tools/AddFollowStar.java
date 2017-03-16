package tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mongodb.Jdbc;

import org.bson.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import sleep.Sleep;

/**
 * 为用户添加粉丝
 * @author chenjiashou
 *
 */
public class AddFollowStar {
	static String PATH_PROGRAM_FILE = "C:\\Users\\chenjiashou\\Desktop\\ALLSAVE";
	
	static List<String> need_follow_program;
	static List<Integer> program_watchtime;
	static Map<String, String> program_id;
	
	static int begin_user_index = 10;
	static int end_user_index = 10;
	
	/**
	 * 对字符串进行二次编码
	 * @param tv_name
	 * @return
	 */
	static String getSearchUrl(String program_name) {
		String encode_str = UrlUtil.getURLEncoderString(program_name);
		encode_str = UrlUtil.getURLEncoderString(encode_str);
		return "http://s.weibo.com/user/&nickname=" + encode_str + "&auth=org_vip";
	}
	
	public static void main(String[] args) {
		getProgramInfo();
		StartAddFollow(begin_user_index, end_user_index);
		System.exit(0);
	}

	private static void StartAddFollow(int beg_id, int end_id) {
		program_id = new HashMap<String, String>();
		for (int i = beg_id; i <= end_id; i++) {
			WebDriver driver = SeleniumUtil.getDriver();
			SeleniumUtil.getAndSaveUserCookie(driver, i, i);
			
			sleep(5000);
			
			OneUserAddFollow(driver);
			driver.close();
		}
	}

	private static void saveProgramId(int index, String uid, String name) {
		Document doc = new Document();
		doc.append("program_name", need_follow_program.get(index));
		List<Document> docs = Jdbc.find("program_info", doc);
		if (docs.size() != 0) {
			return ;
		}
		doc.append("watch_time", program_watchtime.get(index));
		doc.append("uid", uid);
		doc.append("sina_program_name", name);
		Jdbc.insert_doc("program_info", doc);
	}

	private static void OneUserAddFollow(WebDriver driver) {
		for (int i = 0; i < need_follow_program.size(); i++) {
			System.out.println("i =" + i);

			String search_name = need_follow_program.get(i);
			driver.get(getSearchUrl(search_name));
			sleep(15888);
			List<WebElement> divs = driver.findElements(By.xpath("//*[@class='list_person clearfix']"));
			boolean have_find = false;
			for (int j = 0; j < Math.min(divs.size(), 3); j++) {
				sleep(8233);
				Sleep.rand_time(5000, 10000);
				String uid = null;
				String name = null;
				WebElement div = divs.get(j);
				//uid
				List<WebElement> uids = div.findElements(By.className("W_face_radius"));
				if (uids.size() != 0) {
					uid = uids.get(0).getAttribute("uid");
				}
				//System.out.println("uid = " + uid);
				
				//name
				List<WebElement> names = div.findElements(By.className("person_name"));
				if (names.size() != 0) {
					name = names.get(0).getText();
				}
				//System.out.println("name = " + name);
				
				if (is_not_match_programame(name, search_name)) {
					continue;
				}
				have_find = true;
				//follow!
				List<WebElement> buttons = driver.findElements(By.className("person_adbtn"));
				if (buttons.size() != 0) {
					//saveProgramId(i, uid, name);
					buttons.get(0).click();
					sleep(10000);
					//这里还要处理
					List<WebElement> submits = driver.findElements(By.xpath("//*[@action-type='submit']"));
					if (submits.size() != 0) {
						submits.get(0).click();
					}
					//sleep(1000000);
					sleep(5000);
				}
				break;
			}
		}
	}

	private static boolean is_not_match_programame(String name,
			String search_name) {
		if (null == name || name.indexOf(search_name) == -1) {
			return true;
		}
		return false;
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
	
	private static void getProgramInfo() {
		
		//need_follow_program = FileUtil.readLogByList(PATH_PROGRAM_FILE);
		need_follow_program = new ArrayList<String>();
		program_watchtime = new ArrayList<Integer>();
		List<Document> docs = Jdbc.find("program_info");
		for (int i = 0; i < docs.size(); i++) {
			need_follow_program.add(docs.get(i).getString("program_name"));
			program_watchtime.add(docs.get(i).getInteger("watch_time"));
		}
		/*
		List<String> temp_list = new ArrayList<String>();
		program_watchtime = new ArrayList<Integer>();
		int _index = 0;
		for (int i = 0; i < need_follow_program.size(); i++) {
			String temp_str = need_follow_program.get(i).split(" ")[0];
			//String watchtime = need_follow_program.get(i).split(" ")[1];
			if (is_in_blackprogram(temp_str)) {
				continue;
			}
			temp_str = formatProgramName(temp_str);
			temp_list.add(temp_str);
			
			_index++;
			System.out.println(temp_list.get(_index - 1));
		}
		need_follow_program = temp_list;
		*/
	}
	
	private static boolean is_in_blackprogram(String temp_str) {
		String black_program[] = {
			"null",
			"结束",
			"电影",
		};
		if (is_word_in_array(temp_str, black_program)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断temp_str是否在数组中
	 * @param temp_str
	 * @param black_program
	 * @return
	 */
	private static boolean is_word_in_array(String temp_str,
			String[] black_program) {
		for (int i = 0; i < black_program.length; i++) {
			if (black_program[i].equals(temp_str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 格式化名字
	 * @param temp_str
	 * @return
	 */
	private static String formatProgramName(String temp_str) {
		
		return temp_str;
	}

}

