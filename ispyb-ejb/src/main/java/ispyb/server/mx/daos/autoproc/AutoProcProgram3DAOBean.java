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

import ispyb.server.mx.vos.autoproc.AutoProcProgram3VO;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

/**
 * <p>
 * The data access object for AutoProcProgram3 objects (rows of table TableName).
 * </p>
 * 
 * @see {@link AutoProcProgram3DAO}
 */
@Stateless
public class AutoProcProgram3DAOBean implements AutoProcProgram3DAO {

	private final static Logger LOG = Logger.getLogger(AutoProcProgram3DAOBean.class);

	// Generic HQL request to find instances of AutoProcProgram3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK(boolean fetchAttachment) {
		return "from AutoProcProgram3VO vo " + (fetchAttachment ? "left join fetch vo.attachmentVOs " : "")
				+ "where vo.autoProcProgramId = :pk";
	}

	// Generic HQL request to find all instances of AutoProcProgram3
	// TODO choose between left/inner join
	private static final String FIND_ALL(boolean fetchAttachment) {
		return "from AutoProcProgram3VO vo " + (fetchAttachment ? "left join fetch vo.attachmentVOs " : "");
	}

	private static final String FIND_COLLECT_API = 
			"SELECT c.dataCollectionId "
			+ "FROM DataCollection c, AutoProcIntegration api "
			+ " WHERE c.dataCollectionId = api.dataCollectionId AND  "
			+ "api.autoProcProgramId = :autoProcProgramId " 
			+ " ORDER BY c.dataCollectionId ASC ";
	
	private static final String FIND_COLLECT_AP = 
			"SELECT c.dataCollectionId "
			+ "FROM DataCollection c, AutoProcIntegration api, AutoProcScaling_has_Int apshi, AutoProcScaling aps, AutoProc ap "
			+ " WHERE c.dataCollectionId = api.dataCollectionId AND  "
			+ "api.autoProcIntegrationId =  apshi.autoProcIntegrationId AND "
			+ "apshi.autoProcScalingId =  aps.autoProcScalingId AND "
			+ "aps.autoProcId =  ap.autoProcId AND "
			+ "ap.autoProcProgramId = :autoProcProgramId " 
			+ " ORDER BY c.dataCollectionId ASC ";
	
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	/* Creation/Update methods ---------------------------------------------- */

	/**
	 * <p>
	 * Insert the given value object. TODO update this comment for insertion details.
	 * </p>
	 */
	public void create(AutoProcProgram3VO vo) throws Exception {
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
	}

	/**
	 * <p>
	 * Update the given value object. TODO update this comment for update details.
	 * </p>
	 */
	public AutoProcProgram3VO update(AutoProcProgram3VO vo) throws Exception {
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
	public void delete(AutoProcProgram3VO vo) {
		entityManager.remove(vo);
	}

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * Returns the AutoProcProgram3VO instance matching the given primary key.
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
	public AutoProcProgram3VO findByPk(Integer pk, boolean fetchAttachment) {
		try{
		return (AutoProcProgram3VO) entityManager.createQuery(FIND_BY_PK(fetchAttachment))
				.setParameter("pk", pk).getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}

	/**
	 * <p>
	 * Returns the AutoProcProgram3VO instances.
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
	public List<AutoProcProgram3VO> findAll(boolean fetchAttachment) {
		return entityManager.createQuery(FIND_ALL(fetchAttachment)).getResultList();
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
	private void checkAndCompleteData(AutoProcProgram3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getAutoProcProgramId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getAutoProcProgramId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
	/**
	 * Find all dataCollection linked to this autoProcProgramId
	 * @param autoProcProgramId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List findCollects(Integer autoProcProgramId) {
		try{
			Query query1 = entityManager.createNativeQuery(FIND_COLLECT_AP).setParameter("autoProcProgramId", autoProcProgramId);
			List resultList1 = query1.getResultList();
			
			Query query2 = entityManager.createNativeQuery(FIND_COLLECT_API).setParameter("autoProcProgramId", autoProcProgramId);
			List resultList2 = query2.getResultList();
			
			List results = new ArrayList();
			if (resultList1 != null){
				results.addAll(resultList1);
			}
			if (resultList2 != null){
				results.addAll(resultList2);
			}
			
			return results;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
