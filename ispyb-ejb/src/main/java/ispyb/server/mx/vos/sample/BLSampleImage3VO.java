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

import ispyb.server.common.vos.ISPyBValueObject;
import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.mx.vos.collections.EnergyScan3VO;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * BLSampleImage3 value object mapping table BLSampleImage
 * 
 */
@Entity
@Table(name = "BLSampleImage")
@SqlResultSetMapping(name = "blSampleImageNativeQuery", entities = { @EntityResult(entityClass = BLSampleImage3VO.class) })
public class BLSampleImage3VO extends ISPyBValueObject implements Cloneable {

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "blSampleImageId")
	protected Integer blSampleImageId;

	@ManyToOne
	@JoinColumn(name = "blSampleId")
	private BLSample3VO blsampleVO;

	@Column(name = "micronsPerPixelX")
	protected Double micronsPerPixelX;

	@Column(name = "micronsPerPixelY")
	protected Double micronsPerPixelY;

	@Column(name = "imageFullPath")
	protected String imageFullPath;

	@Column(name = "blSampleImageScoreId")
	protected Integer blSampleImageScoreId;

	@Column(name = "comments")
	protected String comments;
	
	@Column(name = "containerInspectionId")
	protected Integer containerInspectionId;

	public BLSampleImage3VO() {
		super();
	}	

	public BLSampleImage3VO(Integer blSampleImageId, BLSample3VO blsampleVO, Double micronsPerPixelX,
			Double micronsPerPixelY, String imageFullPath, Integer blSampleImageScoreId, String comments,
			Integer containerInspectionId) {
		super();
		this.blSampleImageId = blSampleImageId;
		this.blsampleVO = blsampleVO;
		this.micronsPerPixelX = micronsPerPixelX;
		this.micronsPerPixelY = micronsPerPixelY;
		this.imageFullPath = imageFullPath;
		this.blSampleImageScoreId = blSampleImageScoreId;
		this.comments = comments;
		this.containerInspectionId = containerInspectionId;
	}

	public BLSampleImage3VO(BLSampleImage3VO vo) {
		this.blSampleImageId = vo.getBlSampleImageId();
		this.blsampleVO = vo.getBlsampleVO();
		this.micronsPerPixelX = vo.getMicronsPerPixelX();
		this.micronsPerPixelY = vo.getMicronsPerPixelY();
		this.imageFullPath = vo.getImageFullPath();
		this.blSampleImageScoreId = vo.getBlSampleImageScoreId();
		this.comments = vo.getComments();
		this.containerInspectionId = vo.getContainerInspectionId();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	public Integer getBlSampleImageId() {
		return blSampleImageId;
	}

	public void setBlSampleImageId(Integer blSampleImageId) {
		this.blSampleImageId = blSampleImageId;
	}

	public BLSample3VO getBlsampleVO() {
		return blsampleVO;
	}

	public void setBlsampleVO(BLSample3VO blsampleVO) {
		this.blsampleVO = blsampleVO;
	}

	public Double getMicronsPerPixelX() {
		return micronsPerPixelX;
	}

	public void setMicronsPerPixelX(Double micronsPerPixelX) {
		this.micronsPerPixelX = micronsPerPixelX;
	}

	public Double getMicronsPerPixelY() {
		return micronsPerPixelY;
	}

	public void setMicronsPerPixelY(Double micronsPerPixelY) {
		this.micronsPerPixelY = micronsPerPixelY;
	}

	public String getImageFullPath() {
		return imageFullPath;
	}

	public void setImageFullPath(String imageFullPath) {
		this.imageFullPath = imageFullPath;
	}

	public Integer getBlSampleImageScoreId() {
		return blSampleImageScoreId;
	}

	public void setBlSampleImageScoreId(Integer blSampleImageScoreId) {
		this.blSampleImageScoreId = blSampleImageScoreId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Integer getContainerInspectionId() {
		return containerInspectionId;
	}

	public void setContainerInspectionId(Integer containerInspectionId) {
		this.containerInspectionId = containerInspectionId;
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
		return;
	}
	
	@Override
	public String toString(){
		String s = "blSampleImageId="+this.blSampleImageId +", "+
		"imagepath="+this.imageFullPath;		
		return s;
	}

	public void fillVOFromWS(BLSampleImageWS3VO vo) {
		
		this.blSampleImageId = vo.getBlSampleImageId();
		this.micronsPerPixelX = vo.getMicronsPerPixelX();
		this.micronsPerPixelY = vo.getMicronsPerPixelY();
		this.imageFullPath = vo.getImageFullPath();
		this.blSampleImageScoreId = vo.getBlSampleImageScoreId();
		this.comments = vo.getComments();
		this.containerInspectionId = vo.getContainerInspectionId();
		
	}
	
}
