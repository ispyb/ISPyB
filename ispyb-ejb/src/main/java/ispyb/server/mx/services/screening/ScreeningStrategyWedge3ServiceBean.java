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
import ispyb.server.mx.daos.screening.ScreeningStrategyWedge3DAO;
import ispyb.server.mx.vos.screening.ScreeningStrategyWedge3VO;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

/**
 * <p>
 * This session bean handles ISPyB ScreeningStrategyWedge3.
 * </p>
 */
@Stateless
public class ScreeningStrategyWedge3ServiceBean implements ScreeningStrategyWedge3Service,
		ScreeningStrategyWedge3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(ScreeningStrategyWedge3ServiceBean.class);

	@EJB
	private ScreeningStrategyWedge3DAO dao;

	@Resource
	private SessionContext context;

	public ScreeningStrategyWedge3ServiceBean() {
	};

	/**
	 * Create new ScreeningStrategyWedge3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public ScreeningStrategyWedge3VO create(final ScreeningStrategyWedge3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (ScreeningStrategyWedge3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				dao.create(vo);
				return vo;
			}

		});
	}

	/**
	 * Update the ScreeningStrategyWedge3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public ScreeningStrategyWedge3VO update(final ScreeningStrategyWedge3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (ScreeningStrategyWedge3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				return dao.update(vo);
			}

		});
	}

	/**
	 * Remove the ScreeningStrategyWedge3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				ScreeningStrategyWedge3VO vo = findByPk(pk, false);
				// TODO Edit this business code
				delete(vo);
				return vo;
			}

		});

	}

	/**
	 * Remove the ScreeningStrategyWedge3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final ScreeningStrategyWedge3VO vo) throws Exception {
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
	 * @return the ScreeningStrategyWedge3 value object
	 */
	public ScreeningStrategyWedge3VO findByPk(final Integer pk, final boolean withScreeningStrategySubWedge)
			throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (ScreeningStrategyWedge3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				ScreeningStrategyWedge3VO found = dao.findByPk(pk, withScreeningStrategySubWedge);
				return found;
			}

		});
	}

	// TODO remove following method if not adequate
	/**
	 * Find all ScreeningStrategyWedge3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<ScreeningStrategyWedge3VO> findAll(final boolean withScreeningStrategySubWedge) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<ScreeningStrategyWedge3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<ScreeningStrategyWedge3VO> foundEntities = dao.findAll(withScreeningStrategySubWedge);
				return foundEntities;
			}

		});
	}

	/**
	 * Check if user has access rights to create, change and remove ScreeningStrategyWedge3 entities. If not set
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

	
	
	public ScreeningStrategyWedge3VO loadEager(ScreeningStrategyWedge3VO vo) throws Exception{
		ScreeningStrategyWedge3VO newVO = this.findByPk(vo.getScreeningStrategyWedgeId(),true);
		return newVO;
	}


}