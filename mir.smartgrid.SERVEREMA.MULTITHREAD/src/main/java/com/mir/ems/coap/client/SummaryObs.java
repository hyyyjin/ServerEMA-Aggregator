package com.mir.ems.coap.client;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import com.mir.ems.globalVar.global;

public class SummaryObs extends Thread {

	String emaID = "";
	boolean flag = false;

	public SummaryObs(String emaID) {
		// TODO Auto-generated constructor stub
		setEmaID(emaID);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		String uri = "coap://" + global.coapServerIP + ":" + global.coapServerPort + "/EMAP/"+global.ParentnNodeID+"/1.0b/Summary/" + getEmaID();

		CoapClient observeClient = new CoapClient();
		observeClient.setURI(uri);
		observeClient.observe(new CoapHandler() {

			@Override
			public void onLoad(CoapResponse response) {

				String content = response.getResponseText();
				// System.out.println("Summary" + content);
				if (content.contains("SummaryReport")) {
					flag = true;
					if (flag) {
						sendACK();
					}
				}
			}

			@Override
			public void onError() {
				System.err.println("OBSERVING FAILED (press enter to exit)");
			}

		});

	}

	void sendACK() {

		
		flag = false;
		
		String uri = "coap://" + global.coapServerIP + ":" + global.coapServerPort + "/EMAP/"+global.ParentnNodeID+"/1.0b/SummaryACK";

		com.mir.ems.profile.emap.v2.SummaryReported sr = new com.mir.ems.profile.emap.v2.SummaryReported();

		sr.setDestEMA(com.mir.ems.globalVar.global.ParentnNodeID);
		sr.setReportID("reportID");
		sr.setService("SummaryReported");
		sr.setSrcEMA(getEmaID());

		CoapClient client = new CoapClient();

		client.useNONs();
		client.setURI(uri);
		client.put(new CoapHandler() {
			@Override
			public void onError() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLoad(CoapResponse arg0) {
				// TODO Auto-generated method stub

			}
		}, sr.toString(), MediaTypeRegistry.APPLICATION_JSON);


	}

	public String getEmaID() {
		return emaID;
	}

	public void setEmaID(String emaID) {
		this.emaID = emaID;
	}

}