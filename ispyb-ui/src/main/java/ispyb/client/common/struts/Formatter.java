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
 * Formatter.java
 * 
 * Created on Mar 2, 2005
 *
 */
package ispyb.client.common.struts;

import ispyb.common.util.Constants;

import java.text.SimpleDateFormat;

import javax.servlet.jsp.PageContext;

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

	/**
	 * Formatter to display time
	 * 
	 * @param in_date
	 * @param in_pageContext
	 * @return
	 */
	public String time(Object in_date, PageContext in_pageContext) {

		SimpleDateFormat formatter = new SimpleDateFormat(Constants.TIME_FORMAT);
		return formatter.format(in_date);
	}

}
