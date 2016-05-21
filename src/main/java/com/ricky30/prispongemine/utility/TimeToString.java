package com.ricky30.prispongemine.utility;

/**
 * Created by Jamie on 18-May-16.
 */
public class TimeToString
{
	public static String secondsToString(int totalSeconds) 
	{
		final int seconds = totalSeconds % 60; // Seconds more then int minutes
		final int mins = (totalSeconds - seconds) / 60; // Minutes rounded down

		String secondUnit = "seconds";
		String minuteUnit = "minutes";

		if (seconds == 1) 
		{
			secondUnit = "second";
		}

		if (mins == 1) 
		{
			minuteUnit = "minute";
		}

		if (mins == 0) // If mins are 0 don't return "0 minutes"
		{
			return seconds + secondUnit;
		} 
		else 
		{
			return mins + minuteUnit + " " + seconds + secondUnit;
		}
	}
}
