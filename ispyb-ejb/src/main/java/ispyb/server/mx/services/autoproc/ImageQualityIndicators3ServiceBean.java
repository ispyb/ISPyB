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
import ispyb.server.mx.daos.autoproc.ImageQualityIndicators3DAO;
import ispyb.server.mx.vos.autoproc.ImageQualityIndicators3VO;
import ispyb.server.mx.vos.autoproc.ImageQualityIndicatorsWS3VO;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

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

	@EJB
	private ImageQualityIndicators3DAO dao;

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
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (ImageQualityIndicators3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				dao.create(vo);
				return vo;
			}

		});
	}

	/**
	 * Update the ImageQualityIndicators3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public ImageQualityIndicators3VO update(final ImageQualityIndicators3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (ImageQualityIndicators3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				return dao.update(vo);
			}

		});
	}

	/**
	 * Remove the ImageQualityIndicators3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				ImageQualityIndicators3VO vo = findByPk(pk);
				// TODO Edit this business code				
				delete(vo);
				return vo;
			}

		});

	}

	/**
	 * Remove the ImageQualityIndicators3
	 * @param vo the entity to remove.
	 */
	public void delete(final ImageQualityIndicators3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				dao.delete(vo);
				return vo;
			}

		});
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the ImageQualityIndicators3 value object
	 */
	public ImageQualityIndicators3VO findByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (ImageQualityIndicators3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				ImageQualityIndicators3VO found = dao.findByPk(pk);
				return found;
			}

		});
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
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<ImageQualityIndicators3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<ImageQualityIndicators3VO> foundEntities = dao.findAll();
				return foundEntities;
			}

		});
	}

	/**
	 * Check if user has access rights to create, change and remove ImageQualityIndicators3 entities. If not set rollback only and throw AccessDeniedException
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
				//autService.checkUserRightToChangeAdminData();
				return null;
			}

		});
	}

	
	
	/**
	 * 
	 * @param dataCollectionId
	 * @return
	 * @throws Exception
	 */
	public ImageQualityIndicatorsWS3VO[] findForWSByDataCollectionId(final Integer dataCollectionId) throws Exception{
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		EJBAccessCallback callBack = new EJBAccessCallback() {

			public ImageQualityIndicatorsWS3VO[] doInEJBAccess(Object parent) throws Exception {
				List<ImageQualityIndicators3VO> foundEntities = dao.findByDataCollectionId(dataCollectionId);
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
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<ImageQualityIndicators3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<ImageQualityIndicators3VO> foundEntities = dao.findByImageId(imageId);
				return foundEntities;
			}

		});
	}
	
	/**
	 * 
	 * @param dataCollectionId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ImageQualityIndicators3VO> findByDataCollectionId(final Integer dataCollectionId) throws Exception{
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<ImageQualityIndicators3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<ImageQualityIndicators3VO> foundEntities = dao.findByDataCollectionId(dataCollectionId);
				return foundEntities;
			}

		});
	}

}