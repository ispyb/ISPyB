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

package ispyb.server.mx.daos.collections;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.NoResultException;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;




import ispyb.server.mx.vos.collections.WorkflowMesh3VO;

/**
 * <p>
 * 	The data access object for WorkflowMesh3 objects (rows of table
 *  WorkflowMesh).
 * </p>
 * @see {@link WorkflowMesh3DAO}
 */
@Stateless
public class WorkflowMesh3DAOBean implements WorkflowMesh3DAO {

	private final static Logger LOG = Logger.getLogger(WorkflowMesh3DAOBean.class);

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

	/* Creation/Update methods ---------------------------------------------- */

	/**
	 * <p>
	 *  Insert the given value object.
	 * 	TODO update this comment for insertion details.
	 * </p>
	 */
	public void create(WorkflowMesh3VO vo) throws Exception {
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
	}

	/**
	 * <p>
	 *  Update the given value object.
	 * 	TODO update this comment for update details.
	 * </p>
	 */
	public WorkflowMesh3VO update(WorkflowMesh3VO vo) throws Exception {
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/* Deletion methods ----------------------------------------------------- */

	/**
	 * <p>
	 * 	Deletes the given value object.
	 * </p>
	 * @param vo the value object to delete.
	 */
	public void delete(WorkflowMesh3VO vo) {
		entityManager.remove(vo);
	}

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * 	Returns the WorkflowMesh3VO instance matching the given primary key.
	 * </p>
	 * <p>
	 *  <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u>
	 *  if the value object has to be used out the EJB container.
	 * </p>
	 * @param pk the primary key of the object to load.
	 * @param fetchRelation1 if true, the linked instances by the relation "relation1" will be set.
	 * @param fetchRelation2 if true, the linked instances by the relation "relation2" will be set.
	 */
	public WorkflowMesh3VO findByPk(Integer pk) {
		try {
			return (WorkflowMesh3VO) entityManager
					.createQuery(FIND_BY_PK())
					.setParameter("pk", pk).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * <p>
	 * 	Returns the WorkflowMesh3VO instances.
	 * </p>
	 * <p>
	 *  <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u>
	 *  if the value object has to be used out the EJB container.
	 * </p>
	 * @param fetchRelation1 if true, the linked instances by the relation "relation1" will be set.
	 * @param fetchRelation2 if true, the linked instances by the relation "relation2" will be set.
	 */
	@SuppressWarnings("unchecked")
	public List<WorkflowMesh3VO> findAll() {
		return (List<WorkflowMesh3VO>) entityManager.createQuery(
				FIND_ALL()).getResultList();
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
	
	@SuppressWarnings("unchecked")
	public List<WorkflowMesh3VO> findByWorkflowId(Integer workflowId){
		Session session = (Session) this.entityManager.getDelegate();
		Criteria crit = session.createCriteria(WorkflowMesh3VO.class);

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (workflowId != null) {
			Criteria subCrit = crit.createCriteria("workflowVO");
			subCrit.add(Restrictions.eq("workflowId", workflowId));
		}
		crit.addOrder(Order.asc("workflowMeshId"));
		return crit.list();
	}
}

