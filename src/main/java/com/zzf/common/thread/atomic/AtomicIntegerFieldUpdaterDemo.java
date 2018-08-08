package com.zzf.common.thread.atomic;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * 最终stu.score和allScore绝对相等，说明了AtomicIntegerFieldUpdater很好的保证了Candidate.score的线程安全
 * 注意事项：
 * 1、Updater只能修改他可见范围内的变量，因为Updater使用反射得到这个变量，如果变量不可见就会出错。比如如果score声明为private就是不可行的。
 * 2、为了确保变量被正确的读取，它必须是volatile类型的。
 * 3、由于CAS操作会通过对象实例中的偏移量直接进行赋值，因此他不支持static字段（Unsafe.objectFieldOffset()不支持静态变量）
 * @author zhangzengfu
 *
 */
public class AtomicIntegerFieldUpdaterDemo {
	public static class Candidate {
		int id;
		volatile int score;
	}

	public final static AtomicIntegerFieldUpdater<Candidate> scoreUpdater = AtomicIntegerFieldUpdater
			.newUpdater(Candidate.class, "score");

	public static AtomicInteger allScore = new AtomicInteger(0);

	public static void main(String[] args) throws InterruptedException {
		final Candidate stu = new Candidate();
		Thread[] t = new Thread[10000];
		for (int i = 0; i < 10000; i++) {
			t[i] = new Thread() {
				public void run() {
					if (Math.random() > 0.4) {
						scoreUpdater.incrementAndGet(stu);
						allScore.incrementAndGet();
					}
				}
			};
			t[i].start();
		}

		for (int i = 0; i < 10000; i++) {
			t[i].join();
		}

		System.out.println("score=" + stu.score);
		System.out.println("allScore=" + allScore);

	}

}
