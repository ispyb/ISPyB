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
package ispyb.client.mx.prepare;

import fr.improve.struts.taglib.layout.util.FormUtils;
import ispyb.client.common.util.OptionValue;
import ispyb.client.mx.container.ViewContainerForm;
import ispyb.common.util.Constants;
import ispyb.common.util.beamlines.EMBLBeamlineEnum;
import ispyb.common.util.beamlines.ESRFBeamlineEnum;
import ispyb.common.util.beamlines.MAXIVBeamlineEnum;
import ispyb.common.util.beamlines.SOLEILBeamlineEnum;
import ispyb.common.util.beamlines.ALBABeamlineEnum;
import ispyb.server.common.services.shipping.Container3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.server.mx.vos.sample.BLSample3VO;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * @struts.action name="viewContainerForm" path="/user/fillSampleChanger" type="ispyb.client.mx.prepare.FillSampleChanger"
 *                validate="false" parameter="reqCode" scope="request"
 * @struts.action-forward name="success" path="user.prepare.fillSC.page"
 * @struts.action-forward name="error" path="site.default.error.page"
 */
public class FillSampleChanger extends org.apache.struts.actions.DispatchAction {

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private final static Logger LOG = Logger.getLogger(FillSampleChanger.class);

	private Container3Service containerService;

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws Exception {

		this.containerService = (Container3Service) ejb3ServiceLocator.getLocalService(Container3Service.class);
	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	/**
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {

		ArrayList<Container3VO> listInfo = new ArrayList<Container3VO>();
		List<String> shipmentNameList = new ArrayList<String>();
		List<Date> creationDateList = new ArrayList<Date>();
		List<String> containerIdList = new ArrayList<String>();
		List<String> beamlineLocationList = new ArrayList<String>();
		List<String> sampleChangerLocationList = new ArrayList<String>();
		List<Integer> nbSampleList = new ArrayList<Integer>();

		ActionMessages errors = new ActionMessages();
		HttpSession session = request.getSession();

		try {

			// Retrieve Attributes
			ViewContainerForm form = (ViewContainerForm) actForm;
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);

			// Get an object list.

			LOG.debug("Search for processing containers");
			List<Container3VO> containerList = containerService.findByProposalIdAndStatus(proposalId,
					Constants.CONTAINER_STATUS_PROCESS);

			if (!containerList.isEmpty()) {

				for (Iterator<Container3VO> itContainer = containerList.iterator(); itContainer.hasNext();) {

					// Container record
					Container3VO container = itContainer.next();
					listInfo.add(container);
					containerIdList.add(container.getContainerId().toString());
					beamlineLocationList.add(container.getBeamlineLocation());
					sampleChangerLocationList.add(container.getSampleChangerLocation());

					// Additional information: Dewar and Shipping
					Dewar3VO dewar = container.getDewarVO();
					Shipping3VO shipping = dewar.getShippingVO();

					shipmentNameList.add(shipping.getShippingName());
					creationDateList.add(shipping.getCreationDate());
					container = containerService.findByPk(container.getContainerId(), true);
					List<BLSample3VO> sampleList = new ArrayList<BLSample3VO>(container.getSampleVOs());
					nbSampleList.add(sampleList.size());
				}

				// Build beamline list
				List<OptionValue> beamlineValueList = new ArrayList<OptionValue>();

				String[] beamlineList = Constants.BEAMLINE_LOCATION;
				if(Constants.SITE_IS_ALBA()){
					beamlineList = ALBABeamlineEnum.getBeamlineNamesInActivity();
				}
				if(Constants.SITE_IS_ESRF()){
					beamlineList = ESRFBeamlineEnum.getBeamlineNamesInActivity();
				}
				if(Constants.SITE_IS_EMBL()){
					beamlineList = EMBLBeamlineEnum.getBeamlineNamesInActivity();
				}
				if (Constants.SITE_IS_MAXIV()){
					beamlineList = MAXIVBeamlineEnum.getBeamlineNamesInActivity();
				}
				if(Constants.SITE_IS_SOLEIL()){
					beamlineList = SOLEILBeamlineEnum.getBeamlineNamesInActivity();
				}
				
				
				for (int i = 0; i < beamlineList.length; i++) {
					OptionValue option = new OptionValue(beamlineList[i], beamlineList[i]);
					beamlineValueList.add(option);
				}
				session.setAttribute("beamlineList", beamlineValueList);

				// Build sample changer location list
				List<OptionValue> scValueList = new ArrayList<OptionValue>();
				for (int i = 1; i <= Constants.LOCATIONS_IN_SC; i++) {
					OptionValue option = new OptionValue("" + i, "" + i);
					scValueList.add(option);
				}
				session.setAttribute("scList", scValueList);
			}

			// Populate form
			form.setContainersInfo(listInfo);
			form.setContainerIdList(containerIdList.toArray(new String[containerIdList.size()]));
			form.setShipmentNameList(shipmentNameList.toArray(new String[shipmentNameList.size()]));
			form.setCreationDateList(creationDateList.toArray(new Date[creationDateList.size()]));
			form.setNbSampleList(nbSampleList.toArray(new Integer[nbSampleList.size()]));
			form.setBeamlineLocationList(beamlineLocationList.toArray(new String[beamlineLocationList.size()]));
			form.setSampleChangerLocationList(sampleChangerLocationList.toArray(new String[sampleChangerLocationList
					.size()]));

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.EDIT_MODE);
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		return mapping.findForward("success");
	}

	/**
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward update_old(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {

		ActionMessages errors = new ActionMessages();
		try {
			// Retrieve Attributes
			ViewContainerForm form = (ViewContainerForm) actForm;
			Integer containerId = form.getTheContainerId();
			String scLocation = form.getSampleChangerLocation();
			String beamlineLocation = form.getBeamlineLocation();

			Container3VO clv = containerService.findByPk(containerId, false);
			clv.setSampleChangerLocation(scLocation);
			clv.setBeamlineLocation(beamlineLocation);
			containerService.update(clv);
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		return this.display(mapping, actForm, request, in_reponse);
	}

	/**
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward updateAll(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {

		ActionMessages errors = new ActionMessages();
		LOG.debug("updateAll");
		try {
			// Retrieve Attributes
			ViewContainerForm form = (ViewContainerForm) actForm;

			String[] containerIdList = form.getContainerIdList();
			String[] beamlineLocationList = form.getBeamlineLocationList();
			String[] sampleChangerLocationList = form.getSampleChangerLocationList();

			// Check pucks
			Set<String> puckSet = new HashSet<String>();
			for (int i = 0; i < containerIdList.length; i++) {
				// Get Container values
				String beamlineLocation = beamlineLocationList[i];
				String sampleChangerLocation = sampleChangerLocationList[i];
				String puckString = beamlineLocation + "_" + sampleChangerLocation;

				// Check if already exists
				if (puckSet.contains(puckString)) {
					LOG.debug("ERROR: puck " + sampleChangerLocation + " already used at " + beamlineLocation);
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Puck "
							+ sampleChangerLocation + " already used at " + beamlineLocation));
					saveErrors(request, errors);
					return mapping.findForward("error");
				}
				if (!beamlineLocation.equals("") && !sampleChangerLocation.equals("")) {
					puckSet.add(puckString);
				}
			}

			// Update database
			for (int i = 0; i < containerIdList.length; i++) {
				// Get Container values
				int containerId = Integer.valueOf(containerIdList[i]);
				String beamlineLocation = beamlineLocationList[i];
				String sampleChangerLocation = sampleChangerLocationList[i];
				LOG.debug("Updating container: " + containerId + "/" + beamlineLocation + "/" + sampleChangerLocation);

				// Update database
				Container3VO clv = containerService.findByPk(containerId, false);
				clv.setSampleChangerLocation(sampleChangerLocation);
				clv.setBeamlineLocation(beamlineLocation);
				containerService.update(clv);
			}
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}

		return this.display(mapping, actForm, request, in_reponse);
	}

}
