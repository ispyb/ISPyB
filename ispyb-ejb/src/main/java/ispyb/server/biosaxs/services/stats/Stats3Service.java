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

package ispyb.server.biosaxs.services.stats;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;


@Remote
public interface Stats3Service {

	List getExperimentsBy(String type, String start, String end);

	List getFramesBy(String start, String end);

	List getSamplesBy(String start, String end);

	List getSessionsBy(String start, String end);

	List<Map<String, Object>> getAutoprocStatsByDate(String autoprocStatisticsType, Date startDate, Date endDate);

	List<Map<String, Object>> getAutoprocStatsByDate(String autoprocStatisticsType, Date startDate, Date endDate,String beamline);

	List<Map<String, Object>> getDatacollectionStatsByDate(String datacollectionStatisticsType, Date startDate, Date endDate, String[] datacollectionTestProposals);

	List<Map<String, Object>> getDatacollectionStatsByDate(String datacollectionStatisticsType, Date startDate, Date endDate, String[] datacollectionTestProposals, String beamline);

	List<Map<String, Object>> getExperimentStatsByDate(Date startDate, Date endDate, String[] datacollectionTestProposals);

	List<Map<String, Object>> getExperimentStatsByDate(Date startDate, Date endDate, String[] datacollectionTestProposals, String beamline);

}