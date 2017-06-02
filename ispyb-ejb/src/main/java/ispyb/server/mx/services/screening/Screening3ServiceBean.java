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
package ispyb.server.mx.services.screening;

import ispyb.server.mx.vos.screening.Screening3VO;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * <p>
 * This session bean handles ISPyB Screening3.
 * </p>
 */
@Stateless
public class Screening3ServiceBean implements Screening3Service, Screening3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(Screening3ServiceBean.class);

	// Generic HQL request to find instances of Screening3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK(boolean fetchScreeningRank, boolean fetchScreeningOutput) {
		return "from Screening3VO vo " + (fetchScreeningRank ? "left join fetch vo.screeningRankVOs " : "")
				+ (fetchScreeningOutput ? "left join fetch vo.screeningOutputVOs " : "")+ "where vo.screeningId = :pk";
	}

	// Generic HQL request to find all instances of Screening3
	// TODO choose between left/inner join
	private static final String FIND_ALL(boolean fetchScreeningRank, boolean fetchScreeningOutput) {
		return "from Screening3VO vo " + (fetchScreeningRank ? "left join fetch vo.screeningRankVOs " : "")
		+ (fetchScreeningOutput ? "left join fetch vo.screeningOutputVOs " : "");
	}

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@Resource
	private SessionContext context;

	public Screening3ServiceBean() {
	};

	/**
	 * Create new Screening3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public Screening3VO create(final Screening3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the Screening3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public Screening3VO update(final Screening3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the Screening3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		
		checkCreateChangeRemoveAccess();
		Screening3VO vo = findByPk(pk, false,  false);
		// TODO Edit this business code
		delete(vo);
	}

	/**
	 * Remove the Screening3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Screening3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		entityManager.remove(vo);
	}


	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the Screening3 value object
	 */
	public Screening3VO findByPk(final Integer pk, final boolean fetchScreeningRank,  final boolean fetchScreeningOutput) throws Exception {
	
		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		try{
			return (Screening3VO) entityManager.createQuery(FIND_BY_PK(fetchScreeningRank, fetchScreeningOutput ))
				.setParameter("pk", pk).getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}


	// TODO remove following method if not adequate
	/**
	 * Find all Screening3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<Screening3VO> findAll(final boolean fetchScreeningRank,  final boolean fetchScreeningOutput) throws Exception {
	
		List<Screening3VO> foundEntities = entityManager.createQuery(FIND_ALL(fetchScreeningRank, fetchScreeningOutput )).getResultList();
		return foundEntities;
	}

	/**
	 * Check if user has access rights to create, change and remove Screening3 entities. If not set rollback only and
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

	/**
	 * Get all Screening3 entity VOs from a collection of Screening3 local entities.
	 * 
	 * @param localEntities
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	private Screening3VO[] getScreening3VOs(List<Screening3VO> entities) {
		ArrayList results = new ArrayList(entities.size());
		for (Screening3VO vo : entities) {
			results.add(vo);
		}
		Screening3VO[] tmpResults = new Screening3VO[results.size()];
		return (Screening3VO[]) results.toArray(tmpResults);
	}
	
	public Screening3VO loadEager(Screening3VO vo) throws Exception{
		Screening3VO newVO = this.findByPk(vo.getScreeningId(),true, true);
		return newVO;
	}
	
	@SuppressWarnings("unchecked")
	public List<Screening3VO> findFiltered(final Integer dataCollectionId) throws Exception{
		
		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(Screening3VO.class);
		Criteria subCritDc = crit.createCriteria("dataCollectionVO");

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (dataCollectionId != null) {
			subCritDc.add(Restrictions.eq("dataCollectionId", dataCollectionId));
		}
		

		crit.addOrder(Order.desc("screeningId"));

		List<Screening3VO> foundEntities = crit.list();
		return foundEntities;
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
	private void checkAndCompleteData(Screening3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getScreeningId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getScreeningId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}


}