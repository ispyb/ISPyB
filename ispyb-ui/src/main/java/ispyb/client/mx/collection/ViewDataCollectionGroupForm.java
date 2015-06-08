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
package ispyb.client.mx.collection;

import ispyb.common.util.StringUtils;
import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
import ispyb.server.mx.vos.collections.EnergyScan3VO;
import ispyb.server.mx.vos.collections.IspybCrystalClass3VO;
import ispyb.server.mx.vos.collections.IspybReference3VO;
import ispyb.server.mx.vos.collections.XFEFluorescenceSpectrum3VO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

/**
 * @struts.form name="viewDataCollectionGroupForm"
 */
public class ViewDataCollectionGroupForm extends ActionForm implements Serializable {
	static final long serialVersionUID = 0;
	
	private Boolean isUser;
	
	private boolean isDataCollectionGroupView = true;
	
	private Boolean isIndustrial;
	
	private Boolean editSkipAndComments;
	
	private Integer sessionId; // session Id for this collectionGroup
	private String sessionComments; // session comments

	private String sessionBeamLineOperator; // / BeamLine operator of the session
	private String sessionBeamLineOperatorEmail; // local contact email address
	private String mpEmail; // email of the main proposer only used for MXPress experiment
	// session fields for fx accounts
	private String sessionTitle;
	private String structureDeterminations;
	private String dewarTransport;
	private String dataBackupFrance;
	private String dataBackupEurope;
	
	private List<DataCollectionGroup3VO> listDataCollectionGroups = new ArrayList<DataCollectionGroup3VO>(); // List of CollectionGroups
	
	private List<XFEFluorescenceSpectrum3VO> listXFE = new ArrayList<XFEFluorescenceSpectrum3VO>(); // List of XFEFluorescencespectra

	private List<EnergyScan3VO> listEnergyScan = new ArrayList<EnergyScan3VO>(); // List of EnergyScans
	
	private List<IspybCrystalClass3VO> 	listOfCrystalClass = new ArrayList<IspybCrystalClass3VO>(); //list of all crystal classes in ispyb
	
	private List<Integer> listOfNbCrystalPerClass = new ArrayList<Integer>(); // number of crystals / crystal classes
	
	private String[] commentsList;

	private String[] crystalClassList;

	private String[] idList;
	
	private String[] commentsEnergyScanList;

	private String[] crystalClassEnergyScanList;

	private String[] idEnergyScanList;

	private String[] commentsXFEList;

	private String[] crystalClassXFEList;

	private String[] idXFEList;
	
	private List<IspybReference3VO> listReferences = new ArrayList<IspybReference3VO>();
	
	private String referenceText = "Please cite the appropriate references";
	
	private String selectedNbCollect;
	
	public void setIsDataCollectionGroupView(boolean isDataCollectionGroupView){
		this.isDataCollectionGroupView = isDataCollectionGroupView;
	}
	
	public boolean getIsDataCollectionGroupView(){
		return isDataCollectionGroupView;
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
	
	public Boolean getEditSkipAndComments() {
		return editSkipAndComments;
	}

	public void setEditSkipAndComments(Boolean editSkipAndComments) {
		this.editSkipAndComments = editSkipAndComments;
	}
	
	/**
	 * @return Returns the listXFE.
	 */
	public List<XFEFluorescenceSpectrum3VO> getListXFE() {
		return listXFE;
	}

	/**
	 * @param listXFE
	 *            The listXFE to set.
	 */
	public void setListXFE(List<XFEFluorescenceSpectrum3VO> listXFE) {
		this.listXFE = listXFE;
	}

	/**
	 * @return Returns the listEnergyScan.
	 */
	public List<EnergyScan3VO> getListEnergyScan() {
		return listEnergyScan;
	}

	/**
	 * @param listEnergyScan
	 *            The listEnergyScan to set.
	 */
	public void setListEnergyScan(List<EnergyScan3VO> listEnergyScan) {
		this.listEnergyScan = listEnergyScan;
	}
	
	public List<IspybCrystalClass3VO> getListOfCrystalClass() {
		return listOfCrystalClass;
	}

	public void setListOfCrystalClass(List<IspybCrystalClass3VO> listOfCrystalClass) {
		this.listOfCrystalClass = listOfCrystalClass;
	}
	
	public List<Integer> getListOfNbCrystalPerClass(){
		return this.listOfNbCrystalPerClass ;
	}
	
	public void setListOfNbCrystalPerClass(List<Integer> listOfNbCrystalPerClass){
		this.listOfNbCrystalPerClass =  listOfNbCrystalPerClass;
	}
	
	
	/**
	 * @return Returns the sessionId.
	 */
	public Integer getSessionId() {
		return sessionId;
	}
	
	/**
	 * @param sessionId
	 */
	public void setSessionId(Integer sessionId){
		this.sessionId = sessionId;
	}
	/**
	 * @return Returns the sessionComments.
	 */
	public String getSessionComments() {
		return sessionComments;
	}

	/**
	 * @param sessionComments
	 *            The sessionComments to set.
	 */
	public void setSessionComments(String sessionComments) {
		this.sessionComments = sessionComments;
	}

	/**
	 * @return Returns the sessionBeamLineOperator.
	 */
	public String getSessionBeamLineOperator() {
		return sessionBeamLineOperator;
	}

	/**
	 * @param sessionBeamLineOperator
	 *            The sessionBeamLineOperator to set.
	 */
	public void setSessionBeamLineOperator(String sessionBeamLineOperator) {
		this.sessionBeamLineOperator = sessionBeamLineOperator;
	}
	
	/**
	 * @return Returns the sessionBeamLineOperatorEmail.
	 */
	public String getSessionBeamLineOperatorEmail() {
		return sessionBeamLineOperatorEmail;
	}

	/**
	 * @param sessionBeamLineOperatorEmail
	 *            The sessionBeamLineOperatorEmail to set.
	 */
	public void setSessionBeamLineOperatorEmail(String sessionBeamLineOperatorEmail) {
		this.sessionBeamLineOperatorEmail = sessionBeamLineOperatorEmail;
	}
	
	/**
	 * @return Returns the sessionTitle
	 */
	public String getSessionTitle() {
		return sessionTitle;
	}

	/**
	 * @param sessionTitle
	 *            The sessionTitle to set.
	 */
	public void setSessionTitle(String sessionTitle) {
		this.sessionTitle = sessionTitle;
	}
	
	/**
	 * @return Returns the structureDeterminations
	 */
	public String getStructureDeterminations() {
		return this.structureDeterminations;
	}

	/**
	 * @param structureDeterminations
	 *            The structureDeterminations to set.
	 */
	public void setStructureDeterminations(String structureDeterminations) {
		this.structureDeterminations = structureDeterminations;
	}
	
	/**
	 * @return Returns the dewarTransport
	 */
	public String getDewarTransport() {
		return dewarTransport;
	}

	/**
	 * @param dewarTransport
	 *            The dewarTransport to set.
	 */
	public void setDewarTransport(String dewarTransport) {
		this.dewarTransport = dewarTransport;
	}
	
	/**
	 * @return Returns the data backup & express delivery France
	 */
	public String getDataBackupFrance() {
		return dataBackupFrance;
	}

	/**
	 * @param dataBackupFrance
	 *            The dataBackupFrance to set.
	 */
	public void setDataBackupFrance(String dataBackupFrance) {
		this.dataBackupFrance = dataBackupFrance;
	}
	
	/**
	 * @return Returns the data backup & express delivery Europe
	 */
	public String getDataBackupEurope() {
		return dataBackupEurope;
	}

	/**
	 * @param dataBackupEurope
	 *            The dataBackupEurope to set.
	 */
	public void setDataBackupEurope(String dataBackupEurope) {
		this.dataBackupEurope = dataBackupEurope;
	}
	
	/**
	 * 
	 * @return Returns the dataCollectionGroups list
	 */
	public List<DataCollectionGroup3VO> getListDataCollectionGroups(){
		return this.listDataCollectionGroups;
	}
	
	/**
	 * 
	 * @param listDataCollectionGroups
	 */
	public void setListDataCollectionGroups(List<DataCollectionGroup3VO> listDataCollectionGroups){
		this.listDataCollectionGroups = listDataCollectionGroups;
	}
	
	/**
	 * @return Returns the mpEmail.
	 */
	public String getMpEmail() {
		return mpEmail;
	}

	/**
	 * @param mpEmail
	 *            The mpEmail to set.
	 */
	public void setMpEmail(String mpEmail) {
		this.mpEmail = mpEmail;
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
	
	public String[] getCrystalClassList(){
		return this.crystalClassList;
	}
	
	public void setCrystalClassList(String[] crystalClassList){
		this.crystalClassList = crystalClassList;
	}

	public String getCrystalClassList(int index) {

		return StringUtils.getArrayElement(index, crystalClassList);
	}
	
	public void setCrystalClassList(int index, String value) {
		String[] array = StringUtils.setArrayElement(value, index, crystalClassList);
		crystalClassList = array;
	}
	
	public String[] getIdList(){
		return this.idList;
	}
	
	public void setIdList(String[] idList){
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
	 * @return Returns the crystalClassEnergyScanList.
	 */
	public String[] getCrystalClassEnergyScanList() {
		return crystalClassEnergyScanList;
	}

	/**
	 * @param crystalClassEnergyScanList
	 *            The crystalClassEnergyScanList to set.
	 */
	public void setCrystalClassEnergyScanList(String[] crystalClassEnergyScanList) {
		this.crystalClassEnergyScanList = crystalClassEnergyScanList;
	}

	public String getCrystalClassEnergyScanList(int index) {

		return StringUtils.getArrayElement(index, crystalClassEnergyScanList);
	}

	public void setCrystalClassEnergyScanList(int index, String value) {

		String[] array = StringUtils.setArrayElement(value, index, crystalClassEnergyScanList);
		crystalClassEnergyScanList = array;
	}

	/**
	 * @return Returns the commentsEnergyScanList.
	 */
	public String[] getCommentsEnergyScanList() {
		return commentsEnergyScanList;
	}

	/**
	 * @param commentsList
	 *            The commentsList to set.
	 */
	public void setCommentsEnergyScanList(String[] commentsEnergyScanList) {
		this.commentsEnergyScanList = commentsEnergyScanList;
	}

	public String getCommentsEnergyScanList(int index) {

		return StringUtils.getArrayElement(index, commentsEnergyScanList);
	}

	public void setCommentsEnergyScanList(int index, String value) {

		String[] array = StringUtils.setArrayElement(value, index, commentsEnergyScanList);
		commentsEnergyScanList = array;
	}

	/**
	 * @return Returns the idEnergyScanList.
	 */
	public String[] getIdEnergyScanList() {
		return idEnergyScanList;
	}

	/**
	 * @param idEnergyScanList
	 *            The idEnergyScanList to set.
	 */
	public void setIdEnergyScanList(String[] idEnergyScanList) {
		this.idEnergyScanList = idEnergyScanList;
	}

	public String getIdEnergyScanList(int index) {
		return StringUtils.getArrayElement(index, idEnergyScanList);
	}

	public void setIdEnergyScanList(int index, String value) {
		String[] array = StringUtils.setArrayElement(value, index, idEnergyScanList);
		idEnergyScanList = array;
	}
	
	/**
	 * @return Returns the crystalClassXFEList.
	 */
	public String[] getCrystalClassXFEList() {
		return crystalClassXFEList;
	}

	/**
	 * @param crystalClassXFEList
	 *            The crystalClassXFEList to set.
	 */
	public void setCrystalClassXFEList(String[] crystalClassXFEList) {
		this.crystalClassXFEList = crystalClassXFEList;
	}

	public String getCrystalClassXFEList(int index) {

		return StringUtils.getArrayElement(index, crystalClassXFEList);
	}

	public void setCrystalClassXFEList(int index, String value) {

		String[] array = StringUtils.setArrayElement(value, index, crystalClassXFEList);
		crystalClassXFEList = array;
	}

	/**
	 * @return Returns the commentsXFEList.
	 */
	public String[] getCommentsXFEList() {
		return commentsXFEList;
	}

	/**
	 * @param commentsList
	 *            The commentsList to set.
	 */
	public void setCommentsXFEList(String[] commentsXFEList) {
		this.commentsXFEList = commentsXFEList;
	}

	public String getCommentsXFEList(int index) {

		return StringUtils.getArrayElement(index, commentsXFEList);
	}

	public void setCommentsXFEList(int index, String value) {

		String[] array = StringUtils.setArrayElement(value, index, commentsXFEList);
		commentsXFEList = array;
	}

	/**
	 * @return Returns the idXFEList.
	 */
	public String[] getIdXFEList() {
		return idXFEList;
	}

	/**
	 * @param idXFEList
	 *            The idXFEList to set.
	 */
	public void setIdXFEList(String[] idXFEList) {
		this.idXFEList = idXFEList;
	}

	public String getIdXFEList(int index) {
		return StringUtils.getArrayElement(index, idXFEList);
	}

	public void setIdXFEList(int index, String value) {
		String[] array = StringUtils.setArrayElement(value, index, idXFEList);
		idXFEList = array;
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

	public String getSelectedNbCollect() {
		return selectedNbCollect;
	}

	public void setSelectedNbCollect(String selectedNbCollect) {
		this.selectedNbCollect = selectedNbCollect;
	}

}
