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
package ispyb.client.common.admin;

import ispyb.client.common.util.GSonUtils;
import ispyb.client.security.roles.RoleDO;
import ispyb.common.util.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 * 
 * @struts.action name="dataConfidentialityForm" path="/user/dataConfidentialityAction" input="manager.admin.dataConfidentiality.page"
 *                validate="false" parameter="reqCode" scope="request"
 * @struts.action-forward name="error" path="site.default.error.page"
 * @struts.action-forward name="dataConfidentialityActionManager" path="manager.admin.dataConfidentiality.page"
 * @struts.action-forward name="dataConfidentialityActionLocalContact" path="localcontact.admin.dataConfidentiality.page"
 * @struts.action-forward name="dataConfidentialityActionBlom" path="blom.admin.dataConfidentiality.page"
 * 
 */
public class ViewDataConfidentialityAction extends DispatchAction {

	private final static Logger LOG = Logger.getLogger(ViewDataConfidentialityAction.class);

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws Exception {
	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	/**
	 * display all references
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse in_reponse) {
		RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
		String role = roleObject.getName();

		if (role.equals(Constants.ROLE_MANAGER)) {
			return mapping.findForward("dataConfidentialityActionManager");
		} else if (role.equals(Constants.ROLE_LOCALCONTACT)) {
			return mapping.findForward("dataConfidentialityActionLocalContact");
		} else if (role.equals(Constants.ROLE_BLOM)) {
			return mapping.findForward("dataConfidentialityActionBlom");
		}
		return null;
	}

	public ActionForward getDataConfidentialityLog(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		List<String> errors = new ArrayList<String>();
		LOG.debug("getDataConfidentialityLog");

		try {
			String allS = request.getParameter("all");
			String perMonthS = request.getParameter("perMonth");
			String monthS = request.getParameter("month");
			String yearS = request.getParameter("year");
			String perDateS = request.getParameter("perDate");
			String logDateS = request.getParameter("logDate");

			boolean all = Boolean.parseBoolean(allS);
			boolean perMonth = Boolean.parseBoolean(perMonthS);
			boolean perDate = Boolean.parseBoolean(perDateS);

			Date logDate = null;
			if (perDate && logDateS != null && !logDateS.equals("")) {
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				df.setLenient(false);
				try {
					logDate = df.parse(logDateS);
				} catch (Exception e) {

				}
			}

			String year = null;
			String month = null;
			if (perMonth && monthS != null && yearS != null) {
				try {
					int m = Integer.parseInt(monthS);
					if (m > 0 && m < 13) {
						month = Integer.toString(m);
						if (month.length() < 2) {
							month = "0" + month;
						}
					}
					int y = Integer.parseInt(yearS);
					year = Integer.toString(y);
				} catch (NumberFormatException e) {

				}
			}

			String dataConfidentialityLog = "";
			try {

				try {
					String dateFilter = "0";
					if (perDate && logDate != null) {
						SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
						dateFilter = dt.format(logDate);
					}
					if (perMonth && month != null && year != null) {
						dateFilter = year + "-" + month;
					}
					//TODO put back when no runtime error
					// call data protection tool
					HttpClient client = new DefaultHttpClient();
					List<NameValuePair> qparams = new ArrayList<NameValuePair>();
					qparams.add(new BasicNameValuePair("op", "protect"));
					qparams.add(new BasicNameValuePair("tm", dateFilter));
					URI uri = URIUtils.createURI("http", "dch.esrf.fr", -1, "/logview-do.php",
							URLEncodedUtils.format(qparams, "UTF-8"), null);
					HttpPost post = new HttpPost(uri);
					HttpResponse httpRes = client.execute(post);
					BufferedReader rd = new BufferedReader(new InputStreamReader(httpRes.getEntity().getContent()));
					String line = "";
					dataConfidentialityLog = "";
					while ((line = rd.readLine()) != null) {
						System.out.println(line);
						dataConfidentialityLog += line + " \n";
					}
				} catch (Exception e) {
					//
					LOG.error("ERROR: getDataConfidentialityLog ");
					throw e;
				}
			} catch (Exception exp) {
				exp.printStackTrace();
			}

			// --- Write Image to output ---
			response.setContentType("text/html");
			try {
				ServletOutputStream out = response.getOutputStream();
				out.write(dataConfidentialityLog.getBytes());
				out.flush();
				out.close();
			} catch (IOException ioe) {
				LOG.error("Unable to write to outputStream. (IOException)");
				ioe.printStackTrace();
				return mapping.findForward("error");
			}

		} catch (Exception e) {
			e.printStackTrace();
			errors.add(e.toString());
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("errors", errors);
			// data => Gson
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
			return null;
		}
		return null;
	}

}
