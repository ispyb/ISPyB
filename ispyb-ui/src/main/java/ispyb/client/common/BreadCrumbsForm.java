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
 * breadCrumbsFrom.java
 * @author ludovic.launer@esrf.fr
 * Mar 7, 2005
 */

package ispyb.client.common;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessages;

import ispyb.client.security.roles.RoleDO;
import ispyb.common.util.Constants;
import ispyb.common.util.DBTools;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.server.mx.services.sample.BLSample3Service;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
import ispyb.server.mx.vos.collections.Image3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.collections.Workflow3VO;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.Protein3VO;
import ispyb.server.mx.vos.screening.Screening3VO;

/**
 * @struts.form name="breadCrumbsForm"
 */

/**
 * Keeps track of Session related information across the navigation
 * 
 * @author launer
 */
public class BreadCrumbsForm extends ActionForm implements Serializable {
	private static final long serialVersionUID = 1L;

	// form name to store on the session

	protected static final String NAME = "breadCrumbsForm";

	private final static Logger LOG = Logger.getLogger(BreadCrumbsForm.class);

	private Shipping3VO selectedShipping;

	private Dewar3VO selectedDewar;

	private Container3VO selectedContainer;

	private Session3VO selectedSession;

	private DataCollection3VO selectedDataCollection;

	private DataCollectionGroup3VO selectedDataCollectionGroup;
	
	private Workflow3VO selectedWorkflow;

	private BLSample3VO selectedSample;

	private Crystal3VO selectedCrystal;

	private Protein3VO selectedProtein;

	private Proposal3VO selectedProposal;

	private Image3VO selectedImage;

	private Screening3VO selectedScreening;

	private String samplesInSampleChanger;

	private String proposalCode; // store the proposal code to test if MXPressservice

	private String proposalNumber; // store the proposal number to test if manager

	private String fromPage; // store the page from where the action was called

	private String userRole;

	private Boolean userOrIndus;

	private Integer selectedSampleId;

	private Integer selectedContainerId;

	private Integer selectedCrystalId;

	private Integer selectedProteinId;

	private String selectedBeamline;

	private String fullname;

	private boolean selectedCrystalImageURLPresent = false;

	private boolean selectedSampleImageURLPrenset = false;

	private String selectedCrystalImageURL = "";

	private String selectedSampleImageURL = "";

	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	/***************************************************************************
	 * Utility methods
	 */

	/**
	 * GetcrystalImageURL The imageURL information exist for a given Crystal / BlSample
	 * 
	 * @param id
	 *            crystalId / blSampleId
	 * @param crystalImageUrl
	 *            or null
	 * @return
	 */
	public static String GetcrystalImageURL(int id, boolean isCrystal) {
		String crystalImageUrl = "";
		ActionMessages errors = new ActionMessages();

		try {
			BLSample3Service sampleService = (BLSample3Service) ejb3ServiceLocator
					.getLocalService(BLSample3Service.class);
			List lstUuids = null;

			if (!isCrystal) { // Sample	
				BLSample3VO targetSample = sampleService.findByPk(new Integer(id), false, false, false);
			}

		} catch (Exception e) {
			LOG.error("[GetcrystalImageURL]  no URL for " + ((isCrystal) ? "crystalId" : "blSampleId") + " " + id);
		}
		return crystalImageUrl;
	}

	/**
	 * IscrystalImageURLPresent The imageURL information exist for a given Crystal / BlSample
	 * 
	 * @param id
	 *            crystalId / blSampleId
	 * @param isCrystal
	 *            true if it's a Crystal / false if it's a Sample
	 * @return
	 */
	public static boolean IscrystalImageURLPresent(int id, boolean isCrystal) {
		boolean crystalImageURLPresent = false;
		String crystalImageURL = GetcrystalImageURL(id, isCrystal);

		if (crystalImageURL != null && crystalImageURL.trim().compareToIgnoreCase("") != 0)
			crystalImageURLPresent = true;

		return crystalImageURLPresent;
	}

	/***************************************************************************
	 * Business Methods
	 */

	/**
	 * To get the actual form from the session. If it doesn't exist, it creates one new
	 * 
	 * @param request
	 * @return
	 */
	public static BreadCrumbsForm getIt(HttpServletRequest request) {
		HttpSession session = request.getSession();
		BreadCrumbsForm form = (BreadCrumbsForm) session.getAttribute(NAME);
		if (form == null) {
			form = new BreadCrumbsForm();
			session.setAttribute(NAME, form);
		}
		return form;
	}

	/**
	 * Get a new clean form.
	 * 
	 * @param request
	 * @return
	 */
	public static BreadCrumbsForm getItClean(HttpServletRequest request) {
		HttpSession session = request.getSession();
		BreadCrumbsForm form = new BreadCrumbsForm();
		RoleDO role = (RoleDO) session.getAttribute(Constants.CURRENT_ROLE);

		form.setUserRole(role.getName());
		if (role.getName().equals(Constants.ROLE_USER) || role.getName().equals(Constants.ROLE_INDUSTRIAL)) {
			form.setUserOrIndus(true);
		} else
			form.setUserOrIndus(false);
		try {
			String proposalCode = ((String) session.getAttribute(Constants.PROPOSAL_CODE)).toLowerCase();
			String proposalNumber = String.valueOf(request.getSession().getAttribute(Constants.PROPOSAL_NUMBER));
			form.setProposalCode(proposalCode);
			form.setProposalNumber(proposalNumber);
		} catch (Exception e) {

		}

		session.setAttribute(NAME, form);
		return form;
	}

	/**
	 * To set a new form.
	 * 
	 * @param request
	 * @param value
	 */
	public static void setIt(HttpServletRequest request, BreadCrumbsForm value) {
		HttpSession session = request.getSession();
		session.setAttribute(NAME, value);
	}

	/**
	 * To remove it from the session.
	 * 
	 * @param request
	 */
	public static void deleteIt(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute(NAME);
	}

	/***************************************************************************
	 * Setters and getters
	 */

	public static String getName() {
		return NAME;
	}

	public Session3VO getSelectedSession() {
		return selectedSession;
	}

	public void setSelectedSession(Session3VO selectedSession) {
		this.selectedSession = selectedSession;
	}

	public DataCollectionGroup3VO getSelectedDataCollectionGroup() {
		return selectedDataCollectionGroup;
	}

	public void setSelectedDataCollectionGroup(DataCollectionGroup3VO selectedDataCollectionGroup) {
		this.selectedDataCollectionGroup = selectedDataCollectionGroup;
	}
	
	public Workflow3VO getSelectedWorkflow() {
		return selectedWorkflow;
	}

	public void setSelectedWorkflow(Workflow3VO selectedWorkflow) {
		this.selectedWorkflow = selectedWorkflow;
	}

	public DataCollection3VO getSelectedDataCollection() {
		return selectedDataCollection;
	}

	public void setSelectedDataCollection(DataCollection3VO selectedDataCollection) {
		this.selectedDataCollection = selectedDataCollection;
	}

	public Protein3VO getSelectedProtein() {
		return selectedProtein;
	}

	public void setSelectedProtein(Protein3VO selectedProtein) {
		this.selectedProtein = selectedProtein;
	}

	public Shipping3VO getSelectedShipping() {
		return selectedShipping;
	}

	public Dewar3VO getSelectedDewar() {
		return selectedDewar;
	}

	public Container3VO getSelectedContainer() {
		return selectedContainer;
	}

	public BLSample3VO getSelectedSample() {
		return selectedSample;
	}

	public Crystal3VO getSelectedCrystal() {
		return selectedCrystal;
	}

	/**
	 * @param selectedSample
	 *            The selectedSample to set.
	 */
	public void setSelectedSample(BLSample3VO selectedSample) {
		this.selectedSample = selectedSample;

		// Delete related information
		this.selectedCrystal = null;
		this.selectedProtein = null;

		// TODO : the following lines gives bugs linked to primary key = null, to be investigated later
		// // Populate related information
		// try {
		// this.selectedContainer = DBTools.getSelectedContainer(selectedSample.getContainerId());
		// if (this.selectedContainer != null) this.selectedDewar =
		// DBTools.getSelectedDewar(this.selectedContainer.getDewarId());
		// if (this.selectedDewar != null) this.selectedShipping =
		// DBTools.getSelectedShipping(this.selectedDewar.getShippingId());
		// } catch (Exception e) {
		// }
	}

	/**
	 * @param selectedShipping
	 *            The selectedShipping to set.
	 */
	public void setSelectedShipping(Shipping3VO selectedShipping) {
		this.selectedShipping = selectedShipping;
		this.selectedDewar = null;
		this.selectedContainer = null;
		this.selectedSample = null;
	}

	/**
	 * @param selectedDewar
	 *            The selectedDewar to set.
	 */
	public void setSelectedDewar(Dewar3VO selectedDewar) {
		this.selectedDewar = selectedDewar;

		// Delete related information
		this.selectedContainer = null;
		this.selectedSample = null;

		// Populate related information
		try {
			this.selectedShipping = DBTools.getSelectedShipping(selectedDewar.getShippingVOId());
		} catch (Exception e) {
		}
	}

	/**
	 * @param selectedContainer
	 */
	public void setSelectedContainer(Container3VO selectedContainer) {
		this.selectedContainer = selectedContainer;

		// Delete related information
		// TODO Clean this method
		this.selectedSample = null;

		// Populate related information
		try {
			Dewar3VO selectedDewar = DBTools.getSelectedDewar(selectedContainer.getDewarVOId());

			this.selectedDewar = selectedDewar;
			if (selectedDewar != null)
				this.selectedShipping = DBTools.getSelectedShipping(selectedDewar.getShippingVOId());
			else
				this.selectedShipping = null;
		} catch (Exception e) {
		}

	}

	/**
	 * @param selectedCrystal
	 *            The selectedCrystal to set.
	 */
	public void setSelectedCrystal(Crystal3VO selectedCrystal) {
		this.selectedCrystal = selectedCrystal;
	}

	public Proposal3VO getSelectedProposal() {
		return selectedProposal;
	}

	public void setSelectedProposal(Proposal3VO selectedProposal) {
		this.selectedProposal = selectedProposal;
	}

	/**
	 * @return Returns the samplesInSampleChanger.
	 */
	public String getSamplesInSampleChanger() {
		return samplesInSampleChanger;
	}

	/**
	 * @param samplesInSampleChanger
	 *            The samplesInSampleChanger to set.
	 */
	public void setSamplesInSampleChanger(String samplesInSampleChanger) {
		this.samplesInSampleChanger = samplesInSampleChanger;
	}

	/**
	 * @return Returns the proposalCode.
	 */
	public String getProposalCode() {
		return proposalCode;
	}

	/**
	 * @param proposalCode
	 *            The proposalCode to set.
	 */
	public void setProposalCode(String proposalCode) {
		this.proposalCode = proposalCode;
	}

	/**
	 * @return Returns the proposalNumber.
	 */
	public String getProposalNumber() {
		return proposalNumber;
	}

	/**
	 * @param proposalNumber
	 *            The proposalNumber to set.
	 */
	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}

	/**
	 * @return Returns the selectedImage.
	 */
	public Image3VO getSelectedImage() {
		return selectedImage;
	}

	/**
	 * @param selectedImage
	 *            The selectedImage to set.
	 */
	public void setSelectedImage(Image3VO selectedImage) {
		this.selectedImage = selectedImage;
	}

	/**
	 * @return Returns the selectedScreening.
	 */
	public Screening3VO getSelectedScreening() {
		return selectedScreening;
	}

	/**
	 * @param selectedScreening
	 *            The selectedScreening to set.
	 */
	public void setSelectedScreening(Screening3VO selectedScreening) {
		this.selectedScreening = selectedScreening;
	}

	/**
	 * @return Returns the selectedContainerId.
	 */
	public Integer getSelectedContainerId() {
		return selectedContainerId;
	}

	/**
	 * @param selectedContainerId
	 *            The selectedContainerId to set.
	 */
	public void setSelectedContainerId(Integer selectedContainerId) {
		this.selectedContainerId = selectedContainerId;
	}

	/**
	 * @return Returns the selectedCrystalId.
	 */
	public Integer getSelectedCrystalId() {
		return selectedCrystalId;
	}

	/**
	 * @param selectedCrystalId
	 *            The selectedCrystalId to set.
	 */
	public void setSelectedCrystalId(Integer selectedCrystalId) {
		this.selectedCrystalId = selectedCrystalId;
	}

	/**
	 * @return Returns the selectedProteinId.
	 */
	public Integer getSelectedProteinId() {
		return selectedProteinId;
	}

	/**
	 * @param selectedProteinId
	 *            The selectedProteinId to set.
	 */
	public void setSelectedProteinId(Integer selectedProteinId) {
		this.selectedProteinId = selectedProteinId;
	}

	/**
	 * @return Returns the selectedSampleId.
	 */
	public Integer getSelectedSampleId() {
		return selectedSampleId;
	}

	/**
	 * @param selectedSampleId
	 *            The selectedSampleId to set.
	 */
	public void setSelectedSampleId(Integer selectedSampleId) {
		this.selectedSampleId = selectedSampleId;
	}

	/**
	 * @return Returns the fromPage.
	 */
	public String getFromPage() {
		return fromPage;
	}

	/**
	 * @param fromPage
	 *            The fromPage to set.
	 */
	public void setFromPage(String fromPage) {
		this.fromPage = fromPage;
	}

	/**
	 * @return Returns the userRole.
	 */
	public String getUserRole() {
		return userRole;
	}

	/**
	 * @param userRole
	 *            The userRole to set.
	 */
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getSelectedBeamline() {
		return selectedBeamline;
	}

	public void setSelectedBeamline(String selectedBeamline) {
		this.selectedBeamline = selectedBeamline;
	}

	public Boolean getUserOrIndus() {
		return userOrIndus;
	}

	public void setUserOrIndus(Boolean userOrIndus) {
		this.userOrIndus = userOrIndus;
	}

	public boolean isSelectedCrystalImageURLPresent() {
		try {
			if (this.selectedCrystal == null)
				selectedCrystalImageURLPresent = false;
			else
				selectedCrystalImageURLPresent = IscrystalImageURLPresent(this.selectedCrystal.getCrystalId()
						.intValue(), true);
		} catch (Exception e) {
		}
		return selectedCrystalImageURLPresent;
	}

	public boolean isSelectedSampleImageURLPrenset() {
		try {
			if (this.selectedSample == null)
				selectedSampleImageURLPrenset = false;
			else
				selectedSampleImageURLPrenset = IscrystalImageURLPresent(
						this.selectedSample.getBlSampleId().intValue(), false);
		} catch (Exception e) {
		}
		return selectedSampleImageURLPrenset;
	}

	public String getSelectedCrystalImageURL() {
		try {
			if (this.selectedCrystal == null)
				selectedCrystalImageURL = "";
			else
				selectedCrystalImageURL = GetcrystalImageURL(this.selectedCrystal.getCrystalId().intValue(), true);
		} catch (Exception e) {
		}
		return selectedCrystalImageURL;
	}

	public String getSelectedSampleImageURL() {
		try {
			if (this.selectedSample == null)
				selectedSampleImageURL = "";
			else
				selectedSampleImageURL = GetcrystalImageURL(this.selectedSample.getBlSampleId().intValue(), false);
		} catch (Exception e) {
		}
		return selectedSampleImageURL;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getFullname() {
		return this.fullname;
	}
}
