package com.zzf.common.jvm;

/**
 * -Xss128k
 * @author zhangzengfu
 * stack length:996
 * Exception in thread "main" java.lang.StackOverflowError
 * 另外一种栈异常，创建无数个线程，这种情况不能测试会导致操作系统假死
 */
public class JavaVMStackSOF {
	private int stackLength = 1;

	public void stackLeak() {
		stackLength++;
		stackLeak();
	}

	public static void main(String[] args) {
		JavaVMStackSOF oom = new JavaVMStackSOF();
		try {
			oom.stackLeak();
		} catch (Throwable e) {
			System.out.println("stack length:" + oom.stackLength);
			throw e;
		}
	}
}
