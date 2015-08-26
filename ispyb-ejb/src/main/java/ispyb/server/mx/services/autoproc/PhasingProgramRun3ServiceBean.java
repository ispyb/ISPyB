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
import ispyb.server.mx.daos.autoproc.PhasingProgramRun3DAO;
import ispyb.server.mx.vos.autoproc.PhasingProgramRun3VO;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

/**
 * <p>
 *  This session bean handles ISPyB PhasingProgram3.
 * </p>
 */
@Stateless
public class PhasingProgramRun3ServiceBean implements PhasingProgramRun3Service, PhasingProgramRun3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(PhasingProgramRun3ServiceBean.class);

	@EJB
	private PhasingProgramRun3DAO dao;

	@Resource
	private SessionContext context;

	public PhasingProgramRun3ServiceBean() {
	};

	/**
	 * Create new PhasingProgram3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public PhasingProgramRun3VO create(final PhasingProgramRun3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (PhasingProgramRun3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				dao.create(vo);
				return vo;
			}

		});
	}

	/**
	 * Update the PhasingProgram3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public PhasingProgramRun3VO update(final PhasingProgramRun3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (PhasingProgramRun3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				return dao.update(vo);
			}

		});
	}

	/**
	 * Remove the PhasingProgram3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				PhasingProgramRun3VO vo = findByPk(pk, false);
				// TODO Edit this business code				
				delete(vo);
				return vo;
			}

		});

	}

	/**
	 * Remove the PhasingProgram3
	 * @param vo the entity to remove.
	 */
	public void delete(final PhasingProgramRun3VO vo) throws Exception {
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
	 * @param withAttachment
	 * @return the PhasingProgram3 value object
	 */
	public PhasingProgramRun3VO findByPk(final Integer pk, final boolean withAttachment) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (PhasingProgramRun3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				PhasingProgramRun3VO found = dao.findByPk(pk, withAttachment);
				return found;
			}

		});
	}

	/**
	 * Find all PhasingProgram3s and set linked value objects if necessary
	 * @param withAttachment
	 */
	@SuppressWarnings("unchecked")
	public List<PhasingProgramRun3VO> findAll(final boolean withAttachment)throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<PhasingProgramRun3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<PhasingProgramRun3VO> foundEntities = dao.findAll(withAttachment);
				return foundEntities;
			}

		});
	}

	/**
	 * Check if user has access rights to create, change and remove PhasingProgram3 entities. If not set rollback only and throw AccessDeniedException
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
