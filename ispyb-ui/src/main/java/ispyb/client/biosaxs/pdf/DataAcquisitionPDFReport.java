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

package ispyb.client.biosaxs.pdf;

import ispyb.client.biosaxs.dataAdapter.BiosaxsActions;
import ispyb.client.mx.collection.PdfRtfExporter;
import ispyb.server.biosaxs.vos.dataAcquisition.Buffer3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Measurement3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Specimen3VO;
import ispyb.server.biosaxs.vos.datacollection.SaxsDataCollection3VO;
import ispyb.server.biosaxs.vos.datacollection.Subtraction3VO;
import ispyb.server.biosaxs.vos.utils.comparator.MeasurementComparator;
import ispyb.server.common.vos.proposals.Proposal3VO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class DataAcquisitionPDFReport extends DataAcquisitionReport implements IPDFReport {

	String[] ImageColumns = { "Measurement", "Scattering",  "Guinier", "Kratky", "Gnom" };
	
	 public static String[][] FONTS = {
	        {BaseFont.HELVETICA, BaseFont.WINANSI},
	        {"resources/fonts/cmr10.afm", BaseFont.WINANSI},
	        {"resources/fonts/cmr10.pfm", BaseFont.WINANSI},
	        {"c:/windows/fonts/arial.ttf", BaseFont.WINANSI},
	        {"c:/windows/fonts/arial.ttf", BaseFont.IDENTITY_H},
	        {"resources/fonts/Puritan2.otf", BaseFont.WINANSI},
	        {"c:/windows/fonts/msgothic.ttc,0", BaseFont.IDENTITY_H},
	        {"KozMinPro-Regular", "UniJIS-UCS2-H"}
	    };
	 
	 private Font getFont(String baseFont){
		 BaseFont bf = null;
	        Font font;
	            try {
					bf = BaseFont.createFont(baseFont.toString(), FONTS[0][1], BaseFont.EMBEDDED);
				} catch (DocumentException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
	            font = new Font(bf, 6);
	        
		 return font;
		 
	 }

	 private Font getFont(){
		 BaseFont bf = null;
	        Font font;
	            try {
					bf = BaseFont.createFont(FONTS[0][0], FONTS[0][1], BaseFont.EMBEDDED);
				} catch (DocumentException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
	            font = new Font(bf, 6);
	        
		 return font;
	 }
	
	private Paragraph getCell(String text, String helveticaBold){
		Paragraph paragraph = new Paragraph(text, this.getFont(helveticaBold)); 
		return paragraph;
	}
	
	private Paragraph getCell(String text){
		Paragraph paragraph = new Paragraph(text, this.getFont()); 
		return paragraph;
	}
	
	
	private ByteArrayOutputStream exportAsPdf(Experiment3VO experiment, List<Buffer3VO> buffers, Proposal3VO proposal) throws Exception {
		Document document = new Document(PageSize.A4.rotate(), 10, 10, 20, 20);
	
		document.addTitle("exportSamplesView");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, baos);
		HeaderFooter header  = new HeaderFooter(new Phrase(proposal.getTitle() + " " + proposal.getCode() + proposal.getNumber()), false);

		header.setAlignment(Element.ALIGN_CENTER);
		header.getBefore().getFont().setSize(8);
		HeaderFooter footer = new HeaderFooter(new Phrase("Page n."), true);
		footer.setAlignment(Element.ALIGN_RIGHT);
		footer.setBorderWidth(1);
		footer.getBefore().getFont().setSize(6);

		document.setHeader(header);
		document.setFooter(footer);

		document.open();
		
		BaseFont bf = BaseFont.createFont(FONTS[0][0], FONTS[0][1], BaseFont.EMBEDDED);
		Font font = new Font(bf, 6);
		document.add(new Paragraph("Data Acquisition: " + experiment.getName(), font));
		document.add(new Paragraph("Type: " + experiment.getType(), font));
		document.add(new Paragraph("Date: " + experiment.getCreationDate(), font));
		document.add(new Paragraph("Proposal: " + proposal.getCode() + proposal.getNumber(), font));
		document.add(new Paragraph("Title: " + proposal.getTitle() , font));
		
		document.add(new Paragraph(" "));
		document.add(new Paragraph("Measurements", PdfRtfExporter.FONT_DOC_BOLD));
		document.add(new Paragraph(" "));
		document.add(this.getMeasurementTable(experiment, buffers));
		document.newPage();
		document.add(new Paragraph(" "));
		document.add(new Paragraph("Analysis", PdfRtfExporter.FONT_DOC_BOLD));
		document.add(new Paragraph(" "));
		document.add(this.getAnalysis(experiment, buffers));
		document.newPage();
		document.add(this.getImageTable(experiment, buffers));
		
		
		document.close();
		return baos;
	}
	
	
	/**
	 * @param experiment
	 * @return
	 */
	private Element getMeasurementTable(Experiment3VO experiment, List<Buffer3VO> buffers) {
		
		PdfPTable table = new PdfPTable(MEASUREMENT_COLUMNS.length);

		table.setWidthPercentage(100f);
        table.getDefaultCell().setPadding(3);
        table.getDefaultCell().setColspan(MEASUREMENT_COLUMNS.length);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setColspan(1);
        table.getDefaultCell().setBackgroundColor(PdfRtfExporter.BLUE_COLOR);
        for (int i = 0; i < MEASUREMENT_COLUMNS.length; i++) {
            table.addCell(this.getCell(MEASUREMENT_COLUMNS[i], BaseFont.HELVETICA_BOLD));
        }
        table.getDefaultCell().setBackgroundColor(null);
        
        List<List<String>> data = this.getExperimentMesurementData( experiment, buffers);
        
        for (List<String> list : data) {
			for (String string : list) {
				table.addCell(getCell(string));
			}
		}
		return table;
	}
	
	
	private Element getImageTable(Experiment3VO experiment, List<Buffer3VO> buffers) {
		
		PdfPTable table = new PdfPTable(ImageColumns.length);

		table.setWidthPercentage(100f);
        table.getDefaultCell().setPadding(3);
        table.getDefaultCell().setColspan(ImageColumns.length);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setColspan(1);
        table.getDefaultCell().setBackgroundColor(PdfRtfExporter.BLUE_COLOR);
        for (int i = 0; i < ImageColumns.length; i++) {
            table.addCell(this.getCell(ImageColumns[i], BaseFont.HELVETICA_BOLD));
        }
        table.getDefaultCell().setBackgroundColor(null);
        
		List<Measurement3VO> measurements = experiment.getMeasurements();
		Collections.sort(measurements, MeasurementComparator.compare(MeasurementComparator.getComparator(MeasurementComparator.PRIOTIRY_SORT_ASC)));
		for (int x = 0; x < measurements.size(); x++) {
			Measurement3VO measurement = measurements.get(x);
			Specimen3VO specimen = experiment.getSampleById(measurement.getSpecimenId());
			if (specimen.getMacromolecule3VO() != null){
				SaxsDataCollection3VO dataCollection = experiment.getDataCollectionByMeasurementId(measurement.getMeasurementId());
				
				
				
				ArrayList<String> list = this.addAnalysisRow(measurement, experiment, buffers);
				if (list.size()> 0){
					int sizeTable = list.get(0).length();
					PdfPTable nested1 = new PdfPTable(sizeTable);
					nested1.getDefaultCell().setBorder(0);
			       for (int i = 0; i < list.size(); i++) {
			    	   if (i == 0){
			    		   nested1.getDefaultCell().setBackgroundColor(PdfRtfExporter.LIGHT_GREY_COLOR);
			    		   nested1.getDefaultCell().setBorder(0);
			    		   Paragraph cell = this.getCell(this.COLUMNS[i] + ": " + list.get(i));
			    		   nested1.addCell(cell);
			    	   }
			    	   else{
			    		   nested1.getDefaultCell().setBackgroundColor(null);
			    		   nested1.getDefaultCell().setBorder(0);
			    		   PdfPCell cell = new PdfPCell(this.getCell(this.COLUMNS[i] + ": " + list.get(i)));
			    		   cell.setBorder(0);
			    		   nested1.addCell(cell);
			    	   }
			       }
			        table.addCell(new PdfPCell(nested1));
				
		    	   
					if (dataCollection.getSubstraction3VOs() != null){
						if (dataCollection.getSubstraction3VOs().size() > 0){
							Subtraction3VO substraction = ((Subtraction3VO)(dataCollection.getSubstraction3VOs().toArray()[0]));
							
							String scatteringImage = BiosaxsActions.checkFilePathForDevelopment(substraction.getScatteringFilePath());
							if (scatteringImage != null){
								if (new File(scatteringImage).exists()){
									try {
										 PdfPCell cell = new PdfPCell(Image.getInstance(scatteringImage), true);
										 cell.setBorder(0);
										table.addCell(cell);
									} catch (Exception e) {
										 PdfPCell cell = new PdfPCell(this.getCell("Image not found"));
										 cell.setBorder(0);
										 table.addCell(cell );
										 e.printStackTrace();
									}
								}
								else{
									 PdfPCell cell = new PdfPCell(this.getCell("Image not found"));
									 cell.setBorder(0);
									 table.addCell(cell );
								}
							}
							else{
									 PdfPCell cell = new PdfPCell(this.getCell("Image not found"));
									 cell.setBorder(0);
									 table.addCell(cell );
							}
							
							String guinierImage = BiosaxsActions.checkFilePathForDevelopment(substraction.getGuinierFilePath());
							if (guinierImage != null){
								if (new File(guinierImage).exists()){
									try {
										 PdfPCell cell = new PdfPCell(Image.getInstance(guinierImage), true);
										 cell.setBorder(0);
										table.addCell(cell);
									} catch (Exception e) {
										 PdfPCell cell = new PdfPCell(this.getCell("Image not found"));
										 cell.setBorder(0);
										 table.addCell(cell );
											e.printStackTrace();
									}
								}
								else{
									 PdfPCell cell = new PdfPCell(this.getCell("Image not found"));
									 cell.setBorder(0);
									 table.addCell(cell );
								}
							}
							else{
								 PdfPCell cell = new PdfPCell(this.getCell("Image not found"));
								 cell.setBorder(0);
								 table.addCell(cell );
							}
							
							String kraktyImage = BiosaxsActions.checkFilePathForDevelopment(substraction.getKratkyFilePath());
							if (kraktyImage != null){
								if (new File(kraktyImage).exists()){
									try {
										 PdfPCell cell = new PdfPCell(Image.getInstance(kraktyImage), true);
										 cell.setBorder(0);
										 table.addCell(cell);
									} catch (Exception e) {
										 PdfPCell cell = new PdfPCell(this.getCell("Image not found"));
										 cell.setBorder(0);
										 table.addCell(cell );
											e.printStackTrace();
									}
								}
								else{
									 PdfPCell cell = new PdfPCell(this.getCell("Image not found"));
									 cell.setBorder(0);
									 table.addCell(cell );
								}
							}
							else{
								 PdfPCell cell = new PdfPCell(this.getCell("Image not found"));
								 cell.setBorder(0);
								 table.addCell(cell );
							}
							
							String gnomImage = BiosaxsActions.checkFilePathForDevelopment(substraction.getGnomFilePath());
							if (gnomImage != null){
								if (new File(gnomImage).exists()){
									try {
										 PdfPCell cell = new PdfPCell(Image.getInstance(gnomImage), true);
										 cell.setBorder(0);
										table.addCell(cell);
									} catch (Exception e) {
										 PdfPCell cell = new PdfPCell(this.getCell("Image not found"));
										 cell.setBorder(0);
										 table.addCell(cell );
										e.printStackTrace();
									}
								}
								else{
									 PdfPCell cell = new PdfPCell(this.getCell("Image not found"));
									 cell.setBorder(0);
									 table.addCell(cell );
								}
							}
							else{
								 PdfPCell cell = new PdfPCell(this.getCell("Image not found"));
								 cell.setBorder(0);
								 table.addCell(cell );
							}
						}
					}
				}
			}
		}
		return table;
	}

	
	private Element getAnalysis(Experiment3VO experiment, List<Buffer3VO> buffers) {
		PdfPTable table = new PdfPTable(COLUMNS.length);
	      
        /** SUBTITLE **/
        table.getDefaultCell().setColspan(5);
        table.getDefaultCell().setBackgroundColor(PdfRtfExporter.BLUE_COLOR);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(this.getCell("Sample", BaseFont.HELVETICA_BOLD));
        
        table.getDefaultCell().setColspan(5);
        table.getDefaultCell().setBackgroundColor(PdfRtfExporter.BLUE_COLOR);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(this.getCell("Guinier", BaseFont.HELVETICA_BOLD));
        
        table.getDefaultCell().setColspan(3);
        table.getDefaultCell().setBackgroundColor(PdfRtfExporter.BLUE_COLOR);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(this.getCell("Gnom", BaseFont.HELVETICA_BOLD));
        
        table.getDefaultCell().setColspan(2);
        table.getDefaultCell().setBackgroundColor(PdfRtfExporter.BLUE_COLOR);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(this.getCell("Porod", BaseFont.HELVETICA_BOLD));
		table.setWidthPercentage(100f);
        table.getDefaultCell().setPadding(3);
        table.getDefaultCell().setColspan(COLUMNS.length);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setColspan(1);
        table.getDefaultCell().setBackgroundColor(PdfRtfExporter.BLUE_COLOR);
        for (int i = 0; i < COLUMNS.length; i++) {
            table.addCell(this.getCell(COLUMNS[i], BaseFont.HELVETICA_BOLD));
        }
        table.getDefaultCell().setBackgroundColor(null);
        
        List<List<String>> data = this.getExperimentAnalysisData(experiment, buffers);
        
        for (List<String> list : data) {
			for (String string : list) {
				table.addCell(getCell(string));
			}
		}
		return table;
	}

	@Override
	public ByteArrayOutputStream run() throws Exception {
		return this.exportAsPdf(experiment, buffers, proposal);
	}


}
