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

package ispyb.server.mx.vos.sample;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.log4j.Logger;

import ispyb.common.util.StringUtils;
import ispyb.server.common.vos.ISPyBValueObject;
import ispyb.server.mx.vos.collections.Position3VO;

/**
 * BLSubSample3 value object mapping table BLSubSample
 * 
 */
@Entity
@Table(name = "BLSubSample")
public class BLSubSample3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(BLSubSample3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "blSubSampleId")
	protected Integer blSubSampleId;

	@ManyToOne
	@JoinColumn(name = "blSampleId")
	private BLSample3VO blSampleVO;
	
	@ManyToOne
	@JoinColumn(name = "diffractionPlanId")
	private DiffractionPlan3VO diffractionPlanVO;

	@ManyToOne
	@JoinColumn(name = "positionId")
	protected Position3VO positionVO;
	
	@ManyToOne
	@JoinColumn(name = "position2Id")
	private Position3VO position2VO;
	
	@Column(name = "blSubSampleUUID")
	protected String blSubSampleUUID;
	
	@Column(name = "imgFileName")
	protected String imgFileName;
	
	@Column(name = "imgFilePath")
	protected String imgFilePath;
	
	@Column(name = "comments")
	protected String comments;
	
	@Column(name = "recordTimeStamp")
	private Date recordTimeStamp;
		
	public BLSubSample3VO() {
		super();
	}

	public BLSubSample3VO(Integer blSubSampleId, BLSample3VO blSampleVO,
			DiffractionPlan3VO diffractionPlanVO, Position3VO positionVO, Position3VO position2VO,
			String blSubSampleUUID, String imgFileName, String imgFilePath,
			String comments, Date recordTimeStamp) {
		super();
		this.blSubSampleId = blSubSampleId;
		this.blSampleVO = blSampleVO;
		this.diffractionPlanVO = diffractionPlanVO;
		this.positionVO = positionVO;
		this.position2VO = position2VO;
		this.blSubSampleUUID = blSubSampleUUID;
		this.imgFileName = imgFileName;
		this.imgFilePath = imgFilePath;
		this.comments = comments;
		this.recordTimeStamp = recordTimeStamp;
	}
	
	public BLSubSample3VO(BLSubSample3VO vo) {
		super();
		this.blSubSampleId = vo.getBlSubSampleId();
		this.blSampleVO = vo.getBlSampleVO();
		this.diffractionPlanVO = vo.getDiffractionPlanVO();
		this.positionVO = vo.getPositionVO();
		this.position2VO = vo.getPosition2VO();
		this.blSubSampleUUID = vo.getBlSubSampleUUID();
		this.imgFileName = vo.getImgFileName();
		this.imgFilePath = vo.getImgFilePath();
		this.comments = vo.getComments();
		this.recordTimeStamp = vo.getRecordTimeStamp();
	}
	
	public void fillVOFromWS(BLSubSampleWS3VO vo) {
		this.blSubSampleId = vo.getBlSubSampleId();
		this.blSubSampleUUID = vo.getBlSubSampleUUID();
		this.imgFileName = vo.getImgFileName();
		this.imgFilePath = vo.getImgFilePath();
		this.comments = vo.getComments();
		this.recordTimeStamp = vo.getRecordTimeStamp();
	}

	public Integer getBlSubSampleId() {
		return blSubSampleId;
	}

	public void setBlSubSampleId(Integer blSubSampleId) {
		this.blSubSampleId = blSubSampleId;
	}

	public BLSample3VO getBlSampleVO() {
		return blSampleVO;
	}

	public void setBlSampleVO(BLSample3VO blSampleVO) {
		this.blSampleVO = blSampleVO;
	}

	public DiffractionPlan3VO getDiffractionPlanVO() {
		return diffractionPlanVO;
	}

	public void setDiffractionPlanVO(DiffractionPlan3VO diffractionPlanVO) {
		this.diffractionPlanVO = diffractionPlanVO;
	}

	public Position3VO getPositionVO() {
		return positionVO;
	}

	public void setPositionVO(Position3VO positionVO) {
		this.positionVO = positionVO;
	}

	public Position3VO getPosition2VO() {
		return position2VO;
	}

	public void setPosition2VO(Position3VO position2vo) {
		position2VO = position2vo;
	}

	public String getBlSubSampleUUID() {
		return blSubSampleUUID;
	}

	public void setBlSubSampleUUID(String blSubSampleUUID) {
		this.blSubSampleUUID = blSubSampleUUID;
	}

	public String getImgFileName() {
		return imgFileName;
	}

	public void setImgFileName(String imgFileName) {
		this.imgFileName = imgFileName;
	}

	public String getImgFilePath() {
		return imgFilePath;
	}

	public void setImgFilePath(String imgFilePath) {
		this.imgFilePath = imgFilePath;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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
		int maxLengthBLSubSampleUUID = 45;
		int maxLengthImgFileName = 255;
		int maxLengthImgFilePath = 1024;
		int maxLengthComments = 1024;
		
		//blSample
		if(blSampleVO == null)
			throw new IllegalArgumentException(StringUtils.getMessageRequiredField("BLSubSample", "sample"));
		// blSubSampleUUID
		if(!StringUtils.isStringLengthValid(this.blSubSampleUUID, maxLengthBLSubSampleUUID))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("BLSubSample", "blSubSampleUUID", maxLengthBLSubSampleUUID));
		// imgFileName
		if(!StringUtils.isStringLengthValid(this.imgFileName, maxLengthImgFileName))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("BLSubSample", "imgFileName", maxLengthImgFileName));
		// imgFilePath
		if(!StringUtils.isStringLengthValid(this.imgFilePath, maxLengthImgFilePath))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("BLSubSample", "imgFilePath", maxLengthImgFilePath));
		// comments
		if(!StringUtils.isStringLengthValid(this.comments, maxLengthComments))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("BLSubSample", "comments", maxLengthComments));
		
	}

	/**
	 * used to clone an entity to set the linked collectio9ns to null, for webservices
	 */
	@Override
	public BLSubSample3VO clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (BLSubSample3VO) super.clone();
	}
	
	public String toWSString(){
		String s = "blSubSampleId="+this.blSubSampleId +", "+
		"blSubSampleUUID="+this.blSubSampleUUID+", "+
		"imgFileName="+this.imgFileName+", "+
		"imgFilePath="+this.imgFilePath+", "+
		"recordTimeStamp="+this.recordTimeStamp+", "+
		"comments="+this.comments+", ";
		
		return s;
	}
}
