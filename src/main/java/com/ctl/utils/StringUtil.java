package com.ctl.utils;

import net.sf.json.JsonConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Title: StringUtil</p>
 * <p>Description: StringUtil</p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.hanshow.com</p>
 *
 * @author guolin
 * @version 1.0
 * @date 2018-06-07 17:20
 */
public class StringUtil {
    private static Logger logger = LoggerFactory.getLogger(StringUtil.class);
    public static JsonConfig jsonConfig = null;

    static {
        jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
        jsonConfig.registerJsonValueProcessor(Timestamp.class, new JsonDateValueProcessor());
        jsonConfig.registerJsonValueProcessor(Integer.class, new JsonNumberValueProcessor());
        jsonConfig.registerJsonValueProcessor(Long.class, new JsonNumberValueProcessor());
        jsonConfig.registerJsonValueProcessor(Byte.class, new JsonNumberValueProcessor());
        jsonConfig.registerJsonValueProcessor(Float.class, new JsonNumberValueProcessor());
        jsonConfig.registerJsonValueProcessor(Double.class, new JsonNumberValueProcessor());
    }
    /**
     * 单位缩进字符串。
     */
    private static String SPACE = "   ";
    private static String NEWLINE="\n";
    /**
     * 判断是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 判断是否为空去除空字符
     * @param str
     * @return
     */
    public static boolean isEmptyTrim(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断字符串数组是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String[] str) {
        return str == null || str.length == 0;
    }
    /**
     * sql防止注入替换
     * @param paramStr
     * @return
     */
    public static  String sqlResplace(String paramStr) {
        if (StringUtil.isEmptyTrim(paramStr)) {
            logger.info("sqlResplace={}为空", paramStr);
            return null;
        } else {
            logger.info("sqlResplace={}", paramStr);
        }
        StringBuilder strDest = new StringBuilder();
        for (int i = 0; i < paramStr.length(); i++) {
            char ch = paramStr.charAt(i);
            switch (ch) {
                case '\0':
                    strDest.append("\\0");
                    break;
                case '\n':
                    strDest.append("\\n");
                    break;
                case '\r':
                    strDest.append("\\r");
                    break;
                case '\'':
                    strDest.append("\\'");
                    break;
                case '"':
                    strDest.append("\\\"");
                    break;
                case '\\':
                    strDest.append("\\\\");
                    break;
                case '%':
                    strDest.append("\\%");
                    break;
                case '_':
                    strDest.append("\\_");
                    break;
                default:
                    strDest.append(ch);
                    break;
            }
        }
        return strDest.toString();
    }

    /**
     * 返回格式化JSON字符串。
     *
     * @param json 未格式化的JSON字符串。
     * @return 格式化的JSON字符串。
     */
    public static String formatJson(String json) {
        if (StringUtil.isEmptyTrim(json)) {
            logger.info("json={}为空", json);
            return null;
        }
        try {
            StringBuffer result = new StringBuffer();

            int length = json.length();
            int number = 0;
            char key = 0;

            //遍历输入字符串。
            for (int i = 0; i < length; i++) {
                //1、获取当前字符。
                key = json.charAt(i);

                //2、如果当前字符是前方括号、前花括号做如下处理：
                if ((key == '[') || (key == '{')) {
                    //（1）如果前面还有字符，并且字符为“：”，打印：换行和缩进字符字符串。
                    if ((i - 1 > 0) && (json.charAt(i - 1) == ':')) {
                        result.append(NEWLINE);
                        result.append(indent(number));
                    }

                    //（2）打印：当前字符。
                    result.append(key);

                    //（3）前方括号、前花括号，的后面必须换行。打印：换行。
                    result.append(NEWLINE);

                    //（4）每出现一次前方括号、前花括号；缩进次数增加一次。打印：新行缩进。
                    number++;
                    result.append(indent(number));

                    //（5）进行下一次循环。
                    continue;
                }

                //3、如果当前字符是后方括号、后花括号做如下处理：
                if ((key == ']') || (key == '}')) {
                    //（1）后方括号、后花括号，的前面必须换行。打印：换行。
                    result.append(NEWLINE);

                    //（2）每出现一次后方括号、后花括号；缩进次数减少一次。打印：缩进。
                    number--;
                    result.append(indent(number));

                    //（3）打印：当前字符。
                    result.append(key);

                    //（4）如果当前字符后面还有字符，并且字符不为“，”，打印：换行。
                    if (((i + 1) < length) && (json.charAt(i + 1) != ',')) {
                        result.append(NEWLINE);
                    }

                    //（5）继续下一次循环。
                    continue;
                }

                //4、如果当前字符是逗号。逗号后面换行，并缩进，不改变缩进次数。
                if ((key == ',')) {
                    result.append(key);
                    result.append(NEWLINE);
                    result.append(indent(number));
                    continue;
                }

                //5、打印：当前字符。
                result.append(key);
            }

            return result.toString();
        } catch (Exception e) {
            logger.error("格式化jsonStr失败:" + json, e);
            return json;
        }
    }

    /**
     * 返回指定次数的缩进字符串。每一次缩进三个空格，即SPACE。
     *
     * @param number 缩进次数。
     * @return 指定缩进次数的字符串。
     */
    private static String indent(int number) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < number; i++) {
            result.append(SPACE);
        }
        return result.toString();
    }
    /**
     * 根据imgUrl获取imgPath失败
     * @param imgUrl
     * @return
     */
    public static String imagePath(String imgUrl) {
        String imageUrlPath = "";
        try {
            if(isEmptyTrim(imgUrl)){
                return null;
            }
            int index = -1;
//            switch (imgUrl) {
//                case "http://":
//                    index = imgUrl.indexOf("/", 7);
//                    if (index > 0) {
//                        imageUrlPath = imgUrl.substring(index + 1);
//                        logger.info("imageUrl={},imageUrlPath={}", imgUrl, imageUrlPath);
//                    }
//                    break;
//                case "https://":
//                    index = imgUrl.indexOf("/", 8);
//                    if (index > 0) {
//                        imageUrlPath = imgUrl.substring(index + 1);
//                        logger.info("imageUrl={},imageUrlPath={}", imgUrl, imageUrlPath);
//                    }
//                    break;
//                default:
//                    imageUrlPath = imgUrl;
//                    break;
//            }
            if (imgUrl.startsWith("http://")) {
                index = imgUrl.indexOf("/", 7);
                if (index > 0) {
                    imageUrlPath = imgUrl.substring(index + 1);
                    logger.info("imageUrl={},imageUrlPath={}", imgUrl, imageUrlPath);
                }
            }
            if (imgUrl.startsWith("https://")) {
                index = imgUrl.indexOf("/", 8);
                if (index > 0) {
                    imageUrlPath = imgUrl.substring(index + 1);
                    logger.info("imageUrl={},imageUrlPath={}", imgUrl, imageUrlPath);
                }
            }
            if(!imgUrl.startsWith("http://")&&!imgUrl.startsWith("https://")){
                imageUrlPath = imgUrl;
            }
            return imageUrlPath;
        } catch (Exception e) {
            logger.error("根据imgUrl获取imgPath失败",e);
        }
        return imageUrlPath;
    }

    /**
     * 获取请求地址中的参数
     * @param url
     * @return "http://mf3w.cc/?storeId=67&sku=XXX" --> {storeId=67, sku=XXX}
     */
    public static Map<String,String> getQueryParamByRequestURL(String url){
        if(isEmpty(url)){
            return null;
        }
        Pattern pattern = Pattern.compile("((http[s]{0,1}|ftp)://[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)|((www.)|[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)");
        Matcher matcher = pattern.matcher(url);
        if(matcher.matches()){

        }else{
            logger.info("不合法的请求地址");
            return null;
        }
        int index = url.indexOf("?");
        Map<String, String> queryParam = new HashMap<>();
        if (index > 0) {
            String substring = url.substring(index+1);
            String[] splitO = substring.split("&");
            if (splitO != null && splitO.length > 0) {
                for (int i = 0; i < splitO.length; i++) {
                    String[] splitT = splitO[i].split("=");
                    if (splitT != null && splitT.length == 2) {
                        queryParam.put(splitT[0], splitT[1]);
                    }
                }
            }
        }
        return queryParam;
    }
    public static void main(String[] args) {
        System.out.println(imagePath("http://192.168.2.51:8080/group1/M00/00/78/wKgCM1uXuEyAO4mEAAFCquGfj_U946.jpg"));
        System.out.println(jsonConfig);
    }
}
