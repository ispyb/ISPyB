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
/*
 * ProposalUtils.java
 * @author patrice.brenchereau@esrf.fr
 * September 17, 2008
 */

package ispyb.client.common.proposal;

import ispyb.common.util.Constants;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.vos.collections.Session3VO;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.ejb.FinderException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class ProposalUtils {

	private final static Logger LOG = Logger.getLogger(ProposalUtils.class);

	/**
	 * getSessionsAvailable for the current proposal
	 * 
	 * @param request
	 * @return
	 * @throws FinderException
	 * @throws NamingException
	 */
	public static List<Session3VO> getSessionsAvailable(HttpServletRequest request) throws FinderException,
			NamingException {

		List<Session3VO> list = null;
		Calendar today = Calendar.getInstance();
		// Set time to beginning of the day in order to get sessions that started today
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);

		Integer mProposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
		try {
			Timestamp todayTs = new Timestamp(today.getTimeInMillis());

			Session3Service sessionService = (Session3Service) Ejb3ServiceLocator.getInstance().getLocalService(
					Session3Service.class);

			if (Constants.CREATE_SHIPMENT_INCLUDE_ONGOING_SESSIONS)
				list = sessionService.findFiltered(mProposalId, null/* nbMax */, null/* beamline */, todayTs,
						null/* date2 */, null/* endDate */, true, null);
			else
				list = sessionService.findFiltered(mProposalId, null/* nbMax */, null/* beamline */, null,
						null/* date2 */, todayTs, true, null);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("Error while getting sessions list: " + e.getMessage());
		}

		return list;
	}
}
