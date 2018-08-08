package com.zzf.common.thread.future;

public class Client {
	public Data request(final String queryStr) {
		final FutureData future = new FutureData();
		new Thread() {
			public void run() {
				RealData realData = new RealData(queryStr);
				future.setRealData(realData);
			}
		}.start();
		return future;
	}

}
