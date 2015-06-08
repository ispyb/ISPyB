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
 * WorkflowDehydration3 value object mapping table WorkflowDehydration
 * 
 */
@Entity
@Table(name = "WorkflowDehydration")
public class WorkflowDehydration3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(WorkflowDehydration3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "workflowDehydrationId")
	private Integer workflowDehydrationId;

	@ManyToOne
	@JoinColumn(name = "workflowId")
	private Workflow3VO workflowVO;
	
	@Column(name = "dataFilePath")
	protected String dataFilePath;

	@Column(name = "recordTimeStamp")
	protected Date recordTimeStamp;

	public WorkflowDehydration3VO() {
		super();
	}

	public WorkflowDehydration3VO(Integer workflowDehydrationId,
			Workflow3VO workflowVO, String dataFilePath, Date recordTimeStamp) {
		super();
		this.workflowDehydrationId = workflowDehydrationId;
		this.workflowVO = workflowVO;
		this.dataFilePath = dataFilePath;
		this.recordTimeStamp = recordTimeStamp;
	}
	
	public WorkflowDehydration3VO(WorkflowDehydration3VO vo) {
		super();
		this.workflowDehydrationId = vo.getWorkflowDehydrationId();
		this.workflowVO = vo.getWorkflowVO();
		this.dataFilePath = vo.getDataFilePath();
		this.recordTimeStamp = vo.getRecordTimeStamp();
	}

	public void fillVOFromWS(WorkflowDehydrationWS3VO vo) {
		this.workflowDehydrationId = vo.getWorkflowDehydrationId();
		this.workflowVO = null;
		this.dataFilePath = vo.getDataFilePath();
		this.recordTimeStamp = vo.getRecordTimeStamp();
	}
	
	public Integer getWorkflowDehydrationId() {
		return workflowDehydrationId;
	}

	public void setWorkflowDehydrationId(Integer workflowDehydrationId) {
		this.workflowDehydrationId = workflowDehydrationId;
	}

	public Workflow3VO getWorkflowVO() {
		return workflowVO;
	}

	public void setWorkflowVO(Workflow3VO workflowVO) {
		this.workflowVO = workflowVO;
	}

	public String getDataFilePath() {
		return dataFilePath;
	}

	public void setDataFilePath(String dataFilePath) {
		this.dataFilePath = dataFilePath;
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
		//workflow
		if(workflowVO == null)
			throw new IllegalArgumentException(StringUtils.getMessageRequiredField("WorkflowDehydration", "workflow"));
		
	}

	/**
	 * used to clone an entity to set the linked collectio9ns to null, for webservices
	 */
	@Override
	public WorkflowDehydration3VO clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (WorkflowDehydration3VO) super.clone();
	}
	
	public String toWSString(){
		String s = "workflowDehydrationId="+this.workflowDehydrationId +", "+
		"dataFilePath="+this.dataFilePath+", "+
		"recordTimeStamp="+this.recordTimeStamp;
		
		return s;
	}
}
