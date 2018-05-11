package com.zzf.common.thread;

public class ThreadTest {

	/**
	 * 生产者和消费者问题测试（需解决以下两个问题）
	 * 1.假设生产者线程刚向数据存储空间添加了信息的名称，还没有加入该信息的内容，程序就切换到了消费者线程，
	 * 消费者线程将把信息的名称和上一信息的内容联系到一起
	 * 2.生产者放了若干次的数据，消费者才开始取数据，或者是消费者取完一个数据后，还没等到生产者放入新的数据，又重复取出已取过的数据
	 */
	public static void main(String[] args) {
		Info i = new Info();
		Producer pro = new Producer(i);
		Consumer con = new Consumer(i);
		new Thread(pro).start();
		new Thread(con).start();
	}

}
