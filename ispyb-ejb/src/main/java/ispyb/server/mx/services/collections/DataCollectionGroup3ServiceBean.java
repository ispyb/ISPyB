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
import ispyb.server.mx.daos.collections.DataCollectionGroup3DAO;
import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
import ispyb.server.mx.vos.collections.DataCollectionGroupWS3VO;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
<<<<<<< HEAD
=======
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import ispyb.server.common.exceptions.AccessDeniedException;

import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
import ispyb.server.mx.vos.collections.DataCollectionGroupWS3VO;
>>>>>>> aa35504f989cd6f3f35a4019958215da79e405e3

/**
 * <p>
 * This session bean handles ISPyB DataCollectionGroup3.
 * </p>
 */
@Stateless
public class DataCollectionGroup3ServiceBean implements DataCollectionGroup3Service, DataCollectionGroup3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(DataCollectionGroup3ServiceBean.class);

	@EJB
	private DataCollectionGroup3DAO dao;

	@Resource
	private SessionContext context;

	public DataCollectionGroup3ServiceBean() {
	};

	/**
	 * Create new DataCollectionGroup3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public DataCollectionGroup3VO create(final DataCollectionGroup3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (DataCollectionGroup3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				dao.create(vo);
				return vo;
			}

		});
	}

	/**
	 * Update the DataCollectionGroup3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public DataCollectionGroup3VO update(final DataCollectionGroup3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (DataCollectionGroup3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				return dao.update(vo);
			}

		});
	}

	/**
	 * Remove the DataCollectionGroup3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				DataCollectionGroup3VO vo = findByPk(pk, false, false);
				// TODO Edit this business code
				delete(vo);
				return vo;
			}

		});

	}

	/**
	 * Remove the DataCollectionGroup3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final DataCollectionGroup3VO vo) throws Exception {
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
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the DataCollectionGroup3 value object
	 */
	public DataCollectionGroup3VO findByPk(final Integer pk, final boolean withDataCollection, final boolean withScreening) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (DataCollectionGroup3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				DataCollectionGroup3VO found = dao.findByPk(pk, withDataCollection, withScreening);
				return found;
			}

		});
	}

	/**
	 * Find a dataCollectionGroup by its primary key -- webservices object
	 * 
	 * @param pk
	 * @param withLink1
	 * @param withLink2
	 * @return
	 * @throws Exception
	 */
	public DataCollectionGroupWS3VO findForWSByPk(final Integer pk, final boolean withDataCollection, final boolean withScreening) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (DataCollectionGroupWS3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				DataCollectionGroup3VO found = dao.findByPk(pk, withDataCollection, withScreening);
				DataCollectionGroupWS3VO sesLight = getWSDataCollectionGroupVO(found);
				return sesLight;
			}

		});
	}

	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private DataCollectionGroup3VO getLightDataCollectionGroup3VO(DataCollectionGroup3VO vo)
			throws CloneNotSupportedException {
		if (vo == null) 
			return null;
		DataCollectionGroup3VO otherVO = (DataCollectionGroup3VO) vo.clone();
		otherVO.setDataCollectionVOs(null);
		otherVO.setScreeningVOs(null);
		return otherVO;
	}

	private DataCollectionGroupWS3VO getWSDataCollectionGroupVO(DataCollectionGroup3VO vo)
			throws CloneNotSupportedException {
		DataCollectionGroup3VO otherVO = getLightDataCollectionGroup3VO(vo);
		Integer sessionId = null;
		Integer blSampleId = null;
		sessionId = otherVO.getSessionVOId();
		blSampleId = otherVO.getBlSampleVOId();
		otherVO.setSessionVO(null);
		otherVO.setBlSampleVO(null);
		DataCollectionGroupWS3VO wsDataCollectionGroup = new DataCollectionGroupWS3VO(otherVO);
		wsDataCollectionGroup.setSessionId(sessionId);
		wsDataCollectionGroup.setBlSampleId(blSampleId);
		return wsDataCollectionGroup;
	}

	// TODO remove following method if not adequate
	/**
	 * Find all DataCollectionGroup3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<DataCollectionGroup3VO> findAll(final boolean withDataCollection) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<DataCollectionGroup3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<DataCollectionGroup3VO> foundEntities = dao.findAll(withDataCollection);
				return foundEntities;
			}

		});
	}

	/**
	 * Check if user has access rights to create, change and remove DataCollectionGroup3 entities. If not set rollback
	 * only and throw AccessDeniedException
	 * 
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				// AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator
				// .getInstance().getService(
				// AuthorizationServiceLocalHome.class); // TODO change method to the one checking the needed access
				// rights
				// autService.checkUserRightToChangeAdminData();
				return null;
			}

		});
	}

	/**
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public DataCollectionGroup3VO loadEager(DataCollectionGroup3VO vo) throws Exception {
		DataCollectionGroup3VO newVO = this.findByPk(vo.getDataCollectionGroupId(), true, true);
		return newVO;
	}

	@SuppressWarnings("unchecked")
	public List<DataCollectionGroup3VO> findFiltered(final Integer sessionId, final boolean withDataCollection, final boolean withScreenings)
			throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<DataCollectionGroup3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<DataCollectionGroup3VO> foundEntities = dao
						.findFiltered(sessionId, withDataCollection, withScreenings, null, null);
				return foundEntities;
			}

		});
	}

	/**
	 * finds groups for a given workflow
	 * 
	 * @param workflowId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<DataCollectionGroup3VO> findByWorkflow(final Integer workflowId) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<DataCollectionGroup3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<DataCollectionGroup3VO> foundEntities = dao.findFiltered(null,  true, true, workflowId, null);
				return foundEntities;
			}

		});
	}

	/**
	 * find groups for a given blSampleId
	 * 
	 * @param sampleId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<DataCollectionGroup3VO> findBySampleId(final Integer sampleId, final boolean withDataCollection, final boolean withScreenings)
			throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<DataCollectionGroup3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<DataCollectionGroup3VO> foundEntities = dao.findFiltered(null, withDataCollection, withScreenings, null, sampleId);
				return foundEntities;
			}

		});
	}

}