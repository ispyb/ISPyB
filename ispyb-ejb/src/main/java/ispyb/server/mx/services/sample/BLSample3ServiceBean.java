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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
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

import ispyb.common.util.StringUtils;
import ispyb.server.biosaxs.services.sql.SQLQueryKeeper;
import ispyb.server.biosaxs.vos.dataAcquisition.Specimen3VO;
import ispyb.server.common.exceptions.AccessDeniedException;
import ispyb.server.common.services.shipping.Container3Service;
import ispyb.server.common.util.ejb.CastDecimalOrder;
import ispyb.server.common.util.ejb.EJBAccessCallback;
import ispyb.server.common.util.ejb.EJBAccessTemplate;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.BLSampleImage3VO;
import ispyb.server.mx.vos.sample.BLSampleInfo;
import ispyb.server.mx.vos.sample.BLSampleWS3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.DiffractionPlan3VO;
import ispyb.server.mx.vos.sample.DiffractionPlanWS3VO;
import ispyb.server.mx.vos.sample.Protein3VO;
import ispyb.server.mx.vos.sample.SampleInfo;

/**
 * <p>
 * This session bean handles ISPyB BLSample3.
 * </p>
 */
@Stateless
public class BLSample3ServiceBean implements BLSample3Service, BLSample3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(BLSample3ServiceBean.class);

	// Generic HQL request to find instances of BLSample3 by pk
	private static final String FIND_BY_PK(boolean fetchEnergyScan, boolean fetchSubSamples,
			boolean fetchSampleImages) {
		return "from BLSample3VO vo " + (fetchEnergyScan ? "left join fetch vo.energyScanVOs " : "")
				+ (fetchSubSamples ? "left join fetch vo.blSubSampleVOs " : "")
				+ (fetchSampleImages ? "left join fetch vo.blsampleImageVOs " : "")
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
			+ "Crystal.diffractionPlanId as CrystalDiffractionPlanId, "
			+ "Container.sampleChangerLocation, Container.code as containerCode, "
			+ "Container.dewarId as dewarId, "
			+ "Container.comments as containerComments, "
			+ "Dewar.code as dewarCode, "
			+ "Shipping.shippingName as shippingName, "
			+ "Shipping.comments as shippingComments, "
			+ "Container.capacity as containerCapacity, "
			+ "Container.containerType as containerType, "
			+ "Shipping.shippingId as shippingId "
			+ "FROM BLSample, Crystal, Protein,Container, Dewar, Shipping "
			+ "WHERE BLSample.crystalId=Crystal.crystalId AND "
			+ "Crystal.proteinId=Protein.proteinId AND "
			+ "BLSample.containerId=Container.containerId AND "
			+ "Dewar.dewarId=Container.dewarId AND "
			+ "Dewar.shippingId=Shipping.shippingId ";

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@Resource
	private SessionContext context;

	public BLSample3ServiceBean() {
	};

	/**
	 * Create new BLSample3.
	 * 
	 * @param vo
	 *           the entity to persist.
	 * @return the persisted entity.
	 */
	public BLSample3VO create(final BLSample3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the BLSample3 data.
	 * 
	 * @param vo
	 *           the entity data to update.
	 * @return the updated entity.
	 */
	public BLSample3VO update(final BLSample3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the BLSample3 from its pk
	 * 
	 * @param vo
	 *           the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		checkCreateChangeRemoveAccess();
		BLSample3VO vo = findByPk(pk, false, false, false);
		delete(vo);
	}

	/**
	 * Remove the BLSample3
	 * 
	 * @param vo
	 *           the entity to remove.
	 */
	public void delete(final BLSample3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if
	 * necessary
	 * 
	 * @param pk
	 *                  the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the BLSample3 value object
	 */
	public BLSample3VO findByPk(final Integer pk, final boolean withEnergyScan, final boolean withSubSamples,
			final boolean withSampleImages) throws Exception {

		checkCreateChangeRemoveAccess();
		try {
			return (BLSample3VO) entityManager.createQuery(FIND_BY_PK(withEnergyScan, withSubSamples, withSampleImages))
					.setParameter("pk", pk)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Find all BLSample3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<BLSample3VO> findAll(final boolean withEnergyScan, final boolean withSubSamples) throws Exception {

		List<BLSample3VO> foundEntities = entityManager.createQuery(FIND_ALL(withEnergyScan, withSubSamples))
				.getResultList();
		return foundEntities;
	}

	@SuppressWarnings("unchecked")
	public List<BLSample3VO> findByShippingDewarContainer(final Integer shippingId, final List<Integer> dewarIds,
			final Integer containerId, final String dmCode, Integer sortView) throws Exception {

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
			List<BLSample3VO> foundEntities = criteria.list();
			return foundEntities;
		}
		if (dewarIds != null) {
			dewarCriteria.add(Restrictions.in("dewarId", dewarIds));

			if (sortView.equals(2)) {
				dewarCriteria.addOrder(Order.asc("code"));
				containerCriteria.addOrder(Order.asc("code"));
				// criteria.addOrder(Order.asc("location"));
				criteria.addOrder(new CastDecimalOrder("location", true));
			} else {
				Criteria proteinCriteria = session.createCriteria("crystalVO").createCriteria("proteinVO");
				// criteria.addOrder(Order.asc("name"));
				proteinCriteria.addOrder(Order.asc("acronym"));
			}
			List<BLSample3VO> foundEntities = criteria.list();
			return foundEntities;
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
			List<BLSample3VO> foundEntities = criteria.list();
			return foundEntities;
		}

		return ret;
	}

	public List<BLSample3VO> findByShippingId(final Integer shippingId, final Integer sortView) throws Exception {
		return this.findByShippingDewarContainer(shippingId, null, null, null, sortView);
	}

	@SuppressWarnings("rawtypes")
	public List<BLSample3VO> findByShippingIdOrder(final Integer shippingId, final Integer sortView) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		List orders = (List) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				Session session = (Session) entityManager.getDelegate();
				List foundIds = session.createCriteria(BLSample3VO.class).createCriteria("containerVO")
						.createCriteria("dewarVO")
						.createCriteria("shippingVO").add(Restrictions.eq("shippingId", shippingId)).list();
				return foundIds;
			}
		});

		List<BLSample3VO> sampleList = null;
		int nb = orders.size();
		if (nb > 0)
			sampleList = new ArrayList<BLSample3VO>();

		for (int i = 0; i < nb; i++) {
			BLSample3VO blSample = new BLSample3VO();
			Integer blSampleId = (Integer) orders.get(i);
			// load VOs
			blSample = this.findByPk(blSampleId, false, false, false);
			sampleList.add(blSample);
		}
		return sampleList;
	}

	public List<BLSample3VO> findByDewarId(final List<Integer> dewarIds, final Integer sortView) throws Exception {
		return this.findByShippingDewarContainer(null, dewarIds, null, null, sortView);

	}

	public List<BLSample3VO> findByDewarId(final Integer dewarId, final Integer sortView) throws Exception {
		List<Integer> dewarIds = Collections.singletonList(dewarId);
		return this.findByShippingDewarContainer(null, dewarIds, null, null, sortView);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BLSample3VO> findByContainerId(final Integer containerId) throws Exception {
		Session session = (Session) this.entityManager.getDelegate();
		return session.createCriteria(BLSample3VO.class).createCriteria("containerVO")
				.add(Restrictions.eq("containerId", containerId)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.list();
	}

	public List<BLSample3VO> findByCodeAndShippingId(final String dmCode, final Integer shippingId) throws Exception {
		return this.findByShippingDewarContainer(shippingId, null, null, dmCode, null);
	}

	/**
	 * find the sample info (Tuple of BLSample, Container, Crystal, Protein,
	 * DiffractionPlan) for a given blsampleId
	 * 
	 * @param proposalId
	 * @param beamlineLocation
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public BLSampleInfo findSampleInfoForPk(final Integer blSampleId) throws Exception {

		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

		Crystal3Service crystalService = (Crystal3Service) ejb3ServiceLocator.getLocalService(Crystal3Service.class);
		Container3Service containerService = (Container3Service) ejb3ServiceLocator
				.getLocalService(Container3Service.class);
		Protein3Service proteinService = (Protein3Service) ejb3ServiceLocator.getLocalService(Protein3Service.class);
		DiffractionPlan3Service diffractionPlanService = (DiffractionPlan3Service) ejb3ServiceLocator
				.getLocalService(DiffractionPlan3Service.class);

		BLSample3VO blSample = new BLSample3VO();
		BLSampleWS3VO wsSample = new BLSampleWS3VO();
		Container3VO container = new Container3VO();
		Crystal3VO crystal = new Crystal3VO();
		Protein3VO protein = new Protein3VO();
		DiffractionPlan3VO diffractionPlan = new DiffractionPlan3VO();

		// sample
		blSample = this.findByPk(blSampleId, false, false, true);
		wsSample = getWSBLSampleVO(blSample);

		// crystal
		if (blSample != null) {
			Integer crystalId = blSample.getCrystalVOId();
			crystal = crystalService.findByPk(crystalId, false);
		}
		// protein
		if (crystal != null) {
			Integer proteinId = crystal.getProteinVOId();
			protein = proteinService.findByPk(proteinId, false);
		}
		// Integer diffractionPlanId = (Integer) o[3];
		if (blSample != null) {
			Integer containerId = blSample.getContainerVOId();
			container = containerService.findByPk(containerId, false);
		}
		if (blSample != null && blSample.getDiffractionPlanVOId() != null) {
			diffractionPlan = diffractionPlanService.findByPk(blSample.getDiffractionPlanVOId(), false, false);
		} else if (crystal != null && crystal.getDiffractionPlanVOId() != null)
			diffractionPlan = diffractionPlanService.findByPk(crystal.getDiffractionPlanVOId(), false, false);

		BLSampleInfo sampleInfo = new BLSampleInfo(wsSample, protein, crystal, diffractionPlan, container);

		return getWSBLSampleInfoVO(sampleInfo);

	}

	/**
	 * find all sample info (Tuple of BLSample, Container, Crystal, Protein,
	 * DiffractionPlan) for a specified proposal
	 * and a specified beamlineLocation and a given status(blSample or container)
	 * 
	 * @param proposalId
	 * @param beamlineLocation
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public BLSampleInfo[] findSampleInfoForProposal(final Integer proposalId, final String beamlineLocation,
			final String status) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		List orders = (List) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				Query q = entityManager
						.createNativeQuery(
								"SELECT BLSample.blSampleId, Crystal.crystalId, Protein.proteinId,  Container.containerId "
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
		});

		List<BLSampleInfo> listVOs = null;
		int nb = orders.size();
		if (nb > 0)
			listVOs = new ArrayList<BLSampleInfo>();

		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

		Crystal3Service crystalService = (Crystal3Service) ejb3ServiceLocator.getLocalService(Crystal3Service.class);
		Container3Service containerService = (Container3Service) ejb3ServiceLocator
				.getLocalService(Container3Service.class);
		Protein3Service proteinService = (Protein3Service) ejb3ServiceLocator.getLocalService(Protein3Service.class);
		DiffractionPlan3Service diffractionPlanService = (DiffractionPlan3Service) ejb3ServiceLocator
				.getLocalService(DiffractionPlan3Service.class);

		for (int i = 0; i < nb; i++) {
			BLSample3VO blSample = new BLSample3VO();
			BLSampleWS3VO wsSample = new BLSampleWS3VO();
			Container3VO container = new Container3VO();
			Crystal3VO crystal = new Crystal3VO();
			Protein3VO protein = new Protein3VO();
			DiffractionPlan3VO diffractionPlan = new DiffractionPlan3VO();
			Object[] o = (Object[]) orders.get(i);
			Integer blSampleId = (Integer) o[0];
			Integer crystalId = (Integer) o[1];
			Integer proteinId = (Integer) o[2];
			// Integer diffractionPlanId = (Integer) o[3];
			Integer containerId = (Integer) o[3];

			// load VOs
			blSample = this.findByPk(blSampleId, false, false, true);
			String blSampleImage = getSampleImagepath(blSampleId);
			wsSample = getWSBLSampleVO(blSample);

			container = containerService.findByPk(containerId, false);

			crystal = crystalService.findByPk(crystalId, false);

			protein = proteinService.findByPk(proteinId, false);

			if (blSample.getDiffractionPlanVOId() != null) {
				diffractionPlan = diffractionPlanService.findByPk(blSample.getDiffractionPlanVOId(), false, false);
			} else if (crystal.getDiffractionPlanVOId() != null)
				diffractionPlan = diffractionPlanService.findByPk(crystal.getDiffractionPlanVOId(), false, false);

			BLSampleInfo sampleInfo = new BLSampleInfo(wsSample, protein, crystal, diffractionPlan, container);
			listVOs.add(sampleInfo);
		}

		return getWSBLSampleInfoVOs(listVOs);

	}

	/**
	 * find all sample info (Tuple of SampleInfo) for a specified proposal
	 * and a specified beamlineLocation and a given status(blSample or container)
	 * 
	 * @param proposalId
	 * @param beamlineLocation
	 * @param status
	 * @param detachLight
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public SampleInfo[] findForWSSampleInfoLight(final Integer proposalId, final Integer crystalFormId,
			final String beamlineLocation,
			final String status) throws Exception {

		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		List orders = (List) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				List listInfo = new ArrayList<>();
				if (beamlineLocation == null && status == null && crystalFormId == null) {
					List o = entityManager
							.createNativeQuery(SELECT_SAMPLE_INFO + " AND Protein.proposalId = " + proposalId)
							.getResultList();
					listInfo = o;
				} else if (crystalFormId != null) {
					Query q = entityManager
							.createNativeQuery(SELECT_SAMPLE_INFO + " AND Crystal.crystalId = " + crystalFormId);
					List o = q.getResultList();
					listInfo = o;
				}

				else {
					Query q = entityManager
							.createNativeQuery(SELECT_SAMPLE_INFO + " AND Protein.proposalId = " + proposalId
									+ " AND " + "(Container.containerStatus LIKE '" + status
									+ "' OR BLSample.blSampleStatus LIKE '" + status + "') AND "
									+ "(Container.beamlineLocation like '" + beamlineLocation
									+ "' OR (Container.beamlineLocation IS NULL OR Container.beamlineLocation like ''))");

					System.out.println(SELECT_SAMPLE_INFO + " AND Protein.proposalId = " + proposalId
							+ " AND " + "(Container.containerStatus LIKE '" + status
							+ "' OR BLSample.blSampleStatus LIKE '" + status + "') AND "
							+ "(Container.beamlineLocation like '" + beamlineLocation
							+ "' OR (Container.beamlineLocation IS NULL OR Container.beamlineLocation like ''))");
					List o = q.getResultList();
					listInfo = o;
				}
				return listInfo;
			}
		});

		List<SampleInfo> listVOs = null;
		int nb = orders.size();

		if (nb > 0)
			listVOs = new ArrayList<SampleInfo>();

		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		DiffractionPlan3Service diffractionPlanService = (DiffractionPlan3Service) ejb3ServiceLocator
				.getLocalService(DiffractionPlan3Service.class);

		for (int i = 0; i < nb; i++) {
			Object[] o = (Object[]) orders.get(i);
			int j = 0;
			Integer blSampleId = (Integer) o[j++];
			String sampleName = (String) o[j++];
			String sampleCode = (String) o[j++];
			Double holderLength = (Double) o[j++];
			String sampleLocation = (String) o[j++];
			String smiles = (String) o[j++];
			Integer sampleDiffractionPlanId = (Integer) o[j++];
			String proteinAcronym = (String) o[j++];
			Integer crystalId = (Integer) o[j++];
			String crystalSpaceGroup = (String) o[j++];
			Double cellA = (Double) o[j++];
			Double cellB = (Double) o[j++];
			Double cellC = (Double) o[j++];
			Double cellAlpha = (Double) o[j++];
			Double cellBeta = (Double) o[j++];
			Double cellGamma = (Double) o[j++];
			Integer crystalDiffractionPlanId = (Integer) o[j++];
			String containerSCLocation = (String) o[j++];
			String containerCode = (String) o[j++];

			DiffractionPlan3VO diffractionPlan = new DiffractionPlan3VO();

			String blSampleImage = getSampleImagepath(blSampleId);

			Integer dewarId = (Integer) o[j++];
			String containerComments = (String) o[j++];
			String dewarCode = (String) o[j++];
			String shippingName = (String) o[j++];
			String shippingComments = (String) o[j++];

			Integer containerCapacity = (Integer) o[j++];
			String containerType = (String) o[j++];
			Integer shippingId = (Integer) o[j++];

			if (sampleDiffractionPlanId != null) {
				diffractionPlan = diffractionPlanService.findByPk(sampleDiffractionPlanId, false, false);
			} else if (crystalDiffractionPlanId != null)
				diffractionPlan = diffractionPlanService.findByPk(crystalDiffractionPlanId, false, false);
			Double minimalResolution = diffractionPlan.getMinimalResolution();
			String experimentType = diffractionPlan.getExperimentKind();
			DiffractionPlanWS3VO diffPlanws = new DiffractionPlanWS3VO(diffractionPlan);
			SampleInfo sampleInfo = new SampleInfo(blSampleId, sampleName, sampleCode,
					holderLength, sampleLocation, smiles, proteinAcronym, crystalId,
					crystalSpaceGroup, cellA, cellB, cellC, cellAlpha, cellBeta, cellGamma, minimalResolution,
					experimentType, containerSCLocation, containerCode, diffPlanws, blSampleImage, dewarId,
					containerComments, dewarCode, shippingName, shippingComments, containerCapacity, containerType,
					shippingId);
			listVOs.add(sampleInfo);
		}
		if (listVOs == null)
			return null;
		SampleInfo[] tmpResults = new SampleInfo[listVOs.size()];
		return listVOs.toArray(tmpResults);
	}

	/**
	 * find only sample info for a specified proposal and a specified
	 * beamlineLocation and a given status(blSample or
	 * container)
	 * 
	 * @param proposalId
	 * @param beamlineLocation
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public BLSampleWS3VO[] findForWSSampleInfoForProposalLight(final Integer proposalId, final String beamlineLocation,
			final String status) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		List listIds = (List) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				Query q = entityManager
						.createNativeQuery(
								"SELECT BLSample.blSampleId, Crystal.crystalId, Protein.proteinId,  Container.containerId "
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
		});

		BLSampleWS3VO[] tab = null;
		int nb = listIds.size();
		if (nb > 0) {

			tab = new BLSampleWS3VO[nb];
			for (int i = 0; i < nb; i++) {
				Object[] o = (Object[]) listIds.get(i);
				Integer blSampleId = (Integer) o[0];
				// Integer crystalId = (Integer) o[1];
				// Integer proteinId = (Integer) o[2];
				// Integer containerId = (Integer) o[3];
				BLSample3VO blSample = this.findByPk(blSampleId, false, false, true);
				BLSampleWS3VO wsSample = getWSBLSampleVO(blSample);
				tab[i] = wsSample;
			}
		}

		return tab;

	}

	/**
	 * Check if user has access rights to create, change and remove BLSample3
	 * entities. If not set rollback only and
	 * throw AccessDeniedException
	 * 
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
	}

	/**
	 * Get all BLSample3 entity VOs from a collection of BLSample3 local entities.
	 * 
	 * @param localEntities
	 * @return
	 */
	@SuppressWarnings({ "unused" })
	private BLSample3VO[] getBLSample3VOs(List<BLSample3VO> entities) {
		ArrayList<BLSample3VO> results = new ArrayList<BLSample3VO>(entities.size());
		for (BLSample3VO vo : entities) {
			results.add(vo);
		}
		BLSample3VO[] tmpResults = new BLSample3VO[results.size()];
		return results.toArray(tmpResults);
	}

	/**
	 * find all sample info (Tuple of BLSample, Container, Crytsal, Protein,
	 * DiffractionPlan) for a specified proposal
	 * and a specified beamlineLocation and a given status(blSample or container)
	 * 
	 * @param proposalId
	 * @param beamlineLocation
	 * @param status
	 * @param detachLight
	 * @return
	 * @throws Exception
	 */
	public BLSampleInfo[] findForWSSampleInfoForProposal(final Integer proposalId, final String beamlineLocation,
			final String status) throws Exception {
		BLSampleInfo[] ret = this.findSampleInfoForProposal(proposalId, beamlineLocation, status);

		return ret;
	}

	/**
	 * Get all entity VOs from a collection of local entities.
	 * 
	 * @param localEntities
	 * @return
	 */
	@SuppressWarnings("unused")
	private BLSample3VO[] getBLSampleVOs(List<BLSample3VO> entities) {
		ArrayList<BLSample3VO> results = new ArrayList<BLSample3VO>(entities.size());
		for (BLSample3VO vo : entities) {
			results.add(vo);
		}
		BLSample3VO[] tmpResults = new BLSample3VO[results.size()];
		return results.toArray(tmpResults);
	}

	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	@SuppressWarnings({ "unused" })
	private BLSample3VO[] getLightBLSampleVOs(List<BLSample3VO> entities) throws CloneNotSupportedException {
		ArrayList<BLSample3VO> results = new ArrayList<BLSample3VO>(entities.size());
		for (BLSample3VO vo : entities) {
			BLSample3VO otherVO = getLightBLSampleVO(vo);
			results.add(otherVO);
		}
		BLSample3VO[] tmpResults = new BLSample3VO[results.size()];
		return results.toArray(tmpResults);
	}

	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private BLSample3VO getLightBLSampleVO(BLSample3VO vo) throws CloneNotSupportedException {
		BLSample3VO otherVO = (BLSample3VO) vo.clone();
		// otherVO.setEnergyScanVOs(null);
		return otherVO;
	}

	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private BLSampleInfo getLightBLSampleInfo(BLSampleInfo vo) throws CloneNotSupportedException {
		BLSampleInfo otherVO = (BLSampleInfo) vo.clone();
		otherVO.getContainer().setSampleVOs(null);
		otherVO.getCrystal().setSampleVOs(null);
		otherVO.getProtein().setCrystalVOs(null);
		// otherVO.getBlSample().setEnergyScanVOs(null);
		otherVO.getDiffractionPlan().setExperimentKindVOs(null);
		return otherVO;
	}

	/**
	 * Find a sample by its primary key -- webservices object
	 * 
	 * @param pk
	 * @param withLink1
	 * @param withLink2
	 * @return
	 * @throws Exception
	 */
	public BLSampleWS3VO findForWSByPk(final Integer pk, final boolean withEnergyScan, final boolean withSubSamples,
			final boolean withSampleImages) throws Exception {

		checkCreateChangeRemoveAccess();
		try {
			BLSample3VO found = (BLSample3VO) entityManager
					.createQuery(FIND_BY_PK(withEnergyScan, withSubSamples, withSampleImages)).setParameter("pk", pk)
					.getSingleResult();
			BLSampleWS3VO sampleLight = getWSBLSampleVO(found);
			return sampleLight;
		} catch (NoResultException e) {
			return null;
		}
	}

	private BLSampleWS3VO getWSBLSampleVO(BLSample3VO vo) throws CloneNotSupportedException {
		if (vo == null)
			return null;
		BLSample3VO otherVO = getLightBLSampleVO(vo);
		Integer crystalId = null;
		Integer containerId = null;
		crystalId = otherVO.getCrystalVOId();
		containerId = otherVO.getContainerVOId();
		otherVO.setCrystalVO(null);
		otherVO.setContainerVO(null);
		BLSampleWS3VO wsSample = new BLSampleWS3VO(otherVO);
		wsSample.setCrystalId(crystalId);
		wsSample.setContainerId(containerId);
		return wsSample;
	}

	private BLSampleInfo[] getWSBLSampleInfoVOs(List<BLSampleInfo> entities) throws CloneNotSupportedException {
		if (entities == null)
			return null;
		ArrayList<BLSampleInfo> results = new ArrayList<BLSampleInfo>(entities.size());
		for (BLSampleInfo vo : entities) {
			BLSampleInfo otherVO = getWSBLSampleInfoVO(vo);
			results.add(otherVO);
		}
		BLSampleInfo[] tmpResults = new BLSampleInfo[results.size()];
		return results.toArray(tmpResults);
	}

	private BLSampleInfo getWSBLSampleInfoVO(BLSampleInfo vo) throws CloneNotSupportedException {
		BLSampleInfo otherVO = getLightBLSampleInfo(vo);
		otherVO.getBlSample().setCrystalVO(null);
		otherVO.getContainer().setDewarVO(null);
		otherVO.getCrystal().setProteinVO(null);
		otherVO.getProtein().setProposalVO(null);
		return otherVO;
	}

	public BLSample3VO loadEager(BLSample3VO vo) throws Exception {
		BLSample3VO newVO = this.findByPk(vo.getBlSampleId(), true, true, true);
		return newVO;
	}

	@SuppressWarnings("unchecked")
	public List<BLSample3VO> findByProposalIdAndDewarNull(final Integer proposalId) throws Exception {

		checkCreateChangeRemoveAccess();
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

	@SuppressWarnings("unchecked")
	@Override
	public List<BLSample3VO> findFiltered(final Integer proposalId, final Integer proteinId, final String acronym,
			final Integer crystalId, final String name, final String code, final String status,
			final Byte isInSampleChanger, final Integer shippingId, final String sortType) throws Exception {

		checkCreateChangeRemoveAccess();
		Session session = (Session) this.entityManager.getDelegate();
		Criteria criteria = session.createCriteria(BLSample3VO.class);
		Criteria subCritCrystal = null;
		Criteria subCritProtein = null;
		Criteria subCritProposal = null;
		Criteria subCritContainer = null;
		Criteria subCritDewar = null;

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
			System.out.println("Addid restriction Name: " + name + " proposalId: " + proposalId);
			criteria.add(Restrictions.like("name", name));
		}

		if ((code != null) && (!code.isEmpty())) {
			criteria.add(Restrictions.like("code", code));
		}

		if ((status != null) && (!status.isEmpty())) {
			criteria.add(Restrictions.eq("blSampleStatus", status));
		}

		if (isInSampleChanger != null) {
			criteria.add(Restrictions.eq("isInSampleChanger", isInSampleChanger));
		}
		if (shippingId != null) {
			subCritContainer = criteria.createCriteria("containerVO");
			subCritDewar = subCritContainer.createCriteria("dewarVO");
			Criteria subCritShipping = subCritDewar.createCriteria("shippingVO");
			subCritShipping.add(Restrictions.eq("shippingId", shippingId));
		}

		if (sortType != null) {
			if (sortType.equals("container")) {
				subCritContainer = criteria.createCriteria("containerVO");
				subCritDewar = subCritContainer.createCriteria("dewarVO");
				subCritDewar.addOrder(Order.asc("code"));
				subCritContainer.addOrder(Order.asc("code"));
			} else if (sortType.equals("name")) {
				subCritProtein.addOrder(Order.asc("acronym"));
				criteria.addOrder(Order.asc("name"));
			}
		}

		return criteria.list();
	}

	public List<BLSample3VO> findByCrystalNameCode(final Integer crystalId, final String name, final String code)
			throws Exception {

		return this.findFiltered(null, null, null, crystalId, name, code, null, null, null, null);

	}

	public List<BLSample3VO> findByProposalIdAndBlSampleStatus(final Integer proposalId, final String status)
			throws Exception {
		return this.findFiltered(proposalId, null, null, null, null, null, status, null, null, null);

	}

	public List<BLSample3VO> findByProposalId(final Integer proposalId) throws Exception {
		return this.findFiltered(proposalId, null, null, null, null, null, null, null, null, null);

	}

	public List<BLSample3VO> findByNameAndCodeAndProposalId(final String name, final String code,
			final Integer proposalId) throws Exception {
		return this.findFiltered(proposalId, null, null, null, name, code, null, null, null, null);

	}

	@Override
	public List<BLSample3VO> findByCrystalId(Integer crystalId) throws Exception {
		return this.findFiltered(null, null, null, crystalId, null, null, null, null, null, null);
	}

	@Override
	public List<BLSample3VO> findByName(String name) throws Exception {
		return this.findFiltered(null, null, null, null, name, null, null, null, null, null);
	}

	@Override
	public List<BLSample3VO> findByCode(String code) throws Exception {
		return this.findFiltered(null, null, null, null, null, code, null, null, null, null);
	}

	@Override
	public List<BLSample3VO> findByStatus(String status) throws Exception {
		return this.findFiltered(null, null, null, null, null, null, status, null, null, null);
	}

	public List<BLSample3VO> findByProteinId(final Integer proteinId) throws Exception {
		return this.findFiltered(null, proteinId, null, null, null, null, null, null, null, null);
	}

	public List<BLSample3VO> findByProposalIdAndIsInSampleChanger(final Integer proposalId,
			final Byte isInSampleChanger)
			throws Exception {
		return this.findFiltered(proposalId, null, null, null, null, null, null, isInSampleChanger, null, null);
	}

	public List<BLSample3VO> findByAcronymAndProposalId(final String name, final Integer proposalId,
			final String sortType)
			throws Exception {
		LOG.info("Name: " + name + " proposalId: " + proposalId);
		return this.findFiltered(proposalId, null, null, null, name, null, null, null, null, sortType);
	}

	public List<BLSample3VO> findByNameAndProteinId(final String name, final Integer proteinId,
			final Integer shippingId)
			throws Exception {

		return this.findFiltered(null, proteinId, null, null, name, null, null, null, shippingId, null);

	}

	public List<BLSample3VO> findByCodeAndProposalId(final String dmCode, final Integer proposalId) throws Exception {
		return this.findFiltered(proposalId, null, null, null, null, dmCode, null, null, null, null);

	}

	public void resetIsInSampleChanger(final Integer proposalId) throws Exception {
		List<BLSample3VO> list = this.findByProposalId(proposalId);
		for (BLSample3VO vo : list) {
			vo.setIsInSampleChanger(new Byte((new Integer(0)).byteValue()));
			this.update(vo);
		}

	}

	@SuppressWarnings("rawtypes")
	public SampleInfo findForWSSampleInfo(final Integer sampleId) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		List orders = (List) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				Query q = entityManager
						.createNativeQuery(SELECT_SAMPLE_INFO + " AND BLSample.blSampleId = " + sampleId);
				List orders = q.getResultList();
				return orders;
			}
		});

		SampleInfo sampleInfo = null;

		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		DiffractionPlan3Service diffractionPlanService = (DiffractionPlan3Service) ejb3ServiceLocator
				.getLocalService(DiffractionPlan3Service.class);

		if (orders != null && orders.size() > 0) {
			Object[] o = (Object[]) orders.get(0);
			int j = 0;
			Integer blSampleId = (Integer) o[j++];
			String sampleName = (String) o[j++];
			String sampleCode = (String) o[j++];
			Double holderLength = (Double) o[j++];
			String sampleLocation = (String) o[j++];
			String smiles = (String) o[j++];
			Integer sampleDiffractionPlanId = (Integer) o[j++];
			String proteinAcronym = (String) o[j++];
			Integer crystalId = (Integer) o[j++];
			String crystalSpaceGroup = (String) o[j++];
			Double cellA = (Double) o[j++];
			Double cellB = (Double) o[j++];
			Double cellC = (Double) o[j++];
			Double cellAlpha = (Double) o[j++];
			Double cellBeta = (Double) o[j++];
			Double cellGamma = (Double) o[j++];
			Integer crystalDiffractionPlanId = (Integer) o[j++];
			String containerSCLocation = (String) o[j++];
			String containerCode = (String) o[j++];
			DiffractionPlan3VO diffractionPlan = new DiffractionPlan3VO();
			String blSampleImage = getSampleImagepath(blSampleId);

			if (sampleDiffractionPlanId != null) {
				diffractionPlan = diffractionPlanService.findByPk(sampleDiffractionPlanId, false, false);
			} else if (crystalDiffractionPlanId != null)
				diffractionPlan = diffractionPlanService.findByPk(crystalDiffractionPlanId, false, false);
			Double minimalResolution = diffractionPlan.getMinimalResolution();
			String experimentType = diffractionPlan.getExperimentKind();
			DiffractionPlanWS3VO diffPlanws = new DiffractionPlanWS3VO(diffractionPlan);
			sampleInfo = new SampleInfo(blSampleId, sampleName, sampleCode,
					holderLength, sampleLocation, smiles, proteinAcronym, crystalId,
					crystalSpaceGroup, cellA, cellB, cellC, cellAlpha, cellBeta, cellGamma, minimalResolution,
					experimentType, containerSCLocation, containerCode, diffPlanws, blSampleImage);
		}
		return sampleInfo;
	}

	/* Private methods ------------------------------------------------------ */

	private String getSampleImagepath(Integer blSampleId) throws Exception {

		// we suppose that only 1 image is created for now for 1 BLSample
		Set<BLSampleImage3VO> blsampleImageVOs = this.findByPk(blSampleId, false, false, true).getBlsampleImageVOs();
		if (blsampleImageVOs != null && !blsampleImageVOs.isEmpty()) {
			BLSampleImage3VO vo = (BLSampleImage3VO) blsampleImageVOs.toArray()[0];
			return vo.getImageFullPath();
		} else {
			return null;
		}
	}

	/**
	 * Checks the data for integrity. E.g. if references and categories exist.
	 * 
	 * @param vo
	 *               the data to check
	 * @param create
	 *               should be true if the value object is just being created in the
	 *               DB, this avoids some checks like
	 *               testing the primary key
	 * @exception VOValidateException
	 *                                if data is not correct
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

}
