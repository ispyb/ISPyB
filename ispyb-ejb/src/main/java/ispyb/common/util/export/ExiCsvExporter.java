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

import ispyb.common.util.Constants;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.autoproc.SpaceGroup3Service;
import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.server.mx.services.sample.BLSample3Service;
import ispyb.server.mx.vos.autoproc.SpaceGroup3VO;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

/**
 * allows creation of CSV report - general report for EXI, available in the
 * 
 * @author Solange Delageniere
 * 
 */
public class ExiCsvExporter {

	private final static Logger LOG = Logger.getLogger(ExiCsvExporter.class);

	//proposalId
	int proposalId;

	// proposal code
	String proposalCode;

	// session info
	Integer sessionId;

	private Session3VO slv;

	List<DataCollection3VO> dataCollections = new ArrayList<>();

	DecimalFormat df1;

	DecimalFormat df2;

	DecimalFormat df3;

	DecimalFormat df4;

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Session3Service sessionService;

	private DataCollection3Service dataCollectionService;

	private BLSample3Service sampleService;

	private DataCollection3Service dcService;

	private SpaceGroup3Service spacegroupService;


	public ExiCsvExporter(int proposalId, String proposalCode, Integer sessionId,
						  List<DataCollection3VO> dataCollections) throws Exception {
		this.proposalCode = proposalCode;
		this.sessionId = sessionId;
		this.dataCollections = dataCollections;
		init();
	}

	private void init() throws Exception {
		df1 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df1.applyPattern("#####0.0");
		df2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df2.applyPattern("#####0.00");
		df3 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df3.applyPattern("#####0.000");
		df4 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df4.applyPattern("#####0.0000");

		sessionService = (Session3Service) ejb3ServiceLocator
				.getLocalService(Session3Service.class);
		dcService = (DataCollection3Service) ejb3ServiceLocator
				.getLocalService(DataCollection3Service.class);
		spacegroupService = (SpaceGroup3Service) ejb3ServiceLocator
				.getLocalService(SpaceGroup3Service.class);

		dataCollectionService = (DataCollection3Service) ejb3ServiceLocator.getLocalService(DataCollection3Service.class);
		sampleService = (BLSample3Service) ejb3ServiceLocator.getLocalService(BLSample3Service.class);
		
		slv = sessionService.findByPk(sessionId, false/*withDataCollectionGroup*/, false/*withEnergyScan*/, false/*withXFESpectrum*/);
	}

	/**
	 * export datacollection report
	 *
	 * @return
	 * @throws Exception
	 */
	public ByteArrayOutputStream exportDataCollectionReport() throws Exception {
		
		this.init();
		String doc = "";
		// create simple doc and write to a ByteArrayOutputStream
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final String CSV_DELIMITER = ",";

		if (this.dataCollections.isEmpty()) {
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
		if (this.proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX))
			doc += "Crystal class,";
		doc += "Comments";
		doc += "\n";

		// Data
		DecimalFormat df2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df2.applyPattern("#####0.00");
		DecimalFormat df3 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df3.applyPattern("#####0.000");


		Iterator<DataCollection3VO> it = this.dataCollections.iterator();
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
			if (this.proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX)) {
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


	
	private String getDecimalFormat(String value, DecimalFormat df) {
		String ret = "";
		if (value == null || value.isEmpty()) 
			return ret;
		try {
			Double paramValue = new Double(value);
			ret = df.format(paramValue);
		} catch (NumberFormatException e) {
			return "";
		}
		
		return ret;
	}
}
