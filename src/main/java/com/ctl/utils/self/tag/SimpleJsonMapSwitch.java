package com.ctl.utils.self.tag;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  @author 		guolin
 *  @date    		2015-8-7下午7:21:43
 *  @package_name 	com.ctl.self.tag
 *  @project_name   Spring3.0企业应用�?��实战
 *  @version 		version.1.0
 *  @descirption    将json转化为Map,json是简单的不支持复杂的
 */
public class SimpleJsonMapSwitch {
	private static Logger logger= LoggerFactory.getLogger(SimpleJsonMapSwitch.class);
	/**
	 * @description 如果json中想使用':' 可以使用'$#58'替换
	 * @param jsonName  json的文件名，注意json文件名必须放�?class存放路径的json文件夹下面，且传入参数jsonName没有后缀
	 * @return
	 */
	public static Map<String, String> getMapFromJSON(String jsonName){
		return getMapFromJSON(jsonName,null);
	}
	/**
	 * @description 如果json中想使用':' 可以使用'$#58'替换       json中的注释'//','/*'�?只可以放到行首或行尾
	 * @param jsonName 传入参数json文件名字且没有后�?
	 * @param jsonPath 传入参数json文件的路�?
	 * @return
	 */
	public static Map<String, String> getMapFromJSON(String jsonName,String jsonPath){
		try {
			StringBuilder bufpath=new StringBuilder(128);
			Scanner scanner =null;
			InputStream input=null;
			Map<String, String> map=new HashMap<>();
			if(jsonPath==null||"".equals(jsonPath)){
				logger.debug("path is null user default path classpath:json");
				bufpath.append("json").append(File.separator).append(jsonName).append(".js");
				input=SimpleJsonMapSwitch.class.getClassLoader().getResourceAsStream(bufpath.toString());
			}else{
				bufpath.append(jsonPath).append(File.separator).append(jsonName).append(".js");
				input=new FileInputStream(new File(bufpath.toString()));
				logger.debug("user path:"+bufpath);
			}
			scanner=new Scanner(input,"utf-8");
			StringBuffer buf=new StringBuffer(8192);
			boolean isContinue=false;
			while (scanner.hasNext()) {
				String str=scanner.nextLine();
				//注释去掉
				if(str==null||str.trim().startsWith("//")||str.trim().length()<1){
					continue;
				}
				//去除每行�?//'极其后面的内�?
				if(str.trim().contains("//")&&!str.trim().startsWith("//")){
					str=str.split("//")[0];
				}
				//注释去掉
				if(str.trim().startsWith("/*")&&str.trim().endsWith("*/")){
					continue;
				}
				//如果�?/结束且有/*�?��则下�?���?��为正常数�?
				if(isContinue&&str.trim().endsWith("*/")){
					isContinue=false;
					continue;
				}
				//如果�?**/中的内容则去�?
				if(isContinue){
					continue;
				}
				if((str.trim().startsWith("/*")||str.trim().endsWith("/*"))&&!str.trim().endsWith("*/")){
					isContinue=true;
					continue;
				}
				logger.debug(str.trim());
				buf.append(str.trim());
			}
			scanner.close();
			//去除{}�?�?转换为空字符，并将整个json分割成key:value
			String[] values=buf.subSequence(1, buf.length()-1).toString().replaceAll("\"", "").replaceAll("\'", "").split(";");
			for(String val:values){
				String key=val.split(":")[0];
				String value=val.split(":")[1];
				if(value.contains("&#58")){
					value=value.replaceAll("&#58", ":");
					map.put(key,value);
				}else{
					map.put(key,value);
				}
			}
			return map;
		} catch (Exception e) {
			logger.error("json file is not exist or jsonfile from is not right!");
			return null;
		}
		
	}
	
	/**
	 * @param map 要转化为json的map 默认生成的js文件在classpath路径下面的json文件夹下�?
	 * @return StringBuilder返回生成的json字符�?
	 */
	public static StringBuilder createJsonFromMap(Map<String,String> map){
		return createJsonFromMap(map,null);
	}
	/**
	 * @param map 要转化为json的map 
	 * @param filePath 文件的存放路�?for example D:\map2js.js)
	 * @return StringBuilder返回生成的json字符�?
	 */
	public static StringBuilder createJsonFromMap(Map<String,String> map,String filePath){
		StringBuilder buf=new StringBuilder(3000);
		buf.append("{");
		Set<String> keys=map.keySet();
		Iterator<String> iter=keys.iterator();
		while(iter.hasNext()){
			String key=iter.next();
			String value=map.get(key);
			buf.append("\'");
			buf.append(key);
			buf.append("\'");
			buf.append(":");
			buf.append("\'");
			buf.append(value);
			buf.append("\'");
			buf.append(";");
		}
		//去掉�?���?�� ;
		buf.deleteCharAt(buf.length()-1);
		buf.append("}");
		try {
			if(filePath==null||"".equals(filePath)){
				filePath=URLDecoder.decode(SimpleJsonMapSwitch.class.getResource("/").getFile().toString(), "utf-8")+"json"+File.separator+"map2js.js";
			}
			logger.debug("filepath:"+filePath);
			FileWriter file=new FileWriter(new File(filePath));
			file.write(buf.toString());
			file.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
			return null;
		}
		return buf;
	}
	
	public static void main(String[] args) throws  Exception{
		 Map<String,String> js2map=getMapFromJSON("map2js",URLDecoder.decode(SimpleJsonMapSwitch.class.getClassLoader().getResource("").getPath().toString(), "utf-8")+"json");
		 logger.info(js2map.toString());
		 js2map=getMapFromJSON("map2js");
		 logger.info(js2map.toString());
		Map<String,String> map=new TreeMap<>();
		map.put("ApplicationResources_check.prop.20150812074659", "D&#58\\E盘\\JavaWeb\\SpringMVC\\FastLiquidData-ATM\\liquiddataATM\\WEB-INF\\classes\\ApplicationResources_check.prop");
		map.put("ApplicationResources_check.properties", "D&#58\\E盘\\JavaWeb\\SpringMVC\\FastLiquidData-ATM\\conf\\ApplicationResources_check.properties");
		map.put("CashMonthStatMgrImpl.java", "D&#58\\E盘\\JavaWeb\\SpringMVC\\FastLiquidData-ATM\\src\\cn\\grgbanking\\view\\report\\service\\impl\\CashMonthStatMgrImpl.java");
		logger.info(createJsonFromMap(map).toString());
		logger.info(createJsonFromMap(map,URLDecoder.decode(SimpleJsonMapSwitch.class.getClassLoader().getResource("").getPath().toString(), "utf-8")+"json"+File.separator+"map2js").toString());
	}

}
