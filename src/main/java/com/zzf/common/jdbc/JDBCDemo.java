package com.zzf.common.jdbc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

/**
 * @author 张增福
 */
public class JDBCDemo {
	
	/**
	 * 
	 * @param className com.ibm.as400.access.AS400JDBCDriver
	 * 					com.mysql.jdbc.Driver
	 * @param url
	 * @param user
	 * @param password
	 * @return
	 */
	public static Connection getConnection(String className,String url,String user,String password) {
		Connection conn = null;
		try {
			Class.forName(className).newInstance();
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static Connection getConnection(String className,String url) {
		Connection conn = null;
		try {
			Class.forName(className).newInstance();
			conn = DriverManager.getConnection(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public void curd(){
		Connection conn = null;
		Statement stam = null;
		ResultSet rs = null;
		try {
			conn = JDBCDemo.getConnection("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/ok", "root", "");
			stam = conn.createStatement();//  创建一个Statement这个对象
			//增加
			String sql = "INSERT INTO haha(name,age) VALUES('哈哈',19)";//创建插入数据把它存放到一个String 字符里面去
			stam.executeUpdate (sql);//executeUpdate方法比execute执行要快一些
			//修改
			sql="UPDATE hoho set name='xiaowang' where age=18";
			stam.execute (sql);
			//删除
			sql = "DELETE FROM HOHO WHERE age = 18";
			stam.execute(sql);
			//查询
			sql = "select * from hoho";
			rs = stam.executeQuery(sql);
			while(rs.next()){
				rs.getString("name");
				rs.getInt(2);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				rs.close();
				stam.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void curdParam(){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JDBCDemo.getConnection("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/ok", "root", "");
			//增加
			String sql = "INSERT INTO huowu(name,money,beizhu) VALUES(?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, "NND");
			ps.setInt(2, 4000);
			ps.setString(3, "hehe");
			ps.executeUpdate();

			//修改
			sql = "UPDATE huowu SET name= 'YESH',money = 6700 ,beizhu = 'OHYERD' WHERE name = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, "nihao");
			ps.executeUpdate();

			//删除
			sql = "DELETE FROM huowu WHERE name = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, "NND");
			ps.executeUpdate();

			//查询
			sql = "select * from hoho";
			sql = "SELECT * FROM huowu WHERE name = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, "YESH");
			rs = ps.executeQuery();
			while(rs.next()){
				rs.getString("name");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				rs.close();
				ps.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 将图片写入到数据库
	 * @param fileName F:\\舒畅\\49aaa34302000xkq_0.jpg
	 */
	public void writePhoto(String fileName){
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = JDBCDemo.getConnection("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/ok", "root", "");
			String sql = "INSERT INTO sajsa (anasa,sakndnsa) VALUES('hehe',?)";
			stat = conn.prepareStatement(sql);
			File file = new File(fileName);
			FileInputStream fos = new FileInputStream(file);
			stat.setBinaryStream(1, fos, (int) file.length());
			stat.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stat.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 将图片查询出来并写入到磁盘
	 */
	public void readPhoto(){
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		InputStream is = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			conn = JDBCDemo.getConnection("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/ok", "root", "");
			String sql = "SELECT * FROM sajsa ";
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			rs.next();
			is = rs.getBinaryStream("sakndnsa");
			file = new File("D://nam.jpg");
			fos = new FileOutputStream(file);
			int bs = 0;
			while (true) {
				bs = is.read();
				if (bs == -1) {
					break;
				} else {
					fos.write(bs);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				fos.close();
				rs.close();
				stat.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 获得数据库的整体综合信息
	 *
	 */
	public void getDatabaseMetaData(){
		Connection conn = null;
		DatabaseMetaData dmd = null;
		try {
			conn = JDBCDemo.getConnection("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/ok", "root", "");
			dmd = conn.getMetaData();
			System.out.println(dmd.getDatabaseMajorVersion());//获取底层数据库的主版本号
			System.out.println(dmd.getUserName());//获取此数据库的已知的用户名称
			System.out.println(dmd.getDatabaseMinorVersion());//底层数据库的次版本号
			System.out.println(dmd.getDriverName());//获取此 JDBC 驱动程序的名称
			System.out.println(dmd.getDriverVersion());//获取此 JDBC 驱动程序的 String 形式的版本号
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void getResultSetMetaData(){
		Connection conn = null;
		ResultSetMetaData rmd = null;
		Statement stat = null;
		ResultSet rs = null;
		try {
			conn = JDBCDemo.getConnection("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/ok", "root", "");
			stat = conn.createStatement();
			String sql = "SELECT * FROM huowu WHERE name = 'YESH'";
			rs = stat.executeQuery(sql);
			rmd = rs.getMetaData();
			System.out.println(rmd.getColumnCount());//返回此 ResultSet 对象中的列数
			System.out.println(rmd.getPrecision(1));//获取指定列的指定列宽
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stat.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 批量事物提交
	 */
	public void batchSQL(){
		Connection conn = null;
		Statement stat = null;
		try {
			conn = JDBCDemo.getConnection("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/ok", "root", "");
			stat = conn.createStatement();
			conn.setAutoCommit(false);//设置为非自动提交模式
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);// 设置事物隔离级别
			String sql = "INSERT INTO huowu(name,money,beizhu) VALUES('wl;kew',3292,'sak')";
			stat.addBatch(sql);
			String sql1 = "INSERT INTO huowu(name,money,beizhu) VALUES('aknksal',2902,'a,m')";
			stat.addBatch(sql1);
			String sql2 = "INSERT INTO huowu(name,money,beizhu) VALUES('asknsakwq',20328,'sakna')";
			stat.addBatch(sql2);
			int s[] = stat.executeBatch();
			conn.commit();//事物提交
			for (int e = 0; e < s.length; e++) {
				System.out.println(s[e]);
			}
			conn.setAutoCommit (true);//设置为自动提交模式
		} catch (Exception e) {
			try {
				conn.rollback();//事物回滚
			} catch (Exception s) {
				s.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				stat.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	public static void main(String[] args) {
		//JDBCDemo test=new JDBCDemo();
	}
}
