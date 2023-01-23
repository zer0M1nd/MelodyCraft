package hciu.pub.mcmod.melodycraft.utils;

import java.util.Calendar;

public class MiscsHelper {

	/**
	 * Format a given timestamp into a formatted string using a given pattern. <br/>
	 * It replaces string patterns "YYYY","MM","DD","hh","mm","ss","iii" into the
	 * year, month, day, hour, minute, second, millisecond value.
	 * 
	 * @param millis  The timestamp to format
	 * @param pattern The pattern
	 * @return The formatted time string.
	 */
	public static String timeFormat(long millis, String pattern) {
		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(millis);
		return pattern.replace("YYYY", String.format("%04d", now.get(Calendar.YEAR)))
				.replace("MM", String.format("%02d", now.get(Calendar.MONTH) + 1))
				.replace("DD", String.format("%02d", now.get(Calendar.DAY_OF_MONTH)))
				.replace("hh", String.format("%02d", now.get(Calendar.HOUR_OF_DAY)))
				.replace("mm", String.format("%02d", now.get(Calendar.MINUTE)))
				.replace("ss", String.format("%02d", now.get(Calendar.SECOND)))
				.replace("iii", String.format("%03d", now.get(Calendar.MILLISECOND)));
	}
	
	public static long currentTime() {
		Calendar now = Calendar.getInstance();
		return now.getTimeInMillis();
	}

}
