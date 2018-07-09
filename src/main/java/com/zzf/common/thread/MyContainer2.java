package com.zzf.common.thread;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * 写一个固定容量同步容器，拥有put和get方法，以及getcount方法
 * 能够支持两个生产者线程以及10个消费者线程的阻塞调用
 * @author zhangzengfu
 *
 */
public class MyContainer2<T> {
	final private LinkedList<T> lists = new LinkedList<T>();
	final private int MAX = 10;
	private int count = 0;

	public synchronized void put(T t) {
		while (lists.size() == MAX) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		lists.add(t);
		++count;
		this.notifyAll();// 通知消费者进行消费
	}

	public synchronized T get() {
		T t = null;
		while (lists.size() == 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		t = lists.removeFirst();
		count--;
		this.notifyAll();
		return t;
	}

	public static void main(String[] args) {
		MyContainer2<String> c = new MyContainer2<String>();
		// 启动消费者线程
		for (int i = 0; i < 10; i++) {
			new Thread(() -> {
				for (int j = 0; j < 5; j++) {
					System.out.println(c.get());
				}
			}, "c" + i).start();
		}

		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// 启动生产者线程
		for (int i = 0; i < 2; i++) {
			new Thread(() -> {
				for (int j = 0; j < 25; j++) {
					c.put(Thread.currentThread().getName() + " " + j);
				}
			}, "p" + i).start();
		}
	}

}
