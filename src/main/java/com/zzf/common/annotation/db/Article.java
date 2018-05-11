package com.zzf.common.annotation.db;


/** 文章类 用于测试apt的通用性
 * @author cxy
 */
@DbInfo(url="jdbc:mysql://localhost/dbtest",un="root",pw="root",tableName="t_test_article")
public class Article
{
	@Id(column="sid",describe="文章唯一标识")
	private String sid=""; //文章id
	@columns(column="title_",describe="标题",type="string")
	private String title="";
	@columns(column="content_",describe="内容",type="string",length=2000)
	private String content="";
	@columns(column="click_num_",describe="点击量",type="int")
	private int clickNum =0;
	
	public Article(String sid, String title, String content, int clickNum)
	{
		super();
		this.sid = sid;
		this.title = title;
		this.content = content;
		this.clickNum = clickNum;
	}
	
	public String getSid()
	{
		return sid;
	}
	public void setSid(String sid)
	{
		this.sid = sid;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public int getClickNum()
	{
		return clickNum;
	}
	public void setClickNum(int clickNum)
	{
		this.clickNum = clickNum;
	}
}

