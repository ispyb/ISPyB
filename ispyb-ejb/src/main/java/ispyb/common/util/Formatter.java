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
/*
 * Util.java
 * 
 * Created on Mar 1, 2005
 *
 */
package ispyb.common.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Util class with parsers and formatters for dates.
 * 
 * @author ricardo.leal@esrf.fr
 * @version 0.1
 */
public class Formatter {

	/**
	 * Parse a Timestamp value and returns only the date in standard format.
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT);
		try {
			return formatter.format(date);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Parse a Timestamp value and returns the date and time in standard format.
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateTime(Timestamp date) {
		SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
		try {
			return formatter.format(date);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Converts a String in standard formats in a Timestamp
	 * 
	 * @param date
	 * @return
	 */
	public static Timestamp stringToTimestamp(String date) {
		SimpleDateFormat formatterDateTime = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
		SimpleDateFormat formatterDate = new SimpleDateFormat(Constants.DATE_FORMAT);
		try {
			return new Timestamp((formatterDateTime.parse(date)).getTime());
		} catch (Exception e) {
			try {
				return new Timestamp((formatterDate.parse(date)).getTime());
			} catch (Exception ex) {
				return null;
			}
		}
	}

	/**
	 * Get the current year from the system
	 * 
	 */

	static public String getYearStr() {
		java.util.Date DateJ = new java.util.Date();
		String myString2 = DateFormat.getDateInstance(DateFormat.LONG, Locale.FRANCE).format(DateJ);
		int taille2 = myString2.length();
		String Datef = myString2.substring((taille2 - 4), taille2);

		return Datef;
	}

	static public int getYear() {
		String yearStr = getYearStr();
		int year = -1;
		try {
			year = Integer.parseInt(yearStr);
		} catch (Exception e) {
			year = -1;
		}

		return year;
	}

	/**
	 * @param pattern
	 *            the chosen date format pattern
	 * @return the current date with the format dd/mm/yyyy
	 */
	static public String getCurrentDate(String pattern) {
		Date today = Calendar.getInstance().getTime();
		if (pattern == null || pattern.length() == 0)
			pattern = Constants.DATE_FORMAT_DDMMYYYY;
		SimpleDateFormat simple = new SimpleDateFormat(pattern);
		return simple.format(today);
	}

	static public String getCurrentDate() {
		return getCurrentDate(Constants.DATE_FORMAT_DDMMYYYY);
	}

	/**
	 * Format a string value for a db search using the following search modes: "is", "contains", "starts with" and
	 * "ends with"
	 * 
	 * @param inValue
	 *            the string value to be formated
	 * @param searchMode
	 *            the search mode used (see above values) If the search mode is empty: "contains" is used.
	 * @return the formated string value
	 */
	static public String formatForSearch(String inValue, String searchMode) {
		if (inValue == null || inValue.length() == 0)
			return "";

		if (searchMode == null || searchMode.length() == 0)
			searchMode = "contains";

		String outValue = inValue;
		switch (searchMode.charAt(0)) {
		case 'i': // is

			outValue = inValue;
			break;

		case 'c': // contains

			outValue = "%" + inValue + "%";
			break;

		case 's': // starts with

			outValue = inValue + "%";
			break;

		case 'e': // ens with

			outValue = "%" + inValue;
			break;
		}

		return outValue;
	}

	/**
	 * Convert a String to an Integer
	 * 
	 * @param inStr
	 *            the string to be parsed as an Integer
	 * @return an Integer object
	 */
	static public Integer formatInt(String inStr) {
		Integer out = null;
		try {
			out = new Integer((inStr == null ? "" : inStr));
		} catch (Exception e) {
			out = null;
		}
		return out;
	}
}
