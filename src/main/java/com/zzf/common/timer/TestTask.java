package com.zzf.common.timer;

import java.util.Timer;

public class TestTask {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Timer t = new Timer();
		MyTask mytask = new MyTask();
		t.schedule(mytask, 1000, 2000);//设置任务，1秒后开始，每2秒重复一次
	}

}
