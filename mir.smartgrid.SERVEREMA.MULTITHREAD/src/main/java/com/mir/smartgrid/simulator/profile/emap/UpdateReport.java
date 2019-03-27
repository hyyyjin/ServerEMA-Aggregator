package com.mir.smartgrid.simulator.profile.emap;

public class UpdateReport {
	private String srcEMA;
	private String destEMA;
	
	private int requestID;
	private String reportName;
	private String reportType;
	private DRInformation emaUpdatedDRInfo;
	private MgnInformation emaUpdatedMgnInfo;
	
	private String service;
	private String time;
	private String topology= null;
	
	public UpdateReport() {

	}

	public UpdateReport(String srcEMA, String destEMA, int requestID, String reportName, String reportType,
			DRInformation emaUpdatedDRInfo, MgnInformation emaUpdatedMgnInfo, String service,
			String time) {
		this.srcEMA = srcEMA;
		this.destEMA = destEMA;
		this.requestID = requestID;
		this.reportName = reportName;
		this.reportType = reportType;
		this.emaUpdatedDRInfo = emaUpdatedDRInfo;
		this.emaUpdatedMgnInfo = emaUpdatedMgnInfo;
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


	public DRInformation getEmaUpdatedDRInfo() {
		return emaUpdatedDRInfo;
	}

	public void setEmaUpdatedDRInfo(DRInformation emaUpdatedDRInfo) {
		this.emaUpdatedDRInfo = emaUpdatedDRInfo;
	}

	public MgnInformation getEmaUpdatedMgnInfo() {
		return emaUpdatedMgnInfo;
	}

	public void setEmaUpdatedMgnInfo(MgnInformation emaUpdatedMgnInfo) {
		this.emaUpdatedMgnInfo = emaUpdatedMgnInfo;
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

	
	
	public String getTopology() {
		return topology;
	}

	public void setTopology(String topology) {
		this.topology = topology;
	}

	@Override
	public String toString() {
		return "{\"SrcEMA\":\"" + srcEMA + "\","
				+ "\"DestEMA\":\"" + destEMA + "\","
				
				+ "\"requestID\":" + requestID + ","
				+ "\"reportName\":\"" + reportName + "\","
				+ "\"reportType\":\"" + reportType + "\","
				+ "\"EMAupdatedDRInfo\":" + emaUpdatedDRInfo + ","
				+ "\"EMAupdatedMgnInfo\":" + emaUpdatedMgnInfo + ","
				
				+ "\"service\":\"" + service + "\","
				+ "\"time\":\"" + time + "\""
				+ "}";
	}
	
	
}
