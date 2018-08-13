package com.zzf.common.designpatterns.strategy;

/**
 * 策略模式
 * @author zhangzengfu
 *
 */
public class Main {
	public static void main(String[] args) {
		Context context = new Context(new StrategyA());
		context.contextInterface();
	}
}
