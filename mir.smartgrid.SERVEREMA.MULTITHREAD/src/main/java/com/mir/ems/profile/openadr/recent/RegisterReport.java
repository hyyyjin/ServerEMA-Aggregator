package com.mir.ems.profile.openadr.recent;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterReport {

	private String srcEMA, destEMA, service, requestID, report;

	
	
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

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	
	
	@Override
	public String toString() {

		JSONObject json = new JSONObject();

		try {

			if (report != null) {
				
				json.put("venID", getSrcEMA());
				json.put("service", getService());
//				json.put("time", getTime());
				json.put("requestID", getRequestID());
				json.put("oadrReport", getReport());
				
			} else {
				
				json.put("venID", getDestEMA());
				json.put("requestID", getRequestID());
				json.put("service", getService());
//				json.put("time", getTime());

			}
			return json.toString();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return "wrong";
		}

	}

}
