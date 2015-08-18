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
import javax.persistence.EntityResult;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import ispyb.common.util.StringUtils;
import ispyb.server.common.vos.ISPyBValueObject;

import org.apache.log4j.Logger;

/**
 * PhasingHasScaling3 value object mapping table Phasing_has_Scaling
 * 
 */
@Entity
@Table(name = "Phasing_has_Scaling")
@SqlResultSetMapping(name = "phasingHasScalingNativeQuery", entities = { @EntityResult(entityClass = PhasingHasScaling3VO.class) })
public class PhasingHasScaling3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(PhasingHasScaling3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "phasingHasScalingId")
	protected Integer phasingHasScalingId;
	
	@ManyToOne
	@JoinColumn(name = "phasingAnalysisId")
	protected PhasingAnalysis3VO phasingAnalysisVO;
	
	@ManyToOne
	@JoinColumn(name = "autoProcScalingId")
	private AutoProcScaling3VO autoProcScalingVO;
	
	@Column(name = "datasetNumber")
	protected Integer datasetNumber;
	
	@Column(name = "recordTimeStamp")
	protected Date recordTimeStamp;
	
	
	
	public PhasingHasScaling3VO() {
		super();
	}

	public PhasingHasScaling3VO(Integer phasingHasScalingId,
			PhasingAnalysis3VO phasingAnalysisVO, AutoProcScaling3VO autoProcScalingVO,
			Integer datasetNumber, Date recordTimeStamp) {
		super();
		this.phasingHasScalingId = phasingHasScalingId;
		this.phasingAnalysisVO = phasingAnalysisVO;
		this.autoProcScalingVO = autoProcScalingVO;
		this.datasetNumber = datasetNumber;
		this.recordTimeStamp = recordTimeStamp;
	}

	public void  fillVOFromWS(PhasingHasScalingWS3VO vo) {
		this.phasingHasScalingId = vo.getPhasingHasScalingId();
		this.phasingAnalysisVO = null;
		this.autoProcScalingVO = null;
		this.datasetNumber = vo.getDatasetNumber();
		this.recordTimeStamp = vo.getRecordTimeStamp();
	}
	
	
	public Integer getPhasingHasScalingId() {
		return phasingHasScalingId;
	}

	public void setPhasingHasScalingId(Integer phasingHasScalingId) {
		this.phasingHasScalingId = phasingHasScalingId;
	}

	public PhasingAnalysis3VO getPhasingAnalysisVO() {
		return phasingAnalysisVO;
	}

	public void setPhasingAnalysisVO(PhasingAnalysis3VO phasingAnalysisVO) {
		this.phasingAnalysisVO = phasingAnalysisVO;
	}

	public AutoProcScaling3VO getAutoProcScalingVO() {
		return autoProcScalingVO;
	}

	public void setAutoProcScalingVO(AutoProcScaling3VO autoProcScalingVO) {
		this.autoProcScalingVO = autoProcScalingVO;
	}

	public Integer getDatasetNumber() {
		return datasetNumber;
	}

	public void setDatasetNumber(Integer datasetNumber) {
		this.datasetNumber = datasetNumber;
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
		//phasing
		if(phasingAnalysisVO == null)
			throw new IllegalArgumentException(StringUtils.getMessageRequiredField("PhasingHasScaling", "phasingAnalysisVO"));
		//autoProcScaling
		if(autoProcScalingVO == null)
			throw new IllegalArgumentException(StringUtils.getMessageRequiredField("PhasingHasScaling", "autoProcScalingVO"));
		
	}

	/**
	 * used to clone an entity to set the linked collectio9ns to null, for webservices
	 */
	@Override
	public PhasingHasScaling3VO clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (PhasingHasScaling3VO) super.clone();
	}
	
	public String toWSString(){
		String s = "phasingHasScalingId="+this.phasingHasScalingId +", "+
		"datasetNumber="+this.datasetNumber+", "+
		"recordTimeStamp="+this.recordTimeStamp;
		
		return s;
	}
}
