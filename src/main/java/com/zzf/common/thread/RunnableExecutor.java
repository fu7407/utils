package com.zzf.common.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunnableExecutor implements Runnable {

	@Override
	public void run() {
		for (int i = 0; i < 5; i++) {
			System.out.println(Thread.currentThread().getName() + ";random=" + (int) (Math.random() * 10 * 1000));
			try {
				Thread.sleep((int) (Math.random() * 10 * 1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		ExecutorService runnableService = Executors.newFixedThreadPool(3);
		Runnable r1 = new RunnableExecutor();
		runnableService.execute(r1);
		runnableService.execute(new RunnableExecutor());
		runnableService.execute(new RunnableExecutor());
		runnableService.execute(new RunnableExecutor());
		runnableService.shutdown();
		System.out.println("go on");
		System.out.println("end");
	}

}
