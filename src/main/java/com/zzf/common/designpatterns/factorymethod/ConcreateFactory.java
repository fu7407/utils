package com.zzf.common.designpatterns.factorymethod;

public class ConcreateFactory implements Factory {

	/**
	 * 如果实现类多个，可以传入参数，用if-else来判断产生的类
	 */
	@Override
	public Product createProduct() {
		return new ConcreateProduct();
	}

}
