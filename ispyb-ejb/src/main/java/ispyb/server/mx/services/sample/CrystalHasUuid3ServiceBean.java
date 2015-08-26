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
import ispyb.server.mx.daos.sample.CrystalHasUuid3DAO;
import ispyb.server.mx.vos.sample.CrystalHasUuid3VO;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

/**
 * <p>
 * This session bean handles ISPyB CrystalHasUuid3.
 * </p>
 */
@Stateless
public class CrystalHasUuid3ServiceBean implements CrystalHasUuid3Service, CrystalHasUuid3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(CrystalHasUuid3ServiceBean.class);

	@EJB
	private CrystalHasUuid3DAO dao;

	@Resource
	private SessionContext context;

	public CrystalHasUuid3ServiceBean() {
	};

	/**
	 * Create new CrystalHasUuid3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public CrystalHasUuid3VO create(final CrystalHasUuid3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (CrystalHasUuid3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				dao.create(vo);
				return vo;
			}

		});
	}

	/**
	 * Update the CrystalHasUuid3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public CrystalHasUuid3VO update(final CrystalHasUuid3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (CrystalHasUuid3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				return dao.update(vo);
			}

		});
	}

	/**
	 * Remove the CrystalHasUuid3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				CrystalHasUuid3VO vo = findByPk(pk, false, false);
				// TODO Edit this business code
				delete(vo);
				return vo;
			}

		});

	}

	/**
	 * Remove the CrystalHasUuid3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final CrystalHasUuid3VO vo) throws Exception {
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
	 * @return the CrystalHasUuid3 value object
	 */
	public CrystalHasUuid3VO findByPk(final Integer pk, final boolean withLink1, final boolean withLink2)
			throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (CrystalHasUuid3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				CrystalHasUuid3VO found = dao.findByPk(pk, withLink1, withLink2);
				return found;
			}

		});
	}

	// TODO remove following method if not adequate
	/**
	 * Find all CrystalHasUuid3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<CrystalHasUuid3VO> findAll(final boolean withLink1, final boolean withLink2) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<CrystalHasUuid3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<CrystalHasUuid3VO> foundEntities = dao.findAll(withLink1, withLink2);
				return foundEntities;
			}

		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrystalHasUuid3VO> findByCrystalIdAndUuid(final Integer crystalId, final String crystalUUID)
			throws Exception {
		// TODO Auto-generated method stub
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<CrystalHasUuid3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<CrystalHasUuid3VO> foundEntities = dao.findFiltered(crystalId, crystalUUID);
				return foundEntities;
			}

		});
	}

	/**
	 * Check if user has access rights to create, change and remove CrystalHasUuid3 entities. If not set rollback only
	 * and throw AccessDeniedException
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

	

}