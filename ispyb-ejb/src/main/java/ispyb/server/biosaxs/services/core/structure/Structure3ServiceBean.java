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

package ispyb.server.biosaxs.services.core.structure;

import ispyb.server.biosaxs.vos.assembly.Structure3VO;
import ispyb.server.mx.services.sample.BLSample3ServiceLocal;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class Structure3ServiceBean implements Structure3Service, Structure3ServiceLocal {

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@EJB
	private BLSample3ServiceLocal sample3ServiceLocal;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Structure3VO> getStructuresByCrystalId(Integer crystalId) throws Exception {
		String query = "SELECT structure3VO FROM Structure3VO structure3VO where structure3VO.crystalId = :crystalId" ;
		Query EJBQuery = this.entityManager.createQuery(query).setParameter("crystalId", crystalId);
		return (List<Structure3VO>) EJBQuery.getResultList();	
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Structure3VO> getLigandsByGroupName(Integer proposalId, String groupName) throws Exception {
		String query = "SELECT structure3VO FROM Structure3VO structure3VO where structure3VO.proposalId = :proposalId and structure3VO.groupName = :groupName" ;
		Query EJBQuery = this.entityManager.createQuery(query)
				.setParameter("proposalId", proposalId)
				.setParameter("groupName", groupName);
		return (List<Structure3VO>) EJBQuery.getResultList();	
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Structure3VO> getStructuresByProposalId(Integer proposalId) throws Exception {
		String query = "SELECT structure3VO FROM Structure3VO structure3VO where structure3VO.proposalId = :proposalId" ;
		Query EJBQuery = this.entityManager.createQuery(query).setParameter("proposalId", proposalId);
		return (List<Structure3VO>) EJBQuery.getResultList();	
	}
	
	@Override
	public List<Structure3VO> getStructuresByDataCollectionId(Integer dataCollectionId) throws Exception {
		DataCollection3VO dataCollection = entityManager.find(DataCollection3VO.class, dataCollectionId);
		if (dataCollection != null){
			DataCollectionGroup3VO dataCollectionGroup = entityManager.find(DataCollectionGroup3VO.class, dataCollection.getDataCollectionGroupVOId());
			if (dataCollectionGroup != null){
				BLSample3VO blSample = entityManager.find(BLSample3VO.class, dataCollectionGroup.getBlSampleVO().getBlSampleId());
				if (blSample != null){
					Crystal3VO crystal = entityManager.find(Crystal3VO.class, blSample.getCrystalVOId());
					if (crystal != null){
						return this.getStructuresByCrystalId(crystal.getCrystalId());
					}
				}
				
			}
		}
		else{
			throw new Exception("Not datacollection found with dataCollectionId=" + dataCollectionId);
		}
		return null;
	}
	
	@Override
	public List<Structure3VO> getLigandsByDataCollectionId(Integer dataCollectionId) throws Exception {
		DataCollection3VO dataCollection = entityManager.find(DataCollection3VO.class, dataCollectionId);
		
		if (dataCollection != null){
			DataCollectionGroup3VO dataCollectionGroup = entityManager.find(DataCollectionGroup3VO.class, dataCollection.getDataCollectionGroupVOId());
			if (dataCollectionGroup != null){
				BLSample3VO blSample = entityManager.find(BLSample3VO.class, dataCollectionGroup.getBlSampleVO().getBlSampleId());
				if (blSample != null){
					String structureStage = blSample.getStructureStage();
					System.out.println("[getLigandsByDataCollectionId] structureStage " + structureStage);
					if (structureStage != null){
						Session3VO session = entityManager.find(Session3VO.class, dataCollectionGroup.getSessionVOId());
						System.out.println("[getLigandsByDataCollectionId] structureStage " + structureStage + " sessionId " + dataCollectionGroup.getSessionVOId());
						return this.getLigandsByGroupName(session.getProposalVOId(), structureStage);
					}
				}
				
			}
		}
		else{
			throw new Exception("Not datacollection found with dataCollectionId=" + dataCollectionId);
		}
		return null;
	}

}
