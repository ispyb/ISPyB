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
import ispyb.server.mx.daos.sample.Protein3DAO;
import ispyb.server.mx.vos.sample.Protein3VO;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import java.nio.file.AccessDeniedException;
import org.apache.log4j.Logger;

/**
 * <p>
 * This session bean handles ISPyB Protein3.
 * </p>
 */
@Stateless
public class Protein3ServiceBean implements Protein3Service, Protein3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(Protein3ServiceBean.class);

	@EJB
	private Protein3DAO dao;

	@Resource
	private SessionContext context;

	public Protein3ServiceBean() {
	};

	/**
	 * Create new Protein3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public Protein3VO create(final Protein3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Protein3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				dao.create(vo);
				return vo;
			}

		});
	}

	/**
	 * Update the Protein3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public Protein3VO update(final Protein3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Protein3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				return dao.update(vo);
			}

		});
	}

	/**
	 * Remove the Protein3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				Protein3VO vo = findByPk(pk, false);
				// TODO Edit this business code
				delete(vo);
				return vo;
			}

		});

	}

	/**
	 * Remove the Protein3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Protein3VO vo) throws Exception {
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
	 * @return the Protein3 value object
	 */
	public Protein3VO findByPk(final Integer pk, final boolean withLink1) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Protein3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				Protein3VO found = dao.findByPk(pk, withLink1);
				return found;
			}

		});
	}

	// TODO remove following method if not adequate
	/**
	 * Find all Protein3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<Protein3VO> findAll(final boolean withLink1) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Protein3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<Protein3VO> foundEntities = dao.findAll(withLink1);
				return foundEntities;
			}

		});
	}

	@SuppressWarnings("unchecked")
	public List<Protein3VO> findByAcronymAndProposalId(final Integer proposalId, final String acronym, final boolean withCrystal, final boolean sortByAcronym) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Protein3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<Protein3VO> foundEntities = dao.findFiltered(proposalId, acronym, withCrystal, sortByAcronym);
				return foundEntities;
			}

		});
	}
	
	public List<Protein3VO> findByAcronymAndProposalId(final Integer proposalId, final String acronym) throws Exception {
		return findByAcronymAndProposalId(proposalId, acronym, false, false);
	}

	public Protein3VO loadEager(Protein3VO vo) throws Exception {
		Protein3VO newVO = this.findByPk(vo.getProteinId(), true);
		return newVO;
	}

	/**
	 * Check if user has access rights to create, change and remove Protein3 entities. If not set rollback only and
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

	
	
	/**
	 * update the proposalId, returns the nb of rows updated
	 * 
	 * @param newProposalId
	 * @param oldProposalId
	 * @return
	 * @throws Exception
	 */
	public Integer updateProposalId(final Integer newProposalId, final Integer oldProposalId) throws Exception{
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Integer) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				Integer foundEntities = dao.updateProposalId(newProposalId, oldProposalId);
				return foundEntities;
			}

		});
	}

	@Override
	public List<Protein3VO> findByProposalId(Integer proposalId)
			throws Exception {
		return this.findByAcronymAndProposalId(proposalId, null, false, false);
	}
	
	public List<Protein3VO> findByProposalId(final Integer proposalId, final boolean withCrystal, boolean sortByAcronym) throws Exception{
		return this.findByAcronymAndProposalId(proposalId, null, withCrystal, sortByAcronym);
	}

}