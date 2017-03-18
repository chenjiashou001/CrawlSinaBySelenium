package sinaaccount;

public class LoginUser {
	int id;
	String name;
	String password;
	int safe_level;//账号安全等级，0 表示不需要验证码， 1 表示需要验证码
	
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
	
	public void print() {
		System.out.println("--id = " + id 
				+ ",name = " + name 
				+ ",password = " + password
				+ ",safe_level = " + safe_level);
	}
	
}

