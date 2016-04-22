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
/*
 * CsvExporter.java
 * 
 * Created on April 02, 2009
 *
 * ESRF - The European Synchrotron Radiation Facility
 * 6 RUE JULES HOROWITZ,
 * BP 220, 38043 GRENOBLE CEDEX 9, FRANCE
 * Tel +33 (0)4 76 88 20 00 - Fax +33 (0)4 76 88 20 20
 */
package ispyb.client.mx.collection;

import ispyb.common.util.Constants;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.server.mx.services.sample.BLSample3Service;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
import ispyb.server.mx.vos.collections.Session3VO;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * 
 * 
 * @author patrice.brenchereau@esrf.fr
 * 
 *         April 02, 2009
 * 
 */
public class CsvExporter {

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
	private Session3Service sessionService;
	private DataCollection3Service dataCollectionService;
	private BLSample3Service sampleService;
	
	List<DataCollection3VO> aList;

	Session3VO slv;

	String name;

	String proposalCode;

	String proposalNumber;

	/**
	 * 
	 * @param aList
	 * @param slv2
	 * @param name
	 * @param proposalCode
	 * @param proposalNumber
	 */
	public CsvExporter(List<DataCollection3VO> aList, Session3VO slv, String name, String proposalCode, String proposalNumber) {
		super();
		this.slv = slv;
		this.name = name;
		this.aList = aList;
		this.proposalCode = proposalCode;
		this.proposalNumber = proposalNumber;

	}

	/**
	 * Exports the file for UserOnly and Industrial
	 * 
	 * @return
	 * @throws Exception
	 */
	public ByteArrayOutputStream exportAsCsvUserOnly() throws Exception {

		final String CSV_DELIMITER = ",";

		String doc = "";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		if (aList.isEmpty()) {
			doc += "There is no data collection in this report";
			baos.write(doc.getBytes());
			return baos;
		}

		
		// Header
		doc += "Image prefix,";
		doc += "Beamline,";
		doc += "Run no,";
		doc += "Start Time,";
		doc += "Sample Name,";
		doc += "Protein Acronym,";
		doc += "# images,";
		doc += "Wavelength (angstrom),";
		doc += "Distance (mm),";
		doc += "Exp. Time (sec),";
		doc += "Phi start (deg),";
		doc += "Phi range (deg),";
		doc += "Xbeam (mm),";
		doc += "Ybeam (mm),";
		doc += "Detector resol. (angstrom),";
		if (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX))
			doc += "Crystal class,";
		doc += "Comments";
		doc += "\n";

		// Data
		DecimalFormat df2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df2.applyPattern("#####0.00");
		DecimalFormat df3 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df3.applyPattern("#####0.000");

		this.sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
		this.dataCollectionService = (DataCollection3Service) ejb3ServiceLocator.getLocalService(DataCollection3Service.class);
		this.sampleService = (BLSample3Service) ejb3ServiceLocator.getLocalService(BLSample3Service.class);
		Iterator<DataCollection3VO> it = aList.iterator();
		int i = 1;
		while (it.hasNext()) {

			DataCollection3VO col = (DataCollection3VO) it.next();
			DataCollectionGroup3VO dcGroup = col.getDataCollectionGroupVO();
			Session3VO slv = sessionService.findByPk(dcGroup.getSessionVOId(), false, false, false);
			col = dataCollectionService.loadEager(col);
			if(dcGroup.getBlSampleVO() != null){
				dcGroup.setBlSampleVO(sampleService.loadEager(dcGroup.getBlSampleVO()));
			}

			// ImagePrefix
			if (col.getImagePrefix() != null)
				doc += col.getImagePrefix() + CSV_DELIMITER;
			else
				doc += CSV_DELIMITER;

			// Beamline
			if (slv.getBeamlineName() != null)
				doc += slv.getBeamlineName() + CSV_DELIMITER;
			else
				doc += CSV_DELIMITER;

			// Run number
			if (col.getDataCollectionNumber() != null)
				doc += col.getDataCollectionNumber().toString() + CSV_DELIMITER;
			else
				doc += CSV_DELIMITER;

			// Start Time
			if (col.getStartTime() != null) {
				SimpleDateFormat simple1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				String date = simple1.format(col.getStartTime());
				doc += date + CSV_DELIMITER;
			} else
				doc += CSV_DELIMITER;

			// Sample name
			String sampleName = null;
			if (dcGroup.getBlSampleVO() != null)
				//sampleName = samples.findByPrimaryKey(col.getBlSampleVO()).getName();
				sampleName = dcGroup.getBlSampleVO().getName();
			if (sampleName != null)
				doc += sampleName + CSV_DELIMITER;
			else
				doc += CSV_DELIMITER;

			// Protein acronym
			String proteinAcronym = null;
			if (dcGroup.getBlSampleVO() != null && dcGroup.getBlSampleVO().getCrystalVO() != null && dcGroup.getBlSampleVO().getCrystalVO().getProteinVO() != null)
				//proteinAcronym = samples.findByPrimaryKey(col.getBlSampleId()).getAcronym();
				proteinAcronym = dcGroup.getBlSampleVO().getCrystalVO().getProteinVO().getAcronym();
			if (proteinAcronym != null)
				doc += proteinAcronym + CSV_DELIMITER;
			else
				doc += CSV_DELIMITER;

			// Number of images
			if (col.getNumberOfImages() != null)
				doc += col.getNumberOfImages().toString() + CSV_DELIMITER;
			else
				doc += CSV_DELIMITER;

			// Wavelength
			if (col.getWavelength() != null)
				doc += df3.format(col.getWavelength()) + CSV_DELIMITER;
			else
				doc += CSV_DELIMITER;

			// Detector distance
			if (col.getDetectorDistance() != null)
				doc += df2.format(col.getDetectorDistance()) + CSV_DELIMITER;
			else
				doc += CSV_DELIMITER;

			// Exposure time
			if (col.getExposureTime() != null)
				doc += df3.format(col.getExposureTime()) + CSV_DELIMITER;
			else
				doc += CSV_DELIMITER;

			// Axis start
			if (col.getAxisStart() != null)
				doc += df2.format(col.getAxisStart()) + CSV_DELIMITER;
			else
				doc += CSV_DELIMITER;

			// Axis range
			if (col.getAxisRange() != null)
				doc += df2.format(col.getAxisRange()) + CSV_DELIMITER;
			else
				doc += CSV_DELIMITER;

			// Xbeam
			if (col.getXbeam() != null)
				doc += df2.format(col.getXbeam()) + CSV_DELIMITER;
			else
				doc += CSV_DELIMITER;

			// Ybeam
			if (col.getYbeam() != null)
				doc += df2.format(col.getYbeam()) + CSV_DELIMITER;
			else
				doc += CSV_DELIMITER;

			// Resolution
			if (col.getResolution() != null)
				doc += df2.format(col.getResolution()) + CSV_DELIMITER;
			else
				doc += CSV_DELIMITER;

			// SrystalClass (only for IFX proposal in case of MXPress experiment)
			if (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX)) {
				if (dcGroup.getCrystalClass() != null && dcGroup.getCrystalClass() != "")
					doc += dcGroup.getCrystalClass() + CSV_DELIMITER;
				else
					doc += CSV_DELIMITER;
			}

			// Comments
			if (col.getComments() != null && col.getComments() != "") {
				doc += "\"" + col.getComments().replaceAll("\"", "\"\"") + "\"";
			} else
				doc += "";

			i++;

			doc += "\n";
		}

		baos.write(doc.getBytes());
		return baos;
	}

}
