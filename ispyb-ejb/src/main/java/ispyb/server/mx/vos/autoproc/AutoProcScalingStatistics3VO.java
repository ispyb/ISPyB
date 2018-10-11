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

package ispyb.server.mx.vos.autoproc;

import ispyb.server.common.vos.ISPyBValueObject;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * AutoProcScalingStatistics3 value object mapping table AutoProcScalingStatistics
 * 
 */
@Entity
@Table(name = "AutoProcScalingStatistics")
public class AutoProcScalingStatistics3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(AutoProcScalingStatistics3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "autoProcScalingStatisticsId")
	protected Integer autoProcScalingStatisticsId;
	
	@ManyToOne
	@JoinColumn(name = "autoProcScalingId")
	private AutoProcScaling3VO autoProcScalingVO;
	
	@Column(name = "scalingStatisticsType")
	protected String scalingStatisticsType;
	
	@Column(name = "comments")
	protected String comments;
	
	@Column(name = "resolutionLimitLow")
	protected Float resolutionLimitLow;
	
	@Column(name = "resolutionLimitHigh")
	protected Float resolutionLimitHigh;
	
	@Column(name = "rmerge")
	protected Float rmerge;
	
	@Column(name = "rmeasWithinIplusIminus")
	protected Float rmeasWithinIplusIminus;
	
	@Column(name = "rmeasAllIplusIminus")
	protected Float rmeasAllIplusIminus;
	
	@Column(name = "rpimWithinIplusIminus")
	protected Float rpimWithinIplusIminus;
	
	@Column(name = "rpimAllIplusIminus")
	protected Float rpimAllIplusIminus;
	
	@Column(name = "fractionalPartialBias")
	protected Float fractionalPartialBias;
	
	@Column(name = "nTotalObservations")
	protected Integer nTotalObservations;
	
	@Column(name = "nTotalUniqueObservations")
	protected Integer nTotalUniqueObservations;
	
	@Column(name = "meanIoverSigI")
	protected Float meanIoverSigI;
	
	@Column(name = "completeness")
	protected Float completeness;
	
	@Column(name = "multiplicity")
	protected Float multiplicity;
	
	@Column(name = "anomalousCompleteness")
	protected Float anomalousCompleteness;
	
	@Column(name = "anomalousMultiplicity")
	protected Float anomalousMultiplicity;
	
	@Column(name = "recordTimeStamp")
	protected Date recordTimeStamp;
	
	@Column(name = "anomalous")
	protected Boolean anomalous;
	
	@Column(name = "ccHalf")
	protected Float ccHalf;

	@Column(name = "ccAno")
	protected Float ccAno;
	
	@Column(name = "sigAno")
	protected Float sigAno;
	
	@Column(name = "isa")
	protected Float ISa;
	
	@Column(name = "completenessSpherical")
	protected Float completenessSpherical;
		
	@Column(name = "anomalousCompletenessSpherical")
	protected Float anomalousCompletenessSpherical;
	
	@Column(name = "completenessEllipsoidal")
	protected Float completenessEllipsoidal;
		
	@Column(name = "anomalousCompletenessEllipsoidal")
	protected Float anomalousCompletenessEllipsoidal;
		
	public AutoProcScalingStatistics3VO(){
		super();
	}	

	public AutoProcScalingStatistics3VO(Integer autoProcScalingStatisticsId,
			AutoProcScaling3VO autoProcScalingVO, String scalingStatisticsType,
			String comments, Float resolutionLimitLow,
			Float resolutionLimitHigh, Float rmerge,
			Float rmeasWithinIplusIminus, Float rmeasAllIplusIminus,
			Float rpimWithinIplusIminus, Float rpimAllIplusIminus,
			Float fractionalPartialBias, Integer nTotalObservations,
			Integer nTotalUniqueObservations, Float meanIoverSigI,
			Float completeness, Float multiplicity,
			Float anomalousCompleteness, Float anomalousMultiplicity,
			Date recordTimeStamp, Boolean anomalous, Float ccHalf, Float Isa,
			Float completenessSpherical, Float anomalousCompletenessSpherical,
			Float completenessEllipsoidal, Float anomalousCompletenessEllipsoidal) {
		super();
		this.autoProcScalingStatisticsId = autoProcScalingStatisticsId;
		this.autoProcScalingVO = autoProcScalingVO;
		this.scalingStatisticsType = scalingStatisticsType;
		this.comments = comments;
		this.resolutionLimitLow = resolutionLimitLow;
		this.resolutionLimitHigh = resolutionLimitHigh;
		this.rmerge = rmerge;
		this.rmeasWithinIplusIminus = rmeasWithinIplusIminus;
		this.rmeasAllIplusIminus = rmeasAllIplusIminus;
		this.rpimWithinIplusIminus = rpimWithinIplusIminus;
		this.rpimAllIplusIminus = rpimAllIplusIminus;
		this.fractionalPartialBias = fractionalPartialBias;
		this.nTotalObservations = nTotalObservations;
		this.nTotalUniqueObservations = nTotalUniqueObservations;
		this.meanIoverSigI = meanIoverSigI;
		this.completeness = completeness;
		this.multiplicity = multiplicity;
		this.anomalousCompleteness = anomalousCompleteness;
		this.anomalousMultiplicity = anomalousMultiplicity;
		this.recordTimeStamp = recordTimeStamp;
		this.anomalous = anomalous;
		this.ccHalf = ccHalf;
		this.ISa = Isa;
		this.completenessSpherical = completenessSpherical;
		this.anomalousCompletenessSpherical = anomalousCompletenessSpherical;
		this.completenessEllipsoidal = completenessEllipsoidal;
		this.anomalousCompletenessEllipsoidal = anomalousCompletenessEllipsoidal;
	}
	
	public AutoProcScalingStatistics3VO(AutoProcScalingStatistics3VO vo) {
		super();
		this.autoProcScalingStatisticsId = vo.getAutoProcScalingStatisticsId();
		this.autoProcScalingVO = vo.getAutoProcScalingVO();
		this.scalingStatisticsType = vo.getScalingStatisticsType();
		this.comments = vo.getComments();
		this.resolutionLimitLow = vo.getResolutionLimitLow();
		this.resolutionLimitHigh = vo.getResolutionLimitHigh();
		this.rmerge = vo.getRmerge();
		this.rmeasWithinIplusIminus = vo.getRmeasWithinIplusIminus();
		this.rmeasAllIplusIminus = vo.getRmeasAllIplusIminus();
		this.rpimWithinIplusIminus = vo.getRpimWithinIplusIminus();
		this.rpimAllIplusIminus = vo.getRpimAllIplusIminus();
		this.fractionalPartialBias = vo.getFractionalPartialBias();
		this.nTotalObservations = vo.getnTotalObservations();
		this.nTotalUniqueObservations = vo.getnTotalUniqueObservations();
		this.meanIoverSigI = vo.getMeanIoverSigI();
		this.completeness = vo.getCompleteness();
		this.multiplicity = vo.getMultiplicity();
		this.anomalousCompleteness = vo.getAnomalousCompleteness();
		this.anomalousMultiplicity = vo.getAnomalousMultiplicity();
		this.recordTimeStamp = vo.getRecordTimeStamp();
		this.anomalous = vo.getAnomalous();
		this.ccHalf = vo.getCcHalf();
		this.ISa = vo.getISa();
		this.completenessSpherical = vo.getCompletenessSpherical();
		this.anomalousCompletenessSpherical = vo.getAnomalousCompletenessSpherical();
		this.completenessEllipsoidal = vo.getCompletenessEllipsoidal();
		this.anomalousCompletenessEllipsoidal = vo.getAnomalousCompletenessEllipsoidal();

	}
	
	public void fillVOFromWS(AutoProcScalingStatisticsWS3VO vo) {
		this.autoProcScalingStatisticsId = vo.getAutoProcScalingStatisticsId();
		this.autoProcScalingVO = null;
		this.scalingStatisticsType = vo.getScalingStatisticsType();
		this.comments = vo.getComments();
		this.resolutionLimitLow = vo.getResolutionLimitLow();
		this.resolutionLimitHigh = vo.getResolutionLimitHigh();
		this.rmerge = vo.getRmerge();
		this.rmeasWithinIplusIminus = vo.getRmeasWithinIplusIminus();
		this.rmeasAllIplusIminus = vo.getRmeasAllIplusIminus();
		this.rpimWithinIplusIminus = vo.getRpimWithinIplusIminus();
		this.rpimAllIplusIminus = vo.getRpimAllIplusIminus();
		this.fractionalPartialBias = vo.getFractionalPartialBias();
		this.nTotalObservations = vo.getnTotalObservations();
		this.nTotalUniqueObservations = vo.getnTotalUniqueObservations();
		this.meanIoverSigI = vo.getMeanIoverSigI();
		this.completeness = vo.getCompleteness();
		this.multiplicity = vo.getMultiplicity();
		this.anomalousCompleteness = vo.getAnomalousCompleteness();
		this.anomalousMultiplicity = vo.getAnomalousMultiplicity();
		this.recordTimeStamp = vo.getRecordTimeStamp();
		this.anomalous = vo.getAnomalous();
		this.ccHalf = vo.getCcHalf();
		this.ccAno = vo.getCcAno();
		this.sigAno = vo.getSigAno();
		this.ISa = vo.getISa();
		this.completenessSpherical = vo.getCompletenessSpherical();
		this.anomalousCompletenessSpherical = vo.getAnomalousCompletenessSpherical();
		this.completenessEllipsoidal = vo.getCompletenessEllipsoidal();
		this.anomalousCompletenessEllipsoidal = vo.getAnomalousCompletenessEllipsoidal();

	}


	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
	/**
	 * @return Returns the pk.
	 */
	public Integer getAutoProcScalingStatisticsId() {
		return autoProcScalingStatisticsId;
	}

	/**
	 * @param pk The pk to set.
	 */
	public void setAutoProcScalingStatisticsId(Integer autoProcScalingStatisticsId) {
		this.autoProcScalingStatisticsId = autoProcScalingStatisticsId;
	}

	

	public AutoProcScaling3VO getAutoProcScalingVO() {
		return autoProcScalingVO;
	}



	public void setAutoProcScalingVO(AutoProcScaling3VO autoProcScalingVO) {
		this.autoProcScalingVO = autoProcScalingVO;
	}



	public String getScalingStatisticsType() {
		return scalingStatisticsType;
	}

	public void setScalingStatisticsType(String scalingStatisticsType) {
		this.scalingStatisticsType = scalingStatisticsType;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Float getResolutionLimitLow() {
		return resolutionLimitLow;
	}

	public void setResolutionLimitLow(Float resolutionLimitLow) {
		this.resolutionLimitLow = resolutionLimitLow;
	}

	public Float getResolutionLimitHigh() {
		return resolutionLimitHigh;
	}

	public void setResolutionLimitHigh(Float resolutionLimitHigh) {
		this.resolutionLimitHigh = resolutionLimitHigh;
	}

	public Float getRmerge() {
		return rmerge;
	}

	public void setRmerge(Float rmerge) {
		this.rmerge = rmerge;
	}

	public Float getRmeasWithinIplusIminus() {
		return rmeasWithinIplusIminus;
	}

	public void setRmeasWithinIplusIminus(Float rmeasWithinIplusIminus) {
		this.rmeasWithinIplusIminus = rmeasWithinIplusIminus;
	}

	public Float getRmeasAllIplusIminus() {
		return rmeasAllIplusIminus;
	}

	public void setRmeasAllIplusIminus(Float rmeasAllIplusIminus) {
		this.rmeasAllIplusIminus = rmeasAllIplusIminus;
	}

	public Float getRpimWithinIplusIminus() {
		return rpimWithinIplusIminus;
	}

	public void setRpimWithinIplusIminus(Float rpimWithinIplusIminus) {
		this.rpimWithinIplusIminus = rpimWithinIplusIminus;
	}

	public Float getRpimAllIplusIminus() {
		return rpimAllIplusIminus;
	}

	public void setRpimAllIplusIminus(Float rpimAllIplusIminus) {
		this.rpimAllIplusIminus = rpimAllIplusIminus;
	}

	public Float getFractionalPartialBias() {
		return fractionalPartialBias;
	}

	public void setFractionalPartialBias(Float fractionalPartialBias) {
		this.fractionalPartialBias = fractionalPartialBias;
	}

	public Integer getnTotalObservations() {
		return nTotalObservations;
	}

	public void setnTotalObservations(Integer nTotalObservations) {
		this.nTotalObservations = nTotalObservations;
	}

	public Integer getnTotalUniqueObservations() {
		return nTotalUniqueObservations;
	}

	public void setnTotalUniqueObservations(Integer nTotalUniqueObservations) {
		this.nTotalUniqueObservations = nTotalUniqueObservations;
	}

	public Float getMeanIoverSigI() {
		return meanIoverSigI;
	}

	public void setMeanIoverSigI(Float meanIoverSigI) {
		this.meanIoverSigI = meanIoverSigI;
	}

	public Float getCompleteness() {
		return completeness;
	}

	public void setCompleteness(Float completeness) {
		this.completeness = completeness;
	}

	public Float getMultiplicity() {
		return multiplicity;
	}

	public void setMultiplicity(Float multiplicity) {
		this.multiplicity = multiplicity;
	}

	public Float getAnomalousCompleteness() {
		return anomalousCompleteness;
	}

	public void setAnomalousCompleteness(Float anomalousCompleteness) {
		this.anomalousCompleteness = anomalousCompleteness;
	}

	public Float getAnomalousMultiplicity() {
		return anomalousMultiplicity;
	}

	public void setAnomalousMultiplicity(Float anomalousMultiplicity) {
		this.anomalousMultiplicity = anomalousMultiplicity;
	}

	public Date getRecordTimeStamp() {
		return recordTimeStamp;
	}

	public void setRecordTimeStamp(Date recordTimeStamp) {
		this.recordTimeStamp = recordTimeStamp;
	}

	public Boolean getAnomalous() {
		return anomalous;
	}

	public void setAnomalous(Boolean anomalous) {
		this.anomalous = anomalous;
	}
	
	public Integer getAutoProcScalingVOId() {
		return autoProcScalingVO == null ? null : autoProcScalingVO.getAutoProcScalingId();
	}

	public Float getCcHalf() {
		return ccHalf;
	}

	public void setCcHalf(Float ccHalf) {
		this.ccHalf = ccHalf;
	}
	
	public Float getCompletenessSpherical() {
		return completenessSpherical;
	}

	public void setCompletenessSpherical(Float completenessSpherical) {
		this.completenessSpherical = completenessSpherical;
	}

	public Float getAnomalousCompletenessSpherical() {
		return anomalousCompletenessSpherical;
	}

	public void setAnomalousCompletenessSpherical(Float anomalousCompletenessSpherical) {
		this.anomalousCompletenessSpherical = anomalousCompletenessSpherical;
	}

	public Float getCompletenessEllipsoidal() {
		return completenessEllipsoidal;
	}

	public void setCompletenessEllipsoidal(Float completenessEllipsoidal) {
		this.completenessEllipsoidal = completenessEllipsoidal;
	}

	public Float getAnomalousCompletenessEllipsoidal() {
		return anomalousCompletenessEllipsoidal;
	}

	public void setAnomalousCompletenessEllipsoidal(Float anomalousCompletenessEllipsoidal) {
		this.anomalousCompletenessEllipsoidal = anomalousCompletenessEllipsoidal;
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
		String s = "autoProcScalingStatisticsId="+this.autoProcScalingStatisticsId +", "+
		"scalingStatisticsType="+this.scalingStatisticsType+", "+
		"comments="+this.comments+", "+
		"resolutionLimitLow="+this.resolutionLimitLow+", "+
		"resolutionLimitHigh="+this.resolutionLimitHigh+", "+
		"rmerge="+this.rmerge+", "+
		"rmeasWithinIplusIminus="+this.rmeasWithinIplusIminus+", "+
		"rmeasAllIplusIminus="+this.rmeasAllIplusIminus+", "+
		"rpimWithinIplusIminus="+this.rpimWithinIplusIminus+", "+
		"rpimAllIplusIminus="+this.rpimAllIplusIminus+", "+
		"fractionalPartialBias="+this.fractionalPartialBias+", "+
		"nTotalObservations="+this.nTotalObservations+", "+
		"nTotalUniqueObservations="+this.nTotalUniqueObservations+", "+
		"meanIoverSigI="+this.meanIoverSigI+", "+
		"completeness="+this.completeness+", "+
		"multiplicity="+this.multiplicity+", "+
		"anomalousCompleteness="+this.anomalousCompleteness+", "+
		"anomalousMultiplicity="+this.anomalousMultiplicity+", "+
		"recordTimeStamp="+this.recordTimeStamp+", "+
		"anomalous="+this.anomalous+", "+
		"ccHalf="+this.ccHalf;
		
		return s;
	}



	public Float getCcAno() {
		return ccAno;
	}



	public void setCcAno(Float ccAno) {
		this.ccAno = ccAno;
	}



	public Float getSigAno() {
		return sigAno;
	}



	public void setSigAno(Float sigAno) {
		this.sigAno = sigAno;
	}



	public Float getISa() {
		return ISa;
	}



	public void setISa(Float iSa) {
		ISa = iSa;
	}
	
}
