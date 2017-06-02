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


import ispyb.server.mx.vos.screening.ScreeningOutput3VO;

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
 * This session bean handles ISPyB ScreeningOutput3.
 * </p>
 */
@Stateless
public class ScreeningOutput3ServiceBean implements ScreeningOutput3Service, ScreeningOutput3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(ScreeningOutput3ServiceBean.class);

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

	@Resource
	private SessionContext context;

	public ScreeningOutput3ServiceBean() {
	};

	/**
	 * Create new ScreeningOutput3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public ScreeningOutput3VO create(final ScreeningOutput3VO vo) throws Exception {
	
		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the ScreeningOutput3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public ScreeningOutput3VO update(final ScreeningOutput3VO vo) throws Exception {
	
		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}
	
	/**
	 * Remove the ScreeningOutput3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		checkCreateChangeRemoveAccess();
		ScreeningOutput3VO vo = findByPk(pk, false, false);
		// TODO Edit this business code
		delete(vo);
	}

	/**
	 * Remove the ScreeningOutput3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final ScreeningOutput3VO vo) throws Exception {

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
	 * @return the ScreeningOutput3 value object
	 */
	public ScreeningOutput3VO findByPk(final Integer pk, final boolean withScreeningStrategy, final boolean withScreeningOutpuLattice)
			throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		try{
			return (ScreeningOutput3VO) entityManager.createQuery(FIND_BY_PK(withScreeningStrategy, withScreeningOutpuLattice))
				.setParameter("pk", pk).getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}


	// TODO remove following method if not adequate
	/**
	 * Find all ScreeningOutput3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<ScreeningOutput3VO> findAll(final boolean withScreeningStrategy, final boolean withScreeningOutpuLattice) throws Exception {

		List<ScreeningOutput3VO> foundEntities = entityManager.createQuery(FIND_ALL(withScreeningStrategy, withScreeningOutpuLattice)).getResultList();
		return foundEntities;
	}

	/**
	 * Check if user has access rights to create, change and remove ScreeningOutput3 entities. If not set rollback only
	 * and throw AccessDeniedException
	 * 
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
	}


	
	public ScreeningOutput3VO loadEager(ScreeningOutput3VO vo) throws Exception{
		ScreeningOutput3VO newVO = this.findByPk(vo.getScreeningOutputId(),true, true);
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