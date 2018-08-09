package com.zzf.common.thread;

import java.util.concurrent.locks.StampedLock;

/**
 * 读写锁的改进版，StampedLock提供了乐观的读策略
 * @author zhangzengfu
 *
 */
public class StampedLockDemo {
	private double x, y;
	private final StampedLock s1 = new StampedLock();

	void move(double deltaX, double deltaY) {
		long stamp = s1.writeLock();
		try {
			x += deltaX;
			y += deltaY;
		} finally {
			s1.unlockWrite(stamp);
		}
	}

	double distanceFromOrigin() {
		long stamp = s1.tryOptimisticRead();
		double currentX = x, currentY = y;
		if (!s1.validate(stamp)) {// 判断这个stamp是否在读过程发生期间被改过，如果被改过，则说明出现了脏读，需要重新读取
			stamp = s1.readLock();
			try {
				currentX = x;
				currentY = y;
			} finally {
				s1.unlockRead(stamp);
			}
		}
		return Math.sqrt(currentX * currentX + currentY * currentY);
	}

}
