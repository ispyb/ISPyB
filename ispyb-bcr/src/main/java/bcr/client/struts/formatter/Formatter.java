/*
 * Formatter.java
 * 
 * Created on Mar 2, 2005
 *
 */
package bcr.client.struts.formatter;

import java.text.SimpleDateFormat;

import javax.servlet.jsp.PageContext;

import bcr.common.util.Constants;
import fr.improve.struts.taglib.layout.formatter.DispatchFormatter;

/**
 * Formatter to use in Struts-layout
 * 
 * <pre>
 * The < layout:write > and the < layout:collectionItem > tag supports
 * a special type attribute. When this attribute is set, those tags will ask the
 * formatter to format the value using the type specified.
 * </pre>
 * 
 * @author ricardo.leal@esrf.fr
 * @version 0.1
 */
public class Formatter extends DispatchFormatter {

	/**
	 * Overrides the standart date
	 * 
	 * @param in_date
	 * @param in_pageContext
	 * @return
	 */
	public String date(Object in_date, PageContext in_pageContext) {

		SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT);
		return formatter.format(in_date);
	}

	/**
	 * Formatter to display date and time
	 * 
	 * @param in_date
	 * @param in_pageContext
	 * @return
	 */
	public String dateTime(Object in_date, PageContext in_pageContext) {

		SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
		return formatter.format(in_date);
	}

}