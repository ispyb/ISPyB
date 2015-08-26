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
import ispyb.server.mx.daos.collections.UntrustedRegion3DAO;
import ispyb.server.mx.vos.collections.UntrustedRegion3VO;
import ispyb.server.mx.vos.collections.UntrustedRegionWS3VO;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

/**
 * <p>
 *  This session bean handles ISPyB UntrustedRegion3.
 * </p>
 */
@Stateless
public class UntrustedRegion3ServiceBean implements UntrustedRegion3Service,
		UntrustedRegion3ServiceLocal {

	private final static Logger LOG = Logger
			.getLogger(UntrustedRegion3ServiceBean.class);

	@EJB
	private UntrustedRegion3DAO dao;

	@Resource
	private SessionContext context;

	public UntrustedRegion3ServiceBean() {
	};

	/**
	 * Create new UntrustedRegion3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public UntrustedRegion3VO create(final UntrustedRegion3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (UntrustedRegion3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				dao.create(vo);
				return vo;
			}

		});
	}

	/**
	 * Update the UntrustedRegion3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public UntrustedRegion3VO update(final UntrustedRegion3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (UntrustedRegion3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				return dao.update(vo);
			}

		});
	}

	/**
	 * Remove the UntrustedRegion3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				UntrustedRegion3VO vo = findByPk(pk);
				// TODO Edit this business code				
				delete(vo);
				return vo;
			}

		});

	}

	/**
	 * Remove the UntrustedRegion3
	 * @param vo the entity to remove.
	 */
	public void delete(final UntrustedRegion3VO vo) throws Exception {
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
	 * @return the UntrustedRegion3 value object
	 */
	public UntrustedRegion3VO findByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (UntrustedRegion3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				UntrustedRegion3VO found = dao.findByPk(pk);
				return found;
			}

		});
	}

	/**
	 * loads the vo with all the linked object in eager fetch mode
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public UntrustedRegion3VO loadEager(UntrustedRegion3VO vo) throws Exception {
		UntrustedRegion3VO newVO = this.findByPk(vo.getUntrustedRegionId());
		return newVO;
	}

	// TODO remove following method if not adequate
	/**
	 * Find all UntrustedRegion3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<UntrustedRegion3VO> findAll()
			throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<UntrustedRegion3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<UntrustedRegion3VO> foundEntities = dao.findAll();
				return foundEntities;
			}

		});
	}

	/**
	 * Check if user has access rights to create, change and remove UntrustedRegion3 entities. If not set rollback only and throw AccessDeniedException
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
	 * Finds a UntrustedRegion3 entity by its primary key and set linked value objects if necessary.  -- webservices object
	 * @param pk the primary key
	 * @return the Scientist value object
	 */
	public UntrustedRegionWS3VO findForWSByPk(final Integer pk) throws Exception{
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (UntrustedRegionWS3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				UntrustedRegion3VO found = dao.findByPk(pk);
				UntrustedRegionWS3VO untrustedRegionLight = getWSUntrustedRegionVO(found);
				return untrustedRegionLight;
			}

		});
	}
	
	private UntrustedRegionWS3VO getWSUntrustedRegionVO(UntrustedRegion3VO vo) throws CloneNotSupportedException {
		if (vo == null)
			return null;
		UntrustedRegion3VO otherVO = getLightUntrustedRegion3VO(vo);
		Integer detectorId = null;
		detectorId = otherVO.getDetectorVOId();
		otherVO.setDetectorVO(null);
		UntrustedRegionWS3VO wsUntrustedRegion = new UntrustedRegionWS3VO(otherVO);
		wsUntrustedRegion.setDetectorId(detectorId);
		return wsUntrustedRegion;
	}
	
	private UntrustedRegion3VO getLightUntrustedRegion3VO(UntrustedRegion3VO vo) throws CloneNotSupportedException {
		UntrustedRegion3VO otherVO = (UntrustedRegion3VO) vo.clone();
		return otherVO;
	}
	
}