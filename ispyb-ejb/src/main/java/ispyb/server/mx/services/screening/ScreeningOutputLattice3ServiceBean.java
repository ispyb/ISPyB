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

import ispyb.server.mx.vos.screening.ScreeningOutputLattice3VO;
import ispyb.server.mx.vos.screening.ScreeningOutputLatticeWS3VO;

import java.util.List;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * <p>
 * This session bean handles ISPyB ScreeningOutputLattice3.
 * </p>
 */
@Stateless
public class ScreeningOutputLattice3ServiceBean implements ScreeningOutputLattice3Service,
		ScreeningOutputLattice3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(ScreeningOutputLattice3ServiceBean.class);

	// Generic HQL request to find instances of ScreeningOutputLattice3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK() {
		return "from ScreeningOutputLattice3VO vo " + "where vo.screeningOutputId = :pk";
	}

	// Generic HQL request to find all instances of ScreeningOutputLattice3
	// TODO choose between left/inner join
	private static final String FIND_ALL() {
		return "from ScreeningOutputLattice3VO vo " ;
	}

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@Resource
	private SessionContext context;

	public ScreeningOutputLattice3ServiceBean() {
	};

	/**
	 * Create new ScreeningOutputLattice3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public ScreeningOutputLattice3VO create(final ScreeningOutputLattice3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the ScreeningOutputLattice3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public ScreeningOutputLattice3VO update(final ScreeningOutputLattice3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the ScreeningOutputLattice3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
	
		checkCreateChangeRemoveAccess();
		ScreeningOutputLattice3VO vo = findByPk(pk);
		// TODO Edit this business code
		delete(vo);
	}

	/**
	 * Remove the ScreeningOutputLattice3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final ScreeningOutputLattice3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		entityManager.remove(vo);
	}
		
	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the ScreeningOutputLattice3 value object
	 */
	public ScreeningOutputLattice3VO findByPk(final Integer pk)
			throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		try{
			return (ScreeningOutputLattice3VO) entityManager.createQuery(FIND_BY_PK())
				.setParameter("pk", pk).getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}

	// TODO remove following method if not adequate
	/**
	 * Find all ScreeningOutputLattice3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<ScreeningOutputLattice3VO> findAll() throws Exception {

		List<ScreeningOutputLattice3VO> foundEntities = entityManager.createQuery(FIND_ALL()).getResultList();
		return foundEntities;
	}

	/**
	 * Check if user has access rights to create, change and remove ScreeningOutputLattice3 entities. If not set
	 * rollback only and throw AccessDeniedException
	 * 
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
	
		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
	}
	
	@SuppressWarnings("unchecked")
	public List<ScreeningOutputLattice3VO> findFiltered(final Integer dataCollectionId) throws Exception{

		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(ScreeningOutputLattice3VO.class);
		Criteria subCritScOut = crit.createCriteria("screeningOutputVO");
		Criteria subCritSc = subCritScOut.createCriteria("screeningVO");
		Criteria subCritDc = subCritSc.createCriteria("dataCollectionVO");

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (dataCollectionId != null) {
			subCritDc.add(Restrictions.eq("dataCollectionId", dataCollectionId));
		}
		
		crit.addOrder(Order.desc("screeningOutputLatticeId"));

		List<ScreeningOutputLattice3VO> foundEntities = crit.list();
		return foundEntities;
	}
	
	
	public ScreeningOutputLatticeWS3VO findByDataCollectionId(final Integer dataCollectionId) throws Exception{
		List<ScreeningOutputLattice3VO> list = findFiltered(dataCollectionId);
		if (list == null || list.size() == 0)
			return null;
		
		return getWSScreeningOutputLatticeVO(list.get(0));
	}

	
	private ScreeningOutputLatticeWS3VO getWSScreeningOutputLatticeVO(ScreeningOutputLattice3VO vo) throws CloneNotSupportedException {
		ScreeningOutputLattice3VO otherVO = getLightScreeningOutputLattice3VO(vo);
		Integer screeningOutputId = null;
		screeningOutputId = otherVO.getScreeningOutputVOId();
		otherVO.setScreeningOutputVO(null);
		ScreeningOutputLatticeWS3VO wsScreeningOutputLattice = new ScreeningOutputLatticeWS3VO(otherVO);
		wsScreeningOutputLattice.setScreeningOutputId(screeningOutputId);
		return wsScreeningOutputLattice;
	}
	
	private ScreeningOutputLattice3VO getLightScreeningOutputLattice3VO(ScreeningOutputLattice3VO vo) throws CloneNotSupportedException {
		ScreeningOutputLattice3VO otherVO = (ScreeningOutputLattice3VO) vo.clone();
		// otherVO.set(null);
		return otherVO;
	}
	

	/* Private methods ------------------------------------------------------ */

	/**
	 * Checks the data for integrity. E.g. if references and categories exist.
	 * 
	 * @param vo
	 *            the data to check
	 * @param create
	 *            should be true if the value object is just being created in the DB, this avoids some checks like
	 *            testing the primary key
	 * @exception VOValidateException
	 *                if data is not correct
	 */
	private void checkAndCompleteData(ScreeningOutputLattice3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getScreeningOutputLatticeId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getScreeningOutputLatticeId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
}
