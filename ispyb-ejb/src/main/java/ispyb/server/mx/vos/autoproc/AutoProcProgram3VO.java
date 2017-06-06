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

import ispyb.server.common.vos.ISPyBValueObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * AutoProcProgram3 value object mapping table AutoProcProgram
 * 
 */
@Entity
@Table(name = "AutoProcProgram")
public class AutoProcProgram3VO extends ISPyBValueObject {

	private final static Logger LOG = Logger.getLogger(AutoProcProgram3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "autoProcProgramId")
	protected Integer autoProcProgramId;

	@Column(name = "processingCommandLine")
	protected String processingCommandLine;

	@Column(name = "processingPrograms")
	protected String processingPrograms;

	@Column(name = "processingStatus")
	protected String processingStatus;

	@Column(name = "processingMessage")
	protected String processingMessage;

	@Column(name = "processingStartTime")
	protected Date processingStartTime;

	@Column(name = "processingEndTime")
	protected Date processingEndTime;

	@Column(name = "processingEnvironment")
	protected String processingEnvironment;

	@Column(name = "recordTimeStamp")
	protected Date recordTimeStamp;

	@OneToMany
	@JoinColumn(name = "autoProcProgramId")
	protected Set<AutoProcProgramAttachment3VO> attachmentVOs;

	public AutoProcProgram3VO() {
		super();
	}

	public AutoProcProgram3VO(Integer autoProcProgramId, String processingCommandLine, String processingPrograms,
			String processingStatus, String processingMessage, Date processingStartTime, Date processingEndTime,
			String processingEnvironment, Date recordTimeStamp) {
		super();
		this.autoProcProgramId = autoProcProgramId;
		this.processingCommandLine = processingCommandLine;
		this.processingPrograms = processingPrograms;
		this.processingStatus = processingStatus;
		this.processingMessage = processingMessage;
		this.processingStartTime = processingStartTime;
		this.processingEndTime = processingEndTime;
		this.processingEnvironment = processingEnvironment;
		this.recordTimeStamp = recordTimeStamp;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getAutoProcProgramId() {
		return autoProcProgramId;
	}

	/**
	 * @param pk
	 *            The pk to set.
	 */
	public void setAutoProcProgramId(Integer autoProcProgramId) {
		this.autoProcProgramId = autoProcProgramId;
	}

	public String getProcessingCommandLine() {
		return processingCommandLine;
	}

	public void setProcessingCommandLine(String processingCommandLine) {
		this.processingCommandLine = processingCommandLine;
	}

	public String getProcessingPrograms() {
		return processingPrograms;
	}

	public void setProcessingPrograms(String processingPrograms) {
		this.processingPrograms = processingPrograms;
	}

	public String getProcessingStatus() {
		return processingStatus;
	}

	public void setProcessingStatus(String processingStatus) {
		this.processingStatus = processingStatus;
	}

	public String getProcessingMessage() {
		return processingMessage;
	}

	public void setProcessingMessage(String processingMessage) {
		this.processingMessage = processingMessage;
	}

	public Date getProcessingStartTime() {
		return processingStartTime;
	}

	public void setProcessingStartTime(Date processingStartTime) {
		this.processingStartTime = processingStartTime;
	}

	public Date getProcessingEndTime() {
		return processingEndTime;
	}

	public void setProcessingEndTime(Date processingEndTime) {
		this.processingEndTime = processingEndTime;
	}

	public String getProcessingEnvironment() {
		return processingEnvironment;
	}

	public void setProcessingEnvironment(String processingEnvironment) {
		this.processingEnvironment = processingEnvironment;
	}

	public Date getRecordTimeStamp() {
		return recordTimeStamp;
	}

	public void setRecordTimeStamp(Date recordTimeStamp) {
		this.recordTimeStamp = recordTimeStamp;
	}

	public Set<AutoProcProgramAttachment3VO> getAttachmentVOs() {
		return attachmentVOs;
	}

	public void setAttachmentVOs(Set<AutoProcProgramAttachment3VO> attachmentVOs) {
		this.attachmentVOs = attachmentVOs;
	}
	
	public AutoProcProgramAttachment3VO[] getAttachmentTabVOs() {
		return this.attachmentVOs == null ? null : attachmentVOs.toArray(new AutoProcProgramAttachment3VO[this.attachmentVOs.size()]);
	}
	
	public ArrayList<AutoProcProgramAttachment3VO> getAttachmentListVOs() {
		return this.attachmentVOs == null ? null : new ArrayList<AutoProcProgramAttachment3VO>(Arrays.asList(getAttachmentTabVOs()));
	}

	/**
	 * Checks the values of this value object for correctness and completeness. Should be done before persisting the
	 * data in the DB.
	 * 
	 * @param create
	 *            should be true if the value object is just being created in the DB, this avoids some checks like
	 *            testing the primary key
	 * @throws Exception
	 *             if the data of the value object is not correct
	 */
	@Override
	public void checkValues(boolean create) throws Exception {
		// TODO
	}
	
	public String toWSString(){
		String s = "autoProcProgramId="+this.autoProcProgramId +", "+
		"processingCommandLine="+this.processingCommandLine+", "+
		"processingPrograms="+this.processingPrograms+", "+
		"processingStatus="+this.processingStatus+", "+
		"processingMessage="+this.processingMessage+", "+
		"processingStartTime="+this.processingStartTime+", "+
		"processingEndTime="+this.processingEndTime+", "+
		"processingEnvironment="+this.processingEnvironment+", "+
		"recordTimeStamp="+this.recordTimeStamp;
		
		return s;
	}
}
