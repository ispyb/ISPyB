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
 ******************************************************************************************************************************/

package ispyb.server.common.services.ws.rest.session;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;


@Remote
public interface SessionService {

	public List<Map<String, Object>> getSessionViewByProposalId(int proposalId);

	public List<Map<String, Object>> getSessionViewByDates(String startDate,String endDate);

	public List<Map<String, Object>> getSessionViewByProposalAndDates(int proposalId, String startDate,String endDate);
	
	public List<Map<String, Object>> getSessionViewBySessionId(int proposalId, int sessionId);

	public List<Map<String, Object>> getSessionViewByBeamlineOperator(String beamlineOperator);

	public List<Map<String, Object>> getSessionViewByDates(String start, String end, String siteId);


	
}