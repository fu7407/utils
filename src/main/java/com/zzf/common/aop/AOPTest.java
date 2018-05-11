package com.zzf.common.aop;

public class AOPTest {

	/**
	 * @param args
	 * @throws Throwable 
	 */
	public static void main(String[] args) {
		try {
			IPerson obj = (IPerson)AOPFactory.getAOPProxyedObject(PersonImpl.class.newInstance());
			System.out.println(obj.getName("李四"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
