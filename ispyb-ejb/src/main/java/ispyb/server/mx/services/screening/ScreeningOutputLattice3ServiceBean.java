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
package ispyb.server.mx.services.screening;

import ispyb.server.common.util.ejb.EJBAccessCallback;
import ispyb.server.common.util.ejb.EJBAccessTemplate;
import ispyb.server.mx.daos.screening.ScreeningOutputLattice3DAO;
import ispyb.server.mx.vos.screening.ScreeningOutputLattice3VO;
import ispyb.server.mx.vos.screening.ScreeningOutputLatticeWS3VO;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

/**
 * <p>
 * This session bean handles ISPyB ScreeningOutputLattice3.
 * </p>
 */
@Stateless
public class ScreeningOutputLattice3ServiceBean implements ScreeningOutputLattice3Service,
		ScreeningOutputLattice3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(ScreeningOutputLattice3ServiceBean.class);

	@EJB
	private ScreeningOutputLattice3DAO dao;

	@Resource
	private SessionContext context;

	public ScreeningOutputLattice3ServiceBean() {
	};

	/**
	 * Create new ScreeningOutputLattice3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public ScreeningOutputLattice3VO create(final ScreeningOutputLattice3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (ScreeningOutputLattice3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				dao.create(vo);
				return vo;
			}

		});
	}

	/**
	 * Update the ScreeningOutputLattice3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public ScreeningOutputLattice3VO update(final ScreeningOutputLattice3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (ScreeningOutputLattice3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				return dao.update(vo);
			}

		});
	}

	/**
	 * Remove the ScreeningOutputLattice3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				ScreeningOutputLattice3VO vo = findByPk(pk);
				// TODO Edit this business code
				delete(vo);
				return vo;
			}

		});

	}

	/**
	 * Remove the ScreeningOutputLattice3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final ScreeningOutputLattice3VO vo) throws Exception {
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
	 * @return the ScreeningOutputLattice3 value object
	 */
	public ScreeningOutputLattice3VO findByPk(final Integer pk)
			throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (ScreeningOutputLattice3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				ScreeningOutputLattice3VO found = dao.findByPk(pk);
				return found;
			}

		});
	}

	// TODO remove following method if not adequate
	/**
	 * Find all ScreeningOutputLattice3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<ScreeningOutputLattice3VO> findAll() throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<ScreeningOutputLattice3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<ScreeningOutputLattice3VO> foundEntities = dao.findAll();
				return foundEntities;
			}

		});
	}

	/**
	 * Check if user has access rights to create, change and remove ScreeningOutputLattice3 entities. If not set
	 * rollback only and throw AccessDeniedException
	 * 
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
				// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
				// to the one checking the needed access rights
				// autService.checkUserRightToChangeAdminData();
				return null;
			}

		});
	}
	
	@SuppressWarnings("unchecked")
	public List<ScreeningOutputLattice3VO> findFiltered(final Integer dataCollectionId) throws Exception{
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<ScreeningOutputLattice3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<ScreeningOutputLattice3VO> foundEntities = dao.findFiltered(dataCollectionId);
				return foundEntities;
			}
		});
	}
	
	
	public ScreeningOutputLatticeWS3VO findByDataCollectionId(final Integer dataCollectionId) throws Exception{
		List<ScreeningOutputLattice3VO> list = findFiltered(dataCollectionId);
		if (list == null || list.size() == 0)
			return null;
		
		return getWSScreeningOutputLatticeVO(list.get(0));
	}

	
	private ScreeningOutputLatticeWS3VO getWSScreeningOutputLatticeVO(ScreeningOutputLattice3VO vo) throws CloneNotSupportedException {
		ScreeningOutputLattice3VO otherVO = getLightScreeningOutputLattice3VO(vo);
		Integer screeningOutputId = null;
		screeningOutputId = otherVO.getScreeningOutputVOId();
		otherVO.setScreeningOutputVO(null);
		ScreeningOutputLatticeWS3VO wsScreeningOutputLattice = new ScreeningOutputLatticeWS3VO(otherVO);
		wsScreeningOutputLattice.setScreeningOutputId(screeningOutputId);
		return wsScreeningOutputLattice;
	}
	
	private ScreeningOutputLattice3VO getLightScreeningOutputLattice3VO(ScreeningOutputLattice3VO vo) throws CloneNotSupportedException {
		ScreeningOutputLattice3VO otherVO = (ScreeningOutputLattice3VO) vo.clone();
		// otherVO.set(null);
		return otherVO;
	}
}
