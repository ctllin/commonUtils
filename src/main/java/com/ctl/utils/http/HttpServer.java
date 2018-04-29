package com.ctl.utils.http;

import java.io.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ctl.utils.DateUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * @author guolin
 * @date 2016-6-15下午4:38:12
 * @package_name
 * @project_name http
 * @version version.1.0
 */
public class HttpServer {
	public void receiveData(HttpServletRequest request,HttpServletResponse response) {

		PrintWriter out = null;
		response.setContentType("text/html;charset=UTF-8");

		Map map = new HashMap();
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		File directory = null;
		List<FileItem> items = new ArrayList();
		try {
			items = upload.parseRequest(request);
			// 得到所有的文件
			Iterator<FileItem> it = items.iterator();
			while (it.hasNext()) {
				FileItem fItem = (FileItem) it.next();
				String fName = "";
				Object fValue = null;
				if (fItem.isFormField()) { // 普通文本框的值
					fName = fItem.getFieldName();
					// fValue = fItem.getString();
					fValue = fItem.getString("UTF-8");
					map.put(fName, fValue);
				} else { // 获取上传文件的值
					fName = fItem.getFieldName();
					fValue = fItem.getInputStream();
					map.put(fName, fValue);
					String name = fItem.getName();
					if (name != null && !("".equals(name))) {
						name = name
								.substring(name.lastIndexOf(File.separator) + 1);

						// String stamp =
						// StringUtils.getFormattedCurrDateNumberString();
						String timestamp_Str = DateUtil.sdfyyyy_MM_dd.format(new Date());
						directory = new File("d://test");
						directory.mkdirs();

						String filePath = ("d://test") + timestamp_Str+ File.separator + name;
						map.put(fName + "FilePath", filePath);

						InputStream is = fItem.getInputStream();
						FileOutputStream fos = new FileOutputStream(filePath);
						byte[] buffer = new byte[1024];
						while (is.read(buffer) > 0) {
							fos.write(buffer, 0, buffer.length);
						}
						fos.flush();
						fos.close();
						map.put(fName + "FileName", name);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("读取http请求属性值出错!");
			// e.printStackTrace();
		//	logger.error("读取http请求属性值出错");
		}

		// 数据处理

		try {
			out = response.getWriter();
			out.print("{success:true, msg:'接收成功'}");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
