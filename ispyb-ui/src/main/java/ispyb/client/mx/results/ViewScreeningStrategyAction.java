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
/**
 * 
 * 
 */
package ispyb.client.mx.results;

import fr.improve.struts.taglib.layout.util.FormUtils;
import ispyb.client.common.BreadCrumbsForm;
import ispyb.client.common.util.FileUtil;
import ispyb.common.util.Constants;
import ispyb.common.util.PathUtils;
import ispyb.common.util.PropertyLoader;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.server.mx.services.screening.ScreeningStrategy3Service;
import ispyb.server.mx.services.screening.ScreeningStrategySubWedge3Service;
import ispyb.server.mx.services.screening.ScreeningStrategyWedge3Service;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.screening.ScreeningStrategy3VO;
import ispyb.server.mx.vos.screening.ScreeningStrategySubWedge3VO;
import ispyb.server.mx.vos.screening.ScreeningStrategyWedge3VO;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

/**
 * Class to handle results from beamline datacollections / screening strategy
 * 
 * @struts.action name="viewScreeningStrategyForm" path="/user/viewScreeningStrategy"
 *                input="user.result.viewResults.page" validate="false" parameter="reqCode" scope="request"
 * 
 * @struts.action-forward name="error" path="site.default.error.page"
 * @struts.action-forward name="success" path="user.screeningStrategy.view.page"
 * 
 * 
 */
public class ViewScreeningStrategyAction extends DispatchAction {

	ActionMessages errors = new ActionMessages();

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private DataCollection3Service dataCollectionService;

	private ScreeningStrategy3Service screeningStrategyService;

	private ScreeningStrategyWedge3Service screeningStrategyWedgeService;

	private ScreeningStrategySubWedge3Service screeningStrategySubWedgeService;

	private final static Logger LOG = Logger.getLogger(ViewScreeningStrategyAction.class);

	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws Exception {
		this.dataCollectionService = (DataCollection3Service) ejb3ServiceLocator
				.getLocalService(DataCollection3Service.class);
		this.screeningStrategyService = (ScreeningStrategy3Service) ejb3ServiceLocator
				.getLocalService(ScreeningStrategy3Service.class);
		this.screeningStrategyWedgeService = (ScreeningStrategyWedge3Service) ejb3ServiceLocator
				.getLocalService(ScreeningStrategyWedge3Service.class);
		this.screeningStrategySubWedgeService = (ScreeningStrategySubWedge3Service) ejb3ServiceLocator
				.getLocalService(ScreeningStrategySubWedge3Service.class);

	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}

	/**
	 * To display all the parameters linked to a screeningStrategyId.
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse in_reponse) {
		ActionMessages errors = new ActionMessages();
		try {
			// Clean BreadCrumbsForm
			// BreadCrumbsForm.getItClean(request);
			String screeningStrategyIdst = request.getParameter(Constants.SCREENING_STRATEGY_ID);
			Integer screeningStrategyId = new Integer(screeningStrategyIdst);

			ViewScreeningStrategyForm form = (ViewScreeningStrategyForm) actForm;

			form.setScreeningStrategyId(screeningStrategyId);
			String dataCollectionIdst = request.getParameter(Constants.DATA_COLLECTION_ID);
			Integer dataCollectionId = new Integer(dataCollectionIdst);
			form.setDataCollectionId(dataCollectionId);
			// DataCollectionLightValue dclv = dataCollection.findByPrimaryKeyLight(dataCollectionId);
			DataCollection3VO dataCollectionVO = dataCollectionService.findByPk(dataCollectionId, false, true);

			// screeningStrategy
			ScreeningStrategy3VO sslv = screeningStrategyService.findByPk(screeningStrategyId, true);
			form.setScreeningStrategy(sslv);
			ScreeningStrategyValueInfo ssvi = new ScreeningStrategyValueInfo(sslv);
			ssvi.setProgramLog(dataCollectionVO);
			form.setScreeningStrategyValueInfo(ssvi);

			// strategy wedge
			ScreeningStrategyWedge3VO[] screeningStrategyWedgeList;
			List<ScreeningStrategyWedgeValueInfo> screeningWedgeInfoList;
			ScreeningStrategyWedge3VO[] screeningStrategysWedge = new ScreeningStrategyValueInfo(
					screeningStrategyService.findByPk(screeningStrategyId, true)).getScreeningStrategyWedgesTab();

			screeningStrategyWedgeList = screeningStrategysWedge;
			screeningWedgeInfoList = new ArrayList<ScreeningStrategyWedgeValueInfo>();
			for (int k = 0; k < screeningStrategyWedgeList.length; k++) {
				ScreeningStrategyWedgeValueInfo sw = new ScreeningStrategyWedgeValueInfo(
						screeningStrategyWedgeService.findByPk(
								screeningStrategyWedgeList[k].getScreeningStrategyWedgeId(), true));
				screeningWedgeInfoList.add(sw);
			}
			form.setScreeningStrategyWedgeList(screeningStrategyWedgeList);
			form.setListStrategiesWedgeInfo(screeningWedgeInfoList);

			// strategy sub wedge
			ScreeningStrategySubWedge3VO[][] screeningStrategySubWedgeListAll;
			List<ScreeningStrategySubWedgeValueInfo>[] screeningSubWedgeInfoListAll;
			screeningStrategySubWedgeListAll = new ScreeningStrategySubWedge3VO[screeningStrategysWedge.length][];
			screeningSubWedgeInfoListAll = new ArrayList[screeningStrategysWedge.length];
			for (int j = 0; j < screeningStrategysWedge.length; j++) {
				ScreeningStrategySubWedge3VO[] screeningStrategysSubWedge = new ScreeningStrategyWedgeValueInfo(
						screeningStrategyWedgeService.findByPk(
								screeningStrategyWedgeList[j].getScreeningStrategyWedgeId(), true))
						.getScreeningStrategySubWedgesTab();

				screeningStrategySubWedgeListAll[j] = screeningStrategysSubWedge;
				screeningSubWedgeInfoListAll[j] = new ArrayList<ScreeningStrategySubWedgeValueInfo>();
				for (int k = 0; k < screeningStrategySubWedgeListAll[j].length; k++) {
					ScreeningStrategySubWedgeValueInfo ssw = new ScreeningStrategySubWedgeValueInfo(
							screeningStrategySubWedgeService.findByPk(screeningStrategySubWedgeListAll[j][k]
									.getScreeningStrategySubWedgeId()));
					screeningSubWedgeInfoListAll[j].add(ssw);
				}
			}
			form.setScreeningStrategySubWedgeListAll(screeningStrategySubWedgeListAll);
			form.setListStrategiesSubWedgeInfoAll(screeningSubWedgeInfoListAll);

			// strategy sub wedge
			String screeningStrategyWedgeSelIdst = request.getParameter("screeningStrategyWedgeSel");
			Integer screeningStrategyWedgeSelId = -1;
			try {
				screeningStrategyWedgeSelId = new Integer(screeningStrategyWedgeSelIdst);
			} catch (NumberFormatException ex) {

			}

			ScreeningStrategySubWedge3VO[] screeningStrategySubWedgeList;
			screeningStrategySubWedgeList = new ScreeningStrategySubWedge3VO[0];
			List<ScreeningStrategySubWedgeValueInfo> screeningSubWedgeInfoList;
			screeningSubWedgeInfoList = new ArrayList<ScreeningStrategySubWedgeValueInfo>();
			if (screeningStrategyWedgeSelId != -1) {
				ScreeningStrategySubWedge3VO[] screeningStrategysSubWedge = new ScreeningStrategyWedgeValueInfo(
						screeningStrategyWedgeService.findByPk(
								screeningStrategyWedgeList[screeningStrategyWedgeSelId].getScreeningStrategyWedgeId(),
								true)).getScreeningStrategySubWedgesTab();

				screeningStrategySubWedgeList = screeningStrategysSubWedge;
				for (int k = 0; k < screeningStrategySubWedgeList.length; k++) {
					ScreeningStrategySubWedgeValueInfo sw = new ScreeningStrategySubWedgeValueInfo(
							screeningStrategySubWedgeService.findByPk(screeningStrategySubWedgeList[k]
									.getScreeningStrategySubWedgeId()));
					screeningSubWedgeInfoList.add(sw);
				}
			}
			form.setScreeningStrategySubWedgeList(screeningStrategySubWedgeList);
			form.setListStrategiesSubWedgeInfo(screeningSubWedgeInfoList);

			// program log file
			String programLogPath = PathUtils.GetFullLogPath(dataCollectionVO);
			String programLogFilePath = programLogPath + Constants.DNA_FILES_BEST_FILE;

			LOG.debug("displayProgramLogFiles: programLogFilePath= " + programLogFilePath);

			// reformat the dna html file
			String fullFileContent = FileUtil.fileToString(programLogFilePath);

			// case where the file is not found
			if (fullFileContent == null) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.results.viewDNA"));
			}

			// Populate form
			form.setDNAContent(fullFileContent);
			form.setDNASelectedFile(Constants.DNA_FILES_BEST_FILE);

			FormUtils.setFormDisplayMode(request, actForm, FormUtils.INSPECT_MODE);

			// Fill the BreadCrumbs
			BreadCrumbsForm.getIt(request).setSelectedDataCollection(dataCollectionVO);
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			LOG.error(e.toString());
			saveErrors(request, errors);
			return mapping.findForward("error");
		}
		return mapping.findForward("success");
	}

}
