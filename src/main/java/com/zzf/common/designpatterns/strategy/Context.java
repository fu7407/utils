package com.zzf.common.designpatterns.strategy;

public class Context {
	private Strategy strategy;

	public Context(Strategy strategy) {
		this.strategy = strategy;
	}

	public void contextInterface() {
		this.strategy.algorithmInterface();
	}

}
