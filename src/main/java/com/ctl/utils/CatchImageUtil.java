package com.ctl.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * java抓取网络图片
 * @author swinglife
 *
 */
public class CatchImageUtil {
	static Logger logger = LoggerFactory.getLogger(CatchImageUtil.class);
	// 地址
	//private static final String URL = "http://www.csdn.net";
	// 编码
	private static final String ECODING = "UTF-8";
	// 获取img标签正则
	//<img src="1bl7818c717_750_6615.jpg?imageView&amp;quality=98&amp;crop=0_0_750_500"> 这种格式图片判断有误
	private static final String IMGURL_REG = "<img.*src=(.*?)[^>]*?>";
	// 获取src路径的正则
	private static final String IMGSRC_REG = "http:\"?(.*?)(\"|>|\\s+)";
	public static List<String> getImgSrcList(String content){
        List<String> list = new ArrayList<String>();
        //目前img标签标示有3种表达式
        //<img alt="" src="1.jpg"/>   <img alt="" src="1.jpg"></img>     <img alt="" src="1.jpg">
        //开始匹配content中的<img />标签
        Pattern p_img = Pattern.compile("<(img|IMG)(.*?)(/>|></img>|>)");
        Matcher m_img = p_img.matcher(content);
        boolean result_img = m_img.find();
        if (result_img) {
            while (result_img) {
                //获取到匹配的<img />标签中的内容
                String str_img = m_img.group(2);
                //开始匹配<img />标签中的src
               // Pattern p_src = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");
                Pattern p_src = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");
                Matcher m_src = p_src.matcher(str_img);
                if (m_src.find()) {
                    String str_src = m_src.group(3);
                    list.add(str_src);
                }
                //匹配content中是否存在下一个<img />标签，有则继续以上步骤匹配<img />标签中的src
                result_img = m_img.find();
            }
        }
        return list;
    }
	
	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		//获得html文本内容
		Scanner scanner=new Scanner(new File("D:/kaola/kaolaDetail/1428965-68a3e5516d7a7dc21fbe0e7ee13bfc1c_kaolaDetail.html"));
		String HTML =  scanner.nextLine();
		scanner.close();
		//获取图片标签
		List<String> imgUrl = CatchImageUtil.getImageUrl(HTML);
		//获取图片src地址
		List<String> imgSrc = getImageSrc2(HTML);
		List<String> imgSrcList = getImgSrcList(HTML);
		System.out.println(imgSrcList);
		//下载图片
	}
	
	
	/***
	 * 获取HTML内容
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public String getHTML(String url) throws Exception {
		URL uri = new URL(url);
		URLConnection connection = uri.openConnection();
		InputStream in = connection.getInputStream();
		byte[] buf = new byte[1024];
		int length = 0;
		StringBuffer sb = new StringBuffer();
		while ((length = in.read(buf, 0, buf.length)) > 0) {
			sb.append(new String(buf, ECODING));
		}
		in.close();
		return sb.toString();
	}

	/***
	 * 获取ImageUrl地址
	 * 
	 * @param HTML
	 * @return
	 */
	public static List<String> getImageUrl(String HTML) {
		Matcher matcher = Pattern.compile(IMGURL_REG).matcher(HTML);
		List<String> listImgUrl = new ArrayList<String>();
		while (matcher.find()) {
			listImgUrl.add(matcher.group());
		}
		return listImgUrl;
	}
	/***
	 * 获取ImageSrc地址
	 * 
	 * @param listImageUrl
	 * @return
	 */
	public List<String> getImageSrc(List<String> listImageUrl) {
		List<String> listImgSrc = new ArrayList<String>();
		for (String image : listImageUrl) {
			Matcher matcher = Pattern.compile(IMGSRC_REG).matcher(image);
			while (matcher.find()) {
				listImgSrc.add(matcher.group().substring(0, matcher.group().length() - 1));
			}
		}
		return listImgSrc;
	}
	/***
	 * 获取ImageSrc地址
	 * 
	 * @param listImageUrl
	 * @return
	 */
	public static List<String> getImageSrc2(String html) {
		List<String> listImageUrl = getImageUrl(html);
		List<String> listImgSrc = new ArrayList<String>();
		for (String image : listImageUrl) {
			Matcher matcher = Pattern.compile(IMGSRC_REG).matcher(image);
			while (matcher.find()) {
				listImgSrc.add(matcher.group().substring(0, matcher.group().length() - 1));
			}
		}
		return listImgSrc;
	}
	/***
	 * 获取ImageSrc地址
	 * 
	 * @param listImageUrl
	 * @return
	 */
	public static Set<String> getImageSrc2Set(String html) {
		List<String> listImageUrl = getImageUrl(html);
		Set<String> setImgSrc = new HashSet<String>();
		for (String image : listImageUrl) {
			Matcher matcher = Pattern.compile(IMGSRC_REG).matcher(image);
			while (matcher.find()) {
				String imageStr=matcher.group().replaceAll("\"", "");
				if(imageStr.lastIndexOf('?')>0){
					setImgSrc.add(imageStr.substring(0, matcher.group().lastIndexOf('?')));
				}else{
					setImgSrc.add(imageStr);
				}
			}
		}
		return setImgSrc;
	}
	/***
	 * 下载图片
	 * 
	 * @param listImgSrc
	 */
	public static boolean download(List<String> listImgSrc,String downloadPath) {
		try {
			new File(downloadPath).mkdirs();
			for (String url : listImgSrc) {
				String imageName = url.substring(url.lastIndexOf("/") + 1, url.length());
				URL uri = new URL(url);
				InputStream in = uri.openStream();
				FileOutputStream fo = new FileOutputStream(new File(downloadPath+File.separator+imageName));
				byte[] buf = new byte[1024];
				int length = 0;
				logger.debug("开始下载:" + url);
				while ((length = in.read(buf, 0, buf.length)) != -1) {
					fo.write(buf, 0, length);
				}
				in.close();
				fo.close();
				logger.debug(imageName + "下载完成");
			}
			return true;
		} catch (Exception e) {
			logger.error("下载失败",e);
			return false;
		}
	}
	/***
	 * 下载图片
	 * 
	 * @param setImgSrc
	 */
	public static boolean download2(Set<String> setImgSrc,String downloadPath) {
		try {
			new File(downloadPath).mkdirs();
			for (String url : setImgSrc) {
				String imageName = url.substring(url.lastIndexOf("/") + 1, url.length());
				URL uri = new URL(url);
				InputStream in = uri.openStream();
				FileOutputStream fo = new FileOutputStream(new File(downloadPath+File.separator+imageName));
				byte[] buf = new byte[1024];
				int length = 0;
				logger.debug("开始下载:" + url);
				while ((length = in.read(buf, 0, buf.length)) != -1) {
					fo.write(buf, 0, length);
				}
				in.close();
				fo.close();
				logger.debug(imageName + "下载完成");
			}
			return true;
		} catch (Exception e) {
			logger.error("下载失败",e);
			return false;
		}
	}
	/***
	 * 下载图片
	 * 
	 * @param setImgSrc
	 */
	public static boolean download(String url,String downloadPath) {
		try {
			URL uri = new URL(url);
			InputStream in = uri.openStream();
			File file=new File(downloadPath);
			file.getParentFile().mkdirs();
			FileOutputStream fo = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int length = 0;
			logger.debug("开始下载:" + url);
			while ((length = in.read(buf, 0, buf.length)) != -1) {
				fo.write(buf, 0, length);
			}
			in.close();
			fo.close();
			logger.debug(downloadPath + "下载完成");
			return true;
		} catch (Exception e) {
			logger.error("下载失败",e);
			return false;
		}
	}
}


