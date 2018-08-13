package com.zzf.common.designpatterns.factorymethod;

public class Client {
	private Factory factory;

	public Client(Factory factory) {
		this.factory = factory;
	}

	public void doSomeThing() {
		Product product = factory.createProduct();
	}

	public static void main(String[] args) {
		Client client = new Client(new ConcreateFactory());
		client.doSomeThing();
	}
}
