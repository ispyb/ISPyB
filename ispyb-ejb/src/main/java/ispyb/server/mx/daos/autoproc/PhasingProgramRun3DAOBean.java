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

package ispyb.server.mx.daos.autoproc;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;



import ispyb.server.mx.vos.autoproc.PhasingProgramRun3VO;

/**
 * <p>
 * 	The data access object for PhasingProgram3 objects (rows of table
 *  PhasingProgramRun).
 * </p>
 * @see {@link PhasingProgramRun3DAO}
 */
@Stateless
public class PhasingProgramRun3DAOBean implements PhasingProgramRun3DAO {

	private final static Logger LOG = Logger.getLogger(PhasingProgramRun3DAOBean.class);

	// Generic HQL request to find instances of PhasingProgram3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK(boolean fetchAttachment) {
		return "from PhasingProgramRun3VO vo "
				+ (fetchAttachment ? "left join fetch vo.attachmentVOs " : "")
				+ "where vo.phasingProgramRunId = :phasingProgramRunId";
	}

	// Generic HQL request to find all instances of PhasingProgram3
	// TODO choose between left/inner join
	private static final String FIND_ALL(boolean fetchAttachment) {
		return "from PhasingProgramRun3VO vo "
				+ (fetchAttachment ? "left join fetch vo.attachmentVOs " : "");
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
	public void create(PhasingProgramRun3VO vo) throws Exception {
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
	}

	/**
	 * <p>
	 *  Update the given value object.
	 * 	TODO update this comment for update details.
	 * </p>
	 */
	public PhasingProgramRun3VO update(PhasingProgramRun3VO vo) throws Exception {
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
	public void delete(PhasingProgramRun3VO vo) {
		entityManager.remove(vo);
	}

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * 	Returns the PhasingProgramRun3VO instance matching the given primary key.
	 * </p>
	 * <p>
	 *  <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u>
	 *  if the value object has to be used out the EJB container.
	 * </p>
	 * @param pk the primary key of the object to load.
	 * @param fetchAttachment if true, the linked instances by the relation "relation1" will be set.
	 */
	public PhasingProgramRun3VO findByPk(Integer pk, boolean fetchAttachment) {
		try {
			return (PhasingProgramRun3VO) entityManager
					.createQuery(FIND_BY_PK(fetchAttachment))
					.setParameter("phasingProgramRunId", pk).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * <p>
	 * 	Returns the PhasingProgramRun3VO instances.
	 * </p>
	 * <p>
	 *  <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u>
	 *  if the value object has to be used out the EJB container.
	 * </p>
	 * @param fetchAttachment if true, the linked instances by the relation "relation1" will be set.
	 */
	@SuppressWarnings("unchecked")
	public List<PhasingProgramRun3VO> findAll(boolean fetchAttachment) {
		return (List<PhasingProgramRun3VO>) entityManager.createQuery(
				FIND_ALL(fetchAttachment)).getResultList();
	}

	

	/* Private methods ------------------------------------------------------ */

	/**
	 * Checks the data for integrity. E.g. if references and categories exist.
	 * @param vo the data to check
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @exception VOValidateException if data is not correct
	 */
	private void checkAndCompleteData(PhasingProgramRun3VO vo, boolean create)
			throws Exception {

		if (create) {
			if (vo.getPhasingProgramRunId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getPhasingProgramRunId() == null) {
				throw new IllegalArgumentException(
						"Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
}
