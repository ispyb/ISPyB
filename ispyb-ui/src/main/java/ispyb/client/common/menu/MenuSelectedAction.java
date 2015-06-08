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
 * MenuSelectedAction
 * 
 * Ricardo LEAL
 * ESRF - The European Synchrotron Radiation Facility
 * BP220 - 38043 Grenoble Cedex - France
 * Phone: 00 33 (0)4 38 88 19 59
 * ricardo.leal@esrf.fr
 *
 */

package ispyb.client.common.menu;

import ispyb.common.util.Constants;

import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * This class handles all the request to change the menus activated. It receives 3 parameters: TOP_MENU_ID - Changes the
 * TOP menu activated LEFT_MENU_ID - Changes the LEFT menu activated TARGET_URL - URL page to be forwarded All
 * parameters besides these are forwarded to the target.
 * 
 * @struts.action path="/menuSelected" validate="false"
 * 
 * @author LEAL
 * @version
 */
public class MenuSelectedAction extends Action {

	private final static Logger LOG = Logger.getLogger(MenuSelectedAction.class);

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();

		// get parameters from URL
		String menuTopId = request.getParameter(Constants.TOP_MENU_ID);
		String menuLeftId = request.getParameter(Constants.LEFT_MENU_ID);
		String target = request.getParameter(Constants.TARGET_URL);

		// LOG.debug("Parameters: " + request.getParameterNames());

		try {
			if (menuTopId != null) {
				MenuContext menu = (MenuContext) request.getSession().getAttribute(Constants.MENU);
				menu.setActiveTopMenu((new Integer(menuTopId)).intValue());
				// invalidate selected top left menu if url is not empty
				if (target != null) {
					menu.setActiveLeftMenu(-1);
				}
				request.getSession().setAttribute(Constants.MENU, menu);

			}

			if (menuLeftId != null) {
				MenuContext menu = (MenuContext) request.getSession().getAttribute(Constants.MENU);
				menu.setActiveLeftMenu((new Integer(menuLeftId)).intValue());
				request.getSession().setAttribute(Constants.MENU, menu);
			}

			if (target == null) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "target == null"));
			} else {
				// create a target without the Menu parameters.
				target = createAction(request, target, new String[] { Constants.TOP_MENU_ID, Constants.LEFT_MENU_ID,
						Constants.TARGET_URL });
			}

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return new ActionForward(Constants.DEFAULT_ERROR_PAGE);
		}

		return new ActionForward(target, true);
	}

	/**
	 * create a new target with alll the parameters on the request, but without those on the exclude array.
	 * 
	 * @param req
	 * @param target
	 * @param exclude
	 * @return
	 * @throws Exception
	 */
	private String createAction(HttpServletRequest req, String target, String exclude[]) throws Exception {

		String url = target;

		Enumeration enumeration = req.getParameterNames();

		while (enumeration.hasMoreElements()) {
			String param = (String) enumeration.nextElement();
			boolean add = true;

			for (int i = 0; i < exclude.length; i++) {
				if (param.equals(exclude[i])) {
					add = false;
				}
			}

			if (add) {
				url += (url.indexOf("?") == -1 ? "?" : "&") + param + "="
						+ URLEncoder.encode(req.getParameter(param), Constants.DEFAULT_ENCODING);
			}
		}
		return url;
	}
}
