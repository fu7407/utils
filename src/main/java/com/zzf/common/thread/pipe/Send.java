package com.zzf.common.thread.pipe;

import java.io.IOException;
import java.io.PipedOutputStream;

public class Send implements Runnable {
	private PipedOutputStream pos = null;
	
	public Send() {
		this.pos = new PipedOutputStream();
	}

	public void run() {
		String str = "djfidofe";
		try {
			this.pos.write(str.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			this.pos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public PipedOutputStream getPos() {
		return pos;
	}

	public void setPos(PipedOutputStream pos) {
		this.pos = pos;
	}

}
