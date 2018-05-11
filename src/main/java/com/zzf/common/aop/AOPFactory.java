package com.zzf.common.aop;

public class AOPFactory {

	/**
	 * 根据类名创建类实例
	 * 
	 * @param clzName
	 * @return
	 */
	public static Object getClassInstance(String clzName) {
		Class cls;
		try {
			cls = Class.forName(clzName);
			return (Object) cls.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据传入的类名返回代理对象
	 * 
	 * @param clzName
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static Object getAOPProxyedObject(Object obj){
		return new AOPHandler().bind(obj);
	}

	/**
	 * 根据传入的类名返回代理对象
	 * 
	 * @param clzName
	 * @return
	 */
	public static Object getAOPProxyedObject(String clzName) {
		AOPHandler txHandler = new AOPHandler();
		Object obj = getClassInstance(clzName);
		return txHandler.bind(obj);
	}

}
