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

import ispyb.common.util.Constants;
import ispyb.common.util.StringUtils;
import ispyb.server.common.vos.ISPyBValueObject;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * Workflow3 value object mapping table Workflow the resultFilePath contains the
 * path of the directory where the result web pages from the workflow are
 * stored. ISPyB displays all index.html that are stored in this directory or
 * subdirectory, sorted by alphabetical order.
 * 
 */
@Entity
@Table(name = "Workflow")
public class Workflow3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(Workflow3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and
	// enter it here
	// this prevents later invalid class version exceptions when the value
	// object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "workflowId")
	protected Integer workflowId;

	@Column(name = "workflowTitle")
	protected String workflowTitle;

	@Column(name = "workflowType")
	protected String workflowType;

	@Column(name = "comments")
	protected String comments;

	@Column(name = "status")
	protected String status;

	@Column(name = "resultFilePath")
	protected String resultFilePath;

	@Column(name = "logFilePath")
	protected String logFilePath;

	@Column(name = "recordTimeStamp")
	protected Date recordTimeStamp;

	public Workflow3VO() {
		super();
	}

	public Workflow3VO(Integer workflowId, String workflowTitle, String workflowType, String comments, String status,
			String resultFilePath, String logFilePath, Date recordTimeStamp) {
		super();
		this.workflowId = workflowId;
		this.workflowTitle = workflowTitle;
		this.workflowType = workflowType;
		this.comments = comments;
		this.status = status;
		this.resultFilePath = resultFilePath;
		this.logFilePath = logFilePath;
		this.recordTimeStamp = recordTimeStamp;
	}

	public Workflow3VO(Workflow3VO vo) {
		super();
		this.workflowId = vo.getWorkflowId();
		this.workflowTitle = vo.getWorkflowTitle();
		this.workflowType = vo.getWorkflowType();
		this.comments = vo.getComments();
		this.status = vo.getStatus();
		this.resultFilePath = vo.getResultFilePath();
		this.logFilePath = vo.getLogFilePath();
		this.recordTimeStamp = vo.getRecordTimeStamp();
	}

	public Integer getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Integer workflowId) {
		this.workflowId = workflowId;
	}

	public String getWorkflowTitle() {
		return workflowTitle;
	}

	public void setWorkflowTitle(String workflowTitle) {
		this.workflowTitle = workflowTitle;
	}

	public String getWorkflowType() {
		return workflowType;
	}

	public void setWorkflowType(String workflowType) {
		this.workflowType = workflowType;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResultFilePath() {
		return resultFilePath;
	}

	public void setResultFilePath(String resultFilePath) {
		this.resultFilePath = resultFilePath;
	}

	public String getLogFilePath() {
		return logFilePath;
	}

	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}

	public Date getRecordTimeStamp() {
		return recordTimeStamp;
	}

	public void setRecordTimeStamp(Date recordTimeStamp) {
		this.recordTimeStamp = recordTimeStamp;
	}

	/**
	 * Checks the values of this value object for correctness and completeness.
	 * Should be done before persisting the data in the DB.
	 * 
	 * @param create
	 *            should be true if the value object is just being created in
	 *            the DB, this avoids some checks like testing the primary key
	 * @throws Exception
	 *             if the data of the value object is not correct
	 */
	public void checkValues(boolean create) throws Exception {
		int maxLengthWorkflowTitle = 255;
		int maxLengthComments = 1024;
		int maxLengthStatus = 255;
		int maxLengthResultFilePath = 255;
		int maxLengthLogFilePath = 255;
//
//		String[] listWorkflowType = { "BioSAXS Post Processing", "Undefined", "EnhancedCharacterisation", "LineScan",
//				"MeshScan", "XrayCentering", "DiffractionTomography", "Dehydration", "BurnStrategy",
//				"KappaReorientation", "TroubleShooting", "VisualReorientation", "Massif1", "Massif2",
//				"HelicalCharacterisation", "GroupedProcessing", "MXPressE", "MXPressO", "MXPressL", "MXScore",
//				"MXPressI","MXPressM","MXPressA","CollectAndSpectra","LowDoseDC"};

		// workflowTitle
		if (!StringUtils.isStringLengthValid(this.workflowTitle, maxLengthWorkflowTitle))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("Workflow", "workflowTitle",
					maxLengthWorkflowTitle));
		// workflowType
//		if (!StringUtils.isStringInPredefinedList(this.workflowType, listWorkflowType, true))
//			throw new IllegalArgumentException(StringUtils.getMessageErrorPredefinedList("Workflow", "workflowType",
//					listWorkflowType));
		// comments
		if (!StringUtils.isStringLengthValid(this.comments, maxLengthComments))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("Workflow", "comments",
					maxLengthComments));
		// status
		if (!StringUtils.isStringLengthValid(this.status, maxLengthStatus))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("Workflow", "status",
					maxLengthStatus));
		// resultFilePath
		if (!StringUtils.isStringLengthValid(this.resultFilePath, maxLengthResultFilePath))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("Workflow", "resultFilePath",
					maxLengthResultFilePath));
		// logFilePath
		if (!StringUtils.isStringLengthValid(this.logFilePath, maxLengthLogFilePath))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("Workflow", "logFilePath",
					maxLengthLogFilePath));
	}

	/**
	 * used to clone an entity to set the linked collectio9ns to null, for
	 * webservices
	 */
	@Override
	public Workflow3VO clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (Workflow3VO) super.clone();
	}

	public String toWSString() {
		String s = "workflowId=" + this.workflowId + ", " + "workflowTitle=" + this.workflowTitle + ", "
				+ "workflowType=" + this.workflowType + ", " + "comments=" + this.comments + ", " + "status="
				+ this.status + ", " + "resultFilePath=" + this.resultFilePath + ", " + "logFilePath="
				+ this.logFilePath + ", " + "recordTimeStamp=" + this.recordTimeStamp + ", ";

		return s;
	}

	public boolean isMXPress() {
		return this.workflowType != null
				&& (this.workflowType.equals(Constants.WORKFLOW_MXPRESSE)
						|| this.workflowType.equals(Constants.WORKFLOW_MXPRESSO)
						|| this.workflowType.equals(Constants.WORKFLOW_MXPRESSL) || this.workflowType
							.equals(Constants.WORKFLOW_MXScore));
	}

	public boolean isMXPressOI(){
		return this.workflowType != null
				&& ( this.workflowType.equals(Constants.WORKFLOW_MXPRESSO)
						|| this.workflowType.equals(Constants.WORKFLOW_MXPRESSI)
						);
	}
	
	public boolean isMXPressEA(){
		return this.workflowType != null
				&& (this.workflowType.equals(Constants.WORKFLOW_MXPRESSE)						
						|| this.workflowType.equals(Constants.WORKFLOW_MXPRESSA));
	}
	
	public boolean isMXPressEOIA(){
		return this.workflowType != null
				&& (this.workflowType.equals(Constants.WORKFLOW_MXPRESSE)
						|| this.workflowType.equals(Constants.WORKFLOW_MXPRESSO)
						|| this.workflowType.equals(Constants.WORKFLOW_MXPRESSI)
						|| this.workflowType.equals(Constants.WORKFLOW_MXPRESSA));
	}
	
	public boolean isMeshMXpressM(){
		return this.workflowType != null
				&& (this.workflowType.equals(Constants.WORKFLOW_MXPRESSM)
						|| this.workflowType.equals(Constants.WORKFLOW_MESHSCAN));
	}

}
