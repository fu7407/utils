package com.zzf.common.cache;

import java.util.Date;
/**
 * 缓存对象，并记录时间戳
 * @author wnick
 *
 * @param <T>
 */
public class Cache<T> {
	private T cache;
	// 最后更新时间
	private Date timestamp = new Date();

	public T getCache() {
		return cache;
	}

	public void setCache(T cache) {
		this.cache = cache;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
