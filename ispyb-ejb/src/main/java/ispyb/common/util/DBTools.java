/*******************************************************************************
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
 ******************************************************************************/
/*
 * DBTools.java
 * @author ludovic.launer@esrf.fr
 * Feb 17, 2005
 */

package ispyb.common.util;

import ispyb.common.util.upload.ShippingInformation;
import ispyb.common.util.upload.ShippingInformation.DewarInformation;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.services.shipping.Container3Service;
import ispyb.server.common.services.shipping.Dewar3Service;
import ispyb.server.common.services.shipping.Shipping3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.server.mx.services.autoproc.GeometryClassname3Service;
import ispyb.server.mx.services.autoproc.SpaceGroup3Service;
import ispyb.server.mx.services.collections.Image3Service;
import ispyb.server.mx.services.sample.BLSample3Service;
import ispyb.server.mx.services.sample.Crystal3Service;
import ispyb.server.mx.services.sample.DiffractionPlan3Service;
import ispyb.server.mx.services.sample.Protein3Service;
import ispyb.server.mx.vos.autoproc.GeometryClassname3VO;
import ispyb.server.mx.vos.autoproc.SpaceGroup3VO;
import ispyb.server.mx.vos.collections.Image3VO;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.DiffractionPlan3VO;
import ispyb.server.mx.vos.sample.Protein3VO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.NamingException;

public class DBTools {

	private final static Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	/**
	 * Default Diffraction Plan
	 * 
	 * @return The default Diffraction Plan
	 * @throws Exception
	 */
	public static DiffractionPlan3VO GetDefaultDiffractionPlan() throws Exception {
		// DiffractionPlanValue diffractionPlan = null;

		DiffractionPlan3VO diffractionPlan = new DiffractionPlan3VO();
		// ---------------------------------------------------------------------------------------------------
		// Build Query
		DiffractionPlan3Service diffPlan = (DiffractionPlan3Service) ejb3ServiceLocator
				.getLocalService(DiffractionPlan3Service.class);
		diffractionPlan = diffPlan.findByPk(new Integer(Constants.DEFAULT_DIFFRACTION_PLAN_ID), false, false);
		return diffractionPlan;
	}

	/**
	 * getShippingInformation
	 * 
	 * @param shippingId
	 * @return The totalNumber of Baskets in the ShippingInformation
	 */
	public static ShippingInformation getShippingInformation(Integer shippingId) {
		int d, c, s;
		ShippingInformation shippingInformation = new ShippingInformation();

		try {
			Dewar3Service _dewar = (Dewar3Service) ejb3ServiceLocator.getLocalService(Dewar3Service.class);
			Container3Service _container = (Container3Service) ejb3ServiceLocator
					.getLocalService(Container3Service.class);
			BLSample3Service _sample = (BLSample3Service) ejb3ServiceLocator.getLocalService(BLSample3Service.class);
			Crystal3Service _crystal = (Crystal3Service) ejb3ServiceLocator.getLocalService(Crystal3Service.class);
			Protein3Service _protein = (Protein3Service) ejb3ServiceLocator.getLocalService(Protein3Service.class);

			// --- Shipping ---
			Shipping3VO shipping = getSelectedShipping(shippingId);
			Dewar3VO[] allDewars = shipping.getDewars();
			ArrayList<Dewar3VO> lstDewars = new ArrayList<Dewar3VO>();

			shippingInformation.setShipping(shipping);
			// --- Dewar : Remove non Dewars from List --------------------
			for (d = 0; d < allDewars.length; d++) {
				Dewar3VO ldewar = allDewars[d];
				if (ldewar.getType() == null || ldewar.getType().equals(Constants.PARCEL_DEWAR_TYPE))
					lstDewars.add(ldewar);
			}
			// -------------------------------------------------------------
			// --- Dewar ----------------------------------------------------------
			for (d = 0; d < lstDewars.size(); d++) {
				Dewar3VO ldewar = lstDewars.get(d);
				Dewar3VO dewar = _dewar.findByPk(ldewar.getDewarId(), true, true);
				Container3VO[] baskets = dewar.getContainers();

				ShippingInformation.DewarInformation dewarInformation = shippingInformation.new DewarInformation();
				dewarInformation.setDewar(dewar);
				// -- Baskets -------------------------------------------
				for (c = 0; c < baskets.length; c++) {
					Container3VO lcontainer = baskets[c];
					Container3VO container = _container.findByPk(lcontainer.getContainerId(), false);
					List samples = _sample.findByContainerId(lcontainer.getContainerId());

					ShippingInformation.DewarInformation.ContainerInformation containerInformation = dewarInformation.new ContainerInformation();
					containerInformation.setContainer(container);

					// --- Samples ----------------------------
					for (s = 0; s < samples.size(); s++) {
						BLSample3VO lsample = (BLSample3VO) samples.get(s);
						BLSample3VO sample = _sample.findByPk(lsample.getBlSampleId(), false, false, false);
						Crystal3VO crystal = _crystal.findByPk(sample.getCrystalVOId(), false);
						Protein3VO protein = _protein.findByPk(crystal.getProteinVOId(), false);

						ShippingInformation.DewarInformation.ContainerInformation.SampleInformation sampleInformation = containerInformation.new SampleInformation();
						sampleInformation.setSample(sample);
						sampleInformation.setCrystal(crystal);
						sampleInformation.setProtein(protein);
						containerInformation.getListSamples().add(sampleInformation);
					} // --- Samples
					dewarInformation.getListContainers().add(containerInformation);
				} // --- Baskets
				shippingInformation.getListDewars().add(dewarInformation);
			} // --- Dewars
		} catch (CreateException cex) {
			cex.printStackTrace();
		} catch (NamingException nex) {
			nex.printStackTrace();
		} catch (FinderException fex) {
			fex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return shippingInformation;
	}

	public static int GetNumberOfContainers(ShippingInformation shippingInformation) {
		int nbBaskets = 0;
		int d, c;
		int nbDewars = shippingInformation.getListDewars().size();

		for (d = 0; d < nbDewars; d++) {
			DewarInformation dewarInformation = shippingInformation.getListDewars().get(d);
			int nbContainers = dewarInformation.getListContainers().size();
			nbBaskets += nbContainers + 1;
		}

		return nbBaskets;
	}

	/**
	 * retrieves the proposal name on the selected proposal
	 * 
	 * @param proposalId
	 * @return
	 * @throws Exception
	 */
	public static String GetProposalName(Integer proposalId) throws Exception {
		String proposalName = "";
		try {
			Proposal3Service proposalService = (Proposal3Service) ejb3ServiceLocator
					.getLocalService(Proposal3Service.class);
			Proposal3VO selectedProposal = proposalService.findByPk(proposalId);
			proposalName = selectedProposal.getCode() + selectedProposal.getNumber();
		} catch (Exception e) {
			proposalName = "<Unknown proposalId '" + proposalId + "'>";
		}
		return proposalName;
	}

	/**
	 * Retrieves info on the selected Shipping
	 * 
	 * @param shippingId
	 * @return
	 * @throws Exception
	 */
	public static Shipping3VO getSelectedShipping(Integer shippingId) throws Exception {
		Shipping3Service service = (Shipping3Service) ejb3ServiceLocator.getLocalService(Shipping3Service.class);
		Shipping3VO selectedShipping = service.findByPk(shippingId, true);
		return selectedShipping;
	}

	/**
	 * Retrieves info on the selected Dewar
	 * 
	 * @param dewarId
	 * @return
	 * @throws Exception
	 */
	public static Dewar3VO getSelectedDewar(Integer dewarId) throws Exception {
		if (dewarId == null)
			return null;
		Dewar3Service dewar = (Dewar3Service) ejb3ServiceLocator.getLocalService(Dewar3Service.class);
		Dewar3VO selectedDewar = dewar.findByPk(dewarId, false, false);
		return selectedDewar;
	}

	public static Container3VO getSelectedContainer(Integer containerId) throws Exception {
		Container3Service container = (Container3Service) ejb3ServiceLocator.getLocalService(Container3Service.class);
		Container3VO selectedContainer = container.findByPk(containerId, false);
		return selectedContainer;
	}

	public static Protein3VO getSelectedProtein(Integer proteinId) throws Exception {
		Protein3Service protein = (Protein3Service) ejb3ServiceLocator.getLocalService(Protein3Service.class);
		Protein3VO selectedProtein = protein.findByPk(proteinId, false);
		return selectedProtein;
	}

	public static Proposal3VO getProposal(String proposalCode, String proposalNumber) throws CreateException,
			NamingException, FinderException, Exception {
		Proposal3VO prop = null;
		List<Proposal3VO> lstProposals = null;
		Proposal3Service proposalService = (Proposal3Service) ejb3ServiceLocator
				.getLocalService(Proposal3Service.class);
		lstProposals = proposalService.findByCodeAndNumber(proposalCode, proposalNumber, false, false,
				false);
		if (lstProposals != null && lstProposals.size() != 0) {
			prop = lstProposals.get(0);
		}
		return prop;
	}

	public static Image3VO getImage(Integer imageId) {
		Image3VO image = null;
		try {
			Image3Service imageService = (Image3Service) ejb3ServiceLocator.getLocalService(Image3Service.class);

			image = imageService.findByPk(imageId);
		} catch (Exception e) {
		}

		return image;
	}

	/**
	 * crystalHasSample
	 * 
	 * @param crystalId
	 * @return The number of Samples using this Crystal
	 * @throws CreateException
	 * @throws NamingException
	 * @throws FinderException
	 */
	public static int crystalHasSample(int crystalId) throws CreateException, NamingException, FinderException,
			Exception {
		int crystalHasSample = 0;
		try {
			Crystal3Service _crystal = (Crystal3Service) ejb3ServiceLocator.getLocalService(Crystal3Service.class);
			return _crystal.countSamples(crystalId);
//			Crystal3VO targetCrystal = _crystal.findByPk(crystalId, true);
//
//			if (!targetCrystal.getSampleVOs().isEmpty())
//				crystalHasSample = targetCrystal.getSampleVOs().size();
		} catch (javax.ejb.ObjectNotFoundException notFound) {

		}
		return crystalHasSample;
	}

	/**
	 * proteinIsCreatedBySampleSheet
	 * 
	 * @param proteinId
	 * @return 1 if the Protein was created from the Sample Sheet
	 * @throws CreateException
	 * @throws NamingException
	 * @throws FinderException
	 */
	public static int proteinIsCreatedBySampleSheet(int proteinId) throws CreateException, NamingException,
			FinderException, Exception {
		int isCreatedBySampleSheet = 0;
		try {
			Protein3Service _protein = (Protein3Service) ejb3ServiceLocator.getLocalService(Protein3Service.class);
			Protein3VO targetProtein = _protein.findByPk(proteinId, false);

			if (targetProtein.getIsCreatedBySampleSheet() == null)
				isCreatedBySampleSheet = 0;
			else if (targetProtein.getIsCreatedBySampleSheet() == 1)
				isCreatedBySampleSheet = 1;
		} catch (javax.ejb.ObjectNotFoundException notFound) {

		}
		return isCreatedBySampleSheet;
	}

	/**
	 * getShippingIdFromContainerId
	 * 
	 * @param containerId
	 * @return
	 */
	public static Integer getShippingIdFromContainerId(Integer containerId) {
		Integer targetshippingId = null;
		try {
			Container3Service _container = (Container3Service) ejb3ServiceLocator
					.getLocalService(Container3Service.class);
			Container3VO targetContainer = _container.findByPk(containerId, false);

			if (targetContainer.getDewarVOId() != null) {
				Dewar3Service _dewar = (Dewar3Service) ejb3ServiceLocator.getLocalService(Dewar3Service.class);
				Integer shippingId = _dewar.findByPk(targetContainer.getDewarVOId(), false, false).getShippingVOId();
				targetshippingId = shippingId;
			}
		} catch (Exception e) {
		}
		return targetshippingId;
	}

	/**
	 * getProposalIdFromShipping
	 * 
	 * @param shippingId
	 * @return
	 * @throws Exception
	 */
	public static Integer getProposalIdFromShipping(Integer shippingId) throws Exception {
		if (shippingId == null)
			return null;
		Integer proposalId = null;
		Shipping3VO shipping = getSelectedShipping(shippingId);

		proposalId = shipping.getProposalVOId();
		return proposalId;
	}
	
	public static List<String> getAllowedSpaceGroups() {
		List<String> spaceGroups = new ArrayList<String>();
		SpaceGroup3Service spaceGroupService;
		try {
			spaceGroupService = (SpaceGroup3Service) ejb3ServiceLocator.getLocalService(SpaceGroup3Service.class);
			List<SpaceGroup3VO> listSpaceGroupVo  = spaceGroupService.findAllowedSpaceGroups();
			if (listSpaceGroupVo != null ){
				for (Iterator<SpaceGroup3VO> iter = listSpaceGroupVo.iterator(); iter.hasNext();){
					SpaceGroup3VO sp = iter.next();
					spaceGroups.add(sp.getSpaceGroupShortName());
				}
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return spaceGroups;
	}
	
	public static List<GeometryClassname3VO> getAllowedGeometryClassname() {
		try {
			GeometryClassname3Service geometryClassnameService = (GeometryClassname3Service) ejb3ServiceLocator.getLocalService(GeometryClassname3Service.class);
			List<GeometryClassname3VO> listGeometryClassnameVo  = geometryClassnameService.findAll(false);
			if (listGeometryClassnameVo != null){
				List<GeometryClassname3VO> list = new ArrayList<GeometryClassname3VO>();
				for (Iterator<GeometryClassname3VO> iterator = listGeometryClassnameVo.iterator(); iterator.hasNext();) {
					GeometryClassname3VO geometryClassname3VO = (GeometryClassname3VO) iterator.next();
					GeometryClassname3VO g = geometryClassnameService.findByPk(geometryClassname3VO.getGeometryClassnameId(), true);
					list.add(g);
				}
				return list;
			}
			
			return listGeometryClassnameVo;
		} catch (NamingException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return null;
	}

}
