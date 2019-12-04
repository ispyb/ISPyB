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
 * @author ricardo.leal@esrf.fr
 * 
 *         Jun 3, 2005
 * 
 */
public class StringUtils {

	private static final String image_pattern = "<IMG SRC=\"([^\"]*)\"";

	private static final String href_pattern = "<A HREF=.*image.*<IMG SRC=\"((.*)_small[.](.*).*)\"></A>";

	private static final String index_pattern = "<A HREF=.*index[.]html.*A>|<A HREF=.*log.*A>";
	
	private static final String name_pattern = "^[a-zA-Z0-9\\-_]+$";


	/**
	 * Returns User Office code from Proposal code
	 * 
	 * @param proposalCode
	 * @return
	 */
	public static String getUoCode(String proposalCode) {

		String uoCode = proposalCode.toUpperCase();
		if (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_BM14))
			uoCode = "14-U";
		if (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_BM14xxxx))
			uoCode = "14-U";
		if (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_BM161))
			uoCode = "16-01";
		if (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_BM162))
			uoCode = "16-02";

		return uoCode;
	}

	/**
	 * Returns Proposal code from User Office code
	 * 
	 * @param uoCode
	 * @return
	 */
	public static String getProposalCode(String uoCode, String proposalNumber) {

		String proposalCode = uoCode;
		// BM14: the number shoulb be a real number(not a string)
		Integer propNumberInt = 0;
		try {
			propNumberInt = Integer.parseInt(proposalNumber);
		} catch (NumberFormatException e) {

		}
		if ((uoCode.endsWith("14-U") || uoCode.endsWith("14-u")) && propNumberInt < 1000)
			proposalCode = "BM14U";
		if ((uoCode.endsWith("14-U") || uoCode.endsWith("14-u")) && propNumberInt >= 1000)
			proposalCode = "BM14";
		if (uoCode.endsWith("16-01"))
			proposalCode = "BM161";
		if (uoCode.endsWith("16-02"))
			proposalCode = "BM162";

		return proposalCode;
	}

	public static String addEDNAPageStyle(String orig) {
		String tmp = orig;

		String extraStyle = "\n" + "/* Default style for tables */\n" + "\n"
				+ "	div.edna table,td,th { border-style: none;\n" + "	color: black;\n" + "	border-spacing: 0;\n"
				+ "	border-collapse: collapse;\n" + "	padding: 2px;\n" + "	font-size: 90%;\n"
				+ "	empty-cells: show; }\n" + "	div.edna td,th { border-style: solid;\n" + "	border-width: 2px;\n"
				+ "	border-color: darkgray;\n" + "	padding-left: 8px;\n" + "	padding-right: 8px; }\n"
				+ "	div.edna th { background-color: gray;\n" + "	color: white;\n" + "	font-weight: bold;\n"
				+ "	border-top-style: none;\n" + "	border-bottom-style: none; }\n" + "-->\n";

		String stylePattern = "(.*?<style type=\"text/css\">.*?)-->.*?(</style>.*?)(<body.*?>)(.*)</body>(.*)";
		Pattern patternStyle = Pattern.compile(stylePattern, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

		Matcher matcherStyle = patternStyle.matcher(tmp);
		String tmp2 = matcherStyle.replaceAll("$1" + extraStyle + "$2" + "$3" + "<div class = 'edna'>" + "$4"
				+ "</div></body>" + "$5");

		return tmp2;
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
	 * GetProposalNumberAndCode
	 * 
	 * @param userPrincipal
	 * @param proposalCode
	 * @param prefix
	 * @param proposalNumber
	 */
	public static ArrayList<String> GetProposalNumberAndCode(String userPrincipal) {
		userPrincipal = userPrincipal.trim();
		ArrayList<String> authenticationInfo = new ArrayList<String>();

		int start = 0;
		int end = 0;

		String proposalCode = userPrincipal.toLowerCase().substring(0, 2);
		String prefix = userPrincipal.toLowerCase().substring(0, 3);
		String proposalNumber = userPrincipal.toLowerCase().substring(2);

		// Case of MXpress users IFXnn
		String strif = Constants.LOGIN_PREFIX_IFX;
		if (proposalCode.equals(strif)) {
			start = 1;
			end = 3;
		}

		// Case of EHTPX users EHTPXnn
		String streh = Constants.LOGIN_PREFIX_EHTPX;
		if (proposalCode.equals(streh)) {
			start = 0;
			end = 5;
		}

		// Case of OPID users opidxxx
		String opi = Constants.LOGIN_PREFIX_OPID;
		if (prefix.equals(opi)) {
			start = 0;
			end = 4;
		}

		// Case of OPD users opd14 for BM14
		String opd = Constants.LOGIN_PREFIX_OPD;
		if (prefix.equals(opd)) {
			start = 0;
			end = 3;
		}

		// Case of MXIHR inhouse users
		String mxi = Constants.LOGIN_PREFIX_MXIHR;
		if (prefix.equals(mxi)) {
			start = 0;
			end = 5;
		}

		// Case of BM14U users BM14Uxxx and BM16-1 users BM161xxx
		String strbm = Constants.LOGIN_PREFIX_BM;
		if (proposalCode.equals(strbm)) {
			start = 0;
			end = 5;
		}

		if (end != 0) // Make sure Code + Number modified only if solution found
		{
			proposalCode = userPrincipal.substring(start, end);
			proposalNumber = userPrincipal.substring(end);
		}

		// BM14XXXX - Because of LDAP 8 characters truncation ("U" has been removed from account name)
		if (proposalCode.toUpperCase().startsWith("BM14") && !proposalCode.toUpperCase().startsWith("BM14U")) {
			// Need to add "U" because of SMIS update
			// proposalCode = userPrincipal.substring(0, 4)+"u";
			// proposalNumber = userPrincipal.substring(4);
			// 090306 Use BM14 + XXXX
			proposalCode = userPrincipal.substring(0, 4);
			proposalNumber = userPrincipal.substring(4);
		}

		// bm30***
		if (proposalCode.toUpperCase().startsWith("BM30")) {
			proposalCode = userPrincipal.substring(0, 4);
			proposalNumber = userPrincipal.substring(4);
		}

		// --- Store results to return ---
		authenticationInfo.add(0, proposalCode.toLowerCase());
		authenticationInfo.add(1, prefix.toLowerCase());
		authenticationInfo.add(2, proposalNumber.toLowerCase());

		return authenticationInfo;
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
	 * returns true if the 2 given string are equal and not null
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean matchStringNotNull(String s1, String s2) {
		if (s1 == null || s2 == null)
			return false;
		else 
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
	
	/**
	 * Returns the name of the proposal in lowercase 
	 * 
	 * @return
	 */
	public static String getProposalName(String proposalCode, String proposalNumber) {

		String proposalName = "";
		
		if (proposalCode == null && proposalNumber == null)
			return "noproposal";
			
		if (proposalCode != null) {
			proposalName = proposalCode;
			if (proposalNumber != null){
				proposalName = proposalName + proposalNumber;
			}
		} else {
			proposalName = proposalNumber;
		}

		return proposalName;
	}
	
	public static boolean isStringOkForName(String name) {
		
		if (name.matches(name_pattern))
			return true;
		else
			return false;
	}

	public static String breakString(String original, int interval) {
		String formatted = "";
		String separator = "\\n\\r";

		for (int i = 0; i < original.length(); i++) {
			if (i % interval == 0 && i > 0) {
				formatted += separator;
			}
			formatted += original.substring(i, i+1);
		}

		return formatted;
	}
}

