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
 * ViewExportSession.java
 * @author ludovic.launer@esrf.fr
 * Oct 18, 2005
 */

package ispyb.client.mx.collection;

import ispyb.common.util.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

/**
 * 
 * 
 * @struts.action name="viewExportSessionForm" path="/user/viewExportSession"
 *                input="user.collection.viewExportSession.page" validate="false" parameter="reqCode" scope="request"
 * @struts.action-forward name="error" path="site.default.error.page"
 * @struts.action-forward name="viewExportSession" path="user.collection.viewExportSession.page"
 * 
 */
public class ViewExportSession extends DispatchAction {

	/**
	 * To display all the Sessions belonging to a proposalId.
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward exportSession(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse reponse) {
		ActionMessages errors = new ActionMessages();

		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			Integer sessionId = new Integer(request.getParameter(Constants.SESSION_ID));
			viewExportSessionForm form = (viewExportSessionForm) actForm;
			// ---------------------------------------------------------------------------------------------------
			String webTarPath = request.getContextPath() + "/tmp/";
			String finalTarPath = request.getRealPath("/") + "/tmp/";

			// TODO check if this is used and rewrite it
			// SessionExporter sessionExporter = new SessionExporter(sessionId, finalTarPath);

			// Thread sessionExporterThread = new Thread(sessionExporter);
			// sessionExporterThread.start();

			form.setDumpLink(webTarPath + sessionId + ".tar");

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.collection.viewSession"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		} else
			return mapping.findForward("viewExportSession");
	}

}
