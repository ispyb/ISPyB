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
 * WorkflowMesh3 value object mapping table WorkflowMesh
 * 
 */
@Entity
@Table(name = "WorkflowMesh")
public class WorkflowMesh3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(WorkflowMesh3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "workflowMeshId")
	private Integer workflowMeshId;

	@ManyToOne
	@JoinColumn(name = "workflowId")
	private Workflow3VO workflowVO;
		
	@ManyToOne
	@JoinColumn(name = "bestImageId")
	private Image3VO bestImageVO;
	
	@Column(name = "value1")
	protected Double value1;
	
	@Column(name = "value2")
	protected Double value2;
	
	@Column(name = "value3")
	protected Double value3;
	
	@Column(name = "value4")
	protected Double value4;
	
	@Column(name = "cartographyPath")
	protected String cartographyPath;

	@Column(name = "recordTimeStamp")
	protected Date recordTimeStamp;
	
	
	public WorkflowMesh3VO() {
		super();
	}

	public WorkflowMesh3VO(Integer workflowMeshId, Workflow3VO workflowVO, Image3VO bestImageVO, 
			Double value1, Double value2, Double value3, Double value4, String cartographyPath, Date recordTimeStamp) {
		super();
		this.workflowMeshId = workflowMeshId;
		this.workflowVO = workflowVO;
		this.bestImageVO = bestImageVO;
		this.value1 = value1;
		this.value2 = value2;
		this.value3 = value3;
		this.value4 = value4;
		this.cartographyPath = cartographyPath;
		this.recordTimeStamp = recordTimeStamp;
	}
	
	public WorkflowMesh3VO(WorkflowMesh3VO vo) {
		super();
		this.workflowMeshId = vo.getWorkflowMeshId();
		this.workflowVO = vo.getWorkflowVO();
		this.bestImageVO = vo.getBestImageVO();
		this.value1 = vo.getValue1();
		this.value2 = vo.getValue2();
		this.value3 = vo.getValue3();
		this.value4 = vo.getValue4();
		this.cartographyPath = vo.getCartographyPath();
		this.recordTimeStamp = vo.getRecordTimeStamp();
	}
	
	public void fillVOFromWS(WorkflowMeshWS3VO vo) {
		this.workflowMeshId = vo.getWorkflowMeshId();
		this.workflowVO = null;
		this.bestImageVO = null;
		this.value1 = vo.getValue1();
		this.value2 = vo.getValue2();
		this.value3 = vo.getValue3();
		this.value4 = vo.getValue4();
		this.cartographyPath = vo.getCartographyPath();
		this.recordTimeStamp = vo.getRecordTimeStamp();
	}


	public Integer getWorkflowMeshId() {
		return workflowMeshId;
	}

	public void setWorkflowMeshId(Integer workflowMeshId) {
		this.workflowMeshId = workflowMeshId;
	}

	public Workflow3VO getWorkflowVO() {
		return workflowVO;
	}

	public void setWorkflowVO(Workflow3VO workflowVO) {
		this.workflowVO = workflowVO;
	}

	public Image3VO getBestImageVO() {
		return bestImageVO;
	}

	public void setBestImageVO(Image3VO bestImageVO) {
		this.bestImageVO = bestImageVO;
	}

	public Double getValue1() {
		return value1;
	}

	public void setValue1(Double value1) {
		this.value1 = value1;
	}

	public Double getValue2() {
		return value2;
	}

	public void setValue2(Double value2) {
		this.value2 = value2;
	}

	public Double getValue3() {
		return value3;
	}

	public void setValue3(Double value3) {
		this.value3 = value3;
	}

	public Double getValue4() {
		return value4;
	}

	public void setValue4(Double value4) {
		this.value4 = value4;
	}

	public String getCartographyPath() {
		return cartographyPath;
	}

	public void setCartographyPath(String cartographyPath) {
		this.cartographyPath = cartographyPath;
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
		int maxLengthCartographyPath = 255;
		//workflow
		if(workflowVO == null)
			throw new IllegalArgumentException(StringUtils.getMessageRequiredField("WorkflowMesh", "workflow"));
		// cartographyPath
		if(!StringUtils.isStringLengthValid(this.cartographyPath, maxLengthCartographyPath))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("WorkflowMesh", "cartographyPath", maxLengthCartographyPath));
						
	}

	/**
	 * used to clone an entity to set the linked collectio9ns to null, for webservices
	 */
	@Override
	public WorkflowMesh3VO clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (WorkflowMesh3VO) super.clone();
	}
	
	public String toWSString(){
		String s = "workflowMeshId="+this.workflowMeshId +", "+
		"value1="+this.value1+", "+
		"value2="+this.value2+", "+
		"value3="+this.value3+", "+
		"value4="+this.value4+", "+
		"cartographyPath="+this.cartographyPath+", "+
		"recordTimeStamp="+this.recordTimeStamp;
		
		return s;
	}
}

