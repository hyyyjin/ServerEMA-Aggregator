package com.mir.ems.schedule;

import java.util.Calendar;
import java.util.Date;
import com.mir.ems.globalVar.global;

public class ScheduleManager {

	public ScheduleManager() {

	}

	@SuppressWarnings("deprecation")
	public void setAvailbleDate(Date date) {

		global.availableSchedule.put(date, 3000.0);

		Calendar calendar = Calendar.getInstance();

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE) - 1;

		Date yesterday = new Date((year - 1900), month, day);

		if (global.availableSchedule.keySet().contains(yesterday)) {

			global.availableSchedule.remove(yesterday);
		}

	}

	public boolean availableCheck(Date date, double requestPower) {

		if (global.availableSchedule.keySet().contains(date)) {

			double hasPower = global.availableSchedule.get(date).doubleValue();

			if (hasPower - requestPower > 0) {

				double result = hasPower - requestPower;
				global.availableSchedule.replace(date, result);

				return true;

			}

		}

		return false;
	}
}
