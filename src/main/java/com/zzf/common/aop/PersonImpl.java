package com.zzf.common.aop;

public class PersonImpl implements IPerson {

	public String getName(String name) {
		System.out.println("processing");
		return "this is name is " + name + "!";
	}

}
