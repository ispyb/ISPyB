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
 * Created on Jul 15 2009
 *
 * Patrice Brenchereau
 *
 */
package ispyb.client.common.util;

import ispyb.client.security.roles.RoleDO;
import ispyb.common.util.Constants;
import ispyb.server.common.services.proposals.LabContact3Service;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.services.shipping.Container3Service;
import ispyb.server.common.services.shipping.Dewar3Service;
import ispyb.server.common.services.shipping.Shipping3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.LabContact3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.server.mx.services.autoproc.AutoProcProgram3Service;
import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.server.mx.services.sample.BLSample3Service;
import ispyb.server.mx.services.sample.Crystal3Service;
import ispyb.server.mx.services.sample.Protein3Service;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.Protein3VO;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.actions.DispatchAction;

public class Confidentiality extends DispatchAction {
	private final static Logger LOG = Logger.getLogger(Confidentiality.class);
	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	/**
	 * Check if current proposalId matches with current object proposalId (can be extended to manage role access rights)
	 * 
	 * @param request
	 * @param objectProposalId
	 * @return
	 */
	public static boolean isAccessAllowed(HttpServletRequest request, Integer objectProposalId) {

		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
		// LOG.debug("Checking access rights: "+proposalId+" vs "+objectProposalId);

		// Stores, LocalContact, Blom don't have proposalId (cross proposal users)
		if (proposalId == null)
			return true;
		// test role first
		RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
		String role = roleObject.getName();
		if (role != null && (role.equals(Constants.FXMANAGE_ROLE_NAME) || role.equals(Constants.ROLE_LOCALCONTACT)
				|| role.equals(Constants.ROLE_MANAGER))) {
			return true;
			
		}
		String proposalNumber = request.getSession().getAttribute(Constants.PROPOSAL_NUMBER) == null ? null : request
				.getSession().getAttribute(Constants.PROPOSAL_NUMBER).toString();
		if (proposalNumber != null
				&& ((proposalNumber.equals(Constants.PROPOSAL_NUMBER_MANAGER))
						|| (proposalNumber.equals(Constants.PROPOSAL_NUMBER_LOCAL_CONTACT))
						|| (proposalNumber.equals(Constants.PROPOSAL_NUMBER_BLOM)) || (proposalNumber
						.equals(Constants.PROPOSAL_NUMBER_STORE)))) {
			return true;
		}

		// Checking access right
		if (!proposalId.equals(objectProposalId)) {
			LOG.error(
					"Access denied: proposalId=" + proposalId + " trying to access data of proposalId="
							+ objectProposalId);
			return false;
		}
		return true;
	}

	/**
	 * Check if access is allowed for shipping
	 * 
	 * @param request
	 * @param shippingId
	 * @return
	 * @throws Exception
	 */
	public static boolean isAccessAllowedToShipping(HttpServletRequest request, Integer shippingId) throws Exception {

		Shipping3Service shippingService = (Shipping3Service) ejb3ServiceLocator
				.getLocalService(Shipping3Service.class);

		Shipping3VO shipping = shippingService.findByPk(shippingId, false);
		return Confidentiality.isAccessAllowed(request, shipping.getProposalVO().getProposalId());
	}

	/**
	 * Check if access is allowed for dewar
	 * 
	 * @param request
	 * @param dewarId
	 * @return
	 * @throws Exception
	 */
	public static boolean isAccessAllowedToDewar(HttpServletRequest request, Integer dewarId) throws Exception {

		Dewar3Service dewarService = (Dewar3Service) ejb3ServiceLocator.getLocalService(Dewar3Service.class);

		Dewar3VO dewar = dewarService.findByPk(dewarId, false, false);
		Shipping3VO shipping = dewar.getShippingVO();
		return Confidentiality.isAccessAllowed(request, shipping.getProposalVO().getProposalId());
	}

	/**
	 * Check if access is allowed for container
	 * 
	 * @param request
	 * @param containerId
	 * @return
	 * @throws Exception
	 */
	public static boolean isAccessAllowedToContainer(HttpServletRequest request, Integer containerId) throws Exception {

		Container3Service containerService = (Container3Service) ejb3ServiceLocator
				.getLocalService(Container3Service.class);
		Dewar3Service dewarService = (Dewar3Service) ejb3ServiceLocator.getLocalService(Dewar3Service.class);
		Shipping3Service shippingService = (Shipping3Service) ejb3ServiceLocator
				.getLocalService(Shipping3Service.class);

		Container3VO container = containerService.findByPk(containerId, false);
		Dewar3VO dewar = dewarService.findByPk(container.getDewarVOId(), false, false);
		Shipping3VO shipping = shippingService.findByPk(dewar.getShippingVOId(), false);
		return Confidentiality.isAccessAllowed(request, shipping.getProposalVOId());
	}

	/**
	 * Check if access is allowed for sample
	 * 
	 * @param request
	 * @param sampleId
	 * @return
	 * @throws Exception
	 */
	public static boolean isAccessAllowedToSample(HttpServletRequest request, Integer sampleId) throws Exception {

		BLSample3Service sampleService = (BLSample3Service) ejb3ServiceLocator.getLocalService(BLSample3Service.class);
		BLSample3VO sample = sampleService.findByPk(sampleId, false, false, false);
		if (sample == null)
			return false;
		return Confidentiality.isAccessAllowed(request, sample.getCrystalVO().getProteinVO().getProposalVOId());
	}

	/**
	 * Check if access is allowed for crystal
	 * 
	 * @param request
	 * @param crystalId
	 * @return
	 * @throws Exception
	 */
	public static boolean isAccessAllowedToCrystal(HttpServletRequest request, Integer crystalId) throws Exception {

		Crystal3Service crystalService = (Crystal3Service) ejb3ServiceLocator.getLocalService(Crystal3Service.class);
		Protein3Service proteinService = (Protein3Service) ejb3ServiceLocator.getLocalService(Protein3Service.class);

		Crystal3VO crystal = crystalService.findByPk(crystalId, false);
		Protein3VO protein = proteinService.findByPk(crystal.getProteinVOId(), false);
		return Confidentiality.isAccessAllowed(request, protein.getProposalVOId());
	}

	/**
	 * Check if access is allowed for protein
	 * 
	 * @param request
	 * @param proteinId
	 * @return
	 * @throws Exception
	 */
	public static boolean isAccessAllowedToProtein(HttpServletRequest request, Integer proteinId) throws Exception {

		Protein3Service proteinService = (Protein3Service) ejb3ServiceLocator.getLocalService(Protein3Service.class);
		Protein3VO protein = proteinService.findByPk(proteinId, false);
		return Confidentiality.isAccessAllowed(request, protein.getProposalVOId());
	}

	/**
	 * Check if access is allowed for labContact
	 * 
	 * @param request
	 * @param labContactId
	 * @return
	 * @throws Exception
	 */
	public static boolean isAccessAllowedToLabContact(HttpServletRequest request, Integer labContactId)
			throws Exception {

		LabContact3Service labContactService = (LabContact3Service) ejb3ServiceLocator
				.getLocalService(LabContact3Service.class);
		LabContact3VO labContact = labContactService.findByPk(labContactId);
		return Confidentiality.isAccessAllowed(request, labContact.getProposalVOId());
	}

	/**
	 * Check if access is allowed to dataCollectionId
	 * 
	 * @param request
	 * @param dataCollectionId
	 * @return
	 * @throws Exception
	 */
	public static boolean isAccessAllowedToDataCollection(HttpServletRequest request, Integer collectionId)
			throws Exception {

		DataCollection3Service dataCollectionService = (DataCollection3Service) ejb3ServiceLocator
				.getLocalService(DataCollection3Service.class);
		DataCollection3VO dclv = dataCollectionService.findByPk(collectionId, false, false);
		DataCollectionGroup3VO dcg = dclv.getDataCollectionGroupVO();
		// Integer sessionId = new Integer(dclv.getSessionId());
		// Session3Service sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
		// Session3VO sessionlv = sessionService.findByPk(sessionId, false, false, false);
		Session3VO sessionlv = dcg.getSessionVO();
		return Confidentiality.isAccessAllowed(request, sessionlv.getProposalVOId());
	}
	
	/**
	 * Check if access is allowed to autoProcProgramId
	 * 
	 * @param request
	 * @param dataCollectionId
	 * @return
	 * @throws Exception
	 */
	public static boolean isAccessAllowedToAutoProcProgram(HttpServletRequest request, Integer autoProcProgramId)
			throws Exception {

		AutoProcProgram3Service autoProcProgramService = (AutoProcProgram3Service) ejb3ServiceLocator.getLocalService(AutoProcProgram3Service.class);
		List<DataCollection3VO> listCollect = autoProcProgramService.findCollects(autoProcProgramId);
		boolean canAcccess = false;
		if (listCollect != null){
			for (Iterator<DataCollection3VO> iterator = listCollect.iterator(); iterator.hasNext();){
				DataCollection3VO dc = iterator.next();
				DataCollectionGroup3VO dcg = dc.getDataCollectionGroupVO();
				Session3VO sessionlv = dcg.getSessionVO();
				boolean isok = Confidentiality.isAccessAllowed(request, sessionlv.getProposalVOId());
				canAcccess = isok;
				if (!isok){
					return false;
				}
			}
		}
		return canAcccess;
	}

	/**
	 * Check if current proposalId has access to a given file (can be extended to manage role access rights)
	 * 
	 * @param request
	 * @param filePath
	 * @return
	 */
	public static boolean isAccessAllowed(HttpServletRequest request, String filePath) {

		try {
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			// LOG.debug("Checking access rights: "+proposalId+" vs "+filePath);

			// Current proposalName
			Proposal3Service proposalService = (Proposal3Service) ejb3ServiceLocator
					.getLocalService(Proposal3Service.class);
			Proposal3VO proposal = proposalService.findByPk(proposalId);
			// store or manager or localContact
			if (proposalId == null)
				return true;
			String currentProposalName = "";
			if (proposal != null) {
				currentProposalName = proposal.getCode() + proposal.getNumber();
			}

			// FilePath proposalName
			if (Constants.SITE_IS_DLS()) {
				String fields[] = filePath.split("/");
				String filePathProposalName = null;
				if (Constants.PROPOSAL_POSITION_IN_FILEPATH_DIRECTION.equals("right-to-left"))
					filePathProposalName = fields[fields.length - Constants.PROPOSAL_POSITION_IN_FILEPATH];
				else
					// default direction is left-to-right
					filePathProposalName = fields[Constants.PROPOSAL_POSITION_IN_FILEPATH];

				if (currentProposalName.equals("mx0"))
					currentProposalName = "0-0";

				if (!filePathProposalName.equals("0-0")) {
					String visitFields[] = filePathProposalName.split("-");
					if (visitFields.length == 2)
						filePathProposalName = visitFields[0];
					else
						filePathProposalName = visitFields[0] + "-" + visitFields[1];
				}
			}

			// test role first
			RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
			String role = roleObject.getName();
			if (role != null && (role.equals(Constants.ROLE_STORE) || role.equals(Constants.ROLE_BLOM)
					|| role.equals(Constants.FXMANAGE_ROLE_NAME) || role.equals(Constants.ROLE_LOCALCONTACT)
					|| role.equals(Constants.ROLE_MANAGER))) {
				return true;
				
			}
			// Checking access right
			// Better than previous method because file can be in a sub-directory (in which case the
			// PROPOSAL_POSITION_IN_FILEPATH is not correct and gives a wrong filePathProposalName)
			// Allows wrong access to file that have the requesting proposal name in the filename but
			// should be rare...
			if (Constants.SITE_IS_ESRF()) { 
				if (!filePath.toUpperCase().contains(currentProposalName.toUpperCase())) {
					LOG.error(
							"Access denied: proposalName=" + currentProposalName + "(" + proposalId
								+ ") trying to access data at filePath=" + filePath);
					return false;
				}
			}	

		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;

	}
	
	public static boolean isManager(HttpServletRequest request){
		RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
		String role = roleObject.getName();
		return (role != null && role.equals(Constants.ROLE_MANAGER) );
	}
	
	public static boolean isLocalContact(HttpServletRequest request){
		RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
		String role = roleObject.getName();
		return (role != null && role.equals(Constants.ROLE_LOCALCONTACT) );
	}

}
