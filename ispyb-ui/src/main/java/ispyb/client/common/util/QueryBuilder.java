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
 * QueryBuilder.java @author ludovic.launer@esrf.fr Jan 7, 2005
 */

package ispyb.client.common.util;

import ispyb.common.util.Constants;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

public class QueryBuilder
{
//______________________________________________________________________________________________________________________
//AddParameterToQuery
/**
 * @param prop
 *            The Property file to get the parameter from
 * @param queryParameterInProp
 *            Name of the paramater containing the query
 * @return The new parameter formated to used as an extra parameter in the
 *         WHERE clause
 */
//______________________________________________________________________________________________________________________
public static String AddParameterToQuery(Properties prop, String queryParameterInProp)
	{
	String query = prop.getProperty(queryParameterInProp);
	query = " " + query + " ";
	return query;
	}

//______________________________________________________________________________________________________________________
//FormatQueryWithParameters
/**
 * @param query
 *            The query to be modified
 * @args List of parameters to put in the query Populates the query with the
 *       ?x parameters for ejbQL
 */
//______________________________________________________________________________________________________________________
public static String FormatQueryWithParameters(String query, List args)
	{
	String modQuery = query;

	for (int i = 0; i < args.size(); i++)
		{
			modQuery = modQuery.replaceFirst("\\?(\\D|$)", "?" + (i + 1) + " ");
		}

	return modQuery;
	}

//______________________________________________________________________________________________________________________
//toDate
/**
 * @param A
 *            date following the dd/mm/yy format
 * @return A java.sql.Date corresponding to the parameter
 */
//______________________________________________________________________________________________________________________
public static java.sql.Date toDate(String ddmmyyDate) throws ParseException
	{
	try
		{
		DateFormat dateFormatIn = new SimpleDateFormat(Constants.DATE_FORMAT);
		DateFormat dateFormatOut = new SimpleDateFormat("yyyy-MM-dd");

		java.util.Date dateIn = dateFormatIn.parse(ddmmyyDate);
		String sDateOut = dateFormatOut.format(dateIn);

		return Date.valueOf(sDateOut);
		}
	catch (Exception e) {return null;}
	}

}
