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

package ispyb.server.mx.services.ws.rest.datacollectiongroup;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;


@Remote
public interface DataCollectionGroupRestWsService {

	public List<Map<String,Object>> getViewDataCollectionBySessionId(int proposalId, int sessionId);
	
	public List<Map<String, Object>> getViewDataCollectionBySessionIdHavingImages(int proposalId, int sessionId);

	public List<Map<String, Object>> getViewDataCollectionByProteinAcronym(int proposalId, String proteinAcronym);

	public List<Map<String, Object>> getViewDataCollectionBySampleId(int proposalId, int sampleId);
	
	public List<Map<String, Object>> getViewDataCollectionBySampleName(int proposalId, String name);
	
	public List<Map<String, Object>> getViewDataCollectionByImagePrefix(int proposalId, String prefix);

	public Collection<? extends Map<String, Object>> getViewDataCollectionByDataCollectionId(int proposalId, int dataCollectionId);
	
	public List<Map<String, Object>>  getViewDataCollectionByWorkflowId(Integer proposalId, Integer workflowId);
	
} 