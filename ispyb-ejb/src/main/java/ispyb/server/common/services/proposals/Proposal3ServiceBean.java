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

package ispyb.server.common.services.proposals;

import ispyb.server.common.daos.proposals.Proposal3DAO;
import ispyb.server.common.util.ejb.EJBAccessCallback;
import ispyb.server.common.util.ejb.EJBAccessTemplate;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.proposals.ProposalWS3VO;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;

/**
 * <p>
 * This session bean handles Proposal table.
 * </p>
 * 
 */
@Stateless
public class Proposal3ServiceBean implements Proposal3Service, Proposal3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(Proposal3ServiceBean.class);

	@EJB
	private Proposal3DAO dao;

	@Resource
	private SessionContext context;

	public Proposal3ServiceBean() {
	};

	/**
	 * Create new Proposal.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public Proposal3VO create(final Proposal3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Proposal3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				dao.create(vo);
				return vo;
			}

		});
	}

	/**
	 * Update the Proposal data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public Proposal3VO update(final Proposal3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Proposal3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {

				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				return dao.update(vo);
			}

		});
	}

	/**
	 * Remove the Proposal from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				Proposal3VO vo = findByPk(pk);
				// TODO Edit this business code
				delete(vo);
				return vo;
			}

		});

	}

	/**
	 * Remove the Proposal
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Proposal3VO vo) throws Exception {
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
	public Proposal3VO findByPk(final Integer pk) throws Exception {

		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		EJBAccessCallback callBack = new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				Proposal3VO found = dao.findByPk(pk, false, false, false);
				return found;
			};
		};
		Proposal3VO vo = (Proposal3VO) template.execute(callBack);
		return vo;
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
	public Proposal3VO findByPk(final Integer pk, final boolean fetchSessions, final boolean fetchProteins,
			final boolean fetchShippings) throws Exception {

		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		EJBAccessCallback callBack = new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				Proposal3VO found = dao.findByPk(pk, fetchSessions, fetchProteins, fetchShippings);
				return found;
			};
		};
		Proposal3VO vo = (Proposal3VO) template.execute(callBack);
		return vo;
	}

	@SuppressWarnings("unchecked")
	@WebMethod
	public List<Proposal3VO> findByCodeAndNumber(final String code, final String number, final boolean fetchSessions,
			final boolean fetchProteins, final boolean detachLight) throws Exception {

		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		EJBAccessCallback callBack = new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<Proposal3VO> foundEntities = dao.findByCodeAndNumber(code, number, fetchSessions, fetchProteins);
				List<Proposal3VO> vos;
				if (detachLight)
					vos = getLightProposalVOs(foundEntities);
				else
					vos = getProposalVOs(foundEntities);
				return vos;
			};
		};
		List<Proposal3VO> ret = (List<Proposal3VO>) template.execute(callBack);

		return ret;
	}
	
	@SuppressWarnings("unchecked")
	@WebMethod
	public List<Proposal3VO> findByLoginName(final String loginName) throws Exception {

		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		EJBAccessCallback callBack = new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<Proposal3VO> foundEntities = dao.findByLoginName(loginName);
				List<Proposal3VO> vos;
				vos = getProposalVOs(foundEntities);
				return vos;
			};
		};
		List<Proposal3VO> ret = (List<Proposal3VO>) template.execute(callBack);

		return ret;
	}

	public ProposalWS3VO findForWSByCodeAndNumber(final String code, final String number) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		EJBAccessCallback callBack = new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<Proposal3VO> foundEntities = dao.findByCodeAndNumber(code, number, false, false);
				ProposalWS3VO[] vos = getWSProposalVOs(foundEntities);
				return vos;
			};
		};
		ProposalWS3VO[] ret = (ProposalWS3VO[]) template.execute(callBack);
		if(ret != null && ret.length > 0)
			return ret[0];
		return null;
	}

	private ProposalWS3VO[] getWSProposalVOs(List<Proposal3VO> entities) throws CloneNotSupportedException {
		if (entities == null)
			return null;
		ArrayList<ProposalWS3VO> results = new ArrayList<ProposalWS3VO>(entities.size());
		for (Proposal3VO vo : entities) {
			ProposalWS3VO otherVO = getWSProposalVO(vo);
			results.add(otherVO);
		}
		ProposalWS3VO[] tmpResults = new ProposalWS3VO[results.size()];
		return (ProposalWS3VO[]) results.toArray(tmpResults);
	}

	private ProposalWS3VO getWSProposalVO(Proposal3VO vo) throws CloneNotSupportedException {
		Proposal3VO otherVO = getLightProposalVO(vo);
		Integer personId = null;
		personId = otherVO.getPersonVOId();
		otherVO.setPersonVO(null);
		ProposalWS3VO wsProposal = new ProposalWS3VO(otherVO);
		wsProposal.setPersonId(personId);
		return wsProposal;
	}

	/**
	 * Find all Scientists and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	@WebMethod
	public List<Proposal3VO> findAll(final boolean detachLight) throws Exception {

		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		EJBAccessCallback callBack = new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<Proposal3VO> foundEntities = dao.findAll();
				List<Proposal3VO> vos;
				if (detachLight)
					vos = getLightProposalVOs(foundEntities);
				else
					vos = getProposalVOs(foundEntities);
				return vos;
			};
		};
		List<Proposal3VO> ret = (List<Proposal3VO>) template.execute(callBack);
		return ret;
	}

	/**
	 * Finds a Proposal by its code and number and title (if title is null only search by code and number).
	 * 
	 * @param code
	 * @param number
	 * @param title
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@WebMethod
	public List<Proposal3VO> findFiltered(final String code, final String number, final String title) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Proposal3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<Proposal3VO> foundEntities = dao.findFiltered(code, number, title);
				return foundEntities;
			}

		});
	}

	public Integer[] updateProposalFromIds(final Integer newPropId, final Integer oldPropId) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Integer[]) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				Integer[] foundEntities = dao.updateProposalId(newPropId, oldPropId);
				return foundEntities;
			}

		});
	}

	public Integer[] updateProposalFromDesc(String newPropDesc, String oldPropDesc) throws Exception {
		String code = newPropDesc.substring(0, 2);
		String number = newPropDesc.substring(2);
		Integer newPropId = this.findByCodeAndNumber(code, number, false, false, false).get(0).getProposalId();

		Integer oldPropId = this
				.findByCodeAndNumber(oldPropDesc.substring(0, 2), oldPropDesc.substring(2), false, false,
						false).get(0).getProposalId();
		return this.updateProposalFromIds(newPropId, oldPropId);

	}

	/**
	 * Get all entity VOs from a collection of local entities.
	 * 
	 * @param localEntities
	 * @return
	 */
	private List<Proposal3VO> getProposalVOs(List<Proposal3VO> entities) {
		List<Proposal3VO> results = new ArrayList<Proposal3VO>(entities.size());
		for (Proposal3VO vo : entities) {
			vo.getProteinVOs();
			results.add(vo);
		}
		return results;
	}

	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private List<Proposal3VO> getLightProposalVOs(List<Proposal3VO> entities) throws CloneNotSupportedException {
		List<Proposal3VO> results = new ArrayList<Proposal3VO>(entities.size());
		for (Proposal3VO vo : entities) {
			Proposal3VO otherVO = getLightProposalVO(vo);
			results.add(otherVO);
		}
		return results;
	}

	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private Proposal3VO getLightProposalVO(Proposal3VO vo) throws CloneNotSupportedException {
		if (vo == null)
			return null;
		Proposal3VO otherVO = vo.clone();
		otherVO.setProteinVOs(null);
		otherVO.setSessionVOs(null);
		otherVO.setShippingVOs(null);
		return otherVO;
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

}
