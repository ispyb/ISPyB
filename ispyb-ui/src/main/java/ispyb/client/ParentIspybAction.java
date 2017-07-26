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
package ispyb.client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import ispyb.common.util.Constants;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;

/**
 * @struts.action-forward name="error" path="site.default.error.page"
 * @struts.action-forward name="noPermission" path="site.permission.error.page"
 * 
 */

public class ParentIspybAction extends DispatchAction {

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private final static Logger LOG = Logger.getLogger(ParentIspybAction.class);

	
	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		return super.execute(mapping, form, request, response);
	}

	public ActionForward accessDeniedPage(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	final HttpServletResponse response) {
		
		LOG.info(Constants.ACCESS_DENIED);
		return (mapping.findForward("noPermission"));
	}
	
}
