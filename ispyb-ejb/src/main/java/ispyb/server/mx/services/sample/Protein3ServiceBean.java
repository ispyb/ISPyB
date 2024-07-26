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
package ispyb.server.mx.services.sample;


import ispyb.server.mx.services.ws.rest.WsServiceBean;
import ispyb.server.mx.vos.sample.Protein3VO;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

/**
 * <p>
 * This session bean handles ISPyB Protein3.
 * </p>
 */
@Stateless
public class Protein3ServiceBean extends WsServiceBean implements Protein3Service, Protein3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(Protein3ServiceBean.class);

	// Generic HQL request to find instances of Protein3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK(boolean fetchCrystals) {
		return "from Protein3VO vo " + (fetchCrystals ? " left join fetch vo.crystalVOs " : "")
				+ " where vo.proteinId = :pk";
	}

	// Generic HQL request to find all instances of Protein3
	// TODO choose between left/inner join
	private static final String FIND_ALL(boolean fetchLink1) {
		return "from Protein3VO vo " + (fetchLink1 ? "left join fetch vo.crystalVOs " : "");
	}

	private final static String UPDATE_PROPOSALID_STATEMENT = " update Protein  set proposalId = :newProposalId "
			+ " WHERE proposalId = :oldProposalId"; // 2 old value to be replaced

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;
	
	@Resource
	private SessionContext context;

	public Protein3ServiceBean() {
	};

	/**
	 * Create new Protein3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public Protein3VO create(final Protein3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the Protein3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public Protein3VO update(final Protein3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the Protein3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		
		checkCreateChangeRemoveAccess();
		Protein3VO vo = findByPk(pk, false);
		// TODO Edit this business code
		delete(vo);
	}



	/**
	 * Remove the Protein3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Protein3VO vo) throws Exception {

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
	 * @return the Protein3 value object
	 */
	public Protein3VO findByPk(final Integer pk, final boolean withLink1) throws Exception {
	
		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		try {
			Query query = entityManager.createQuery(FIND_BY_PK(withLink1)).setParameter("pk", pk);
			return (Protein3VO) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	// TODO remove following method if not adequate
	/**
	 * Find all Protein3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<Protein3VO> findAll(final boolean withLink1) throws Exception {

		List<Protein3VO> foundEntities = entityManager.createQuery(FIND_ALL(withLink1)).getResultList();
		return foundEntities;
	}

	@SuppressWarnings("unchecked")
	public List<Protein3VO> findByAcronymAndProposalId(final Integer proposalId, final String acronym, final boolean withCrystal, final boolean sortByAcronym) throws Exception {
		
		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(Protein3VO.class);
		Criteria subCrit = crit.createCriteria("proposalVO");

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (proposalId != null) {
			subCrit.add(Restrictions.eq("proposalId", proposalId));
		}

		if (acronym != null && !acronym.isEmpty()) {
			crit.add(Restrictions.like("acronym", acronym.toUpperCase()));
		}

		if (withCrystal) {
			crit.setFetchMode("crystalVOs", FetchMode.JOIN);
		}
		if (sortByAcronym) {
			crit.addOrder(Order.asc("acronym"));
		} else
			crit.addOrder(Order.desc("proteinId"));

		List<Protein3VO> foundEntities = crit.list();
		return foundEntities;
	}
	
	public List<Protein3VO> findByAcronymAndProposalId(final Integer proposalId, final String acronym) throws Exception {
		return findByAcronymAndProposalId(proposalId, acronym, false, false);
	}

	public Protein3VO loadEager(Protein3VO vo) throws Exception {
		Protein3VO newVO = this.findByPk(vo.getProteinId(), true);
		return newVO;
	}
	

	private String getViewTableQuery(){
		return this.getQueryFromResourceFile("/queries/ProteinServiceBean/getViewTableQuery.sql");
	}
	

	/**
	 * Check if user has access rights to create, change and remove Protein3 entities. If not set rollback only and
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
	 * update the proposalId, returns the nb of rows updated
	 * 
	 * @param newProposalId
	 * @param oldProposalId
	 * @return
	 * @throws Exception
	 */
	public Integer updateProposalId(final Integer newProposalId, final Integer oldProposalId) throws Exception{
		
		int nbUpdated = 0;
		Query query = entityManager.createNativeQuery(UPDATE_PROPOSALID_STATEMENT)
				.setParameter("newProposalId", newProposalId).setParameter("oldProposalId", oldProposalId);
		nbUpdated = query.executeUpdate();

		return new Integer(nbUpdated);
	}

	@Override
	public List<Protein3VO> findByProposalId(Integer proposalId)
			throws Exception {
		return this.findByAcronymAndProposalId(proposalId, null, false, false);
	}
	
	public List<Protein3VO> findByProposalId(final Integer proposalId, final boolean withCrystal, boolean sortByAcronym) throws Exception{
		return this.findByAcronymAndProposalId(proposalId, null, withCrystal, sortByAcronym);
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
	private void checkAndCompleteData(Protein3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getProteinId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getProteinId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}

	
	@Override
	public List<Map<String, Object>>  getStatsByProposal(int proposalId) {
		String mySQLQuery = getViewTableQuery() + " where proposalId = :proposalId";		
		Session session = (Session) this.entityManager.getDelegate();
		LOG.debug("Update the group_concat max length");
		SQLQuery updateGroupConcat = session.createSQLQuery("SET SESSION group_concat_max_len = 1000000");
		updateGroupConcat.executeUpdate();
		SQLQuery query = session.createSQLQuery(mySQLQuery);
		query.setParameter("proposalId", proposalId);		
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		return executeSQLQuery(query);
	}
	
}