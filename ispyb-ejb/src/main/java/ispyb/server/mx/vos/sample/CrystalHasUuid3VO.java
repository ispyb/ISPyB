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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CrystalHasUuid3 value object mapping table Crystal_has_UUID
 * 
 */
@Entity
@Table(name = "Crystal_has_UUID")
public class CrystalHasUuid3VO extends ISPyBValueObject implements Cloneable {


	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "crystal_has_UUID_Id")
	private Integer crystalHasUuidId;
	
	@Column(name = "crystalId")
	private Integer crystalId;
	
	@Column(name = "UUID")
	private String uuid;
	
	@Column(name = "imageURL")
	private String imageURL;

	
	
	public CrystalHasUuid3VO() {
		super();
	}

	public CrystalHasUuid3VO(Integer crystalHasUuidId, Integer crystalId,
			String uuid, String imageURL) {
		super();
		this.crystalHasUuidId = crystalHasUuidId;
		this.crystalId = crystalId;
		this.uuid = uuid;
		this.imageURL = imageURL;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getCrystalHasUuidId() {
		return crystalHasUuidId;
	}

	/**
	 * @param pk The pk to set.
	 */
	public void setCrystalHasUuidId(Integer crystalHasUuidId) {
		this.crystalHasUuidId = crystalHasUuidId;
	}


	public Integer getCrystalId() {
		return crystalId;
	}

	public void setCrystalId(Integer crystalId) {
		this.crystalId = crystalId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	

	
	/**
	 * Checks the values of this value object for correctness and
	 * completeness. Should be done before persisting the data in the DB.
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @throws Exception if the data of the value object is not correct
	 */
	public void checkValues(boolean create) throws Exception {
		//TODO
	}
}

