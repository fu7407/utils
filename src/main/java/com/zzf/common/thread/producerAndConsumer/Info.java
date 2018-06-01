package com.zzf.common.thread.producerAndConsumer;

public class Info {
	private String name = "张三";
	private String content = "java 讲师";
	private boolean flag = false;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public synchronized void set(String name,String content){
		if(!flag){
			try{
				super.wait();//等待消费者取走
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		this.setName(name);
		try{
			Thread.sleep(90);//加入延迟
		}catch(Exception e){
			e.printStackTrace();
		}
		this.setContent(content);
		flag = false;//修改标志位，表示可以取走
		super.notify();//唤醒等待线程
	}
	
	public synchronized void get(){
		if(flag){
			try{
				super.wait();//等待生产者生产
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		try{
			Thread.sleep(300);//加入延迟
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println(this.getName() + "--->" + this.getContent());
		flag = true;//修改标志位，表示可以生产
		super.notify();//唤醒等待线程
	}
	
}
