package com.zzf.common.designpatterns.simplefactory;

public class SubOperation extends Operation {

	@Override
	public double getResult() {
		double result = 0;
		result = this.getNumber1() - this.getNumber2();
		return result;
	}

}
