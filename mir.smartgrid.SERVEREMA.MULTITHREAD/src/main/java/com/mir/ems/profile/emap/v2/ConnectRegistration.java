package com.mir.ems.profile.emap.v2;

import org.json.JSONException;
import org.json.JSONObject;

public class ConnectRegistration {

	private String srcEMA, destEMA, requestID, service, version, time;

	public ConnectRegistration() {

	}

	public ConnectRegistration(String srcEMA, String destEMA, String requestID, String service, String version,
			String time) {

		setDestEMA(destEMA);
		setRequestID(requestID);
		setService(service);
		setSrcEMA(srcEMA);
		setTime(time);
		setVersion(version);

	}

	public String getSrcEMA() {
		return srcEMA;
	}

	public void setSrcEMA(String srcEMA) {
		this.srcEMA = srcEMA;
	}

	public String getDestEMA() {
		return destEMA;
	}

	public void setDestEMA(String destEMA) {
		this.destEMA = destEMA;
	}

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString(){
		
		JSONObject json = new JSONObject();
		
		try {
			json.put("SrcEMA", getSrcEMA());
			json.put("DestEMA", getDestEMA());
			json.put("requestID", getRequestID());
			json.put("service", getService());
			json.put("version", getVersion());
			json.put("time", getTime());
			
			return json.toString();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return "wrong";
		}

		
	}
}
