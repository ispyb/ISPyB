/*
 * TrackinUtils.java
 * @author patrice.brenchereau@esrf.fr
 * July 8, 2008
 */

package bcr.client.tracking;

import java.util.ArrayList;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import bcr.common.util.Constants;

public class TrackingUtils {

	private static final String ESRF_BC_START = "ESRF";

	private static final int ESRF_BC_LEN = 11;

	private static Logger LOG = Logger.getLogger(TrackingUtils.class);

	/**
	 * @param barcode
	 * @return
	 */
	public static boolean isvalidDewarBarcode(String barcode) {

		if (barcode == null)
			return false;
		else if (barcode.length() == 0)
			return false;
		else if (!barcode.startsWith(ESRF_BC_START))
			return false;
		else if (barcode.length() != ESRF_BC_LEN)
			return false;
		else if (barcode.contains("-"))
			return false;
		else
			return true;
	}

	/**
	 * @param barcode
	 * @return
	 */
	public static boolean isemptyCourierBarcode(String barcode) {

		if (barcode == null)
			return true;
		else if (barcode.length() == 0)
			return true;
		else
			return false;
	}

	/**
	 * @param barcode
	 * @return
	 */
	public static boolean isvalidCourierBarcode(String barcode) {

		if (barcode == null)
			return false;
		else if (barcode.length() == 0)
			return false;
		else
			return true;
	}

	/**
	 * @param courierName
	 * @param originalTrackingNumber
	 * @return
	 */
	public static String processTrackingNumber(String courierName, String originalTrackingNumber) {

		String finalTrackingNumber = "";

		// FEDEX
		if (courierName.equals("FEDEX")) {
			// Manual
			// 0123 4567 8901 (12 digits) -> 0123 4567 8901 (12 digits)
			if (originalTrackingNumber.length() == 12)
				finalTrackingNumber = originalTrackingNumber;
			// Sheet #1 & #2 (manual)
			// 0123 4567 8901 2345 (16 digits) -> 0123 4567 8901 (12 digits)
			else if (originalTrackingNumber.length() == 16)
				finalTrackingNumber = originalTrackingNumber.substring(0, 12);
			// Sheet #3 (web)
			// 0123456789012345 6789 0123 4567 8901 (32 digits) -> 6789 0123 4567 (12 digits)
			else if (originalTrackingNumber.length() == 32)
				finalTrackingNumber = originalTrackingNumber.substring(16, 28);
			// other web code (29/05/2012)
			else if (originalTrackingNumber.length() >= 13)
				finalTrackingNumber = originalTrackingNumber.substring(originalTrackingNumber.length() - 12,
						originalTrackingNumber.length());
			else
				finalTrackingNumber = "";
		}

		// DHL
		else if (courierName.equals("DHL")) {
			// Sheet #1
			// 012 3456 789 (10 digits) -> 012 3456 789 (10 digits)
			if (originalTrackingNumber.length() == 10)
				finalTrackingNumber = originalTrackingNumber;
			else
				finalTrackingNumber = "";
		}

		// TNT
		else if (courierName.equals("TNT")) {
			// International #1
			// GD 012345678 WW (13 digits) -> 012345678 (9 digits)
			if (originalTrackingNumber.length() == 13)
				finalTrackingNumber = originalTrackingNumber.substring(2, 11);
			// International #2
			// 012345678 (9 digits) -> 012345678 (9 digits)
			else if (originalTrackingNumber.length() == 9)
				finalTrackingNumber = originalTrackingNumber;
			// National #1
			// 0123456789012345 (16 digits) -> 0123456789012345 (16 digits)
			else if (originalTrackingNumber.length() == 16)
				finalTrackingNumber = originalTrackingNumber;
			// National #2
			// 0123456789 (10 digits) -> 000000 0123456789 (16 digits)
			else if (originalTrackingNumber.length() == 10)
				finalTrackingNumber = "000000" + originalTrackingNumber;
			else
				finalTrackingNumber = "";
		}

		// TODO Other courier
		else {
			finalTrackingNumber = originalTrackingNumber;
		}

		return finalTrackingNumber;
	}

	/**
	 * @return
	 * @throws NamingException
	 * @throws CreateException
	 * @throws FinderException
	 */
	public static ArrayList getCouriers() {

		ArrayList myList = new ArrayList();
		for (int i = 0; i < Constants.COURIER_LIST.length; i++) {
			myList.add(Constants.COURIER_LIST[i][Constants.COURIER_LIST_COURIER_BARCODE]);
		}
		LOG.debug("Courier list = '" + myList + "'");
		return myList;
	}

	/**
	 * @param courierName
	 * @return
	 */
	public static String normalizeCourierName(String courierName) {

		String result = "";
		for (int i = 0; i < Constants.COURIER_LIST.length; i++) {
			if (Constants.COURIER_LIST[i][Constants.COURIER_LIST_COURIER_BARCODE].equals(courierName)) {
				result = Constants.COURIER_LIST[i][Constants.COURIER_LIST_COURIER_NAME];
				break;
			}
		}
		LOG.debug("Courier found = '" + result + "'");
		return result;
	}

	/**
	 * @param courierName
	 * @return
	 */
	public static String getCourierUrl(String courierName) {

		String result = "";
		for (int i = 0; i < Constants.COURIER_LIST.length; i++) {
			if (Constants.COURIER_LIST[i][Constants.COURIER_LIST_COURIER_NAME].equals(courierName)
					|| Constants.COURIER_LIST[i][Constants.COURIER_LIST_COURIER_BARCODE].equals(courierName)) {
				result = Constants.COURIER_LIST[i][Constants.COURIER_LIST_COURIER_URL];
				break;
			}
		}
		LOG.debug("Courier url = '" + result + "'");
		return result;
	}

	/**
	 * @param courierName
	 * @return
	 */
	public static String getCourierUrlParameter(String courierName) {

		String result = "";
		for (int i = 0; i < Constants.COURIER_LIST.length; i++) {
			if (Constants.COURIER_LIST[i][Constants.COURIER_LIST_COURIER_NAME].equals(courierName)
					|| Constants.COURIER_LIST[i][Constants.COURIER_LIST_COURIER_BARCODE].equals(courierName)) {
				result = Constants.COURIER_LIST[i][Constants.COURIER_LIST_COURIER_URL_PARAM];
				break;
			}
		}
		LOG.debug("Courier url = '" + result + "'");
		return result;
	}

}
