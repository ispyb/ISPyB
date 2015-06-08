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

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.NoResultException;

import org.apache.log4j.Logger;

import ispyb.server.mx.vos.collections.Workflow3VO;

/**
 * <p>
 * 	The data access object for Workflow3 objects (rows of table
 *  Workflow).
 * </p>
 * @see {@link Workflow3DAO}
 */
@Stateless
public class Workflow3DAOBean implements Workflow3DAO {

	private final static Logger LOG = Logger.getLogger(Workflow3DAOBean.class);

	// Generic HQL request to find instances of Workflow3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK() {
		return "from Workflow3VO vo "
				+ "where vo.workflowId = :pk";
	}

	// Generic HQL request to find all instances of Workflow3
	private static final String FIND_ALL() {
		return "from Workflow3VO vo ";
	}
	
	/** Get workflow by status **/
	private static final String FIND_BY_STATUS() {
		return "from Workflow3VO vo where vo.status = :status ";
	}
	
	/** Get inputParameter by workFlowId **/
	private static final String FIND_INPUT_BY_WORKFLOW_ID() {
		return "from InputParameterWorkflow vo where vo.workflowId= :workflowId ";
	}
	
	private static final String COUNT_WF = 
		"SELECT count(w.workflowId) as nbW "+
		"FROM Workflow w, DataCollectionGroup g, BLSession s, Proposal p "+
		"WHERE w.workflowId = g.workflowId  and "+
		"      g.sessionId = s.sessionId and "+
		"      s.proposalId = p.proposalId and "+
		"      s.proposalId = p.proposalId and "+
		"      p.proposalCode != 'opid' and "+
		"      p.proposalId != 1170 and "+
		"     YEAR(g.startTime) = :year AND "+
		"     s.beamlineName like :beamline AND " +
		"     w.workflowType = :workflowType AND "+
		"     w.status = :status ";
	
	private static final String WORKFLOW_RESULT =  "SELECT w.logFilePath  "+
			"FROM Workflow w, DataCollectionGroup g, BLSession s, Proposal p "+
			"WHERE w.workflowId = g.workflowId  and "+
			"      g.sessionId = s.sessionId and "+
			"      s.proposalId = p.proposalId and "+
			"      s.proposalId = p.proposalId and "+
			"      p.proposalCode != 'opid' and "+
			"      p.proposalId != 1170 and "+
			"     YEAR(g.startTime) = :year AND "+
			"     s.beamlineName like :beamline AND " +
			"     w.workflowType = :workflowType AND "+
			"     w.status = 'Failure' ";

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	/* Creation/Update methods ---------------------------------------------- */

	/**
	 * <p>
	 *  Insert the given value object.
	 * 	TODO update this comment for insertion details.
	 * </p>
	 */
	public void create(Workflow3VO vo) throws Exception {
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
	}

	/**
	 * <p>
	 *  Update the given value object.
	 * 	TODO update this comment for update details.
	 * </p>
	 */
	public Workflow3VO update(Workflow3VO vo) throws Exception {
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
	public void delete(Workflow3VO vo) {
		entityManager.remove(vo);
	}

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * 	Returns the Workflow3VO instance matching the given primary key.
	 * </p>
	 * <p>
	 *  <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u>
	 *  if the value object has to be used out the EJB container.
	 * </p>
	 * @param pk the primary key of the object to load.
	 */
	public Workflow3VO findByPk(Integer pk) {
		try {
			return (Workflow3VO) entityManager
					.createQuery(FIND_BY_PK())
					.setParameter("pk", pk).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * <p>
	 * 	Returns the Workflow3VO instances.
	 * </p>
	 * <p>
	 *  <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u>
	 *  if the value object has to be used out the EJB container.
	 * </p>
	 */
	@SuppressWarnings("unchecked")
	public List<Workflow3VO> findAll() {
		return (List<Workflow3VO>) entityManager.createQuery(
				FIND_ALL()).getResultList();
	}

	

	/* Private methods ------------------------------------------------------ */

	/**
	 * Checks the data for integrity. E.g. if references and categories exist.
	 * @param vo the data to check
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @exception VOValidateException if data is not correct
	 */
	private void checkAndCompleteData(Workflow3VO vo, boolean create)
			throws Exception {

		if (create) {
			if (vo.getWorkflowId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getWorkflowId() == null) {
				throw new IllegalArgumentException(
						"Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
	public Integer countWF(String year, String beamline, String workflowType, String status) {
		try {
			String query = COUNT_WF;
			BigInteger ret = (BigInteger)this.entityManager.createNativeQuery(query)
					.setParameter("year", year)
					.setParameter("beamline", beamline)
					.setParameter("status", status)
					.setParameter("workflowType", workflowType).getSingleResult();
			return ret.intValue();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public List getWorkflowResult(String year, String beamline, String workflowType) {
		try {
			String query = WORKFLOW_RESULT;
			List ret = this.entityManager.createNativeQuery(query)
					.setParameter("year", year)
					.setParameter("beamline", beamline)
					.setParameter("workflowType", workflowType).getResultList();
			return ret;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	/** For BioSAXS WorkFlows **/
	@SuppressWarnings("unchecked")
	public List<Workflow3VO> findAll(String status) {
		try {
			String query = FIND_BY_STATUS();
			List<Workflow3VO> ret = this.entityManager.createQuery(query)
					.setParameter("status", status).getResultList();
			return ret;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Workflow3VO> findInputParametersByWorkflowId(int workflowId) {
		try {
			String query = FIND_INPUT_BY_WORKFLOW_ID();
			List<Workflow3VO> ret = this.entityManager.createQuery(query).setParameter("workflowId", workflowId).getResultList();
			return ret;
		} catch (NoResultException e) {
			return null;
		}
	}
	
}

