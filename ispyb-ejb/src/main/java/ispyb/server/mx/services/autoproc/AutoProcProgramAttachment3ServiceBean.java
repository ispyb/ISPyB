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

import ispyb.server.common.util.ejb.EJBAccessCallback;
import ispyb.server.common.util.ejb.EJBAccessTemplate;

import ispyb.server.mx.vos.autoproc.AutoProcProgramAttachment3VO;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * <p>
 *  This session bean handles ISPyB AutoProcProgramAttachment3.
 * </p>
 */
@Stateless
public class AutoProcProgramAttachment3ServiceBean implements AutoProcProgramAttachment3Service,
		AutoProcProgramAttachment3ServiceLocal {

	private final static Logger LOG = Logger
			.getLogger(AutoProcProgramAttachment3ServiceBean.class);

	// Generic HQL request to find instances of AutoProcProgramAttachment3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK() {
		return "from AutoProcProgramAttachment3VO vo " 
				+ "where vo.autoProcProgramAttachmentId = :pk";
	}

	// Generic HQL request to find all instances of AutoProcProgramAttachment3
	// TODO choose between left/inner join
	private static final String FIND_ALL() {
		return "from AutoProcProgramAttachment3VO vo " ;
	}
	
	private static final String FIND_AUTOPROC_XSCALE = "SELECT * " +
			"FROM AutoProcProgramAttachment  " +
			"WHERE  autoProcProgramId = :autoProcProgramId AND " +
			"fileName like '%XSCALE%' ";

	private static final String FIND_AUTOPROC_NOANOM_AIMLESS = "SELECT * " +
			"FROM AutoProcProgramAttachment  " +
			"WHERE  autoProcProgramId = :autoProcProgramId AND " +
			"fileName like '%_noanom_aimless%' ";

	private static final String FIND_AUTOPROC_AIMLESS = "SELECT * " +
			"FROM AutoProcProgramAttachment  " +
			"WHERE  autoProcProgramId = :autoProcProgramId AND " +
			"fileName like '%_aimless%' ";

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public AutoProcProgramAttachment3ServiceBean() {
	};

	/**
	 * Create new AutoProcProgramAttachment3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public AutoProcProgramAttachment3VO create(final AutoProcProgramAttachment3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the AutoProcProgramAttachment3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public AutoProcProgramAttachment3VO update(final AutoProcProgramAttachment3VO vo) throws Exception {
	
		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the AutoProcProgramAttachment3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		checkCreateChangeRemoveAccess();
		AutoProcProgramAttachment3VO vo = findByPk(pk);
		// TODO Edit this business code				
		delete(vo);
	}

	/**
	 * Remove the AutoProcProgramAttachment3
	 * @param vo the entity to remove.
	 */
	public void delete(final AutoProcProgramAttachment3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		entityManager.remove(vo);
	}


	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the AutoProcProgramAttachment3 value object
	 */
	public AutoProcProgramAttachment3VO findByPk(final Integer pk) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		try{
			return (AutoProcProgramAttachment3VO) entityManager.createQuery(FIND_BY_PK())
				.setParameter("pk", pk).getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}

	// TODO remove following method if not adequate
	/**
	 * Find all AutoProcProgramAttachment3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<AutoProcProgramAttachment3VO> findAll()
			throws Exception {

		List<AutoProcProgramAttachment3VO> foundEntities = entityManager.createQuery(FIND_ALL()).getResultList();
		return foundEntities;
	}

	/**
	 * Check if user has access rights to create, change and remove AutoProcProgramAttachment3 entities. If not set rollback only and throw AccessDeniedException
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
		
				//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
				//autService.checkUserRightToChangeAdminData();
	}
	
	/**
	 * find xscale files attachments
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<AutoProcProgramAttachment3VO> findXScale(final Integer autoProcProgramId) throws Exception {
		
		String query = FIND_AUTOPROC_XSCALE ;
		try{
			
			List<AutoProcProgramAttachment3VO> listVOs = this.entityManager.createNativeQuery(query, "autoProcProgramAttachmentNativeQuery")
					.setParameter("autoProcProgramId", autoProcProgramId).getResultList();
			return listVOs;
		}catch(Exception e){
			return null;
		}
	}

	/**
	 * find noanom correct files attachments
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<AutoProcProgramAttachment3VO> findNoanomCorrect(final Integer autoProcProgramId) throws Exception {

		String query = FIND_AUTOPROC_NOANOM_AIMLESS ;
		try{

			List<AutoProcProgramAttachment3VO> listVOs = this.entityManager.createNativeQuery(query, "autoProcProgramAttachmentNativeQuery")
					.setParameter("autoProcProgramId", autoProcProgramId).getResultList();
			return listVOs;
		}catch(Exception e){
			return null;
		}
	}

	/**
	 * find anom correct files attachments
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<AutoProcProgramAttachment3VO> findAimless(final Integer autoProcProgramId) throws Exception {

		String query = FIND_AUTOPROC_AIMLESS ;
		try{

			List<AutoProcProgramAttachment3VO> listVOs = this.entityManager.createNativeQuery(query, "autoProcProgramAttachmentNativeQuery")
					.setParameter("autoProcProgramId", autoProcProgramId).getResultList();
			return listVOs;
		}catch(Exception e){
			return null;
		}
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
	private void checkAndCompleteData(AutoProcProgramAttachment3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getAutoProcProgramAttachmentId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getAutoProcProgramAttachmentId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}

}