package com.mir.ems.coap.smartmeter;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.mir.ems.coap.HandleCoAPMessage;

public class ActiveEnergy extends CoapResource {
	String uri;
	String payloadText;
	public HandleCoAPMessage handleCoAPMessage;
	public ActiveEnergy(String name) {
		super(name);
	}

	public void handleGET(CoapExchange exchange){

	}
	
	public void handlePUT(CoapExchange exchange){
		System.out.println(exchange.getRequestText());
		uri = getName();
		payloadText= exchange.getRequestText();
		handleCoAPMessage = new HandleCoAPMessage(uri, exchange);
	}
	
	public void handleDELETE(CoapExchange exchange){

	}
	
	public void handlePOST(CoapExchange exchange){
		uri = getName();
		payloadText= exchange.getRequestText();
		handleCoAPMessage = new HandleCoAPMessage(uri, exchange);
	}
}
