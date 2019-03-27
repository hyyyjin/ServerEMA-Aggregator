package com.mir.ven;

import java.util.Calendar;

public class TimeFormat {

	
	public String getCurrentTime(){
		

		String currentTimeForm ="";
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);
		
		String date;

		if (month < 10 && day < 10) {
			date = Integer.toString(year)+"-" + "0" + Integer.toString(month) +"-"+ "0" + Integer.toString(day);
		} else if (month < 10 && !(day < 10)) {
			date = Integer.toString(year) +"-"+ "0" + Integer.toString(month) +"-"+ Integer.toString(day);
		} else if (!(month < 10) && day < 10) {
			date = Integer.toString(year) +"-"+ Integer.toString(month) +"-"+ "0" + Integer.toString(day);
		} else {
			date = Integer.toString(year) +"-"+ Integer.toString(month) +"-"+ Integer.toString(day);
		}
		
		String start_hour, start_minute, start_sec;

		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int sec = calendar.get(Calendar.SECOND);
		start_hour = Integer.toString(hour);
		start_minute = Integer.toString(minute);
		start_sec = Integer.toString(sec);
		
		currentTimeForm = date + "T" + start_hour+":"+start_minute+":"+start_sec+"Z";
		
		
		return currentTimeForm;
	}
	
}
