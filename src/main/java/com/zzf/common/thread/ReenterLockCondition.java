package com.zzf.common.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 重入锁及其好搭档Condition条件
 * 可参考ArrayBlockingQueue类，该类有使用到
 * @author zhangzengfu
 *
 */
public class ReenterLockCondition implements Runnable {

	public static ReentrantLock lock = new ReentrantLock();
	public static Condition condition = lock.newCondition();

	@Override
	public void run() {
		try {
			lock.lock();
			condition.await();// 使当前线程等待，同时释放当前锁，当其他线程调用signal()或signalAll()方法时，线程重新获得锁并继续执行
			System.out.println("Thread is going on");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		ReenterLockCondition tl = new ReenterLockCondition();
		Thread t1 = new Thread(tl);
		t1.start();
		Thread.sleep(2000);
		lock.lock();
		condition.signal();// 唤醒一个等待中的线程
		lock.unlock();
	}

}
