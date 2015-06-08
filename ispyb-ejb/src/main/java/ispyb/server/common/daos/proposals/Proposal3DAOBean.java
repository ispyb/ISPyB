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
package ispyb.server.common.daos.proposals;

import ispyb.server.common.vos.proposals.Proposal3VO;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * <p>
 * The data access object for Proposal objects (rows of table Proposal).
 * </p>
 * 
 * @see {@link Proposal3DAO}
 */
@Stateless
public class Proposal3DAOBean implements Proposal3DAO {

	private final static Logger LOG = Logger.getLogger(Proposal3DAOBean.class);

	// Generic HQL request to find instances of Proposal by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK(Integer pk, boolean fetchSessions, boolean fetchProteins,
			boolean fetchShippings) {
		return "from Proposal3VO vo  " + (fetchSessions ? " left join fetch vo.sessionVOs " : "")
				+ (fetchShippings ? " left join fetch vo.shippingVOs " : "")
				+ (fetchProteins ? " left join fetch vo.proteinVOs " : "") + "where vo.proposalId = :pk";
	}

	// Generic HQL request to find all instances of Proposal
	// TODO choose between left/inner join
	private static final String FIND_ALL() {
		return "from Proposal3VO vo ";
	}

	
	private static final String FIND_CODE_NUMBER(String code, String number, boolean fetchSessions,
			boolean fetchProteins) {
		return "from Proposal3VO vo" + (fetchSessions ? " left join fetch vo.sessionVOs " : "")
				+ (fetchProteins ? " left join fetch vo.proteinVOs " : "")
				+ " where vo.code LIKE :code AND vo.number = :number ";
	}

	private static final String FIND_CODE(String code, boolean fetchSessions, boolean fetchProteins) {
		return "from Proposal3VO vo" + (fetchSessions ? " left join fetch vo.sessionVOs " : "")
				+ (fetchProteins ? " left join fetch vo.proteinVOs " : "") + " where vo.code LIKE :code ";
	}

	private final static String UPDATE_PROPOSALID_SESSIONS = " update BLSession  set proposalId = :newProposalId "
			+ " WHERE proposalId = :oldProposalId"; // 2 old value to be replaced

	private final static String UPDATE_PROPOSALID_SHIPPINGS = " update Shipping  set proposalId = :newProposalId "
			+ " WHERE proposalId = :oldProposalId"; // 2 old value to be replaced

	private final static String UPDATE_PROPOSALID_PROTEINS = " update Protein  set proposalId = :newProposalId "
			+ " WHERE proposalId = :oldProposalId"; // 2 old value to be replaced

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public Proposal3DAOBean() {
	};

	/* Creation/Update methods ---------------------------------------------- */

	/**
	 * <p>
	 * Insert the given value object. TODO update this comment for insertion details.
	 * </p>
	 * 
	 * @throws VOValidateException
	 *             if the value object data integrity is not ok.
	 */
	public void create(Proposal3VO vo) throws Exception {
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
	}

	/**
	 * <p>
	 * Update the given value object. TODO update this comment for update details.
	 * </p>
	 * 
	 * @throws VOValidateException
	 *             if the value object data integrity is not ok.
	 */
	public Proposal3VO update(Proposal3VO vo) throws Exception {
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
	public void delete(Proposal3VO vo) {
		entityManager.remove(vo);
	}

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * Returns the ProposalVO instance matching the given primary key.
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
	@SuppressWarnings("rawtypes")
	public Proposal3VO findByPk(Integer pk, boolean fetchSessions, boolean fetchProteins, boolean fetchShippings) {
		Query query = entityManager.createQuery(FIND_BY_PK(pk, fetchSessions, fetchProteins, fetchShippings))
				.setParameter("pk", pk);
		List listVOs = query.getResultList();
		if (listVOs == null || listVOs.isEmpty())
			return null;
		return (Proposal3VO) listVOs.toArray()[0];
	}

	/**
	 * <p>
	 * Returns the ProposalVO instances.
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
	public List<Proposal3VO> findAll() {
		Query query = entityManager.createQuery(FIND_ALL());
		List<Proposal3VO> vos = query.getResultList();
		return vos;
	}

	@SuppressWarnings("unchecked")
	public List<Proposal3VO> findByCodeAndNumber(String code, String number, boolean fetchSessions, boolean fetchProteins) {
		Query query;
		if (number == null)
			query = entityManager.createQuery(FIND_CODE(code, fetchSessions, fetchProteins)).setParameter("code", code);
		else
			query = entityManager.createQuery(FIND_CODE_NUMBER(code, number, fetchSessions, fetchProteins))
					.setParameter("code", code).setParameter("number", number);

		List<Proposal3VO> vos = query.getResultList();

		return vos;
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
	private void checkAndCompleteData(Proposal3VO vo, boolean create) throws Exception {
		if (create) {
			if (vo.getProposalId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getProposalId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}

	/**
	 * Finds a Proposal entity by its code and number and title (if title is null only search by code and number).
	 * 
	 * @param code
	 * @param number
	 * @param title
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List findFiltered(String code, String number, String title) {

		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(Proposal3VO.class);

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (code != null && !code.isEmpty()) {
			crit.add(Restrictions.like("code", code.toUpperCase()));
		}

		if (number != null && !number.isEmpty()) {
			crit.add(Restrictions.eq("number", number));
		}

		if (title != null && !title.isEmpty())
			crit.add(Restrictions.like("title", title));

		crit.addOrder(Order.desc("proposalId"));

		return crit.list();
	}

	/**
	 * update the proposalId, returns the nb of rows updated
	 * 
	 * @param newProposalId
	 * @param oldProposalId
	 * @return
	 */
	public Integer[] updateProposalId(Integer newProposalId, Integer oldProposalId) {
		Integer[] nbUpdated = new Integer[3];
		Query query = entityManager.createNativeQuery(UPDATE_PROPOSALID_SESSIONS)
				.setParameter("newProposalId", newProposalId).setParameter("oldProposalId", oldProposalId);
		nbUpdated[0] = query.executeUpdate();

		query = entityManager.createNativeQuery(UPDATE_PROPOSALID_SHIPPINGS)
				.setParameter("newProposalId", newProposalId).setParameter("oldProposalId", oldProposalId);
		nbUpdated[1] = query.executeUpdate();

		query = entityManager.createNativeQuery(UPDATE_PROPOSALID_PROTEINS)
				.setParameter("newProposalId", newProposalId).setParameter("oldProposalId", oldProposalId);
		nbUpdated[2] = query.executeUpdate();

		return nbUpdated;
	}
}
