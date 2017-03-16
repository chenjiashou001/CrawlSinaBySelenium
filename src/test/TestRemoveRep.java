package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

public class TestRemoveRep {
	static HashSet<String> alluserid = new HashSet<String>();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String tempstr=null;
        try
        {
            File file=new File("C:\\Users\\chenjiashou\\Desktop\\SAVE_USER\\USERS.txt");
            if(!file.exists())
                throw new FileNotFoundException();            
//            BufferedReader br=new BufferedReader(new FileReader(file));            
//            while((tempstr=br.readLine())!=null)
//                sb.append(tempstr);    
            //另一种读取方式
            FileInputStream fis=new FileInputStream(file);
            BufferedReader br=new BufferedReader(new InputStreamReader(fis));
            while((tempstr=br.readLine())!=null){
                if (alluserid.contains(tempstr)) {
                	continue;
                }
                alluserid.add(tempstr);
            	TestFile.writeLog("C:\\Users\\chenjiashou\\Desktop\\SAVE_USER\\USERS1.txt", tempstr);
            }
        }
        catch(IOException ex)
        {
            System.out.println(ex.getStackTrace());
        }
	}

}
