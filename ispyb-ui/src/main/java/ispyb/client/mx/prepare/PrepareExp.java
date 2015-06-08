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
import ispyb.client.common.BreadCrumbsForm;
import ispyb.client.common.shipping.GenericViewDewarForm;
import ispyb.common.util.Constants;
import ispyb.common.util.IspybDateUtils;
import ispyb.server.common.services.shipping.Container3Service;
import ispyb.server.common.services.shipping.Dewar3Service;
import ispyb.server.common.services.shipping.DewarTransportHistory3Service;
import ispyb.server.common.services.shipping.Shipping3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.DewarTransportHistory3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * @struts.action name="genericViewDewarForm" path="/user/prepareExp" type="ispyb.client.mx.prepare.PrepareExp" validate="false"
 *                parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="prepareExpPage" path="user.prepare.view.page"
 * @struts.action-forward name="selectDewarPage" path="user.prepare.selectDewar.page"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 */
public class PrepareExp extends org.apache.struts.actions.DispatchAction {

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Dewar3Service dewarService;

	private DewarTransportHistory3Service dewarTransportHistoryService;

	private Container3Service containerService;

	private Shipping3Service shippingService;

	private final static Logger LOG = Logger.getLogger(PrepareExp.class);

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws Exception {

		this.dewarService = (Dewar3Service) ejb3ServiceLocator.getLocalService(Dewar3Service.class);
		this.dewarTransportHistoryService = (DewarTransportHistory3Service) ejb3ServiceLocator
				.getLocalService(DewarTransportHistory3Service.class);
		this.containerService = (Container3Service) ejb3ServiceLocator.getLocalService(Container3Service.class);
		this.shippingService = (Shipping3Service) ejb3ServiceLocator.getLocalService(Shipping3Service.class);
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
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse in_reponse) {

		ActionMessages errors = new ActionMessages();
		try {
			// Clean BreadCrumbsForm
			BreadCrumbsForm.getItClean(request);
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		return mapping.findForward("prepareExpPage");
	}

	/**
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward selectDewar(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {

		ActionMessages errors = new ActionMessages();
		try {
			// Clean BreadCrumbsForm
			BreadCrumbsForm.getItClean(request);
			BreadCrumbsForm.getIt(request).setFromPage(Constants.PAGE_PREPARE);

			// Retrieve Attributes
			GenericViewDewarForm form = (GenericViewDewarForm) actForm;
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			String selectAll = request.getParameter("selectAll");
			if (selectAll == null) {
				selectAll = (String) request.getSession().getAttribute("selectAll");
			}
			boolean isAll = false;
			if (selectAll != null)
				isAll = (selectAll.equals("true"));
			String viewSelected = request.getParameter("viewSelected");
			if (viewSelected == null) {
				viewSelected = (String) request.getSession().getAttribute("viewSelected");
			}
			boolean isViewSelected = false;
			if (viewSelected != null)
				isViewSelected = (viewSelected.equals("true"));
			int nbDays = 60;
			List<Dewar3VO> listDewars = new ArrayList<Dewar3VO>();

			if (isAll) {
				nbDays = 0;
				if (isViewSelected) {
					listDewars = dewarService.findFiltered(proposalId, null, null, null, null, null, null,
							Constants.DEWAR_STATUS_PROCESS, null, true, true);
				} else {
					listDewars = dewarService.findFiltered(proposalId, null, null, null, null, null, null, null, null, true, true);
				}

			} else {
				Date today = new Date();
				Date startDay = IspybDateUtils.rollDateByDay(today, -nbDays);
				listDewars = dewarService.findFiltered(proposalId, null, null, null, null, startDay, today, null, null, true, true);

			}

			// Get an object list.
			// Retrieve information from DB

			// Remove sent dewar
			List<Dewar3VO> listDewarsOk = new ArrayList<Dewar3VO>();
			for (Iterator<Dewar3VO> itDewar = listDewars.iterator(); itDewar.hasNext();) {
				Dewar3VO dewar = itDewar.next();
				if (dewar.getDewarStatus() == null || !dewar.getDewarStatus().equals(Constants.DEWAR_STATUS_SENT_TO_USER)) {
					listDewarsOk.add(dewar);
				}
			}
			// Populate with Info
			// load history
			// and count nb samples
			List<Integer> listNbSamplesPerDewar = new ArrayList<Integer>();
			// int i=0;
			for (Iterator<Dewar3VO> iterator = listDewarsOk.iterator(); iterator.hasNext();) {
				Dewar3VO dewar3vo = iterator.next();
				// dewar3vo = dewarService.loadEager(dewar3vo);
				// listDewarsOk.set(i, dewar3vo);
				// i++;
				Integer nbSample = 0;
				nbSample = dewarService.countDewarSamples(dewar3vo.getDewarId());
				listNbSamplesPerDewar.add(nbSample);
			}
			// sort by selected and shipping creation date

			List<Dewar3VO> listDewarSort = new ArrayList<Dewar3VO>();
			for (Iterator<Dewar3VO> iterator = listDewarsOk.iterator(); iterator.hasNext();) {
				Dewar3VO dewar3vo = iterator.next();
				Date creationDate = dewar3vo.getShippingVO().getCreationDate();
				if (creationDate == null) {
					listDewarSort.add(dewar3vo);
				} else {
					int nbD = listDewarSort.size();
					int index = nbD;
					for (int i = 0; i < nbD; i++) {
						Dewar3VO dewar = listDewarSort.get(i);
						Date cd = dewar.getShippingVO().getCreationDate();
						if (cd == null) {
							index = i;
							break;
						} else {
							if (cd.before(creationDate)) {
								index = i;
								break;
							}
						}
					}
					if (index < nbD) {
						listDewarSort.add(index, dewar3vo);
					} else {
						listDewarSort.add(dewar3vo);
					}
				}
			}
			List<Dewar3VO> listDewarSort2 = new ArrayList<Dewar3VO>();
			for (Iterator<Dewar3VO> iterator = listDewarSort.iterator(); iterator.hasNext();) {
				Dewar3VO dewar3vo = iterator.next();
				String st = dewar3vo.getDewarStatus();
				if (st == null || !st.equals(Constants.DEWAR_STATUS_PROCESS)) {
					listDewarSort2.add(dewar3vo);
				} else {
					int nbD = listDewarSort2.size();
					int index = nbD;
					for (int i = 0; i < nbD; i++) {
						Dewar3VO dewar = listDewarSort2.get(i);
						String sti = dewar.getDewarStatus();
						if (sti == null || !sti.equals(Constants.DEWAR_STATUS_PROCESS)) {
							index = i;
							break;
						}
					}
					if (index < nbD) {
						listDewarSort2.add(index, dewar3vo);
					} else {
						listDewarSort2.add(dewar3vo);
					}
				}
			}
			listDewarsOk = listDewarSort2;
			form.setNbDays(nbDays);
			form.setListDewars(listDewarsOk);
			form.setListNbSamplesPerDewar(listNbSamplesPerDewar);
			form.setUserOrIndus(true);

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.EDIT_MODE);
			request.getSession().setAttribute("selectAll", Boolean.toString(isAll));
			request.getSession().setAttribute("viewSelected", Boolean.toString(isViewSelected));

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		return mapping.findForward("selectDewarPage");
	}

	/**
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward updateDewar(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		try {
			String actionName = request.getParameter("actionName");
			int dewarId = Integer.valueOf(request.getParameter("dewarId"));
			String status = "";
			if (actionName.equals("setdewar"))
				status = Constants.DEWAR_STATUS_PROCESS;
			else
				status = Constants.DEWAR_STATUS_AT_ESRF;
			LOG.debug("updateDewar: " + actionName + " " + dewarId);
			// Update Dewar status value
			Dewar3VO dewar = dewarService.findByPk(dewarId, false, false);
			dewar.setDewarStatus(status);
			dewarService.update(dewar);

			// Add Dewar event
			Timestamp dateTime = new java.sql.Timestamp((new java.util.Date()).getTime());
			DewarTransportHistory3VO newHistory = new DewarTransportHistory3VO();
			if (status.equals(Constants.DEWAR_STATUS_PROCESS))
				newHistory.setDewarStatus(Constants.DEWAR_STATUS_PROCESS);
			else
				newHistory.setDewarStatus(Constants.DEWAR_STATUS_UNPROCESS);
			newHistory.setStorageLocation("");
			newHistory.setArrivalDate(dateTime);
			newHistory.setDewarVO(dewar);
			dewarTransportHistoryService.create(newHistory);

			Shipping3VO shipping = dewar.getShippingVO();
			if (!shipping.getShippingStatus().equals(Constants.SHIPPING_STATUS_SENT_TO_USER)) {
				// Update Shipping status value
				shipping.setShippingStatus(status);
				shippingService.update(shipping);
			}

			// Get Dewar location
			String dewarLocation = null;
			if (dewar != null && dewar.getStorageLocation() != null) {
				dewarLocation = dewar.getStorageLocation();
			}

			// Update Containers value
			List<Container3VO> tab = containerService.findByDewarId(dewarId);
			for (int i = 0; i < tab.size(); i++) {

				// Set status
				Container3VO contVal = tab.get(i);
				contVal.setContainerStatus(status);
				// Set Beamline location
				String beamlineLocation = contVal.getBeamlineLocation();
				if (dewarLocation != null && !dewarLocation.equals("")) {
					beamlineLocation = dewarLocation;
				}
				contVal.setBeamlineLocation(beamlineLocation);
				containerService.update(contVal);
				LOG.debug("Update container " + contVal.getContainerId());
			}

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		return this.selectDewar(mapping, actForm, request, response);
	}

}
