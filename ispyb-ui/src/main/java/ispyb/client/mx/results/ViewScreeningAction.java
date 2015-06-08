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
 * ViewScreeningAction.java
 * 
 */
package ispyb.client.mx.results;

import fr.improve.struts.taglib.layout.util.FormUtils;
import ispyb.client.common.BreadCrumbsForm;
import ispyb.common.util.Constants;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.screening.Screening3Service;
import ispyb.server.mx.services.screening.ScreeningOutput3Service;
import ispyb.server.mx.vos.screening.Screening3VO;
import ispyb.server.mx.vos.screening.ScreeningInput3VO;
import ispyb.server.mx.vos.screening.ScreeningOutput3VO;
import ispyb.server.mx.vos.screening.ScreeningOutputLattice3VO;
import ispyb.server.mx.vos.screening.ScreeningRank3VO;
import ispyb.server.mx.vos.screening.ScreeningStrategy3VO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

/**
 * Class to handle DNA screening results from one datacollection
 * 
 * @struts.action name="viewResultsForm" path="/user/viewScreening" input="user.results.view.page" validate="false"
 *                parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 * @struts.action-forward name="success" path="user.results.viewScreening.page"
 * 
 * @struts.action-forward name="viewDNAImages" path="user.results.DNAImages.page"
 * 
 * 
 */
public class ViewScreeningAction extends DispatchAction {

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Screening3Service screeningService;

	private ScreeningOutput3Service screeningOutputService;

	ActionMessages errors = new ActionMessages();

	private final static Logger LOG = Logger.getLogger(ViewScreeningAction.class);

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws Exception {

		this.screeningService = (Screening3Service) ejb3ServiceLocator.getLocalService(Screening3Service.class);
		this.screeningOutputService = (ScreeningOutput3Service) ejb3ServiceLocator
				.getLocalService(ScreeningOutput3Service.class);

	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	/**
	 * To display all the Screening parameters associated to a screening. at the moment we suppose that 1 screening
	 * gives : 1 screeningInput, 1 screeningRank 1 screeningOuptput, 1 screeningOutputLattice and several
	 * screeningStrategys
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		try {

			String screeningIdst = request.getParameter(Constants.SCREENING_ID);
			Integer screeningId = new Integer(screeningIdst);

			ScreeningInput3VO silv = new ScreeningInput3VO();
			ScreeningRank3VO srlv = new ScreeningRank3VO();
			ScreeningStrategy3VO sslv = new ScreeningStrategy3VO();
			ScreeningOutputLattice3VO sollv = new ScreeningOutputLattice3VO();
			ScreeningOutput3VO sov = new ScreeningOutput3VO();

			ScreeningStrategy3VO[] screeningStrategyList;

			LOG.debug("display screening for id = " + screeningId);

			Screening3VO sv = screeningService.findByPk(screeningId, true, true);

			// if (sv.getScreeningInputsTab().length > 0)
			// silv = sv.getScreeningInputsTab()[0];
			if (sv.getScreeningRanksTab().length > 0)
				srlv = sv.getScreeningRanksTab()[0];

			if (sv.getScreeningOutputsTab().length > 0) {
				sov = sv.getScreeningOutputsTab()[0];
				sov = screeningOutputService.loadEager(sov);
			}

			// Populate form

			ViewResultsForm form = (ViewResultsForm) actForm;

			if (sov.getScreeningStrategysTab().length > 0) {

				sslv = sov.getScreeningStrategysTab()[0];
				screeningStrategyList = sov.getScreeningStrategysTab();
				form.setScreeningStrategyList(screeningStrategyList);
			}
			if (sov.getScreeningOutputLatticesTab().length > 0)
				sollv = sov.getScreeningOutputLatticesTab()[0];

			form.setScreeningInput(silv);
			form.setScreeningRank(srlv);
			form.setScreeningOutput(sov);
			form.setScreeningOutputLattice(sollv);
			form.setScreeningStrategy(sslv);

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);

			// fill the information bar

			BreadCrumbsForm.getIt(request).setSelectedScreening(sv);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.screening.view"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		} else
			return mapping.findForward("success");
	}

}
