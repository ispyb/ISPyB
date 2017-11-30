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

package ispyb.server.mx.vos.collections;

import ispyb.common.util.StringUtils;
import ispyb.server.common.vos.ISPyBValueObject;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.screening.Screening3VO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * DataCollectionGroup3 value object mapping table DataCollectionGroup
 * 
 */
@Entity
@Table(name = "DataCollectionGroup")
@SqlResultSetMapping(name = "dataCollectionGroupNativeQuery", entities = { @EntityResult(entityClass = DataCollectionGroup3VO.class) })
public class DataCollectionGroup3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(DataCollectionGroup3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "dataCollectionGroupId")
	protected Integer dataCollectionGroupId;
	
	@ManyToOne
	@JoinColumn(name = "sessionId")
	private Session3VO sessionVO;
	
	@ManyToOne
	@JoinColumn(name = "workflowId")
	protected Workflow3VO workflowVO;
	
	@ManyToOne
	@JoinColumn(name = "blSampleId")
	protected BLSample3VO blSampleVO;
	
	@Column(name = "experimentType")
	protected String experimentType;
	
	@Column(name = "startTime")
	protected Date startTime;
	
	@Column(name = "endTime")
	protected Date endTime;
	
	@Column(name = "crystalClass")
	protected String crystalClass;
	
	@Column(name = "comments")
	protected String comments;
	
	@Column(name = "detectorMode")
	protected String detectorMode;
	
	@Column(name = "actualSampleBarcode")
	protected String actualSampleBarcode;
	
	@Column(name = "actualSampleSlotInContainer")
	protected Integer actualSampleSlotInContainer;
	
	@Column(name = "actualContainerBarcode")
	protected String actualContainerBarcode;
	
	@Column(name = "actualContainerSlotInSC")
	protected Integer actualContainerSlotInSC;
	
	@Column(name = "xtalSnapshotFullPath")
	protected String xtalSnapshotFullPath;
	
	@OneToMany
	@JoinColumn(name = "dataCollectionGroupId")
	protected Set<DataCollection3VO> dataCollectionVOs;
	
	@OneToMany
	@JoinColumn(name = "dataCollectionGroupId")
	private Set<Screening3VO> screeningVOs;
	

	public DataCollectionGroup3VO() {
		super();
	}

	
	
	public DataCollectionGroup3VO(Integer dataCollectionGroupId,
			Session3VO sessionVO, BLSample3VO blSampleVO, Workflow3VO workflowVO, 
			String experimentType, Date startTime, Date endTime,
			String crystalClass, String comments, String detectorMode,
			String actualSampleBarcode, Integer actualSampleSlotInContainer,
			String actualContainerBarcode, Integer actualContainerSlotInSC, String xtalSnapshotFullPath) {
		super();
		this.dataCollectionGroupId = dataCollectionGroupId;
		this.sessionVO = sessionVO;
		this.blSampleVO = blSampleVO;
		this.workflowVO = workflowVO;
		this.experimentType = experimentType;
		this.startTime = startTime;
		this.endTime = endTime;
		this.crystalClass = crystalClass;
		this.comments = comments;
		this.detectorMode = detectorMode;
		this.actualSampleBarcode = actualSampleBarcode;
		this.actualSampleSlotInContainer = actualSampleSlotInContainer;
		this.actualContainerBarcode = actualContainerBarcode;
		this.actualContainerSlotInSC = actualContainerSlotInSC;
		this.xtalSnapshotFullPath =  xtalSnapshotFullPath;
	}
	
	public DataCollectionGroup3VO(DataCollectionGroup3VO vo) {
		super();
		this.dataCollectionGroupId = vo.getDataCollectionGroupId();
		this.sessionVO = vo.getSessionVO();
		this.blSampleVO = vo.getBlSampleVO();
		this.workflowVO = vo.getWorkflowVO();
		this.experimentType = vo.getExperimentType();
		this.startTime = vo.getStartTime();
		this.endTime = vo.getEndTime();
		this.crystalClass = vo.getCrystalClass();
		this.comments = vo.getComments();
		this.detectorMode = vo.getDetectorMode();
		this.actualSampleBarcode = vo.getActualSampleBarcode();
		this.actualSampleSlotInContainer = vo.getActualSampleSlotInContainer();
		this.actualContainerBarcode = vo.getActualContainerBarcode();
		this.actualContainerSlotInSC = vo.getActualContainerSlotInSC();	
		this.xtalSnapshotFullPath =  vo.getXtalSnapshotFullPath();
	}
	
	public void fillVOFromWS(DataCollectionGroupWS3VO vo) {
		this.dataCollectionGroupId = vo.getDataCollectionGroupId();
		this.experimentType = vo.getExperimentType();
		//this.startTime = vo.getStartTime();
		//this.endTime = vo.getEndTime();
		this.crystalClass = vo.getCrystalClass();
		this.comments = vo.getComments();
		this.detectorMode = vo.getDetectorMode();
		this.actualSampleBarcode = vo.getActualSampleBarcode();
		this.actualSampleSlotInContainer = vo.getActualSampleSlotInContainer();
		this.actualContainerBarcode = vo.getActualContainerBarcode();
		this.actualContainerSlotInSC = vo.getActualContainerSlotInSC();
		this.xtalSnapshotFullPath =  vo.getXtalSnapshotFullPath();
	}




	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getDataCollectionGroupId() {
		return dataCollectionGroupId;
	}

	/**
	 * @param pk The pk to set.
	 */
	public void setDataCollectionGroupId(Integer dataCollectionGroupId) {
		this.dataCollectionGroupId = dataCollectionGroupId;
	}

	
	public Session3VO getSessionVO() {
		return sessionVO;
	}



	public void setSessionVO(Session3VO sessionVO) {
		this.sessionVO = sessionVO;
	}



	public BLSample3VO getBlSampleVO() {
		return blSampleVO;
	}



	public void setBlSampleVO(BLSample3VO blSampleVO) {
		this.blSampleVO = blSampleVO;
	}


	public Workflow3VO getWorkflowVO() {
		return workflowVO;
	}

	public void setWorkflowVO(Workflow3VO workflowVO) {
		this.workflowVO = workflowVO;
	}



	public String getExperimentType() {
		return experimentType;
	}

	public void setExperimentType(String experimentType) {
		this.experimentType = experimentType;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getCrystalClass() {
		return crystalClass;
	}

	public void setCrystalClass(String crystalClass) {
		this.crystalClass = crystalClass;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getDetectorMode() {
		return detectorMode;
	}

	public void setDetectorMode(String detectorMode) {
		this.detectorMode = detectorMode;
	}

	public String getActualSampleBarcode() {
		return actualSampleBarcode;
	}

	public void setActualSampleBarcode(String actualSampleBarcode) {
		this.actualSampleBarcode = actualSampleBarcode;
	}

	public Integer getActualSampleSlotInContainer() {
		return actualSampleSlotInContainer;
	}

	public void setActualSampleSlotInContainer(Integer actualSampleSlotInContainer) {
		this.actualSampleSlotInContainer = actualSampleSlotInContainer;
	}

	public String getActualContainerBarcode() {
		return actualContainerBarcode;
	}

	public void setActualContainerBarcode(String actualContainerBarcode) {
		this.actualContainerBarcode = actualContainerBarcode;
	}

	public Integer getActualContainerSlotInSC() {
		return actualContainerSlotInSC;
	}

	public void setActualContainerSlotInSC(Integer actualContainerSlotInSC) {
		this.actualContainerSlotInSC = actualContainerSlotInSC;
	}
	
	public String getXtalSnapshotFullPath() {
		return xtalSnapshotFullPath;
	}
	
	public void setXtalSnapshotFullPath(String xtalSnapshotFullPath) {
		this.xtalSnapshotFullPath = xtalSnapshotFullPath;
	}

	public Integer getSessionVOId(){
		return sessionVO == null ? null : sessionVO.getSessionId();
	}
	
	public Integer getBlSampleVOId(){
		return blSampleVO == null ? null : blSampleVO.getBlSampleId();
	}
	
	public Integer getWorkflowVOId(){
		return workflowVO == null ? null : workflowVO.getWorkflowId();
	}
	
	public Set<DataCollection3VO> getDataCollectionVOs() {
		return dataCollectionVOs;
	}

	public void setDataCollectionVOs(Set<DataCollection3VO> dataCollectionVOs) {
		this.dataCollectionVOs = dataCollectionVOs;
	}
	
	public DataCollection3VO[] getDataCollectionsTab(){
		return this.dataCollectionVOs == null ? null : dataCollectionVOs.toArray(new DataCollection3VO[this.dataCollectionVOs.size()]);
	}
	
	public ArrayList<DataCollection3VO> getDataCollectionsList(){
		return this.dataCollectionVOs == null ? null : new ArrayList<DataCollection3VO>(Arrays.asList(getDataCollectionsTab()));
	}

	public Set<Screening3VO> getScreeningVOs() {
		return screeningVOs;
	}

	public void setScreeningVOs(Set<Screening3VO> screeningVOs) {
		this.screeningVOs = screeningVOs;
	}

	public Screening3VO[] getScreeningsTab(){
		return this.screeningVOs == null ? null : screeningVOs.toArray(new Screening3VO[this.screeningVOs.size()]);
	}
	
	public ArrayList<Screening3VO> getScreeningsList(){
		return this.screeningVOs == null ? null : new ArrayList<Screening3VO>(Arrays.asList(getScreeningsTab()));
	}

	/**
	 * Checks the values of this value object for correctness and
	 * completeness. Should be done before persisting the data in the DB.
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @throws Exception if the data of the value object is not correct
	 */
	public void checkValues(boolean create) throws Exception {
		int maxLengthCrystalClass = 20;
		int maxLengthComments = 1024;
		int maxLengthDetectorMode = 255;
		int maxLengthActualSampleBarcode = 45;
		int maxLengthActualContainerBarcode = 45;
		int maxLengthXtalSnapshotFullPath = 255;
		
		String[] listExperimentType = {"EM", "SAD","SAD - Inverse Beam","OSC","Collect - Multiwedge","MAD","Helical","Multi-positional","Mesh","Burn","MAD - Inverse Beam","Characterization", "Dehydration"};
		//session
		if(sessionVO == null)
			throw new IllegalArgumentException(StringUtils.getMessageRequiredField("DataCollectionGroup", "session"));
		// experimentType
		if(!StringUtils.isStringInPredefinedList(this.experimentType, listExperimentType, true))
			throw new IllegalArgumentException(StringUtils.getMessageErrorPredefinedList("DataCollectionGroup", "experimentType", listExperimentType));
		// crystalClass
		if(!StringUtils.isStringLengthValid(this.crystalClass, maxLengthCrystalClass))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("DataCollectionGroup", "crystalClass", maxLengthCrystalClass));
		// comments
		if(!StringUtils.isStringLengthValid(this.comments, maxLengthComments))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("DataCollectionGroup", "comments", maxLengthComments));
		// detectorMode
		if(!StringUtils.isStringLengthValid(this.detectorMode, maxLengthDetectorMode))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("DataCollectionGroup", "detectorMode", maxLengthDetectorMode));
		// actualSampleBarcode
		if(!StringUtils.isStringLengthValid(this.actualSampleBarcode, maxLengthActualSampleBarcode))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("DataCollectionGroup", "actualSampleBarcode", maxLengthActualSampleBarcode));
		// actualContainerBarcode
		if(!StringUtils.isStringLengthValid(this.actualContainerBarcode, maxLengthActualContainerBarcode))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("DataCollectionGroup", "actualContainerBarcode", maxLengthActualContainerBarcode));
		// xtalSnapshotFullPath
		if(!StringUtils.isStringLengthValid(this.xtalSnapshotFullPath, maxLengthXtalSnapshotFullPath))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("DataCollectionGroup", "xtalSnapshotFullPath", maxLengthXtalSnapshotFullPath));
		
	}

	public String toWSString(){
		String s = "dataCollectionGroupId="+this.dataCollectionGroupId +", "+
		"experimentType="+this.experimentType+", "+
		"startTime="+this.startTime+", "+
		"endTime="+this.endTime+", "+
		"crystalClass="+this.crystalClass+", "+
		"comments="+this.comments+", "+
		"detectorMode="+this.detectorMode+", "+
		"actualSampleBarcode="+this.actualSampleBarcode+", "+
		"actualSampleSlotInContainer="+this.actualSampleSlotInContainer+", "+
		"actualContainerBarcode="+this.actualContainerBarcode+", "+
		"actualContainerSlotInSC="+this.actualContainerSlotInSC+", "+
		"xtalSnapshotFullPath="+this.xtalSnapshotFullPath+", ";
		
		return s;
	}
	
	public boolean isFirstCollect(DataCollection3VO collect){
		if (collect != null && this.dataCollectionVOs != null && this.dataCollectionVOs.size() > 1){
			List<DataCollection3VO> list = this.getDataCollectionsList();
			DataCollection3VO vo = list.get(0);
			Date startTime = vo.getStartTime();
			int nb = list.size();
			for (int i=1; i<nb; i++){
				DataCollection3VO c = list.get(i);
				Date st = c.getStartTime();
				if (st != null && startTime != null && st.before(startTime)){
					startTime = st;
					vo = c;
				}
			}
			
			return vo.getDataCollectionId().equals(collect.getDataCollectionId());
		}
		return true;
	}
}
