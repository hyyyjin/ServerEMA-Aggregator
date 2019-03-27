package com.mir.ems.coap;

import org.eclipse.californium.core.CoapResource;

public class CoAPObserver extends CoapResource{
	
	private final int MAX_OBS_NUM = 20;

	public CoAPObserver(String name) {
		// TODO Auto-generated constructor stub
		super(name);
		
		for (int i = 1; i <= MAX_OBS_NUM; i++) {

			add(new CoAPObserverSubPath("CLIENT_EMA" + i, name));
			add(new CoAPObserverSubPath("CHILD_EMA" + i, name));
			add(new CoAPObserverSubPath("DEVICE" + i, name));
			
		}
	}
}
