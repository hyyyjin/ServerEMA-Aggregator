/*
 * 
 * Copyright (c) 2018, Hyunjin Park, Mobile Intelligence and Routing Lab
 * All rights reserved
 * 
 * 2018.07.05(Thu)
 * Edited by Hyunjin Park
 * Hanyang University
 * 
 * Convert duration into seconds Duration should be an ISO8601 String :
 * http://en.wikipedia.org/wiki/ISO_8601
 * PT10S: 10 Seconds
 * 
 */

package com.mir.ven;

public abstract class ISO8601 {

	final int sec = 1000;
	final int min = 60 * 1000;
	final int hour = 3600 * 1000;

	public int parseDutrationTotalSeconds(String duration) {

		String[] durationParse;
		int pollDuration = 1000;

		if (duration.contains("S")) {

			durationParse = duration.split("T");
			pollDuration = sec * Integer.parseInt(durationParse[1].replaceAll("S", ""));

		} else if (duration.contains("M")) {

			durationParse = duration.split("T");
			pollDuration = min * Integer.parseInt(durationParse[1].replaceAll("M", ""));

		} else if (duration.contains("H")) {

			durationParse = duration.split("T");
			pollDuration = hour * Integer.parseInt(durationParse[1].replaceAll("H", ""));

		} else {
			System.err.println(
					"Wrong Duration Format, Check the abstract class ISO8601. Default Poll Frequency is 1 Second");
		}

		return pollDuration;
	}
}
