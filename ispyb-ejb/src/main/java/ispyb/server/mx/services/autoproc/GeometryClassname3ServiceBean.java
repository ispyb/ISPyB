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

import ispyb.server.common.exceptions.AccessDeniedException;

import ispyb.server.mx.vos.autoproc.GeometryClassname3VO;

/**
 * <p>
 *  This session bean handles ISPyB GeometryClassname3.
 * </p>
 */
@Stateless
public class GeometryClassname3ServiceBean implements GeometryClassname3Service, GeometryClassname3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(GeometryClassname3ServiceBean.class);
	
	// Generic HQL request to find instances of GeometryClassname3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK(boolean fetchSpaceGroups) {
		return "from GeometryClassname3VO vo "+ (fetchSpaceGroups ? "left join fetch vo.spaceGroupVOs " : "") + "where vo.geometryClassnameId = :pk";
	}

	// Generic HQL request to find all instances of GeometryClassname3
	// TODO choose between left/inner join
	private static final String FIND_ALL(boolean fetchSpaceGroups) {
		return "from GeometryClassname3VO vo " + (fetchSpaceGroups ? "left join fetch vo.spaceGroupVOs " : "")+" ORDER BY vo.geometryOrder";
	}
	
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public GeometryClassname3ServiceBean() {
	};

	/**
	 * Create new GeometryClassname3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public GeometryClassname3VO create(final GeometryClassname3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the GeometryClassname3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public GeometryClassname3VO update(final GeometryClassname3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the GeometryClassname3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		
		checkCreateChangeRemoveAccess();
		GeometryClassname3VO vo = findByPk(pk, false);			
		delete(vo);
	}

	/**
	 * Remove the GeometryClassname3
	 * @param vo the entity to remove.
	 */
	public void delete(final GeometryClassname3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @return the GeometryClassname3 value object
	 */
	public GeometryClassname3VO findByPk(final Integer pk, final boolean fetchSpaceGroups) throws Exception {

		checkCreateChangeRemoveAccess();
		try {
			return (GeometryClassname3VO) entityManager
					.createQuery(FIND_BY_PK(fetchSpaceGroups))
					.setParameter("pk", pk).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	/**
	 * Find all GeometryClassname3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<GeometryClassname3VO> findAll(final boolean fetchSpaceGroups)
			throws Exception {

		List<GeometryClassname3VO> foundEntities = entityManager.createQuery(FIND_ALL(fetchSpaceGroups)).getResultList();
		return foundEntities;
	}

	/**
	 * Check if user has access rights to create, change and remove GeometryClassname3 entities. If not set rollback only and throw AccessDeniedException
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
	}

	/* Private methods ------------------------------------------------------ */

	/**
	 * Checks the data for integrity. E.g. if references and categories exist.
	 * @param vo the data to check
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @exception VOValidateException if data is not correct
	 */
	private void checkAndCompleteData(GeometryClassname3VO vo, boolean create)
			throws Exception {

		if (create) {
			if (vo.getGeometryClassnameId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getGeometryClassnameId() == null) {
				throw new IllegalArgumentException(
						"Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}

}