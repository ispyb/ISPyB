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

package ispyb.server.common.services.shipping.external;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ispyb.server.common.vos.shipping.Shipping3VO;

import javax.ejb.Remote;

import com.google.gson.JsonElement;

@Remote
public interface External3Service {
	public Shipping3VO storeShipping(String proposalCode, String proposalNumber, Shipping3VO shipping) throws Exception;
	
	public Shipping3VO storeShippingFull(String proposalCode, String proposalNumber, Shipping3VO shipping) throws Exception;

	public List<Map<String, Object>> getDataCollectionByProposal(String proposalCode, String proposalNumber);

	public List<Map<String, Object>> getSessionsByProposalCodeAndNumber(String proposalCode, String proposalNumber);

	public List<Map<String, Object>> getDataCollectionFromShippingId(int shippingId);

	public List<Map<String, Object>> getAllDataCollectionFromShippingId(int shippingId);

	public List<Map<String, Object>> getAutoprocResultByDataCollectionIdList(
			ArrayList<Integer> inList);

	public List<Map<String, Object>> getPhasingAnalysisByDataCollectionIdListQuery(Integer autoProcIntegrationId);

}