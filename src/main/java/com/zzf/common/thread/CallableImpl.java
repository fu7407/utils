package com.zzf.common.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class CallableImpl implements Callable<String> {

	private String acceptStr;

	public CallableImpl(String acceptStr) {
		this.acceptStr = acceptStr;
	}

	@Override
	public String call() throws Exception {
		Thread.sleep(1000);
		return this.acceptStr + " append some chars and return it!";
	}

	public static void main(String[] args) throws Exception {
		Callable<String> callable = new CallableImpl("my callable test !");
		FutureTask<String> task = new FutureTask<>(callable);
		long beginTime = System.currentTimeMillis();
		new Thread(task).start();
		String result = task.get();
		long endTime = System.currentTimeMillis();
		System.out.println("hello:" + result);
		System.out.println("cast:" + (endTime - beginTime) + " millis!");
	}

}
