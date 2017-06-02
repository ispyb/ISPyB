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

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import ispyb.server.common.exceptions.AccessDeniedException;

import ispyb.server.mx.vos.autoproc.PhasingHasScaling3VO;

/**
 * <p>
 *  This session bean handles ISPyB PhasingHasScaling3.
 * </p>
 */
@Stateless
public class PhasingHasScaling3ServiceBean implements PhasingHasScaling3Service,PhasingHasScaling3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(PhasingHasScaling3ServiceBean.class);

	// Generic HQL request to find instances of PhasingHasScaling3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK() {
		return "from PhasingHasScaling3VO vo "
				+ "where vo.phasingHasScalingId = :pk";
	}

	// Generic HQL request to find all instances of PhasingHasScaling3
	// TODO choose between left/inner join
	private static final String FIND_ALL() {
		return "from PhasingHasScaling3VO vo ";
	}

	private static final String FIND_BY_AUTOPROC = "SELECT * "
			+ "FROM Phasing_has_Scaling, AutoProcScaling "
			+ "WHERE Phasing_has_Scaling.autoProcScalingId = AutoProcScaling.autoProcScalingId AND "
			+ "AutoProcScaling.autoProcId = :autoProcId ";
	
	
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public PhasingHasScaling3ServiceBean() {
	};

	/**
	 * Create new PhasingHasScaling3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public PhasingHasScaling3VO create(final PhasingHasScaling3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the PhasingHasScaling3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public PhasingHasScaling3VO update(final PhasingHasScaling3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the PhasingHasScaling3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
	
		checkCreateChangeRemoveAccess();
		PhasingHasScaling3VO vo = findByPk(pk);			
		delete(vo);
	}

	/**
	 * Remove the PhasingHasScaling3
	 * @param vo the entity to remove.
	 */
	public void delete(final PhasingHasScaling3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @return the PhasingHasScaling3 value object
	 */
	public PhasingHasScaling3VO findByPk(final Integer pk) throws Exception {
	
		checkCreateChangeRemoveAccess();
		try {
			return (PhasingHasScaling3VO) entityManager
					.createQuery(FIND_BY_PK())
					.setParameter("pk", pk).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Find all PhasingHasScaling3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<PhasingHasScaling3VO> findAll()throws Exception {

		List<PhasingHasScaling3VO> foundEntities = entityManager.createQuery(FIND_ALL()).getResultList();
		return foundEntities;
	}

	/**
	 * Check if user has access rights to create, change and remove PhasingHasScaling3 entities. If not set rollback only and throw AccessDeniedException
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
	}

	@SuppressWarnings("unchecked")
	public List<PhasingHasScaling3VO> findFiltered(final Integer autoProcScalingId)throws Exception {
		Session session = (Session) this.entityManager.getDelegate();
		Criteria criteria = session.createCriteria(PhasingHasScaling3VO.class);
		
		if (autoProcScalingId != null) {
			Criteria subCritAutoProcScaling = criteria.createCriteria("autoProcScalingVO");
			subCritAutoProcScaling.add(Restrictions.eq("autoProcScalingId", autoProcScalingId));
			subCritAutoProcScaling.addOrder(Order.asc("autoProcScalingId"));
		}
		List<PhasingHasScaling3VO> foundEntities = criteria.list();
		return foundEntities;
	}

	@SuppressWarnings("unchecked")
	public List<PhasingHasScaling3VO> findByAutoProc(final Integer autoProcId) throws Exception {
		
		String query = FIND_BY_AUTOPROC;
		List<PhasingHasScaling3VO> listVOs = this.entityManager.createNativeQuery(query, "phasingHasScalingNativeQuery")
				.setParameter("autoProcId", autoProcId).getResultList();
		if (listVOs == null || listVOs.isEmpty())
			listVOs = null;
		List<PhasingHasScaling3VO> foundEntities = listVOs;
		return foundEntities;
	}
	
	/* Private methods ------------------------------------------------------ */

	/**
	 * Checks the data for integrity. E.g. if references and categories exist.
	 * @param vo the data to check
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @exception VOValidateException if data is not correct
	 */
	private void checkAndCompleteData(PhasingHasScaling3VO vo, boolean create)
			throws Exception {

		if (create) {
			if (vo.getPhasingHasScalingId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getPhasingHasScalingId() == null) {
				throw new IllegalArgumentException(
						"Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
}