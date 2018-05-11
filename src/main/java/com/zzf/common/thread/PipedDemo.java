package com.zzf.common.thread;

import java.io.IOException;

public class PipedDemo {

	/**
	 * 验证管道流通信测试
	 * @param args
	 */
	public static void main(String[] args) {
		Send s = new Send();
		Receive r = new Receive();
		try {
			s.getPos().connect(r.getPis());//连接管道
		} catch (IOException e) {
			e.printStackTrace();
		}
		new Thread(s).start();
		new Thread(r).start();
	}

}
