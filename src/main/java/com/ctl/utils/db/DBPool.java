package com.ctl.utils.db;

//
//\u4E00\u4E2A\u6548\u679C\u975E\u5E38\u4E0D\u9519\u7684JAVA\u6570\u636E\u5E93\u8FDE\u63A5\u6C60.
//\u867D\u7136\u73B0\u5728\u7528APACHE COMMONS DBCP\u53EF\u4EE5\u975E\u5E38\u65B9\u4FBF\u7684\u5EFA\u7ACB\u6570\u636E\u5E93\u8FDE\u63A5\u6C60\uFF0C
//\u4F46\u662F\u50CF\u8FD9\u7BC7\u6587\u7AE0\u628A\u6570\u636E\u5E93\u8FDE\u63A5\u6C60\u7684\u5185\u90E8\u539F\u7406\u5199\u7684\u8FD9\u4E48\u900F\u5F7B\uFF0C\u6CE8\u89C6\u8FD9\u4E48\u5B8C\u6574\uFF0C
//\u771F\u662F\u975E\u5E38\u96BE\u5F97\uFF0C\u8BA9\u5F00\u53D1\u4EBA\u5458\u53EF\u4EE5\u66F4\u6DF1\u5C42\u6B21\u7684\u7406\u89E3\u6570\u636E\u5E93\u8FDE\u63A5\u6C60\uFF0C\u771F\u662F\u975E\u5E38\u611F
//\u8C22\u8FD9\u7BC7\u6587\u7AE0\u7684\u4F5C\u8005\u3002
//
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Vector;

 class PooledConnection {
	Connection connection = null; // \u6570\u636E\u5E93\u8FDE\u63A5
	boolean busy = false; // \u6B64\u8FDE\u63A5\u662F\u5426\u6B63\u5728\u4F7F\u7528\u7684\u6807\u5FD7\uFF0C\u9ED8\u8BA4\u6CA1\u6709\u6B63\u5728\u4F7F\u7528

	// \u6784\u9020\u51FD\u6570\uFF0C\u6839\u636E\u4E00\u4E2A Connection \u6784\u544A\u4E00\u4E2A PooledConnection \u5BF9\u8C61
	public PooledConnection(Connection connection) {
		this.connection = connection;
	}

	// \u8FD4\u56DE\u6B64\u5BF9\u8C61\u4E2D\u7684\u8FDE\u63A5
	public Connection getConnection() {
		return connection;
	}

	// \u8BBE\u7F6E\u6B64\u5BF9\u8C61\u7684\uFF0C\u8FDE\u63A5
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	// \u83B7\u5F97\u5BF9\u8C61\u8FDE\u63A5\u662F\u5426\u5FD9
	public boolean isBusy() {
		return busy;
	}

	// \u8BBE\u7F6E\u5BF9\u8C61\u7684\u8FDE\u63A5\u6B63\u5728\u5FD9
	public void setBusy(boolean busy) {
		this.busy = busy;
	}
}

public class DBPool {

	private String jdbcDriver = ""; // \u6570\u636E\u5E93\u9A71\u52A8
	private String dbUrl = ""; // \u6570\u636E URL
	private String dbUsername = ""; // \u6570\u636E\u5E93\u7528\u6237\u540D
	private String dbPassword = ""; // \u6570\u636E\u5E93\u7528\u6237\u5BC6\u7801
	private String testTable = ""; // \u6D4B\u8BD5\u8FDE\u63A5\u662F\u5426\u53EF\u7528\u7684\u6D4B\u8BD5\u8868\u540D\uFF0C\u9ED8\u8BA4\u6CA1\u6709\u6D4B\u8BD5\u8868
	private int initialConnections = 10; // \u8FDE\u63A5\u6C60\u7684\u521D\u59CB\u5927\u5C0F
	private int incrementalConnections = 5; // \u8FDE\u63A5\u6C60\u81EA\u52A8\u589E\u52A0\u7684\u5927\u5C0F
	private int maxConnections = 50; // \u8FDE\u63A5\u6C60\u6700\u5927\u7684\u5927\u5C0F
	private Vector<PooledConnection> connections = null; // \u5B58\u653E\u8FDE\u63A5\u6C60\u4E2D\u6570\u636E\u5E93\u8FDE\u63A5\u7684\u5411\u91CF ,
															// \u521D\u59CB\u65F6\u4E3A null
	// \u5B83\u4E2D\u5B58\u653E\u7684\u5BF9\u8C61\u4E3A PooledConnection \u578B

	public DBPool(String jdbcDriver, String dbUrl, String dbUsername,
			String dbPassword) {

		this.jdbcDriver = jdbcDriver;
		this.dbUrl = dbUrl;
		this.dbUsername = dbUsername;
		this.dbPassword = dbPassword;
	}

	public int getInitialConnections() {
		return this.initialConnections;
	}

	public void setInitialConnections(int initialConnections) {
		this.initialConnections = initialConnections;
	}

	public int getIncrementalConnections() {
		return this.incrementalConnections;
	}

	public void setIncrementalConnections(int incrementalConnections) {
		this.incrementalConnections = incrementalConnections;
	}

	public int getMaxConnections() {
		return this.maxConnections;
	}

	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}

	public String getTestTable() {
		return this.testTable;
	}

	public void setTestTable(String testTable) {
		this.testTable = testTable;
	}

	public synchronized void createPool() throws Exception {
		// \u786E\u4FDD\u8FDE\u63A5\u6C60\u6CA1\u6709\u521B\u5EFA
		// \u5047\u5982\u8FDE\u63A5\u6C60\u5DF1\u7ECF\u521B\u5EFA\u4E86\uFF0C\u4FDD\u5B58\u8FDE\u63A5\u7684\u5411\u91CF connections \u4E0D\u4F1A\u4E3A\u7A7A
		if (connections != null) {
			return; // \u5047\u5982\u5DF1\u7ECF\u521B\u5EFA\uFF0C\u5219\u8FD4\u56DE
		}
		// \u5B9E\u4F8B\u5316 JDBC Driver \u4E2D\u6307\u5B9A\u7684\u9A71\u52A8\u7C7B\u5B9E\u4F8B
		Driver driver = (Driver) (Class.forName(this.jdbcDriver).newInstance());
		DriverManager.registerDriver(driver); // \u6CE8\u518C JDBC \u9A71\u52A8\u7A0B\u5E8F
		// \u521B\u5EFA\u4FDD\u5B58\u8FDE\u63A5\u7684\u5411\u91CF , \u521D\u59CB\u65F6\u6709 0 \u4E2A\u5143\u7D20
		connections = new Vector<PooledConnection>();
		// \u6839\u636E initialConnections \u4E2D\u8BBE\u7F6E\u7684\u503C\uFF0C\u521B\u5EFA\u8FDE\u63A5\u3002
		createConnections(this.initialConnections);
		System.out.println(" \u6570\u636E\u5E93\u8FDE\u63A5\u6C60\u521B\u5EFA\u6210\u529F\uFF01 ");
	}

	private void createConnections(int numConnections) throws SQLException {
		// \u5FAA\u73AF\u521B\u5EFA\u6307\u5B9A\u6570\u76EE\u7684\u6570\u636E\u5E93\u8FDE\u63A5
		for (int x = 0; x < numConnections; x++) {
			// \u662F\u5426\u8FDE\u63A5\u6C60\u4E2D\u7684\u6570\u636E\u5E93\u8FDE\u63A5\u7684\u6570\u91CF\u5DF1\u7ECF\u8FBE\u5230\u6700\u5927\uFF1F\u6700\u5927\u503C\u7531\u7C7B\u6210\u5458 maxConnections
			// \u6307\u51FA\uFF0C\u5047\u5982 maxConnections \u4E3A 0 \u6216\u8D1F\u6570\uFF0C\u8868\u793A\u8FDE\u63A5\u6570\u91CF\u6CA1\u6709\u9650\u5236\u3002
			// \u5047\u5982\u8FDE\u63A5\u6570\u5DF1\u7ECF\u8FBE\u5230\u6700\u5927\uFF0C\u5373\u9000\u51FA\u3002
			if (this.maxConnections > 0
					&& this.connections.size() >= this.maxConnections) {
				break;
			}
			// add a new PooledConnection object to connections vector
			// \u589E\u52A0\u4E00\u4E2A\u8FDE\u63A5\u5230\u8FDE\u63A5\u6C60\u4E2D\uFF08\u5411\u91CF connections \u4E2D\uFF09
			try {
				connections.addElement(new PooledConnection(newConnection()));
			} catch (SQLException e) {
				System.out.println(" \u521B\u5EFA\u6570\u636E\u5E93\u8FDE\u63A5\u5931\u8D25\uFF01 " + e.getMessage());
				throw new SQLException();
			}
			System.out.println(" \u6570\u636E\u5E93\u8FDE\u63A5\u5DF1\u521B\u5EFA ......");
		}
	}

	private Connection newConnection() throws SQLException {
		// \u521B\u5EFA\u4E00\u4E2A\u6570\u636E\u5E93\u8FDE\u63A5
		Connection conn = DriverManager.getConnection(dbUrl, dbUsername,
				dbPassword);
		// \u5047\u5982\u8FD9\u662F\u7B2C\u4E00\u6B21\u521B\u5EFA\u6570\u636E\u5E93\u8FDE\u63A5\uFF0C\u5373\u68C0\u67E5\u6570\u636E\u5E93\uFF0C\u83B7\u5F97\u6B64\u6570\u636E\u5E93\u7B54\u5E94\u652F\u6301\u7684
		// \u6700\u5927\u5BA2\u6237\u8FDE\u63A5\u6570\u76EE
		// connections.size()==0 \u8868\u793A\u76EE\u524D\u6CA1\u6709\u8FDE\u63A5\u5DF1\u88AB\u521B\u5EFA
		if (connections.size() == 0) {
			DatabaseMetaData metaData = conn.getMetaData();
			int driverMaxConnections = metaData.getMaxConnections();
			// \u6570\u636E\u5E93\u8FD4\u56DE\u7684 driverMaxConnections \u82E5\u4E3A 0 \uFF0C\u8868\u793A\u6B64\u6570\u636E\u5E93\u6CA1\u6709\u6700\u5927
			// \u8FDE\u63A5\u9650\u5236\uFF0C\u6216\u6570\u636E\u5E93\u7684\u6700\u5927\u8FDE\u63A5\u9650\u5236\u4E0D\u77E5\u9053
			// driverMaxConnections \u4E3A\u8FD4\u56DE\u7684\u4E00\u4E2A\u6574\u6570\uFF0C\u8868\u793A\u6B64\u6570\u636E\u5E93\u7B54\u5E94\u5BA2\u6237\u8FDE\u63A5\u7684\u6570\u76EE
			// \u5047\u5982\u8FDE\u63A5\u6C60\u4E2D\u8BBE\u7F6E\u7684\u6700\u5927\u8FDE\u63A5\u6570\u91CF\u5927\u4E8E\u6570\u636E\u5E93\u7B54\u5E94\u7684\u8FDE\u63A5\u6570\u76EE , \u5219\u7F6E\u8FDE\u63A5\u6C60\u7684\u6700\u5927
			// \u8FDE\u63A5\u6570\u76EE\u4E3A\u6570\u636E\u5E93\u7B54\u5E94\u7684\u6700\u5927\u6570\u76EE
			if (driverMaxConnections > 0
					&& this.maxConnections > driverMaxConnections) {
				this.maxConnections = driverMaxConnections;
			}
		}
		return conn; // \u8FD4\u56DE\u521B\u5EFA\u7684\u65B0\u7684\u6570\u636E\u5E93\u8FDE\u63A5
	}

	public synchronized Connection getConnection() throws SQLException {
		// \u786E\u4FDD\u8FDE\u63A5\u6C60\u5DF1\u88AB\u521B\u5EFA
		if (connections == null) {
			return null; // \u8FDE\u63A5\u6C60\u8FD8\u6CA1\u521B\u5EFA\uFF0C\u5219\u8FD4\u56DE null
		}
		Connection conn = getFreeConnection(); // \u83B7\u5F97\u4E00\u4E2A\u53EF\u7528\u7684\u6570\u636E\u5E93\u8FDE\u63A5
		// \u5047\u5982\u76EE\u524D\u6CA1\u6709\u53EF\u4EE5\u4F7F\u7528\u7684\u8FDE\u63A5\uFF0C\u5373\u6240\u6709\u7684\u8FDE\u63A5\u90FD\u5728\u4F7F\u7528\u4E2D
		while (conn == null) {
			// \u7B49\u4E00\u4F1A\u518D\u8BD5
			wait(250);
			conn = getFreeConnection(); // \u91CD\u65B0\u518D\u8BD5\uFF0C\u76F4\u5230\u83B7\u5F97\u53EF\u7528\u7684\u8FDE\u63A5\uFF0C\u5047\u5982
			// getFreeConnection() \u8FD4\u56DE\u7684\u4E3A null
			// \u5219\u8868\u660E\u521B\u5EFA\u4E00\u6279\u8FDE\u63A5\u540E\u4E5F\u4E0D\u53EF\u83B7\u5F97\u53EF\u7528\u8FDE\u63A5
		}
		return conn; // \u8FD4\u56DE\u83B7\u5F97\u7684\u53EF\u7528\u7684\u8FDE\u63A5
	}

	private Connection getFreeConnection() throws SQLException {
		// \u4ECE\u8FDE\u63A5\u6C60\u4E2D\u83B7\u5F97\u4E00\u4E2A\u53EF\u7528\u7684\u6570\u636E\u5E93\u8FDE\u63A5
		Connection conn = findFreeConnection();
		if (conn == null) {
			// \u5047\u5982\u76EE\u524D\u8FDE\u63A5\u6C60\u4E2D\u6CA1\u6709\u53EF\u7528\u7684\u8FDE\u63A5
			// \u521B\u5EFA\u4E00\u4E9B\u8FDE\u63A5
			createConnections(incrementalConnections);
			// \u91CD\u65B0\u4ECE\u6C60\u4E2D\u67E5\u627E\u662F\u5426\u6709\u53EF\u7528\u8FDE\u63A5
			conn = findFreeConnection();
			if (conn == null) {
				// \u5047\u5982\u521B\u5EFA\u8FDE\u63A5\u540E\u4ECD\u83B7\u5F97\u4E0D\u5230\u53EF\u7528\u7684\u8FDE\u63A5\uFF0C\u5219\u8FD4\u56DE null
				return null;
			}
		}
		return conn;
	}

	private Connection findFreeConnection() throws SQLException {
		Connection conn = null;
		PooledConnection pConn = null;
		// \u83B7\u5F97\u8FDE\u63A5\u6C60\u5411\u91CF\u4E2D\u6240\u6709\u7684\u5BF9\u8C61
		Enumeration<PooledConnection> enumerate = connections.elements();
		// \u904D\u5386\u6240\u6709\u7684\u5BF9\u8C61\uFF0C\u770B\u662F\u5426\u6709\u53EF\u7528\u7684\u8FDE\u63A5
		while (enumerate.hasMoreElements()) {
			pConn = (PooledConnection) enumerate.nextElement();
			if (!pConn.isBusy()) {
				// \u5047\u5982\u6B64\u5BF9\u8C61\u4E0D\u5FD9\uFF0C\u5219\u83B7\u5F97\u5B83\u7684\u6570\u636E\u5E93\u8FDE\u63A5\u5E76\u628A\u5B83\u8BBE\u4E3A\u5FD9
				conn = pConn.getConnection();
				pConn.setBusy(true);
				// \u6D4B\u8BD5\u6B64\u8FDE\u63A5\u662F\u5426\u53EF\u7528
				if (!testConnection(conn)) {
					// \u5047\u5982\u6B64\u8FDE\u63A5\u4E0D\u53EF\u518D\u7528\u4E86\uFF0C\u5219\u521B\u5EFA\u4E00\u4E2A\u65B0\u7684\u8FDE\u63A5\uFF0C
					// \u5E76\u66FF\u6362\u6B64\u4E0D\u53EF\u7528\u7684\u8FDE\u63A5\u5BF9\u8C61\uFF0C\u5047\u5982\u521B\u5EFA\u5931\u8D25\uFF0C\u8FD4\u56DE null
					try {
						conn = newConnection();
					} catch (SQLException e) {
						System.out.println(" \u521B\u5EFA\u6570\u636E\u5E93\u8FDE\u63A5\u5931\u8D25\uFF01 " + e.getMessage());
						return null;
					}
					pConn.setConnection(conn);
				}
				break; // \u5DF1\u7ECF\u627E\u5230\u4E00\u4E2A\u53EF\u7528\u7684\u8FDE\u63A5\uFF0C\u9000\u51FA
			}
		}
		return conn; // \u8FD4\u56DE\u627E\u5230\u5230\u7684\u53EF\u7528\u8FDE\u63A5
	}

	private boolean testConnection(Connection conn) {
		try {
			// \u5224\u5B9A\u6D4B\u8BD5\u8868\u662F\u5426\u5B58\u5728
			if (testTable.equals("")) {
				// \u5047\u5982\u6D4B\u8BD5\u8868\u4E3A\u7A7A\uFF0C\u8BD5\u7740\u4F7F\u7528\u6B64\u8FDE\u63A5\u7684 setAutoCommit() \u65B9\u6CD5
				// \u6765\u5224\u5B9A\u8FDE\u63A5\u5426\u53EF\u7528\uFF08\u6B64\u65B9\u6CD5\u53EA\u5728\u90E8\u5206\u6570\u636E\u5E93\u53EF\u7528\uFF0C\u5047\u5982\u4E0D\u53EF\u7528 ,
				// \u629B\u51FA\u5F02\u5E38\uFF09\u3002\u6CE8\u91CD\uFF1A\u4F7F\u7528\u6D4B\u8BD5\u8868\u7684\u65B9\u6CD5\u66F4\u53EF\u9760
				conn.setAutoCommit(true);
			} else { // \u6709\u6D4B\u8BD5\u8868\u7684\u65F6\u5019\u4F7F\u7528\u6D4B\u8BD5\u8868\u6D4B\u8BD5
				// check if this connection is valid
				Statement stmt = conn.createStatement();
				stmt.execute("select count(*) from " + testTable);
			}
		} catch (SQLException e) {
			// \u4E0A\u9762\u629B\u51FA\u5F02\u5E38\uFF0C\u6B64\u8FDE\u63A5\u5DF1\u4E0D\u53EF\u7528\uFF0C\u5173\u95ED\u5B83\uFF0C\u5E76\u8FD4\u56DE false;
			closeConnection(conn);
			return false;
		}
		// \u8FDE\u63A5\u53EF\u7528\uFF0C\u8FD4\u56DE true
		return true;
	}

	public void returnConnection(Connection conn) {
		// \u786E\u4FDD\u8FDE\u63A5\u6C60\u5B58\u5728\uFF0C\u5047\u5982\u8FDE\u63A5\u6CA1\u6709\u521B\u5EFA\uFF08\u4E0D\u5B58\u5728\uFF09\uFF0C\u76F4\u63A5\u8FD4\u56DE
		if (connections == null) {
			System.out.println(" \u8FDE\u63A5\u6C60\u4E0D\u5B58\u5728\uFF0C\u65E0\u6CD5\u8FD4\u56DE\u6B64\u8FDE\u63A5\u5230\u8FDE\u63A5\u6C60\u4E2D !");
			return;
		}
		PooledConnection pConn = null;
		Enumeration<PooledConnection> enumerate = connections.elements();
		// \u904D\u5386\u8FDE\u63A5\u6C60\u4E2D\u7684\u6240\u6709\u8FDE\u63A5\uFF0C\u627E\u5230\u8FD9\u4E2A\u8981\u8FD4\u56DE\u7684\u8FDE\u63A5\u5BF9\u8C61
		while (enumerate.hasMoreElements()) {
			pConn = (PooledConnection) enumerate.nextElement();
			// \u5148\u627E\u5230\u8FDE\u63A5\u6C60\u4E2D\u7684\u8981\u8FD4\u56DE\u7684\u8FDE\u63A5\u5BF9\u8C61
			if (conn == pConn.getConnection()) {
				// \u627E\u5230\u4E86 , \u8BBE\u7F6E\u6B64\u8FDE\u63A5\u4E3A\u7A7A\u95F2\u72B6\u6001
				pConn.setBusy(false);
				break;
			}
		}
	}

	public synchronized void refreshConnections() throws SQLException {
		// \u786E\u4FDD\u8FDE\u63A5\u6C60\u5DF1\u521B\u65B0\u5B58\u5728
		if (connections == null) {
			System.out.println(" \u8FDE\u63A5\u6C60\u4E0D\u5B58\u5728\uFF0C\u65E0\u6CD5\u5237\u65B0 !");
			return;
		}
		PooledConnection pConn = null;
		Enumeration<PooledConnection> enumerate = connections.elements();
		while (enumerate.hasMoreElements()) {
			// \u83B7\u5F97\u4E00\u4E2A\u8FDE\u63A5\u5BF9\u8C61
			pConn = (PooledConnection) enumerate.nextElement();
			// \u5047\u5982\u5BF9\u8C61\u5FD9\u5219\u7B49 5 \u79D2 ,5 \u79D2\u540E\u76F4\u63A5\u5237\u65B0
			if (pConn.isBusy()) {
				wait(5000); // \u7B49 5 \u79D2
			}
			// \u5173\u95ED\u6B64\u8FDE\u63A5\uFF0C\u7528\u4E00\u4E2A\u65B0\u7684\u8FDE\u63A5\u4EE3\u66FF\u5B83\u3002
			closeConnection(pConn.getConnection());
			pConn.setConnection(newConnection());
			pConn.setBusy(false);
		}
	}

	public synchronized void closeConnectionPool() throws SQLException {
		// \u786E\u4FDD\u8FDE\u63A5\u6C60\u5B58\u5728\uFF0C\u5047\u5982\u4E0D\u5B58\u5728\uFF0C\u8FD4\u56DE
		if (connections == null) {
			System.out.println(" \u8FDE\u63A5\u6C60\u4E0D\u5B58\u5728\uFF0C\u65E0\u6CD5\u5173\u95ED !");
			return;
		}
		PooledConnection pConn = null;
		Enumeration<PooledConnection> enumerate = connections.elements();
		while (enumerate.hasMoreElements()) {
			pConn = (PooledConnection) enumerate.nextElement();
			// \u5047\u5982\u5FD9\uFF0C\u7B49 5 \u79D2
			if (pConn.isBusy()) {
				wait(5000); // \u7B49 5 \u79D2
			}
			// 5 \u79D2\u540E\u76F4\u63A5\u5173\u95ED\u5B83
			closeConnection(pConn.getConnection());
			// \u4ECE\u8FDE\u63A5\u6C60\u5411\u91CF\u4E2D\u5220\u9664\u5B83
			connections.removeElement(pConn);
		}
		// \u7F6E\u8FDE\u63A5\u6C60\u4E3A\u7A7A
		connections = null;
	}

	private synchronized void closeConnection(Connection conn) {
		try {//\u4ECEvector\u4E2D\u627E\u5230\u8981\u5173\u95ED\u7684Connection\u5148\u5173\u95ED\u7136\u540E\u4ECE\u961F\u5217\u4E2D\u5220\u9664
			PooledConnection pConn = null;
			Enumeration<PooledConnection> enumerate = connections.elements();
			while (enumerate.hasMoreElements()) {
				pConn = (PooledConnection) enumerate.nextElement();
				if(conn==pConn.getConnection()){
					conn.close();
					connections.removeElement(pConn);
				}
			}
			
			
			conn.close();
		} catch (SQLException e) {
			System.out.println(" \u5173\u95ED\u6570\u636E\u5E93\u8FDE\u63A5\u51FA\u9519\uFF1A " + e.getMessage());
		}
	}

	private void wait(int mSeconds) {
		try {
			Thread.sleep(mSeconds);
		} catch (InterruptedException e) {
		}
	}


	public static void main(String[] args) {

		//DBPool connPool = new DBPool("oracle.jdbc.driver.OracleDriver","jdbc:oracle:thin:@127.0.0.1:1521:fastatm", "fastatm","fastatm");
		// service mariadb start mysql需要使用高版本
		DBPool connPool = new DBPool("com.mysql.cj.jdbc.Driver","jdbc:mysql://192.168.42.7:3306/test?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT&useSSL=false", "ctl","liebe");
		try {
			connPool.createPool();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			for (int i = 0; i < 10; i++) {
				Connection conn = connPool.getConnection();
				connPool.closeConnection(conn);
			}
		} catch (SQLException ex1) {
			ex1.printStackTrace();
		}

	}

}