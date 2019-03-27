package com.mir.ems.coap;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

public class CoAPClient extends Thread{

	CoapResponse response;

	String addr, path;
	byte[] text;
	
	public CoAPClient(String addr, String path, byte[] text) {
		
		this.addr = addr;
		this.path = path;
		this.text = text;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

		CoapClient client = new CoapClient();

		String uri = "coap://" + addr + path;

		client.setURI(uri);

		if (path.contains("dimming") || path.contains("control")) {
			
			client.setURI(uri);
			response = client.put(text, MediaTypeRegistry.TEXT_PLAIN);
			System.out.println("PUT MESSAGE: " + text);
			System.out.println("Path: " + path);
			
			
		}
		super.run();
	}
	public CoAPClient(String addr, String path, String text) {
		CoapClient client = new CoapClient();
		String uri = "coap:/" + addr + path;

		client.setURI(uri);
		response = client.put(text, MediaTypeRegistry.TEXT_PLAIN);
	}

}
