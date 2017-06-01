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

import ispyb.server.mx.vos.autoproc.ModelBuilding3VO;

/**
 * <p>
 *  This session bean handles ISPyB ModelBuilding3.
 * </p>
 */
@Stateless
public class ModelBuilding3ServiceBean implements ModelBuilding3Service,
		ModelBuilding3ServiceLocal {

	private final static Logger LOG = Logger
			.getLogger(ModelBuilding3ServiceBean.class);
	
	// Generic HQL request to find instances of ModelBuilding3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK() {
		return "from ModelBuilding3VO vo "
				+ "where vo.modelBuildingId = :modelBuildingId";
	}

	// Generic HQL request to find all instances of ModelBuilding3
	// TODO choose between left/inner join
	private static final String FIND_ALL() {
		return "from ModelBuilding3VO vo ";
	}

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public ModelBuilding3ServiceBean() {
	};

	/**
	 * Create new ModelBuilding3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public ModelBuilding3VO create(final ModelBuilding3VO vo) throws Exception {
	
		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the ModelBuilding3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public ModelBuilding3VO update(final ModelBuilding3VO vo) throws Exception {
	
		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the ModelBuilding3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
	
		checkCreateChangeRemoveAccess();
		ModelBuilding3VO vo = findByPk(pk);				
		delete(vo);
	}

	/**
	 * Remove the ModelBuilding3
	 * @param vo the entity to remove.
	 */
	public void delete(final ModelBuilding3VO vo) throws Exception {
		
		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		entityManager.remove(vo);	
	}
		
	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @return the ModelBuilding3 value object
	 */
	public ModelBuilding3VO findByPk(final Integer pk) throws Exception {
	
		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		try {
			return (ModelBuilding3VO) entityManager
					.createQuery(FIND_BY_PK())
					.setParameter("modelBuildingId", pk).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	/**
	 * Find all ModelBuilding3s and set linked value objects if necessary
	 */
	@SuppressWarnings("unchecked")
	public List<ModelBuilding3VO> findAll()
			throws Exception {

		List<ModelBuilding3VO> foundEntities = entityManager.createQuery(FIND_ALL()).getResultList();
		return foundEntities;
	}

	/**
	 * Check if user has access rights to create, change and remove ModelBuilding3 entities. If not set rollback only and throw AccessDeniedException
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
	}

	@SuppressWarnings("unchecked")
	public List<ModelBuilding3VO> findFiltered(final Integer phasingAnalysisId) throws Exception {
		Session session = (Session) this.entityManager.getDelegate();
		Criteria criteria = session.createCriteria(ModelBuilding3VO.class);
		
		if (phasingAnalysisId != null) {
			Criteria subCrit = criteria.createCriteria("phasingAnalysisVO");
			subCrit.add(Restrictions.eq("phasingAnalysisId", phasingAnalysisId));
			subCrit.addOrder(Order.asc("phasingAnalysisId"));
		}
		List<ModelBuilding3VO> foundEntities = criteria.list();
		return foundEntities;
	}

	
	/* Private methods ------------------------------------------------------ */

	/**
	 * Checks the data for integrity. E.g. if references and categories exist.
	 * @param vo the data to check
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @exception VOValidateException if data is not correct
	 */
	private void checkAndCompleteData(ModelBuilding3VO vo, boolean create)
			throws Exception {

		if (create) {
			if (vo.getModelBuildingId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getModelBuildingId() == null) {
				throw new IllegalArgumentException(
						"Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
}