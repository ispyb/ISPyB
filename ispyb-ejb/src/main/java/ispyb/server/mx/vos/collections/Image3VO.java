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
 * Image3 value object mapping table Image
 * 
 */
@Entity
@Table(name = "Image")
@SqlResultSetMapping(name = "imageNativeQuery", entities = { @EntityResult(entityClass = Image3VO.class) })
public class Image3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(Image3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "imageId")
	protected Integer imageId;
	
	@ManyToOne
	@JoinColumn(name = "dataCollectionId")
	private DataCollection3VO dataCollectionVO;
	
	@Column(name = "imageNumber")
	protected Integer imageNumber;
	
	@Column(name = "fileName")
	protected String fileName;
	
	@Column(name = "fileLocation")
	protected String fileLocation;
	
	@Column(name = "measuredIntensity")
	protected Float measuredIntensity;
	
	@Column(name = "jpegFileFullPath")
	protected String jpegFileFullPath;
	
	@Column(name = "jpegThumbnailFileFullPath")
	protected String jpegThumbnailFileFullPath;
	
	@Column(name = "temperature")
	protected Float temperature;
	
	@Column(name = "cumulativeIntensity")
	protected Float cumulativeIntensity;
	
	@Column(name = "synchrotronCurrent")
	protected Float synchrotronCurrent;
	
	@Column(name = "comments")
	protected String comments;
	
	@Column(name = "machineMessage")
	protected String machineMessage;

	
	public Image3VO() {
		super();
	}

	

	public Image3VO(Integer imageId, DataCollection3VO dataCollectionVO,
			Integer imageNumber, String fileName, String fileLocation,
			Float measuredIntensity, String jpegFileFullPath,
			String jpegThumbnailFileFullPath, Float temperature,
			Float cumulativeIntensity, Float synchrotronCurrent,
			String comments, String machineMessage) {
		super();
		this.imageId = imageId;
		this.dataCollectionVO = dataCollectionVO;
		this.imageNumber = imageNumber;
		this.fileName = fileName;
		this.fileLocation = fileLocation;
		this.measuredIntensity = measuredIntensity;
		this.jpegFileFullPath = jpegFileFullPath;
		this.jpegThumbnailFileFullPath = jpegThumbnailFileFullPath;
		this.temperature = temperature;
		this.cumulativeIntensity = cumulativeIntensity;
		this.synchrotronCurrent = synchrotronCurrent;
		this.comments = comments;
		this.machineMessage = machineMessage;
	}
	
	public Image3VO(Image3VO vo) {
		this.imageId = vo.getImageId();
		this.dataCollectionVO = vo.getDataCollectionVO();
		this.imageNumber = vo.getImageNumber();
		this.fileName = vo.getFileName();
		this.fileLocation = vo.getFileLocation();
		this.measuredIntensity = vo.getMeasuredIntensity();
		this.jpegFileFullPath = vo.getJpegFileFullPath();
		this.jpegThumbnailFileFullPath = vo.getJpegThumbnailFileFullPath();
		this.temperature = vo.getTemperature();
		this.cumulativeIntensity = vo.getCumulativeIntensity();
		this.synchrotronCurrent = vo.getSynchrotronCurrent();
		this.comments = vo.getComments();
		this.machineMessage = vo.getMachineMessage();
	}
	
	public  void fillVOFromWS(ImageWS3VO vo) {
		this.imageId = vo.getImageId();
		this.dataCollectionVO = null;
		this.imageNumber = vo.getImageNumber();
		this.fileName = vo.getFileName();
		this.fileLocation = vo.getFileLocation();
		this.measuredIntensity = vo.getMeasuredIntensity();
		this.jpegFileFullPath = vo.getJpegFileFullPath();
		this.jpegThumbnailFileFullPath = vo.getJpegThumbnailFileFullPath();
		this.temperature = vo.getTemperature();
		this.cumulativeIntensity = vo.getCumulativeIntensity();
		this.synchrotronCurrent = vo.getSynchrotronCurrent();
		this.comments = vo.getComments();
		this.machineMessage = vo.getMachineMessage();
	}



	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getImageId() {
		return imageId;
	}

	/**
	 * @param pk The pk to set.
	 */
	public void setImageId(Integer imageId) {
		this.imageId = imageId;
	}

	
	
	public DataCollection3VO getDataCollectionVO() {
		return dataCollectionVO;
	}


	public void setDataCollectionVO(DataCollection3VO dataCollectionVO) {
		this.dataCollectionVO = dataCollectionVO;
	}

	public Integer getImageNumber() {
		return imageNumber;
	}

	public void setImageNumber(Integer imageNumber) {
		this.imageNumber = imageNumber;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public Float getMeasuredIntensity() {
		return measuredIntensity;
	}

	public void setMeasuredIntensity(Float measuredIntensity) {
		this.measuredIntensity = measuredIntensity;
	}

	public String getJpegFileFullPath() {
		return jpegFileFullPath;
	}

	public void setJpegFileFullPath(String jpegFileFullPath) {
		this.jpegFileFullPath = jpegFileFullPath;
	}

	public String getJpegThumbnailFileFullPath() {
		return jpegThumbnailFileFullPath;
	}

	public void setJpegThumbnailFileFullPath(String jpegThumbnailFileFullPath) {
		this.jpegThumbnailFileFullPath = jpegThumbnailFileFullPath;
	}

	public Float getTemperature() {
		return temperature;
	}

	public void setTemperature(Float temperature) {
		this.temperature = temperature;
	}

	public Float getCumulativeIntensity() {
		return cumulativeIntensity;
	}

	public void setCumulativeIntensity(Float cumulativeIntensity) {
		this.cumulativeIntensity = cumulativeIntensity;
	}

	public Float getSynchrotronCurrent() {
		return synchrotronCurrent;
	}

	public void setSynchrotronCurrent(Float synchrotronCurrent) {
		this.synchrotronCurrent = synchrotronCurrent;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getMachineMessage() {
		return machineMessage;
	}

	public void setMachineMessage(String machineMessage) {
		this.machineMessage = machineMessage;
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
		int maxLengthFileName = 255;
		int maxLengthFileLocation = 255;
		int maxLengthJpegFileFullPath = 255;
		int maxLengthJepgThumbnailFileFullPath = 255;
		int maxLengthComments = 1024;
		int maxLengthMachineMessage = 1024;
		
		//dataCollection
		if(dataCollectionVO == null)
			throw new IllegalArgumentException(StringUtils.getMessageRequiredField("Image", "dataCollection"));
		// fileName
		if(!StringUtils.isStringLengthValid(this.fileName, maxLengthFileName))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("Image", "fileName", maxLengthFileName));
		// fileLocation
		if(!StringUtils.isStringLengthValid(this.fileLocation, maxLengthFileLocation))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("Image", "fileLocation", maxLengthFileLocation));
		// jpegFileFullPath
		if(!StringUtils.isStringLengthValid(this.jpegFileFullPath, maxLengthJpegFileFullPath))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("Image", "jpegFileFullPath", maxLengthJpegFileFullPath));
		// jpegThumbnailFileFullPath
		if(!StringUtils.isStringLengthValid(this.jpegThumbnailFileFullPath, maxLengthJepgThumbnailFileFullPath))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("Image", "jpegThumbnailFileFullPath", maxLengthJepgThumbnailFileFullPath));
		// comments
		if(!StringUtils.isStringLengthValid(this.comments, maxLengthComments))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("Image", "comments", maxLengthComments));
		// machineMessage
		if(!StringUtils.isStringLengthValid(this.machineMessage, maxLengthMachineMessage))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("Image", "machineMessage", maxLengthMachineMessage));
		
	}
	
	public String toWSString(){
		String s = "imageId="+this.imageId +", "+
		"imageNumber="+this.imageNumber+", "+
		"fileName="+this.fileName+", "+
		"imageNumber="+this.imageNumber+", "+
		"fileLocation="+this.fileLocation+", "+
		"measuredIntensity="+this.measuredIntensity+", "+
		"jpegFileFullPath="+this.jpegFileFullPath+", "+
		"jpegThumbnailFileFullPath="+this.jpegThumbnailFileFullPath+", "+
		"temperature="+this.temperature+", "+
		"cumulativeIntensity="+this.cumulativeIntensity+", "+
		"synchrotronCurrent="+this.synchrotronCurrent+", "+
		"comments="+this.comments+", "+
		"machineMessage="+this.machineMessage;
		
		return s;
	}

}
