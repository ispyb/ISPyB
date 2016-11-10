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
package ispyb.client.common.util;

import ispyb.common.util.Constants;

import java.io.File;
import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author DELAGENI
 * 
 */
public class UrlUtils {

	private static final String image_pattern = "<IMG SRC=\"([^\"]*)\"";

	private static final String href_pattern = "<A HREF=.*image.*<IMG SRC=\"((.*)_small[.](.*).*)\"></A>";

	private static final String index_pattern = "<A HREF=.*index[.]html.*A>|<A HREF=.*log.*A>";


	/**
	 * Rewrite all image URL in DNA index page
	 * 
	 * @param orig
	 *            - File in string format
	 * @param pathImg
	 *            - path of the image source to replace
	 * @param pathHref
	 *            - path of the <a> link to replace
	 * @param fullDNAPath
	 *            - path to folder where DNA images are stored
	 * @return
	 */
	public static String formatImageURL(String orig, String pathImg, String pathHref, String fullDNAPath) {
		// TODO replace by stylesheet
		String newOrig = orig.replaceAll("<TD>", "<TD><FONT COLOR=\"#003366\">");

		// reformat href tags
		Pattern patternHref = Pattern.compile(href_pattern, Pattern.CASE_INSENSITIVE);
		Matcher matcherHref = patternHref.matcher(newOrig);
		String href_pattern_subs = "<A HREF=\"" + pathHref + "&file=" + fullDNAPath + "$2" + "." + "$3"
				+ "\"><IMG SRC=\"" + "$1" + "\"></A>";
		String tmpHref = matcherHref.replaceAll(href_pattern_subs);

		// reformat img tags
		Pattern pattern = Pattern.compile(image_pattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(tmpHref);
		String image_pattern_subs = "<img src=\"" + pathImg + "&" + Constants.IMG_DNA_PATH_PARAM + "=" + fullDNAPath
				+ "$1\"";
		String tmp1 = matcher.replaceAll(image_pattern_subs);

		return tmp1;
	}

	/**
	 * Rewrite all URL and links in EDNA index page
	 * 
	 * @param orig
	 *            - File in string format
	 * @param pathImg
	 *            - path of the image source to replace
	 * @param pathHref
	 *            - path of the <a> link to replace
	 * @param fullEDNAPath
	 *            - path to folder where DNA images are stored
	 * @return
	 */
	public static String formatEDNApageURL(String orig, String pathImg, String pathHrefImg, String pathHrefFile,
			String fullEDNAPath) {

		String newOrig = orig;

		// reformat href tags for strategy files
		// <a href='edna_html/strategy_1/ControlCharacterisationv12_dataInput.xml'
		// href='viewResults.do?reqCode=displayEDNAFile&EDNAFilePath=/data/pyarch/id29/mx415/20100517/ins/insulin3/ins-insulin3_1_dnafiles/index/edna_html/strategy_1/ControlCharacterisationv12_dataInput.xml'
		String href_pattern_edna_strategy = Constants.SITE_IS_DLS() ? "<A HREF='summary_html/(strategy_[0-9]+/[^']*)'"
				: "<A HREF='edna_html/(strategy_1/[^']*)'";
		Pattern patternHref = Pattern.compile(href_pattern_edna_strategy, Pattern.CASE_INSENSITIVE);

		String href_pattern_subs = "<A HREF='" + pathHrefFile + "&" + Constants.EDNA_FILE_PATH + "=" + fullEDNAPath
				+ "$1" + "'";

		Matcher matcherHref = patternHref.matcher(newOrig);
		String tmp1 = matcherHref.replaceAll(href_pattern_subs);

		// reformat href tags
		// href='edna_html/ref-testscale_1_001_pred.jpg'
		// href='viewResults.do?reqCode=viewEDNAImage&EDNAImagePath=/data/pyarch/id29/mx415/20100517/ins/insulin3/ins-insulin3_1_dnafiles/index/edna_html/ref-testscale_1_001_pred_thumbnail.jpg
		String href_pattern_edna = Constants.SITE_IS_DLS() ? "<A HREF='summary_html/([^']*)'"
				: "<A HREF='edna_html/([^']*)'";
		patternHref = Pattern.compile(href_pattern_edna, Pattern.CASE_INSENSITIVE);

		href_pattern_subs = "<A HREF='" + pathHrefImg + "&" + Constants.EDNA_IMAGE_PATH + "=" + fullEDNAPath + "$1"
				+ "'";

		matcherHref = patternHref.matcher(tmp1);
		String tmp2 = matcherHref.replaceAll(href_pattern_subs);

		// reformat img tags
		// <img src='edna_html/ref-testscale_1_001_pred_thumbnail.jpg'
		// <img
		// src='imageDownload.do?reqCode=getEDNAImage&EDNAImagePath=/data/pyarch/id29/mx415/20100517/ins/insulin3/ins-insulin3_1_dnafiles/index/edna_html/ref-testscale_1_001_pred_thumbnail.jpg'
		String image_pattern_edna = Constants.SITE_IS_DLS() ? "<IMG SRC='summary_html/([^']*)'"
				: "<IMG SRC='edna_html/([^']*)'";
		Pattern pattern = Pattern.compile(image_pattern_edna, Pattern.CASE_INSENSITIVE);
		String image_pattern_subs = "<img src='" + pathImg + "&" + Constants.EDNA_IMAGE_PATH + "=" + fullEDNAPath
				+ "$1" + "'";

		Matcher matcher = pattern.matcher(tmp2);
		String tmp3 = matcher.replaceAll(image_pattern_subs);

		// reformat img tags
		// <img src='ref-bPGM_BeF_G1P_1_2_0001.img'
		// <img
		// src='imageDownload.do?reqCode=getEDNAImage&EDNAImagePath=/data/pyarch/id29/mx415/20100517/ins/insulin3/ins-insulin3_1_dnafiles/index/edna_html/ref-testscale_1_001_pred_thumbnail.jpg'
		String tmp4 = tmp3;
		String image_pattern_edna2 = "SRC=\"(.*)jpg\" title";
		Pattern pattern2 = Pattern.compile(image_pattern_edna2, Pattern.CASE_INSENSITIVE);
		String image_pattern_subs2 = "src='" + pathImg + "&" + Constants.EDNA_IMAGE_PATH + "=" + fullEDNAPath + "$1"
				+ "jpg' title";
		
		Matcher matches = pattern2.matcher(tmp3);

		boolean find = matches.find();
		if (find) {
			tmp4 = matches.replaceAll(image_pattern_subs2);
		}

		String tmp5 = tmp4;
		String href_pattern_log = "<A HREF=\"(.*)_LOG\\.HTML\">";
		Pattern patternHrefLog = Pattern.compile(href_pattern_log, Pattern.CASE_INSENSITIVE);
		String href_pattern_subs_log = "<A HREF='" + pathHrefFile + "&" + Constants.EDNA_FILE_PATH + "=" + fullEDNAPath
				+ "$1" + "_log.html'>";

		Matcher matcherHrefLog = patternHrefLog.matcher(tmp4);
		if (matcherHrefLog.find()) {
			tmp5 = matcherHrefLog.replaceAll(href_pattern_subs_log);
		}

		// reformat href tag
		// href='best_log.html'
		// href='viewResults.do?reqCode=displayEDNAFile&EDNAFilePath=/data/pyarch/id14eh4/opid144/20111130/RAW_DATA/bPGM_BeF_G1P_1_2_dnafiles/index/best_log.html'
		// String href_pattern_log = "<a href=.*_log*.html";
		String tmp6 = tmp5;

		String href_pattern_html = "<A HREF=\"(.*)LOG(.*)\\.HTML\">";
		Pattern pattern3 = Pattern.compile(href_pattern_html, Pattern.CASE_INSENSITIVE);
		String href_pattern_subs3 = "<A HREF='" + pathHrefFile + "&" + Constants.EDNA_FILE_PATH + "=" + fullEDNAPath
				+ "$1" + "log" + "$2" + ".html'>";

		Matcher matches3 = pattern3.matcher(tmp5);
		if (matches3.find()) {
			//tmp6 = matches3.replaceAll(image_pattern_subs3);
			tmp6 = matches3.replaceAll(href_pattern_subs3);
		}
		
		// reformat img tags
		// <img SRC=abb_27_Jan_2014_01.png ALT
		// <img
		// src='imageDownload.do?reqCode=getEDNAImage&EDNAImagePath=/data/pyarch/id29/mx415/20100517/.../abb_27_Jan_2014_01.png'
		String tmp7 = tmp6;
		//String image_pattern_edna3 = "SRC=([^\\s]+).png";
		String image_pattern_edna3 = "SRC=\"(.*)png\" title";
		Pattern pattern7 = Pattern.compile(image_pattern_edna3, Pattern.CASE_INSENSITIVE);
		String image_pattern_subs7 = "src='" + pathImg + "&" + Constants.EDNA_IMAGE_PATH + "=" + fullEDNAPath + "$1" + "png' title";
		
		//String image_pattern_subs7 = "src='" + pathImg + "&" + Constants.EDNA_IMAGE_PATH + "=" + fullEDNAPath + "$1" + ".png' ";
		

		Matcher matches7 = pattern7.matcher(tmp6);

		boolean find7 = matches7.find();
		if (find7) {
			tmp7 = matches7.replaceAll(image_pattern_subs7);
		}

		// reformat href txt file
		// href='abb_27_Jan_2014_01.txt'
		// href='viewResults.do?reqCode=displayEDNAFile&EDNAFilePath=/data/pyarch/id14eh4/opid144/20111130/RAW_DATA/bPGM_BeF_G1P_1_2_dnafiles/index/abb_27_Jan_2014_01.txt'
		// String href_pattern_log = "<a href=.*.txt";
		String tmp8 = tmp7;

		String txt_pattern = "<a HREF=([^\\s]+).txt>";
		Pattern pattern8 = Pattern.compile(txt_pattern, Pattern.CASE_INSENSITIVE);
		String image_pattern_subs8 = "<A HREF='" + pathHrefFile + "&" + Constants.EDNA_FILE_PATH + "=" + fullEDNAPath
				+ "$1" + ".txt' target=_blank>";

		Matcher matches8 = pattern8.matcher(tmp7);
		if (matches8.find()) {
			tmp8 = matches8.replaceAll(image_pattern_subs8);
		}

		String tmp9 = tmp8;
		
		String image_pattern_linkImg = "<A HREF=\"(.*[^_])\\.html\">";
		Pattern pattern9 = Pattern.compile(image_pattern_linkImg, Pattern.CASE_INSENSITIVE);
		String image_pattern_subs9 = "<A HREF='" + pathImg + "&" + Constants.EDNA_IMAGE_PATH + "=" + fullEDNAPath
				+ "$1" + ".jpg'>";

		Matcher matches9 = pattern9.matcher(tmp9);
		if (matches9.find()) {
			tmp9 = matches9.replaceAll(image_pattern_subs9);
		}

		return tmp9;

	}

	public static String formatWorkflowResultpageURL(String orig, String pathImg, String pathHrefFile,
			String fullEDNAPath) {

		String newOrig = orig;
		// reformat img tags
		// <img src='grid.png'
		// <img
		// src='imageDownload.do?reqCode=getEDNAImage&EDNAImagePath=/data/pyarch/id29/mx415/20100517/ins/insulin3/ins-insulin3_1_dnafiles/index/edna_html/grid.png'
		String tmp1 = orig;
		String image_pattern_edna2 = "SRC=\"(.*)png\" title";
		Pattern pattern2 = Pattern.compile(image_pattern_edna2, Pattern.CASE_INSENSITIVE);
		String image_pattern_subs2 = "src='" + pathImg + "&" + Constants.EDNA_IMAGE_PATH + "=" + fullEDNAPath + "/$1"
				+ "png' title";

		Matcher matches = pattern2.matcher(newOrig);

		boolean find = matches.find();
		if (find) {
			tmp1 = matches.replaceAll(image_pattern_subs2);
		}

		// reformat img tags
		// <img src='grid.jpg'
		// <img
		// src='imageDownload.do?reqCode=getEDNAImage&EDNAImagePath=/data/pyarch/id29/mx415/20100517/ins/insulin3/ins-insulin3_1_dnafiles/index/edna_html/grid.jpg'
		String tmp2 = tmp1;
		String image_pattern_edna3 = "SRC=\"(.*)jpg\" title";
		Pattern pattern3 = Pattern.compile(image_pattern_edna3, Pattern.CASE_INSENSITIVE);
		String image_pattern_subs3 = "src='" + pathImg + "&" + Constants.EDNA_IMAGE_PATH + "=" + fullEDNAPath + "/$1"
				+ "jpg' title";

		Matcher matches3 = pattern3.matcher(tmp1);

		find = matches3.find();
		if (find) {
			tmp2 = matches3.replaceAll(image_pattern_subs3);
		}

		// reformat href tag
		// href='best_log.html'
		// href='viewResults.do?reqCode=displayHtmlFile&HTML_FILE=/data/pyarch/id14eh4/opid144/20111130/RAW_DATA/bPGM_BeF_G1P_1_2_dnafiles/index/best_log.html'
		// String href_pattern_log = "<a href=.*_log.html";
		String tmp4 = tmp2;

		String href_pattern = "<A HREF=\"(.*[^_])\\.html\">";
		Pattern patternHref = Pattern.compile(href_pattern, Pattern.CASE_INSENSITIVE);

		String href_pattern_subs = "<A HREF='" + pathHrefFile + "&HTML_FILE=" + fullEDNAPath + "/$1"
				+ ".html' target='_blank'>";

		Matcher matches4 = patternHref.matcher(tmp4);
		if (matches4.find()) {
			tmp4 = matches4.replaceAll(href_pattern_subs);
		}
		return tmp4;

	}

	public static ArrayList<String> getFormatImageURL_DNAPath(String orig, String pathImg, String pathHref,
			String fullDNAPath) {
		// reformat img tags
		ArrayList<String> lstImages = new ArrayList<String>();

		Pattern pattern = Pattern.compile(image_pattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(orig);
		String image_pattern_subs = fullDNAPath + "$1";

		String candidate = null;

		while (matcher.find()) {
			candidate = orig.substring(matcher.start(), matcher.end());

			Matcher matcher2 = pattern.matcher(candidate);
			String image_pattern_subs2 = fullDNAPath + "$1";
			String tmp1 = matcher2.replaceAll(image_pattern_subs);

			lstImages.add(tmp1);
		}

		return lstImages;
	}

	/**
	 * Remove the html links : index.html + dpm_log.html
	 * 
	 * @param orig
	 *            - File in string format
	 * @return
	 */
	public static String deleteIndexLinks(String orig) {

		Pattern pattern = Pattern.compile(index_pattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(orig);
		String image_pattern_subs = "";
		String tmp1 = matcher.replaceAll(image_pattern_subs);

		return tmp1;
	}

	public static String formatImageURL_Denzo(String orig, String path, String fullDNAPath) {
		// TODO replace by stylesheet
		String newOrig = orig.replaceAll("<TD>", "<TD><FONT COLOR=\"#003366\">");

		String image_pattern_subs2 = "<img src=" + path + "&" + Constants.IMG_DNA_PATH_PARAM + "=" + fullDNAPath + "$1"
				+ ">";
		Pattern pattern2 = Pattern.compile("<IMG SRC=([^>]*)>", Pattern.CASE_INSENSITIVE);
		Matcher matcher2 = pattern2.matcher(newOrig);
		String tmp2 = matcher2.replaceAll(image_pattern_subs2);

		return tmp2;
	}

	public static String[] setArrayElement(String element, int index, String[] array) {
		if (array == null) {
			array = new String[index + 1];
		} else if (index >= array.length) {
			String[] tmpArray = new String[index + 1];
			System.arraycopy(array, 0, tmpArray, 0, array.length);
			array = tmpArray;
		}
		array[index] = element;
		return array;
	}

	public static String getArrayElement(int index, String[] array) {
		if (array == null || index >= array.length) {
			return null;
		}
		return array[index];
	}

	public static Object[] setArrayElement(Object element, int index, Object[] array) {
		if (array == null) {
			array = (Object[]) Array.newInstance(element.getClass(), index + 1);
		} else if (index >= array.length) {
			Object[] tmpArray = (Object[]) Array.newInstance(element.getClass(), index + 1);
			System.arraycopy(array, 0, tmpArray, 0, array.length);
			array = tmpArray;
		}
		array[index] = element;
		return array;
	}

	public static Object getArrayElement(int index, Object[] array) {
		if (array == null || index >= array.length) {
			return null;
		}
		return array[index];
	}



	/**
	 * FitPathToOS
	 * 
	 * @param fullFilePath
	 * @return
	 */
	public static String FitPathToOS(String fullFilePath) {
		String newPath = fullFilePath;
		boolean isWindows = (System.getProperty("os.name").indexOf("Win") != -1) ? true : false;

		if (isWindows && newPath != null) {
			// newPath = fullFilePath.replace("/data/", "W://");
			newPath = newPath.replace(Constants.DATA_FILEPATH_START, Constants.DATA_FILEPATH_WINDOWS_MAPPING);
			newPath = newPath.replace(Constants.DATA_PDB_FILEPATH_START, Constants.DATA_PDB_FILEPATH_WINDOWS_MAPPING);
		}
		return newPath;
	}

	/**
	 * getShortFilename
	 * 
	 * @param fullFilePath
	 * @return
	 */
	public static String getShortFilename(String fullFilePath) {
		String shortFilename = fullFilePath;
		try {
			File f = new File(fullFilePath);
			shortFilename = f.getName();
		} catch (Exception e) {
		}

		return shortFilename;
	}

	public static boolean isEmpty(String s) {
		if (s == null || s.length() < 1) {
			return true;
		}
		return false;
	}

	public static boolean isInteger(String s) {
		try {
			int number = Integer.parseInt(s);
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	/**
	 * Rewrite all URL and links in html page
	 * 
	 * @param orig
	 *            - File in string format
	 * @param pathHref
	 *            - path of the <a> link to replace
	 * @return
	 */
	public static String formatURL(String orig, String pathHrefFile) {

		// reformat href tags for txt files
		// <a href=filename.txt
		// href='viewDatacollection.do?reqCode=downloadFile&fullFilePath=/data/pyarch/id29\mx1101\20100909\hemo\4087\filename.txt'

		String href_pattern_txt = "<A HREF=([^>]*.txt)>";

		Pattern patternHref = Pattern.compile(href_pattern_txt, Pattern.CASE_INSENSITIVE);

		String href_pattern_subs = "<A HREF='viewDataCollection.do?reqCode=downloadFile&" + Constants.FULL_FILE_PATH
				+ "=" + pathHrefFile + "$1" + "'>";

		Matcher matcherHref = patternHref.matcher(orig);
		String tmp1 = matcherHref.replaceAll(href_pattern_subs);

		// reformat href tags for fit files
		// <a href=filename.fit
		// href='viewDatacollection.do?reqCode=downloadFile&fullFilePath=/data/pyarch/id29\mx1101\20100909\hemo\4087\filename.fit'

		String href_pattern_fit = "<A HREF=([^>]*.fit)>";

		Pattern patternHref2 = Pattern.compile(href_pattern_fit, Pattern.CASE_INSENSITIVE);

		Matcher matcherHref2 = patternHref2.matcher(tmp1);
		String tmp2 = matcherHref2.replaceAll(href_pattern_subs);

		return tmp2;
	}

	/**
	 * returns true if the specified email is well formatted
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isValidEmailAdress(String email) {
		return Pattern.matches("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)+$", email.toLowerCase());
	}

	/**
	 * useful for webservice because we can not use java.sql.date in wsdl
	 * 
	 * @param date
	 * @return
	 */
	public static Timestamp dateToTimestamp(Date date) {
		if (date == null)
			return null;
		java.sql.Timestamp stamp = new Timestamp(date.getTime());
		return stamp;
	}

	/**
	 * returns the date that correspond to the given text DD-MM-YYYY
	 * 
	 * @param s
	 * @return
	 */
	public static Date toDate(String s) {
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		df.setLenient(false);
		Date d = null;
		try {
			d = df.parse(s);
		} catch (Exception e) {

		}
		return d;
	}

	public static Timestamp getCurrentTimeStamp() {
		return new Timestamp(new Date().getTime());
	}

	public static String getCurrentDate() {
		Date todayD = new Date();
		DateFormat shortDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
		return shortDateFormat.format(todayD);
	}

	/**
	 * returns the fx code if the proposal is ifx
	 * 
	 * @param code
	 * @return
	 */
	public static String getProposalCode(String code) {
		if (code != null && code.equalsIgnoreCase("ifx")) {
			return "fx";
		}
		return code;
	}

	/**
	 * return true if the length of the given string is <= the given maxLength
	 * 
	 * @param s
	 * @param maxLength
	 * @return
	 */
	public static boolean isStringLengthValid(String s, int maxLength) {
		return s == null || s.length() <= maxLength;
	}

	/**
	 * return true if the given string is a boolean
	 * 
	 * @param s
	 * @param canBeNull
	 * @return
	 */
	public static boolean isBoolean(String s, boolean canBeNull) {
		if (s == null)
			return canBeNull;
		try {
			Boolean b = Boolean.parseBoolean(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * returns the error message if the field's length is too long
	 * 
	 * @param className
	 * @param field
	 * @param maxLength
	 * @return
	 */
	public static String getMessageErrorMaxLength(String className, String field, int maxLength) {
		return className + ": The '" + field + "' field is too long! max. char. " + maxLength + ".";
	}

	/**
	 * returns the error message when a field is required
	 * 
	 * @param className
	 * @param field
	 * @return
	 */
	public static String getMessageRequiredField(String className, String field) {
		return className + ": The '" + field + "' field can not be null.";
	}

	/**
	 * returns the error message when a field is not a boolean
	 * 
	 * @param className
	 * @param field
	 * @return
	 */
	public static String getMessageBooleanField(String className, String field) {
		return className + ": The '" + field + "' field is not a boolean.";
	}

	/**
	 * returns true is the given string is in the given list
	 * 
	 * @param field
	 * @param listPredefinedValues
	 * @param canBeNull
	 * @return
	 */
	public static boolean isStringInPredefinedList(String field, String[] listPredefinedValues, boolean canBeNull) {
		if (field == null)
			return canBeNull;
		boolean isInList = false;
		for (int k = 0; k < listPredefinedValues.length; k++) {
			if (field.equals(listPredefinedValues[k])) {
				isInList = true;
				break;
			}
		}
		return isInList;
	}

	/**
	 * returns the error message if the field is not in the given list
	 * 
	 * @param className
	 * @param field
	 * @param listPredefinedValues
	 * @return
	 */
	public static String getMessageErrorPredefinedList(String className, String field, String[] listPredefinedValues) {
		String listToString = "";
		for (int k = 0; k < listPredefinedValues.length - 1; k++) {
			listToString += listPredefinedValues[k] + ", ";
		}
		listToString += listPredefinedValues[listPredefinedValues.length - 1];
		return className + ": The value of the '" + field + "' field must be in the following list: {" + listToString
				+ "}.";
	}

	/**
	 * returns true if the 2 given string are equal
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean matchString(String s1, String s2) {
		if (s1 == null && s2 == null)
			return true;
		if (s1 != null && s2 != null && s1.equalsIgnoreCase(s2)) {
			return true;
		}
		return false;
	}

	/**
	 * returns the filename of a filePath
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath) {
		if (filePath == null) {
			return null;
		}
		int id = filePath.lastIndexOf("/");
		int n = filePath.length();
		if (id != -1 && id != (n - 1) && id != (n - 2)) {
			return filePath.substring(id + 1);
		}
		return filePath;
	}

	public static Double formatDoubleInfinity(Double d) {
		if (d == null)
			return null;
		if (d.equals(Double.POSITIVE_INFINITY)) {
			return Double.MAX_VALUE;
		} else if (d.equals(Double.NEGATIVE_INFINITY)) {
			return Double.MIN_VALUE;
		} else if (d.equals(Double.NaN)) {
			return null;
		} else
			return d;
	}

	public static String getDirectoryFilename(String fullFilePath) {
		String directoryFilename = fullFilePath;
		try {
			File f = new File(fullFilePath);
			directoryFilename = getShortFilename(f.getParent());
		} catch (Exception e) {
		}

		return directoryFilename;
	}

	/**
	 * Prepare String for URL by replacing specific char by html Hex code
	 * 
	 * @param valueStr
	 * @return the formated String
	 */
	final public static String prepareStringForURL(String valueStr) {
		if (valueStr == null || valueStr.length() == 0) {
			return "";
		}
		valueStr = valueStr.trim();
		if (!valueStr.equals("") && !valueStr.equals("&nbsp;") && !(valueStr.toUpperCase()).equals("NULL")
				&& !valueStr.equals("NONE") && !valueStr.equals("0")) {

			valueStr = replaceInString(valueStr, " ", "%20");
			valueStr = replaceInString(valueStr, "<", "%3C");
			valueStr = replaceInString(valueStr, ">", "%3E");
			valueStr = replaceInString(valueStr, "\"", "%22");
			valueStr = replaceInString(valueStr, "'", "%60");
			valueStr = replaceInString(valueStr, "&", "%26");
		}
		return valueStr;
	}

	/**
	 * Replace a string by another in a given string
	 * 
	 * @param inputStr
	 *            the string to be parsed
	 * @param oldStr
	 *            the string to be replaced
	 * @param newStr
	 *            the string that replaces the old one
	 * @return the new string
	 */
	final public static String replaceInString(final String inputStr, final String oldStr, final String newStr) {
		int MaxBufLength = 5000;
		if ((inputStr == null) || inputStr.equals("")) {
			return inputStr;
		}
		if ((oldStr == null) || oldStr.equals("")) {
			return inputStr;
		}
		int LeninputStr = inputStr.length();
		int LenOldStrToRepl = oldStr.length();
		int LenNewStrToRepl = newStr.length();
		Vector AuxVect = new Vector(10);
		char[] RawChArr = inputStr.toCharArray();
		char[] OldReplChArr = oldStr.toCharArray();
		char[] NewReplChArr = newStr.toCharArray();
		char[] NewChArr = new char[MaxBufLength];
		int NewChCount = 0;
		int FreePassCount = 0;
		for (int i = 0; i < LeninputStr; i++) {
			if (FreePassCount == 0) {
				boolean StrMatches = true;
				if (i <= (LeninputStr - LenOldStrToRepl)) {
					for (int k = 0; k < LenOldStrToRepl; k++) {
						if (RawChArr[i + k] != OldReplChArr[k]) {
							StrMatches = false;
							break;
						}
					}
				} else {
					StrMatches = false;
				}
				if (NewChCount > (MaxBufLength - 3)) {
					String CurPartStr = String.valueOf(NewChArr, 0, NewChCount);
					AuxVect.addElement(CurPartStr);
					NewChArr = new char[MaxBufLength];
					NewChCount = 0;
				}
				if (StrMatches) {
					for (int j = 0; j < LenNewStrToRepl; j++) {
						NewChArr[NewChCount++] = NewReplChArr[j];
					}
					FreePassCount = LenOldStrToRepl - 1;
				} else {
					NewChArr[NewChCount++] = RawChArr[i];
				}
			} else {
				FreePassCount--;
			}
		}
		int AmOfParts = AuxVect.size();
		if (AmOfParts == 0) {
			return String.valueOf(NewChArr, 0, NewChCount);
		}
		if (NewChCount > 0) {
			AuxVect.addElement(String.valueOf(NewChArr, 0, NewChCount));
			AmOfParts++;
		}
		StringBuffer s = new StringBuffer(AmOfParts * MaxBufLength);
		for (int j = 0; j < AmOfParts; j++) {
			s.append((String) (AuxVect.elementAt(j)));
		}
		return s.toString();
	}
}
