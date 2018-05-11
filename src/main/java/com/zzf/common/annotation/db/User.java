package com.zzf.common.annotation.db;


/**
 * @author cxy
 */
@DbInfo(url="jdbc:mysql://localhost/dbtest",un="root",pw="root",tableName="t_test_user")
public class User
{
	@Id(column="id_",describe="唯一标识")
	private String id; 
	@columns(column="user_name_",describe="用户名",type="string")
	private String userName; 
	@columns(column="friend_num_",describe="好友数量",type="int",length=10)
	private int friendNum;
	
	public User(String id, String userName, int friendNum)
	{
		super();
		this.id = id;
		this.userName = userName;
		this.friendNum = friendNum;
	}
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public int getFriendNum()
	{
		return friendNum;
	}
	public void setFriendNum(int friendNum)
	{
		this.friendNum = friendNum;
	} 
}

