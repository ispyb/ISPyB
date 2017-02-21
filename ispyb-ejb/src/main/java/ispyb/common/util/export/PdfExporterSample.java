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

import ispyb.server.mx.vos.sample.BLSample3VO;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 
 * @author DELAGENI
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style -
 *         Code Templates
 */
public class PdfExporterSample {

	List aList;

	String viewName;

	String sortView;
	
	String proposalDesc;

	DecimalFormat df1 = new DecimalFormat("#####0.0");

	private final static Logger LOG = Logger.getLogger(PdfExporterSample.class);

	/**
	 * 
	 * @param aList
	 * @param slv
	 * @param proposalCode
	 */
	public PdfExporterSample(List aList, String viewName, String sortView, String proposalDesc) {
		super();
		this.viewName = viewName;
		this.sortView = sortView;
		this.aList = aList;
		this.proposalDesc = proposalDesc;

	}

	/**
	 * Exports the file for viewSample for shipment
	 * 
	 * @return
	 * @throws Exception
	 */
	public ByteArrayOutputStream exportAsPdf() throws Exception {

		// create simple doc and write to a ByteArrayOutputStream
		Document document = new Document(PageSize.A4.rotate(), 20, 20, 20, 20);
		document.addTitle("exportSamplesView");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, baos);
		HeaderFooter header;

		// header + footer
		if (viewName != null)
			header = new HeaderFooter(new Phrase("Samples for Proposal: " + proposalDesc + "  ---  "
					+ viewName), false);

		else
			header = new HeaderFooter(new Phrase("Samples for Proposal: " + proposalDesc), false);

		header.setAlignment(Element.ALIGN_CENTER);
		header.setBorderWidth(1);
		header.getBefore().getFont().setSize(8);
		HeaderFooter footer = new HeaderFooter(new Phrase("Page n."), true);
		footer.setAlignment(Element.ALIGN_RIGHT);
		footer.setBorderWidth(1);
		footer.getBefore().getFont().setSize(6);

		document.setHeader(header);
		document.setFooter(footer);

		document.open();

		if (aList.isEmpty()) {
			document.add(new Paragraph("There is no samples in this report"));
			document.close();
			return baos;
		}
		// Create first table for samples

		int NumColumns = 19;
		PdfPTable table = new PdfPTable(NumColumns);
		int headerwidths[] = { 6, 6, 6, 6, 6, 4, 6, 4, 4, 4, 4, 4, 4, 8, 5, 5, 5, 10, 6 }; // percentage
		table.setWidths(headerwidths);

		table.setWidthPercentage(100); // percentage
		table.getDefaultCell().setPadding(3);
		table.getDefaultCell().setBorderWidth(1);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

		// header
		PdfPCell cell = new PdfPCell();
		table.addCell(new Paragraph("Protein", new Font(Font.HELVETICA, 8)));
		table.addCell(new Paragraph("Sample name", new Font(Font.HELVETICA, 8)));
		table.addCell(new Paragraph("Smp code", new Font(Font.HELVETICA, 8)));
		table.addCell(new Paragraph("Dewar", new Font(Font.HELVETICA, 8)));
		table.addCell(new Paragraph("Container", new Font(Font.HELVETICA, 8)));
		table.addCell(new Paragraph("Loc. in cont.", new Font(Font.HELVETICA, 8)));
		table.addCell(new Paragraph("Space group", new Font(Font.HELVETICA, 8)));
		table.addCell(new Paragraph("Cell a", new Font(Font.HELVETICA, 8)));
		table.addCell(new Paragraph("Cell b", new Font(Font.HELVETICA, 8)));
		table.addCell(new Paragraph("Cell c", new Font(Font.HELVETICA, 8)));
		table.addCell(new Paragraph("Cell alpha", new Font(Font.HELVETICA, 8)));
		table.addCell(new Paragraph("Cell beta", new Font(Font.HELVETICA, 8)));
		table.addCell(new Paragraph("Cell gamma", new Font(Font.HELVETICA, 8)));

		cell = new PdfPCell(new Paragraph("Crystal comments", new Font(Font.HELVETICA, 8)));
		table.addCell(cell);

		table.addCell(new Paragraph("Already observed resol.", new Font(Font.HELVETICA, 8)));
		table.addCell(new Paragraph("Required resol.", new Font(Font.HELVETICA, 8)));
		table.addCell(new Paragraph("Min. resol.", new Font(Font.HELVETICA, 8)));
		table.addCell(new Paragraph("Sample comments", new Font(Font.HELVETICA, 8)));
		table.addCell(new Paragraph("Sample status", new Font(Font.HELVETICA, 8)));

		table.setHeaderRows(1); // this is the end of the table header

		table.getDefaultCell().setBorderWidth(1);
		DecimalFormat df1 = new DecimalFormat("#####0.0");
		DecimalFormat df2 = new DecimalFormat("#####0.00");

		Iterator it = aList.iterator();
		int i = 1;
		String currentContainer = "next";
		String nextContainer = "next";
		
		while (it.hasNext()) {
			table.getDefaultCell().setGrayFill(0.99f);
			if (i % 2 == 1) {
				table.getDefaultCell().setGrayFill(0.9f);
			}
			BLSample3VO samplefv = (BLSample3VO) it.next();
			LOG.debug("table of datacollections pdf "+samplefv.getBlSampleId());

			if (samplefv.getContainerVO() != null && samplefv.getContainerVO().getCode() != null)
				nextContainer = samplefv.getContainerVO().getCode();
			else
				nextContainer = "next";

			// in the case of view sorted by dewar/container, we add a page break afetr each container
			if (sortView.equals("2") && !currentContainer.equals(nextContainer)) {
				document.add(table);
				table.deleteBodyRows();
				document.newPage();
			}

			if (samplefv.getCrystalVO().getProteinVO().getAcronym() != null)
				table.addCell(new Paragraph(samplefv.getCrystalVO().getProteinVO().getAcronym(), new Font(
						Font.HELVETICA, 8)));
			else
				table.addCell("");

			if (samplefv.getName() != null)
				table.addCell(new Paragraph(samplefv.getName(), new Font(Font.HELVETICA, 8)));
			else
				table.addCell("");

			if (samplefv.getCode() != null)
				table.addCell(new Paragraph(samplefv.getCode(), new Font(Font.HELVETICA, 8)));
			else
				table.addCell("");

			if (samplefv.getContainerVO() != null && samplefv.getContainerVO().getDewarVO() != null
					&& samplefv.getContainerVO().getDewarVO().getCode() != null)
				table.addCell(new Paragraph(samplefv.getContainerVO().getDewarVO().getCode(), new Font(Font.HELVETICA,
						8)));
			else
				table.addCell("");

			if (samplefv.getContainerVO() != null && samplefv.getContainerVO().getCode() != null) {
				currentContainer = samplefv.getContainerVO().getCode();
				table.addCell(new Paragraph(currentContainer, new Font(Font.HELVETICA, 8)));
			} else {
				currentContainer = "current";
				table.addCell("");
			}

			if (samplefv.getLocation() != null)
				table.addCell(new Paragraph(samplefv.getLocation(), new Font(Font.HELVETICA, 8)));
			else
				table.addCell("");

			if (samplefv.getCrystalVO().getSpaceGroup() != null)
				table.addCell(new Paragraph(samplefv.getCrystalVO().getSpaceGroup(), new Font(Font.HELVETICA, 8)));
			else
				table.addCell("");

			if (samplefv.getCrystalVO().getCellA() != null)
				table.addCell(new Paragraph(df1.format(samplefv.getCrystalVO().getCellA()), new Font(Font.HELVETICA, 8)));
			else
				table.addCell("");

			if (samplefv.getCrystalVO().getCellB() != null)
				table.addCell(new Paragraph(df1.format(samplefv.getCrystalVO().getCellB()), new Font(Font.HELVETICA, 8)));
			else
				table.addCell("");

			if (samplefv.getCrystalVO().getCellC() != null)
				table.addCell(new Paragraph(df1.format(samplefv.getCrystalVO().getCellC()), new Font(Font.HELVETICA, 8)));
			else
				table.addCell("");

			if (samplefv.getCrystalVO().getCellAlpha() != null)
				table.addCell(new Paragraph(df1.format(samplefv.getCrystalVO().getCellAlpha()), new Font(
						Font.HELVETICA, 8)));
			else
				table.addCell("");

			if (samplefv.getCrystalVO().getCellBeta() != null)
				table.addCell(new Paragraph(df1.format(samplefv.getCrystalVO().getCellBeta()), new Font(Font.HELVETICA,
						8)));
			else
				table.addCell("");

			if (samplefv.getCrystalVO().getCellGamma() != null)
				table.addCell(new Paragraph(df1.format(samplefv.getCrystalVO().getCellGamma()), new Font(
						Font.HELVETICA, 8)));
			else
				table.addCell("");

			if (samplefv.getCrystalVO().getComments() != null)
				table.addCell(new Paragraph(samplefv.getCrystalVO().getComments(), new Font(Font.HELVETICA, 8)));
			else
				table.addCell("");

			if (samplefv.getDiffractionPlanVO() != null && samplefv.getDiffractionPlanVO().getObservedResolution() != null){
				table.addCell(new Paragraph(df2.format(samplefv.getDiffractionPlanVO().getObservedResolution()),
						new Font(Font.HELVETICA, 8)));
			}else if (samplefv.getCrystalVO().getDiffractionPlanVO() != null
					&& samplefv.getCrystalVO().getDiffractionPlanVO().getObservedResolution() != null)
				table.addCell(new Paragraph(df2.format(samplefv.getCrystalVO().getDiffractionPlanVO().getObservedResolution()),
						new Font(Font.HELVETICA, 8)));
			else
				table.addCell("");

			if (samplefv.getDiffractionPlanVO() != null
					&& samplefv.getDiffractionPlanVO().getRequiredResolution() != null){
				table.addCell(new Paragraph(df2.format(samplefv.getDiffractionPlanVO().getRequiredResolution()),
						new Font(Font.HELVETICA, 8)));
			}else if (samplefv.getCrystalVO().getDiffractionPlanVO() != null
					&& samplefv.getCrystalVO().getDiffractionPlanVO().getRequiredResolution() != null)
				table.addCell(new Paragraph(df2.format(samplefv.getCrystalVO().getDiffractionPlanVO().getRequiredResolution()),
						new Font(Font.HELVETICA, 8)));
			else
				table.addCell("");
			
			if (samplefv.getDiffractionPlanVO() != null
					&& samplefv.getDiffractionPlanVO().getMinimalResolution() != null){
				table.addCell(new Paragraph(df2.format(samplefv.getDiffractionPlanVO().getMinimalResolution()),
						new Font(Font.HELVETICA, 8)));
			}else if (samplefv.getCrystalVO().getDiffractionPlanVO() != null
					&& samplefv.getCrystalVO().getDiffractionPlanVO().getMinimalResolution() != null)
				table.addCell(new Paragraph(df2.format(samplefv.getCrystalVO().getDiffractionPlanVO().getMinimalResolution()),
						new Font(Font.HELVETICA, 8)));
			else
				table.addCell("");

			if (samplefv.getComments() != null && samplefv.getComments() != "")
				table.addCell(new Paragraph(samplefv.getComments(), new Font(Font.HELVETICA, 8)));
			else
				table.addCell("");

			if (samplefv.getBlSampleStatus() != null)
				table.addCell(new Paragraph(samplefv.getBlSampleStatus(), new Font(Font.HELVETICA, 8)));
			else
				table.addCell("");

			if (i % 2 == 1) {
				table.getDefaultCell().setGrayFill(0.0f);
			}

			i++;
		}

		document.add(table);

		document.close();

		return baos;
	}
}
