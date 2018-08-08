package com.zzf.common.thread.producerAndConsumerBlockingQueue;

public class PCData {
	private final int intData;

	public PCData(int d) {
		this.intData = d;
	}

	public PCData(String d) {
		this.intData = Integer.valueOf(d);
	}

	public int getData() {
		return intData;
	}

	@Override
	public String toString() {
		return "data:" + intData;
	}

}
