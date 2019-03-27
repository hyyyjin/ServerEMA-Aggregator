package com.mir.smartgrid.simulator.profile.emap;

import java.util.Arrays;

public class DistributeEvent {
	private String srcEMA;
	private String destEMA;
	
	private int requestID;
	private int responseCode;
	private String responseDescription;
	private String type;
	private PriceInformation[] emaDRPriceInformation;
	private EventInformation[] emaDREventInformation;
	
	private String service;
	private String time;
	
	public DistributeEvent() {
		this.srcEMA = null;
		this.destEMA = null;
		this.requestID = 0;
		this.responseCode = 0;
		this.responseDescription = null;
		this.type = null;
		this.emaDRPriceInformation = null;
		this.emaDREventInformation = null;
		this.service = null;
		this.time = null;
	}

	public DistributeEvent(String srcEMA, String destEMA, int requestID, int responseCode, String responseDescription,
			String type, PriceInformation[] emaDRPriceInformation, EventInformation[] emaDREventInformation,
			String service, String time) {
		this.srcEMA = srcEMA;
		this.destEMA = destEMA;
		this.requestID = requestID;
		this.responseCode = responseCode;
		this.responseDescription = responseDescription;
		this.type = type;
		this.emaDRPriceInformation = emaDRPriceInformation;
		this.emaDREventInformation = emaDREventInformation;
		this.service = service;
		this.time = time;
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

	public int getRequestID() {
		return requestID;
	}

	public void setRequestID(int requestID) {
		this.requestID = requestID;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseDescription() {
		return responseDescription;
	}

	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public PriceInformation[] getEmaDRPriceInformation() {
		return emaDRPriceInformation;
	}

	public void setEmaDRPriceInformation(PriceInformation[] emaDRPriceInformation) {
		this.emaDRPriceInformation = emaDRPriceInformation;
	}

	public EventInformation[] getEmaDREventInformation() {
		return emaDREventInformation;
	}

	public void setEmaDREventInformation(EventInformation[] emaDREventInformation) {
		this.emaDREventInformation = emaDREventInformation;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "{\"SrcEMA\":\"" + srcEMA + "\","
				+ "\"DestEMA\":\"" + destEMA + "\","
				
				+ "\"requestID\":" + requestID + ","
				+ "\"responseCode\":" + responseCode + ","
				+ "\"responseDescription\":\"" + responseDescription + "\","
				+ "\"type\":\"" + type + "\","
				+ "\"EMADRPriceInformation\":" + Arrays.toString(emaDRPriceInformation) + ","
				+ "\"EMADREventInformation\":" + Arrays.toString(emaDREventInformation) + ","
				
				+ "\"service\":\"" + service + "\","
				+ "\"time\":\"" + time + "\""
				+ "}";
	}
	
	

	
	
}
