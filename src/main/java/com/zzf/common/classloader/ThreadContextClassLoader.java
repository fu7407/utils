package com.zzf.common.classloader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ThreadContextClassLoader {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		System.out.println(contextClassLoader);

		Thread.currentThread().setContextClassLoader(new MyClassLoader());
		System.out.println(Thread.currentThread().getContextClassLoader());

		Class.forName("com.mysql.jdbc.Driver");// (---)
		Connection conn = DriverManager.getConnection("");
	}
}
