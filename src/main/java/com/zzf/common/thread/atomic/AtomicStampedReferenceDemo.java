package com.zzf.common.thread.atomic;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 有一家蛋糕店，为了挽留客户，决定为贵宾卡里小于20元的账户一次赠送20元，刺激客户充值和消费。但条件是每个客户只能赠送一次
 * 不能使用AtomicReference,必须用AtomicStampedReference
 * 此程序可以确保每个账户只充值一次20元
 * @author zhangzengfu
 *
 */
public class AtomicStampedReferenceDemo {
	static AtomicStampedReference<Integer> money = new AtomicStampedReference<Integer>(19, 0);

	public static void main(String[] args) {
		for (int i = 0; i < 3; i++) {// 模拟多个线程同时为用户充值
			final int timestamp = money.getStamp();// 这个是每个客户只充值一次的关键
			new Thread() {
				@Override
				public void run() {
					while (true) {
						while (true) {
							Integer m = money.getReference();
							if (m < 20) {// 充值
								if (money.compareAndSet(m, m + 20, timestamp, timestamp + 1)) {
									System.out.println("余额小于20，充值成功，余额：" + money.getReference() + "元");
									break;
								}
							} else {
								// System.out.println("余额大于20，无需充值");
								break;
							}
						}
					}
				}
			}.start();
		}

		new Thread() {// 消费线程，模拟消费行为
			@Override
			public void run() {
				for (int i = 0; i < 100; i++) {
					while (true) {
						int timestamp = money.getStamp();
						Integer m = money.getReference();
						if (m > 10) {
							System.out.println("大于10元");
							if (money.compareAndSet(m, m - 10, timestamp, timestamp + 1)) {
								System.out.println("成功消费10元，余额：" + money.getReference() + "元");
								break;
							}
						} else {
							System.out.println("没有足够的余额");
							break;
						}
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {

					}
				}
			}
		}.start();

	}

}
