package com.mir.ems.database.item;

import java.util.Date;

import org.json.JSONObject;

public class Emap_Cema_Profile {

	private String emaID, qOs, type, registrationID, transportName, transportAddress, reportName, reportType, state,
			profileName, requestID, version, eventID;
	private String time, maxTime, minTime, connect, protocol;
	private int customerPriority, reportOnly, httpPullModel, xmlSignature, emaCNT, priority, dimming;
	private double margin, minValue=Integer.MAX_VALUE, maxValue, avgValue, power, generate, storage;
	private boolean pullModel, tableChanged, realTimetableChanged;
	private JSONObject EMARegisteredInfo, EMARegisteredMgnInfo;

	public Emap_Cema_Profile() {

	}

	public Emap_Cema_Profile(String protocol, String emaID, String registrationID, String qos, String state, double power, int dimming,
			double margin, double generate, double storage, double maxValue, double minValue, double avgValue,
			String maxTime, String minTime, int priority, boolean pullModel, boolean tableChanged, boolean realTimetableChanged, String connect) {

		setProtocol(protocol);
		setRegistrationID(registrationID);
		setEmaID(emaID);
		setqOs(qos);
		setState(state);
		setPower(power);
		setDimming(dimming);
		setMargin(margin);
		setGenerate(generate);
		setStorage(storage);
		setMaxValue(maxValue);
		setMinValue(minValue);
		setAvgValue(avgValue);
		setMaxTime(maxTime);
		setMinTime(minTime);
		setPriority(priority);
		setPullModel(pullModel);
		setTableChanged(tableChanged);
		setRealTimetableChanged(realTimetableChanged);
		setConnect(connect);
	}

	public Emap_Cema_Profile(String emaID, String qOs, String registrationID, int emaCNT, int customerPriority,
			double margin, double minValue, double maxValue, double avgValue, double power, double generate,
			double storage, JSONObject EMARegisteredInfo, JSONObject EMARegisteredMgnInfo, String state) {

		this.setEmaID(emaID);
		this.setqOs(qOs);
		this.setRegistrationID(registrationID);
		this.setEmaCNT(emaCNT);
		this.setCustomerPriority(customerPriority);
		this.setMargin(margin);
		this.setMinValue(minValue);
		this.setMaxValue(maxValue);
		this.setAvgValue(avgValue);
		this.setPower(power);
		this.setGenerate(generate);
		this.setStorage(storage);
		this.setEMARegisteredInfo(EMARegisteredInfo);
		this.setEMARegisteredMgnInfo(EMARegisteredMgnInfo);
		setState(state);

	}

	
	
	public String getEventID() {
		return eventID;
	}

	public void setEventID(String eventID) {
		this.eventID = eventID;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getConnect() {
		return connect;
	}

	public void setConnect(String connect) {
		this.connect = connect;
	}

	public boolean isTableChanged() {
		return tableChanged;
	}

	public void setTableChanged(boolean tableChanged) {
		this.tableChanged = tableChanged;
	}

	public boolean isRealTimetableChanged() {
		return realTimetableChanged;
	}

	public void setRealTimetableChanged(boolean realTimetableChanged) {
		this.realTimetableChanged = realTimetableChanged;
	}

	public boolean isPullModel() {
		return pullModel;
	}

	public Emap_Cema_Profile setPullModel(boolean pullModel) {
		this.pullModel = pullModel;
		return this;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getDimming() {
		return dimming;
	}

	public void setDimming(int dimming) {
		this.dimming = dimming;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public Emap_Cema_Profile setMaxValue(double maxValue) {
		this.maxValue = maxValue;
		return this;

	}

	public JSONObject getEMARegisteredInfo() {
		return this.EMARegisteredInfo;
	}

	public Emap_Cema_Profile setEMARegisteredInfo(JSONObject eMARegisteredInfo) {
		this.EMARegisteredInfo = eMARegisteredInfo;
		return this;
	}

	public JSONObject getEMARegisteredMgnInfo() {
		return this.EMARegisteredMgnInfo;
	}

	public Emap_Cema_Profile setEMARegisteredMgnInfo(JSONObject eMARegisteredMgnInfo) {
		this.EMARegisteredMgnInfo = eMARegisteredMgnInfo;
		return this;

	}

	public String getEmaID() {
		return emaID;
	}

	public Emap_Cema_Profile setEmaID(String emaID) {
		this.emaID = emaID;
		return this;
	}

	public String getqOs() {
		return qOs;
	}

	public Emap_Cema_Profile setqOs(String qOs) {
		this.qOs = qOs;
		return this;
	}

	public String getType() {
		return type;
	}

	public Emap_Cema_Profile setType(String type) {
		this.type = type;
		return this;
	}

	public String getTransportName() {
		return transportName;
	}

	public Emap_Cema_Profile setTransportName(String transportName) {
		this.transportName = transportName;
		return this;
	}

	public String getTransportAddress() {
		return transportAddress;
	}

	public Emap_Cema_Profile setTransportAddress(String transportAddress) {
		this.transportAddress = transportAddress;
		return this;
	}

	public String getReportName() {
		return reportName;
	}

	public Emap_Cema_Profile setReportName(String reportName) {
		this.reportName = reportName;
		return this;
	}

	public String getReportType() {
		return reportType;
	}

	public Emap_Cema_Profile setReportType(String reportType) {
		this.reportType = reportType;
		return this;
	}

	public String getState() {
		return state;
	}

	public Emap_Cema_Profile setState(String state) {
		this.state = state;
		return this;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(String maxTime) {
		this.maxTime = maxTime;
	}

	public String getMinTime() {
		return minTime;
	}

	public void setMinTime(String minTime) {
		this.minTime = minTime;
	}

	public String getRequestID() {
		return requestID;
	}

	public Emap_Cema_Profile setRequestID(String requestID) {
		this.requestID = requestID;
		return this;
	}

	public String getVersion() {
		return version;
	}

	public Emap_Cema_Profile setVersion(String version) {
		this.version = version;
		return this;
	}

	public int getCustomerPriority() {
		return customerPriority;
	}

	public Emap_Cema_Profile setCustomerPriority(int customerPriority) {
		this.customerPriority = customerPriority;
		return this;
	}

	public int getReportOnly() {
		return reportOnly;
	}

	public Emap_Cema_Profile setReportOnly(int reportOnly) {
		this.reportOnly = reportOnly;
		return this;
	}

	public int getHttpPullModel() {
		return httpPullModel;
	}

	public Emap_Cema_Profile setHttpPullModel(int httpPullModel) {
		this.httpPullModel = httpPullModel;
		return this;
	}

	public String getProfileName() {
		return profileName;
	}

	public Emap_Cema_Profile setProfileName(String profileName) {
		this.profileName = profileName;
		return this;
	}

	public int getXmlSignature() {
		return xmlSignature;
	}

	public Emap_Cema_Profile setXmlSignature(int xmlSignature) {
		this.xmlSignature = xmlSignature;
		return this;
	}

	public String getRegistrationID() {
		return registrationID;
	}

	public Emap_Cema_Profile setRegistrationID(String registrationID) {
		this.registrationID = registrationID;
		return this;
	}

	public int getEmaCNT() {
		return emaCNT;
	}

	public Emap_Cema_Profile setEmaCNT(int emaCNT) {
		this.emaCNT = emaCNT;
		return this;
	}

	public double getMargin() {
		return margin;
	}

	public Emap_Cema_Profile setMargin(double margin) {
		this.margin = margin;
		return this;
	}

	public double getMinValue() {
		return minValue;
	}

	public Emap_Cema_Profile setMinValue(double minValue) {
		this.minValue = minValue;
		return this;
	}

	public double getAvgValue() {
		return avgValue;
	}

	public Emap_Cema_Profile setAvgValue(double avgValue) {
		this.avgValue = avgValue;
		return this;
	}

	public double getPower() {
		return power;
	}

	public Emap_Cema_Profile setPower(double power) {
		this.power = power;
		return this;
	}

	public double getGenerate() {
		return generate;
	}

	public Emap_Cema_Profile setGenerate(double generate) {
		this.generate = generate;
		return this;
	}

	public double getStorage() {
		return storage;
	}

	public Emap_Cema_Profile setStorage(double storage) {
		this.storage = storage;
		return this;
	}

}
