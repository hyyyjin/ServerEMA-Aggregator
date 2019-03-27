package com.mir.ems.price;

public class Industrial {

	private String type, stage, season;
	private double price, summer, springFall, winter;
	private double lightSummer, lightSpringFall, lightWinter, midSummer, midSpringFall, midWinter, maxSummer,
			maxSpringFall, maxWinter;
	private SeasonTimetable timetable;

	public Industrial(String type, String stage, double price, double summer, double springFall, double winter) {

		setType(type);
		setStage(stage);
		setPrice(price);
		setSummer(summer);
		setSpringFall(springFall);
		setWinter(winter);

	}

	public Industrial(String type, String stage, double price, double lightSummer, double lightSpringFall,
			double lightWinter, double midSummer, double midSpringFall, double midWinter, double maxSummer,
			double maxSpringFall, double maxWinter) {

		setType(type);
		setStage(stage);
		setPrice(price);
		setLightSummer(lightSummer);
		setLightSpringFall(lightSpringFall);
		setLightWinter(lightWinter);
		setMidSummer(midSummer);
		setMidSpringFall(midSpringFall);
		setMidWinter(midWinter);
		setMaxSummer(maxSummer);
		setMaxSpringFall(maxSpringFall);
		setMaxWinter(maxWinter);

	}

	public Industrial(String season, SeasonTimetable timetable) {

		setSeason(season);
		setTimetable(timetable);

	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getSummer() {
		return summer;
	}

	public void setSummer(double summer) {
		this.summer = summer;
	}

	public double getSpringFall() {
		return springFall;
	}

	public void setSpringFall(double springFall) {
		this.springFall = springFall;
	}

	public double getWinter() {
		return winter;
	}

	public void setWinter(double winter) {
		this.winter = winter;
	}

	public double getLightSummer() {
		return lightSummer;
	}

	public void setLightSummer(double lightSummer) {
		this.lightSummer = lightSummer;
	}

	public double getLightSpringFall() {
		return lightSpringFall;
	}

	public void setLightSpringFall(double lightSpringFall) {
		this.lightSpringFall = lightSpringFall;
	}

	public double getLightWinter() {
		return lightWinter;
	}

	public void setLightWinter(double lightWinter) {
		this.lightWinter = lightWinter;
	}

	public double getMidSummer() {
		return midSummer;
	}

	public void setMidSummer(double midSummer) {
		this.midSummer = midSummer;
	}

	public double getMidSpringFall() {
		return midSpringFall;
	}

	public void setMidSpringFall(double midSpringFall) {
		this.midSpringFall = midSpringFall;
	}

	public double getMidWinter() {
		return midWinter;
	}

	public void setMidWinter(double midWinter) {
		this.midWinter = midWinter;
	}

	public double getMaxSummer() {
		return maxSummer;
	}

	public void setMaxSummer(double maxSummer) {
		this.maxSummer = maxSummer;
	}

	public double getMaxSpringFall() {
		return maxSpringFall;
	}

	public void setMaxSpringFall(double maxSpringFall) {
		this.maxSpringFall = maxSpringFall;
	}

	public double getMaxWinter() {
		return maxWinter;
	}

	public void setMaxWinter(double maxWinter) {
		this.maxWinter = maxWinter;
	}

	public SeasonTimetable getTimetable() {
		return timetable;
	}

	public void setTimetable(SeasonTimetable timetable) {
		this.timetable = timetable;
	}

}