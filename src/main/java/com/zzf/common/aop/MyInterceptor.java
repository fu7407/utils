package com.zzf.common.aop;

public class MyInterceptor implements Interceptor {

	public void after() {
		System.out.println("post-processing");
	}

	public void before() {
		System.out.println("pre-processing");
	}

}
