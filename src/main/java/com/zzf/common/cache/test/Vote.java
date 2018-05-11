package com.zzf.common.cache.test;

/**
 * 
 * @author wnick
 * 
 */
public class Vote {
	private Integer voteId;// 投票编号
	private String voteTitle;// 投票主题
	private int visitCount = 0;
	
	public Integer getVoteId() {
		return voteId;
	}
	public void setVoteId(Integer voteId) {
		this.voteId = voteId;
	}
	public String getVoteTitle() {
		return voteTitle;
	}
	public void setVoteTitle(String voteTitle) {
		this.voteTitle = voteTitle;
	}
	public int getVisitCount() {
		return visitCount;
	}
	public void setVisitCount(int visitCount) {
		this.visitCount = visitCount;
	}

}
