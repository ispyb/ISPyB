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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import ispyb.common.util.StringUtils;
import ispyb.server.common.vos.ISPyBValueObject;

import org.apache.log4j.Logger;

/**
 * Detector3 value object mapping table Detector
 * 
 */
@Entity
@Table(name = "Detector")
public class Detector3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(Detector3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "detectorId")
	protected Integer detectorId;

	@Column(name = "detectorType")
	protected String detectorType;
	
	@Column(name = "detectorManufacturer")
	protected String detectorManufacturer;
	
	@Column(name = "detectorModel")
	protected String detectorModel;
	
	@Column(name = "detectorPixelSizeHorizontal")
	protected Double detectorPixelSizeHorizontal;
	
	@Column(name = "detectorPixelSizeVertical")
	protected Double detectorPixelSizeVertical;
	
	@Column(name = "detectorSerialNumber")
	protected String detectorSerialNumber;
	
	@Column(name = "detectorDistanceMax")
	protected Double detectorDistanceMax;
	
	@Column(name = "detectorDistanceMin")
	protected Double detectorDistanceMin;
	
	@Column(name = "trustedPixelValueRangeLower")
	protected Double trustedPixelValueRangeLower;
	
	@Column(name = "trustedPixelValueRangeUpper")
	protected Double trustedPixelValueRangeUpper;
	
	@Column(name = "sensorThickness")
	protected Double sensorThickness;
	
	@Column(name = "overload")
	protected Double overload;
	
	@Column(name = "XGeoCorr")
	protected String xGeoCorr;
	
	@Column(name = "YGeoCorr")
	protected String yGeoCorr;
	
	@Column(name = "detectorMode")
	protected String detectorMode;
	

	public Detector3VO() {
		super();
	}
	
	public Detector3VO(Detector3VO vo){
		super();
		this.detectorId = vo.getDetectorId();
		this.detectorType = vo.getDetectorType();
		this.detectorManufacturer = vo.getDetectorManufacturer();
		this.detectorModel = vo.getDetectorModel();
		this.detectorPixelSizeHorizontal = vo.getDetectorPixelSizeHorizontal();
		this.detectorPixelSizeVertical = vo.getDetectorPixelSizeVertical();
		this.detectorSerialNumber = vo.getDetectorSerialNumber();
		this.detectorDistanceMax = vo.getDetectorDistanceMax();
		this.detectorDistanceMin = vo.getDetectorDistanceMin();
		this.trustedPixelValueRangeLower = vo.getTrustedPixelValueRangeLower();
		this.trustedPixelValueRangeUpper = vo.getTrustedPixelValueRangeUpper();
		this.sensorThickness = vo.getSensorThickness();
		this.overload = vo.getOverload();
		this.xGeoCorr = vo.getxGeoCorr();
		this.yGeoCorr = vo.getyGeoCorr();
		this.detectorMode = vo.getDetectorMode();
	}

	public Detector3VO(Integer detectorId, String detectorType,
			String detectorManufacturer, String detectorModel,
			Double detectorPixelSizeHorizontal,
			Double detectorPixelSizeVertical, String detectorSerialNumber,
			Double detectorDistanceMax, Double detectorDistanceMin,
			Double trustedPixelValueRangeLower,
			Double trustedPixelValueRangeUpper, Double sensorThickness,
			Double overload, String xGeoCorr, String yGeoCorr, String detectorMode) {
		super();
		this.detectorId = detectorId;
		this.detectorType = detectorType;
		this.detectorManufacturer = detectorManufacturer;
		this.detectorModel = detectorModel;
		this.detectorPixelSizeHorizontal = detectorPixelSizeHorizontal;
		this.detectorPixelSizeVertical = detectorPixelSizeVertical;
		this.detectorSerialNumber = detectorSerialNumber;
		this.detectorDistanceMax = detectorDistanceMax;
		this.detectorDistanceMin = detectorDistanceMin;
		this.trustedPixelValueRangeLower = trustedPixelValueRangeLower;
		this.trustedPixelValueRangeUpper = trustedPixelValueRangeUpper;
		this.sensorThickness = sensorThickness;
		this.overload = overload;
		this.xGeoCorr = xGeoCorr;
		this.yGeoCorr = yGeoCorr;
		this.detectorMode = detectorMode;
	}



	public Integer getDetectorId() {
		return detectorId;
	}

	public void setDetectorId(Integer detectorId) {
		this.detectorId = detectorId;
	}

	public String getDetectorType() {
		return detectorType;
	}

	public void setDetectorType(String detectorType) {
		this.detectorType = detectorType;
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

	public String getDetectorSerialNumber() {
		return detectorSerialNumber;
	}

	public void setDetectorSerialNumber(String detectorSerialNumber) {
		this.detectorSerialNumber = detectorSerialNumber;
	}

	public Double getDetectorDistanceMax() {
		return detectorDistanceMax;
	}

	public void setDetectorDistanceMax(Double detectorDistanceMax) {
		this.detectorDistanceMax = detectorDistanceMax;
	}

	public Double getDetectorDistanceMin() {
		return detectorDistanceMin;
	}

	public void setDetectorDistanceMin(Double detectorDistanceMin) {
		this.detectorDistanceMin = detectorDistanceMin;
	}

	public Double getTrustedPixelValueRangeLower() {
		return trustedPixelValueRangeLower;
	}

	public void setTrustedPixelValueRangeLower(Double trustedPixelValueRangeLower) {
		this.trustedPixelValueRangeLower = trustedPixelValueRangeLower;
	}

	public Double getTrustedPixelValueRangeUpper() {
		return trustedPixelValueRangeUpper;
	}

	public void setTrustedPixelValueRangeUpper(Double trustedPixelValueRangeUpper) {
		this.trustedPixelValueRangeUpper = trustedPixelValueRangeUpper;
	}

	public Double getSensorThickness() {
		return sensorThickness;
	}

	public void setSensorThickness(Double sensorThickness) {
		this.sensorThickness = sensorThickness;
	}

	public Double getOverload() {
		return overload;
	}

	public void setOverload(Double overload) {
		this.overload = overload;
	}

	public String getxGeoCorr() {
		return xGeoCorr;
	}

	public void setxGeoCorr(String xGeoCorr) {
		this.xGeoCorr = xGeoCorr;
	}

	public String getyGeoCorr() {
		return yGeoCorr;
	}

	public void setyGeoCorr(String yGeoCorr) {
		this.yGeoCorr = yGeoCorr;
	}
	

	public String getDetectorMode() {
		return detectorMode;
	}

	public void setDetectorMode(String detectorMode) {
		this.detectorMode = detectorMode;
	}

	/**
	 * Checks the values of this value object for correctness and
	 * completeness. Should be done before persisting the data in the DB.
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @throws Exception if the data of the value object is not correct
	 */
	public void checkValues(boolean create) throws Exception {
		int maxLengthDetectorType = 255;
		int maxLengthDetectorManufacturer = 255;
		int maxLengthDetectorModel = 255;
		int maxLengthXGeoCorr = 255;
		int maxLengthYGeoCorr = 255;
		int maxLengthDetectorMode = 255;
		
		// detectorType
		if(!StringUtils.isStringLengthValid(this.detectorType, maxLengthDetectorType))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("Detector", "detectorType", maxLengthDetectorType));
		// detectorManufacturer
		if(!StringUtils.isStringLengthValid(this.detectorManufacturer, maxLengthDetectorManufacturer))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("Detector", "detectorManufacturer", maxLengthDetectorManufacturer));
		// detectorModel
		if(!StringUtils.isStringLengthValid(this.detectorModel, maxLengthDetectorModel))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("Detector", "detectorModel", maxLengthDetectorModel));
		// xGeoCorr
		if(!StringUtils.isStringLengthValid(this.xGeoCorr, maxLengthXGeoCorr))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("Detector", "xGeoCorr", maxLengthXGeoCorr));
		// yGeoCorr
		if(!StringUtils.isStringLengthValid(this.yGeoCorr, maxLengthYGeoCorr))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("Detector", "yGeoCorr", maxLengthYGeoCorr));
		//detectorMode
		if(!StringUtils.isStringLengthValid(this.detectorMode, maxLengthDetectorMode))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("Detector", "detectorMode", maxLengthDetectorMode));
		
		
	}

	/**
	 * used to clone an entity to set the linked collectio9ns to null, for webservices
	 */
	@Override
	public Detector3VO clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (Detector3VO) super.clone();
	}
	
	public String toWSString(){
		String s = "detectorId="+this.detectorId +", "+
		"detectorType="+this.detectorType+", "+
		"detectorMode="+this.detectorMode+", "+
		"detectorManufacturer="+this.detectorManufacturer+", "+
		"detectorModel="+this.detectorModel+", "+
		"detectorPixelSizeHorizontal="+this.detectorPixelSizeHorizontal+", "+
		"detectorPixelSizeVertical="+this.detectorPixelSizeVertical+", "+
		"detectorSerialNumber="+this.detectorSerialNumber+", "+
		"detectorDistanceMax="+this.detectorDistanceMax+", "+
		"detectorDistanceMin="+this.detectorDistanceMin+", "+
		"trustedPixelValueRangeLower="+this.trustedPixelValueRangeLower+", "+
		"trustedPixelValueRangeUpper="+this.trustedPixelValueRangeUpper+", "+
		"sensorThickness="+this.sensorThickness+", "+
		"overload="+this.overload+", "+
		"xGeoCorr="+this.xGeoCorr+", "+
		"yGeoCorr="+this.yGeoCorr;
		
		return s;
	}
	
}
