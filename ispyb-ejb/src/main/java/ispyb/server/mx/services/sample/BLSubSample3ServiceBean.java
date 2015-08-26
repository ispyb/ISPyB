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

import ispyb.server.common.util.ejb.EJBAccessCallback;
import ispyb.server.common.util.ejb.EJBAccessTemplate;
import ispyb.server.mx.daos.sample.BLSubSample3DAO;
import ispyb.server.mx.vos.sample.BLSubSample3VO;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

/**
 * <p>
 *  This session bean handles ISPyB BLSubSample3.
 * </p>
 */
@Stateless
public class BLSubSample3ServiceBean implements BLSubSample3Service,
		BLSubSample3ServiceLocal {

	private final static Logger LOG = Logger
			.getLogger(BLSubSample3ServiceBean.class);

	@EJB
	private BLSubSample3DAO dao;

	@Resource
	private SessionContext context;

	public BLSubSample3ServiceBean() {
	};

	/**
	 * Create new BLSubSample3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public BLSubSample3VO create(final BLSubSample3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (BLSubSample3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				dao.create(vo);
				return vo;
			}

		});
	}

	/**
	 * Update the BLSubSample3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public BLSubSample3VO update(final BLSubSample3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (BLSubSample3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				return dao.update(vo);
			}

		});
	}

	/**
	 * Remove the BLSubSample3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				BLSubSample3VO vo = findByPk(pk);
				// TODO Edit this business code				
				delete(vo);
				return vo;
			}

		});

	}

	/**
	 * Remove the BLSubSample3
	 * @param vo the entity to remove.
	 */
	public void delete(final BLSubSample3VO vo) throws Exception {
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
	 * @return the BLSubSample3 value object
	 */
	public BLSubSample3VO findByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (BLSubSample3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				BLSubSample3VO found = dao.findByPk(pk);
				return found;
			}

		});
	}

	
	/**
	 * Find all BLSubSample3s and set linked value objects if necessary
	 */
	@SuppressWarnings("unchecked")
	public List<BLSubSample3VO> findAll()throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<BLSubSample3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<BLSubSample3VO> foundEntities = dao.findAll();
				
				return foundEntities;
			}

		});
	}

	/**
	 * Check if user has access rights to create, change and remove BLSubSample3 entities. If not set rollback only and throw AccessDeniedException
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

	
}
