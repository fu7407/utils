package com.zzf.common.designpatterns.templatemethod;

public abstract class HappyPeople {
	public void celebrateSpringFestival() {
		subscribeTicket();
		travel();
		celebrate();
	}

	protected final void subscribeTicket() {
		// ...
	}

	protected abstract void travel();

	protected final void celebrate() {
		// ...
	}

}
