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
 * GridInfo3 value object mapping table GridInfo
 * 
 */
@Entity
@Table(name = "GridInfo")
@SqlResultSetMapping(name = "gridInfoNativeQuery", entities = { @EntityResult(entityClass = GridInfo3VO.class) })
public class GridInfo3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(GridInfo3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "gridInfoId")
	protected Integer gridInfoId;

	@ManyToOne
	@JoinColumn(name = "workflowMeshId")
	private WorkflowMesh3VO workflowMeshVO;
	
	@Column(name = "xOffset")
	protected Double xOffset;
	
	@Column(name = "yOffset")
	protected Double yOffset;
	
	@Column(name = "dx_mm")
	protected Double dx_mm;
	
	@Column(name = "dy_mm")
	protected Double dy_mm;
	
	@Column(name = "steps_x")
	protected Double steps_x;
	
	@Column(name = "steps_y")
	protected Double steps_y;
	
	@Column(name = "meshAngle")
	protected Double meshAngle;
	
	

	public GridInfo3VO() {
		super();
	}

	public GridInfo3VO(Integer gridInfoId, WorkflowMesh3VO workflowMeshVO,
			Double xOffset, Double yOffset, Double dx_mm, Double dy_mm,
			Double steps_x, Double steps_y, Double meshAngle) {
		super();
		this.gridInfoId = gridInfoId;
		this.workflowMeshVO = workflowMeshVO;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.dx_mm = dx_mm;
		this.dy_mm = dy_mm;
		this.steps_x = steps_x;
		this.steps_y = steps_y;
		this.meshAngle = meshAngle;
	}
	
	public GridInfo3VO(GridInfo3VO vo) {
		super();
		this.gridInfoId = vo.getGridInfoId();
		this.workflowMeshVO = vo.getWorkflowMeshVO();
		this.xOffset = vo.getxOffset();
		this.yOffset = vo.getyOffset();
		this.dx_mm = vo.getDx_mm();
		this.dy_mm = vo.getDy_mm();
		this.steps_x = vo.getSteps_x();
		this.steps_y = vo.getSteps_y();
		this.meshAngle = vo.getMeshAngle();
	}

	public void fillVOFromWS(GridInfoWS3VO vo) {
		this.gridInfoId = vo.getGridInfoId();
		this.xOffset = vo.getxOffset();
		this.yOffset = vo.getyOffset();
		this.dx_mm = vo.getDx_mm();
		this.dy_mm = vo.getDy_mm();
		this.steps_x = vo.getSteps_x();
		this.steps_y = vo.getSteps_y();
		this.meshAngle = vo.getMeshAngle();
	}

	
	public Integer getGridInfoId() {
		return gridInfoId;
	}

	public void setGridInfoId(Integer gridInfoId) {
		this.gridInfoId = gridInfoId;
	}
	
	public WorkflowMesh3VO getWorkflowMeshVO() {
		return workflowMeshVO;
	}

	public void setWorkflowMeshVO(WorkflowMesh3VO workflowMeshVO) {
		this.workflowMeshVO = workflowMeshVO;
	}

	public Double getxOffset() {
		return xOffset;
	}

	public void setxOffset(Double xOffset) {
		this.xOffset = xOffset;
	}

	public Double getyOffset() {
		return yOffset;
	}

	public void setyOffset(Double yOffset) {
		this.yOffset = yOffset;
	}

	public Double getDx_mm() {
		return dx_mm;
	}

	public void setDx_mm(Double dx_mm) {
		this.dx_mm = dx_mm;
	}

	public Double getDy_mm() {
		return dy_mm;
	}

	public void setDy_mm(Double dy_mm) {
		this.dy_mm = dy_mm;
	}

	public Double getSteps_x() {
		return steps_x;
	}

	public void setSteps_x(Double steps_x) {
		this.steps_x = steps_x;
	}

	public Double getSteps_y() {
		return steps_y;
	}

	public void setSteps_y(Double steps_y) {
		this.steps_y = steps_y;
	}

	public Double getMeshAngle() {
		return meshAngle;
	}

	public void setMeshAngle(Double meshAngle) {
		this.meshAngle = meshAngle;
	}

	/**
	 * Checks the values of this value object for correctness and
	 * completeness. Should be done before persisting the data in the DB.
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @throws Exception if the data of the value object is not correct
	 */
	public void checkValues(boolean create) throws Exception {
		//workflow
		if(workflowMeshVO == null)
			throw new IllegalArgumentException(StringUtils.getMessageRequiredField("GridInfo", "workflowMesh"));
		
	}

	/**
	 * used to clone an entity to set the linked collectio9ns to null, for webservices
	 */
	@Override
	public GridInfo3VO clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (GridInfo3VO) super.clone();
	}
	
	public String toWSString(){
		String s = "gridInfoId="+this.gridInfoId +", "+
		"xOffset"+this.xOffset+", "+
		"yOffset="+this.yOffset+", "+
		"dx_mm="+this.dx_mm+", "+
		"dy_mm="+this.dy_mm+", "+
		"steps_x="+this.steps_x+", "+
		"steps_y="+this.steps_y+", "+
		"meshAngle="+this.meshAngle+", ";
		
		return s;
	}
}
