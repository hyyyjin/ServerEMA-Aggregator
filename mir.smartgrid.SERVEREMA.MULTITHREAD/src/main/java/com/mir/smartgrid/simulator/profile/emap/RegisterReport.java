package com.mir.smartgrid.simulator.profile.emap;

public class RegisterReport {
	private String srcEMA;
	private String destEMA;
	
	private int requestID;
	private String reportName;
	private String reportType;
	private DRInformation emaRegisteredDRInformation;
	private MgnInformation emaRegisteredMgnInformation;
	
	private String service;
	private String time;
	
	public RegisterReport() {

	}

	public RegisterReport(String srcEMA, String destEMA, int requestID, String reportName, String reportType,
			DRInformation emaRegisteredDRInformation, MgnInformation emaRegisteredMgnInformation, String service,
			String time) {
		this.srcEMA = srcEMA;
		this.destEMA = destEMA;
		this.requestID = requestID;
		this.reportName = reportName;
		this.reportType = reportType;
		this.emaRegisteredDRInformation = emaRegisteredDRInformation;
		this.emaRegisteredMgnInformation = emaRegisteredMgnInformation;
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

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public DRInformation getEmaRegisteredDRInformation() {
		return emaRegisteredDRInformation;
	}

	public void setEmaRegisteredDRInformation(DRInformation emaRegisteredDRInformation) {
		this.emaRegisteredDRInformation = emaRegisteredDRInformation;
	}

	public MgnInformation getEmaRegisteredMgnInformation() {
		return emaRegisteredMgnInformation;
	}

	public void setEmaRegisteredMgnInformation(MgnInformation emaRegisteredMgnInformation) {
		this.emaRegisteredMgnInformation = emaRegisteredMgnInformation;
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
				+ "\"reportName\":\"" + reportName + "\","
				+ "\"reportType\":\"" + reportType + "\","
				+ "\"EMAregisteredDRInformation\":" + emaRegisteredDRInformation + ","
				+ "\"EMAregisteredMgnInformation\":" + emaRegisteredMgnInformation + ","
				
				+ "\"service\":\"" + service + "\","
				+ "\"time\":\"" + time + "\""
				+ "}";
	}
	
	
}
