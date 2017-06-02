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
import ispyb.server.mx.daos.sample.Crystal3DAO;
import ispyb.server.mx.vos.sample.Crystal3VO;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

/**
 * <p>
 * This session bean handles ISPyB Crystal3.
 * </p>
 */
@Stateless
public class Crystal3ServiceBean implements Crystal3Service, Crystal3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(Crystal3ServiceBean.class);

	@EJB
	private Crystal3DAO dao;

	@Resource
	private SessionContext context;

	public Crystal3ServiceBean() {
	};

	/**
	 * Create new Crystal3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public Crystal3VO create(final Crystal3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Crystal3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				dao.create(vo);
				return vo;
			}

		});
	}

	/**
	 * Update the Crystal3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public Crystal3VO update(final Crystal3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Crystal3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				return dao.update(vo);
			}

		});
	}

	/**
	 * Remove the Crystal3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				Crystal3VO vo = findByPk(pk, false);
				// TODO Edit this business code
				delete(vo);
				return vo;
			}

		});

	}

	/**
	 * Remove the Crystal3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Crystal3VO vo) throws Exception {
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
	 * @param fetchSamples
	 * @return the Crystal3 value object
	 */
	public Crystal3VO findByPk(final Integer pk, final boolean fetchSamples) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Crystal3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				Crystal3VO found = dao.findByPk(pk, fetchSamples);
				return found;
			}

		});
	}

	public Crystal3VO loadEager(Crystal3VO vo) throws Exception {
		Crystal3VO newVO = this.findByPk(vo.getCrystalId(), true);
		return newVO;
	}

	// TODO remove following method if not adequate
	/**
	 * Find all Crystal3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<Crystal3VO> findAll(final boolean withLink1, final boolean withLink2) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Crystal3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<Crystal3VO> foundEntities = dao.findAll(withLink1, withLink2);
				return foundEntities;
			}

		});
	}

	@SuppressWarnings("unchecked")
	public List<Crystal3VO> findFiltered(final Integer proposalId, final Integer proteinId, final String acronym,
			final String spaceGroup) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Crystal3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<Crystal3VO> foundEntities = dao.findFiltered(proposalId, proteinId, acronym, spaceGroup);
				return foundEntities;
			}

		});
	}

	public List<Crystal3VO> findByProposalId(final Integer proposalId) throws Exception {
		return this.findFiltered(proposalId, null, null, null);
	}

	@Override
	public List<Crystal3VO> findByProteinId(Integer proteinId) throws Exception {
		return this.findFiltered(null, proteinId, null, null);
	}

	@SuppressWarnings("unchecked")
	public Crystal3VO findByAcronymAndCellParam(final String acronym, final Crystal3VO currentCrystal,
			final Integer proposalId) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		List<Crystal3VO> list = (List<Crystal3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<Crystal3VO> foundEntities = dao.findByAcronymAndCellParam(proposalId, acronym, currentCrystal);
				return foundEntities;
			}

		});
		if (list.size() > 0)
			return list.get(0);
		else
			return null;
	}

	/**
	 * Check if user has access rights to create, change and remove Crystal3 entities. If not set rollback only and
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

	
	
	public Integer countSamples(final Integer crystalId)throws Exception{
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Integer) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				return dao.countSamples(crystalId);
			}
		});
	}

	
	/**
	 * returns the pdb full path for a given acronym/spaceGroup
	 * @param proteinAcronym
	 * @param spaceGroup
	 * @return
	 * @throws Exception
	 */
	public String findPdbFullPath(final String proteinAcronym, final String spaceGroup) throws Exception{
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (String) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<Crystal3VO> foundEntities = dao.findFiltered(null, null, proteinAcronym, spaceGroup);
				String fileFullPath = "";
				if (foundEntities != null && foundEntities.size() > 0){
					Crystal3VO crystal = foundEntities.get(0);
					if (crystal.getPdbFilePath() != null && crystal.getPdbFileName() != null){
						fileFullPath = crystal.getPdbFilePath()+crystal.getPdbFileName();
					}
				}
				return fileFullPath;
			}

		});
	}
}