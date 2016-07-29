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
package ispyb.client.mx.results;

import ispyb.client.common.BreadCrumbsForm;
import ispyb.common.util.Constants;
import ispyb.common.util.PathUtils;
import ispyb.common.util.beamlines.ESRFBeamlineEnum;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.autoproc.AutoProc3Service;
import ispyb.server.mx.services.autoproc.AutoProcIntegration3Service;
import ispyb.server.mx.services.autoproc.AutoProcProgram3Service;
import ispyb.server.mx.services.autoproc.AutoProcScalingStatistics3Service;
import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.server.mx.vos.autoproc.AutoProc3VO;
import ispyb.server.mx.vos.autoproc.AutoProcIntegration3VO;
import ispyb.server.mx.vos.autoproc.AutoProcProgramAttachment3VO;
import ispyb.server.mx.vos.autoproc.AutoProcScalingStatistics3VO;
import ispyb.server.mx.vos.autoproc.AutoProcStatus3VO;
import ispyb.server.mx.vos.autoproc.IspybAutoProcAttachment3VO;
import ispyb.server.mx.vos.collections.DataCollection3VO;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

/**
 * Class to meet Ajax request for autoprocs. (select box on viewResults_top.jsp)
 * 
 * @struts.action name="getAutoProcsForm" path="/user/getAutoProcs" input="user.collection.viewResults.page"
 *                validate="false" parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * @struts.action-forward name="successLeft" path="/tiles/bodies/mx/results/viewAutoProcsLeft.jsp"
 * @struts.action-forward name="successRight" path="/tiles/bodies/mx/results/viewAutoProcsRight.jsp"
 */

public class GetAutoProcsAction extends DispatchAction {
	ActionMessages errors = new ActionMessages();

	private final static Logger LOG = Logger.getLogger(GetAutoProcsAction.class);

	// protected IspybAutoProcAttachment3Service ispybAutoProcAttachment;

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private AutoProc3Service apService;

	private AutoProcScalingStatistics3Service apssService;

	private AutoProcProgram3Service appService;

	private AutoProcIntegration3Service autoProcIntegrationService;

	private DataCollection3Service dataCollectionService;

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws Exception {
		// this.ispybAutoProcAttachment = (IspybAutoProcAttachment3Service) ejb3ServiceLocator
		// .getLocalService(IspybAutoProcAttachment3Service.class);
		this.apService = (AutoProc3Service) ejb3ServiceLocator.getLocalService(AutoProc3Service.class);
		this.appService = (AutoProcProgram3Service) ejb3ServiceLocator.getLocalService(AutoProcProgram3Service.class);
		this.apssService = (AutoProcScalingStatistics3Service) ejb3ServiceLocator
				.getLocalService(AutoProcScalingStatistics3Service.class);
		this.autoProcIntegrationService = (AutoProcIntegration3Service) ejb3ServiceLocator
				.getLocalService(AutoProcIntegration3Service.class);
		this.dataCollectionService = (DataCollection3Service) ejb3ServiceLocator
				.getLocalService(DataCollection3Service.class);
	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	/**
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {
		return mapping.findForward("error");
	}

	/**
	 * Get the autoprocs and return the left hand part of the table.
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward displayLeft(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {
		GetAutoProcsForm form = (GetAutoProcsForm) actForm;

		try {
			Integer autoProcId = Integer.parseInt(form.getAutoProc());

			AutoProc3VO apv = apService.findByPk(autoProcId);

			if (apv == null) {
				return mapping.findForward("error");
			}

			AutoProcScalingStatistics3VO apssv_overall = apssService.getBestAutoProcScalingStatistic(apssService
					.findByAutoProcId(apv.getAutoProcId(), "overall"));
			AutoProcScalingStatistics3VO apssv_outer = apssService.getBestAutoProcScalingStatistic(apssService
					.findByAutoProcId(apv.getAutoProcId(), "outerShell"));

			if (apssv_overall != null) {
				form.setOverallCompleteness("" + apssv_overall.getCompleteness() + "%");
				form.setOverallResolution("" + apssv_overall.getResolutionLimitLow() + "-"
						+ apssv_overall.getResolutionLimitHigh() + " &#8491;");
				form.setOverallIOverSigma("" + apssv_overall.getMeanIoverSigI());
				if (apssv_overall.getRmerge() == null)
					form.setOverallRsymm("");
				else {
					form.setOverallRsymm("" + apssv_overall.getRmerge() + "%");
				}
				form.setOverallMultiplicity("" + apssv_overall.getMultiplicity());
			}

			if (apssv_outer != null) {
				form.setOuterCompleteness("" + apssv_outer.getCompleteness() + "%");
				form.setOuterResolution("" + apssv_outer.getResolutionLimitLow() + "-"
						+ apssv_overall.getResolutionLimitHigh() + " &#8491;");
				form.setOuterIOverSigma("" + apssv_outer.getMeanIoverSigI());
				form.setOuterRsymm("" + apssv_outer.getRmerge() + "%");
				form.setOuterMultiplicity("" + apssv_outer.getMultiplicity());
			}

			double refinedCellA = ((double) ((int) (apv.getRefinedCellA() * 10))) / 10;
			double refinedCellB = ((double) ((int) (apv.getRefinedCellB() * 10))) / 10;
			double refinedCellC = ((double) ((int) (apv.getRefinedCellC() * 10))) / 10;

			form.setUnitCellA("" + refinedCellA + " &#8491;"); // angstrom symbol
			form.setUnitCellB("" + refinedCellB + " &#8491;");
			form.setUnitCellC("" + refinedCellC + " &#8491;");

			form.setUnitCellAlpha("" + apv.getRefinedCellAlpha() + " &#176;"); // degree symbol
			form.setUnitCellBeta("" + apv.getRefinedCellBeta() + " &#176;");
			form.setUnitCellGamma("" + apv.getRefinedCellGamma() + " &#176;");

			List<AutoProcStatus3VO> autoProcEvents = new ArrayList<AutoProcStatus3VO>();
			if (autoProcId != null) {
				List<AutoProcIntegration3VO> autoProcIntegrations = autoProcIntegrationService
						.findByAutoProcId(autoProcId);
				if (!autoProcIntegrations.isEmpty()) {
					autoProcEvents = (autoProcIntegrations.iterator().next()).getAutoProcStatusList();
				}
			}
			form.setAutoProcEvents(autoProcEvents);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mapping.findForward("successLeft");
	}

	/**
	 * Get the autoprocs and display right hand part of table
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	@SuppressWarnings("unused")
	public ActionForward displayRight(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {

		ActionMessages errors = new ActionMessages();
		GetAutoProcsForm form = (GetAutoProcsForm) actForm;

		try {

			// auto proc attachment
			// Get an object list.
			// List<IspybAutoProcAttachment3VO> listOfAutoProcAttachment = ispybAutoProcAttachment.findAll();
			List<IspybAutoProcAttachment3VO> listOfAutoProcAttachment = (List<IspybAutoProcAttachment3VO>) request
					.getSession().getAttribute(Constants.ISPYB_AUTOPROC_ATTACH_LIST);

			Integer autoProcId = Integer.parseInt(form.getAutoProc());
			Integer autoProcProgramId = null;

			if (autoProcId == null)
				return mapping.findForward("error");

			AutoProc3VO apv = apService.findByPk(autoProcId);

			if (apv != null)
				autoProcProgramId = apv.getAutoProcProgramVOId();

			if (autoProcId != null) {
				List<AutoProcIntegration3VO> autoProcIntegrations = autoProcIntegrationService
						.findByAutoProcId(autoProcId);

				if (!autoProcIntegrations.isEmpty()) {
					autoProcProgramId = (autoProcIntegrations.iterator().next()).getAutoProcProgramVOId();
				}
			}

			List<AutoProcProgramAttachment3VO> attachments = null;
			List<AutoProcAttachmentWebBean> outputAutoProcProgAttachmentsWebBeans = new ArrayList<AutoProcAttachmentWebBean>();
			List<AutoProcAttachmentWebBean> inputAutoProcProgAttachmentsWebBeans = new ArrayList<AutoProcAttachmentWebBean>();
			List<AutoProcAttachmentWebBean> correctionFilesAttachmentsWebBeans = new ArrayList<AutoProcAttachmentWebBean>();
			int nbAutoProcAttachmentsWebBeans = 0;
			// AutoProcAttachmentWebBean[] attachmentWebBeans = null;

			LOG.debug("autoProcProgramId = " + autoProcProgramId);

			if (autoProcProgramId != null) {
				attachments = new ArrayList(appService.findByPk(autoProcProgramId, true).getAttachmentVOs());

				if (!attachments.isEmpty()) {
					// attachmentWebBeans = new AutoProcAttachmentWebBean[attachments.size()];

					LOG.debug("nb attachments = " + attachments.size());
					int i = 0;
					for (Iterator<AutoProcProgramAttachment3VO> iterator = attachments.iterator(); iterator.hasNext();) {
						AutoProcProgramAttachment3VO att = iterator.next();
						AutoProcAttachmentWebBean attBean = new AutoProcAttachmentWebBean(att);
						// gets the ispyb auto proc attachment file
						IspybAutoProcAttachment3VO aAutoProcAttachment = getAutoProcAttachment(attBean.getFileName(),
								listOfAutoProcAttachment);
						// display all attachments, even if there are not linked to a known file's type
						if (aAutoProcAttachment == null){
							
							boolean output = true;
							if(attBean.getFileName().toUpperCase().contains(".INP"))
								output = false;
							String ouptputS = "output";
							if (!output){
								ouptputS = "input";
							}
							aAutoProcAttachment = new IspybAutoProcAttachment3VO(-1, attBean.getFileName(), attBean.getFileName(), "XDS", ouptputS, false);
						}
						attBean.setIspybAutoProcAttachment(aAutoProcAttachment);
						if (attBean.isOutput()) {
							outputAutoProcProgAttachmentsWebBeans.add(attBean);
							nbAutoProcAttachmentsWebBeans++;
						} else if (attBean.isInput()) {
							inputAutoProcProgAttachmentsWebBeans.add(attBean);
							nbAutoProcAttachmentsWebBeans++;
						}
						// attachmentWebBeans[i] = attBean;
						//LOG.debug("attBean = " + attBean.getAutoProcProgramAttachmentId()+" - "+attBean.getFileName());
						i = i + 1;
					}

				} else
					LOG.debug("attachments is empty");

			}

			// Issue 1507: Correction files for ID29 & ID23-1
			if (Constants.SITE_IS_ESRF()) {
				Integer dataCollectionId = null;
				if (BreadCrumbsForm.getIt(request).getSelectedDataCollection() != null)
					dataCollectionId = BreadCrumbsForm.getIt(request).getSelectedDataCollection().getDataCollectionId();
				if (dataCollectionId != null) {
					DataCollection3VO dataCollection = dataCollectionService.findByPk(dataCollectionId, false,
							false);
					String beamLineName = dataCollection.getDataCollectionGroupVO().getSessionVO().getBeamlineName();
					String[] correctionFiles = ESRFBeamlineEnum.retrieveCorrectionFilesNameWithName(beamLineName);
					if (correctionFiles != null) {
						for (int k = 0; k < correctionFiles.length; k++) {
							String correctionFileName = correctionFiles[k];
							String dir = ESRFBeamlineEnum.retrieveDirectoryNameWithName(beamLineName);
							if (dir != null) {
								String correctionFilePath = "/data/pyarch/" + dir + "/" + correctionFileName;
								String fullFilePath = PathUtils.FitPathToOS(correctionFilePath);
								File f = new File(fullFilePath);
								if (f != null && f.exists()) {
									// fake attachment
									AutoProcProgramAttachment3VO att = new AutoProcProgramAttachment3VO(-1, null,
											"Correction File", correctionFileName, correctionFilePath, null);
									AutoProcAttachmentWebBean attBean = new AutoProcAttachmentWebBean(att);
									IspybAutoProcAttachment3VO aAutoProcAttachment = new IspybAutoProcAttachment3VO(
											null, correctionFileName, "correction file",  "XDS", "correction", false );
									attBean.setIspybAutoProcAttachment(aAutoProcAttachment);
									correctionFilesAttachmentsWebBeans.add(attBean);
									attachments.add(attBean);
									nbAutoProcAttachmentsWebBeans++;
								}
							}
						}// end for
					}
				}
			}
			// request.getSession().setAttribute("attachmentWebBeans", attachmentWebBeans);
			form.setAutoProcProgAttachments(attachments);
			// form.setAutoProcProgAttachmentsWebBeans(attachmentWebBeans);
			form.setOutputAutoProcProgAttachmentsWebBeans(outputAutoProcProgAttachmentsWebBeans);
			form.setInputAutoProcProgAttachmentsWebBeans(inputAutoProcProgAttachmentsWebBeans);
			form.setCorrectionFilesAttachmentsWebBeans(correctionFilesAttachmentsWebBeans);
			form.setNbAutoProcAttachmentsWebBeans(nbAutoProcAttachmentsWebBeans);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mapping.findForward("successRight");
	}

	// returns the auto proc attachment linked to the specified filename - null if not found
	private IspybAutoProcAttachment3VO getAutoProcAttachment(String fileName,
			List<IspybAutoProcAttachment3VO> listOfAutoProcAttachment) {
		return ViewResultsAction.getAutoProcAttachment(fileName, listOfAutoProcAttachment);
	}
}
