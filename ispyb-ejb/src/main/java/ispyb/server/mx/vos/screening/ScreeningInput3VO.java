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
import ispyb.server.mx.vos.sample.DiffractionPlan3VO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * ScreeningInput3 value object mapping table ScreeningInput
 * 
 */
@Entity
@Table(name = "ScreeningInput")
public class ScreeningInput3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(ScreeningInput3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "screeningInputId")
	protected Integer screeningInputId;
	
	@ManyToOne
	@JoinColumn(name = "screeningId")
	private Screening3VO screeningVO;
	
	@ManyToOne
	@JoinColumn(name = "diffractionPlanId")
	private DiffractionPlan3VO diffractionPlanVO;
	
	@Column(name = "beamX")
	protected Double beamX;
	
	@Column(name = "beamY")
	protected Double beamY;
	
	@Column(name = "rmsErrorLimits")
	protected Double rmsErrorLimits;
	
	@Column(name = "minimumFractionIndexed")
	protected Double minimumFractionIndexed;
	
	@Column(name = "maximumFractionRejected")
	protected Double maximumFractionRejected;
	
	@Column(name = "minimumSignalToNoise")
	protected Double minimumSignalToNoise;
	
	@Column(name = "xmlSampleInformation")
	protected String xmlSampleInformation;
	
	 
	public ScreeningInput3VO(){
		super();
	}

	
	public ScreeningInput3VO(Integer screeningInputId,
			Screening3VO screeningVO, DiffractionPlan3VO diffractionPlanVO,
			Double beamX, Double beamY, Double rmsErrorLimits,
			Double minimumFractionIndexed, Double maximumFractionRejected,
			Double minimumSignalToNoise, String xmlSampleInformation) {
		super();
		this.screeningInputId = screeningInputId;
		this.screeningVO = screeningVO;
		this.diffractionPlanVO = diffractionPlanVO;
		this.beamX = beamX;
		this.beamY = beamY;
		this.rmsErrorLimits = rmsErrorLimits;
		this.minimumFractionIndexed = minimumFractionIndexed;
		this.maximumFractionRejected = maximumFractionRejected;
		this.minimumSignalToNoise = minimumSignalToNoise;
		this.xmlSampleInformation = xmlSampleInformation;
	}
	
	public ScreeningInput3VO(ScreeningInput3VO vo) {
		super();
		this.screeningInputId = vo.getScreeningInputId();
		this.screeningVO = vo.getScreeningVO();
		this.diffractionPlanVO = vo.getDiffractionPlanVO();
		this.beamX = vo.getBeamX();
		this.beamY = vo.getBeamY();
		this.rmsErrorLimits = vo.getRmsErrorLimits();
		this.minimumFractionIndexed = vo.getMinimumFractionIndexed();
		this.maximumFractionRejected = vo.getMaximumFractionRejected();
		this.minimumSignalToNoise =vo.getMinimumSignalToNoise();
		this.xmlSampleInformation = vo.getXmlSampleInformation();
	}
	
	public void fillVOFromWS(ScreeningInputWS3VO vo) {
		this.screeningInputId = vo.getScreeningInputId();
		this.screeningVO = null;
		this.diffractionPlanVO = null;
		this.beamX = vo.getBeamX();
		this.beamY = vo.getBeamY();
		this.rmsErrorLimits = vo.getRmsErrorLimits();
		this.minimumFractionIndexed = vo.getMinimumFractionIndexed();
		this.maximumFractionRejected = vo.getMaximumFractionRejected();
		this.minimumSignalToNoise =vo.getMinimumSignalToNoise();
		this.xmlSampleInformation = vo.getXmlSampleInformation();
	}


	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}


	/**
	 * @return Returns the pk.
	 */
	public Integer getScreeningInputId() {
		return screeningInputId;
	}

	/**
	 * @param pk The pk to set.
	 */
	public void setScreeningInputId(Integer screeningInputId) {
		this.screeningInputId = screeningInputId;
	}

	

	public Screening3VO getScreeningVO() {
		return screeningVO;
	}


	public void setScreeningVO(Screening3VO screeningVO) {
		this.screeningVO = screeningVO;
	}


	public DiffractionPlan3VO getDiffractionPlanVO() {
		return diffractionPlanVO;
	}


	public void setDiffractionPlanVO(DiffractionPlan3VO diffractionPlanVO) {
		this.diffractionPlanVO = diffractionPlanVO;
	}


	public Integer getScreeningVOId() {
		return screeningVO == null ? null : screeningVO.getScreeningId();
	}

	
	public Integer getDiffractionPlanVOId() {
		return diffractionPlanVO == null ? null : diffractionPlanVO.getDiffractionPlanId();
	}

	public Double getBeamX() {
		return beamX;
	}

	public void setBeamX(Double beamX) {
		this.beamX = beamX;
	}

	public Double getBeamY() {
		return beamY;
	}

	public void setBeamY(Double beamY) {
		this.beamY = beamY;
	}

	public Double getRmsErrorLimits() {
		return rmsErrorLimits;
	}

	public void setRmsErrorLimits(Double rmsErrorLimits) {
		this.rmsErrorLimits = rmsErrorLimits;
	}

	public Double getMinimumFractionIndexed() {
		return minimumFractionIndexed;
	}

	public void setMinimumFractionIndexed(Double minimumFractionIndexed) {
		this.minimumFractionIndexed = minimumFractionIndexed;
	}

	public Double getMaximumFractionRejected() {
		return maximumFractionRejected;
	}

	public void setMaximumFractionRejected(Double maximumFractionRejected) {
		this.maximumFractionRejected = maximumFractionRejected;
	}

	public Double getMinimumSignalToNoise() {
		return minimumSignalToNoise;
	}

	public void setMinimumSignalToNoise(Double minimumSignalToNoise) {
		this.minimumSignalToNoise = minimumSignalToNoise;
	}

	public String getXmlSampleInformation() {
		return xmlSampleInformation;
	}

	public void setXmlSampleInformation(String xmlSampleInformation) {
		this.xmlSampleInformation = xmlSampleInformation;
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
}
