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
 * SelectProtein.java
 * @author ludovic.launer@esrf.fr
 * June 28, 2005
 */

package ispyb.client.mx.sample;

import fr.improve.struts.taglib.layout.util.FormUtils;
import ispyb.common.util.DBTools;
import ispyb.common.util.Constants;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.sample.BLSample3Service;
import ispyb.server.mx.services.sample.Crystal3Service;
import ispyb.server.mx.services.sample.Protein3Service;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.Protein3VO;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * @struts.action name="viewCrystalForm" path="/user/selectProteinAction" type="ispyb.client.mx.sample.SelectProteinAction"
 *                validate="false" input="user.sample.protein.select.page" parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="proteinSelectPage" path="user.sample.protein.select.page"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 */

public class SelectProteinAction extends org.apache.struts.actions.DispatchAction {

	// Session Attributes
	Integer mProposalId = null;

	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Protein3Service proteinService;

	private BLSample3Service sampleService;

	private Crystal3Service crystalService;

	private final static Logger LOG = Logger.getLogger(SelectProteinAction.class);

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws CreateException, NamingException {

		this.sampleService = (BLSample3Service) ejb3ServiceLocator.getLocalService(BLSample3Service.class);
		this.proteinService = (Protein3Service) ejb3ServiceLocator.getLocalService(Protein3Service.class);
		this.crystalService = (Crystal3Service) ejb3ServiceLocator.getLocalService(Crystal3Service.class);

	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	/**
	 * display
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_response
	 * @return
	 */
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_response) {
		ActionMessages errors = new ActionMessages();
		LOG.debug("calculate the list of proteins for acronym or for proposal");

		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			ViewCrystalForm form = (ViewCrystalForm) actForm; // Parameters submited by form

			this.RetrieveSessionAttributes(request); // Session parameters
			// ---------------------------------------------------------------------------------------------------
			// Build Query
			// Retrieve information from DB
			List listInfo = proteinService.findByProposalId(mProposalId);

			// Populate with Info
			form.setListProtein(listInfo);

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		return mapping.findForward("proteinSelectPage");
	}

	/**
	 * exportForPocketSample
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_response
	 * @return
	 */
	public ActionForward exportForPocketSample(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_response) {
		ActionMessages errors = new ActionMessages();

		try {
			// ---------------------------------------------------------------------------------------------------
			// Retrieve Attributes
			ViewCrystalForm form = (ViewCrystalForm) actForm; // Parameters submited by form

			this.RetrieveSessionAttributes(request); // Session parameters
			// ---------------------------------------------------------------------------------------------------
			Hashtable selectedProteins = form.getSelectedProteins();
			Enumeration proteinIds = selectedProteins.keys();

			// Write to file
			Date today = new Date();
			File f = null;
			FileWriter fr = null;
			String fileName = "tmp/" + this.mProposalId + "_" + today.getDate() + "_" + today.getMonth() + "_"
					+ today.getYear() + ".txt";
			String textFilePath = request.getRealPath(fileName);
			String exportedContent = new String();

			while (proteinIds.hasMoreElements()) // Retrieve selected Proteins
			{
				if (f == null) // Create output file
				{
					f = new File(textFilePath);
					fr = new FileWriter(f);
				}
				// Retrieve information
				// Protein
				Integer key = (Integer) proteinIds.nextElement();
				Integer proteinId = (Integer) selectedProteins.get(key);
				Protein3VO protein = DBTools.getSelectedProtein(proteinId);
				// Crystal
				String spacegroup = new String("");
				List lstCrystal = crystalService.findByProteinId(proteinId);
				if (!lstCrystal.isEmpty()) {
					Crystal3VO crystal = (Crystal3VO) crystalService.findByProteinId(proteinId).toArray()[0];
					spacegroup = crystal.getSpaceGroup();
				}

				// Add info into File
				String l = protein.getAcronym() + " - " + spacegroup + "\n";
				if (exportedContent == null)
					exportedContent = new String("");
				exportedContent += l;
				fr.write(l);
			}
			if (fr != null)
				fr.close();

			// Build URL to file
			String url = request.getRequestURL().toString();
			String context = request.getContextPath();
			int c = url.indexOf(context);
			url = url.substring(0, c + context.length());
			url = url + "/" + fileName;

			form.setExportedContent(exportedContent);
			form.setExportedContentUrl(url);

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		return mapping.findForward("proteinSelectPage");
	}

	/**
	 * RetrieveSessionAttributes
	 * 
	 * @param request
	 *            The Request to retrieve the Session Attributes from Populates the class Attributes with Attributes
	 *            stored in the Session
	 */
	public void RetrieveSessionAttributes(HttpServletRequest request) {
		this.mProposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
	}

}
