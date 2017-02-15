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

import ispyb.server.common.services.shipping.Container3Service;
import ispyb.server.common.util.ejb.EJBAccessCallback;
import ispyb.server.common.util.ejb.EJBAccessTemplate;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.mx.daos.sample.BLSample3DAO;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.BLSampleInfo;
import ispyb.server.mx.vos.sample.BLSampleWS3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.DiffractionPlan3VO;
import ispyb.server.mx.vos.sample.DiffractionPlanWS3VO;
import ispyb.server.mx.vos.sample.Protein3VO;
import ispyb.server.mx.vos.sample.SampleInfo;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

/**
 * <p>
 * This session bean handles ISPyB BLSample3.
 * </p>
 */
@Stateless
public class BLSample3ServiceBean implements BLSample3Service, BLSample3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(BLSample3ServiceBean.class);

	@EJB
	private BLSample3DAO dao;

	@Resource
	private SessionContext context;

	public BLSample3ServiceBean() {
	};

	/**
	 * Create new BLSample3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public BLSample3VO create(final BLSample3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (BLSample3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				dao.create(vo);
				return vo;
			}

		});
	}

	/**
	 * Update the BLSample3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public BLSample3VO update(final BLSample3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (BLSample3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				return dao.update(vo);
			}

		});
	}

	/**
	 * Remove the BLSample3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				BLSample3VO vo = findByPk(pk, false, false);
				delete(vo);
				return vo;
			}

		});

	}

	/**
	 * Remove the BLSample3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final BLSample3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
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
	 * @return the BLSample3 value object
	 */
	public BLSample3VO findByPk(final Integer pk, final boolean withEnergyScan, final boolean withSubSamples) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (BLSample3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				BLSample3VO found = dao.findByPk(pk, withEnergyScan, withSubSamples);
				return found;
			}

		});
	}

	/**
	 * Find all BLSample3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<BLSample3VO> findAll(final boolean withEnergyScan, final boolean withSubSamples) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<BLSample3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<BLSample3VO> foundEntities = dao.findAll(withEnergyScan, withSubSamples);
				return foundEntities;
			}

		});
	}

	@SuppressWarnings("unchecked")
	public List<BLSample3VO> findByShippingDewarContainer(final Integer shippingId, final Integer dewarId,
			final Integer containerId, final String dmCode, final Integer sortView) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<BLSample3VO>) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				List<BLSample3VO> foundEntities = dao.findByShippingDewarContainer(shippingId, dewarId, containerId,
						dmCode, sortView);
				return foundEntities;
			}
		});
	}

	public List<BLSample3VO> findByShippingId(final Integer shippingId, final Integer sortView) throws Exception {
		return this.findByShippingDewarContainer(shippingId, null, null, null, sortView);
	}
	
	@SuppressWarnings("rawtypes")
	public List<BLSample3VO> findByShippingIdOrder(final Integer shippingId, final Integer sortView) throws Exception{
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		List orders = (List) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				List foundIds = dao.findByShippingIdOrder(shippingId,  sortView);
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
			blSample = this.findByPk(blSampleId, false, false);
			sampleList.add(blSample);
		}
		return sampleList;
	}

	// @SuppressWarnings("unchecked")
	// public List<BLSample3VO> findByShippingId(final Integer shippingId) throws Exception {
	// EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
	// return (List<BLSample3VO>) template.execute(new EJBAccessCallback() {
	// public Object doInEJBAccess(Object parent) throws Exception {
	// List<BLSample3VO> foundEntities = dao.findByShippingDewarContainer(shippingId);
	// return foundEntities;
	// }
	// });
	// }

	public List<BLSample3VO> findByDewarId(final Integer dewarId, final Integer sortView) throws Exception {
		return this.findByShippingDewarContainer(null, dewarId, null, null, sortView);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BLSample3VO> findByContainerId(final Integer containerId) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<BLSample3VO>) template.execute(new EJBAccessCallback() {

			public List<BLSample3VO> doInEJBAccess(Object parent) throws Exception {
				return dao.findByContainerId(containerId);
			}

		});
	}

	public List<BLSample3VO> findByCodeAndShippingId(final String dmCode, final Integer shippingId) throws Exception {
		return this.findByShippingDewarContainer(shippingId, null, null, dmCode, null);
	}

	/**
	 * find the sample info (Tuple of BLSample, Container, Crystal, Protein, DiffractionPlan) for a given blsampleId
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
		blSample = this.findByPk(blSampleId, false, false);
		wsSample = getWSBLSampleVO(blSample);

		// crystal
		if (blSample != null){
			Integer crystalId = blSample.getCrystalVOId();
			crystal = crystalService.findByPk(crystalId, false);
		}
		// protein
		if (crystal != null){
			Integer proteinId = crystal.getProteinVOId();
			protein = proteinService.findByPk(proteinId, false);
		}
		// Integer diffractionPlanId = (Integer) o[3];
		if (blSample != null){
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
	 * find all sample info (Tuple of BLSample, Container, Crystal, Protein, DiffractionPlan) for a specified proposal
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
				List foundIds = dao.findSampleInfoForProposal(proposalId, beamlineLocation, status);
				return foundIds;
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
			blSample = this.findByPk(blSampleId, false, false);
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
	public SampleInfo[] findForWSSampleInfoLight(final Integer proposalId, final Integer crystalFormId, final String beamlineLocation,
			final String status) throws Exception{
		
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		List orders = (List) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				List listInfo= new ArrayList<>();
				if (beamlineLocation == null && status == null && crystalFormId == null)
					listInfo = dao.findSampleInfoLightForProposal(proposalId);
				else if (crystalFormId != null)
					listInfo = dao.findSampleInfoLightForCrystalId(crystalFormId);
				else
					listInfo = dao.findSampleInfoLightForProposal(proposalId, beamlineLocation, status);
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
			int j=0;
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
			DiffractionPlan3VO diffractionPlan = new DiffractionPlan3VO();
			
			if (sampleDiffractionPlanId != null) {
				diffractionPlan = diffractionPlanService.findByPk(sampleDiffractionPlanId, false, false);
			} else if (crystalDiffractionPlanId != null)
				diffractionPlan = diffractionPlanService.findByPk(crystalDiffractionPlanId, false, false);
			Double minimalResolution = diffractionPlan.getMinimalResolution();
			String experimentType = diffractionPlan.getExperimentKind();
			DiffractionPlanWS3VO diffPlanws = new DiffractionPlanWS3VO(diffractionPlan);
			SampleInfo sampleInfo = new SampleInfo(blSampleId, sampleName, sampleCode,
					 holderLength,  sampleLocation,  smiles, proteinAcronym, crystalId,
					 crystalSpaceGroup, cellA, cellB, cellC, cellAlpha, cellBeta, cellGamma, minimalResolution,
					 experimentType, containerSCLocation, diffPlanws) ;
			listVOs.add(sampleInfo);
		}
		if (listVOs == null)
			return null;
		SampleInfo[] tmpResults = new SampleInfo[listVOs.size()];
		return listVOs.toArray(tmpResults);
	}

	/**
	 * find only sample info for a specified proposal and a specified beamlineLocation and a given status(blSample or
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
				List foundIds = dao.findSampleInfoForProposal(proposalId, beamlineLocation, status);
				return foundIds;
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
				BLSample3VO blSample = this.findByPk(blSampleId, false, false);
				BLSampleWS3VO wsSample = getWSBLSampleVO(blSample);
				tab[i] = wsSample;
			}
		}

		return tab;

	}

	/**
	 * Check if user has access rights to create, change and remove BLSample3 entities. If not set rollback only and
	 * throw AccessDeniedException
	 * 
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
				// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);
				// to the one checking the needed access rights
				// autService.checkUserRightToChangeAdminData();
				return null;
			}

		});
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
	 * find all sample info (Tuple of BLSample, Container, Crytsal, Protein, DiffractionPlan) for a specified proposal
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
		//otherVO.setEnergyScanVOs(null);
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
		//otherVO.getBlSample().setEnergyScanVOs(null);
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
	public BLSampleWS3VO findForWSByPk(final Integer pk, final boolean withEnergyScan, final boolean withSubSamples) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (BLSampleWS3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				BLSample3VO found = dao.findByPk(pk, withEnergyScan, withSubSamples);
				BLSampleWS3VO sampleLight = getWSBLSampleVO(found);
				return sampleLight;
			}

		});
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
		BLSample3VO newVO = this.findByPk(vo.getBlSampleId(), true, true);
		return newVO;
	}

	@SuppressWarnings("unchecked")
	public List<BLSample3VO> findByProposalIdAndDewarNull(final Integer proposalId) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<BLSample3VO>) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				return dao.findByProposalIdAndDewarNull(proposalId);
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BLSample3VO> findFiltered(final Integer proposalId, final Integer proteinId, final String acronym,
			final Integer crystalId, final String name, final String code, final String status,
			final Byte isInSampleChanger, final Integer shippingId, final String sortType) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<BLSample3VO>) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				return dao.findFiltered(proposalId, proteinId, acronym, crystalId, name, code, status,
						isInSampleChanger, shippingId, sortType);
			}
		});
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

	public List<BLSample3VO> findByProposalIdAndIsInSampleChanger(final Integer proposalId, final Byte isInSampleChanger)
			throws Exception {
		return this.findFiltered(proposalId, null, null, null, null, null, null, isInSampleChanger, null, null);
	}

	public List<BLSample3VO> findByAcronymAndProposalId(final String acronym, final Integer proposalId, final String sortType)
			throws Exception {
		return this.findFiltered(proposalId, null, acronym, null, null, null, null, null, null, sortType);
	}

	public List<BLSample3VO> findByNameAndProteinId(final String name, final Integer proteinId, final Integer shippingId)
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
				List listInfo = dao.findSampleInfo(sampleId);
				return listInfo;
			}
		});

		SampleInfo sampleInfo = null;

		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		DiffractionPlan3Service diffractionPlanService = (DiffractionPlan3Service) ejb3ServiceLocator
		.getLocalService(DiffractionPlan3Service.class);

		if (orders != null && orders.size() > 0){
			Object[] o = (Object[]) orders.get(0);
			int j=0;
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
			DiffractionPlan3VO diffractionPlan = new DiffractionPlan3VO();
			
			if (sampleDiffractionPlanId != null) {
				diffractionPlan = diffractionPlanService.findByPk(sampleDiffractionPlanId, false, false);
			} else if (crystalDiffractionPlanId != null)
				diffractionPlan = diffractionPlanService.findByPk(crystalDiffractionPlanId, false, false);
			Double minimalResolution = diffractionPlan.getMinimalResolution();
			String experimentType = diffractionPlan.getExperimentKind();
			DiffractionPlanWS3VO diffPlanws = new DiffractionPlanWS3VO(diffractionPlan);
			sampleInfo = new SampleInfo(blSampleId, sampleName, sampleCode,
						 holderLength,  sampleLocation,  smiles, proteinAcronym, crystalId,
						 crystalSpaceGroup, cellA, cellB, cellC, cellAlpha, cellBeta, cellGamma, minimalResolution,
						 experimentType, containerSCLocation, diffPlanws) ;
		}
		return sampleInfo ;
	}
}