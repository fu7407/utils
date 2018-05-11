package com.zzf.common;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.zzf.common.jdbc.JDBCDemo;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 
 * @author 张增福
 * 需要使用jxl.jar(excel的相关类)和jt400.jar(AS400的驱动)
 */
public class FunctionExcelUtil {

	/**
	 * DB 连接方式
	 * 
	 * @return
	 */
	public static Connection getConn() {
		Connection conn = null;
		try {
			Class.forName("com.ibm.as400.access.AS400JDBCDriver").newInstance();
			String url = "jdbc:as400://128.232.9.223/cl4motdta";
			String user = "EUISUSRM";
			String password = "citic123";
			conn = DriverManager.getConnection(url, user, password);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * MYSQl 连接方式
	 * 
	 */
	public static Connection getMysqlConn() {
		Connection conn = null;
		try {
			Class.forName("org.gjt.mm.mysql.Driver").newInstance();
			String url = "jdbc:mysql://localhost:3306/pas?user=root&password=123456&useUnicode=true&characterEncoding=UTF-8";
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 从数据库中导出数据到Excel表中
	 * 
	 */
	public void getData_DBToExcel() throws SQLException {
		Connection conn = FunctionExcelUtil.getConn();
		PreparedStatement prm = null;
		ResultSet re = null;
		String sql = " select crtable from  lcck WHERE compgrp like 'WP%' ";
		try {
			prm = conn.prepareStatement(sql);
			re = prm.executeQuery();
			WritableWorkbook wsb = Workbook.createWorkbook(new File("E:/lcck.xls"));
			WritableSheet ws = wsb.createSheet("lcck", 0);
			int index = 0;
			System.out.println("开始");
			while (re.next()) {
				ws.addCell(new Label(0, index, re.getString("crtable")));
				index++;
				System.out.println("正在导出第" + index + "条数据");
			}
			System.out.println("完成");
			wsb.write();
			wsb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将Excel表中的数据插入到数据库中
	 * 
	 * @throws SQLException
	 */
	public void getData_ExcelToDB() throws SQLException {
		Connection conn = null;
		try {
			conn = FunctionExcelUtil.getConn();
			PreparedStatement prm = null;
			StringBuffer buf = new StringBuffer();
			buf.append("insert into lcjn (CRTABLE,EDITSDATE,EDITEDATE,CALFLAG01,CALFLAG02,CALFLAG03,CALFLAG04,JOB_NAME,USER_PROFILE,DATIME) \n");
			buf.append("values(?,?,?,?,?,?,?,?,?,?)");
			InputStream is = new FileInputStream("/E:lcck.xls");
			Workbook rwb = Workbook.getWorkbook(is);
			Sheet st = rwb.getSheet(0);
			System.out.println("开始");
			for (int i = 0; i < st.getRows(); i++) {
				Cell c00 = st.getCell(0, i);
				prm = conn.prepareStatement(buf.toString());
				prm.setString(1, c00.getContents());
				prm.setString(2, "0");
				prm.setString(3, "0");
				prm.setString(4, "");
				prm.setString(5, "");
				prm.setString(6, "");
				prm.setString(7, "Y");
				prm.setString(8, "liquidate");
				prm.setString(9, "CHN0001536");
				prm.setDate(10, new Date(0));
				prm.execute();
				System.out.println("正在导出第" + i + "条数据");
			}
			System.out.println("完成");
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
		} finally {

		}
	}
	
	public void insertDomainFromExcel() throws SQLException {
		Connection conn = null;
		PreparedStatement prm = null;
		try {
			conn = JDBCDemo.getConnection("org.mariadb.jdbc.Driver", "jdbc:mysql://132.97.184.60:3306/sale_dn?characterEncoding=utf-8", "domain","domain");
			String sql = "insert into saledomain (domain,price,remark) values(?,?,?) ";
			InputStream is = new FileInputStream("E:/yumingexchange.xls");
			Workbook rwb = Workbook.getWorkbook(is);
			Sheet st = rwb.getSheet(1);
			System.out.println("开始");
			for (int i = 1; i < st.getRows(); i++) {
//				System.out.println(st.getCell(0, i).getContents()+","+st.getCell(2, i).getContents()+","+st.getCell(1, i).getContents());
				prm = conn.prepareStatement(sql);
				prm.setString(1, st.getCell(0, i).getContents());
				prm.setString(2, st.getCell(2, i).getContents());
				prm.setString(3, st.getCell(1, i).getContents());
				prm.execute();
				System.out.println("正在导出第" + i + "条数据");
			}
			System.out.println("完成");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getDomain() throws SQLException {
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		try {
			WritableWorkbook wsb = Workbook.createWorkbook(new File("E:/domain.xls"));
			WritableSheet ws = wsb.createSheet("domain", 0);
			int index = 0;
			
			conn = JDBCDemo.getConnection("org.mariadb.jdbc.Driver", "jdbc:mysql://132.97.184.60:3306/domain?characterEncoding=utf-8", "domain","domain");
			String sql = "select * from sys_topdomainprice where status='Y' ";
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			while(rs.next()){
				ws.addCell(new Label(0, index, rs.getString("topcode")));
				index++;
			}
			wsb.write();
			wsb.close();
			System.out.println("完成");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void getDomainToExcel() throws SQLException {
		String character [] = new String[]{"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
		//String character [] = new String[]{"0","1","2","3","4","5","6","7","8","9"};
		//String character [] = new String[]{"a","o","e","i","u","v"};
		try {
			WritableWorkbook wsb = Workbook.createWorkbook(new File("E:/domain4.xls"));
			WritableSheet ws = wsb.createSheet("domain", 0);
			String temp [] = new String[character.length * character.length * character.length];
			
			int t_index = 0;
			for(int i=0;i<character.length;i++) {
				for(int j=0;j<character.length;j++) {
					for(int k=0;k<character.length;k++) {
						temp[t_index] = character[i]+character[j]+character[k];
						t_index++;
					}
				}
			}
			
			int index = 0;
			System.out.println("开始");
			for(int i=0;i<temp.length;i++) {
				ws.addCell(new Label(0, index, temp[i]));
				index++;
				System.out.println("正在导出第" + index + "条数据");
			}
			System.out.println("完成");
			wsb.write();
			wsb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public List<String> getDataFromExcel(String filePath, int col){
		List<String> list = new ArrayList<String>();
		try {
			InputStream is = new FileInputStream(filePath);
			Workbook rwb = Workbook.getWorkbook(is);
			Sheet st = rwb.getSheet(0);
			System.out.println("开始读取文件"+filePath+"的数据");
			for (int i = 0; i < st.getRows(); i++) {
				String data = st.getCell(col, i).getContents();
				if(data!=null && !"".equals(data.trim()))list.add(data);
//				System.out.println("正在导出第" + i + "条数据");
			}
			System.out.println("读取文件"+filePath+"的数据完成");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public void getdiff(){
		try {
			WritableWorkbook wsb = Workbook.createWorkbook(new File("E:/diff.xls"));
			WritableSheet ws = wsb.createSheet("diff", 0);
			int index = 0;
			
			List<String> list_gd  = getDataFromExcel("E:/gd.xls", 1);
			List<String> list_fs  = getDataFromExcel("E:/fs.xls", 1);
			
			List<String> list2  = getDataFromExcel("E:/domain.xls", 0);
			for(int i=0;i<list2.size();i++){
				String temp = list2.get(i);
				if(temp.contains(".广东")){
					for(int j=0;j<list_gd.size();j++){
						if(list_gd.get(j).equals(list2.get(i)))break;
						if(j==list_gd.size()-1 && !list_gd.get(j).equals(list2.get(i))){
							ws.addCell(new Label(0, index, list2.get(i)));
							index++;
							System.out.println("正在导入第" + index + "条数据:"+list2.get(i));
						}
					}
				}else if(temp.contains(".佛山")){
					for(int j=0;j<list_fs.size();j++){
						if(list_fs.get(j).equals(list2.get(i)))break;
						if(j==list_fs.size()-1 && !list_fs.get(j).equals(list2.get(i))){
							ws.addCell(new Label(0, index, list2.get(i)));
							index++;
							System.out.println("正在导入第" + index + "条数据:"+list2.get(i));
						}
					}
				}
			}
			wsb.write();
			wsb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FunctionExcelUtil fun = new FunctionExcelUtil();
		try {
			fun.getDomainToExcel();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}