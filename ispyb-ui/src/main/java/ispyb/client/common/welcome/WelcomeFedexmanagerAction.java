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
package ispyb.client.common.welcome;

import ispyb.common.util.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * @struts.action path="/fedexmanager/welcomeFedexmanager"
 * 
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 * @struts.action-forward name="success" path="fedexmanager.welcome.page"
 * 
 */
public class WelcomeFedexmanagerAction extends Action {
	private final Logger LOG = Logger.getLogger(WelcomeFedexmanagerAction.class);
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		LOG.debug("retrieve IFX proposals attached to MXPress service");

		String proposalCode = Constants.PROPOSAL_CODE_FX;
		String proposalNumber = Constants.PROPOSAL_NUMBER_MANAGER;
		request.getSession().setAttribute(Constants.PROPOSAL_CODE, proposalCode);
		request.getSession().setAttribute(Constants.PROPOSAL_NUMBER, proposalNumber);

		return mapping.findForward("success");
	}

}
