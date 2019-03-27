package com.mir.ems.price;

import java.util.Timer;
import java.util.TimerTask;

import com.mir.ems.GUI.MainFrame;


public class RealTimePriceTimer {

	public RealTimePriceTimer() {

		Timer timer = new Timer();
		timer.schedule(new UpdateTask(), 0, 100);

	}

	private class UpdateTask extends TimerTask {
		public void run() {
			
			
			if (MainFrame.rdbtnmntmNewRadioItem_1.isSelected()) {
				
				
				
			}
			
		}
	}

}
