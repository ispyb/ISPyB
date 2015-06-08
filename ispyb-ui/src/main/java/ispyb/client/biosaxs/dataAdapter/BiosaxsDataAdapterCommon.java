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

package ispyb.client.biosaxs.dataAdapter;

import ispyb.client.common.util.FileUtil;
import ispyb.common.util.Constants;
import ispyb.server.common.util.LoggerFormatter;
import ispyb.server.common.util.LoggerFormatter.Package;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This class contains all the common methods used on BiosaxsDataAdapterAction
 * 
 */
public class BiosaxsDataAdapterCommon {

	private final static Logger LOGGER = Logger.getLogger(BiosaxsDataAdapterAction.class);

	/**
	 * Format the error message to send back to the client. The format is JSON
	 * 
	 * @param exp
	 * @return
	 */
	public static String getErrorMessage(Exception exp) {
		HashMap<String, String> error = new HashMap<String, String>();
		StringWriter sw = new StringWriter();
		exp.printStackTrace(new PrintWriter(sw));
		error.put("trace", sw.toString());
		if (exp.getMessage() != null) {
			error.put("message", exp.getMessage());
		}
		return new Gson().toJson(error);
	};

	/**
	 * Logs using LoggerFormatter the start of the methods
	 * 
	 * @param methodName
	 * @param request
	 *            : needed because in the request we obtain all the parameters coming from the client
	 * @return the start time in milliseconds which it is used as identifier for the logs
	 */
	public static long logMethod(String methodName, HttpServletRequest request) {
		return logMethod(methodName, request, LoggerFormatter.Package.BIOSAXS_UI);
	}

	/**
	 * @param string
	 * @param request
	 * @param biosaxsMobile
	 * @return
	 */
	public static long logMethod(String methodName, HttpServletRequest request, Package packageFormatter) {
		// LOGGER.info("--------------");
		// LOGGER.info(methodName);
		long start = System.currentTimeMillis();
		Enumeration<String> enumeration = request.getParameterNames();
		Integer proposalId = getProposalId(request);
		List<String> params = new ArrayList<String>();
		params.add("proposalId=" + String.valueOf(proposalId));
		while (enumeration.hasMoreElements()) {
			String parameterName = enumeration.nextElement();
			params.add(parameterName + "=" + request.getParameter(parameterName));
		}
		LOGGER.info(params);

		LoggerFormatter.log(LOGGER, packageFormatter, methodName, start, start, params.toString());
		return start;
	}

	/**
	 * Form a comma separated string returns a list of integers
	 * 
	 * @param commaSeparated
	 * @return
	 */
	public static List<Integer> parseToInteger(String commaSeparated) {
		if (commaSeparated != null){
			List<String> macromoleculeIdsString = Arrays.asList(commaSeparated.split(","));
			ArrayList<Integer> macromolecules = new ArrayList<Integer>();
			if (!macromoleculeIdsString.equals("")) {
				for (String string : macromoleculeIdsString) {
					try {
						/** TODO: Reg expresion on this **/
						macromolecules.add(Integer.valueOf(string));
					} catch (Exception e) {
						/** No parseable value ***/
						System.out.println(e.getMessage());
					}
				}
			}
			return macromolecules;
		}
		return null;
	}

	/**
	 * @param parameter
	 * @return
	 */
	public static List<String> parseToString(String commaSeparated) {
		List<String> macromoleculeIdsString = Arrays.asList(commaSeparated.split(","));
		return macromoleculeIdsString;
	}

	/**
	 * For logging and unexpected error when there is not id
	 * 
	 * @param methodName
	 * @param error
	 */
	public static void logError(String methodName, Exception error) {
		error.printStackTrace();
		LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_UI_ERROR, methodName, -1, System.currentTimeMillis(),
				error.getMessage(), error);
	}

	/**
	 * 
	 * @param methodName
	 * @param id
	 *            , the id should be the same as the method has been logged at the starting
	 * @param error
	 */
	public static void logError(String methodName, long id, Exception error) {
		error.printStackTrace();
		LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_UI_ERROR, methodName, id, System.currentTimeMillis(),
				error.getMessage(), error);
	}

	/**
	 * @return the Gson object from the Gsonbuilder to serialize/deserialize all the objects
	 */
	public static Gson getGson(String dateFormat) {
		return new GsonBuilder().setDateFormat(dateFormat).excludeFieldsWithModifiers(Modifier.PRIVATE).serializeNulls().create();
	}

	public static Gson getGson() {
		return new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).serializeNulls().create();
	}

	public static Gson getGson(ExclusionStrategy strategies) {
		return new GsonBuilder().setExclusionStrategies(strategies).excludeFieldsWithModifiers(Modifier.PRIVATE).serializeNulls()
				.create();
	}

	public static Gson getGson(String dateFormat, ExclusionStrategy strategies) {
		return new GsonBuilder().setExclusionStrategies(strategies).setDateFormat(dateFormat)
				.excludeFieldsWithModifiers(Modifier.PRIVATE).serializeNulls().create();
	}

	/**
	 * Obtains the proposal Id (integer) stored in the session. The sessionId never travels to the client
	 * 
	 * @param request
	 * @return
	 */
	public static Integer getProposalId(HttpServletRequest request) {
		return (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
	}

	/**
	 * Check if in the session we have the experiments or it is necessary to retrieve them again
	 * 
	 * @param request
	 * @param scope
	 * @return
	 */
	// public static List<Experiment3VO> getExperimentsOnSession(HttpServletRequest request, ExperimentScope scope){
	// if (request.getSession().getAttribute("biosaxssessionticket") != null){
	// BiosaxsSessionTicket biosaxsSessionTicket = BiosaxsDataAdapterCommon.getTicket(request);
	// if (biosaxsSessionTicket.getExperiments().get(scope) != null){
	// return biosaxsSessionTicket.getExperiments().get(scope);
	// }
	// }
	// return null;
	// }

	/**
	 * Obtains the ticket in session if it exists or null
	 * 
	 * @param request
	 * @return
	 */
	// public static BiosaxsSessionTicket getTicket(HttpServletRequest request){
	// return (BiosaxsSessionTicket)request.getSession().getAttribute("biosaxssessionticket");
	// }
	//
	// public static void setTicket(HttpServletRequest request, BiosaxsSessionTicket biosaxsSessionTicket){
	// request.getSession().setAttribute("biosaxssessionticket", biosaxsSessionTicket);
	// }

	/**
	 * Because the list of experiments and the scope is importact factor for the performance, they are in the session
	 * 
	 * @param request
	 * @return
	 */
	// public static void setExperimentsOnSession(HttpServletRequest request, ExperimentScope scope, List<Experiment3VO> experiments){
	// if (BiosaxsDataAdapterCommon.getTicket(request) == null){
	// BiosaxsDataAdapterCommon.setTicket(request, new BiosaxsSessionTicket());
	// }
	//
	// BiosaxsSessionTicket ticket = BiosaxsDataAdapterCommon.getTicket(request);
	// ticket.setExperiments(scope, experiments);
	// BiosaxsDataAdapterCommon.setTicket(request, ticket);
	// }

	/**
	 * This method creates the headers in order to send back the response
	 * 
	 * @param response
	 * @param content
	 */
	public static void sendResponseToclient(HttpServletResponse response, String content) {
		response.setContentType("application/json; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		try {
			response.getWriter().flush();
			response.getWriter().write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method creates the headers in order to send back the response
	 * 
	 * @param response
	 * @param content
	 */
	public static void sendTextToclient(HttpServletResponse response, String content) {
		response.setContentType("text/plain; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		try {
			response.getWriter().flush();
			response.getWriter().write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param filePath
	 * @param response
	 */
	public static ActionForward sendImageToClient(String filePath, HttpServletResponse response) {
		try {
			filePath = BiosaxsActions.checkFilePathForDevelopment(filePath);
			if (new File(filePath).exists()) {
				byte[] imageBytes = FileUtil.readBytes(filePath);
				response.setContentLength(imageBytes.length);
				ServletOutputStream out = response.getOutputStream();
				response.setContentType("image/png");
				out.write(imageBytes);
				out.close();
			}
		} catch (Exception exp) {
			response.setStatus(500);
			return null;
		}
		return null;
	}

	/**
	 * @param filePath
	 * @param response
	 */
	public static ActionForward sendFileToClient(String filePath, HttpServletResponse response, String contentType) {
		try {
			filePath = BiosaxsActions.checkFilePathForDevelopment(filePath);
			File file = new File(filePath);
			if (file.exists()) {
				byte[] imageBytes = FileUtil.readBytes(filePath);
				response.setContentLength(imageBytes.length);
				ServletOutputStream out = response.getOutputStream();
				response.setHeader("Content-disposition", "attachment; filename=" + file.getName());
				response.setContentType(contentType);
				out.write(imageBytes);
				out.close();
			}
		} catch (Exception exp) {
			response.setStatus(500);
			return null;
		}
		return null;
	}
}
