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
package ispyb.server.common.services.shipping;

import ispyb.server.common.daos.shipping.Dewar3DAO;
import ispyb.server.common.util.ejb.EJBAccessCallback;
import ispyb.server.common.util.ejb.EJBAccessTemplate;
import ispyb.server.common.vos.shipping.Dewar3VO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

/**
 * <p>
 * This session bean handles ISPyB Dewar3.
 * </p>
 */
@Stateless
public class Dewar3ServiceBean implements Dewar3Service, Dewar3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(Dewar3ServiceBean.class);

	@EJB
	private Dewar3DAO dao;

	@Resource
	private SessionContext context;

	public Dewar3ServiceBean() {
	};

	/**
	 * Create new Dewar3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public Dewar3VO create(final Dewar3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Dewar3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				dao.create(vo);
				return vo;
			}

		});
	}

	/**
	 * Update the Dewar3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public Dewar3VO update(final Dewar3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Dewar3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				return dao.update(vo);
			}

		});
	}

	/**
	 * Remove the Dewar3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				Dewar3VO vo = findByPk(pk, false, false);
				delete(vo);
				return vo;
			}

		});

	}

	/**
	 * Remove the Dewar3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Dewar3VO vo) throws Exception {
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
	 * @return the Dewar3 value object
	 */
	public Dewar3VO findByPk(final Integer pk, final boolean withContainers, final boolean withDewarTransportHistory)
			throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Dewar3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				Dewar3VO found = dao.findByPk(pk, withContainers, withDewarTransportHistory);
				return found;
			}

		});
	}
	
	public Dewar3VO findByPk(final Integer pk, final boolean withContainers, final boolean withDewarTransportHistory, final boolean withSamples)
			throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Dewar3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				Dewar3VO found = dao.findByPk(pk, withContainers, withDewarTransportHistory, withSamples);
				return found;
			}

		});
	}

	/**
	 * Find all Dewar3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<Dewar3VO> findAll(final boolean withContainers, final boolean withDewarTransportHistory)
			throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Dewar3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<Dewar3VO> foundEntities = dao.findAll(withContainers, withDewarTransportHistory);
				return foundEntities;
			}

		});
	}

	public List<Dewar3VO> findFiltered(final Integer proposalId, final Integer shippingId, final String type,
			final String code, final String comments, final Date date1, final Date date2, final String dewarStatus,
			final String storageLocation, final boolean withDewarHistory, final boolean withContainer) throws Exception {
		return this.findFiltered(proposalId, shippingId, type, code, comments, date1, date2, dewarStatus,
				storageLocation, null, withDewarHistory, withContainer);
	}

	@Deprecated
	@SuppressWarnings("unchecked")
	public List<Dewar3VO> findByCustomQuery(final Integer proposalId, final String dewarName, final String comments,
			final String barCode, final String dewarStatus, final String storageLocation,
			final Date experimentDateStart, final Date experimentDateEnd, final String operatorSiteNumber)
			throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Dewar3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<Dewar3VO> foundEntities = dao.findByCustomQuery(proposalId, dewarName, comments, barCode,
						dewarStatus, storageLocation, experimentDateStart, experimentDateEnd, operatorSiteNumber);
				return foundEntities;
			}

		});
	}

	// findFiltered(Integer proposalId, Integer shippingId, String type, String code,
	// String comments, Date date1, Date date2, String dewarStatus, String storageLocation)
	@Override
	public List<Dewar3VO> findByProposalId(final Integer proposalId) throws Exception {
		return this.findFiltered(proposalId, null, null, null, null, null, null, null, null, false, false);
	}

	@Override
	public List<Dewar3VO> findByShippingId(int shippindId) throws Exception {
		return this.findFiltered(null, shippindId, null, null, null, null, null, null, null, false, false);
	}

	@Override
	public List<Dewar3VO> findByType(String type) throws Exception {
		return this.findFiltered(null, null, type, null, null, null, null, null, null, false, false);
	}

	@Override
	public List<Dewar3VO> findByCode(String code) throws Exception {
		return this.findFiltered(null, null, null, code, null, null, null, null, null, false, false);
	}

	@Override
	public List<Dewar3VO> findByComments(String comments) throws Exception {
		return this.findFiltered(null, null, null, null, comments, null, null, null, null, false, false);
	}

	@Override
	public List<Dewar3VO> findByExperimentDate(Date experimentDateStart) throws Exception {
		return this.findFiltered(null, null, null, null, null, experimentDateStart, experimentDateStart, null, null,
				false, false);
	}

	@Override
	public List<Dewar3VO> findByExperimentDate(Date experimentDateStart, Date experimentDateEnd) throws Exception {
		return this.findFiltered(null, null, null, null, null, experimentDateStart, experimentDateEnd, null, null,
				false, false);
	}

	@Override
	public List<Dewar3VO> findByStatus(String status) throws Exception {
		return this.findFiltered(null, null, null, null, null, null, null, status, null, false, false);
	}

	@Override
	public List<Dewar3VO> findByStorageLocation(String storageLocation) throws Exception {
		return this.findFiltered(null, null, null, null, null, null, null, null, storageLocation, false, false);
	}

	/**
	 * Check if user has access rights to create, change and remove Dewar3 entities. If not set rollback only and throw
	 * AccessDeniedException
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

	/**
	 * Get all Dewar3 entity VOs from a collection of Dewar3 local entities.
	 * 
	 * @param localEntities
	 * @return
	 */
	@SuppressWarnings({ "unused" })
	private Dewar3VO[] getDewar3VOs(List<Dewar3VO> entities) {
		ArrayList<Dewar3VO> results = new ArrayList<Dewar3VO>(entities.size());
		for (Dewar3VO vo : entities) {
			results.add(vo);
		}
		Dewar3VO[] tmpResults = new Dewar3VO[results.size()];
		return results.toArray(tmpResults);
	}

	@Override
	public List<Dewar3VO> findFiltered(final Integer proposalId, final Integer shippingId, final String type,
			final String code, final String comments, final Date date1, final Date date2, final String dewarStatus,
			final String storageLocation, final Integer dewarId, final boolean withDewarHistory,
			final boolean withContainer) throws Exception {
		return this.findFiltered(proposalId, shippingId, type, code, null, comments, date1, date2, dewarStatus,
				storageLocation, dewarId, null, false, withDewarHistory, withContainer);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Dewar3VO> findFiltered(final Integer proposalId, final Integer shippingId, final String type,
			final String code, final String barCode, final String comments, final Date date1, final Date date2,
			final String dewarStatus, final String storageLocation, final Integer dewarId, final Integer firstExperimentId, final boolean fetchSession,
			final boolean withDewarHistory, final boolean withContainer) throws Exception {

		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Dewar3VO>) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				return dao.findFiltered(proposalId, shippingId, type, code, barCode, comments, date1, date2,
						dewarStatus, storageLocation, dewarId, firstExperimentId, fetchSession, withDewarHistory, withContainer);
			}

		});
	}

	public Dewar3VO loadEager(Dewar3VO vo) throws Exception {
		Dewar3VO newVO = this.findByPk(vo.getDewarId(), true, true);
		return newVO;
	}

	@Override
	public List<Dewar3VO> findByBarCode(String barCode) throws Exception {
		return this.findFiltered(null, null, null, null, barCode, null, null, null, null, null, null,null,  false, false,
				false);
	}

	public List<Dewar3VO> findByDateWithHistory(final java.sql.Date firstDate) throws Exception {
		return dao.findByDateWithHistory(firstDate);
	}

	public Integer countDewarSamples(final Integer dewarId) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Integer) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				return dao.countDewarSamples(dewarId);
			}
		});
	}
	
	public List<Dewar3VO> findByExperiment(final Integer experimentId, final String dewarStatus) throws Exception{
		return this.findFiltered(null, null, null, null, null, null, null, null, dewarStatus, null, null, experimentId, false, false, false);
	}

}