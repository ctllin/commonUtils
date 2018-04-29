package com.ctl.utils.ftp;

import java.io.*;
import java.net.SocketException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import com.ctl.utils.ConfigUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FTPClientUtil {
    static Logger logger = LoggerFactory.getLogger(FTPClientUtil.class);
    private static FTPClient fClient;
    private static String username; // 登陆FTP 用户名
    private static String password; // 用户密码，支持强密码
    private static String url; // FTP 地址
    private static int port;// FTP 端口
    private static String remoteDir;// FTP 远程目录
    private static String ftpname;// FTP 链接的名字
    public static String ftpLocalDir;//ftp下载到本地路径
    static {
        try {
            username =  ConfigUtils.getType( "ftp.username");
            password =  ConfigUtils.getType( "ftp.password");
            url =  ConfigUtils.getType( "ftp.url");
            port = Integer.parseInt( ConfigUtils.getType( "ftp.port"));
            remoteDir =  ConfigUtils.getType( "ftp.remoteDir");
            if(null==remoteDir||"".equals(remoteDir.trim())){
                remoteDir=File.separator;
            }
            ftpLogin();
        } catch (Exception e) {
            logger.error("登录失败:",e);
            fClient=null;
        } finally {
            ftpname =  ConfigUtils.getType( "ftp.ftpname");
            ftpLocalDir= ConfigUtils.getType( "ftp.local.dir");
            String loginInfo= new StringBuilder("ftpname:").append(ftpname).append( "\tftpLocalDir:").append(ftpLocalDir).toString();
            logger.info(loginInfo);
        }

    }
    public static FTPClient ftpLogin() {
        if(fClient==null||!FTPReply.isPositiveCompletion(fClient.getReplyCode())||!fClient.isAvailable()||!fClient.isRemoteVerificationEnabled()){
            fClient = new FTPClient();
            try {
                fClient.connect(url,port);
                fClient.login(username, password);
                //fClient.setDefaultTimeout(6000);
                // fClient.setSoTimeout(3000);
                fClient.setDataTimeout(Integer.parseInt(ConfigUtils.getType("ftp.dataTimeout")));       //设置传输超时时间为60秒
                fClient.setConnectTimeout(Integer.parseInt(ConfigUtils.getType("ftp.connectTimeout")));       //连接超时为6
                int reply = fClient.getReplyCode();
                if (FTPReply.isPositiveCompletion(reply)) {// 登陆到ftp服务器
                    fClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                    fClient.setFileTransferMode(FTPClient.STREAM_TRANSFER_MODE);
                    //fClient.setControlEncoding("UTF-8");
                    // fClient.enterLocalPassiveMode();
                    //fClient.setBufferSize(2048);
                    logger.info("Client login success："+fClient.getStatus());
                    logger.info("当前路径："+fClient.printWorkingDirectory());
                }else{
                    fClient.disconnect();
                    fClient=null;
                }
                // fClient.changeWorkingDirectory(remoteDir);
            } catch (SocketException e) {
                fClient=null;
                logger.error("Client login fail:",e);
            } catch (IOException e) {
                fClient=null;
                logger.error("Client login fail:",e);
            }
        }
        return fClient;
    }

    /**
     * 返回上一级目录(父目录)
     */
    public static void toParentDir() {
        try {
            logger.info("当前路径："+fClient.printWorkingDirectory());
            boolean result=fClient.changeToParentDirectory();
            if(result){
                logger.info("返回到父目录成功");
                logger.info("当前路径："+fClient.printWorkingDirectory());
            }else{
                logger.warn("返回父目录失败");
            }
        } catch (IOException e) {
            logger.error("返回上一级目录(父目录)失败：",e);
        }
        logger.info(ftpname + " " + url + " 返回到上层目录。");
    }

    /**
     * 变更到根目录
     * @return
     * @throws IOException
     */
    public static boolean  changeToRootDirectory() throws  IOException{
        boolean result = fClient.changeWorkingDirectory("/");
        if (result) {
            logger.info(ftpname + " " + url + " 变更到跟目录成功" );
        } else {
            logger.info(ftpname + " " + url + " 变更到跟目录失败");
        }
        logger.info("当前路径："+fClient.printWorkingDirectory());
        return result;
    }

    /**
     * 变更工作目录
     *
     * @param remoteDir
     *            --目录路径
     */
    public static boolean changeDir(String remoteDir) throws IOException{
        boolean result = false;
        try {
            logger.info("当前路径："+fClient.printWorkingDirectory());
            remoteDir= remoteDir.replaceAll("//",",").replaceAll("\\\\",",");
            String paths[]=remoteDir.split(",");
            if(paths.length>1){
                logger.info("多级目录变更");
                for(int i=0;i<paths.length;i++){
                    fClient.changeWorkingDirectory(paths[i]);
                    logger.info("当前路径："+fClient.printWorkingDirectory());
                }
            }else{
                if(",".equals(remoteDir)){
                    remoteDir=File.separator;
                }
                boolean changeWorkingDirectorySuccess = fClient.changeWorkingDirectory(remoteDir);
                if (changeWorkingDirectorySuccess) {
                    result = true;
                    logger.info(ftpname + " " + url + " 变更工作目录为:" + remoteDir);
                } else {
                    logger.info(ftpname + " " + url + " 变更工作目录" + remoteDir + "失败");
                }
            }

        } catch (IOException e) {
            logger.error(ftpname + " " + url + " 变更工作目录" + remoteDir + "失败,异常 ",e);
        }
        logger.info("当前路径："+fClient.printWorkingDirectory());
        return result;
    }
    /**
     *
     * @param childDir 是否包含子目录
     * @throws Exception
     */
    public static void listFiles(boolean childDir) throws Exception {
        FTPFile[] files = fClient.mlistDir();
        if (files.length > 0) {
            for (FTPFile filename : files) {
                if (filename.getType() == 1&& (!filename.getName().equals(".")) && childDir) {// did
                    changeDir(filename.getName());
                    listFiles(childDir);
                } else if (filename.getType() == 0) {// file
                    logger.info(filename.getName());
                }
            }
            toParentDir();
        }
    }

    /**
     *
     * @param list 返回文件集合
     * @throws IOException
     */
    public static void listFilesStr(List<String> list) throws IOException{
        listFilesStr(false,list);
    }
    public static void listFilesStr(boolean childDir,List<String> list) throws IOException{
        listFilesStr(childDir,File.separator,list);
    }

    /**
     *
     * @param childDir
     * @param currentWorkingDirectory  相对更目录的路径 例如/mnt/ftpclient/20991201
     * @param list
     * @throws IOException
     */
    public static void listFilesStr(boolean childDir,String currentWorkingDirectory,List<String> list) throws IOException{
        if(currentWorkingDirectory==null||"".equals(currentWorkingDirectory.trim())){
             changeDir(currentWorkingDirectory);
        }else{
            changeDir(currentWorkingDirectory);
            FTPFile[] clientTmp=fClient.listFiles();
            for (int i = 0; i < clientTmp.length; i++) {
                if(clientTmp[i].isDirectory()){
                    if(childDir&&!".".equals(clientTmp[i].getName())&&!"..".equals(clientTmp[i].getName())){
                        String getName=clientTmp[i].getName();
                        //System.err.println(getName);
                        listFilesStr(childDir,getName,list);
                    }
                }else{
                    list.add(clientTmp[i].getName());
                }
            }
            toParentDir();
        }
    }



    /**
     * 关闭client连接
     */
    public static void close(){
        try {
            fClient.logout();
            fClient.disconnect();
            fClient=null;
        } catch (IOException e) {
            logger.error("disconnect fail:",e);
        }
    }


    /**
     * 下载文件
     *
     * @param localFilePath
     *            本地文件名及路径
     * @param remoteFileName
     *            远程文件名称
     * @return
     */
    public static boolean downloadFile(String localFilePath,String remoteFileName) {
        BufferedOutputStream outStream = null;
        boolean success = false;
        try {
            logger.info("当前路径："+fClient.printWorkingDirectory());
            outStream = new BufferedOutputStream(new FileOutputStream(localFilePath));
            fClient.enterLocalPassiveMode();
            success = fClient.retrieveFile(remoteFileName, outStream);
            if (success) {
                logger.info(ftpname + " " + url + " 文件下载:" + remoteFileName+ " 成功");
            } else {
                logger.info(ftpname + " " + url + " 文件下载:" + remoteFileName+ " 失败:");
            }
        } catch (Exception e) {
            logger.error(ftpname + " " + url + " 文件下载:" + remoteFileName+ " 失败,异常:" , e);
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }

    /**
     *
     * @param remoteDir  例如 ："document"+File.separator+"Java"
     * @return
     * @throws IOException
     */
    public static int mkdirs(String remoteDir) throws IOException{
        return fClient.mkd(remoteDir);
    }

    /**
     *
     * @param localPath
     * @param remoteFileName 不能包含路径，如果需要上传到指定的问价夹需要进入指定文件夹在上传
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static boolean storeUniqueFile(String localPath,String remoteFileName) throws FileNotFoundException,IOException{
        logger.info("当前路径："+fClient.printWorkingDirectory());
        boolean result=fClient.storeUniqueFile(remoteFileName,new FileInputStream(new File(URLDecoder.decode(localPath,"utf-8"))));
        if(result){
            logger.info("上传文件成功storeUniqueFile");
            logger.info("当前路径："+fClient.printWorkingDirectory());
        }else{
            logger.warn("上传文件失败");
        }
        return  result;
    }

    /**
     *
     * @param localPath
     * @param remotePath 可以包含路径
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static boolean storeFile(String localPath,String remotePath) throws FileNotFoundException,IOException{
        logger.info("当前路径："+fClient.printWorkingDirectory());
        boolean result= fClient.storeFile(remotePath,new FileInputStream(new File(URLDecoder.decode(localPath,"utf-8"))));
        if(result){
            logger.info("上传文件成功storeFile");
            logger.info("当前路径："+fClient.printWorkingDirectory());
        }else{
            logger.warn("上传文件失败");
        }
        return  result;
    }
    public static void main(String[] args) throws Exception {
        //遍历所有的文件
        FTPClientUtil.listFiles(true);
        FTPClientUtil.mkdirs("document"+File.separator+"Java");
        FTPClientUtil.mkdirs("document"+File.separator+"Python");
        FTPClientUtil.mkdirs("document"+File.separator+"Del");
        List<String> listFileName=new ArrayList<>();
        String localFilePath=FTPClientUtil.class.getClass().getResource("/").getPath()+"config.properties";
        FTPClientUtil.changeDir("document");
        FTPClientUtil.changeDir("Del");
        int index=localFilePath.indexOf(":");
        if(index>0){
            logger.info("可能为windows系统去掉第一个/");
            localFilePath=localFilePath.substring(1);
        }else{
            logger.info("非windows系统");
        }
        FTPClientUtil.storeUniqueFile(localFilePath,File.separator+"document"+File.separator+"Del"+File.separator+"config.properties");
        FTPClientUtil.changeToRootDirectory();
        FTPClientUtil.listFilesStr(true,"document"+File.separator+"Java",listFileName);
        logger.info("listFileName:"+listFileName);
        listFileName.clear();
        FTPClientUtil.listFilesStr(listFileName);
        logger.info("listFileName:"+listFileName);
        FTPClientUtil.downloadFile("d:\\config.properties","document"+File.separator+"Del"+File.separator+"config.properties");
        FTPClientUtil.storeFile("d:\\config.properties","document"+File.separator+"Java"+File.separator+"config.properties");
        FTPClientUtil.close();
    }
}
