package com.ctl.utils.http; /**
 * @author guolin
 * @date 2016-6-15下午4:50:14
 * @package_name
 * @project_name http
 * @version version.1.0
 */

import java.net.*;
import java.io.*;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;


public class HttpUp {
    public static int access(String URLString) {
        try {
            StringBuffer response = new StringBuffer();

            HttpClient client = new HttpClient();
            PostMethod method = new PostMethod(URLString);

            //设置Http Post数据，这里是上传文件
            File f = new File("d:\\a1.txt");
            FileInputStream fi = new FileInputStream(f);
            InputStreamRequestEntity fr = new InputStreamRequestEntity(fi);
            method.setRequestEntity((RequestEntity) fr);
            try {
                client.executeMethod(method); //这一步就把文件上传了
                //下面是读取网站的返回网页，例如上传成功之类的
                if (method.getStatusCode() == HttpStatus.SC_OK) {
                    //读取为 InputStream，在网页内容数据量大时候推荐使用
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(method.getResponseBodyAsStream(),
                                    "GBK"));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                }
            } catch (IOException e) {
                System.out.println("执行HTTP Post请求" + URLString + "时，发生异常！");
                e.printStackTrace();
            } finally {
                method.releaseConnection();
            }
            System.out.println("--------------------" + response.toString());
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    public static void main(String args[]) {
        new HttpUp();

    }
}

