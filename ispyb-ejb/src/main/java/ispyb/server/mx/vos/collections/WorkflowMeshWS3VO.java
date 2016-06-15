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
* WorkflowMesh class used for webservices
* @author BODIN
*
*/
public class WorkflowMeshWS3VO extends WorkflowMesh3VO {
	private static final long serialVersionUID = 1L;

	private Integer workflowId;
	
	private Integer bestPositionId;
	
	private Integer bestImageId;

	public Integer getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Integer workflowId) {
		this.workflowId = workflowId;
	}

	public Integer getBestPositionId() {
		return bestPositionId;
	}

	public void setBestPositionId(Integer bestPositionId) {
		this.bestPositionId = bestPositionId;
	}

	public Integer getBestImageId() {
		return bestImageId;
	}

	public void setBestImageId(Integer bestImageId) {
		this.bestImageId = bestImageId;
	}

	public WorkflowMeshWS3VO() {
		super();
	}
	public WorkflowMeshWS3VO(WorkflowMesh3VO vo) {
		super(vo);
		this.workflowId = vo.getWorkflowVO().getWorkflowId();
		this.bestImageId = (vo.getBestImageVO() == null ? null: vo.getBestImageVO().getImageId());
		
	}
	@Override
	public String toWSString(){
		String s= super.toWSString();
		s += ", workflowId="+this.workflowId+", "+
		"bestPositionId="+this.bestPositionId+", "+
		"bestImageId="+this.bestImageId;
		return s;
	}
	
}
