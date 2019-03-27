package com.mir.update.database;

import com.mir.ems.globalVar.*;


public class cema_database {
	

	
	public String srcEMA, destEMA, reportEMA, reportName, reportType;
	
	public int requestID;
	public String time;
	
	
	//EMAupdatedDRinfo
	public String itemunit, marketContext, createDataTime, oadrReport, oadrReportDescription, energyReal;
	public int siSclecode, resourceID, readingType, duration, rid, hertz, ac, oadrMinPeriod,
	oadrMaxPeriod, oadrOnChange, reportRequestID, reportSpecfierID;
	
	//EMAupdatedMgnInfo
	public int emaCNT, dimming, priority;
	public String state;
	public double power, margin, generate, storage, maxValue, minValue, avgValue;
	public String maxTime, minTime;
	

	
	
	public cema_database() {
		
	}
	
	public cema_database(String srcEMA, String destEMA, String reportEMA, String reportName, String reportType,String itemunit, String marketContext, String createDataTime, String oadrReport, String oadrReportDescription, String energyReal,
			int requestId, String time, int siSclecode, int resourceID, int readingType, int duration, int rid, int hertz, int ac, int oadrMinPeriod,
			int oadrMaxPeriod, int oadrOnChange,int reportRequestID,int reportSpecfierID, int emaCNT, int dimming, String state, double power, double margin,double generate,double storage,double maxValue,double minValue,double avgValue, String maxTime, String minTime 
			) {
		this.setCema_databse(srcEMA, destEMA, reportEMA, reportName, reportType, itemunit, marketContext, createDataTime, oadrReport, oadrReportDescription, energyReal, requestId, time, siSclecode, resourceID, readingType, duration, rid, hertz, ac, oadrMinPeriod, oadrMaxPeriod, oadrOnChange, reportRequestID, reportSpecfierID, emaCNT, dimming, state, power, margin, generate, storage, maxValue, minValue, avgValue, maxTime, minTime);
		
		
	}
	
	public void buildup(String emaID, String qos, String state, double power, int dimming, double margin, double generate, double storage, double maxValue, double minValue, double avgValue,
			String maxTime, String minTime, int priority){
		
		
//		System.out.println("디비 빌드업");
		setAvgValue(avgValue);
		setAc(ac);
		setCreateDataTime(createDataTime);
		setDestEMA(destEMA);
		setDimming(dimming);
		setDuration(duration);
		setEmaCNT(emaCNT);
		setEnergyReal(energyReal);
		setGenerate(generate);
		setHertz(hertz);
		setItemunit(itemunit);
		setMargin(margin);
		setMarketContext(marketContext);
		setMaxTime(maxTime);
		setMaxValue(maxValue);
		setMinTime(minTime);
		setMinValue(minValue);
		setOadrMaxPeriod(oadrMaxPeriod);
		setOadrMinPeriod(oadrMinPeriod);
		setOadrOnChange(oadrOnChange);
		setOadrReport(oadrReportDescription);
		setOadrReportDescription(oadrReportDescription);
		setPower(power);
		setReadingType(readingType);
		setRequestID(reportRequestID);
		setResourceID(resourceID);
		setRid(rid);
		setReportEMA(reportEMA);
		setReportRequestID(reportRequestID);
		setReportName(reportName);
		setReportSpecfierID(reportSpecfierID);
		setReportType(reportType);
		setSiSclecode(siSclecode);
		setSrcEMA(srcEMA);
		setState(state);
		setStorage(storage);
		setTime(minTime);
		
		global.saveCEMA.put(++global.REPORTSEQ_CEMA, this);
		++global.REPORTCNT_CEMA;
	
	}
	
	public void buildup(String srcEMA, String destEMA, String reportEMA, String reportName, String reportType,String itemunit, String marketContext, String createDataTime, String oadrReport, String oadrReportDescription, String energyReal,
			int requestId, String time, int siSclecode, int resourceID, int readingType, int duration, int rid, int hertz, int ac, int oadrMinPeriod,
			int oadrMaxPeriod, int oadrOnChange,int reportRequestID,int reportSpecfierID, int emaCNT, int dimming, String state, double power, double margin,double generate,double storage,double maxValue,double minValue,double avgValue, String maxTime, String minTime 
			) {
		setAvgValue(avgValue);
		setAc(ac);
		setCreateDataTime(createDataTime);
		setDestEMA(destEMA);
		setDimming(dimming);
		setDuration(duration);
		setEmaCNT(emaCNT);
		setEnergyReal(energyReal);
		setGenerate(generate);
		setHertz(hertz);
		setItemunit(itemunit);
		setMargin(margin);
		setMarketContext(marketContext);
		setMaxTime(maxTime);
		setMaxValue(maxValue);
		setMinTime(minTime);
		setMinValue(minValue);
		setOadrMaxPeriod(oadrMaxPeriod);
		setOadrMinPeriod(oadrMinPeriod);
		setOadrOnChange(oadrOnChange);
		setOadrReport(oadrReportDescription);
		setOadrReportDescription(oadrReportDescription);
		setPower(power);
		setReadingType(readingType);
		setRequestID(reportRequestID);
		setResourceID(resourceID);
		setRid(rid);
		setReportEMA(reportEMA);
		setReportRequestID(reportRequestID);
		setReportName(reportName);
		setReportSpecfierID(reportSpecfierID);
		setReportType(reportType);
		setSiSclecode(siSclecode);
		setSrcEMA(srcEMA);
		setState(state);
		setStorage(storage);
		setTime(minTime);
		
		global.saveCEMA.put(++global.REPORTSEQ_CEMA, this);
		++global.REPORTCNT_CEMA;
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
	
	}
	
	public void setCema_databse(String srcEMA, String destEMA, String reportEMA, String reportName, String reportType,String itemunit, String marketContext, String createDataTime, String oadrReport, String oadrReportDescription, String energyReal,
			int requestId, String time, int siSclecode, int resourceID, int readingType, int duration, int rid, int hertz, int ac, int oadrMinPeriod,
			int oadrMaxPeriod, int oadrOnChange,int reportRequestID,int reportSpecfierID, int emaCNT, int dimming, String state, double power, double margin,double generate,double storage,double maxValue,double minValue,double avgValue, String maxTime, String minTime 
			) {
		this.setAvgValue(avgValue);
		this.setAc(ac);
		this.setCreateDataTime(createDataTime);
		this.setDestEMA(destEMA);
		this.setDimming(dimming);
		this.setDuration(duration);
		this.setEmaCNT(emaCNT);
		this.setEnergyReal(energyReal);
		this.setGenerate(generate);
		this.setHertz(hertz);
		this.setItemunit(itemunit);
		this.setMargin(margin);
		this.setMarketContext(marketContext);
		this.setMaxTime(maxTime);
		this.setMaxValue(maxValue);
		this.setMinTime(minTime);
		this.setMinValue(minValue);
		this.setOadrMaxPeriod(oadrMaxPeriod);
		this.setOadrMinPeriod(oadrMinPeriod);
		this.setOadrOnChange(oadrOnChange);
		this.setOadrReport(oadrReportDescription);
		this.setOadrReportDescription(oadrReportDescription);
		this.setPower(power);
		this.setReadingType(readingType);
		this.setRequestID(reportRequestID);
		this.setResourceID(resourceID);
		this.setRid(rid);
		this.setReportEMA(reportEMA);
		this.setReportRequestID(reportRequestID);
		this.setReportName(reportName);
		this.setReportSpecfierID(reportSpecfierID);
		this.setReportType(reportType);
		this.setSiSclecode(siSclecode);
		this.setSrcEMA(srcEMA);
		this.setState(state);
		this.setStorage(storage);
		this.setTime(minTime);
		
	
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

	public String getReportEMA() {
		return reportEMA;
	}

	public void setReportEMA(String reportEMA) {
		this.reportEMA = reportEMA;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public int getRequestID() {
		return requestID;
	}

	public void setRequestID(int requestID) {
		this.requestID = requestID;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getItemunit() {
		return itemunit;
	}

	public void setItemunit(String itemunit) {
		this.itemunit = itemunit;
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

	public String getOadrReport() {
		return oadrReport;
	}

	public void setOadrReport(String oadrReport) {
		this.oadrReport = oadrReport;
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

	public int getOadrMinPeriod() {
		return oadrMinPeriod;
	}

	public void setOadrMinPeriod(int oadrMinPeriod) {
		this.oadrMinPeriod = oadrMinPeriod;
	}

	public int getOadrMaxPeriod() {
		return oadrMaxPeriod;
	}

	public void setOadrMaxPeriod(int oadrMaxPeriod) {
		this.oadrMaxPeriod = oadrMaxPeriod;
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

	public int getReportSpecfierID() {
		return reportSpecfierID;
	}

	public void setReportSpecfierID(int reportSpecfierID) {
		this.reportSpecfierID = reportSpecfierID;
	}

	public int getEmaCNT() {
		return emaCNT;
	}

	public void setEmaCNT(int emaCNT) {
		this.emaCNT = emaCNT;
	}

	public int getDimming() {
		return dimming;
	}

	public void setDimming(int dimming) {
		this.dimming = dimming;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public double getPower() {
		return power;
	}

	public void setPower(double power) {
		this.power = power;
	}

	public double getMargin() {
		return margin;
	}

	public void setMargin(double margin) {
		this.margin = margin;
	}

	public double getGenerate() {
		return generate;
	}

	public void setGenerate(double generate) {
		this.generate = generate;
	}

	public double getStorage() {
		return storage;
	}

	public void setStorage(double storage) {
		this.storage = storage;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public double getAvgValue() {
		return avgValue;
	}

	public void setAvgValue(double avgValue) {
		this.avgValue = avgValue;
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
	
	
	
	

}
