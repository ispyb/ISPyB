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

package ispyb.common.util.export;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.RtfWriter2;

import ispyb.common.util.Constants;
import ispyb.common.util.Formatter;
import ispyb.common.util.PathUtils;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.server.mx.services.collections.Image3Service;
import ispyb.server.mx.services.collections.IspybCrystalClass3Service;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
import ispyb.server.mx.vos.collections.IspybCrystalClass3VO;
import ispyb.server.mx.vos.collections.Session3VO;

/**
 * allows creation of PDF or RTF report - general report for EXI, available in the
 * 
 * @author Solange Delageniere
 * 
 */
public class ExiPdfRtfExporter {

	private final static Logger LOG = Logger.getLogger(ExiPdfRtfExporter.class);

	// constants color
	public final static Color GREEN_COLOR = new Color(17, 197, 3);

	public final static Color RED_COLOR = new Color(202, 58, 2);

	public final static Color WHITE_COLOR = new Color(255, 255, 255);

	public final static Color BLUE_COLOR = new Color(160, 200, 237);

	public final static Color LIGHT_GREY_COLOR = new Color(192, 192, 192);

	public final static Color LIGHT_BLUE_COLOR = new Color(126, 182, 235);

	public final static Color SUBWEDGE_COLOR = new Color(238, 248, 252);

	public final static Color WEDGE_COLOR = new Color(171, 222, 245);

	public final static Color LIGHT_YELLOW_COLOR = new Color(245, 246, 206);

	public final static Color GREEN_COLOR_RANK = new Color(0, 139, 139);

	public final static Color NULL_COLOR_RANK = new Color(104, 136, 168);

	// constant fonts
	public final static Font FONT_HELVETICA_10 = new Font(Font.HELVETICA, 10, Font.BOLD);

	public final static Font VERY_SMALL_FONT = new Font(Font.HELVETICA, 2);

	public final static Font FONT_SPACE = new Font(Font.HELVETICA, 6);

	public final static Font FONT_TITLE = new Font(Font.HELVETICA, 8, Font.BOLD | Font.UNDERLINE);

	public final static Font FONT_TITLE_11 = new Font(Font.HELVETICA, 11, Font.BOLD | Font.UNDERLINE);

	public final static Font FONT_DOC = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.BLACK);
	
	public final static Font FONT_DOC_BLUE = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.BLUE);

	public final static Font FONT_DOC_ITALIC = new Font(Font.HELVETICA, 8, Font.ITALIC, Color.BLACK);

	public final static Font FONT_DOC_11 = new Font(Font.HELVETICA, 11, Font.NORMAL, Color.BLACK);

	public final static Font FONT_DOC_11_ITALIC = new Font(Font.HELVETICA, 11, Font.ITALIC, Color.BLACK);

	public final static Font FONT_DOC_EXPONENT = new Font(Font.HELVETICA, 11, Font.NORMAL, Color.BLACK);

	public final static Font FONT_DOC_EXPONENT_BLUE = new Font(Font.HELVETICA, 11, Font.NORMAL, Color.BLUE);

	public final static Font FONT_DOC_BOLD = new Font(Font.HELVETICA, 8, Font.BOLD);
	public final static Font FONT_DOC_PARAM_TITLE = new Font(Font.HELVETICA, 8, Font.NORMAL, LIGHT_GREY_COLOR);

	public final static Font FONT_INDEXING_NOTDONE = new Font(Font.HELVETICA, 8, Font.NORMAL, LIGHT_GREY_COLOR);

	public final static Font FONT_INDEXING_FAILED = new Font(Font.HELVETICA, 8, Font.NORMAL, RED_COLOR);

	public final static Font FONT_INDEXING_SUCCESS = new Font(Font.HELVETICA, 8, Font.NORMAL, GREEN_COLOR);

	public final static int NB_COL_DATACOLLECTION = 8;

	public final static int SIZE_FONT = 8;

	public final static int SMALL_FONT = 6;

	public final static int TEXT_RISE_EXP = 2;

	public final static int TEXT_RISE_SUB = -2;

	public final static int NB_DATA_COLLECTION_PER_PAGE = 2;

	public final static float INDENTATION_LEFT = 10f;

	public final static float GREY_FILL_DATA = 0.99f;

	public final static float GREY_FILL_DATA_COLLECT = 0.8f;

	public final static float GREY_FILL_HEADER = 0.6f;

	// images size
	public final static float CRYSTAL_IMAGE_WIDTH = 281;

	public final static float CRYSTAL_IMAGE_HEIGHT = 174;

	public final static float IMAGE_HEIGHT = 120;

	// public final static float CRYSTAL_IMAGE_WIDTH = 160;
	// public final static float CRYSTAL_IMAGE_HEIGHT = 99;
	public final static float DIFF_IMAGE_WIDTH = 174;

	public final static float DIFF_IMAGE_HEIGHT = 174;

	// proposal
	String proposalDesc;

	// session info
	Integer sessionId;
	
	// nb row to export
	Integer nbRowsMax;
	
	private Session3VO slv;


	// dataCollectionViewObject List
	List<Map<String, Object>> dataCollections = new ArrayList<Map<String, Object>>();

	DecimalFormat df2;

	DecimalFormat df3;

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
	
	private Session3Service sessionService;
	
	private DataCollection3Service dcService;
	
	private Image3Service imageService;
		
	public ExiPdfRtfExporter(String proposalDesc, Integer sessionId,
			List<Map<String, Object>> dataCollections, Integer nbRowsMax) throws Exception {
		this.proposalDesc = proposalDesc;
		this.sessionId = sessionId;
		this.dataCollections = dataCollections;
		this.nbRowsMax = nbRowsMax;
		init();
	}

	private void init() throws Exception {
		df2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df2.applyPattern("#####0.00");
		df3 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df3.applyPattern("#####0.000");

		sessionService = (Session3Service) ejb3ServiceLocator
				.getLocalService(Session3Service.class);
		dcService = (DataCollection3Service) ejb3ServiceLocator
				.getLocalService(DataCollection3Service.class);
		imageService = (Image3Service) ejb3ServiceLocator
				.getLocalService(Image3Service.class);
		
		slv = sessionService.findByPk(sessionId, false/*withDataCollectionGroup*/, false/*withEnergyScan*/, false/*withXFESpectrum*/);

	}

	/**
	 * export datacollection report
	 * 
	 * @param rtfFormat
	 * @return
	 * @throws Exception
	 */
	public ByteArrayOutputStream exportDataCollectionReport(boolean rtfFormat) throws Exception {
		
		this.init();
		// create simple doc and write to a ByteArrayOutputStream
		Document document = new Document(PageSize.A4.rotate(), 20, 20, 20, 20);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (!rtfFormat) {
			PdfWriter.getInstance(document, baos);
		} else {
			RtfWriter2.getInstance(document, baos);
		}

		// =============================
		// Header + footer
		// =============================

		setHeader(document);
		setFooter(document);
		document.open();

		// ===============================
		// Body
		// ===============================
		// Crystallographer added only for IFX proposal in case of MXPress
		// experiment
		setCrystallographer(document);
		// Session comments
		setSessionComments(document);
		// session title& info
		setSessionTable(document);

		// ======================
		// Data Collection table
		// ======================
		document.add(new Paragraph(" "));
		setDataCollectionTable(document);

		// ======================
		// End of file
		// ======================
		document.close();
		return baos;

	}

	/**
	 * sets the header in the specified document
	 * 
	 * @param document
	 * @throws Exception
	 */
	private void setHeader(Document document) throws Exception {
		HeaderFooter header;
		String h = "Data Collections for Proposal: " + proposalDesc;
		if (slv != null) {
			h += " on Beamline: " + (slv.getBeamlineName() == null ? "" : slv.getBeamlineName())
					+ "  ---  Session start date: "
					+ (slv.getStartDate() == null ? "" : Formatter.formatDate(slv.getStartDate()));
		}
		header = new HeaderFooter(new Phrase(h, FONT_HELVETICA_10), false);
		header.setAlignment(Element.ALIGN_CENTER);
		header.setBorderWidth(1);
		header.getBefore().getFont().setSize(SIZE_FONT);
		document.setHeader(header);
	}

	/**
	 * set footer in the specified document
	 * 
	 * @param document
	 * @throws Exception
	 */
	private void setFooter(Document document) throws Exception {
		HeaderFooter footer = new HeaderFooter(new Phrase("Page n."), true);
		footer.setAlignment(Element.ALIGN_RIGHT);
		footer.setBorderWidth(1);
		footer.getBefore().getFont().setSize(SMALL_FONT);
		document.setFooter(footer);
	}

	/**
	 * set the crystallographer - only for IFX proposal
	 * 
	 * @param document
	 * @throws Exception
	 */
	private void setCrystallographer(Document document) throws Exception {
		// Crystallographer added only for IFX proposal in case of MXPress
		// experiment
		if (proposalDesc.toLowerCase().contains(Constants.PROPOSAL_CODE_FX) && slv != null) {
			String beamlineOp = "";
			if (slv.getBeamlineOperator() != null) {
				beamlineOp = slv.getBeamlineOperator();
			}
			document.add(new Paragraph("Crystallographer:", FONT_TITLE));
			document.add(new Paragraph(beamlineOp, FONT_DOC));
		}
	}

	/**
	 * set sessions comments
	 * 
	 * @param document
	 * @throws Exception
	 */
	private void setSessionComments(Document document) throws Exception {
		if (slv != null) {
			document.add(new Paragraph(Constants.SESSION_VISIT_CAP + " comments:", FONT_TITLE));
			document.add(new Paragraph(slv.getComments(), FONT_DOC));
		}
	}

	/***
	 * sets the sessions informations in the pdf document for fx or ix accounts
	 * (Issue 1049)
	 * 
	 * @param document
	 * @throws Exception
	 */
	private void setSessionTable(Document document) throws Exception {
		String proposalCode = proposalDesc.substring(0, 2);
		if (slv != null
				&& (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX) || proposalCode
						.equals(Constants.PROPOSAL_CODE_IX))) {
			if (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX)) { // session
																					// title
																					// only
																					// for
																					// FX
																					// accounts
				document.add(new Paragraph("Session title:", FONT_TITLE));
				document.add(new Paragraph(slv.getSessionTitle(), FONT_DOC));
			}
			Table sessionTable = new Table(2);
			// sessionTable.setWidth(50); // percentage
			sessionTable.setPadding(3);
			sessionTable.setCellsFitPage(true);
			sessionTable.getDefaultCell().setBorderWidth(0);
			sessionTable.setBorder(0);
			sessionTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			boolean hasData = false;
			// print only if the value > 0
			if (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX)) { // structure
																					// determinations
																					// only
																					// for
																					// FX
																					// accounts
				if (slv.getStructureDeterminations() != null && !slv.getStructureDeterminations().isNaN()
						&& slv.getStructureDeterminations() != 0) {
					hasData = true;
					sessionTable.addCell(new Paragraph("Structure determinations", FONT_DOC_BOLD));
					sessionTable.addCell(new Paragraph("" + slv.getStructureDeterminations(), FONT_DOC));
				}
			}
			if (slv.getDewarTransport() != null && !slv.getDewarTransport().isNaN() && slv.getDewarTransport() != 0) {
				hasData = true;
				sessionTable.addCell(new Paragraph("Dewar transport", FONT_DOC_BOLD));
				sessionTable.addCell(new Paragraph("" + slv.getDewarTransport(), FONT_DOC));
			}
			if (slv.getDatabackupFrance() != null && !slv.getDatabackupFrance().isNaN()
					&& slv.getDatabackupFrance() != 0) {
				hasData = true;
				sessionTable.addCell(new Paragraph("Data backup & Express delivery France", FONT_DOC_BOLD));
				sessionTable.addCell(new Paragraph("" + slv.getDatabackupFrance(), FONT_DOC));
			}
			if (slv.getDatabackupEurope() != null && !slv.getDatabackupEurope().isNaN()
					&& slv.getDatabackupEurope() != 0) {
				hasData = true;
				sessionTable.addCell(new Paragraph("Data backup & Express delivery Europe", FONT_DOC_BOLD));
				sessionTable.addCell(new Paragraph("" + slv.getDatabackupEurope(), FONT_DOC));
			}
			if (hasData) {
				document.add(sessionTable);
			}
		}
	}

	/**
	 * set the dataCollection table
	 * 
	 * @param document
	 * @throws Exception
	 */
	private void setDataCollectionTable(Document document) throws Exception {
		document.add(new Paragraph("Data Collections:", FONT_TITLE));
		document.add(new Paragraph(" "));
		if (dataCollections.isEmpty()) {
			document.add(new Paragraph("There is no data collection in this report", FONT_DOC));
		} else {
			document.add(new Paragraph(" "));
			
			// need the list of DCgroups for crystal class summary
			Map<String, String> mapDataCollectionGroupIdCClass = new HashMap<String, String>();

			// DataCollection Rows
			Iterator<Map<String, Object>> it = dataCollections.iterator();
			int i = 0;
			while (it.hasNext() && i < nbRowsMax) {
				Map<String, Object> dataCollectionMapData = it.next();
				LOG.info("dcMap=" + dataCollectionMapData.toString());
				setDataCollectionMapData(document, dataCollectionMapData);
				
				if (getCellParam(dataCollectionMapData, "DataCollectionGroup_crystalClass") != null) {
					String dcgId = getCellParam(dataCollectionMapData, "DataCollectionGroup_dataCollectionGroupId");
					if (!mapDataCollectionGroupIdCClass.containsKey(dcgId)){
							mapDataCollectionGroupIdCClass.put(dcgId, getCellParam(dataCollectionMapData, "DataCollectionGroup_crystalClass"));
					}
				}					
				i++;
			}
			setCrystalClassSummary(document, mapDataCollectionGroupIdCClass);
			document.add(new Paragraph(" "));
		}
		document.add(new Paragraph(" "));			
			
	}

	/**
	 * set a line for a specified dataCollection in the dataCollection table
	 * 
	 * @param document
	 * @param table
	 * @param col
	 * @param session
	 * @param df2
	 * @param df3
	 * @throws Exception
	 */
	private void setDataCollectionMapData(Document document, Map<String, Object> dataCollectionMapItem) throws Exception {

		// 1st row
		String parag = getCellParam(dataCollectionMapItem, "DataCollectionGroup_experimentType") 
				+ " " + getCellParam(dataCollectionMapItem, "DataCollection_startTime");
		Paragraph p = new Paragraph(parag, FONT_DOC_BLUE);
		document.add(p);
		
		//row2		
		parag = getCellParam(dataCollectionMapItem,"DataCollection_imageDirectory");
		document.add(new Paragraph(parag, FONT_DOC_ITALIC));	
	
		//row3
		Table table = new Table(NB_COL_DATACOLLECTION);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		table.getDefaultCell().setBorderWidth(0);

		// 1st Cell
		parag = "Workflow: \n" 
				+ "Protein: \n" 
				+ 	"Sample: \n" 
				+ 	"Prefix: \n" 
				+ 	"Run #: \n" 
				+ 	"Images: \n" 
				+ 	"Transmission: \n";
		LOG.info("parag=" + parag);
		p = new Paragraph(parag, FONT_DOC);
		table.addCell(p);
		
		// Cell2
		parag = getCellParam(dataCollectionMapItem, "Workflow_workflowType") + "\n" 
				+ getCellParam(dataCollectionMapItem, "Protein_acronym") + "\n" 
				+ getCellParam(dataCollectionMapItem, "BLSample_name") + "\n" 
				+ getCellParam(dataCollectionMapItem, "DataCollection_imagePrefix") + "\n" 
				+ getCellParam(dataCollectionMapItem, "DataCollection_dataCollectionNumber") + "\n" 
				+ getCellParam(dataCollectionMapItem, "Protein_acronym") + "\n" 
				+ getCellParam(dataCollectionMapItem, "DataCollection_transmission") + "\n";
		LOG.info("parag=" + parag);
		p = new Paragraph(parag, FONT_DOC_BOLD);
		table.addCell(p);
		
		// 3 Cell
		
		parag = "Resolution (corner): \n"
				+ 	"Wavelength: \n" 
				+ 	"Omega range: \n" 
				+ 	"Omega start: \n" 
				+ 	"Exposure time: \n" 
				+ 	"Flux start: \n" 
				+ 	"Flux end: \n" ;

		table.addCell(new Paragraph(parag, FONT_DOC));
		
		
		// Cell 4
		parag = getCellParam(dataCollectionMapItem, "DataCollection_resolutionAtCorner")+ "\n" 
		+ 	getCellParam(dataCollectionMapItem, "DataCollection_wavelength") + "\n" 
		+ 	getCellParam(dataCollectionMapItem, "DataCollection_axisRange") + "\n" 
		+ 	getCellParam(dataCollectionMapItem, "DataCollection_omegaStart") + "\n" 
		+ 	getCellParam(dataCollectionMapItem, "exposureTime") + "\n" 
		+ 	getCellParam(dataCollectionMapItem, "DataCollection_flux") + "\n" 
		+	getCellParam(dataCollectionMapItem, "DataCollection_flux_end") + "\n" ;
		
		table.addCell(new Paragraph(parag, FONT_DOC_BOLD));



		// 5 Cell add cell containing autoproc results
		table.addCell(" ");
		
		// 6 Cell : thumbnail
		
		if (!getCellParam(dataCollectionMapItem, "lastImageId").isEmpty()) {
			String thumbnailPath = (imageService.findByPk(new Integer(getCellParam(dataCollectionMapItem, "lastImageId")))).getJpegThumbnailFileFullPath();
			Cell cellThumbnail = getCellImage(thumbnailPath);
			cellThumbnail.setBorderWidth(0);
			table.addCell(cellThumbnail);
		} else {
			table.addCell(" ");
		}
		
		//cellThumbnail.setRowspan(nbRows);
		
		
		// 7 Cell : snapshot
		Cell cellSnapshot = getCellImage(dataCollectionMapItem,"DataCollection_xtalSnapshotFullPath1");
		//cellSnapshot.setRowspan(nbRows);
		cellSnapshot.setBorderWidth(0);
		table.addCell(cellSnapshot);
		
		// 8 Cell : graph or other plot
		if (!getCellParam(dataCollectionMapItem, "DataCollection_dataCollectionId").isEmpty()) {
			String plotPath = (dcService.findByPk(new Integer(getCellParam(dataCollectionMapItem, "DataCollection_dataCollectionId")), false, false)).getImageQualityIndicatorsPlotPath();
			Cell cellGraph = getCellImage(plotPath);
			cellGraph.setBorderWidth(0);
			table.addCell(cellGraph);
		} else {
			table.addCell(" ");
		}

		document.add(table);
		
		// row3
		if (dataCollectionMapItem.get("DataCollection_comments") != null && dataCollectionMapItem.get("DataCollection_comments") != "")
			document.add(new Paragraph(dataCollectionMapItem.get("DataCollection_comments").toString(), FONT_DOC));
		else
			document.add(new Paragraph(" "));
				
		return;
	}
	
	/**
	 * get the value or replace by blank if null to fill a cell paragraph
	 * 
	 * @param param
	 * @param dataCollectionMap
	 * @throws Exception
	 */
	private String getCellParam(Map<String, Object> dataCollectionMap, String param) throws Exception {

		String paramValue = "";
		if (dataCollectionMap.get(param) != null) {		
			paramValue = dataCollectionMap.get(param).toString();
		}
		return paramValue;
		
	}

	/**
	 * returns a simple cell witha given value inside
	 * 
	 * @param value
	 * @return
	 */
	private Cell getCellValue(String value) {
		Cell cell = new Cell();
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.add(new Paragraph(value, FONT_DOC_BOLD));
		cell.setColspan(1);
		return cell;
	}

	/**
	 * returns a cell with a given image inside
	 * 
	 * @param image
	 * @return
	 * @throws Exception
	 */
	private Cell getCellImage(Map<String, Object> dataCollectionMapItem, String imageParam) throws Exception {
		
		if (dataCollectionMapItem.get(imageParam) != null && !(dataCollectionMapItem.get(imageParam).toString()).equals("") ) {
			String image = dataCollectionMapItem.get(imageParam).toString();
			image = PathUtils.getPath(image);
			try {				
				Image jpg1 = Image.getInstance(image);
				jpg1.scaleAbsolute(jpg1.getWidth() * IMAGE_HEIGHT / jpg1.getHeight(), IMAGE_HEIGHT);
				Cell cell = new Cell(jpg1);
				cell.setLeading(0);
				cell.setBorderWidth(0);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_CENTER);
				return cell;
			} catch (IOException e) {
				return new Cell(new Paragraph(image + " not found", FONT_DOC));
			}
		}
		return new Cell(new Paragraph("", FONT_DOC));
	}

	/**
	 * returns a cell with a given image inside
	 * 
	 * @param image
	 * @return
	 * @throws Exception
	 */
	private Cell getCellImage(String imagePath) throws Exception {
		
		if (imagePath != null ) {
			String image = PathUtils.getPath(imagePath);
			try {				
				Image jpg1 = Image.getInstance(image);
				jpg1.scaleAbsolute(jpg1.getWidth() * IMAGE_HEIGHT / jpg1.getHeight(), IMAGE_HEIGHT);
				Cell cell = new Cell(jpg1);
				cell.setLeading(0);
				cell.setBorderWidth(0);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_CENTER);
				return cell;
			} catch (IOException e) {
				return new Cell(new Paragraph(image + " not found", FONT_DOC));
			}
		}
		return new Cell(new Paragraph("", FONT_DOC));
	}
	private Chunk getChunkImage(String image) throws BadElementException, MalformedURLException, IOException {
		Image jpg = Image.getInstance(image);
		jpg.scaleAbsolute(jpg.getWidth() * 10 / jpg.getHeight(), 10);
		return new Chunk(jpg, 0, 0);
	}

	private Cell getEmptyCell(int colspan) throws Exception {
		Cell emptyCell = new Cell(new Paragraph("", FONT_DOC));
		emptyCell.setColspan(colspan);
		return emptyCell;
	}
	
	/**
	 * set the summary - IFX proposal
	 * 
	 * @param document
	 * @throws Exception
	 */
	private void setCrystalClassSummary(Document document, Map<String, String> mapDataCollectionGroupIdCClass) throws Exception {
		
		if (proposalDesc.toLowerCase().startsWith(Constants.PROPOSAL_CODE_FX) ) {
			
			IspybCrystalClass3Service ispybCrystalClassService = (IspybCrystalClass3Service) ejb3ServiceLocator
					.getLocalService(IspybCrystalClass3Service.class);
			List<IspybCrystalClass3VO> listOfCrystalClass = ispybCrystalClassService.findAll();
			List<Integer> listOfNbCrystalPerClass = getListOfNbCrystalPerClass(listOfCrystalClass, mapDataCollectionGroupIdCClass);

			document.add(new Paragraph(" "));
			document.add(new Paragraph("Summary:", FONT_TITLE));
			document.add(new Paragraph(" "));

			int NumColumnsCC = 2;
			Table tableCC = new Table(NumColumnsCC);
			int headerwidthsCC[] = { 30, 15 }; // percentage
			tableCC.setWidths(headerwidthsCC);

			tableCC.setWidth(50); // percentage
			tableCC.setPadding(3);
			tableCC.setCellsFitPage(true);
			tableCC.getDefaultCell().setBorderWidth(1);
			tableCC.getDefaultCell().setGrayFill(GREY_FILL_HEADER);
			tableCC.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			tableCC.addCell(new Paragraph("Crystal class", FONT_DOC_BOLD));
			tableCC.addCell(new Paragraph("Number of crystals", FONT_DOC_BOLD));
			tableCC.getDefaultCell().setGrayFill(GREY_FILL_DATA);

			int nbCC = listOfCrystalClass.size();
			for (int cc = 0; cc < nbCC; cc++) {
				if (listOfNbCrystalPerClass.get(cc) > 0) {
					tableCC.addCell(new Paragraph(listOfCrystalClass.get(cc).getCrystalClassName() + " ("
							+ listOfCrystalClass.get(cc).getCrystalClassCode() + ")", FONT_DOC));
					tableCC.addCell(new Paragraph(listOfNbCrystalPerClass.get(cc).toString(), FONT_DOC));
				}
				LOG.debug("classe " + listOfCrystalClass.get(cc).getCrystalClassCode() + ": "
						+ listOfNbCrystalPerClass.get(cc).toString());
			}

			document.add(tableCC);

			// total
			// int nbPuckScreen = nbCrystal3;
			// int nbTotal = nbCrystal1 + nbCrystal2 + nbCrystalT;
			int nbPuckScreen = listOfNbCrystalPerClass.get(getCrystalClassIndex(listOfCrystalClass, "PS"));
			// nbTotal = C+CR+T+SC+SCR
			int nbTotal = listOfNbCrystalPerClass.get(getCrystalClassIndex(listOfCrystalClass, "C"))
					+ listOfNbCrystalPerClass.get(getCrystalClassIndex(listOfCrystalClass, "SC"))
					+ listOfNbCrystalPerClass.get(getCrystalClassIndex(listOfCrystalClass, "T"))
					+ listOfNbCrystalPerClass.get(getCrystalClassIndex(listOfCrystalClass, "CR"))
					+ listOfNbCrystalPerClass.get(getCrystalClassIndex(listOfCrystalClass, "SCR"));
			document.add(new Paragraph(" "));
			document.add(new Paragraph("Total number of tests: " + new String(new Integer(nbTotal).toString()),
					FONT_DOC));
			document.add(new Paragraph("Nb of puck screens: " + new String(new Integer(nbPuckScreen).toString()),
					FONT_DOC));
			document.add(new Paragraph("Total number of samples: "
					+ new String(new Integer(nbTotal + (nbPuckScreen * 10)).toString()), FONT_DOC));
		}
	}

	private int getCrystalClassIndex(List<IspybCrystalClass3VO> list, String crystalClass) {
		int nb = list.size();
		for (int i = 0; i < nb; i++) {
			if (crystalClass != null && list.get(i).getCrystalClassCode().equals(crystalClass)) {
				return i;
			}
		}
		return -1;
	}
	
	
	private List<Integer> getListOfNbCrystalPerClass(List<IspybCrystalClass3VO> listOfCrystalClass,
			Map<String, String> mapDataCollectionGroupIdCClass) {
		
		List<Integer> listOfNbCrystalPerClass = new ArrayList<Integer>(); // number of crystals / crystal classes
		String crystalClass;

			// for MXPress users, the table with crystal class will be displayed
			// calculate number of Crystals of class 1,2, ... 5, T
			// Browse dataCollections
			int nbCC = listOfCrystalClass.size();
			for (int i = 0; i < nbCC; i++) {
				listOfNbCrystalPerClass.add(0);
			}
			for (Iterator<String> it = mapDataCollectionGroupIdCClass.keySet().iterator(); it.hasNext();) {
				crystalClass = mapDataCollectionGroupIdCClass.get(it.next());
				int idCc = getCrystalClassIndex(listOfCrystalClass, crystalClass);
				if (idCc != -1) {
					listOfNbCrystalPerClass.set(idCc, listOfNbCrystalPerClass.get(idCc) + 1);
				}
			}
		
		return listOfNbCrystalPerClass;
	}


}
