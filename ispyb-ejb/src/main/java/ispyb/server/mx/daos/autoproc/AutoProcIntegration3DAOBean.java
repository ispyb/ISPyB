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

package ispyb.server.mx.daos.autoproc;

import ispyb.server.mx.vos.autoproc.AutoProcIntegration3VO;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * <p>
 * The data access object for AutoProcIntegration3 objects (rows of table AutoProcIntegration).
 * </p>
 * 
 * @see {@link AutoProcIntegration3DAO}
 */
@Stateless
public class AutoProcIntegration3DAOBean implements AutoProcIntegration3DAO {

	private final static Logger LOG = Logger.getLogger(AutoProcIntegration3DAOBean.class);

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

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	/* Creation/Update methods ---------------------------------------------- */

	/**
	 * <p>
	 * Insert the given value object. TODO update this comment for insertion details.
	 * </p>
	 */
	public void create(AutoProcIntegration3VO vo) throws Exception {
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
	}

	/**
	 * <p>
	 * Update the given value object. TODO update this comment for update details.
	 * </p>
	 */
	public AutoProcIntegration3VO update(AutoProcIntegration3VO vo) throws Exception {
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/* Deletion methods ----------------------------------------------------- */

	/**
	 * <p>
	 * Deletes the given value object.
	 * </p>
	 * 
	 * @param vo
	 *            the value object to delete.
	 */
	public void delete(AutoProcIntegration3VO vo) {
		entityManager.remove(vo);
	}

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * Returns the AutoProcIntegration3VO instance matching the given primary key.
	 * </p>
	 * <p>
	 * <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u> if the value object has to be
	 * used out the EJB container.
	 * </p>
	 * 
	 * @param pk
	 *            the primary key of the object to load.
	 * @param fetchRelation1
	 *            if true, the linked instances by the relation "relation1" will be set.
	 * @param fetchRelation2
	 *            if true, the linked instances by the relation "relation2" will be set.
	 */
	public AutoProcIntegration3VO findByPk(Integer pk) {
		try{
		return (AutoProcIntegration3VO) entityManager.createQuery(FIND_BY_PK())
				.setParameter("pk", pk).getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}

	/**
	 * <p>
	 * Returns the AutoProcIntegration3VO instances.
	 * </p>
	 * <p>
	 * <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u> if the value object has to be
	 * used out the EJB container.
	 * </p>
	 * 
	 * @param fetchRelation1
	 *            if true, the linked instances by the relation "relation1" will be set.
	 * @param fetchRelation2
	 *            if true, the linked instances by the relation "relation2" will be set.
	 */
	@SuppressWarnings("unchecked")
	public List<AutoProcIntegration3VO> findAll() {
		return entityManager.createQuery(FIND_ALL()).getResultList();
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
	
	/**
	 * 
	 * @param autoProcId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AutoProcIntegration3VO> findByAutoProcId(Integer autoProcId){
		String query = FIND_BY_AUTOPROC_ID;
		List<AutoProcIntegration3VO> listVOs = this.entityManager.createNativeQuery(query, "autoProcIntegrationNativeQuery")
				.setParameter("autoProcId", autoProcId).getResultList();
		return listVOs;
	}
	
	@SuppressWarnings("rawtypes")
	/**
	 * get the autoProcStatus for a given dataCollectionId and a given processingProgram (fastProc or parallelProc or edna-fastproc)
	 * true if we can find at least one autoProc with XSCALE file
	 * @param dataCollectionId
	 * @param processingProgram
	 * @return
	 */
	public Boolean getAutoProcStatus(Integer dataCollectionId, String processingProgram) {
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


	@SuppressWarnings("rawtypes")
	/**
	 * get the autoProcStatus for a given dataCollectionId and a given processingProgram (fastProc or parallelProc or edna-fastproc)
	 * true if we can find at least one autoProc with XSCALE file
	 * @param dataCollectionId
	 * @param processingProgram
	 * @return
	 */
	public Boolean getAutoProcXia2DialsStatus(Integer dataCollectionId, String processingProgram) {
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
}
