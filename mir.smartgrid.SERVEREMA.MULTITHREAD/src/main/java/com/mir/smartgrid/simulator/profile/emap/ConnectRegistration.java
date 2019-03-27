package com.mir.smartgrid.simulator.profile.emap;

public class ConnectRegistration {
	// Mapping field
    private String srcEMA;
	private String destEMA;
	
	// OpenADR field
	private int requestID;
    private int version;
    
    //Expansion field
    private int customerPriority;
    private String qoS;
    private String service;
    private String type;
    private String time;

	public ConnectRegistration() {
		
	}

	public ConnectRegistration(String srcEMA, String destEMA, int requestID, int version, int customerPriority,
			String qoS, String service, String type, String time) {
		super();
		this.srcEMA = srcEMA;
		this.destEMA = destEMA;
		this.requestID = requestID;
		this.version = version;
		this.customerPriority = customerPriority;
		this.qoS = qoS;
		this.service = service;
		this.type = type;
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

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getCustomerPriority() {
		return customerPriority;
	}

	public void setCustomerPriority(int customerPriority) {
		this.customerPriority = customerPriority;
	}

	public String getQoS() {
		return qoS;
	}

	public void setQoS(String qoS) {
		this.qoS = qoS;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
				+ "\"version\":" + version + ","
				
				+ "\"customerPriority\":" + customerPriority + ","
				+ "\"QoS\":\"" + qoS + "\","
				+ "\"service\":\"" + service + "\","
				+ "\"type\":\"" + type + "\","
				+ "\"time\":\"" + time + "\""
				+ "}";
	}

	
	
}
