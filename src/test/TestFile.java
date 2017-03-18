package test;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class TestFile {
    //写文件，支持中文字符，在linux redhad下测试过
    public static void writeLog(String path, String str)
    {
        try
        {
        File file=new File(path);
        if(!file.exists())
            file.createNewFile();
        //SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        FileOutputStream out=new FileOutputStream(file,true); //如果追加方式用true        
        StringBuffer sb=new StringBuffer();
        //sb.append("-----------"+sdf.format(new Date())+"------------\n");
        sb.append(str+"\r\n");
        out.write(sb.toString().getBytes("utf-8"));//注意需要转换对应的字符集
        out.close();
        }
        catch(IOException ex)
        {
            System.out.println(ex.getStackTrace());
        }
    }    
    public static String readLog(String path)
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
            //另一种读取方式
            FileInputStream fis=new FileInputStream(file);
            BufferedReader br=new BufferedReader(new InputStreamReader(fis));
            while((tempstr=br.readLine())!=null)
                sb.append(tempstr);
        }
        catch(IOException ex)
        {
            System.out.println(ex.getStackTrace());
        }
        return sb.toString();
    }


}

