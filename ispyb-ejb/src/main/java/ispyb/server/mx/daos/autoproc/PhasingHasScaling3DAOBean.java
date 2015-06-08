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
import javax.persistence.PersistenceContext;
import javax.persistence.NoResultException;

import org.apache.log4j.Logger;


import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import ispyb.server.mx.vos.autoproc.PhasingHasScaling3VO;

/**
 * <p>
 * 	The data access object for PhasingHasScaling3 objects (rows of table
 *  Phasing_has_Scaling).
 * </p>
 * @see {@link PhasingHasScaling3DAO}
 */
@Stateless
public class PhasingHasScaling3DAOBean implements PhasingHasScaling3DAO {

	private final static Logger LOG = Logger.getLogger(PhasingHasScaling3DAOBean.class);

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

	/* Creation/Update methods ---------------------------------------------- */

	/**
	 * <p>
	 *  Insert the given value object.
	 * 	TODO update this comment for insertion details.
	 * </p>
	 */
	public void create(PhasingHasScaling3VO vo) throws Exception {
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
	}

	/**
	 * <p>
	 *  Update the given value object.
	 * 	TODO update this comment for update details.
	 * </p>
	 */
	public PhasingHasScaling3VO update(PhasingHasScaling3VO vo) throws Exception {
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
	public void delete(PhasingHasScaling3VO vo) {
		entityManager.remove(vo);
	}

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * 	Returns the PhasingHasScaling3VO instance matching the given primary key.
	 * </p>
	 * <p>
	 *  <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u>
	 *  if the value object has to be used out the EJB container.
	 * </p>
	 * @param pk the primary key of the object to load.
	 */
	public PhasingHasScaling3VO findByPk(Integer pk) {
		try {
			return (PhasingHasScaling3VO) entityManager
					.createQuery(FIND_BY_PK())
					.setParameter("pk", pk).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * <p>
	 * 	Returns the PhasingHasScaling3VO instances.
	 * </p>
	 * <p>
	 *  <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u>
	 *  if the value object has to be used out the EJB container.
	 * </p>
	 */
	@SuppressWarnings("unchecked")
	public List<PhasingHasScaling3VO> findAll() {
		return (List<PhasingHasScaling3VO>) entityManager.createQuery(
				FIND_ALL()).getResultList();
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
	
	
	@SuppressWarnings("unchecked")
	public List<PhasingHasScaling3VO> findFiltered(Integer autoProcScalingId) {
		Session session = (Session) this.entityManager.getDelegate();
		Criteria criteria = session.createCriteria(PhasingHasScaling3VO.class);
		
		if (autoProcScalingId != null) {
			Criteria subCritAutoProcScaling = criteria.createCriteria("autoProcScalingVO");
			subCritAutoProcScaling.add(Restrictions.eq("autoProcScalingId", autoProcScalingId));
			subCritAutoProcScaling.addOrder(Order.asc("autoProcScalingId"));
		}
		
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<PhasingHasScaling3VO> findByAutoProc(Integer autoProcId) {
		String query = FIND_BY_AUTOPROC;
		List<PhasingHasScaling3VO> listVOs = this.entityManager.createNativeQuery(query, "phasingHasScalingNativeQuery")
				.setParameter("autoProcId", autoProcId).getResultList();
		if (listVOs == null || listVOs.isEmpty())
			return null;
		return listVOs;
	}


}
