package ispyb.ws.soap.em;

import org.apache.log4j.Logger;

import ispyb.server.common.util.LoggerFormatter;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;

public class EMDataCollection {
	
	protected Logger log = Logger.getLogger(ToolsForEMDataCollection.class);

//	protected long now;
//
//	protected final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
//	
//	/** Loggers **/
//	protected long logInit(String methodName) {
//		log.info("-----------------------");
//		this.now = System.currentTimeMillis();
//		log.info(methodName.toUpperCase());
//		LoggerFormatter.log(log, LoggerFormatter.Package.EM_WS, methodName, System.currentTimeMillis(),
//				System.currentTimeMillis(), "");
//		return this.now;
//	}
//
//	/** Loggers **/
//	protected long logInit(String methodName, String params) {
//		log.info("-----------------------");
//		this.now = System.currentTimeMillis();
//		log.info(methodName.toUpperCase());
//		LoggerFormatter.log(log, LoggerFormatter.Package.EM_WS, methodName, System.currentTimeMillis(),
//				System.currentTimeMillis(), params);
//		return this.now;
//	}
//
//	protected long logInit(String methodName, LoggerFormatter.Package packageName) {
//		log.info("-----------------------");
//		this.now = System.currentTimeMillis();
//		log.info(methodName.toUpperCase());
//		LoggerFormatter.log(log, packageName, methodName, System.currentTimeMillis(), System.currentTimeMillis(), "");
//		return this.now;
//
//	}
//
//	protected void logFinish(String methodName, long id) {
//		log.debug("### [" + methodName.toUpperCase() + "] Execution time was " + (System.currentTimeMillis() - this.now) + " ms.");
//		LoggerFormatter.log(log, LoggerFormatter.Package.EM_WS, methodName, id, System.currentTimeMillis(),
//				System.currentTimeMillis() - this.now);
//
//	}
}
