package com.mir.smartgrid.simulator.profile.emap;

public class UpdatedReport {
	private String srcEMA;
	private String destEMA;
	
	private int requestID;
	private int responseCode;
	private String responseDescription;
	
	private EnergyInformation emaEnergyInfo;
	private TopologyInformation emaTopologyInfo;
	private String service;
	private String time;
	
	public UpdatedReport() {
		
	}

	public UpdatedReport(String srcEMA, String destEMA, int requestID, int responseCode, String responseDescription,
			EnergyInformation emaEnergyInfo, TopologyInformation emaTopologyInfo, String service, String time) {
		this.srcEMA = srcEMA;
		this.destEMA = destEMA;
		this.requestID = requestID;
		this.responseCode = responseCode;
		this.responseDescription = responseDescription;
		this.emaEnergyInfo = emaEnergyInfo;
		this.emaTopologyInfo = emaTopologyInfo;
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

	public EnergyInformation getEmaEnergyInfo() {
		return emaEnergyInfo;
	}

	public void setEmaEnergyInfo(EnergyInformation emaEnergyInfo) {
		this.emaEnergyInfo = emaEnergyInfo;
	}

	public TopologyInformation getEmaTopologyInfo() {
		return emaTopologyInfo;
	}

	public void setEmaTopologyInfo(TopologyInformation emaTopologyInfo) {
		this.emaTopologyInfo = emaTopologyInfo;
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
				+ "\"EMAEnergyinfo\":" + emaEnergyInfo + ","
				+ "\"EMATopologyinfo\":" + emaTopologyInfo + ","
				
				+ "\"service\":\"" + service + "\","
				+ "\"time\":\"" + time + "\""
				+ "}";
	}

}
