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

package ispyb.server.common.daos.proposals;

import ispyb.server.common.vos.proposals.LabContact3VO;

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

/**
 * <p>
 * The data access object for LabContact3 objects (rows of table TableName).
 * </p>
 * 
 * @see {@link LabContact3DAO}
 */
@Stateless
public class LabContact3DAOBean implements LabContact3DAO {

	private final static Logger LOG = Logger.getLogger(LabContact3DAOBean.class);

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

	/* Creation/Update methods ---------------------------------------------- */

	/**
	 * <p>
	 * Insert the given value object. TODO update this comment for insertion details.
	 * </p>
	 */
	public void create(LabContact3VO vo) throws Exception {
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
	}

	/**
	 * <p>
	 * Update the given value object. TODO update this comment for update details.
	 * </p>
	 */
	public LabContact3VO update(LabContact3VO vo) throws Exception {
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
	public void delete(LabContact3VO vo) {
		entityManager.remove(vo);
	}

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * Returns the LabContact3VO instance matching the given primary key.
	 * </p>
	 * <p>
	 * <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u> if the value object has to be
	 * used out the EJB container.
	 * </p>
	 * 
	 * @param pk
	 *            the primary key of the object to load.
	 */
	public LabContact3VO findByPk(Integer pk) {
		try {
			return (LabContact3VO) entityManager.createQuery(FIND_BY_PK()).setParameter("pk", pk).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * <p>
	 * Returns the LabContact3VO instances.
	 * </p>
	 * <p>
	 * <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u> if the value object has to be
	 * used out the EJB container.
	 * </p>
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<LabContact3VO> findAll() {
		return entityManager.createQuery(FIND_ALL()).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<LabContact3VO> findFiltered(Integer proposalId, String cardName) {

		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(LabContact3VO.class);

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (cardName != null && !cardName.isEmpty()) {
			crit.add(Restrictions.like("cardName", cardName));
		}

		if (proposalId != null) {
			Criteria subCrit = crit.createCriteria("proposalVO");
			subCrit.add(Restrictions.eq("proposalId", proposalId));
		}

		crit.addOrder(Order.desc("labContactId"));

		return crit.list();
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

	@SuppressWarnings("unchecked")
	@Override
	public List<LabContact3VO> findByPersonIdAndProposalId(Integer personId, Integer proposalId) {
		@SuppressWarnings("deprecation")
		Session session = (Session) this.entityManager.getDelegate();
		return session.createQuery("from LabContact3VO where personId=:personId and proposalId=:proposalId")
				.setParameter("personId", personId).setParameter("proposalId", proposalId)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LabContact3VO> findByProposalId(Integer proposalId) {
		@SuppressWarnings("deprecation")
		Session session = (Session) this.entityManager.getDelegate();
		return session.createQuery("from LabContact3VO where proposalId=:proposalId")
				.setParameter("proposalId", proposalId)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<LabContact3VO> findByCardName(String cardNameWithNum) {
		@SuppressWarnings("deprecation")
		Session session = (Session) this.entityManager.getDelegate();
		return session.createCriteria(LabContact3VO.class).add(Restrictions.eq("cardName", cardNameWithNum)).list();

	}

	/* returns the nb of shipments linked to a labcontact */
	public Integer hasShipping(Integer labContactId) throws Exception {
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
}
