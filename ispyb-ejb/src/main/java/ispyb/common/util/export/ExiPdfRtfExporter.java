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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.collections.WorkflowMesh3VO;

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

	public final static Font FONT_DOC_ITALIC = new Font(Font.HELVETICA, 8, Font.ITALIC, Color.BLACK);

	public final static Font FONT_DOC_11 = new Font(Font.HELVETICA, 11, Font.NORMAL, Color.BLACK);

	public final static Font FONT_DOC_11_ITALIC = new Font(Font.HELVETICA, 11, Font.ITALIC, Color.BLACK);

	public final static Font FONT_DOC_EXPONENT = new Font(Font.HELVETICA, 11, Font.NORMAL, Color.BLACK);

	public final static Font FONT_DOC_EXPONENT_ITALIC = new Font(Font.HELVETICA, 11, Font.ITALIC, Color.BLACK);

	public final static Font FONT_DOC_EXPONENT_BLUE = new Font(Font.HELVETICA, 11, Font.NORMAL, Color.BLUE);

	public final static Font FONT_DOC_BOLD = new Font(Font.HELVETICA, 8, Font.BOLD);
	public final static Font FONT_DOC_PARAM_TITLE = new Font(Font.HELVETICA, 8, Font.NORMAL, LIGHT_GREY_COLOR);

	public final static Font FONT_INDEXING_NOTDONE = new Font(Font.HELVETICA, 8, Font.NORMAL, LIGHT_GREY_COLOR);

	public final static Font FONT_INDEXING_FAILED = new Font(Font.HELVETICA, 8, Font.NORMAL, RED_COLOR);

	public final static Font FONT_INDEXING_SUCCESS = new Font(Font.HELVETICA, 8, Font.NORMAL, GREEN_COLOR);

	public final static int NB_COL_DATACOLLECTION = 12;

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

	// sample name
	String name;

	// proposal
	String proposalDesc;

	// session info
	Integer sessionId;
	
	private Session3VO slv;


	// dataCollectionViewObject List
	List<Map<String, Object>> dataCollections = new ArrayList<Map<String, Object>>();

	DecimalFormat df2;

	DecimalFormat df3;

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
	
	private Session3Service sessionService;
	
	private DataCollection3Service dataCollectionService;
		

	public ExiPdfRtfExporter(String name, String proposalDesc, Integer sessionId,
			List<Map<String, Object>> dataCollections) throws Exception {
		this.name = name;
		this.proposalDesc = proposalDesc;
		this.sessionId = sessionId;
		this.dataCollections = dataCollections;
		init();
	}

	private void init() throws Exception {
		df2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df2.applyPattern("#####0.00");
		df3 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df3.applyPattern("#####0.000");
		sessionService = (Session3Service) ejb3ServiceLocator
				.getLocalService(Session3Service.class);
		dataCollectionService = (DataCollection3Service) ejb3ServiceLocator
				.getLocalService(DataCollection3Service.class);
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
		if (name != null) {
			h += "  ---  Sample: " + name;
		} else if (slv != null) {
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
	 * sets the header in the specified document
	 * 
	 * @param document
	 * @throws Exception
	 */
	private void setHeaderForDetails(Document document) throws Exception {
		HeaderFooter header;
		String h = "Screening report for Proposal: " + proposalDesc;
		if (name != null) {
			h += "  ---  Sample: " + name;
		} else if (slv != null) {
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
	 * sets the header in the specified document
	 * 
	 * @param document
	 * @throws Exception
	 */
	private void setHeaderMXPressO(Document document) throws Exception {
		HeaderFooter header;
		String h = "MXPressO/MXPressE for Proposal: " + proposalDesc;
		if (name != null) {
			h += "  ---  Sample: " + name;
		} else if (slv != null) {
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
	 * set the crystallographer - only for OFX proposal
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
			Table table = new Table(NB_COL_DATACOLLECTION);
			table = setDataCollectionHeader(document, table, true);
			DecimalFormat df2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
			df2.applyPattern("#####0.00");
			DecimalFormat df3 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
			df3.applyPattern("#####0.000");
			
			// DataCollection Rows
			Iterator<Map<String, Object>> it = dataCollections.iterator();
			int i = 0;
			while (it.hasNext()) {
				Map<String, Object> dataCollectionMapData = it.next();
				setDataCollectionMapData(document, table, dataCollectionMapData);
				i++;
			}
			document.add(table);
		}
	}

	/**
	 * set the header for the dataCollection table
	 * 
	 * @param document
	 * @param table
	 * @throws Exception
	 */
	private Table setDataCollectionHeader(Document document, Table table, boolean withAutoProcessing) throws Exception {
		int nbCol = NB_COL_DATACOLLECTION;
		boolean withoutAutoProc = !withAutoProcessing;
		boolean withSampleName = name != null;
		String proposalCode = proposalDesc.substring(0, 2);
		boolean isIfx = proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX);
		if (withoutAutoProc) {
			nbCol -= 5;
		}
		if (withSampleName) {
			nbCol += 1;
		}
		if (isIfx) {
			nbCol += 1;
		}
		table = new Table(nbCol);
		int[] headersWidth = new int[nbCol];
		int i = 0;
		// image prefix
		headersWidth[i] = 12;
		if (withSampleName && !withAutoProcessing)
			headersWidth[i] = 10;
		i++;
		// beamline
		if (withSampleName) {
			headersWidth[i++] = 5;
		}
		// run no
		headersWidth[i++] = 5;
		// nb images
		headersWidth[i++] = 6;
		// auto proc
		if (withAutoProcessing) {
			headersWidth[i++] = 9; // space group
			headersWidth[i++] = 9; // unit cell
			headersWidth[i++] = 10; // completeness
			headersWidth[i++] = 9; // rsymm
			headersWidth[i++] = 9; // resolution
		}
		// detector
		headersWidth[i++] = 7;
		// wavelength
		headersWidth[i++] = 8;
		// phi range
		headersWidth[i++] = 5;
		// crytsl class
		if (isIfx) {
			headersWidth[i++] = 8;
		}
		// comments
		headersWidth[i++] = 17;
		//
		table.setWidths(headersWidth);

		table.setWidth(100); // percentage
		table.setPadding(3);
		table.setCellsFitPage(true);
		table.getDefaultCell().setBorderWidth(1);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

		// DataCollection Header

		table.getDefaultCell().setGrayFill(GREY_FILL_HEADER);
		table.addCell(new Paragraph("Image prefix", FONT_DOC_BOLD));
		if (withSampleName)
			table.addCell(new Paragraph("Beamline", FONT_DOC_BOLD));
		table.addCell(new Paragraph("Run no", FONT_DOC_BOLD));
		table.addCell(new Paragraph("# images", FONT_DOC_BOLD));
		if (withAutoProcessing) {
			table.addCell(new Paragraph("Space Group", FONT_DOC_BOLD));
			table.addCell(new Paragraph("Unit Cell", FONT_DOC_BOLD));
			table.addCell(new Paragraph("Completeness (Inner, Outer, Overall)", FONT_DOC_BOLD));
			table.addCell(new Paragraph("Rsymm (Inner, Outer, Overall)", FONT_DOC_BOLD));
			table.addCell(new Paragraph("Resolution", FONT_DOC_BOLD));
		}
		table.addCell(new Paragraph("Detector\nResolution\n(" + Constants.ANGSTROM + ")", FONT_DOC_BOLD));
		table.addCell(new Paragraph("Wavelength\n(" + Constants.ANGSTROM + ")", FONT_DOC_BOLD));
		table.addCell(new Paragraph("Phi range\n(" + Constants.DEGREE + ")", FONT_DOC_BOLD));
		// Column crystalClass only for IFX proposal in case of MXPress
		// experiment
		if (isIfx)
			table.addCell(new Paragraph("Crystal class", FONT_DOC_BOLD));
		table.addCell(new Paragraph("Comments", FONT_DOC_BOLD));
		table.getDefaultCell().setBorderWidth(1);
		return table;
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
	private void setDataCollectionMapData(Document document, Table table, Map<String, Object> col) throws Exception {

		table.getDefaultCell().setGrayFill(GREY_FILL_DATA);
		
		if (col.get("DataCollection_imagePrefix()") != null)
			table.addCell(new Paragraph((String) col.get("DataCollection_imagePrefix()"), FONT_DOC));
		else
			table.addCell("");
		// The beamline name is only displayed for select by protein or by
		// sample name
		if (name != null) {
			if (slv.getBeamlineName() != null)
				table.addCell(new Paragraph(slv.getBeamlineName(), FONT_DOC));
			else
				table.addCell("");
		}

		if (col.get("DataCollection_dataCollectionNumber") != null)
			table.addCell(new Paragraph(col.get("DataCollection_dataCollectionNumber").toString(), FONT_DOC));
		else
			table.addCell("");

		if (col.get("DataCollection_numberOfImages") != null) 
			table.addCell(new Paragraph(col.get("DataCollection_numberOfImages").toString(), FONT_DOC));
		else
			table.addCell("");

		// detector resolution
		if (col.get("DataCollection_resolutionAtCorner") != null)
			table.addCell(new Paragraph(df2.format(col.get("DataCollection_resolutionAtCorner")), FONT_DOC));
		else
			table.addCell("");

		// wavelength
		if (col.get("DataCollection_wavelength") != null)
			table.addCell(new Paragraph(df3.format(col.get("DataCollection_wavelength")), FONT_DOC));
		else
			table.addCell("");

		// phi range
		if (col.get("DataCollection_axisRange") != null)
			table.addCell(new Paragraph(df2.format(col.get("DataCollection_axisRange")), FONT_DOC));
		else
			table.addCell("");

		// Column crystalClass only for IFX proposal in case of MXPress
		// experiment
//		if (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX)) {
//			DataCollectionGroup3Service dataCollectionGroupService = (DataCollectionGroup3Service) ejb3ServiceLocator
//					.getLocalService(DataCollectionGroup3Service.class);
//			DataCollectionGroup3VO group = dataCollectionGroupService.findByPk(col.getDataCollectionGroupVOId(), true, true);
//			boolean firstCollect = group.isFirstCollect(col);
//			if (dcGroup.getCrystalClass() != null) {
//				int idCC = getCrystalClassIndex(listOfCrystalClass, dcGroup.getCrystalClass().trim().toUpperCase());
//				String crystalS = "";
//				if (idCC == -1) {
//					crystalS = dcGroup.getCrystalClass().toString();
//				} else {
//					crystalS = listOfCrystalClass.get(idCC).getCrystalClassName();
//				}
//				String crystalCell = crystalS;
//				if (!firstCollect && crystalS != null && !crystalS.equals("")) {
//					crystalCell = "(" + crystalS + ")";
//				}
//				table.addCell(new Paragraph(crystalCell, FONT_DOC));
//			} else
//				table.addCell("");
//
//		}
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		if (col.get("DataCollection_comments") != null && col.get("DataCollection_comments") != "")
			table.addCell(new Paragraph(col.get("DataCollection_comments").toString(), FONT_DOC));
		else
			table.addCell("");
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	}


	/**
	 * export detailed dataCollection report: new screening report
	 * 
	 * @return
	 * @throws Exception
	 */
	public ByteArrayOutputStream exportDetails(boolean rtfFormat) throws Exception {
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

		setHeaderForDetails(document);
		setFooter(document);
		document.open();

		// ===============================
		// Body
		// ===============================
		// Session comments
		setSessionComments(document);
		document.add(new Paragraph(" "));
		// build the list with the collect and collects group by workflow +
		// energyScan+ XRFSpectra
		// this list is sorted by time
		List<Map<String, Object>> listSessionDataObject = dataCollections;
		if (listSessionDataObject.isEmpty()) {
			document.add(new Paragraph("There is no data in this report", ExiPdfRtfExporter.FONT_DOC));
		} else {
			// for each object, we will display a different table
			int countTables = 0;
			for (Iterator<Map<String, Object>> iterator = listSessionDataObject.iterator(); iterator.hasNext();) {
				Map<String, Object> sessionDataObject = (Map<String, Object>) iterator.next();
				if (countTables > 1) {
					// System.out.println("page break");
					document.newPage();
					countTables = 0;
				}
				setDetailSessionObjectTable(document, sessionDataObject);
				countTables++;
			}
		}

		// ======================
		// End of file
		// ======================
		document.close();
		return baos;
	}

	/**
	 * set a table for a sessionDataObject
	 * 
	 * @param document
	 * @param sessionDataObject
	 * @param mRequest
	 */
	private void setDetailSessionObjectTable(Document document, Map<String, Object> sessionDataObject) {
		try {
			DataCollection3VO dataCollection = dataCollectionService.findByPk((Integer)sessionDataObject.get("DataCollection_dataCollectionId"), false/*withImage*/, false/*withAutoProcIntegration*/);

			int nbCol = 6;
			int nbRows = 5;

			Table table = new Table(nbCol);

			int l = 0;

			int[] headersWidth = new int[nbCol];

			headersWidth[l++] = 10; // def
			headersWidth[l++] = 6; // parameters title
			headersWidth[l++] = 6; // parameters value
			headersWidth[l++] = 12; // Thumbnail
			headersWidth[l++] = 12; // Snapshot
			headersWidth[l++] = 22; // Graph
			table.setWidths(headersWidth);

			table.setWidth(100); // percentage
			table.setPadding(1);
			table.setCellsFitPage(true);
			table.setTableFitsPage(true);
			table.setBorderWidth(1);
			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			// no header
			// first Row
			// firstCell: def : date
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String collectTime = formatter.format(sessionDataObject.get("DataCollection_startTime"));
			Cell c = getCellValue(collectTime);
			c.setBorderWidthBottom(0);
			table.addCell(c);
			
			// second Cell param
			setCellParam2(table, sessionDataObject, "Workflow_workflowType", 1);
			// third Cell : thumbnail
			Cell cellThumbnail = getCellImage(sessionDataObject.get("DataCollection_bestWilsonPlotPath").toString());
			cellThumbnail.setRowspan(nbRows);
			cellThumbnail.setBorderWidth(0);
			table.addCell(cellThumbnail);
			// 4 Cell : snapshot
			Cell cellSnapshot = getCellImage(sessionDataObject.get("DataCollection_xtalSnapshotFullPath1").toString());
			cellSnapshot.setRowspan(nbRows);
			cellSnapshot.setBorderWidth(0);
			table.addCell(cellSnapshot);
			// 5 Cell : graph or other snapshot
			Cell cellGraph = getCellImage(sessionDataObject.get("DataCollection_xtalSnapshotFullPath2").toString());
			cellGraph.setRowspan(nbRows);
			cellGraph.setBorderWidth(0);
			table.addCell(cellGraph);

			// second row
			Cell c2 = getCellValue(sessionDataObject.get("DataCollection_imagePrefix").toString() + " " + sessionDataObject.get("DataCollection_runNumber").toString());
			c2.setBorderWidth(0);
			table.addCell(c2);
			// param2
			setCellParam2(table, sessionDataObject, "Workflow_workflowType", 1);

			// third row
			Cell c3 = getCellValue(sessionDataObject.get("DataCollectionGroup_experimentType").toString());
			c3.setBorderWidth(0);
			table.addCell(c3);
			// param3
			setCellParam2(table, sessionDataObject, "Workflow_workflowType", 1);

			// 4 row
			Cell c4 = getCellValue(sessionDataObject.get("Protein_acronym").toString());
			c4.setBorderWidth(0);
			table.addCell(c4);
			// param4
			setCellParam2(table, sessionDataObject, "Workflow_workflowType", 1);

			// 5 row
			Cell c5 = new Cell();
			c5.setHorizontalAlignment(Element.ALIGN_LEFT);
			c5.add(new Paragraph(sessionDataObject.get("DataCollection_comments").toString(), FONT_DOC_ITALIC));
			c5.setBorderWidth(0);
			c5.setRowspan(nbRows - 4);
			table.addCell(c5);
			// param4
			setCellParam2(table, sessionDataObject, "Workflow_workflowType",1);


			// results

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * set a cell for a param or empty cell if no more param in the list
	 * 
	 * @param table
	 * @param listParam
	 * @param id
	 * @param colSpanTitle
	 * @throws Exception
	 */
	private void setCellParam2(Table table, Map<String, Object> dataCollectionMap, String param, int colSpanTitle) throws Exception {

		String paramValue = dataCollectionMap.get("param").toString();
		if (paramValue != null && !paramValue.isEmpty())	{
			Cell cellTitle = new Cell();
			cellTitle.setBorderWidth(0);
			cellTitle.setColspan(colSpanTitle);
			cellTitle.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cellTitle.add(new Paragraph(param, FONT_DOC_PARAM_TITLE));
			table.addCell(cellTitle);

			Cell cellValue = new Cell();
			cellValue.setBorderWidth(0);
			cellValue.setHorizontalAlignment(Element.ALIGN_LEFT);
			cellValue.add(new Paragraph(paramValue, FONT_DOC));
			table.addCell(cellValue);
		} else {
			Cell cellTitle = getEmptyCell(1);
			cellTitle.setBorderWidth(0);
			cellTitle.setColspan(colSpanTitle);
			table.addCell(cellTitle);

			Cell cellValue = getEmptyCell(1);
			cellValue.setBorderWidth(0);
			table.addCell(cellValue);
		}
	}

	/**
	 * returns the cell for the mesh map
	 * 
	 * @param wfMesh
	 * @return
	 */
	private Cell getMeshMapCell(WorkflowMesh3VO wfMesh) {
		String imgMap = null;
		String cartographyPath = wfMesh.getCartographyPath();
		if (cartographyPath != null) {
			imgMap = PathUtils.FitPathToOS(cartographyPath);
		}
		try {
			if (imgMap != null) {
				Image jpgMap = Image.getInstance(imgMap);
				// System.out.println(jpgMap.getWidth()+" * "+jpgMap.getHeight());
				jpgMap.scaleAbsolute(jpgMap.getWidth() * IMAGE_HEIGHT / jpgMap.getHeight(), IMAGE_HEIGHT);
				Cell cellImage = new Cell(jpgMap);
				cellImage.setLeading(0);
				cellImage.setBorderWidth(0);
				cellImage.setHorizontalAlignment(Element.ALIGN_CENTER);
				return cellImage;
			} else {
				return new Cell(new Paragraph("no map found", FONT_DOC));
			}
		} catch (IOException e) {
			try {
				return new Cell(new Paragraph(imgMap + " not found", FONT_DOC));
			} catch (BadElementException e1) {
				e1.printStackTrace();
				return null;
			}
		} catch (Exception e) {
			try {
				return new Cell(new Paragraph(imgMap + " not found", FONT_DOC));
			} catch (BadElementException e1) {
				e1.printStackTrace();
				return null;
			}
		}

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
	private Cell getCellImage(String image) throws Exception {
		if (image != null && !image.equals("")) {
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
	 * export datacollection report
	 * 
	 * @param rtfFormat
	 * @return
	 * @throws Exception
	 */
	public ByteArrayOutputStream exportNewDataCollectionReport(boolean rtfFormat) throws Exception {
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

}
