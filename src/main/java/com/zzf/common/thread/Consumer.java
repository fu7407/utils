package com.zzf.common.thread;

public class Consumer implements Runnable {
	private Info info = null;
	
	public Consumer(Info info){
		this.info = info;
	}
	
	public void run() {
		for(int i=0;i<50;i++){
			try{
				Thread.sleep(100);//�����ӳ�
			}catch(Exception e){
				e.printStackTrace();
			}
			this.info.get();
		}
	}

}
