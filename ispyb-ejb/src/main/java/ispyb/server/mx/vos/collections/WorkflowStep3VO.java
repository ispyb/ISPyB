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
import javax.persistence.Table;

import ispyb.server.common.vos.ISPyBValueObject;

@Entity
@Table(name = "WorkflowStep")
public class WorkflowStep3VO extends ISPyBValueObject implements Cloneable {

	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "workflowStepId")
	protected Integer workflowStepId;

	@Column(name = "workflowId")
	protected Integer workflowId;

	@Column(name = "workflowStepType")
	protected String workflowStepType;

	@Column(name = "status")
	protected String status;

	@Column(name = "folderPath")
	protected String folderPath;

	@Column(name = "imageResultFilePath")
	protected String imageResultFilePath;

	@Column(name = "htmlResultFilePath")
	protected String htmlResultFilePath;
	
	@Column(name = "resultFilePath")
	protected String resultFilePath;
	
	@Column(name = "recordTimeStamp")
	protected Date recordTimeStamp;

	@Column(name = "crystalSizeX")
	protected String crystalSizeX;
	
	@Column(name = "crystalSizeY")
	protected String crystalSizeY;
	
	@Column(name = "crystalSizeZ")
	protected String crystalSizeZ;
	
	@Column(name = "maxDozorScore")
	protected String maxDozorScore;
	
	@Column(name = "comments")
	protected String comments;

	public WorkflowStep3VO() {
		super();
	}

	@Override
	public void checkValues(boolean create) throws Exception {
		
	}

	public Integer getWorkflowStepId() {
		return workflowStepId;
	}

	public void setWorkflowStepId(Integer workflowStepId) {
		this.workflowStepId = workflowStepId;
	}

	public Integer getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Integer workflowId) {
		this.workflowId = workflowId;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public String getImageResultFilePath() {
		return imageResultFilePath;
	}

	public void setImageResultFilePath(String imageResultFilePath) {
		this.imageResultFilePath = imageResultFilePath;
	}

	public String getHtmlResultFilePath() {
		return htmlResultFilePath;
	}

	public void setHtmlResultFilePath(String htmlResultFilePath) {
		this.htmlResultFilePath = htmlResultFilePath;
	}

	public String getResultFilePath() {
		return resultFilePath;
	}

	public void setResultFilePath(String resultFilePath) {
		this.resultFilePath = resultFilePath;
	}

	public Date getRecordTimeStamp() {
		return recordTimeStamp;
	}

	public void setRecordTimeStamp(Date recordTimeStamp) {
		this.recordTimeStamp = recordTimeStamp;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getWorkflowStepType() {
		return workflowStepType;
	}

	public void setWorkflowStepType(String workflowStepType) {
		this.workflowStepType = workflowStepType;
	}

	public String getCrystalSizeX() {
		return crystalSizeX;
	}

	public void setCrystalSizeX(String crystalSizeX) {
		this.crystalSizeX = crystalSizeX;
	}

	public String getCrystalSizeY() {
		return crystalSizeY;
	}

	public void setCrystalSizeY(String crystalSizeY) {
		this.crystalSizeY = crystalSizeY;
	}

	public String getCrystalSizeZ() {
		return crystalSizeZ;
	}

	public void setCrystalSizeZ(String crystalSizeZ) {
		this.crystalSizeZ = crystalSizeZ;
	}

	public String getMaxDozorScore() {
		return maxDozorScore;
	}

	public void setMaxDozorScore(String maxDozorScore) {
		this.maxDozorScore = maxDozorScore;
	}

}
