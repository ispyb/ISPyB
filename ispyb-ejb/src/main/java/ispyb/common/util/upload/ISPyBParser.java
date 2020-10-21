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
 * eHTPXXLSParser.java
 * 
 * Created on 10 October 2006, 09:15
 * Updated 29/10/2009 - PBU - Code formatting
 */

package ispyb.common.util.upload;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import ispyb.common.util.Constants;
import ispyb.common.util.DBTools;
import ispyb.common.util.upload.ShippingInformation.DewarInformation;
import ispyb.common.util.upload.ShippingInformation.DewarInformation.ContainerInformation;
import ispyb.common.util.upload.ShippingInformation.DewarInformation.ContainerInformation.SampleInformation;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.sample.DiffractionPlan3Service;
import ispyb.server.mx.services.sample.Protein3Service;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.DiffractionPlan3VO;
import ispyb.server.mx.vos.sample.Protein3VO;

/**
 * The instance class for parsing eHTPX style Excel Spreadsheets
 * 
 * @author IMB
 */
public class ISPyBParser extends XLSParser {
	private final Logger LOG = Logger.getLogger(ISPyBParser.class);

	/**
	 * Creates a new instance of eHTPXXLSParser and initialises the JAXB Object Factory
	 * 
	 * @throws javax.xml.bind.JAXBException
	 *             If there is a problem binding to the JAXB process
	 */

	private HSSFWorkbook mWorkbook = null;
	
	private final String PWD = "ispyb";

	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	public ISPyBParser() throws JAXBException {
		super();
	}

	/**
	 * main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ISPyBParser parser = new ISPyBParser();
			// parser.open("V:\\home\\launer\\test4.xls");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Define the positions of all the information in this spreadsheet
	// Note: rows and columns start at zero!
	private static final int checkRow = 0;

	private static final short checkCol = 0;

	private static final int puckRow = 1;

	private static final short puckCol = 3;

	private static final int dewarRow = 2;

	private static final short dewarCol = puckCol;

	private static final String ShippingLabel = "Shipping Id";

	private static final String ProposalAndShippingLabel = "Proposal Id / Shipping Id";

	private static final int idLabelRow = 3;

	private static final short idLabelCol = puckCol - 1;

	private static final int value1IdRow = 3;

	private static final short value1IdCol = puckCol;

	private static final int value2IdRow = 3;

	private static final short value2IdCol = puckCol + 1;

	private static final int dataRow = 6;

	private static final short samplePosCol = 0;

	private static final short proteinNameCol = 1;

	private static final short proteinAcronymCol = 2;

	private static final short spaceGroupCol = 3;

	private static final short sampleNameCol = 4;

	private static final short pinBarCodeCol = 5;

	private static final short preObsResolutionCol = 6;

	private static final short neededResolutionCol = 7;

	private static final short experimentTypeCol = 9;

	private static final short preferredBeamCol = 8;

	private static final short nbOfPositionsCol = 10;
	
	private static final short radiationSensitivityCol =11;
	
	private static final short requiredCompletenessCol = 12;
	
	private static final short requiredMultiplicityCol = 13;

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

	// private static final short ligandIDCol = 20;
	// private static final short screenedCol = 21;
	// private static final short annealableCol = 22;
	// private static final short crystallographerCol = 23;
	// private static final short observationsCol = 24;
	// private static final short priorityCol = 25;
	// private static final short scheduledCol = 26;
	// private static final short observedResolutionCol = 27;
	//
	// private static final short DCol = 28;
	// private static final short timeCol = 29;
	// private static final short DfCol = 30;
	// private static final short FTotalCol = 31;
	// private static final short collectedCol = 32;
	// private static final short dataSetIDCol = 33;
	// private static final short processedCol = 34;
	// private static final short archivedCol = 35;
	// private static final short spreadsheetCol = 36;

	//private static final short proteinAcronymRow = 49;
	
	private static final short proteinAcronymRow = 56;

	/**
	 * export
	 * 
	 * @param fullFileName
	 * @param populatedTemplateFileName
	 * @param shippingInformation
	 * @throws XlsUploadException
	 * @throws Exception
	 */
	public void export(String fullFileName, String populatedTemplateFileName, ShippingInformation shippingInformation)
			throws XlsUploadException, Exception {

		// Create new Excel filesystem to read from
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(populatedTemplateFileName));
		HSSFWorkbook workbook = null;
		try {
			// Now extract the workbook
			workbook = new HSSFWorkbook(fs);
		} catch (org.apache.poi.hssf.record.RecordFormatException rfe) {
			XlsUploadException ex = new XlsUploadException(
					"[Known APACHE-POI limitation...sorry]A  worksheet in the file has a drop-down list selected",
					"Check all Worksheets in your file and make sure no drop-down list is selected");
			throw ex;
		}

		HSSFSheet sheet = null;

		int nbSheetsInFile = workbook.getNumberOfSheets();
		int nbSheetsInInfo = DBTools.GetNumberOfContainers(shippingInformation) - 1;
		int nbSheetsToDelete = nbSheetsInFile - nbSheetsInInfo;
		int i;
		// Create Additional Sheets if needed
		if (nbSheetsToDelete > 0) {
			for (i = nbSheetsInFile - 1; i >= nbSheetsInFile - nbSheetsToDelete; i--) {
				// workbook.removeSheetAt(i);
			}
		}
		// Populate Sheet
		int currentSheetNumber = -1;
		HSSFRow row = null;
		HSSFCell cell = null;

		try {

			DiffractionPlan3Service difPlanService = (DiffractionPlan3Service) ejb3ServiceLocator
					.getLocalService(DiffractionPlan3Service.class);

			for (int d = 0; d < shippingInformation.getListDewars().size(); d++) {

				// Dewar
				DewarInformation dewar = shippingInformation.getListDewars().get(d);

				// Container
				for (int c = 0; c < dewar.getListContainers().size(); c++) {
					currentSheetNumber++;
					sheet = workbook.getSheetAt(currentSheetNumber);

					ContainerInformation container = dewar.getListContainers().get(c);

					// Populate Courrier
					row = sheet.getRow(courrierNameRow);
					cell = row.getCell(courrierNameCol);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(new HSSFRichTextString(shippingInformation.getShipping()
							.getDeliveryAgentAgentName()));

					row = sheet.getRow(trackingNumberRow);
					cell = row.getCell(trackingNumberCol);
					cell.setCellValue(new HSSFRichTextString(shippingInformation.getShipping()
							.getDeliveryAgentAgentCode()));

					row = sheet.getRow(shippingDateRow);
					cell = row.getCell(shippingDateCol);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(shippingInformation.getShipping().getDeliveryAgentShippingDate());

					// Populate Puck
					row = sheet.getRow(puckRow);
					cell = row.getCell(puckCol);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(new HSSFRichTextString(container.getContainer().getCode()));

					// Populate Dewar
					row = sheet.getRow(dewarRow);
					cell = row.getCell(dewarCol);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(new HSSFRichTextString(dewar.getDewar().getCode()));

					// Sample
					for (int s = 0; s < container.getListSamples().size(); s++) {
						SampleInformation sample = container.getListSamples().get(s);
						Crystal3VO crystal = sample.getCrystal();
						Protein3VO protein = sample.getProtein();
						DiffractionPlan3VO diffractionPlan = null;

						// DiffractionPlanLightValue diffractionPlan = sample.getSample().getDiffractionPlan();
						if (sample.getSample().getDiffractionPlanVOId() != null)
							diffractionPlan = difPlanService.findByPk(sample.getSample().getDiffractionPlanVOId(),
									false, false);
						else if (diffractionPlan == null)
							diffractionPlan = difPlanService.findByPk(crystal.getDiffractionPlanVOId(), false, false);

						int currentRow = dataRow + s;
						// Try to extract Sample Location
						Integer locationIncontainer = null;
						try {
							String _locationInContainer = sample.getSample().getLocation();
							locationIncontainer = Integer.parseInt(_locationInContainer);
						} catch (Exception e) {
						}
						if (locationIncontainer != null && locationIncontainer <= Constants.BASKET_SAMPLE_CAPACITY) {
							currentRow = dataRow + locationIncontainer - 1;
						}

						// Protein acronym - SpaceGroup
						row = sheet.getRow(currentRow);
						cell = row.getCell(proteinAcronymCol);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(new HSSFRichTextString(protein.getAcronym() + " - " + crystal.getSpaceGroup()));

						// Sample Name
						row = sheet.getRow(currentRow);
						cell = row.getCell(sampleNameCol);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(new HSSFRichTextString(sample.getSample().getName()));

						// Pin Barcode
						row = sheet.getRow(currentRow);
						cell = row.getCell(pinBarCodeCol);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(new HSSFRichTextString(sample.getSample().getCode()));

						// Pre-observed resolution
						row = sheet.getRow(currentRow);
						cell = row.getCell(preObsResolutionCol);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						if (diffractionPlan != null && diffractionPlan.getObservedResolution() != null)
							cell.setCellValue(diffractionPlan.getObservedResolution());
						else
							cell.setCellValue(new HSSFRichTextString(""));

						// Needed resolution
						row = sheet.getRow(currentRow);
						cell = row.getCell(neededResolutionCol);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						if (diffractionPlan != null && diffractionPlan.getMinimalResolution() != null)
							cell.setCellValue(diffractionPlan.getMinimalResolution());
						else
							cell.setCellValue(new HSSFRichTextString(""));

						// Preferred beam diameter
						row = sheet.getRow(currentRow);
						cell = row.getCell(preferredBeamCol);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						if (diffractionPlan != null) {
							if (diffractionPlan.getPreferredBeamDiameter() != null)
								cell.setCellValue(diffractionPlan.getPreferredBeamDiameter());
						} else
							cell.setCellValue(new HSSFRichTextString(""));

						// Experiment Type
						row = sheet.getRow(currentRow);
						cell = row.getCell(experimentTypeCol);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						if (diffractionPlan != null && diffractionPlan.getExperimentKind() != null)
							cell.setCellValue(new HSSFRichTextString(diffractionPlan.getExperimentKind()));
						else
							cell.setCellValue(new HSSFRichTextString(Constants.LIST_EXPERIMENT_KIND[0]));
						//
						// // Anomalous Scatterer
						// row = sheet.getRow(currentRow);
						// cell = row.getCell(anomalousScattererCol);
						// cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						// if (crystal.getAnomalousScatterers().length>0) {
						// AnomalousScattererLightValue an = crystal.getAnomalousScatterers()[0];
						// cell.setCellValue(new HSSFRichTextString(an.getElement()));
						// }
						// else
						// cell.setCellValue(new HSSFRichTextString(""));

						// Unit Cell a
						row = sheet.getRow(currentRow);
						cell = row.getCell(unitCellACol);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue((crystal.getCellA() != null) ? crystal.getCellA() : 0);

						// Unit Cell b
						row = sheet.getRow(currentRow);
						cell = row.getCell(unitCellBCol);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue((crystal.getCellB() != null) ? crystal.getCellB() : 0);

						// Unit Cell c
						row = sheet.getRow(currentRow);
						cell = row.getCell(unitCellCCol);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue((crystal.getCellC() != null) ? crystal.getCellC() : 0);

						// Unit Cell alpha
						row = sheet.getRow(currentRow);
						cell = row.getCell(unitCellAlphaCol);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue((crystal.getCellAlpha() != null) ? crystal.getCellAlpha() : 0);

						// Unit Cell beta
						row = sheet.getRow(currentRow);
						cell = row.getCell(unitCellBetaCol);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue((crystal.getCellBeta() != null) ? crystal.getCellBeta() : 0);

						// Unit Cell gamma
						row = sheet.getRow(currentRow);
						cell = row.getCell(unitCellGammaCol);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue((crystal.getCellGamma() != null) ? crystal.getCellGamma() : 0);

						// LoopType
//						row = sheet.getRow(currentRow);
//						cell = row.getCell(loopTypeCol);
//						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//						cell.setCellValue(new HSSFRichTextString(sample.getSample().getLoopType()));

						// HolderLength
//						row = sheet.getRow(currentRow);
//						cell = row.getCell(holderLengthCol);
//						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
//						cell.setCellValue(sample.getSample().getHolderLength());

						// SMILES
						row = sheet.getRow(currentRow);
						cell = row.getCell(smilesCol);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(new HSSFRichTextString(sample.getSample().getSmiles()));
						
						// min osc width
						row = sheet.getRow(currentRow);
						cell = row.getCell(minOscWidthCol);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						if (diffractionPlan != null) {
							if (diffractionPlan.getMinOscWidth() != null)
								cell.setCellValue(diffractionPlan.getMinOscWidth());
						} else
							cell.setCellValue(new HSSFRichTextString(""));

						// Comments
						row = sheet.getRow(currentRow);
						cell = row.getCell(commentsCol);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(new HSSFRichTextString(sample.getSample().getComments()));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Save Populated template
		FileOutputStream fout = new FileOutputStream(populatedTemplateFileName);
		workbook.write(fout);
		fout.close();
	}

	/**
	 * PopulateExistingShipment
	 * 
	 * @param templateFileName
	 * @param populatedTemplateFileName
	 * @param shippingId
	 * @throws XlsUploadException
	 * @throws Exception
	 */
	public void populateExistingShipment(String templateFileName, String populatedTemplateFileName, int shippingId)
			throws XlsUploadException, Exception {

		// Create new Excel filesystem to read from
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(templateFileName));
		HSSFWorkbook workbook = null;
		ShippingInformation shippingInformation = DBTools.getShippingInformation(shippingId);

		try {
			// Now extract the workbook
			workbook = new HSSFWorkbook(fs);
			int nbDewars = shippingInformation.getListDewars().size();
			int nbSheetsForDewar = 7;
			int nbSheetstoCopy = (nbDewars == 0) ? 0 : (nbDewars * nbSheetsForDewar) - 1;

			// Copy right number of sheets = 1 per dewar
			for (int d = 1; d <= nbSheetstoCopy; d++) {
				workbook.cloneSheet(0);
			}

			// Populate Sheet
			for (int dew = 0; dew < nbDewars; dew++) {
				int sheetStart = (dew == 0) ? 0 : (dew * nbSheetsForDewar);
				int sheetStop = ((dew + 1) * nbSheetsForDewar) - 1;
				int puckNumber = 1;
				for (int s = sheetStart; s <= sheetStop; s++) {
					String dewarCode = shippingInformation.getListDewars().get(dew).dewar.getCode();
					if (dewarCode == null || dewarCode.trim().equalsIgnoreCase(""))
						dewarCode = Integer.toString(dew);

					String puckCode = "Puck" + puckNumber;

					// Populate
					workbook.setSheetName(s, dewarCode + "_" + puckNumber);
					HSSFSheet sheet = workbook.getSheetAt(s);

					if (!Constants.SITE_IS_MAXIV()) {
						sheet.protectSheet(PWD);
					}

					// Dewar Code
					HSSFRow row = sheet.getRow(dewarRow);
					if (row == null)
						row = sheet.createRow(dewarRow);
					HSSFCell cell = row.getCell(dewarCol);
					if (cell == null)
						cell = row.createCell(dewarCol);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(new HSSFRichTextString(dewarCode));

					// Puck Code
					row = sheet.getRow(puckRow);
					if (row == null)
						row = sheet.createRow(puckRow);
					cell = row.getCell(puckCol);
					if (cell == null)
						cell = row.createCell(puckCol);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(new HSSFRichTextString(puckCode));

					// LabelCode
					row = sheet.getRow(idLabelRow);
					if (row == null)
						row = sheet.createRow(idLabelRow);
					cell = row.getCell(idLabelCol);
					if (cell == null)
						cell = row.createCell(idLabelCol);
					cell.setCellValue(new HSSFRichTextString(ProposalAndShippingLabel));

					// ProposalId
					Integer proposalId = shippingInformation.getShipping().getProposalVOId();
					row = sheet.getRow(value1IdRow);
					if (row == null)
						row = sheet.createRow(value1IdRow);
					cell = row.getCell(value1IdCol);
					if (cell == null)
						cell = row.createCell(value1IdCol);
					cell.setCellValue(proposalId);

					// ShippingId
					row = sheet.getRow(value2IdRow);
					if (row == null)
						row = sheet.createRow(value2IdRow);
					cell = row.getCell(value2IdCol);
					if (cell == null)
						cell = row.createCell(value2IdCol);
					cell.setCellValue(shippingId);

					// Courrier Name
					String courrierName = shippingInformation.getShipping().getDeliveryAgentAgentName();
					row = sheet.getRow(courrierNameRow);
					if (row == null)
						row = sheet.createRow(courrierNameRow);
					cell = row.getCell(courrierNameCol);
					if (cell == null)
						cell = row.createCell(courrierNameCol);
					cell.setCellValue(new HSSFRichTextString(courrierName));

					// Tracking Number
					String trackingNumber = shippingInformation.getShipping().getDeliveryAgentAgentCode();
					row = sheet.getRow(trackingNumberRow);
					if (row == null)
						row = sheet.createRow(trackingNumberRow);
					cell = row.getCell(trackingNumberCol);
					if (cell == null)
						cell = row.createCell(trackingNumberCol);
					cell.setCellValue(new HSSFRichTextString(trackingNumber));

					// Shipping Date
					Date _shippingDate = shippingInformation.getShipping().getDeliveryAgentShippingDate();
					String shippingDate = "";
					if (_shippingDate != null)
						shippingDate = _shippingDate.getDay() + "/" + _shippingDate.getMonth() + "/"
								+ (_shippingDate.getYear() + 1900);
					row = sheet.getRow(shippingDateRow);
					if (row == null)
						row = sheet.createRow(shippingDateRow);
					cell = row.getCell(shippingDateCol);
					if (cell == null)
						cell = row.createCell(shippingDateCol);
					cell.setCellValue(new HSSFRichTextString(shippingDate));

					//sheet.setProtect(true);
					if (!Constants.SITE_IS_MAXIV()) {
						sheet.protectSheet(PWD);
					}
					puckNumber++;
				}
			}
		} catch (org.apache.poi.hssf.record.RecordFormatException rfe) {
			XlsUploadException ex = new XlsUploadException(
					"[Known APACHE-POI limitation...sorry]A  worksheet in the file has a drop-down list selected",
					"Check all Worksheets in your file and make sure no drop-down list is selected");
			throw ex;
		}

		// ave Populated template
		FileOutputStream fout = new FileOutputStream(populatedTemplateFileName);
		workbook.write(fout);
		fout.flush();
		fout.close();
	}

	/**
	 * populate
	 * 
	 * @param templateFileName
	 * @param populatedTemplateFileName
	 * @param listProteins
	 * @param dmCodesinSC
	 * @throws XlsUploadException
	 * @throws Exception
	 */
	public void populate(String templateFileName, String populatedTemplateFileName, List listProteins,
			String[][] dmCodesinSC) throws XlsUploadException, Exception {

		// Create new Excel filesystem to read from
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(templateFileName));
		HSSFWorkbook workbook = null;
		try {
			workbook = new HSSFWorkbook(fs); // Now extract the workbook
		} catch (org.apache.poi.hssf.record.RecordFormatException rfe) {
			XlsUploadException ex = new XlsUploadException(
					"[Known APACHE-POI limitation...sorry]A  worksheet in the file has a drop-down list selected",
					"Check all Worksheets in your file and make sure no drop-down list is selected");
			throw ex;
		}
		Protein3Service proteinService = (Protein3Service) Ejb3ServiceLocator.getInstance().getLocalService(
				Protein3Service.class);

		HSSFSheet sheet = null;
		for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
			sheet = workbook.getSheetAt(sheetNum);
			Iterator it = listProteins.iterator();
			int currentRow = this.proteinAcronymRow;
			List<String> listProtein = new ArrayList<String>();
			while (it.hasNext()) {
				Protein3VO protein = (Protein3VO) it.next();
				// protein = proteinService.loadEager(protein);
				Crystal3VO[] crystals = protein.getCrystals();

				// Retrieve Xtals for SpaceGroup
				for (int c = 0; c < crystals.length; c++) {
					String acronym = protein.getAcronym();
					Crystal3VO xtal = crystals[c];
					// Replace database empty values by 'Undefined'
					if (xtal.getSpaceGroup() != null && !xtal.getSpaceGroup().equals("")) {
						acronym += Constants.PROTEIN_ACRONYM_SPACE_GROUP_SEPARATOR + xtal.getSpaceGroup();
					} else {
						acronym += Constants.PROTEIN_ACRONYM_SPACE_GROUP_SEPARATOR + "Undefined";
					}
					// Add to list (but don't duplicate)
					if (!listProtein.contains(acronym)) {
						listProtein.add(acronym);
						// Populate Acronym - SpaceGroup
						try {
							HSSFRow row = sheet.getRow(currentRow);
							if (row == null)
								row = sheet.createRow(currentRow);
							HSSFCell cell = row.getCell(proteinAcronymCol);
							if (cell == null)
								cell = row.createCell(proteinAcronymCol);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue(acronym);
							currentRow++;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		// Populate DM Codes
		if (dmCodesinSC != null) {
			for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
				sheet = workbook.getSheetAt(sheetNum);
				int basketLocation = sheetNum + 1;
				for (int sampleLocation = 0; sampleLocation < Constants.BASKET_SAMPLE_CAPACITY; sampleLocation++) {
					int rowNumber = dataRow + sampleLocation;
					String dmCode = dmCodesinSC[sheetNum + 1][sampleLocation + 1];
					HSSFRow row = sheet.getRow(rowNumber);
					if (row == null)
						row = sheet.createRow(rowNumber);
					HSSFCell cell = row.getCell(pinBarCodeCol);
					if (cell == null)
						cell = row.createCell(pinBarCodeCol);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(new HSSFRichTextString(dmCode));
				}
			}
		}

		// Save Populated template
		FileOutputStream fout = new FileOutputStream(populatedTemplateFileName);
		workbook.write(fout);
		fout.flush();
		fout.close();
	}

	/**
	 * Validate
	 * 
	 * @param file
	 * @param _listProteinAcronym_SampleName
	 * @param proposalId
	 * @return
	 * @throws XlsUploadException
	 * @throws Exception
	 */
	@Override
	public List validate(InputStream file, Hashtable _listProteinAcronym_SampleName, Integer proposalId)
			throws XlsUploadException, Exception {

		Hashtable<String, Hashtable<String, Integer>> listProteinAcronym_SampleName = _listProteinAcronym_SampleName;

		// Create new Excel filesystem to read from
		POIFSFileSystem fs = new POIFSFileSystem(file);
		HSSFWorkbook workbook = null;
		HashMap usedSampleNames = new HashMap();
		HashMap usedPuckCodes = new HashMap();
		HashMap usedDMCodes = new HashMap();

		Integer shippingId = null;

		Hashtable<String, Hashtable<String, String>> listSampleName_ProteinAcronym_InSpreadsheet = new Hashtable<String, Hashtable<String, String>>();
		try {
			workbook = new HSSFWorkbook(fs); // Now extract the workbook
			this.mWorkbook = workbook;
		} catch (org.apache.poi.hssf.record.RecordFormatException rfe) {
			XlsUploadException ex = new XlsUploadException(
					"[Known APACHE-POI limitation...sorry]A  worksheet in the file has a drop-down list selected",
					"Check all Worksheets in your file and make sure no drop-down list is selected");
			this.getValidationErrors().add(ex);
			// No need to keep on validating !
			return this.getValidationErrors();
		}

		// Check the Shipment belongs to the right Proposal
		HSSFSheet firstsheet = workbook.getSheetAt(0);
		String idLabel = cellToString(firstsheet.getRow(idLabelRow).getCell(idLabelCol));

		// Check proposalId based on file proposalId
		if (idLabel != null && idLabel.trim().equalsIgnoreCase(ProposalAndShippingLabel)) {
			boolean proposalOK = true;
			Integer sheetProposalId = Integer.decode(cellToString(firstsheet.getRow(value1IdRow).getCell(value1IdCol)));
			try {
				if (proposalId.intValue() != sheetProposalId.intValue())
					proposalOK = false;
			} catch (Exception e) {
				proposalOK = false;
			}
			if (!proposalOK) {
				this.getValidationErrors().add(
						new XlsUploadException(
								"Current Proposal <> XLS sheet Proposal : " + DBTools.GetProposalName(proposalId)
										+ "<>" + DBTools.GetProposalName(sheetProposalId),
								"Check the XLS template was created for the right Proposal"));
				return this.getValidationErrors();
			}
		}
		// Check proposalId based on file shippingId (for compatibility reasons)
		if (idLabel != null && idLabel.trim().equalsIgnoreCase(ShippingLabel)) {
			Integer sheetProposalId = null;
			boolean proposalOK = true;
			String _shippingId = cellToString(firstsheet.getRow(value1IdRow).getCell(value1IdCol));
			try {
				shippingId = Integer.decode(_shippingId);
				sheetProposalId = DBTools.getProposalIdFromShipping(shippingId);
				if (proposalId.intValue() != sheetProposalId.intValue())
					proposalOK = false;
			} catch (Exception e) {
				proposalOK = false;
			}
			if (!proposalOK)
				this.getValidationErrors().add(
						new XlsUploadException("Current Proposal <> XLS sheet Proposal : " + proposalId + "<>"
								+ sheetProposalId, "Check the XLS template was created for the right Proposal"));
		}

		for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
			HSSFSheet sheet = workbook.getSheetAt(sheetNum);

			// // Version n-1
			// if (cellToString(sheet.getRow(checkRow).getCell(checkCol)).indexOf(Constants.TEMPLATE_VERSION_N_1)==-1) {
			// XlsUploadException ex = new XlsUploadException( "The XLS template you are using is obsolete and will no
			// longer be supported in a near future.",
			// "Download and use latest template");
			// this.getValidationErrors().add(ex);
			// }

			// Current Version
			try {
				if (cellToString(sheet.getRow(checkRow).getCell(checkCol)).indexOf(Constants.TEMPLATE_VERSION) == -1) {
					XlsUploadException ex = new XlsUploadException("The XLS template does not have the right version",
							"Download and use latest template");
					this.getValidationErrors().add(ex);
					// No need to keep on validating !
					return this.getValidationErrors();
				}
			} catch (Exception e) {
				XlsUploadException ex = new XlsUploadException(
						"A problem occured while reading XLS template version on sheet #" + sheetNum,
						"Download and use latest template");
				this.getValidationErrors().add(ex);
				// No need to keep on validating !
				return this.getValidationErrors();
			}

			// Dewar + Container
			String puckCode = cellToString(sheet.getRow(puckRow).getCell(puckCol));
			String dewarCode = cellToString(sheet.getRow(dewarRow).getCell(dewarCol));

			if (puckCode == "")
				this.getValidationErrors().add(
						new XlsUploadException("Puck name is empty for worksheet : " + workbook.getSheetName(sheetNum),
								"Fill in Puck name on top of the page"));
			if (dewarCode == "")
				this.getValidationErrors().add(
						new XlsUploadException(
								"Dewar name is empty for worksheet : " + workbook.getSheetName(sheetNum),
								"Fill in Dewar name on top of the page"));
			// PuckCode not used twice
			if (puckCode != "" && usedPuckCodes.containsKey(puckCode)) {
				// PuckCode already used
				this.getValidationErrors().add(
						new XlsUploadException("Puck Code already used : " + puckCode + " ("
								+ usedSampleNames.get(puckCode) + ")", "Change Puck Code"));
			} else if (puckCode != "") {
				// PuckCode is new
				usedSampleNames.put(puckCode, "Worksheet: " + workbook.getSheetName(sheetNum));
			}

			boolean emptySheet = true;
			// Reset list of Sample Names
			usedSampleNames = new HashMap();

			for (int i = dataRow; i < dataRow + Constants.BASKET_SAMPLE_CAPACITY; i += 1) {

				// Retrieve interesting values from spreadsheet
				String samplePos = cellToString(sheet.getRow(i).getCell(samplePosCol));
				String proteinAcronym = cellToString(sheet.getRow(i).getCell(proteinAcronymCol));
				String sampleName = cellToString(sheet.getRow(i).getCell(sampleNameCol));
				String spaceGroup = cellToString(sheet.getRow(i).getCell(spaceGroupCol)).toUpperCase().trim()
						.replace(" ", "");
				String dmCode = cellToString(sheet.getRow(i).getCell(pinBarCodeCol)).toUpperCase().trim()
						.replace(" ", "");

				int separatorStart = proteinAcronym.indexOf(Constants.PROTEIN_ACRONYM_SPACE_GROUP_SEPARATOR);
				if (separatorStart != -1) {
					proteinAcronym = proteinAcronym.substring(0, separatorStart);
				}

				// SampleName + ProteinAcronym
				if (proteinAcronym != "" && sampleName != "")
					emptySheet = false;

				if (proteinAcronym == "" && sampleName != "")
					this.getValidationErrors().add(
							new XlsUploadException("Protein Acronym is empty for worksheet : "
									+ workbook.getSheetName(sheetNum) + " Sample Position: " + samplePos,
									"Fill in Protein Acronym"));
				if (sampleName == "" && proteinAcronym != "")
					this.getValidationErrors().add(
							new XlsUploadException("Sample Name is empty for worksheet : "
									+ workbook.getSheetName(sheetNum) + " Sample Position: " + samplePos,
									"Fill in Sample Name"));
				// SampleName does not contain forbidden characters
				if (sampleName != "" && !sampleName.matches(Constants.MASK_SHIPMENT_LEGAL_CHARACTERS)) {
					this.getValidationErrors().add(
							new XlsUploadException("Sample Name contains forbidden characters : '" + sampleName
									+ "' : for worksheet: " + workbook.getSheetName(sheetNum) + " Sample Position: "
									+ samplePos, "Use any of the following characters only : "
									+ Constants.MASK_SHIPMENT_LEGAL_CHARACTERS));
				}

				// DM code not used twice ---
				if (!dmCode.equalsIgnoreCase("") && usedDMCodes.containsKey(dmCode)) {
					// SampleName already used
					this.getValidationErrors().add(
							new XlsUploadException("Pin Barcode already used : " + dmCode + " ("
									+ usedDMCodes.get(dmCode) + ")", "Change Pin Barcode"));
				} else if (!sampleName.equalsIgnoreCase("")) {
					// SampleName is new
					usedDMCodes.put(dmCode, "Worksheet: " + workbook.getSheetName(sheetNum) + " Sample Position: "
							+ samplePos);
				}

				// Space Group
				if (spaceGroup != "" && !SPACE_GROUPS.contains(spaceGroup)) {
					this.getValidationErrors().add(
							new XlsUploadException("In worksheet " + workbook.getSheetName(sheetNum)
									+ " Spacegroup is unknown: " + spaceGroup,
									"Make sure you're using a value from the drop-down list"));
				}

				// (SampleName,Protein Acronym) not used twice
				// Check in ISPyB
				if (sampleName != "" && proteinAcronym != "") {
					if (listProteinAcronym_SampleName.containsKey(proteinAcronym)) {
						// Protein Acronym already used
						Hashtable<String, Integer> listSampleName = listProteinAcronym_SampleName.get(proteinAcronym);
						if (listSampleName.containsKey(sampleName)) {
							// Sample Name
							this.getValidationErrors().add(
									new XlsUploadException("<Sample Name>_<Protein Acronym> : " + sampleName + "_"
											+ proteinAcronym + " : already used for your Proposal in ISPyB: sheet "
											+ workbook.getSheetName(sheetNum) + " Sample Position: " + samplePos,
											"Change Sample Name"));
						}
					}
					// DLS ######
					else if (Constants.ALLOWED_TO_CREATE_PROTEINS) {
						// Allow to add unknown proteins
						LOG.debug("protein acronym '" + proteinAcronym + "' is unknown and will be created");
					} else {
						// Don't allow to add unknown proteins
						LOG.error("unknown protein acronym '" + proteinAcronym + "'");
						this.getValidationErrors().add(
								new XlsUploadException("In worksheet " + workbook.getSheetName(sheetNum)
										+ " Protein Acronym is unknown: '" + proteinAcronym + "'",
										"Make sure you're using a value from the drop-down list"));
					}
				}
				// Check in Spreadsheet
				if (sampleName != "" && proteinAcronym != "") {
					if (listSampleName_ProteinAcronym_InSpreadsheet.containsKey(sampleName)) {
						// SampleName used
						Hashtable<String, String> listProteinAcronym = listSampleName_ProteinAcronym_InSpreadsheet
								.get(sampleName);
						if (listProteinAcronym.containsKey(proteinAcronym)) {
							// SampleName + ProteinAcronym already used !
							this.getValidationErrors().add(
									new XlsUploadException("<Sample Name>_<Protein Acronym> : " + sampleName + "_"
											+ proteinAcronym + " : already used inside the spreadsheet: sheet "
											+ workbook.getSheetName(sheetNum) + " Sample Position: " + samplePos,
											"Change Sample Name"));
						} else {
							// ProteinAcronym not used for this SampleName
							listProteinAcronym.put(proteinAcronym, "In worksheet " + workbook.getSheetName(sheetNum)
									+ " Sample Position: " + samplePos);
							listSampleName_ProteinAcronym_InSpreadsheet.put(sampleName, listProteinAcronym);
						}
					} else {
						// SampleName not used, create entry
						Hashtable<String, String> listProteinAcronym = new Hashtable<String, String>();
						listProteinAcronym.put(proteinAcronym, "In worksheet " + workbook.getSheetName(sheetNum)
								+ " Sample Position: " + samplePos);
						listSampleName_ProteinAcronym_InSpreadsheet.put(sampleName, listProteinAcronym);
					}
				}
			}

			// --- Empty Sheet ---
			if (emptySheet) {
				this.getValidationWarnings().add(
						new XlsUploadException("Worksheet is empty : " + workbook.getSheetName(sheetNum),
								"It will not be uploaded"));
			} else {
				this.getValidationWarnings().add(
						new XlsUploadException("Worksheet has data : " + workbook.getSheetName(sheetNum),
								"It will be uploaded"));
			}
		}
		return this.getValidationErrors();
	}

	/**
	 * RetrieveShippingId
	 * 
	 * @param file
	 * @throws Exception
	 */
	@Override
	public void retrieveShippingId(InputStream file) throws Exception {

		HSSFWorkbook workbook = null;
		String _shippingId = "";
		Integer shippingId = null;

		if (this.mWorkbook == null) {
			// Load from file
			// Create new Excel filesystem to read from
			POIFSFileSystem fs = new POIFSFileSystem(file);
			// Now extract the workbook
			workbook = new HSSFWorkbook(fs);
			this.mWorkbook = workbook;
		} else {
			// Use pre-loaded file
			workbook = this.mWorkbook;
		}

		HSSFSheet sheet = workbook.getSheetAt(0);
		String idLabel = cellToString(sheet.getRow(idLabelRow).getCell(idLabelCol));
		if (idLabel != null && idLabel.trim().equalsIgnoreCase(ProposalAndShippingLabel))
			_shippingId = cellToString(sheet.getRow(value2IdRow).getCell(value2IdCol));
		else if (idLabel != null && idLabel.trim().equalsIgnoreCase(ShippingLabel))
			_shippingId = cellToString(sheet.getRow(value1IdRow).getCell(value1IdCol));
		else
			_shippingId = "";

		try {
			shippingId = Integer.decode(_shippingId);
			this.setShippingId(shippingId);
		} catch (Exception e) {
			shippingId = null;
		}
	}

	/**
	 * Uses the input stream to extract the information from the spreadsheet
	 * 
	 * @param file
	 *            The input stream to use
	 * @throws java.lang.Exception
	 *             when there is a problem
	 */
	// @Override
	// public void open(InputStream file) throws Exception {
	//
	// HSSFWorkbook workbook = null;
	// List<String> allowedSpaceGroups = DBTools.getAllowedSpaceGroups();
	// Integer sheetProposalId = DBTools.getProposalIdFromShipping(this.getShippingId());
	//
	// String courrierName = "";
	// String shippingDate = "";
	// String trackingNumber = "";
	//
	// if (this.mWorkbook == null) {
	// // Load from file
	// // Create new Excel filesystem to read from
	// POIFSFileSystem fs = new POIFSFileSystem(file);
	// // Now extract the workbook
	// workbook = new HSSFWorkbook(fs);
	// this.mWorkbook = workbook;
	// } else {
	// // Use pre-loaded file
	// workbook = this.mWorkbook;
	// }
	//
	// // As we will need it later, create a UUID Generator...
	// UUIDGenerator uuidGenerator = UUIDGenerator.getInstance();
	//
	// // Working through each of the worksheets in the spreadsheet
	// // ASSUMPTION: one excel file = one shipment
	// for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
	// boolean sheetIsEmpty = true;
	// HSSFSheet sheet = workbook.getSheetAt(sheetNum);
	//
	// // DeliveryAgent ----
	// // --- Retrieve Shipment related information
	// if (sheetNum == 0) {
	// courrierName = cellToString(sheet.getRow(courrierNameRow).getCell(courrierNameCol));
	// shippingDate = cellToString(sheet.getRow(shippingDateRow).getCell(shippingDateCol));
	// trackingNumber = cellToString(sheet.getRow(trackingNumberRow).getCell(trackingNumberCol));
	//
	// retrieveShippingId(file);
	//
	// DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
	// Date shipDate = null;
	// Calendar shipCal = Calendar.getInstance();
	// try {
	// shipDate = fmt.parse(shippingDate);
	// shipCal.setTime(shipDate);
	// } catch (Exception e) {
	// shipCal = Calendar.getInstance();
	// }
	//
	// DeliveryAgent deliveryAgent = getObjFactory().createDeliveryAgent();
	// deliveryAgent.setAgentCode(trackingNumber);
	// deliveryAgent.setAgentName(courrierName);
	// deliveryAgent.setShippingDate(shipCal);
	// deliveryAgent.setDeliveryDate(Calendar.getInstance());
	//
	// this.setDeliveryAgent(deliveryAgent);
	// }
	//
	// // Dewar
	// Dewar dewar = getObjFactory().createDewar();
	// dewar.setBarCode(cellToString(sheet.getRow(dewarRow).getCell(dewarCol)).trim());
	// // This is an internal function, which checks whether this dewar has already been created elsewhere....
	// // for example in a different worksheet
	// dewar = addDewar(dewar);
	//
	// // Puck
	// Container container = getObjFactory().createContainer();
	// container.setType("Puck");
	// container.setCode(cellToString(sheet.getRow(puckRow).getCell(puckCol)).trim());
	// container.setCapacity(Constants.BASKET_SAMPLE_CAPACITY);
	// // TBD: need to add sanity check that this puck has not already been put in the dewar!
	//
	// for (int i = dataRow; i < dataRow + Constants.BASKET_SAMPLE_CAPACITY; i += 1) {
	// // --- Retrieve interesting values from spreadsheet
	// String puckCode = cellToString(sheet.getRow(puckRow).getCell(puckCol));
	// String dewarCode = cellToString(sheet.getRow(dewarRow).getCell(dewarCol));
	// String proteinName = cellToString(sheet.getRow(i).getCell(proteinNameCol));
	// String proteinAcronym = cellToString(sheet.getRow(i).getCell(proteinAcronymCol));
	// String samplePos = cellToString(sheet.getRow(i).getCell(samplePosCol));
	// String sampleName = cellToString(sheet.getRow(i).getCell(sampleNameCol));
	// String pinBarCode = cellToString(sheet.getRow(i).getCell(pinBarCodeCol));
	// double preObsResolution = cellToDouble(sheet.getRow(i).getCell(preObsResolutionCol));
	// double neededResolution = cellToDouble(sheet.getRow(i).getCell(neededResolutionCol));
	// double oscillationRange = cellToDouble(sheet.getRow(i).getCell(oscillationRangeCol));
	// String experimentType = cellToString(sheet.getRow(i).getCell(experimentTypeCol));
	// String anomalousScatterer = cellToString(sheet.getRow(i).getCell(anomalousScattererCol));
	// String spaceGroup = cellToString(sheet.getRow(i).getCell(spaceGroupCol)).toUpperCase().trim()
	// .replace(" ", "");
	// double unitCellA = cellToDouble(sheet.getRow(i).getCell(unitCellACol));
	// double unitCellB = cellToDouble(sheet.getRow(i).getCell(unitCellBCol));
	// double unitCellC = cellToDouble(sheet.getRow(i).getCell(unitCellCCol));
	// double unitCellAlpha = cellToDouble(sheet.getRow(i).getCell(unitCellAlphaCol));
	// double unitCellBeta = cellToDouble(sheet.getRow(i).getCell(unitCellBetaCol));
	// double unitCellGamma = cellToDouble(sheet.getRow(i).getCell(unitCellGammaCol));
	// String loopType = cellToString(sheet.getRow(i).getCell(loopTypeCol));
	// double holderLength = cellToDouble(sheet.getRow(i).getCell(holderLengthCol));
	// String sampleComments = cellToString(sheet.getRow(i).getCell(commentsCol));
	//
	// // Fill in values by default
	// // Protein Name
	// if (proteinName.equalsIgnoreCase(""))
	// proteinName = proteinAcronym;
	//
	// // --- Check the Sheet is not empty and all required fields are present ---
	// boolean sampleRowOK = true;
	// if (puckCode == "" || dewarCode == "" || proteinAcronym == "" || sampleName == "") {
	// sampleRowOK = false;
	// }
	// if (!sampleRowOK) {
	// // Skip this line
	// } else {
	// sheetIsEmpty = false;
	// String crystalID = uuidGenerator.generateRandomBasedUUID().toString();
	// String diffractionPlanUUID = uuidGenerator.generateRandomBasedUUID().toString();
	// if ((null != crystalID) && (!crystalID.equals(""))) {
	// // Parse ProteinAcronym - SpaceGroup
	// // Pre-filled spreadsheet (including protein_acronym - SpaceGroup)
	// int separatorStart = proteinAcronym.indexOf(Constants.PROTEIN_ACRONYM_SPACE_GROUP_SEPARATOR);
	// if (separatorStart != -1) {
	// String acronym = proteinAcronym.substring(0, separatorStart);
	// String prefilledSpaceGroup = proteinAcronym
	// .substring(
	// separatorStart + Constants.PROTEIN_ACRONYM_SPACE_GROUP_SEPARATOR.length(),
	// proteinAcronym.length());
	// proteinAcronym = acronym;
	// if (allowedSpaceGroups.contains(spaceGroup.toUpperCase())) {
	// // Do nothing = use spaceGroup from dropdown list
	// } else if (allowedSpaceGroups.contains(prefilledSpaceGroup.toUpperCase())) {
	// // Used pre-filled space group
	// spaceGroup = prefilledSpaceGroup;
	// }
	// }
	//
	// // Protein
	// // We might eventually want to include more details in the spreadsheet, but for the time being
	// // just the name is sufficient.
	// Protein protein = getObjFactory().createProtein();
	// protein.setName(proteinName);
	// protein.setAcronym(proteinAcronym);
	// protein.setProteinUUID(uuidGenerator.generateRandomBasedUUID().toString());
	//
	// // Crystal
	// Crystal crystal = getObjFactory().createCrystalDetailsCrystal();
	// crystal.setCrystalUUID(crystalID);
	// crystal.setSpacegroup(spaceGroup);
	// if ((crystal.getSpacegroup() == null) || (crystal.getSpacegroup().equals(""))) {
	// crystal.setSpacegroup("Undefined");
	// } else {
	// Crystal3Service crystalService = (Crystal3Service) ejb3ServiceLocator
	// .getLocalService(Crystal3Service.class);
	// // TODO SD in the case where space group is not empty and no cell dimensions have been
	// // entered,
	// // fill the crystal with the default value of the crystal = protein + space group
	// List col = crystalService.findFiltered(sheetProposalId, null, proteinAcronym, spaceGroup);
	// if (!col.isEmpty()) {
	// Crystal3VO Crystal3VO = new Crystal3VO();
	// for (Iterator iterator = col.iterator(); iterator.hasNext();) {
	// Crystal3VO = (Crystal3VO) iterator.next();
	// }
	// if (unitCellA == 0 && unitCellB == 0 && unitCellC == 0 && unitCellAlpha == 0
	// && unitCellBeta == 0 && unitCellGamma == 0) {
	// unitCellA = Crystal3VO.getCellA();
	// unitCellB = Crystal3VO.getCellB();
	// unitCellC = Crystal3VO.getCellC();
	// unitCellAlpha = Crystal3VO.getCellAlpha();
	// unitCellBeta = Crystal3VO.getCellBeta();
	// unitCellGamma = Crystal3VO.getCellGamma();
	// }
	// }
	// }
	//
	// crystal.setResolution(preObsResolution);
	// crystal.setProtein(protein);
	// // Create the crystal name from the uuid and ligandid
	// String crystalName = crystal.getCrystalUUID();
	// crystal.setName(crystalName);
	// crystal.setA(unitCellA);
	// crystal.setB(unitCellB);
	// crystal.setC(unitCellC);
	// crystal.setAlpha(unitCellAlpha);
	// crystal.setBeta(unitCellBeta);
	// crystal.setGamma(unitCellGamma);
	//
	// // And add the crystal to the list
	// addCrystal(crystal);
	//
	// // Holder
	// Holder holder = getObjFactory().createHolder();
	// holder.setName(sampleName);
	// holder.setCode(pinBarCode);
	// holder.setPosition(samplePos);
	//
	// // Create Identifier elements
	// holder.setCrystalUUID(crystal.getCrystalUUID());
	// uk.ac.ehtpx.model.CrystalIdentifier crystalIdentifier = getObjFactory()
	// .createCrystalIdentifier();
	// crystalIdentifier.setProteinAcronym(proteinAcronym);
	// crystalIdentifier.setSpacegroup(crystal.getSpacegroup());
	// holder.setCrystalIdentifier(crystalIdentifier);
	// holder.setDiffractionPlanUUID(diffractionPlanUUID);
	//
	// // ASSUMPTION: holder is SPINE standard!
	// holder.setHolderLength(holderLength);
	// holder.setLoopLength(0.5);
	// holder.setLoopType(loopType);
	// holder.setWireWidth(0.010);
	// holder.setComments(sampleComments);
	// // Add holder to the container...
	// container.getHolder().add(holder);
	//
	// // Diffraction Plan
	// Exposure exposure = getObjFactory().createExposure();
	// exposure.setPriority(0);
	// exposure.setResolution(neededResolution);
	// exposure.setTime(0);
	// exposure.setAbsorptionEdge("Peak");
	//
	// ScreenPlanElement screenPlan = getObjFactory().createScreenPlanElement();
	// screenPlan.getExposure().add(exposure);
	// screenPlan.setExperimentType(experimentType);
	// screenPlan.setRequestedOverallDResLow(neededResolution);
	// screenPlan.setRequestedOverallDResHigh(neededResolution);
	// screenPlan.setAnomalousScattererElement(anomalousScatterer);
	// screenPlan.setResolutionAtHome(Double.toString(preObsResolution));
	// screenPlan.setNotesComments(sampleComments);
	// screenPlan.setOscillationAnglePerImage(oscillationRange);
	// // LL: Again not sure if these are good default values?
	// screenPlan.setForceOnePassOnly(false);
	// screenPlan.setMultiplicity(0.0);
	// screenPlan.setRequestedMeanIOverSigIAll(0.0);
	// screenPlan.setCollectionStrategy("Rank crystals");
	// screenPlan.setTemperature("100K");
	//
	// // LL: Is there enough information in the spreadsheet to create a CollectPlan?
	// // LL: otherwise where do some columns belong?? You know this area best, so feel free
	// // to mess around with this set up as you need!
	// SweepInformationElement sweepInformation = getObjFactory().createSweepInformationElement();
	// // sweepInformation.setPhiRange(Df);
	// sweepInformation.setPhiRange(0);
	// sweepInformation.setResolution(10.0);
	// // sweepInformation.setPriority(priority);
	// sweepInformation.setPriority(0);
	//
	// // sweepInformation.setPhiEnd(FTotal);
	// sweepInformation.setPhiEnd(0);
	// // sweepInformation.setPhiStart(FTotal);
	// sweepInformation.setPhiStart(0);
	// sweepInformation.setOscillationRange(1.0);
	//
	// CollectPlanElement collectPlan = getObjFactory().createCollectPlanElement();
	// collectPlan.getSweepInformation().add(sweepInformation);
	// // collectPlan.setNotesComments(observations);
	// collectPlan.setExperimentType("MR");
	// collectPlan.setTemperature("100K");
	// collectPlan.setProcessingLevel("Collect only");
	//
	// DiffractionPlanElement diffractionPlan = getObjFactory().createDiffractionPlanElement();
	// diffractionPlan.setDiffractionPlanUUID(diffractionPlanUUID);
	// // Create Identifier elements
	// diffractionPlan.getCrystalUUID().add(crystal.getCrystalUUID());
	// uk.ac.ehtpx.model.CrystalIdentifier crystalIdentifier_DiffPlan = getObjFactory()
	// .createCrystalIdentifier();
	// crystalIdentifier_DiffPlan.setProteinAcronym(proteinAcronym);
	// crystalIdentifier_DiffPlan.setSpacegroup(crystal.getSpacegroup());
	// diffractionPlan.getCrystalIdentifier().add(crystalIdentifier_DiffPlan);
	//
	// diffractionPlan.setScreenPlan(screenPlan);
	// diffractionPlan.setCollectPlan(collectPlan);
	//
	// addDiffractionPlan(diffractionPlan);
	//
	// }
	// }
	// }
	// // Check Container was not empty
	// if (sheetIsEmpty)
	// this.removeDewarIfEmpty(dewar);
	// else
	// dewar.getContainer().add(container);
	// }
	// }

	/**
	 * Used when creating a spreadsheet from known data
	 * 
	 * @param filename
	 *            The filename to write the data to in Excel format
	 * @throws java.lang.Exception
	 *             When there is a problem
	 */
	@Override
	public void write(String filename) throws Exception {
		// TBD: something that will write out the information from the database....
	}
}
