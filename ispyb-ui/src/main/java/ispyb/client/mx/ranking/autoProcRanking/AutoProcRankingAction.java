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
package ispyb.client.mx.ranking.autoProcRanking;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ispyb.client.common.util.OptionValue;
import ispyb.client.security.roles.RoleDO;
import ispyb.common.util.Constants;
import ispyb.common.util.StringUtils;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.server.mx.vos.collections.DataCollection3VO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @struts.action name="autoProcRankingForm" path="/user/autoProcRanking" input="user.collection.viewDataCollection.page"
 *                parameter="reqCode" scope="request" validate="false"
 * 
 * @struts.action-forward name="success" path="user.ranking.autoProcRanking.page"
 * @struts.action-forward name="localContactSuccess" path="localcontact.ranking.autoProcRanking.page"
 * @struts.action-forward name="displayFlexChart" path="user.ranking.autoProcRankingChart.page"
 * @struts.action-forward name="localcontactdisplayFlexChart" path="localcontact.ranking.autoProcRankingChart.page"
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 */
public class AutoProcRankingAction  extends DispatchAction {
	private DataCollection3Service dataCollectionService;

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
	
	
	/**
	 * Initialize the needed services.
	 * 
	 * @throws Exception
	 */
	private void initServices() throws Exception {
		this.dataCollectionService = (DataCollection3Service) ejb3ServiceLocator.getLocalService(DataCollection3Service.class);
	}

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		this.initServices();
		return super.execute(mapping, form, request, response);
	}
	
	/**
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		HttpSession session = request.getSession();
		AutoProcRankingForm form = (AutoProcRankingForm) actForm;
		Set<Integer> rankList = (Set<Integer>) session.getAttribute(Constants.DATACOLLECTIONID_SET);
		try {
			// Criteria weights
			AutoProcRankingCriteriaVO criteriaVO = null;
			criteriaVO = (AutoProcRankingCriteriaVO) session.getAttribute(Constants.AUTOPROC_RANKING_CRITERIA);
			if (criteriaVO == null)
				criteriaVO = new AutoProcRankingCriteriaVO();
			
			// Build dcList
			List<DataCollection3VO> dcList = new ArrayList<DataCollection3VO>();
			Iterator<Integer> iter = rankList.iterator();
			while (iter.hasNext()) {
				int dataCollectionId = iter.next();
				DataCollection3VO dc = this.dataCollectionService.findByPk(dataCollectionId, false, false);
				dcList.add(dc);
			}
			
			// Call Sample Ranking
			AutoProcRanking autoProcRanking = new AutoProcRanking();
			autoProcRanking.setWeights(criteriaVO);
			List<AutoProcRankingVO> autoProcRankingValues = autoProcRanking.rank(dcList);
			
			// Set DataCollection list (for export)
			List<DataCollection3VO> dataCollectionList = new ArrayList<DataCollection3VO>();
			ArrayList<DataCollection3VO> list = (ArrayList<DataCollection3VO>) request.getSession().getAttribute(
					Constants.DATACOLLECTION_LIST);
			for (int i = 0; i < autoProcRankingValues.size(); i++) {
				AutoProcRankingVO autoProcRankingVO = autoProcRankingValues.get(i);
				DataCollection3VO dataCollectionDb = dataCollectionService.findByPk(
						autoProcRankingVO.getDataCollectionId(), false,  false);
				autoProcRankingVO.setImagePrefix(dataCollectionDb.getImagePrefix());
				autoProcRankingVO.setDataCollectionNumber(dataCollectionDb.getDataCollectionNumber());
				autoProcRankingVO.setStartTime(StringUtils.dateToTimestamp(dataCollectionDb.getStartTime()));
				for (Iterator<DataCollection3VO> iter3 = list.iterator(); iter3.hasNext();) {
					DataCollection3VO d = iter3.next();
					if (autoProcRankingVO.getDataCollectionId().equals(d.getDataCollectionId())) {
						dataCollectionList.add(d);
						break;
					}
				}
			}
			request.getSession().setAttribute(Constants.AUTOPROC_RANKING_LIST, autoProcRankingValues);
			request.getSession().setAttribute(Constants.DATACOLLECTION_LIST, dataCollectionList);
			
			// Pre-select top5 DataCollection
			if (form.getSelectedItems() == null || form.getSelectedItems().size() == 0) {
				Set<Integer> selectedItems = new HashSet<Integer>();
				int nbDataCollection = Math.min(autoProcRankingValues.size(), Constants.TOP_DATACOLLECTIONS);
				for (int i = 0; i < nbDataCollection; i++) {
					AutoProcRankingVO autoProcRankingVO = autoProcRankingValues.get(i);
					selectedItems.add(autoProcRankingVO.getDataCollectionId());
				}
				form.setSelectedItems(selectedItems);
			}

			// Criteria weights
			List<OptionValue> weightValueList = new ArrayList<OptionValue>();
			for (int i = 0; i <= 10; i++) {
				OptionValue option = new OptionValue("" + i, "" + i);
				weightValueList.add(option);
			}
			session.setAttribute("weightValueList", weightValueList);

			// Set up weights
			form.setWeightOverallRFactor(criteriaVO.getWeightOverallRFactor());
			form.setWeightHighestResolution(criteriaVO.getWeightHighestResolution());
			form.setWeightCompleteness(criteriaVO.getWeightCompleteness());

			// Set up form
			form.setAutoProcRankingValue(autoProcRankingValues);

			// Set up Session values
			session.setAttribute(Constants.AUTOPROC_RANKING_VALUE_LIST, autoProcRankingValues);
			session.setAttribute(Constants.AUTOPROC_RANKING_CRITERIA, criteriaVO);
			
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}
		//redirect according role
		RoleDO roleObject = (RoleDO)request.getSession().getAttribute(Constants.CURRENT_ROLE);
		String role = roleObject.getName();
		if (role.equals(Constants.ROLE_LOCALCONTACT) ) {
			return mapping.findForward("localContactSuccess");
		}else{
			return mapping.findForward("success");
		}
	}
	
	
	/**
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActionForward update(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		String submitAction = request.getParameter("actionName");
		AutoProcRankingForm form = (AutoProcRankingForm) actForm;
		List<AutoProcRankingVO> autoProcRankingValues = (List<AutoProcRankingVO>) request.getSession().getAttribute(
				Constants.AUTOPROC_RANKING_VALUE_LIST);

		if (submitAction.equals("sortOnImagePrefix"))
			Collections.sort(autoProcRankingValues, AutoProcRankingComparator.ORDER_ImagePrefix);
		else if (submitAction.equals("sortOnStartTime"))
			Collections.sort(autoProcRankingValues, AutoProcRankingComparator.ORDER_StartTime);
		else if (submitAction.equals("sortOnSpaceGroup"))
			Collections.sort(autoProcRankingValues, AutoProcRankingComparator.ORDER_SpaceGroup);
		else if (submitAction.equals("sortOnOverallRFactorRank"))
			Collections.sort(autoProcRankingValues, AutoProcRankingComparator.ORDER_RANK_OverallRFactor);
		else if (submitAction.equals("sortOnHighestResolutionRank"))
			Collections.sort(autoProcRankingValues, AutoProcRankingComparator.ORDER_RANK_HighestResolution);
		else if (submitAction.equals("sortOnCompletenessRank"))
			Collections.sort(autoProcRankingValues, AutoProcRankingComparator.ORDER_RANK_Completeness);
		else if (submitAction.equals("sortOnTotalRank"))
			Collections.sort(autoProcRankingValues, AutoProcRankingComparator.ORDER_RANK_Total);
		else if (submitAction.equals("ComparisonFlexChart"))
			return this.drawComparisonFlexChart(mapping, actForm, request, response);
		else if (submitAction.equals("Save"))
			return this.save(mapping, actForm, request, response);
		else if (submitAction.equals("Rank"))
			return this.rank(mapping, actForm, request, response);
		// Error...
		else {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Unknown submit button: "
					+ submitAction));
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		// Set up form
		form.setAutoProcRankingValue(autoProcRankingValues);

		return mapping.findForward("success");
	}
	
	/**
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward rank(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession();
		AutoProcRankingForm form = (AutoProcRankingForm) actForm;

		// Get weights
		Integer weightOverallRFactor = form.getWeightOverallRFactor();
		Integer weighHighestResolution = form.getWeightHighestResolution();
		Integer weightCompleteness = form.getWeightCompleteness();

		// Save weights
		AutoProcRankingCriteriaVO criteriaVO = null;
		criteriaVO = (AutoProcRankingCriteriaVO) session.getAttribute(Constants.AUTOPROC_RANKING_CRITERIA);
		if (criteriaVO == null)
			criteriaVO = new AutoProcRankingCriteriaVO();
		criteriaVO.setWeightOverallRFactor(weightOverallRFactor);
		criteriaVO.setWeightHighestResolution(weighHighestResolution);
		criteriaVO.setWeightCompleteness(weightCompleteness);
		session.setAttribute(Constants.AUTOPROC_RANKING_CRITERIA, criteriaVO);

		return this.display(mapping, actForm, request, response);
	}
	
	/**
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward save(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();

		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Not yet implemented"));
		saveErrors(request, errors);
		return (mapping.findForward("error"));
	}
	
	
	/**
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActionForward drawComparisonFlexChart(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		AutoProcRankingForm form = (AutoProcRankingForm) actForm;
		HttpSession session = request.getSession();

		try {

			// Get selected DataCollection
			Set<Integer> selectedItems = form.getSelectedItems();
//			LOG.debug("Drawing Flex chart for: " + selectedItems);
//			if (selectedItems.size() == 0) {
//				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail",
//						"Please select a Data Collection"));
//				saveErrors(request, errors);
//				return mapping.findForward("error");
//			}

			// Get  ranking values
			List<AutoProcRankingVO> autoProcRankingValuesAll = (List<AutoProcRankingVO>) session
					.getAttribute(Constants.AUTOPROC_RANKING_VALUE_LIST);
			if (autoProcRankingValuesAll.size() == 0)
				return null;
//			AutoProcRankingCriteriaVO criteriaVO = null;
//			criteriaVO = (AutoProcRankingCriteriaVO) session.getAttribute(Constants.AUTOPROC_RANKING_CRITERIA);

			// Save selected items list
			session.setAttribute("selectedItems", selectedItems);

			// Build chart
//			String fileUrl = AutoProcRankingChart.buildComparisonFlexChart(request, autoProcRankingValuesAll, criteriaVO,
//					selectedItems, Constants.CHART_TEMP_DIR);
//			if (fileUrl == null || fileUrl.equals("")) {
//				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail",
//						"Problem generating chart..."));
//				saveErrors(request, errors);
//				return mapping.findForward("error");
//			}
//			form.setReportDataUrl(fileUrl);
//			form.setChartTitle("AutoProc Ranking Comparison");
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}
		
		//redirect according role
		RoleDO roleObject = (RoleDO)request.getSession().getAttribute(Constants.CURRENT_ROLE);
		String role = roleObject.getName();
		if (role.equals(Constants.ROLE_LOCALCONTACT) ) {
			return mapping.findForward("localcontactdisplayFlexChart");
		}else{
			return mapping.findForward("displayFlexChart");
		}
	}
	
	private Gson getGson(){
		return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").excludeFieldsWithModifiers(Modifier.PRIVATE).serializeNulls().create();
	}
	
	public String getErrorMessage(Exception exp){
		HashMap<String, String> error = new HashMap<String, String>();
		StringWriter sw = new StringWriter();
		exp.printStackTrace(new PrintWriter(sw));
		error.put("trace", sw.toString());
		if (exp.getMessage() != null){
			error.put("message", exp.getMessage());
		}
		return new Gson().toJson(error);
	};
	
	
	/**
	 * getSession
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public ActionForward getAutoProcData(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try{
			HttpSession session = request.getSession();
			// Get  ranking values
			List<AutoProcRankingVO> autoProcRankingValuesAll = (List<AutoProcRankingVO>) session.getAttribute(Constants.AUTOPROC_RANKING_VALUE_LIST);
			
			Gson gson = getGson();
			String json = gson.toJson(autoProcRankingValuesAll);
			response.setContentType("application/json; charset=UTF-8");
			response.setHeader("Cache-Control","no-cache");
			response.getWriter().flush();
			response.getWriter().write(json);
		}
		catch(Exception exp){
			exp.printStackTrace();
			response.getWriter().write( getErrorMessage(exp));
			response.setStatus(500);
			return null;
		}
		return null;
	}
	
	
	
}
