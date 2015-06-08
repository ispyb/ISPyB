/*******************************************************************************
 * This file is part of ISPyB.
 * 
 * ISPyB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ISPyB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ISPyB.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau, M. Bodin, A. De Maria Antolinos
 ******************************************************************************/
package ispyb.common.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class IspybDateUtils {

	/**
	 * Rolls a date by the given amount of days. If the days are negative the date is rolled back in history, if they
	 * are positive the date is rolled forward to the future.
	 * 
	 * @param date
	 *            the date to roll
	 * @param days
	 *            the number of days to roll this date (a positive number rolls the date into the future
	 * @return the rolled date
	 */
	public static Date rollDateByDay(Date date, int days) {
		if (date == null || days == 0) {
			return date;
		}
		Calendar cal = getCalendar();
		cal.clear();
		cal.setTime(date);
		// roll date
		cal = rollCalendarDay(cal, days);
		// return date
		return cal.getTime();
	}

	/**
	 * Roll calendar by amount of days (may be positive or negative) even over year boundaries.
	 * 
	 * @param cal
	 *            the calendar object
	 * @param days
	 *            the number of days (positive number will roll into the future, negative number will roll into the
	 *            past)
	 * @return the calendar with the new date
	 */
	public static Calendar rollCalendarDay(Calendar cal, int days) {
		if (days == 0 || cal == null) {
			return cal;
		}
		int delta = (days > 0 ? 1 : -1);
		for (int i = 0; i < Math.abs(days); i++) {
			if (delta == 1 && cal.get(Calendar.DAY_OF_MONTH) == 31 && cal.get(Calendar.MONTH) == Calendar.DECEMBER) {
				// roll to new year
				cal.set(cal.get(Calendar.YEAR) + 1, Calendar.JANUARY, 1);
			} else if (delta == -1 && cal.get(Calendar.DAY_OF_MONTH) == 1
					&& cal.get(Calendar.MONTH) == Calendar.JANUARY) {
				// roll to new year
				cal.set(cal.get(Calendar.YEAR) - 1, Calendar.DECEMBER, 31);
			} else {
				cal.add(Calendar.DAY_OF_YEAR, delta);
			}
		}
		return cal;
	}

	/**
	 * Creates a calendar instance configured for french dates (local, timezone, start day of week and minimal days for
	 * first week in year are configured correctly).
	 * 
	 * @return
	 */
	public static Calendar getCalendar() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"), Locale.FRANCE);
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		// the first thursday in a year defines the first week
		cal.setMinimalDaysInFirstWeek(4);
		return cal;
	}

}
