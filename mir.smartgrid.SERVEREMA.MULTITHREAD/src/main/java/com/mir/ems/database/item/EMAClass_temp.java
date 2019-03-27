package com.mir.ems.database.item;

import java.util.ArrayList;
import java.util.Date;

public class EMAClass_temp {

	String emaID, protocol, reportType;
	Double power, margin, generated, stored;
	int emaCnt;
	Date timeStamp;
	
	public EMAClass_temp(String emaID, String protocol, Double power, Double margin, Double generated, Double stored,
			int emaCnt, String reportType, Date timeStamp) {
		this.emaID = emaID;
		this.protocol = protocol;
		this.power = power;
		this.margin = margin;
		this.generated = generated;
		this.stored = stored;
		this.emaCnt = emaCnt;
		this.reportType = reportType;
		this.timeStamp = timeStamp;
		
	}

	public String getEmaID() {
		return emaID;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getReportType() {
		return reportType;
	}

	public Double getPower() {
		return power;
	}

	public Double getMargin() {
		return margin;
	}

	public Double getGenerated() {
		return generated;
	}

	public Double getStored() {
		return stored;
	}

	public int getEmaCnt() {
		return emaCnt;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	@Override
	public String toString() {
		return emaID + "/" + protocol + "/" + reportType + "/"
				+ power + "/" + margin + "/" + generated + "/" + stored + "/"
				+ emaCnt + "/" + timeStamp;
	}

}
