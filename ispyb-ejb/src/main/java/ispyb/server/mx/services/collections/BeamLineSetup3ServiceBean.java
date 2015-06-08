/*******************************************************************************
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
 ******************************************************************************************************************************/

package ispyb.server.mx.services.collections;

import ispyb.server.common.util.ejb.EJBAccessCallback;
import ispyb.server.common.util.ejb.EJBAccessTemplate;
import ispyb.server.mx.daos.collections.BeamLineSetup3DAO;
import ispyb.server.mx.vos.collections.BeamLineSetup3VO;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.jws.WebMethod;

import java.nio.file.AccessDeniedException;
import org.apache.log4j.Logger;

/**
 * <p>
 * This session bean handles BeamLineSetup table.
 * </p>
 * 
 */
@Stateless
public class BeamLineSetup3ServiceBean implements BeamLineSetup3Service, BeamLineSetup3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(BeamLineSetup3ServiceBean.class);

	@EJB
	private BeamLineSetup3DAO dao;

	@Resource
	private SessionContext context;

	public BeamLineSetup3ServiceBean() {
	};

	/**
	 * Create new BeamLineSetup.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public BeamLineSetup3VO create(final BeamLineSetup3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (BeamLineSetup3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				dao.create(vo);
				return vo;
			}

		});
	}

	/**
	 * Update the BeamLineSetup data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public BeamLineSetup3VO update(final BeamLineSetup3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (BeamLineSetup3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {

				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				return dao.update(vo);
			}

		});
	}

	/**
	 * Remove the BeamLineSetup from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				BeamLineSetup3VO vo = findByPk(pk);
				// TODO Edit this business code
				delete(vo);
				return vo;
			}

		});

	}

	/**
	 * Remove the BeamLineSetup
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final BeamLineSetup3VO vo) throws Exception {
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
	 * @return the Scientist value object
	 */
	@WebMethod
	public BeamLineSetup3VO findByPk(final Integer pk) throws Exception {

		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		EJBAccessCallback callBack = new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				BeamLineSetup3VO found = dao.findByPk(pk);
				return found;
			};
		};
		BeamLineSetup3VO vo = (BeamLineSetup3VO) template.execute(callBack);
		return vo;
	}

	/**
	 * Find all Scientists and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	@WebMethod
	public List<BeamLineSetup3VO> findAll() throws Exception {

		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		EJBAccessCallback callBack = new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<BeamLineSetup3VO> foundEntities = dao.findAll();
				return foundEntities;
			};
		};
		List<BeamLineSetup3VO> ret = (List<BeamLineSetup3VO>) template.execute(callBack);
		return ret;
	}

	/**
	 * Check if user has access rights to create, change and remove Scientist entities. If not set rollback only and
	 * throw AccessDeniedException
	 * 
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {

				// TODO add an authorization service bean for ISPyB
				// AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance()
				// .getService(AuthorizationServiceLocalHome.class); // TODO change method to the one checking the
				// // needed access rights
				// autService.checkUserRightToChangeAdminData();
				return null;
			}

		});
	}

	
	
	/**
	 * Find a beamLineSetup  for a given screeningInput
	 * @param screeningInputId
	 * @param detachLight
	 * @return
	 * @throws Exception
	 */
	public BeamLineSetup3VO findByScreeningInputId(final Integer screeningInputId, final boolean detachLight) throws Exception{
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		EJBAccessCallback callBack = new EJBAccessCallback() {

			public BeamLineSetup3VO doInEJBAccess(Object parent) throws Exception {
				BeamLineSetup3VO foundEntities = dao.findByScreeningInputId(screeningInputId);
				BeamLineSetup3VO vos;
				if (detachLight)
					vos = getLightBeamLineSetupVO(foundEntities);
				else
					vos = getBeamLineSetupVO(foundEntities);
				return vos;
			};
		};
		BeamLineSetup3VO ret = (BeamLineSetup3VO) template.execute(callBack);

		return ret;
	}
	
	

	
	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private BeamLineSetup3VO getLightBeamLineSetupVO(BeamLineSetup3VO vo) throws CloneNotSupportedException {
		BeamLineSetup3VO otherVO = (BeamLineSetup3VO) vo.clone();
		//otherVO.setSessionVOs(null);
		return otherVO;
	}
	
	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private BeamLineSetup3VO getBeamLineSetupVO(BeamLineSetup3VO vo)  {
		//vo.get;
		return vo;
	}

}
