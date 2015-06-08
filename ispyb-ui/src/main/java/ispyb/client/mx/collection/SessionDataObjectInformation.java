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


import ispyb.client.mx.results.AutoProcessingData;

import java.util.List;


/**
 * A session data object represents data linked to a session: it could be a EnergyScan, a XRFSpectra, a Workflow, 
 * a DataDollectionGroup or a DataCollection
 * @author BODIN
 *
 */
public class SessionDataObjectInformation extends SessionDataObject {
	
	/* session information */
	protected Integer sessionId;
	
	/*experiment type, in case of workflow, it's the workflow type */
	protected String experimentType;
	
	/* information about sample and protein, if the link exists */
	protected String proteinAcronym ;
	
	protected String sampleName;
	
	protected String sampleNameProtein ;
	
	/* dc information, last collect for the workflow */
	protected String imagePrefix;
	
	protected String barcode;
	
	protected String samplePosition;
	
	protected Integer dataCollectionId;
	
	protected String runNumber;
	
	/* list of parameters (text, value) for a session data */
	protected List<Param> listParameters;
	
	protected String crystalClass;
	
	protected String comments;
	
	/* image thumbnail information */
	protected boolean imageThumbnailExist;
	
	protected Integer imageId;
	
	protected String imageThumbnailPath;
	
	/* crystal snapshot information */
	
	protected boolean crystalSnapshotExist;
	
	protected String crystalSnapshotPath;
	
	/* graph information, could be an image or a plot to display*/
	
	protected boolean graphExist;
	
	protected String graphPath;
	
	protected List<AutoProcessingData> graphData;
	
	/* second graph is possible in case of xray centering for example */
	
	protected boolean graph2Exist;
	
	protected String graph2Path;
	
	/* result status and a list of text, values results */

	protected String resultStatus;	
	
	protected String result;
	
	protected List<Param> listResults;
	
	
	
	public SessionDataObjectInformation() {
		super();
	}
	
	public SessionDataObjectInformation(SessionDataObject o) {
		super(o);
	}


	public String getImagePrefix() {
		return imagePrefix;
	}

	public Integer getSessionId() {
		return sessionId;
	}

	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}

	public void setImagePrefix(String imagePrefix) {
		this.imagePrefix = imagePrefix;
	}

	public Integer getDataCollectionId() {
		return dataCollectionId;
	}

	public void setDataCollectionId(Integer dataCollectionId) {
		this.dataCollectionId = dataCollectionId;
	}

	public String getRunNumber() {
		return runNumber;
	}

	public void setRunNumber(String runNumber) {
		this.runNumber = runNumber;
	}

	public List<Param> getListParameters() {
		return listParameters;
	}

	public void setListParameters(List<Param> listParameters) {
		this.listParameters = listParameters;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	
	public boolean isImageThumbnailExist() {
		return imageThumbnailExist;
	}

	public void setImageThumbnailExist(boolean imageThumbnailExist) {
		this.imageThumbnailExist = imageThumbnailExist;
	}

	public String getImageThumbnailPath() {
		return imageThumbnailPath;
	}

	public Integer getImageId() {
		return imageId;
	}

	public void setImageId(Integer imageId) {
		this.imageId = imageId;
	}

	public void setImageThumbnailPath(String imageThumbnailPath) {
		this.imageThumbnailPath = imageThumbnailPath;
	}

	
	public boolean isCrystalSnapshotExist() {
		return crystalSnapshotExist;
	}

	public void setCrystalSnapshotExist(boolean crystalSnapshotExist) {
		this.crystalSnapshotExist = crystalSnapshotExist;
	}

	public String getCrystalSnapshotPath() {
		return crystalSnapshotPath;
	}

	public void setCrystalSnapshotPath(String crystalSnapshotPath) {
		this.crystalSnapshotPath = crystalSnapshotPath;
	}
	
	public boolean isGraphExist() {
		return graphExist;
	}

	public void setGraphExist(boolean graphExist) {
		this.graphExist = graphExist;
	}

	public String getGraphPath() {
		return graphPath;
	}

	public void setGraphPath(String graphPath) {
		this.graphPath = graphPath;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getResultStatus() {
		return resultStatus;
	}

	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}

	public List<Param> getListResults() {
		return listResults;
	}

	public void setListResults(List<Param> listResults) {
		this.listResults = listResults;
	}

	public String getExperimentType() {
		return experimentType;
	}

	public void setExperimentType(String experimentType) {
		this.experimentType = experimentType;
	}

	public String getProteinAcronym() {
		return proteinAcronym;
	}

	public void setProteinAcronym(String proteinAcronym) {
		this.proteinAcronym = proteinAcronym;
	}

	public String getSampleName() {
		return sampleName;
	}

	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}

	public String getSampleNameProtein() {
		return sampleNameProtein;
	}

	public void setSampleNameProtein(String sampleNameProtein) {
		this.sampleNameProtein = sampleNameProtein;
	}
	
	public List<AutoProcessingData> getGraphData() {
		return graphData;
	}

	public void setGraphData(List<AutoProcessingData> graphData) {
		this.graphData = graphData;
	}
	
	public boolean isGraph2Exist() {
		return graph2Exist;
	}

	public void setGraph2Exist(boolean graph2Exist) {
		this.graph2Exist = graph2Exist;
	}

	public String getGraph2Path() {
		return graph2Path;
	}

	public void setGraph2Path(String graph2Path) {
		this.graph2Path = graph2Path;
	}

	public String getCrystalClass() {
		return crystalClass;
	}

	public void setCrystalClass(String crystalClass) {
		this.crystalClass = crystalClass;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getSamplePosition() {
		return samplePosition;
	}

	public void setSamplePosition(String samplePosition) {
		this.samplePosition = samplePosition;
	}

}
