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

package ispyb.server.mx.daos.sample;

import ispyb.common.util.StringUtils;
import ispyb.server.common.util.ejb.CastDecimalOrder;
import ispyb.server.mx.vos.sample.BLSample3VO;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * <p>
 * The data access object for BLSample3 objects (rows of table BLSample).
 * </p>
 * 
 * @see {@link BLSample3DAO}
 */
@Stateless
public class BLSample3DAOBean implements BLSample3DAO {

	private final static Logger LOG = Logger.getLogger(BLSample3DAOBean.class);

	// Generic HQL request to find instances of BLSample3 by pk
	private static final String FIND_BY_PK(boolean fetchEnergyScan, boolean fetchSubSamples) {
		return "from BLSample3VO vo " + (fetchEnergyScan ? "left join fetch vo.energyScanVOs " : "")
		+ (fetchSubSamples ? "left join fetch vo.blSubSampleVOs " : "")
				+ "where vo.blSampleId = :pk";
	}

	// Generic HQL request to find all instances of BLSample3
	private static final String FIND_ALL(boolean fetchEnergyScan, boolean fetchSubSamples) {
		return "from BLSample3VO vo " + (fetchEnergyScan ? "left join fetch vo.energyScanVOs " : "")
		+ (fetchSubSamples ? "left join fetch vo.blSubSampleVOs " : "");
	}
	
	private static final String SELECT_SAMPLE_INFO = " SELECT BLSample.blSampleId, BLSample.name, BLSample.code,  "
			+ "BLSample.holderLength, BLSample.location, BLSample.SMILES, BLSample.diffractionPlanId as BLSampleDiffractionPlanId, Protein.acronym, "
			+ "Crystal.crystalId, Crystal.spaceGroup, Crystal.cell_a, Crystal.cell_b, Crystal.cell_c, "
			+ "Crystal.cell_alpha, Crystal.cell_beta, Crystal.cell_gamma, "
			+ "Crystal.diffractionPlanId as CrystalDiffractionPlanId, Container.sampleChangerLocation  "
			+ "FROM BLSample, Crystal, Protein,Container "
			+ "WHERE BLSample.crystalId=Crystal.crystalId AND "
			+ "Crystal.proteinId=Protein.proteinId AND "
			+ "BLSample.containerId=Container.containerId ";

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	/* Creation/Update methods ---------------------------------------------- */

	/**
	 * <p>
	 * Insert the given value object. TODO update this comment for insertion details.
	 * </p>
	 */
	public void create(BLSample3VO vo) throws Exception {
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
	}

	/**
	 * <p>
	 * Update the given value object. TODO update this comment for update details.
	 * </p>
	 */
	public BLSample3VO update(BLSample3VO vo) throws Exception {
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/* Deletion methods ----------------------------------------------------- */

	/**
	 * <p>
	 * Deletes the given value object.
	 * </p>
	 * 
	 * @param vo
	 *            the value object to delete.
	 */
	public void delete(BLSample3VO vo) {
		entityManager.remove(vo);
	}

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * Returns the BLSample3VO instance matching the given primary key.
	 * </p>
	 * <p>
	 * <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u> if the value object has to be
	 * used out the EJB container.
	 * </p>
	 * 
	 * @param pk
	 *            the primary key of the object to load.
	 * @param fetchRelation1
	 *            if true, the linked instances by the relation "relation1" will be set.
	 * @param fetchRelation2
	 *            if true, the linked instances by the relation "relation2" will be set.
	 */
	public BLSample3VO findByPk(Integer pk, boolean fetchEnergyScan,  boolean fetchSubSamples) {
		try {
			return (BLSample3VO) entityManager.createQuery(FIND_BY_PK(fetchEnergyScan, fetchSubSamples)).setParameter("pk", pk)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * <p>
	 * Returns the BLSample3VO instances.
	 * </p>
	 * <p>
	 * <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u> if the value object has to be
	 * used out the EJB container.
	 * </p>
	 * 
	 * @param fetchRelation1
	 *            if true, the linked instances by the relation "relation1" will be set.
	 * @param fetchRelation2
	 *            if true, the linked instances by the relation "relation2" will be set.
	 */
	@SuppressWarnings("unchecked")
	public List<BLSample3VO> findAll(boolean fetchEnergyScan,  boolean fetchSubSamples) {
		return entityManager.createQuery(FIND_ALL(fetchEnergyScan, fetchSubSamples)).getResultList();
	}

	public Session getSession() {
		return (Session) this.entityManager.getDelegate();

	}

	@SuppressWarnings("unchecked")
	public List<BLSample3VO> findByShippingId(Integer shippingId) {
		Session session = (Session) this.entityManager.getDelegate();
		return session.createCriteria(BLSample3VO.class).createCriteria("containerVO").createCriteria("dewarVO")
				.createCriteria("shippingVO").add(Restrictions.eq("shippingId", shippingId)).list();
	}

	@SuppressWarnings("unchecked")
	public List<BLSample3VO> findByContainerId(Integer containerId) {
		Session session = (Session) this.entityManager.getDelegate();
		return session.createCriteria(BLSample3VO.class).createCriteria("containerVO")
				.add(Restrictions.eq("containerId", containerId)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.list();
	}

	@SuppressWarnings("unchecked")
	public List<BLSample3VO> findByShippingDewarContainer(Integer shippingId, Integer dewarId, Integer containerId,
			String dmCode, Integer sortView) {

		Session session = (Session) this.entityManager.getDelegate();
		Criteria criteria = session.createCriteria(BLSample3VO.class);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		Criteria containerCriteria = criteria.createCriteria("containerVO");
		Criteria dewarCriteria = containerCriteria.createCriteria("dewarVO");

		List<BLSample3VO> ret = new ArrayList<BLSample3VO>();

		if (sortView == null) {
			sortView = 0;
		}

		if (!StringUtils.isEmpty(dmCode)) {
			criteria.add(Restrictions.like("code", dmCode));
		}

		if (containerId != null) {
			containerCriteria.add(Restrictions.eq("containerId", containerId));
			criteria.addOrder(new CastDecimalOrder("location", true));
			// ret = criteria.addOrder(Order.asc("location")).list();
			return criteria.list();
		}
		if (dewarId != null) {
			dewarCriteria.add(Restrictions.eq("dewarId", dewarId));

			if (sortView.equals(2)) {
				dewarCriteria.addOrder(Order.asc("code"));
				containerCriteria.addOrder(Order.asc("code"));
				// criteria.addOrder(Order.asc("location"));
				criteria.addOrder(new CastDecimalOrder("location", true));
			} else {
				Criteria proteinCriteria = session.createCriteria("crystalVO").createCriteria("proteinVO");
				proteinCriteria.addOrder(Order.asc("acronym"));
				criteria.addOrder(Order.asc("name"));
			}
			return criteria.list();
		}

		if (shippingId != null) {
			Criteria shippingCriteria = dewarCriteria.createCriteria("shippingVO");
			shippingCriteria.add(Restrictions.eq("shippingId", shippingId));
			if (sortView.equals(2)) {
				dewarCriteria.addOrder(Order.asc("code"));
				containerCriteria.addOrder(Order.asc("code"));
				// criteria.addOrder(Order.asc("location"));
				criteria.addOrder(new CastDecimalOrder("location", true));
			} else {
				Criteria proteinCriteria = session.createCriteria("crystalVO").createCriteria("proteinVO");
				proteinCriteria.addOrder(Order.asc("acronym"));
				criteria.addOrder(Order.asc("name"));
			}
			return criteria.list();
		}

		return ret;
	}

	@SuppressWarnings("unchecked")
	public List<BLSample3VO> findByProposalIdAndDewarNull(Integer proposalId) {
		Session session = (Session) this.entityManager.getDelegate();

		Criteria criteria = session.createCriteria(BLSample3VO.class);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !
		criteria.add(Restrictions.isNull("containerVO"));
		// Criteria subCritContainer = criteria.createCriteria("containerVO");
		// subCritContainer.add(Restrictions.isNull("dewarVO"));

		if (proposalId != null) {
			Criteria subCritCrystal = criteria.createCriteria("crystalVO");
			Criteria subCritProtein = subCritCrystal.createCriteria("proteinVO");
			Criteria subsubCritProposal = subCritProtein.createCriteria("proposalVO");
			subsubCritProposal.add(Restrictions.eq("proposalId", proposalId));
			// criteria.createAlias("proposalVO", "ps");
			// criteria.add(Restrictions.eq("ps.proposalId", proposalId));
		}

		return criteria.list();

	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<BLSample3VO> findFiltered(Integer proposalId, Integer proteinId, String acronym, Integer crystalId,
			String name, String code, String blSampleStatus, Byte isInSampleChanger, Integer shippingId) {

		Session session = (Session) this.entityManager.getDelegate();
		Criteria criteria = session.createCriteria(BLSample3VO.class);
		Criteria subCritCrystal = null;
		Criteria subCritProtein = null;
		Criteria subCritProposal = null;

		if (proposalId != null) {
			subCritCrystal = criteria.createCriteria("crystalVO");
			subCritProtein = subCritCrystal.createCriteria("proteinVO");
			subCritProposal = subCritProtein.createCriteria("proposalVO");
			subCritProposal.add(Restrictions.eq("proposalId", proposalId));
		}

		if (proteinId != null || acronym != null) {
			if (subCritCrystal == null)
				subCritCrystal = criteria.createCriteria("crystalVO");
			if (subCritProtein == null)
				subCritProtein = subCritCrystal.createCriteria("proteinVO");
			if (proteinId != null)
				subCritProtein.add(Restrictions.eq("proteinId", proteinId));
			if (acronym != null) {
				subCritProtein.add(Restrictions.ilike("acronym", acronym));
			}

		}

		if (crystalId != null) {
			if (subCritCrystal == null)
				subCritCrystal = criteria.createCriteria("crystalVO");
			subCritCrystal.add(Restrictions.eq("crystalId", crystalId));
		}

		if ((name != null) && (!name.isEmpty())) {
			criteria.add(Restrictions.like("name", name));
		}

		if ((code != null) && (!code.isEmpty())) {
			criteria.add(Restrictions.like("code", code));
		}

		if ((blSampleStatus != null) && (!blSampleStatus.isEmpty())) {
			criteria.add(Restrictions.eq("blSampleStatus", blSampleStatus));
		}

		if (isInSampleChanger != null) {
			criteria.add(Restrictions.eq("isInSampleChanger", isInSampleChanger));
		}
		if (shippingId != null) {
			Criteria subCritContainer = criteria.createCriteria("containerVO");
			Criteria subCritDewar = subCritContainer.createCriteria("dewarVO");
			Criteria subCritShipping = subCritDewar.createCriteria("shippingVO");
			subCritShipping.add(Restrictions.eq("shippingId", shippingId));
		}

		return criteria.list();

	}

	/**
	 * find all sample info (Tuple of BLSample, Container, Crystal, Protein, DiffractionPlan) for a specified proposal
	 * and a specified beamlineLocation and a given status(blSample or container)
	 * 
	 * @param proposalId
	 * @param beamlineLocation
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public List findSampleInfoForProposal(Integer proposalId, String beamlineLocation, String status) throws Exception {

		Query q = this.entityManager
				.createNativeQuery("SELECT BLSample.blSampleId, Crystal.crystalId, Protein.proteinId,  Container.containerId "
						+ "FROM BLSample, Crystal, Protein,Container "
						+ "WHERE BLSample.crystalId=Crystal.crystalId AND "
						+ "Crystal.proteinId=Protein.proteinId AND "
						+ "BLSample.containerId=Container.containerId AND "
						+ "Protein.proposalId = "
						+ proposalId
						+ " AND "
						+ "(Container.containerStatus LIKE '"
						+ status
						+ "' OR BLSample.blSampleStatus LIKE '"
						+ status
						+ "') AND "
						+ "(Container.beamlineLocation like '"
						+ beamlineLocation
						+ "' OR (Container.beamlineLocation IS NULL OR Container.beamlineLocation like ''))");
		List orders = q.getResultList();

		return orders;
	}

	/**
	 * find all sample info (Tuple of SampleInfo) for a specified proposal
	 * 
	 * @param proposalId
	 * @param beamlineLocation
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public List findSampleInfoLightForProposal(Integer proposalId)
			throws Exception {

		Query q = this.entityManager
				.createNativeQuery(SELECT_SAMPLE_INFO + " AND Protein.proposalId = " + proposalId);
		List orders = q.getResultList();

		return orders;
	}
	
	/**
	 * find all sample info (Tuple of SampleInfo) for a specified proposal and a specified beamlineLocation and a given
	 * status(blSample or container)
	 * 
	 * @param proposalId
	 * @param beamlineLocation
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public List findSampleInfoLightForProposal(Integer proposalId, String beamlineLocation, String status)
			throws Exception {

		Query q = this.entityManager
				.createNativeQuery(SELECT_SAMPLE_INFO + " AND Protein.proposalId = " + proposalId
						+ " AND " + "(Container.containerStatus LIKE '" + status
						+ "' OR BLSample.blSampleStatus LIKE '" + status + "') AND "
						+ "(Container.beamlineLocation like '" + beamlineLocation
						+ "' OR (Container.beamlineLocation IS NULL OR Container.beamlineLocation like ''))");
		List orders = q.getResultList();

		return orders;
	}
	
	/**
	 * find all sample info (Tuple of SampleInfo) for a specified crystalId
	 * 
	 * @param crystalId
	 * @return
	 * @throws Exception
	 */
	public List findSampleInfoLightForCrystalId(Integer crystalId)
			throws Exception {

		Query q = this.entityManager
				.createNativeQuery(SELECT_SAMPLE_INFO + " AND Crystal.crystalId = " + crystalId );
		List orders = q.getResultList();

		return orders;
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
	private void checkAndCompleteData(BLSample3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getBlSampleId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getBlSampleId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
	
	public List<BLSample3VO> findByShippingIdOrder(Integer shippingId, Integer sortView) {
		if (sortView == null) {
			sortView = 0;
		}

		String query = "SELECT  BLSample.blSampleId "
				+ "FROM BLSample, Crystal, Protein,Container, Dewar "
				+ "WHERE BLSample.crystalId=Crystal.crystalId AND "
				+ "Crystal.proteinId=Protein.proteinId AND "
				+ "BLSample.containerId=Container.containerId AND "
				+ "Container.dewarId = Dewar.dewarId AND "
				+ "Dewar.shippingId = " +shippingId +"  ";
		if (sortView.equals(2)) {
			query += "ORDER BY Dewar.code, Container.code, CAST( BLSample.location AS DECIMAL(3,0)) "	;
			//criteria.addOrder(new CastDecimalOrder("location", true));
		} else {
			query += "ORDER BY Protein.acronym, BLSample.name"	;
		}
				
		Query q = this.entityManager
				.createNativeQuery(query);
						
		List orders = q.getResultList();

		return orders;
		
		

	}

	
	public List findSampleInfo(Integer sampleId)
			throws Exception {

		Query q = this.entityManager
				.createNativeQuery(SELECT_SAMPLE_INFO + " AND BLSample.blSampleId = " + sampleId);
		List orders = q.getResultList();

		return orders;
	}
}
