package com.zzf.common.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
 * -Xms20m -Xmx20m ##设置堆大小20m，并将最小和最大值设置相等，避免扩展
 * -XX:+HeapDumpOnOutOfMemoryError ##dump出当前的内存堆转储快照
 * -XX:HeapDumpPath=E:\job   ##指定路径(转储文件还是挺大的)
 * -XX:SurvivorRatio=8    ## 存活比2:8
 * @author zhangzengfu
 *  Java heap space
 */
public class HeapOOM {

	static class OOMObject {

	}

	public static void main(String[] args) {
		List<OOMObject> list = new ArrayList<OOMObject>();
		while (true) {
			list.add(new OOMObject());
		}
	}

}
