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
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.daos.autoproc.AutoProcProgram3DAO;
import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.server.mx.vos.autoproc.AutoProcProgram3VO;
import ispyb.server.mx.vos.collections.DataCollection3VO;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

/**
 * <p>
 *  This session bean handles ISPyB AutoProcProgram3.
 * </p>
 */
@Stateless
public class AutoProcProgram3ServiceBean implements AutoProcProgram3Service,
		AutoProcProgram3ServiceLocal {

	private final static Logger LOG = Logger
			.getLogger(AutoProcProgram3ServiceBean.class);

	@EJB
	private AutoProcProgram3DAO dao;

	@Resource
	private SessionContext context;

	public AutoProcProgram3ServiceBean() {
	};

	/**
	 * Create new AutoProcProgram3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public AutoProcProgram3VO create(final AutoProcProgram3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (AutoProcProgram3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				dao.create(vo);
				return vo;
			}

		});
	}

	/**
	 * Update the AutoProcProgram3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public AutoProcProgram3VO update(final AutoProcProgram3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (AutoProcProgram3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				return dao.update(vo);
			}

		});
	}

	/**
	 * Remove the AutoProcProgram3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				AutoProcProgram3VO vo = findByPk(pk, false);
				// TODO Edit this business code				
				delete(vo);
				return vo;
			}

		});

	}

	/**
	 * Remove the AutoProcProgram3
	 * @param vo the entity to remove.
	 */
	public void delete(final AutoProcProgram3VO vo) throws Exception {
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
	 * @return the AutoProcProgram3 value object
	 */
	public AutoProcProgram3VO findByPk(final Integer pk, final boolean withAttachment
			) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (AutoProcProgram3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				AutoProcProgram3VO found = dao.findByPk(pk, withAttachment);
				return found;
			}

		});
	}

	// TODO remove following method if not adequate
	/**
	 * Find all AutoProcProgram3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<AutoProcProgram3VO> findAll(final boolean withAttachment)
			throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<AutoProcProgram3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<AutoProcProgram3VO> foundEntities = dao.findAll(withAttachment);
				return foundEntities;
			}

		});
	}

	/**
	 * Check if user has access rights to create, change and remove AutoProcProgram3 entities. If not set rollback only and throw AccessDeniedException
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
	 * Find all dataCollection linked to this autoProcProgramId
	 * @param autoProcProgramId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List<DataCollection3VO> findCollects(final Integer autoProcProgramId) throws Exception{
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		List foundEntities = (List) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				List foundEntities = dao.findCollects(autoProcProgramId);
				return foundEntities;
			}
		});
		List<DataCollection3VO> listVOs = null;
		if (foundEntities != null ){
			int nb = foundEntities.size();
			if (nb > 0){
				listVOs = new ArrayList<DataCollection3VO>();
			}
			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			DataCollection3Service dataCollectionService = (DataCollection3Service) ejb3ServiceLocator.getLocalService(DataCollection3Service.class);
			for (int i = 0; i < nb; i++) {
				Integer dataCollectionId = (Integer) foundEntities.get(i);
				if (dataCollectionId != null){
					DataCollection3VO vo = dataCollectionService.findByPk(dataCollectionId, false, false, false);
					listVOs.add(vo);
				}
			}
		}
		return listVOs;
	}

}
