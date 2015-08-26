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
package ispyb.server.mx.services.screening;

import ispyb.server.common.util.ejb.EJBAccessCallback;
import ispyb.server.common.util.ejb.EJBAccessTemplate;
import ispyb.server.mx.daos.screening.Screening3DAO;
import ispyb.server.mx.vos.screening.Screening3VO;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

/**
 * <p>
 * This session bean handles ISPyB Screening3.
 * </p>
 */
@Stateless
public class Screening3ServiceBean implements Screening3Service, Screening3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(Screening3ServiceBean.class);

	@EJB
	private Screening3DAO dao;

	@Resource
	private SessionContext context;

	public Screening3ServiceBean() {
	};

	/**
	 * Create new Screening3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public Screening3VO create(final Screening3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Screening3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				dao.create(vo);
				return vo;
			}

		});
	}

	/**
	 * Update the Screening3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public Screening3VO update(final Screening3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Screening3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				return dao.update(vo);
			}

		});
	}

	/**
	 * Remove the Screening3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				Screening3VO vo = findByPk(pk, false,  false);
				// TODO Edit this business code
				delete(vo);
				return vo;
			}

		});

	}

	/**
	 * Remove the Screening3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Screening3VO vo) throws Exception {
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
	 * @return the Screening3 value object
	 */
	public Screening3VO findByPk(final Integer pk, final boolean fetchScreeningRank,  final boolean fetchScreeningOutput) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Screening3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				Screening3VO found = dao.findByPk(pk, fetchScreeningRank,  fetchScreeningOutput);
				return found;
			}

		});
	}

	// TODO remove following method if not adequate
	/**
	 * Find all Screening3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<Screening3VO> findAll(final boolean fetchScreeningRank,  final boolean fetchScreeningOutput) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Screening3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<Screening3VO> foundEntities = dao.findAll(fetchScreeningRank,  fetchScreeningOutput);
				return foundEntities;
			}

		});
	}

	/**
	 * Check if user has access rights to create, change and remove Screening3 entities. If not set rollback only and
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
	 * Get all Screening3 entity VOs from a collection of Screening3 local entities.
	 * 
	 * @param localEntities
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	private Screening3VO[] getScreening3VOs(List<Screening3VO> entities) {
		ArrayList results = new ArrayList(entities.size());
		for (Screening3VO vo : entities) {
			results.add(vo);
		}
		Screening3VO[] tmpResults = new Screening3VO[results.size()];
		return (Screening3VO[]) results.toArray(tmpResults);
	}
	
	public Screening3VO loadEager(Screening3VO vo) throws Exception{
		Screening3VO newVO = this.findByPk(vo.getScreeningId(),true, true);
		return newVO;
	}
	
	@SuppressWarnings("unchecked")
	public List<Screening3VO> findFiltered(final Integer dataCollectionId) throws Exception{
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<Screening3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<Screening3VO> foundEntities = dao.findFiltered(dataCollectionId);
				return foundEntities;
			}

		});
	}

}