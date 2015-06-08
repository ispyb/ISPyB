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

package ispyb.client.common.reference;

import ispyb.client.common.BreadCrumbsForm;
import ispyb.common.util.Constants;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.mx.services.collections.IspybReference3Service;
import ispyb.server.mx.vos.collections.IspybReference3VO;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

/**
 * 
 * @struts.action name="viewReferenceForm" path="/viewReference" input="guest.reference.viewReference.page"
 *                validate="false" parameter="reqCode" scope="request"
 * @struts.action-forward name="error" path="site.default.error.page"
 * @struts.action-forward name="viewReference" path="guest.reference.viewReference.page"
 * 
 */
public class ViewReferenceAction extends DispatchAction {

	private final Logger LOG = Logger.getLogger(ViewReferenceAction.class);

	private final static Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Proposal3Service proposalService;

	private IspybReference3Service referenceService;

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws Exception {
		this.proposalService = (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);
		this.referenceService = (IspybReference3Service) ejb3ServiceLocator
				.getLocalService(IspybReference3Service.class);
	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	/**
	 * display all references
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {
		ActionMessages errors = new ActionMessages();
		List<IspybReference3VO> listReferences = (List<IspybReference3VO>) request.getSession().getAttribute(
				Constants.ISPYB_REFERENCE_LIST);
		if (listReferences == null) {
			// load the references list
			try {
				listReferences = referenceService.findAll();
				request.getSession().setAttribute(Constants.REFERENCES_LIST, listReferences);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		ViewReferenceForm form = (ViewReferenceForm) actForm;
		form.setListReferences(listReferences);
		request.getSession().setAttribute(Constants.REFERENCES_LIST, listReferences);
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}
		return redirectPageFromRole(mapping, request);
	}

	private ActionForward redirectPageFromRole(ActionMapping mapping, HttpServletRequest request) {
		// redirection page according to the Role ------------------------

		//RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
		//String role = roleObject.getName();

		return mapping.findForward("viewReference");
	}

	@SuppressWarnings("unchecked")
	public ActionForward downloadListOfReference(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		LOG.debug("downloadListOfReference");
		String listOfReference = request.getParameter("listOfReference");
		String[] listReferencesId = listOfReference.split(",");
		List<IspybReference3VO> allRef = (List<IspybReference3VO>) request.getSession().getAttribute(
				Constants.ISPYB_REFERENCE_LIST);
		List<IspybReference3VO> listReferences = new ArrayList<IspybReference3VO>();
		for (int i = 0; i < listReferencesId.length; i++) {
			try {
				Integer id = Integer.parseInt(listReferencesId[i]);
				for (Iterator<IspybReference3VO> iterator = allRef.iterator(); iterator.hasNext();) {
					IspybReference3VO ispybReference3VO = iterator.next();
					if (ispybReference3VO.getReferenceId().equals(id)) {
						listReferences.add(ispybReference3VO);
						break;
					}
				}
			} catch (NumberFormatException e) {

			}

		}
		return sendReferenceList(listReferences, mapping, actForm, request, response);
	}

	@SuppressWarnings("unchecked")
	public ActionForward downloadReference(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		List<IspybReference3VO> listReferences = (List<IspybReference3VO>) request.getSession().getAttribute(
				Constants.REFERENCES_LIST);
		return sendReferenceList(listReferences, mapping, actForm, request, response);
	}

	private ActionForward sendReferenceList(List<IspybReference3VO> listReferences, ActionMapping mapping,
			ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {
		// create the file
		try {
			Date today = Calendar.getInstance().getTime();
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			df.setLenient(false);
			String date = df.format(today);
			String s = date + "";
			// Get ProposalId
			Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
			if (proposalId != null) {
				Proposal3VO proposal = proposalService.findByPk(proposalId);
				if (proposal != null) {
					s = date + "-" + proposal.getCode() + proposal.getNumber();
				}
			}
			if (BreadCrumbsForm.getIt(request).getSelectedSample() != null) {
				String sampleName = BreadCrumbsForm.getIt(request).getSelectedSample().getName();
				s += "-" + sampleName;
			}
			if (BreadCrumbsForm.getIt(request).getSelectedSession() != null) {
				String sessionBeamLine = BreadCrumbsForm.getIt(request).getSelectedSession().getBeamlineName();
				s += "-" + sessionBeamLine;
			}
			if (BreadCrumbsForm.getIt(request).getSelectedProtein() != null) {
				String acronym = BreadCrumbsForm.getIt(request).getSelectedProtein().getAcronym();
				s += "-" + acronym;
			}

			String outFilename = "ISPyB-biblio-" + s + ".bib";
			outFilename = outFilename.replaceAll(" ", "_");
			//String realPath = request.getRealPath("\\tmp\\") + "\\" + outFilename;
			// File out = new File(realPath);
			// if (out.exists())
			// out.delete();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			String doc = "";
			if (listReferences == null || listReferences.isEmpty()) {
				doc += "There is no references in this report";
				baos.write(doc.getBytes());
			} else {
				for (Iterator<IspybReference3VO> iterator = listReferences.iterator(); iterator.hasNext();) {
					IspybReference3VO ispybReference3VO = iterator.next();
					Blob bibtextBlob = ispybReference3VO.getReferenceBibtext();
					byte[] bdata = bibtextBlob.getBytes(1, (int) bibtextBlob.length());
					String bibText = new String(bdata);
					doc += bibText + "\n";
				}
				baos.write(doc.getBytes());
			}

			response.setContentType("application/text");
			response.setHeader("Content-disposition", "attachment; filename=" + outFilename);
			response.setContentLength(baos.size());
			ServletOutputStream sos;
			try {
				sos = response.getOutputStream();
				baos.writeTo(sos);
				sos.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ActionForward downloadSimpleReference(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		LOG.debug("downloadSimpleReference");
		try {
			List<IspybReference3VO> listReferences = new ArrayList<IspybReference3VO>();
			Integer referenceId = new Integer(request.getParameter(Constants.REFERENCE_ID));
			if (referenceId != null) {
				IspybReference3VO vo = referenceService.findByPk(referenceId);
				if (vo != null) {
					listReferences.add(vo);
				}
			}
			sendReferenceList(listReferences, mapping, actForm, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		// return redirectPageFromRole(mapping, request);
	}
	
	
	public static List<IspybReference3VO> getReferences(List<String> listOfBeamlines, List<IspybReference3VO> allRef, int displayMesh){
		// list of references
		List<IspybReference3VO> listOfReferences = new ArrayList<IspybReference3VO>();
		if (allRef != null){
			for (Iterator<IspybReference3VO> iterator = allRef.iterator(); iterator.hasNext();) {
				IspybReference3VO ispybReference3VO = iterator.next();
				if (ispybReference3VO.getBeamline().equals(Constants.REFERENCE_ALL_BEAMLINE)
					|| ispybReference3VO.getBeamline().equals(Constants.REFERENCE_ALL_XRF)) {
					listOfReferences.add(ispybReference3VO);
				} else if (ispybReference3VO.getBeamline().equals(Constants.REFERENCE_MESH) && (displayMesh == 1)) {
					listOfReferences.add(ispybReference3VO);
				} else {
					String beamline = ispybReference3VO.getBeamline();
					boolean isBeamlineInvolved = listOfBeamlines.contains(beamline);
					if (isBeamlineInvolved) {
						listOfReferences.add(ispybReference3VO);
					}
				}
			}
		}
		return listOfReferences ;
	}
	
	
	public static String getReferenceText(List<String> listOfBeamlines){
		String beamlinesText = "";
		for (Iterator<String> iterator = listOfBeamlines.iterator(); iterator.hasNext();) {
			String bl = iterator.next();
			beamlinesText += bl + ", ";
		}
		if (beamlinesText.length() > 2) {
			beamlinesText = beamlinesText.substring(0, beamlinesText.length() - 2);
		}
		String referenceText = "When reporting data collected on " + beamlinesText
		+ "<br/>please cite the appropriate references";
		
		return referenceText ;
	}
	
	
}
