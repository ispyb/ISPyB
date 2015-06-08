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

package ispyb.server.mx.daos.screening;

import ispyb.server.mx.vos.screening.ScreeningOutput3VO;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * <p>
 * The data access object for ScreeningOutput3 objects (rows of table ScreeningOutput).
 * </p>
 * 
 * @see {@link ScreeningOutput3DAO}
 */
@Stateless
public class ScreeningOutput3DAOBean implements ScreeningOutput3DAO {

	private final static Logger LOG = Logger.getLogger(ScreeningOutput3DAOBean.class);

	// Generic HQL request to find instances of ScreeningOutput3 by pkc
	// TODO choose between left/inner join
	private static final String FIND_BY_PK(boolean fetchScreeningStrategy, boolean fetchScreeningOutpuLattice) {
		return "from ScreeningOutput3VO vo " + (fetchScreeningStrategy ? "left join fetch vo.screeningStrategyVOs " : "")
				 + (fetchScreeningOutpuLattice ? "left join fetch vo.screeningOutputLatticeVOs " : "")+ "where vo.screeningOutputId = :pk";
	}

	// Generic HQL request to find all instances of ScreeningOutput3
	// TODO choose between left/inner join
	private static final String FIND_ALL(boolean fetchScreeningStrategy, boolean fetchScreeningOutpuLattice) {
		return "from ScreeningOutput3VO vo " + (fetchScreeningStrategy ? "left join fetch vo.screeningStrategyVOs " : "")
		 + (fetchScreeningOutpuLattice ? "left join fetch vo.screeningOutputLatticeVOs " : "");
	}

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	/* Creation/Update methods ---------------------------------------------- */

	/**
	 * <p>
	 * Insert the given value object. TODO update this comment for insertion details.
	 * </p>
	 */
	public void create(ScreeningOutput3VO vo) throws Exception {
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
	}

	/**
	 * <p>
	 * Update the given value object. TODO update this comment for update details.
	 * </p>
	 */
	public ScreeningOutput3VO update(ScreeningOutput3VO vo) throws Exception {
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
	public void delete(ScreeningOutput3VO vo) {
		entityManager.remove(vo);
	}

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * Returns the ScreeningOutput3VO instance matching the given primary key.
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
	public ScreeningOutput3VO findByPk(Integer pk, boolean fetchScreeningStrategy, boolean fetchScreeningOutpuLattice) {
		try{
			return (ScreeningOutput3VO) entityManager.createQuery(FIND_BY_PK(fetchScreeningStrategy, fetchScreeningOutpuLattice))
				.setParameter("pk", pk).getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}

	/**
	 * <p>
	 * Returns the ScreeningOutput3VO instances.
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
	public List<ScreeningOutput3VO> findAll(boolean fetchScreeningStrategy,boolean fetchScreeningOutpuLattice) {
		return entityManager.createQuery(FIND_ALL(fetchScreeningStrategy, fetchScreeningOutpuLattice)).getResultList();
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
	private void checkAndCompleteData(ScreeningOutput3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getScreeningOutputId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getScreeningOutputId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
}
