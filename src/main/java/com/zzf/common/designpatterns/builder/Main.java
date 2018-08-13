package com.zzf.common.designpatterns.builder;

/**
 * 建造者模式
 * @author zhangzengfu
 *
 */
public class Main {
	public static void main(String[] args) {
		TicketHelper helper = new TicketHelper();
		helper.buildAdult("成人票");
		helper.buildChildrenForSeat("有座儿童票");
		helper.buildChildrenNoSeat("无座儿童票");
		helper.buildElderly("老人票");
		helper.buildSoldier("军人票");
		Object ticket = TicketBuilder.builder(helper);
	}
}
