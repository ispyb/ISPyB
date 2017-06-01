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
package ispyb.server.mx.services.collections;

import ispyb.server.common.util.ejb.EJBAccessCallback;
import ispyb.server.common.util.ejb.EJBAccessTemplate;

import ispyb.server.mx.vos.collections.Image3VO;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * <p>
 *  This session bean handles ISPyB Image3.
 * </p>
 */
@Stateless
public class Image3ServiceBean implements Image3Service,
		Image3ServiceLocal {

	private final static Logger LOG = Logger
			.getLogger(Image3ServiceBean.class);

	// Generic HQL request to find instances of Image3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK() {
		return "from Image3VO vo " + "where vo.imageId = :pk";
	}

	// Generic HQL request to find all instances of Image3
	// TODO choose between left/inner join
	private static final String FIND_ALL() {
		return "from Image3VO vo ";
	}

	private static String FIND_BY_IMAGE_PROPOSAL = "SELECT DISTINCT i.imageId, i.dataCollectionId, i.imageNumber, " +
			"i.fileName, i.fileLocation, i.measuredIntensity, i.jpegFileFullPath, i.jpegThumbnailFileFullPath, " +
			"i.temperature, i.cumulativeIntensity, i.synchrotronCurrent, i.comments, i.machineMessage " +
			" FROM Image i, DataCollection dc, DataCollectionGroup dcg, BLSession ses, Proposal pro "
			+ "WHERE  i.imageId  = :imageId AND i.dataCollectionId = dc.dataCollectionId AND " +
			"dc.dataCollectionGroupId = dcg.dataCollectionGroupId AND " + 
			"dcg.sessionId = ses.sessionId AND ses.proposalId = pro.proposalId AND pro.proposalId = :proposalId";
	
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@Resource
	private SessionContext context;

	public Image3ServiceBean() {
	};

	/**
	 * Create new Image3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public Image3VO create(final Image3VO vo) throws Exception {
	
		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}


	/**
	 * Update the Image3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public Image3VO update(final Image3VO vo) throws Exception {
	
		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the Image3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		checkCreateChangeRemoveAccess();
		Image3VO vo = findByPk(pk);
		// TODO Edit this business code				
		delete(vo);
	}

	/**
	 * Remove the Image3
	 * @param vo the entity to remove.
	 */
	public void delete(final Image3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the Image3 value object
	 */
	public Image3VO findByPk(final Integer pk) throws Exception {
	
		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		try{
			return (Image3VO) entityManager.createQuery(FIND_BY_PK()).setParameter("pk", pk).getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}

	// TODO remove following method if not adequate
	/**
	 * Find all Image3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<Image3VO> findAll()
			throws Exception {
	
		List<Image3VO> foundEntities = entityManager.createQuery(FIND_ALL()).getResultList();
		return foundEntities;
	}

	/**
	 * Check if user has access rights to create, change and remove Image3 entities. If not set rollback only and throw AccessDeniedException
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
	
		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
	}
	
	/**
	 * 
	 * @param fileLocation
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Image3VO> findFiltered(final String fileLocation, final String fileName) throws Exception{
	
		Session session = (Session) this.entityManager.getDelegate();
		Criteria crit = session.createCriteria(Image3VO.class);

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (fileLocation != null && !fileLocation.isEmpty()) {
			crit.add(Restrictions.like("fileLocation", fileLocation));
		}

		if (fileName != null && !fileName.isEmpty()) {
			crit.add(Restrictions.like("fileName", fileName));
		}
		
		crit.addOrder(Order.desc("imageId"));

		List<Image3VO> foundEntities = crit.list();
		return foundEntities;
	}
	
	@SuppressWarnings("unchecked")
	public List<Image3VO> findByDataCollectionId(final Integer dataCollectionId) throws Exception {

		Session session = (Session) this.entityManager.getDelegate();
		Criteria crit = session.createCriteria(Image3VO.class);

		Criteria subCrit = crit.createCriteria("dataCollectionVO");

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !
		
		if (dataCollectionId != null) {
			subCrit.add(Restrictions.eq("dataCollectionId", dataCollectionId));
		}
		
		crit.addOrder(Order.desc("imageId"));
		
		List<Image3VO> foundEntities = crit.list();
		return foundEntities;
	}
	
	@SuppressWarnings("unchecked")
	public List<Image3VO> findByDataCollectionGroupId(final Integer dataCollectionGroupId) throws Exception {
	
		Session session = (Session) this.entityManager.getDelegate();
		Criteria crit = session.createCriteria(Image3VO.class);

		Criteria subCrit = crit.createCriteria("dataCollectionVO");

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (dataCollectionGroupId != null){
			Criteria dataCollectionGroupCrit = subCrit.createCriteria("dataCollectionGroupVO");
			dataCollectionGroupCrit.add(Restrictions.eq("dataCollectionGroupId", dataCollectionGroupId));
		}

		crit.addOrder(Order.desc("imageId"));

		List<Image3VO> foundEntities = crit.list();
		return foundEntities;
	}

	
	/**
	 * two variables to guarantee the user fecths only its own images
	 * @param imageId
	 * @param proposalId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Image3VO> findByImageIdAndProposalId (final Integer imageId, final Integer proposalId)throws Exception{

		String query = FIND_BY_IMAGE_PROPOSAL;
		List<Image3VO> listVOs = this.entityManager.createNativeQuery(query, "imageNativeQuery")
				.setParameter("imageId", imageId).setParameter("proposalId", proposalId).getResultList();
		if (listVOs == null || listVOs.isEmpty())
			listVOs = null;
		List<Image3VO> foundEntities = listVOs;
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
	private void checkAndCompleteData(Image3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getImageId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getImageId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}

}