package com.ctl.utils.file;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * 监控某一个磁盘变化
 * Created by jason on 2017/2/14.
 */
public class WatchDirService {
    static Logger logger = LoggerFactory.getLogger(WatchDirService.class);
    private WatchService watchService;
    private boolean notDone = true;
    public WatchDirService(String dirPath){
        init(dirPath);
    }

    private void init(String dirPath) {
        Path path = Paths.get(dirPath);
        try {
            watchService = FileSystems.getDefault().newWatchService();
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,StandardWatchEventKinds.ENTRY_MODIFY,StandardWatchEventKinds.ENTRY_DELETE);
        } catch (IOException e) {
           logger.error("WatchDirService 调用init方式方法失败",e);
        }
    }

    public void start(){
        logger.info("...watch...");
        while (notDone){
            try {
                WatchKey watchKey = watchService.poll(60, TimeUnit.SECONDS);
                logger.info("...change...");
                if(watchKey != null){
                    List<WatchEvent<?>> events = watchKey.pollEvents();
                    for (WatchEvent event : events){
                        WatchEvent.Kind<?> kind = event.kind();
                        if (kind == StandardWatchEventKinds.OVERFLOW){
                            continue;
                        }
                        WatchEvent<Path> ev = event;
                        Path path = ev.context();
                        if(kind == StandardWatchEventKinds.ENTRY_CREATE){
                            logger.info("create " + path.getFileName());
                        }else if(kind == StandardWatchEventKinds.ENTRY_MODIFY){
                            logger.info("modify " + path.getFileName());
                        }else if(kind == StandardWatchEventKinds.ENTRY_DELETE){
                            logger.info("delete " + path.getFileName());
                        }
                    }
                    if(!watchKey.reset()){
                        logger.info("exit watch server");
                        break;
                    }
                }
            } catch (InterruptedException e) {
                logger.error("监控异常退出",e);
                return;
            }
        }
    }
    public static void main(String[] args) {
    	  WatchDirService watchDirServer = new WatchDirService("d://");
          watchDirServer.start();
	}
}
