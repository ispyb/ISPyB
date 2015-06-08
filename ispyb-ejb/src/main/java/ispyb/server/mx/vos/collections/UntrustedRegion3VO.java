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
 * UntrustedRegion3 value object mapping table UntrustedRegion
 * 
 */
@Entity
@Table(name = "UntrustedRegion")
public class UntrustedRegion3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(UntrustedRegion3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "untrustedRegionId")
	private Integer untrustedRegionId;

	@ManyToOne
	@JoinColumn(name = "detectorId")
	private Detector3VO detectorVO;
	
	@Column(name = "x1")
	private Integer x1;
	
	@Column(name = "x2")
	private Integer x2;
	
	@Column(name = "y1")
	private Integer y1;
	
	@Column(name = "y2")
	private Integer y2;

	
	
	public UntrustedRegion3VO() {
		super();
	}
	
	public UntrustedRegion3VO(UntrustedRegion3VO vo){
		super();
		this.untrustedRegionId = vo.getUntrustedRegionId();
		this.detectorVO = vo.getDetectorVO();
		this.x1 = vo.getX1();
		this.x2 = vo.getX2();
		this.y1 = vo.getY1();
		this.y2 = vo.getY2();
	}

	public UntrustedRegion3VO(Integer untrustedRegionId,
			Detector3VO detectorVO, Integer x1, Integer x2, Integer y1, Integer y2) {
		super();
		this.untrustedRegionId = untrustedRegionId;
		this.detectorVO = detectorVO;
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}

	public void fillVOFromWS(UntrustedRegionWS3VO vo) {
		this.untrustedRegionId = vo.getUntrustedRegionId();
		this.detectorVO = null;
		this.x1 = vo.getX1();
		this.x2 = vo.getX2();
		this.y1 = vo.getY1();
		this.y2 = vo.getY2();
	}

	public Integer getUntrustedRegionId() {
		return untrustedRegionId;
	}

	public void setUntrustedRegionId(Integer untrustedRegionId) {
		this.untrustedRegionId = untrustedRegionId;
	}

	public Detector3VO getDetectorVO() {
		return detectorVO;
	}

	public void setDetectorVO(Detector3VO detectorVO) {
		this.detectorVO = detectorVO;
	}
	
	public Integer getDetectorVOId() {
		return detectorVO == null ? null: detectorVO.getDetectorId();
	}
	
	public Integer getX1() {
		return x1;
	}

	public void setX1(Integer x1) {
		this.x1 = x1;
	}

	public Integer getX2() {
		return x2;
	}

	public void setX2(Integer x2) {
		this.x2 = x2;
	}

	public Integer getY1() {
		return y1;
	}

	public void setY1(Integer y1) {
		this.y1 = y1;
	}

	public Integer getY2() {
		return y2;
	}

	public void setY2(Integer y2) {
		this.y2 = y2;
	}

	/**
	 * Checks the values of this value object for correctness and
	 * completeness. Should be done before persisting the data in the DB.
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @throws Exception if the data of the value object is not correct
	 */
	public void checkValues(boolean create) throws Exception {
		//detector
		if(detectorVO == null)
			throw new IllegalArgumentException(StringUtils.getMessageRequiredField("UntrustedRegion", "detectorVO"));
		
	}

	/**
	 * used to clone an entity to set the linked collectio9ns to null, for webservices
	 */
	@Override
	public UntrustedRegion3VO clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (UntrustedRegion3VO) super.clone();
	}
	
	public String toWSString(){
		String s = "untrustedRegionId="+this.untrustedRegionId +", "+
		"x1="+this.x1+", "+
		"x2="+this.x2+", "+
		"y1="+this.y1+", "+
		"y2="+this.y2;
		
		return s;
	}
	
}
