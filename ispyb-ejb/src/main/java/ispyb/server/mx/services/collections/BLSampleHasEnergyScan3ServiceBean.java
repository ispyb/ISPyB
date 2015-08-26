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
import ispyb.server.mx.daos.collections.BLSampleHasEnergyScan3DAO;
import ispyb.server.mx.vos.collections.BLSampleHasEnergyScan3VO;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

/**
 * <p>
 *  This session bean handles ISPyB BLSampleHasEnergyScan3.
 * </p>
 */
@Stateless
public class BLSampleHasEnergyScan3ServiceBean implements BLSampleHasEnergyScan3Service,
		BLSampleHasEnergyScan3ServiceLocal {

	private final static Logger LOG = Logger
			.getLogger(BLSampleHasEnergyScan3ServiceBean.class);

	@EJB
	private BLSampleHasEnergyScan3DAO dao;

	@Resource
	private SessionContext context;

	public BLSampleHasEnergyScan3ServiceBean() {
	};

	/**
	 * Create new BLSampleHasEnergyScan3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public BLSampleHasEnergyScan3VO create(final BLSampleHasEnergyScan3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (BLSampleHasEnergyScan3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				dao.create(vo);
				return vo;
			}

		});
	}

	/**
	 * Update the BLSampleHasEnergyScan3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public BLSampleHasEnergyScan3VO update(final BLSampleHasEnergyScan3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (BLSampleHasEnergyScan3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				return dao.update(vo);
			}

		});
	}

	/**
	 * Remove the BLSampleHasEnergyScan3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				BLSampleHasEnergyScan3VO vo = findByPk(pk, false, false);
				// TODO Edit this business code				
				delete(vo);
				return vo;
			}

		});

	}

	/**
	 * Remove the BLSampleHasEnergyScan3
	 * @param vo the entity to remove.
	 */
	public void delete(final BLSampleHasEnergyScan3VO vo) throws Exception {
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
	 * @return the BLSampleHasEnergyScan3 value object
	 */
	public BLSampleHasEnergyScan3VO findByPk(final Integer pk, final boolean withLink1,
			final boolean withLink2) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (BLSampleHasEnergyScan3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				BLSampleHasEnergyScan3VO found = dao.findByPk(pk, withLink1, withLink2);
				return found;
			}

		});
	}

	// TODO remove following method if not adequate
	/**
	 * Find all BLSampleHasEnergyScan3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<BLSampleHasEnergyScan3VO> findAll(final boolean withLink1, final boolean withLink2)
			throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<BLSampleHasEnergyScan3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<BLSampleHasEnergyScan3VO> foundEntities = dao.findAll(withLink1,
						withLink2);
				return foundEntities;
			}

		});
	}

	/**
	 * Check if user has access rights to create, change and remove BLSampleHasEnergyScan3 entities. If not set rollback only and throw AccessDeniedException
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
//				AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator
//						.getInstance().getService(
//								AuthorizationServiceLocalHome.class); // TODO change method to the one checking the needed access rights
//				autService.checkUserRightToChangeAdminData();
				return null;
			}

		});
	}

	
	
	@SuppressWarnings("unchecked")
	public List<BLSampleHasEnergyScan3VO> findFiltered(final Integer sampleId ) throws Exception{
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<BLSampleHasEnergyScan3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<BLSampleHasEnergyScan3VO> foundEntities = dao.findFiltered(sampleId);
				return foundEntities;
			}

		});
	}
	
	@SuppressWarnings("unchecked")
	public List<BLSampleHasEnergyScan3VO> findByEnergyScan(final Integer energyScanId ) throws Exception{
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<BLSampleHasEnergyScan3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<BLSampleHasEnergyScan3VO> foundEntities = dao.findByEnergyScan(energyScanId);
				return foundEntities;
			}

		});
	}

}