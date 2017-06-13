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

package ispyb.server.common.services.proposals;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import ispyb.server.common.exceptions.AccessDeniedException;
import ispyb.server.common.vos.proposals.LabContact3VO;



/**
 * <p>
 * The data access object for LabContact3 objects (rows of table TableName).
 * </p>
 * 
 */
@Stateless
public class LabContact3ServiceBean implements LabContact3Service, LabContact3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(LabContact3ServiceBean.class);

	private final static String HAS_SHIPPING = "SELECT COUNT(*) FROM Shipping "
			+ "WHERE sendingLabContactId = :labContactId OR returnLabContactId = :labContactId";

	// Generic HQL request to find instances of LabContact3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK() {
		return "from LabContact3VO vo where vo.labContactId = :pk";
	}

	// Generic HQL request to find all instances of LabContact3
	// TODO choose between left/inner join
	private static final String FIND_ALL() {
		return "from LabContact3VO vo ";
	}

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public LabContact3ServiceBean() {
	};

	/* Creation/Update methods ---------------------------------------------- */
	
	/**
	 * Create new LabContact3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	
	public LabContact3VO create(final LabContact3VO vo) throws Exception {
		
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}
	
	/**
	 * Update the LabContact3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public LabContact3VO update(final LabContact3VO vo) throws Exception {
		
		this.checkAndCompleteData(vo, false);
		return this.entityManager.merge(vo);
	}

	/**
	 * Remove the LabContact3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		
		LabContact3VO vo = findByPk(pk);
		checkCreateChangeRemoveAccess();
		delete(vo);
	}


	/**
	 * Remove the LabContact3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final LabContact3VO vo) throws Exception {
		
		entityManager.remove(vo);
	}
	
		

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the LabContact3 value object
	 */
	public LabContact3VO findByPk(final Integer pk) throws Exception {
		checkCreateChangeRemoveAccess();
		try {
			return (LabContact3VO) entityManager.createQuery(FIND_BY_PK()).setParameter("pk", pk).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	// TODO remove following method if not adequate
	/**
	 * Find all LabContact3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<LabContact3VO> findAll() throws Exception {
		
		List<LabContact3VO> foundEntities = this.entityManager.createQuery(FIND_ALL()).getResultList();
		return foundEntities;
	}

	@SuppressWarnings("unchecked")
	public List<LabContact3VO> findFiltered(Integer proposalId, String cardName)  {
		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(LabContact3VO.class);

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (cardName != null && !cardName.isEmpty()) {
			crit.add(Restrictions.ilike("cardName", cardName));
		}

		if (proposalId != null) {
			Criteria subCrit = crit.createCriteria("proposalVO");
			subCrit.add(Restrictions.eq("proposalId", proposalId));
		}

		crit.addOrder(Order.desc("labContactId"));

		return crit.list();
	}

	/**
	 * Check if user has access rights to create, change and remove LabContact3 entities. If not set rollback only and
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

	@SuppressWarnings("unchecked")
	@Override
	public List<LabContact3VO> findByPersonIdAndProposalId(final Integer personId, final Integer proposalId) throws Exception {
		
		checkCreateChangeRemoveAccess();
		Session session = (Session) this.entityManager.getDelegate();
		return session.createQuery("from LabContact3VO where personId=:personId and proposalId=:proposalId")
					.setParameter("personId", personId).setParameter("proposalId", proposalId)
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LabContact3VO> findByCardName(final String cardNameWithNum) throws Exception {

		checkCreateChangeRemoveAccess();
		Session session = (Session) this.entityManager.getDelegate();
		return session.createCriteria(LabContact3VO.class).add(Restrictions.eq("cardName", cardNameWithNum)).list();
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
	private void checkAndCompleteData(LabContact3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getLabContactId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getLabContactId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
	public Integer hasShipping(final Integer labContactId) throws Exception {

		Query query = entityManager.createNativeQuery(HAS_SHIPPING).setParameter("labContactId", labContactId);
		try{
			BigInteger res = (BigInteger) query.getSingleResult();
			return new Integer(res.intValue());
		}catch(NoResultException e){
			System.out.println("ERROR in hasShipping - NoResultException: "+labContactId);
			e.printStackTrace();
			return 0;
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LabContact3VO> findByProposalId(final Integer proposalId) throws Exception {
		
		checkCreateChangeRemoveAccess();
		Session session = (Session) this.entityManager.getDelegate();
		return session.createQuery("from LabContact3VO where proposalId=:proposalId")
				.setParameter("proposalId", proposalId)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

	}

}