package com.mir.smartgrid.simulator.profile.emap;

public class DRInformation {	
	private long id;
	
	private String itemUnit;
	private String marketContext;
	private String createDataTime;
	private int siSclecode;
	private int resourceID;
	private int readingType;
	private String oadrReport;
	private int duration;
	private int rid;
	private int hertz;
	private int ac;
	private int oadrMinPeriod;
	private int oadrMaxPeriod;
	private String oadrReportDescription;
	private String energyReal;
	private int oadrOnChange;
	private int reportRequestID;
	private int reportSpecifierID;
	
	public DRInformation() {

	}

	public DRInformation(String itemUnit, String marketContext, String createDataTime, int siSclecode, int resourceID,
			int readingType, String oadrReport, int duration, int rid, int hertz, int ac, int oadrMinPeriod,
			int oadrMaxPeriod, String oadrReportDescription, String energyReal, int oadrOnChange, int reportRequestID,
			int reportSpecifierID) {
		this.itemUnit = itemUnit;
		this.marketContext = marketContext;
		this.createDataTime = createDataTime;
		this.siSclecode = siSclecode;
		this.resourceID = resourceID;
		this.readingType = readingType;
		this.oadrReport = oadrReport;
		this.duration = duration;
		this.rid = rid;
		this.hertz = hertz;
		this.ac = ac;
		this.oadrMinPeriod = oadrMinPeriod;
		this.oadrMaxPeriod = oadrMaxPeriod;
		this.oadrReportDescription = oadrReportDescription;
		this.energyReal = energyReal;
		this.oadrOnChange = oadrOnChange;
		this.reportRequestID = reportRequestID;
		this.reportSpecifierID = reportSpecifierID;
	}
	
	public DRInformation(DRInformation drInfo) {
		this();
		this.itemUnit = drInfo.itemUnit;
		this.marketContext = drInfo.marketContext;
		this.createDataTime = drInfo.createDataTime;
		this.siSclecode = drInfo.siSclecode;
		this.resourceID = drInfo.resourceID;
		this.readingType = drInfo.readingType;
		this.oadrReport = drInfo.oadrReport;
		this.duration = drInfo.duration;
		this.rid = drInfo.rid;
		this.hertz = drInfo.hertz;
		this.ac = drInfo.ac;
		this.oadrMinPeriod = drInfo.oadrMinPeriod;
		this.oadrMaxPeriod = drInfo.oadrMaxPeriod;
		this.oadrReportDescription = drInfo.oadrReportDescription;
		this.energyReal = drInfo.energyReal;
		this.oadrOnChange = drInfo.oadrOnChange;
		this.reportRequestID = drInfo.reportRequestID;
		this.reportSpecifierID = drInfo.reportSpecifierID;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getItemUnit() {
		return itemUnit;
	}

	public void setItemUnit(String itemUnit) {
		this.itemUnit = itemUnit;
	}

	public String getMarketContext() {
		return marketContext;
	}

	public void setMarketContext(String marketContext) {
		this.marketContext = marketContext;
	}

	public String getCreateDataTime() {
		return createDataTime;
	}

	public void setCreateDataTime(String createDataTime) {
		this.createDataTime = createDataTime;
	}

	public int getSiSclecode() {
		return siSclecode;
	}

	public void setSiSclecode(int siSclecode) {
		this.siSclecode = siSclecode;
	}

	public int getResourceID() {
		return resourceID;
	}

	public void setResourceID(int resourceID) {
		this.resourceID = resourceID;
	}

	public int getReadingType() {
		return readingType;
	}

	public void setReadingType(int readingType) {
		this.readingType = readingType;
	}

	public String getOadrReport() {
		return oadrReport;
	}

	public void setOadrReport(String oadrReport) {
		this.oadrReport = oadrReport;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getRid() {
		return rid;
	}

	public void setRid(int rid) {
		this.rid = rid;
	}

	public int getHertz() {
		return hertz;
	}

	public void setHertz(int hertz) {
		this.hertz = hertz;
	}

	public int getAc() {
		return ac;
	}

	public void setAc(int ac) {
		this.ac = ac;
	}

	public int getOadrMaxPeriod() {
		return oadrMaxPeriod;
	}

	public void setOadrMaxPeriod(int oadrMaxPeriod) {
		this.oadrMaxPeriod = oadrMaxPeriod;
	}

	public int getOadrMinPeriod() {
		return oadrMinPeriod;
	}

	public void setOadrMinPeriod(int oadrMinPeriod) {
		this.oadrMinPeriod = oadrMinPeriod;
	}

	public String getOadrReportDescription() {
		return oadrReportDescription;
	}

	public void setOadrReportDescription(String oadrReportDescription) {
		this.oadrReportDescription = oadrReportDescription;
	}

	public String getEnergyReal() {
		return energyReal;
	}

	public void setEnergyReal(String energyReal) {
		this.energyReal = energyReal;
	}

	public int getOadrOnChange() {
		return oadrOnChange;
	}

	public void setOadrOnChange(int oadrOnChange) {
		this.oadrOnChange = oadrOnChange;
	}

	public int getReportRequestID() {
		return reportRequestID;
	}

	public void setReportRequestID(int reportRequestID) {
		this.reportRequestID = reportRequestID;
	}

	public int getReportSpecifierID() {
		return reportSpecifierID;
	}

	public void setReportSpecifierID(int reportSpecifierID) {
		this.reportSpecifierID = reportSpecifierID;
	}

	@Override
	public String toString() {
		return "{\"itemunit\":\"" + itemUnit + "\","
				+ "\"marketContext\":\"" + marketContext + "\","
				+ "\"createDataTime\":\"" + createDataTime + "\","
				+ "\"siSclecode\":" + siSclecode + ","
				+ "\"resourceID\":" + resourceID + ","
				+ "\"readingType\":" + readingType + ","
				+ "\"oadrReport\":\"" + oadrReport + "\","
				+ "\"duration\":" + duration + ","
				+ "\"rid\":" + rid + ","
				+ "\"hertz\":" + hertz + ","
				+ "\"ac\":" + ac + ","
				+ "\"oadrMinPeriod\":" + oadrMinPeriod + ","
				+ "\"oadrMAXPeriod\":" + oadrMaxPeriod + ","							
				+ "\"oadrReportDescription\":\"" + oadrReportDescription + "\","
				+ "\"energyReal\":\"" + energyReal + "\","
				+ "\"oadrOnChange\":" + oadrOnChange + ","
				+ "\"reportRequestID\":" + reportRequestID + ","
				+ "\"reportSpecifierID\":" + reportSpecifierID 
				+ "}";
	}

}
