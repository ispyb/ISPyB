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
 * ViewDataCollectionForm.java
 */
package ispyb.client.mx.collection;

import ispyb.common.util.Constants;
import ispyb.common.util.StringUtils;
import ispyb.server.mx.vos.autoproc.AutoProc3VO;
import ispyb.server.mx.vos.autoproc.AutoProcScalingStatistics3VO;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.IspybReference3VO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.struts.action.ActionForm;

/**
 * @struts.form name="viewDataCollectionForm"
 */
public class ViewDataCollectionForm extends ActionForm implements Serializable {

	static final long serialVersionUID = 0;
	
	private boolean isDataCollectionGroupView = false;

	private Boolean isUser;

	private Boolean isIndustrial;

	private Boolean editSkipAndComments;

	private String displayType; // DisplayForSession, DisplayForProtein,...

	private DataCollection3VO selectedDataCollection = new DataCollection3VO();

	private Boolean skip;

	private Boolean skipAll = false;
	
	private Boolean rankAll = true;

	private String DCcomments; // dataCollection other comments

	private String comments; // dataCollection comments

	private String crystalClass;

	private List<DataCollectionBean> listInfo = new ArrayList<DataCollectionBean>(); // List of Collections

	private String[] commentsList;

	private String[] idList;

	private String[] skipList = new String[1];
	
	private AutoProc3VO[] autoProcs;

	private AutoProcScalingStatistics3VO[] autoProcStatisticsOverall;

	private AutoProcScalingStatistics3VO[] autoProcStatisticsInner;

	private AutoProcScalingStatistics3VO[] autoProcStatisticsOuter;

	private String selectedProteinAcronym;
	
	private String selectedExperimentType;

	// Sample ranking
	private boolean firstDisplay = true;

	private Set<Integer> rankList = new HashSet<Integer>();
	
	private Set<Integer> rankAutoProcList = new HashSet<Integer>();
	
	private String selectedRankMode = "EDNA";

	// Collection sorting
	private String sortOrder = Constants.SORT_NoOrder;

	private String sampleName; // = null; // value searched

	private String proteinAcronym; // = null; // value searched

	private String beamlineName; // = null; // value searched

	private String experimentDateStart; // = null; // value searched

	private String experimentDateEnd; // = null; // value searched

	private String maxRecords; // = null; // max value searched

	private String nbRecords; // = null; // max value searched

	private String minNumberOfImages; // = null; // max value searched

	private String maxNumberOfImages; // = null; // max value searched
	
	private String imagePrefix ; // = null; // max value searched

	private Integer sessionId; // session Id for this collection

	private Integer theDataCollectionId;

	private boolean[] hasSnapshot;

	private List dataCollectionIds = new ArrayList();

	private double rMergeCutoff = 10; // default value of RSymm

	private double iSigmaCutoff = 1;

	private String htmlContent;
	
	private List<IspybReference3VO> listReferences = new ArrayList<IspybReference3VO>();
	
	private String referenceText = "Please cite the appropriate references";
	
	private String mode = "session";
	
	public Integer dataCollectionGroupId = null;

	
	// ______________________________________________________________________________________________________________________

	
	public double getRMergeCutoff() {
		return rMergeCutoff;
	}

	public void setRMergeCutoff(double mergeCutoff) {
		rMergeCutoff = mergeCutoff;
	}

	public double getISigmaCutoff() {
		return iSigmaCutoff;
	}

	public void setISigmaCutoff(double sigmaCutoff) {
		iSigmaCutoff = sigmaCutoff;
	}

	public ViewDataCollectionForm() {
		super();
	}

	/**
	 * @return Returns the comments.
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @return Returns the selectedDataCollection.
	 */
	public DataCollection3VO getSelectedDataCollection() {
		return selectedDataCollection;
	}

	/**
	 * @param selectedDataCollection
	 *            The selectedDataCollection to set.
	 */
	public void setSelectedDataCollection(DataCollection3VO selectedDataCollection) {
		this.selectedDataCollection = selectedDataCollection;
	}

	/**
	 * @return Returns the listInfo.
	 */
	public List<DataCollectionBean> getListInfo() {
		return listInfo;
	}

	/**
	 * @param listInfo
	 *            The listInfo to set.
	 */
	public void setListInfo(List<DataCollectionBean> listInfo) {
		this.listInfo = listInfo;
	}

	
	/**
	 * @return Returns the commentsList.
	 */
	public String[] getCommentsList() {
		return commentsList;
	}

	/**
	 * @param commentsList
	 *            The commentsList to set.
	 */
	public void setCommentsList(String[] commentsList) {
		this.commentsList = commentsList;
	}

	public String getCommentsList(int index) {

		return StringUtils.getArrayElement(index, commentsList);
	}

	public void setCommentsList(int index, String value) {

		String[] array = StringUtils.setArrayElement(value, index, commentsList);
		commentsList = array;
	}

	/**
	 * @return Returns the skipList.
	 */
	public String[] getSkipList() {
		return skipList;
	}

	/**
	 * @param skipList
	 *            The skipList to set.
	 */
	public void setSkipList(String[] skipList) {
		this.skipList = skipList;
	}

	public String getSkipList(int index) {

		return StringUtils.getArrayElement(index, skipList);
	}

	public void setSkipList(int index, String value) {

		String[] array = StringUtils.setArrayElement(value, index, skipList);
		skipList = array;
	}

	/**
	 * @return Returns the idList.
	 */
	public String[] getIdList() {
		return idList;
	}

	/**
	 * @param idList
	 *            The idList to set.
	 */
	public void setIdList(String[] idList) {
		this.idList = idList;
	}

	public String getIdList(int index) {
		return StringUtils.getArrayElement(index, idList);
	}

	public void setIdList(int index, String value) {
		String[] array = StringUtils.setArrayElement(value, index, idList);
		idList = array;
	}

	/**
	 * @return Returns the sampleName.
	 */
	public String getSampleName() {
		return sampleName;
	}

	/**
	 * @return Returns the sessionId.
	 */
	public Integer getSessionId() {
		return sessionId;
	}

	/**
	 * @param comments
	 *            The comments to set.
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @param sampleName
	 *            The sampleName to set.
	 */
	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}

	/**
	 * @return Returns the proteinAcronym.
	 */
	public String getProteinAcronym() {
		return proteinAcronym;
	}

	/**
	 * @param proteinAcronym
	 *            The proteinAcronym to set.
	 */
	public void setProteinAcronym(String proteinAcronym) {
		this.proteinAcronym = proteinAcronym;
	}

	/**
	 * @param sessionId
	 *            The sessionId to set.
	 */
	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return Returns the dataCollectionIds.
	 */
	public List getDataCollectionIds() {
		return dataCollectionIds;
	}

	/**
	 * @param dataCollectionIds
	 *            The dataCollectionIds to set.
	 */
	public void setDataCollectionIds(List dataCollectionIds) {
		this.dataCollectionIds = dataCollectionIds;
	}

	/**
	 * @return Returns the skip.
	 */
	public Boolean getSkip() {
		return skip;
	}

	/**
	 * @param skip
	 *            The skip to set.
	 */
	public void setSkip(Boolean skip) {
		this.skip = skip;
	}

	/**
	 * @return Returns the skip.
	 */
	public Boolean getSkipAll() {
		return skipAll;
	}

	/**
	 * @param skip
	 *            The skip to set.
	 */
	public void setSkipAll(Boolean skip) {
		this.skipAll = skip;
	}

	/**
	 * @return Returns the rankAll.
	 */
	public Boolean getRankAll() {
		return rankAll;
	}

	/**
	 * @param rankAll
	 *            The skip to set.
	 */
	public void setRankAll(Boolean rankAll) {
		this.rankAll = rankAll;
	}

	/**
	 * @return Returns the crystalClass.
	 */
	public String getCrystalClass() {
		return crystalClass;
	}

	/**
	 * @param crystalClass
	 *            The crystalClass to set.
	 */
	public void setCrystalClass(String crystalClass) {
		this.crystalClass = crystalClass;
	}

	/**
	 * @return Returns the dCcomments.
	 */
	public String getDCcomments() {
		return DCcomments;
	}

	/**
	 * @param ccomments
	 *            The dCcomments to set.
	 */
	public void setDCcomments(String ccomments) {
		DCcomments = ccomments;
	}

	/**
	 * @return Returns the theDataCollectionId.
	 */
	public Integer getTheDataCollectionId() {
		return theDataCollectionId;
	}

	/**
	 * @param theDataCollectionId
	 *            The theDataCollectionId to set.
	 */
	public void setTheDataCollectionId(Integer theDataCollectionId) {
		this.theDataCollectionId = theDataCollectionId;
	}

	public boolean[] getHasSnapshot() {
		return hasSnapshot;
	}

	public void setHasSnapshot(boolean[] hasSnapshot) {
		this.hasSnapshot = hasSnapshot;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Set<Integer> getRankList() {
		return rankList;
	}

	public void setRankList(Set<Integer> rankList) {
		this.rankList = rankList;
	}

	public String getRankList(int index) {
		if (this.rankList.contains(index))
			return "on";
		else
			return null;
	}

	public void setRankList(int index, String value) {
		if (value.equals("on"))
			this.rankList.add(index);
		else
			this.rankList.remove(index);
	}
	
	public Set<Integer> getRankAutoProcList() {
		return rankAutoProcList;
	}

	public void setRankAutoProcList(Set<Integer> rankAutoProcList) {
		this.rankAutoProcList = rankAutoProcList;
	}

	public String getRankAutoProcList(int index) {
		if (this.rankAutoProcList.contains(index))
			return "on";
		else
			return null;
	}

	public void setRankAutoProcList(int index, String value) {
		if (value.equals("on"))
			this.rankAutoProcList.add(index);
		else
			this.rankAutoProcList.remove(index);
	}
	

	public boolean isFirstDisplay() {
		return firstDisplay;
	}

	public void setFirstDisplay(boolean firstDisplay) {
		this.firstDisplay = firstDisplay;
	}

	public Boolean getIsUser() {
		return isUser;
	}

	public void setIsUser(Boolean isUser) {
		this.isUser = isUser;
		this.isIndustrial = !isUser;
	}

	public Boolean getIsIndustrial() {
		return isIndustrial;
	}

	public void setIsIndustrial(Boolean isIndustrial) {
		this.isIndustrial = isIndustrial;
		this.isUser = !isIndustrial;
	}

	public String getSelectedProteinAcronym() {
		return selectedProteinAcronym;
	}

	public void setSelectedProteinAcronym(String selectedProteinAcronym) {
		this.selectedProteinAcronym = selectedProteinAcronym;
	}

	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	public String getBeamlineName() {
		return beamlineName;
	}

	public void setBeamlineName(String beamlineName) {
		this.beamlineName = beamlineName;
	}

	public String getExperimentDateStart() {
		return experimentDateStart;
	}

	public void setExperimentDateStart(String experimentDateStart) {
		this.experimentDateStart = experimentDateStart;
	}

	public String getExperimentDateEnd() {
		return experimentDateEnd;
	}

	public void setExperimentDateEnd(String experimentDateEnd) {
		this.experimentDateEnd = experimentDateEnd;
	}

	public String getMaxRecords() {
		return maxRecords;
	}

	public void setMaxRecords(String maxRecords) {
		this.maxRecords = maxRecords;
	}

	public String getNbRecords() {
		return nbRecords;
	}

	public void setNbRecords(String nbRecords) {
		this.nbRecords = nbRecords;
	}

	public String getMinNumberOfImages() {
		return minNumberOfImages;
	}

	public void setMinNumberOfImages(String minNumberOfImages) {
		this.minNumberOfImages = minNumberOfImages;
	}

	public String getMaxNumberOfImages() {
		return maxNumberOfImages;
	}

	public void setMaxNumberOfImages(String maxNumberOfImages) {
		this.maxNumberOfImages = maxNumberOfImages;
	}


	public String getImagePrefix() {
		return imagePrefix;
	}

	public void setImagePrefix(String imagePrefix) {
		this.imagePrefix = imagePrefix;
	}

	public AutoProc3VO[] getAutoProcs() {
		return autoProcs;
	}

	public void setAutoProcs(AutoProc3VO[] autoProcs) {
		this.autoProcs = autoProcs;
	}

	public AutoProcScalingStatistics3VO[] getAutoProcStatisticsOverall() {
		return autoProcStatisticsOverall;
	}

	public void setAutoProcStatisticsOverall(AutoProcScalingStatistics3VO[] autoProcScalingStatistics) {
		this.autoProcStatisticsOverall = autoProcScalingStatistics;
	}

	public AutoProcScalingStatistics3VO[] getAutoProcStatisticsInner() {
		return autoProcStatisticsInner;
	}

	public void setAutoProcStatisticsInner(AutoProcScalingStatistics3VO[] autoProcScalingStatistics) {
		this.autoProcStatisticsInner = autoProcScalingStatistics;
	}

	public AutoProcScalingStatistics3VO[] getAutoProcStatisticsOuter() {
		return autoProcStatisticsOuter;
	}

	public void setAutoProcStatisticsOuter(AutoProcScalingStatistics3VO[] autoProcScalingStatistics) {
		this.autoProcStatisticsOuter = autoProcScalingStatistics;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

	public Boolean getEditSkipAndComments() {
		return editSkipAndComments;
	}

	public void setEditSkipAndComments(Boolean editSkipAndComments) {
		this.editSkipAndComments = editSkipAndComments;
	}

	
	public void setIsDataCollectionGroupView(boolean isDataCollectionGroupView){
		this.isDataCollectionGroupView = isDataCollectionGroupView;
	}
	
	public boolean getIsDataCollectionGroupView(){
		return isDataCollectionGroupView;
	}

	public String getSelectedExperimentType() {
		return selectedExperimentType;
	}

	public void setSelectedExperimentType(String selectedExperimentType) {
		this.selectedExperimentType = selectedExperimentType;
	}

	public String getSelectedRankMode() {
		return selectedRankMode;
	}

	public void setSelectedRankMode(String selectedRankMode) {
		this.selectedRankMode = selectedRankMode;
	}

	public List<IspybReference3VO> getListReferences() {
		return listReferences;
	}

	public void setListReferences(List<IspybReference3VO> listReferences) {
		this.listReferences = listReferences;
	}
	
	public String getReferenceText() {
		return referenceText;
	}

	public void setReferenceText(String referenceText) {
		this.referenceText = referenceText;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Integer getDataCollectionGroupId() {
		return dataCollectionGroupId;
	}

	public void setDataCollectionGroupId(Integer dataCollectionGroupId) {
		this.dataCollectionGroupId = dataCollectionGroupId;
	}

	
	
}
