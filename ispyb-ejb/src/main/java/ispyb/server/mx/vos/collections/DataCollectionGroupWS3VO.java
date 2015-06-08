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


/**
 * DataCollectionGroup class for webservices
 * @author 
 *
 */
public class DataCollectionGroupWS3VO extends DataCollectionGroup3VO {

	private static final long serialVersionUID = -3064624683236886675L;

	private Integer sessionId;
	
	private Integer blSampleId;

	private Integer workflowId;
	
	public DataCollectionGroupWS3VO() {
		super();
	}

	public DataCollectionGroupWS3VO(DataCollectionGroup3VO vo) {
		super(vo);
	}


	public DataCollectionGroupWS3VO(Integer sessionId, Integer blSampleId, Integer workflowId) {
		super();
		this.sessionId = sessionId;
		this.blSampleId = blSampleId;
		this.workflowId = workflowId;
	}



	public Integer getSessionId() {
		return sessionId;
	}



	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}



	public Integer getBlSampleId() {
		return blSampleId;
	}



	public void setBlSampleId(Integer blSampleId) {
		this.blSampleId = blSampleId;
	}
	
	public Integer getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Integer workflowId) {
		this.workflowId = workflowId;
	}

	@Override
	public String toWSString(){
		String s= super.toWSString();
		s += ", sessionId="+this.sessionId+", "+
		"blSampleId="+this.blSampleId+", "+
		"workflowId="+this.workflowId;
		return s;
	}
	
	
}
