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

import ispyb.server.mx.vos.autoproc.IspybAutoProcAttachment3VO;

/**
 * <p>
 *  This session bean handles ISPyB IspybAutoProcAttachment3.
 * </p>
 */
@Stateless
public class IspybAutoProcAttachment3ServiceBean implements IspybAutoProcAttachment3Service,
		IspybAutoProcAttachment3ServiceLocal {

	private final static Logger LOG = Logger
			.getLogger(IspybAutoProcAttachment3ServiceBean.class);

	// Generic HQL request to find instances of IspybAutoProcAttachment3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK() {
		return "from IspybAutoProcAttachment3VO vo "  + "where vo.autoProcAttachmentId = :pk";
	}

	// Generic HQL request to find all instances of IspybAutoProcAttachment3
	// TODO choose between left/inner join
	private static final String FIND_ALL() {
		return "from IspybAutoProcAttachment3VO vo " ;
	}

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public IspybAutoProcAttachment3ServiceBean() {
	};

	/**
	 * Create new IspybAutoProcAttachment3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public IspybAutoProcAttachment3VO create(final IspybAutoProcAttachment3VO vo) throws Exception {
		
		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the IspybAutoProcAttachment3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public IspybAutoProcAttachment3VO update(final IspybAutoProcAttachment3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the IspybAutoProcAttachment3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		
		checkCreateChangeRemoveAccess();
		IspybAutoProcAttachment3VO vo = findByPk(pk);		
		delete(vo);
	}

	/**
	 * Remove the IspybAutoProcAttachment3
	 * @param vo the entity to remove.
	 */
	public void delete(final IspybAutoProcAttachment3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @return the IspybAutoProcAttachment3 value object
	 */
	public IspybAutoProcAttachment3VO findByPk(final Integer pk) throws Exception {

		checkCreateChangeRemoveAccess();
		try{
			return (IspybAutoProcAttachment3VO) entityManager.createQuery(FIND_BY_PK())
					.setParameter("pk", pk).getSingleResult();
			}catch(NoResultException e){
				return null;
			}
	}

	/**
	 * Find all IspybAutoProcAttachment3s and set linked value objects if necessary
	 */
	@SuppressWarnings("unchecked")
	public List<IspybAutoProcAttachment3VO> findAll()
			throws Exception {
	
		List<IspybAutoProcAttachment3VO> foundEntities = entityManager.createQuery(FIND_ALL()).getResultList();
		return foundEntities;
	}

	/**
	 * Check if user has access rights to create, change and remove IspybAutoProcAttachment3 entities. If not set rollback only and throw AccessDeniedException
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
	
		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
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
	private void checkAndCompleteData(IspybAutoProcAttachment3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getAutoProcAttachmentId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getAutoProcAttachmentId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
}