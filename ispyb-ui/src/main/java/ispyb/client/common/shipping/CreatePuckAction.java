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

import ispyb.client.common.util.GSonUtils;
import ispyb.common.util.Constants;
import ispyb.common.util.StringUtils;
import ispyb.server.common.services.proposals.LabContact3Service;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.services.shipping.Container3Service;
import ispyb.server.common.services.shipping.Dewar3Service;
import ispyb.server.common.services.shipping.Shipping3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.LabContact3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.server.mx.services.sample.BLSample3Service;
import ispyb.server.mx.services.sample.Crystal3Service;
import ispyb.server.mx.services.sample.DiffractionPlan3Service;
import ispyb.server.mx.services.sample.Protein3Service;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.DiffractionPlan3VO;
import ispyb.server.mx.vos.sample.Protein3VO;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @struts.action name="createPuckForm" path="/user/createPuckAction" input="user.shipping.createPuck.page"
 *                validate="false" parameter="reqCode" scope="request"
 * @struts.action-forward name="error" path="site.default.error.page"
 * @struts.action-forward name="createPuckAction" path="user.shipping.createPuck.page"
 * 
 */
public class CreatePuckAction extends DispatchAction {

	private final static Logger LOG = Logger.getLogger(CreatePuckAction.class);

	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Protein3Service proteinService;

	private Dewar3Service dewarService;

	private Container3Service containerService;

	private BLSample3Service sampleService;

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws Exception {
		this.proteinService = (Protein3Service) ejb3ServiceLocator.getLocalService(Protein3Service.class);
		this.dewarService = (Dewar3Service) ejb3ServiceLocator.getLocalService(Dewar3Service.class);
		this.containerService = (Container3Service) ejb3ServiceLocator.getLocalService(Container3Service.class);
		this.sampleService = (BLSample3Service) ejb3ServiceLocator.getLocalService(BLSample3Service.class);

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
		// if (request.getParameter(Constants.CONTAINER_ID) != null)
		request.getSession().setAttribute(Constants.CONTAINER_ID, request.getParameter(Constants.CONTAINER_ID));
		// if (request.getParameter(Constants.DEWAR_ID)!= null){
		request.getSession().setAttribute(Constants.DEWAR_ID, request.getParameter(Constants.DEWAR_ID));
		// }
		return redirectPageFromRole(mapping, request);
	}

	private ActionForward redirectPageFromRole(ActionMapping mapping, HttpServletRequest request) {
		// redirection page according to the Role ------------------------

		// RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
		// String role = roleObject.getName();

		return mapping.findForward("createPuckAction");
	}

	@SuppressWarnings("unchecked")
	public void getInformationForPuck(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		List<String> errors = new ArrayList<String>();
		LOG.debug("getInformationForPuck");
		Integer containerId = null;
		if (request.getSession().getAttribute(Constants.CONTAINER_ID) != null) {
			try {
				containerId = Integer.parseInt((String) request.getSession().getAttribute(Constants.CONTAINER_ID));
			} catch (NumberFormatException e) {

			}
		}
		Integer dewarId = null;
		if (request.getSession().getAttribute(Constants.DEWAR_ID) != null) {
			try {
				dewarId = Integer.parseInt((String) request.getSession().getAttribute(Constants.DEWAR_ID));
			} catch (NumberFormatException e) {

			}
		}

		try {
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			// retrieve the proteins
			List<String> proteinList = new ArrayList<String>();
			List<Crystal3VO> crystalValuesList = new ArrayList<Crystal3VO>();
			List<Protein3VO> proteinTab = proteinService.findByProposalId(proposalId, true, true);
			if (proteinTab != null) {
				for (Iterator<Protein3VO> iterator = proteinTab.iterator(); iterator.hasNext();) {
					Protein3VO protein3vo = iterator.next();
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
			List<String> allowedSpaceGroups = (List<String>) request.getSession().getAttribute(
					Constants.ISPYB_ALLOWED_SPACEGROUPS_LIST);
			;
			// experiment type
			List<String> experimentTypeList = Arrays.asList(Constants.LIST_EXPERIMENT_KIND);

			List<SamplePuck> listSamples = new ArrayList<SamplePuck>();
			if (containerId != null) {
				List<BLSample3VO> listS = sampleService.findByContainerId(containerId);
				if (listS != null) {
					for (Iterator<BLSample3VO> iterator = listS.iterator(); iterator.hasNext();) {
						BLSample3VO blSample3VO = iterator.next();
						listSamples.add(new SamplePuck(blSample3VO));
					}
				}
			}
			//
			HashMap<String, String> puckInfo = new HashMap<String, String>();
			String todayDate = "";
			Date today = new Date();
			DateFormat format = new SimpleDateFormat("yyyyMMdd");
			todayDate = format.format(today);
			String shipmentName = "ship-" + todayDate;
			String dewarCode = "Dewar1";
			String puckCode = "Puck1";
			Integer shippingId = null;
			if (dewarId != null) {
				Dewar3VO dewar = dewarService.findByPk(dewarId, true, false);
				dewarCode = dewar.getCode();
				if (dewar != null) {
					if (dewar.getShippingVO() != null) {
						shipmentName = dewar.getShippingVO().getShippingName();
						shippingId = dewar.getShippingVO().getShippingId();
					}
					if (dewar.getContainerVOs() != null) {
						int nb = dewar.getContainerVOs().size();
						if (nb > 0) {
							puckCode = "Puck" + (nb + 1);
						}
					}
				}
			}
			if (containerId != null) {
				Container3VO container = containerService.findByPk(containerId, false);
				if (container != null) {
					puckCode = container.getCode();
					if (container.getDewarVO() != null) {
						dewarCode = container.getDewarVO().getCode();
						if (container.getDewarVO().getShippingVO() != null) {
							shipmentName = container.getDewarVO().getShippingVO().getShippingName();
							shippingId = container.getDewarVO().getShippingVO().getShippingId();
						}
					}
				}
			}

			puckInfo.put("shipmentName", shipmentName);
			puckInfo.put("dewarCode", dewarCode);
			puckInfo.put("puckCode", puckCode);
			puckInfo.put("shippingId", "" + shippingId);
			puckInfo.put("dewarId", "" + dewarId);
			puckInfo.put("containerId", "" + containerId);

			HashMap<String, Object> data = new HashMap<String, Object>();
			// context path
			data.put("contextPath", request.getContextPath());
			// protein list
			data.put("proteinList", proteinList);
			// spacegroup
			data.put("spaceGroupList", allowedSpaceGroups);
			// experiment type
			data.put("experimentTypeList", experimentTypeList);
			// crystalValuesList
			data.put("crystalValuesList", crystalValuesList);
			// listSamples
			data.put("listSamples", listSamples);
			// puck information
			data.put("puckInfo", puckInfo);
			// canPaste
			data.put("canPaste", false);
			request.getSession().setAttribute(Constants.CONTAINER_ID, null);
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
		} catch (Exception e) {
			e.printStackTrace();
			errors.add(e.toString());
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("errors", errors);
			// data => Gson
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
			return;
		}
	}

	@SuppressWarnings("unchecked")
	public void savePuck(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		List<String> errors = new ArrayList<String>();
		LOG.debug("savePuck");
		try {
			Object[] o = saveContainer(mapping, actForm, request, response);
			Container3VO containerVO = (Container3VO) o[0];
			errors = (List<String>) o[1];
			if (errors == null) {
				errors = new ArrayList<String>();
			}

			List<SamplePuck> listSamplesToDisplay = new ArrayList<SamplePuck>();
			List<BLSample3VO> listS = this.sampleService.findByContainerId(containerVO.getContainerId());
			if (listS != null) {
				for (Iterator<BLSample3VO> iterator = listS.iterator(); iterator.hasNext();) {
					BLSample3VO blSample3VO = iterator.next();
					listSamplesToDisplay.add(new SamplePuck(blSample3VO));
				}
			}
			if (errors.size() > 0) {
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("errors", errors);
				// data => Gson
				GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
				return;
			}
			HashMap<String, Object> data = new HashMap<String, Object>();
			// context path
			data.put("contextPath", request.getContextPath());
			// shippingId
			data.put("shippingId", containerVO.getDewarVO().getShippingVO().getShippingId());
			// listSamples
			data.put("listSamples", listSamplesToDisplay);
			data.put("errors", errors);
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
		} catch (Exception e) {
			e.printStackTrace();
			errors.add(e.toString());
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("errors", errors);
			// data => Gson
			GSonUtils.sendToJs(response, data, "dd-MM-yyyy HH:mm:ss");
		}
	}

	@SuppressWarnings("unchecked")
	public static Object[] saveContainer(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		List<String> errors = new ArrayList<String>();
		LOG.debug("saveContainer");
		try {
			Protein3Service proteinService = (Protein3Service) ejb3ServiceLocator
					.getLocalService(Protein3Service.class);
			Proposal3Service proposalService = (Proposal3Service) ejb3ServiceLocator
					.getLocalService(Proposal3Service.class);
			LabContact3Service labContactService = (LabContact3Service) ejb3ServiceLocator
					.getLocalService(LabContact3Service.class);
			Shipping3Service shippingService = (Shipping3Service) ejb3ServiceLocator
					.getLocalService(Shipping3Service.class);
			Dewar3Service dewarService = (Dewar3Service) ejb3ServiceLocator.getLocalService(Dewar3Service.class);
			Container3Service containerService = (Container3Service) ejb3ServiceLocator
					.getLocalService(Container3Service.class);
			DiffractionPlan3Service difPlanService = (DiffractionPlan3Service) ejb3ServiceLocator
					.getLocalService(DiffractionPlan3Service.class);
			Crystal3Service crystalService = (Crystal3Service) ejb3ServiceLocator
					.getLocalService(Crystal3Service.class);
			BLSample3Service sampleService = (BLSample3Service) ejb3ServiceLocator
					.getLocalService(BLSample3Service.class);
			List<String> allowedSpaceGroups = (List<String>) request.getSession().getAttribute(
					Constants.ISPYB_ALLOWED_SPACEGROUPS_LIST);
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			Proposal3VO proposalVO = proposalService.findByPk(proposalId);
			List<LabContact3VO> listLabContacts = labContactService.findFiltered(proposalId, null);
			LabContact3VO sendingShippingLabContactVO = null;
			LabContact3VO returnShippingLabContactVO = null;
			if (listLabContacts != null && listLabContacts.size() > 0) {
				sendingShippingLabContactVO = listLabContacts.get(0);
				returnShippingLabContactVO = listLabContacts.get(0);
			} else {
				errors.add("No labConctact found");
			}

			List<Crystal3VO> listCrystalCreated = crystalService.findByProposalId(proposalId);
			List<DiffractionPlan3VO> listDifPlanCreated = new ArrayList<DiffractionPlan3VO>();
			List<DiffractionPlan3VO> listDifPlanUpdated = new ArrayList<DiffractionPlan3VO>();
			List<BLSample3VO> listSampleCreated = new ArrayList<BLSample3VO>();
			List<BLSample3VO> listSampleUpdated = new ArrayList<BLSample3VO>();
			List<BLSample3VO> listSampleRemoved = new ArrayList<BLSample3VO>();
			// As we will need it later, create a UUID Generator...

			String listSamples = request.getParameter("listSamples");
			String shipmentName = request.getParameter("shipmentName");
			String dewarCode = request.getParameter("dewarCode");
			String puckCode = request.getParameter("puckCode");
			String shippingIdst = request.getParameter("shippingId");
			String dewarIdst = request.getParameter("dewarId");
			String containerIdst = request.getParameter("containerId");
			Gson gson = GSonUtils.getGson("dd-MM-yyyy");
			Type mapType = new TypeToken<ArrayList<SamplePuck>>() {
			}.getType();
			ArrayList<SamplePuck> samples = gson.fromJson(listSamples, mapType);

			Integer containerId = null;
			Integer dewarId = null;
			Integer shippingId = null;

			if (listSamples == null && shipmentName == null && dewarCode == null && puckCode == null) {
				shipmentName = (String) request.getSession().getAttribute("shipmentName");
				dewarCode = (String) request.getSession().getAttribute("dewarCode");
				puckCode = (String) request.getSession().getAttribute("puckCode");
				shippingId = (Integer) request.getSession().getAttribute("shippingId");
				dewarId = (Integer) request.getSession().getAttribute("dewarId");
				containerId = (Integer) request.getSession().getAttribute("containerId");
				samples = (ArrayList<SamplePuck>) request.getSession().getAttribute("listSamples");

			}

			Container3VO containerVO = null;
			boolean createContainer = true;
			try {
				if (containerId == null) {
					containerId = Integer.parseInt(containerIdst);
				}
				containerVO = containerService.findByPk(containerId, true);
				createContainer = !(containerVO != null);
			} catch (NumberFormatException e) {

			}

			Dewar3VO dewarVO = null;
			boolean createDewar = true;
			try {
				if (dewarId == null) {
					dewarId = Integer.parseInt(dewarIdst);
				}
				dewarVO = dewarService.findByPk(dewarId, true, false);
				createDewar = !(dewarVO != null);
			} catch (NumberFormatException e) {

			}

			Shipping3VO shippingVO = null;
			boolean createShipping = true;
			try {
				if (shippingId == null) {
					shippingId = Integer.parseInt(shippingIdst);
				}
				shippingVO = shippingService.findByPk(shippingId, true);
				createShipping = !(shippingVO != null);
				if (shippingVO != null) {
					sendingShippingLabContactVO = shippingVO.getSendingLabContactVO();
					returnShippingLabContactVO = shippingVO.getReturnLabContactVO();
				}
			} catch (NumberFormatException e) {

			}

			// Shipping
			if (shippingVO == null) {
				shippingVO = new Shipping3VO();
				shippingVO.setProposalVO(proposalVO);
				shippingVO.setShippingName(shipmentName);
				shippingVO.setCreationDate(new Date());
				shippingVO.setShippingStatus(Constants.SHIPPING_STATUS_OPENED);
				shippingVO.setTimeStamp(StringUtils.getCurrentTimeStamp());
				shippingVO.setShippingType(Constants.DEWAR_TRACKING_SHIPPING_TYPE);
				shippingVO.setSendingLabContactVO(sendingShippingLabContactVO);
				shippingVO.setReturnLabContactVO(returnShippingLabContactVO);
			}
			if (dewarVO == null) {
				// Dewar
				dewarVO = new Dewar3VO();
				dewarVO.setShippingVO(shippingVO);
				dewarVO.setCode(dewarCode);
				dewarVO.setType(Constants.PARCEL_DEWAR_TYPE);
				dewarVO.setDewarStatus(Constants.SHIPPING_STATUS_OPENED);
				dewarVO.setTimeStamp(StringUtils.getCurrentTimeStamp());
				Integer dewarAvgCustomsValue = null;
				if (sendingShippingLabContactVO != null
						&& sendingShippingLabContactVO.getDewarAvgCustomsValue() != null) {
					dewarAvgCustomsValue = sendingShippingLabContactVO.getDewarAvgCustomsValue();
				}
				dewarVO.setCustomsValue(dewarAvgCustomsValue);
				Integer transportValue = null;
				if (sendingShippingLabContactVO != null
						&& sendingShippingLabContactVO.getDewarAvgTransportValue() != null) {
					transportValue = sendingShippingLabContactVO.getDewarAvgTransportValue();
				}
				dewarVO.setTransportValue(transportValue);
			}
			int nbSamples = 0;
			if (samples != null) {
				for (Iterator<SamplePuck> iterator = samples.iterator(); iterator.hasNext();) {
					SamplePuck samplePuck = iterator.next();
					if (samplePuck.proteinAcronym != null && !samplePuck.proteinAcronym.isEmpty() && samplePuck.sampleName != null && !samplePuck.sampleName.isEmpty()){
						nbSamples++;
					}				
				}
			}
			//container
			if (containerVO == null) {
				containerVO = new Container3VO();
				containerVO.setCode(puckCode);				
				containerVO.setTimeStamp(StringUtils.getCurrentTimeStamp());
				containerVO.setDewarVO(dewarVO);
				if (samples != null && (nbSamples > Constants.SPINE_SAMPLE_CAPACITY || Constants.SITE_IS_MAXIV())){
					containerVO.setContainerType(Constants.CONTAINER_TYPE_UNIPUCK);
					containerVO.setCapacity(Constants.UNIPUCK_SAMPLE_CAPACITY);
				} else {
					containerVO.setContainerType(Constants.CONTAINER_TYPE_SPINE);
					containerVO.setCapacity(Constants.SPINE_SAMPLE_CAPACITY);
				}
			}		
			
			boolean isError = false;
			if (samples != null) {
				for (Iterator<SamplePuck> iterator = samples.iterator(); iterator.hasNext();) {
					SamplePuck samplePuck = iterator.next();
					boolean sampleCreateMode = true;
					BLSample3VO sampleFromDB = null;
					if (samplePuck.getSampleId() != null && !samplePuck.getSampleId().equals("-1")) {
						sampleFromDB = sampleService.findByPk(Integer.parseInt(samplePuck.getSampleId()), false, false, false);
						sampleCreateMode = false;
					}
					boolean sampleRowOK = true;
					// position
					Integer position = -1;
					try {
						if (samplePuck.getPosition() != null && !samplePuck.getPosition().isEmpty())
							position = Integer.parseInt(samplePuck.getPosition());
					} catch (NumberFormatException ex) {
						errors.add("position incorrect " + samplePuck.getPosition());
						sampleRowOK = false;
					}
					// sample Name
					String sampleName = samplePuck.getSampleName();
					// protein acronym
					String proteinAcronym = samplePuck.getProteinAcronym();
					// spaceGroup
					String spaceGroup = samplePuck.getSpaceGroup();
					// preObservedResolution
					Double preObservedResolution = null;
					try {
						if (samplePuck.getPreObservedResolution() != null
								&& !samplePuck.getPreObservedResolution().isEmpty())
							preObservedResolution = Double.parseDouble(samplePuck.getPreObservedResolution());
					} catch (NumberFormatException ex) {
						errors.add("preObservedResolution incorrect " + samplePuck.getPreObservedResolution());
						sampleRowOK = false;
					}
					// neededResolution
					Double neededResolution = null;
					try {
						if (samplePuck.getNeededResolution() != null && !samplePuck.getNeededResolution().isEmpty())
							neededResolution = Double.parseDouble(samplePuck.getNeededResolution());
					} catch (NumberFormatException ex) {
						errors.add("neededResolution incorrect " + samplePuck.getNeededResolution());
					}
					// preferredBeamDiameter
					Double preferredBeamDiameter = null;
					try {
						if (samplePuck.getPreferredBeamDiameter() != null && !samplePuck.getPreferredBeamDiameter().isEmpty())
							preferredBeamDiameter = Double.parseDouble(samplePuck.getPreferredBeamDiameter());
					} catch (NumberFormatException ex) {
						errors.add("preferredBeamDiameter incorrect " + samplePuck.getPreferredBeamDiameter());
						sampleRowOK = false;
					}
					// experimentType
					String experimentType = samplePuck.getExperimentType();
					if (experimentType != null && experimentType.isEmpty()) {
						experimentType = null;
					}
					// numberOfPositions
					Integer numberOfPositions = null;
					try {
						if (samplePuck.getNumberOfPositions() != null && !samplePuck.getNumberOfPositions().isEmpty())
							numberOfPositions = Integer.parseInt(samplePuck.getNumberOfPositions());
					} catch (NumberFormatException ex) {
						errors.add("numberOfPositions incorrect " + samplePuck.getNumberOfPositions());
						sampleRowOK = false;
					}

					// radiationSensitivity
					Double radiationSensitivity = null;
					try {
						if (samplePuck.getRadiationSensitivity() != null && !samplePuck.getRadiationSensitivity().isEmpty())
							radiationSensitivity = Double.parseDouble(samplePuck.getRadiationSensitivity());
					} catch (NumberFormatException ex) {
						errors.add("radiationSensitivity incorrect " + samplePuck.getRadiationSensitivity());
						sampleRowOK = false;
					}

					// aimedMultiplicity
					Double aimedMultiplicity = null;
					try {
						if (samplePuck.getAimedMultiplicity() != null && !samplePuck.getAimedMultiplicity().isEmpty())
							aimedMultiplicity = Double.parseDouble(samplePuck.getAimedMultiplicity());
					} catch (NumberFormatException ex) {
						errors.add("aimedMultiplicity incorrect " + samplePuck.getAimedMultiplicity());
						sampleRowOK = false;
					}

					// aimedCompleteness
					Double aimedCompleteness = null;
					try {
						if (samplePuck.getAimedCompleteness() != null && !samplePuck.getAimedCompleteness().isEmpty())
							aimedCompleteness = Double.parseDouble(samplePuck.getAimedCompleteness());
					} catch (NumberFormatException ex) {
						errors.add("requiredCompleteness incorrect " + samplePuck.getAimedCompleteness());
						sampleRowOK = false;
					}

					// minOscWidth
					Double minOscWidth = null;
					try {
						if (samplePuck.getMinOscWidth() != null && !samplePuck.getMinOscWidth().isEmpty())
							minOscWidth = Double.parseDouble(samplePuck.getMinOscWidth());
					} catch (NumberFormatException ex) {
						errors.add("minOscWidth incorrect " + samplePuck.getMinOscWidth());
						sampleRowOK = false;
					}
					
					// axisRange
					Double axisRange = null;
					try {
						if (samplePuck.getAxisRange() != null && !samplePuck.getAxisRange().isEmpty())
							axisRange = Double.parseDouble(samplePuck.getAxisRange());
					} catch (NumberFormatException ex) {
						errors.add("axisRange incorrect " + samplePuck.getAxisRange());
						sampleRowOK = false;
					}

					// unitCellA
					Double unitCellA = null;
					try {
						if (samplePuck.getUnitCellA() != null && !samplePuck.getUnitCellA().isEmpty())
							unitCellA = Double.parseDouble(samplePuck.getUnitCellA());
					} catch (NumberFormatException ex) {
						errors.add("unitCellA incorrect " + samplePuck.getUnitCellA());
						sampleRowOK = false;
					}
					// unitCellB
					Double unitCellB = null;
					try {
						if (samplePuck.getUnitCellB() != null && !samplePuck.getUnitCellB().isEmpty())
							unitCellB = Double.parseDouble(samplePuck.getUnitCellB());
					} catch (NumberFormatException ex) {
						errors.add("unitCellB incorrect " + samplePuck.getUnitCellB());
						sampleRowOK = false;
					}
					// unitCellC
					Double unitCellC = null;
					try {
						if (samplePuck.getUnitCellC() != null && !samplePuck.getUnitCellC().isEmpty())
							unitCellC = Double.parseDouble(samplePuck.getUnitCellC());
					} catch (NumberFormatException ex) {
						errors.add("unitCellC incorrect " + samplePuck.getUnitCellC());
						sampleRowOK = false;
					}
					// unitCellAlpha
					Double unitCellAlpha = null;
					try {
						if (samplePuck.getUnitCellAlpha() != null && !samplePuck.getUnitCellAlpha().isEmpty())
							unitCellAlpha = Double.parseDouble(samplePuck.getUnitCellAlpha());
					} catch (NumberFormatException ex) {
						errors.add("unitCellAlpha incorrect " + samplePuck.getUnitCellAlpha());
						sampleRowOK = false;
					}
					// unitCellBeta
					Double unitCellBeta = null;
					try {
						if (samplePuck.getUnitCellBeta() != null && !samplePuck.getUnitCellBeta().isEmpty())
							unitCellBeta = Double.parseDouble(samplePuck.getUnitCellBeta());
					} catch (NumberFormatException ex) {
						errors.add("unitCellBeta incorrect " + samplePuck.getUnitCellBeta());
						sampleRowOK = false;
					}
					// unitCellGamma
					Double unitCellGamma = null;
					try {
						if (samplePuck.getUnitCellGamma() != null && !samplePuck.getUnitCellGamma().isEmpty())
							unitCellGamma = Double.parseDouble(samplePuck.getUnitCellGamma());
					} catch (NumberFormatException ex) {
						errors.add("unitCellGamma incorrect " + samplePuck.getUnitCellGamma());
						sampleRowOK = false;
					}
					// SMILES
					String smiles = samplePuck.getSmiles();
					// comments
					String comments = samplePuck.getComments();
					// pin barcode
					String pinBarcode = samplePuck.getPinBarcode();
					//
					boolean sampleNameRulesOk = sampleName != null
							&& sampleName.matches(Constants.MASK_BASIC_CHARACTERS_WITH_DASH_UNDERSCORE_NO_SPACE);

					if (proteinAcronym != null && proteinAcronym.isEmpty()) {
						if (sampleCreateMode)
							continue;
						else {
							listSampleRemoved.add(sampleFromDB);
							continue;
						}
					}
					// if (proteinAcronym == null || proteinAcronym.isEmpty()){
					// errors.add("The protein acronym is empty");
					// sampleRowOK = false;
					// }
					if (sampleName == null || sampleName.isEmpty()) {
						errors.add("The sample name is empty");
						sampleRowOK = false;
					} else if (!sampleNameRulesOk) {
						errors.add("The sample name " + sampleName + " is not well formatted");
						sampleRowOK = false;
					}

					if (sampleRowOK) {
						// Parse ProteinAcronym - SpaceGroup
						// Pre-filled spreadsheet (including protein_acronym - SpaceGroup)
						int separatorStart = proteinAcronym.indexOf(Constants.PROTEIN_ACRONYM_SPACE_GROUP_SEPARATOR);
						if (separatorStart != -1) {
							String acronym = proteinAcronym.substring(0, separatorStart);
							String prefilledSpaceGroup = proteinAcronym
									.substring(
											separatorStart + Constants.PROTEIN_ACRONYM_SPACE_GROUP_SEPARATOR.length(),
											proteinAcronym.length());
							proteinAcronym = acronym;
							if (spaceGroup == null || allowedSpaceGroups.contains(spaceGroup.toUpperCase())) {
								// Do nothing = use spaceGroup from dropdown list
							} else if (allowedSpaceGroups.contains(prefilledSpaceGroup.toUpperCase())) {
								// Used pre-filled space group
								spaceGroup = prefilledSpaceGroup;
							}
						}
						List<Protein3VO> proteinTab = proteinService.findByAcronymAndProposalId(proposalId,
								proteinAcronym);
						if (proteinTab == null || proteinTab.size() == 0) {
							errors.add("Protein '" + proteinAcronym + "' can't be found \n ");
						} else {
							Protein3VO protein = proteinTab.get(0);

							if (CreateShippingFileAction.isLocationOccInContainer(position, listSampleCreated,
									containerVO)) {
								errors.add("The sample position is incorrect: " + position);
								isError = true;
								break;
							}
							// DiffractionPlan
							DiffractionPlan3VO difPlan = null;
							if (sampleCreateMode) {
								difPlan = new DiffractionPlan3VO();
								difPlan.setObservedResolution(preObservedResolution);
								difPlan.setRequiredResolution(neededResolution);
								difPlan.setExposureTime((double) 0);
								difPlan.setPreferredBeamDiameter(preferredBeamDiameter);
								difPlan.setRadiationSensitivity(radiationSensitivity);
								difPlan.setAimedCompleteness(aimedCompleteness);
								difPlan.setAimedMultiplicity(aimedMultiplicity);
								difPlan.setNumberOfPositions(numberOfPositions);
								difPlan.setExperimentKind(experimentType);
								difPlan.setAxisRange(axisRange);
								difPlan = difPlanService.create(difPlan);
								listDifPlanCreated.add(difPlan);
							} else { // update
								if (sampleFromDB != null) {
									difPlan = sampleFromDB.getDiffractionPlanVO();
									if (difPlan == null) {
										difPlan = new DiffractionPlan3VO();
										difPlan.setObservedResolution(preObservedResolution);
										difPlan.setRequiredResolution(neededResolution);
										difPlan.setExposureTime((double) 0);
										difPlan.setPreferredBeamDiameter(preferredBeamDiameter);
										difPlan.setRadiationSensitivity(radiationSensitivity);
										difPlan.setAimedCompleteness(aimedCompleteness);
										difPlan.setAimedMultiplicity(aimedMultiplicity);
										difPlan.setNumberOfPositions(numberOfPositions);
										difPlan.setExperimentKind(experimentType);
										difPlan.setMinOscWidth(minOscWidth);
										difPlan.setAxisRange(axisRange);
										listDifPlanCreated.add(difPlan);
									} else {
										difPlan.setObservedResolution(preObservedResolution);
										difPlan.setRequiredResolution(neededResolution);
										difPlan.setPreferredBeamDiameter(preferredBeamDiameter);
										difPlan.setRadiationSensitivity(radiationSensitivity);
										difPlan.setAimedCompleteness(aimedCompleteness);
										difPlan.setAimedMultiplicity(aimedMultiplicity);
										difPlan.setNumberOfPositions(numberOfPositions);
										difPlan.setExperimentKind(experimentType);
										listDifPlanUpdated.add(difPlan);
										difPlan.setMinOscWidth(minOscWidth);
										difPlan.setAxisRange(axisRange);
									}
								}
							}
							// Crystal
							Crystal3VO crystalVO = new Crystal3VO();
							String crystalID = UUID.randomUUID().toString();
							crystalVO.setProteinVO(protein);
							crystalVO.setCrystalUUID(crystalID);
							crystalVO.setSpaceGroup(spaceGroup);
							crystalVO.setName(crystalID);
							crystalVO.setCellA(unitCellA);
							crystalVO.setCellB(unitCellB);
							crystalVO.setCellC(unitCellC);
							crystalVO.setCellAlpha(unitCellAlpha);
							crystalVO.setCellBeta(unitCellBeta);
							crystalVO.setCellGamma(unitCellGamma);
							crystalVO.setDiffractionPlanVO(difPlan);
							if ((crystalVO.getSpaceGroup() == null) || (crystalVO.getSpaceGroup().equals(""))) {
								crystalVO.setSpaceGroup(CreateShippingFileAction.DEFAULT_SPACE_GROUP);
							}
							Crystal3VO crystal = CreateShippingFileAction.getCrystal(listCrystalCreated, crystalVO);
							if (crystal == null) {							
								crystalVO = crystalService.create(crystalVO);
								listCrystalCreated.add(crystalVO);
							} else {
								crystalVO = crystal;
							}

							// BLSample
							BLSample3VO sample = null;
							if (CreateShippingFileAction.isSampleNameAlreadyExist(sampleName, protein,
									listSampleCreated)) {
								errors.add("The sample name already exists: " + sampleName);
								isError = true;
								break;
							}

							List<BLSample3VO> samplesWithSameName = sampleService.findByNameAndProteinId(sampleName,
									protein.getProteinId(), shippingId);
							if (!samplesWithSameName.isEmpty()) {
								boolean isSampleWithSameName = true;
								if (!sampleCreateMode) {
									isSampleWithSameName = (samplesWithSameName.size() > 1 || !samplesWithSameName
											.get(0).getBlSampleId().equals(sampleFromDB.getBlSampleId()));
								}
								if (isSampleWithSameName) {
									errors.add("[" + protein.getAcronym() + " + " + sampleName
											+ "] is already existing, and should be unique.\n");
									isError = true;
									break;
								}

							}
							if (sampleCreateMode) {
								sample = new BLSample3VO();
								sample.setCrystalVO(crystalVO);
								sample.setDiffractionPlanVO(difPlan);
								sample.setName(sampleName);
								sample.setLocation("" + position);
								sample.setHolderLength(CreateShippingFileAction.DEFAULT_HOLDER_LENGTH);
								sample.setLoopLength(CreateShippingFileAction.DEFAULT_LOOP_LENGTH);
								sample.setLoopType(CreateShippingFileAction.DEFAULT_LOOP_TYPE);
								sample.setWireWidth(CreateShippingFileAction.DEFAULT_WIRE_WIDTH);
								sample.setSmiles(smiles);
								sample.setComments(comments);
								sample.setCode(pinBarcode);
								sample.setContainerVO(containerVO);

								listSampleCreated.add(sample);
							} else { // update
								sample = sampleFromDB;
								sample.setCrystalVO(crystalVO);
								sample.setDiffractionPlanVO(difPlan);
								sample.setName(sampleName);
								sample.setCode(pinBarcode);
								sample.setSmiles(smiles);
								sample.setComments(comments);
								listSampleUpdated.add(sample);
							}
						}

					} else {
						isError = true;
						continue;
					}
				}
			}
			if (!isError) {
				// creation in db
				if (createShipping) {
					shippingVO = shippingService.create(shippingVO);
				}
				if (createDewar) {
					dewarVO.setShippingVO(shippingVO);
					dewarVO = dewarService.create(dewarVO);
				}
				if (createContainer) {
					containerVO.setDewarVO(dewarVO);
					containerVO = containerService.create(containerVO);
				} else {
					containerVO = containerService.update(containerVO);
				}
//				for (Iterator<DiffractionPlan3VO> d = listDifPlanCreated.iterator(); d.hasNext();) {
//					DiffractionPlan3VO diffractionPlanVO = d.next();
//					diffractionPlanVO = difPlanService.create(diffractionPlanVO);
//				}
				for (Iterator<DiffractionPlan3VO> d = listDifPlanUpdated.iterator(); d.hasNext();) {
					DiffractionPlan3VO diffractionPlanVO = d.next();
					diffractionPlanVO = difPlanService.update(diffractionPlanVO);
				}
				for (Iterator<BLSample3VO> s = listSampleCreated.iterator(); s.hasNext();) {
					BLSample3VO sampleVO = s.next();
					sampleVO.setContainerVO(containerVO);
					sampleVO = sampleService.create(sampleVO);
				}
				for (Iterator<BLSample3VO> s = listSampleUpdated.iterator(); s.hasNext();) {
					BLSample3VO sampleVO = s.next();
					sampleVO = sampleService.update(sampleVO);
				}
				for (Iterator<BLSample3VO> s = listSampleRemoved.iterator(); s.hasNext();) {
					BLSample3VO sampleVO = s.next();
					sampleService.deleteByPk(sampleVO.getBlSampleId());
				}
			}
			Object[] o = new Object[2];
			o[0] = containerVO;
			o[1] = errors;
			return o;

		} catch (Exception e) {
			e.printStackTrace();
			errors.add(e.toString());
			Object[] o = new Object[2];
			o[0] = null;
			o[1] = errors;
			return o;
		}
	}

}
