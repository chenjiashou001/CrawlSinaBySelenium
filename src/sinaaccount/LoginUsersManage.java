package sinaaccount;

import java.util.ArrayList;
import java.util.List;

import mongodb.Jdbc;

import org.bson.Document;

import com.mongodb.client.MongoCursor;

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
			//user.setSafe_level(doc.getInteger("safelevel"));
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
	
}