package sinaaccount;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bson.Document;

import tools.ComUtil;

public class LoginUser {
	
	int id = -1;
	String care_enough = null;
	String name = null;
	String password = null;
	int safe_level = -1;//账号安全等级，0 表示不需要验证码， 1 表示需要验证码，2表示冻住
	Set<String> need_care_program_set = null;
	int today_care_cnt = -1;
	int total_need_care_cnt = -1;
	
	public Document toDoc() {
		Document doc = new Document();
		doc.append("id", id);
		doc.append("name", name);
		doc.append("password", password);
		doc.append("safelevel", safe_level);
		doc.append("careenough", care_enough);
		doc.append("total_need_care_cnt", total_need_care_cnt);
		doc.append("today_care_cnt", today_care_cnt);
		if (need_care_program_set != null) {
			List<String> tmp_list = new ArrayList<String>();
			tmp_list.addAll(need_care_program_set);
			doc.append("str_need_care_program_set", ComUtil.ListStrToLongStr(tmp_list));
		}
		return doc;
	}
	
	public Set<String> getNeed_care_program_set() {
		return need_care_program_set;
	}
	public void setNeed_care_program_set(Set<String> need_care_program_set) {
		this.need_care_program_set = need_care_program_set;
	}
	public int getToday_care_cnt() {
		return today_care_cnt;
	}
	public void setToday_care_cnt(int today_care_cnt) {
		this.today_care_cnt = today_care_cnt;
	}

	public int getTotal_need_care_cnt() {
		return total_need_care_cnt;
	}
	public void setTotal_need_care_cnt(int total_need_care_cnt) {
		this.total_need_care_cnt = total_need_care_cnt;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getSafe_level() {
		return safe_level;
	}
	public void setSafe_level(int safe_level) {
		this.safe_level = safe_level;
	}
	public String getCare_enough() {
		return care_enough;
	}

	public void setCare_enough(String care_enough) {
		this.care_enough = care_enough;
	}
	public void print() {
		System.out.println("--id = " + id 
				+ ",name = " + name 
				+ ",password = " + password
				+ ",safe_level = " + safe_level);
	}
	
}

