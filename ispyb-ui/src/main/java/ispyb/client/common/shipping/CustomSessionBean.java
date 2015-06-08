/*******************************************************************************
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
 ******************************************************************************/
package ispyb.client.common.shipping;

import ispyb.common.util.Constants;
import ispyb.server.mx.vos.collections.Session3VO;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class CustomSessionBean implements Comparable<CustomSessionBean> {

	private Integer sessionId;

	private Timestamp startDate;

	private String beamLineName;

	// --------------------
	public CustomSessionBean(Integer sessionId, Timestamp startDate, String beamLineName) {
		this.sessionId = sessionId;
		this.startDate = startDate;
		this.beamLineName = beamLineName;
	}

	public CustomSessionBean(Session3VO session) {
		this.sessionId = session.getSessionId();
		this.startDate = (Timestamp) session.getStartDate();
		this.beamLineName = session.getBeamlineName();
	}

	// --------------------
	public int compareTo(CustomSessionBean o) {
		if(this.getStartDate() == null)
			return o.getStartDate() == null ? 0 : 1;
		if(o.getStartDate() == null)
			return -1;
		return this.getStartDate().compareTo(o.getStartDate());
	}

	// --------------------
	public String getSessionDescription() {
		SimpleDateFormat dateStandard = new SimpleDateFormat(Constants.DATE_FORMAT);
		String dateFormated = "";
		if(getStartDate() != null)
			dateFormated = dateStandard.format(getStartDate().getTime());
		return dateFormated + " " + getBeamLineName();
	}

	// --------------------
	@Override
	public boolean equals(Object other) {

		// If it's not the correct type, clearly it isn't equal to this.
		if (!(other instanceof CustomSessionBean)) {
			return false;
		}

		return equals((CustomSessionBean) other);
	}

	/**
	 * This class is not using strict ordering. This means that the object is not Comparable, and each check for
	 * equality will test all members for equality. We do not check collections for equality however, so you would be
	 * wise to not use this if you have collection typed EJB References.
	 */
	public boolean equals(CustomSessionBean that) {

		// try to get lucky.
		if (this == that) {
			return true;
		}
		// this clearly isn't null.
		if (null == that) {
			return false;
		}

		if (this.sessionId != that.sessionId) {

			if (this.sessionId == null || that.sessionId == null) {
				return false;
			}

			if (!this.sessionId.equals(that.sessionId)) {
				return false;
			}

		}

		if (this.startDate != that.startDate) {

			if (this.startDate == null || that.startDate == null) {
				return false;
			}

			if (!this.startDate.equals(that.startDate)) {
				return false;
			}

		}

		if (this.beamLineName != that.beamLineName) {

			if (this.beamLineName == null || that.beamLineName == null) {
				return false;
			}

			if (!this.beamLineName.equals(that.beamLineName)) {
				return false;
			}

		}

		return true;

	}

	// --------------------
	public Integer getSessionId() {
		return sessionId;
	}

	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getBeamLineName() {
		return beamLineName;
	}

	public void setBeamLineName(String beamLineName) {
		this.beamLineName = beamLineName;
	}
}
