package com.mir.ems.coap;

import org.eclipse.californium.core.CoapResource;

public class CoAPSummaryObserver extends CoapResource{
	
	private final int MAX_OBS_NUM = 20;

	public CoAPSummaryObserver(String name) {
		// TODO Auto-generated constructor stub
		super(name);
		
		for (int i = 1; i <= MAX_OBS_NUM; i++) {

			add(new SummaryObserverSubPath("CLIENT_EMA" + i, name));
			add(new SummaryObserverSubPath("CHILD_EMA" + i, name));
			add(new SummaryObserverSubPath("DEVICE" + i, name));
			

		}
	}
}
