package com.zzf.common.designpatterns.templatemethod;

public class PassengerByAir extends HappyPeople {

	@Override
	protected void travel() {
		System.out.println("Travelling by Air");
	}

}
