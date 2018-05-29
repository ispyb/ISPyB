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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;


import ispyb.server.common.exceptions.AccessDeniedException;
import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.mx.services.sample.Crystal3Service;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.DiffractionPlan3VO;
import ispyb.server.mx.vos.sample.Protein3VO;

/**
 * <p>
 * This session bean handles ISPyB Container3.
 * </p>
 */
@Stateless
public class Container3ServiceBean implements Container3Service, Container3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(Container3ServiceBean.class);
	
	// Generic HQL request to find instances of Container3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK(boolean fetchSamples) {
		return "from Container3VO vo " 
				+ (fetchSamples ? " LEFT JOIN FETCH vo.sampleVOs sa LEFT JOIN FETCH sa.blsampleImageVOs LEFT JOIN FETCH sa.blSubSampleVOs " : " ")
				+ "where vo.containerId = :pk";
	}

	// Generic HQL request to find all instances of Container3
	// TODO choose between left/inner join
	private static final String FIND_ALL() {
			return "from Container3VO vo ";
	}
	
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;
	
	@EJB
	private Crystal3Service crystal3Service;

	public Container3ServiceBean() {
	};

	/**
	 * Create new Container3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public Container3VO create(final Container3VO vo) throws Exception {
		
		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}


	/**
	 * Update the Container3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public Container3VO update(final Container3VO vo) throws Exception {
		
		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the Container3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		
		checkCreateChangeRemoveAccess();
		Container3VO vo = findByPk(pk, false);
		delete(vo);
	}

	/**
	 * Remove the Container3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Container3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		entityManager.remove(entityManager.merge(vo));
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the Container3 value object
	 */
	public Container3VO findByPk(final Integer pk, final boolean fetchSamples) throws Exception {
		
		checkCreateChangeRemoveAccess();
		try {
			return (Container3VO) entityManager.createQuery(FIND_BY_PK(fetchSamples)).setParameter("pk", pk)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	// TODO remove following method if not adequate
	/**
	 * Find all Container3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<Container3VO> findAll() throws Exception {
		
		List<Container3VO> foundEntities = entityManager.createQuery(FIND_ALL()).getResultList();
		return foundEntities;
	}

	@SuppressWarnings("unchecked")
	public List<Container3VO> findByDewarId(final Integer dewarId) throws Exception {
		
		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(Container3VO.class);
		Criteria subCrit = crit.createCriteria("dewarVO");

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !
		
		subCrit.add(Restrictions.eq("dewarId", dewarId));
		
		crit.addOrder(Order.desc("containerId"));

		List<Container3VO> foundEntities = crit.list();
		return foundEntities;
	}

	@SuppressWarnings("unchecked")
	public List<Container3VO> findByProposalIdAndStatus(final Integer proposalId, final String containerStatusProcess) throws Exception {
		
		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(Container3VO.class);
		Criteria subCrit = crit.createCriteria("dewarVO");

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !
		
		Criteria subSubCrit = subCrit.createCriteria("shippingVO");
		Criteria subSubSubCrit = subSubCrit.createCriteria("proposalVO");
		subSubSubCrit.add(Restrictions.eq("proposalId", proposalId));
		crit.add(Restrictions.like("containerStatus", containerStatusProcess));
		crit.addOrder(Order.desc("containerId"));

		List<Container3VO> foundEntities = crit.list();
		return foundEntities;
	}

	/**
	 * Check if user has access rights to create, change and remove Container3 entities. If not set rollback only and
	 * throw AccessDeniedException
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
	public List<Container3VO> findByCode(final Integer dewarId, final String code)throws Exception{
		
		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(Container3VO.class);
		Criteria subCrit = crit.createCriteria("dewarVO");

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		subCrit.add(Restrictions.eq("dewarId", dewarId));
		crit.add(Restrictions.like("code", code));
		crit.addOrder(Order.desc("containerId"));

		List<Container3VO> foundEntities = crit.list();
		return foundEntities;
	}

	@Override
	public Container3VO savePuck(Container3VO container, int proposalId) throws Exception {
		Container3VO containerDB = this.findByPk(container.getContainerId(), true);
		
		containerDB.setSampleChangerLocation(container.getSampleChangerLocation());
		containerDB.setCapacity(container.getCapacity());
		containerDB.setCode(container.getCode());
		containerDB.setContainerType(container.getContainerType());
		containerDB.setBeamlineLocation(container.getBeamlineLocation());
		
		Set<String> locations = new HashSet<String>();
		/** Adding Sample **/
		for (BLSample3VO sample : container.getSampleVOs()) {
			locations.add(sample.getLocation());
			DiffractionPlan3VO diff = sample.getDiffractionPlanVO();
			diff = entityManager.merge(diff);
			
			Crystal3VO crystal = sample.getCrystalVO();
			
			Crystal3VO searchCrystal = crystal3Service.findByAcronymAndCellParam(sample.getCrystalVO().getProteinVO().getAcronym(), crystal, proposalId); 
			if (searchCrystal != null ){
				/** Crystal for this acronym and cell unit parameters already exist **/
				sample.setCrystalVO(searchCrystal);
			}
			else{
				/** Crystal not found then we create a new one **/
				crystal.setCrystalId(null);
				Protein3VO protein = entityManager.find(Protein3VO.class, sample.getCrystalVO().getProteinVO().getProteinId());
				crystal.setProteinVO(protein);
				crystal = entityManager.merge(crystal);
				sample.setCrystalVO(crystal);
			}
			
			sample.setDiffractionPlanVO(diff);
			sample.setContainerVO(containerDB);
			sample = entityManager.merge(sample);
		}
		/** Retrieving container **/
		containerDB = this.findByPk(container.getContainerId(), true);
		
		System.out.println("sample locations: " + locations.toString());
		List<BLSample3VO> toBeRemoved = new ArrayList<BLSample3VO>();
		
		/** Locations of potentially removed samples **/
		Set<String> totalLocations = new HashSet<String>();
		for (BLSample3VO sample : containerDB.getSampleVOs()) {
			if (!locations.contains(sample.getLocation())){
				toBeRemoved.add(sample);
			}
			totalLocations.add(sample.getLocation());
		}
		System.out.println("total locations: " + totalLocations.toString());
		
		System.out.println("to be removed: " + toBeRemoved.size());
		for (BLSample3VO sample : toBeRemoved) {
			System.out.println("Removing" + sample.getLocation());
			entityManager.remove(sample);
		}
		
		return this.findByPk(container.getContainerId(), true);
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
	private void checkAndCompleteData(Container3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getContainerId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getContainerId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
}