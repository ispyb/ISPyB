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
import ispyb.server.mx.daos.collections.Detector3DAO;
import ispyb.server.mx.vos.collections.Detector3VO;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import java.nio.file.AccessDeniedException;
import org.apache.log4j.Logger;

/**
 * <p>
 *  This session bean handles ISPyB Detector3.
 * </p>
 */
@Stateless
public class Detector3ServiceBean implements Detector3Service,
		Detector3ServiceLocal {

	private final static Logger LOG = Logger
			.getLogger(Detector3ServiceBean.class);

	@EJB
	private Detector3DAO dao;

	@Resource
	private SessionContext context;

	public Detector3ServiceBean() {
	};

	/**
	 * Create new Detector3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public Detector3VO create(final Detector3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Detector3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				dao.create(vo);
				return vo;
			}

		});
	}

	/**
	 * Update the Detector3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public Detector3VO update(final Detector3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Detector3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				return dao.update(vo);
			}

		});
	}

	/**
	 * Remove the Detector3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				Detector3VO vo = findByPk(pk);
				// TODO Edit this business code				
				delete(vo);
				return vo;
			}

		});

	}

	/**
	 * Remove the Detector3
	 * @param vo the entity to remove.
	 */
	public void delete(final Detector3VO vo) throws Exception {
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
	 * @return the Detector3 value object
	 */
	public Detector3VO findByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Detector3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				Detector3VO found = dao.findByPk(pk);
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
	public Detector3VO loadEager(Detector3VO vo) throws Exception {
		Detector3VO newVO = this.findByPk(vo.getDetectorId());
		return newVO;
	}

	// TODO remove following method if not adequate
	/**
	 * Find all Detector3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<Detector3VO> findAll()
			throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Detector3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<Detector3VO> foundEntities = dao.findAll();
				return foundEntities;
			}

		});
	}

	/**
	 * Check if user has access rights to create, change and remove Detector3 entities. If not set rollback only and throw AccessDeniedException
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
	 * Find a dataCollectionGroup by its primary key -- webservices object
	 * @param pk
	 * @param withLink1
	 * @param withLink2
	 * @return
	 * @throws Exception
	 */
	public Detector3VO findForWSByPk(final Integer pk) throws Exception{
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Detector3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				Detector3VO found = dao.findByPk(pk);
				return found;
			}

		});
	}
	
	/**
	 * returns the detector with the given characteristics -- null otherwise
	 * @param detectorType
	 * @param detectorManufacturer
	 * @param detectorModel
	 * @param detectorPixelSizeHorizontal
	 * @param detectorPixelSizeVertical
	 * @return
	 * @throws Exception
	 */
	public Detector3VO findByCharacteristics(final String detectorType, final String detectorManufacturer, 
			final String detectorModel, final Double detectorPixelSizeHorizontal, 
			final Double detectorPixelSizeVertical) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Detector3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				List<Detector3VO> vos = dao.findFiltered(detectorType, detectorManufacturer, 
						detectorModel, detectorPixelSizeHorizontal, 
						detectorPixelSizeVertical, null);
				if (vos == null || vos.size() == 0)
					return null;
				return vos.get(0);
			}

		});
	}

	/**
	 * returns the detector with the given characteristics -- null otherwise
	 * @param detectorType
	 * @param detectorManufacturer
	 * @param detectorModel
	 * @param detectorMode
	 * @return
	 * @throws Exception
	 */
	public Detector3VO findDetector(final String detectorType, final String detectorManufacturer, 
			final String detectorModel, final String detectorMode) throws Exception{
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Detector3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				List<Detector3VO> vos = dao.findFiltered(detectorType, detectorManufacturer, 
						detectorModel, null, 
						null, detectorMode);
				if (vos == null || vos.size() == 0)
					return null;
				return vos.get(0);
			}

		});
	}
	
}