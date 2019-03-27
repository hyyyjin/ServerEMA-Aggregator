package com.mir.ems.profile.emap.v2;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class Report {

	private String duration, reportDescription, reportRequestID, reportSpecifierID, reportName, createdDateTime;

	ArrayList<String> reportParam = new ArrayList<String>();

	@Override
	public String toString() {

	
		
		JSONObject json = new JSONObject();

		try {

			json.put("duration", getDuration());
			json.put("reportDescription", getReportDescription());
			json.put("reportRequestID", getReportRequestID());
			json.put("reportSpecifierID", getReportSpecifierID());
			json.put("reportName", getReportName());
			json.put("createdDateTime", getCreatedDateTime());
			
			return json.toString();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return "wrong";
		}

	}

	public Report addReportParams(String duration, String reportDescription, String reportRequestID,
			String reportSpecifierID, String reportName, String createdDateTime) {

		setCreatedDateTime(createdDateTime);
		setDuration(duration);
		setReportDescription(reportDescription);
		setReportName(reportName);
		setReportRequestID(reportRequestID);
		setReportSpecifierID(reportSpecifierID);

		this.reportParam.add(toString());

		return this;
	}

	public String getReportParams() {

		return this.reportParam.toString();
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getReportDescription() {
		return reportDescription;
	}

	public void setReportDescription(String reportDescription) {
		this.reportDescription = reportDescription;
	}

	public String getReportRequestID() {
		return reportRequestID;
	}

	public void setReportRequestID(String reportRequestID) {
		this.reportRequestID = reportRequestID;
	}

	public String getReportSpecifierID() {
		return reportSpecifierID;
	}

	public void setReportSpecifierID(String reportSpecifierID) {
		this.reportSpecifierID = reportSpecifierID;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(String createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

}
