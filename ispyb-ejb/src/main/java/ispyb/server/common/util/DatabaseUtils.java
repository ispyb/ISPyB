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

package ispyb.server.common.util;

import ispyb.common.util.Constants;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * Utils for connection and statements in database
 * 
 */
public class DatabaseUtils {
	private static DataSource ds = null;

	private static final Logger LOG = Logger.getLogger(DatabaseUtils.class);

	/**
	 * close the connection for the specified resultSet, the specified statement and the specified connection
	 * 
	 * @param res
	 * @param stmt
	 * @param connection
	 */
	public final static void closeConnection(ResultSet res, Statement stmt, Connection connection) {
		try {
			if (res != null) {
				res.close();
			}
		} catch (Exception e) {
			LOG.error("Could not close result set - ignoring", e);
		}
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (Exception e) {
			LOG.error("Could not close statement - ignoring", e);
		}
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {
			LOG.error("Could not close connection - ignoring", e);
		}
	}

	/**
	 * Returns the connection
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		return getConnection("java:/comp/env/jdbc/experimental");
	}

	/**
	 * Returns the connection with the specified name
	 * 
	 * @param connection
	 * @return
	 */
	public static Connection getConnection(String connection) {
		try {
			if (ds == null) {
				Context initCtx = new InitialContext();
				// ds = (DataSource)initCtx.lookup("java:/comp/env/jdbc/experimental");
				// ds = (DataSource)initCtx.lookup("java:/mysql/ispyb_db");
				// ds = (DataSource)initCtx.lookup(Constants.getProperty("ISPyB.dbJndiName"));
				ds = (DataSource) initCtx.lookup(connection);
			}
			return ds.getConnection();
		} catch (Exception e) {
			try {
				Context initCtx = new InitialContext();
				ds = (DataSource) initCtx.lookup(Constants.getProperty("ISPyB.dbJndiName"));
				return ds.getConnection();
			} catch (Exception e2) {
				try {
					Context initCtx = new InitialContext();
					ds = (DataSource) initCtx.lookup("java:/comp/env/jdbc/experimental");
					return ds.getConnection();
				} catch (Exception e3) {
					LOG.error("Could not lookup datasource '" + Constants.getProperty("ISPyB.dbJndiName") + "' ", e);
					throw new RuntimeException("Could not lookup datasource", e);
				}
			}
		}
	}

	/**
	 * Returns the Integer value of the specified column
	 * 
	 * @param rs
	 * @param colName
	 * @return
	 * @throws SQLException
	 */
	public final static Integer getInteger(ResultSet rs, String colName) throws SQLException {
		int val = rs.getInt(colName);
		if (rs.wasNull()) {
			return null;
		}
		return new Integer(val);
	}

	/**
	 * Returns the String value of the specified column
	 * 
	 * @param rs
	 * @param colName
	 * @return
	 * @throws SQLException
	 */
	public final static String getString(ResultSet rs, String colName) throws SQLException {
		return rs.getString(colName);
	}

	/**
	 * Returns the Float value for the specified column
	 * 
	 * @param rs
	 * @param colName
	 * @return
	 * @throws SQLException
	 */
	public final static Float getFloat(ResultSet rs, String colName) throws SQLException {
		float val = rs.getFloat(colName);
		if (rs.wasNull()) {
			return null;
		}
		return new Float(val);
	}

	/**
	 * Returns the TimeStamp value for the specified column
	 * 
	 * @param rs
	 * @param colName
	 * @return
	 * @throws SQLException
	 */
	public final static Timestamp getTimestamp(ResultSet rs, String colName) throws SQLException {
		if (colName == null || colName.length() == 0 || rs == null) {
			throw new IllegalArgumentException("getTimestamp: one or more parameters are null");
		}
		Timestamp timestamp = rs.getTimestamp(colName);
		if (rs.wasNull()) {
			return null;
		}
		return timestamp;
	}

	/**
	 * Returns the double value for the specified column, null if not double
	 * 
	 * @param rs
	 * @param colName
	 * @return
	 * @throws SQLException
	 */
	public final static Double getDouble(ResultSet rs, String colName) throws SQLException {
		double val = rs.getDouble(colName);
		if (rs.wasNull()) {
			return null;
		}
		return new Double(val);
	}

	/**
	 * Returns the byte value for the specified column
	 * 
	 * @param rs
	 * @param colName
	 * @return
	 * @throws SQLException
	 */
	public final static Byte getByte(ResultSet rs, String colName) throws SQLException {
		byte val = rs.getByte(colName);
		if (rs.wasNull()) {
			return null;
		}
		return new Byte(val);
	}

	/**
	 * Returns a date value from a JDBC result set containing only the day part.
	 * 
	 * @param rs
	 *            the result set
	 * @param colName
	 *            the coloumn containg the date
	 * @return the date value with time part set to 00:00:00,000
	 * @throws SQLException
	 */
	public final static Date getDate(ResultSet rs, String colName) throws SQLException {
		if (colName == null || colName.length() == 0 || rs == null) {
			throw new IllegalArgumentException("getDate: one or more parameters are null");
		}
		Date res = convertSQLDate2Date(rs.getDate(colName));
		if (rs.wasNull()) {
			return null;
		}
		return res;
	}

	/**
	 * Converts a java.sql.Date object to a java.util.Date object.
	 * 
	 * @param date
	 *            the java.sql.Date object
	 * @return a java.util.Date object
	 */
	public static final Date convertSQLDate2Date(java.sql.Date date) {
		if (date == null) {
			return null;
		}
		return new Date(date.getTime());
	}

	/**
	 * Returns the Boolean value of the specified column
	 * 
	 * @param rs
	 * @param colName
	 * @return
	 * @throws SQLException
	 */
	public final static Boolean getBoolean(ResultSet rs, String colName) throws SQLException {
		boolean val = rs.getBoolean(colName);
		if (rs.wasNull()) {
			return null;
		}
		return new Boolean(val);
	}
}
