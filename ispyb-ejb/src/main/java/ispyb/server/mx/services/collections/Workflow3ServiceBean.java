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
import ispyb.server.mx.daos.collections.Workflow3DAO;
import ispyb.server.mx.vos.collections.InputParameterWorkflow;
import ispyb.server.mx.vos.collections.Workflow3VO;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

/**
 * <p>
 *  This session bean handles ISPyB Workflow3.
 * </p>
 */
@Stateless
public class Workflow3ServiceBean implements Workflow3Service,
		Workflow3ServiceLocal {

	private final static Logger LOG = Logger
			.getLogger(Workflow3ServiceBean.class);

	@EJB
	private Workflow3DAO dao;

	@Resource
	private SessionContext context;

	public Workflow3ServiceBean() {
	};

	/**
	 * Create new Workflow3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public Workflow3VO create(final Workflow3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Workflow3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				dao.create(vo);
				return vo;
			}

		});
	}

	/**
	 * Update the Workflow3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public Workflow3VO update(final Workflow3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Workflow3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				return dao.update(vo);
			}

		});
	}

	/**
	 * Remove the Workflow3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				Workflow3VO vo = findByPk(pk);
				// TODO Edit this business code				
				delete(vo);
				return vo;
			}

		});

	}

	/**
	 * Remove the Workflow3
	 * @param vo the entity to remove.
	 */
	public void delete(final Workflow3VO vo) throws Exception {
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
	 * @return the Workflow3 value object
	 */
	public Workflow3VO findByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Workflow3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				Workflow3VO found = dao.findByPk(pk);
				return found;
			}

		});
	}

	
	/**
	 * Find all Workflow3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<Workflow3VO> findAll()
			throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Workflow3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<Workflow3VO> foundEntities = dao.findAll();
				return foundEntities;
			}

		});
	}

	/**
	 * Check if user has access rights to create, change and remove Workflow3 entities. If not set rollback only and throw AccessDeniedException
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
	
	public Integer countWF(final String year, final String beamline, final String workflowType, final String status) throws Exception{
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Integer) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				Integer foundEntities = dao.countWF(year, beamline, workflowType, status);
				return foundEntities;
			}

		});
	}
	
	public List getWorkflowResult(final String year, final String beamline, final String workflowType) throws Exception{
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List foundEntities = dao.getWorkflowResult(year, beamline, workflowType);
				return foundEntities;
			}

		});
	}

	@SuppressWarnings("unchecked")
	public List<Workflow3VO> findAllByStatus(final String status) throws Exception{
		
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Workflow3VO>) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				List foundEntities = dao.findAll(status);
				return foundEntities;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<InputParameterWorkflow> findInputParametersByWorkflowId(final int workflowId) throws Exception{
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<InputParameterWorkflow>) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				List foundEntities = dao.findInputParametersByWorkflowId(workflowId);
				return foundEntities;
			}
		});
	}
	
}
