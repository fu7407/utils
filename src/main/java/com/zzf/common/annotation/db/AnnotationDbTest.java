package com.zzf.common.annotation.db;


/** 
 * @author cxy
 */
public class AnnotationDbTest
{
	public static void main(String[] args) throws Exception
	{
		//创建对象 然后使用DbApDbAp.saveToDb(object); 将其保存到数据库
		//id不用给值，后面会自动生成
		User u1=new User("", "cxy", 1000); 
		User u2=new User("", "lyh", 100);
		DbAp.saveToDb(u1);
		DbAp.saveToDb(u2);
		DbAp.queryFromDb(User.class);
		System.out.println("========================");
		
		Article a1=new Article("", "标题", "内容", 100);
		Article a2=new Article("", "标题1", "内容1", 200);
		DbAp.saveToDb(a1);
		DbAp.saveToDb(a2);
		DbAp.queryFromDb(Article.class);
	}
}

