package tools;

import java.io.UnsupportedEncodingException;
/**
 * urlת�롢����
 *
 * @author lifq 
 * @date 2015-3-17 ����04:09:35
 */
public class UrlUtil {
    private final static String ENCODE = "UTF-8"; 
    /**
     * URL ����
     *
     * @return String
     * @author lifq
     * @date 2015-3-17 ����04:09:51
     */
    public static String getURLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * URL ת��
     *
     * @return String
     * @author lifq
     * @date 2015-3-17 ����04:10:28
     */
    public static String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 
     * @return void
     * @author lifq
     * @date 2015-3-17 ����04:09:16
     */
    public static void main(String[] args) {
        String str = "���±���";
        System.out.println(str = getURLEncoderString(str));
        System.out.println(str = getURLEncoderString(str));
        //%25E5%2586%259B%25E4%25BA%258B%25E6%258A%25A5%25E9%2581%2593
        System.out.println(str = getURLDecoderString(str));
        System.out.println(getURLDecoderString(str));
    }

}