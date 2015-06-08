package bcr.common.log;

import org.apache.log4j.Level;

/**
 * My own {@link org.apache.log4j.Level} for logging.
 * 
 * @author Jaikiran Pai
 * 
 */
public class StatTraceLevel extends Level {

	/**
	 * Value of my trace level. This value is lesser than {@link org.apache.log4j.Priority#DEBUG_INT} and higher than
	 * {@link org.apache.log4j.Level#TRACE_INT}
	 */
	// public static final int ISPYB_STAT_INT = DEBUG_INT - 10;
	public static final int ISPYB_STAT_INT = FATAL_INT + 10;

	/**
	 * {@link Level} representing my log level
	 */
	public static final Level ISPYB_STAT = new StatTraceLevel(ISPYB_STAT_INT, "ISPYB_STAT", 7);

	/**
	 * Constructor
	 * 
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	protected StatTraceLevel(int arg0, String arg1, int arg2) {
		super(arg0, arg1, arg2);

	}

	/**
	 * Checks whether <code>sArg</code> is "ISPYB_STAT" level. If yes then returns {@link MyTraceLevel#ISPYB_STAT}, else
	 * calls {@link MyTraceLevel#toLevel(String, Level)} passing it {@link Level#DEBUG} as the defaultLevel
	 * 
	 * @see Level#toLevel(java.lang.String)
	 * @see Level#toLevel(java.lang.String, org.apache.log4j.Level)
	 * 
	 */
	public static Level toLevel(String sArg) {
		if (sArg != null && sArg.toUpperCase().equals("ISPYB_STAT")) {
			return ISPYB_STAT;
		}
		return toLevel(sArg, Level.DEBUG);
	}

	/**
	 * Checks whether <code>val</code> is {@link MyTraceLevel#ISPYB_STAT_INT}. If yes then returns
	 * {@link MyTraceLevel#ISPYB_STAT}, else calls {@link MyTraceLevel#toLevel(int, Level)} passing it
	 * {@link Level#DEBUG} as the defaultLevel
	 * 
	 * @see Level#toLevel(int)
	 * @see Level#toLevel(int, org.apache.log4j.Level)
	 * 
	 */
	public static Level toLevel(int val) {
		if (val == ISPYB_STAT_INT) {
			return ISPYB_STAT;
		}
		return toLevel(val, Level.DEBUG);
	}

	/**
	 * Checks whether <code>val</code> is {@link MyTraceLevel#ISPYB_STAT_INT}. If yes then returns
	 * {@link MyTraceLevel#ISPYB_STAT}, else calls {@link Level#toLevel(int, org.apache.log4j.Level)}
	 * 
	 * @see Level#toLevel(int, org.apache.log4j.Level)
	 */
	public static Level toLevel(int val, Level defaultLevel) {
		if (val == ISPYB_STAT_INT) {
			return ISPYB_STAT;
		}
		return Level.toLevel(val, defaultLevel);
	}

	/**
	 * Checks whether <code>sArg</code> is "ISPYB_STAT" level. If yes then returns {@link MyTraceLevel#ISPYB_STAT}, else
	 * calls {@link Level#toLevel(java.lang.String, org.apache.log4j.Level)}
	 * 
	 * @see Level#toLevel(java.lang.String, org.apache.log4j.Level)
	 */
	public static Level toLevel(String sArg, Level defaultLevel) {
		if (sArg != null && sArg.toUpperCase().equals("ISPYB_STAT")) {
			return ISPYB_STAT;
		}
		return Level.toLevel(sArg, defaultLevel);
	}

}