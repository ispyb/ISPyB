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

package ispyb.server.mx.services.ws.rest.phasing;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;


@Remote
public interface PhasingRestWsService {


	public List<Map<String, Object>> getPhasingViewByDataCollectionId(int dataCollectionId, int proposalId);

	public List<Map<String, Object>> getPhasingViewByAutoProcIntegrationId(int autoprocIntegrationId, int proposalId);

	public List<Map<String, Object>> getPhasingViewByBlSampleId(int blSampleId, int proposalId);
	
	public List<Map<String, Object>> getPhasingViewByProteinId(int proteinId, int proposalId);

	public List<Map<String, Object>> getPhasingViewBySessionId(int sessionId, int proposalId);

	public List<Map<String, Object>> getPhasingViewByStepId(int phasingStepId, int proposalId);

	public List<Map<String, Object>> getPhasingFilesViewByStepId(int phasingStepId, int proposalId);
	
	public List<Map<String, Object>> getPhasingFilesViewByPhasingProgramAttachmentId(int phasingProgramAttachmentId, int proposalId);

	public List<Map<String, Object>> getPhasingViewByDataCollectionGroupId(int dataCollectionGroupId, int proposalId);
	
}