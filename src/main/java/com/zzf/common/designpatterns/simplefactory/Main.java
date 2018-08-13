package com.zzf.common.designpatterns.simplefactory;

/**
 * 简单工厂
 * @author zhangzengfu
 *
 */
public class Main {
	public static void main(String[] args) {
		Operation oper = OperationFactory.createOperation("-");
		oper.setNumber1(1);
		oper.setNumber2(2);
		System.out.println(oper.getResult());
	}
}
