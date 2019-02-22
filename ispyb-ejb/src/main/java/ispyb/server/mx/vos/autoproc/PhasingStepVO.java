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

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.log4j.Logger;

import ispyb.server.common.vos.ISPyBValueObject;


@Entity
@Table(name = "PhasingStep")
public class PhasingStepVO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(PhasingStepVO.class);

	private static final long serialVersionUID = 1234567901234567890L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "phasingStepId", unique = true, nullable = false)
	protected Integer phasingStepId;
	
	@Column(name="previousPhasingStepId")	
	protected Integer previousPhasingStepId;
	
	@ManyToOne
	@JoinColumn(name = "programRunId")
	protected PhasingProgramRun3VO phasingProgramRunVO;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "spaceGroupId")
	protected SpaceGroup3VO spaceGroupVO;
	
	@Column(name="autoProcScalingId")	
	protected Integer autoProcScalingId;
	
	@Column(name="phasingAnalysisId")	
	protected Integer phasingAnalysisId;
	
	/** ENUMERATION 'PREPARE', 'SUBSTRUCTUREDETERMINATION', 'PHASING', 'MODELBUILDING' **/
	@Column(name="phasingStepType")	
	protected String phasingStepType;
	
	@Column(name="method")	
	protected String method;
	
	@Column(name="solventContent")	
	protected String solventContent;
	
	@Column(name="enantiomorph")	
	protected String enantiomorph;
	
	@Column(name="lowRes")	
	protected String lowRes;
	
	@Column(name="highRes")	
	protected String highRes;
	
	@Column(name="groupName")	
	protected String groupName;
	
	@Column(name = "recordTimeStamp")
	protected Date recordTimeStamp;
	
	
	public PhasingStepVO() {
		super();
	}


	@Override
	public void checkValues(boolean create) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public Integer getPhasingStepId() {
		return phasingStepId;
	}


	public void setPhasingStepId(Integer phasingStepId) {
		this.phasingStepId = phasingStepId;
	}


	public Integer getPreviousPhasingStepId() {
		return previousPhasingStepId;
	}


	public void setPreviousPhasingStepId(Integer previousPhasingStepId) {
		this.previousPhasingStepId = previousPhasingStepId;
	}


	public PhasingProgramRun3VO getPhasingProgramRunVO() {
		return phasingProgramRunVO;
	}


	public void setPhasingProgramRunVO(PhasingProgramRun3VO phasingProgramRunVO) {
		this.phasingProgramRunVO = phasingProgramRunVO;
	}


	public SpaceGroup3VO getSpaceGroupVO() {
		return spaceGroupVO;
	}


	public void setSpaceGroupVO(SpaceGroup3VO spaceGroupVO) {
		this.spaceGroupVO = spaceGroupVO;
	}


	public Integer getAutoProcScalingId() {
		return autoProcScalingId;
	}


	public void setAutoProcScalingId(Integer autoProcScalingId) {
		this.autoProcScalingId = autoProcScalingId;
	}


	public Integer getPhasingAnalysisId() {
		return phasingAnalysisId;
	}


	public void setPhasingAnalysisId(Integer phasingAnalysisId) {
		this.phasingAnalysisId = phasingAnalysisId;
	}


	public String getPhasingStepType() {
		return phasingStepType;
	}


	public void setPhasingStepType(String phasingStepType) {
		this.phasingStepType = phasingStepType;
	}


	public String getMethod() {
		return method;
	}


	public void setMethod(String method) {
		this.method = method;
	}


	public String getSolventContent() {
		return solventContent;
	}


	public void setSolventContent(String solventContent) {
		this.solventContent = solventContent;
	}


	public String getEnantiomorph() {
		return enantiomorph;
	}


	public void setEnantiomorph(String enantiomorph) {
		this.enantiomorph = enantiomorph;
	}


	public String getLowRes() {
		return lowRes;
	}


	public void setLowRes(String lowRes) {
		this.lowRes = lowRes;
	}


	public String getHighRes() {
		return highRes;
	}


	public void setHighRes(String highRes) {
		this.highRes = highRes;
	}


	public Date getRecordTimeStamp() {
		return recordTimeStamp;
	}


	public void setRecordTimeStamp(Date recordTimeStamp) {
		this.recordTimeStamp = recordTimeStamp;
	}


	public String getGroupName() {
		return groupName;
	}


	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

}
