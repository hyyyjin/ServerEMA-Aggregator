package com.mir.ems.profile.emap.v2;

public class SummaryReport {

	private String service, SrcEMA, DestEMA, requestID, summaryType, summary;

	
	@Override
	public String toString() {
		
		return "{\"summary" + "\":" +  getSummary() + ", "
				+ "\"SrcEMA" + "\":" + "\"" + getSrcEMA() + "\"" + ", "
				+"\"DestEMA" + "\":" + "\"" + getDestEMA() + "\"" + ", "
				+"\"reportID" + "\":" + "\"" + getRequestID() + "\"" + ", "
				+"\"service" + "\":" + "\"" + getSummaryType() + "\"" + ", "
				+"\"summaryType" + "\":" + "\"" + getService() + "\"" +"}";
		
	}
	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getSrcEMA() {
		return SrcEMA;
	}

	public void setSrcEMA(String srcEMA) {
		SrcEMA = srcEMA;
	}

	public String getDestEMA() {
		return DestEMA;
	}

	public void setDestEMA(String destEMA) {
		DestEMA = destEMA;
	}

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public String getSummaryType() {
		return summaryType;
	}

	public void setSummaryType(String summaryType) {
		this.summaryType = summaryType;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	public static void main(String[] args){
		
		
		Summary sm = new Summary();
		
		for(int i=0; i<10; i++){
			
			sm.addsummaryParam("CLIENT_EMA"+i, 100, 200, 300, 400, 500, 600);
				
		}
			
		SummaryReport sr = new SummaryReport();
		sr.setDestEMA("destEMA");
		sr.setRequestID("requestID");
		sr.setService("service");
		sr.setSrcEMA("srcEMA");
		sr.setSummary(sm.getEventParams());
		sr.setSummaryType("summaryType");
		
		System.out.println(sr.toString());
		
	}
}
