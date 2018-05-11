package com.zzf.common.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class AOPHandler implements InvocationHandler {
	

	private List<MyInterceptor> interceptors = null;
	
	private Object originalObject;
	
	/**
	 * 返回动态代理实例
	 * @param obj
	 * @return
	 */
	public Object bind(Object obj){
		this.originalObject = obj;
		return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), this);
	}

	/**
	 * 在Invoke方法中，加载对应的Interceptor，并进行预处理（before）、后处理（after）过程
	 */
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object result = null;
		try{
			invokeInterceptorsBefore();
			result = method.invoke(originalObject, args);
			invokeInterceptorsAfter();
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 加载Interceptor
	 * @return
	 */
	private synchronized List<MyInterceptor> getInterceptors() {
		if( null == interceptors){
			interceptors = new ArrayList<MyInterceptor>();
			interceptors.add(new MyInterceptor());//读取配置，加载Interceptor实例
		}
		return interceptors;
	}
	
	/**
	 * 执行预处理方法
	 * @param invInfo
	 */
	private void invokeInterceptorsBefore(){
		List<MyInterceptor> interceptors = this.getInterceptors();
		for(int i=0;i<interceptors.size();i++){
			((Interceptor)interceptors.get(i)).before();
		}
	}
	
	/**
	 * 执行后处理方法
	 * @param invInfo
	 */
	private void invokeInterceptorsAfter(){
		List<MyInterceptor> interceptors = this.getInterceptors();
		for(int i=interceptors.size()-1;i>=0;i--){
			((Interceptor)interceptors.get(i)).after();
		}
	}

}
