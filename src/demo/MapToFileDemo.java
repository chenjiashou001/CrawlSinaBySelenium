package demo;

import java.util.HashMap;
import java.util.Map;

public class MapToFileDemo {

	public static void main(String[] args) {
		Map<String, String> map_name_psw = new HashMap<String, String>();
		map_name_psw.put("chen", "1213");
		map_name_psw.put("sadf", "123");
		
		System.out.println(map_name_psw.toString());
		String str_map = map_name_psw.toString();
		Map<String, String> new_map = new HashMap<String, String>();
		
	}

}




