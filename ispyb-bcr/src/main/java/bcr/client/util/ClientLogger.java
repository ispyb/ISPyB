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
package bcr.client.util;

import org.apache.log4j.Logger;

/**
 * @author Ricardo Leal
 */

public class ClientLogger {

	private static Logger log;

	private static void initialize() {
		try {
			String name = ClientLogger.class.getName();
			log = Logger.getLogger(name);
		} catch (Exception e) {
			System.err.println("Error creating Logger: " + e.getMessage());
			e.printStackTrace(System.err);
		}
	}

	public static Logger getInstance() {
		if (log == null) {
			initialize();
		}
		return log;
	}

}
