package test;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class TestFile {
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
            //��һ�ֶ�ȡ��ʽ
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