package com.mir.ems.profile.openadr.recent;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class Event {

	private String eventID, eventSignals, modificationReason, marketContext, createdDateTime, eventStatus, vtnComment,
			properties, components, specificDestEMA, dtStart, duration, tolerance, notification, rampUp, recovery;
	private int modificationNumber, priority;
	private boolean testEvent;

	ArrayList<String> eventParam = new ArrayList<String>();

	public Event addEventParams(String eventID, String eventSignals, int modificationNumber, String modificationReason,
			int priority, String marketContext, String createdDateTime, String eventStatus, boolean testEvent,
			String vtnComment, String properties, String components, String specificDestEMA, String dtStart,
			String duration, String tolerance, String notification, String rampUp, String recovery) {

		setComponents(components);
		setCreatedDateTime(createdDateTime);
		setDtStart(dtStart);
		setDuration(duration);
		setEventID(eventID);
		setEventSignals(eventSignals);
		setEventStatus(eventStatus);
		setMarketContext(marketContext);
		setModificationNumber(modificationNumber);
		setModificationReason(modificationReason);
		setNotification(notification);
		setPriority(priority);
		setProperties(properties);
		setRampUp(rampUp);
		setRecovery(recovery);
		setSpecificDestEMA(specificDestEMA);
		setTestEvent(testEvent);
		setTolerance(tolerance);
		setVtnComment(vtnComment);

		this.eventParam.add(toString());

		return this;
	}


	public String getEventParams() {

		return this.eventParam.toString();
	}	
	@Override
	public String toString() {
		
		
		
		return "{\"eventSignals" + "\":" +  getEventSignals() + ", "
				+ "\"eventID" + "\":" + "\"" + getEventID() + "\"" + ", "
				+"\"modificationNumber" + "\":" + "\"" + getModificationNumber() + "\"" + ", "
				+"\"modificationReason" + "\":" + "\"" + getModificationReason() + "\"" + ", "
				+"\"priority" + "\":" + "\"" + getPriority() + "\"" + ", "
				+"\"eiMarketContext" + "\":" + "\"" + getMarketContext() + "\"" + ", "
				+"\"createdDateTime" + "\":" + "\"" + getCreatedDateTime() + "\"" + ", "
				+"\"eventStatus" + "\":" + "\"" + getEventStatus() + "\"" + ", "
				+"\"testEvent" + "\":" + "\"" + isTestEvent() + "\"" + ", "
				+"\"vtnComment" + "\":" + "\"" + getVtnComment() + "\"" + ", "
				+"\"properties" + "\":" + "\"" + getProperties() + "\"" + ", "
				+"\"components" + "\":" + "\"" + getComponents() + "\"" + ", "
				+"\"venID" + "\":" + "\"" + getSpecificDestEMA() + "\"" + ", "
				+"\"dtStart" + "\":" + "\"" + getDtStart() + "\"" + ", "
				+"\"duration" + "\":" + "\"" + getDuration() + "\"" + ", "
				+"\"tolerance" + "\":" + "\"" + getTolerance() + "\"" + ", "
				+"\"notification" + "\":" + "\"" + getNotification() + "\"" + ", "
				+"\"rampUp" + "\":" + "\"" + getRampUp() + "\"" + ", "
				+"\"recovery" + "\":" + "\"" + getRecovery() + "\"" + "}";
		
	}

	public int getModificationNumber() {
		return modificationNumber;
	}

	public void setModificationNumber(int modificationNumber) {
		this.modificationNumber = modificationNumber;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean isTestEvent() {
		return testEvent;
	}

	public void setTestEvent(boolean testEvent) {
		this.testEvent = testEvent;
	}

	public String getEventID() {
		return eventID;
	}

	public void setEventID(String eventID) {
		this.eventID = eventID;
	}

	public String getEventSignals() {
		return eventSignals;
	}

	public void setEventSignals(String eventSignals) {
		this.eventSignals = eventSignals;
	}

	public String getModificationReason() {
		return modificationReason;
	}

	public void setModificationReason(String modificationReason) {
		this.modificationReason = modificationReason;
	}

	public String getMarketContext() {
		return marketContext;
	}

	public void setMarketContext(String marketContext) {
		this.marketContext = marketContext;
	}

	public String getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(String createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public String getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}

	public String getVtnComment() {
		return vtnComment;
	}

	public void setVtnComment(String vtnComment) {
		this.vtnComment = vtnComment;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public String getComponents() {
		return components;
	}

	public void setComponents(String components) {
		this.components = components;
	}

	public String getSpecificDestEMA() {
		return specificDestEMA;
	}

	public void setSpecificDestEMA(String specificDestEMA) {
		this.specificDestEMA = specificDestEMA;
	}

	public String getDtStart() {
		return dtStart;
	}

	public void setDtStart(String dtStart) {
		this.dtStart = dtStart;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getTolerance() {
		return tolerance;
	}

	public void setTolerance(String tolerance) {
		this.tolerance = tolerance;
	}

	public String getNotification() {
		return notification;
	}

	public void setNotification(String notification) {
		this.notification = notification;
	}

	public String getRampUp() {
		return rampUp;
	}

	public void setRampUp(String rampUp) {
		this.rampUp = rampUp;
	}

	public String getRecovery() {
		return recovery;
	}

	public void setRecovery(String recovery) {
		this.recovery = recovery;
	}

}
