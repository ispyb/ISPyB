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

import ispyb.client.common.BreadCrumbsForm;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;

/**
 * @struts.action name="createShippingFileForm" path="/user/createShippingFileAction"
 *                type="ispyb.client.common.shipping.CreateShippingFileAction" input="user.shipping.file.page" validate="false"
 *                parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="shippingCreateFilePage" path="user.shipping.file.page"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 */
public class CreateShippingFileAction extends DispatchAction {
	private final Logger LOG = Logger.getLogger(CreateShippingFileAction.class);

	private static final int NB_COL = 22;

	private static final int MAX_SHIPPING_NAME = 45;

	private static final int MAX_DEWAR_CODE = 45;

	private static final int MAX_CONTAINER_CODE = 45;

	private static final int MAX_SAMPLE_BARCODE = 45;

	private static final int MAX_SAMPLE_NAME = Constants.BLSAMPLE_NAME_NB_CHAR;

	private static final int MAX_SAMPLE_COMMENTS = 1024;

	private static final String DEFAULT_EXPERIMENT_TYPE = "Default";

	public static final String DEFAULT_SPACE_GROUP = "Undefined";

	public static final Double DEFAULT_HOLDER_LENGTH = 22.0;

	public static final Double DEFAULT_LOOP_LENGTH = 0.5;

	public static final String DEFAULT_LOOP_TYPE = "Nylon";

	public static final Double DEFAULT_WIRE_WIDTH = 0.010;

	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Protein3Service proteinService;

	private Proposal3Service proposalService;

	private LabContact3Service labContactService;

	private Shipping3Service shippingService;

	private Dewar3Service dewarService;

	private Container3Service containerService;

	private DiffractionPlan3Service difPlanService;

	private Crystal3Service crystalService;

	private BLSample3Service sampleService;

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws Exception {

		this.proteinService = (Protein3Service) ejb3ServiceLocator.getLocalService(Protein3Service.class);
		this.proposalService = (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);
		this.labContactService = (LabContact3Service) ejb3ServiceLocator.getLocalService(LabContact3Service.class);
		this.shippingService = (Shipping3Service) ejb3ServiceLocator.getLocalService(Shipping3Service.class);
		this.dewarService = (Dewar3Service) ejb3ServiceLocator.getLocalService(Dewar3Service.class);
		this.containerService = (Container3Service) ejb3ServiceLocator.getLocalService(Container3Service.class);
		this.difPlanService = (DiffractionPlan3Service) ejb3ServiceLocator.getLocalService(DiffractionPlan3Service.class);
		this.crystalService = (Crystal3Service) ejb3ServiceLocator.getLocalService(Crystal3Service.class);
		this.sampleService = (BLSample3Service) ejb3ServiceLocator.getLocalService(BLSample3Service.class);
	}

	/**
	 * execute
	 * 
	 * @param args
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse in_response)
			throws Exception {
		this.initServices();
		// Retrieve Attributes
		String reqCode = request.getParameter("reqCode");

		if (reqCode != null) {
			if (reqCode.equals("display"))
				return this.display(mapping, actForm, request, in_response);
			if (reqCode.equals("uploadCsvFile"))
				return this.uploadCsvFile(mapping, actForm, request, in_response);

		}
		return this.display(mapping, actForm, request, in_response);
	}

	/**
	 * display
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse in_reponse) {
		ActionMessages errors = new ActionMessages();
		try {
			CreateShippingFileForm form = (CreateShippingFileForm) actForm; // Parameters submited by form
			BreadCrumbsForm.getItClean(request);
			List<String> listFieldSeparator = new ArrayList<String>();
			listFieldSeparator.add(",");
			listFieldSeparator.add(";");
			List<String> listTextSeparator = new ArrayList<String>();
			listTextSeparator.add("");
			listTextSeparator.add("'");
			listTextSeparator.add("\"");

			form.setListFieldSeparator(listFieldSeparator);
			form.setListTextSeparator(listTextSeparator);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		return mapping.findForward("shippingCreateFilePage");
	}

	/**
	 * uploadCsvFile
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	public ActionForward uploadCsvFile(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {
		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();
		try {
			CreateShippingFileForm form = (CreateShippingFileForm) actForm; // Parameters submitted by form
			String currentProposalCode = (String) request.getSession().getAttribute(Constants.PROPOSAL_CODE);
			String currentProposalNumber = (String) request.getSession().getAttribute(Constants.PROPOSAL_NUMBER);
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			boolean errorFile = false;
			Proposal3VO proposalVO = proposalService.findByPk(proposalId);
			List<LabContact3VO> listLabContacts = labContactService.findFiltered(proposalId, null);
			LabContact3VO shippingLabContactVO = null;
			if (listLabContacts != null && listLabContacts.size() > 0) {
				shippingLabContactVO = listLabContacts.get(0);
			} else {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.upload", "No labConctact found"));
				errorFile = true;
			}
			List<String> allowedSpaceGroups = (List<String>) request.getSession().getAttribute(
					Constants.ISPYB_ALLOWED_SPACEGROUPS_LIST);
			String fieldSeparator = form.getFieldSeparator();
			String textSeparator = form.getTextSeparator();
			char fieldSep = fieldSeparator.charAt(0);
			char textSep = ' ';
			if (textSeparator.length() > 0)
				textSep = textSeparator.charAt(0);

			String uploadedFileName;
			String realCSVPath;

			List<Shipping3VO> listShippingCreated = new ArrayList<Shipping3VO>();
			List<Dewar3VO> listDewarCreated = new ArrayList<Dewar3VO>();
			List<Container3VO> listContainerCreated = new ArrayList<Container3VO>();
			// List<Crystal3VO> listCrystalCreated = new ArrayList<Crystal3VO>();
			List<Crystal3VO> listCrystalCreated = crystalService.findByProposalId(proposalId);
			List<DiffractionPlan3VO> listDifPlanCreated = new ArrayList<DiffractionPlan3VO>();
			List<BLSample3VO> listSampleCreated = new ArrayList<BLSample3VO>();

			if (request != null) {
				uploadedFileName = form.getRequestFile().getFileName();
				if (uploadedFileName.equals("")) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.shipping.upload.file.empty"));
					errorFile = true;
				} else if (!uploadedFileName.endsWith(".csv")) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.shipping.upload.file.format"));
					errorFile = true;
				}

				if (!errorFile) {
					realCSVPath = request.getRealPath("/") + "/tmp/" + uploadedFileName;
					// Write the received file to tmp directory
					FormFile f = form.getRequestFile();
					InputStream in = f.getInputStream();
					File outputFile = new File(realCSVPath);
					if (outputFile.exists())
						outputFile.delete();
					FileOutputStream out = new FileOutputStream(outputFile);
					while (in.available() != 0) {
						out.write(in.read());
						out.flush();
					}
					out.flush();
					out.close();

					// received file
					Reader inFile = new FileReader(realCSVPath);
					LOG.info(" ---[uploadFile] Upload Shipment csv ");
					CSVStrategy csvStrategy = new CSVStrategy(fieldSep, textSep, CSVStrategy.COMMENTS_DISABLED);
					CSVParser parser = new CSVParser(inFile, csvStrategy);
					String[][] values = parser.getAllValues();
					int nbRows = values.length;
					boolean isError = false;
					for (int i = 0; i < nbRows; i++) {
						int nbCol = values[i].length;
						if (nbCol != NB_COL) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.shipping.upload.file.data",
									"The number of columns is incorrect (" + nbCol + " instead of " + NB_COL + ")"));
							isError = true;
							break;
						}
						int j = 0;
						// proposalCode & proposalNumber
						String proposalCode = values[i][j++];
						String proposalNumber = values[i][j++];
						if (proposalCode == null || proposalNumber == null || !(proposalCode.equalsIgnoreCase(currentProposalCode))
								|| !(proposalNumber.equals(currentProposalNumber))) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.shipping.upload.file.data",
									"The proposal is incorrect line " + (i + 1)));
							isError = true;
							break;
						}
						// visitNumber
						j++;
						// shippingName
						String shippingName = values[i][j++];
						if (shippingName == null || shippingName.trim().length() == 0 || shippingName.length() > MAX_SHIPPING_NAME) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.shipping.upload.file.data",
									"The shipping name is incorrect line " + (i + 1) + " (required and < " + MAX_SHIPPING_NAME
											+ " characters)"));
							isError = true;
							break;
						}
						// dewarCode
						String dewarCode = values[i][j++];
						if (dewarCode == null || dewarCode.trim().length() == 0 || dewarCode.length() > MAX_DEWAR_CODE) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.shipping.upload.file.data",
									"The dewar code is incorrect line " + (i + 1) + " (required and < " + MAX_DEWAR_CODE
											+ " characters)"));
							isError = true;
							break;
						}
						// containerCode
						String containerCode = values[i][j++];
						if (containerCode == null || containerCode.trim().length() == 0 || containerCode.length() > MAX_CONTAINER_CODE) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.shipping.upload.file.data",
									"The container code is incorrect line " + (i + 1) + " (required and < " + MAX_CONTAINER_CODE
											+ " characters)"));
							isError = true;
							break;
						}
						// preObsResolution
						String preObsResolutionS = values[i][j++];
						Double preObsResolution = null;
						if (preObsResolutionS == null || preObsResolutionS.trim().length() != 0) {
							try {
								preObsResolution = Double.parseDouble(preObsResolutionS);
							} catch (NumberFormatException e) {
								errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.shipping.upload.file.data",
										"Wrong fromat for preObsResolution line " + (i + 1)));
								isError = true;
								break;
							}
						}
						// neededResolution
						String neededResolutionS = values[i][j++];
						Double neededResolution = null;
						if (neededResolutionS == null || neededResolutionS.trim().length() != 0) {
							try {
								neededResolution = Double.parseDouble(neededResolutionS);
							} catch (NumberFormatException e) {
								errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.shipping.upload.file.data",
										"Wrong fromat for neededResolution line " + (i + 1)));
								isError = true;
								break;
							}
						}
						// oscillationRange
						String oscillationRangeS = values[i][j++];
						Double oscillationRange = null;
						if (oscillationRangeS == null || oscillationRangeS.trim().length() != 0) {
							try {
								oscillationRange = Double.parseDouble(oscillationRangeS);
							} catch (NumberFormatException e) {
								errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.shipping.upload.file.data",
										"Wrong fromat for oscillationRange line " + (i + 1)));
								isError = true;
								break;
							}
						}
						// proteinAcronym
						String proteinAcronym = values[i][j++];
						Protein3VO protein = null;
						if (proteinAcronym == null || proteinAcronym.trim().length() == 0) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.shipping.upload.file.data",
									"The protein Acronym is required line " + (i + 1)));
							isError = true;
							break;
						}
						List<Protein3VO> proteinTab = proteinService.findByAcronymAndProposalId(proposalId, proteinAcronym);
						if (proteinTab == null || proteinTab.size() == 0) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.shipping.upload.file.data",
									"Protein " + proteinAcronym + " can not be found in ISPyB, line " + (i + 1)));
							isError = true;
							break;
						} else {
							protein = proteinTab.get(0);
						}
						// proteinName
						String proteinName = values[i][j++];
						if (proteinName == null || proteinName.trim().length() == 0
								|| !protein.getName().equalsIgnoreCase(proteinName)) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.shipping.upload.file.data",
									"The protein name is incorrect line " + (i + 1)
											+ " (required and must correspond to the proteinAcronym). The expected proteinName is "
											+ protein.getName()));
							isError = true;
							break;
						}
						// spaceGroup
						String spaceGroup = values[i][j++];
						if (spaceGroup != null && !spaceGroup.equals("") && !allowedSpaceGroups.contains(spaceGroup.toUpperCase())) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.shipping.upload.file.data",
									"The space group can not be found in ISPyB line " + (i + 1)));
							isError = true;
							break;
						}
						// sampleBarcode
						String sampleBarcode = values[i][j++];
						if (sampleBarcode != null && sampleBarcode.length() > MAX_SAMPLE_BARCODE) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.shipping.upload.file.data",
									"The sample barcode is incorrect line " + (i + 1) + " (< " + MAX_SAMPLE_BARCODE + " characters)"));
							isError = true;
							break;
						}
						// sampleName
						String sampleName = values[i][j++];
						if (sampleName == null || sampleName.trim().length() == 0 || sampleName.length() > MAX_SAMPLE_NAME
								|| !sampleName.matches(Constants.MASK_BASIC_CHARACTERS_WITH_DASH_UNDERSCORE_NO_SPACE)) {
							errors.add(
									ActionMessages.GLOBAL_MESSAGE,
									new ActionMessage(
											"error.user.shipping.upload.file.data",
											"The sample name is incorrect line "
													+ (i + 1)
													+ " (required and  < "
													+ MAX_SAMPLE_NAME
													+ " characters, unique name for the protein acronym, must contain only a-z, A-Z or 0-9 or - or _ characters.)"));
							isError = true;
							break;
						}
						// samplePosition
						String samplePos = values[i][j++];
						int samplePosition = 0;
						try {
							samplePosition = Integer.parseInt(samplePos);
						} catch (NumberFormatException e) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.shipping.upload.file.data",
									"The sample position is incorrect: " + samplePos + ", line " + (i + 1)
											+ " (required number between 1 and 10)"));
							isError = true;
							break;
						}
						if (samplePosition < 1 || samplePosition > Constants.BASKET_SAMPLE_CAPACITY) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.shipping.upload.file.data",
									"The sample position is incorrect: " + samplePos + ", line " + (i + 1)
											+ " (required number between 1 and 10)"));
							isError = true;
							break;
						}
						// sample comments
						String sampleComments = values[i][j++];
						if (sampleComments != null && sampleComments.length() > MAX_SAMPLE_COMMENTS) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.shipping.upload.file.data",
									"The sample comments is incorrect line " + (i + 1) + " ( < " + MAX_SAMPLE_COMMENTS
											+ " characters)"));
							isError = true;
							break;
						}
						// cell_a
						String cellA = values[i][j++];
						Double cell_a = null;
						if (cellA == null || cellA.trim().length() != 0) {
							try {
								cell_a = Double.parseDouble(cellA);
							} catch (NumberFormatException e) {
								isError = true;
								errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.shipping.upload.file.data",
										"Wrong fromat for cell_a line " + (i + 1)));
								break;
							}
						}
						// cell_b
						String cellB = values[i][j++];
						Double cell_b = null;
						if (cellB == null || cellB.trim().length() != 0) {
							try {
								cell_b = Double.parseDouble(cellB);
							} catch (NumberFormatException e) {
								errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.shipping.upload.file.data",
										"Wrong fromat for cell_b line " + (i + 1)));
								isError = true;
								break;
							}
						}
						// cell_c
						String cellC = values[i][j++];
						Double cell_c = null;
						if (cellC == null || cellC.trim().length() != 0) {
							try {
								cell_c = Double.parseDouble(cellC);
							} catch (NumberFormatException e) {
								errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.shipping.upload.file.data",
										"Wrong fromat for cell_c line " + (i + 1)));
								isError = true;
								break;
							}
						}
						// cell_alpha
						String cellAlpha = values[i][j++];
						Double cell_alpha = null;
						if (cellAlpha == null || cellAlpha.trim().length() != 0) {
							try {
								cell_alpha = Double.parseDouble(cellAlpha);
							} catch (NumberFormatException e) {
								errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.shipping.upload.file.data",
										"Wrong fromat for cell_alpha line " + (i + 1)));
								isError = true;
								break;
							}
						}
						// cell_beta
						String cellBeta = values[i][j++];
						Double cell_beta = null;
						if (cellBeta == null || cellBeta.trim().length() != 0) {
							try {
								cell_beta = Double.parseDouble(cellBeta);
							} catch (NumberFormatException e) {
								errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.shipping.upload.file.data",
										"Wrong fromat for cell_beta line " + (i + 1)));
								isError = true;
								break;
							}
						}
						// cell_gamma
						String cellGamma = values[i][j++];
						Double cell_gamma = null;
						if (cellGamma == null || cellGamma.trim().length() != 0) {
							try {
								cell_gamma = Double.parseDouble(cellGamma);
							} catch (NumberFormatException e) {
								errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.shipping.upload.file.data",
										"Wrong fromat for cell_gamma line " + (i + 1)));
								isError = true;
								break;
							}
						}
						// creation of the objects
						// Shipping
						Shipping3VO shippingVO = new Shipping3VO();
						shippingVO.setProposalVO(proposalVO);
						shippingVO.setShippingName(shippingName);
						shippingVO.setCreationDate(new Date());
						shippingVO.setShippingStatus(Constants.SHIPPING_STATUS_OPENED);
						shippingVO.setTimeStamp(StringUtils.getCurrentTimeStamp());
						shippingVO.setShippingType(Constants.DEWAR_TRACKING_SHIPPING_TYPE);
						shippingVO.setSendingLabContactVO(shippingLabContactVO);
						shippingVO.setReturnLabContactVO(shippingLabContactVO);
						Shipping3VO shipment = getShipment(listShippingCreated, shippingVO);
						if (shipment == null) {
							shippingVO = shippingService.create(shippingVO);
							listShippingCreated.add(shippingVO);
						} else {
							shippingVO = shipment;
						}
						// Dewar
						Dewar3VO dewarVO = new Dewar3VO();
						dewarVO.setShippingVO(shippingVO);
						dewarVO.setCode(dewarCode);
						dewarVO.setType(Constants.PARCEL_DEWAR_TYPE);
						dewarVO.setDewarStatus(Constants.SHIPPING_STATUS_OPENED);
						dewarVO.setTimeStamp(StringUtils.getCurrentTimeStamp());
						dewarVO.setCustomsValue(shippingLabContactVO.getDewarAvgCustomsValue());
						dewarVO.setTransportValue(shippingLabContactVO.getDewarAvgTransportValue());
						Dewar3VO dewar = getDewar(listDewarCreated, dewarVO);
						if (dewar == null) {
							dewarVO = dewarService.create(dewarVO);
							listDewarCreated.add(dewarVO);
						} else {
							dewarVO = dewar;
						}
						// Container
						Container3VO containerVO = new Container3VO();
						containerVO.setContainerType("Puck");
						containerVO.setCode(containerCode);
						containerVO.setCapacity(Constants.BASKET_SAMPLE_CAPACITY);
						containerVO.setTimeStamp(StringUtils.getCurrentTimeStamp());
						containerVO.setDewarVO(dewarVO);
						Container3VO container = getContainer(listContainerCreated, containerVO);
						if (container == null) {
							containerVO = containerService.create(containerVO);
							listContainerCreated.add(containerVO);
						} else {
							containerVO = container;
						}
						if (isLocationOccInContainer(samplePosition, listSampleCreated, containerVO)) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.shipping.upload.file.data",
									"The sample position is incorrect: " + samplePos + ", line " + (i + 1)));
							isError = true;
							break;
						}
						// DiffractionPlan
						DiffractionPlan3VO difPlan = new DiffractionPlan3VO();
						difPlan.setObservedResolution(preObsResolution);
						difPlan.setRequiredResolution(neededResolution);
						difPlan.setExposureTime((double) 0);
						difPlan.setOscillationRange(oscillationRange);
						difPlan.setExperimentKind(DEFAULT_EXPERIMENT_TYPE);
						difPlan = difPlanService.create(difPlan);
						listDifPlanCreated.add(difPlan);
						// Crystal
						Crystal3VO crystalVO = new Crystal3VO();
						String crystalID = UUID.randomUUID().toString();
						crystalVO.setProteinVO(protein);
						crystalVO.setCrystalUUID(crystalID);
						crystalVO.setSpaceGroup(spaceGroup);
						crystalVO.setName(crystalID);
						crystalVO.setCellA(cell_a);
						crystalVO.setCellB(cell_b);
						crystalVO.setCellC(cell_c);
						crystalVO.setCellAlpha(cell_alpha);
						crystalVO.setCellBeta(cell_beta);
						crystalVO.setCellGamma(cell_gamma);
						crystalVO.setDiffractionPlanVO(difPlan);
						if ((crystalVO.getSpaceGroup() == null) || (crystalVO.getSpaceGroup().equals(""))) {
							crystalVO.setSpaceGroup(DEFAULT_SPACE_GROUP);
						}
						Crystal3VO crystal = getCrystal(listCrystalCreated, crystalVO);
						if (crystal == null) {
							crystalVO = crystalService.create(crystalVO);
							listCrystalCreated.add(crystalVO);

						} else {
							crystalVO = crystal;
						}

						if (!crystalVO.hasCellInfo()) {
							messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.free",
									"Warning: the unit cell parameters are not filled for the spaceGroup " + crystalVO.getSpaceGroup()
											+ "!"));
						}
						// BLSample
						BLSample3VO sample = new BLSample3VO();
						sample.setCrystalVO(crystalVO);
						sample.setDiffractionPlanVO(difPlan);
						sample.setName(sampleName);
						sample.setCode(sampleBarcode);
						sample.setLocation("" + samplePosition);
						sample.setHolderLength(DEFAULT_HOLDER_LENGTH);
						sample.setLoopLength(DEFAULT_LOOP_LENGTH);
						sample.setLoopType(DEFAULT_LOOP_TYPE);
						sample.setWireWidth(DEFAULT_WIRE_WIDTH);
						sample.setComments(sampleComments);
						sample.setContainerVO(containerVO);
						if (isSampleNameAlreadyExist(sampleName, protein, listSampleCreated)) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.shipping.upload.file.data",
									"The sample name already exists: " + sampleName + ", line " + (i + 1)));
							isError = true;
							break;
						}
						sample = sampleService.create(sample);
						listSampleCreated.add(sample);
					}
					if (isError) {
						// remove created in db
						for (Iterator<Shipping3VO> ship = listShippingCreated.iterator(); ship.hasNext();) {
							Shipping3VO shipVO = ship.next();
							shippingService.delete(shipVO);
						}
					}
					else{
					messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.upload.shipping"));
					}
				}
			}
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		if (!messages.isEmpty())
			saveMessages(request, messages);
		if (!errors.isEmpty())
			saveErrors(request, errors);

		return display(mapping, actForm, request, in_reponse);
	}

	private Shipping3VO getShipment(List<Shipping3VO> listShipping, Shipping3VO shipping) {
		for (Iterator<Shipping3VO> sh = listShipping.iterator(); sh.hasNext();) {
			Shipping3VO s = sh.next();
			if (s.getShippingName().equals(shipping.getShippingName())) {
				return s;
			}
		}
		return null;
	}

	private Dewar3VO getDewar(List<Dewar3VO> listdewar, Dewar3VO dewarVO) {
		for (Iterator<Dewar3VO> d = listdewar.iterator(); d.hasNext();) {
			Dewar3VO dewar = d.next();
			if (dewarVO.getShippingVO().getShippingName().equals(dewar.getShippingVO().getShippingName())
					&& dewar.getCode().equals(dewarVO.getCode())) {
				return dewar;
			}
		}
		return null;
	}

	private Container3VO getContainer(List<Container3VO> listContainer, Container3VO containerVO) {
		for (Iterator<Container3VO> c = listContainer.iterator(); c.hasNext();) {
			Container3VO cont = c.next();
			if (containerVO.getDewarVO().getCode().equals(cont.getDewarVO().getCode())
					&& containerVO.getDewarVO().getShippingVO().getShippingName()
							.equals(cont.getDewarVO().getShippingVO().getShippingName())
					&& cont.getCode().equals(containerVO.getCode())) {
				return cont;
			}
		}
		return null;
	}

	public static Crystal3VO getCrystal(List<Crystal3VO> listCrystal, Crystal3VO crystalVO) {
		for (Iterator<Crystal3VO> c = listCrystal.iterator(); c.hasNext();) {
			Crystal3VO crystal = c.next();
			if (crystalVO.getProteinVOId().equals(crystal.getProteinVOId())
					&& crystal.getSpaceGroup() != null
					&& crystal.getSpaceGroup().equals(crystalVO.getSpaceGroup())
					&& ((crystalVO.getCellA() != null && crystalVO.getCellA().equals(crystal.getCellA())) || (crystalVO.getCellA() == null && crystal
							.getCellA() == null))
					&& ((crystalVO.getCellB() != null && crystalVO.getCellB().equals(crystal.getCellB())) || (crystalVO.getCellB() == null && crystal
							.getCellB() == null))
					&& ((crystalVO.getCellC() != null && crystalVO.getCellC().equals(crystal.getCellC())) || (crystalVO.getCellC() == null && crystal
							.getCellC() == null))
					&& ((crystalVO.getCellAlpha() != null && crystalVO.getCellAlpha().equals(crystal.getCellAlpha())) || (crystalVO
							.getCellAlpha() == null && crystal.getCellAlpha() == null))
					&& ((crystalVO.getCellBeta() != null && crystalVO.getCellBeta().equals(crystal.getCellBeta())) || (crystalVO
							.getCellBeta() == null && crystal.getCellBeta() == null))
					&& ((crystalVO.getCellGamma() != null && crystalVO.getCellGamma().equals(crystal.getCellGamma())) || (crystalVO
							.getCellGamma() == null && crystal.getCellGamma() == null))) {
				return crystal;
			}
		}
		return null;
	}

	public static boolean isLocationOccInContainer(Integer samplePosition, List<BLSample3VO> listSampleCreated,
			Container3VO containerVO) {
		for (Iterator<BLSample3VO> s = listSampleCreated.iterator(); s.hasNext();) {
			BLSample3VO sample = s.next();
			if (sample.getContainerVO().getCode().equals(containerVO.getCode())
					&& sample.getContainerVO().getDewarVO().getCode().equals(containerVO.getDewarVO().getCode())
					&& sample.getContainerVO().getDewarVO().getShippingVO().getShippingName()
							.equals(containerVO.getDewarVO().getShippingVO().getShippingName())
					&& Integer.parseInt(sample.getLocation()) == samplePosition) {
				return true;
			}
		}
		return false;
	}

	public static boolean isSampleNameAlreadyExist(String sampleName, Protein3VO proteinVO, List<BLSample3VO> listSampleCreated)
			throws Exception {
		for (Iterator<BLSample3VO> s = listSampleCreated.iterator(); s.hasNext();) {
			BLSample3VO sample = s.next();
			if (sample.getCrystalVO().getProteinVOId() == proteinVO.getProteinId() && sampleName.equals(sample.getName())) {
				return true;
			}
		}
		return false;
	}

}
