package com.ricky30.prispongemine;

/**
 * Created by Jamie on 18-May-16.
 */
public class TimeToString {

	public static String secondsToString(int totalSeconds) {
		int seconds = totalSeconds % 60;
		int mins = ((totalSeconds - seconds) / 60);

		String secondUnit = "seconds";
		String minuteUnit = "minutes";

		if (seconds == 1) {
			secondUnit = "second";
		}

		if (mins == 1) {
			minuteUnit = "minute";
		}

		if (mins == 0) {
			return seconds + secondUnit;
		} else {
			return mins + minuteUnit + " " + seconds + secondUnit;
		}


	}

}
