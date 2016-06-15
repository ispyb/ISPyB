/*************************************************************************************************
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
 ****************************************************************************************************/
package ispyb.server.mx.vos.collections;


import java.io.Serializable;

/**
 * xds information to generate XDS.inp & mosflm
 * @author BODIN
 *
 */
public class XDSInfo implements Serializable, Cloneable {

	private static final long serialVersionUID = -5174839165591105266L;

	
	/*beamlineSetup*/
	private Double polarisation;
	
	/*dataCollection*/
	private Double axisRange;
	private Double axisStart;
	private Double axisEnd;
	private Double detectorDistance;
	private String fileTemplate;
	private String imageDirectory;
	private String imageSuffix;
	private Integer numberOfImages;
	private Integer startImageNumber;
	private Double phiStart;
	private Double kappaStart;
	private Double wavelength;
	private Double xbeam;
	private Double ybeam;
	
	/*detector*/
	private Double detectorPixelSizeHorizontal;
	private Double detectorPixelSizeVertical;
	private String detectorManufacturer;
	private String detectorModel;
		
	public XDSInfo() {
		super();
	}
	
	public XDSInfo(Double polarisation, Double axisRange, Double axisStart,
			Double axisEnd, Double detectorDistance, String fileTemplate,
			String imageDirectory, String imageSuffix, Integer numberOfImages,
			Integer startImageNumber, Double phiStart, Double kappaStart,
			Double wavelength, Double xbeam, Double ybeam,
			Double detectorPixelSizeHorizontal,
			Double detectorPixelSizeVertical, String detectorManufacturer,
			String detectorModel) {
		super();
		this.polarisation = polarisation;
		this.axisRange = axisRange;
		this.axisStart = axisStart;
		this.axisEnd = axisEnd;
		this.detectorDistance = detectorDistance;
		this.fileTemplate = fileTemplate;
		this.imageDirectory = imageDirectory;
		this.imageSuffix = imageSuffix;
		this.numberOfImages = numberOfImages;
		this.startImageNumber = startImageNumber;
		this.phiStart = phiStart;
		this.kappaStart = kappaStart;
		this.wavelength = wavelength;
		this.xbeam = xbeam;
		this.ybeam = ybeam;
		this.detectorPixelSizeHorizontal = detectorPixelSizeHorizontal;
		this.detectorPixelSizeVertical = detectorPixelSizeVertical;
		this.detectorManufacturer = detectorManufacturer;
		this.detectorModel = detectorModel;
	}

	public Double getPolarisation() {
		return polarisation;
	}

	public void setPolarisation(Double polarisation) {
		this.polarisation = polarisation;
	}

	public Double getAxisRange() {
		return axisRange;
	}

	public void setAxisRange(Double axisRange) {
		this.axisRange = axisRange;
	}

	public Double getAxisStart() {
		return axisStart;
	}

	public void setAxisStart(Double axisStart) {
		this.axisStart = axisStart;
	}

	public Double getAxisEnd() {
		return axisEnd;
	}

	public void setAxisEnd(Double axisEnd) {
		this.axisEnd = axisEnd;
	}

	public Double getDetectorDistance() {
		return detectorDistance;
	}

	public void setDetectorDistance(Double detectorDistance) {
		this.detectorDistance = detectorDistance;
	}

	public String getFileTemplate() {
		return fileTemplate;
	}

	public void setFileTemplate(String fileTemplate) {
		this.fileTemplate = fileTemplate;
	}

	public String getImageDirectory() {
		return imageDirectory;
	}

	public void setImageDirectory(String imageDirectory) {
		this.imageDirectory = imageDirectory;
	}

	public String getImageSuffix() {
		return imageSuffix;
	}

	public void setImageSuffix(String imageSuffix) {
		this.imageSuffix = imageSuffix;
	}

	public Integer getNumberOfImages() {
		return numberOfImages;
	}

	public void setNumberOfImages(Integer numberOfImages) {
		this.numberOfImages = numberOfImages;
	}

	public Integer getStartImageNumber() {
		return startImageNumber;
	}

	public void setStartImageNumber(Integer startImageNumber) {
		this.startImageNumber = startImageNumber;
	}

	public Double getPhiStart() {
		return phiStart;
	}

	public void setPhiStart(Double phiStart) {
		this.phiStart = phiStart;
	}

	public Double getKappaStart() {
		return kappaStart;
	}

	public void setKappaStart(Double kappaStart) {
		this.kappaStart = kappaStart;
	}

	public Double getWavelength() {
		return wavelength;
	}

	public void setWavelength(Double wavelength) {
		this.wavelength = wavelength;
	}

	public Double getXbeam() {
		return xbeam;
	}

	public void setXbeam(Double xbeam) {
		this.xbeam = xbeam;
	}

	public Double getYbeam() {
		return ybeam;
	}

	public void setYbeam(Double ybeam) {
		this.ybeam = ybeam;
	}

	public Double getDetectorPixelSizeHorizontal() {
		return detectorPixelSizeHorizontal;
	}

	public void setDetectorPixelSizeHorizontal(Double detectorPixelSizeHorizontal) {
		this.detectorPixelSizeHorizontal = detectorPixelSizeHorizontal;
	}

	public Double getDetectorPixelSizeVertical() {
		return detectorPixelSizeVertical;
	}

	public void setDetectorPixelSizeVertical(Double detectorPixelSizeVertical) {
		this.detectorPixelSizeVertical = detectorPixelSizeVertical;
	}

	public String getDetectorManufacturer() {
		return detectorManufacturer;
	}

	public void setDetectorManufacturer(String detectorManufacturer) {
		this.detectorManufacturer = detectorManufacturer;
	}

	public String getDetectorModel() {
		return detectorModel;
	}

	public void setDetectorModel(String detectorModel) {
		this.detectorModel = detectorModel;
	}
}
