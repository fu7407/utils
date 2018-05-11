package com.zzf.common.timer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

public class MyTask extends TimerTask {

	public void run() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		System.out.println("当前时间为："+sdf.format(new Date()));
	}

}
