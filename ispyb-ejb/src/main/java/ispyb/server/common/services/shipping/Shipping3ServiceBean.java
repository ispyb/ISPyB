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

import ispyb.server.common.daos.shipping.Shipping3DAO;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.util.ejb.EJBAccessCallback;
import ispyb.server.common.util.ejb.EJBAccessTemplate;
import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

/**
 * <p>
 * This session bean handles ISPyB Shipping3.
 * </p>
 */
@Stateless
public class Shipping3ServiceBean implements Shipping3Service, Shipping3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(Shipping3ServiceBean.class);

	@EJB
	private Shipping3DAO dao;

	@Resource
	private SessionContext context;

	public Shipping3ServiceBean() {
	};

	/**
	 * Create new Shipping3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public Shipping3VO create(final Shipping3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Shipping3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				dao.create(vo);
				return vo;
			}

		});
	}

	/**
	 * Update the Shipping3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public Shipping3VO update(final Shipping3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Shipping3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				return dao.update(vo);
			}

		});
	}

	/**
	 * Remove the Shipping3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				Shipping3VO vo = findByPk(pk, false);
				delete(vo);
				return vo;
			}

		});

	}

	/**
	 * Remove the Shipping3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Shipping3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
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
	 * @return the Shipping3 value object
	 */
	public Shipping3VO findByPk(final Integer pk, final boolean withDewars) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Shipping3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				Shipping3VO found = dao.findByPk(pk, withDewars);
				return found;
			}

		});
	}
	
	@Override
	public List<Map<String, Object>> getShippingById(final Integer shippingId) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Map<String, Object>>) template.execute(new EJBAccessCallback() {
			public List<Map<String, Object>> doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				List<Map<String, Object>> found = dao.getShippingById(shippingId);
				return found;
			}

		});
	}
	

	// TODO remove following method if not adequate
	/**
	 * Find all Shipping3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<Shipping3VO> findAll() throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Shipping3VO>) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				List<Shipping3VO> foundEntities = dao.findAll();
				return foundEntities;
			}

		});
	}

	@SuppressWarnings("unchecked")
	public List<Shipping3VO> findFiltered(final Integer proposalId, final String type) throws Exception {

		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Shipping3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<Shipping3VO> foundEntities = dao.findFiltered(proposalId, type);
				return foundEntities;
			}

		});
	}

	/**
	 * Check if user has access rights to create, change and remove Shipping3 entities. If not set rollback only and throw
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Shipping3VO> findByStatus(final String status, final boolean withDewars) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Shipping3VO>) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				return dao.findByStatus(status, withDewars);
			}

		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Shipping3VO> findByProposal(final Integer userProposalId, final boolean withDewars) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Shipping3VO>) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				return dao.findFiltered(userProposalId, null, null, null, null, null, null, null, null, withDewars);
			}

		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Shipping3VO> findByCreationDate(final Date firstDate, final boolean withDewars) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Shipping3VO>) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				return dao.findFiltered(null, null, null, null, null, firstDate, null, null, null, withDewars);
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Shipping3VO> findByCreationDateInterval(final Date firstDate, final Date endDate) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Shipping3VO>) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				return dao.findFiltered(null, null, null, null, null, firstDate, endDate, null, null, false);
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Shipping3VO> findByBeamLineOperator(final String beamlineOperatorSiteNumber, final boolean withDewars)
			throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Shipping3VO>) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				return dao.findByBeamLineOperator(beamlineOperatorSiteNumber, withDewars);
			}
		});
	}

	@Override
	public List<Shipping3VO> findByProposalCode(final String proposalCode, final boolean withDewars) throws Exception {

		return this.findFiltered(null, null, proposalCode, null, null, null, null, null, withDewars);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Shipping3VO> findFiltered(final Integer proposalId, final String shippingName, final String proposalCode,
			final String proposalNumber, final String mainProposer, final Date date, final Date date2,
			final String operatorSiteNumber, final boolean withDewars) throws Exception {

		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Shipping3VO>) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				return dao.findFiltered(proposalId, shippingName, proposalCode, null, mainProposer, date, date2, operatorSiteNumber,
						null, withDewars);
			}
		});

	}

	@Override
	public List<Shipping3VO> findByCustomQuery(final Integer proposalId, final String shippingName, final String proposalCode,
			final String proposalNumber, final String mainProposer, final Date date, final Date date2, final String operatorSiteNumber)
			throws Exception {

		return this.findFiltered(proposalId, shippingName, proposalCode, proposalNumber, mainProposer, date, date2,
				operatorSiteNumber, false);

	}

	/**
	 * update the proposalId, returns the nb of rows updated
	 * 
	 * @param newProposalId
	 * @param oldProposalId
	 * @return
	 * @throws Exception
	 */
	public Integer updateProposalId(final Integer newProposalId, final Integer oldProposalId) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Integer) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				Integer foundEntities = dao.updateProposalId(newProposalId, oldProposalId);
				return foundEntities;
			}

		});
	}

	public int deleteAllSamplesAndContainersForShipping(Integer shippingId) throws Exception {
		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		Dewar3Service dewarService = (Dewar3Service) ejb3ServiceLocator.getLocalService(Dewar3Service.class);
		Container3Service containerService = (Container3Service) ejb3ServiceLocator.getLocalService(Container3Service.class);

		int nb = 0;
		Shipping3VO ship = this.findByPk(shippingId, true);
		Set<Dewar3VO> dewars = ship.getDewarVOs();
		for (Dewar3VO dewar3vo : dewars) {
			Dewar3VO dewar = dewarService.findByPk(dewar3vo.getDewarId(), true, true);
			Set<Container3VO> containers = dewar.getContainerVOs();
			for (Container3VO container3vo : containers) {
				containerService.deleteByPk(container3vo.getContainerId());
				nb = nb + 1;
			}
		}
		return nb;
	}

	@Override
	public List<Shipping3VO> findByProposalType(String proposalType) throws Exception {
		return this.findFiltered(null, null, proposalType, null, null, null, null, null, false);
	}

	@Override
	public List<Shipping3VO> findByProposalCodeAndDates(String proposalType, Date firstDate, Date endDate) throws Exception {
		return this.findFiltered(null, null, proposalType, null, null, firstDate, endDate, null, false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Shipping3VO> findByIsStorageShipping(final Boolean isStorageShipping) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Shipping3VO>) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				return dao.findByIsStorageShipping(isStorageShipping);
			}

		});
	}

	public Shipping3VO loadEager(Shipping3VO vo) throws Exception {
		Shipping3VO newVO = this.findByPk(vo.getShippingId(), true);
		return newVO;
	}

	public Integer[] countShippingInfo(final Integer shippingId) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Integer[]) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				return dao.countShippingInfo(shippingId);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ispyb.server.common.services.shipping.Shipping3Service#findByPk(java.lang.Integer, boolean, boolean)
	 */
	@Override
	public Shipping3VO findByPk(final Integer pk, final boolean withDewars, final boolean withSession) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Shipping3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				Shipping3VO found = dao.findByPk(pk, withDewars, withSession);
				return found;
			}

		});
	}
}