package com.zzf.common.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Hashtable;

public class ConnectionPoolManager {
	
	private Hashtable<Connection,String> connectionList = new Hashtable<Connection,String>();// 这个集合是为了装Connection对象用的

	private int maxTotalCount = 0;// 最大空闲数

	private int checkingMS = 0;// 每次检查等待的毫秒数

	private int checkingMaxTimes = 0;// 检查的最大次数

	private String jdbcDriver = ""; // 数据库的驱动
	private String url = ""; // 数据库的URL
	private String userName = ""; // 数据库的帐号
	private String userPassword = "";// 数据库的密码

	public ConnectionPoolManager(int fitCount, String jdbcDriver, String url,
			String userName, String userPassword, int maxTotolCount,
			int checkingMS, int checkingMaxTimes) throws Exception {
		this.maxTotalCount = maxTotolCount; // 这个是最大空闲数
		this.checkingMaxTimes = checkingMaxTimes; // 这个是检查的最大次数
		this.checkingMS = checkingMS; // 这个是每次检查等待的毫秒数
		this.url = url; // 这是连接数据库的URL
		this.userName = userName; // 这个是连接数据库的帐号
		this.userPassword = userPassword; // 这个是连接数据库的密码
		this.jdbcDriver = jdbcDriver; // 这个是连接数据库的驱动

		for (int i = 0; i < fitCount; i++) { // 这个是先初始化Connetion如果大于空闲的数量的话,循环就退出
			Class.forName(jdbcDriver); 
			Connection conn = DriverManager.getConnection(url, userName,
					userPassword);
			this.connectionList.put(conn, "FREE"); // 把这个连接把它添加到集合当中去
		}
	}

	/**
	 * 获取任意一个FREE的Connection
	 * 
	 * @return
	 */
	private Connection getFreeConnection() { 
		Object[] objList = this.connectionList.keySet().toArray(); // 查看SET的视图,返回一个包含set中所有元素的数组
		for (int i=0;i<objList.length;i++) { // 然后开始遍历Object的类型的数组
			Object obj = objList[i];
			String mode = (String) this.connectionList.get(obj);
			if (true == mode.equalsIgnoreCase("FREE")) {
				return (Connection) obj;				
			}
		}
		return null;						
	}

	/**
	 * 获取 Connection  如果没有的话就等待,等待超时的话就自动的退出了
	 * @return
	 * @throws Exception
	 */
	public Connection getConnection() throws Exception {  
		Connection conn = this.getFreeConnection();	
		if (null != conn) {					
			this.connectionList.put(conn, "BUSY");// 修改当前Connection的状态为BUSY	
			return conn;				
		}

		int curCount = this.connectionList.size();		
		if (curCount >= this.maxTotalCount) {				//判断如果INT的变量大于最大或等于最大空闲数的话
			for (int i = 0; i < this.checkingMaxTimes; i++) {  //开始循环如果I这个变量超过了最大检查数的话,就抛出异常,
				Thread.sleep(checkingMS);				
				Connection curConn = this.getFreeConnection();
				if (null != curConn) {
					this.connectionList.put(conn, "BUSY");			//就把VALUES值改成忙的状态
					return curConn;							//返回一个Connection的一个对象
				}
			}
			Exception e = new Exception("等待超时,白白!");
			throw e;
		} else {
			Class.forName(jdbcDriver);
			Connection curConn = DriverManager.getConnection(url,userName, userPassword);
			this.connectionList.put(conn, "BUSY");
			return curConn;
		}
	}

	public void returnConnection(Connection conn) {
		this.connectionList.put(conn, "FREE");
	}

}
