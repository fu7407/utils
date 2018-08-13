package com.zzf.common.designpatterns.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ProductList extends Observable {

	private List<String> productList = null;

	private static ProductList instance;

	private ProductList() {

	}

	public static ProductList getInstance() {
		if (instance == null) {
			instance = new ProductList();
			instance.productList = new ArrayList<String>();
		}
		return instance;
	}

	/**
	 * 增加观察者
	 * @param observer
	 */
	public void addProductListObserver(Observer observer) {
		this.addObserver(observer);
	}

	/**
	 * 添加产品
	 * @param newProduct
	 */
	public void addProudct(String newProduct) {
		productList.add(newProduct);
		System.out.println("产品列表新增了产品 ：" + newProduct);
		this.setChanged();// 设置被观察对象发生变化
		this.notifyObservers(newProduct);// 通知观察者，并传递新产品
	}

}
