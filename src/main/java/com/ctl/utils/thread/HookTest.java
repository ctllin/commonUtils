package com.ctl.utils.thread;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <p>Title: HookTest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.hanshow.com</p>
 *
 * @author guolin
 * @version 1.0
 * @date 2018-11-29 21:14
 */
public class HookTest {
    //private final static String LOCK_PATH = "/home/ctl/locks";
    private final static String LOCK_PATH = ".";
    private final static String LOCK_FILE = ".lock";
    private final static String PERMISSIONS = "rw-------";

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("The program received kill SIGNAL.");
            getLockFile().toFile().delete();
        }));
        try {
            checkRunnning();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (; ; ) {
            try {
                TimeUnit.MILLISECONDS.sleep(1);
                System.out.println("program is runnning.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void checkRunnning() throws IOException {
        Path path = getLockFile();
        if (path.toFile().exists()) {
            throw new RuntimeException("The program already running.");
        }
        Set<PosixFilePermission> perms = PosixFilePermissions.fromString(PERMISSIONS);
        Files.createFile(path, PosixFilePermissions.asFileAttribute(perms));
    }

    private static Path getLockFile() {
        return Paths.get(LOCK_PATH, LOCK_FILE);
    }
}
