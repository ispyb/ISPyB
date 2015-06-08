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

package ispyb.server.mx.vos.screening;

import ispyb.server.common.vos.ISPyBValueObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * ScreeningStrategySubWedge3 value object mapping table ScreeningStrategySubWedge
 * 
 */
@Entity
@Table(name = "ScreeningStrategySubWedge")
public class ScreeningStrategySubWedge3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(ScreeningStrategySubWedge3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "screeningStrategySubWedgeId")
	protected Integer screeningStrategySubWedgeId;
	
	@ManyToOne
	@JoinColumn(name = "screeningStrategyWedgeId")
	private ScreeningStrategyWedge3VO  screeningStrategyWedgeVO;
	
	@Column(name = "subWedgeNumber")
	protected Integer subWedgeNumber;
	
	@Column(name = "rotationAxis")
	protected String rotationAxis;
	
	@Column(name = "axisStart")
	protected Double axisStart;
	
	@Column(name = "axisEnd")
	protected Double axisEnd;
	
	@Column(name = "exposureTime")
	protected Double exposureTime;
	
	@Column(name = "transmission")
	protected Double transmission;
	
	@Column(name = "oscillationRange")
	protected Double oscillationRange;
	
	@Column(name = "completeness")
	protected Double completeness;
	
	@Column(name = "multiplicity")
	protected Double multiplicity;
	
	@Column(name = "doseTotal")
	protected Double doseTotal;
	
	@Column(name = "numberOfImages")
	protected Integer numberOfImages;

	@Column(name = "comments")
	protected String comments;
	
	public ScreeningStrategySubWedge3VO(){
		super();
	}

	
	
	public ScreeningStrategySubWedge3VO(Integer screeningStrategySubWedgeId,
			ScreeningStrategyWedge3VO screeningStrategyWedgeVO,
			Integer subWedgeNumber, String rotationAxis, Double axisStart,
			Double axisEnd, Double exposureTime, Double transmission,
			Double oscillationRange, Double completeness, Double multiplicity,
			Double doseTotal, Integer numberOfImages, String comments) {
		super();
		this.screeningStrategySubWedgeId = screeningStrategySubWedgeId;
		this.screeningStrategyWedgeVO = screeningStrategyWedgeVO;
		this.subWedgeNumber = subWedgeNumber;
		this.rotationAxis = rotationAxis;
		this.axisStart = axisStart;
		this.axisEnd = axisEnd;
		this.exposureTime = exposureTime;
		this.transmission = transmission;
		this.oscillationRange = oscillationRange;
		this.completeness = completeness;
		this.multiplicity = multiplicity;
		this.doseTotal = doseTotal;
		this.numberOfImages = numberOfImages;
		this.comments = comments;
	}
	
	public ScreeningStrategySubWedge3VO(ScreeningStrategySubWedge3VO vo) {
		super();
		this.screeningStrategySubWedgeId = vo.getScreeningStrategySubWedgeId();
		this.screeningStrategyWedgeVO = vo.getScreeningStrategyWedgeVO();
		this.subWedgeNumber = vo.getSubWedgeNumber();
		this.rotationAxis = vo.getRotationAxis();
		this.axisStart = vo.getAxisStart();
		this.axisEnd = vo.getAxisEnd();
		this.exposureTime = vo.getExposureTime();
		this.transmission = vo.getTransmission();
		this.oscillationRange = vo.getOscillationRange();
		this.completeness = vo.getCompleteness();
		this.multiplicity = vo.getMultiplicity();
		this.doseTotal = vo.getDoseTotal();
		this.numberOfImages = vo.getNumberOfImages();
		this.comments = vo.getComments();
	}

	public void fillVOFromWS(ScreeningStrategySubWedgeWS3VO vo) {
		this.screeningStrategySubWedgeId = vo.getScreeningStrategySubWedgeId();
		this.screeningStrategyWedgeVO = null;
		this.subWedgeNumber = vo.getSubWedgeNumber();
		this.rotationAxis = vo.getRotationAxis();
		this.axisStart = vo.getAxisStart();
		this.axisEnd = vo.getAxisEnd();
		this.exposureTime = vo.getExposureTime();
		this.transmission = vo.getTransmission();
		this.oscillationRange = vo.getOscillationRange();
		this.completeness = vo.getCompleteness();
		this.multiplicity = vo.getMultiplicity();
		this.doseTotal = vo.getDoseTotal();
		this.numberOfImages = vo.getNumberOfImages();
		this.comments = vo.getComments();
	}



	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getScreeningStrategySubWedgeId() {
		return screeningStrategySubWedgeId;
	}

	/**
	 * @param pk The pk to set.
	 */
	public void setScreeningStrategySubWedgeId(Integer screeningStrategySubWedgeId) {
		this.screeningStrategySubWedgeId = screeningStrategySubWedgeId;
	}

	

	public ScreeningStrategyWedge3VO getScreeningStrategyWedgeVO() {
		return screeningStrategyWedgeVO;
	}



	public void setScreeningStrategyWedgeVO(
			ScreeningStrategyWedge3VO screeningStrategyWedgeVO) {
		this.screeningStrategyWedgeVO = screeningStrategyWedgeVO;
	}


	public Integer getScreeningStrategyWedgeVOId() {
		return screeningStrategyWedgeVO == null ? null : screeningStrategyWedgeVO.getScreeningStrategyWedgeId();
	}

	public Integer getSubWedgeNumber() {
		return subWedgeNumber;
	}

	public void setSubWedgeNumber(Integer subWedgeNumber) {
		this.subWedgeNumber = subWedgeNumber;
	}

	public String getRotationAxis() {
		return rotationAxis;
	}

	public void setRotationAxis(String rotationAxis) {
		this.rotationAxis = rotationAxis;
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

	public Double getExposureTime() {
		return exposureTime;
	}

	public void setExposureTime(Double exposureTime) {
		this.exposureTime = exposureTime;
	}

	public Double getTransmission() {
		return transmission;
	}

	public void setTransmission(Double transmission) {
		this.transmission = transmission;
	}

	public Double getOscillationRange() {
		return oscillationRange;
	}

	public void setOscillationRange(Double oscillationRange) {
		this.oscillationRange = oscillationRange;
	}

	public Double getCompleteness() {
		return completeness;
	}

	public void setCompleteness(Double completeness) {
		this.completeness = completeness;
	}

	public Double getMultiplicity() {
		return multiplicity;
	}

	public void setMultiplicity(Double multiplicity) {
		this.multiplicity = multiplicity;
	}

	public Double getDoseTotal() {
		return doseTotal;
	}

	public void setDoseTotal(Double doseTotal) {
		this.doseTotal = doseTotal;
	}

	public Integer getNumberOfImages() {
		return numberOfImages;
	}

	public void setNumberOfImages(Integer numberOfImages) {
		this.numberOfImages = numberOfImages;
	}
	
	

	public String getComments() {
		return comments;
	}



	public void setComments(String comments) {
		this.comments = comments;
	}



	/**
	 * Checks the values of this value object for correctness and
	 * completeness. Should be done before persisting the data in the DB.
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @throws Exception if the data of the value object is not correct
	 */
	public void checkValues(boolean create) throws Exception {
		//TODO
	}
	
	public String toWSString(){
		String s = "screeningStrategySubWedgeId="+this.screeningStrategySubWedgeId +", "+
		"subWedgeNumber="+this.subWedgeNumber+", "+
		"rotationAxis="+this.rotationAxis+", "+
		"axisStart="+this.axisStart+", "+
		"axisEnd="+this.axisEnd+", "+
		"exposureTime="+this.exposureTime+", "+
		"transmission="+this.transmission+", "+
		"oscillationRange="+this.oscillationRange+", "+
		"completeness="+this.completeness+", "+
		"multiplicity="+this.multiplicity+", "+
		"doseTotal="+this.doseTotal+", "+
		"numberOfImages="+this.numberOfImages+", "+
		"comments="+this.comments;
		
		return s;
	}
}

