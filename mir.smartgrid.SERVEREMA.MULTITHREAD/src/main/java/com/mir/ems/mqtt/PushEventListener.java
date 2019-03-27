package com.mir.ems.mqtt;

public interface PushEventListener {

	//When c-ema status disconnected
	void eventNotification(String emaID, int type, int strday, int strtime, int endday, int endtime, double value);
	
	
}
