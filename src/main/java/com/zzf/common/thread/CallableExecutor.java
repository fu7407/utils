package com.zzf.common.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 线程池中使用Callable
 * @author zhangzengfu
 *
 */
public class CallableExecutor implements Callable<Boolean> {

	int i;

	public CallableExecutor(int i) {
		this.i = i;
	}

	@Override
	public Boolean call() throws Exception {
		try {
			switch (i) {
			case 1:
				while (true) {
					System.out.println(Thread.currentThread().getName() + ";i=" + this.i);
					Thread.sleep(200);
				}
			default:
				Thread.sleep(500);
				System.out.println(Thread.currentThread().getName() + ";i=" + this.i);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 线程c1被阻塞的时候，由于没有占用CPU,是不能给自己的中断状态位置的，这样就会产生一个InterruptedException异常
	 * @param args
	 */
	public static void main(String[] args) {
		ExecutorService callableService = Executors.newFixedThreadPool(3);
		Future<Boolean> c1 = callableService.submit(new CallableExecutor(1));
		Future<Boolean> c2 = callableService.submit(new CallableExecutor(2));
		Future<Boolean> c3 = callableService.submit(new CallableExecutor(3));
		try {
			boolean b2 = c2.get();
			boolean b3 = c3.get();
			System.out.println("b2=" + b2);
			System.out.println("b3=" + b3);
		} catch (Exception e) {
			e.printStackTrace();
		}
		c1.cancel(true);// c1是死循环，现在退出
		callableService.shutdownNow();
	}
}
