package com.zzf.common.thread.pipe;

import java.io.IOException;
import java.io.PipedInputStream;

public class Receive implements Runnable {
	private PipedInputStream pis = null;

	public Receive() {
		this.pis = new PipedInputStream();
	}

	public void run() {
		byte [] b = new byte[1024];
		int len=0;
		try {
			len = this.pis.read(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			this.pis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("接收的内容为：" + new String(b,0,len));
	}

	public PipedInputStream getPis() {
		return pis;
	}

	public void setPis(PipedInputStream pis) {
		this.pis = pis;
	}

}
