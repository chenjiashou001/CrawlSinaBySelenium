package sinaaccount;

public class LoginUser {
	int id;
	String name;
	String password;
	int safe_level;//�˺Ű�ȫ�ȼ���0 ��ʾ����Ҫ��֤�룬 1 ��ʾ��Ҫ��֤��
	
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