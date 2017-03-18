package tools;

import java.util.List;

/**
 * 常用工具
 * @author chenhuan001
 *
 */
public class ComUtil {
	public static final String SPLITFLAG = "###";
	final static char []NEED_TRIM_CHAR = {
		'\r',
		'\n',
		' ',
	};
	
	public static String str_trim_beg(String bsd) {
		 
		if (bsd == null) {
			return null;
		}
		String ret_str = "";
		for (int i = 0; i < bsd.length(); i++) {
			char temp_c = bsd.charAt(i);
			if (isCharInArray(temp_c, NEED_TRIM_CHAR)) {
				continue;
			} else {
				ret_str = bsd.substring(i);
				break;
			}
		}
		return ret_str;
	}
	
	public static boolean isCharInArray(char c, char[] array_c) {
		for (int i = 0; i < array_c.length; i++) {
			if (array_c[i] == c) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isStringInArray(String str, String[] array_str) {
		if (null == array_str || null == str) {
			return false;
		}
		for (int i = 0; i < array_str.length && array_str[i] != null; i++) {
			if (array_str[i].equals(str)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean charIsNumber(char c) {
		return (c >= '0' && c <= '9');
	}
	
	public static String ListStrToLongStr(List<String> strs) {
		StringBuffer sb = new StringBuffer();
		for(String str : strs) {
			sb.append(str + SPLITFLAG);
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
