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
 * Created on Nov 17, 2004 - 4:03:02 PM
 *
 * Ricardo LEAL
 * ESRF - The European Synchrotron Radiation Facility
 * BP220 - 38043 Grenoble Cedex - France
 * Phone: 00 33 (0)4 38 88 19 59
 * ricardo.leal@esrf.fr
 *
 */
package ispyb.client.common.util.log;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

/**
 * @author Ricardo Leal
 */

public class Log4StatLogger {

	private static Logger LOG;

	private static void initialize() {
		try {
			String name = Log4StatLogger.class.getName();
			LOG = Logger.getLogger(name);
		} catch (Exception e) {
			System.err.println("Error creating Logger: " + e.getMessage());
			e.printStackTrace(System.err);
		}
	}

	public static Logger getInstance() {
		if (LOG == null) {
			initialize();
		}
		return LOG;
	}

	public static void Log4Stat(String msg, String detail, String value) {
		if (LOG == null) {
			initialize();
		}
		if (LOG != null) {
			MDC.put("detail", detail);
			MDC.put("value", value);
			LOG.log(StatTraceLevel.ISPYB_STAT, msg);
			MDC.remove("detail");
			MDC.remove("value");
		}
	}

}
