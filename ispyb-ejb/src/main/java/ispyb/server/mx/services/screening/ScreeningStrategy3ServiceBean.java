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


import ispyb.server.mx.vos.screening.ScreeningStrategy3VO;

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
 * This session bean handles ISPyB ScreeningStrategy3.
 * </p>
 */
@Stateless
public class ScreeningStrategy3ServiceBean implements ScreeningStrategy3Service, ScreeningStrategy3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(ScreeningStrategy3ServiceBean.class);

	// Generic HQL request to find instances of ScreeningStrategy3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK(boolean fetchScreeningStrategyWedge) {
		return "from ScreeningStrategy3VO vo " + (fetchScreeningStrategyWedge ? "left join fetch vo.screeningStrategyWedgeVOs " : "")
				 + "where vo.screeningStrategyId = :pk";
	}

	// Generic HQL request to find all instances of ScreeningStrategy3
	// TODO choose between left/inner join
	private static final String FIND_ALL(boolean fetchScreeningStrategyWedge) {
		return "from ScreeningStrategy3VO vo " + (fetchScreeningStrategyWedge ? "left join fetch vo.screeningStrategyWedgeVOs " : "");
	}

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;
	
	@Resource
	private SessionContext context;

	public ScreeningStrategy3ServiceBean() {
	};

	/**
	 * Create new ScreeningStrategy3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public ScreeningStrategy3VO create(final ScreeningStrategy3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the ScreeningStrategy3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public ScreeningStrategy3VO update(final ScreeningStrategy3VO vo) throws Exception {
	
		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the ScreeningStrategy3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		checkCreateChangeRemoveAccess();
		ScreeningStrategy3VO vo = findByPk(pk, false);
		// TODO Edit this business code
		delete(vo);
	}

	/**
	 * Remove the ScreeningStrategy3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final ScreeningStrategy3VO vo) throws Exception {
	
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
	 * @return the ScreeningStrategy3 value object
	 */
	public ScreeningStrategy3VO findByPk(final Integer pk, final boolean withScreeningStrategyWedge)
			throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		try{
			return (ScreeningStrategy3VO) entityManager.createQuery(FIND_BY_PK(withScreeningStrategyWedge))
				.setParameter("pk", pk).getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}


	// TODO remove following method if not adequate
	/**
	 * Find all ScreeningStrategy3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<ScreeningStrategy3VO> findAll(final boolean withScreeningStrategyWedge) throws Exception {
	
		List<ScreeningStrategy3VO> foundEntities = entityManager.createQuery(FIND_ALL(withScreeningStrategyWedge)).getResultList();
		return foundEntities;
	}

	/**
	 * Check if user has access rights to create, change and remove ScreeningStrategy3 entities. If not set rollback
	 * only and throw AccessDeniedException
	 * 
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
	
		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
	}

	public ScreeningStrategy3VO loadEager(ScreeningStrategy3VO vo) throws Exception{
		ScreeningStrategy3VO newVO = this.findByPk(vo.getScreeningStrategyId(),true);
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
	private void checkAndCompleteData(ScreeningStrategy3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getScreeningStrategyId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getScreeningStrategyId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}

}
