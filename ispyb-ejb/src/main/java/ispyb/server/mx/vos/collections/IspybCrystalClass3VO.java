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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * IspybCrystalClass3 value object mapping table IspybCrystalClass
 * 
 */
@Entity
@Table(name = "IspybCrystalClass")
public class IspybCrystalClass3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(IspybCrystalClass3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "crystalClassId")
	protected Integer crystalClassId;
	
	@Column(name = "crystalClass_code")
	protected String crystalClassCode;
	
	@Column(name = "crystalClass_name")
	protected String crystalClassName;

	

	public IspybCrystalClass3VO() {
		super();
	}

	public IspybCrystalClass3VO(Integer crystalClassId,
			String crystalClassCode, String crystalClassName) {
		super();
		this.crystalClassId = crystalClassId;
		this.crystalClassCode = crystalClassCode;
		this.crystalClassName = crystalClassName;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getCrystalClassId() {
		return crystalClassId;
	}

	/**
	 * @param pk The pk to set.
	 */
	public void setCrystalClassId(Integer crystalClassId) {
		this.crystalClassId = crystalClassId;
	}

	

	public String getCrystalClassCode() {
		return crystalClassCode;
	}

	public void setCrystalClassCode(String crystalClassCode) {
		this.crystalClassCode = crystalClassCode;
	}

	public String getCrystalClassName() {
		return crystalClassName;
	}

	public void setCrystalClassName(String crystalClassName) {
		this.crystalClassName = crystalClassName;
	}

	/**
	 * Checks the values of this value object for correctness and
	 * completeness. Should be done before persisting the data in the DB.
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @throws Exception if the data of the value object is not correct
	 */
	public void checkValues(boolean create) throws Exception {
		int maxLengthCrystalClassCode = 20;
		int maxLengthCrystalClassName = 255;
		
		// crystalClassCode
		if(!StringUtils.isStringLengthValid(this.crystalClassCode, maxLengthCrystalClassCode))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("IspybCrystalClass", "crystalClassCode", maxLengthCrystalClassCode));
		// crystalClassName
		if(!StringUtils.isStringLengthValid(this.crystalClassName, maxLengthCrystalClassName))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("IspybCrystalClass", "crystalClassName", maxLengthCrystalClassName));
	}
}

