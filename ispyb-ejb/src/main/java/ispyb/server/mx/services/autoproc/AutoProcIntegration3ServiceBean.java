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

import ispyb.server.common.util.ejb.EJBAccessCallback;
import ispyb.server.common.util.ejb.EJBAccessTemplate;

import ispyb.server.mx.vos.autoproc.AutoProcIntegration3VO;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * <p>
 *  This session bean handles ISPyB AutoProcIntegration3.
 * </p>
 */
@Stateless
public class AutoProcIntegration3ServiceBean implements AutoProcIntegration3Service,
		AutoProcIntegration3ServiceLocal {

	private final static Logger LOG = Logger
			.getLogger(AutoProcIntegration3ServiceBean.class);
	
	// Generic HQL request to find instances of AutoProcIntegration3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK() {
		return "from AutoProcIntegration3VO vo " + "where vo.autoProcIntegrationId = :pk";
	}

	// Generic HQL request to find all instances of AutoProcIntegration3
	// TODO choose between left/inner join
	private static final String FIND_ALL() {
		return "from AutoProcIntegration3VO vo " ;
	}
	
	private static String FIND_BY_AUTOPROC_ID = "SELECT DISTINCT * " +
			"FROM AutoProcIntegration api, AutoProcScaling_has_Int aphi, AutoProcScaling aps " +
			"WHERE aps.autoProcId = :autoProcId AND aps.autoProcScalingId = aphi.autoProcScalingId AND " +
			"api.autoProcIntegrationId= aphi.autoProcIntegrationId " ;
	
	private static final String FIND_AUTOPROC_STATUS = "SELECT app.processingPrograms, api.autoProcIntegrationId, apppa.fileName " +
			"FROM `AutoProcIntegration` api, AutoProcProgram app, AutoProcProgramAttachment apppa " +
			"WHERE api.dataCollectionId = :dataCollectionId AND " +
			"api.autoProcProgramId = app.autoProcProgramId AND " +
			"app.processingPrograms like :processingProgram AND  " +
			"apppa.autoProcProgramId = app.autoProcProgramId AND " +
			"apppa.fileName like '%XSCALE%' ";

	/* XIA2_DIALS don't create any XSCALE files so here we look for
	 * a file with the prefix 'di' and the suffix '.mtz'.
	 */
	private static final String FIND_XIA2_DIALS_STATUS = "SELECT app.processingPrograms, api.autoProcIntegrationId, apppa.fileName " +
			"FROM `AutoProcIntegration` api, AutoProcProgram app, AutoProcProgramAttachment apppa " +
			"WHERE api.dataCollectionId = :dataCollectionId AND " +
			"api.autoProcProgramId = app.autoProcProgramId AND " +
			"app.processingPrograms like :processingProgram AND  " +
			"apppa.autoProcProgramId = app.autoProcProgramId AND " +
			"apppa.fileName like 'di%.mtz' ";

	/* FAST DP don't create any XSCALE files so here we look for
	 * a file with the prefix 'fast' and the suffix '.mtz.gz'.
	 */
	private static final String FIND_FASTDP_STATUS = "SELECT app.processingPrograms, api.autoProcIntegrationId, apppa.fileName " +
			"FROM `AutoProcIntegration` api, AutoProcProgram app, AutoProcProgramAttachment apppa " +
			"WHERE api.dataCollectionId = :dataCollectionId AND " +
			"api.autoProcProgramId = app.autoProcProgramId AND " +
			"app.processingPrograms like :processingProgram AND  " +
			"apppa.autoProcProgramId = app.autoProcProgramId AND " +
			"apppa.fileName like '%fast_dp%.mtz.gz' ";

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public AutoProcIntegration3ServiceBean() {
	};

	/**
	 * Create new AutoProcIntegration3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public AutoProcIntegration3VO create(final AutoProcIntegration3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the AutoProcIntegration3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public AutoProcIntegration3VO update(final AutoProcIntegration3VO vo) throws Exception {
	
		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the AutoProcIntegration3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		checkCreateChangeRemoveAccess();
		AutoProcIntegration3VO vo = findByPk(pk);			
		delete(vo);
	}

	/**
	 * Remove the AutoProcIntegration3
	 * @param vo the entity to remove.
	 */
	public void delete(final AutoProcIntegration3VO vo) throws Exception {
		
		checkCreateChangeRemoveAccess();
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the AutoProcIntegration3 value object
	 */
	public AutoProcIntegration3VO findByPk(final Integer pk) throws Exception {
		
		checkCreateChangeRemoveAccess();
		try{
			return (AutoProcIntegration3VO) entityManager.createQuery(FIND_BY_PK())
					.setParameter("pk", pk).getSingleResult();
			}catch(NoResultException e){
				return null;
			}
	}

	// TODO remove following method if not adequate
	/**
	 * Find all AutoProcIntegration3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<AutoProcIntegration3VO> findAll() throws Exception {

		List<AutoProcIntegration3VO> foundEntities = entityManager.createQuery(FIND_ALL()).getResultList();
		return foundEntities;
	}

	/**
	 * Check if user has access rights to create, change and remove AutoProcIntegration3 entities. If not set rollback only and throw AccessDeniedException
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
				//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
				//autService.checkUserRightToChangeAdminData();
	}
	
	/**
	 * 
	 * @param autoProcId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<AutoProcIntegration3VO> findByAutoProcId(final Integer autoProcId) throws Exception{
		String query = FIND_BY_AUTOPROC_ID;
		List<AutoProcIntegration3VO> listVOs = this.entityManager.createNativeQuery(query, "autoProcIntegrationNativeQuery")
				.setParameter("autoProcId", autoProcId).getResultList();
		return listVOs;
	}

	/**
	 * get the autoProcStatus for a given dataCollectionId and a given processingProgram (fastProc or parallelProc or edna-fastproc)
	 * true if we can find at least one autoProc with XSCALE file
	 * @param dataCollectionId
	 * @param processingProgram
	 * @return
	 * @throws Exception
	 */
	public Boolean getAutoProcStatus(final Integer dataCollectionId, final String processingProgram) throws Exception {
		String query = FIND_AUTOPROC_STATUS ;
		try{
			List res = this.entityManager.createNativeQuery(query)
					.setParameter("dataCollectionId", dataCollectionId)
					.setParameter("processingProgram", processingProgram)
					.getResultList();
			if (res == null || res.isEmpty()){
				return false;
			}
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * get the autoProcStatus for a XIA2_DIALS
	 * @param dataCollectionId
	 * @param processingProgram
	 * @return
	 * @throws Exception
	 */
	public Boolean getAutoProcXia2DialsStatus(final Integer dataCollectionId, final String processingProgram) throws Exception {
		String query = FIND_XIA2_DIALS_STATUS ;
		try{
			List res = this.entityManager.createNativeQuery(query)
					.setParameter("dataCollectionId", dataCollectionId)
					.setParameter("processingProgram", processingProgram)
					.getResultList();
			if (res == null || res.isEmpty()){
				return false;
			}
			return true;
		}catch(Exception e){
			return false;
		}
	}

	/**
	 * get the autoProcStatus for a FAST DP
	 * @param dataCollectionId
	 * @param processingProgram
	 * @return
	 * @throws Exception
	 */
	public Boolean getAutoProcFastDPStatus(final Integer dataCollectionId, final String processingProgram) throws Exception {
		String query = FIND_FASTDP_STATUS ;
		try{
			List res = this.entityManager.createNativeQuery(query)
					.setParameter("dataCollectionId", dataCollectionId)
					.setParameter("processingProgram", processingProgram)
					.getResultList();
			if (res == null || res.isEmpty()){
				return false;
			}
			return true;
		}catch(Exception e){
			return false;
		}
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
	private void checkAndCompleteData(AutoProcIntegration3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getAutoProcIntegrationId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getAutoProcIntegrationId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
}