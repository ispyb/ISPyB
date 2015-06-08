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

import ispyb.common.util.StringUtils;
import ispyb.server.common.vos.ISPyBValueObject;

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

import org.apache.log4j.Logger;

/**
 * AutoProcProgramAttachment3 value object mapping table AutoProcProgramAttachment
 * 
 */
@Entity
@Table(name = "AutoProcProgramAttachment")
@SqlResultSetMapping(name = "autoProcProgramAttachmentNativeQuery", entities = { @EntityResult(entityClass = AutoProcProgramAttachment3VO.class) })
public class AutoProcProgramAttachment3VO extends ISPyBValueObject implements Cloneable  {

	private final static Logger LOG = Logger.getLogger(AutoProcProgramAttachment3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "autoProcProgramAttachmentId")
	protected Integer autoProcProgramAttachmentId;
	
	@ManyToOne
	@JoinColumn(name = "autoProcProgramId")
	private AutoProcProgram3VO autoProcProgramVO;
	
	@Column(name = "fileType")
	protected String fileType;
	
	@Column(name = "fileName")
	protected String fileName;
	
	@Column(name = "filePath")
	protected String filePath;
	
	@Column(name = "recordTimeStamp")
	protected Date recordTimeStamp;
	
	public AutoProcProgramAttachment3VO(){
		super();
	}
	

	

	public AutoProcProgramAttachment3VO(Integer autoProcProgramAttachmentId,
			AutoProcProgram3VO autoProcProgramVO, String fileType,
			String fileName, String filePath, Date recordTimeStamp) {
		super();
		this.autoProcProgramAttachmentId = autoProcProgramAttachmentId;
		this.autoProcProgramVO = autoProcProgramVO;
		this.fileType = fileType;
		this.fileName = fileName;
		this.filePath = filePath;
		this.recordTimeStamp = recordTimeStamp;
	}
	
	public AutoProcProgramAttachment3VO(AutoProcProgramAttachment3VO vo) {
		super();
		this.autoProcProgramAttachmentId = vo.getAutoProcProgramAttachmentId();
		this.autoProcProgramVO = vo.getAutoProcProgramVO();
		this.fileType = vo.getFileType();
		this.fileName = vo.getFileName();
		this.filePath = vo.getFilePath();
		this.recordTimeStamp = vo.getRecordTimeStamp();
	}

	public void fillVOFromWS(AutoProcProgramAttachmentWS3VO vo) {
		this.autoProcProgramAttachmentId = vo.getAutoProcProgramAttachmentId();
		this.autoProcProgramVO = null;
		this.fileType = vo.getFileType();
		this.fileName = vo.getFileName();
		this.filePath = vo.getFilePath();
		this.recordTimeStamp = vo.getRecordTimeStamp();
	}


	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getAutoProcProgramAttachmentId() {
		return autoProcProgramAttachmentId;
	}

	/**
	 * @param pk The pk to set.
	 */
	public void setAutoProcProgramAttachmentId(Integer autoProcProgramAttachmentId) {
		this.autoProcProgramAttachmentId = autoProcProgramAttachmentId;
	}

	

	public AutoProcProgram3VO getAutoProcProgramVO() {
		return autoProcProgramVO;
	}




	public void setAutoProcProgramVO(AutoProcProgram3VO autoProcProgramVO) {
		this.autoProcProgramVO = autoProcProgramVO;
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
	
	public Integer getAutoProcProgramVOId() {
		return autoProcProgramVO == null ? null : autoProcProgramVO.getAutoProcProgramId();
	}

	/**
	 * Checks the values of this value object for correctness and
	 * completeness. Should be done before persisting the data in the DB.
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @throws Exception if the data of the value object is not correct
	 */
	public void checkValues(boolean create) throws Exception {
		int maxLengthFileName = 255;
		int maxLengthFilePath = 255;
		String[] listFileType = {"Log", "Result", "Graph"};
		// autoProcProgramVO
		if (autoProcProgramVO == null)
			throw new IllegalArgumentException(StringUtils.getMessageRequiredField("AutoProcProgramAttachment", "autoProcProgram"));
		// fileType
		if(!StringUtils.isStringInPredefinedList(this.fileType, listFileType, true))
			throw new IllegalArgumentException(StringUtils.getMessageErrorPredefinedList("AutoProcProgramAttachment", "fileType", listFileType));
		// fileName
		if (!StringUtils.isStringLengthValid(this.fileName, maxLengthFileName))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("AutoProcProgramAttachment", "fileName",
					maxLengthFileName));
		// filePath
		if (!StringUtils.isStringLengthValid(this.filePath, maxLengthFilePath))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("AutoProcProgramAttachment", "filePath",
					maxLengthFilePath));
		
	}
	
	public String toWSString(){
		String s = "autoProcProgramAttachmentId="+this.autoProcProgramAttachmentId +", "+
		"fileType="+this.fileType+", "+
		"fileName="+this.fileName+", "+
		"filePath="+this.filePath+", "+
		"recordTimeStamp="+this.recordTimeStamp;
		
		return s;
	}
}

