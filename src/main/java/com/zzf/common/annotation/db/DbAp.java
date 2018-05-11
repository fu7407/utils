package com.zzf.common.annotation.db;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;


/** 通用处理器
 * @author cxy
 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({"DbInfo","Id","columns"})
public class DbAp
{
	static CachedRowSet crs;
	
	public static void saveToDb(Object obj) throws Exception
	{
		Map<String,String> dbInfo=new HashMap<String,String>();  //用来存储数据库相关信息
		Map<String,String> pkInfo=new HashMap<String,String>(); //主键信息
		Map<String,Map<String,Object>> columnInfo=new HashMap<String,Map<String,Object>>(); //字段信息
		
		Class clz=obj.getClass();
		//获取对象的信息
		getClassInfo(clz, dbInfo, pkInfo, columnInfo);
		
		//插入操作
		String uuid=UUID.randomUUID().toString().replaceAll("-", "");
		String insertSql="";
		String colStr="("+pkInfo.get("c")+",";
		String val="('"+uuid+"',";
		//拼装insert语句
		insertSql="insert into "+dbInfo.get("table")+" ";
		for(String one : columnInfo.keySet())
		{
			colStr+=one+",";
			String methodName="get"+String.valueOf(one.charAt(0)).toUpperCase()+one.substring(1);
			Method m=clz.getMethod(methodName, null);
			Object valObj=m.invoke(obj, null);
			if(valObj instanceof String)
			{
				val+="'"+valObj+"',";
			}
			if(valObj instanceof Integer)
			{
				val+=valObj+",";
			}
			
		}
		colStr=colStr.substring(0, colStr.length()-1);
		val=val.substring(0, val.length()-1);
		insertSql=insertSql+colStr+") values "+val+");";
		//System.out.println(insertSql);
		crs.setCommand(insertSql);
		crs.execute();
		crs.close();
		System.out.println(obj.toString()+"插入成功");
	}

	
	
	public static void deleteToDb(Object obj) throws Exception{}
	public static void updateToDb(Object obj) throws Exception{}
	public static void queryFromDb(Class clz) throws Exception
	{
		Map<String,String> dbInfo=new HashMap<String,String>();  //用来存储数据库相关信息
		Map<String,String> pkInfo=new HashMap<String,String>(); //主键信息
		Map<String,Map<String,Object>> columnInfo=new HashMap<String,Map<String,Object>>(); //字段信息
		
		//获取对象的信息
		getClassInfo(clz, dbInfo, pkInfo, columnInfo);
		
		//查询语句
		StringBuilder sql= new StringBuilder("");
		sql.append("select ");
		for(String one : columnInfo.keySet())
		{
			sql.append(one+" as "+columnInfo.get(one).get("d")+",");
		}
		sql.delete(sql.length()-1, sql.length());
		sql.append(" from "+dbInfo.get("table"));
		crs.setCommand(sql.toString());
		crs.execute();
		
		//通用型遍历
		ResultSetMetaData rsm=crs.getMetaData();
		int colNum=rsm.getColumnCount();
		String[] colName=new String[colNum]; //字段名
		String[] colLabel=new String[colNum]; //别名
		for(int i=1;i<=colNum;i++)
		{
			colName[i-1]=rsm.getColumnName(i);
			colLabel[i-1]=rsm.getColumnLabel(i);
		}
		//把结果集封装成List<Map<String,String>>
		List<Map<String,String>> dbData=new ArrayList<Map<String,String>>();
		while(crs.next())
		{
			Map<String,String> one = new HashMap<String, String>();
			for(int i=1;i<=colNum;i++)
			{
				one.put(colLabel[i-1], crs.getString(i));
			}
			dbData.add(one);
		}
		//System.out.println(dbData);
		for(String one:colLabel)
		{
			System.out.print(one+"\t\t");
		}
		System.out.println();
		
		for(Map<String,String> one : dbData)
		{
			for(String one1:colLabel)
			{
				System.out.print(one.get(one1)+"\t\t");
			}
			System.out.println();
		}
	}
	
	/**生成数据库操作
	 * @param clz
	 * @throws Exception
	 */
	public static void createTable(Class clz,Map<String,String> dbInfo, Map<String,String>pkInfo,Map<String,Map<String,Object>> columnInfo) throws Exception
	{
		//数据库信息
		if(clz.isAnnotationPresent(DbInfo.class))
		{
			DbInfo d=(DbInfo) clz.getAnnotation(DbInfo.class);
			dbInfo.put("url",d.url());
			dbInfo.put("un",d.un());
			dbInfo.put("pw",d.pw());
			dbInfo.put("table",d.tableName());
		}
		RowSetFactory rsf=RowSetProvider.newFactory();
		crs=rsf.createCachedRowSet();
		crs.setUrl(dbInfo.get("url"));
		crs.setUsername(dbInfo.get("un"));
		crs.setPassword(dbInfo.get("pw"));
		
		StringBuilder sql= new StringBuilder("CREATE TABLE IF NOT EXISTS ");
		sql.append(dbInfo.get("table"));
		sql.append(" ( ");
		sql.append(pkInfo.get("c")+" "+pkInfo.get("t")+" NOT NULL UNIQUE PRIMARY KEY,");
		for(String one : columnInfo.keySet())
		{
			sql.append(one +" "+columnInfo.get(one).get("t")+"("+columnInfo.get(one).get("l")+"),");
		}
		sql.delete(sql.length()-1, sql.length());
		sql.append(" );");
		crs.setCommand(sql.toString());
		crs.execute();
	}
	
	/** 获取对类信息
	 */
	private static void getClassInfo(Class clz, Map<String, String> dbInfo,
			Map<String, String> pkInfo,
			Map<String, Map<String, Object>> columnInfo) throws Exception
	{
		//遍历所有字段包括私有的
		for(Field f:clz.getDeclaredFields()) 
		{
			//System.out.println(f.getName());
			//关键字信息
			if(f.isAnnotationPresent(Id.class))
			{
				Id id=f.getAnnotation(Id.class);
				pkInfo.put("t",jtd(f.getClass().getSimpleName().toString())+"(32)");
				pkInfo.put("c",id.column());
				pkInfo.put("d",id.describe());
				pkInfo.put("u",id.generator());
			}
			
			//获取字段信息
			Map<String,Object> tempOne=null;
			if(f.isAnnotationPresent(columns.class))
			{
				columns c=f.getAnnotation(columns.class);
				tempOne = new HashMap<String,Object>();
				tempOne.put("t", jtd(c.type()));
				tempOne.put("c", c.column());
				tempOne.put("d", c.describe());
				tempOne.put("l", c.length());
				columnInfo.put(f.getName().toString(), tempOne);
			}
		}
		createTable(clz,dbInfo,pkInfo,columnInfo); //如果表不存在那么就创建数据表
//		System.out.println("annotation信息获取结束。");
//		System.out.println(dbInfo);
//		System.out.println(pkInfo);
//		System.out.println(columnInfo);
	}
	
	/** javaTypeToDbType、java类型和数据库类型的转换
	 * @param type String
	 * @return VARCHAR
	 */
	public static String jtd(String type)
	{
		if("String".equals(type))	return  "varchar";
		if("int".equals(type))	return  "int";
		//其他的自己扩展吧
		return  "varchar";
	}
}



