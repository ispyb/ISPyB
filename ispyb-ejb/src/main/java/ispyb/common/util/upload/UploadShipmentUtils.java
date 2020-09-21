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
 ******************************************************************************************************************************/

package ispyb.common.util.upload;

import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import ispyb.common.util.Constants;
import ispyb.common.util.DBTools;
import ispyb.common.util.StringUtils;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.services.shipping.Container3Service;
import ispyb.server.common.services.shipping.Shipping3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
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

public class UploadShipmentUtils {

	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	// Define the positions of all the information in this spreadsheet
	// Note: rows and columns start at zero!
	// private static final int checkRow = 0;

	// private static final short checkCol = 0;

	private static final int puckRow = 1;

	private static final short puckCol = 3;

	private static final int dewarRow = 2;

	private static final short dewarCol = puckCol;

	private static final int dataRow = 6;

	private static final short samplePosCol = 0;

	private static final short proteinNameCol = 1;

	private static final short proteinAcronymCol = 2;

	private static final short spaceGroupCol = 3;

	private static final short sampleNameCol = 4;

	private static final short pinBarCodeCol = 5;

	private static final short preObsResolutionCol = 6;

	private static final short neededResolutionCol = 7;

	private static final short preferredBeamCol = 8;

	private static final short experimentTypeCol = 9;

	private static final short nbOfPositionsCol = 10;
	
	private static final short radiationSensitivityCol =11;
	
	private static final short aimedCompletenessCol = 12;
	
	private static final short aimedMultiplicityCol = 13;

	private static final short unitCellACol = 14;

	private static final short unitCellBCol = 15;

	private static final short unitCellCCol = 16;

	private static final short unitCellAlphaCol = 17;

	private static final short unitCellBetaCol = 18;

	private static final short unitCellGammaCol = 19;
	
	private static final short smilesCol = 20;
	
	private static final short minOscWidthCol = 21;

	private static final short commentsCol = 22;

	private static final short courrierNameRow = 1;

	private static final short courrierNameCol = 10;

	private static final short trackingNumberRow = 2;

	private static final short trackingNumberCol = 10;

	private static final short shippingDateRow = 3;

	private static final short shippingDateCol = 10;
	
	private static Double holderLength = new Double(22);
	
	private static String loopType = "Nylon";
	
	private static final Logger LOG = Logger.getLogger(UploadShipmentUtils.class);


	/**
	 * PopulateTemplate
	 * 
	 * @param request
	 * @param getTemplateFullPathOnly
	 * @param getTemplateFilenameOnly
	 * @param populateDMCodes
	 * @param selectedBeamlineName
	 * @param hashDMCodesForBeamline
	 * @param populateForExport
	 * @param nbContainersToExport
	 * @param populateForShipment
	 * @param shippingId
	 * @return
	 */
	public static String PopulateTemplate(HttpServletRequest request, boolean getTemplateFullPathOnly,
			boolean getTemplateFilenameOnly, boolean populateDMCodes, String selectedBeamlineName, List hashDMCodesForBeamline,
			boolean populateForExport, int nbContainersToExport, boolean populateForShipment, int shippingId) {

		String populatedTemplatePath = "";
		try {

			String xlsPath;
			String proposalCode;
			String proposalNumber;
			String populatedTemplateFileName;
			// GregorianCalendar calendar = new GregorianCalendar();
			String today = ".xls";
			if (request != null) {
				xlsPath = Constants.TEMPLATE_POPULATED_RELATIVE_PATH;
				if (populateForShipment)
					xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + Constants.TEMPLATE_XLS_POPULATED_FROM_SHIPMENT;
				else if (populateForExport) {
					switch (nbContainersToExport) {
					case 1:
						xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + Constants.TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME1;
						break;
					case 2:
						xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + Constants.TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME2;
						break;
					case 3:
						xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + Constants.TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME3;
						break;
					case 4:
						xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + Constants.TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME4;
						break;
					case 5:
						xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + Constants.TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME5;
						break;
					case 6:
						xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + Constants.TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME6;
						break;
					case 7:
						xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + Constants.TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME7;
						break;
					case 8:
						xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + Constants.TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME8;
						break;
					case 9:
						xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + Constants.TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME9;
						break;
					case 10:
						xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + Constants.TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME10;
						break;
					}
				}

				proposalCode = (String) request.getSession().getAttribute(Constants.PROPOSAL_CODE);
				proposalNumber = String.valueOf(request.getSession().getAttribute(Constants.PROPOSAL_NUMBER));

				if (Constants.SITE_IS_MAXIV() && proposalCode == "all" && proposalNumber == "manager"){
					Proposal3Service proposalService = (Proposal3Service) Ejb3ServiceLocator.getInstance().getLocalService(
							Proposal3Service.class);
					Proposal3VO p = proposalService.findByPk((int)request.getSession().getAttribute("proposalId"));
					proposalCode = p.getCode();
					proposalNumber = p.getNumber();

				}
				if (populateForShipment)
					populatedTemplateFileName = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + proposalCode + proposalNumber
							+ "_shipment_" + shippingId + today;
				else
					populatedTemplateFileName = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + proposalCode + proposalNumber
							+ ((populateDMCodes) ? "_#" : "") + today;

				populatedTemplatePath = request.getContextPath() + populatedTemplateFileName;

				if (getTemplateFilenameOnly && populateForShipment)
					return proposalCode + proposalNumber + "_shipment_" + shippingId + today;
				if (getTemplateFilenameOnly && !populateForShipment)
					return proposalCode + proposalNumber + ((populateDMCodes) ? "_#" : "") + today;

				String requestPath = request.getRealPath("/");
				xlsPath = requestPath + xlsPath;
				String prefix = new File(xlsPath).getParent();
				populatedTemplateFileName	= (prefix + "/" + new File(populatedTemplateFileName).getName());
			
			} else {
				xlsPath = "C:/" + Constants.TEMPLATE_POPULATED_RELATIVE_PATH;
				proposalCode = "ehtpx";
				proposalNumber = "1";
				populatedTemplateFileName = "C:/" + Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + proposalCode + proposalNumber + today;
			}
			if (getTemplateFullPathOnly)
				return populatedTemplateFileName;

			// Retrieve DM codes from DB
			String beamlineName = selectedBeamlineName;

			String[][] dmCodesinSC = null;

			File originalTemplate = new File(xlsPath);
			File populatedTemplate = new File(populatedTemplateFileName);
			FileUtils.copyFile(originalTemplate, populatedTemplate);
			ISPyBParser parser = new ISPyBParser();

			// Copy template to tmp folder
			File xlsTemplate = new File(xlsPath);
			File xlsPopulatedTemplate = new File(populatedTemplateFileName);
			FileUtils.copyFile(xlsTemplate, xlsPopulatedTemplate);

			// Get the list of Proteins
			Proposal3Service proposalService = (Proposal3Service) Ejb3ServiceLocator.getInstance().getLocalService(
					Proposal3Service.class);
			List<Proposal3VO> proposals = proposalService.findByCodeAndNumber(proposalCode, proposalNumber, false, true, false);
			Proposal3VO proposalLight = proposals.get(0);

			// List<Protein3VO> listProteins = new ArrayList<Protein3VO>(proposalLight.getProteinVOs());
			Protein3Service protein3Service = (Protein3Service) ejb3ServiceLocator.getLocalService(Protein3Service.class);
			List<Protein3VO> listProteins = protein3Service.findByProposalId(proposalLight.getProposalId(), true, true);

			parser.populate(xlsPath, populatedTemplateFileName, listProteins, dmCodesinSC);

			if (populateForShipment)
				parser.populateExistingShipment(populatedTemplateFileName, populatedTemplateFileName, shippingId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return populatedTemplatePath;
	}

	public static String[] importFromXls(InputStream file, Integer shippingId, boolean deleteAllShipment,
			List<String> allowedSpaceGroups) throws Exception {
		String msgError = "";
		String msgWarning = "";
		Protein3Service proteinService = (Protein3Service) ejb3ServiceLocator.getLocalService(Protein3Service.class);
		Crystal3Service crystalService = (Crystal3Service) ejb3ServiceLocator.getLocalService(Crystal3Service.class);

		BLSample3Service sampleService = (BLSample3Service) ejb3ServiceLocator.getLocalService(BLSample3Service.class);

		DiffractionPlan3Service difPlanService = (DiffractionPlan3Service) ejb3ServiceLocator
				.getLocalService(DiffractionPlan3Service.class);

		Container3Service containerService = (Container3Service) ejb3ServiceLocator.getLocalService(Container3Service.class);
		Shipping3Service shippingService = (Shipping3Service) ejb3ServiceLocator.getLocalService(Shipping3Service.class);

		Shipping3VO shipment = shippingService.findByPk(shippingId, true);
		Set<Dewar3VO> dewars = shipment.getDewarVOs();

		HSSFWorkbook workbook = null;
		Integer sheetProposalId = DBTools.getProposalIdFromShipping(shippingId);

		String courrierName = "";
		String shippingDate = "";
		String trackingNumber = "";

		POIFSFileSystem fs = new POIFSFileSystem(file);
		// Now extract the workbook
		workbook = new HSSFWorkbook(fs);

		// Working through each of the worksheets in the spreadsheet
		// ASSUMPTION: one excel file = one shipment
		for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
			boolean sheetIsEmpty = true;
			HSSFSheet sheet = workbook.getSheetAt(sheetNum);

			// DeliveryAgent ----
			// --- Retrieve Shipment related information
			if (sheetNum == 0) {
				if (sheet.getRow(courrierNameRow).getCell(courrierNameCol) == null) {
					msgError += "The format of the xls file is incorrect (courrier name missing)";
					String[] msg = new String[2];
					msg[0] = msgError;
					msg[1] = msgWarning;
					return msg;
				}
				courrierName = (sheet.getRow(courrierNameRow).getCell(courrierNameCol)).toString();
				if (sheet.getRow(shippingDateRow).getCell(shippingDateCol) == null) {
					msgError += "The format of the xls file is incorrect (shipping Date missing)";
					String[] msg = new String[2];
					msg[0] = msgError;
					msg[1] = msgWarning;
					return msg;
				}
				shippingDate = (sheet.getRow(shippingDateRow).getCell(shippingDateCol)).toString();
				if (sheet.getRow(trackingNumberRow).getCell(trackingNumberCol) == null) {
					msgError += "The format of the xls file is incorrect (tracking number missing)";
					String[] msg = new String[2];
					msg[0] = msgError;
					msg[1] = msgWarning;
					return msg;
				}
				trackingNumber = (sheet.getRow(trackingNumberRow).getCell(trackingNumberCol)).toString();

				// retrieveShippingId(file);

				DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
				Date shipDate = null;
				Calendar shipCal = Calendar.getInstance();
				try {
					shipDate = fmt.parse(shippingDate);
					shipCal.setTime(shipDate);
				} catch (Exception e) {
					shipCal = Calendar.getInstance();
				}
				shipment.setDeliveryAgentAgentCode(trackingNumber);
				shipment.setDeliveryAgentAgentName(courrierName);
				shipment.setDeliveryAgentShippingDate(shipDate);
			}
			// Dewar
			String dewarCode = (sheet.getRow(dewarRow).getCell(dewarCol)).toString().trim();
			Integer dewarId = null;
			Dewar3VO dewar = null;
			// check if dewar exists
			for (Dewar3VO dewar3vo : dewars) {
				if (dewar3vo.getCode().equals(dewarCode)) {
					dewarId = dewar3vo.getDewarId();
					dewar = dewar3vo;
					break;
				}
			}

			if (dewar == null) {
				msgError += "Dewar with code '" + dewarCode + "' does not correspond to any dewar. Please check the dewar's name.\n";
				sheetIsEmpty = true;
			} else {
				// Puck
				Container3VO newContainer = new Container3VO();
				newContainer.setDewarVO(dewar);
				newContainer.setContainerType(Constants.CONTAINER_TYPE_SPINE);
				newContainer.setCode((sheet.getRow(puckRow).getCell(puckCol)).toString().trim());
				newContainer.setCapacity(Constants.BASKET_SAMPLE_CAPACITY);
				newContainer.setTimeStamp(StringUtils.getCurrentTimeStamp());
				if (!deleteAllShipment) {
					// check sheet empty before
					boolean sheetEmpty = true;
					for (int i = dataRow; i < dataRow + Constants.BASKET_SAMPLE_CAPACITY; i += 1) {
						boolean sampleRowOK = true;
						String puckCode = cellToString(sheet.getRow(puckRow).getCell(puckCol));
						String proteinAcronym = cellToString(sheet.getRow(i).getCell(proteinAcronymCol));
						String sampleName = cellToString(sheet.getRow(i).getCell(sampleNameCol));
						boolean sampleNameRulesOk = sampleName.matches(Constants.MASK_BASIC_CHARACTERS_WITH_DASH_UNDERSCORE_NO_SPACE);

						if (puckCode.isEmpty() || dewarCode.isEmpty() || proteinAcronym.isEmpty() || sampleName.isEmpty()
								|| !sampleNameRulesOk) {
							sampleRowOK = false;
						}
						if (!sampleRowOK) {
							// Skip this line we do not create the sample
						} else {
							sheetEmpty = false;
							break;
						}
					}
					List<Container3VO> listContainerFromDB = containerService.findByCode(dewar.getDewarId(), newContainer.getCode());
					if (listContainerFromDB != null && listContainerFromDB.size() > 0 && !sheetEmpty) { // delete it in
																										// order to be
																										// replaced by
																										// the new one
						containerService.deleteByPk(listContainerFromDB.get(0).getContainerId());
						msgWarning += "The Puck " + newContainer.getCode() + " has been deleted and a new one has been added.";
					}
				}
				Container3VO container = containerService.create(newContainer);
				// List<Crystal3VO> listCrystalCreated = new ArrayList<Crystal3VO>();
				List<Crystal3VO> listCrystalCreated = crystalService.findByProposalId(sheetProposalId);
				// TBD: need to add sanity check that this puck has not already been put in the dewar!
				
				Set<BLSample3VO> sampleVOs = new HashSet();
				
				// count the samples to decide if SPINE or UNIPUCK
				int nbSamplesInPuck = 0;

				for (int i = dataRow; i < dataRow + Constants.BASKET_SAMPLE_CAPACITY; i += 1) {
					
					// --- Retrieve interesting values from spreadsheet
					String puckCode = cellToString(sheet.getRow(puckRow).getCell(puckCol));
					//String proteinName = cellToString(sheet.getRow(i).getCell(proteinNameCol));
					String proteinName ="";
					String proteinAcronym = cellToString(sheet.getRow(i).getCell(proteinAcronymCol));
					String samplePos = cellToString(sheet.getRow(i).getCell(samplePosCol));
					String sampleName = cellToString(sheet.getRow(i).getCell(sampleNameCol));
					String pinBarCode = cellToString(sheet.getRow(i).getCell(pinBarCodeCol));
					double preObsResolution = cellToDouble(sheet.getRow(i).getCell(preObsResolutionCol));
					double neededResolution = cellToDouble(sheet.getRow(i).getCell(neededResolutionCol));
					double preferedBeamDiameter = cellToDouble(sheet.getRow(i).getCell(preferredBeamCol));
					String experimentType = cellToString(sheet.getRow(i).getCell(experimentTypeCol));
					int nbOfPositions = cellToInt(sheet.getRow(i).getCell(nbOfPositionsCol));
					String spaceGroup = cellToString(sheet.getRow(i).getCell(spaceGroupCol)).toUpperCase().trim().replace(" ", "");
					double unitCellA = cellToDouble(sheet.getRow(i).getCell(unitCellACol));
					double radiationSensitivity = cellToDouble(sheet.getRow(i).getCell(radiationSensitivityCol));
					double aimedCompleteness = cellToDouble(sheet.getRow(i).getCell(aimedCompletenessCol));
					double aimedMultiplicity = cellToDouble(sheet.getRow(i).getCell(aimedMultiplicityCol));
					double unitCellB = cellToDouble(sheet.getRow(i).getCell(unitCellBCol));
					double unitCellC = cellToDouble(sheet.getRow(i).getCell(unitCellCCol));
					double unitCellAlpha = cellToDouble(sheet.getRow(i).getCell(unitCellAlphaCol));
					double unitCellBeta = cellToDouble(sheet.getRow(i).getCell(unitCellBetaCol));
					double unitCellGamma = cellToDouble(sheet.getRow(i).getCell(unitCellGammaCol));
					String sampleComments = cellToString(sheet.getRow(i).getCell(commentsCol));
					String smiles = cellToString(sheet.getRow(i).getCell(smilesCol));
					double minOscWidth = cellToDouble(sheet.getRow(i).getCell(minOscWidthCol));

					// Fill in values by default
					// Protein Name
					if (proteinName.equalsIgnoreCase(""))
						proteinName = proteinAcronym;

					// --- Check the Sheet is not empty for this line and all required fields are present ---
					boolean sampleRowOK = true;
					boolean sampleNameRulesOk = sampleName.matches(Constants.MASK_BASIC_CHARACTERS_WITH_DASH_UNDERSCORE_NO_SPACE);
					if (puckCode.isEmpty() || dewarCode.isEmpty() || proteinAcronym.isEmpty() || sampleName.isEmpty()
							|| sampleName.length() > Constants.BLSAMPLE_NAME_NB_CHAR || !sampleNameRulesOk) {
						sampleRowOK = false;
						if (!(sampleName.isEmpty() && proteinAcronym.isEmpty())) {
							msgError += "Error with the sample: " + sampleName;
							if (puckCode.isEmpty()) {
								msgError += " (The puck code is empty)";
							}
							if (dewarCode.isEmpty()) {
								msgError += " (The dewar code is empty)";
							}
							if (proteinAcronym.isEmpty()) {
								msgError += " (The protein acronym is empty)";
							}
							if (sampleName.isEmpty()) {
								msgError += " (The sample name is empty)";
							}
							if (sampleName.length() > Constants.BLSAMPLE_NAME_NB_CHAR) {
								msgError += " (The sample name is too long : max 8 characters)";
							}
							if (!sampleNameRulesOk) {
								msgError += " (The sample name is not well formatted)";
							}
							msgError += "\n.";
						}
					}

					if (!sampleRowOK) {
						// Skip this line we do not create the sample
					} else {
						sheetIsEmpty = false;
						nbSamplesInPuck = nbSamplesInPuck + 1;
						
						String crystalID = UUID.randomUUID().toString();
						// String diffractionPlanUUID = uuidGenerator.generateRandomBasedUUID().toString();
						if ((null != crystalID) && (!crystalID.equals(""))) {
							// Parse ProteinAcronym - SpaceGroup
							// Pre-filled spreadsheet (including protein_acronym - SpaceGroup)
							int separatorStart = proteinAcronym.indexOf(Constants.PROTEIN_ACRONYM_SPACE_GROUP_SEPARATOR);
							if (separatorStart != -1) {
								String acronym = proteinAcronym.substring(0, separatorStart);
								String prefilledSpaceGroup = proteinAcronym.substring(separatorStart
										+ Constants.PROTEIN_ACRONYM_SPACE_GROUP_SEPARATOR.length(), proteinAcronym.length());
								proteinAcronym = acronym;
								if (allowedSpaceGroups.contains(spaceGroup.toUpperCase())) {
									// Do nothing = use spaceGroup from dropdown list
								} else if (allowedSpaceGroups.contains(prefilledSpaceGroup.toUpperCase())) {
									// Used pre-filled space group
									spaceGroup = prefilledSpaceGroup;
								}
							}
							// Protein
							// We might eventually want to include more details in the spreadsheet, but for the time
							// being
							// just the name is sufficient.
							List<Protein3VO> proteinTab = proteinService.findByAcronymAndProposalId(sheetProposalId, proteinAcronym);
							if (proteinTab == null || proteinTab.size() == 0) {
								msgError += "Protein '" + proteinAcronym + "' can't be found \n ";
							} else {
								Protein3VO protein = proteinTab.get(0);
								// unique sample name
								List<BLSample3VO> samplesWithSameName = sampleService.findByNameAndProteinId(sampleName,
										protein.getProteinId(), shippingId);

								boolean validName = true;
								if (!samplesWithSameName.isEmpty()) {
									validName = false;
									msgError += "[" + protein.getAcronym() + " + " + sampleName
											+ "] is already existing, and should be unique.\n";
								}
								if (validName) {
									// Diffraction Plan
									DiffractionPlan3VO difPlan = new DiffractionPlan3VO();

									difPlan.setNumberOfPositions(nbOfPositions);
									difPlan.setObservedResolution(preObsResolution);
									difPlan.setRequiredResolution(neededResolution);
									difPlan.setExposureTime((double) 0);
									difPlan.setPreferredBeamDiameter(preferedBeamDiameter);
									if (experimentType == null || experimentType.isEmpty()) {
										experimentType = "Default";
									}
									difPlan.setExperimentKind(experimentType);
									difPlan.setRadiationSensitivity(radiationSensitivity);
									difPlan.setAimedCompleteness(aimedCompleteness);
									difPlan.setAimedMultiplicity(aimedMultiplicity);
									difPlan.setMinOscWidth(minOscWidth);
									difPlan = difPlanService.create(difPlan);

									// Crystal
									Crystal3VO crystal = new Crystal3VO();
									crystal.setProteinVO(protein);
									crystal.setDiffractionPlanVO(difPlan);
									crystal.setCrystalUUID(crystalID);
									crystal.setSpaceGroup(spaceGroup);
									if ((crystal.getSpaceGroup() == null) || (crystal.getSpaceGroup().equals(""))) {
										crystal.setSpaceGroup("Undefined");
									} else {

										// TODO SD in the case where space group is not empty and no cell dimensions
										// have been
										// entered,
										// fill the crystal with the default value of the crystal = protein + space
										// group
										List<Crystal3VO> tab = crystalService.findFiltered(sheetProposalId, null, proteinAcronym,
												spaceGroup);
										if (tab != null && tab.size() > 0) {
											Crystal3VO newCrystal3VO = new Crystal3VO();
											int j = 0;
											for (Crystal3VO crystal3vo : tab) {
												newCrystal3VO = tab.get(j);
												j = j + 1;
											}

											if (newCrystal3VO != null && unitCellA == 0 && unitCellB == 0 && unitCellC == 0
													&& unitCellAlpha == 0 && unitCellBeta == 0 && unitCellGamma == 0) {
												unitCellA = (newCrystal3VO.getCellA() == null ? 0 : newCrystal3VO.getCellA());
												unitCellB = (newCrystal3VO.getCellB() == null ? 0 : newCrystal3VO.getCellB());
												unitCellC = (newCrystal3VO.getCellC() == null ? 0 : newCrystal3VO.getCellC());
												unitCellAlpha = (newCrystal3VO.getCellAlpha() == null ? 0 : newCrystal3VO
														.getCellAlpha());
												unitCellBeta = (newCrystal3VO.getCellBeta() == null ? 0 : newCrystal3VO.getCellBeta());
												unitCellGamma = (newCrystal3VO.getCellGamma() == null ? 0 : newCrystal3VO
														.getCellGamma());
											}
										}
									}
									// crystal.setResolution(preObsResolution);
									// Create the crystal name from the uuid and ligandid
									String crystalName = crystal.getCrystalUUID();
									crystal.setName(crystalName);
									crystal.setCellA(unitCellA);
									crystal.setCellB(unitCellB);
									crystal.setCellC(unitCellC);
									crystal.setCellAlpha(unitCellAlpha);
									crystal.setCellBeta(unitCellBeta);
									crystal.setCellGamma(unitCellGamma);
									// crystal = crystalService.create(crystal);
									Crystal3VO crystalC = getCrystal(listCrystalCreated, crystal);
									if (crystalC == null) {
										crystal = crystalService.create(crystal);
										listCrystalCreated.add(crystal);

									} else {
										crystal = crystalC;
									}
									if (!crystal.hasCellInfo()) {
										msgWarning += "Warning: the unit cell parameters are not filled for the spaceGroup "
												+ crystal.getSpaceGroup() + " (" + proteinAcronym + ")!";
									}
									// And add the crystal to the list
									// addCrystal(crystal);
									// Holder
									BLSample3VO holder = new BLSample3VO();
									holder.setCrystalVO(crystal);
									holder.setName(sampleName);
									holder.setCode(pinBarCode);
									holder.setLocation(samplePos);
									holder.setSmiles(smiles);

									// ASSUMPTION: holder is SPINE standard!
									holder.setHolderLength(holderLength);
									holder.setLoopLength(0.5);
									holder.setLoopType(loopType);
									holder.setWireWidth(0.010);
									holder.setComments(sampleComments);
									// Add holder to the container...
									
									holder.setDiffractionPlanVO(difPlan);
									holder = sampleService.create(holder);							
									sampleVOs.add(holder);
									
									LOG.debug("sample created in container : " + container.getContainerId());
								} // end validName
							} // end protein
						}// end crystalID
					} // end sampleRowOK
				} // for dataRow
				
				container.setSampleVOs(sampleVOs);
				containerService.update(container);
				
				if (nbSamplesInPuck > Constants.SPINE_SAMPLE_CAPACITY || Constants.SITE_IS_MAXIV()) {
					container.setContainerType(Constants.CONTAINER_TYPE_UNIPUCK);
					container.setCapacity(Constants.UNIPUCK_SAMPLE_CAPACITY);
				} else { 
					container.setContainerType(Constants.CONTAINER_TYPE_SPINE);
					container.setCapacity(Constants.SPINE_SAMPLE_CAPACITY);
				}
				containerService.update(container);
				
				// all samples were empty
				if (sheetIsEmpty) {
					if (container != null) {
						// remove the container
						containerService.deleteByPk(container.getContainerId());
					}
				}
				
			} // end dewar != null
						
		}
		String[] msg = new String[2];
		msg[0] = msgError;
		msg[1] = msgWarning;
		return msg;
	}

	/**
	 * Converts from Excel Cell contents to a String
	 * 
	 * @param cell
	 *            The Cell to convert
	 * @return A String value of the contents of the cell
	 */
	public static String cellToString(HSSFCell cell) {
		String retVal = "";
		if (cell == null) {
			return retVal;
		}
		if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			retVal = cell.getStringCellValue();
		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			retVal = String.valueOf(new Double(cell.getNumericCellValue()).intValue());
		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
			if (new Boolean(cell.getBooleanCellValue()) == Boolean.TRUE) {
				retVal = "true";
			} else {
				retVal = "false";
			}
		}
		return retVal;
	}

	/**
	 * Converts from Excel Cell contents to a double
	 * 
	 * @param cell
	 *            The Cell to convert
	 * @return The double value contained within the Cell or 0.0 if the Cell is not the correct type or is undefined
	 */
	public static double cellToDouble(HSSFCell cell) {
		Double retVal = new Double(0.0);
		if (cell == null) {
			return retVal.doubleValue();
		}
		if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			retVal = new Double(cell.getNumericCellValue());
		}
		return retVal.doubleValue();
	}
	
	/**
	 * Converts from Excel Cell contents to a double
	 * 
	 * @param cell
	 *            The Cell to convert
	 * @return The double value contained within the Cell or 0.0 if the Cell is not the correct type or is undefined
	 */
	public static int cellToInt(HSSFCell cell) {
		Double retVal = new Double(0);
		if (cell == null) {
			return retVal.intValue();
		}
		if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			retVal = new Double(cell.getNumericCellValue());
		}
		return retVal.intValue();
	}
	
	public static Crystal3VO getCrystal(List<Crystal3VO> listCrystal, Crystal3VO crystalVO) {
		for (Iterator<Crystal3VO> c = listCrystal.iterator(); c.hasNext();) {
			Crystal3VO crystal = c.next();
			if (crystalVO.getProteinVOId() != null && crystalVO.getProteinVOId().equals(crystal.getProteinVOId())
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

}
