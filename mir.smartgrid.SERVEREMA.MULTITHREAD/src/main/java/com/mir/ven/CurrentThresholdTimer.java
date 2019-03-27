package com.mir.ven;

import com.mir.ems.globalVar.global;

public class CurrentThresholdTimer extends Thread {

	int timer = 1 * 1000;

	public CurrentThresholdTimer(int timer) {
		this.timer = timer * 1000;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();

		try {
			Thread.sleep(this.timer);
			global.currentVal = 400;
//			System.out.println((timer/1000)+"Sec after Threshold Value: "+ global.currentVal);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
