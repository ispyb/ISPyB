/*******************************************************************************
 * Copyright (c) 2004-2013
 * Contributors: L. Armanet, M. Camerlenghi, L. Cardonne, S. Delageniere,
 *               L. Duparchy, S. Ohlsson, P. Pascal, I. Schneider, S.Schulze,
 *               F. Torres
 * 
 * This file is part of the MIS tools package.
 * 
 * The MIS tools package is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * The MIS tools package is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with the MIS tools package.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package ispyb.client.common.util;

import ispyb.common.util.StringUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * This class contains useful methods for servlets and JSPs.
 */
public class MISServletUtils {

	private static final Logger LOG = Logger.getLogger(MISServletUtils.class);

	public static final String FILE_TYPE_PDF = "application/pdf";

	public static final String FILE_TYPE_EXCEL = "application/vnd.ms-excel";

	public static final int OUTPUT_BUFFER_SIZE = 8192;

	/**
	 * Creates a URL from an HttpServletRequest that can be used to reload exactly the same page again later.
	 * 
	 * @param req
	 * @param resp
	 * @return the URL to reload the same page
	 */
	public final static String getReloadURL(HttpServletRequest req, HttpServletResponse resp) {
		String queryString = req.getQueryString();
		if (queryString == null) {
			queryString = "";
		} else {
			queryString = "?" + queryString;
		}
		String reloadURL = req.getRequestURL().append(queryString).toString();
		reloadURL = resp.encodeURL(reloadURL);
		return reloadURL;
	}

	/**
	 * Creates a URL from an HttpServletRequest that can be used to reload the same page again later. Does not copy any
	 * query parameters.
	 * 
	 * @param req
	 * @param resp
	 * @return the URL to reload the same page
	 */
	public final static String getReloadURLWithoutQuery(HttpServletRequest req, HttpServletResponse resp) {
		String reloadURL = req.getRequestURL().toString();
		reloadURL = resp.encodeURL(reloadURL);
		return reloadURL;
	}

	/**
	 * Checks if the current session id is true.
	 * 
	 * @param req
	 *            the HttpServletRequest that the session is associated with
	 * @return
	 */
	public final static boolean isSessionValid(HttpServletRequest req) {
		if (req.getRequestedSessionId() != null && !req.isRequestedSessionIdValid()) {
			return false;
		}
		return true;
	}

	/**
	 * Prints all request parameters to a string.
	 * 
	 * @param req
	 * @return
	 */
	public final static String requestParamsToStr(ServletRequest req) {
		StringBuffer result = new StringBuffer("[");
		Enumeration enumeration = req.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String paramName = (String) enumeration.nextElement();
			result.append(paramName);
			result.append("=");
			result.append(req.getParameter(paramName));
			if (enumeration.hasMoreElements()) {
				result.append(", ");
			}
		}
		result.append("]");
		return result.toString();
	}

	/**
	 * Prints all request attributes to a string.
	 * 
	 * @param req
	 * @return
	 */
	public final static String requestAttribsToStr(ServletRequest req) {
		StringBuffer result = new StringBuffer("[");
		Enumeration enumeration = req.getAttributeNames();
		while (enumeration.hasMoreElements()) {
			String attrName = (String) enumeration.nextElement();
			result.append(attrName);
			result.append("=");
			result.append(req.getAttribute(attrName));
			if (enumeration.hasMoreElements()) {
				result.append(", ");
			}
		}
		result.append("]");
		return result.toString();
	}

	/**
	 * Send a stream to the browser
	 * 
	 * @param resp
	 *            the HttpServletResponse object
	 * @param byteArrayOutputStream
	 *            the stream to be sent to the browser
	 * @param contentType
	 *            the MIME type of the stream
	 * @deprecated
	 */
	@Deprecated
	public static final void sendToBrowser(HttpServletResponse resp, ByteArrayOutputStream byteArrayOutputStream,
			String contentType) {
		InputStream input = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
		Integer length = new Integer(byteArrayOutputStream.size());
		sendToBrowser(resp, input, length, contentType, null, false, false);
	}

	/**
	 * Send a file in form of a byte array to the browser.
	 * 
	 * @param resp
	 *            the HttpServletResponse object
	 * @param fileData
	 *            the byte array containing the file data
	 * @param contentType
	 *            the MIME type of the stream. Null accepted.
	 * @param fileName
	 *            default file name in the "save as" dialog. Null accepted.
	 * @param inLine
	 *            boolean. If true the file should be open inside the the browser (forcAttachment is then ignored).
	 * @param forceAttachment
	 *            boolean. If true the browser should be is forced to download the stream as a named file and shouldn't
	 *            open the file inline. (the MIMEType is set to an unrecognizable type )
	 */
	public static final void sendToBrowser(HttpServletResponse resp, byte[] fileData, String contentType,
			String fileName, boolean inLine, boolean forceAttachment) {
		InputStream input = new ByteArrayInputStream(fileData);
		sendToBrowser(resp, input, new Integer(fileData.length), contentType, fileName, inLine, forceAttachment);
	}

	/**
	 * Send a file to the browser
	 * 
	 * @param resp
	 *            the HttpServletResponse object
	 * @param input
	 *            the InputStream to be sent to the browser
	 * @param length
	 *            the length of the stream to be sent to the browser. Null accepted. Whenever the client, such as a
	 *            browser requests a page it establishes socket connection internally with the web server to get the
	 *            requested content. Client gets the page data with multiple connections depending on size of the data.
	 *            If the data is more, such as with multiple gifs then it needs to establish connection for multiple
	 *            times to get the data. The number of connections established will depend on default content length of
	 *            the header, and size of the content to be traversed from web server to the client. By increasing
	 *            content length, you can reduce traffic and increase the performance.
	 * @param contentType
	 *            the MIME type of the stream. Null accepted.
	 * @param fileName
	 *            default file name in the "save as" dialog. Null accepted.
	 * @param inline
	 *            boolean. If true the file should be open inside the the browser (forcAttachment is then ignored).
	 * @param forceAttachment
	 *            boolean. If true the browser should be is forced to download the stream as a named file and shouldn t
	 *            open the file inline. (the MIMEType is set to an unrecognizable type )
	 * @throws IOException
	 */

	public static final void sendToBrowser(HttpServletResponse resp, InputStream input, Integer length,
			String contentType, String fileName, boolean inLine, boolean forceAttachment) {
		OutputStream out = null;
		try {
			fileName = StringUtils.prepareStringForURL(fileName);
			resp.reset();
			resp.setHeader("Pragma", "public");
			resp.setHeader("Cache-Control", "max-age=0");/* solves IE blank page issue */
			resp.setHeader("Content-Transfer-Encoding", "binary");
			resp.setContentType(contentType);
			if (inLine)
				resp.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");
			else {
				resp.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
				if (forceAttachment)
					resp.setContentType("application/x-download");
			}
			if (length != null)
				resp.setContentLength(length.intValue());
			out = resp.getOutputStream();
			BufferedInputStream bufferedInputStream = new BufferedInputStream(input);
			byte buffer[] = new byte[OUTPUT_BUFFER_SIZE];
			int read;
			while ((read = bufferedInputStream.read(buffer, 0, buffer.length)) != -1)
				out.write(buffer, 0, read);

		} catch (IOException ioe) {
			LOG.error("sendToBrowser error: Request maybe aborted by client", ioe);
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
					out = null;
				}
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				LOG.error("closing stream error", e);
			}
		}
	}

	/**
	 * Send a file to the browser
	 * 
	 * @param resp
	 *            the HttpServletResponse object
	 * @param file
	 *            the File to be sent to the browser
	 * @param contentType
	 *            the MIME type of the stream. Null accepted.
	 * @param fileName
	 *            the default file name in the "save as" dialog. Null accepted.
	 * @param inline
	 *            boolean. If true the file should be open inside the the browser (forcAttachment is then ignored).
	 * @param forceAttachment
	 *            boolean. If true the browser should be is forced to download the stream as a named file and shouldn't
	 *            open the file inline. (the MIMEType is set to an unrecognizable type )
	 * @throws IOException
	 */
	public static final void sendToBrowser(HttpServletResponse resp, File file, String contentType, String fileName,
			boolean inLine, boolean forceAttachment) {
		try {
			if (fileName == null)
				fileName = file.getName();
			InputStream input = new FileInputStream(file);
			Integer length = new Integer(new Long(file.length()).intValue());
			sendToBrowser(resp, input, length, contentType, fileName, inLine, forceAttachment);
		} catch (IOException ioe) {
			LOG.error("sendToBrowser: Request aborted by client ... (" + ioe.getMessage() + ")");
		}
	}

	/**
	 * Send a stream to the browser
	 * 
	 * @param resp
	 *            the HttpServletResponse object
	 * @param fileName
	 *            the name of the file to be sent to the browser
	 * @param inLine
	 *            if true the browser is force to download the stream as a named file
	 * @param forceAttachment
	 *            boolean. If true the browser should be is forced to download the stream as a named file and shouldn't
	 *            open the file inline. (the MIMEType is set to an unrecognizable type)
	 * @throws IOException
	 */
	public static final void sendToBrowser(HttpServletResponse resp, String path, String contentType, boolean inLine,
			boolean forceAttachment) {
		File file = new File(path);
		sendToBrowser(resp, file, contentType, null, inLine, forceAttachment);
	}

	/**
	 * Get the MIME type of a file using the servlet context
	 * 
	 * @param servlet
	 *            the HttpServlet object
	 * @param fileName
	 *            the name of the file to be parsed
	 * @return the file MIME type or null
	 * @throws Exception
	 */
	public static final String getMimeType(HttpServlet servlet, String fileName) {
		try {
			return servlet.getServletContext().getMimeType(fileName);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Return a parameter or an attribute with the given name. If a parameter is found it is returned, only if no
	 * parameter can be found an attriubte is searched.
	 * 
	 * @param request
	 * @param paramName
	 * @return
	 */
	public final static String getParamOrAttr(HttpServletRequest request, String paramName) {
		if (request == null)
			return null;
		if (request.getParameter(paramName) != null)
			return request.getParameter(paramName);
		return (String) request.getAttribute(paramName);
	}

	/**
	 * Set a request value to the session. The request value is searched in the parameters and attributes.
	 * 
	 * @param request
	 * @param paramName
	 */
	public final static void setRequestValueToSession(HttpServletRequest request, String paramName) {
		if (request == null)
			return;
		if (request.getParameter(paramName) != null) {
			HttpSession session = request.getSession();
			session.setAttribute(paramName, request.getParameter(paramName));
		} else if (request.getAttribute(paramName) != null) {
			HttpSession session = request.getSession();
			session.setAttribute(paramName, request.getAttribute(paramName));
		}
	}

	/**
	 * Set a session value to the request as attribute.
	 * 
	 * @param request
	 * @param paramName
	 * @return
	 */
	public final static HttpServletRequest setSessionValueToRequest(HttpServletRequest request, String paramName) {
		return setSessionValueToRequest(request, paramName, false);
	}

	public final static HttpServletRequest setSessionValueToRequest(HttpServletRequest request, String paramName,
			boolean remove) {
		if (request == null)
			return null;
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute(paramName) != null) {
			request.setAttribute(paramName, session.getAttribute(paramName));
			if (remove)
				session.removeAttribute(paramName);
		}
		return request;

	}

	/**
	 * Returns the browser information of a request
	 * 
	 * @param req
	 * @return
	 */
	public final static String getUserAgent(HttpServletRequest req) {
		return req.getHeader("User-Agent");
	}

	/**
	 * Checks if the browser is IE
	 * 
	 * @param req
	 * @return
	 */
	public final static boolean isIE(HttpServletRequest req) {
		return (getUserAgent(req).toLowerCase().indexOf("msie") > -1);
	}

	/**
	 * Return the major version number if the requesting browser is IE otherwise null.
	 * 
	 * @param req
	 * @return
	 */
	public final static Integer getIEVersion(HttpServletRequest req) {
		if (!isIE(req))
			return null;
		String userAgent = getUserAgent(req).toLowerCase();
		String str = userAgent.substring(userAgent.indexOf("msie") + 5);
		String majorVersion = str.substring(0, str.indexOf("."));
		try {
			return Integer.valueOf(majorVersion);
		} catch (NumberFormatException e) {
			LOG.error("getIEVersion: version string does not represent a valid number", e);
			return null;
		}
	}
}