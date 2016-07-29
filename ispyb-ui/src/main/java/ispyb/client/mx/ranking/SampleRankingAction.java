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
 * SampleRankingAction.java
 * @author patrice.brenchereau@esrf.fr
 * March 09, 2009
 */

package ispyb.client.mx.ranking;

import ispyb.client.common.util.OptionValue;
import ispyb.client.security.roles.RoleDO;
import ispyb.common.util.Constants;
import ispyb.common.util.StringUtils;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.server.mx.vos.collections.DataCollection3VO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

/**
 * @struts.action name="sampleRankingForm" path="/user/sampleRanking" input="user.collection.viewDataCollection.page"
 *                parameter="reqCode" scope="request" validate="false"
 * 
 * @struts.action-forward name="success" path="user.ranking.sampleRanking.page"
 * @struts.action-forward name="localContactSuccess" path="localcontact.ranking.sampleRanking.page"
 * @struts.action-forward name="displayChart" path="user.ranking.sampleRankingChart.page"
 * @struts.action-forward name="localcontactdisplayChart" path="localcontact.ranking.sampleRankingChart.page"
 * @struts.action-forward name="displayFlexChart" path="user.ranking.sampleRankingFlexChart.page"
 * @struts.action-forward name="localcontactdisplayFlexChart" path="localcontact.ranking.sampleRankingFlexChart.page"
 * @struts.action-forward name="error" path="site.default.error.page"
 * 
 */
public class SampleRankingAction extends DispatchAction {

	private DataCollection3Service dataCollectionService;

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private final static Logger LOG = Logger.getLogger(SampleRankingAction.class);

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
	public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		HttpSession session = request.getSession();
		SampleRankingForm form = (SampleRankingForm) actForm;
		Set<Integer> rankList = (Set<Integer>) session.getAttribute(Constants.DATACOLLECTIONID_SET);
		LOG.debug("Ranking dataCollections: " + rankList);
		long startTime, endTime;

		try {
			// Criteria weights
			SampleRankingCriteriaVO criteriaVO = null;
			criteriaVO = (SampleRankingCriteriaVO) session.getAttribute(Constants.SAMPLE_RANKING_CRITERIA);
			if (criteriaVO == null)
				criteriaVO = new SampleRankingCriteriaVO();

			// Build sampleScreeningVOList
			LOG.debug("Building SampleScreeningVO list");
			startTime = System.currentTimeMillis();
			List<SampleScreeningVO> sampleScreeningVOList = new ArrayList<SampleScreeningVO>();
			Iterator<Integer> iter = rankList.iterator();
			while (iter.hasNext()) {
				int dataCollectionId = iter.next();
				SampleScreeningVO sampleScreeningVO = SampleScreeningUtils.load(dataCollectionId);
				sampleScreeningVOList.add(sampleScreeningVO);
			}
			endTime = System.currentTimeMillis();
			LOG.debug("Loading screening data: " + (endTime - startTime) + " ms");

			// Call Sample Ranking
			LOG.debug("Ranking dataCollections");
			startTime = System.currentTimeMillis();
			SampleRanking sampleRanking = new SampleRanking();
			sampleRanking.setWeights(criteriaVO);
			List<SampleRankingVO> sampleRankingValues = new ArrayList<SampleRankingVO>();
			if (!sampleScreeningVOList.isEmpty()) {
				sampleRankingValues = sampleRanking.rank(sampleScreeningVOList);
			}

			endTime = System.currentTimeMillis();
			LOG.debug("Ranking data: " + (endTime - startTime) + " ms");

			// Populate DataCollection values for display
			// Set DataCollection list (for export)
			List<DataCollection3VO> dataCollectionList = new ArrayList<DataCollection3VO>();
			ArrayList<DataCollection3VO> list = (ArrayList<DataCollection3VO>) request.getSession().getAttribute(
					Constants.DATACOLLECTION_LIST);
			for (int i = 0; i < sampleRankingValues.size(); i++) {
				SampleRankingVO sampleRankingVO = sampleRankingValues.get(i);
				// DataCollection3VO dataCollectionDb =
				// dataCollections.findByPrimaryKey(sampleRankingVO.getDataCollectionId());
				DataCollection3VO dataCollectionDb = dataCollectionService.findByPk(sampleRankingVO.getDataCollectionId(), false,
						false);
				sampleRankingVO.setImagePrefix(dataCollectionDb.getImagePrefix());
				sampleRankingVO.setDataCollectionNumber(dataCollectionDb.getDataCollectionNumber());
				sampleRankingVO.setStartTime(StringUtils.dateToTimestamp(dataCollectionDb.getStartTime()));
				for (Iterator<DataCollection3VO> iter3 = list.iterator(); iter3.hasNext();) {
					DataCollection3VO d = iter3.next();
					if (sampleRankingVO.getDataCollectionId().equals(d.getDataCollectionId())) {
						dataCollectionList.add(d);
						break;
					}
				}
			}
			request.getSession().setAttribute(Constants.SAMPLE_RANKING_LIST, sampleRankingValues);
			request.getSession().setAttribute(Constants.DATACOLLECTION_LIST, dataCollectionList);
			// // Debug
			// for ( int i=0; i<sampleRankingValues.size(); i++ ) {
			// SampleRankingVO smp = sampleRankingValues.get(i);
			// String res = "";
			// res += "\t"+smp.getImagePrefix()+"_"+smp.getDataCollectionNumber()+"\t";
			// // // Values
			// // res += smp.getTheoreticalResolutionValue()+"\t";
			// // res += smp.getTotalExposureTimeValue()+"\t";
			// // res += smp.getMosaicityValue()+"\t";
			// // res += smp.getNumberOfSpotsIndexedValue()+"\t";
			// // res += smp.getNumberOfImagesValue()+"\t";
			// // Scores
			// res += smp.getTheoreticalResolutionScore()+"\t";
			// res += smp.getTotalExposureTimeScore()+"\t";
			// res += smp.getMosaicityScore()+"\t";
			// res += smp.getNumberOfSpotsIndexedScore()+"\t";
			// res += smp.getNumberOfImagesScore()+"\t";
			// res += smp.getTotalScore()+"\t";
			// res += "\n";
			// System.out.println(res);
			//
			// }

			// Pre-select top5 DataCollection
			if (form.getSelectedItems() == null || form.getSelectedItems().size() == 0) {
				Set<Integer> selectedItems = new HashSet<Integer>();
				int nbDataCollection = Math.min(sampleRankingValues.size(), Constants.TOP_DATACOLLECTIONS);
				for (int i = 0; i < nbDataCollection; i++) {
					SampleRankingVO sampleRankingVO = sampleRankingValues.get(i);
					selectedItems.add(sampleRankingVO.getDataCollectionId());
				}
				form.setSelectedItems(selectedItems);
			}

			// Criteria weights
			List<OptionValue> weightValueList = new ArrayList();
			for (int i = 0; i <= 10; i++) {
				OptionValue option = new OptionValue("" + i, "" + i);
				weightValueList.add(option);
			}
			session.setAttribute("weightValueList", weightValueList);

			// Set up weights
			form.setWeightRankingResolution(criteriaVO.getWeightRankingResolution());
			form.setWeightExposureTime(criteriaVO.getWeightExposureTime());
			form.setWeightMosaicity(criteriaVO.getWeightMosaicity());
			form.setWeightNumberOfSpots(criteriaVO.getWeightNumberOfSpots());
			form.setWeightNumberOfImages(criteriaVO.getWeightNumberOfImages());

			// Set up form
			form.setSampleRankingValue(sampleRankingValues);

			// Set up Session values
			session.setAttribute(Constants.SAMPLE_RANKING_VALUE_LIST, sampleRankingValues);
			session.setAttribute(Constants.SAMPLE_RANKING_CRITERIA, criteriaVO);
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}
		// redirect according role
		RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
		String role = roleObject.getName();
		if (role.equals(Constants.ROLE_LOCALCONTACT)) {
			return mapping.findForward("localContactSuccess");
		} else {
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
	public ActionForward update(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		String submitAction = request.getParameter("actionName");
		SampleRankingForm form = (SampleRankingForm) actForm;
		List<SampleRankingVO> sampleRankingValues = (List<SampleRankingVO>) request.getSession().getAttribute(
				Constants.SAMPLE_RANKING_VALUE_LIST);

		if (submitAction.equals("sortOnImagePrefix"))
			Collections.sort(sampleRankingValues, SampleRankingComparator.ORDER_ImagePrefix);
		else if (submitAction.equals("sortOnStartTime"))
			Collections.sort(sampleRankingValues, SampleRankingComparator.ORDER_StartTime);
		else if (submitAction.equals("sortOnSpaceGroup"))
			Collections.sort(sampleRankingValues, SampleRankingComparator.ORDER_SpaceGroup);
		else if (submitAction.equals("sortOnTheoreticalResolutionRank"))
			Collections.sort(sampleRankingValues, SampleRankingComparator.ORDER_RANK_TheoreticalResolution);
		else if (submitAction.equals("sortOnExposureTimeRank"))
			Collections.sort(sampleRankingValues, SampleRankingComparator.ORDER_RANK_TotalExposureTime);
		else if (submitAction.equals("sortOnMosaicityRank"))
			Collections.sort(sampleRankingValues, SampleRankingComparator.ORDER_RANK_Mosaicity);
		else if (submitAction.equals("sortOnNumberOfSpotsRank"))
			Collections.sort(sampleRankingValues, SampleRankingComparator.ORDER_RANK_NumberOfSpotsIndexed);
		else if (submitAction.equals("sortOnNumberOfImagesRank"))
			Collections.sort(sampleRankingValues, SampleRankingComparator.ORDER_RANK_NumberOfImages);
		else if (submitAction.equals("sortOnTotalRank"))
			Collections.sort(sampleRankingValues, SampleRankingComparator.ORDER_RANK_Total);
		else if (submitAction.equals("ComparisonChart"))
			return this.drawComparisonChart(mapping, actForm, request, response);
		else if (submitAction.equals("ComparisonFlexChart"))
			return this.drawComparisonFlexChart(mapping, actForm, request, response);
		else if (submitAction.equals("DistributionChart"))
			return this.drawDistributionChart(mapping, actForm, request, response);
		else if (submitAction.equals("Save"))
			return this.save(mapping, actForm, request, response);
		else if (submitAction.equals("Rank"))
			return this.rank(mapping, actForm, request, response);
		// Error...
		else {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Unknown submit button: " + submitAction));
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		// Set up form
		form.setSampleRankingValue(sampleRankingValues);

		return mapping.findForward("success");
	}

	/**
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward rank(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		HttpSession session = request.getSession();
		SampleRankingForm form = (SampleRankingForm) actForm;

		// Get weights
		Integer weightRankingResolution = form.getWeightRankingResolution();
		Integer weightExposureTime = form.getWeightExposureTime();
		Integer weightMosaicity = form.getWeightMosaicity();
		Integer weightNumberOfSpots = form.getWeightNumberOfSpots();
		Integer weightNumberOfImages = form.getWeightNumberOfImages();

		// Save weights
		SampleRankingCriteriaVO criteriaVO = null;
		criteriaVO = (SampleRankingCriteriaVO) session.getAttribute(Constants.SAMPLE_RANKING_CRITERIA);
		if (criteriaVO == null)
			criteriaVO = new SampleRankingCriteriaVO();
		criteriaVO.setWeightRankingResolution(weightRankingResolution);
		criteriaVO.setWeightExposureTime(weightExposureTime);
		criteriaVO.setWeightMosaicity(weightMosaicity);
		criteriaVO.setWeightNumberOfSpots(weightNumberOfSpots);
		criteriaVO.setWeightNumberOfImages(weightNumberOfImages);
		session.setAttribute(Constants.SAMPLE_RANKING_CRITERIA, criteriaVO);

		return this.display(mapping, actForm, request, response);
	}

	/**
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward save(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {

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
	public ActionForward drawComparisonChart(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		SampleRankingForm form = (SampleRankingForm) actForm;
		HttpSession session = request.getSession();

		try {

			// Get selected DataCollection
			Set<Integer> selectedItems = form.getSelectedItems();
			LOG.debug("Drawing chart for: " + selectedItems);
			if (selectedItems.size() == 0) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Please select a Data Collection"));
				saveErrors(request, errors);
				return mapping.findForward("error");
			}

			// Get sample ranking values
			List<SampleRankingVO> sampleRankingValuesAll = (List<SampleRankingVO>) session
					.getAttribute(Constants.SAMPLE_RANKING_VALUE_LIST);
			if (sampleRankingValuesAll.size() == 0)
				return null;
			SampleRankingCriteriaVO criteriaVO = null;
			criteriaVO = (SampleRankingCriteriaVO) session.getAttribute(Constants.SAMPLE_RANKING_CRITERIA);

			// Use only selected samples
			List<SampleRankingVO> sampleRankingValues = new ArrayList<SampleRankingVO>();
			for (int i = 0; i < sampleRankingValuesAll.size(); i++) {
				SampleRankingVO sampleRankingVO = sampleRankingValuesAll.get(i);
				if (selectedItems.contains(sampleRankingVO.getDataCollectionId())) {
					sampleRankingValues.add(sampleRankingVO);
				}
			}

			// Build chart
			String fileUrl = SampleRankingChart.buildComparisonChart(request, sampleRankingValues, criteriaVO,
					Constants.CHART_TEMP_DIR);
			if (fileUrl == null || fileUrl.equals("")) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Problem generating chart..."));
				saveErrors(request, errors);
				return mapping.findForward("error");
			}
			form.setReportDataUrl(fileUrl);
			form.setChartTitle("Sample Ranking Comparison");
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		// redirect according role
		RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
		String role = roleObject.getName();
		if (role.equals(Constants.ROLE_LOCALCONTACT)) {
			return mapping.findForward("localcontactdisplayChart");
		} else {
			return mapping.findForward("displayChart");
		}
	}

	/**
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward drawComparisonFlexChart(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		SampleRankingForm form = (SampleRankingForm) actForm;
		HttpSession session = request.getSession();

		try {

			// Get selected DataCollection
			Set<Integer> selectedItems = form.getSelectedItems();
			LOG.debug("Drawing Flex chart for: " + selectedItems);
			if (selectedItems.size() == 0) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Please select a Data Collection"));
				saveErrors(request, errors);
				return mapping.findForward("error");
			}

			// Get sample ranking values
			List<SampleRankingVO> sampleRankingValuesAll = (List<SampleRankingVO>) session
					.getAttribute(Constants.SAMPLE_RANKING_VALUE_LIST);
			if (sampleRankingValuesAll.size() == 0)
				return null;
			SampleRankingCriteriaVO criteriaVO = null;
			criteriaVO = (SampleRankingCriteriaVO) session.getAttribute(Constants.SAMPLE_RANKING_CRITERIA);

			// Save selected items list
			session.setAttribute("selectedItems", selectedItems);

			// Build chart
			String fileUrl = SampleRankingChart.buildComparisonFlexChart(request, sampleRankingValuesAll, criteriaVO, selectedItems,
					Constants.CHART_TEMP_DIR);
			if (fileUrl == null || fileUrl.equals("")) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Problem generating chart..."));
				saveErrors(request, errors);
				return mapping.findForward("error");
			}
			form.setReportDataUrl(fileUrl);
			form.setChartTitle("Sample Ranking Comparison");
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		// redirect according role
		RoleDO roleObject = (RoleDO) request.getSession().getAttribute(Constants.CURRENT_ROLE);
		String role = roleObject.getName();
		if (role.equals(Constants.ROLE_LOCALCONTACT)) {
			return mapping.findForward("localcontactdisplayFlexChart");
		} else {
			return mapping.findForward("displayFlexChart");
		}
	}

	/**
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward selectSamples(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages errors = new ActionMessages();
		SampleRankingForm form = (SampleRankingForm) actForm;
		HttpSession session = request.getSession();

		try {
			String status = request.getParameter("status");
			if (status.equals("cancel")) {
				Set<Integer> selectedItems = (Set<Integer>) session.getAttribute("selectedItems");
				form.setSelectedItems(selectedItems);
				return this.display(mapping, actForm, request, response);
			}
			String idListStr = request.getParameter("idList");
			LOG.debug("selectSamples: " + idListStr);

			String[] idList;
			String delimeter = ",";
			idList = idListStr.split(delimeter);
			Set<Integer> selectedItems = new HashSet<Integer>();
			for (int i = 0; i < idList.length; i++) {
				if (!idList[i].equals(""))
					selectedItems.add(Integer.valueOf(idList[i]));
			}
			form.setSelectedItems(selectedItems);
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		return this.display(mapping, actForm, request, response);
	}

	/**
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward drawDistributionChart(ActionMapping mapping, ActionForm actForm, HttpServletRequest request,
			HttpServletResponse response) {

		final int NB_SLOTS = 20;

		ActionMessages errors = new ActionMessages();
		SampleRankingForm form = (SampleRankingForm) actForm;
		HttpSession session = request.getSession();

		try {

			// Get sample ranking values
			List<SampleRankingVO> sampleRankingValuesAll = (List<SampleRankingVO>) session
					.getAttribute(Constants.SAMPLE_RANKING_VALUE_LIST);
			if (sampleRankingValuesAll.size() == 0)
				return null;

			// Calculate distribution
			Integer[] distribution = new Integer[NB_SLOTS];
			for (int i = 0; i < distribution.length; i++)
				distribution[i] = 0;
			System.out.println(">> Distribution1");
			for (int i = 0; i < sampleRankingValuesAll.size(); i++) {
				Double curVal = sampleRankingValuesAll.get(i).getTotalScore() / 100 * NB_SLOTS;
				int numSlot = curVal.intValue();
				distribution[numSlot]++;
				System.out.println("[" + i + "] " + curVal + " " + numSlot);
			}
			System.out.println(">> Distribution2");
			for (int i = 0; i < distribution.length; i++) {
				System.out.println("[" + i + "] " + distribution[i]);
			}

			// Build chart
			String fileUrl = SampleRankingChart.buildDistributionChart(request, "Sample Ranking Distribution", distribution,
					Constants.CHART_TEMP_DIR);
			if (fileUrl == null || fileUrl.equals("")) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", "Problem generating chart..."));
				saveErrors(request, errors);
				return mapping.findForward("error");
			}

			form.setReportDataUrl(fileUrl);
			form.setChartTitle("Sample Ranking Distribution");
		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return (mapping.findForward("error"));
		}

		return (mapping.findForward("displayChart"));
	}

}
