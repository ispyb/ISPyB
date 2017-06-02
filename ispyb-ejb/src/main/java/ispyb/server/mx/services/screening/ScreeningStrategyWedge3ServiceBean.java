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
package ispyb.server.mx.services.screening;

import ispyb.server.mx.vos.screening.ScreeningStrategyWedge3VO;

import java.util.List;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * <p>
 * This session bean handles ISPyB ScreeningStrategyWedge3.
 * </p>
 */
@Stateless
public class ScreeningStrategyWedge3ServiceBean implements ScreeningStrategyWedge3Service,
		ScreeningStrategyWedge3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(ScreeningStrategyWedge3ServiceBean.class);

	// Generic HQL request to find instances of ScreeningStrategyWedge3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK(boolean fetchScreeningStrategySubWedge) {
		return "from ScreeningStrategyWedge3VO vo " + (fetchScreeningStrategySubWedge ? "left join fetch vo.screeningStrategySubWedgeVOs " : "")
				 + " where vo.screeningStrategyWedgeId = :pk";
	}

	// Generic HQL request to find all instances of ScreeningStrategyWedge3
	// TODO choose between left/inner join
	private static final String FIND_ALL(boolean fetchScreeningStrategySubWedge) {
		return "from ScreeningStrategyWedge3VO vo " + (fetchScreeningStrategySubWedge ? "left join fetch vo.vo.screeningStrategySubWedgeVOs " : "");
	}

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@Resource
	private SessionContext context;

	public ScreeningStrategyWedge3ServiceBean() {
	};

	/**
	 * Create new ScreeningStrategyWedge3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public ScreeningStrategyWedge3VO create(final ScreeningStrategyWedge3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the ScreeningStrategyWedge3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public ScreeningStrategyWedge3VO update(final ScreeningStrategyWedge3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}
	
	/**
	 * Remove the ScreeningStrategyWedge3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
	
		checkCreateChangeRemoveAccess();
		ScreeningStrategyWedge3VO vo = findByPk(pk, false);
		// TODO Edit this business code
		delete(vo);
	}

	/**
	 * Remove the ScreeningStrategyWedge3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final ScreeningStrategyWedge3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the ScreeningStrategyWedge3 value object
	 */
	public ScreeningStrategyWedge3VO findByPk(final Integer pk, final boolean withScreeningStrategySubWedge)
			throws Exception {
	
		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		try{
			return (ScreeningStrategyWedge3VO) entityManager.createQuery(FIND_BY_PK(withScreeningStrategySubWedge))
				.setParameter("pk", pk).getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}

	// TODO remove following method if not adequate
	/**
	 * Find all ScreeningStrategyWedge3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<ScreeningStrategyWedge3VO> findAll(final boolean withScreeningStrategySubWedge) throws Exception {

		List<ScreeningStrategyWedge3VO> foundEntities = entityManager.createQuery(FIND_ALL(withScreeningStrategySubWedge)).getResultList();
		return foundEntities;
	}

	/**
	 * Check if user has access rights to create, change and remove ScreeningStrategyWedge3 entities. If not set
	 * rollback only and throw AccessDeniedException
	 * 
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
	}

	
	
	public ScreeningStrategyWedge3VO loadEager(ScreeningStrategyWedge3VO vo) throws Exception{
		ScreeningStrategyWedge3VO newVO = this.findByPk(vo.getScreeningStrategyWedgeId(),true);
		return newVO;
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
	private void checkAndCompleteData(ScreeningStrategyWedge3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getScreeningStrategyWedgeId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getScreeningStrategyWedgeId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}

}