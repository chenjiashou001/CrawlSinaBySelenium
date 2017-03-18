package tvprograms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mongodb.Jdbc;

import org.bson.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import sinaaccount.LoginUser;
import sinaaccount.LoginUsersManage;
import sleep.Sleep;
import tools.AddFollowStar;
import tools.ComUtil;
import tools.FileUtil;
import tools.LogUtil;
import tools.Login;
import tools.SeleniumUtil;

public class ProgramsUtil {
	public static final String COMPLETE_PROGRAM_FILE_PATH = "Data/program_info.txt";
	public static final String BASE_PROGRAM_FILE_PATH = "Data/TvProgramNeedCare.txt";
	private static List<Program> _programs = null;
	private static Map<String, Integer> _map_name_id = null;
	private static int _programs_size = -1;
	private static String [] _BLACK_TV_PROGRAM_NAME= {"雪豹", "奋斗", "口述", "非洲", "秦始皇", "爱情公寓", 
			"生命", "一线", "生活圈", "纪录片", "悬崖", "春节", "见证", "功夫", "疯猴", "剧场", "再见", "等着我",
			"黑猩猩", "专题", "过年", "夕阳红", "来玩吧", "传家", "传奇", "乡土", "经典剧场", "过年啦", 
			"天天健康", null};
	
	public static int getIdByTvProgramName(String program_name) {
		init();
		if (_map_name_id.containsKey(program_name)) {
			return _map_name_id.get(program_name);
		} else {
			return -1;
		}
	}
	
	public static int get_programs_size() {
		init();
		return _programs_size;
	}

	private static void loadProgramsByFile(String path) {
		if (-1 != _programs_size) {//已经加载过就不管了
			return;
		}
		_programs = new ArrayList<Program>();
		_map_name_id = new HashMap<String, Integer>();
		List<Document> doc_programs = FileUtil.readDocsFromFile(path);
		int program_index = 0;
		for (int i = 0; i < doc_programs.size(); i++) {
			Document doc_program = doc_programs.get(i);
			String tv_program_name = doc_program.getString("program_name");  
			if (ComUtil.isStringInArray(tv_program_name, _BLACK_TV_PROGRAM_NAME)) {
				continue;
			}
			//将document中的属性全部加载到class program中
			Program program = new Program(doc_program);
			program.setId(program_index);
			_map_name_id.put(tv_program_name, program_index);
			program_index++;
			_programs.add(program);
		}
		_programs_size = program_index;
		LogUtil.info("Program_info load 完成");
	}
	
	public static void init() {
		if (-1 == _programs_size)  {
			loadProgramsByFile(COMPLETE_PROGRAM_FILE_PATH);
		}
	}
	
	public static String getTvProgramNameById(int id) {
		init();		
		return _programs.get(id).getTv_name();
	}
	
	public static List<Program> getProgramList() {
		init();
		return _programs;
	}
	
	public static Program getProgramById(int id) {
		init();
		return _programs.get(id);
	}
	
	public static Program getProgramByTvProgramName(String name) {
		init();
		return _programs.get(_map_name_id.get(name));
	}
	
	public static void main(String[] args) {
		//TEST
		formatSinaTags("标签： NBA NBA新闻 NBA图片 NBA投票 NBA视频 NBA球迷互动 NBA数据酷 NBA战报 NBA评论 NBA球员特写");
		getDetailPorgramInfoByTVProgramName();
//		init();
//		
//		int size = 0;
//		System.out.println(size = get_programs_size());
//		for (int i = 0; i < size; i++) {
//			Program program = getProgramById(i);
//			System.out.println(program.tv_name + " " + program.getTv_watch_time());
//			//LogUtil.debug(getTvProgramNameById(i));
//			//LogUtil.error(getProgramById(i));
//			//break;
//			//System.out.println(program.getId());
//			//System.out.println();
//			//PrintClass.print(program);
//			
//			//System.out.println(PrintClass.outObjPropertyString(program));
//			//LogUtil.debug(PrintClass.outObjPropertyString(program));
//			//break;
//		}
	}

	private static void formatSinaTags(String str) {
		String[] tags = str.split(" ");
		for (String tag: tags) {
			System.out.println(tag);
		}
	}

	/*
	 * 使用用户名 + tv播放量，加在sina上爬取获得programinfo
	 */
	private static void getDetailPorgramInfoByTVProgramName() {
		_programs = new ArrayList<Program>();
//		WebDriver driver = SeleniumUtil.getDriver();
//		Login login = new Login();
//		LoginUsersManage manage = new LoginUsersManage();
//		LoginUser sinauser = manage.getUserByIndex(0);
//		login.login(sinauser.getName(), sinauser.getPassword(), sinauser.getSafe_level(), driver);
		List<String> base_pro_infos = FileUtil.readLogByList(BASE_PROGRAM_FILE_PATH);
		for (String base_pro_info : base_pro_infos) {
			String tvname = base_pro_info.split(" ")[0];
			System.out.println(tvname);
//			String tvwatchtime = base_pro_info.split(" ")[1];
//			Document program_doc = getDocProgramByTvProgramName(tvname, driver);
//			if (program_doc == null) {
//				continue;
//			}
//			program_doc.append("tv_name", tvname);
//			program_doc.append("tv_watch_tim", tvwatchtime);
//			Jdbc.insert_doc("tv_program_info", program_doc);
//			Sleep.rand_time(30000, 30000);
			//debug 
			//break;
		}
	}

	private static Document getDocProgramByTvProgramName(String tvname, WebDriver driver) {
		System.out.println("now program is " + tvname);
		Document doc = new Document();
		String search_name = tvname;
		driver.get(AddFollowStar.getSearchUrl(search_name));
		
		List<WebElement> divs = driver.findElements(By.xpath("//*[@class='list_person clearfix']"));
		boolean have_find = false;
		
		for (int j = 0; j < Math.min(divs.size(), getProgramChoiseWay(search_name)); j++) {
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
			
			if (AddFollowStar.is_not_match_programame(name, search_name)) {
				continue;
			}
			have_find = true;
			//follow!
			//getProgramInfo

			//fans_num
			String fans_num = null;
			List<WebElement> spans = div.findElements(By.tagName("span"));
			System.out.println(spans.size());
			for (WebElement span : spans) {
				String str_span = span.getText();
				int index = str_span.indexOf("粉丝");
				if (index != -1) {
					fans_num = str_span;
				}
			}
			
			//sina_tags
			String str_tags = null;
			List<WebElement> tags = div.findElements(By.className("person_label"));
			if (tags.size() != 0) {
				str_tags = tags.get(0).getText();
				if (str_tags.indexOf("：") != -1) {
					str_tags.substring(str_tags.indexOf("："));
				}
				List<String> list_tags = new ArrayList<String>(Arrays.asList(str_tags.split(" ")));
				str_tags = ComUtil.ListStrToLongStr(list_tags);
				//System.out.println(str_tags);
			}
			
			doc.append("sina_id", uid);
			doc.append("sina_name", name);
			doc.append("fans_num", fans_num);
			doc.append("str_sina_tags", str_tags);
			return doc;
		}
		return null;
	}

	private static int getProgramChoiseWay(String search_name) {
		String [] choise_first = {"冠军欧洲",""};
		if (ComUtil.isStringInArray(search_name, choise_first)) {
			return 1;
		} else {
			return 3;
		}
	}

}
