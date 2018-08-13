package com.zzf.common.designpatterns.templatemethod;

public class PassengerByTrain extends HappyPeople {

	@Override
	protected void travel() {
		System.out.println("Travelling by Train");
	}

}
