package com.zzf.common.jvm;

import java.lang.reflect.Field;

/**
 * 直接内存溢出（通过-XX:MaxDirectMemorySize指定，如不指定，则默认与java堆的最大值一样）
 * -Xmx20M -XX:MaxDirectMemorySize=10M
 * @author zhangzengfu
 * Exception in thread "main" java.lang.OutOfMemoryError
 */
public class DirectMemoryOOM {

	private static final int _1MB = 1024 * 1024;

	public static void main(String[] args) throws Exception {
		Field unsafeField = sun.misc.Unsafe.class.getDeclaredFields()[0];
		unsafeField.setAccessible(true);
		sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);
		while (true) {
			unsafe.allocateMemory(_1MB);
		}
	}
}
