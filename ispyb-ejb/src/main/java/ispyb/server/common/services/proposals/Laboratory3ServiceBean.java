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

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import ispyb.common.util.StringUtils;
import ispyb.server.common.exceptions.AccessDeniedException;
import ispyb.server.common.vos.proposals.Laboratory3VO;

/**
 * <p>
 * This session bean handles ISPyB Laboratory3.
 * </p>
 */
@Stateless
public class Laboratory3ServiceBean implements Laboratory3Service, Laboratory3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(Laboratory3ServiceBean.class);

	private static String SELECT_LABORATORY = "SELECT l.laboratoryId, l.laboratoryUUID, l.name, l.address, "
			+ "l.city, l.country, l.url, l.organization, l.laboratoryExtPk  ";

	private static String FIND_BY_PROPOSAL_CODE_NUMBER = SELECT_LABORATORY
			+ " FROM Laboratory l, Person p, Proposal pro "
			+ "WHERE l.laboratoryId = p.laboratoryId AND p.personId = pro.personId AND pro.proposalCode like :code AND pro.proposalNumber = :number ";

	// Generic HQL request to find instances of Laboratory3 by pk
	private static final String FIND_BY_PK() {
		return "from Laboratory3VO vo  where vo.laboratoryId = :pk";
	}

	private static final String FIND_BY_LABORATORY_EXT_PK() {
		return "from Laboratory3VO vo  where vo.laboratoryExtPk = :labExtPk order by vo.laboratoryId desc";
	}

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public Laboratory3ServiceBean() {
	};

	public Laboratory3VO merge(Laboratory3VO detachedInstance) {
		try {
			Laboratory3VO result = entityManager.merge(detachedInstance);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	/**
	 * Create new Laboratory3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public Laboratory3VO create(final Laboratory3VO vo) throws Exception {
		return this.merge(vo);
	}

	/**
	 * Update the Laboratory3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public Laboratory3VO update(final Laboratory3VO vo) throws Exception {
		return this.merge(vo);
	}

	/**
	 * Remove the Laboratory3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		Laboratory3VO vo = findByPk(pk);
		checkCreateChangeRemoveAccess();
		delete(vo);
	}

	/**
	 * Remove the Laboratory3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Laboratory3VO vo) throws Exception {
		entityManager.remove(vo);
	}

	/**
	 * Finds a labo entity by its primary key 
	 * 
	 * @param pk
	 *            the primary key
	 * @return the Laboratory3 value object
	 */
	public Laboratory3VO findByPk(final Integer pk) throws Exception {
		try {
			return (Laboratory3VO) entityManager.createQuery(FIND_BY_PK())
					.setParameter("pk", pk).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Finds a labo entity by its extLabPk
	 * @param laboExtPk  the ext labo key
	 * @return the Laboratory3 value object
	 */
	@SuppressWarnings("unchecked")
	public Laboratory3VO findByLaboratoryExtPk(final Integer laboExtPk) {
		List<Laboratory3VO> listVOs =  this.entityManager.createQuery(FIND_BY_LABORATORY_EXT_PK())
					.setParameter("labExtPk", laboExtPk).getResultList();
		if (listVOs == null || listVOs.isEmpty())
			return null;
			
		return (Laboratory3VO) listVOs.toArray()[0];
		
	}

	@SuppressWarnings("unchecked")
	public Laboratory3VO findLaboratoryByProposalCodeAndNumber(String code, String number) {
		
		String query = FIND_BY_PROPOSAL_CODE_NUMBER;
		List<Laboratory3VO> listVOs = this.entityManager.createNativeQuery(query, "laboratoryNativeQuery")
				.setParameter("code", code).setParameter("number", number).getResultList();
		if (listVOs == null || listVOs.isEmpty())
			return null;
		
		return (Laboratory3VO) listVOs.toArray()[0];
	}

	@SuppressWarnings("unchecked")
	public List<Laboratory3VO> findFiltered(String laboratoryName, String city, String country) {

		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(Laboratory3VO.class);

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (!StringUtils.isEmpty(laboratoryName))
			crit.add(Restrictions.like("name", laboratoryName));
		if (!StringUtils.isEmpty(city))
			crit.add(Restrictions.like("city", city));
		if (!StringUtils.isEmpty(country))
			crit.add(Restrictions.like("country", country));

		return crit.list();

	}

	public List<Laboratory3VO> findByNameAndCityAndCountry(final String laboratoryName, final String city,
			final String country) throws Exception {
		
		return this.findFiltered(laboratoryName, city, country);
	}

	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private Laboratory3VO getLightLaboratoryVO(Laboratory3VO vo) throws CloneNotSupportedException {
		if (vo == null) return null;
		Laboratory3VO otherVO = (Laboratory3VO) vo.clone();
		return otherVO;
	}

	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private Laboratory3VO getLaboratoryVO(Laboratory3VO vo) {
		return vo;
	}

	/**
	 * Check if user has access rights to create, change and remove Laboratory3 entities. If not set rollback only and
	 * throw AccessDeniedException
	 * 
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
		//TODO
		return;
	}


}