package com.ctl.utils;

import com.ctl.utils.json.JsonDateValueProcessor;
import com.ctl.utils.json.JsonNumberValueProcessor;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.io.IOUtils;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>Title: FastFdsTest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.hanshow.com</p>
 *
 * @author guolin
 * @version 1.0
 * @date 2018-08-14 21:04
 */
public class FastFdsUtil {
    static Logger logger = LoggerFactory.getLogger(FastFdsUtil.class);
    static JsonConfig jsonConfig = new JsonConfig();
    static {
        try {
            String confPath = FastFdsUtil.class.getClassLoader().getResource("fdfs_client.conf").getPath();
            ClientGlobal.init(confPath);
            Map<String, Object> map = new HashMap<>();
            jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
            jsonConfig.registerJsonValueProcessor(Timestamp.class, new JsonDateValueProcessor());
            jsonConfig.registerJsonValueProcessor(Integer.class, new JsonNumberValueProcessor());
            jsonConfig.registerJsonValueProcessor(Long.class, new JsonNumberValueProcessor());
            jsonConfig.registerJsonValueProcessor(Byte.class, new JsonNumberValueProcessor());
            jsonConfig.registerJsonValueProcessor(Float.class, new JsonNumberValueProcessor());
            jsonConfig.registerJsonValueProcessor(Double.class, new JsonNumberValueProcessor());
        } catch (IOException e) {
            logger.error("FastFds初始化失败", e);
        } catch (MyException e) {
            logger.error("FastFds初始化失败", e);
        }
    }

    public String[] fileLocalUpload(String localFilePath) {
        return fileLocalUpload(localFilePath, null);
    }

    public String[] fileLocalUpload(String localFilePath, NameValuePair[] nvp) {

        try {
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;

            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
//          NameValuePair nvp = new NameValuePair("age", "18");
//            NameValuePair nvp[] = new NameValuePair[]{
//                    new NameValuePair("age", "18"),
//                    new NameValuePair("sex", "male")
//            };
            String fileIds[] = storageClient.upload_file(localFilePath, "png", nvp);
            logger.info("fileIds.length={}", fileIds.length);
            logger.info("组名={}", fileIds[0]);
            logger.info("路径={} ", fileIds[1]);
            return fileIds;
        } catch (FileNotFoundException e) {
            logger.error("FastFds上传失败", e);
        } catch (IOException e) {
            logger.error("FastFds上传失败", e);
        } catch (MyException e) {
            logger.error("FastFds上传失败", e);
        }
        return null;
    }

    public boolean fileServerDownload(String remoteFilename) {
        return fileServerDownload("group1", remoteFilename, System.getProperty("java.io.tmpdir") + UUID.randomUUID().toString() + ".tmp");
    }

    public boolean fileServerDownload(String groupName, String remoteFilename, String localDownloadPath) {
        try {
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            byte[] b = storageClient.download_file(groupName, remoteFilename);
            IOUtils.write(b, new FileOutputStream(localDownloadPath));
            return true;
        } catch (Exception e) {
            logger.error("FastFds下载失败", e);
            return false;
        }
    }

    public FileInfo getFileInfo(String remoteFilename) {
        return getFileInfo("group1", remoteFilename);
    }

    public FileInfo getFileInfo(String groupName, String remoteFilename) {
        try {
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            FileInfo fi = storageClient.get_file_info(groupName, remoteFilename);
            System.out.println(fi.getSourceIpAddr());
            System.out.println(fi.getFileSize());
            System.out.println(fi.getCreateTimestamp());
            System.out.println(fi.getCrc32());
            return fi;
        } catch (Exception e) {
            logger.error("FastFds获取文件信息失败", e);
            return null;
        }
    }

    public NameValuePair[] getFileMate(String remoteFilename) {
        return getFileMate("group1", remoteFilename);
    }

    public NameValuePair[] getFileMate(String groupName, String remoteFilename) {
        try {
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            NameValuePair nvps[] = storageClient.get_metadata(groupName, remoteFilename);
            for (NameValuePair nvp : nvps) {
                System.out.println(nvp.getName() + ":" + nvp.getValue());
            }
            return nvps;
        } catch (Exception e) {
            logger.error("FastFds获取文件描述信息失败", e);
            return null;
        }
    }

    public boolean delete(String remoteFilename) {
        return delete("group1", remoteFilename);
    }

    public boolean delete(String groupName, String remoteFilename) {
        try {
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            int i = storageClient.delete_file("group1", remoteFilename);
            logger.info(i == 0 ? "删除成功" : "删除失败:" + i);
            return i == 0 ? true : false;
        } catch (Exception e) {
            logger.error("FastFds删除失败", e);
            return false;
        }
    }

    public static void main(String[] args) {
        FastFdsUtil fastFdsTest = new FastFdsUtil();
        logger.info(JSONObject.fromObject(fastFdsTest.getFileInfo("M00/00/00/wKgqHVty2OeAACY6AAE0vHMtwgw725.png")).toString());
        fastFdsTest.fileLocalUpload("E:\\fAPP\\_20180814101019.png");
        logger.info("downresult={}", fastFdsTest.fileServerDownload("group1", "M00/00/00/wKgqHVty1dWANx6HAAE0vHMtwgw855.png", "e:\\test.png"));
        logger.info("delresult={}", fastFdsTest.delete("M00/00/00/wKgqHVty18mAV-6UAAE0vHMtwgw879.png"));
    }
}