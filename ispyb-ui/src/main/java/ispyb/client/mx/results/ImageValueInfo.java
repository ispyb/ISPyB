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

import ispyb.server.mx.vos.collections.Image3VO;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class ImageValueInfo extends Image3VO {
	private static final long serialVersionUID = 1L;

	protected boolean imageFileExists = true;

	protected Integer previousImageId = null;

	protected Integer nextImageId = null;

	protected Integer currentImageId = null;

	protected Integer imageNumberInfo = null;

	protected boolean isFirst = false;

	protected boolean isLast = false;

	protected Integer synchrotronCurrentFormatted = null;

	protected Double measuredIntensityFormatted = null;

	protected Double cumulativeIntensityFormatted = null;

	protected Double temperatureFormatted = null;

	protected boolean isLastImageHorizontal = false;

	protected boolean isLastImageVertical = false;

	protected String imageWidth = "";

	public ImageValueInfo(Image3VO imageValue) {
		super(imageValue);
		try {
			boolean isWindows = (System.getProperty("os.name").indexOf("Win") != -1) ? true : false;
			String targetFileName = imageValue.getFileLocation() + File.separator + imageValue.getFileName();
			targetFileName = (isWindows) ? "C:" + targetFileName : targetFileName;
			File targetFile = new File(targetFileName);
			this.imageFileExists = targetFile.exists();
		} catch (Exception e) {
		}
	}

	// _________________________________________________________________________________________________________
	public boolean isImageFileExists() {
		return imageFileExists;
	}

	public void setImageFileExists(boolean imageFileExists) {
		this.imageFileExists = imageFileExists;
	}

	public Integer getNextImageId() {
		return nextImageId;
	}

	public void setNextImageId(Integer nextImageId) {
		this.nextImageId = nextImageId;
	}

	public Integer getPreviousImageId() {
		return previousImageId;
	}

	public void setPreviousImageId(Integer previousImageId) {
		this.previousImageId = previousImageId;
	}

	public Integer getCurrentImageId() {
		return currentImageId;
	}

	public void setCurrentImageId(Integer currentImageId) {
		this.currentImageId = currentImageId;
	}

	public Integer getImageNumberInfo() {
		return imageNumberInfo;
	}

	public void setImageNumberInfo(Integer imageNumberInfo) {
		this.imageNumberInfo = imageNumberInfo;
	}

	public boolean isFirst() {
		return isFirst;
	}

	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	public boolean isLast() {
		return isLast;
	}

	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}

	public Integer getSynchrotronCurrentFormatted() {
		return synchrotronCurrentFormatted;
	}

	public void setSynchrotronCurrentFormatted(Integer synchrotronCurrentFormatted) {
		this.synchrotronCurrentFormatted = synchrotronCurrentFormatted;
	}

	public Double getMeasuredIntensityFormatted() {
		return measuredIntensityFormatted;
	}

	public void setMeasuredIntensityFormatted(Double measuredIntensityFormatted) {
		this.measuredIntensityFormatted = measuredIntensityFormatted;
	}

	public Double getCumulativeIntensityFormatted() {
		return cumulativeIntensityFormatted;
	}

	public void setCumulativeIntensityFormatted(Double cumulativeIntensityFormatted) {
		this.cumulativeIntensityFormatted = cumulativeIntensityFormatted;
	}

	public Double getTemperatureFormatted() {
		return temperatureFormatted;
	}

	public void setTemperatureFormatted(Double temperatureFormatted) {
		this.temperatureFormatted = temperatureFormatted;
	}

	public boolean isLastImageHorizontal() {
		return isLastImageHorizontal;
	}

	public void setLastImageHorizontal(boolean isLastImageHorizontal) {
		this.isLastImageHorizontal = isLastImageHorizontal;
	}

	public boolean isLastImageVertical() {
		return isLastImageVertical;
	}

	public void setLastImageVertical(boolean isLastImageVertical) {
		this.isLastImageVertical = isLastImageVertical;
	}

	public String getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(String imageWidth) {
		this.imageWidth = imageWidth;
	}

	/**
	 * @returns the formatted image Data.
	 */
	public void setFormattedData() {

		DecimalFormat df2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df2.applyPattern("####");
		DecimalFormat df1 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df1.applyPattern("#####0.0");
		DecimalFormat dfs = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		dfs.applyPattern("#.###E0");

		Double temperature = null;
		Double measuredIntensity = null;
		Double cumulativeIntensity = null;
		Integer synchrotronCurrent = null;
		if (this.getTemperature() != null)
			temperature = new Double(df1.format(this.getTemperature()));
		if (this.getMeasuredIntensity() != null)
			measuredIntensity = new Double(dfs.format(this.getMeasuredIntensity()));
		if (this.getCumulativeIntensity() != null)
			cumulativeIntensity = new Double(dfs.format(this.getCumulativeIntensity()));
		if (this.getSynchrotronCurrent() != null)
			synchrotronCurrent = new Integer(df2.format(this.getSynchrotronCurrent()));

		this.synchrotronCurrentFormatted = synchrotronCurrent;
		this.cumulativeIntensityFormatted = cumulativeIntensity;
		this.measuredIntensityFormatted = measuredIntensity;
		this.temperatureFormatted = temperature;
	}

}
