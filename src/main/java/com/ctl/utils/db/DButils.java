package com.ctl.utils.db;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.lang.reflect.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.ctl.utils.BeanUtil;
import com.ctl.utils.ConfigUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 * @category
 */
@SuppressWarnings({"rawtypes", "unchecked", "unused", "deprecation"})
public class DButils {
    private static ThreadLocal<Connection> connHolder;
    private static String driver;
    private static String url;
    private static String username;
    private static String password;
    //\u6570\u636E\u5E93\u7C7B\u578Boracle mysql db2
    private static String databasetype;
    private static Connection conn;
    private static Logger logger = LoggerFactory.getLogger(DButils.class);
    private static boolean autoCommit;

    static {
        connHolder = new ThreadLocal<Connection>();
        try {
            databasetype = ConfigUtils.getType("databasetype");
            logger.info("数据库类型" + databasetype);
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
            } else if ("sqlite".equals(databasetype)) {
                driver = "org.sqlite.JDBC";
                url = ConfigUtils.getType("sqlite.url");//"jdbc:sqlite:E:\\Users\\ctl\\Desktop\\xlh.db";
            }
        } catch (Exception e) {
            logger.info("DButils.staic{} \u4ECE\u8D44\u6E90\u6587\u4EF6\u4E2D\u83B7\u53D6\u6570\u636E\u5E93\u53C2\u6570\u5931\u8D25!");
        }
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            logger.info("DButils.staic{} Class.forName(driver) error");
        }
    }

    static class KeyValue {
        Object key;
        Object value;

        public Object getKey() {
            return key;
        }

        public void setKey(Object key) {
            this.key = key;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public KeyValue() {
            super();
        }

        public KeyValue(Object key, Object value) {
            super();
            this.key = key;
            this.value = value;
        }

        public String toString() {
            return this.key + ":" + this.value;
        }

    }

    /**
     * @param c                                    Person.class
     * @param columns                              \u8981\u5339\u914D\u7684\u5217\u540D
     * @param \u5217\u6240\u5BF9\u5E94\u7684\u503C
     * @category\u591A\u6761\u4EF6\u7CBE\u786E\u67E5\u8BE2 select * from person where id=1 and name='ctl'
     */
    public static List getEntitysByColumnsAnd(Class c, String[] columns,
                                              Object[] conditionsValues) {
        String tableName = c.getSimpleName().toLowerCase();// person \u8868\u7684\u540D\u5B57
        if (!isTableExist(tableName)) {
            System.err.println("\u8868'" + tableName + "'\u4E0D\u5B58\u5728");
            logger.info("DButils.getEntitysByColumnsAnd()" + tableName
                    + " is not isExist! \u9000\u51FA");
            return null;
        }
        List<String> columnList = getColumns(c);
        for (int i = 0; i < columns.length; i++) {
            if (!columnList.contains(columns[i])) {
                System.err.println("\u5217\u540D'" + columns[i] + "'\u4E0D\u5B58\u5728");
                logger.info("DButils.getEntitysByColumnsAnd()" + columns[i]
                        + " is not isExist! \u9000\u51FA");
                return null;
            }
        }
        if (columns.length != conditionsValues.length) {
            System.err.println("\u5217\u4E2A\u6570\u4E0E\u5217\u5BF9\u5E94\u7684value\u7684\u4E2A\u6570\u4E0D\u540C");
            logger.info("DButils.getEntitysByColumnsAnd() \u5217\u4E2A\u6570\u4E0E\u5217\u5BF9\u5E94\u7684value\u7684\u4E2A\u6570\u4E0D\u540C! \u9000\u51FA");
            return null;
        }
        List list = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Field[] fields = c.getFields();// \u83B7\u53D6\u6240\u6709\u7684public\u5C5E\u6027
        if (fields.length == 0) {
            // fields=c.getDeclaredFields();//\u83B7\u53D6\u6240\u6709\u7684private\u5C5E\u6027
            fields = c.getDeclaredFields();// \u83B7\u53D6get set \u65B9\u6CD5
        }
        StringBuilder sql = new StringBuilder("select * from " + tableName)
                .append(" where");
        for (int i = 0; i < columns.length; i++) {
            if (i < columns.length - 1) {
                sql.append(" " + columns[i]).append("=? and");
            } else {
                sql.append(" " + columns[i]).append("=?");
            }
        }
        logger.info("DButils.getEntitysByColumnsAnd() sql:"
                + sql.toString());
        try {
            if (!isTableExist(tableName)) {
                System.err.println("\u8868\u4E0D\u5B58\u5728");
                return list;
            }
            ps = (PreparedStatement) getConnection().prepareStatement(
                    sql.toString());
            for (int i = 0; i < columns.length; i++) {
                ps.setObject(i + 1, conditionsValues[i]);
            }
            rs = ps.executeQuery();
            logger.info("DButils.getEntitysByColumnsAnd() sql:"
                    + ps.toString().split(":")[1]);
            System.out.println("%n" + ps.toString().split(":")[1]);
            logger.info("DButils.getEntitysByColumnsAnd() sql:"
                    + ps.toString().split(":")[1]);
            Object obj = null;
            while (rs.next()) {
                obj = c.newInstance();
                for (int k = 0; k < fields.length; k++) {
                    fields[k].setAccessible(true);
                    fields[k].set(obj, rs.getObject(k + 1));
                }
                list.add(obj);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            logger.info("DButils.getEntitysByColumnsAnd() getEntitysByColumnsAnd error");
        } finally {
            rs = null;
            ps = null;
        }
        return list;

    }

    /**
     * @param c
     * @param columns
     * @param
     * @category \u591A\u6761\u4EF6\u6A21\u7CCA\u67E5\u8BE2
     */
    public static List getEntitysByColumnsOr(Class c, String[] columns,
                                             Object[] conditionsValues) {
        String tableName = c.getSimpleName().toLowerCase();// person \u8868\u7684\u540D\u5B57
        if (!isTableExist(tableName)) {
            System.err.println("\u8868'" + tableName + "'\u4E0D\u5B58\u5728");
            logger.info("DButils.getEntitysByColumnsOr()" + tableName
                    + " is not isExist! \u9000\u51FA");
            return null;
        }
        List<String> columnList = getColumns(c);
        for (int i = 0; i < columns.length; i++) {
            if (!columnList.contains(columns[i])) {
                System.err.println("\u5217\u540D'" + columns[i] + "'\u4E0D\u5B58\u5728");
                logger.info("DButils.getEntitysByColumnsOr()" + columns[i]
                        + " is not isExist! \u9000\u51FA");
                return null;
            }
        }
        if (columns.length != conditionsValues.length) {
            System.err.println("\u5217\u4E2A\u6570\u4E0E\u5217\u5BF9\u5E94\u7684value\u7684\u4E2A\u6570\u4E0D\u540C");
            logger.info("DButils.getEntitysByColumnsOr() \u5217\u4E2A\u6570\u4E0E\u5217\u5BF9\u5E94\u7684value\u7684\u4E2A\u6570\u4E0D\u540C! \u9000\u51FA");
            return null;
        }
        List list = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Field[] fields = c.getFields();// \u83B7\u53D6\u6240\u6709\u7684public\u5C5E\u6027
        if (fields.length == 0) {
            // fields=c.getDeclaredFields();//\u83B7\u53D6\u6240\u6709\u7684private\u5C5E\u6027
            fields = c.getDeclaredFields();// \u83B7\u53D6get set \u65B9\u6CD5
        }
        StringBuilder sql = new StringBuilder("select * from " + tableName)
                .append(" where");
        for (int i = 0; i < columns.length; i++) {
            if (i < columns.length - 1) {
                sql.append(" " + columns[i]).append("=? or");
            } else {
                sql.append(" " + columns[i]).append("=?");
            }
        }
        logger.info("DButils.getEntitysByColumnsOr() sql:"
                + sql.toString());
        try {
            if (!isTableExist(tableName)) {
                System.err.println("\u8868\u4E0D\u5B58\u5728");
                return list;
            }
            ps = (PreparedStatement) getConnection().prepareStatement(
                    sql.toString());
            for (int i = 0; i < columns.length; i++) {
                ps.setObject(i + 1, conditionsValues[i]);
            }
            rs = ps.executeQuery();
            logger.info("DButils.getEntitysByColumnsOr() sql:"
                    + ps.toString().split(":")[1]);
            System.out.println("%n" + ps.toString().split(":")[1]);
            Object obj = null;
            while (rs.next()) {
                obj = c.newInstance();
                for (int k = 0; k < fields.length; k++) {
                    fields[k].setAccessible(true);
                    fields[k].set(obj, rs.getObject(k + 1));
                }
                list.add(obj);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            logger.info("DButils.getEntitysByColumnsOr() getEntitysByColumnsOr error");
        } finally {
            rs = null;
            ps = null;
        }
        return list;

    }

    /**
     * @author Administrator
     * @category start mysql server
     */
    public static void startMySqlServer() {
        try {
            Runtime.getRuntime().exec("net start mysql");
            logger.info("DButils.startMySqlServer() ");
        } catch (IOException e) {
            logger.info("DButils.startMySqlServer() mysql server start error");
        }
    }

    /**
     * @param string
     * @category\u521D\u59CB\u5316\u6570\u636E\u5E93
     */
    public static void init(String string, String databasetype) {
        if (databasetype.equals("mysql")) {
            startMySqlServer();
        } else if (databasetype.equals("oracle")) {
            startOracleServer();
        }
        new InitJFrame(string, databasetype);
    }

    private static void startOracleServer() {
        try {
            Runtime.getRuntime().exec(
                    new String[]{"net start OracleOraDb11g_home1TNSListener",
                            " net start OracleServiceORCL"});
            logger.info("DButils.startOracleServer ");
        } catch (IOException e) {
            logger.info("DButils.startOracleServer oracle server start error");
        }
    }

    /**
     * @author Administrator
     * @category stop mysql server
     */
    public static void stopMySqlServer() {
        try {
            Runtime.getRuntime().exec("net stop mysql");
        } catch (IOException e) {
            logger.info("DButils.stopMySqlServer() mysql server stop error");
            System.err.println("mysql server stop error");
        }
    }

    /**
     * @category \u662F\u5426\u6267\u884Cinit\u65B9\u6CD5 \u82E5isExcuteInit\u4E3Atrue\u8FD9\u6267\u884C \u5426\u5219\u4E0D\u6267\u884C
     * @param isExcuteInit
     */
//	public static void isExecuteInit(boolean isExcuteInit) {
//		if (isExcuteInit) {
//			RegisterUtil.putIntSystemRoot("isInit", 1);
//		} else {
//			RegisterUtil.putIntSystemRoot("isInit", 0);
//		}
//	}

    /**
     * @param autoCommit
     * @category \u6570\u636E\u5E93\u662F\u5426\u81EA\u52A8\u63D0\u4EA4
     */
    public static void setAutoCommit(boolean autoCommit) {
        DButils.autoCommit = autoCommit;
//		if (autoCommit) {
//			RegisterUtil.putIntSystemRoot("autoCommit", 1);// 1\u662F\u81EA\u52A8\u63D0\u4EA4
//		} else {
//			RegisterUtil.putIntSystemRoot("autoCommit", 0);// 0\u662F\u9700\u8981\u6267\u884Ccommitt
//		}
    }

    /**
     * @return boolean
     * @category get isAutoCommit
     */
    public static boolean getAutoCommit() {
//		int flag = RegisterUtil.getIntFromSystemRoot("autoCommit", 1);
        return false;
    }

    /**
     * @category \u5982\u679CautoCommit=true \u5219\u4E0D\u505A\u64CD\u4F5C\u5426\u5219\u6267\u884Cconn.commit()
     */
    public static void commit() {
//		int isCommit = RegisterUtil.getIntFromSystemRoot("autoCommit", 1);
//		if (isCommit != 1) {
        try {
            conn.commit();
        } catch (SQLException e) {
            logger.info("DButils.commit() commit error");
            System.out.println("db commit false");
            exceptionPrint(e);
        }

//		}
    }

    /**
     * @return Connection
     * @throws SQLException
     * @throws ClassNotFoundException
     */

    public static Connection getConnection() {
        conn = connHolder.get();
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(url, username, password);
                //conn.setAutoCommit(getAutoCommit());
                conn.setAutoCommit(false);
                connHolder.set(conn);
            }
        } catch (Exception e) {
            logger.info("DButils.getConnection{} getConnection error");
            logger.error(e.getMessage());
            init("getConnection", databasetype);
            exceptionPrint(e);
        }
        return conn;
    }

    /**
     * @return
     * @throws InterruptedException
     * @description \u91C7\u7528\u4E86\u7EBF\u7A0B\u6C60\u6280\u672F\u6700\u591A150\u4E2A
     */
    public static Connection getConnection2() throws InterruptedException {
        return ThreadLocalBlockingQueueUtils.getConnection();
    }

    /**
     * @param e
     * @category \u6253\u5370\u9519\u8BEF
     */
    public static void exceptionPrint(Exception e) {
        StackTraceElement[] errors = e.getStackTrace();
        System.out.println();
        for (int i = 0; i < errors.length; i++) {
            if (DButils.class.getName().equals(errors[i].getClassName())) {
                System.out.print("Exception: " + getCurrentDataTime() + "  "
                        + errors[i].getClassName() + "."
                        + errors[i].getMethodName() + "() line:"
                        + errors[i].getLineNumber() + "%n");
            }
        }
        e.printStackTrace();
        System.out.println();
    }

    /**
     * @param sql
     * @return int \u5982\u679C\u8FD4\u56DE-1\u4EE3\u8868\u51FA\u9519
     * @category \u6839\u636Esql\u5F97\u5230\u8BB0\u5F55\u603B\u6570
     */
    public static int getCount(String sql) {
        try {
            ResultSet rs = getConnection().createStatement().executeQuery(sql);
            int count = -1;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            return count;
        } catch (Exception e) {
            logger.info("DButils.getCount{} getCount error sql:" + sql);
            exceptionPrint(e);
        }
        return -1;
    }

    /**
     * @param c Person.class
     * @return -1 \u51FA\u9519 >=0 right
     * @see \u83B7\u53D6\u67D0\u5F20\u8868\u7684\u603B\u8BB0\u5F55\u6570
     */
    public static int getCount(Class c) {
        String tableName = c.getSimpleName().toLowerCase();// person \u8868\u7684\u540D\u5B57
        if (!isTableExist(tableName)) {
            System.err.println("\u8868'" + tableName + "'\u4E0D\u5B58\u5728");
            return -1;
        }
        int count = -1;
        try {
            ResultSet rs = getConnection().prepareStatement(
                    "select count(" + getColumns(c).get(0) + ") from "
                            + tableName).executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            return count;
        } catch (Exception e) {
            logger.info("DButils.getCount{} getCount error");
            e.printStackTrace();
        }
        return count;
    }

    /**
     * @category \u5F39\u51FA\u6570\u636E\u5E93\u914D\u7F6E\u7A97\u53E3
     */
    public static void init() {
        init(null, databasetype);
    }

    /**
     * @throws SQLException
     * @category close Connection
     */
    public static void close() throws SQLException {
        conn.close();
        conn = null;
        connHolder.set(null);
    }


    public static int createTable(Class c) {
        String tableName = c.getSimpleName().toLowerCase();// person
        return 0;
    }

    /**
     * @param c   \u53C2\u6570\u4F8B\u5982Person.class
     * @param obj \u53C2\u6570\u4F8B\u5982 person obj\u4E3A\u67D0\u4E00\u5B9E\u4F8B\u5BF9\u8C61 // Person person=new Person();
     * @category \u8BE5\u65B9\u6CD5\u7528\u4E8E\u5411\u6570\u636E\u5E93\u4E2D\u63D2\u5165\u6761\u6570\u636E \u63D2\u5165\u7684\u5BF9\u8C61\u662F\u4E00\u4E2A\u5B9E\u4F53\u7C7B\u7684\u5BF9\u8C61
     */
    public static void insertEntity(Class c, Object obj) {
        if (obj == null || c.getSimpleName().equals(obj.getClass().getName()))
            return;
        Field[] fields = obj.getClass().getDeclaredFields();
        int fieldSize = fields.length;
        String tableName = c.getSimpleName().toLowerCase();// person
        String[] types1 = {"int", "java.lang.String", "boolean", "char",
                "float", "double", "long", "short", "byte", "date"};
        String[] types2 = {"java.lang.Integer", "java.lang.String",
                "java.lang.Boolean", "java.lang.Character", "java.lang.Float",
                "java.lang.Double", "java.lang.Long", "java.lang.Short",
                "java.lang.Byte", "java.util.Date"};

        StringBuffer sql = new StringBuffer("replace into " + tableName
                + " values(");
        for (int i = 0; i < fieldSize; i++) {
            sql.append("?,");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
        logger.info("DButils.insertEntity() insertEntity sql:" + sql);
        PreparedStatement ps = null;
        try {
            ps = getConnection().prepareStatement(sql.toString());
            for (int j = 0; j < fieldSize; j++) {
                fields[j].setAccessible(true);
                for (int i = 0; i < types1.length; i++) {
                    if (fields[j].getType().getName().equalsIgnoreCase(
                            types1[i])
                            || fields[j].getType().getName().equalsIgnoreCase(
                            types2[i])) {

                        if (fields[j].get(obj) != null
                                && !"".equals(fields[j].get(obj))
                                && !"null".equals(fields[j].get(obj))) {
                            // System.out.print(fields[j].getName() + ":"
                            // + fields[j].get(obj) + " ");
                            ps.setObject(j + 1, fields[j].get(obj));
                        } else {
                            // System.out.print(fields[j].getName() + ":"
                            // + fields[j].get(obj) + " ");
                            ps.setObject(j + 1, null);
                        }
                    }
                }
            }
            ps.executeUpdate();
            System.out.println("%nsql:" + ps.toString().split(":")[1].trim());
            logger.info("DButils.insertEntity() insertEntity sql:"
                    + ps.toString().split(":")[1].trim() + " Entity:" + obj);
            ps.close();
            ps = null;
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    /**
     * @param c           for example Person.class
     * @param primaryKeys primaryKeys\u4E3A\u4E3B\u952E,\u53C2\u6570\u987A\u5E8F\u548C\u8868\u4E2D\u4FDD\u6301\u4E00\u81F4 \u5982\u679Cid\uFF0C name \u4E3A\u4E3B\u952E \u7C7B\u540D\u4E3APerson \u5219
     *                    getEntity(Person.class,1,"name")
     * @return Object
     * @category \u6839\u636E\u4F20\u5165\u7684\u4E3B\u952E\u503C\u8FD4\u56DE\u4E00\u4E2A\u5B9E\u4F53\u5BF9\u8C61
     */
    public static Object getEntity(Class c, Object... primaryKeys) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseMetaData dmd = null;
        Object obj = null;// \u8981\u8FD4\u56DE\u7684\u5BF9\u8C61
        String tableName = c.getSimpleName().toLowerCase();// person \u8868\u7684\u540D\u5B57
        List primaryKeyNameList = new ArrayList();// \u5B58\u653E\u4ECE\u8868\u4E2D\u83B7\u53D6\u7684\u4E3B\u952E
        Field[] fields = c.getFields();// \u83B7\u53D6\u6240\u6709\u7684\u5C5E\u6027
        Method[] methods = null;
        if (fields.length == 0) {// \u5F53\u7C7B\u7684\u5C5E\u6027\u90FD\u662Fprivate\u65F6
            // fields=c.getDeclaredFields();//\u83B7\u53D6\u6240\u6709\u7684private\u5C5E\u6027
            methods = c.getDeclaredMethods();// \u83B7\u53D6get set \u65B9\u6CD5
        }
        StringBuilder sql = new StringBuilder("select * from " + tableName
                + " where ");
        try {
            obj = c.newInstance();
            if (!isTableExist(tableName)) {
                System.err.println("\u8868\u4E0D\u5B58\u5728");
                return obj;
            }
            dmd = getConnection().getMetaData();
            rs = dmd.getPrimaryKeys(null, null, tableName);
            while (rs.next()) {// \u83B7\u53D6\u6240\u6709\u7684\u4E3B\u952E
                sql.append(rs.getObject(4) + "=?");
                sql.append(" and ");
                primaryKeyNameList.add(rs.getObject(4));// \u5C06\u4ECE\u8868\u4E2D\u83B7\u53D6\u7684 \u4E3B\u952E\u5B57\u6BB5\u5B58\u5230 list\u4E2D\uFF0C
                // \u4E3B\u952E\u4F4D\u4E8E\u8868\u4E2D\u7B2C\u51E0\u5217=rs.getString(5)
            }
            sql.delete(sql.length() - 4, sql.length());
            if (!sql.toString().contains("where")) {
                System.err.println("\u6CA1\u6709\u627E\u5230\u4E3B\u952E");
                return obj;
            }
            ps = (PreparedStatement) getConnection().prepareStatement(
                    sql.toString());
            for (int l = 0; l < primaryKeyNameList.size(); l++) {
                ps.setObject(l + 1, primaryKeys[l]);
            }
            rs = ps.executeQuery();
            System.out.println(ps.toString().split(":")[1]);
            List<String> tableColumns = getColumns(c);
            if (rs.next()) {
                if (fields.length > 0) {// \u5982\u679C\u7C7B \u7684\u5C5E\u6027\u4E3Apublic
                    for (int k = 0; k < fields.length; k++) {
                        fields[k].set(obj, rs.getObject(k + 1));
                    }
                } else {// \u5982\u679C\u7C7B \u7684\u5C5E\u6027\u4E3Aprivate
                    for (int k = 0; k < methods.length; k++) {
                        for (int i = 0; i < tableColumns.size(); i++) {
                            if (methods[k].getName().equalsIgnoreCase(
                                    "set" + tableColumns.get(i))) {
                                methods[k].invoke(obj, rs
                                        .getObject(tableColumns.get(i)));
                            }
                        }
                    }
                }
            }
            rs.close();
            ps.close();
            rs = null;
            ps = null;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("DButils.getEntity() getEntity error talbename:"
                    + tableName);
        }
        return obj;
    }

    /**
     * @param tableName \u6807\u7684\u540D\u5B57
     * @return 0\u8868\u4E0D\u5B58\u5728 >=1\u8868\u5B58\u5728
     * @category \u8868\u4E0D\u5B58\u5728 1\u8868\u5B58\u5728
     */
    public static boolean isTableExist(String tableName) {
        int v = getCount("SELECT count(table_name) FROM information_schema.TABLES WHERE table_name='"
                + tableName + "' ");
        if (v >= 1) {
            return true;
        } else {
            System.err.println("\u8868 \u4E0D\u5B58\u5728 table not exist");
            logger.info("DButils.isTableExist() \u8868\u4E0D\u5B58\u5728");
            return false;
        }
    }

    /**
     * @param c Person.class (\u7C7B\u540D\u4E0E\u8868\u540D\u4E00\u81F4)
     * @return List
     * @category \u83B7\u53D6\u67D0\u4E2A\u8868\u4E2D\u6240\u6709\u7684\u5217\u540D
     */
    public static List<String> getColumns(Class c) {
        List<String> list = new ArrayList<String>();
        String tableName = c.getSimpleName().toLowerCase();// person \u8868\u7684\u540D\u5B57
        if (!isTableExist(tableName)) {
            System.err.println("talbe is not exist");
            return list;
        }
        String sql = "select COLUMN_NAME from information_schema.columns where table_name='"
                + tableName + "'";
        try {
            ResultSet rs = getConnection().createStatement().executeQuery(sql);
            while (rs.next()) {
                if (!list.contains(rs.getString(1))) {
                    list.add(rs.getString(1));// \u9632\u6B62\u4E0D\u540C\u6570\u636E\u5E93\u4E2D\u6709\u76F8\u540C\u7684\u8868\u540D
                }
            }
            rs.close();
        } catch (Exception e) {
            logger.info("DButils.getColumns() getColumns error ");
            e.printStackTrace();
        }
        return list;
    }

    /**
     * @return String now system date yyyy/MM/dd hh:mm:ss
     * @category \u8FD4\u56DE\u5F53\u524D\u65F6\u95F4 String
     */
    public static String getCurrentDataTime(String datetimeFormat) {
        Date date = new Date();
        SimpleDateFormat sdf = null;
        if (datetimeFormat == null || "".equals(datetimeFormat)) {
            sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        }
        sdf.format(date);
        return sdf.format(date);
    }

    /**
     * @return String now system date
     * @category \u8FD4\u56DE\u5F53\u524D\u65F6\u95F4 String
     */
    public static String getCurrentDataTime() {
        return getCurrentDataTime(null);
    }

    /**
     * @param
     * @return database name
     * @category \u8FD4\u56DE\u6570\u636E\u5E93\u7684\u540D\u5B57
     */
    public static String getDatabaseName(Class c) {
        String tableName = c.getSimpleName().toLowerCase();// person \u8868\u7684\u540D\u5B57
        try {
            ResultSet rs = getConnection().getMetaData().getPrimaryKeys(null,
                    null, tableName);
            String result = null;
            if (rs.next()) {
                System.out.println("database:" + rs.getString(1));
                result = rs.getString(1);
                rs.close();
                rs = null;
                return result;
            }

        } catch (Exception e) {
            System.err.println("\u83B7\u53D6\u6570\u636E\u5E93\u540D\u5931\u8D25");
            logger.info("DButils.getDatabaseName() getDatabaseName error");
            exceptionPrint(e);

        }
        return null;
    }

    /**
     * @param c Person\u3002class
     * @return int \u4E0B\u4E00\u4E2A\u81EA\u589E\u503C \u5982\u679C\u6CA1\u6709\u5219\u8FD4\u56DEnull
     * @category \u8FD4\u56DE int auto_increment\u7684\u4E0B\u4E00\u4E2A\u81EA\u589E\u503C
     */
    public static int getAutoIncremet(Class c) {
        String tableName = c.getSimpleName().toLowerCase();// person \u8868\u7684\u540D\u5B57
        if (!isTableExist(tableName)) {
            System.err.println("\u8868'" + tableName + "'\u4E0D\u5B58\u5728");
            return -1;
        }
        int count = -1;
        try {
            ResultSet rs = getConnection().prepareStatement(
                    "SELECT auto_increment FROM information_schema.`TABLES` WHERE TABLE_SCHEMA='"
                            + getDatabaseName(c) + "' AND TABLE_NAME='"
                            + tableName + "'").executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
        } catch (Exception e) {
            logger.info("DButils.getAutoIncremet() getAutoIncremet error");
            e.printStackTrace();
        }
        return count;
    }

    /**
     * @param c          Person.class
     * @param columnName \u8981\u67E5\u627E\u7684\u67D0\u4E00\u5217\u7684\u5217\u540D
     * @return List<String> \u8FD4\u56DE\u67D0\u4E00\u5217\u7684\u6240\u6709\u503C
     * @category \u67E5\u627E\u67D0\u4E00\u5217\u7684\u6240\u6709\u503C
     */
    public static List<String> getColumnData(Class c, String columnName) {
        if (!getColumns(c).contains(columnName)) {
            System.err.println("\u5217\u540D'" + columnName + "'\u4E0D\u5B58\u5728");
            return null;
        }
        List<String> list = new ArrayList<String>();
        String tableName = c.getSimpleName().toLowerCase();// person \u8868\u7684\u540D\u5B57
        if (!isTableExist(tableName)) {
            System.err.println("talbe is not exist");
            return list;
        }
        String sql = "select " + columnName + " from " + tableName;
        try {
            ResultSet rs = getConnection().createStatement().executeQuery(sql);
            while (rs.next()) {
                list.add(rs.getString(1));
            }
            rs.close();
        } catch (Exception e) {
            logger.info("DButils.getColumnData() getColumnData error");
            exceptionPrint(e);
        }
        return list;
    }

    /**
     * @param c          Person.class \u4E14Person \u6240\u6709\u7684\u5C5E\u6027\u5FC5\u987B\u5168\u4E3A\u4E3Apublic\u7C7B\u578B\u6216\u8005\u5168\u90E8\u4E3Aprivate
     * @param columnName \u8868\u4E2D\u7684\u67D0\u5B57\u6BB5
     * @param value      columnName\u5BF9\u5E94\u7684\u503C
     * @return List
     * @category \u6839\u636E\u6761\u4EF6\u67E5\u8BE2 \u8FD4\u56DEwhere columnName=value
     */
    public static List getEntitys(Class c, String columnName, Object value) {
        if (!getColumns(c).contains(columnName)) {
            System.err.println("\u5217\u540D'" + columnName + "'\u4E0D\u5B58\u5728");
            logger.info("DButils.getEntitys() \u5217\u540D\u4E0D\u5B58\u5728 \u63A8\u51FA");
            return null;
        }
        List list = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String tableName = c.getSimpleName().toLowerCase();// person \u8868\u7684\u540D\u5B57
        Field[] fields = c.getFields();// \u83B7\u53D6\u6240\u6709\u7684public\u5C5E\u6027
        Method[] methods = null;
        if (fields.length == 0) {
            fields = c.getDeclaredFields();// \u83B7\u53D6\u6240\u6709\u7684private\u5C5E\u6027
            methods = c.getDeclaredMethods();// \u83B7\u53D6get set \u65B9\u6CD5
        }
        StringBuilder sql = new StringBuilder("select * from " + tableName
                + " where " + columnName + "=?");
        try {
            if (!isTableExist(tableName)) {
                System.err.println("\u8868\u4E0D\u5B58\u5728");
                return list;
            }
            ps = (PreparedStatement) getConnection().prepareStatement(
                    sql.toString());
            ps.setObject(1, value);
            rs = ps.executeQuery();
            System.out.println("%n" + ps.toString().split(":")[1]);
            Object obj = null;
            while (rs.next()) {
                obj = c.newInstance();

                if (fields.length > 0) {// \u5982\u679C\u7C7B \u7684\u5C5E\u6027\u4E3Apublic
                    for (int k = 0; k < fields.length; k++) {
                        fields[k].setAccessible(true);
                        fields[k].set(obj, rs.getObject(k + 1));
                    }
                } else {// \u5982\u679C\u7C7B \u7684\u5C5E\u6027\u4E3Aprivate
                    for (int k = 0; k < methods.length / 2; k++) {
                        methods[k * 2].invoke(obj, rs.getObject(k + 1));
                    }
                }
                list.add(obj);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            logger.info("DButils.getEntitys() getEntitys error");
            e.printStackTrace();
        } finally {
            rs = null;
            ps = null;
        }
        return list;
    }

    /**
     * @param c Person.class
     * @return List\u6240\u6709\u7684\u6570\u636E
     * @see \u83B7\u53D6\u6240\u6709\u7684\u6570\u636E
     */
    public static List getAllEntitys(Class c) {
        String tableName = c.getSimpleName().toLowerCase();// person \u8868\u7684\u540D\u5B57
        if (!isTableExist(tableName)) {
            System.err.println("\u8868'" + tableName + "'\u4E0D\u5B58\u5728");
            logger.info("DButils.getAllEntitys()" + tableName + " is not isExist! \u9000\u51FA");

            return null;
        }
        List list = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Field[] fields = c.getFields();// \u83B7\u53D6\u6240\u6709\u7684public\u5C5E\u6027
        if (fields.length == 0) {
            // fields=c.getDeclaredFields();//\u83B7\u53D6\u6240\u6709\u7684private\u5C5E\u6027
            fields = c.getDeclaredFields();// \u83B7\u53D6get set \u65B9\u6CD5
        }
        StringBuilder sql = new StringBuilder("select * from " + tableName);
        try {
            if (!isTableExist(tableName)) {
                System.err.println("\u8868\u4E0D\u5B58\u5728");
                return list;
            }
            ps = (PreparedStatement) getConnection().prepareStatement(
                    sql.toString());

            rs = ps.executeQuery();
            System.out.println("%n" + ps.toString().split(":")[1]);
            Object obj = null;
            while (rs.next()) {
                obj = c.newInstance();
                for (int k = 0; k < fields.length; k++) {
                    try {
                        fields[k].setAccessible(true);
                        fields[k].set(obj, rs.getObject(k + 1));
                    } catch (Exception e) {
                        logger.error("getAllEntitys", e);
                    }
                }
                list.add(obj);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            logger.info("DButils.getAllEntitys() getAllEntitys error");
        } finally {
            rs = null;
            ps = null;
        }
        return list;

    }


    public static void main(String[] args) throws Exception {
        //mysql.url=jdbc:mysql://192.168.42.7:3306/mysql?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT&useSSL=false
        //使用的是系统库mysql中的系统表user
        Connection conn = DButils.getConnection();
        logger.info(conn.toString());
        User info = new User();
        List<User> list = DButils.getAllEntitys(User.class);
        for (int i = 0; i < list.size(); i++) {
            logger.info(JSONObject.fromObject(list.get(i)).toString());
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param c
     * @param primaryKey
     * @param keyName
     */
    public static void deleteByPrimaryKey_Sqlite(Class c, Object primaryKey, Object keyName) {
        String tableName = c.getSimpleName().toLowerCase();// person
        StringBuilder sql = new StringBuilder("delete from ").append(tableName).append(" where ");
        if (primaryKey != null && keyName != null && !"".equals(primaryKey) && !"".equals(keyName)) {
            sql.append(keyName).append("='").append(primaryKey).append("'");
            logger.info(sql.toString());
            PreparedStatement ps = null;
            try {
                ps = getConnection().prepareStatement(sql.toString());
                ps.executeUpdate();
                getConnection().commit();
                logger.error("delete " + tableName + " success");
                ps.close();
                ps = null;
            } catch (Exception e1) {
                logger.error("delete " + tableName + " fail", e1);
            }
        } else {
            logger.info("delte " + tableName + "fail" + "parameter is not right!");
        }

    }

    /**
     * @param c          Class.class
     * @param primaryKey 主键值
     * @param keyName    主键对应数据库名称
     * @param obj        要更新的javabean对象
     */
    public static void updateEntityByPrimaryKey_Sqlite(Class<?> c, Object primaryKey, Object keyName, Object obj) {
        if (obj == null || c.getSimpleName().equals(obj.getClass().getName()))
            return;
        Field[] fields = obj.getClass().getDeclaredFields();
        int fieldSize = fields.length;
        String tableName = c.getSimpleName().toLowerCase();// person
        StringBuilder sql = new StringBuilder("update  ").append(tableName).append(" set ");
        for (int j = 0; j < fieldSize; j++) {
            fields[j].setAccessible(true);
            Object filedValue = BeanUtil.getFieldValueByName(fields[j].getName(), obj);
            if (filedValue != null && !"".equals(filedValue)) {
                sql.append(fields[j].getName()).append("='").append(filedValue).append("'").append(",");
            }
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" where ").append(keyName).append("='").append(primaryKey).append("'");
        logger.info(sql.toString());
        PreparedStatement ps = null;
        try {
            ps = getConnection().prepareStatement(sql.toString());
            ps.executeUpdate();
            getConnection().commit();
            logger.error("update " + tableName + " success");
            ps.close();
            ps = null;
        } catch (Exception e1) {
            logger.error("update " + tableName + " fail", e1);
        }
    }


    /**
     * sqlite 使用javabean对象插入数据
     *
     * @param c
     * @param obj
     */
    public static void insertEntity_Sqlite(Class c, Object obj) throws Exception {
        if (obj == null || c.getSimpleName().equals(obj.getClass().getName()))
            return;
        Field[] fields = obj.getClass().getDeclaredFields();
        int fieldSize = fields.length;
        String tableName = c.getSimpleName().toLowerCase();// person
        StringBuilder sql = new StringBuilder("insert into ").append(tableName).append(" ");
        StringBuilder filedsBuf = new StringBuilder("(");
        StringBuilder vlauesBuf = new StringBuilder("(");
        for (int j = 0; j < fieldSize; j++) {
            fields[j].setAccessible(true);
            Object filedValue = BeanUtil.getFieldValueByName(fields[j].getName(), obj);
            if (filedValue != null && !"".equals(filedValue)) {
                filedsBuf.append(fields[j].getName()).append(",");
                vlauesBuf.append("'").append(filedValue).append("'").append(",");
            }
        }
        filedsBuf.deleteCharAt(filedsBuf.length() - 1);
        vlauesBuf.deleteCharAt(vlauesBuf.length() - 1);
        filedsBuf.append(")");
        vlauesBuf.append(")");

        sql.append(filedsBuf).append(" values ").append(vlauesBuf);
        logger.info("DButils.insertEntity() insertEntity sql:" + sql);
        PreparedStatement ps = null;
        try {
            ps = getConnection().prepareStatement(sql.toString());
            ps.executeUpdate();
            getConnection().commit();
            ps.close();
            ps = null;
        } catch (Exception e1) {
            logger.error("insert to " + tableName + "fail", e1);
        }
    }

    /**
     * select * from users order by id limit 10 offset 0;//，
     *
     * @param orderby 排序条件
     * @param c       javabean对象
     * @param params  查询条件 javabean 精确
     * @return
     * @desc limit表明查询多少条结果
     * @desc offset代表从第几条记录“之后“开始查询
     */
    public static List getAllEntitys_Sqlite(Class<?> c, Object params, String orderby, int limit, int offset) {
        String tableName = c.getSimpleName().toLowerCase();// person \u8868\u7684\u540D\u5B57
        List list = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Field[] fields = c.getFields();// \u83B7\u53D6\u6240\u6709\u7684public\u5C5E\u6027
        int fieldSize = fields.length;
        if (fields.length == 0) {
            fields = c.getDeclaredFields();// \u83B7\u53D6get set \u65B9\u6CD5
            fieldSize = fields.length;
        }
        StringBuilder sql = new StringBuilder("select * from ").append(tableName).append(" where 1=1 ");
        if (params != null && !"".equals(params)) {
            for (int j = 0; j < fieldSize; j++) {
                fields[j].setAccessible(true);
                Object filedValue = BeanUtil.getFieldValueByName(fields[j].getName(), params);
                if (filedValue != null && !"".equals(filedValue)) {
                    sql.append(" and ").append(fields[j].getName()).append("='").append(filedValue).append("' ");
                }
            }
        }
        if (null != orderby && !"".equals(orderby)) {
            sql.append(" order by ").append(orderby).append(" ");
        }
        if (limit != -1 && offset != -1) {
            sql.append(" limit ").append(limit).append(" offset ").append(offset).append(" ");
        }
        logger.info("DButils.getAllEntitys_SqliteLimit() " + sql);
        try {
            ps = (PreparedStatement) getConnection().prepareStatement(sql.toString());
            rs = ps.executeQuery();
            Object obj = null;
            while (rs.next()) {
                obj = c.newInstance();
                for (int k = 0; k < fields.length; k++) {
                    fields[k].setAccessible(true);
                    fields[k].set(obj, rs.getObject(k + 1));
                }
                list.add(obj);
            }

        } catch (Exception e) {
            logger.error("DButils.getAllEntitys() getAllEntitys error", e);
        } finally {
            try {
                rs.close();
                ps.close();
            } catch (SQLException e) {
                logger.info("close ResultSet PreparedStatement error:", e);
            }
            rs = null;
            ps = null;
        }
        return list;
    }

    /**
     * select * from users order by id limit 10 offset 0;//，
     *
     * @param orderby 排序条件
     * @param c       javabean对象
     * @return
     */
    public static List getAllEntitys_Sqlite(Class<?> c, String orderby) {
        return getAllEntitys_Sqlite(c, null, orderby, -1, -1);
    }

    /**
     * select * from users order by id limit 10 offset 0;//，
     *
     * @param c javabean对象
     * @return
     * @desc limit表明查询多少条结果
     * @desc offset代表从第几条记录“之后“开始查询
     */
    public static List getAllEntitys_Sqlite(Class<?> c, int limit, int offset) {
        return getAllEntitys_Sqlite(c, null, null, limit, offset);
    }

    /**
     * 根据javabean对象获取数据
     *
     * @param c
     * @return
     */
    public static List getAllEntitys_Sqlite(Class<?> c) {
        return getAllEntitys_Sqlite(c, -1, -1);
    }

    /**
     * @param c      JavaBean.class
     * @param params javabean 对象--查询条件
     * @return
     */
    public static List getAllEntitys_Sqlite(Class<?> c, Object params) {
        return getAllEntitys_Sqlite(c, params, null, -1, -1);
    }

    /**
     * @param c         Person.class
     * @param columns   \u8981\u6392\u5E8F\u7684columns \u5982 Id\uFF0Cname \u6216\u5355\u72EC\u4E00\u4E2Aname
     * @param ascOrDesc asc\u6216desc
     * @return List\u6240\u6709\u7684\u6570\u636E
     * @see \u83B7\u53D6\u6240\u6709\u7684\u6570\u636E
     */
    public static List getAllEntitysOrderByColumns(Class c, String[] columns,
                                                   String ascOrDesc) {
        String tableName = c.getSimpleName().toLowerCase();// person \u8868\u7684\u540D\u5B57
        if (!isTableExist(tableName)) {
            System.err.println("\u8868'" + tableName + "'\u4E0D\u5B58\u5728");
            logger.info("DButils.getAllEntitysOrderByColumns()" + tableName
                    + " is not isExist! \u9000\u51FA");

            return null;
        }
        List<String> columnList = getColumns(c);
        for (int i = 0; i < columns.length; i++) {
            if (!columnList.contains(columns[i])) {
                System.err.println("\u5217\u540D'" + columns[i] + "'\u4E0D\u5B58\u5728");
                logger.info("DButils.getAllEntitysOrderByColumns()" +
                        columns[i] + " is not isExist! \u9000\u51FA");
                return null;
            }
        }
        if (!ascOrDesc.equalsIgnoreCase("asc")
                && ascOrDesc.equalsIgnoreCase("desc")) {
            logger.info("DButils.getAllEntitysOrderByColumns() \u6392\u5E8F\u5173\u952E\u5B57\u9519\u8BEF");
            return null;
        }
        List list = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Field[] fields = c.getFields();// \u83B7\u53D6\u6240\u6709\u7684public\u5C5E\u6027
        if (fields.length == 0) {
            // fields=c.getDeclaredFields();//\u83B7\u53D6\u6240\u6709\u7684private\u5C5E\u6027
            fields = c.getDeclaredFields();// \u83B7\u53D6get set \u65B9\u6CD5
        }
        StringBuilder sql = new StringBuilder("select * from " + tableName
                + " order by ");
        for (int i = 0; i < columns.length; i++) {
            if (i < columns.length - 1) {
                sql.append("" + columns[i] + ",");
            } else {
                sql.append("" + columns[i] + " " + ascOrDesc);
            }
        }
        logger.info("DButils.getAllEntitysOrderByColumn() sql:"
                + sql.toString());
        try {
            if (!isTableExist(tableName)) {
                System.err.println("\u8868\u4E0D\u5B58\u5728");
                return list;
            }
            ps = (PreparedStatement) getConnection().prepareStatement(
                    sql.toString());

            rs = ps.executeQuery();
            System.out.println("%n" + ps.toString().split(":")[1]);
            Object obj = null;
            while (rs.next()) {
                obj = c.newInstance();
                for (int k = 0; k < fields.length; k++) {
                    fields[k].setAccessible(true);
                    fields[k].set(obj, rs.getObject(k + 1));
                }
                list.add(obj);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            logger.info("DButils.getAllEntitysOrderByColumn() getAllEntitysOrderByColumn error");
        } finally {
            rs = null;
            ps = null;
        }
        return list;

    }

    /**
     * @param c          Person.class
     * @param fromNumber \u4ECE\u6570\u636E\u5E93\u7684\u7B2C\u51E0\u6761\u5F00\u59CB\uFF080\uFF0C1\uFF0C2\uFF0C3\uFF09
     * @param number     \u4ECEfromNumber\u5F00\u59CB\u83B7\u53D6\u591A\u5C11\u884C
     * @return List
     * @see \u83B7\u53D6\u6570\u636E\u4E2D\u7684\u67D0\u51E0\u6761\u8BB0\u5F55
     */
    public static List getEntitysLimit(Class c, int fromNumber, int number) {
        String tableName = c.getSimpleName().toLowerCase();// person \u8868\u7684\u540D\u5B57
        if (!isTableExist(tableName)) {
            System.err.println("\u8868'" + tableName + "'\u4E0D\u5B58\u5728");
            return null;
        }
        List list = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Field[] fields = c.getFields();// \u83B7\u53D6\u6240\u6709\u7684public\u5C5E\u6027
        if (fields.length == 0) {
            // fields=c.getDeclaredFields();//\u83B7\u53D6\u6240\u6709\u7684private\u5C5E\u6027
            fields = c.getDeclaredFields();// \u83B7\u53D6get set \u65B9\u6CD5
        }
        StringBuilder sql = new StringBuilder("select * from " + tableName)
                .append(" limit ?,?");
        try {
            if (!isTableExist(tableName)) {
                System.err.println("\u8868\u4E0D\u5B58\u5728");
                return list;
            }
            ps = (PreparedStatement) getConnection().prepareStatement(
                    sql.toString());
            ps.setInt(1, fromNumber);
            ps.setInt(2, number);
            rs = ps.executeQuery();
            System.out.println("%n" + ps.toString().split(":")[1]);
            logger.info("DButils.getEntitysLimit()" +
                    ps.toString().split(":")[1]);

            Object obj = null;
            while (rs.next()) {
                obj = c.newInstance();
                for (int k = 0; k < fields.length; k++) {
                    fields[k].setAccessible(true);
                    fields[k].set(obj, rs.getObject(k + 1));
                }
                list.add(obj);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            logger.info("DButils.getEntitysLimit() getEntitysLimit error");
            exceptionPrint(e);
        } finally {
            rs = null;
            ps = null;
        }
        return list;

    }

    /**
     * @param c Person.class
     * @return list
     * @category \u8FD4\u56DE\u8868\u4E2D\u6240\u6709\u7684\u4E3B\u952E
     */
    public static List<String> getPrimaryKeys(Class c) {
        String tableName = c.getSimpleName().toLowerCase();// person \u8868\u7684\u540D\u5B57
        ResultSet rs = null;
        List<String> list = new ArrayList<String>();
        try {
            rs = getConnection().getMetaData().getPrimaryKeys(null, null,
                    tableName);
            while (rs.next()) {
                list.add(rs.getString(4));
            }
            rs.close();
        } catch (Exception e) {
            logger.info("DButils.getPrimaryKeys() getPrimaryKeys error");
            exceptionPrint(e);
        }
        return list;
    }

    /**
     * @param c          Person.class
     * @param primaryKey \u6309\u8868\u4E2D\u4E3B\u952E\u5220\u9664 \u5982\u679C\u4E3B\u952E\u4E3Aid\u3002name
     *                   \u5219deleteByPrimaryKey(Person.class,1,"ctl");
     * @category \u6839\u636E\u4E3B\u952E\u5220\u9664\u6570\u636E
     */
    public static void deleteByPrimaryKey(Class c, Object... primaryKey) {
        String tableName = c.getSimpleName().toLowerCase();// person \u8868\u7684\u540D\u5B57
        List<String> primaryKeysList = getPrimaryKeys(c);
        StringBuilder sb = new StringBuilder("delete from " + tableName + " where ");
        for (int i = 0; i < primaryKeysList.size(); i++) {
            sb.append(primaryKeysList.get(i) + "=? and ");
        }
        sb.delete(sb.length() - 4, sb.length());
        PreparedStatement ps = null;
        try {
            ps = getConnection().prepareStatement(sb.toString());
            for (int i = 0; i < primaryKeysList.size(); i++) {
                ps.setObject(i + 1, primaryKey[i]);
            }
            ps.executeUpdate();
            System.out.println(ps.toString().split(":")[1].trim());
            ps.close();
        } catch (Exception e) {
            logger.info("DButils.deleteByPrimaryKey() deleteByPrimaryKey error");
            ps = null;
            exceptionPrint(e);
        }

    }

    /**
     * @param c           Person.class
     * @param columnName  \u5217\u540D
     * @param columnValue \u5217\u540D\u5BF9\u5E94\u7684\u503C
     * @see \u5220\u9664\u5217\u540D\u4E3AcolumnName\u5217\u503C\u4E3AcolumnValue\u7684\u6570\u636E
     */
    public static void deleteByColumn(Class c, String columnName,
                                      Object columnValue) {
        if (!getColumns(c).contains(columnName)) {
            System.err.println("\u5217\u540D'" + columnName + "'" + "\u4E0D\u5B58\u5728");
            return;
        }
        String tableName = c.getSimpleName().toLowerCase();// person \u8868\u7684\u540D\u5B57
        StringBuilder sql = new StringBuilder("delete from ").append(tableName)
                .append(" where ").append(columnName + "=?");
        try {
            PreparedStatement ps = getConnection().prepareStatement(
                    sql.toString());
            ps.setObject(1, columnValue);
            // ps.setObject(2,columnValue );
            ps.executeUpdate();
            System.out.println(ps.toString().split(":")[1].trim());
            ps.close();
        } catch (Exception e) {
            logger.info("DButils.deleteByColumn() deleteByColumn error");
            System.err.println("delete error");
            e.printStackTrace();
        }
    }

    /**
     * @param c   Person.class
     * @param obj \u5B9E\u4F53\u5BF9\u8C61
     * @return \u4ECE\u6570\u636E\u5E93\u4E2D\u83B7\u53D6\u4E3B\u952E \u7136\u540E\u4E0E\u5B9E\u4F53\u7C7B\u76F8\u5339\u914D\uFF0C\u8FD4\u56DE\u5BF9\u8C61\u4E2D\u7684\u4E3B\u952E\u540D\u548C\u503C
     * @category \u4ECE\u5B9E\u4F53\u7C7B\u5BF9\u8C61\u4E2D\u83B7\u53D6\u4E3B\u952E\u7684\u5217\u540D\u548Cvalue \u5229\u7528\u7684\u662Ffiled\u83B7\u53D6
     */
    public static List<KeyValue> getEntityPrimaryKeyValueField(Class c,
                                                               Object obj) {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        List<String> primaryKeys = getPrimaryKeys(c);
        Field[] fields = c.getFields();// \u83B7\u53D6\u6240\u6709\u7684\u5C5E\u6027
        for (int i = 0; i < fields.length; i++) {// \u6240\u6709\u5C5E\u6027\u4E3Apublic
            try {
                for (int j = 0; j < primaryKeys.size(); j++) {
                    if (primaryKeys.get(j)
                            .equalsIgnoreCase(fields[i].getName())) {
                        KeyValue kv = new KeyValue();
                        kv.setKey(fields[i].getName());
                        kv.setValue(fields[i].get(obj));
                        keyValues.add(kv);
                        // System.out.println(fields[i].getName() + ":"
                        // + fields[i].get(obj));
                    }
                }
            } catch (Exception e) {
                logger.info("DButils.getEntityPrimaryKeyValueField() getEntityPrimaryKeyValueField error");
                exceptionPrint(e);
            }
        }
        if (fields.length == 0) {// \u5F53\u7C7B\u7684\u5C5E\u6027\u90FD\u662Fprivate\u65F6
            // fields=c.getDeclaredFields();//\u83B7\u53D6\u6240\u6709\u7684private\u5C5E\u6027
            fields = c.getDeclaredFields();// //\u83B7\u5F97\u5BF9\u8C61\u6240\u6709\u5C5E\u6027
            for (int i = 0; i < fields.length; i++) {
                try {
                    fields[i].setAccessible(true);// \u4FEE\u6539\u8BBF\u95EE\u6743\u9650 \u53EF\u4EE5\u8BBF\u95EEprivate
                    for (int j = 0; j < primaryKeys.size(); j++) {
                        if (primaryKeys.get(j).equalsIgnoreCase(
                                fields[i].getName())) {
                            KeyValue kv = new KeyValue();
                            kv.setKey(fields[i].getName());
                            kv.setValue(fields[i].get(obj));// \u8BFB\u53D6\u5C5E\u6027\u503C
                            keyValues.add(kv);
                            // System.out.println(fields[i].getName() + ":"
                            // + fields[i].get(obj));
                        }
                    }
                } catch (Exception e) {
                    logger.info("DButils.getEntityPrimaryKeyValueField() getEntityPrimaryKeyValueField error");
                    exceptionPrint(e);
                }
            }
        }
        return keyValues;
    }

    /**
     * @param c   Person.class
     * @param obj \u5B9E\u4F53\u5BF9\u8C61
     * @return \u4ECE\u6570\u636E\u5E93\u4E2D\u83B7\u53D6\u4E3B\u952E \u7136\u540E\u4E0E\u5B9E\u4F53\u7C7B\u76F8\u5339\u914D\uFF0C\u8FD4\u56DE\u5BF9\u8C61\u4E2D\u7684\u4E3B\u952E\u540D\u548C\u503C
     * @category \u4ECE\u5B9E\u4F53\u7C7B\u5BF9\u8C61\u4E2D\u83B7\u53D6\u4E3B\u952E\u7684\u5217\u540D\u548Cvalue \u5229\u7528\u7684\u662FMethod get\u65B9\u6CD5\u83B7\u53D6
     */
    public static List<KeyValue> getEntityPrimaryKeyValueMethod(Class c,
                                                                Object obj) {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        List<String> primaryKeys = getPrimaryKeys(c);
        Field[] fields = c.getFields();// \u83B7\u53D6\u6240\u6709\u7684\u5C5E\u6027
        for (int i = 0; i < fields.length; i++) {// \u6240\u6709\u5C5E\u6027\u4E3Apublic
            try {
                for (int j = 0; j < primaryKeys.size(); j++) {
                    if (primaryKeys.get(j)
                            .equalsIgnoreCase(fields[i].getName())) {
                        KeyValue kv = new KeyValue();
                        kv.setKey(fields[i].getName());
                        kv.setValue(fields[i].get(obj));
                        keyValues.add(kv);
                        System.out.println(fields[i].getName() + ":"
                                + fields[i].get(obj));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (fields.length == 0) {// \u5F53\u7C7B\u7684\u5C5E\u6027\u90FD\u662Fprivate\u65F6
            // fields=c.getDeclaredFields();//\u83B7\u53D6\u6240\u6709\u7684private\u5C5E\u6027
            fields = c.getDeclaredFields();// //\u83B7\u5F97\u5BF9\u8C61\u6240\u6709\u5C5E\u6027
            for (int i = 0; i < fields.length; i++) {
                try {
                    fields[i].setAccessible(true);// \u4FEE\u6539\u8BBF\u95EE\u6743\u9650 \u53EF\u4EE5\u8BBF\u95EEprivate
                    for (int j = 0; j < primaryKeys.size(); j++) {
                        if (primaryKeys.get(j).equalsIgnoreCase(
                                fields[i].getName())) {
                            KeyValue kv = new KeyValue();
                            kv.setKey(fields[i].getName());
                            kv.setValue(fields[i].get(obj));// \u8BFB\u53D6\u5C5E\u6027\u503C
                            keyValues.add(kv);
                            System.out.println(fields[i].getName() + ":"
                                    + fields[i].get(obj));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return keyValues;
    }

    /**
     * @param c   Person.class
     * @param obj \u5B9E\u4F53\u5BF9\u8C61
     * @return \u4ECE\u6570\u636E\u5E93\u4E2D\u83B7\u53D6\u4E3B\u952E \u7136\u540E\u4E0E\u5B9E\u4F53\u7C7B\u76F8\u5339\u914D\uFF0C\u8FD4\u56DE\u5BF9\u8C61\u4E2D\u7684\u4E3B\u952E\u503C
     * @category \u4ECE\u5B9E\u4F53\u7C7B\u5BF9\u8C61\u4E2D\u6309\u987A\u5E8F\u83B7\u53D6\u6240\u6709\u4E3B\u952E\u7684value
     */
    public static List<Object> getEntityPKValues(Class c, Object obj) {
        List<Object> keyValues = new ArrayList<Object>();
        List<String> primaryKeys = getPrimaryKeys(c);
        Field[] fields = c.getFields();// \u83B7\u53D6\u6240\u6709\u7684\u5C5E\u6027
        for (int i = 0; i < fields.length; i++) {// \u6240\u6709\u5C5E\u6027\u4E3Apublic
            try {
                for (int j = 0; j < primaryKeys.size(); j++) {
                    if (primaryKeys.get(j)
                            .equalsIgnoreCase(fields[i].getName())) {
                        keyValues.add(fields[i].get(obj));
                        System.out.println(fields[i].getName() + ":"
                                + fields[i].get(obj));
                    }
                }
            } catch (Exception e) {
                logger.info("DButils.getEntityPKValues() getEntityPKValues error");
                exceptionPrint(e);
            }
        }
        if (fields.length == 0) {// \u5F53\u7C7B\u7684\u5C5E\u6027\u90FD\u662Fprivate\u65F6
            // fields=c.getDeclaredFields();//\u83B7\u53D6\u6240\u6709\u7684private\u5C5E\u6027
            fields = c.getDeclaredFields();// //\u83B7\u5F97\u5BF9\u8C61\u6240\u6709\u5C5E\u6027
            for (int i = 0; i < fields.length; i++) {
                try {
                    fields[i].setAccessible(true);// \u4FEE\u6539\u8BBF\u95EE\u6743\u9650 \u53EF\u4EE5\u8BBF\u95EEprivate
                    for (int j = 0; j < primaryKeys.size(); j++) {
                        if (primaryKeys.get(j).equalsIgnoreCase(
                                fields[i].getName())) {
                            keyValues.add(fields[i].get(obj));
                            System.out.println(fields[i].getName() + ":"
                                    + fields[i].get(obj));
                        }
                    }
                } catch (Exception e) {
                    logger.info("DButils.getEntityPKValues() getEntityPKValues error");
                    exceptionPrint(e);
                }
            }
        }
        return keyValues;
    }

    /**
     * @param c   Person.class
     * @param obj person
     * @category \u5C06\u5B9E\u4F53\u7C7B\u5BF9\u8C61\u8DDF\u65B0\u5230\u6570\u636E\u5E93\u4E2D\uFF0C\u5982\u679C\u5BF9\u8C61\u4E2D\u7684\u5C5E\u6027\u4E0E\u6570\u636E\u4E2D\u4E0D\u4E00\u81F4\u5219\u66F4\u65B0\uFF0C\u5BF9\u8C61\u67D0\u5C5E\u6027\u4E3A\u7A7A\u5219\u4E0D\u66F4\u6539\u8BE5\u5C5E\u6027
     * @see \u5982\u679C\u6709\u4E3B\u952E\u5219\u6267\u884C\u66F4\u884C\uFF0C\u6CA1\u6709\u4E3B\u952E\u5219\u6267\u884C\u63D2\u5165\u64CD\u4F5C
     */
    public static void updateEntity(Class c, Object obj) {
        String tableName = c.getSimpleName().toLowerCase();// person \u8868\u7684\u540D\u5B57
        List<String> primaryKeys = getPrimaryKeys(c);
        List<KeyValue> keyValues = getEntityPrimaryKeyValueField(c, obj);
        List<String> columns = getColumns(c);
        List<Object> values = getEntityPKValues(c, obj);
        Object tableDate = getEntity(c, values.toArray(new Object[]{}));
        // System.out.println(o);
        Field[] fields = c.getFields();// \u83B7\u53D6\u6240\u6709\u7684\u5C5E\u6027
        System.out.println("\u6570\u636E\u5E93\u6BD4\u5BF9\u524D:" + obj);
        System.out.println("\u6570\u636E\u5E93\u4E2D\u6570\u636E:" + tableDate);
        for (int i = 0; i < fields.length; i++) {// \u6240\u6709\u5C5E\u6027\u4E3Apublic
            try {
                for (int j = 0; j < columns.size(); j++) {
                    if (columns.get(j).equalsIgnoreCase(fields[i].getName())) {
                        System.out.println(fields[i].getName() + ":" + fields[i].get(obj));
                        if (fields[i].get(obj) == null) {
                            fields[i].set(obj, fields[i].get(tableDate));
                        } else if (!fields[i].get(obj).equals(
                                fields[i].get(tableDate))) {
                            continue;
                        }
                    }
                }
            } catch (Exception e) {
                logger.info("DButils.updateEntity() updateEntity error");
                exceptionPrint(e);
            }
        }

        if (fields.length == 0) {// \u5F53\u7C7B\u7684\u5C5E\u6027\u90FD\u662Fprivate\u65F6
            // fields=c.getDeclaredFields();//\u83B7\u53D6\u6240\u6709\u7684private\u5C5E\u6027
            fields = c.getDeclaredFields();// //\u83B7\u5F97\u5BF9\u8C61\u6240\u6709\u5C5E\u6027
            for (int i = 0; i < fields.length; i++) {
                try {
                    fields[i].setAccessible(true);// \u4FEE\u6539\u8BBF\u95EE\u6743\u9650 \u53EF\u4EE5\u8BBF\u95EEprivate
                    for (int j = 0; j < columns.size(); j++) {
                        if (columns.get(j)
                                .equalsIgnoreCase(fields[i].getName())) {
                            System.out.println(fields[i].getName() + ":"
                                    + fields[i].get(obj));
                            if (fields[i].get(obj) == null) {
                                fields[i].set(obj, fields[i].get(tableDate));
                            } else if (!fields[i].get(obj).equals(
                                    fields[i].get(tableDate))) {
                                continue;
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.info("DButils.updateEntity() updateEntity error");
                }
            }
        }
        System.out.println("\u6570\u636E\u5E93\u6BD4\u5BF9\u540E:" + obj);
        insertEntity(c, obj);
    }

    static class InitJFrame {
        private InitJFrame(String databasetype) {
            this(null, databasetype);
        }

        private InitJFrame(final String jfTitle, String databasetype) {
            initJFrame_mysql(jfTitle, databasetype);
            initJFrame_oracle(jfTitle, databasetype);
        }

        private void initJFrame_oracle(final String jfTitle, String databasetype) {
            if (databasetype.equals("oracle")) {
                Thread t_oracle = new Thread() {
                    public void run() {
                        String title;
                        Font fontGlobal = new Font("\u6977\u4F53", 11, 10);
                        if (jfTitle != null && !"".equals(jfTitle)
                                && !"null".equals(jfTitle)) {
                            title = "oracle\u6570\u636E\u5E93\u521D\u59CB\u5316:" + jfTitle;
                        } else {
                            title = "oracle\u6570\u636E\u5E93\u521D\u59CB\u5316";
                        }
                        JFrame jf = new JFrame(title);

                        jf.setVisible(true);
                        jf.setBounds(600, 260, 266, 166);
                        // jf.setBounds(600, 260, 266, 366);
                        jf.setVisible(true);
                        //	jf.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        jf.setResizable(false);

                        JPanel jpanel = new JPanel();
                        jpanel.setBounds(0, 0, 300, 600);
                        jf.add(jpanel);
                        jpanel.setBackground(new Color(199, 237, 204));
                        jpanel.setLayout(null);

                        JLabel jls2 = new JLabel("\u4E3B\u673A\u540D/ip:");
                        jls2.setBounds(0, 5, 50, 45);
                        jpanel.add(jls2);
                        jls2.setFont(fontGlobal);

                        final JTextField jtfip = new JTextField(18);
                        jtfip.setBounds(50, 17, 100, 20);
                        jpanel.add(jtfip);
                        jtfip.setText("127.0.0.1");

                        JLabel jls3 = new JLabel("\u7AEF\u53E3:");
                        jls3.setBounds(155, 5, 50, 45);
                        jpanel.add(jls3);
                        jls3.setFont(fontGlobal);

                        final JTextField jtfport = new JTextField(15);
                        jtfport.setBounds(180, 17, 45, 20);
                        jpanel.add(jtfport);
                        jtfport.setText("1521");

                        JLabel jls4 = new JLabel("\u7528\u6237\u540D:");
                        jls4.setBounds(16, 35, 50, 45);
                        jpanel.add(jls4);
                        jls4.setFont(fontGlobal);

                        final JTextField jtfip1 = new JTextField(18);
                        jtfip1.setBounds(50, 46, 58, 20);
                        jpanel.add(jtfip1);
                        jtfip1.setText(username);

                        JLabel jls5 = new JLabel("\u5BC6\u7801:");
                        jls5.setBounds(125, 35, 50, 45);
                        jpanel.add(jls5);
                        jls5.setFont(fontGlobal);

                        final JPasswordField jtfport1 = new JPasswordField(15);
                        jtfport1.setBounds(150, 46, 75, 20);
                        jpanel.add(jtfport1);
                        jtfport1.setText(password);

                        final JLabel jls6 = new JLabel("SID:");
                        jls6.setBounds(26, 65, 50, 45);
                        jpanel.add(jls6);
                        jls6.setFont(fontGlobal);

                        final JTextField jtfip11 = new JTextField(18);
                        jtfip11.setBounds(50, 77, 58, 20);
                        jpanel.add(jtfip11);
                        jtfip11.setText("fastatm");

//						JLabel jls51 = new JLabel("\u7F16\u7801:");
//						jls51.setBounds(123, 65, 50, 45);
//						jpanel.add(jls51);
//						jls51.setFont(fontGlobal);
//
//						final JTextField jtfport11 = new JTextField(15);
//						jtfport11.setBounds(150, 77, 75, 20);
//						jpanel.add(jtfport11);
//						jtfport11.setText("utf-8");

                        final JButton linkBtn = new JButton("\u521D\u59CB\u5316DButils\u5DE5\u5177\u7C7B");
                        linkBtn.setBounds(56, 111, 150, 20);
                        jpanel.add(linkBtn);
                        linkBtn.setMargin(new Insets(0, 0, 0, 0));// \u8FD9\u6837\u8BBE\u7F6Ebutton\u4E2D\u7684\u5B57\u4F53\u4E0Ebutton\u65E0\u4E0A\u4E0B\u8FB9\u8DDD
                        linkBtn.setFont(fontGlobal);
                        MouseAdapter linkServerListener = new MouseAdapter() {
                            public void mouseClicked(MouseEvent e) {
                                String ip = jtfip.getText().trim();
                                int port = Integer.parseInt(jtfport.getText()
                                        .trim());
                                username = jtfip1.getText().trim();
                                password = jtfport1.getText().trim();
                                String sid = jtfip11.getText().trim();
                                //String code = jtfport11.getText().trim();
                                driver = "oracle.jdbc.driver.OracleDriver";
                                url = "jdbc:oracle:thin:@" + ip + ":" + port + ":"
                                        + sid;
                                //javax.swing.JOptionPane.showMessageDialog(null,username+" "+password+" "+url);
//								RegisterUtil.putStringSystemRoot("oracle.driver",
//										driver);
//								RegisterUtil.putStringSystemRoot("oracle.url", url);
//								RegisterUtil.putStringSystemRoot("oracle.username",
//										username);
//								RegisterUtil.putStringSystemRoot("oracle.password",
//										password);
                                //System.out.println(RegisterUtil.getStringFromSystemRoot("oracle.driver"));
                                System.out.println(driver);
                                System.out.println(url);
                                System.out.println(username);
                                System.out.println(password);
                                logger.info(driver);
                                try {
                                    conn = DriverManager.getConnection(url,
                                            username, password);
                                    if (conn != null) {
                                        System.out.println(conn);
                                        conn.setAutoCommit(getAutoCommit());
                                        connHolder.set(conn);
                                        linkBtn.setText("\u521D\u59CB\u5316\u6210\u529F\uFF01\u8BF7\u5173\u95ED\u7A97\u4F53!");
                                        //RegisterUtil.putIntSystemRoot("isInit", 0);
                                    }
                                } catch (Exception e2) {
                                    logger.error(e2.getMessage());
                                } finally {
                                    if (conn != null) {
                                        try {
                                            conn.close();
                                        } catch (SQLException e1) {
                                            logger.error(e1.getMessage());
                                        }
                                    }
                                }
                            }
                        };
                        linkBtn.addMouseListener(linkServerListener);
                    }
                };
                t_oracle.start();
            }

        }

        private void initJFrame_mysql(final String jfTitle, String databasetype) {
            if (databasetype.equals("mysql")) {
                Thread t_mysql = new Thread() {
                    public void run() {
                        String title;
                        Font fontGlobal = new Font("\u6977\u4F53", 11, 10);
                        if (jfTitle != null && !"".equals(jfTitle)
                                && !"null".equals(jfTitle)) {
                            title = "mysql\u6570\u636E\u5E93\u521D\u59CB\u5316:" + jfTitle;
                        } else {
                            title = "mysql\u6570\u636E\u5E93\u521D\u59CB\u5316";
                        }
                        JFrame jf = new JFrame(title);

                        jf.setVisible(true);
                        jf.setBounds(600, 260, 266, 166);
                        // jf.setBounds(600, 260, 266, 366);
                        jf.setVisible(true);
                        jf.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                        jf.setResizable(false);

                        JPanel jpanel = new JPanel();
                        jpanel.setBounds(0, 0, 300, 600);
                        jf.add(jpanel);
                        jpanel.setBackground(new Color(199, 237, 204));
                        jpanel.setLayout(null);

                        JLabel jls2 = new JLabel("\u4E3B\u673A\u540D/ip:");
                        jls2.setBounds(0, 5, 50, 45);
                        jpanel.add(jls2);
                        jls2.setFont(fontGlobal);

                        final JTextField jtfip = new JTextField(18);
                        jtfip.setBounds(50, 17, 100, 20);
                        jpanel.add(jtfip);
                        jtfip.setText("127.0.0.1");

                        JLabel jls3 = new JLabel("\u7AEF\u53E3:");
                        jls3.setBounds(155, 5, 50, 45);
                        jpanel.add(jls3);
                        jls3.setFont(fontGlobal);

                        final JTextField jtfport = new JTextField(15);
                        jtfport.setBounds(180, 17, 45, 20);
                        jpanel.add(jtfport);
                        jtfport.setText("3306");

                        JLabel jls4 = new JLabel("\u7528\u6237\u540D:");
                        jls4.setBounds(16, 35, 50, 45);
                        jpanel.add(jls4);
                        jls4.setFont(fontGlobal);

                        final JTextField jtfip1 = new JTextField(18);
                        jtfip1.setBounds(50, 46, 58, 20);
                        jpanel.add(jtfip1);
                        jtfip1.setText("root");

                        JLabel jls5 = new JLabel("\u5BC6\u7801:");
                        jls5.setBounds(125, 35, 50, 45);
                        jpanel.add(jls5);
                        jls5.setFont(fontGlobal);

                        final JPasswordField jtfport1 = new JPasswordField(15);
                        jtfport1.setBounds(150, 46, 75, 20);
                        jpanel.add(jtfport1);
                        jtfport1.setText("root");

                        JLabel jls6 = new JLabel("\u6570\u636E\u5E93\u540D:");
                        jls6.setBounds(6, 65, 50, 45);
                        jpanel.add(jls6);
                        jls6.setFont(fontGlobal);

                        final JTextField jtfip11 = new JTextField(18);
                        jtfip11.setBounds(50, 77, 58, 20);
                        jpanel.add(jtfip11);
                        jtfip11.setText("test");

                        JLabel jls51 = new JLabel("\u7F16\u7801:");
                        jls51.setBounds(123, 65, 50, 45);
                        jpanel.add(jls51);
                        jls51.setFont(fontGlobal);

                        final JTextField jtfport11 = new JTextField(15);
                        jtfport11.setBounds(150, 77, 75, 20);
                        jpanel.add(jtfport11);
                        jtfport11.setText("utf-8");

                        final JButton linkBtn = new JButton("\u521D\u59CB\u5316DButils\u5DE5\u5177\u7C7B");
                        linkBtn.setBounds(56, 111, 150, 20);
                        jpanel.add(linkBtn);
                        linkBtn.setMargin(new Insets(0, 0, 0, 0));// \u8FD9\u6837\u8BBE\u7F6Ebutton\u4E2D\u7684\u5B57\u4F53\u4E0Ebutton\u65E0\u4E0A\u4E0B\u8FB9\u8DDD
                        linkBtn.setFont(fontGlobal);
                        MouseAdapter linkServerListener = new MouseAdapter() {
                            public void mouseClicked(MouseEvent e) {
                                String ip = jtfip.getText().trim();
                                int port = Integer.parseInt(jtfport.getText()
                                        .trim());
                                username = jtfip1.getText().trim();
                                password = jtfport1.getText().trim();
                                String databaseName = jtfip11.getText().trim();
                                String code = jtfport11.getText().trim();
                                driver = "com.mysql.jdbc.Driver";
                                url = "jdbc:mysql://" + ip + ":" + port + "/"
                                        + databaseName
                                        + "?unicode%%=true&characterEncoding%%="
                                        + code;
//								RegisterUtil.putStringSystemRoot("mysql.driver",driver);
//								RegisterUtil.putStringSystemRoot("mysql.url", url);
//								RegisterUtil.putStringSystemRoot("mysql.username",username);
//								RegisterUtil.putStringSystemRoot("mysql.password",password);
                                //System.out.println(RegisterUtilgetStringFromSystemRoot("mysql.driver"));
                                System.out.println(driver);
                                System.out.println(url);
                                System.out.println(username);
                                System.out.println(password);

                                try {
                                    conn = DriverManager.getConnection(url,
                                            username, password);
                                    if (conn != null) {
                                        conn.setAutoCommit(getAutoCommit());
                                        connHolder.set(conn);
                                        linkBtn.setText("\u521D\u59CB\u5316\u6210\u529F\uFF01\u8BF7\u5173\u95ED\u7A97\u4F53");
                                        //RegisterUtil.putIntSystemRoot("isInit", 0);
                                    }
                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                }
                            }
                        };
                        linkBtn.addMouseListener(linkServerListener);
                    }
                };
                t_mysql.start();
            }
        }
    }

    /**
     * @return List<String>
     * @category \u83B7\u53D6\u6240\u6709\u7684\u6570\u636E\u5E93\u540D
     */
    public static List<String> getAllDatabaseNames() {
        List<String> list = new ArrayList<String>();
        try {
            ResultSet rs = DButils.getConnection().prepareStatement(
                    "show databases").executeQuery();
            while (rs.next()) {
                list.add(rs.getString(1));
            }
            rs.close();
        } catch (SQLException e) {
            init();
            logger.info("DButils.getAllDatabaseNames() getAllDatabaseNames error");
            exceptionPrint(e);
        }
        return list;

    }

    /**
     * @param sql\u8BED\u53E5 sql
     * @return \u67E5\u8BE2\u8FD4\u56DE\u7684\u96C6\u5408
     * @category \u7ED9\u8DDDsql\u8BED\u53E5\u8FD4\u56DEResultSet\u96C6\u5408
     */
    public static ResultSet getResultSet(String sql) {
        ResultSet rs = null;
        try {
            rs = getConnection().createStatement().executeQuery(sql);
        } catch (SQLException e) {
            logger.info("DButils.getResultSet() getResultSet error");
            exceptionPrint(e);
        }
        return rs;
    }

    /**
     * @param sqls \u8981\u6267\u884C\u7684sql\u8BED\u53E5
     * @return int[] \u6267\u884CBatch\u540E\u7684\u6570\u7EC4
     * @category \u6279\u5904\u7406sqkl\u8BED\u53E5
     */
    public static int[] updateStatement(String[] sqls) {
        Statement st = null;
        int result[] = null;
        try {
            st = getConnection().createStatement();
            for (int i = 0; i < sqls.length; i++) {
                st.addBatch(sqls[i]);
            }
            result = st.executeBatch();
            st.close();
            st = null;
        } catch (SQLException e) {
            st = null;
            logger.info("DButils.update() update error");
            exceptionPrint(e);
        }
        return result;
    }

    /**
     * @param c          Person class
     * @param fromNumber litmi from
     * @param number     to
     * @param columnName \u8868\u4E2D\u7684\u5217\u540D
     * @param orderby    \u6309\u4EC0\u4E48\u6392\u5E8F
     * @return
     */
    public static List getEntitysLimitOderBy(Class c, int fromNumber,
                                             int number, String columnName, String orderby) {
        String tableName = c.getSimpleName().toLowerCase();// person \u8868\u7684\u540D\u5B57
        if (!isTableExist(tableName)) {
            System.err.println("\u8868'" + tableName + "'\u4E0D\u5B58\u5728");
            return null;
        }
        List<String> columnList = getColumns(c);
        if (!columnList.contains(columnName)) {
            System.err.println("\u5217\u540D'" + columnName + "'\u4E0D\u5B58\u5728");
            logger.info("DButils.getEntitysLimitOderBy()" + columnName
                    + " is not isExist! \u9000\u51FA");
            return null;
        }
        if (orderby == null
                || (!orderby.equalsIgnoreCase("asc") && !orderby
                .equalsIgnoreCase("desc"))) {
            logger.info("DButils.getEntitysLimitOderBy() \u6392\u5E8F\u5173\u952E\u5B57\u9519\u8BEF");
            return null;
        }
        List list = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Field[] fields = c.getFields();// \u83B7\u53D6\u6240\u6709\u7684public\u5C5E\u6027
        if (fields.length == 0) {
            // fields=c.getDeclaredFields();//\u83B7\u53D6\u6240\u6709\u7684private\u5C5E\u6027
            fields = c.getDeclaredFields();// \u83B7\u53D6get set \u65B9\u6CD5
        }
        StringBuilder sql = new StringBuilder("select * from ").append(
                tableName).append(" order by ").append(columnName + " ")
                .append(orderby).append(" limit ?,?");
        logger.info("DButils.getEntitysLimitOderBy() sql:"
                + sql.toString());
        try {
            if (!isTableExist(tableName)) {
                System.err.println("\u8868\u4E0D\u5B58\u5728");
                return list;
            }
            ps = (PreparedStatement) getConnection().prepareStatement(
                    sql.toString());
            ps.setInt(1, fromNumber);
            ps.setInt(2, number);
            rs = ps.executeQuery();
            System.out.println("%n" + ps.toString().split(":")[1]);
            logger.info("DButils.getEntitysLimitOderBy()" + ps.toString()
                    .split(":")[1]);

            Object obj = null;
            while (rs.next()) {
                obj = c.newInstance();
                for (int k = 0; k < fields.length; k++) {
                    fields[k].setAccessible(true);
                    fields[k].set(obj, rs.getObject(k + 1));
                }
                list.add(obj);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            logger.info("DButils.getEntitysLimitOderBy() getEntitysLimitOderBy error");
            exceptionPrint(e);
        } finally {
            rs = null;
            ps = null;
        }
        return list;

    }


}
