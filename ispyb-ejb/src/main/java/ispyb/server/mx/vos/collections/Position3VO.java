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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ispyb.server.common.vos.ISPyBValueObject;

import org.apache.log4j.Logger;

/**
 * Position3 value object mapping table Position
 * 
 */
@Entity
@Table(name = "Position")
public class Position3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(Position3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "positionId")
	protected Integer positionId;

	@ManyToOne
	@JoinColumn(name = "relativePositionId")
	private Position3VO relativePositionVO;
	
	@Column(name = "posX")
	protected Double posX;
	
	@Column(name = "posY")
	protected Double posY;
	
	@Column(name = "posZ")
	protected Double posZ;
	
	@Column(name = "scale")
	protected Double scale;
	
	@Column(name = "recordTimeStamp")
	protected Date recordTimeStamp;
	

	public Position3VO() {
		super();
	}

	public Position3VO(Integer positionId, Position3VO relativePositionVO,
			Double posX, Double posY, Double posZ, Double scale, Date recordTimeStamp) {
		super();
		this.positionId = positionId;
		this.relativePositionVO = relativePositionVO;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.scale = scale;
		this.recordTimeStamp = recordTimeStamp;
	}
	
	public Position3VO(Position3VO vo) {
		super();
		this.positionId = vo.getPositionId();
		this.relativePositionVO = vo.getRelativePositionVO();
		this.posX = vo.getPosX();
		this.posY = vo.getPosY();
		this.posZ = vo.getPosZ();
		this.scale = vo.getScale();
		this.recordTimeStamp = vo.getRecordTimeStamp();
	}
	
	public void fillVOFromWS(PositionWS3VO vo) {
		this.positionId = vo.getPositionId();
		this.posX = vo.getPosX();
		this.posY = vo.getPosY();
		this.posZ = vo.getPosZ();
		this.scale = vo.getScale();
		this.recordTimeStamp = vo.getRecordTimeStamp();
	}



	public Integer getPositionId() {
		return positionId;
	}

	public void setPositionId(Integer positionId) {
		this.positionId = positionId;
	}

	public Position3VO getRelativePositionVO() {
		return relativePositionVO;
	}

	public void setRelativePositionVO(Position3VO relativePositionVO) {
		this.relativePositionVO = relativePositionVO;
	}

	public Double getPosX() {
		return posX;
	}

	public void setPosX(Double posX) {
		this.posX = posX;
	}

	public Double getPosY() {
		return posY;
	}

	public void setPosY(Double posY) {
		this.posY = posY;
	}

	public Double getPosZ() {
		return posZ;
	}

	public void setPosZ(Double posZ) {
		this.posZ = posZ;
	}

	public Double getScale() {
		return scale;
	}

	public void setScale(Double scale) {
		this.scale = scale;
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
		
	}

	/**
	 * used to clone an entity to set the linked collectio9ns to null, for webservices
	 */
	@Override
	public Position3VO clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (Position3VO) super.clone();
	}
	
	public String toWSString(){
		String s = "positionId="+this.positionId +", "+
		"posX="+this.posX+", "+
		"posY="+this.posY+", "+
		"posZ="+this.posZ+", "+
		"scale="+this.scale+", "+
		"recordTimeStamp="+this.recordTimeStamp+", ";
		
		return s;
	}
}
