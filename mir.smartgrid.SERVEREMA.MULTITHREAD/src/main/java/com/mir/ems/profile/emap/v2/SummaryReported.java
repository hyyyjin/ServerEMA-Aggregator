package com.mir.ems.profile.emap.v2;

import org.json.JSONException;
import org.json.JSONObject;

public class SummaryReported {

	private String service, SrcEMA, DestEMA, reportID;

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

	public String getReportID() {
		return reportID;
	}

	public void setReportID(String reportID) {
		this.reportID = reportID;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub

		JSONObject json = new JSONObject();

		try {

			json.put("service", getService());
			json.put("SrcEMA", getSrcEMA());
			json.put("DestEMA", getDestEMA());
			json.put("reportID", getReportID());

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json.toString();
	}

}
