package com.zzf.common.designpatterns.builder;

public class TicketHelper {

	public void buildAdult(String info) {
		System.out.println("构造成年人票逻辑：" + info);
	}

	public void buildChildrenForSeat(String info) {
		System.out.println("构造有座儿童票逻辑：" + info);
	}

	public void buildChildrenNoSeat(String info) {
		System.out.println("构造无座儿童票逻辑：" + info);
	}

	public void buildElderly(String info) {
		System.out.println("构造老年人票逻辑：" + info);
	}

	public void buildSoldier(String info) {
		System.out.println("构造军人及家属票逻辑：" + info);
	}

}
