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

package ispyb.server.common.vos.shipping;

import ispyb.server.common.vos.ISPyBValueObject;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * DewarTransportHistory3 value object mapping table DewarTransportHistory
 * 
 */
@Entity
@Table(name = "DewarTransportHistory")
public class DewarTransportHistory3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(DewarTransportHistory3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "dewarTransportHistoryId")
	private Integer dewarTransportHistoryId;

	@ManyToOne
	@JoinColumn(name = "dewarId")
	private Dewar3VO dewarVO;

	@Column(name = "dewarStatus")
	private String dewarStatus;

	@Column(name = "storageLocation")
	private String storageLocation;

	@Column(name = "arrivalDate")
	private Date arrivalDate;

	public DewarTransportHistory3VO() {
		super();
	}

	public DewarTransportHistory3VO(Integer dewarTransportHistoryId, Dewar3VO dewarVO, String dewarStatus,
			String storageLocation, Date arrivalDate) {
		super();
		this.dewarTransportHistoryId = dewarTransportHistoryId;
		this.dewarVO = dewarVO;
		this.dewarStatus = dewarStatus;
		this.storageLocation = storageLocation;
		this.arrivalDate = arrivalDate;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getDewarTransportHistoryId() {
		return dewarTransportHistoryId;
	}

	/**
	 * @param pk
	 *            The pk to set.
	 */
	public void setDewarTransportHistoryId(Integer dewarTransportHistoryId) {
		this.dewarTransportHistoryId = dewarTransportHistoryId;
	}

	public Integer getDewarVOId() {
		return this.dewarVO.getDewarId();
	}

	public Dewar3VO getDewarVO() {
		return dewarVO;
	}

	public void setDewarVO(Dewar3VO dewarVO) {
		this.dewarVO = dewarVO;
	}

	public String getDewarStatus() {
		return dewarStatus;
	}

	public void setDewarStatus(String dewarStatus) {
		this.dewarStatus = dewarStatus;
	}

	public String getStorageLocation() {
		return storageLocation;
	}

	public void setStorageLocation(String storageLocation) {
		this.storageLocation = storageLocation;
	}

	public Date getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
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
	}
}
