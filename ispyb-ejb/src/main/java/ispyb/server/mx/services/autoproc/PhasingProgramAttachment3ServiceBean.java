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
import ispyb.server.mx.daos.autoproc.PhasingProgramAttachment3DAO;
import ispyb.server.mx.vos.autoproc.PhasingProgramAttachment3VO;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
<<<<<<< HEAD
=======
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import ispyb.server.common.exceptions.AccessDeniedException;

import ispyb.server.mx.vos.autoproc.PhasingProgramAttachment3VO;
>>>>>>> aa35504f989cd6f3f35a4019958215da79e405e3

/**
 * <p>
 *  This session bean handles ISPyB PhasingProgramAttachment3.
 * </p>
 */
@Stateless
public class PhasingProgramAttachment3ServiceBean implements PhasingProgramAttachment3Service,PhasingProgramAttachment3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(PhasingProgramAttachment3ServiceBean.class);

	@EJB
	private PhasingProgramAttachment3DAO dao;

	@Resource
	private SessionContext context;

	public PhasingProgramAttachment3ServiceBean() {
	};

	/**
	 * Create new PhasingProgramAttachment3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public PhasingProgramAttachment3VO create(final PhasingProgramAttachment3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (PhasingProgramAttachment3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				dao.create(vo);
				return vo;
			}

		});
	}

	/**
	 * Update the PhasingProgramAttachment3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public PhasingProgramAttachment3VO update(final PhasingProgramAttachment3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (PhasingProgramAttachment3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				return dao.update(vo);
			}

		});
	}

	/**
	 * Remove the PhasingProgramAttachment3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				PhasingProgramAttachment3VO vo = findByPk(pk);
				// TODO Edit this business code				
				delete(vo);
				return vo;
			}

		});

	}

	/**
	 * Remove the PhasingProgramAttachment3
	 * @param vo the entity to remove.
	 */
	public void delete(final PhasingProgramAttachment3VO vo) throws Exception {
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
	 * @return the PhasingProgramAttachment3 value object
	 */
	public PhasingProgramAttachment3VO findByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (PhasingProgramAttachment3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				PhasingProgramAttachment3VO found = dao.findByPk(pk);
				return found;
			}

		});
	}

	
	/**
	 * Find all PhasingProgramAttachment3s and set linked value objects if necessary
	 */
	@SuppressWarnings("unchecked")
	public List<PhasingProgramAttachment3VO> findAll()throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<PhasingProgramAttachment3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<PhasingProgramAttachment3VO> foundEntities = dao.findAll();
				return foundEntities;
			}

		});
	}

	/**
	 * Check if user has access rights to create, change and remove PhasingProgramAttachment3 entities. If not set rollback only and throw AccessDeniedException
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

	@SuppressWarnings("unchecked")
	public List<PhasingProgramAttachment3VO> findFiltered(final Integer phasingProgramRunId)throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return ( List<PhasingProgramAttachment3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				 List<PhasingProgramAttachment3VO> foundEntities = dao.findFiltered(phasingProgramRunId);
				return foundEntities;
			}

		});
	}
	
}