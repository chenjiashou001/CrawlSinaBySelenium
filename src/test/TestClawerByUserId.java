package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import etc.ClawerByUserId;

public class TestClawerByUserId {
	public static void main(String[] args) {
		int cnt = 0;
		ClawerByUserId clawer = new ClawerByUserId();
		//clawer.login();
		String path = "C:\\Users\\chenjiashou\\Desktop\\SAVE_USER\\USERS.txt";
		List<String> userids = new ArrayList<String>();
		StringBuffer sb=new StringBuffer();
        String tempstr=null;
        try
        {
            File file=new File(path);
            if(!file.exists())
                throw new FileNotFoundException();            
//            BufferedReader br=new BufferedReader(new FileReader(file));            
//            while((tempstr=br.readLine())!=null)
//                sb.append(tempstr);    
            //另一种读取方式
            FileInputStream fis=new FileInputStream(file);
            BufferedReader br=new BufferedReader(new InputStreamReader(fis));
            while((tempstr=br.readLine())!=null)
            {
            	if (cnt < 1200) {
            		cnt++;
            		continue;
            	}
            	userids.add(tempstr.substring(3));
            }
        }
        catch(IOException ex)
        {
            System.out.println(ex.getStackTrace());
        }
		//userids.add("3457655792");
		clawer.clawer_users(userids);
	}
}

