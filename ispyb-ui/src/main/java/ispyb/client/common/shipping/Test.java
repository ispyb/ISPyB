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
/**
 * Test.java
 * @author ludovic.launer@esrf.fr
 * Nov 25, 2004
 */

package ispyb.client.common.shipping;

import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.services.shipping.Shipping3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * @struts.action name="viewDewarForm" path="/user/Test" type="ispyb.client.common.shipping.Test" validate="false"
 *                parameter="reqCode" scope="session"
 * 
 * 
 */

public class Test extends org.apache.struts.actions.DispatchAction {

	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Session3Service sessionService;

	private Shipping3Service shippingService;

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		try {
			this.sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
			this.shippingService = (Shipping3Service) ejb3ServiceLocator.getLocalService(Shipping3Service.class);

			// _session.updateProposalId(11,1);
			// shippingService.updateProposalId(742, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ActionForward("http://localhost/", true);
	}

}
