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

package ispyb.server.common.vos.proposals;

import ispyb.server.common.vos.ISPyBValueObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * Laboratory3 value object mapping table Laboratory
 * 
 */
@Entity
@Table(name = "Laboratory")
@SqlResultSetMapping(name = "laboratoryNativeQuery", entities = { @EntityResult(entityClass = Laboratory3VO.class) })
public class Laboratory3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(Laboratory3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "laboratoryId")
	protected Integer laboratoryId;
	
	@Column(name = "laboratoryUUID")
	protected String laboratoryUUID;
	
	@Column(name = "name")
	protected String name;
	
	@Column(name = "address")
	protected String address;
	
	@Column(name = "city")
	protected String city;
	
	@Column(name = "country")
	protected String country;
	
	@Column(name = "url")
	protected String url;
	
	@Column(name = "organization")
	protected String organization;

	@Column(name = "laboratoryExtPk")
	protected Integer laboratoryExtPk;


	public Laboratory3VO(Integer laboratoryId, String laboratoryUUID,
			String name, String address, String city, String country,
			String url, String organization, Integer laboratoryExtPk) {
		super();
		this.laboratoryId = laboratoryId;
		this.laboratoryUUID = laboratoryUUID;
		this.name = name;
		this.address = address;
		this.city = city;
		this.country = country;
		this.url = url;
		this.organization = organization;
		this.laboratoryExtPk = laboratoryExtPk;
	}
	
	

	public Laboratory3VO() {
		super();
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}



	/**
	 * @return Returns the pk.
	 */
	public Integer getLaboratoryId() {
		return laboratoryId;
	}

	/**
	 * @param pk The pk to set.
	 */
	public void setLaboratoryId(Integer laboratoryId) {
		this.laboratoryId = laboratoryId;
	}

	

	public String getLaboratoryUUID() {
		return laboratoryUUID;
	}

	public void setLaboratoryUUID(String laboratoryUUID) {
		this.laboratoryUUID = laboratoryUUID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}	

	public Integer getLaboratoryExtPk() {
		return laboratoryExtPk;
	}

	public void setLaboratoryExtPk(Integer laboratoryExtPk) {
		this.laboratoryExtPk = laboratoryExtPk;
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
