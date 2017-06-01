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
import ispyb.server.mx.vos.autoproc.AutoProcStatus3VO;

/**
 * <p>
 *  This session bean handles ISPyB AutoProcStatus3.
 * </p>
 */
@Stateless
public class AutoProcStatus3ServiceBean implements AutoProcStatus3Service,
		AutoProcStatus3ServiceLocal {

	private final static Logger LOG = Logger
			.getLogger(AutoProcStatus3ServiceBean.class);

	// Generic HQL request to find instances of AutoProcStatus3 by pk
	private static final String FIND_BY_PK() {
		return "from AutoProcStatus3VO vo "
				+ "where vo.autoProcStatusId = :pk";
	}

	// Generic HQL request to find all instances of AutoProcStatus3
	private static final String FIND_ALL() {
		return "from AutoProcStatus3VO vo ";
	}

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public AutoProcStatus3ServiceBean() {
	};

	/**
	 * Create new AutoProcStatus3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public AutoProcStatus3VO create(final AutoProcStatus3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the AutoProcStatus3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public AutoProcStatus3VO update(final AutoProcStatus3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the AutoProcStatus3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		checkCreateChangeRemoveAccess();
		AutoProcStatus3VO vo = findByPk(pk);
		// TODO Edit this business code				
		delete(vo);
	}

	/**
	 * Remove the AutoProcStatus3
	 * @param vo the entity to remove.
	 */
	public void delete(final AutoProcStatus3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @return the AutoProcStatus3 value object
	 */
	public AutoProcStatus3VO findByPk(final Integer pk) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		try{
			return (AutoProcStatus3VO) entityManager.createQuery(FIND_BY_PK()).setParameter("pk", pk).getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}

	/**
	 * Find all AutoProcStatus3s and set linked value objects if necessary
	 */
	@SuppressWarnings("unchecked")
	public List<AutoProcStatus3VO> findAll() throws Exception {

		List<AutoProcStatus3VO> foundEntities = entityManager.createQuery(FIND_ALL()).getResultList();
		return foundEntities;
	}

	/**
	 * Check if user has access rights to create, change and remove AutoProcStatus3 entities. If not set rollback only and throw AccessDeniedException
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
	private void checkAndCompleteData(AutoProcStatus3VO vo, boolean create)
			throws Exception {

		if (create) {
			if (vo.getAutoProcStatusId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getAutoProcStatusId() == null) {
				throw new IllegalArgumentException(
						"Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
}