package cookie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import mongodb.Jdbc;

import org.bson.Document;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

/**
 * cookie 存储类,存储到mongodb中
 * @author chenjiashou
 *
 */
public class CookieUtil {
	static final String COOKIE_COLLECITION_NAME = "cookie";
	
	/**
	 * 将dirver的cookie以cookie_name名字存入mongodb
	 * @param cookie_name
	 * @param driver
	 * @return
	 */
	static public boolean save(String cookie_name, WebDriver driver) {
		StringBuffer sb = new StringBuffer();
		Set<Cookie> cookieSet = null;
		
		try{
			cookieSet = driver.manage().getCookies();
		} catch(Exception e) {
			System.out.println("save cookie 异常");
			return false;
		}
		
		for (Cookie ck : cookieSet) {
            sb.append(ck.getName() + ";" + ck.getValue() + ";"
                    + ck.getDomain() + ";" + ck.getPath() + ";"
                    + ck.getExpiry() + ";" + ck.isSecure());
            sb.append("\r\n");
        }
        Document doc = new Document("name", cookie_name);
        doc.append("content", sb.toString());
        Jdbc.insert_doc(COOKIE_COLLECITION_NAME, doc);
        System.out.println("CookieUtil :" + cookie_name + "写入成功!");
		return true;
	}
	
	/**
	 * 将cookie_name的cookie写入drivoer中去
	 * @param cookie_name
	 * @param driver
	 * @return
	 */
	static public boolean get(String cookie_name, WebDriver driver) {
		 List<Document> docs = Jdbc.find("cookie", new Document("name", cookie_name));
		 if (docs.size() == 0) {
			 return false;
		 }
		 StringBuffer sb = new StringBuffer(docs.get(0).getString("content"));
         String line;
         String[] lines = sb.toString().split("\r\n");
         for (int i = 0; i < lines.length; i++) {
        	 line = lines[i];
             StringTokenizer str=new StringTokenizer(line,";");
             while(str.hasMoreTokens()) {
                 String name = str.nextToken();
                 String value = str.nextToken();
                 String domain = str.nextToken();
                 String path = str.nextToken();
                 Date expiry = null;
                 
                 String dt;
                 if(!(dt=str.nextToken()).equals("null"))
                 {
                	 //System.out.println(dt);
                     //expiry=new Date(dt);
                     //System.out.println(expiry);
                 }
                 boolean isSecure = new Boolean(str.nextToken()).booleanValue();
                 
                 //System.out.println(domain);
                 if (domain.indexOf(".tanx.com") != -1) {//什么鬼新浪
                	 domain = null;
                 }
                 Cookie ck = new Cookie(name,value,domain,path,expiry,isSecure);
                 driver.manage().addCookie(ck);
             }
         }
         
		return true;
	}
	
}
