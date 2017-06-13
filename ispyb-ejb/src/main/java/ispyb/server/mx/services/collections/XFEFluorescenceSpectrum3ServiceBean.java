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

import ispyb.server.mx.vos.collections.XFEFluorescenceSpectrum3VO;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
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
 *  This session bean handles ISPyB XFEFluorescenceSpectrum3.
 * </p>
 */
@Stateless
public class XFEFluorescenceSpectrum3ServiceBean implements XFEFluorescenceSpectrum3Service,
		XFEFluorescenceSpectrum3ServiceLocal {

	private final static Logger LOG = Logger
			.getLogger(XFEFluorescenceSpectrum3ServiceBean.class);


	// Generic HQL request to find instances of XFEFluorescenceSpectrum3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK() {
		return "from XFEFluorescenceSpectrum3VO vo "  + "where vo.xfeFluorescenceSpectrumId = :pk";
	}

	// Generic HQL request to find all instances of XFEFluorescenceSpectrum3
	// TODO choose between left/inner join
	private static final String FIND_ALL() {
		return "from XFEFluorescenceSpectrum3VO vo " ;
	}

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@Resource
	private SessionContext context;

	public XFEFluorescenceSpectrum3ServiceBean() {
	};

	/**
	 * Create new XFEFluorescenceSpectrum3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public XFEFluorescenceSpectrum3VO create(final XFEFluorescenceSpectrum3VO vo) throws Exception {
	
		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the XFEFluorescenceSpectrum3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public XFEFluorescenceSpectrum3VO update(final XFEFluorescenceSpectrum3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the XFEFluorescenceSpectrum3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
	
		checkCreateChangeRemoveAccess();
		XFEFluorescenceSpectrum3VO vo = findByPk(pk);
		// TODO Edit this business code				
		delete(vo);
	}

	/**
	 * Remove the XFEFluorescenceSpectrum3
	 * @param vo the entity to remove.
	 */
	public void delete(final XFEFluorescenceSpectrum3VO vo) throws Exception {
		
		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the XFEFluorescenceSpectrum3 value object
	 */
	public XFEFluorescenceSpectrum3VO findByPk(final Integer pk) throws Exception {
		
		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		try{
			return (XFEFluorescenceSpectrum3VO) entityManager.createQuery(FIND_BY_PK())
				.setParameter("pk", pk).getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}


	// TODO remove following method if not adequate
	/**
	 * Find all XFEFluorescenceSpectrum3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<XFEFluorescenceSpectrum3VO> findAll()
			throws Exception {

		List<XFEFluorescenceSpectrum3VO> foundEntities = entityManager.createQuery(FIND_ALL()).getResultList();
		return foundEntities;
	}

	/**
	 * Check if user has access rights to create, change and remove XFEFluorescenceSpectrum3 entities. If not set rollback only and throw AccessDeniedException
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
		
		// TODO add an authorization service bean for ISPyB
//		AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator
//				.getInstance().getService(
//					AuthorizationServiceLocalHome.class); // TODO change method to the one checking the needed access rights
//				autService.checkUserRightToChangeAdminData();

	}
	
	
	@SuppressWarnings("unchecked")
	public List<XFEFluorescenceSpectrum3VO> findFiltered(final Integer sessionId, final Integer sampleId, final Integer proposalId) throws Exception{

		Session session = (Session)entityManager.getDelegate();

		Criteria crit = session.createCriteria(XFEFluorescenceSpectrum3VO.class);
		
		Criteria subCritSess = crit.createCriteria("sessionVO");

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (sessionId != null) {
			subCritSess.add(Restrictions.eq("sessionId", sessionId));
		}
		
		if (proposalId != null) {
			Criteria subCritProposal = subCritSess.createCriteria("proposalVO");
			subCritProposal.add(Restrictions.eq("proposalId", proposalId));
		}
		
		if (sampleId != null) {
			Criteria subCritSample = crit.createCriteria("blSampleVO");
			subCritSample.add(Restrictions.eq("blSampleId", sampleId));
		}

		crit.addOrder(Order.desc("xfeFluorescenceSpectrumId"));

		List<XFEFluorescenceSpectrum3VO> foundEntities = crit.list();
		return foundEntities;
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
	private void checkAndCompleteData(XFEFluorescenceSpectrum3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getXfeFluorescenceSpectrumId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getXfeFluorescenceSpectrumId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
}