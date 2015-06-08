/*
 * QueryBuilder.java @author ludovic.launer@esrf.fr Jan 7, 2005
 */

package bcr.client.util;


import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

import bcr.common.util.Constants;

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