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
package ispyb.client.common.shipping;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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
import org.apache.struts.actions.DispatchAction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ispyb.client.common.BreadCrumbsForm;
import ispyb.client.common.util.Confidentiality;
import ispyb.client.common.util.GSonUtils;
import ispyb.common.util.Constants;
import ispyb.common.util.StringUtils;
import ispyb.server.common.services.shipping.Container3Service;
import ispyb.server.common.services.shipping.Dewar3Service;
import ispyb.server.common.services.shipping.Shipping3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.server.mx.services.sample.BLSample3Service;
import ispyb.server.mx.services.sample.Protein3Service;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.Protein3VO;

/**
 *  
 * @struts.action name="fillShipmentForm" path="/user/fillShipmentAction" input="user.shipping.fillShipment.page"
 *                validate="false" parameter="reqCode" scope="request"
 * @struts.action-forward name="error" path="site.default.error.page"
 * @struts.action-forward name="fillShipmentAction" path="user.shipping.fillShipment.page"
 * 
 */
public class FillShipmentAction   extends DispatchAction{

	private final static Logger LOG = Logger.getLogger(FillShipmentAction.class);
	
	
	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
	private Protein3Service proteinService;
	private BLSample3Service sampleService;
	private Shipping3Service shippingService;
	private Dewar3Service dewarService;
	private Container3Service containerService;
	
	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws Exception {
		this.proteinService = (Protein3Service) ejb3ServiceLocator.getLocalService(Protein3Service.class);
		this.sampleService = (BLSample3Service) ejb3ServiceLocator.getLocalService(BLSample3Service.class);
		this.shippingService = (Shipping3Service) ejb3ServiceLocator.getLocalService(Shipping3Service.class);
		this.dewarService = (Dewar3Service) ejb3ServiceLocator.getLocalService(Dewar3Service.class);
		this.containerService = (Container3Service) ejb3ServiceLocator.getLocalService(Container3Service.class);
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
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {
		ActionMessages errors = new ActionMessages();
		
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}
		BreadCrumbsForm.getItClean(request);
		String shipId = request.getParameter(Constants.SHIPPING_ID);
		try{
			Integer shippingId = Integer.parseInt(shipId);
			// Confidentiality (check if proposalId matches)
			if (!Confidentiality.isAccessAllowedToShipping(request, shippingId)) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Access denied"));
				saveErrors(request, errors);
				return (mapping.findForward("error"));
			}
			Shipping3VO shippingVO = shippingService.findByPk(shippingId, false);
			BreadCrumbsForm.getIt(request).setSelectedShipping(shippingVO);
		}catch(NumberFormatException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		request.getSession().setAttribute(Constants.SHIPPING_ID, request.getParameter(Constants.SHIPPING_ID));
		
		return redirectPageFromRole(mapping, request);
	}
	
	private ActionForward redirectPageFromRole(ActionMapping mapping, HttpServletRequest request) {
		// redirection page according to the Role ------------------------

		//RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
		//String role = roleObject.getName();
		
		return mapping.findForward("fillShipmentAction");
	}
	
	@SuppressWarnings("unchecked")
	public ActionForward getInformationForShipment(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		List<String> errors = new ArrayList<String>();
		LOG.debug("getInformationForShipment");
		Integer shipmentId = null;
		if (request.getSession().getAttribute(Constants.SHIPPING_ID) != null){
			try{
				shipmentId = Integer.parseInt((String)request.getSession().getAttribute(Constants.SHIPPING_ID));
			}catch(NumberFormatException e){
				e.printStackTrace();
			}catch(ClassCastException e){
				try{
					shipmentId = ((Integer)request.getSession().getAttribute(Constants.SHIPPING_ID));
				}catch(Exception exp){
					exp.printStackTrace();
				}
			}
		}
		if (BreadCrumbsForm.getIt(request).getSelectedShipping() != null)
			shipmentId = BreadCrumbsForm.getIt(request).getSelectedShipping().getShippingId();
		LOG.debug("getInformationForShipment with shipment= "+shipmentId);
		
		List<String> listErrors = (List<String>) request.getSession().getAttribute(Constants.ERROR_LIST);
		if (listErrors != null)
			errors = listErrors;
		
		try {
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			// Confidentiality (check if proposalId matches)
			if (!Confidentiality.isAccessAllowedToShipping(request, shipmentId)) {
				errors.add("Access denied");
				BreadCrumbsForm.getItClean(request);
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("errors", errors);
				//data => Gson
				GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
				return null;
			}
			// retrieve the proteins
			List<String> proteinList = new ArrayList<String>();
			List<Crystal3VO> crystalValuesList = new ArrayList<Crystal3VO>();
			List<Protein3VO> proteinTab = proteinService.findByProposalId(proposalId, true, true);
			if (proteinTab != null){
				for (Iterator<Protein3VO> iterator = proteinTab.iterator(); iterator.hasNext();) {
					Protein3VO protein3vo = (Protein3VO) iterator.next();
					Crystal3VO[] crystals = protein3vo.getCrystals();
					// Retrieve Xtals for SpaceGroup
					for (int c = 0; c < crystals.length; c++) {
						Crystal3VO xtal = crystals[c];
						String acronym = xtal.getAcronymSpaceGroup();
						
						// Add to list (but don't duplicate)
						if (!proteinList.contains(acronym)) {
							proteinList.add(acronym);
							crystalValuesList.add(xtal);
						}
					}
				}
			}
			// retrieve the space group
			List<String> allowedSpaceGroups = (List<String>) request.getSession().getAttribute(Constants.ISPYB_ALLOWED_SPACEGROUPS_LIST);
			// experiment type
			List<String> experimentTypeList = Arrays.asList(Constants.LIST_EXPERIMENT_KIND);
			
			
			// Shipment
			Shipping3VO shippingVO = shippingService.findByPk(shipmentId, false);
			List<Dewar3VO> listDewar = dewarService.findByShippingId(shipmentId);
			List<Dewar> listOfDewar = new ArrayList<Dewar>();
			//List<List<ContainerWS3VO>> listOfContainer = new ArrayList<List<ContainerWS3VO>>();
			List<List<Container3VO>> listOfContainer = new ArrayList<List<Container3VO>>();
			List<List<List<SamplePuck>>> listOfSamples = new ArrayList<List<List<SamplePuck>>>();
			for (Iterator<Dewar3VO> iterator = listDewar.iterator(); iterator.hasNext();) {
				Dewar3VO dewarVO = (Dewar3VO) iterator.next();
				Dewar dewar = new Dewar(dewarVO);
				boolean warningIcon = false;
				String alertMessage = "Please print your component labels and stick them on your shipment components prior to sending (see instructions on first page).";
				String tooltip = "Print component labels";
				if (dewarVO.getSessionVO() == null){
					warningIcon = true;
					alertMessage = "Please print your component labels and stick them on your shipment components prior to sending (see instructions on first page).\n\nWARNING: your labels won\'t have Beamline, Experiment Date and Local Contact (you should setup an experiment for this dewar).";
					tooltip = "Print component labels (incomplete)";
				}else{ // session is not null
					String dewarStatus = dewarVO.getDewarStatus();
					if (dewarStatus == null){
						warningIcon = true;
						alertMessage = "Please print your component labels and stick them on your shipment components prior to sending (see instructions on first page).";
						tooltip = "You must print component labels";
					}else if (dewarStatus.equals(Constants.SHIPPING_STATUS_OPENED)){
						warningIcon = true;
						alertMessage = "Please print your component labels and stick them on your shipment components prior to sending (see instructions on first page).";
						tooltip = "You must print component labels";
					}else if (!dewarStatus.equals(Constants.SHIPPING_STATUS_SENT_TO_USER)){
						warningIcon = false;
						alertMessage = "Please print your component labels and stick them on your shipment components prior to sending (see instructions on first page).";
						tooltip = "You must print component labels";
					}
				}
				dewar.setWarningIcon(warningIcon);
				dewar.setAlertMessage(alertMessage);
				dewar.setTooltip(tooltip);
				listOfDewar.add(dewar);
				//List<ContainerWS3VO> listContainers = containerService.findWSByDewarId(dewarVO.getDewarId());
				List<Container3VO> listContainers = containerService.findByDewarId(dewarVO.getDewarId());				
				listOfContainer.add(listContainers);
				List<List<SamplePuck>> listS = new ArrayList<List<SamplePuck>>();
				for (Iterator<Container3VO> iterator2 = listContainers.iterator(); iterator2.hasNext();) {				
				//for (Iterator<ContainerWS3VO> iterator2 = listContainers.iterator(); iterator2.hasNext();) {
					Container3VO containerVO = (Container3VO) iterator2.next();
					List<BLSample3VO> listSamplesInContainer = sampleService.findByContainerId(containerVO.getContainerId());
					List<SamplePuck> listSamples = new ArrayList<SamplePuck>();
					if (listSamplesInContainer != null){
						for (Iterator<BLSample3VO> iterator3 = listSamplesInContainer.iterator(); iterator3.hasNext();) {
							Integer sampleId = ((BLSample3VO) iterator3.next()).getBlSampleId();
							BLSample3VO blSample3VO = sampleService.findByPk(sampleId, false, false, false);
							listSamples.add(new SamplePuck(blSample3VO));
						}
						listS.add(listSamples);
					}
				}
				listOfSamples.add(listS);
			}
			
			//
			Boolean canPaste = false;
			Object puckCodeToCopy = request.getSession().getAttribute(Constants.COPY_PUCK_CODE);
			Object listSamples = request.getSession().getAttribute(Constants.COPY_PUCK_SAMPLES);
			canPaste = puckCodeToCopy != null && listSamples != null;
						
			HashMap<String, Object> data = new HashMap<String, Object>();
			//context path
			data.put("contextPath", request.getContextPath());
			// protein list
			data.put("proteinList", proteinList);
			// spacegroup
			data.put("spaceGroupList", allowedSpaceGroups);
			// experiment type
			data.put("experimentTypeList", experimentTypeList);
			// crystalValuesList
			data.put("crystalValuesList", crystalValuesList);
			// shipping
			data.put("shippingId", shipmentId);
			// shippingNmae
			data.put("shippingName", shippingVO.getShippingName());
			// listDewar
			data.put("listDewar", listOfDewar);
			// listOfContainer
			data.put("listOfContainer", listOfContainer);
			// listOfSamples
			data.put("listOfSamples", listOfSamples);
			// canPaste
			data.put("canPaste", canPaste);
			//errors 
			data.put("errors", errors);
			request.getSession().setAttribute(Constants.CONTAINER_ID, null);
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
		}catch(Exception e){
			e.printStackTrace();
			errors.add(e.toString());
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("errors", errors);
			//data => Gson
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
			return null;
		}
		return null;
	}
	
	public ActionForward addDewar(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {
		List<String> errors = new ArrayList<String>();
		try{
			String dewarName = request.getParameter("dewarName");
			Dewar3VO dewar3vo = new Dewar3VO();
			Shipping3VO shippingVO  = BreadCrumbsForm.getIt(request).getSelectedShipping();
			dewar3vo.setDewarId(null);
			dewar3vo.setCode(dewarName);
			dewar3vo.setShippingVO(shippingVO);
			dewar3vo.setTimeStamp(new Timestamp(new Date().getTime()));
			dewar3vo.setBarCode(null);
			dewar3vo.setType("Dewar");
			dewar3vo.setDewarStatus(Constants.SHIPPING_STATUS_OPENED);
			
			Integer shippingId = shippingVO.getShippingId();
			List<Dewar3VO> dewars = this.dewarService.findFiltered(null, shippingId, null, dewarName.trim(), null, null,
					null, null, null, null, false, false);
			if (dewars.size() > 0) {
				errors.add("Label or Bar Code "+dewarName+" already exists for this shipment");
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("errors", errors);
				//data => Gson
				GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
				return null;
			}
			
			if (shippingVO.getFirstExp() != null)
				dewar3vo.setSessionVO(shippingVO.getFirstExp());
			// set default cost found in sending labcontact
			if (shippingVO.getSendingLabContactVO() != null){
				dewar3vo.setCustomsValue(shippingVO.getSendingLabContactVO().getDewarAvgCustomsValue());
				dewar3vo.setTransportValue(shippingVO.getSendingLabContactVO().getDewarAvgTransportValue());
			}
			
			// Create the dewar
			dewar3vo = dewarService.create(dewar3vo);
			
			return getInformationForShipment(mapping, actForm, request, response);
		}catch(Exception exp){
			exp.printStackTrace();
			errors.add(exp.toString());
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("errors", errors);
			//data => Gson
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
			return null;
		}
	}
	
	public ActionForward editDewar(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {
		List<String> errors = new ArrayList<String>();
		try{
			String dewarName = request.getParameter("dewarName");
			String dewarIdSt = request.getParameter("dewarId");
			Integer dewarId = null;
			Shipping3VO shippingVO  = BreadCrumbsForm.getIt(request).getSelectedShipping();
			try{
				dewarId = Integer.parseInt(dewarIdSt);
				Dewar3VO dewarVO = this.dewarService.findByPk(dewarId, false, false);
				List<Dewar3VO> dewars = this.dewarService.findFiltered(null, shippingVO.getShippingId(), null, dewarName.trim(), null, null,
						null, null, null, null, false, false);
				if (dewars.size() > 0) {
					errors.add("Label or Bar Code "+dewarName+" already exists for this shipment");
					HashMap<String, Object> data = new HashMap<String, Object>();
					data.put("errors", errors);
					//data => Gson
					GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
					return null;
				}
				dewarVO.setCode(dewarName);
				dewarService.update(dewarVO);
				return getInformationForShipment(mapping, actForm, request, response);
			}catch(NumberFormatException e){
				errors.add("Error with the dewar "+dewarIdSt);
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("errors", errors);
				//data => Gson
				GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
				return null;
			}
			
		}catch(Exception exp){
			exp.printStackTrace();
			errors.add(exp.toString());
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("errors", errors);
			//data => Gson
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
			return null;
		}
	}
	
	public ActionForward removeDewar(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {
		List<String> errors = new ArrayList<String>();
		try{
			String dewarIdSt = request.getParameter("dewarId");
			Integer dewarId = null;
			try{
				dewarId = Integer.parseInt(dewarIdSt);
				Dewar3VO dewarVO = this.dewarService.findByPk(dewarId, false, false);
				
				this.dewarService.deleteByPk(dewarVO.getDewarId());
				return getInformationForShipment(mapping, actForm, request, response);
			}catch(NumberFormatException e){
				errors.add("Error with the dewar "+dewarIdSt);
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("errors", errors);
				//data => Gson
				GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
				return null;
			}
			
		}catch(Exception exp){
			exp.printStackTrace();
			errors.add(exp.toString());
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("errors", errors);
			//data => Gson
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
			return null;
		}
	}
	
	
	public ActionForward addPuck(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {
		List<String> errors = new ArrayList<String>();
		try{
			String puckName = request.getParameter("puckName");
			if (puckName == null || puckName.isEmpty()){
				errors.add("Label or Bar Code is required");
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("errors", errors);
				//data => Gson
				GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
				return null;
			}
			Container3VO containerVO = new Container3VO();
			String dewarIdSt = request.getParameter("dewarId");
			Integer dewarId = null;
			try{
				dewarId = Integer.parseInt(dewarIdSt);
				Dewar3VO dewarVO = this.dewarService.findByPk(dewarId, false, false);
				
				List<Container3VO> containers = this.containerService.findByCode(dewarId, puckName);
				if (containers.size() > 0) {
					errors.add("Label or Bar Code "+puckName+" already exists for this dewar");
					HashMap<String, Object> data = new HashMap<String, Object>();
					data.put("errors", errors);
					//data => Gson
					GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
					return null;
				}
				
				containerVO.setCode(puckName);
				containerVO.setContainerType("Puck");
				containerVO.setCapacity(Constants.BASKET_SAMPLE_CAPACITY);
				containerVO.setTimeStamp(StringUtils.getCurrentTimeStamp());
				containerVO.setDewarVO(dewarVO);
				
				containerVO= containerService.create(containerVO);
				
				return getInformationForShipment(mapping, actForm, request, response);
			}catch(NumberFormatException e){
				errors.add("Error with the dewar "+dewarIdSt);
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("errors", errors);
				//data => Gson
				GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
				return null;
			}
		}catch(Exception exp){
			exp.printStackTrace();
			errors.add(exp.toString());
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("errors", errors);
			//data => Gson
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
			return null;
		}
	}
	
	public ActionForward editPuck(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {
		List<String> errors = new ArrayList<String>();
		try{
			String puckName = request.getParameter("puckName");
			String containerIdSt = request.getParameter("containerId");
			Integer containerId = null;
			try{
				containerId = Integer.parseInt(containerIdSt);
				Container3VO containerVO = this.containerService.findByPk(containerId, false);
				List<Container3VO> containers = this.containerService.findByCode(containerVO.getDewarVOId(), puckName.trim());
				if (containers.size() > 0) {
					errors.add("Label  "+puckName+" already exists for this dewar");
					HashMap<String, Object> data = new HashMap<String, Object>();
					data.put("errors", errors);
					//data => Gson
					GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
					return null;
				}
				containerVO.setCode(puckName);
				containerService.update(containerVO);
				return getInformationForShipment(mapping, actForm, request, response);
			}catch(NumberFormatException e){
				errors.add("Error with the container "+containerIdSt);
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("errors", errors);
				//data => Gson
				GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
				return null;
			}
			
		}catch(Exception exp){
			exp.printStackTrace();
			errors.add(exp.toString());
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("errors", errors);
			//data => Gson
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
			return null;
		}
	}
	
	public ActionForward removePuck(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {
		List<String> errors = new ArrayList<String>();
		try{
			String containerIdSt = request.getParameter("containerId");
			Integer containerId = null;
			try{
				containerId = Integer.parseInt(containerIdSt);
				Container3VO containerVO = this.containerService.findByPk(containerId, false);
				
				this.containerService.deleteByPk(containerVO.getContainerId());
				return getInformationForShipment(mapping, actForm, request, response);
			}catch(NumberFormatException e){
				errors.add("Error with the container "+containerIdSt);
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("errors", errors);
				//data => Gson
				GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
				return null;
			}
			
		}catch(Exception exp){
			exp.printStackTrace();
			errors.add(exp.toString());
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("errors", errors);
			//data => Gson
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public ActionForward savePuck(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {
		Object[] o = CreatePuckAction.saveContainer(mapping, actForm, request, response);
		List<String> errors = (List<String>) o[1];
		request.getSession().setAttribute(Constants.ERROR_LIST, errors);
		return getInformationForShipment(mapping, actForm, request, response);
	}
	
	public ActionForward copyPuck(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {
		List<String> errors = new ArrayList<String>();
		try{
			String containerIdSt = request.getParameter("containerId");
			Integer containerId = null;
			try{
				containerId = Integer.parseInt(containerIdSt);
				Container3VO containerVO = this.containerService.findByPk(containerId, false);
				String puckName = containerVO.getCode();
				
				String listSamples = request.getParameter("listSamples");
				Gson gson = GSonUtils.getGson("dd-MM-yyyy");
				Type mapType = new TypeToken<ArrayList<SamplePuck>>() {}.getType();
				ArrayList<SamplePuck> samples = gson.fromJson(listSamples,mapType);
				
				request.getSession().setAttribute(Constants.COPY_PUCK_CODE, puckName);
				request.getSession().setAttribute(Constants.COPY_PUCK_SAMPLES, samples);
				
				
				return null;
			}catch(NumberFormatException e){
				errors.add("Error with the container "+containerIdSt);
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("errors", errors);
				//data => Gson
				GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
				return null;
			}
			
		}catch(Exception exp){
			exp.printStackTrace();
			errors.add(exp.toString());
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("errors", errors);
			//data => Gson
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public ActionForward pastePuck(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {
		List<String> errors = new ArrayList<String>();
		try{
			String dewarIdSt = request.getParameter("dewarId");
			Integer dewarId = null;
			try{
				dewarId = Integer.parseInt(dewarIdSt);
				Dewar3VO dewarVO = this.dewarService.findByPk(dewarId, true, false);
				
				String puckCodeToCopy = (String) request.getSession().getAttribute(Constants.COPY_PUCK_CODE);
				ArrayList<SamplePuck> listSamples = (ArrayList<SamplePuck>) request.getSession().getAttribute(Constants.COPY_PUCK_SAMPLES);
				Integer nbCopy = (Integer) request.getSession().getAttribute(Constants.COPY_PUCK_NB);
				if (nbCopy == null){
					nbCopy = 1;
				}
				String t = Integer.toString(nbCopy);
				nbCopy = nbCopy+1;
				request.getSession().setAttribute(Constants.COPY_PUCK_NB, nbCopy);
				
				String newPuckCode = puckCodeToCopy+"_"+t;
				
				ArrayList<SamplePuck> newListSamples = new ArrayList<SamplePuck>();
				for (Iterator<SamplePuck> iterator = listSamples.iterator(); iterator.hasNext();) {
					SamplePuck samplePuck = (SamplePuck) iterator.next();
					SamplePuck newSamplePuck = new SamplePuck(null, samplePuck.getPosition(), samplePuck.getSampleName()+"_"+t,
							samplePuck.getProteinAcronym(), samplePuck.getSpaceGroup(), samplePuck.getPreObservedResolution(), 
							samplePuck.getNeededResolution(), samplePuck.getPreferredBeamDiameter(), samplePuck.getExperimentType(), 
							samplePuck.getNumberOfPositions(),samplePuck.getRadiationSensitivity(),samplePuck.getAimedCompleteness(),samplePuck.getAimedMultiplicity(),
							samplePuck.getUnitCellA(), samplePuck.getUnitCellB(), samplePuck.getUnitCellC(), 
							samplePuck.getUnitCellAlpha(), samplePuck.getUnitCellBeta(), samplePuck.getUnitCellGamma(), 
							samplePuck.getSmiles(), samplePuck.getComments(), samplePuck.getPinBarcode(), samplePuck.getMinOscWidth(), samplePuck.getAxisRange());
					
					newListSamples.add(newSamplePuck);
				}
				
				request.getSession().setAttribute("dewarId", dewarId);
				request.getSession().setAttribute("shippingId", dewarVO.getShippingVO().getShippingId());
				request.getSession().setAttribute("containerId",null );
				request.getSession().setAttribute("puckCode", newPuckCode);
				request.getSession().setAttribute("dewarCode",dewarVO.getCode() );
				request.getSession().setAttribute("shipmentName", dewarVO.getShippingVO().getShippingName());
				request.getSession().setAttribute("listSamples", newListSamples);
				
				
				Object[] o = CreatePuckAction.saveContainer(mapping, actForm, request, response);
				request.getSession().setAttribute(Constants.ERROR_LIST, (List<String>) o[1]);
				
				return getInformationForShipment(mapping, actForm, request, response);
			}catch(NumberFormatException e){
				errors.add("Error with the dewar "+dewarIdSt);
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("errors", errors);
				//data => Gson
				GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
				return null;
			}
			
		}catch(Exception exp){
			exp.printStackTrace();
			errors.add(exp.toString());
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("errors", errors);
			//data => Gson
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
			return null;
		}
	}
	
	public ActionForward cutPuck(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {
		List<String> errors = new ArrayList<String>();
		try{
			String containerIdSt = request.getParameter("containerId");
			Integer containerId = null;
			try{
				containerId = Integer.parseInt(containerIdSt);
				Container3VO containerVO = this.containerService.findByPk(containerId, false);
				String puckName = containerVO.getCode();
				
				String listSamples = request.getParameter("listSamples");
				Gson gson = GSonUtils.getGson("dd-MM-yyyy");
				Type mapType = new TypeToken<ArrayList<SamplePuck>>() {}.getType();
				ArrayList<SamplePuck> samples = gson.fromJson(listSamples,mapType);
				
				request.getSession().setAttribute(Constants.COPY_PUCK_CODE, puckName);
				request.getSession().setAttribute(Constants.COPY_PUCK_SAMPLES, samples);
				
				
				return null;
			}catch(NumberFormatException e){
				errors.add("Error with the container "+containerIdSt);
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("errors", errors);
				//data => Gson
				GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
				return null;
			}
			
		}catch(Exception exp){
			exp.printStackTrace();
			errors.add(exp.toString());
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("errors", errors);
			//data => Gson
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
			return null;
		}
	}
}
