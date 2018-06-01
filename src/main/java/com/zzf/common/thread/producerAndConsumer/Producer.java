package com.zzf.common.thread.producerAndConsumer;

public class Producer implements Runnable {
	private Info info = null;
	
	public Producer(Info info){
		this.info = info;
	}

	public void run() {
		boolean flag = false;//定义标记位
		for(int i=0;i<50;i++){
			if(flag){//如果为true,则设置第一个信息
				this.info.set("张三","java 讲师");
				flag = false;//修改标记位
			}else{//如果为false,则设置第二个信息
				this.info.set("mldn","www.mldnjava.cn");
				flag = true;//修改标记位
			}
		}
	}

}
