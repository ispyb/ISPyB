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

import ispyb.common.util.StringUtils;
import ispyb.server.common.vos.ISPyBValueObject;

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
 * DataCollectionFileAttachment3 value object mapping table DataCollectionFileAttachment
 * 
 */
@Entity
@Table(name = "DataCollectionFileAttachment")
@SqlResultSetMapping(name = "DataCollectionFileAttachmentNativeQuery", entities = { @EntityResult(entityClass = DataCollectionFileAttachment3VO.class) })
public class DataCollectionFileAttachment3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(DataCollectionFileAttachment3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "dataCollectionFileAttachmentId")
	protected Integer dataCollectionFileAttachmentId;
	
	@ManyToOne
	@JoinColumn(name = "dataCollectionId")
	private DataCollection3VO dataCollectionVO;
		
	@Column(name = "fileType")
	protected String fileType;
		
	@Column(name = "fileFullPath")
	protected String fileFullPath;
		
	public DataCollectionFileAttachment3VO() {
		super();
	}

	

	public DataCollectionFileAttachment3VO(Integer dataCollectionFileAttachmentId, DataCollection3VO dataCollectionVO,
			String fileType, String fileFullPath) {
		super();
		this.dataCollectionFileAttachmentId = dataCollectionFileAttachmentId;
		this.dataCollectionVO = dataCollectionVO;
		this.fileType = fileType;
		this.fileFullPath = fileFullPath;
	}
	
	public DataCollectionFileAttachment3VO(DataCollectionFileAttachment3VO vo) {
		this.dataCollectionFileAttachmentId = vo.getDataCollectionFileAttachmentId();
		this.dataCollectionVO = vo.getDataCollectionVO();
		this.fileType = vo.getFileType();
		this.fileFullPath = vo.getFileFullPath();
	}
	
//	public  void fillVOFromWS(DataCollectionFileAttachmentWS3VO vo) {
//		this.dataCollectionFileAttachmentId = vo.getDataCollectionFileAttachmentId();
//		this.dataCollectionVO = null;
//		this.fileType = vo.getFileType();
//		this.fileFullPath = vo.getFileFullPath();
//	}



	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
	public Integer getDataCollectionFileAttachmentId() {
		return dataCollectionFileAttachmentId;
	}

	public void setDataCollectionFileAttachmentId(Integer dataCollectionFileAttachmentId) {
		this.dataCollectionFileAttachmentId = dataCollectionFileAttachmentId;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileFullPath() {
		return fileFullPath;
	}

	public void setFileFullPath(String fileFullPath) {
		this.fileFullPath = fileFullPath;
	}


	public DataCollection3VO getDataCollectionVO() {
		return dataCollectionVO;
	}

	public void setDataCollectionVO(DataCollection3VO dataCollectionVO) {
		this.dataCollectionVO = dataCollectionVO;
	}

	public Integer getDataCollectionVOId() {
		return dataCollectionVO == null ? null : dataCollectionVO.getDataCollectionId();
	}

	
	/**
	 * Checks the values of this value object for correctness and
	 * completeness. Should be done before persisting the data in the DB.
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @throws Exception if the data of the value object is not correct
	 */
	public void checkValues(boolean create) throws Exception {
		int maxLengthFileType = 255;
		int maxLengthFileFullPath = 255;
		
		//dataCollection
		if(dataCollectionVO == null)
			throw new IllegalArgumentException(StringUtils.getMessageRequiredField("DataCollectionFileAttachment", "dataCollection"));
		// fileName
		if(!StringUtils.isStringLengthValid(this.fileType, maxLengthFileType))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("DataCollectionFileAttachment", "fileType", maxLengthFileType));
		// fileFullPath
		if(!StringUtils.isStringLengthValid(this.fileFullPath, maxLengthFileFullPath))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("DataCollectionFileAttachment", "fileFullPath", maxLengthFileFullPath));
	}
	
	public String toWSString(){
		String s = "DataCollectionFileAttachmentId= " + this.dataCollectionFileAttachmentId +
		" fileType=" + this.fileType + " fileFullPath= " + this.fileFullPath;
		
		return s;
	}

}
