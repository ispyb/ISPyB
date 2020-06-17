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

package ispyb.client.mx.collection;

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

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
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

import ispyb.client.mx.ranking.SampleRankingVO;
import ispyb.client.mx.ranking.autoProcRanking.AutoProcRankingVO;
import ispyb.common.util.Constants;
import ispyb.common.util.Formatter;
import ispyb.common.util.PathUtils;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.collections.DataCollectionGroup3Service;
import ispyb.server.mx.services.collections.WorkflowMesh3Service;
import ispyb.server.mx.vos.autoproc.AutoProc3VO;
import ispyb.server.mx.vos.autoproc.AutoProcScalingStatistics3VO;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
import ispyb.server.mx.vos.collections.EnergyScan3VO;
import ispyb.server.mx.vos.collections.IspybCrystalClass3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.collections.Workflow3VO;
import ispyb.server.mx.vos.collections.WorkflowMesh3VO;
import ispyb.server.mx.vos.collections.XFEFluorescenceSpectrum3VO;
import ispyb.server.mx.vos.screening.Screening3VO;
import ispyb.server.mx.vos.screening.ScreeningOutput3VO;
import ispyb.server.mx.vos.screening.ScreeningOutputLattice3VO;

/**
 * allows creation of PDF or RTF report - general report, available in the
 * dataCollection tab - Detailed report, available in the dataCollection tab -
 * ranking report, available in the ranking page MXPressO/MXPressE reports are
 * available from the session report page or dataCollectionGroup page. This
 * report is used by the scientists to check the workflows during the experiment
 * New screening and general report are available from the session report Page
 * 
 * @author Marjolaine Bodin
 * 
 */
public class PdfRtfExporter {

	private final static Logger LOG = Logger.getLogger(PdfRtfExporter.class);
	
	private Boolean isRTF;

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
	
	public final static float IMAGE_HEIGHT_ICON = 12;
		
	public final static float IMAGE_HEIGHT_SMALL = 50;
	public final static float IMAGE_HEIGHT_SNAPSHOT = 60;

	// public final static float CRYSTAL_IMAGE_WIDTH = 160;
	// public final static float CRYSTAL_IMAGE_HEIGHT = 99;
	public final static float DIFF_IMAGE_WIDTH = 174;

	public final static float DIFF_IMAGE_HEIGHT = 174;

	// sample name
	String name;

	// proposal
	String proposalCode;

	String proposalNumber;

	// session info
	Session3VO slv;

	// data collection list
	List<DataCollection3VO> dataCollectionList;

	// dataCollection group list
	List<DataCollectionGroup3VO> dataCollectionGroupList;

	// energy scan list
	List<EnergyScan3VO> energyScanList;

	// XRF Spectra
	List<XFEFluorescenceSpectrum3VO> xfeList;

	// list of all crystal classes in ispyb
	List<IspybCrystalClass3VO> listOfCrystalClass = null;

	List<Integer> listOfNbCrystalPerClass = new ArrayList<Integer>();

	// auto proc wrapper
	AutoProcShellWrapper wrapper;

	// sample ranking list
	List<SampleRankingVO> sampleRankingVOList;

	// autoproc ranking list
	List<AutoProcRankingVO> autoProcRankingVOList;

	// sessionDataObject List
	List<SessionDataObjectInformation> sessionDataObjectList;

	DecimalFormat df2;

	DecimalFormat df3;

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	public PdfRtfExporter(String name, String proposalCode, String proposalNumber, Session3VO slv,
			List<DataCollection3VO> dataCollectionList, List<EnergyScan3VO> energyScanList,
			List<XFEFluorescenceSpectrum3VO> xfeList, List<IspybCrystalClass3VO> listOfCrystalClass,
			List<Integer> listOfNbCrystalPerClass, AutoProcShellWrapper wrapper,
			List<SampleRankingVO> sampleRankingVOList, List<AutoProcRankingVO> autoProcRankingVOList,
			List<DataCollectionGroup3VO> dataCollectionGroupList,
			List<SessionDataObjectInformation> sessionDataObjectList) throws CreateException, NamingException {
		this.name = name;
		this.proposalCode = proposalCode;
		this.proposalNumber = proposalNumber;
		this.slv = slv;
		this.dataCollectionList = dataCollectionList;
		this.energyScanList = energyScanList;
		this.xfeList = xfeList;
		this.listOfCrystalClass = listOfCrystalClass;
		this.listOfNbCrystalPerClass = listOfNbCrystalPerClass;
		this.wrapper = wrapper;
		this.sampleRankingVOList = sampleRankingVOList;
		this.autoProcRankingVOList = autoProcRankingVOList;
		this.dataCollectionGroupList = dataCollectionGroupList;
		this.sessionDataObjectList = sessionDataObjectList;
		init();
	}

	private void init() {
		df2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df2.applyPattern("#####0.00");
		df3 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df3.applyPattern("#####0.000");
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
			isRTF = new Boolean(false);
			PdfWriter.getInstance(document, baos);			
		} else {
			isRTF = new Boolean(true);
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
		// Energy scans
		// ======================
		document.add(new Paragraph(" "));
		setEnergyScansTable(document);

		// ======================
		// XRF Spectra
		// ======================
		document.add(new Paragraph(" "));
		setXfrSpectraTable(document);

		// ======================
		// Summary
		// ======================
		setSummary(document);

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
		String h = "Data Collections for Proposal: " + proposalCode + proposalNumber;
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
		String h = "Screening report for Proposal: " + proposalCode + proposalNumber;
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
		String h = "MXPressO/MXPressE for Proposal: " + proposalCode + proposalNumber;
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
		if (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX) && slv != null) {
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
		if (dataCollectionList.isEmpty()) {
			document.add(new Paragraph("There is no data collection in this report", FONT_DOC));
		} else {
			Table table = new Table(NB_COL_DATACOLLECTION);
			table = setDataCollectionHeader(true);
			DecimalFormat df2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
			df2.applyPattern("#####0.00");
			DecimalFormat df3 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
			df3.applyPattern("#####0.000");
			// DataCollection Rows
			AutoProc3VO[] autoProcs = wrapper.getAutoProcs();
			AutoProcScalingStatistics3VO[] autoProcsOverall = wrapper.getScalingStatsOverall();
			AutoProcScalingStatistics3VO[] autoProcsInner = wrapper.getScalingStatsInner();
			AutoProcScalingStatistics3VO[] autoProcsOuter = wrapper.getScalingStatsOuter();
			Session3Service sessionService = (Session3Service) ejb3ServiceLocator
					.getLocalService(Session3Service.class);
			DataCollectionGroup3Service dcgService = (DataCollectionGroup3Service) ejb3ServiceLocator
					.getLocalService(DataCollectionGroup3Service.class);

			Iterator<DataCollection3VO> it = dataCollectionList.iterator();
			int i = 0;
			while (it.hasNext()) {
				DataCollection3VO col = it.next();
				ScreeningOutput3VO screeningOutput = null;
				ScreeningOutputLattice3VO screeningOutputLattice = null;
				DataCollectionGroup3VO colGroup = dcgService.findByPk(col.getDataCollectionGroupVOId(), false, true);
				Screening3VO[] tabScreening = colGroup.getScreeningsTab();
				if (tabScreening != null && tabScreening.length > 0) {
					Screening3VO screeningVO = tabScreening[0];
					ScreeningOutput3VO[] screeningOutputTab = screeningVO.getScreeningOutputsTab();
					if (screeningOutputTab != null && screeningOutputTab.length > 0) {
						screeningOutput = screeningOutputTab[0];
						if (screeningOutputTab[0].getScreeningOutputLatticesTab() != null
								&& screeningOutputTab[0].getScreeningOutputLatticesTab().length > 0) {
							screeningOutputLattice = screeningOutputTab[0].getScreeningOutputLatticesTab()[0];
						}
					}
				}
				setDataCollectionData(table, col, sessionService, autoProcs[i], autoProcsOverall[i],
						autoProcsInner[i], autoProcsOuter[i], true, true, screeningOutput, screeningOutputLattice);
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
	private Table setDataCollectionHeader(boolean withAutoProcessing) throws Exception {
		int nbCol = NB_COL_DATACOLLECTION;
		boolean withoutAutoProc = !withAutoProcessing;
		boolean withSampleName = name != null;
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
		Table table = new Table(nbCol);
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
	private void setDataCollectionData(Table table, DataCollection3VO col,
			Session3Service sessionService, AutoProc3VO autoProcValue, AutoProcScalingStatistics3VO autoProcOverall,
			AutoProcScalingStatistics3VO autoProcInner, AutoProcScalingStatistics3VO autoProcOuter,
			boolean withAutoProcessing, boolean setEDNAInfo, ScreeningOutput3VO screeningOutput,
			ScreeningOutputLattice3VO screeningOutputLattice) throws Exception {
		// Session3VO slv = sessionService.findByPk(col.getSessionId(), false,
		// false, false);
		DataCollectionGroup3VO dcGroup = col.getDataCollectionGroupVO();
		Session3VO slv = dcGroup.getSessionVO();
		// here slv is not null
		if (col.getNumberOfImages() != null) {
			if (!DataCollectionExporter.isDataCollectionScreening(col)) {
				table.getDefaultCell().setGrayFill(GREY_FILL_DATA_COLLECT);
			} else
				table.getDefaultCell().setGrayFill(GREY_FILL_DATA);
		}
		if (col.getImagePrefix() != null)
			table.addCell(new Paragraph(col.getImagePrefix(), FONT_DOC));
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

		if (col.getDataCollectionNumber() != null)
			table.addCell(new Paragraph(col.getDataCollectionNumber().toString(), FONT_DOC));
		else
			table.addCell("");

		if (col.getNumberOfImages() != null)
			table.addCell(new Paragraph(col.getNumberOfImages().toString(), FONT_DOC));
		else
			table.addCell("");

		if (withAutoProcessing) {
			// space group
			if (autoProcValue != null && autoProcValue.getSpaceGroup() != null) {
				Paragraph p = new Paragraph(autoProcValue.getSpaceGroup(), FONT_DOC);
				table.addCell(p);
			} else if (setEDNAInfo && screeningOutputLattice != null && screeningOutputLattice.getSpaceGroup() != null) {
				Paragraph p = new Paragraph(screeningOutputLattice.getSpaceGroup(), FONT_DOC);
				table.addCell(p);
			} else
				table.addCell("");

			// unit cell
			if (autoProcValue != null && autoProcValue.getSpaceGroup() != null)
				table.addCell(new Paragraph(autoProcValue.getRefinedCellA() + " ("
						+ autoProcValue.getRefinedCellAlpha() + ")\n" + autoProcValue.getRefinedCellB() + " ("
						+ autoProcValue.getRefinedCellBeta() + ")\n" + autoProcValue.getRefinedCellC() + " ("
						+ autoProcValue.getRefinedCellGamma() + ")", FONT_DOC));
			else if (setEDNAInfo && screeningOutputLattice != null && screeningOutputLattice.getUnitCell_a() != null
					&& screeningOutputLattice.getUnitCell_b() != null && screeningOutputLattice.getUnitCell_c() != null
					&& screeningOutputLattice.getUnitCell_alpha() != null
					&& screeningOutputLattice.getUnitCell_beta() != null
					&& screeningOutputLattice.getUnitCell_gamma() != null) {
				Paragraph p = new Paragraph(df3.format(screeningOutputLattice.getUnitCell_a()) + " ("
						+ df3.format(screeningOutputLattice.getUnitCell_alpha()) + ")\n"
						+ df3.format(screeningOutputLattice.getUnitCell_b()) + " ("
						+ df3.format(screeningOutputLattice.getUnitCell_beta()) + ")\n"
						+ df3.format(screeningOutputLattice.getUnitCell_c()) + " ("
						+ df3.format(screeningOutputLattice.getUnitCell_gamma()) + ")", FONT_DOC);
				table.addCell(p);
			} else
				table.addCell("");

			// completeness, rsymm, processed resolution
			String completenessString = new String();
			String rSymmString = new String();
			String resolutionString = new String();

			if (autoProcOverall != null && autoProcInner != null && autoProcOuter != null) {
				
				completenessString += (autoProcInner.getCompleteness() == null ? "" : df2.format(autoProcInner.getCompleteness())) + "\n"
						+ (autoProcOuter.getCompleteness() == null ? "" : df2.format(autoProcOuter.getCompleteness())) + "\n"
						+ (autoProcOverall.getCompleteness() == null ? "" : df2.format(autoProcOverall.getCompleteness()));
				rSymmString += (autoProcInner.getRmerge() == null ? "" : df2.format(autoProcInner.getRmerge())) + "\n"
						+ (autoProcOuter.getRmerge() == null ? "" : df2.format(autoProcOuter.getRmerge())) + "\n"
						+ (autoProcOverall.getRmerge() == null ? "" : df2.format(autoProcOverall.getRmerge()));
				resolutionString += autoProcInner.getResolutionLimitLow() + " - "
						+ autoProcInner.getResolutionLimitHigh() + "\n" + autoProcOuter.getResolutionLimitLow() + " - "
						+ autoProcOuter.getResolutionLimitHigh() + "\n" + autoProcOverall.getResolutionLimitLow()
						+ " - " + autoProcOverall.getResolutionLimitHigh();
			} else if (setEDNAInfo && screeningOutput != null && screeningOutput.getRankingResolution() != null) {
				resolutionString = df2.format(screeningOutput.getRankingResolution());
			}
			table.addCell(new Paragraph(completenessString, FONT_DOC));
			table.addCell(new Paragraph(rSymmString, FONT_DOC));
			table.addCell(new Paragraph(resolutionString, FONT_DOC));
		}

		// detector resolution
		if (col.getResolution() != null)
			table.addCell(new Paragraph(df2.format(col.getResolution()), FONT_DOC));
		else
			table.addCell("");

		// wavelength
		if (col.getWavelength() != null)
			table.addCell(new Paragraph(df3.format(col.getWavelength()), FONT_DOC));
		else
			table.addCell("");

		// phi range
		if (col.getAxisRange() != null)
			table.addCell(new Paragraph(df2.format(col.getAxisRange()), FONT_DOC));
		else
			table.addCell("");

		// Column crystalClass only for IFX proposal in case of MXPress
		// experiment
		if (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX)) {
			// if (col.getCrystalClass() != null && col.getCrystalClass() != "")
			// table.addCell(new
			// Paragraph(col.getCrystalClass(), new Font(Font.HELVETICA, 8)));
			// else table.addCell("");
			DataCollectionGroup3Service dataCollectionGroupService = (DataCollectionGroup3Service) ejb3ServiceLocator
					.getLocalService(DataCollectionGroup3Service.class);
			DataCollectionGroup3VO group = dataCollectionGroupService.findByPk(col.getDataCollectionGroupVOId(), true, true);
			boolean firstCollect = group.isFirstCollect(col);
			if (dcGroup.getCrystalClass() != null) {
				int idCC = getCrystalClassIndex(listOfCrystalClass, dcGroup.getCrystalClass().trim().toUpperCase());
				String crystalS = "";
				if (idCC == -1) {
					crystalS = dcGroup.getCrystalClass().toString();
				} else {
					crystalS = listOfCrystalClass.get(idCC).getCrystalClassName();
				}
				String crystalCell = crystalS;
				if (!firstCollect && crystalS != null && !crystalS.equals("")) {
					crystalCell = "(" + crystalS + ")";
				}
				table.addCell(new Paragraph(crystalCell, FONT_DOC));
			} else
				table.addCell("");

		}
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		if (col.getComments() != null && col.getComments() != "")
			table.addCell(new Paragraph(col.getComments(), FONT_DOC));
		else
			table.addCell("");
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	}

	/**
	 * set the energy scan table
	 * 
	 * @param document
	 * @throws Exception
	 */
	private void setEnergyScansTable(Document document) throws Exception {
		if (slv == null) {// if session is null, no sense to display energy
							// scans
			return;
		}
		document.add(new Paragraph("Energy Scans:", FONT_TITLE));
		document.add(new Paragraph(" "));
		if (energyScanList == null || energyScanList.isEmpty()) {
			document.add(new Paragraph("There is no energy scan in this report", FONT_DOC));
		} else {
			int NumColumnsES = 12;
			Table tableES = new Table(NumColumnsES);
			int headerwidthsSessionES[] = { 7, 7, 7, 7, 7, 7, 6, 6, 7, 7, 7, 25 }; // percentage
			int headerwidthsMXPressES[] = { 7, 7, 7, 7, 7, 7, 6, 6, 7, 7, 7, 8, 17 }; // percentage
			tableES.setWidths(headerwidthsSessionES);
			// Column crystalClass added only for IFX proposal in case of
			// MXPress experiment
			if (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX)) {
				tableES = new Table(NumColumnsES + 1);
				tableES.setWidths(headerwidthsMXPressES);
			}

			tableES.setWidth(100); // percentage
			tableES.setPadding(3);
			tableES.setCellsFitPage(true);
			tableES.getDefaultCell().setBorderWidth(1);
			tableES.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

			// Energy Scan Header

			tableES.getDefaultCell().setGrayFill(GREY_FILL_HEADER);
			tableES.addCell(new Paragraph("Element", FONT_DOC_BOLD));
			tableES.addCell(new Paragraph("Inflection Energy\n(keV)", FONT_DOC_BOLD));
			tableES.addCell(new Paragraph("Exposure Time\n(s)", FONT_DOC_BOLD));
			tableES.addCell(new Paragraph("Inflection f'\n(e)", FONT_DOC_BOLD));
			tableES.addCell(new Paragraph("Inflection f''\n(e)", FONT_DOC_BOLD));
			tableES.addCell(new Paragraph("Peak Energy\n(keV)", FONT_DOC_BOLD));
			tableES.addCell(new Paragraph("Peak f'\n(e)", FONT_DOC_BOLD));
			tableES.addCell(new Paragraph("Peak f''\n(e)", FONT_DOC_BOLD));
			tableES.addCell(new Paragraph("Beam size Hor.\n(" + Constants.MICRO + "m)", FONT_DOC_BOLD));
			tableES.addCell(new Paragraph("Beam size Ver.\n(" + Constants.MICRO + "m)", FONT_DOC_BOLD));
			tableES.addCell(new Paragraph("Transm. Factor\n(%)", FONT_DOC_BOLD));
			tableES.addCell(new Paragraph("Remote Energy\n(keV)", FONT_DOC_BOLD));
			tableES.addCell(new Paragraph("Remote f'\n(e)", FONT_DOC_BOLD));
			tableES.addCell(new Paragraph("Remote f''\n(e)", FONT_DOC_BOLD));
			// Column crystalClass only for IFX proposal in case of MXPress
			// experiment
			if (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX))
				tableES.addCell(new Paragraph("Crystal class", FONT_DOC_BOLD));
			tableES.addCell(new Paragraph("Comments", FONT_DOC_BOLD));
			tableES.getDefaultCell().setGrayFill(GREY_FILL_DATA);
			tableES.getDefaultCell().setBorderWidth(1);

			// Energy Scan Rows

			Iterator<EnergyScan3VO> itES = energyScanList.iterator();
			while (itES.hasNext()) {
				EnergyScan3VO col = itES.next();

				if (col.getElement() != null)
					tableES.addCell(new Paragraph(col.getElement().toString(), FONT_DOC));
				else
					tableES.addCell("");

				if (col.getExposureTime() != null)
					tableES.addCell(new Paragraph(col.getExposureTime().toString(), FONT_DOC));
				else
					tableES.addCell("");

				if (col.getInflectionEnergy() != null)
					tableES.addCell(new Paragraph(col.getInflectionEnergy().toString(), FONT_DOC));
				else
					tableES.addCell("");

				if (col.getInflectionFPrime() != null)
					tableES.addCell(new Paragraph(col.getInflectionFPrime().toString(), FONT_DOC));
				else
					tableES.addCell("");

				if (col.getInflectionFDoublePrime() != null)
					tableES.addCell(new Paragraph(col.getInflectionFDoublePrime().toString(), FONT_DOC));
				else
					tableES.addCell("");

				if (col.getPeakEnergy() != null)
					tableES.addCell(new Paragraph(col.getPeakEnergy().toString(), FONT_DOC));
				else
					tableES.addCell("");

				if (col.getPeakFPrime() != null)
					tableES.addCell(new Paragraph(col.getPeakFPrime().toString(), FONT_DOC));
				else
					tableES.addCell("");

				if (col.getPeakFDoublePrime() != null)
					tableES.addCell(new Paragraph(col.getPeakFDoublePrime().toString(), FONT_DOC));
				else
					tableES.addCell("");

				if (col.getBeamSizeHorizontal() != null)
					tableES.addCell(new Paragraph(col.getBeamSizeHorizontal().toString(), FONT_DOC));
				else
					tableES.addCell("");

				if (col.getBeamSizeVertical() != null)
					tableES.addCell(new Paragraph(col.getBeamSizeVertical().toString(), FONT_DOC));
				else
					tableES.addCell("");

				if (col.getTransmissionFactor() != null)
					tableES.addCell(new Paragraph(col.getTransmissionFactor().toString(), FONT_DOC));
				else
					tableES.addCell("");

				if (col.getRemoteEnergy() != null)
					tableES.addCell(new Paragraph(col.getRemoteEnergy().toString(), FONT_DOC));
				else
					tableES.addCell("");

				if (col.getRemoteFPrime() != null)
					tableES.addCell(new Paragraph(col.getRemoteFPrime().toString(), FONT_DOC));
				else
					tableES.addCell("");

				if (col.getRemoteFDoublePrime() != null)
					tableES.addCell(new Paragraph(col.getRemoteFDoublePrime().toString(), FONT_DOC));
				else
					tableES.addCell("");


				if (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX)) {
					// if (col.getCrystalClass()!= null) tableES.addCell(new
					// Paragraph(col.getCrystalClass().toString(),
					// new Font(Font.HELVETICA, 8)));
					// else tableES.addCell("");
					if (col.getCrystalClass() != null) {
						int idCC = getCrystalClassIndex(listOfCrystalClass, col.getCrystalClass().trim().toUpperCase());
						if (idCC == -1) {
							tableES.addCell(new Paragraph(col.getCrystalClass().toString(), FONT_DOC));
						} else
							tableES.addCell(new Paragraph(listOfCrystalClass.get(idCC).getCrystalClassName(), FONT_DOC));
					} else
						tableES.addCell("");
				}

				tableES.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				if (col.getComments() != null)
					tableES.addCell(new Paragraph(col.getComments().toString(), FONT_DOC));
				else
					tableES.addCell("");
				tableES.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

			}
			document.add(tableES);
		}
	}

	/**
	 * set the XFR Spectra table
	 * 
	 * @param document
	 * @throws Exception
	 */
	private void setXfrSpectraTable(Document document) throws Exception {
		if (slv == null) {// if session is null, no sense to display XFRSpectra
			return;
		}
		document.add(new Paragraph("XRF Spectra:", FONT_TITLE));
		document.add(new Paragraph(" "));
		if (xfeList == null || xfeList.isEmpty()) {
			document.add(new Paragraph("There is no XRF spectra in this report", FONT_DOC));
		} else {
			int NumColumnsXRF = 6;
			Table tableXRF = new Table(NumColumnsXRF);
			int headerwidthsSessionXRF[] = { 15, 15, 15, 15, 15, 25 }; // percentage
			int headerwidthsMXPressXRF[] = { 16, 16, 15, 15, 15, 8, 17 }; // percentage
			tableXRF.setWidths(headerwidthsSessionXRF);
			// Column crystalClass added only for IFX proposal in case of
			// MXPress experiment
			if (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX)) {
				tableXRF = new Table(NumColumnsXRF + 1);
				tableXRF.setWidths(headerwidthsMXPressXRF);
			}

			tableXRF.setWidth(100); // percentage
			tableXRF.setPadding(3);
			tableXRF.setCellsFitPage(true);
			tableXRF.getDefaultCell().setBorderWidth(1);
			tableXRF.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

			// XRF Spectra Header

			tableXRF.getDefaultCell().setGrayFill(GREY_FILL_HEADER);
			tableXRF.addCell(new Paragraph("Energy\n(keV)", FONT_DOC_BOLD));
			tableXRF.addCell(new Paragraph("Exposure Time\n(s)", FONT_DOC_BOLD));
			tableXRF.addCell(new Paragraph("Beam size Hor.\n(" + Constants.MICRO + "m)", FONT_DOC_BOLD));
			tableXRF.addCell(new Paragraph("Beam size Ver.\n(" + Constants.MICRO + "m)", FONT_DOC_BOLD));
			tableXRF.addCell(new Paragraph("Transm. Factor\n(%)", FONT_DOC_BOLD));
			// Column crystalClass only for IFX proposal in case of MXPress
			// experiment
			if (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX))
				tableXRF.addCell(new Paragraph("Crystal class", FONT_DOC_BOLD));
			tableXRF.addCell(new Paragraph("Comments", FONT_DOC_BOLD));
			tableXRF.getDefaultCell().setGrayFill(GREY_FILL_DATA);
			tableXRF.getDefaultCell().setBorderWidth(1);

			// XRF Spectra Rows
			Iterator<XFEFluorescenceSpectrum3VO> itES = xfeList.iterator();
			while (itES.hasNext()) {
				XFEFluorescenceSpectrum3VO col = itES.next();
				if (col.getEnergy() != null)
					tableXRF.addCell(new Paragraph(col.getEnergy().toString(), FONT_DOC));
				else
					tableXRF.addCell("");

				if (col.getExposureTime() != null)
					tableXRF.addCell(new Paragraph(col.getExposureTime().toString(), FONT_DOC));
				else
					tableXRF.addCell("");

				if (col.getBeamSizeHorizontal() != null)
					tableXRF.addCell(new Paragraph(col.getBeamSizeHorizontal().toString(), FONT_DOC));
				else
					tableXRF.addCell("");

				if (col.getBeamSizeVertical() != null)
					tableXRF.addCell(new Paragraph(col.getBeamSizeVertical().toString(), FONT_DOC));
				else
					tableXRF.addCell("");

				if (col.getBeamTransmission() != null)
					tableXRF.addCell(new Paragraph(col.getBeamTransmission().toString(), FONT_DOC));
				else
					tableXRF.addCell("");

				if (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX)) {
					// if (col.getCrystalClass()!= null) tableXRF.addCell(new
					// Paragraph(col.getCrystalClass().toString(), new
					// Font(Font.HELVETICA, 8)));
					// else tableXRF.addCell("");
					if (col.getCrystalClass() != null) {
						int idCC = getCrystalClassIndex(listOfCrystalClass, col.getCrystalClass().trim().toUpperCase());
						if (idCC == -1) {
							tableXRF.addCell(new Paragraph(col.getCrystalClass().toString(), FONT_DOC));
						} else
							tableXRF.addCell(new Paragraph(listOfCrystalClass.get(idCC).getCrystalClassName(), FONT_DOC));
					} else
						tableXRF.addCell("");
				}

				tableXRF.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				if (col.getComments() != null)
					tableXRF.addCell(new Paragraph(col.getComments().toString(), FONT_DOC));
				else
					tableXRF.addCell("");
				tableXRF.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

			}
			document.add(tableXRF);
		}
	}

	/**
	 * set the summary - IFX proposal
	 * 
	 * @param document
	 * @throws Exception
	 */
	private void setSummary(Document document) throws Exception {
		if (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX) && name == null) {
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

	/**
	 * export detailed dataCollection
	 * 
	 * @return
	 * @throws Exception
	 */
	public ByteArrayOutputStream exportScreeningAsPDF(boolean rtfFormat, HttpServletRequest mRequest) throws Exception {
		// create simple doc and write to a ByteArrayOutputStream
		Document document = new Document(PageSize.A4.rotate(), 20, 20, 20, 20);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (!rtfFormat) {
			isRTF = new Boolean(false);
			PdfWriter.getInstance(document, baos);
		} else {
			isRTF = new Boolean(true);
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
		// Session comments
		setSessionComments(document);
		document.add(new Paragraph(" "));
		if (dataCollectionList.isEmpty()) {
			document.add(new Paragraph("There is no data collection in this report", PdfRtfExporter.FONT_DOC));
		} else {
			Iterator<DataCollection3VO> it = dataCollectionList.iterator();
			Session3Service sessionService = (Session3Service) ejb3ServiceLocator
					.getLocalService(Session3Service.class);
			AutoProc3VO[] autoProcs = wrapper.getAutoProcs();
			AutoProcScalingStatistics3VO[] autoProcsOverall = wrapper.getScalingStatsOverall();
			AutoProcScalingStatistics3VO[] autoProcsInner = wrapper.getScalingStatsInner();
			AutoProcScalingStatistics3VO[] autoProcsOuter = wrapper.getScalingStatsOuter();
			int i = 0;
			// int mNbDataCollectionOnPage = 0;
			boolean firstPage = true;
			while (it.hasNext()) {
				if (firstPage) {
					firstPage = false;
				} else {
					document.newPage();
				}
				DataCollection3VO dcValue = it.next();
				DataCollectionExporter dcExporter = new DataCollectionExporter(df2, df3, proposalCode, proposalNumber,
						mRequest);
				DataCollectionInformation dcInfo = dcExporter.getDataCollectionInformation(dcValue,
						getSampleRankingVO(dcValue.getDataCollectionId()),
						getAutoProcRankingVO(dcValue.getDataCollectionId()));
				// dataCollection general info: Date, screen/Collect, indexing
				// status
				setDataCollectionGeneralInfo(document, dcValue, dcInfo);
				Table table = new Table(NB_COL_DATACOLLECTION);
				table.setCellsFitPage(true);
				
				// dataCollection table - one row
				table = setDataCollectionHeader(true);
				document.add(table);
				setDataCollectionData( table, dcValue, sessionService, autoProcs[i], autoProcsOverall[i],
						autoProcsInner[i], autoProcsOuter[i], true, false, null, null);
				
				
				i++;
				document.add(new Paragraph(" ", VERY_SMALL_FONT));
				// Images
				setImagesTable(document, dcInfo);
				setEDNATable(document, dcInfo);
				setStrategyTable(document, dcInfo);

			}
		}

		// ======================
		// End of file
		// ======================
		document.close();
		return baos;
	}

	/**
	 * export detailed dataCollection report: new screening report
	 * 
	 * @return
	 * @throws Exception
	 */
	public ByteArrayOutputStream exportDetails(boolean rtfFormat, HttpServletRequest mRequest) throws Exception {
		// create simple doc and write to a ByteArrayOutputStream
		Document document = new Document(PageSize.A4.rotate(), 20, 20, 20, 20);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (!rtfFormat) {
			isRTF = new Boolean(false);
			PdfWriter.getInstance(document, baos);
		} else {
			isRTF = new Boolean(true);
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
		List<SessionDataObjectInformation> listSessionDataObject = sessionDataObjectList;
		if (listSessionDataObject.isEmpty()) {
			document.add(new Paragraph("There is no data in this report", PdfRtfExporter.FONT_DOC));
		} else {
			// for each object, we will display a different table
			int countTables = 0;
			for (Iterator<SessionDataObjectInformation> iterator = listSessionDataObject.iterator(); iterator.hasNext();) {
				SessionDataObjectInformation sessionDataObject = (SessionDataObjectInformation) iterator.next();
				if (countTables > 1) {
					// System.out.println("page break");
					document.newPage();
					countTables = 0;
				}
				setDetailSessionObjectTable(document, sessionDataObject, mRequest);
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
	private void setDetailSessionObjectTable(Document document, SessionDataObjectInformation sessionDataObject,
			HttpServletRequest mRequest) {
		try {

			int nbCol = 6;
			List<Param> listParam = sessionDataObject.getListParameters();
			int nbParam = listParam.size();
			boolean secondGraph = sessionDataObject.getGraph2Path() != null
					&& !sessionDataObject.getGraph2Path().isEmpty();
			if (secondGraph)
				nbCol += 1;

			Table table1 = new Table(nbCol);

			int l = 0;

			int[] headersWidth = new int[nbCol];

			headersWidth[l++] = 10; // def
			headersWidth[l++] = 8; // parameters title
			headersWidth[l++] = 8; // parameters value
			headersWidth[l++] = 12; // Thumbnail
			headersWidth[l++] = 12; // Snapshot
			headersWidth[l++] = 20; // Graph
			if (secondGraph)
				headersWidth[l++] = 20; // Graph2
			table1.setWidths(headersWidth);

			table1.setWidth(100); // percentage
			table1.setPadding(5);
			table1.setCellsFitPage(true);
			table1.setTableFitsPage(true);
			table1.setBorderWidth(0);
			table1.setBorder(0);
			table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.getDefaultCell().setBorder(0);
			table1.getDefaultCell().setBorderWidth(0);
			
			String parag ="";
			// first Row
			// firstCell: def : date
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String date = formatter.format(sessionDataObject.getDataTime()) ;
			parag = date + "\n" 
					+ sessionDataObject.getImagePrefix() + " " + sessionDataObject.getRunNumber() + "\n" 
					+ sessionDataObject.getExperimentType() + "\n" 
					+ sessionDataObject.getSampleNameProtein() +"\n" 
					+ sessionDataObject.getComments() + "\n" 
					;
			
			Paragraph p = new Paragraph(parag, FONT_DOC_BOLD);
			Cell cell = new Cell(p);
			cell.setBorder(0);
			cell.setHorizontalAlignment("LEFT");
			table1.addCell(cell);
						
			// Cell2 
			parag ="";
			for (int i = 0; i < nbParam; i++) {
				parag = parag + listParam.get(i).getText() + ": \n";
			}
			p = new Paragraph(parag, FONT_DOC_PARAM_TITLE);
			cell = new Cell(p);
			cell.setBorder(0);
			cell.setHorizontalAlignment("RIGHT");
			table1.addCell(cell);
			
			// Cell3
			parag ="";			
			for (int i = 0; i < nbParam; i++) {
				parag = parag + listParam.get(i).getValue() + "\n";
			}
			p = new Paragraph(parag, FONT_DOC);
			cell = new Cell(p);
			cell.setBorder(0);
			cell.setHorizontalAlignment("LEFT");
			table1.addCell(cell);
						
			// Cell4 : thumbnail
			Cell cellThumbnail = getCellImage(sessionDataObject.getImageThumbnailPath());
			cellThumbnail.setBorderWidth(0);
			table1.addCell(cellThumbnail);
			
			// 5 Cell : snapshot
			Cell cellSnapshot = getCellImage(sessionDataObject.getCrystalSnapshotPath());
			cellSnapshot.setBorderWidth(0);
			table1.addCell(cellSnapshot);
			
			// 6 Cell : graph
			Cell cellGraph = getCellGraph(sessionDataObject);
			cellGraph.setBorderWidth(0);
			table1.addCell(cellGraph);
			
			// 7 Cell : graph2
			if (secondGraph) {
				Cell cellGraph2 = getCellImage(sessionDataObject.getGraph2Path());
				cellGraph2.setBorderWidth(0);
				table1.addCell(cellGraph2);
			}
			document.add(table1);
			
			// 	other rows			
			// results
					
			// workflow result status
			Table table = new Table(1);
			table.setBorderWidth(0);
			table.setBorder(0);
			
			if (sessionDataObject.isWorkflow()) {
				parag ="";
				Cell resultCell = getWorkflowResult(sessionDataObject.getWorkflow(), mRequest);
				resultCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				resultCell.setBorder(0);
				table.addCell(resultCell);
				document.add(table);
			}
						
			// collect OSC
			table = new Table(1);
			table.setBorderWidth(0);
			table.setBorder(0);

			if ((sessionDataObject.isDataCollection() && !sessionDataObject.getDataCollection()
					.getDataCollectionGroupVO().getExperimentType().equals(Constants.EXPERIMENT_TYPE_CHARACTERIZATION))) {
				DataCollectionExporter dcExporter = new DataCollectionExporter(df2, df3, proposalCode, proposalNumber,
						mRequest);
				DataCollection3VO dataCollection = sessionDataObject.getDataCollection();
				
				DataCollectionInformation dcInfo = dcExporter.getDataCollectionInformation(dataCollection,
						getSampleRankingVO(dataCollection.getDataCollectionId()),
						getAutoProcRankingVO(dataCollection.getDataCollectionId()));
				Cell resultCell = getAutoProcResultStatus(dcInfo);
				resultCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				resultCell.setBorder(0);
				table.addCell(resultCell);
				
				document.add(table);
				document.add(new Paragraph(" ", VERY_SMALL_FONT));

				setAutoProcResultsTable(document, dcInfo);
				
			} else if (sessionDataObject.isWorkflow() && sessionDataObject.getWorkflow().isMXPressEOIA()) { // MXPRESS
																										// wf
				DataCollectionExporter dcExporter = new DataCollectionExporter(df2, df3, proposalCode, proposalNumber,
						mRequest);
				DataCollection3VO dataCollection = sessionDataObject.getListDataCollection().get(0);
				
				DataCollectionInformation dcInfo = dcExporter.getDataCollectionInformation(dataCollection,
						getSampleRankingVO(dataCollection.getDataCollectionId()),
						getAutoProcRankingVO(dataCollection.getDataCollectionId()));
				Cell resultCell = getAutoProcResultStatus(dcInfo);
				resultCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				resultCell.setBorder(0);
				table.addCell(resultCell);

				document.add(table);
				
				document.add(new Paragraph(" ", VERY_SMALL_FONT));

				setAutoProcResultsTable(document, dcInfo);
				
			} else if ((sessionDataObject.isDataCollection() && sessionDataObject.getDataCollection()
					.getDataCollectionGroupVO().getExperimentType().equals(Constants.EXPERIMENT_TYPE_CHARACTERIZATION))) { // Characterization
				DataCollectionExporter dcExporter = new DataCollectionExporter(df2, df3, proposalCode, proposalNumber,
						mRequest);
				DataCollection3VO dataCollection = sessionDataObject.getDataCollection();
				
				DataCollectionInformation dcInfo = dcExporter.getDataCollectionInformation(dataCollection,
						getSampleRankingVO(dataCollection.getDataCollectionId()),
						getAutoProcRankingVO(dataCollection.getDataCollectionId()));
				Cell resultCell = getCharacterizationResultStatus(dcInfo, mRequest);
				resultCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				resultCell.setBorder(0);
				table.addCell(resultCell);
				
				document.add(table);
				document.add(new Paragraph(" ", VERY_SMALL_FONT));

				setStrategyTable2(document, dcInfo);
				
			} else {
				Table table2 = new Table(nbCol);
				table2.setWidths(headersWidth);

				table2.setWidth(100); // percentage
				table2.setPadding(5);
				table2.setCellsFitPage(true);
				table2.setTableFitsPage(true);
				table2.setBorderWidth(0);
				table2.setBorder(0);
				table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				table2.getDefaultCell().setBorder(0);
				table2.getDefaultCell().setBorderWidth(0);
				
				//col 1
				table2.addCell(" ");
				
				// col2
				parag ="";
				List<Param> listResults = sessionDataObject.getListResults();
				if (listResults != null) {
					int nbResults = listResults.size();
					for (int j = 0; j < nbResults; j++) {
						parag = parag + setParamText(table2, listResults, j) + ": \n";
					}
				}
				p = new Paragraph(parag, FONT_DOC_PARAM_TITLE);
				p.setAlignment("RIGHT");
				table2.addCell(p);

				//col3
				parag ="";
				if (listResults != null) {
					int nbResults = listResults.size();
					for (int j = 0; j < nbResults; j++) {
						parag = parag + setParamValue(table2, listResults, j) + "\n";
					}
				}
				p = new Paragraph(parag, FONT_DOC);
				p.setAlignment("LEFT");
				table2.addCell(p);

				// other cols
				for (int i= 3; i< nbCol; i++) {
					table2.addCell(" ");
				}				
				document.add(table2);		
				p = new Paragraph("______________________________________________________________________________________________________________________________________", FONT_SPACE);
				p.setAlignment("CENTER");
				document.add(p);
				document.add(new Paragraph(" ", VERY_SMALL_FONT));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * returns the cell for the result of a workflow: status of this workflow
	 * 
	 * @param workflow
	 * @param mRequest
	 * @return
	 * @throws BadElementException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private Cell getWorkflowResult(Workflow3VO workflow, HttpServletRequest mRequest) throws BadElementException,
			MalformedURLException, IOException {
		Cell resultsCell = new Cell();
		Paragraph p = new Paragraph();

		if (workflow != null && workflow.getWorkflowType() != null) {
			p.add(new Phrase(workflow.getWorkflowType() + " ", FONT_DOC_BOLD));
			String img = mRequest.getRealPath(Constants.IMAGE_BLANK);
			String wfStatus = workflow.getStatus();
			if (wfStatus != null) {
				if (wfStatus.equals("Failure")) {
					img = mRequest.getRealPath(Constants.IMAGE_FAILED);
					p.setFont(FONT_INDEXING_FAILED);
				} else if (wfStatus.equals("Launched")) {
					img = mRequest.getRealPath(Constants.IMAGE_LAUNCHED);
					p.setFont(FONT_INDEXING_NOTDONE);
				} else if (wfStatus.equals("Success")) {
					img = mRequest.getRealPath(Constants.IMAGE_SUCCESS);
					p.setFont(FONT_INDEXING_SUCCESS);
				}
			}
			if (! isRTF)
				p.add(getChunkImage(img));
			p.add(wfStatus);
			p.add(new Phrase("  "));
		}

		resultsCell.add(p);
		return resultsCell;
	}

	/**
	 * get Status for Characterization (indexing and Strategy)
	 * 
	 * @param dcInfo
	 * @param mRequest
	 * @return
	 * @throws BadElementException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private Cell getCharacterizationResultStatus(DataCollectionInformation dcInfo, HttpServletRequest mRequest)
			throws BadElementException, MalformedURLException, IOException {
		Cell resultsCell = new Cell();
		Paragraph p = new Paragraph();
		if (dcInfo != null && dcInfo.getScreeningIndexingSuccess() != null) {
			p.add(new Phrase("Indexing ", FONT_DOC_BOLD));
			String img = mRequest.getRealPath(Constants.IMAGE_FAILED);
			p.setFont(FONT_INDEXING_FAILED);
			if (dcInfo.getScreeningIndexingSuccess() == 1) {
				img = mRequest.getRealPath(Constants.IMAGE_SUCCESS);
				p.setFont(FONT_INDEXING_SUCCESS);
			}
			if (!isRTF)
				p.add(getChunkImage(img));
			p.add(new Phrase("  "));
		}

		if (dcInfo != null && dcInfo.getScreeningStrategySuccess() != null) {
			p.add(new Phrase("Strategy ", FONT_DOC_BOLD));
			String img = mRequest.getRealPath(Constants.IMAGE_FAILED);
			p.setFont(FONT_INDEXING_FAILED);
			if (dcInfo.getScreeningStrategySuccess() == 1) {
				img = mRequest.getRealPath(Constants.IMAGE_SUCCESS);
				p.setFont(FONT_INDEXING_SUCCESS);
			}
			if (!isRTF)
				p.add(getChunkImage(img));
			p.add(new Phrase("  "));
		}

		resultsCell.add(p);
		return resultsCell;
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
	private void setCellParam(Table table, List<Param> listParam, int id, int colSpanTitle) throws Exception {
		if (id < listParam.size()) {
			Param param = listParam.get(id);
			Cell cellTitle = new Cell();
			cellTitle.setBorderWidth(0);
			cellTitle.setColspan(colSpanTitle);
			cellTitle.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cellTitle.add(new Paragraph(param.getText(), FONT_DOC_PARAM_TITLE));
			table.addCell(cellTitle);

			Cell cellValue = new Cell();
			cellValue.setBorderWidth(0);
			cellValue.setHorizontalAlignment(Element.ALIGN_LEFT);
			cellValue.add(new Paragraph(param.getValue(), FONT_DOC));
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
	
	private String setParamText(Table table, List<Param> listParam, int id) throws Exception {
		String text = "";
		if (id < listParam.size()) {
			Param param = listParam.get(id);			
			text = param.getText();
		}
		return text;
	}
	
	private String setParamValue(Table table, List<Param> listParam, int id) throws Exception {
		String text = "";
		if (id < listParam.size()) {
			Param param = listParam.get(id);			
			text = param.getValue();
		}
		return text;
	}
	
	private void setEmptyCell(Table table, int nb) throws Exception {
		Cell emptyCell = new Cell(new Paragraph("empty", FONT_DOC));
		for (int i = 0; i < nb; i++) {
			table.addCell(emptyCell);
		}
				
	}


	/**
	 * export ranking dataCollection
	 * 
	 * @return
	 * @throws Exception
	 */
	public ByteArrayOutputStream exportRankingAsPDF(boolean rtfFormat, HttpServletRequest mRequest) throws Exception {
		// create simple doc and write to a ByteArrayOutputStream
		Document document = new Document(PageSize.A4.rotate(), 20, 20, 20, 20);
		// Document document = new Document(PageSize.A4);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (!rtfFormat) {
			isRTF = new Boolean(false);
			PdfWriter.getInstance(document, baos);
		} else {
			isRTF = new Boolean(true);
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
		// Session comments
		setSessionComments(document);
		document.add(new Paragraph(" "));
		if (dataCollectionList.isEmpty()) {
			document.add(new Paragraph("There is no data collection in this report", PdfRtfExporter.FONT_DOC));
		} else {
			Iterator<DataCollection3VO> it = dataCollectionList.iterator();
			Session3Service sessionService = (Session3Service) ejb3ServiceLocator
					.getLocalService(Session3Service.class);
			AutoProc3VO[] autoProcs = wrapper.getAutoProcs();
			AutoProcScalingStatistics3VO[] autoProcsOverall = wrapper.getScalingStatsOverall();
			AutoProcScalingStatistics3VO[] autoProcsInner = wrapper.getScalingStatsInner();
			AutoProcScalingStatistics3VO[] autoProcsOuter = wrapper.getScalingStatsOuter();
			int i = 0;
			boolean firstPage = true;
			while (it.hasNext()) {
				if (firstPage) {
					firstPage = false;
				} else {
					document.newPage();
				}
				DataCollection3VO dcValue = it.next();
				DataCollectionExporter dcExporter = new DataCollectionExporter(df2, df3, proposalCode, proposalNumber,
						mRequest);
				DataCollectionInformation dcInfo = dcExporter.getDataCollectionInformation(dcValue,
						getSampleRankingVO(dcValue.getDataCollectionId()),
						getAutoProcRankingVO(dcValue.getDataCollectionId()));
				// dataCollection general info: Date, screen/Collect, indexing
				// status
				setDataCollectionGeneralInfo(document, dcValue, dcInfo);
				Table table = new Table(NB_COL_DATACOLLECTION);
				table.setCellsFitPage(true);
				// dataCollection table - one row
				table = setDataCollectionHeader(false);
				setDataCollectionData(table, dcValue, sessionService, autoProcs[i], autoProcsOverall[i],
						autoProcsInner[i], autoProcsOuter[i], false, false, null, null);
				document.add(table);
				i++;
				document.add(new Paragraph(" ", VERY_SMALL_FONT));
				setRankingTable(document, dcInfo);
				// Images
				setImagesTable(document, dcInfo);
				setEDNATable(document, dcInfo);
				setStrategyTable(document, dcInfo);
			}
		}

		// ======================
		// End of file
		// ======================
		document.close();
		return baos;
	}

	/**
	 * export auto proc ranking dataCollection
	 * 
	 * @return
	 * @throws Exception
	 */
	public ByteArrayOutputStream exportAutoProcRankingAsPDF(boolean rtfFormat, HttpServletRequest mRequest)
			throws Exception {
		// create simple doc and write to a ByteArrayOutputStream
		Document document = new Document(PageSize.A4.rotate(), 20, 20, 20, 20);
		// Document document = new Document(PageSize.A4);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (!rtfFormat) {
			isRTF = new Boolean(false);
			PdfWriter.getInstance(document, baos);
		} else {
			isRTF = new Boolean(true);
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
		// Session comments
		setSessionComments(document);
		document.add(new Paragraph(" "));
		if (dataCollectionList.isEmpty()) {
			document.add(new Paragraph("There is no data collection in this report", PdfRtfExporter.FONT_DOC));
		} else {
			Iterator<DataCollection3VO> it = dataCollectionList.iterator();
			Session3Service sessionService = (Session3Service) ejb3ServiceLocator
					.getLocalService(Session3Service.class);
			AutoProc3VO[] autoProcs = wrapper.getAutoProcs();
			AutoProcScalingStatistics3VO[] autoProcsOverall = wrapper.getScalingStatsOverall();
			AutoProcScalingStatistics3VO[] autoProcsInner = wrapper.getScalingStatsInner();
			AutoProcScalingStatistics3VO[] autoProcsOuter = wrapper.getScalingStatsOuter();
			int i = 0;
			boolean firstPage = true;
			while (it.hasNext()) {
				if (firstPage) {
					firstPage = false;
				} else {
					document.newPage();
				}
				DataCollection3VO dcValue = it.next();
				DataCollectionExporter dcExporter = new DataCollectionExporter(df2, df3, proposalCode, proposalNumber,
						mRequest);
				DataCollectionInformation dcInfo = dcExporter.getDataCollectionInformation(dcValue,
						getSampleRankingVO(dcValue.getDataCollectionId()),
						getAutoProcRankingVO(dcValue.getDataCollectionId()));
				// dataCollection general info: Date, screen/Collect, indexing
				// status
				setDataCollectionGeneralInfo(document, dcValue, dcInfo);
				Table table = new Table(NB_COL_DATACOLLECTION);
				table.setCellsFitPage(true);
				// dataCollection table - one row
				table = setDataCollectionHeader(false);
				setDataCollectionData( table, dcValue, sessionService, autoProcs[i], autoProcsOverall[i],
						autoProcsInner[i], autoProcsOuter[i], false, false, null, null);
				
				document.add(table);
				i++;
				document.add(new Paragraph(" ", VERY_SMALL_FONT));
				setAutoProcRankingTable(document, dcInfo);
				// Images
				setImagesTable(document, dcInfo);
				setEDNATable(document, dcInfo);
				setStrategyTable(document, dcInfo);
			}
		}

		// ======================
		// End of file
		// ======================
		document.close();
		return baos;
	}

	/**
	 * sets the date and if it is a SCREEN or a COLLECT and indexing status
	 * 
	 * @param document
	 * @param dcValue
	 * @param date
	 * @throws Exception
	 */
	private void setDataCollectionGeneralInfo(Document document, DataCollection3VO dcValue,
			DataCollectionInformation dcInfo) throws Exception {
		String collect = "Collect";
		if (DataCollectionExporter.isDataCollectionScreening(dcValue)) {
			collect = "Screen";
		}
		String date = dcInfo.getDataCollectionDate();
		String screeningSuccess = dcInfo.getScreeningSuccess();
		String screeningFailure = dcInfo.getScreeningFailure();
		String screeningNotDone = dcInfo.getScreeningNotDone();
		Table aTable = new Table(3);
		aTable.setWidth(100); // percentage
		aTable.setPadding(3);
		aTable.setCellsFitPage(true);
		aTable.getDefaultCell().setBorderWidth(0);
		aTable.setBorderWidth(0);
		aTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		aTable.addCell(new Paragraph(date, FONT_DOC));
		aTable.addCell(new Paragraph(collect, FONT_DOC));
		// no indexing availabe
		if (screeningNotDone != null && !screeningNotDone.equals("")) {
			aTable.addCell(new Paragraph(screeningNotDone, FONT_INDEXING_NOTDONE));
		} else if (screeningFailure != null && !screeningFailure.equals("")) { // indexing
																				// failed
			aTable.addCell(new Paragraph(screeningFailure, FONT_INDEXING_FAILED));
		} else if (screeningSuccess != null && !screeningSuccess.equals("")) { // indexing
																				// successful
			aTable.addCell(new Paragraph(screeningSuccess, FONT_INDEXING_SUCCESS));
		} else {
			aTable.addCell(new Paragraph(" "));
		}
		document.add(aTable);
		document.add(new Paragraph(" ", VERY_SMALL_FONT));
	}

	/**
	 * set the EDNA Table
	 * 
	 * @param document
	 * @param dcInfo
	 * @throws Exception
	 */
	private void setEDNATable(Document document, DataCollectionInformation dcInfo) throws Exception {
		if (dcInfo.getSpacegroup() != "") {
			int noCol = 4;
			Table tableEDNA = new Table(noCol);
			int headerEDNA[] = { 10, 10, 10, 10 }; // percentage
			tableEDNA.setWidths(headerEDNA);
			tableEDNA.setWidth(70); // percentage
			tableEDNA.setPadding(3);
			tableEDNA.setCellsFitPage(true);
			tableEDNA.setTableFitsPage(true);
			// tableEDNA.getDefaultCell().setBorderWidth(1);
			tableEDNA.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			// EDNA Header
			tableEDNA.getDefaultCell().setBackgroundColor(BLUE_COLOR);
			tableEDNA.addCell(new Paragraph("Space Group", FONT_DOC_BOLD));
			tableEDNA.addCell(new Paragraph("Unit Cell", FONT_DOC_BOLD));
			tableEDNA.addCell(new Paragraph("Mosaicity\n(" + Constants.ANGSTROM + ")", FONT_DOC_BOLD));
			tableEDNA.addCell(new Paragraph("Ranking Resolution\n(" + Constants.ANGSTROM + ")", FONT_DOC_BOLD));
			tableEDNA.getDefaultCell().setBorderWidth(1);
			tableEDNA.getDefaultCell().setBackgroundColor(WHITE_COLOR);
			// EDNA data
			String unitCell = dcInfo.getCellA() + " (" + dcInfo.getCellAlpha() + ")\n" + dcInfo.getCellB() + " ("
					+ dcInfo.getCellBeta() + ")\n" + dcInfo.getCellC() + " (" + dcInfo.getCellGamma() + ")";

			tableEDNA.addCell(new Paragraph(dcInfo.getSpacegroup(), FONT_DOC));
			tableEDNA.addCell(new Paragraph(unitCell, FONT_DOC));
			tableEDNA.addCell(new Paragraph(dcInfo.getMosaicity(), FONT_DOC));
			tableEDNA.addCell(new Paragraph(dcInfo.getResObserved(), FONT_DOC));
			//

			document.add(tableEDNA);
			document.add(new Paragraph(" ", VERY_SMALL_FONT));
		}
	}

	/*
	 * copy from setEDNA table, waiting for feedback that we can remove the old
	 * reports and old setEDNA table
	 */
	private void setEDNATable2(Document document, DataCollectionInformation dcInfo) throws Exception {
		if (dcInfo.getSpacegroup() != "") {
			int noCol = 4;
			Table tableEDNA = new Table(noCol);
			int headerEDNA[] = { 10, 10, 10, 10 }; // percentage
			tableEDNA.setWidths(headerEDNA);
			tableEDNA.setWidth(100); // percentage
			tableEDNA.setPadding(3);
			tableEDNA.setCellsFitPage(true);
			tableEDNA.setTableFitsPage(true);
			// tableEDNA.getDefaultCell().setBorderWidth(1);
			tableEDNA.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			// EDNA Header
			tableEDNA.getDefaultCell().setGrayFill(GREY_FILL_HEADER);
			tableEDNA.addCell(new Paragraph("Space Group", FONT_DOC_BOLD));
			tableEDNA.addCell(new Paragraph("Unit Cell (a, b, c, alpha, beta, gamma)", FONT_DOC_BOLD));
			tableEDNA.addCell(new Paragraph("Mosaicity (" + Constants.DEGREE + ")", FONT_DOC_BOLD));
			tableEDNA.addCell(new Paragraph("Ranking Resolution (" + Constants.ANGSTROM + ")", FONT_DOC_BOLD));
			tableEDNA.getDefaultCell().setBorderWidth(1);
			tableEDNA.getDefaultCell().setBackgroundColor(WHITE_COLOR);
			// EDNA data
			String unitCell = dcInfo.getCellA() + ", " + dcInfo.getCellB() + ", " + dcInfo.getCellC() + "\n"
					+ dcInfo.getCellAlpha() + ", " + dcInfo.getCellBeta() + ", " + dcInfo.getCellGamma();
			tableEDNA.addCell(new Paragraph(dcInfo.getSpacegroup(), FONT_DOC));
			tableEDNA.addCell(new Paragraph(unitCell, FONT_DOC));
			tableEDNA.addCell(new Paragraph(dcInfo.getMosaicity(), FONT_DOC));
			tableEDNA.addCell(new Paragraph(dcInfo.getResObserved(), FONT_DOC));
			//

			document.add(tableEDNA);
			document.add(new Paragraph(" ", VERY_SMALL_FONT));
		}
	}

	private void setImagesTable(Document document, DataCollectionInformation dcInfo) throws Exception {
		String imgCrystal = dcInfo.getPathjpgCrystal();
		String imgDiff1 = dcInfo.getPathDiffractionImg1();
		String imgDiff2 = dcInfo.getPathDiffractionImg2();
		int nbCol = 0;
		if (imgCrystal != null && !imgCrystal.equals("")) {
			nbCol++;
		}
		if (imgDiff1 != null && !imgDiff1.equals("")) {
			nbCol++;
		}
		if (imgDiff2 != null && !imgDiff2.equals("")) {
			nbCol++;
		}
		if (nbCol > 0) {
			Table tableImage = new Table(nbCol);
			tableImage.setCellsFitPage(true);
			tableImage.setWidth(100); // percentage
			tableImage.getDefaultCell().setBorderWidth(0);
			tableImage.setBorderWidth(0);
			tableImage.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			// ------------------------------------- Insert crystal image
			// ----------------------------
			if (imgCrystal != null && !imgCrystal.equals("")) {
				try {
					Image jpgCrystal = Image.getInstance(imgCrystal);
					jpgCrystal.scaleAbsolute(CRYSTAL_IMAGE_WIDTH, CRYSTAL_IMAGE_HEIGHT);
					Cell cellCrystalImage = new Cell(jpgCrystal);
					cellCrystalImage.setLeading(0);
					cellCrystalImage.setBorderWidth(0);
					cellCrystalImage.setHorizontalAlignment(Element.ALIGN_CENTER);
					tableImage.addCell(cellCrystalImage);
				} catch (IOException e) {
					tableImage.addCell(new Paragraph(imgCrystal + " not found", FONT_DOC));
				}
			}
			// --- Image 1
			if (imgDiff1 != null && !imgDiff1.equals("")) {
				try {
					Image jpg1 = Image.getInstance(imgDiff1);
					jpg1.scaleAbsolute(DIFF_IMAGE_WIDTH, DIFF_IMAGE_HEIGHT);
					Cell cellDiffractionThumbnail1 = new Cell(jpg1);
					cellDiffractionThumbnail1.setLeading(0);
					cellDiffractionThumbnail1.setBorderWidth(0);
					cellDiffractionThumbnail1.setHorizontalAlignment(Element.ALIGN_CENTER);
					tableImage.addCell(cellDiffractionThumbnail1);
				} catch (IOException e) {
					tableImage.addCell(new Paragraph(imgDiff1 + " not found", FONT_DOC));
				}
			}
			// --- Image 2
			if (imgDiff2 != null && !imgDiff2.equals("")) {
				try {
					Image jpg2 = Image.getInstance(imgDiff2);
					jpg2.scaleAbsolute(DIFF_IMAGE_WIDTH, DIFF_IMAGE_HEIGHT);
					Cell cellDiffractionThumbnail2 = new Cell(jpg2);
					cellDiffractionThumbnail2.setLeading(0);
					cellDiffractionThumbnail2.setBorderWidth(0);
					cellDiffractionThumbnail2.setHorizontalAlignment(Element.ALIGN_CENTER);
					tableImage.addCell(cellDiffractionThumbnail2);
				} catch (IOException e) {
					tableImage.addCell(new Paragraph(imgDiff2 + " not found", FONT_DOC));
				}
			}
			//
			document.add(tableImage);
			document.add(new Paragraph(" ", VERY_SMALL_FONT));
		}
	}

	/**
	 * set the strategy table and wedge & subwedge strategy tables
	 * 
	 * @param document
	 * @param dcInfo
	 * @throws Exception
	 */
	private void setStrategyTable(Document document, DataCollectionInformation dcInfo) throws Exception {
		// ------------------------------------------- Strategy Wedge
		// ------------------------------------------------
		List<StrategyWedgeInformation> strategyWedgeInformationList = dcInfo.getListStrategyWedgeInformation();
		for (Iterator<StrategyWedgeInformation> i = strategyWedgeInformationList.iterator(); i.hasNext();) {
			document.add(new Paragraph(" ", VERY_SMALL_FONT));
			StrategyWedgeInformation swi = i.next();
			Table strategyWedgeTable = new Table(10);
			int headerStrategyWedge[] = { 8, 10, 10, 10, 10, 10, 10, 10, 10, 20 }; // percentage
			strategyWedgeTable.setWidths(headerStrategyWedge);
			strategyWedgeTable.setWidth(100);
			strategyWedgeTable.setCellsFitPage(true);
			strategyWedgeTable.setTableFitsPage(true);
			strategyWedgeTable.setPadding(0);
			strategyWedgeTable.getDefaultCell().setBorderWidth(0);
			strategyWedgeTable.setBorderWidth(0);
			strategyWedgeTable.getDefaultCell().setBackgroundColor(WEDGE_COLOR);

			strategyWedgeTable.addCell(new Paragraph("Wedge number", FONT_DOC_BOLD));
			strategyWedgeTable.addCell(new Paragraph("Resolution\n(" + Constants.ANGSTROM + ")", FONT_DOC_BOLD));
			strategyWedgeTable.addCell(new Paragraph("Completeness", FONT_DOC_BOLD));
			strategyWedgeTable.addCell(new Paragraph("Multiplicity", FONT_DOC_BOLD));
			strategyWedgeTable.addCell(new Paragraph("Total dose", FONT_DOC_BOLD));
			strategyWedgeTable.addCell(new Paragraph("Number of Images", FONT_DOC_BOLD));
			strategyWedgeTable.addCell(new Paragraph("Phi\n(" + Constants.DEGREE + ")", FONT_DOC_BOLD));
			strategyWedgeTable.addCell(new Paragraph("Kappa\n(" + Constants.DEGREE + ")", FONT_DOC_BOLD));
			strategyWedgeTable.addCell(new Paragraph("Wavelength", FONT_DOC_BOLD));
			strategyWedgeTable.addCell(new Paragraph("Comments", FONT_DOC_BOLD));

			strategyWedgeTable.getDefaultCell().setBackgroundColor(WHITE_COLOR);
			// rows
			strategyWedgeTable.addCell(new Paragraph(swi.getWedgeNumber(), FONT_DOC));
			strategyWedgeTable.addCell(new Paragraph(swi.getResolution(), FONT_DOC));
			strategyWedgeTable.addCell(new Paragraph(swi.getCompleteness(), FONT_DOC));
			strategyWedgeTable.addCell(new Paragraph(swi.getMultiplicity(), FONT_DOC));
			strategyWedgeTable.addCell(new Paragraph(swi.getDoseTotal(), FONT_DOC));
			strategyWedgeTable.addCell(new Paragraph(swi.getNumberOfImages(), FONT_DOC));
			strategyWedgeTable.addCell(new Paragraph(swi.getPhi(), FONT_DOC));
			strategyWedgeTable.addCell(new Paragraph(swi.getKappa(), FONT_DOC));
			strategyWedgeTable.addCell(new Paragraph(swi.getWavelength(), FONT_DOC));
			strategyWedgeTable.addCell(new Paragraph(swi.getComments(), FONT_DOC));

			document.add(strategyWedgeTable);

			// --- strategy sub wedge
			if (swi.getListStrategySubWedgeInformation().size() > 0) {
				document.add(new Paragraph(" ", VERY_SMALL_FONT));

				Table strategySubWedgeTable = new Table(12);
				int headerStrategySubWedge[] = { 8, 15, 10, 10, 10, 10, 10, 10, 10, 8, 8, 20 }; // percentage
				strategySubWedgeTable.setWidths(headerStrategySubWedge);
				strategySubWedgeTable.setWidth(80);
				strategySubWedgeTable.setCellsFitPage(true);
				strategySubWedgeTable.setTableFitsPage(true);
				strategySubWedgeTable.setPadding(0);
				strategySubWedgeTable.getDefaultCell().setBorderWidth(0);
				strategySubWedgeTable.setBorderWidth(0);
				strategySubWedgeTable.getDefaultCell().setBackgroundColor(SUBWEDGE_COLOR);

				strategySubWedgeTable.addCell(new Paragraph("Sub Wedge number", FONT_DOC_BOLD));
				strategySubWedgeTable.addCell(new Paragraph("Rotation axis", FONT_DOC_BOLD));
				strategySubWedgeTable.addCell(new Paragraph("Axis start", FONT_DOC_BOLD));
				strategySubWedgeTable.addCell(new Paragraph("Axis end", FONT_DOC_BOLD));
				strategySubWedgeTable.addCell(new Paragraph("Exposure time", FONT_DOC_BOLD));
				strategySubWedgeTable.addCell(new Paragraph("Transmission", FONT_DOC_BOLD));
				strategySubWedgeTable.addCell(new Paragraph("Oscillation Range", FONT_DOC_BOLD));
				strategySubWedgeTable.addCell(new Paragraph("Completeness", FONT_DOC_BOLD));
				strategySubWedgeTable.addCell(new Paragraph("Multiplicity", FONT_DOC_BOLD));
				strategySubWedgeTable.addCell(new Paragraph("Total dose", FONT_DOC_BOLD));
				strategySubWedgeTable.addCell(new Paragraph("Number of images", FONT_DOC_BOLD));
				strategySubWedgeTable.addCell(new Paragraph("Comments", FONT_DOC_BOLD));

				strategySubWedgeTable.getDefaultCell().setBackgroundColor(WHITE_COLOR);
				// rows
				for (Iterator<StrategySubWedgeInformation> s = swi.getListStrategySubWedgeInformation().iterator(); s
						.hasNext();) {
					StrategySubWedgeInformation sswi = s.next();
					strategySubWedgeTable.addCell(new Paragraph(sswi.getSubWedgeNumber(), FONT_DOC));
					strategySubWedgeTable.addCell(new Paragraph(sswi.getRotationAxis(), FONT_DOC));
					strategySubWedgeTable.addCell(new Paragraph(sswi.getAxisStart(), FONT_DOC));
					strategySubWedgeTable.addCell(new Paragraph(sswi.getAxisEnd(), FONT_DOC));
					strategySubWedgeTable.addCell(new Paragraph(sswi.getExposureTime(), FONT_DOC));
					strategySubWedgeTable.addCell(new Paragraph(sswi.getTransmission(), FONT_DOC));
					strategySubWedgeTable.addCell(new Paragraph(sswi.getOscillationRange(), FONT_DOC));
					strategySubWedgeTable.addCell(new Paragraph(sswi.getCompleteness(), FONT_DOC));
					strategySubWedgeTable.addCell(new Paragraph(sswi.getMultiplicity(), FONT_DOC));
					strategySubWedgeTable.addCell(new Paragraph(sswi.getDoseTotal(), FONT_DOC));
					strategySubWedgeTable.addCell(new Paragraph(sswi.getNumberOfImages(), FONT_DOC));
					strategySubWedgeTable.addCell(new Paragraph(sswi.getComments(), FONT_DOC));
				}
				document.add(strategySubWedgeTable);
			}
		}
		document.add(new Paragraph(" ", VERY_SMALL_FONT));
	}

	/**
	 * set the strategy table and wedge & subwedge strategy tables copy from
	 * setStrategyTable, waiting for feedback that we can remove the old reports
	 * and the first method
	 * 
	 * @param document
	 * @param dcInfo
	 * @throws Exception
	 */
	private void setStrategyTable2(Document document, DataCollectionInformation dcInfo) throws Exception {
		// ------------------------------------------- Strategy Wedge
		// ------------------------------------------------
		List<StrategyWedgeInformation> strategyWedgeInformationList = dcInfo.getListStrategyWedgeInformation();
		for (Iterator<StrategyWedgeInformation> i = strategyWedgeInformationList.iterator(); i.hasNext();) {
			document.add(new Paragraph(" ", VERY_SMALL_FONT));
			StrategyWedgeInformation swi = i.next();
			Table strategyWedgeTable = new Table(10);
			int headerStrategyWedge[] = { 8, 10, 10, 10, 10, 10, 10, 10, 10, 20 }; // percentage
			strategyWedgeTable.setWidths(headerStrategyWedge);
			strategyWedgeTable.setWidth(100);
			strategyWedgeTable.setCellsFitPage(true);
			strategyWedgeTable.setTableFitsPage(true);
			strategyWedgeTable.setPadding(1);
			strategyWedgeTable.getDefaultCell().setBorderWidth(1);
			strategyWedgeTable.setBorderWidth(1);
			strategyWedgeTable.getDefaultCell().setBackgroundColor(LIGHT_GREY_COLOR);
			strategyWedgeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

			strategyWedgeTable.addCell(new Paragraph("Wedge number", FONT_DOC_BOLD));
			strategyWedgeTable.addCell(new Paragraph("Resolution (" + Constants.ANGSTROM + ")", FONT_DOC_BOLD));
			strategyWedgeTable.addCell(new Paragraph("Completeness", FONT_DOC_BOLD));
			strategyWedgeTable.addCell(new Paragraph("Multiplicity", FONT_DOC_BOLD));
			strategyWedgeTable.addCell(new Paragraph("Total dose", FONT_DOC_BOLD));
			strategyWedgeTable.addCell(new Paragraph("Number of Images", FONT_DOC_BOLD));
			strategyWedgeTable.addCell(new Paragraph("Phi (" + Constants.DEGREE + ")", FONT_DOC_BOLD));
			strategyWedgeTable.addCell(new Paragraph("Kappa (" + Constants.DEGREE + ")", FONT_DOC_BOLD));
			strategyWedgeTable.addCell(new Paragraph("Wavelength", FONT_DOC_BOLD));
			strategyWedgeTable.addCell(new Paragraph("Comments", FONT_DOC_BOLD));

			strategyWedgeTable.getDefaultCell().setBackgroundColor(WHITE_COLOR);
			// rows
			strategyWedgeTable.addCell(new Paragraph(swi.getWedgeNumber(), FONT_DOC));
			strategyWedgeTable.addCell(new Paragraph(swi.getResolution(), FONT_DOC));
			strategyWedgeTable.addCell(new Paragraph(swi.getCompleteness(), FONT_DOC));
			strategyWedgeTable.addCell(new Paragraph(swi.getMultiplicity(), FONT_DOC));
			strategyWedgeTable.addCell(new Paragraph(swi.getDoseTotal(), FONT_DOC));
			strategyWedgeTable.addCell(new Paragraph(swi.getNumberOfImages(), FONT_DOC));
			strategyWedgeTable.addCell(new Paragraph(swi.getPhi(), FONT_DOC));
			strategyWedgeTable.addCell(new Paragraph(swi.getKappa(), FONT_DOC));
			strategyWedgeTable.addCell(new Paragraph(swi.getWavelength(), FONT_DOC));
			strategyWedgeTable.addCell(new Paragraph(swi.getComments(), FONT_DOC));

			document.add(strategyWedgeTable);

			// --- strategy sub wedge
			if (swi.getListStrategySubWedgeInformation().size() > 0) {
				document.add(new Paragraph(" ", VERY_SMALL_FONT));

				Table strategySubWedgeTable = new Table(12);
				int headerStrategySubWedge[] = { 8, 15, 10, 10, 10, 10, 10, 10, 10, 8, 8, 20 }; // percentage
				strategySubWedgeTable.setWidths(headerStrategySubWedge);
				strategySubWedgeTable.setWidth(97);
				strategySubWedgeTable.setCellsFitPage(true);
				strategySubWedgeTable.setTableFitsPage(true);
				strategySubWedgeTable.setPadding(1);
				strategySubWedgeTable.getDefaultCell().setBorderWidth(1);
				strategySubWedgeTable.setBorderWidth(1);
				strategySubWedgeTable.getDefaultCell().setBackgroundColor(LIGHT_GREY_COLOR);
				strategySubWedgeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

				strategySubWedgeTable.addCell(new Paragraph("Sub Wedge number", FONT_DOC_BOLD));
				strategySubWedgeTable.addCell(new Paragraph("Rotation axis", FONT_DOC_BOLD));
				strategySubWedgeTable.addCell(new Paragraph("Axis start", FONT_DOC_BOLD));
				strategySubWedgeTable.addCell(new Paragraph("Axis end", FONT_DOC_BOLD));
				strategySubWedgeTable.addCell(new Paragraph("Exposure time", FONT_DOC_BOLD));
				strategySubWedgeTable.addCell(new Paragraph("Transmission", FONT_DOC_BOLD));
				strategySubWedgeTable.addCell(new Paragraph("Oscillation Range", FONT_DOC_BOLD));
				strategySubWedgeTable.addCell(new Paragraph("Completeness", FONT_DOC_BOLD));
				strategySubWedgeTable.addCell(new Paragraph("Multiplicity", FONT_DOC_BOLD));
				strategySubWedgeTable.addCell(new Paragraph("Total dose", FONT_DOC_BOLD));
				strategySubWedgeTable.addCell(new Paragraph("Number of images", FONT_DOC_BOLD));
				strategySubWedgeTable.addCell(new Paragraph("Comments", FONT_DOC_BOLD));

				strategySubWedgeTable.getDefaultCell().setBackgroundColor(WHITE_COLOR);
				// rows
				for (Iterator<StrategySubWedgeInformation> s = swi.getListStrategySubWedgeInformation().iterator(); s
						.hasNext();) {
					StrategySubWedgeInformation sswi = s.next();
					strategySubWedgeTable.addCell(new Paragraph(sswi.getSubWedgeNumber(), FONT_DOC));
					strategySubWedgeTable.addCell(new Paragraph(sswi.getRotationAxis(), FONT_DOC));
					strategySubWedgeTable.addCell(new Paragraph(sswi.getAxisStart(), FONT_DOC));
					strategySubWedgeTable.addCell(new Paragraph(sswi.getAxisEnd(), FONT_DOC));
					strategySubWedgeTable.addCell(new Paragraph(sswi.getExposureTime(), FONT_DOC));
					strategySubWedgeTable.addCell(new Paragraph(sswi.getTransmission(), FONT_DOC));
					strategySubWedgeTable.addCell(new Paragraph(sswi.getOscillationRange(), FONT_DOC));
					strategySubWedgeTable.addCell(new Paragraph(sswi.getCompleteness(), FONT_DOC));
					strategySubWedgeTable.addCell(new Paragraph(sswi.getMultiplicity(), FONT_DOC));
					strategySubWedgeTable.addCell(new Paragraph(sswi.getDoseTotal(), FONT_DOC));
					strategySubWedgeTable.addCell(new Paragraph(sswi.getNumberOfImages(), FONT_DOC));
					strategySubWedgeTable.addCell(new Paragraph(sswi.getComments(), FONT_DOC));
				}
				document.add(strategySubWedgeTable);
			}
		}
		document.add(new Paragraph(" ", VERY_SMALL_FONT));
	}

	private void setRankingTable(Document document, DataCollectionInformation dcInfo) throws Exception {
		SampleRankingVO rankingVO = dcInfo.getSampleRankingVO();
		if (rankingVO == null) {
			return;
		}
		DecimalFormat decimalFormat1 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		decimalFormat1.applyPattern("0");
		DecimalFormat decimalFormat2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		decimalFormat2.applyPattern("0.00");
		Table aTable = new Table(6);
		// aTable.setWidth(100); // percentage
		aTable.setPadding(3);
		aTable.setCellsFitPage(true);
		aTable.getDefaultCell().setBorderWidth(1);
		aTable.setBorderWidth(1);
		aTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		// header
		aTable.getDefaultCell().setGrayFill(GREY_FILL_HEADER);
		aTable.addCell(new Paragraph("Ranking Resolution\n(" + Constants.ANGSTROM + ")", FONT_DOC_BOLD));
		aTable.addCell(new Paragraph("Exposure Time\n(s)", FONT_DOC_BOLD));
		aTable.addCell(new Paragraph("Mosaicity\n(" + Constants.DEGREE + ")", FONT_DOC_BOLD));
		aTable.addCell(new Paragraph("Number of spots", FONT_DOC_BOLD));
		aTable.addCell(new Paragraph("Number of images", FONT_DOC_BOLD));
		aTable.addCell(new Paragraph("Total", FONT_DOC_BOLD));
		aTable.getDefaultCell().setBackgroundColor(WHITE_COLOR);
		// row
		String rankRes = "#" + decimalFormat1.format(rankingVO.getTheoreticalResolutionRank());
		if (rankingVO.getTheoreticalResolutionValue() != null
				&& !Double.isNaN(rankingVO.getTheoreticalResolutionValue())) {
			rankRes += "   " + decimalFormat2.format(rankingVO.getTheoreticalResolutionValue());
		}
		String rankExp = "#" + decimalFormat1.format(rankingVO.getTotalExposureTimeRank());
		if (rankingVO.getTotalExposureTimeValue() != null && !Double.isNaN(rankingVO.getTotalExposureTimeValue())) {
			rankExp += "   " + decimalFormat2.format(rankingVO.getTotalExposureTimeValue());
		}
		String rankMos = "#" + decimalFormat1.format(rankingVO.getMosaicityRank());
		if (rankingVO.getMosaicityValue() != null && !Double.isNaN(rankingVO.getMosaicityValue())) {
			rankMos += "   " + decimalFormat2.format(rankingVO.getMosaicityValue());
		}
		String rankSpot = "#" + decimalFormat1.format(rankingVO.getNumberOfSpotsIndexedRank());
		if (rankingVO.getNumberOfSpotsIndexedValue() != null && !Double.isNaN(rankingVO.getNumberOfSpotsIndexedValue())) {
			rankSpot += "   " + decimalFormat1.format(rankingVO.getNumberOfSpotsIndexedValue());
		}
		String rankImages = "#" + decimalFormat1.format(rankingVO.getNumberOfImagesRank());
		if (rankingVO.getNumberOfImagesValue() != null && !Double.isNaN(rankingVO.getNumberOfImagesValue())) {
			rankImages += "   " + decimalFormat1.format(rankingVO.getNumberOfImagesValue());
		}
		String rankTotal = "#" + decimalFormat1.format(rankingVO.getTotalRank());
		if (rankingVO.getTotalScore() != null && !Double.isNaN(rankingVO.getTotalScore())) {
			rankTotal += "   " + decimalFormat1.format(rankingVO.getTotalScore()) + " %";
		}
		// ... rank res
		if (rankingVO.getTheoreticalResolutionRank() == 1) {
			aTable.getDefaultCell().setBackgroundColor(GREEN_COLOR_RANK);
		} else if (rankingVO.getTheoreticalResolutionValue() == null
				|| Double.isNaN(rankingVO.getTheoreticalResolutionValue())) {
			aTable.getDefaultCell().setBackgroundColor(NULL_COLOR_RANK);
		} else {
			aTable.getDefaultCell().setBackgroundColor(WHITE_COLOR);
		}
		aTable.addCell(new Paragraph(rankRes, FONT_DOC));
		// ... exp. time
		if (rankingVO.getTotalExposureTimeRank() == 1) {
			aTable.getDefaultCell().setBackgroundColor(GREEN_COLOR_RANK);
		} else if (rankingVO.getTotalExposureTimeValue() == null || Double.isNaN(rankingVO.getTotalExposureTimeValue())) {
			aTable.getDefaultCell().setBackgroundColor(NULL_COLOR_RANK);
		} else {
			aTable.getDefaultCell().setBackgroundColor(WHITE_COLOR);
		}
		aTable.addCell(new Paragraph(rankExp, FONT_DOC));
		// ... mosaicity
		if (rankingVO.getMosaicityRank() == 1) {
			aTable.getDefaultCell().setBackgroundColor(GREEN_COLOR_RANK);
		} else if (rankingVO.getMosaicityValue() == null || Double.isNaN(rankingVO.getMosaicityValue())) {
			aTable.getDefaultCell().setBackgroundColor(NULL_COLOR_RANK);
		} else {
			aTable.getDefaultCell().setBackgroundColor(WHITE_COLOR);
		}
		aTable.addCell(new Paragraph(rankMos, FONT_DOC));
		// ... number of spots
		if (rankingVO.getNumberOfSpotsIndexedRank() == 1) {
			aTable.getDefaultCell().setBackgroundColor(GREEN_COLOR_RANK);
		} else if (rankingVO.getNumberOfSpotsIndexedValue() == null
				|| Double.isNaN(rankingVO.getNumberOfSpotsIndexedValue())) {
			aTable.getDefaultCell().setBackgroundColor(NULL_COLOR_RANK);
		} else {
			aTable.getDefaultCell().setBackgroundColor(WHITE_COLOR);
		}
		aTable.addCell(new Paragraph(rankSpot, FONT_DOC));
		// ... number of images
		if (rankingVO.getNumberOfImagesRank() == 1) {
			aTable.getDefaultCell().setBackgroundColor(GREEN_COLOR_RANK);
		} else if (rankingVO.getNumberOfImagesValue() == null || Double.isNaN(rankingVO.getNumberOfImagesValue())) {
			aTable.getDefaultCell().setBackgroundColor(NULL_COLOR_RANK);
		} else {
			aTable.getDefaultCell().setBackgroundColor(WHITE_COLOR);
		}
		aTable.addCell(new Paragraph(rankImages, FONT_DOC));
		// ... total
		if (rankingVO.getTotalRank() == 1) {
			aTable.getDefaultCell().setBackgroundColor(GREEN_COLOR_RANK);
		} else if (rankingVO.getTotalScore() == null || Double.isNaN(rankingVO.getTotalScore())) {
			aTable.getDefaultCell().setBackgroundColor(NULL_COLOR_RANK);
		} else {
			aTable.getDefaultCell().setBackgroundColor(WHITE_COLOR);
		}
		aTable.addCell(new Paragraph(rankTotal, FONT_DOC));
		//
		document.add(aTable);
		document.add(new Paragraph(" ", VERY_SMALL_FONT));
	}

	private SampleRankingVO getSampleRankingVO(Integer dataCollectionId) {
		if (sampleRankingVOList == null)
			return null;
		for (Iterator<SampleRankingVO> s = sampleRankingVOList.iterator(); s.hasNext();) {
			SampleRankingVO sampleRankingVO = s.next();
			if (sampleRankingVO.getDataCollectionId().equals(dataCollectionId)) {
				return sampleRankingVO;
			}
		}
		return null;
	}

	private AutoProcRankingVO getAutoProcRankingVO(Integer dataCollectionId) {
		if (autoProcRankingVOList == null)
			return null;
		for (Iterator<AutoProcRankingVO> s = autoProcRankingVOList.iterator(); s.hasNext();) {
			AutoProcRankingVO autoProcRankingVO = s.next();
			if (autoProcRankingVO.getDataCollectionId().equals(dataCollectionId)) {
				return autoProcRankingVO;
			}
		}
		return null;
	}

	private void setAutoProcRankingTable(Document document, DataCollectionInformation dcInfo) throws Exception {
		AutoProcRankingVO rankingVO = dcInfo.getAutoProcRankingVO();
		if (rankingVO == null) {
			return;
		}
		DecimalFormat decimalFormat1 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		decimalFormat1.applyPattern("0");
		DecimalFormat decimalFormat2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		decimalFormat2.applyPattern("0.00");
		Table aTable = new Table(4);
		// aTable.setWidth(100); // percentage
		aTable.setPadding(3);
		aTable.setCellsFitPage(true);
		aTable.getDefaultCell().setBorderWidth(1);
		aTable.setBorderWidth(1);
		aTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		// header
		aTable.getDefaultCell().setGrayFill(GREY_FILL_HEADER);
		aTable.addCell(new Paragraph("R-factor\n(" + Constants.ANGSTROM + ")", FONT_DOC_BOLD));
		aTable.addCell(new Paragraph("Highest resolution\n(" + Constants.ANGSTROM + ")", FONT_DOC_BOLD));
		aTable.addCell(new Paragraph("Completeness\n(%)", FONT_DOC_BOLD));
		aTable.addCell(new Paragraph("Total", FONT_DOC_BOLD));
		aTable.getDefaultCell().setBackgroundColor(WHITE_COLOR);
		// row
		String rankRfactor = "#" + decimalFormat1.format(rankingVO.getOverallRFactorRank());
		if (rankingVO.getOverallRFactorValue() != null && !Double.isNaN(rankingVO.getOverallRFactorValue())) {
			rankRfactor += "   " + decimalFormat2.format(rankingVO.getOverallRFactorValue());
		}
		String rankHighestRes = "#" + decimalFormat1.format(rankingVO.getHighestResolutionRank());
		if (rankingVO.getHighestResolutionValue() != null && !Double.isNaN(rankingVO.getHighestResolutionValue())) {
			rankHighestRes += "   " + decimalFormat2.format(rankingVO.getHighestResolutionValue());
		}
		String rankCom = "#" + decimalFormat1.format(rankingVO.getCompletenessRank());
		if (rankingVO.getCompletenessValue() != null && !Double.isNaN(rankingVO.getCompletenessValue())) {
			rankCom += "   " + decimalFormat2.format(rankingVO.getCompletenessValue());
		}
		String rankTotal = "#" + decimalFormat1.format(rankingVO.getTotalRank());
		if (rankingVO.getTotalScore() != null && !Double.isNaN(rankingVO.getTotalScore())) {
			rankTotal += "   " + decimalFormat1.format(rankingVO.getTotalScore()) + " %";
		}
		// ... R-factor
		if (rankingVO.getOverallRFactorRank() == 1) {
			aTable.getDefaultCell().setBackgroundColor(GREEN_COLOR_RANK);
		} else if (rankingVO.getOverallRFactorValue() == null || Double.isNaN(rankingVO.getOverallRFactorValue())) {
			aTable.getDefaultCell().setBackgroundColor(NULL_COLOR_RANK);
		} else {
			aTable.getDefaultCell().setBackgroundColor(WHITE_COLOR);
		}
		aTable.addCell(new Paragraph(rankRfactor, FONT_DOC));
		// ... highest resolution
		if (rankingVO.getHighestResolutionRank() == 1) {
			aTable.getDefaultCell().setBackgroundColor(GREEN_COLOR_RANK);
		} else if (rankingVO.getHighestResolutionValue() == null || Double.isNaN(rankingVO.getHighestResolutionValue())) {
			aTable.getDefaultCell().setBackgroundColor(NULL_COLOR_RANK);
		} else {
			aTable.getDefaultCell().setBackgroundColor(WHITE_COLOR);
		}
		aTable.addCell(new Paragraph(rankHighestRes, FONT_DOC));
		// ... completeness
		if (rankingVO.getCompletenessRank() == 1) {
			aTable.getDefaultCell().setBackgroundColor(GREEN_COLOR_RANK);
		} else if (rankingVO.getCompletenessValue() == null || Double.isNaN(rankingVO.getCompletenessValue())) {
			aTable.getDefaultCell().setBackgroundColor(NULL_COLOR_RANK);
		} else {
			aTable.getDefaultCell().setBackgroundColor(WHITE_COLOR);
		}
		aTable.addCell(new Paragraph(rankCom, FONT_DOC));
		// ... total
		if (rankingVO.getTotalRank() == 1) {
			aTable.getDefaultCell().setBackgroundColor(GREEN_COLOR_RANK);
		} else if (rankingVO.getTotalScore() == null || Double.isNaN(rankingVO.getTotalScore())) {
			aTable.getDefaultCell().setBackgroundColor(NULL_COLOR_RANK);
		} else {
			aTable.getDefaultCell().setBackgroundColor(WHITE_COLOR);
		}
		aTable.addCell(new Paragraph(rankTotal, FONT_DOC));
		//
		document.add(aTable);
		document.add(new Paragraph(" ", VERY_SMALL_FONT));
	}

	/**
	 * export the MXPressO /MXpressE report
	 * 
	 * @param rtfFormat
	 * @param mRequest
	 * @return
	 * @throws Exception
	 */
	public ByteArrayOutputStream exportMXPressOWorkflowReport(boolean rtfFormat, HttpServletRequest mRequest)
			throws Exception {
		// create simple doc and write to a ByteArrayOutputStream
		Document document = new Document(PageSize.A4.rotate(), 20, 20, 20, 20);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (!rtfFormat) {
			isRTF = new Boolean(false);
			PdfWriter.getInstance(document, baos);
		} else {
			isRTF = new Boolean(true);
			RtfWriter2.getInstance(document, baos);
		}

		// =============================
		// Header + footer
		// =============================

		setHeaderMXPressO(document);
		setFooter(document);
		document.open();

		// ===============================
		// Body
		// ===============================
		// For each MXPressO workflow, display snapshot, results and
		// autoprocessingResults
		setMXPressO(document, mRequest);

		// ======================
		// End of file
		// ======================
		document.close();
		return baos;

	}

	/**
	 * iterates over the collects to set the MXPressO table
	 * idem for MXPressI
	 * 
	 * @param document
	 * @param mRequest
	 * @throws Exception
	 */
	private void setMXPressO(Document document, HttpServletRequest mRequest) throws Exception {
		if (dataCollectionGroupList == null || dataCollectionGroupList.isEmpty()) {
			document.add(new Paragraph("There is no data collection group in this report", FONT_DOC));
		} else {
			int countTables = 0;
			int id = 0;
			for (Iterator<DataCollectionGroup3VO> iterator = dataCollectionGroupList.iterator(); iterator.hasNext();) {
				// System.out.println("id: "+id+", countTables= "+countTables);
				DataCollectionGroup3VO dataCollectionGroupVO = (DataCollectionGroup3VO) iterator.next();
				if (dataCollectionGroupVO.getWorkflowVO() != null && dataCollectionGroupVO.getWorkflowVO().isMXPressEOIA()) {
					List<DataCollection3VO> sortedListCollect = ViewSessionSummaryAction
							.getSortedDataCollectionList(dataCollectionGroupVO.getDataCollectionsList());
					DataCollection3VO lastCollectOSC = null;
					// for MXPressE, we need the collect just before the last,
					// to have EDNA results
					DataCollection3VO nextToLastCollectChar = null;

					if (sortedListCollect != null && sortedListCollect.size() > 0) {
						lastCollectOSC = sortedListCollect.get(0);
					}
					if (sortedListCollect != null
							&& sortedListCollect.size() > 1
							&& dataCollectionGroupVO.getWorkflowVO().getWorkflowType().equals(Constants.WORKFLOW_MXPRESSE)) {
						nextToLastCollectChar = sortedListCollect.get(1);
					}

					// is mxpresse with edna res
					boolean withEDNARes = false;
					if (nextToLastCollectChar != null) {
						DataCollectionExporter dcExporter = new DataCollectionExporter(df2, df3, proposalCode,
								proposalNumber, mRequest);
						DataCollectionInformation dcInfo = dcExporter.getDataCollectionInformation(
								nextToLastCollectChar, getSampleRankingVO(nextToLastCollectChar.getDataCollectionId()),
								getAutoProcRankingVO(nextToLastCollectChar.getDataCollectionId()));
						if (dcInfo.getSpacegroup() != "") {
							withEDNARes = true;
						}
					}
					if (id != 0 && withEDNARes) {
						document.newPage();
					} else {
						if (countTables > 1) {
							// System.out.println("page break");
							document.newPage();
							countTables = 0;
						}
					}
					setMXPressOTable(document, dataCollectionGroupVO, mRequest, lastCollectOSC, nextToLastCollectChar);
					countTables++;
					if (withEDNARes) {
						countTables++;
					}
					id++;
				}

				//
			}
		}
	}

	/**
	 * set the table for a given MXPressO
	 * 
	 * @param document
	 * @param dataCollectionGroupVO
	 * @param mRequest
	 * @param lastCollectOSC
	 * @param nextToLastCollectChar
	 * @throws Exception
	 */
	private void setMXPressOTable(Document document, DataCollectionGroup3VO dataCollectionGroupVO,
			HttpServletRequest mRequest, DataCollection3VO lastCollectOSC, DataCollection3VO nextToLastCollectChar)
			throws Exception {
		if (dataCollectionGroupVO != null) {

			DataCollectionExporter dcExporter = new DataCollectionExporter(df2, df3, proposalCode, proposalNumber,
					mRequest);
			if (lastCollectOSC != null) {

				DataCollectionInformation dcInfo = dcExporter.getDataCollectionInformation(lastCollectOSC,
						getSampleRankingVO(lastCollectOSC.getDataCollectionId()),
						getAutoProcRankingVO(lastCollectOSC.getDataCollectionId()));

				int noCol = 3;
				Table tableWF = new Table(noCol);
				int[] headersWidthWF = new int[noCol];
				int l = 0;

				headersWidthWF[l++] = 27; // snapshot
				headersWidthWF[l++] = 24; // map 1
				headersWidthWF[l++] = 51; // map2
				// tableWF.setWidths(headersWidthWF);

				tableWF.setWidth(100); // percentage
				tableWF.setPadding(1);
				tableWF.setCellsFitPage(true);
				tableWF.setTableFitsPage(true);
				tableWF.getDefaultCell().setBorderWidth(1);
				tableWF.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				// first row: protein acronym and sample name (or image prefix)
				// and startTime
				String collectName = lastCollectOSC.getImagePrefix();
				if (dataCollectionGroupVO.getBlSampleVO() != null
						&& dataCollectionGroupVO.getBlSampleVO().getCrystalVO() != null) {
					collectName = dataCollectionGroupVO.getBlSampleVO().getCrystalVO().getProteinVO().getAcronym()
							+ "-" + dataCollectionGroupVO.getBlSampleVO().getName();
				}
				tableWF.addCell(new Paragraph(collectName, FONT_DOC));

				String collectTime = lastCollectOSC.getStartTime().toString();
				Cell c = new Cell(new Paragraph(collectTime, FONT_DOC));
				c.setColspan(2);
				tableWF.addCell(c);

				// second row: first snapshot + cartography graphes (2 mesh)
				// first snapshot
				String imgCrystal = dcInfo.getPathJpgCrystal3();
				Cell cellCrystalImage = getCellImage(imgCrystal);
				tableWF.addCell(cellCrystalImage);
				// workflowMesh cartography
				Workflow3VO mxExpressOWF = dataCollectionGroupVO.getWorkflowVO();
				List<WorkflowMesh3VO> listWFMesh = new ArrayList<WorkflowMesh3VO>();
				Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
				WorkflowMesh3Service workflowMeshService = (WorkflowMesh3Service) ejb3ServiceLocator
						.getLocalService(WorkflowMesh3Service.class);
				listWFMesh = workflowMeshService.findByWorkflowId(mxExpressOWF.getWorkflowId());

				if (listWFMesh != null && listWFMesh.size() > 0) {
					Cell cellImg1 = getMeshMapCell(listWFMesh.get(0));
					if (cellImg1 == null) {
						tableWF.addCell(new Paragraph("Image not found", FONT_DOC));
					} else {
						tableWF.addCell(cellImg1);
					}
					if (listWFMesh.size() > 1) {
						Cell cellImg2 = getMeshMapCell(listWFMesh.get(1));
						if (cellImg2 == null) {
							tableWF.addCell(new Paragraph("Image not found", FONT_DOC));
						} else {
							tableWF.addCell(cellImg2);
						}
					} else
						tableWF.addCell(new Paragraph("No Workflow found", FONT_DOC));
				} else {
					tableWF.addCell(new Paragraph("No Workflow found", FONT_DOC));
					tableWF.addCell(new Paragraph("No Workflow found", FONT_DOC));
				}

				//
				// third row:
				// autoprocessing results
				Cell resultCell = getAutoProcResultStatus(dcInfo);
				resultCell.setColspan(3);
				resultCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				tableWF.addCell(resultCell);

				Integer idDc = -1;
				for (int k = 0; k < dataCollectionList.size(); k++) {
					DataCollection3VO dcVo = dataCollectionList.get(k);
					if (dcVo.getDataCollectionId().equals(lastCollectOSC.getDataCollectionId())) {
						idDc = k;
						break;
					}
				}

				if (idDc != -1) {
					AutoProc3VO[] autoProcs = wrapper.getAutoProcs();
					AutoProcScalingStatistics3VO[] autoProcsOverall = wrapper.getScalingStatsOverall();
					AutoProcScalingStatistics3VO[] autoProcsInner = wrapper.getScalingStatsInner();
					AutoProcScalingStatistics3VO[] autoProcsOuter = wrapper.getScalingStatsOuter();

					AutoProc3VO autoProcValue = autoProcs[idDc];
					AutoProcScalingStatistics3VO autoProcOverall = autoProcsOverall[idDc];
					AutoProcScalingStatistics3VO autoProcInner = autoProcsInner[idDc];
					AutoProcScalingStatistics3VO autoProcOuter = autoProcsOuter[idDc];
					if (autoProcValue == null) {
						Cell cNo = new Cell(new Paragraph("No autoprocessing results found", FONT_DOC));
						cNo.setColspan(3);
						cNo.setHorizontalAlignment(Element.ALIGN_LEFT);
						tableWF.addCell(cNo);
						document.add(tableWF);
					} else {
						int noColSPG = 5;
						Table tableSPG = new Table(noColSPG);
						int[] headersWidth = new int[noColSPG];
						int i = 0;

						headersWidth[i++] = 7; // space group
						headersWidth[i++] = 10; // completeness
						headersWidth[i++] = 9; // resolution
						headersWidth[i++] = 9; // rsymm
						headersWidth[i++] = 12; // unit cell
						tableSPG.setWidths(headersWidth);

						tableSPG.setWidth(100); // percentage
						tableSPG.setPadding(3);
						tableSPG.setCellsFitPage(true);
						tableSPG.setTableFitsPage(true);
						tableSPG.getDefaultCell().setBorderWidth(1);
						tableSPG.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

						tableSPG.getDefaultCell().setGrayFill(GREY_FILL_HEADER);
						tableSPG.addCell(new Paragraph("Space Group", FONT_DOC_BOLD));
						tableSPG.addCell(new Paragraph("Completeness (Inner, Outer, Overall)", FONT_DOC_BOLD));
						tableSPG.addCell(new Paragraph("Resolution", FONT_DOC_BOLD));
						tableSPG.addCell(new Paragraph("Rsymm (Inner, Outer, Overall)", FONT_DOC_BOLD));
						tableSPG.addCell(new Paragraph("Unit Cell (a, b, c, alpha, beta, gamma)", FONT_DOC_BOLD));

						tableSPG.getDefaultCell().setGrayFill(GREY_FILL_DATA);

						// space group
						if (autoProcValue != null && autoProcValue.getSpaceGroup() != null) {
							Paragraph p = new Paragraph(autoProcValue.getSpaceGroup(), FONT_DOC);
							tableSPG.addCell(p);
						} else
							tableSPG.addCell("");

						// completeness, rsymm, processed resolution
						String completenessString = new String();
						String rSymmString = new String();
						String resolutionString = new String();

						if (autoProcOverall != null && autoProcInner != null && autoProcOuter != null) {
							completenessString += (autoProcInner.getCompleteness() == null ? "" : df2.format(autoProcInner.getCompleteness())) + "\n"
						+ (autoProcOuter.getCompleteness() == null ? "" : df2.format(autoProcOuter.getCompleteness())) + "\n"
						+ (autoProcOverall.getCompleteness() == null ? "" : df2.format(autoProcOverall.getCompleteness()));
							rSymmString += (autoProcInner.getRmerge() == null ? "" : df2.format(autoProcInner
									.getRmerge()))
									+ "\n"
									+ (autoProcOuter.getRmerge() == null ? "" : df2.format(autoProcOuter.getRmerge()))
									+ "\n"
									+ (autoProcOverall.getRmerge() == null ? "" : df2.format(autoProcOverall
											.getRmerge()));
							resolutionString += autoProcInner.getResolutionLimitLow() + " - "
									+ autoProcInner.getResolutionLimitHigh() + "\n"
									+ autoProcOuter.getResolutionLimitLow() + " - "
									+ autoProcOuter.getResolutionLimitHigh() + "\n"
									+ autoProcOverall.getResolutionLimitLow() + " - "
									+ autoProcOverall.getResolutionLimitHigh();
						}
						tableSPG.addCell(new Paragraph(completenessString, FONT_DOC));
						tableSPG.addCell(new Paragraph(resolutionString, FONT_DOC));
						tableSPG.addCell(new Paragraph(rSymmString, FONT_DOC));

						// unit cell
						if (autoProcValue != null && autoProcValue.getSpaceGroup() != null) {
							tableSPG.addCell(new Paragraph("" + autoProcValue.getRefinedCellA() + ", "
									+ autoProcValue.getRefinedCellB() + ", " + autoProcValue.getRefinedCellC() + "\n"
									+ autoProcValue.getRefinedCellAlpha() + ", " + autoProcValue.getRefinedCellBeta()
									+ ", " + autoProcValue.getRefinedCellGamma(), FONT_DOC));
						} else {
							tableSPG.addCell("");
						}

						document.add(tableWF);
						document.add(new Paragraph("Best autoprocessing result", FONT_DOC));
						document.add(tableSPG);
					}// end autoProc null
				}// end idDc -1

				//
				document.add(new Paragraph(" ", FONT_SPACE));

			}// end lastCollect null
			if (nextToLastCollectChar != null) {
				DataCollectionInformation dcInfo = dcExporter.getDataCollectionInformation(nextToLastCollectChar,
						getSampleRankingVO(nextToLastCollectChar.getDataCollectionId()),
						getAutoProcRankingVO(nextToLastCollectChar.getDataCollectionId()));

				if (dcInfo.getSpacegroup() != "") {
					document.add(new Paragraph("EDNA characterisation output and collect strategy", FONT_DOC));
				}
				setEDNATable2(document, dcInfo);
				setStrategyTable2(document, dcInfo);
			}
		} // end dataCollectionGroup null
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
		return new Cell(new Paragraph("no path to image", FONT_DOC));
	}
	
	/**
	 * returns a cell with a given image inside
	 * 
	 * @param image
	 * @return
	 * @throws Exception
	 */
	private Cell getCellIcon(String image) throws Exception {
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
		return new Cell(new Paragraph("no path to image", FONT_DOC));
	}

	/**
	 * return the cell with the graph for a session data object
	 * 
	 * @param sessionDataObject
	 * @return
	 * @throws Exception
	 */
	private Cell getCellGraph(SessionDataObjectInformation sessionDataObject) throws Exception {
		String autoProcGraph = "";
		if (sessionDataObject.getGraphPath() != null && !sessionDataObject.getGraphPath().isEmpty()) {
			autoProcGraph = sessionDataObject.getGraphPath();
		}
		return getCellImage(autoProcGraph);
	}

	private Chunk getChunkImage(String image) throws BadElementException, MalformedURLException, IOException {
		Image jpg = Image.getInstance(image);
		jpg.scaleAbsolute(jpg.getWidth() * 10 / jpg.getHeight(), 10);
		return new Chunk(jpg, 0, 0);
	}

	/**
	 * return the cell with the status of the different autoProc
	 * 
	 * @param dcInfo
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws BadElementException
	 */
	private Cell getAutoProcResultStatus(DataCollectionInformation dcInfo) throws BadElementException,
			MalformedURLException, IOException {
		Cell resultsCell = new Cell();
		Paragraph p = new Paragraph();
		// edna
		
		if (dcInfo != null && dcInfo.getAutoProcEdnaStatus() != null) {
			if (dcInfo.getAutoProcEdnaStatus().contains("Green")){
				p.setFont(FONT_INDEXING_SUCCESS);
			} else {
				p.setFont(FONT_INDEXING_FAILED);
			}
			
			p.add(new Phrase("EDNA_proc ", FONT_DOC_BOLD));
			if (!isRTF)
				p.add(getChunkImage(dcInfo.getAutoProcEdnaStatus()));
			
			
			//p.add(dcInfo.getAutoProcEdnaStatus());
			p.add(new Phrase("  "));
		}
        
		if (!Constants.SITE_IS_MAXIV()) {
				// fastproc
		    if (dcInfo != null && dcInfo.getAutoProcFastStatus() != null) {
			
			      if (dcInfo.getAutoProcFastStatus().contains("Green")){
				       p.setFont(FONT_INDEXING_SUCCESS);
			       } else {
				     p.setFont(FONT_INDEXING_FAILED);
			       }
			     p.add(new Phrase("grenades_fastproc ", FONT_DOC_BOLD));

			    if (!isRTF)
				    p.add(getChunkImage(dcInfo.getAutoProcFastStatus()));			

			    //p.add(dcInfo.getAutoProcFastStatus());
			    p.add(new Phrase("  "));
		      }

		    // parallelproc
		    if (dcInfo != null && dcInfo.getAutoProcParallelStatus() != null) {
			
			      if (dcInfo.getAutoProcParallelStatus().contains("Green")){
				      p.setFont(FONT_INDEXING_SUCCESS);
			      } else {
				      p.setFont(FONT_INDEXING_FAILED);
			      }
			      p.add(new Phrase("grenades_parallelproc ", FONT_DOC_BOLD));
			      if (!isRTF)
				    p.add(getChunkImage(dcInfo.getAutoProcParallelStatus()));
			
			      //p.add(dcInfo.getAutoProcParallelStatus());
			    p.add(new Phrase("  "));
		      }
    }

		if (Constants.SITE_IS_MAXIV()) {
			// fast_dp
			if (dcInfo != null && dcInfo.getAutoProcFastDPStatus() != null) {
				p.add(new Phrase("fast_dp ", FONT_DOC_BOLD));
				p.add(getChunkImage(dcInfo.getAutoProcFastDPStatus()));
				p.add(new Phrase("  "));
			}
      
      	// autoPROC
		   if (dcInfo != null && dcInfo.getAutoProcAutoPROCStatus() != null) {
			    p.add(new Phrase("autoPROC ", FONT_DOC_BOLD));
			    p.add(getChunkImage(dcInfo.getAutoProcAutoPROCStatus()));
			    p.add(new Phrase("  "));
		    }
      
		}
		resultsCell.add(p);
		return resultsCell;
	}

	/**
	 * set the autoproc results table, if no autoProc add a cell at the end of
	 * topTable
	 * 
	 * @param document
	 * @param topTable
	 * @param collect
	 * @throws Exception
	 */
	private void setAutoProcResultsTable(Document document, DataCollectionInformation collect) throws Exception {
		Integer idDc = -1;
		for (int k = 0; k < dataCollectionList.size(); k++) {
			DataCollection3VO dcVo = dataCollectionList.get(k);
			if (dcVo.getDataCollectionId().equals(collect.getDataCollectionId())) {
				idDc = k;
				break;
			}
		}

		if (idDc != -1) {
			AutoProc3VO[] autoProcs = wrapper.getAutoProcs();
			AutoProcScalingStatistics3VO[] autoProcsOverall = wrapper.getScalingStatsOverall();
			AutoProcScalingStatistics3VO[] autoProcsInner = wrapper.getScalingStatsInner();
			AutoProcScalingStatistics3VO[] autoProcsOuter = wrapper.getScalingStatsOuter();

			AutoProc3VO autoProcValue = autoProcs[idDc];
			AutoProcScalingStatistics3VO autoProcOverall = autoProcsOverall[idDc];
			AutoProcScalingStatistics3VO autoProcInner = autoProcsInner[idDc];
			AutoProcScalingStatistics3VO autoProcOuter = autoProcsOuter[idDc];
			if (autoProcValue == null) {
				// Cell cNo = new Cell(new
				// Paragraph("No autoprocessing results found", FONT_DOC));
				// cNo.setHorizontalAlignment(Element.ALIGN_LEFT);
				// Table tableNR = new Table(1);
				// tableNR.setWidth(100); // percentage
				// tableNR.setPadding(3);
				// tableNR.setCellsFitPage(true);
				// tableNR.setTableFitsPage(true);
				// tableNR.getDefaultCell().setBorderWidth(1);
				// tableNR.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				// tableNR.addCell(cNo);
				// document.add(tableNR);
			} else {
				int noColSPG = 5;
				Table tableSPG = new Table(noColSPG);
				int[] headersWidth = new int[noColSPG];
				int i = 0;

				headersWidth[i++] = 7; // space group
				headersWidth[i++] = 10; // completeness
				headersWidth[i++] = 9; // resolution
				headersWidth[i++] = 9; // rsymm
				headersWidth[i++] = 12; // unit cell
				tableSPG.setWidths(headersWidth);

				tableSPG.setWidth(100); // percentage
				tableSPG.setPadding(3);
				tableSPG.setCellsFitPage(true);
				tableSPG.setTableFitsPage(true);
				tableSPG.getDefaultCell().setBorderWidth(1);
				tableSPG.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

				tableSPG.getDefaultCell().setGrayFill(GREY_FILL_HEADER);
				tableSPG.addCell(new Paragraph("Space Group", FONT_DOC_BOLD));
				tableSPG.addCell(new Paragraph("Completeness (Inner, Outer, Overall)", FONT_DOC_BOLD));
				tableSPG.addCell(new Paragraph("Resolution", FONT_DOC_BOLD));
				tableSPG.addCell(new Paragraph("Rsymm (Inner, Outer, Overall)", FONT_DOC_BOLD));
				tableSPG.addCell(new Paragraph("Unit Cell (a, b, c, alpha, beta, gamma)", FONT_DOC_BOLD));

				tableSPG.getDefaultCell().setGrayFill(GREY_FILL_DATA);

				// space group
				if (autoProcValue != null && autoProcValue.getSpaceGroup() != null) {
					Paragraph p = new Paragraph(autoProcValue.getSpaceGroup(), FONT_DOC);
					tableSPG.addCell(p);
				} else
					tableSPG.addCell("");

				// completeness, rsymm, processed resolution
				String completenessString = new String();
				String rSymmString = new String();
				String resolutionString = new String();

				if (autoProcOverall != null && autoProcInner != null && autoProcOuter != null) {
					completenessString += (autoProcInner.getCompleteness() == null ? "" : df2.format(autoProcInner.getCompleteness())) + "\n"
						+ (autoProcOuter.getCompleteness() == null ? "" : df2.format(autoProcOuter.getCompleteness())) + "\n"
						+ (autoProcOverall.getCompleteness() == null ? "" : df2.format(autoProcOverall.getCompleteness()));
					rSymmString += (autoProcInner.getRmerge() == null ? "" : df2.format(autoProcInner.getRmerge()))
							+ "\n" + (autoProcOuter.getRmerge() == null ? "" : df2.format(autoProcOuter.getRmerge()))
							+ "\n"
							+ (autoProcOverall.getRmerge() == null ? "" : df2.format(autoProcOverall.getRmerge()));
					resolutionString += autoProcInner.getResolutionLimitLow() + " - "
							+ autoProcInner.getResolutionLimitHigh() + "\n" + autoProcOuter.getResolutionLimitLow()
							+ " - " + autoProcOuter.getResolutionLimitHigh() + "\n"
							+ autoProcOverall.getResolutionLimitLow() + " - "
							+ autoProcOverall.getResolutionLimitHigh();
				}
				tableSPG.addCell(new Paragraph(completenessString, FONT_DOC));
				tableSPG.addCell(new Paragraph(resolutionString, FONT_DOC));
				tableSPG.addCell(new Paragraph(rSymmString, FONT_DOC));

				// unit cell
				if (autoProcValue != null && autoProcValue.getSpaceGroup() != null) {
					tableSPG.addCell(new Paragraph("" + autoProcValue.getRefinedCellA() + ", "
							+ autoProcValue.getRefinedCellB() + ", " + autoProcValue.getRefinedCellC() + "\n"
							+ autoProcValue.getRefinedCellAlpha() + ", " + autoProcValue.getRefinedCellBeta() + ", "
							+ autoProcValue.getRefinedCellGamma(), FONT_DOC));
				} else {
					tableSPG.addCell("");
				}
				document.add(tableSPG);
			}// end autoProc null
		}// end idDc -1

		//
		document.add(new Paragraph(" ", FONT_SPACE));
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
			isRTF = new Boolean(false);
			PdfWriter.getInstance(document, baos);
		} else {
			isRTF = new Boolean(true);
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
		setDataCollectionTable2(document);

		// ======================
		// Energy scans
		// ======================
		document.add(new Paragraph(" "));
		setEnergyScansTable(document);

		// ======================
		// XRF Spectra
		// ======================
		document.add(new Paragraph(" "));
		setXfrSpectraTable(document);

		// ======================
		// Summary
		// ======================
		setSummary(document);

		// ======================
		// End of file
		// ======================
		document.close();
		return baos;

	}

	/**
	 * set the dataCollection table
	 * 
	 * @param document
	 * @throws Exception
	 */
	private void setDataCollectionTable2(Document document) throws Exception {
		document.add(new Paragraph("Data Collections:", FONT_TITLE));
		document.add(new Paragraph(" "));
		if (sessionDataObjectList.isEmpty()) {
			document.add(new Paragraph("There is no collect in this report", FONT_DOC));
		} else {
			Table table = new Table(NB_COL_DATACOLLECTION);
			table = setDataCollectionHeader(true);
			DecimalFormat df2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
			df2.applyPattern("#####0.00");
			DecimalFormat df3 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
			df3.applyPattern("#####0.000");
			// DataCollection Rows
			AutoProc3VO[] autoProcs = wrapper.getAutoProcs();
			AutoProcScalingStatistics3VO[] autoProcsOverall = wrapper.getScalingStatsOverall();
			AutoProcScalingStatistics3VO[] autoProcsInner = wrapper.getScalingStatsInner();
			AutoProcScalingStatistics3VO[] autoProcsOuter = wrapper.getScalingStatsOuter();
			Session3Service sessionService = (Session3Service) ejb3ServiceLocator
					.getLocalService(Session3Service.class);
			DataCollectionGroup3Service dcgService = (DataCollectionGroup3Service) ejb3ServiceLocator
					.getLocalService(DataCollectionGroup3Service.class);

			Iterator<SessionDataObjectInformation> it = sessionDataObjectList.iterator();
			while (it.hasNext()) {
				SessionDataObjectInformation col = it.next();
				ScreeningOutput3VO screeningOutput = null;
				ScreeningOutputLattice3VO screeningOutputLattice = null;
				DataCollection3VO collect = null;
				String nbImage = "";
				if (col.isDataCollection()) {
					collect = col.getDataCollection();
					nbImage = collect.getNumberOfImages() == null ? "" : collect.getNumberOfImages().toString();
				} else if (col.isWorkflow()) {
					collect = col.getListDataCollection().get(0);
					nbImage = collect.getNumberOfImages() == null ? "" : collect.getNumberOfImages().toString();
					if (col.getWorkflow().isMeshMXpressM()
							|| (col.getWorkflow().getWorkflowType().equals(Constants.WORKFLOW_XRAYCENTERING))) {
						nbImage = getParamByKey(col.getListParameters(), ViewSessionSummaryAction.KEY_NB_TOT_IMAGES);
					}
				}
				int index = -1;
				if (collect != null)
					index = getDataCollectionId(dataCollectionList, collect.getDataCollectionId());
				if (index != -1) {
					collect = dataCollectionList.get(index);

					if (collect != null) {
						DataCollectionGroup3VO colGroup = dcgService.findByPk(collect.getDataCollectionGroupVOId(), false, true);
						Screening3VO[] tabScreening = colGroup.getScreeningsTab();
						if (tabScreening != null && tabScreening.length > 0) {
							Screening3VO screeningVO = tabScreening[0];
							ScreeningOutput3VO[] screeningOutputTab = screeningVO.getScreeningOutputsTab();
							if (screeningOutputTab != null && screeningOutputTab.length > 0) {
								screeningOutput = screeningOutputTab[0];
								if (screeningOutputTab[0].getScreeningOutputLatticesTab() != null
										&& screeningOutputTab[0].getScreeningOutputLatticesTab().length > 0) {
									screeningOutputLattice = screeningOutputTab[0].getScreeningOutputLatticesTab()[0];
								}
							}
						}

						setDataCollectionData2(document, table, collect, sessionService, autoProcs[index],
								autoProcsOverall[index], autoProcsInner[index], autoProcsOuter[index], true, true,
								screeningOutput, screeningOutputLattice, nbImage);
					}
				}
			}
			document.add(table);
		}
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
	private void setDataCollectionData2(Document document, Table table, DataCollection3VO col,
			Session3Service sessionService, AutoProc3VO autoProcValue, AutoProcScalingStatistics3VO autoProcOverall,
			AutoProcScalingStatistics3VO autoProcInner, AutoProcScalingStatistics3VO autoProcOuter,
			boolean withAutoProcessing, boolean setEDNAInfo, ScreeningOutput3VO screeningOutput,
			ScreeningOutputLattice3VO screeningOutputLattice, String nbImage) throws Exception {
		// Session3VO slv = sessionService.findByPk(col.getSessionId(), false,
		// false, false);
		DataCollectionGroup3VO dcGroup = col.getDataCollectionGroupVO();
		Session3VO slv = dcGroup.getSessionVO();
		// here slv is not null
		if (col.getNumberOfImages() != null) {
			if (!DataCollectionExporter.isDataCollectionScreening(col)) {
				table.getDefaultCell().setGrayFill(GREY_FILL_DATA_COLLECT);
			} else
				table.getDefaultCell().setGrayFill(GREY_FILL_DATA);
		}
		if (col.getImagePrefix() != null)
			table.addCell(new Paragraph(col.getImagePrefix(), FONT_DOC));
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

		if (col.getDataCollectionNumber() != null)
			table.addCell(new Paragraph(col.getDataCollectionNumber().toString(), FONT_DOC));
		else
			table.addCell("");

		if (nbImage != null)
			table.addCell(new Paragraph(nbImage, FONT_DOC));
		else
			table.addCell("");

		if (withAutoProcessing) {
			// space group
			if (autoProcValue != null && autoProcValue.getSpaceGroup() != null) {
				Paragraph p = new Paragraph(autoProcValue.getSpaceGroup(), FONT_DOC);
				table.addCell(p);
			} else if (setEDNAInfo && screeningOutputLattice != null && screeningOutputLattice.getSpaceGroup() != null) {
				Paragraph p = new Paragraph(screeningOutputLattice.getSpaceGroup(), FONT_DOC);
				table.addCell(p);
			} else
				table.addCell("");

			// unit cell
			if (autoProcValue != null && autoProcValue.getSpaceGroup() != null)
				table.addCell(new Paragraph(autoProcValue.getRefinedCellA() + " ("
						+ autoProcValue.getRefinedCellAlpha() + ")\n" + autoProcValue.getRefinedCellB() + " ("
						+ autoProcValue.getRefinedCellBeta() + ")\n" + autoProcValue.getRefinedCellC() + " ("
						+ autoProcValue.getRefinedCellGamma() + ")", FONT_DOC));
			else if (setEDNAInfo && screeningOutputLattice != null && screeningOutputLattice.getUnitCell_a() != null
					&& screeningOutputLattice.getUnitCell_b() != null && screeningOutputLattice.getUnitCell_c() != null
					&& screeningOutputLattice.getUnitCell_alpha() != null
					&& screeningOutputLattice.getUnitCell_beta() != null
					&& screeningOutputLattice.getUnitCell_gamma() != null) {
				Paragraph p = new Paragraph(df3.format(screeningOutputLattice.getUnitCell_a()) + " ("
						+ df3.format(screeningOutputLattice.getUnitCell_alpha()) + ")\n"
						+ df3.format(screeningOutputLattice.getUnitCell_b()) + " ("
						+ df3.format(screeningOutputLattice.getUnitCell_beta()) + ")\n"
						+ df3.format(screeningOutputLattice.getUnitCell_c()) + " ("
						+ df3.format(screeningOutputLattice.getUnitCell_gamma()) + ")", FONT_DOC);
				table.addCell(p);
			} else
				table.addCell("");

			// completeness, rsymm, processed resolution
			String completenessString = new String();
			String rSymmString = new String();
			String resolutionString = new String();

			if (autoProcOverall != null && autoProcInner != null && autoProcOuter != null) {
				completenessString += (autoProcInner.getCompleteness() == null ? "" : df2.format(autoProcInner.getCompleteness())) + "\n"
						+ (autoProcOuter.getCompleteness() == null ? "" : df2.format(autoProcOuter.getCompleteness())) + "\n"
						+ (autoProcOverall.getCompleteness() == null ? "" : df2.format(autoProcOverall.getCompleteness()));
				rSymmString += (autoProcInner.getRmerge() == null ? "" : df2.format(autoProcInner.getRmerge())) + "\n"
						+ (autoProcOuter.getRmerge() == null ? "" : df2.format(autoProcOuter.getRmerge())) + "\n"
						+ (autoProcOverall.getRmerge() == null ? "" : df2.format(autoProcOverall.getRmerge()));
				resolutionString += autoProcInner.getResolutionLimitLow() + " - "
						+ autoProcInner.getResolutionLimitHigh() + "\n" + autoProcOuter.getResolutionLimitLow() + " - "
						+ autoProcOuter.getResolutionLimitHigh() + "\n" + autoProcOverall.getResolutionLimitLow()
						+ " - " + autoProcOverall.getResolutionLimitHigh();
			} else if (setEDNAInfo && screeningOutput != null && screeningOutput.getRankingResolution() != null) {
				resolutionString = df2.format(screeningOutput.getRankingResolution());
			}
			table.addCell(new Paragraph(completenessString, FONT_DOC));
			table.addCell(new Paragraph(rSymmString, FONT_DOC));
			table.addCell(new Paragraph(resolutionString, FONT_DOC));
		}

		// detector resolution
		if (col.getResolution() != null)
			table.addCell(new Paragraph(df2.format(col.getResolution()), FONT_DOC));
		else
			table.addCell("");

		// wavelength
		if (col.getWavelength() != null)
			table.addCell(new Paragraph(df3.format(col.getWavelength()), FONT_DOC));
		else
			table.addCell("");

		// phi range
		if (col.getAxisRange() != null)
			table.addCell(new Paragraph(df2.format(col.getAxisRange()), FONT_DOC));
		else
			table.addCell("");

		// Column crystalClass only for IFX proposal in case of MXPress
		// experiment
		if (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX)) {
			// if (col.getCrystalClass() != null && col.getCrystalClass() != "")
			// table.addCell(new
			// Paragraph(col.getCrystalClass(), new Font(Font.HELVETICA, 8)));
			// else table.addCell("");
			DataCollectionGroup3Service dataCollectionGroupService = (DataCollectionGroup3Service) ejb3ServiceLocator
					.getLocalService(DataCollectionGroup3Service.class);
			DataCollectionGroup3VO group = dataCollectionGroupService.findByPk(col.getDataCollectionGroupVOId(), true, true);
			boolean firstCollect = group.isFirstCollect(col);
			if (dcGroup.getCrystalClass() != null) {
				int idCC = getCrystalClassIndex(listOfCrystalClass, dcGroup.getCrystalClass().trim().toUpperCase());
				String crystalS = "";
				if (idCC == -1) {
					crystalS = dcGroup.getCrystalClass().toString();
				} else {
					crystalS = listOfCrystalClass.get(idCC).getCrystalClassName();
				}
				String crystalCell = crystalS;
				if (!firstCollect && crystalS != null && !crystalS.equals("")) {
					crystalCell = "(" + crystalS + ")";
				}
				table.addCell(new Paragraph(crystalCell, FONT_DOC));
			} else
				table.addCell("");

		}
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		if (col.getComments() != null && col.getComments() != "")
			table.addCell(new Paragraph(col.getComments(), FONT_DOC));
		else
			table.addCell("");
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	}

	/**
	 * returns the index of a given dataCollectionId in a list of
	 * dataCollections
	 * 
	 * @param list
	 * @param dataCollectionId
	 * @return
	 */
	private static int getDataCollectionId(List<DataCollection3VO> list, Integer dataCollectionId) {
		if (list == null || dataCollectionId == null)
			return -1;
		int i = 0;
		for (Iterator<DataCollection3VO> iterator = list.iterator(); iterator.hasNext();) {
			DataCollection3VO dataCollection3VO = (DataCollection3VO) iterator.next();
			if (dataCollection3VO.getDataCollectionId().equals(dataCollectionId))
				return i;
			i++;
		}
		return -1;

	}

	/**
	 * return the Param value for a given key, empty if the key does not exist
	 * 
	 * @param listParam
	 * @param key
	 * @return
	 */
	private static String getParamByKey(List<Param> listParam, String key) {
		if (listParam == null || key == null) {
			return "";
		}
		for (Iterator<Param> iterator = listParam.iterator(); iterator.hasNext();) {
			Param param = (Param) iterator.next();
			if (param.getKey().equals(key)) {
				return param.getValue();
			}
		}
		return "";
	}
	
	/**
	 * returns a cell with a given image inside
	 * 
	 * @param image
	 * @return
	 * @throws Exception
	 */
	private Cell getCellImage(String imagePath, float image_size) throws Exception {
		
		if (imagePath != null ) {
			String image = PathUtils.getPath(imagePath);
			try {				
				Image jpg1 = Image.getInstance(image);
				jpg1.scaleAbsolute(jpg1.getWidth() * image_size / jpg1.getHeight(), image_size);
				Cell cell = new Cell(jpg1);
				cell.setLeading(0);
				cell.setBorderWidth(0);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_CENTER);
				return cell;
			} catch (IOException e) {
				LOG.info(image + " not found");
				return new Cell(new Paragraph("Image not found", FONT_DOC));
				
			}
		}
		return new Cell(new Paragraph("", FONT_DOC));
	}
}
