package com.zzf.common.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 实现一个容器，提供两个方法 add size
 * 写两个线程，线程1添加10个元素到容器，线程2实现监控元素的个数，当监控到5个时，线程2给出提示并退出
 * 给lists添加volatile之后，t2能够接到通知，但是t2线程的死循环很浪费cpu，如果不死循环该怎么做呢？
 * 这里使用wait和notify可以做到，wait会释放锁，notify不会释放锁
 * 需要注意的是运用这种方法，必须保证t2先执行，也就是首先让t2先监控才行
 * 使用latch（门闩）替代wait、notify来进行通知，好处是通信方式简单，同时也可以指定等待时间
 * 使用await和countdown代替wait和notify
 * countdownlatch不涉及锁定，当count的值为0时当前线程继续运行
 * 当不涉及同步，只涉及线程通信时，用synchornized+wait/notify就显得太重了
 * 这时应该考虑countdownlatch、cyclicbarrier、semaphore
 * @author zhangzengfu
 *
 */
public class MyContainer {
	// 添加volatile，使t2能够得到通知
	volatile List lists = new ArrayList();

	public void add(Object o) {
		lists.add(o);
	}

	public int size() {
		return lists.size();
	}

	public static void main(String[] args) {
		MyContainer c = new MyContainer();

		CountDownLatch latch = new CountDownLatch(1);
		new Thread(() -> {
			System.out.println("t2 start");
			if (c.size() != 5) {
				try {
					latch.await();
					// latch.await(5000, TimeUnit.MILLISECONDS);//也可以指定等待时间
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println("t2 end");
		}, "t2").start();

		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		new Thread(() -> {
			System.out.println("t1 start");
			for (int i = 0; i < 10; i++) {
				c.add(new Object());
				System.out.println("t1 add " + i);
				if (c.size() == 5) {
					latch.countDown();
				}
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("t1 end");
		}, "t1").start();
	}

}
