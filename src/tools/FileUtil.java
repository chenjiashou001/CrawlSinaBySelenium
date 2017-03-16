package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
	String newline = "\r\n";//windows
	
	 //д�ļ���֧�������ַ�����linux redhad�²��Թ�
    public static void writeLog(String path, String str)
    {
        try
        {
        File file=new File(path);
        if(!file.exists())
            file.createNewFile();
        //SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        FileOutputStream out=new FileOutputStream(file,true); //���׷�ӷ�ʽ��true        
        StringBuffer sb=new StringBuffer();
        //sb.append("-----------"+sdf.format(new Date())+"------------\n");
        sb.append(str+"\r\n");
        out.write(sb.toString().getBytes("utf-8"));//ע����Ҫת����Ӧ���ַ���
        out.close();
        }
        catch(IOException ex)
        {
            System.out.println(ex.getStackTrace());
        }
    }
    
    /**
     * ���ļ�����һ��String����
     * @param path
     * @return
     */
    public static String readLogByString(String path)
    {
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
            //��һ�ֶ�ȡ��ʽ
            FileInputStream fis=new FileInputStream(file);
            BufferedReader br=new BufferedReader(new InputStreamReader(fis, "utf-8"));
            while((tempstr=br.readLine())!=null) {
                sb.append(tempstr + "\r\n");
            }
        }
        catch(IOException ex)
        {
            System.out.println(ex.getStackTrace());
        }
        return sb.toString();
    }
    
    /**
     * ���ļ�����ÿ�е�����List�ķ���
     * @param path
     * @return
     */
    public static List<String> readLogByList(String path) {
    	List<String> lines = new ArrayList<String>();
        String tempstr = null;
        try {
            File file = new File(path);
            if(!file.exists()) {
                throw new FileNotFoundException();
            }
//            BufferedReader br=new BufferedReader(new FileReader(file));            
//            while((tempstr=br.readLine())!=null)
//                sb.append(tempstr);    
            //��һ�ֶ�ȡ��ʽ
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "utf-8"));
            while((tempstr = br.readLine()) != null) {
            	lines.add(tempstr.toString());
            }
        } catch(IOException ex) {
            System.out.println(ex.getStackTrace());
        }
        return lines;
    }
    
	public static void main(String[] args) {
		String FilePath = "C:\\Users\\chenjiashou\\Desktop\\ALLSAVE";
		/*
		String str = readLogByString(FilePath);
		try {
			str = new String(str.getBytes("gbk"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(str);
		*/
		List<String> strs = readLogByList(FilePath);
		for (int i = 0; i < strs.size(); i++) {
			try {
				String prt_str = new String(strs.get(i).getBytes("utf-8"), "gbk");
				//System.out.println(prt_str);
				System.out.println(strs.get(i));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

}
