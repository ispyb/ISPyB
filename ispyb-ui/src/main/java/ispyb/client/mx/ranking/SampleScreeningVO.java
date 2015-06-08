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
 * SampleScreeningVO.java
 * @author patrice.brenchereau@esrf.fr
 * March 09, 2009
 */

package ispyb.client.mx.ranking;

import java.util.List;

public class SampleScreeningVO {
	
	private java.lang.Integer 	dataCollectionId;
	private List<ScreeningVO>	screeningVOList;

	/**
	 * Screening
	 */
	class ScreeningVO {
		
		private java.lang.Integer 	screeningId;
		private java.sql.Timestamp 	timeStamp;
		private java.lang.String 	programVersion;
		private ScreeningOutputVO	screeningOutputVO;
		
		public java.lang.Integer getScreeningId() {
			return screeningId;
		}
		public void setScreeningId(java.lang.Integer screeningId) {
			this.screeningId = screeningId;
		}
		public java.sql.Timestamp getTimeStamp() {
			return timeStamp;
		}
		public void setTimeStamp(java.sql.Timestamp timeStamp) {
			this.timeStamp = timeStamp;
		}

		public java.lang.String getProgramVersion() {
			return programVersion;
		}
		public void setProgramVersion(java.lang.String programVersion) {
			this.programVersion = programVersion;
		}
		public ScreeningOutputVO getScreeningOutputVO() {
			return screeningOutputVO;
		}
		public void setScreeningOutputVO(ScreeningOutputVO screeningOutputVO) {
			this.screeningOutputVO = screeningOutputVO;
		}
	}
	
	
	/**
	 * ScreeningOutput
	 */
	class ScreeningOutputVO {
		
		private java.lang.Integer 	screeningOutputId;
		private java.lang.String 	statusDescription;
		private java.lang.Integer 	rejectedReflections;
		private java.lang.Double 	resolutionObtained;
		private java.lang.Double 	spotDeviationR;
		private java.lang.Double 	spotDeviationTheta;
		private java.lang.Double 	beamShiftX;
		private java.lang.Double 	beamShiftY;
		private java.lang.Integer 	numSpotsFound;
		private java.lang.Integer 	numSpotsUsed;
		private java.lang.Integer 	numSpotsRejected;
		private java.lang.Double 	mosaicity;
		private java.lang.Double 	ioverSigma;
		private java.lang.Byte 		diffractionRings;
		private java.lang.Byte 		indexingSuccess;
		private java.lang.Byte 		strategySuccess;
		private java.lang.Byte 		mosaicityEstimated;
		private java.lang.Double 	rankingResolution;
		private java.lang.String 	program;
		private java.lang.Double 	doseTotal;
		private java.lang.Double 	totalExposureTime;
		private java.lang.Double 	totalRotationRange;
		private java.lang.Integer 	totalNumberOfImages;
		private java.lang.Double 	rFriedel;
		private ScreeningOutputLatticeVO		screeningOutputLatticeVO;
		private List<ScreeningStrategyVO>	screeningStrategyVOList;
		
		public java.lang.Integer getScreeningOutputId() {
			return screeningOutputId;
		}
		public void setScreeningOutputId(java.lang.Integer screeningOutputId) {
			this.screeningOutputId = screeningOutputId;
		}
		public java.lang.String getStatusDescription() {
			return statusDescription;
		}
		public void setStatusDescription(java.lang.String statusDescription) {
			this.statusDescription = statusDescription;
		}
		public java.lang.Integer getRejectedReflections() {
			return rejectedReflections;
		}
		public void setRejectedReflections(java.lang.Integer rejectedReflections) {
			this.rejectedReflections = rejectedReflections;
		}
		public java.lang.Double getResolutionObtained() {
			return resolutionObtained;
		}
		public void setResolutionObtained(java.lang.Double resolutionObtained) {
			this.resolutionObtained = resolutionObtained;
		}
		public java.lang.Double getSpotDeviationR() {
			return spotDeviationR;
		}
		public void setSpotDeviationR(java.lang.Double spotDeviationR) {
			this.spotDeviationR = spotDeviationR;
		}
		public java.lang.Double getSpotDeviationTheta() {
			return spotDeviationTheta;
		}
		public void setSpotDeviationTheta(java.lang.Double spotDeviationTheta) {
			this.spotDeviationTheta = spotDeviationTheta;
		}
		public java.lang.Double getBeamShiftX() {
			return beamShiftX;
		}
		public void setBeamShiftX(java.lang.Double beamShiftX) {
			this.beamShiftX = beamShiftX;
		}
		public java.lang.Double getBeamShiftY() {
			return beamShiftY;
		}
		public void setBeamShiftY(java.lang.Double beamShiftY) {
			this.beamShiftY = beamShiftY;
		}
		public java.lang.Integer getNumSpotsFound() {
			return numSpotsFound;
		}
		public void setNumSpotsFound(java.lang.Integer numSpotsFound) {
			this.numSpotsFound = numSpotsFound;
		}
		public java.lang.Integer getNumSpotsUsed() {
			return numSpotsUsed;
		}
		public void setNumSpotsUsed(java.lang.Integer numSpotsUsed) {
			this.numSpotsUsed = numSpotsUsed;
		}
		public java.lang.Integer getNumSpotsRejected() {
			return numSpotsRejected;
		}
		public void setNumSpotsRejected(java.lang.Integer numSpotsRejected) {
			this.numSpotsRejected = numSpotsRejected;
		}
		public java.lang.Double getMosaicity() {
			return mosaicity;
		}
		public void setMosaicity(java.lang.Double mosaicity) {
			this.mosaicity = mosaicity;
		}
		public java.lang.Double getIoverSigma() {
			return ioverSigma;
		}
		public void setIoverSigma(java.lang.Double ioverSigma) {
			this.ioverSigma = ioverSigma;
		}
		public java.lang.Byte getDiffractionRings() {
			return diffractionRings;
		}
		public void setDiffractionRings(java.lang.Byte diffractionRings) {
			this.diffractionRings = diffractionRings;
		}
		
		public java.lang.Byte getIndexingSuccess() {
			return indexingSuccess;
		}
		public void setIndexingSuccess(java.lang.Byte indexingSuccess) {
			this.indexingSuccess = indexingSuccess;
		}
		public java.lang.Byte getStrategySuccess() {
			return strategySuccess;
		}
		public void setStrategySuccess(java.lang.Byte strategySuccess) {
			this.strategySuccess = strategySuccess;
		}
		public java.lang.Double getRankingResolution() {
			return rankingResolution;
		}
		public void setRankingResolution(java.lang.Double rankingResolution) {
			this.rankingResolution = rankingResolution;
		}
		public java.lang.String getProgram() {
			return program;
		}
		public void setProgram(java.lang.String program) {
			this.program = program;
		}
		public java.lang.Double getDoseTotal() {
			return doseTotal;
		}
		public void setDoseTotal(java.lang.Double doseTotal) {
			this.doseTotal = doseTotal;
		}
		public java.lang.Double getTotalExposureTime() {
			return totalExposureTime;
		}
		public void setTotalExposureTime(java.lang.Double totalExposureTime) {
			this.totalExposureTime = totalExposureTime;
		}
		public java.lang.Double getTotalRotationRange() {
			return totalRotationRange;
		}
		public void setTotalRotationRange(java.lang.Double totalRotationRange) {
			this.totalRotationRange = totalRotationRange;
		}
		public java.lang.Integer getTotalNumberOfImages() {
			return totalNumberOfImages;
		}
		public void setTotalNumberOfImages(java.lang.Integer totalNumberOfImages) {
			this.totalNumberOfImages = totalNumberOfImages;
		}
		public java.lang.Double getrFriedel() {
			return rFriedel;
		}
		public void setrFriedel(java.lang.Double rFriedel) {
			this.rFriedel = rFriedel;
		}
		public ScreeningOutputLatticeVO getScreeningOutputLatticeVO() {
			return screeningOutputLatticeVO;
		}
		public void setScreeningOutputLatticeVO(
				ScreeningOutputLatticeVO screeningOutputLatticeVO) {
			this.screeningOutputLatticeVO = screeningOutputLatticeVO;
		}
		public List<ScreeningStrategyVO> getScreeningStrategyVOList() {
			return screeningStrategyVOList;
		}
		public void setScreeningStrategyVOList(
				List<ScreeningStrategyVO> screeningStrategyVOList) {
			this.screeningStrategyVOList = screeningStrategyVOList;
		}
		public java.lang.Byte getMosaicityEstimated() {
			return mosaicityEstimated;
		}
		public void setMosaicityEstimated(java.lang.Byte mosaicityEstimated) {
			this.mosaicityEstimated = mosaicityEstimated;
		}
	}
	
	/**
	 * ScreeningOutputLattice
	 */
	class ScreeningOutputLatticeVO {
		
		private java.lang.Integer 	screeningOutputLatticeId;
		private java.lang.String 	spaceGroup;
		private java.lang.String 	pointGroup;
		private java.lang.String 	bravaisLattice;
		private java.lang.Double 	rawOrientationMatrixAX;
		private java.lang.Double 	rawOrientationMatrixAY;
		private java.lang.Double 	rawOrientationMatrixAZ;
		private java.lang.Double 	rawOrientationMatrixBX;
		private java.lang.Double 	rawOrientationMatrixBY;
		private java.lang.Double 	rawOrientationMatrixBZ;
		private java.lang.Double 	rawOrientationMatrixCX;
		private java.lang.Double 	rawOrientationMatrixCY;
		private java.lang.Double 	rawOrientationMatrixCZ;
		private java.lang.Double 	unitCellA;
		private java.lang.Double 	unitCellB;
		private java.lang.Double 	unitCellC;
		private java.lang.Double 	unitCellAlpha;
		private java.lang.Double 	unitCellBeta;
		private java.lang.Double 	unitCellGamma;
		private java.sql.Timestamp 	timeStamp;
		
		public java.lang.Integer getScreeningOutputLatticeId() {
			return screeningOutputLatticeId;
		}
		public void setScreeningOutputLatticeId(
				java.lang.Integer screeningOutputLatticeId) {
			this.screeningOutputLatticeId = screeningOutputLatticeId;
		}
		public java.lang.String getSpaceGroup() {
			return spaceGroup;
		}
		public void setSpaceGroup(java.lang.String spaceGroup) {
			this.spaceGroup = spaceGroup;
		}
		public java.lang.String getPointGroup() {
			return pointGroup;
		}
		public void setPointGroup(java.lang.String pointGroup) {
			this.pointGroup = pointGroup;
		}
		public java.lang.String getBravaisLattice() {
			return bravaisLattice;
		}
		public void setBravaisLattice(java.lang.String bravaisLattice) {
			this.bravaisLattice = bravaisLattice;
		}
		public java.lang.Double getRawOrientationMatrixAX() {
			return rawOrientationMatrixAX;
		}
		public void setRawOrientationMatrixAX(java.lang.Double rawOrientationMatrixAX) {
			this.rawOrientationMatrixAX = rawOrientationMatrixAX;
		}
		public java.lang.Double getRawOrientationMatrixAY() {
			return rawOrientationMatrixAY;
		}
		public void setRawOrientationMatrixAY(java.lang.Double rawOrientationMatrixAY) {
			this.rawOrientationMatrixAY = rawOrientationMatrixAY;
		}
		public java.lang.Double getRawOrientationMatrixAZ() {
			return rawOrientationMatrixAZ;
		}
		public void setRawOrientationMatrixAZ(java.lang.Double rawOrientationMatrixAZ) {
			this.rawOrientationMatrixAZ = rawOrientationMatrixAZ;
		}
		public java.lang.Double getRawOrientationMatrixBX() {
			return rawOrientationMatrixBX;
		}
		public void setRawOrientationMatrixBX(java.lang.Double rawOrientationMatrixBX) {
			this.rawOrientationMatrixBX = rawOrientationMatrixBX;
		}
		public java.lang.Double getRawOrientationMatrixBY() {
			return rawOrientationMatrixBY;
		}
		public void setRawOrientationMatrixBY(java.lang.Double rawOrientationMatrixBY) {
			this.rawOrientationMatrixBY = rawOrientationMatrixBY;
		}
		public java.lang.Double getRawOrientationMatrixBZ() {
			return rawOrientationMatrixBZ;
		}
		public void setRawOrientationMatrixBZ(java.lang.Double rawOrientationMatrixBZ) {
			this.rawOrientationMatrixBZ = rawOrientationMatrixBZ;
		}
		public java.lang.Double getRawOrientationMatrixCX() {
			return rawOrientationMatrixCX;
		}
		public void setRawOrientationMatrixCX(java.lang.Double rawOrientationMatrixCX) {
			this.rawOrientationMatrixCX = rawOrientationMatrixCX;
		}
		public java.lang.Double getRawOrientationMatrixCY() {
			return rawOrientationMatrixCY;
		}
		public void setRawOrientationMatrixCY(java.lang.Double rawOrientationMatrixCY) {
			this.rawOrientationMatrixCY = rawOrientationMatrixCY;
		}
		public java.lang.Double getRawOrientationMatrixCZ() {
			return rawOrientationMatrixCZ;
		}
		public void setRawOrientationMatrixCZ(java.lang.Double rawOrientationMatrixCZ) {
			this.rawOrientationMatrixCZ = rawOrientationMatrixCZ;
		}
		public java.lang.Double getUnitCellA() {
			return unitCellA;
		}
		public void setUnitCellA(java.lang.Double unitCellA) {
			this.unitCellA = unitCellA;
		}
		public java.lang.Double getUnitCellB() {
			return unitCellB;
		}
		public void setUnitCellB(java.lang.Double unitCellB) {
			this.unitCellB = unitCellB;
		}
		public java.lang.Double getUnitCellC() {
			return unitCellC;
		}
		public void setUnitCellC(java.lang.Double unitCellC) {
			this.unitCellC = unitCellC;
		}
		public java.lang.Double getUnitCellAlpha() {
			return unitCellAlpha;
		}
		public void setUnitCellAlpha(java.lang.Double unitCellAlpha) {
			this.unitCellAlpha = unitCellAlpha;
		}
		public java.lang.Double getUnitCellBeta() {
			return unitCellBeta;
		}
		public void setUnitCellBeta(java.lang.Double unitCellBeta) {
			this.unitCellBeta = unitCellBeta;
		}
		public java.lang.Double getUnitCellGamma() {
			return unitCellGamma;
		}
		public void setUnitCellGamma(java.lang.Double unitCellGamma) {
			this.unitCellGamma = unitCellGamma;
		}
		public java.sql.Timestamp getTimeStamp() {
			return timeStamp;
		}
		public void setTimeStamp(java.sql.Timestamp timeStamp) {
			this.timeStamp = timeStamp;
		}
	}
	
	/**
	 * ScreeningStrategy
	 */
	class ScreeningStrategyVO {
		private java.lang.Integer screeningStrategyId;
		private java.lang.Double phiStart;
		private java.lang.Double phiEnd;
		private java.lang.Double rotation;
		private java.lang.Double exposureTime;
		private java.lang.Double resolution;
		private java.lang.Double completeness;
		private java.lang.Double multiplicity;
		private java.lang.Byte anomalous;
		private java.lang.String program;
		private java.lang.Double rankingResolution;
		private List<ScreeningStrategyWedgeVO>	screeningStrategyWedgeVOList;
		
		public java.lang.Integer getScreeningStrategyId() {
			return screeningStrategyId;
		}
		public void setScreeningStrategyId(java.lang.Integer screeningStrategyId) {
			this.screeningStrategyId = screeningStrategyId;
		}
		public java.lang.Double getPhiStart() {
			return phiStart;
		}
		public void setPhiStart(java.lang.Double phiStart) {
			this.phiStart = phiStart;
		}
		public java.lang.Double getPhiEnd() {
			return phiEnd;
		}
		public void setPhiEnd(java.lang.Double phiEnd) {
			this.phiEnd = phiEnd;
		}
		public java.lang.Double getRotation() {
			return rotation;
		}
		public void setRotation(java.lang.Double rotation) {
			this.rotation = rotation;
		}
		public java.lang.Double getExposureTime() {
			return exposureTime;
		}
		public void setExposureTime(java.lang.Double exposureTime) {
			this.exposureTime = exposureTime;
		}
		public java.lang.Double getResolution() {
			return resolution;
		}
		public void setResolution(java.lang.Double resolution) {
			this.resolution = resolution;
		}
		public java.lang.Double getCompleteness() {
			return completeness;
		}
		public void setCompleteness(java.lang.Double completeness) {
			this.completeness = completeness;
		}
		public java.lang.Double getMultiplicity() {
			return multiplicity;
		}
		public void setMultiplicity(java.lang.Double multiplicity) {
			this.multiplicity = multiplicity;
		}
		public java.lang.Byte getAnomalous() {
			return anomalous;
		}
		public void setAnomalous(java.lang.Byte anomalous) {
			this.anomalous = anomalous;
		}
		public java.lang.String getProgram() {
			return program;
		}
		public void setProgram(java.lang.String program) {
			this.program = program;
		}
		public java.lang.Double getRankingResolution() {
			return rankingResolution;
		}
		public void setRankingResolution(java.lang.Double rankingResolution) {
			this.rankingResolution = rankingResolution;
		}
		public List<ScreeningStrategyWedgeVO> getScreeningStrategyWedgeVOList() {
			return screeningStrategyWedgeVOList;
		}
		public void setScreeningStrategyWedgeVOList(
				List<ScreeningStrategyWedgeVO> screeningStrategyWedgeVOList) {
			this.screeningStrategyWedgeVOList = screeningStrategyWedgeVOList;
		}
	}
	
	/**
	 * ScreeningStrategyWedge
	 */
	class ScreeningStrategyWedgeVO {
		private java.lang.Integer screeningStrategyWedgeId;
		private java.lang.Integer wedgeNumber;
		private java.lang.Double resolution;
		private java.lang.Double completeness;
		private java.lang.Double multiplicity;
		private java.lang.Double doseTotal;
		private java.lang.Integer numberOfImages;
		private java.lang.Double phi;
		private java.lang.Double kappa;
		private java.lang.String comments;
		private java.lang.Double wavelength;
		
		public java.lang.Integer getScreeningStrategyWedgeId() {
			return screeningStrategyWedgeId;
		}
		public void setScreeningStrategyWedgeId(java.lang.Integer screeningStrategyWedgeId) {
			this.screeningStrategyWedgeId = screeningStrategyWedgeId;
		}
		public java.lang.Integer getWedgeNumber() {
			return wedgeNumber;
		}
		public void setWedgeNumber(java.lang.Integer wedgeNumber) {
			this.wedgeNumber = wedgeNumber;
		}
		public java.lang.Double getResolution() {
			return resolution;
		}
		public void setResolution(java.lang.Double resolution) {
			this.resolution = resolution;
		}
		public java.lang.Double getCompleteness() {
			return completeness;
		}
		public void setCompleteness(java.lang.Double completeness) {
			this.completeness = completeness;
		}
		public java.lang.Double getMultiplicity() {
			return multiplicity;
		}
		public void setMultiplicity(java.lang.Double multiplicity) {
			this.multiplicity = multiplicity;
		}
		public java.lang.Double getDoseTotal() {
			return doseTotal;
		}
		public void setDoseTotal(java.lang.Double doseTotal) {
			this.doseTotal = doseTotal;
		}
		public java.lang.Integer getNumberOfImages() {
			return numberOfImages;
		}
		public void setNumberOfImages(java.lang.Integer numberOfImages) {
			this.numberOfImages = numberOfImages;
		}
		public java.lang.Double getPhi() {
			return phi;
		}
		public void setPhi(java.lang.Double phi) {
			this.phi = phi;
		}
		public java.lang.Double getKappa() {
			return kappa;
		}
		public void setKappa(java.lang.Double kappa) {
			this.kappa = kappa;
		}
		public java.lang.String getComments() {
			return comments;
		}
		public void setComments(java.lang.String comments) {
			this.comments = comments;
		}
		public java.lang.Double getWavelength() {
			return wavelength;
		}
		public void setWavelength(java.lang.Double wavelength) {
			this.wavelength = wavelength;
		}
		
	}

	
	
	public Integer getDataCollectionId() {
		return dataCollectionId;
	}

	public void setDataCollectionId(Integer dataCollectionId) {
		this.dataCollectionId = dataCollectionId;
	}

	public List<ScreeningVO> getScreeningVOList() {
		return screeningVOList;
	}

	public void setScreeningVOList(List<ScreeningVO> screeningVOList) {
		this.screeningVOList = screeningVOList;
	}


}
