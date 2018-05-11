package com.zzf.common.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 缓存主体功能
 * 定义多少时间执行startTaskHandles()
 * @author wnick
 *
 * @param <T>
 */
public abstract class CacheManager<T> {
	
	private static final Log logger = LogFactory.getLog(CacheManager.class);
	
	public static final boolean START_THREAD = true;
	
	private BaseCache<Cache<T>> baseCache = new BaseCache<Cache<T>>(getCapacity());
	
	/*配置信息											*/
	
	// 缓存刷新时间 单位为分钟
	private int refreshPeriod;

	// 缓存容量
	private int capacity;

	// 达到容量后,移除最后访问缓存的个数
	public final int OVER_CAPACITY_REMOVE = 10;

	public int getRefreshPeriod() {
		return refreshPeriod == 0 ? 1 : refreshPeriod;
	}

	public void setRefreshPeriod(int refreshPeriod) {
		this.refreshPeriod = refreshPeriod;
	}

	public int getCapacity() {
		capacity = (capacity == 0 ? 50 : capacity);
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	/*配置信息完											*/
	
	public void init(){
		if (START_THREAD) {
			logger.info("启动 startTask() 方法!");
			this.startTask();
		} else {
			logger.info("当前不启动 startTask() 方法!START_THREAD = false");
		}
	}
	

	/**
	 * 固定时间更新访问数到数据库
	 */
	private void startTask() {
		new Timer().schedule(new TimerTask() {
			public void run() {
				logger.info("执行startTask()  ");
				try {
					startTaskHandle();
				} catch (Exception e) {
					e.printStackTrace();
					logger.info("流量定时更行到数据库异常:" + e.getMessage());
				}
			}
		}, 5, getRefreshPeriod() * 10 * 1000 );
	}

	/**
	 * 执行任务时内容
	 */
	public abstract void startTaskHandle();

	/**
	 * 移除缓存最后修改的对象
	 */
	public abstract void flushCache();
	
	public void putCache(String key, Cache<T> cache) {
		// 是否超过预定容量
		if (getBaseCache().size() > getCapacity()) {
			flushCache();
		}
		getBaseCache().put(key, cache);
	}

	/**
	 * 所有缓存数据
	 * 
	 * @return
	 */
	public List<Cache<T>> getAllCache() {
		Map<String, Cache<T>> map = getBaseCache().getMap();
		return mapToList(map);
	}
	
	private List<Cache<T>> mapToList(Map<String, Cache<T>> map) {
		if (map == null || map.size() == 0)
			return null;

		List<Cache<T>> list = new ArrayList<Cache<T>>(map.size());

		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			list.add(map.get(key));
		}
		return list;
	}

	/**
	 * 按最后修改时间排序 获得要被移除缓存的列表
	 * @return
	 */
	public List<Cache<T>> getflushList() {

		Map<String, Cache<T>> map = getBaseCache().getMap();
		List<Cache<T>> list = mapToList(map);

		Comparator<Cache<T>> comp = new Comparator<Cache<T>>() {
			public int compare(Cache<T> o1, Cache<T> o2) {
				if (o1.getTimestamp().compareTo(o2.getTimestamp()) > 0)
					return 1;
				else
					return 0;
			}
		};
		Collections.sort(list, comp);

		int fromIndex = (list.size() - OVER_CAPACITY_REMOVE);
		return list.subList(fromIndex < 0 ? 0 : fromIndex, list.size());
	}

	public void removeAll() {
		getBaseCache().removeAll();
	}

	/**
	 * 移除缓存
	 * 
	 * @param key
	 */
	public void removeCache(String key) {
		getBaseCache().remove(key);
	}

	/**
	 * 获取被缓存的对象;
	 * 
	 * @param
	 * @return
	 */
	public Cache<T> getCache(String key) {
		return getBaseCache().get(key);
	}

	/**
	 * 此对象是否被缓存;
	 * 
	 * @param key
	 * @return
	 */
	public boolean hasCache(String key) {
		return getBaseCache().hasCache(key);
	}

	private BaseCache<Cache<T>> getBaseCache() {
		return baseCache;
	}
}
