package sinaaccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mongodb.Jdbc;

import org.bson.Document;

import tools.FileUtil;

public class InsertLoginUser {
	/**
	 * careEnough: 	1 haveCareEnoughUser
	 * 			   	0 haven't
	 * safeLevel:	0 just login
	 * 				1 need verify code
	 * 				2 need unfreezed by phone
	 * 				3 can't login
	 * info:		about account information
	 */
	static String sina_account[] = {
		//name  				,careEnough	, safeLevel	,	info	   id
		"690707520@qq.com		,	1		,		0	,	jiashou",//	0
		"kongxiaoyue123@sina.com,	1		,		0	,	xiaoyue",//	1
		"18654689276			,	1		,		0	,	shengnan",//2
		"15175245812			,	1		,		0	,	hongxin",//	3
		"18038548714			,	1		,		0	,	other",//	4
		"15322779609			,	1		,		0	,	other",//	5
		"13480660142			,	1		,		2	,	level7",//	6  
		"18810397525			,	1		,		2	,	shidi",//	7
		"17164496130			,	1		,		2	,	other",//	8
		"17164499793			,	1		,		2	,	other",//	9  
		"17164499787			,	1		,		2	,	other",//	10
		"18897473067			,	1		,		0	,	other",//	11 
		"13487610499			,	1		,		2	,	other",//	12 
		"14786946392			,	1		,		2	,	other",//	13
		"13873645554			,	1		,		0	,	other",//	14
		"18874642879			,	1		,		2	,	other",//	15
		"18374140968			,	1		,		2	,	other",//	16
		"13469034516			,	1		,		0	,	other",//	17
		"13517379980			,	1		,		2	,	other",//	18
		"15116321648			,	1		,		0	,	other",//	19
		"13617307234			,	0		,		0	,	other",//	20
		"18711036856			,	0		,		0	,	other",//	21 
		"15874924964			,	0		,		0	,	other",//	22 
		"13575114970			,	0		,		0	,	other",//	23 
		"15073366520			,	0		,		0	,	other",//	24 
		"13755009831			,	0		,		0	,	other",//	25 
		"13574560030			,	0		,		0	,	other",//	26 
		"13574690113			,	0		,		0	,	other",//	27 
		"13789207247			,	0		,		0	,	other",//	28 
		null
	};
	
	static final String PATH_OF_LOGINUSER_PASSWORD = "SecretFold\\SinaLoginUserInfo.txt";
	static Map<String, String> map_name_password = null;
	
	public static void main(String[] args) {
		getUserPassword();
		Jdbc.deleteCollection("loginuser");// clean login_user database
		List<Document> docs = new ArrayList<Document>();
		for (int i = 0; sina_account[i] != null; i ++) {
			Document doc = new Document("id", i);
			String str_accounts = removeTab(sina_account[i]);
			
			int id = i;
			String []strs = str_accounts.split(",");
			String name  = strs[0];
			String password = map_name_password.get(name);
			String careEnough = strs[1];
			String safeLevel = strs[2];
			String info = strs[3];
			
			doc.append("id", 		id);
			doc.append("name", 		name);
			doc.append("password", 	password);
			doc.append("careEnough",careEnough);
			doc.append("safeLevel", safeLevel);
			doc.append("info", 		info);
			docs.add(doc);
		}
		Jdbc.insert_docs("loginuser", docs);
	}

	/**
	 * remove whitespace and tab
	 * @param str
	 * @return
	 */
	private static String removeTab(String str) {
		String ret_str = "";
		for (int i = 0; i < str.length(); i++) {
			if ('\t' == str.charAt(i) || ' ' == str.charAt(i)) {
				continue;
			}
			ret_str += str.charAt(i);
		}
		return ret_str;
	}

	private static void getUserPassword() {
		map_name_password = new HashMap<String, String>();
		List<String> str_map_name_psws = FileUtil.readLogByList(PATH_OF_LOGINUSER_PASSWORD);
		for (int i = 0; i < str_map_name_psws.size(); i++) {
			String str_map_name_psw = str_map_name_psws.get(i);
			String user_name = str_map_name_psw.split(",")[0];
			String user_psw = str_map_name_psw.split(",")[1];
			map_name_password.put(user_name, user_psw);
		}
	}

}




