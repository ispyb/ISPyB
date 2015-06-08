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

package ispyb.server.common.vos.admin;

import ispyb.server.common.vos.ISPyBValueObject;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * AdminActivity3 value object mapping table AdminActivity
 * 
 */
@Entity
@Table(name = "AdminActivity")
public class AdminActivity3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(AdminActivity3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "adminActivityId")
	private Integer adminActivityId;

	@Column(name = "username")
	private String username;

	@Column(name = "action")
	private String action;

	@Column(name = "comments")
	private String comments;

	@Column(name = "dateTime")
	private Date dateTime;

	public Integer getAdminActivityId() {
		return adminActivityId;
	}

	public void setAdminActivityId(Integer adminActivityId) {
		this.adminActivityId = adminActivityId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
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

	/**
	 * used to clone an entity to set the linked collections to null, for webservices
	 */
	@Override
	public AdminActivity3VO clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (AdminActivity3VO) super.clone();
	}
}
