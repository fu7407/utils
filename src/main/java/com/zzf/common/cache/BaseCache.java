package com.zzf.common.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BaseCache<T> {

	private Map<String, T> forms = null;

	public BaseCache(int capacity) {
		forms = new ConcurrentHashMap<String, T>(capacity);
	}

	public Map<String, T> getMap() {
		return forms;
	}

	public int size() {
		return forms.size();
	}

	// 添加被缓存的对象;
	public void put(String key, T value) {
		forms.put(key, value);
	}

	// 删除被缓存的对象;
	public void remove(String key) {
		forms.remove(key);
	}
	
	// 删除所有被缓存的对象;
	public void removeAll() {
		forms.clear();
	}

	// 获取被缓存的对象;
	public T get(String key) {
		return (T) forms.get(key);
	}
	
	// 是否有缓存
	public boolean hasCache(String key){
		return forms.containsKey(key);
	}
}
