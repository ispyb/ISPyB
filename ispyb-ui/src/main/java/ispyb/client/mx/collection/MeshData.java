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

import ispyb.server.mx.vos.collections.GridInfo3VO;

/**
 * data used for the map mesh, from the table of motor positions and image quality indicators.
 * @author BODIN
 *
 */
public class MeshData {
	
	protected Integer dataCollectionId;
	protected Integer imageId;
	protected Integer imageQualityIndicatorsId;
	protected String imageFileLocation;
	protected boolean imageFilePresent;
	protected String imagePrefix;
	protected Integer dataCollectionNumber;
	
	protected GridInfo3VO gridInfo;
	
	protected Integer spotTotal;
	protected Integer goodBraggCandidates;
	protected Float method1Res;
	protected Float method2Res;
	protected Double totalIntegratedSignal;
	protected Double dozor_score;
	
	public MeshData() {
		super();
	}

	

	public MeshData(Integer dataCollectionId, Integer imageId,
			Integer imageQualityIndicatorsId, String imageFileLocation,
			boolean imageFilePresent, String imagePrefix, Integer dataCollectionNumber, 
			GridInfo3VO gridInfo, 
			Integer spotTotal, Integer goodBraggCandidates, Float method1Res,
			Float method2Res, Double totalIntegratedSignal, Double dozor_score) {
		super();
		this.dataCollectionId = dataCollectionId;
		this.imageId = imageId;
		this.imageQualityIndicatorsId = imageQualityIndicatorsId;
		this.imageFileLocation = imageFileLocation;
		this.imageFilePresent = imageFilePresent;
		this.imagePrefix = imagePrefix;
		this.dataCollectionNumber = dataCollectionNumber;
		this.gridInfo = gridInfo;
		this.spotTotal = spotTotal;
		this.goodBraggCandidates = goodBraggCandidates;
		this.method1Res = method1Res;
		this.method2Res = method2Res;
		this.totalIntegratedSignal = totalIntegratedSignal;
		this.dozor_score = dozor_score;
	}



	public Integer getDataCollectionId() {
		return dataCollectionId;
	}

	public void setDataCollectionId(Integer dataCollectionId) {
		this.dataCollectionId = dataCollectionId;
	}

	public Integer getImageId() {
		return imageId;
	}

	public void setImageId(Integer imageId) {
		this.imageId = imageId;
	}

	public Integer getImageQualityIndicatorsId() {
		return imageQualityIndicatorsId;
	}

	public void setImageQualityIndicatorsId(Integer imageQualityIndicatorsId) {
		this.imageQualityIndicatorsId = imageQualityIndicatorsId;
	}

	public String getImageFileLocation() {
		return imageFileLocation;
	}

	public void setImageFileLocation(String imageFileLocation) {
		this.imageFileLocation = imageFileLocation;
	}

	public boolean isImageFilePresent() {
		return imageFilePresent;
	}

	public void setImageFilePresent(boolean imageFilePresent) {
		this.imageFilePresent = imageFilePresent;
	}

	public String getImagePrefix() {
		return imagePrefix;
	}

	public void setImagePrefix(String imagePrefix) {
		this.imagePrefix = imagePrefix;
	}

	public Integer getDataCollectionNumber() {
		return dataCollectionNumber;
	}

	public void setDataCollectionNumber(Integer dataCollectionNumber) {
		this.dataCollectionNumber = dataCollectionNumber;
	}
	
	public GridInfo3VO getGridInfo() {
		return gridInfo;
	}

	public void setGridInfo(GridInfo3VO gridInfo) {
		this.gridInfo = gridInfo;
	}

	public Integer getSpotTotal() {
		return spotTotal;
	}

	public void setSpotTotal(Integer spotTotal) {
		this.spotTotal = spotTotal;
	}

	public Integer getGoodBraggCandidates() {
		return goodBraggCandidates;
	}

	public void setGoodBraggCandidates(Integer goodBraggCandidates) {
		this.goodBraggCandidates = goodBraggCandidates;
	}

	public Float getMethod1Res() {
		return method1Res;
	}

	public void setMethod1Res(Float method1Res) {
		this.method1Res = method1Res;
	}

	public Float getMethod2Res() {
		return method2Res;
	}

	public void setMethod2Res(Float method2Res) {
		this.method2Res = method2Res;
	}

	public Double getTotalIntegratedSignal() {
		return totalIntegratedSignal;
	}

	public void setTotalIntegratedSignal(Double totalIntegratedSignal) {
		this.totalIntegratedSignal = totalIntegratedSignal;
	}

	public Double getDozor_score() {
		return dozor_score;
	}

	public void setDozor_score(Double dozor_score) {
		this.dozor_score = dozor_score;
	}
	
}
