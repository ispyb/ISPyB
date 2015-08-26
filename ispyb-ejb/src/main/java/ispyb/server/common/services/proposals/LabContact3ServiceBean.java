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
package ispyb.server.common.services.proposals;

import ispyb.server.common.daos.proposals.LabContact3DAO;
import ispyb.server.common.util.ejb.EJBAccessCallback;
import ispyb.server.common.util.ejb.EJBAccessTemplate;
import ispyb.server.common.vos.proposals.LabContact3VO;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

/**
 * <p>
 * This session bean handles ISPyB LabContact3.
 * </p>
 */
@Stateless
public class LabContact3ServiceBean implements LabContact3Service, LabContact3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(LabContact3ServiceBean.class);

	@EJB
	private LabContact3DAO dao;

	@Resource
	private SessionContext context;

	public LabContact3ServiceBean() {
	};

	/**
	 * Create new LabContact3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public LabContact3VO create(final LabContact3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (LabContact3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				dao.create(vo);
				return vo;
			}

		});
	}

	/**
	 * Update the LabContact3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public LabContact3VO update(final LabContact3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (LabContact3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				return dao.update(vo);
			}

		});
	}

	/**
	 * Remove the LabContact3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				LabContact3VO vo = findByPk(pk);
				// TODO Edit this business code
				delete(vo);
				return vo;
			}

		});

	}

	/**
	 * Remove the LabContact3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final LabContact3VO vo) throws Exception {
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
	 * @return the LabContact3 value object
	 */
	public LabContact3VO findByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (LabContact3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				LabContact3VO found = dao.findByPk(pk);
				return found;
			}

		});
	}

	// TODO remove following method if not adequate
	/**
	 * Find all LabContact3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<LabContact3VO> findAll() throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<LabContact3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<LabContact3VO> foundEntities = dao.findAll();
				return foundEntities;
			}

		});
	}

	@SuppressWarnings("unchecked")
	public List<LabContact3VO> findFiltered(final Integer proposalId, final String cardName) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<LabContact3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<LabContact3VO> foundEntities = dao.findFiltered(proposalId, cardName);
				return foundEntities;
			}

		});
	}

	/**
	 * Check if user has access rights to create, change and remove LabContact3 entities. If not set rollback only and
	 * throw AccessDeniedException
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
	public List<LabContact3VO> findByPersonIdAndProposalId(final Integer personId, final Integer proposalId) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<LabContact3VO>) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				return dao.findByPersonIdAndProposalId(personId, proposalId);
			}

		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LabContact3VO> findByCardName(final String cardNameWithNum) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<LabContact3VO>) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				return dao.findByCardName(cardNameWithNum);
			}

		});
	}
	
	public Integer hasShipping(final Integer labContactId) throws Exception {

		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Integer) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				Integer foundEntities = dao.hasShipping(labContactId);
				return foundEntities;
			}

		});
	};


}