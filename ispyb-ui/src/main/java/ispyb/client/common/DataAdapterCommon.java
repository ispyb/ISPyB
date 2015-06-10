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

package ispyb.client.common;

import ispyb.client.common.util.FileUtil;
import ispyb.common.util.Constants;
import ispyb.common.util.PathUtils;
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
 * This class contains all the common methods 
 * 
 */
public class DataAdapterCommon {
	
	//TODO factorise here methods common coming from BioSaxsDataAdapter and used by MX

	private final static Logger LOGGER = Logger.getLogger(DataAdapterCommon.class);


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
			filePath = PathUtils.getPath(filePath);
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
			filePath = PathUtils.getPath(filePath);
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
