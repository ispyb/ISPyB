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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import ispyb.common.util.StringUtils;
import ispyb.server.common.vos.ISPyBValueObject;

import org.apache.log4j.Logger;

/**
 * PhasingProgram3 value object mapping table PhasingProgram
 * 
 */
@Entity
@Table(name="PhasingProgramRun")
@SqlResultSetMapping(name = "phasingProgramRunNativeQuery", entities = { @EntityResult(entityClass = PhasingProgramRun3VO.class) })
public class PhasingProgramRun3VO extends ISPyBValueObject implements Cloneable {
	
	private final static Logger LOG = Logger.getLogger(PhasingProgramRun3VO.class);
	
	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;
	
	@Id
	@GeneratedValue
	@Column(name="phasingProgramRunId")	
	protected Integer phasingProgramRunId;
	
	@Column(name = "phasingCommandLine")
	protected String phasingCommandLine;
	
	@Column(name = "phasingPrograms")
	protected String phasingPrograms;
	
	@Column(name = "phasingStatus")
	protected Boolean phasingStatus;

	@Column(name = "phasingMessage")
	protected String phasingMessage;
	
	@Column(name = "phasingStartTime")
	protected Date phasingStartTime;
	
	@Column(name = "phasingEndTime")
	protected Date phasingEndTime;
	
	@Column(name = "phasingEnvironment")
	protected String phasingEnvironment;
	
	@Column(name = "phasingDirectory")
	protected String phasingDirectory;

	@Column(name = "recordTimeStamp")
	protected Date recordTimeStamp;
	
	@OneToMany
	@JoinColumn(name = "phasingProgramRunId")
	private Set<PhasingProgramAttachment3VO> attachmentVOs;
	
	public PhasingProgramRun3VO() {
		super();
	}
	
	

	public PhasingProgramRun3VO(Integer phasingProgramRunId,
			String phasingCommandLine, String phasingPrograms,
			Boolean phasingStatus, String phasingMessage,
			Date phasingStartTime, Date phasingEndTime,
			String phasingEnvironment, Date recordTimeStamp) {
		super();
		this.phasingProgramRunId = phasingProgramRunId;
		this.phasingCommandLine = phasingCommandLine;
		this.phasingPrograms = phasingPrograms;
		this.phasingStatus = phasingStatus;
		this.phasingMessage = phasingMessage;
		this.phasingStartTime = phasingStartTime;
		this.phasingEndTime = phasingEndTime;
		this.phasingEnvironment = phasingEnvironment;
		this.recordTimeStamp = recordTimeStamp;
	}



	public Integer getPhasingProgramRunId() {
		return phasingProgramRunId;
	}

	public void setPhasingProgramRunId(Integer phasingProgramRunId) {
		this.phasingProgramRunId = phasingProgramRunId;
	}

	public String getPhasingCommandLine() {
		return phasingCommandLine;
	}

	public void setPhasingCommandLine(String phasingCommandLine) {
		this.phasingCommandLine = phasingCommandLine;
	}

	public String getPhasingPrograms() {
		return phasingPrograms;
	}

	public void setPhasingPrograms(String phasingPrograms) {
		this.phasingPrograms = phasingPrograms;
	}

	public Boolean getPhasingStatus() {
		return phasingStatus;
	}

	public void setPhasingStatus(Boolean phasingStatus) {
		this.phasingStatus = phasingStatus;
	}

	public String getPhasingMessage() {
		return phasingMessage;
	}

	public void setPhasingMessage(String phasingMessage) {
		this.phasingMessage = phasingMessage;
	}

	public Date getPhasingStartTime() {
		return phasingStartTime;
	}

	public void setPhasingStartTime(Date phasingStartTime) {
		this.phasingStartTime = phasingStartTime;
	}

	public Date getPhasingEndTime() {
		return phasingEndTime;
	}

	public void setPhasingEndTime(Date phasingEndTime) {
		this.phasingEndTime = phasingEndTime;
	}

	public String getPhasingEnvironment() {
		return phasingEnvironment;
	}

	public void setPhasingEnvironment(String phasingEnvironment) {
		this.phasingEnvironment = phasingEnvironment;
	}

	public String getPhasingDirectory() {
		return phasingEnvironment;
	}

	public void setPhasingDirectory(String phasingDirectory) {
		this.phasingDirectory = phasingDirectory;
	}

	public Date getRecordTimeStamp() {
		return recordTimeStamp;
	}

	public void setRecordTimeStamp(Date recordTimeStamp) {
		this.recordTimeStamp = recordTimeStamp;
	}

	public Set<PhasingProgramAttachment3VO> getAttachmentVOs() {
		return attachmentVOs;
	}

	public void setAttachmentVOs(Set<PhasingProgramAttachment3VO> attachmentVOs) {
		this.attachmentVOs = attachmentVOs;
	}
	
	public PhasingProgramAttachment3VO[] getAttachmentTabVOs() {
		return this.attachmentVOs == null ? null : attachmentVOs.toArray(new PhasingProgramAttachment3VO[this.attachmentVOs.size()]);
	}
	
	public ArrayList<PhasingProgramAttachment3VO> getAttachmentListVOs() {
		return this.attachmentVOs == null ? null : new ArrayList<PhasingProgramAttachment3VO>(Arrays.asList(getAttachmentTabVOs()));
	}


	/**
	 * Checks the values of this value object for correctness and
	 * completeness. Should be done before persisting the data in the DB.
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @throws Exception if the data of the value object is not correct
	 */
	public void checkValues(boolean create) throws Exception {
		int maxLengthPhasingCommandLine = 255;
		int maxLengthPhasingPrograms = 255;
		int maxLengthPhasingMessage= 255;
		int maxLengthPhasingEnvironment = 255;
		// phasingCommandLine
		if(!StringUtils.isStringLengthValid(this.phasingCommandLine, maxLengthPhasingCommandLine))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("PhasingProgram", "phasingCommandLine", maxLengthPhasingCommandLine));
		// phasingPrograms
		if(!StringUtils.isStringLengthValid(this.phasingPrograms, maxLengthPhasingPrograms))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("PhasingProgram", "phasingPrograms", maxLengthPhasingPrograms));
		// phasingMessage
		if(!StringUtils.isStringLengthValid(this.phasingMessage, maxLengthPhasingMessage))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("PhasingProgram", "phasingMessage", maxLengthPhasingMessage));
		// phasingEnvironment
		if(!StringUtils.isStringLengthValid(this.phasingEnvironment, maxLengthPhasingEnvironment))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("PhasingProgram", "phasingEnvironment", maxLengthPhasingEnvironment));
		
	}
	
	/**
	 * used to clone an entity to set the linked collectio9ns to null, for webservices
	 */
	@Override
	public PhasingProgramRun3VO clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (PhasingProgramRun3VO) super.clone();
	}
	
	public String toWSString(){
		String s = "phasingProgramRunId="+this.phasingProgramRunId +", "+
		"phasingCommandLine="+this.phasingCommandLine+", "+
		"phasingPrograms="+this.phasingPrograms+", "+
		"phasingStatus="+this.phasingStatus+", "+
		"phasingMessage="+this.phasingMessage+", "+
		"phasingStartTime="+this.phasingStartTime+", "+
		"phasingEndTime="+this.phasingEndTime+", "+
		"phasingEnvironment="+this.phasingEnvironment+", "+
		"recordTimeStamp="+this.recordTimeStamp;
		
		return s;
	}
}
