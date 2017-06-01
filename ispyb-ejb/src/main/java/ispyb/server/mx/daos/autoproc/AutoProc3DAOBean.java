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

import ispyb.server.mx.vos.autoproc.AutoProc3VO;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * <p>
 * The data access object for AutoProc3 objects (rows of table TableName).
 * </p>
 * 
 * @see {@link AutoProc3DAO}
 */
@Stateless
public class AutoProc3DAOBean implements AutoProc3DAO {

	private final static Logger LOG = Logger.getLogger(AutoProc3DAOBean.class);

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

	/* Creation/Update methods ---------------------------------------------- */

	/**
	 * <p>
	 * Insert the given value object. TODO update this comment for insertion details.
	 * </p>
	 */
	public void create(AutoProc3VO vo) throws Exception {
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
	}

	/**
	 * <p>
	 * Update the given value object. TODO update this comment for update details.
	 * </p>
	 */
	public AutoProc3VO update(AutoProc3VO vo) throws Exception {
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
	public void delete(AutoProc3VO vo) {
		entityManager.remove(vo);
	}

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * Returns the AutoProc3VO instance matching the given primary key.
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
	public AutoProc3VO findByPk(Integer pk) {
		try{
		return (AutoProc3VO) entityManager.createQuery(FIND_BY_PK())
				.setParameter("pk", pk).getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}

	/**
	 * <p>
	 * Returns the AutoProc3VO instances.
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
	public List<AutoProc3VO> findAll() {
		return entityManager.createQuery(FIND_ALL()).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<AutoProc3VO> findByDataCollectionId(Integer dataCollectionId) {
		List<AutoProc3VO> listVOs = this.entityManager.createNativeQuery(FIND_BY_COLLECTION_ID, "autoProcNativeQuery")
				.setParameter("dataCollectionId", dataCollectionId).getResultList();
		return listVOs;

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
	
	/**
	 * Load the autoProc for a specified dataCollection and a specified anomalous mode, and sort them by the space group number
	 * @param dataCollectionId
	 * @param anomalous
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<AutoProc3VO> findByAnomalousDataCollectionIdAndOrderBySpaceGroupNumber(Integer dataCollectionId, Integer anomalous) {
		String query = FIND_BY_ANOMALOUS_DATACOLLECTIONID;
		List<AutoProc3VO> listVOs = this.entityManager.createNativeQuery(query, "autoProcNativeQuery")
				.setParameter("dataCollectionId", dataCollectionId).
				setParameter("anomalous", anomalous).getResultList();
		return listVOs;
	}
}
