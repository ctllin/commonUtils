package com.ctl.utils.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.ctl.utils.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 * @descritpion \u521B\u5EFA\u6570\u636E\u5E93\u8FDE\u63A5\u7684\u7EBF\u7A0B\u7C7B
 */
class CreateConnection extends Thread {
    LinkedBlockingQueue<Connection> queue = ThreadLocalBlockingQueueUtils.getQueue();
    //LogUtil log = new LogUtil();
    Logger logger = LoggerFactory.getLogger(CreateConnection.class);

    public synchronized void run() {
        logger.info("CreateConnection.run(),\u8FDB\u5165\u521B\u5EFA\u6570\u636E\u5E93\u8FDE\u63A5\u7684\u7EBF\u7A0Brun\u65B9\u6CD5");
        boolean result = false;
        List<Long> list = new ArrayList<Long>();
        while (true) {
            try {
                Random rand = new Random();
                int num = ThreadLocalBlockingQueueUtils.getThreadPoolMaxNum()
                        - ThreadLocalBlockingQueueUtils.getThreadPoolMinNum();
                int randSize = ThreadLocalBlockingQueueUtils
                        .getThreadPoolMinNum() + rand.nextInt(num) + 1;
                if (queue.size() >= randSize) {
                    Thread.sleep(100);
                    logger.info("CreateConnection.run(),\u5DF2\u8FBE\u5230\u6700\u5927\u8FDE\u63A5\u6570randSize="
                            + randSize);
                    continue;
                }
                Connection conn = null;
                long start;
                long end;
                start = System.currentTimeMillis();
                conn = DriverManager.getConnection(
                        ThreadLocalBlockingQueueUtils.getUrl(),
                        ThreadLocalBlockingQueueUtils.getUsername(),
                        ThreadLocalBlockingQueueUtils.getPassword());
                end = System.currentTimeMillis();
                long time = end - start;
                if (conn != null) {
                    result = queue.offer(conn, 1, TimeUnit.SECONDS);
                    list.add(time);
                    long allTime = 0;
                    for (int i = 0; i < list.size(); i++) {
                        allTime = allTime + list.get(i);
                    }
                    logger.info("CreateConnection.run(),DriverManager.getConnection() \u7528\u65F6\u3010" + time
                            + "\u3011 \u5E73\u5747\u7528\u65F6\u3010" + allTime / list.size() + "\u3011");
                } else {
                    // System.out.println("DriverManager.getConnection is null");
                    logger.info("CreateConnection.run(),DriverManager.getConnection()\u8FD4\u56DE null");
                    continue;
                }
                if (result == false) {
                    Thread.sleep(100);
                    logger.info("CreateConnection.run(),\u5DF2\u8FBE\u5230\u6700\u5927\u8FDE\u63A5\u6570queue.size()="
                            + queue.size());
                    logger.info("CreateConnection.run()" +
                            "\u5DF2\u8FBE\u5230\u6700\u5927\u8FDE\u63A5\u6570queue.size()="
                            + queue.size());
                    // System.out.println("\u5DF2\u7ECF\u6EE1\u4E86size=\u3010"
                    // + queue.size() + "\u3011");
                } else {
                    logger.info("CreateConnection.run(),\u3010" + queue.size() + "\u3011"
                            + "createConnection success:" + conn);
                    // System.out.println("\u3010" + queue.size() + "\u3011"
                    // + "createConnection success:" + conn);
                    logger.info("CreateConnection.run() \u3010" + queue.size() + "\u3011"
                            + "createConnection success:" + conn);
                }
            } catch (InterruptedException e) {
                // e.printStackTrace();
                logger.info("getConnection" + e.getMessage());
                // System.err.println(e.getMessage());
            } catch (SQLException e) {
                logger.error("getConnection" + e.getMessage());
                // e.printStackTrace();
                // System.err.println(e.getMessage());
            }
        }
    }
}

public class ThreadLocalBlockingQueueUtils {
    private static Logger logger = LoggerFactory.getLogger(ThreadLocalBlockingQueueUtils.class);
    private static ThreadLocal<LinkedBlockingQueue<Connection>> queueHoder = new ThreadLocal<LinkedBlockingQueue<Connection>>();
    public static int num = 0;
    private static String driver;
    private static String url;
    private static String username;
    private static String password;
    // \u7EBF\u7A0B\u6C60\u6700\u5927\u6570\u636E\u5E93\u8FDE\u63A5\u6570
    private static int threadPoolMaxNum;
    // \u7EBF\u7A0B\u6C60\u6700\u5C0F\u6570\u636E\u5E93\u8FDE\u63A5\u6570(LinkedBlockingQueue\u7EF4\u6301\u7684\u6700\u5C0F\u503C\uFF0C\u800C\u662F\u5C11\u4E8E\u65F6\u4E0D\u65AD\u521B\u5EFA)
    private static int threadPoolMinNum;
    // \u6570\u636E\u5E93\u7C7B\u578Boracle mysql db2
    private static String databasetype;

    public static int getThreadPoolMaxNum() {
        return threadPoolMaxNum;
    }

    public static int getThreadPoolMinNum() {
        return threadPoolMinNum;
    }

    public static String getUrl() {
        return url;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    static {
        databasetype = ConfigUtils.getType("databasetype");
        threadPoolMaxNum = Integer.parseInt(ConfigUtils
                .getType("threadPoolMaxNum"));
        threadPoolMinNum = Integer.parseInt(ConfigUtils
                .getType("threadPoolMinNum"));
        if (databasetype.equals("mysql")) {
            driver = ConfigUtils.getType("mysql.driver");
            url = ConfigUtils.getType("mysql.url");
            username = ConfigUtils.getType("mysql.username");
            password = ConfigUtils.getType("mysql.password");
        } else if (databasetype.equals("oracle")) {
            driver = ConfigUtils.getType("oracle.driver");
            url = ConfigUtils.getType("oracle.url");
            username = ConfigUtils.getType("oracle.username");
            password = ConfigUtils.getType("oracle.password");
        }
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        CreateConnection createConn = new CreateConnection();
        createConn.setDaemon(true);
        createConn.start();
        logger.info("static{},\u6267\u884C\u521D\u59CB\u5316");
        // javax.swing.JOptionPane.showMessageDialog(null, "static{}");
    }

    /**
     * @return
     * @description \u53D6\u51FALinkedBlockingQueue\u961F\u5217
     */
    public static synchronized LinkedBlockingQueue<Connection> getQueue() {
        LinkedBlockingQueue<Connection> queue = queueHoder.get();
        if (queue == null) {
            queue = new LinkedBlockingQueue<Connection>(threadPoolMaxNum);
            queueHoder.set(queue);
            return queue;
        }
        return queue;
    }

    public static Connection getConnection() {
        // System.out.println("\u8FDB\u5165getConnection");
        class GetConnectionClazz extends Thread {
            LinkedBlockingQueue<Connection> queue = ThreadLocalBlockingQueueUtils
                    .getQueue();
            // \u7528\u4E8E\u5C06\u83B7\u53D6\u7684\u6570\u636E\u5E93\u8FDE\u63A5\u4F20\u5230\u7EBF\u7A0B\u5916\u90E8
            private Connection conn;
            // \u7528\u4E8E\u5C06\u83B7\u53D6\u7684\u5F53\u524D\u961F\u5217\u5927\u5C0F\u4F20\u5230\u7EBF\u7A0B\u5916\u90E8
            private int queueSize;

            public int getQueueSize() {
                return queueSize;
            }

            public Connection getConn() {
                return conn;
            }

            public synchronized void run() {
                // System.out.println("\u8FDB\u5165getConnection run()");
                try {
                    // System.err.println("-----"+conn+"--------");
                    while (conn == null) {// \u5F88\u91CD\u8981\u6CA1\u6709\u8BE5while\u5FAA\u73AF\u5F53\u6309F5\u4E0D\u65AD\u5237\u65B0\u65F6\uFF0C\u53EA\u8981\u6709\u4E00\u4E2A\u53D6\u51FA\u6765\u4E3A\u7A7A\u540E\u9762\u7684\u5168\u4E3A\u7A7A
                        conn = queue.poll(2, TimeUnit.SECONDS);
                    }
                    queueSize = queue.size();
                    // System.err.println("*******"+conn+"*********");
                    // if (conn != null) {
                    // System.err.println("\u3010" + queue.size() + "\u3011"
                    // + "getConnecion\u6210\u529F\uFF1A" + conn);
                    // }
                } catch (InterruptedException e) {
                    logger.error("getConnection" + e.getMessage());
                }
            }
        }
        GetConnectionClazz jj = new GetConnectionClazz();
        jj.start();
        try {
            jj.join();
        } catch (InterruptedException e) {
            logger.error("getConnection()" + e.getMessage());
        }
        logger.info("getConnection(),\u3010" + jj.getQueueSize()
                + "\u3011" + "getConnecion\u6210\u529F\uFF1A" + jj.getConn());
        return jj.getConn();
    }

    public static void main(String[] args) throws SQLException {
        CreateConnection cc = new CreateConnection();
        cc.start();
        for (; ; ) {
            try {
                long start = System.currentTimeMillis();
                Connection conn = getConnection();
//				System.out.println("/*************\u3010"
//						+ ThreadLocalBlockingQueueUtils.getQueue().size()
//						+ "\u3011" + conn + "*************/");
                Thread.sleep(50);
                long end = System.currentTimeMillis();
                System.out.println("\u7528\u65F6:" + (end - start));
                if (conn != null) {
                    conn.close();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
