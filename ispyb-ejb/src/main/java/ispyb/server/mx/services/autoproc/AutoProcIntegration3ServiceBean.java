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
<<<<<<< HEAD
import ispyb.server.mx.daos.autoproc.AutoProcIntegration3DAO;
=======

>>>>>>> aa35504f989cd6f3f35a4019958215da79e405e3
import ispyb.server.mx.vos.autoproc.AutoProcIntegration3VO;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

/**
 * <p>
 *  This session bean handles ISPyB AutoProcIntegration3.
 * </p>
 */
@Stateless
public class AutoProcIntegration3ServiceBean implements AutoProcIntegration3Service,
		AutoProcIntegration3ServiceLocal {

	private final static Logger LOG = Logger
			.getLogger(AutoProcIntegration3ServiceBean.class);

	@EJB
	private AutoProcIntegration3DAO dao;

	@Resource
	private SessionContext context;

	public AutoProcIntegration3ServiceBean() {
	};

	/**
	 * Create new AutoProcIntegration3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public AutoProcIntegration3VO create(final AutoProcIntegration3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (AutoProcIntegration3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				dao.create(vo);
				return vo;
			}

		});
	}

	/**
	 * Update the AutoProcIntegration3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public AutoProcIntegration3VO update(final AutoProcIntegration3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (AutoProcIntegration3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				return dao.update(vo);
			}

		});
	}

	/**
	 * Remove the AutoProcIntegration3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				AutoProcIntegration3VO vo = findByPk(pk);
				// TODO Edit this business code				
				delete(vo);
				return vo;
			}

		});

	}

	/**
	 * Remove the AutoProcIntegration3
	 * @param vo the entity to remove.
	 */
	public void delete(final AutoProcIntegration3VO vo) throws Exception {
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
	 * @return the AutoProcIntegration3 value object
	 */
	public AutoProcIntegration3VO findByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (AutoProcIntegration3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				AutoProcIntegration3VO found = dao.findByPk(pk);
				return found;
			}

		});
	}

	// TODO remove following method if not adequate
	/**
	 * Find all AutoProcIntegration3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<AutoProcIntegration3VO> findAll()
			throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<AutoProcIntegration3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<AutoProcIntegration3VO> foundEntities = dao.findAll();
				return foundEntities;
			}

		});
	}

	/**
	 * Check if user has access rights to create, change and remove AutoProcIntegration3 entities. If not set rollback only and throw AccessDeniedException
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
	 * 
	 * @param autoProcId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<AutoProcIntegration3VO> findByAutoProcId(final Integer autoProcId) throws Exception{
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<AutoProcIntegration3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<AutoProcIntegration3VO> foundEntities = dao.findByAutoProcId(autoProcId);
				return foundEntities;
			}

		});
	}
	
	/**
	 * get the autoProcStatus for a given dataCollectionId and a given processingProgram (fastProc or parallelProc or edna-fastproc)
	 * true if we can find at least one autoProc with XSCALE file
	 * @param dataCollectionId
	 * @param processingProgram
	 * @return
	 * @throws Exception
	 */
	public Boolean getAutoProcStatus(final Integer dataCollectionId, final String processingProgram) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Boolean) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				Boolean foundEntities = dao.getAutoProcStatus(dataCollectionId, processingProgram);
				return foundEntities;
			}

		});
	}

	/**
	 * get the autoProcStatus for a XIA2_DIALS
	 * @param dataCollectionId
	 * @param processingProgram
	 * @return
	 * @throws Exception
	 */
	public Boolean getAutoProcXia2DialsStatus(final Integer dataCollectionId, final String processingProgram) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Boolean) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				Boolean foundEntities = dao.getAutoProcXia2DialsStatus(dataCollectionId, processingProgram);
				return foundEntities;
			}

		});
	}

}