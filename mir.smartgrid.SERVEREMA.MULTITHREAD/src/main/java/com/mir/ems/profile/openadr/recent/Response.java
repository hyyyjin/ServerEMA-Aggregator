package com.mir.ems.profile.openadr.recent;

import org.json.JSONException;
import org.json.JSONObject;

public class Response {

	private String destEMA, responseDescription, requestID, service;
	private int responseCode;

	@Override
	public String toString() {
		JSONObject json = new JSONObject();

		try {
//			json.put("SrcEMA", getSrcEMA());
			json.put("venID", getDestEMA());
			json.put("responseCode", getResponseCode());
			json.put("responseDescription", getResponseDescription());
			json.put("requestID", getRequestID());
			json.put("service", getService());
//			json.put("time", getTime());

			return json.toString();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return "wrong";
		}
	}

	public String getDestEMA() {
		return destEMA;
	}

	public void setDestEMA(String destEMA) {
		this.destEMA = destEMA;
	}

	public String getResponseDescription() {
		return responseDescription;
	}

	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
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

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

}
