package com.zzf.common.designpatterns.observer;

/**
 * 观察者模式
 * @author zhangzengfu
 *
 */
public class Main {
	public static void main(String[] args) {
		ProductList observable = ProductList.getInstance();
		JingDongObserver jdObserver = new JingDongObserver();
		TaoBaoObserver tbObserver = new TaoBaoObserver();
		observable.addObserver(jdObserver);
		observable.addObserver(tbObserver);
		observable.addProudct("产品1");
	}
}
