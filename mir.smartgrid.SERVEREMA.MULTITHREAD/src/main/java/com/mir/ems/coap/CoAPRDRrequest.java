package com.mir.ems.coap;

import java.text.SimpleDateFormat;
import java.util.concurrent.Callable;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import com.mir.ems.globalVar.global;

public class CoAPRDRrequest implements Callable<Boolean> {

	private String optID;
	static CoapClient client = new CoapClient();
	CoapResponse resp;

	String pathSet = "coap://" + global.coapServerIP + ":" + global.coapServerPort + "/EMAP/"
			+ global.getParentnNodeID() + "/" + global.version + "/";
	

	public CoAPRDRrequest(String optID) {
		setOptID(optID);
	}

	@Override
	public Boolean call() {

		try {

			com.mir.ems.profile.emap.v2.Available available = new com.mir.ems.profile.emap.v2.Available ();
			available.addEventParams(getCurrentTime(System.currentTimeMillis()), "PT10S", 90.0, 20181127,
					165520, 20181127, 165520);

			com.mir.ems.profile.emap.v2.CreateOpt createOpt = new com.mir.ems.profile.emap.v2.CreateOpt();
			createOpt.setAvailable(available.getEventParams());
			createOpt.setCreatedDateTime(getCurrentTime(System.currentTimeMillis()));
			createOpt.setDestEMA(global.getParentnNodeID());
			createOpt.setMarketContext("marketContext");
			createOpt.setOptID(getOptID());
			createOpt.setOptReason("Emergency");
			createOpt.setOptType("RdrRequest");
			createOpt.setRequestID("requestID");
			createOpt.setService("CreateOpt");
			createOpt.setSrcEMA(global.CHILD_ID);

			String uri = pathSet + "Opt";

			client.setURI(uri);
			resp = client.put(createOpt.toString(), MediaTypeRegistry.APPLICATION_JSON);
			
			if(resp.getResponseText().toString().contains("CreatedOpt")) return true;
			else return false;
			

		} catch (Exception e) {

			e.printStackTrace();
			return false;

		}
	}


	public String getOptID() {
		return optID;
	}

	public void setOptID(String optID) {
		this.optID = optID;
	}
	public String getCurrentTime(long currentTime) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime);
	}
}
