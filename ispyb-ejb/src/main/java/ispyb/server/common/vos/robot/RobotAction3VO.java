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

package ispyb.server.common.vos.robot;

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

import ispyb.server.common.vos.ISPyBValueObject;
import ispyb.server.mx.vos.collections.DataCollectionGroupWS3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.sample.BLSample3VO;

/**
 * Person3 value object mapping table RobotAction
 * 
 */
@Entity
@Table(name = "RobotAction")
@SqlResultSetMapping(name = "robotNativeQuery", entities = { @EntityResult(entityClass = RobotAction3VO.class) })

public class RobotAction3VO extends ISPyBValueObject implements Cloneable {
	
	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "robotActionId")
	protected Integer robotActionId;

	@ManyToOne
	@JoinColumn(name = "blsessionId")
	private Session3VO sessionVO;
	
	@ManyToOne
	@JoinColumn(name = "blsampleId")
	private BLSample3VO blSampleVO;

	//TODO put an enum ?
	@Column(name = "actionType")
	protected String actionType;

	@Column(name = "startTimestamp")
	protected Date startTime;

	@Column(name = "endTimestamp")
	protected Date endTime;

	//TODO put an enum ?
	@Column(name = "status")
	protected String status;
	
	@Column(name = "message")
	protected String message;
	
	@Column(name = "containerLocation")
	protected Integer containerLocation;

	@Column(name = "dewarLocation")
	protected Integer dewarLocation;

	@Column(name = "sampleBarcode")
	protected String sampleBarcode;

	@Column(name = "xtalSnapshotBefore")
	protected String xtalSnapshotBefore;

	@Column(name = "xtalSnapshotAfter")
	protected String xtalSnapshotAfter;

	
	public RobotAction3VO() {
		super();
	}

	public RobotAction3VO(Integer robotActionId, Session3VO sessionVO, BLSample3VO blSampleVO, String actionType, Date startTime,
			Date endTime, String status, String message, Integer containerLocation, Integer dewarLocation, String sampleBarcode,
			String xtalSnapshotBefore, String xtalSnapshotAfter) {
		super();
		this.robotActionId = robotActionId;
		this.sessionVO = sessionVO;
		this.blSampleVO = blSampleVO;
		this.actionType = actionType;
		this.startTime = startTime;
		this.endTime = endTime;
		this.status = status;
		this.message = message;
		this.containerLocation = containerLocation;
		this.dewarLocation = dewarLocation;
		this.sampleBarcode = sampleBarcode;
		this.xtalSnapshotBefore = xtalSnapshotBefore;
		this.xtalSnapshotAfter = xtalSnapshotAfter;
	}
	
	public RobotAction3VO(RobotAction3VO vo) {
		super();
		this.robotActionId = vo.getRobotActionId();
		this.sessionVO = vo.getSessionVO();
		this.blSampleVO = vo.getBlSampleVO();
		this.actionType = vo.getActionType();
		this.startTime = vo.getStartTime();
		this.endTime = vo.getEndTime();
		this.status = vo.getStatus();
		this.message = vo.getMessage();
		this.containerLocation = vo.getContainerLocation();
		this.dewarLocation = vo.getDewarLocation();
		this.sampleBarcode = vo.getSampleBarcode();
		this.xtalSnapshotBefore = vo.getXtalSnapshotBefore();
		this.xtalSnapshotAfter = vo.getXtalSnapshotAfter();
	}

	public Integer getRobotActionId() {
		return robotActionId;
	}

	public void setRobotActionId(Integer robotActionId) {
		this.robotActionId = robotActionId;
	}

	public Session3VO getSessionVO() {
		return sessionVO;
	}

	public void setSessionVO(Session3VO sessionVO) {
		this.sessionVO = sessionVO;
	}

	public BLSample3VO getBlSampleVO() {
		return blSampleVO;
	}

	public void setBlSampleVO(BLSample3VO blSampleVO) {
		this.blSampleVO = blSampleVO;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getContainerLocation() {
		return containerLocation;
	}

	public void setContainerLocation(Integer containerLocation) {
		this.containerLocation = containerLocation;
	}

	public Integer getDewarLocation() {
		return dewarLocation;
	}

	public void setDewarLocation(Integer dewarLocation) {
		this.dewarLocation = dewarLocation;
	}

	public String getSampleBarcode() {
		return sampleBarcode;
	}

	public void setSampleBarcode(String sampleBarcode) {
		this.sampleBarcode = sampleBarcode;
	}

	public String getXtalSnapshotBefore() {
		return xtalSnapshotBefore;
	}

	public void setXtalSnapshotBefore(String xtalSnapshotBefore) {
		this.xtalSnapshotBefore = xtalSnapshotBefore;
	}

	public String getXtalSnapshotAfter() {
		return xtalSnapshotAfter;
	}

	public void setXtalSnapshotAfter(String xtalSnapshotAfter) {
		this.xtalSnapshotAfter = xtalSnapshotAfter;
	}

	public void fillVOFromWS(RobotActionWS3VO vo) {
		this.robotActionId = vo.getRobotActionId();
		this.actionType = vo.getActionType();
		this.startTime = vo.getStartTime();
		this.endTime = vo.getEndTime();
		this.status = vo.getStatus();
		this.message = vo.getMessage();
		this.containerLocation = vo.getContainerLocation();
		this.dewarLocation = vo.getDewarLocation();
		this.sampleBarcode = vo.getSampleBarcode();
		this.xtalSnapshotBefore = vo.getXtalSnapshotBefore();
		this.xtalSnapshotAfter = vo.getXtalSnapshotAfter();
	}
	
	@Override
	public void checkValues(boolean create) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
