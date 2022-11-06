package com.shah.fse.util;

import java.sql.Timestamp;
import java.util.Calendar;

public class TimeStampUtil {

	public static Calendar getCalendarBeforeTenDays() {
		Calendar currentCalendar = Calendar.getInstance();
		currentCalendar.add(Calendar.DAY_OF_MONTH, -10);
		return currentCalendar;
	}
	
	public static Calendar getCalendarFromTimeStamp(Timestamp timeStamp) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeStamp.getTime());
		return calendar;
	}
	
}
