package com.mir.ems.profile.emap.v2;

import org.json.JSONException;
import org.json.JSONObject;

public class CreatePartyRegistration {
	private String srcEMA, destEMA, requestID, profileName, transportName, service, time;
	private boolean reportOnly, xmlSignature, httpPullModel;

	public CreatePartyRegistration() {
		
	}

	public CreatePartyRegistration(String srcEMA, String destEMA, String requestID, String profileName, String transportName, boolean reportOnly, boolean xmlSignature, boolean httpPullModel, String service, String time) {

		setDestEMA(destEMA);
		setHttpPullModel(httpPullModel);
		setProfileName(profileName);
		setReportOnly(reportOnly);
		setRequestID(requestID);
		setService(service);
		setSrcEMA(srcEMA);
		setTransportName(transportName);
		setXmlSignature(xmlSignature);
		setTime(time);
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
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

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getTransportName() {
		return transportName;
	}

	public void setTransportName(String transportName) {
		this.transportName = transportName;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public boolean isReportOnly() {
		return reportOnly;
	}

	public void setReportOnly(boolean reportOnly) {
		this.reportOnly = reportOnly;
	}

	public boolean isXmlSignature() {
		return xmlSignature;
	}

	public void setXmlSignature(boolean xmlSignature) {
		this.xmlSignature = xmlSignature;
	}

	public boolean isHttpPullModel() {
		return httpPullModel;
	}

	public void setHttpPullModel(boolean httpPullModel) {
		this.httpPullModel = httpPullModel;
	}

	@Override
	public String toString(){
		
		JSONObject json = new JSONObject();
		
		try {
			json.put("SrcEMA", getSrcEMA());
			json.put("DestEMA", getDestEMA());
			json.put("requestID", getRequestID());
			json.put("profileName", getProfileName());
			json.put("transportName", getTransportName());
			json.put("reportOnly", isReportOnly());
			json.put("xmlSignature", isXmlSignature());
			json.put("httpPullModel", isHttpPullModel());
			json.put("service", getService());
			json.put("time", getTime());
			
			return json.toString();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return "wrong";
		}

		
	}
	

}
