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
 ******************************************************************************************************************************/

package ispyb.server.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;


/**
 * LoggerFormatter makes uniform the logs so software can be created to parse them automatically
 *
 */
public class LoggerFormatter {

	public enum Type {
	    START, 
	    END, 
	    ERROR 
	}
	
	public enum Package {
	    BIOSAXS_WS, 
	    BIOSAXS_WS_ERROR, 
	    
	    BIOSAXS_UI, 
	    BIOSAXS_UI_ERROR, 
	    
	    BIOSAXS_DB, 
	    BIOSAXS_MOBILE, 
	    
	    CRIMS_WS_ERROR, 
	    CRIMS_WS,
	    
	    BIOSAXS_WORKFLOW_ERROR, 
	    BIOSAXS_WORKFLOW, 
	    
	    ISPyB_API, 
	    ISPyB_API_ERROR,
	    
	    
	    ISPyB_MX_AUTOPROCESSING,
	    ISPyB_MX_AUTOPROCESSING_ERROR, 
	    
	    ISPyB_API_LOGIN,
	    ISPyB_API_LOGIN_ERROR,
	    
	    EM_WS
	    
	}

	
	private static void log(Logger log, Package pack, String methodName, Type type, long id, long time, long duration, String params, String comments){
		HashMap<String, String> message = new HashMap<String, String>();
		message.put("PACKAGE", pack.toString());
		message.put("METHOD", methodName);
		message.put("TYPE", type.toString());
		message.put("ID", String.valueOf(id));
		message.put("DURATION", String.valueOf(duration));
		message.put("PARAMS", String.valueOf(params));
		message.put("COMMENTS", String.valueOf(comments));
		message.put("LOGGER", "v2.0");
		message.put("DATE", GregorianCalendar.getInstance().getTime().toString());
		log.info(new Gson().toJson(message));
		
	}
	
	public static void log(Logger log, Package pack, String methodName, long id, long time,  String params){
		LoggerFormatter.log(log, pack, methodName, Type.START,  id, time, -1, params, "");
		
	}
	
	public static void log(Logger log, Package pack, String methodName, long id, long time, long duration){
		LoggerFormatter.log(log, pack, methodName, Type.END,  id, time, duration, "", "");
	}
	
	public static void log(Logger log, Package pack, String methodName,  long id, long time, String cause, Exception error){
		LoggerFormatter.log(log, pack, methodName, Type.ERROR, id, time, -1, cause, Arrays.toString(error.getStackTrace()).toString());
	}
	
	public static void log(Logger log, Package pack, String methodName, String text){
		List<String> message = new ArrayList<String>();
		message.add(pack.toString());
		message.add(methodName);
		message.add(text);
		log.info(message);
	}
}
