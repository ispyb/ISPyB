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

import ispyb.common.util.PathUtils;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.screening.ScreeningStrategy3VO;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class ScreeningStrategyValueInfo extends ScreeningStrategy3VO {
	private static final long serialVersionUID = 0;

	private Integer totNbImages = null;

	private Double totExposureTime = null;

	private boolean programLogFileExists = true;

	private String programLog = null;

	public ScreeningStrategyValueInfo(ScreeningStrategy3VO screeningStrategyValue) {
		super(screeningStrategyValue);
		try {
			NumberFormat nf1 = NumberFormat.getIntegerInstance();
			DecimalFormat df1 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
			df1.applyPattern("#####0.0");
			DecimalFormat df2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
			df2.applyPattern("#####0.00");
			DecimalFormat df3 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
			df3.applyPattern("#####0.000");

			Double nbImagesdb = null;
			Double rotation = this.getRotation();
			Double phiS = this.getPhiStart();
			Double phiE = this.getPhiEnd();
			Double exposureT = this.getExposureTime();

			// calculate total number of images
			if (phiE != null && phiS != null && rotation != null && rotation.compareTo(new Double(0)) != 0) {
				nbImagesdb = (phiE - phiS) / rotation;
				this.totNbImages = new Integer((int) Math.round(nbImagesdb));
			}

			// Format data
			Double phiSformat = null;
			Double phiEformat = null;
			Double rotationf = null;
			Double exposureTf = null;
			Double resolutionf = null;
			Double completenessf = null;
			Double multiplicityf = null;
			Double totExposureTimef = null;

			if (phiS != null)
				phiSformat = new Double(df1.format(phiS));
			if (phiE != null)
				phiEformat = new Double(df1.format(phiE));
			if (rotation != null)
				rotationf = new Double(df2.format(rotation));
			if (exposureT != null) {
				exposureTf = new Double(df3.format(exposureT));
				// calculate total exposure time
				totExposureTimef = new Double(df2.format(nbImagesdb * exposureT));
			}
			if (this.getResolution() != null)
				resolutionf = new Double(df2.format(this.getResolution()));
			if (this.getCompleteness() != null)
				completenessf = new Double(df1.format(this.getCompleteness().doubleValue() * 100));
			if (this.getMultiplicity() != null)
				multiplicityf = new Double(df1.format(this.getMultiplicity()));
			if (totExposureTime != null)
				totExposureTimef = new Double(df2.format(totExposureTime));

			this.setPhiStart(phiSformat);
			this.setPhiEnd(phiEformat);
			this.setRotation(rotationf);
			this.setExposureTime(exposureTf);
			this.setResolution(resolutionf);
			this.setCompleteness(completenessf);
			this.setMultiplicity(multiplicityf);
			this.setTotExposureTime(totExposureTimef);
			this.setTotNbImages(totNbImages);

		} catch (Exception e) {
		}
	}

	// _________________________________________________________________________________________________________

	public boolean isProgramLogFileExists() {
		return programLogFileExists;
	}

	public void setProgramLogFileExists(boolean programLogFileExists) {
		this.programLogFileExists = programLogFileExists;
	}

	public Integer getTotNbImages() {
		return totNbImages;
	}

	public void setTotNbImages(Integer totNbImages) {
		this.totNbImages = totNbImages;
	}

	public Double getTotExposureTime() {
		return totExposureTime;
	}

	public void setTotExposureTime(Double totExposureTime) {
		this.totExposureTime = totExposureTime;
	}

	public String getProgramLog() {
		return programLog;
	}

	public void setProgramLog(String programLog) {
		this.programLog = programLog;
	}

	/**
	 * Set the program log file path.
	 * 
	 * @param the
	 *            datacollectionId
	 */
	public void setProgramLog(DataCollection3VO dataCollectionVO) {

		try {
			String programLogPath = PathUtils.GetFullLogPath(dataCollectionVO);
			String programLogFilePath = programLogPath + "best.log";

			boolean logFilePresent = (new File(programLogFilePath)).exists();
			this.setProgramLogFileExists(logFilePresent);

			if (logFilePresent)
				this.programLog = programLogFilePath;

		} catch (Exception e) {
		}

	}

}
