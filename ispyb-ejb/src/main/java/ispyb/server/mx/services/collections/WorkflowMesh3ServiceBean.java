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
package ispyb.server.mx.services.collections;

import ispyb.server.common.util.ejb.EJBAccessCallback;
import ispyb.server.common.util.ejb.EJBAccessTemplate;

import ispyb.server.mx.vos.collections.WorkflowMesh3VO;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
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
 *  This session bean handles ISPyB WorkflowMesh3.
 * </p>
 */
@Stateless
public class WorkflowMesh3ServiceBean implements WorkflowMesh3Service,
		WorkflowMesh3ServiceLocal {

	private final static Logger LOG = Logger
			.getLogger(WorkflowMesh3ServiceBean.class);


	// Generic HQL request to find instances of WorkflowMesh3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK() {
		return "from WorkflowMesh3VO vo "
				+ "where vo.workflowMeshId = :pk";
	}

	// Generic HQL request to find all instances of WorkflowMesh3
	// TODO choose between left/inner join
	private static final String FIND_ALL() {
		return "from WorkflowMesh3VO vo ";
	}

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;
	
	@Resource
	private SessionContext context;

	public WorkflowMesh3ServiceBean() {
	};

	/**
	 * Create new WorkflowMesh3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public WorkflowMesh3VO create(final WorkflowMesh3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the WorkflowMesh3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public WorkflowMesh3VO update(final WorkflowMesh3VO vo) throws Exception {
	
		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the WorkflowMesh3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
	
		checkCreateChangeRemoveAccess();
		WorkflowMesh3VO vo = findByPk(pk);
		// TODO Edit this business code				
		delete(vo);
	}

	/**
	 * Remove the WorkflowMesh3
	 * @param vo the entity to remove.
	 */
	public void delete(final WorkflowMesh3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the WorkflowMesh3 value object
	 */
	public WorkflowMesh3VO findByPk(final Integer pk) throws Exception {
		
		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		try {
			return (WorkflowMesh3VO) entityManager
					.createQuery(FIND_BY_PK())
					.setParameter("pk", pk).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	// TODO remove following method if not adequate
	/**
	 * Find all WorkflowMesh3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<WorkflowMesh3VO> findAll()throws Exception {

		List<WorkflowMesh3VO> foundEntities = (List<WorkflowMesh3VO>) entityManager.createQuery(
				FIND_ALL()).getResultList();
		return foundEntities;
	}

	/**
	 * Check if user has access rights to create, change and remove WorkflowMesh3 entities. If not set rollback only and throw AccessDeniedException
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
	
		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
	}

	/**
	 * find the workflow mesh for a specified workflow
	 * @param workflowId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<WorkflowMesh3VO> findByWorkflowId(final Integer workflowId) throws Exception{

		Session session = (Session) this.entityManager.getDelegate();
		Criteria crit = session.createCriteria(WorkflowMesh3VO.class);

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (workflowId != null) {
			Criteria subCrit = crit.createCriteria("workflowVO");
			subCrit.add(Restrictions.eq("workflowId", workflowId));
		}
		crit.addOrder(Order.asc("workflowMeshId"));

		List<WorkflowMesh3VO> foundEntities = crit.list();
		return foundEntities;
	}
	

	/* Private methods ------------------------------------------------------ */

	/**
	 * Checks the data for integrity. E.g. if references and categories exist.
	 * @param vo the data to check
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @exception VOValidateException if data is not correct
	 */
	private void checkAndCompleteData(WorkflowMesh3VO vo, boolean create)
			throws Exception {

		if (create) {
			if (vo.getWorkflowMeshId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getWorkflowMeshId() == null) {
				throw new IllegalArgumentException(
						"Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
}
