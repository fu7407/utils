package com.zzf.common.cache.test;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zzf.common.cache.Cache;
import com.zzf.common.cache.CacheManager;


/**
 * 投票缓存管理
 * 
 * @author wnick
 * 
 */
public class VoteCacheManager extends CacheManager<Vote> {

	private static final Log logger = LogFactory.getLog(VoteCacheManager.class);

	@Override
	public void startTaskHandle() {
		saveVisitCount();
	}

	private VoteCacheManager() {
		this.init();
	}

	private static final class SurveyCacheManagerHolder {
		public static VoteCacheManager INSTANCE = new VoteCacheManager();
	}
	
	/**
	 * 内部类的单例
	 * @return
	 */
	public static VoteCacheManager getInstance() {
		return SurveyCacheManagerHolder.INSTANCE;
	}

	/**
	 * 获取被缓存的对象; 如果当前没有此对象缓存,则创建缓存
	 * 
	 * @param formId
	 * @return
	 */
	@Override
	public Cache<Vote> getCache(String id) {
		if (!hasCache(id)) {
			Cache<Vote> cache = new Cache<Vote>();
			
			//Vote v = xxxService.getxxx(Long.valueOf(formId));
			// 从数据库中取
			Vote v = new Vote();
			v.setVoteTitle("投票标题");
			v.setVoteId(1);
			v.setVisitCount(10);
			
			cache.setCache(v);
			putCache(Long.toString(v.getVoteId()), cache);
			return cache;
		} else {
			return super.getCache(id);
		}
	}

	/**
	 * 移除缓存最后修改的对象
	 */
	@Override
	public void flushCache() {
		List<Cache<Vote>> list = getflushList();

		for (int i = 0; i < list.size(); i++) {
			Vote v  = list.get(i).getCache();
			System.out.println( "在移除缓存数据先保存 流量信息" + v.getVoteTitle() + ",保存到数据库中" );
			removeCache(v.getVoteId().toString());
		}

		logger.info("移除缓存最后修改的对象,数量:" + OVER_CAPACITY_REMOVE);
	}

	/**
	 * 更新缓存内容到数据库
	 * 
	 */
	public void saveVisitCount() {
		List<Cache<Vote>> caches = getAllCache();

		if (caches == null || caches.size() == 0) {
			logger.info("流量定时更行到数据库,这次更新的数量为(单位/个): 0   时间间隔为(单位/分钟):" + getRefreshPeriod());
			return;
		}

		logger.info("流量定时更行到数据库,这次更新的数量为(单位/个):" + caches.size()
				+ " 时间间隔为(单位/分钟):" + getRefreshPeriod());

		for (Cache<Vote> cache : caches) {
			System.out.println( cache.getCache().getVoteTitle() + " 访问了 "+ cache.getCache().getVisitCount() +" 次,保存到数据库中" );
		}			
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" 缓存容量 capacity: " + getCapacity());
		sb.append(" 超过容量删除缓存的个数 OVER_CAPACITY_REMOVE: " + OVER_CAPACITY_REMOVE);
		sb.append(" 定时刷新时间 refreshTime: " + getRefreshPeriod());
		return sb.toString();
	}

	/**
	 * 增加一次点击数
	 * 
	 * @param formId
	 */
	public void addVisitCount(String id) {
		Vote v = getCache(id).getCache();
		v.setVisitCount(v.getVisitCount()+1);
	}
	
	public static void main(String[] args) {
		VoteCacheManager cache = VoteCacheManager.getInstance();
		String id = "1";
		p("简单的模拟投票流程");
		p("访问投票id为:"+id + "的投票");
		Vote v = cache.getCache(id).getCache();
		p("访问前:访问次数：" + v.getVisitCount()+ ",投票名称:" + v.getVoteTitle());
		cache.addVisitCount(id);
		p("访问后:访问次数：" + v.getVisitCount() + ",投票名称:" + v.getVoteTitle());
		
		p("有其他人访问");
		cache.addVisitCount(id);
		p("有其他人访问");
		cache.addVisitCount(id);
		p("最后访问次数：" + v.getVisitCount() + ",投票名称:" + v.getVoteTitle());
		p("");
	}
	
	private static void p(Object o){
		System.out.println(o);
	}
}
