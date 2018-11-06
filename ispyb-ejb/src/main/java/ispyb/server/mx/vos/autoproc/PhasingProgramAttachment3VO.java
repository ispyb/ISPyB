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
 * PhasingProgramAttachment3 value object mapping table PhasingProgramAttachment
 * 
 */
@Entity
@Table(name = "PhasingProgramAttachment")
public class PhasingProgramAttachment3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(PhasingProgramAttachment3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "phasingProgramAttachmentId")
	protected Integer phasingProgramAttachmentId;

	@ManyToOne
	@JoinColumn(name = "phasingProgramRunId")
	private PhasingProgramRun3VO phasingProgramRunVO;
	
	@Column(name = "fileType")
	protected String fileType;
	
	@Column(name = "fileName")
	protected String fileName;
	
	@Column(name = "filePath")
	protected String filePath;
	
	@Column(name = "input")
	protected Byte input;	
	
	@Column(name = "recordTimeStamp")
	protected Date recordTimeStamp;
	
	
	public PhasingProgramAttachment3VO() {
		super();
	}

	public PhasingProgramAttachment3VO(Integer phasingProgramAttachmentId,
			PhasingProgramRun3VO phasingProgramRunVO, String fileType,
			String fileName, String filePath, Date recordTimeStamp) {
		super();
		this.phasingProgramAttachmentId = phasingProgramAttachmentId;
		this.phasingProgramRunVO = phasingProgramRunVO;
		this.fileType = fileType;
		this.fileName = fileName;
		this.filePath = filePath;
		this.recordTimeStamp = recordTimeStamp;
	}
	
	public void  fillVOFromWS(PhasingProgramAttachmentWS3VO vo) {
		this.phasingProgramAttachmentId = vo.getPhasingProgramAttachmentId();
		this.phasingProgramRunVO = null;
		this.fileType = vo.getFileType();
		this.fileName = vo.getFileName();
		this.filePath = vo.getFilePath();
		this.recordTimeStamp = vo.getRecordTimeStamp();
	}
	
	

	public Integer getPhasingProgramAttachmentId() {
		return phasingProgramAttachmentId;
	}

	public void setPhasingProgramAttachmentId(Integer phasingProgramAttachmentId) {
		this.phasingProgramAttachmentId = phasingProgramAttachmentId;
	}

	public PhasingProgramRun3VO getPhasingProgramRunVO() {
		return phasingProgramRunVO;
	}

	public void setPhasingProgramRunVO(PhasingProgramRun3VO phasingProgramRunVO) {
		this.phasingProgramRunVO = phasingProgramRunVO;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
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
		/*String[] listFileType = {"Map", "Logfile"};
		int maxLengthFileName = 45;
		int maxLengthFilePath = 255;
		//phasingProgram
		if(phasingProgramRunVO == null)
			throw new IllegalArgumentException(StringUtils.getMessageRequiredField("PhasingProgramAttachment", "phasingProgramRunVO"));
		// fileType
		if(!StringUtils.isStringInPredefinedList(this.fileType, listFileType, true))
			throw new IllegalArgumentException(StringUtils.getMessageErrorPredefinedList("PhasingProgramAttachment", "fileType", listFileType));
		// fileName
		if(!StringUtils.isStringLengthValid(this.fileName, maxLengthFileName))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("PhasingProgramAttachment", "fileName", maxLengthFileName));
		// filePath
		if(!StringUtils.isStringLengthValid(this.filePath, maxLengthFilePath))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("PhasingProgramAttachment", "filePath", maxLengthFilePath));
		*/
	}

	/**
	 * used to clone an entity to set the linked collectio9ns to null, for webservices
	 */
	@Override
	public PhasingProgramAttachment3VO clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (PhasingProgramAttachment3VO) super.clone();
	}
	
	public String toWSString(){
		String s = "phasingProgramAttachmentId="+this.phasingProgramAttachmentId +", "+
		"fileType="+this.fileType+", "+
		"fileName="+this.fileName+", "+
		"filePath="+this.filePath+", "+
		"recordTimeStamp="+this.recordTimeStamp;
		
		return s;
	}

	public Byte getInput() {
		return input;
	}

	public void setInput(Byte input) {
		this.input = input;
	}
}
