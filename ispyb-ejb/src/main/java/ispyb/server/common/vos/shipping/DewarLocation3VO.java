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
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * DewarLocation3 value object mapping table DewarLocation
 * 
 */
@Entity
@Table(name = "DewarLocation")
public class DewarLocation3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(DewarLocation3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "eventId")
	private Integer eventId;
	
	@Column(name = "dewarNumber")
	private String dewarNumber;
	
	@Column(name = "userId")
	private String userId;
	
	@Column(name = "dateTime")
	private Date dateTime;
	
	@Column(name = "locationName")
	private String locationName;
	
	@Column(name = "courierName")
	private String courierName;
	
	@Column(name = "courierTrackingNumber")
	private String courierTrackingNumber;

	
	
	public DewarLocation3VO() {
		super();
	}

	public DewarLocation3VO(Integer eventId, String dewarNumber, String userId,
			Date dateTime, String locationName, String courierName,
			String courierTrackingNumber) {
		super();
		this.eventId = eventId;
		this.dewarNumber = dewarNumber;
		this.userId = userId;
		this.dateTime = dateTime;
		this.locationName = locationName;
		this.courierName = courierName;
		this.courierTrackingNumber = courierTrackingNumber;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getEventId() {
		return eventId;
	}

	/**
	 * @param pk The pk to set.
	 */
	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	
	
	public String getDewarNumber() {
		return dewarNumber;
	}

	public void setDewarNumber(String dewarNumber) {
		this.dewarNumber = dewarNumber;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getCourierName() {
		return courierName;
	}

	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}

	public String getCourierTrackingNumber() {
		return courierTrackingNumber;
	}

	public void setCourierTrackingNumber(String courierTrackingNumber) {
		this.courierTrackingNumber = courierTrackingNumber;
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
