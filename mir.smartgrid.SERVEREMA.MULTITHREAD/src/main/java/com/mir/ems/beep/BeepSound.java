package com.mir.ems.beep;

import java.awt.Toolkit;

public class BeepSound extends Thread {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		Toolkit toolkit = Toolkit.getDefaultToolkit();

		for (int i = 0; i < 5; i++) {

			toolkit.beep();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}
}
