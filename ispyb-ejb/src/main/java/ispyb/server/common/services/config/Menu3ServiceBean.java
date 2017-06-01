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
package ispyb.server.common.services.config;

import ispyb.server.common.daos.config.Menu3DAO;
import ispyb.server.common.util.ejb.EJBAccessCallback;
import ispyb.server.common.util.ejb.EJBAccessTemplate;
import ispyb.server.common.vos.config.Menu3VO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

/**
 * <p>
 *  This session bean handles ISPyB Menu3.
 * </p>
 */
@Stateless
public class Menu3ServiceBean implements Menu3Service,
		Menu3ServiceLocal {

	private final static Logger LOG = Logger
			.getLogger(Menu3ServiceBean.class);

	@EJB
	private Menu3DAO dao;

	@Resource
	private SessionContext context;

	public Menu3ServiceBean() {
	};

	/**
	 * Create new Menu3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public Menu3VO create(final Menu3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Menu3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				dao.create(vo);
				return vo;
			}

		});
	}

	/**
	 * Update the Menu3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public Menu3VO update(final Menu3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Menu3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				return dao.update(vo);
			}

		});
	}

	/**
	 * Remove the Menu3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				Menu3VO vo = findByPk(pk, false);
				// TODO Edit this business code				
				delete(vo);
				return vo;
			}

		});

	}

	/**
	 * Remove the Menu3
	 * @param vo the entity to remove.
	 */
	public void delete(final Menu3VO vo) throws Exception {
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
	 * @return the Menu3 value object
	 */
	public Menu3VO findByPk(final Integer pk, final boolean withMenuGroup) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Menu3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				Menu3VO found = dao.findByPk(pk, withMenuGroup);
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
	public Menu3VO loadEager(Menu3VO vo) throws Exception {
		Menu3VO newVO = this.findByPk(vo.getMenuId(), true);
		return newVO;
	}

	// TODO remove following method if not adequate
	/**
	 * Find all Menu3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<Menu3VO> findAll(final boolean withMenuGroup,
			 final boolean detachLight)
			throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Menu3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				Collection<Menu3VO> foundEntities = dao.findAll(withMenuGroup);
				List<Menu3VO> vos;
				if (detachLight)
					vos = getLightMenu3VOs(foundEntities);
				else
					vos = getMenu3VOs(foundEntities);
				return vos;
			}

		});
	}

	/**
	 * Check if user has access rights to create, change and remove Menu3 entities. If not set rollback only and throw AccessDeniedException
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
	 * Get all Menu3 entity VOs from a collection of Menu3 local entities.
	 * @param localEntities
	 * @return
	 */
	private List<Menu3VO> getMenu3VOs(Collection<Menu3VO> entities) {
		List<Menu3VO> results = new ArrayList<Menu3VO>(entities.size());
		for (Menu3VO vo : entities) {
			results.add(vo);
		}
		return results;
	}

	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private List<Menu3VO> getLightMenu3VOs(Collection<Menu3VO> entities)
			throws CloneNotSupportedException {
		List<Menu3VO> results = new ArrayList<Menu3VO>(entities.size());
		for (Menu3VO vo : entities) {
			Menu3VO otherVO = getLightMenu3VO(vo);
			results.add(otherVO);
		}
		return results;
	}

	/**
	 * Get a clone of an entity witout linked collections
	 * used for webservices
	 * 
	 * @param localEntity
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private Menu3VO getLightMenu3VO(Menu3VO vo)
			throws CloneNotSupportedException {
		Menu3VO otherVO = vo.clone();
		//otherVO.setXxxxxxx(null);
		//otherVO.setYyyyyyy(null);
		return otherVO;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Menu3VO> findFiltered(final Integer parentId, final Integer menuGroupId, final String proposalCode)throws Exception{
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Menu3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				Collection<Menu3VO> foundEntities = dao.findFiltered(parentId, menuGroupId, proposalCode);
				List<Menu3VO> vos = getMenu3VOs(foundEntities);
				return vos;
			}

		});
	}
}
