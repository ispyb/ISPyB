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
import ispyb.server.mx.vos.sample.Crystal3VO;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import ispyb.server.mx.vos.sample.Crystal3VO;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.axis.utils.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;



import ispyb.server.common.exceptions.AccessDeniedException;
import ispyb.server.mx.services.sample.Crystal3Service;
import ispyb.server.mx.services.sample.Crystal3ServiceLocal;

/**
 * <p>
 * This session bean handles ISPyB Crystal3.
 * </p>
 */
@Stateless
public class Crystal3ServiceBean implements Crystal3Service, Crystal3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(Crystal3ServiceBean.class);

	// Generic HQL request to find instances of Crystal3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK(boolean fetchSamples) {
		return "from Crystal3VO vo " + (fetchSamples ? "left join fetch vo.sampleVOs " : "")
				+ "where vo.crystalId = :pk";
	}

	// Generic HQL request to find all instances of Crystal3
	// TODO choose between left/inner join
	private static final String FIND_ALL(boolean fetchLink1, boolean fetchLink2) {
		return "from Crystal3VO vo " + (fetchLink1 ? "<inner|left> join fetch vo.link1 " : "")
				+ (fetchLink2 ? "<inner|left> join fetch vo.link2 " : "");
	}

	private final static String COUNT_SAMPLE = "SELECT " + " count(DISTINCT(bls.blSampleId)) samplesNumber "
			+ "FROM Crystal c  " + "	 LEFT JOIN BLSample bls ON bls.crystalId = c.crystalId "
			+ "WHERE c.crystalId = :crystalId ";

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

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

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the Crystal3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public Crystal3VO update(final Crystal3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the Crystal3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		checkCreateChangeRemoveAccess();
		Crystal3VO vo = findByPk(pk, false);
		// TODO Edit this business code
		delete(vo);
	}


	/**
	 * Remove the Crystal3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Crystal3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		entityManager.remove(entityManager.merge(vo));
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

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		try {
			return (Crystal3VO) entityManager.createQuery(FIND_BY_PK(fetchSamples)).setParameter("pk", pk)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
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
	
		List<Crystal3VO> foundEntities = entityManager.createQuery(FIND_ALL(withLink1, withLink2)).getResultList();
		return foundEntities;
	}

	@SuppressWarnings("unchecked")
	public List<Crystal3VO> findFiltered(final Integer proposalId, final Integer proteinId, final String acronym,
			final String spaceGroup) throws Exception {

		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(Crystal3VO.class).setFetchMode("structure3VOs", FetchMode.JOIN);

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (acronym != null && !spaceGroup.isEmpty()) {
			crit.add(Restrictions.like("spaceGroup", spaceGroup));
		}

		Criteria subCrit = crit.createCriteria("proteinVO");

		if (acronym != null && !acronym.isEmpty()) {

			subCrit.add(Restrictions.like("acronym", acronym.toUpperCase()));
		}
		
		if (proteinId != null ) {
			subCrit.add(Restrictions.eq("proteinId", proteinId));
		}
		

		if (proposalId != null) {
			Criteria subSubCrit = subCrit.createCriteria("proposalVO");
			subSubCrit.add(Restrictions.eq("proposalId", proposalId));
		}
		subCrit.addOrder(Order.asc("acronym"));
		crit.addOrder(Order.desc("crystalId"));
		
		List<Crystal3VO> foundEntities = crit.list();
		return foundEntities;
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
				
				Session session = (Session) entityManager.getDelegate();

				Criteria crit = session.createCriteria(Crystal3VO.class);
				crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

				Criteria subCrit = crit.createCriteria("proteinVO");

				if (acronym != null && !acronym.isEmpty()) {
					subCrit.add(Restrictions.like("acronym", acronym.toUpperCase()));
				}

				if (proposalId != null) {
					Criteria subSubCrit = subCrit.createCriteria("proposalVO");
					subSubCrit.add(Restrictions.eq("proposalId", proposalId));
					
				}
				if (currentCrystal.getSpaceGroup() != null){
					if (!StringUtils.isEmpty(currentCrystal.getSpaceGroup())) {
						crit.add(Restrictions.like("spaceGroup", currentCrystal.getSpaceGroup()));
					}
					else {
						crit.add(Restrictions.isNull("spaceGroup"));
					}
				}
					
				if (currentCrystal.getCellA() != null) {
					crit.add(Restrictions.eq("cellA", currentCrystal.getCellA()));
				}
				else{
					crit.add(Restrictions.isNull("cellA"));
				}
				
				if (currentCrystal.getCellB() != null) {
					crit.add(Restrictions.eq("cellB", currentCrystal.getCellB()));
				}
				else{
					crit.add(Restrictions.isNull("cellB"));
				}
				if (currentCrystal.getCellC() != null) {
					crit.add(Restrictions.eq("cellC", currentCrystal.getCellC()));
				}
				else{
					crit.add(Restrictions.isNull("cellB"));
				}
				if (currentCrystal.getCellAlpha() != null) {
					crit.add(Restrictions.eq("cellAlpha", currentCrystal.getCellAlpha()));
				}
				else{
					crit.add(Restrictions.isNull("cellAlpha"));
				}
				if (currentCrystal.getCellBeta() != null) {
					crit.add(Restrictions.eq("cellBeta", currentCrystal.getCellBeta()));
				}
				else{
					crit.add(Restrictions.isNull("cellBeta"));
				}
				if (currentCrystal.getCellGamma() != null) {
					crit.add(Restrictions.eq("cellGamma", currentCrystal.getCellGamma()));
				}
				else{
					crit.add(Restrictions.isNull("cellGamma"));
				}
				crit.addOrder(Order.desc("crystalId"));

				List<Crystal3VO> foundEntities = crit.list();
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

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
	}

	
	
	public Integer countSamples(final Integer crystalId)throws Exception{

		Query query = entityManager.createNativeQuery(COUNT_SAMPLE).setParameter("crystalId", crystalId);
		try{
			BigInteger res = (BigInteger) query.getSingleResult();
			return new Integer(res.intValue());
		}catch(NoResultException e){
			System.out.println("ERROR in countSamples - NoResultException: "+crystalId);
			e.printStackTrace();
			return 0;
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * returns the pdb full path for a given acronym/spaceGroup
	 * @param proteinAcronym
	 * @param spaceGroup
	 * @return
	 * @throws Exception
	 */
	public String findPdbFullPath(final String proteinAcronym, final String spaceGroup) throws Exception{
		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(Crystal3VO.class);

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (proteinAcronym != null && !spaceGroup.isEmpty()) {
			crit.add(Restrictions.like("spaceGroup", spaceGroup));
		}

		Criteria subCrit = crit.createCriteria("proteinVO");

		if (proteinAcronym != null && !proteinAcronym.isEmpty()) {

			subCrit.add(Restrictions.like("acronym", proteinAcronym.toUpperCase()));
		}

		subCrit.addOrder(Order.asc("acronym"));
		crit.addOrder(Order.desc("crystalId"));
		
		List<Crystal3VO> foundEntities = crit.list();
		String fileFullPath = "";
		if (foundEntities != null && foundEntities.size() > 0){
			Crystal3VO crystal = foundEntities.get(0);
			if (crystal.getPdbFilePath() != null && crystal.getPdbFileName() != null){
				fileFullPath = crystal.getPdbFilePath()+crystal.getPdbFileName();
			}
		}
		return fileFullPath;
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
	private void checkAndCompleteData(Crystal3VO vo, boolean create) throws Exception {

//		if (create) {
//			if (vo.getCrystalId() != null) {
//				throw new IllegalArgumentException(
//						"Primary key is already set! This must be done automatically. Please, set it to null!");
//			}
//		} else {
//			if (vo.getCrystalId() == null) {
//				throw new IllegalArgumentException("Primary key is not set for update!");
//			}
//		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
	
}