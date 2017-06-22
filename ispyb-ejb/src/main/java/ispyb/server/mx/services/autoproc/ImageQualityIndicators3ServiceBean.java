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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import ispyb.server.common.exceptions.AccessDeniedException;
import ispyb.server.common.util.ejb.EJBAccessCallback;
import ispyb.server.common.util.ejb.EJBAccessTemplate;

import ispyb.server.mx.vos.autoproc.ImageQualityIndicators3VO;
import ispyb.server.mx.vos.autoproc.ImageQualityIndicatorsWS3VO;

/**
 * <p>
 *  This session bean handles ISPyB ImageQualityIndicators3.
 * </p>
 */
@Stateless
public class ImageQualityIndicators3ServiceBean implements ImageQualityIndicators3Service,
		ImageQualityIndicators3ServiceLocal {

	private final static Logger LOG = Logger
			.getLogger(ImageQualityIndicators3ServiceBean.class);
	
	// Generic HQL request to find instances of ImageQualityIndicators3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK() {
		return "from ImageQualityIndicators3VO vo " + "where vo.imageQualityIndicatorsId = :pk";
	}

	// Generic HQL request to find all instances of ImageQualityIndicators3
	// TODO choose between left/inner join
	private static final String FIND_ALL() {
		return "from ImageQualityIndicators3VO vo " ;
	}

	private static String FIND_BY_DATACOLLECTION_ID = "select * " + " FROM ImageQualityIndicators q, Image i "
			+ "WHERE q.imageId = i.imageId  AND  i.dataCollectionId = :dataCollectionId ";

	private static String FIND_BY_IMAGE_ID = "select * FROM ImageQualityIndicators q " + "WHERE q.imageId = :imageId ";

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;
	
	@Resource
	private SessionContext context;

	public ImageQualityIndicators3ServiceBean() {
	};

	/**
	 * Create new ImageQualityIndicators3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public ImageQualityIndicators3VO create(final ImageQualityIndicators3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the ImageQualityIndicators3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public ImageQualityIndicators3VO update(final ImageQualityIndicators3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the ImageQualityIndicators3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		checkCreateChangeRemoveAccess();
		ImageQualityIndicators3VO vo = findByPk(pk);
		// TODO Edit this business code				
		delete(vo);
	}

	/**
	 * Remove the ImageQualityIndicators3
	 * @param vo the entity to remove.
	 */
	public void delete(final ImageQualityIndicators3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the ImageQualityIndicators3 value object
	 */
	public ImageQualityIndicators3VO findByPk(final Integer pk) throws Exception {
		
		checkCreateChangeRemoveAccess();
		try{
			return (ImageQualityIndicators3VO) entityManager.createQuery(FIND_BY_PK())
					.setParameter("pk", pk).getSingleResult();
			}catch(NoResultException e){
				return null;
			}
	}

	// TODO remove following method if not adequate
	/**
	 * Find all ImageQualityIndicators3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<ImageQualityIndicators3VO> findAll()
			throws Exception {

		List<ImageQualityIndicators3VO> foundEntities = entityManager.createQuery(FIND_ALL()).getResultList();
		return foundEntities;
	}

	/**
	 * Check if user has access rights to create, change and remove ImageQualityIndicators3 entities. If not set rollback only and throw AccessDeniedException
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
	}
	
	/**
	 * 
	 * @param dataCollectionId
	 * @return
	 * @throws Exception
	 */
	
	public List<ImageQualityIndicators3VO> findByDataCollectionIdDAO(Integer dataCollectionId) {
		String query = FIND_BY_DATACOLLECTION_ID;
		List<ImageQualityIndicators3VO> listVOs = this.entityManager
				.createNativeQuery(query, "imageQualityIndicatorsNativeQuery")
				.setParameter("dataCollectionId", dataCollectionId).getResultList();
		if (listVOs == null || listVOs.isEmpty())
			return null;
		return listVOs;
	}
	
	public ImageQualityIndicatorsWS3VO[] findForWSByDataCollectionId(final Integer dataCollectionId) throws Exception{
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		EJBAccessCallback callBack = new EJBAccessCallback() {

			public ImageQualityIndicatorsWS3VO[] doInEJBAccess(Object parent) throws Exception {
				List<ImageQualityIndicators3VO> foundEntities = findByDataCollectionIdDAO(dataCollectionId);
				ImageQualityIndicatorsWS3VO[] vos;
				vos = getWSImageQualityIndicatorsVOs(foundEntities);
				return vos;
			};
		};
		ImageQualityIndicatorsWS3VO[] ret = (ImageQualityIndicatorsWS3VO[]) template.execute(callBack);

		return ret;
	}
	
	private ImageQualityIndicatorsWS3VO[] getWSImageQualityIndicatorsVOs(List<ImageQualityIndicators3VO> entities) throws CloneNotSupportedException {
		if(entities == null)
			return null;
		List<ImageQualityIndicatorsWS3VO> results = new ArrayList<ImageQualityIndicatorsWS3VO>(entities.size());
		for (ImageQualityIndicators3VO vo : entities) {
			ImageQualityIndicatorsWS3VO otherVO = getWSImageQualityIndicatorsVO(vo);
			results.add(otherVO);
		}
		ImageQualityIndicatorsWS3VO[] tmpResults = new ImageQualityIndicatorsWS3VO[results.size()];
		return (ImageQualityIndicatorsWS3VO[]) results.toArray(tmpResults);
	}
	
	private ImageQualityIndicatorsWS3VO getWSImageQualityIndicatorsVO(ImageQualityIndicators3VO vo) throws CloneNotSupportedException {
		ImageQualityIndicators3VO otherVO = getLightImageQualityIndicators3VO(vo);
		Integer imageId = null;
		Integer autoProcProgramId = null;
		imageId =otherVO.getImageVOId();
		autoProcProgramId = otherVO.getAutoProcProgramVOId();
		otherVO.setImageVO(null);
		otherVO.setAutoProcProgramVO(null);
		ImageQualityIndicatorsWS3VO wsImageQualityIndicators = new ImageQualityIndicatorsWS3VO(otherVO);
		wsImageQualityIndicators.setImageId(imageId);
		wsImageQualityIndicators.setAutoProcProgramId(autoProcProgramId);
		return wsImageQualityIndicators;
	}
	
	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private ImageQualityIndicators3VO getLightImageQualityIndicators3VO(ImageQualityIndicators3VO vo) throws CloneNotSupportedException {
		ImageQualityIndicators3VO otherVO = (ImageQualityIndicators3VO) vo.clone();
		//otherVO.set(null);
		return otherVO;
	}
	
	/**
	 * 
	 * @param imageId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ImageQualityIndicators3VO> findByImageId(final Integer imageId) throws Exception{
		
		String query = FIND_BY_IMAGE_ID;
		List<ImageQualityIndicators3VO> listVOs = this.entityManager
				.createNativeQuery(query, "imageQualityIndicatorsNativeQuery").setParameter("imageId", imageId)
				.getResultList();
		if (listVOs == null || listVOs.isEmpty())
			listVOs = null;
		
		List<ImageQualityIndicators3VO> foundEntities = listVOs;
		return foundEntities;
	}
	
	/**
	 * 
	 * @param dataCollectionId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ImageQualityIndicators3VO> findByDataCollectionId(final Integer dataCollectionId) throws Exception{
		
		List<ImageQualityIndicators3VO> foundEntities = findByDataCollectionIdDAO(dataCollectionId);
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
	private void checkAndCompleteData(ImageQualityIndicators3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getImageQualityIndicatorsId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getImageQualityIndicatorsId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
}