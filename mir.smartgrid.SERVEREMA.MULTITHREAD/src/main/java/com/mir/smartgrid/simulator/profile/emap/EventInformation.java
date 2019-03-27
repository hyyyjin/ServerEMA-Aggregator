package com.mir.smartgrid.simulator.profile.emap;

public class EventInformation {
	private long id;

	private String profileName;
	private int modificationNumber;
	private boolean testEvent;
	private String marketContext;
	private String eventStatus;
	private int uid;

	private int duration;
	private int eventID;
	private String signalName;
	private String signalType;
	private int signalID;
	private int currentValue;
	private String startYMD;
	private String startTime;
	private String endYMD;
	private String endTime;

	private double threshold;
	private double capacity;
	private int priority;

	public EventInformation() {

	}

	public EventInformation(String profileName, int modificationNumber, boolean testEvent, String marketContext,
			String eventStatus, int uid, int duration, int eventID, String signalName, String signalType, int signalID,
			int currentValue, String startYMD, String startTime, String endYMD, String endTime, double threshold,
			double capacity, int priority) {
		this.profileName = profileName;
		this.modificationNumber = modificationNumber;
		this.testEvent = testEvent;
		this.marketContext = marketContext;
		this.eventStatus = eventStatus;
		this.uid = uid;
		this.duration = duration;
		this.eventID = eventID;
		this.signalName = signalName;
		this.signalType = signalType;
		this.signalID = signalID;
		this.currentValue = currentValue;
		this.startYMD = startYMD;
		this.startTime = startTime;
		this.endYMD = endYMD;
		this.endTime = endTime;
		this.threshold = threshold;
		this.capacity = capacity;
		this.priority = priority;
	}

	public EventInformation(EventInformation event) {
		this();
		this.profileName = event.profileName;
		this.modificationNumber = event.modificationNumber;
		this.testEvent = event.testEvent;
		this.marketContext = event.marketContext;
		this.eventStatus = event.eventStatus;
		this.uid = event.uid;
		this.duration = event.duration;
		this.eventID = event.eventID;
		this.signalName = event.signalName;
		this.signalType = event.signalType;
		this.signalID = event.signalID;
		this.currentValue = event.currentValue;
		this.startYMD = event.startYMD;
		this.startTime = event.startTime;
		this.endYMD = event.endYMD;
		this.endTime = event.endTime;
		this.threshold = event.threshold;
		this.capacity = event.capacity;
		this.priority = event.priority;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public int getModificationNumber() {
		return modificationNumber;
	}

	public void setModificationNumber(int modificationNumber) {
		this.modificationNumber = modificationNumber;
	}

	public boolean isTestEvent() {
		return testEvent;
	}

	public void setTestEvent(boolean testEvent) {
		this.testEvent = testEvent;
	}

	public String getMarketContext() {
		return marketContext;
	}

	public void setMarketContext(String marketContext) {
		this.marketContext = marketContext;
	}

	public String getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getEventID() {
		return eventID;
	}

	public void setEventID(int eventID) {
		this.eventID = eventID;
	}

	public String getSignalName() {
		return signalName;
	}

	public void setSignalName(String signalName) {
		this.signalName = signalName;
	}

	public String getSignalType() {
		return signalType;
	}

	public void setSignalType(String signalType) {
		this.signalType = signalType;
	}

	public int getSignalID() {
		return signalID;
	}

	public void setSignalID(int signalID) {
		this.signalID = signalID;
	}

	public int getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(int currentValue) {
		this.currentValue = currentValue;
	}

	public String getStartYMD() {
		return startYMD;
	}

	public void setStartYMD(String startYMD) {
		this.startYMD = startYMD;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndYMD() {
		return endYMD;
	}

	public void setEndYMD(String endYMD) {
		this.endYMD = endYMD;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public double getCapacity() {
		return capacity;
	}

	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void updateInformation(ControlMessage control) {
		this.startYMD = control.getStartYMD();
		this.startTime = control.getStartTime();
		this.endYMD = control.getEndYMD();
		this.endTime = control.getEndTime();
		this.threshold = control.getThreshold();
	}

	@Override
	public String toString() {
		return "{\"profileName\":\"" + profileName + "\"," + "\"modificationNumber\":" + modificationNumber + ","
				+ "\"testEvent\":" + testEvent + "," + "\"marketContext\":\"" + marketContext + "\","
				+ "\"eventStatus\":\"" + eventStatus + "\"," + "\"uid\":" + uid + ","

				+ "\"duration\":" + duration + "," + "\"eventID\":" + eventID + "," + "\"signalName\":\"" + signalName
				+ "\"," + "\"signalType\":\"" + signalType + "\"," + "\"signalID\":" + signalID + ","
				+ "\"currentValue\":" + currentValue + "," + "\"startYMD\":\"" + startYMD + "\"," + "\"startTime\":\""
				+ startTime + "\"," + "\"endYMD\":\"" + endYMD + "\"," + "\"endTime\":\"" + endTime + "\","

				+ "\"threshold\":" + threshold + "," + "\"capacity\":" + capacity + "," + "\"priority\":" + priority
				+ "" + "}";
	}

	class ControlMessage {
		private String startYMD;
		private String startTime;
		private String endYMD;
		private String endTime;
		private double threshold;

		public ControlMessage() {

		}

		public String getStartYMD() {
			return startYMD;
		}

		public void setStartYMD(String startYMD) {
			this.startYMD = startYMD;
		}

		public String getStartTime() {
			return startTime;
		}

		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}

		public String getEndYMD() {
			return endYMD;
		}

		public void setEndYMD(String endYMD) {
			this.endYMD = endYMD;
		}

		public String getEndTime() {
			return endTime;
		}

		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}

		public double getThreshold() {
			return threshold;
		}

		public void setThreshold(double threshold) {
			this.threshold = threshold;
		}

	}

}
