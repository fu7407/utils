package com.zzf.common.designpatterns.simplefactory;

/**
 * 简单工厂
 * @author zhangzengfu
 *
 */
public class OperationFactory {

	public static Operation createOperation(String operate) {
		Operation oper = null;
		switch (operate) {
		case "+":
			oper = new AddOperation();
			break;
		case "-":
			oper = new SubOperation();
			break;
		}
		return oper;
	}

}
