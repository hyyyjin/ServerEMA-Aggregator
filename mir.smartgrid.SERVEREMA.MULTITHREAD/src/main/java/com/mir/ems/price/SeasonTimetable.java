package com.mir.ems.price;

public class SeasonTimetable {

	String stage, strTime, endTime;

	public SeasonTimetable(String stage, String strTime, String endTime) {

		setStage(stage);
		setStrTime(strTime);
		setEndTime(endTime);

	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getStrTime() {
		return strTime;
	}

	public void setStrTime(String strTime) {
		this.strTime = strTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}