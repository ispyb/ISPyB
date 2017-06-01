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
package ispyb.server.mx.services.autoproc;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import ispyb.server.common.exceptions.AccessDeniedException;

import ispyb.server.mx.vos.autoproc.AutoProc3VO;

/**
 * <p>
 * This session bean handles ISPyB AutoProc3.
 * </p>
 */
@Stateless
public class AutoProc3ServiceBean implements AutoProc3Service, AutoProc3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(AutoProc3ServiceBean.class);
	
	// Generic HQL request to find instances of AutoProc3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK() {
		return "from AutoProc3VO vo "  + "where vo.autoProcId = :pk";
	}
	
	// Generic HQL request to find all instances of AutoProc3
	// TODO choose between left/inner join
	private static final String FIND_ALL() {
		return "from AutoProc3VO vo " ;
	}

	private static final String FIND_BY_COLLECTION_ID = "select * from AutoProc ap , AutoProcScaling aps, AutoProcScaling_has_Int apshi1, AutoProcScaling_has_Int apshi2, AutoProcIntegration api"
			+ " WHERE aps.autoProcId = ap.autoProcId AND aps.autoProcScalingId = apshi1.autoProcScalingId AND apshi1.autoProcScaling_has_IntId = apshi2.autoProcScaling_has_IntId"
			+ " AND apshi2.autoProcIntegrationId = api.autoProcIntegrationId AND api.dataCollectionId = :dataCollectionId";

	
	private static final String FIND_BY_ANOMALOUS_DATACOLLECTIONID =  
		"SELECT o.autoProcId, o.autoProcProgramId, o.spaceGroup, " +
					"o.refinedCell_a, o.refinedCell_b, o.refinedCell_c, " +
					"o.refinedCell_alpha, o.refinedCell_beta, o.refinedCell_gamma, o.recordTimeStamp "+ 
				"FROM AutoProc o, AutoProcScaling aps, AutoProcScaling_has_Int apshi , AutoProcIntegration api, SpaceGroup sp  "+
				"WHERE  api.dataCollectionId = :dataCollectionId AND api.anomalous = :anomalous AND "+
				"api.autoProcIntegrationId = apshi.autoProcIntegrationId AND apshi.autoProcScalingId = aps.autoProcScalingId AND "+
				"aps.autoProcId = o.autoProcId AND "+
				"REPLACE(o.spaceGroup, ' ', '') = sp.spaceGroupShortName  "+
				"ORDER BY sp.spaceGroupNumber DESC";
	
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public AutoProc3ServiceBean() {
	};

	/**
	 * Create new AutoProc3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public AutoProc3VO create(final AutoProc3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the AutoProc3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public AutoProc3VO update(final AutoProc3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the AutoProc3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		
		checkCreateChangeRemoveAccess();
		AutoProc3VO vo = findByPk(pk);
		delete(vo);
	}

	/**
	 * Remove the AutoProc3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final AutoProc3VO vo) throws Exception {
		
		checkCreateChangeRemoveAccess();
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the AutoProc3 value object
	 */
	public AutoProc3VO findByPk(final Integer pk) throws Exception {

		checkCreateChangeRemoveAccess();
		try{
			return (AutoProc3VO) entityManager.createQuery(FIND_BY_PK())
					.setParameter("pk", pk).getSingleResult();
			}catch(NoResultException e){
				return null;
			}
	}

	// TODO remove following method if not adequate
	/**
	 * Find all AutoProc3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<AutoProc3VO> findAll() throws Exception {
		
		List<AutoProc3VO> foundEntities = entityManager.createQuery(FIND_ALL()).getResultList();
		return foundEntities;
	}

	@SuppressWarnings("unchecked")
	public List<AutoProc3VO> findByDataCollectionId(final Integer dataCollectionId) throws Exception {

		List<AutoProc3VO> foundEntities = this.entityManager.createNativeQuery(FIND_BY_COLLECTION_ID, "autoProcNativeQuery")
				.setParameter("dataCollectionId", dataCollectionId).getResultList();
		return foundEntities;
	}

	/**
	 * Load the autoProc for a specified dataCollection and a specified anomalous mode, and sort them by the space group
	 * number
	 * 
	 * @param dataCollectionId
	 * @param anomalous
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<AutoProc3VO> findByAnomalousDataCollectionIdAndOrderBySpaceGroupNumber(final Integer dataCollectionId,
			final boolean anomalous) throws Exception {
		
		String query = FIND_BY_ANOMALOUS_DATACOLLECTIONID;
		List<AutoProc3VO> listVOs = this.entityManager.createNativeQuery(query, "autoProcNativeQuery")
				.setParameter("dataCollectionId", dataCollectionId).
				setParameter("anomalous", anomalous).getResultList();
		return listVOs;
	}

	/**
	 * Check if user has access rights to create, change and remove AutoProc3 entities. If not set rollback only and
	 * throw AccessDeniedException
	 * 
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
				// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
				// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
				// to the one checking the needed access rights
				// autService.checkUserRightToChangeAdminData();
	}
	
	/* Private methods ------------------------------------------------------ */

	/**
	 * Checks the data for integrity. E.g. if references and categories exist.
	 * 
	 * @param vo
	 *            the data to check
	 * @param create
	 *            should be true if the value object is just being created in the DB, this avoids some checks like
	 *            testing the primary key
	 * @exception VOValidateException
	 *                if data is not correct
	 */
	private void checkAndCompleteData(AutoProc3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getAutoProcId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getAutoProcId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
}
