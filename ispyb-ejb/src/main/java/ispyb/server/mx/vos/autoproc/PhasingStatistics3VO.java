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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ispyb.common.util.StringUtils;
import ispyb.server.common.vos.ISPyBValueObject;

import org.apache.log4j.Logger;

/**
 * PhasingStatistics3 value object mapping table PhasingStatistics
 * 
 */
@Entity
@Table(name = "PhasingStatistics")
public class PhasingStatistics3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(PhasingStatistics3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "phasingStatisticsId")
	protected Integer phasingStatisticsId;

	@ManyToOne
	@JoinColumn(name = "phasingHasScalingId1")
	protected PhasingHasScaling3VO phasingHasScaling1VO;
	
	@ManyToOne
	@JoinColumn(name = "phasingHasScalingId2")
	protected PhasingHasScaling3VO phasingHasScaling2VO;
	
	@Column(name = "binNumber")
	protected Integer binNumber;
	
	@Column(name = "numberOfBins")
	protected Integer numberOfBins;
	
	@Column(name = "phasingStepId")
	protected Integer phasingStepId;
	
	@Column(name = "lowRes")
	protected Double lowRes;
	
	@Column(name = "highRes")
	protected Double highRes;
	
	@Column(name = "metric")
	protected String metric;
	
	@Column(name = "statisticsValue")
	protected Double statisticsValue;
	
	@Column(name = "nReflections")
	protected Integer nReflections;
	
	@Column(name = "recordTimeStamp")
	protected Date recordTimeStamp;
	
	

	public PhasingStatistics3VO() {
		super();
	}

	
	public PhasingStatistics3VO(Integer phasingStatisticsId,
			PhasingHasScaling3VO phasingHasScaling1VO,
			PhasingHasScaling3VO phasingHasScaling2VO, Integer numberOfBins,
			Integer binNumber, Double lowRes, Double highRes, String metric,
			Double statisticsValue, Integer nReflections, Date recordTimeStamp) {
		super();
		this.phasingStatisticsId = phasingStatisticsId;
		this.phasingHasScaling1VO = phasingHasScaling1VO;
		this.phasingHasScaling2VO = phasingHasScaling2VO;
		this.numberOfBins = numberOfBins;
		this.binNumber = binNumber;
		this.lowRes = lowRes;
		this.highRes = highRes;
		this.metric = metric;
		this.statisticsValue = statisticsValue;
		this.nReflections = nReflections;
		this.recordTimeStamp = recordTimeStamp;
	}


	public void  fillVOFromWS(PhasingStatisticsWS3VO vo) {
		this.phasingStatisticsId = vo.getPhasingStatisticsId();
		this.phasingHasScaling1VO = null;
		this.phasingHasScaling2VO = null;
		this.numberOfBins = vo.getNumberOfBins();
		this.binNumber = vo.getBinNumber();
		this.lowRes = vo.getLowRes();
		this.highRes = vo.getHighRes();
		this.metric = vo.getMetric();
		this.statisticsValue = vo.getStatisticsValue();
		this.nReflections = vo.getnReflections();
		this.recordTimeStamp = vo.getRecordTimeStamp();
	}

	public Integer getPhasingStatisticsId() {
		return phasingStatisticsId;
	}

	public void setPhasingStatisticsId(Integer phasingStatisticsId) {
		this.phasingStatisticsId = phasingStatisticsId;
	}

	public PhasingHasScaling3VO getPhasingHasScaling1VO() {
		return phasingHasScaling1VO;
	}

	public void setPhasingHasScaling1VO(PhasingHasScaling3VO phasingHasScaling1VO) {
		this.phasingHasScaling1VO = phasingHasScaling1VO;
	}

	public PhasingHasScaling3VO getPhasingHasScaling2VO() {
		return phasingHasScaling2VO;
	}

	public void setPhasingHasScaling2VO(PhasingHasScaling3VO phasingHasScaling2VO) {
		this.phasingHasScaling2VO = phasingHasScaling2VO;
	}

	public Integer getNumberOfBins() {
		return numberOfBins;
	}

	public void setNumberOfBins(Integer numberOfBins) {
		this.numberOfBins = numberOfBins;
	}

	public Integer getBinNumber() {
		return binNumber;
	}

	public void setBinNumber(Integer binNumber) {
		this.binNumber = binNumber;
	}

	public Double getLowRes() {
		return lowRes;
	}

	public void setLowRes(Double lowRes) {
		this.lowRes = lowRes;
	}

	public Double getHighRes() {
		return highRes;
	}

	public void setHighRes(Double highRes) {
		this.highRes = highRes;
	}

	public String getMetric() {
		return metric;
	}

	public void setMetric(String metric) {
		this.metric = metric;
	}

	public Double getStatisticsValue() {
		return statisticsValue;
	}

	public void setStatisticsValue(Double statisticsValue) {
		this.statisticsValue = statisticsValue;
	}

	public Integer getnReflections() {
		return nReflections;
	}

	public void setnReflections(Integer nReflections) {
		this.nReflections = nReflections;
	}

	public Date getRecordTimeStamp() {
		return recordTimeStamp;
	}

	public void setRecordTimeStamp(Date recordTimeStamp) {
		this.recordTimeStamp = recordTimeStamp;
	}

	/**
	 * Checks the values of this value object for correctness and
	 * completeness. Should be done before persisting the data in the DB.
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @throws Exception if the data of the value object is not correct
	 */
	public void checkValues(boolean create) throws Exception {
//		String[] listMetric = {"Rcullis", "CC", "PhasingPower", "FOM", "<d\"/sig>","Best CC","CC(1/2)","Weak CC","CFOM","Pseudo_free_CC","CC of partial model"};
//		//phasingHasScaling1VO
//		if(phasingHasScaling1VO == null)
//			throw new IllegalArgumentException(StringUtils.getMessageRequiredField("PhasingStatistics", "phasingHasScaling1VO"));
//		// metric
//		if(!StringUtils.isStringInPredefinedList(this.metric, listMetric, true))
//			throw new IllegalArgumentException(StringUtils.getMessageErrorPredefinedList("PhasingStatistics", "metric", listMetric));
		
	}

	/**
	 * used to clone an entity to set the linked collectio9ns to null, for webservices
	 */
	@Override
	public PhasingStatistics3VO clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (PhasingStatistics3VO) super.clone();
	}
	
	public String toWSString(){
		String s = "phasingStatisticsId="+this.phasingStatisticsId +", "+
		"numberOfBins="+this.numberOfBins+", "+
		"binNumber="+this.binNumber+", "+
		"lowRes="+this.lowRes+", "+
		"highRes="+this.highRes+", "+
		"metric="+this.metric+", "+
		"statisticsValue="+this.statisticsValue+", "+
		"nReflections="+this.nReflections+", "+
		"recordTimeStamp="+this.recordTimeStamp;
		
		return s;
	}
	
	public Integer getPhasingStepId() {
		return phasingStepId;
	}


	public void setPhasingStepId(Integer phasingStepId) {
		this.phasingStepId = phasingStepId;
	}
	
}
