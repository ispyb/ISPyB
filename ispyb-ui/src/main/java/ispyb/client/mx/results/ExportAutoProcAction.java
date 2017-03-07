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

package ispyb.client.mx.results;

import ispyb.client.common.BreadCrumbsForm;
import ispyb.client.mx.collection.AutoProcShellWrapper;
import ispyb.client.mx.collection.PdfRtfExporter;
import ispyb.client.mx.collection.ViewDataCollectionAction;
import ispyb.common.util.Constants;
import ispyb.common.util.Formatter;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.autoproc.AutoProc3Service;
import ispyb.server.mx.services.autoproc.AutoProcScalingStatistics3Service;
import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.server.mx.vos.autoproc.AutoProc3VO;
import ispyb.server.mx.vos.autoproc.AutoProcScalingStatistics3VO;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.Session3VO;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.RtfWriter2;

/**
 * export auto proc info in PDF or RTF document. Issue 1187
 * 
 * @author Marjolaine Bodin
 * 
 * @struts.action name="exportAutoProcForm" path="/user/exportAutoProc" input="userOnly.results.viewResults.page"
 *                parameter="reqCode" scope="request" validate="false"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 */
public class ExportAutoProcAction extends DispatchAction {

	private Session3Service sessionService;

	private DataCollection3Service dataCollectionService;

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	// proposal
	String proposalCode;

	String proposalNumber;

	// session info
	Session3VO slv = null;

	// dataCollection info
	DataCollection3VO dc = null;

	// auto proc
	private AutoProc3VO autoProc;

	private AutoProcScalingStatistics3VO autoProcStatisticsOverall;

	private AutoProcScalingStatistics3VO autoProcStatisticsInner;

	private AutoProcScalingStatistics3VO autoProcStatisticsOuter;

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws Exception {
		this.sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
		this.dataCollectionService = (DataCollection3Service) ejb3ServiceLocator
				.getLocalService(DataCollection3Service.class);

	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	public ActionForward exportAutoProcAsPdf(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();

		try {
			ByteArrayOutputStream baos = prepareExport(mapping, actForm, request, response, false);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.viewResults"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		return null;

	}

	public ActionForward exportAutoProcAsRtf(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();

		try {
			ByteArrayOutputStream baos = prepareExport(mapping, actForm, request, response, true);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.viewResults"));
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}
		return null;
	}

	private ByteArrayOutputStream prepareExport(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response, boolean rtfFormat) throws Exception {

		// Get data to populate the form
		BreadCrumbsForm bar = BreadCrumbsForm.getIt(request);
		proposalCode = (String) request.getSession().getAttribute(Constants.PROPOSAL_CODE);
		proposalNumber = String.valueOf(request.getSession().getAttribute(Constants.PROPOSAL_NUMBER));

		// session?
		slv = null;
		Integer sessionId = null;
		if (bar.getSelectedSession() != null) {
			sessionId = bar.getSelectedSession().getSessionId();
			slv = sessionService.findByPk(sessionId, false, false, false);
			bar.setSelectedSession(slv);
		}
		// DataCollection
		String dataCollectionImagePrefix = "";
		Integer dataCollectionId = null;
		dc = null;
		if (bar.getSelectedDataCollection() != null) {
			dataCollectionId = bar.getSelectedDataCollection().getDataCollectionId();
			dc = dataCollectionService.findByPk(dataCollectionId, false,  false);
			bar.setSelectedDataCollection(dc);
			dataCollectionImagePrefix = dc.getImagePrefix();
		}
		
		// autoProcId
		Integer autoProcId = null;
		String s = request.getParameter("autoProcId");
		if(s != null)
			autoProcId = new Integer(s);
		// auto proc
		if(autoProcId ==null){
			List<Integer> listDataCollectionIds = new ArrayList<Integer>();
			if(dc != null)
				listDataCollectionIds.add(dc.getDataCollectionId());
			AutoProcShellWrapper wrapper = getAutoProcWrapper(actForm, listDataCollectionIds);
			autoProc = wrapper.getAutoProcs()[0];
			autoProcStatisticsOverall = wrapper.getScalingStatsOverall()[0];
			autoProcStatisticsOuter = wrapper.getScalingStatsOuter()[0];
			autoProcStatisticsInner = wrapper.getScalingStatsInner()[0];
		}else{
			AutoProc3Service autoProcService = (AutoProc3Service) ejb3ServiceLocator.getLocalService(AutoProc3Service.class);
			AutoProcScalingStatistics3Service apssService = (AutoProcScalingStatistics3Service) ejb3ServiceLocator.getLocalService(AutoProcScalingStatistics3Service.class);
			autoProc= autoProcService.findByPk(autoProcId);
			Collection<?> autoProcsOverall = apssService.findByAutoProcId(autoProc.getAutoProcId(), "overall");
			Collection<?> autoProcsOuter = apssService.findByAutoProcId(autoProc.getAutoProcId(), "outerShell");

			if (!autoProcsOverall.isEmpty())
				autoProcStatisticsOverall = (AutoProcScalingStatistics3VO) autoProcsOverall.iterator().next();
			if (!autoProcsOuter.isEmpty())
				autoProcStatisticsOuter = (AutoProcScalingStatistics3VO) autoProcsOuter.iterator().next();
			List<AutoProcScalingStatistics3VO> autoProcStatistics = apssService.findByAutoProcId(autoProc.getAutoProcId(), "innerShell");
			autoProcStatisticsInner = apssService.getBestAutoProcScalingStatistic(autoProcStatistics);
		}

		// Filename
		String filename = getFileName(proposalCode, proposalNumber, dataCollectionImagePrefix);
		ByteArrayOutputStream baos;
		baos = exportAutoProc(!rtfFormat);

		// setting some response headers
		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma", "public");
		// setting the content type
		String fileExtension = (rtfFormat) ? ".rtf" : ".pdf";
		String mimeType = (rtfFormat) ? "application/rtf" : "application/pdf";
		response.setContentType(mimeType);
		filename += fileExtension;
		response.setHeader("Content-Disposition", "attachment; filename=" + filename);
		// the contentlength is needed for MSIE!!!
		response.setContentLength(baos.size());
		// write ByteArrayOutputStream to the ServletOutputStream
		ServletOutputStream out = response.getOutputStream();
		baos.writeTo(out);
		out.flush();
		out.close();

		BreadCrumbsForm.setIt(request, bar);

		return baos;
	}

	private String getFileName(String proposalCode, String proposalNumber, String dcImagePrefix) {
		// Filename
		String filename = proposalCode + proposalNumber + "_" + dcImagePrefix;
		filename = filename.replaceAll(" ", "_");
		return filename;
	}

	public ByteArrayOutputStream exportAutoProc(boolean exportAsPDF) throws Exception {
		// create simple doc and write to a ByteArrayOutputStream
		Document document = new Document(PageSize.A4);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (exportAsPDF) {
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
		// dataCollection info
		setDataCollectionInfo(document);
		document.add(new Paragraph(" "));
		// Session comments
		setAutoProcInfo(document);

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
		String h = "Auto Processing for Proposal: " + proposalCode + proposalNumber;
		if (slv != null) {
			h += " on Beamline: " + (slv.getBeamlineName() == null ? "" : slv.getBeamlineName())
					+ "  ---  Session start date: "
					+ (slv.getStartDate() == null ? "" : Formatter.formatDate(slv.getStartDate()));
		}
		header = new HeaderFooter(new Phrase(h, PdfRtfExporter.FONT_HELVETICA_10), false);
		header.setAlignment(Element.ALIGN_CENTER);
		header.setBorderWidth(1);
		header.getBefore().getFont().setSize(PdfRtfExporter.SIZE_FONT);
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
		footer.getBefore().getFont().setSize(PdfRtfExporter.SMALL_FONT);
		document.setFooter(footer);
	}

	/**
	 * set dataCollection information - for the moment the image prefix
	 * 
	 * @param document
	 * @throws Exception
	 */
	private void setDataCollectionInfo(Document document) throws Exception {
		document.add(new Paragraph("Data collection image prefix", PdfRtfExporter.FONT_TITLE_11));
		if (dc != null)
			document.add(new Paragraph(dc.getImagePrefix(), PdfRtfExporter.FONT_DOC_11));
	}

	private void setAutoProcInfo(Document document) throws Exception {
		// header
		Table headerTable = new Table(1);
		headerTable.getDefaultCell().setBorderWidth(0);
		headerTable.setBorderWidth(0);
		headerTable.setCellsFitPage(true);
		headerTable.setAlignment(Element.ALIGN_LEFT);
		headerTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		headerTable.getDefaultCell().setBackgroundColor(PdfRtfExporter.LIGHT_YELLOW_COLOR);
		headerTable.getDefaultCell().setLeading(3);
		headerTable.setWidth(100); // percentage
		headerTable.addCell(new Paragraph("Crystal data and data-collection statistics", PdfRtfExporter.FONT_DOC_11));
		headerTable.addCell(new Paragraph("Values in parentheses are for the highest resolution shell.",
				PdfRtfExporter.FONT_DOC_11));
		document.add(headerTable);
		document.add(new Paragraph(" ", PdfRtfExporter.VERY_SMALL_FONT));
		// auto proc table
		Table autoProcTable = new Table(2);
		autoProcTable.getDefaultCell().setBorderWidth(0);
		autoProcTable.setBorderWidth(0);
		autoProcTable.setCellsFitPage(true);
		autoProcTable.setAlignment(Element.ALIGN_LEFT);
		autoProcTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		// autoProcTable.getDefaultCell().setLeading(3);
		autoProcTable.setPadding(2);
		autoProcTable.setWidth(100); // percentage
		// data
		String spaceGroup = autoProc == null ? "" : autoProc.getSpaceGroup();
		String unitCell_a = autoProc == null ? "" : autoProc.getRefinedCellA().toString();
		String unitCell_b = autoProc == null ? "" : autoProc.getRefinedCellB().toString();
		String unitCell_c = autoProc == null ? "" : autoProc.getRefinedCellC().toString();
		String unitCell_alpha = autoProc == null ? "" : autoProc.getRefinedCellAlpha().toString();
		String unitCell_beta = autoProc == null ? "" : autoProc.getRefinedCellBeta().toString();
		String unitCell_gamma = autoProc == null ? "" : autoProc.getRefinedCellGamma().toString();
		String resolutionRange = "";
		String nTotalObservations = "";
		String nTotalUniqueReflections = "";
		String completeness = "";
		String multiplicity = "";
		String isigma = "";
		String rmerge = "";
		if (autoProcStatisticsOverall != null) {
			resolutionRange = autoProcStatisticsOverall.getResolutionLimitLow() + " - "
					+ autoProcStatisticsOverall.getResolutionLimitHigh();
			nTotalObservations = "" + autoProcStatisticsOverall.getnTotalObservations();
			nTotalUniqueReflections = "" + (autoProcStatisticsOverall.getnTotalUniqueObservations() == null? "":autoProcStatisticsOverall.getnTotalUniqueObservations());
			completeness = "" + autoProcStatisticsOverall.getCompleteness();
			multiplicity = "" + autoProcStatisticsOverall.getMultiplicity();
			isigma = "" + autoProcStatisticsOverall.getMeanIoverSigI();
			rmerge = "" +( autoProcStatisticsOverall.getRmerge() == null ? "":autoProcStatisticsOverall.getRmerge()) ;
		}
		if (autoProcStatisticsOuter != null) {
			resolutionRange += " (" + autoProcStatisticsOuter.getResolutionLimitLow() + " - "
					+ autoProcStatisticsOuter.getResolutionLimitHigh() + ")";
			nTotalObservations += " (" + autoProcStatisticsOuter.getnTotalObservations() + ")";
			nTotalUniqueReflections += autoProcStatisticsOuter.getnTotalUniqueObservations() == null ? "" : (" (" + autoProcStatisticsOuter.getnTotalUniqueObservations() + ")");
			completeness += " (" + autoProcStatisticsOuter.getCompleteness() + ")";
			multiplicity += " (" + autoProcStatisticsOuter.getMultiplicity() + ")";
			isigma += " (" + autoProcStatisticsOuter.getMeanIoverSigI() + ")";
			rmerge += " (" + (autoProcStatisticsOuter.getRmerge() == null? "":autoProcStatisticsOuter.getRmerge()) + ")";
		}
		// space group
		autoProcTable.addCell(new Paragraph("Space Group", PdfRtfExporter.FONT_DOC_11));
		autoProcTable.addCell(new Paragraph(spaceGroup, PdfRtfExporter.FONT_DOC_11));
		// unit cell parameters
		autoProcTable.addCell(new Paragraph("Unit-cell parameters (" + Constants.ANGSTROM + ")",
				PdfRtfExporter.FONT_DOC_11));
		autoProcTable.addCell(new Paragraph("", PdfRtfExporter.FONT_DOC_11));
		Paragraph pa = new Paragraph("\t a", PdfRtfExporter.FONT_DOC_11);
		pa.setAlignment(Element.ALIGN_JUSTIFIED);
		pa.setIndentationLeft(PdfRtfExporter.INDENTATION_LEFT);
		autoProcTable.addCell(pa);
		autoProcTable.addCell(new Paragraph(unitCell_a, PdfRtfExporter.FONT_DOC_11));
		Paragraph pb = new Paragraph("\t b", PdfRtfExporter.FONT_DOC_11);
		pb.setAlignment(Element.ALIGN_JUSTIFIED);
		pb.setIndentationLeft(PdfRtfExporter.INDENTATION_LEFT);
		autoProcTable.addCell(pb);
		autoProcTable.addCell(new Paragraph(unitCell_b, PdfRtfExporter.FONT_DOC_11));
		Paragraph pc = new Paragraph("\t c", PdfRtfExporter.FONT_DOC_11);
		pc.setAlignment(Element.ALIGN_JUSTIFIED);
		pc.setIndentationLeft(PdfRtfExporter.INDENTATION_LEFT);
		autoProcTable.addCell(pc);
		autoProcTable.addCell(new Paragraph(unitCell_c, PdfRtfExporter.FONT_DOC_11));
		// Issue 1733: cell angles info added
		Paragraph palpha = new Paragraph("\t alpha", PdfRtfExporter.FONT_DOC_11);
		palpha.setAlignment(Element.ALIGN_JUSTIFIED);
		palpha.setIndentationLeft(PdfRtfExporter.INDENTATION_LEFT);
		autoProcTable.addCell(palpha);
		autoProcTable.addCell(new Paragraph(unitCell_alpha, PdfRtfExporter.FONT_DOC_11));
		Paragraph pbeta = new Paragraph("\t beta", PdfRtfExporter.FONT_DOC_11);
		pbeta.setAlignment(Element.ALIGN_JUSTIFIED);
		pbeta.setIndentationLeft(PdfRtfExporter.INDENTATION_LEFT);
		autoProcTable.addCell(pbeta);
		autoProcTable.addCell(new Paragraph(unitCell_beta, PdfRtfExporter.FONT_DOC_11));
		Paragraph pgamma = new Paragraph("\t gamma", PdfRtfExporter.FONT_DOC_11);
		pgamma.setAlignment(Element.ALIGN_JUSTIFIED);
		pgamma.setIndentationLeft(PdfRtfExporter.INDENTATION_LEFT);
		autoProcTable.addCell(pgamma);
		autoProcTable.addCell(new Paragraph(unitCell_gamma, PdfRtfExporter.FONT_DOC_11));
		// resolution range
		autoProcTable
				.addCell(new Paragraph("Resolution range (" + Constants.ANGSTROM + ")", PdfRtfExporter.FONT_DOC_11));
		autoProcTable.addCell(new Paragraph(resolutionRange, PdfRtfExporter.FONT_DOC_11));
		// Observed reflections
		autoProcTable.addCell(new Paragraph("Observed reflections", PdfRtfExporter.FONT_DOC_11));
		autoProcTable.addCell(new Paragraph(nTotalObservations, PdfRtfExporter.FONT_DOC_11));
		// No. of unique reflections
		autoProcTable.addCell(new Paragraph("No. of unique reflections", PdfRtfExporter.FONT_DOC_11));
		autoProcTable.addCell(new Paragraph(nTotalUniqueReflections, PdfRtfExporter.FONT_DOC_11));
		// Completeness
		autoProcTable.addCell(new Paragraph("Completeness (%)", PdfRtfExporter.FONT_DOC_11));
		autoProcTable.addCell(new Paragraph(completeness, PdfRtfExporter.FONT_DOC_11));
		// multiplicity
		autoProcTable.addCell(new Paragraph("Multiplicity", PdfRtfExporter.FONT_DOC_11));
		autoProcTable.addCell(new Paragraph(multiplicity, PdfRtfExporter.FONT_DOC_11));
		new Phrase();
		// I/σ(I)
		Phrase p = Phrase.getInstance("<I/" + (char) 963 + "(I)>");
		p.setFont(PdfRtfExporter.FONT_DOC_11);
		autoProcTable.addCell(p);
		autoProcTable.addCell(new Paragraph(isigma, PdfRtfExporter.FONT_DOC_11));
		// Rmerge
		Chunk c1 = new Chunk("R", PdfRtfExporter.FONT_DOC_11);
		Chunk c2 = new Chunk("merge", PdfRtfExporter.FONT_DOC_EXPONENT);
		c2.setTextRise(PdfRtfExporter.TEXT_RISE_SUB);
		Chunk c3 = new Chunk("(%)", PdfRtfExporter.FONT_DOC_11);
		Chunk c4 = new Chunk("#", PdfRtfExporter.FONT_DOC_EXPONENT_BLUE);
		c4.setTextRise(PdfRtfExporter.TEXT_RISE_EXP);
		Paragraph rMergeParagraph = new Paragraph();
		rMergeParagraph.add(c1);
		rMergeParagraph.add(c2);
		rMergeParagraph.add(c3);
		rMergeParagraph.add(c4);
		autoProcTable.addCell(rMergeParagraph);
		autoProcTable.addCell(new Paragraph(rmerge, PdfRtfExporter.FONT_DOC_11));
		document.add(autoProcTable);
		document.add(new Paragraph(" ", PdfRtfExporter.FONT_DOC_11));
		// nota bene info
		Paragraph nbParagraph = new Paragraph();
		nbParagraph.add(c4);
		nbParagraph.add(c1);
		nbParagraph.add(c2);
		Chunk cesp = new Chunk(" ", PdfRtfExporter.FONT_DOC_11);
		Chunk c5 = new Chunk(" = ", PdfRtfExporter.FONT_DOC_11);
		nbParagraph.add(c5);
		Phrase pSigma = Phrase.getInstance("" + (char) 931);
		pSigma.setFont(PdfRtfExporter.FONT_DOC_11);
		nbParagraph.add(pSigma);
		Chunk chkl = new Chunk("hkl", PdfRtfExporter.FONT_DOC_EXPONENT);
		chkl.setTextRise(PdfRtfExporter.TEXT_RISE_SUB);
		nbParagraph.add(chkl);
		nbParagraph.add(cesp);
		nbParagraph.add(pSigma);
		Chunk ci = new Chunk("i", PdfRtfExporter.FONT_DOC_EXPONENT);
		ci.setTextRise(PdfRtfExporter.TEXT_RISE_SUB);
		nbParagraph.add(ci);
		Chunk c8 = new Chunk(" |I", PdfRtfExporter.FONT_DOC_11);
		nbParagraph.add(c8);
		nbParagraph.add(ci);
		Chunk c9 = new Chunk("(hkl) - (I(hkl))| / ", PdfRtfExporter.FONT_DOC_11);
		nbParagraph.add(c9);
		nbParagraph.add(pSigma);
		nbParagraph.add(chkl);
		nbParagraph.add(cesp);
		nbParagraph.add(pSigma);
		nbParagraph.add(ci);
		nbParagraph.add(cesp);
		Chunk c10 = new Chunk("I", PdfRtfExporter.FONT_DOC_11);
		nbParagraph.add(c10);
		nbParagraph.add(ci);
		Chunk c11 = new Chunk("(hkl), where ", PdfRtfExporter.FONT_DOC_11);
		nbParagraph.add(c11);
		Chunk c12 = new Chunk("I", PdfRtfExporter.FONT_DOC_11_ITALIC);
		nbParagraph.add(c12);
		Chunk cii = new Chunk("i", PdfRtfExporter.FONT_DOC_EXPONENT_ITALIC);
		cii.setTextRise(PdfRtfExporter.TEXT_RISE_SUB);
		nbParagraph.add(cii);
		Chunk c13 = new Chunk("(hkl)", PdfRtfExporter.FONT_DOC_11_ITALIC);
		nbParagraph.add(c13);
		Chunk c14 = new Chunk(" is the ", PdfRtfExporter.FONT_DOC_11);
		nbParagraph.add(c14);
		Chunk c15 = new Chunk("i", PdfRtfExporter.FONT_DOC_11_ITALIC);
		nbParagraph.add(c15);
		Chunk c16 = new Chunk("th observation of reflection ", PdfRtfExporter.FONT_DOC_11);
		nbParagraph.add(c16);
		Chunk chklI = new Chunk("hkl", PdfRtfExporter.FONT_DOC_11_ITALIC);
		nbParagraph.add(chklI);
		Chunk c17 = new Chunk(" and ", PdfRtfExporter.FONT_DOC_11);
		nbParagraph.add(c17);
		Chunk c18 = new Chunk("<I(hkl)> ", PdfRtfExporter.FONT_DOC_11_ITALIC);
		nbParagraph.add(c18);
		Chunk c19 = new Chunk(" is the weighted average intensity for all observations of reflection ",
				PdfRtfExporter.FONT_DOC_11);
		nbParagraph.add(c19);
		nbParagraph.add(chklI);
		Chunk c20 = new Chunk(".", PdfRtfExporter.FONT_DOC_11);
		nbParagraph.add(c20);
		document.add(nbParagraph);
		document.add(new Paragraph(" ", PdfRtfExporter.FONT_DOC_11));
	}

	private AutoProcShellWrapper getAutoProcWrapper(ActionForm actForm, List<Integer> dataCollectionList)
			throws Exception {
		ExportAutoProcForm form = (ExportAutoProcForm) actForm;
		double rMerge = 10.0;
		double iSigma = 1.0;

		if (form.getRmergeCutoff() != null)
			rMerge = Double.parseDouble(form.getRmergeCutoff());
		if (form.getIsigmaCutoff() != null)
			iSigma = Double.parseDouble(form.getIsigmaCutoff());

		AutoProcShellWrapper wrapper = ViewDataCollectionAction.getAutoProcStatisticsIds(dataCollectionList, rMerge,
				iSigma);
		return wrapper;
	}

}
