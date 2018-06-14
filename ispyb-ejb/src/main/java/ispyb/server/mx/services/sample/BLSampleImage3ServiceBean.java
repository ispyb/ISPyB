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
package ispyb.server.mx.services.sample;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import ispyb.server.mx.vos.sample.BLSampleImage3VO;

/**
 * <p>
 * This session bean handles ISPyB BLSample3.
 * </p>
 */
@Stateless
public class BLSampleImage3ServiceBean implements BLSampleImage3Service, BLSampleImage3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(BLSampleImage3ServiceBean.class);

	// Generic HQL request to find instances of BLSample3 by pk
	private static final String FIND_BY_PK() {
		return "from BLSampleImage3VO vo where vo.blSampleImageId = :pk";
	}

	// Generic HQL request to find instances of BLSample3 by pk
	private static final String FIND_BY_BLSAMPLE() {
		return "from BLSampleImage3VO vo where vo.blSampleId = :blsampleId";
	}
	
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;
	
	@Resource
	private SessionContext context;

	public BLSampleImage3ServiceBean() {
	};

	/**
	 * Create new BLSample3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public BLSampleImage3VO create(final BLSampleImage3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the BLSample3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public BLSampleImage3VO update(final BLSampleImage3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the BLSample3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		checkCreateChangeRemoveAccess();
		BLSampleImage3VO vo = findByPk(pk);
		delete(vo);
	}

	/**
	 * Remove the BLSample3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final BLSampleImage3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the BLSample3 value object
	 */
	public BLSampleImage3VO findByPk(final Integer pk) throws Exception {
	
		checkCreateChangeRemoveAccess();
		try {
			return (BLSampleImage3VO) entityManager.createQuery(FIND_BY_PK()).setParameter("pk", pk)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BLSampleImage3VO> findByBlSampleId(Integer blsampleId) throws Exception {	
		List<BLSampleImage3VO> foundEntities = entityManager.createQuery(FIND_BY_BLSAMPLE()).getResultList();
		return foundEntities;
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
	private void checkAndCompleteData(BLSampleImage3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getBlSampleImageId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getBlSampleImageId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
	/**
	 * Check if user has access rights to create, change and remove BLSample3 entities. If not set rollback only and
	 * throw AccessDeniedException
	 * 
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
	}

	
}
