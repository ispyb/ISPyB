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

import ispyb.client.mx.ranking.SampleRankingVO;
import ispyb.client.mx.ranking.autoProcRanking.AutoProcRankingVO;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DataCollectionInformation {

	private ScreeningInformation screeningInformation = new ScreeningInformation();

	private SampleRankingVO sampleRankingVO = new SampleRankingVO();

	private AutoProcRankingVO autoProcRankingVO = new AutoProcRankingVO();
	
	private Integer dataCollectionId = null;

	private String dataCollectionDate = "";

	private String imagePrefix = "";

	private String dataCollectionNumber = "";

	private String numberOfImages = "";

	private String wavelength = "";

	private String detectorDistance = "";

	private String exposureTime = "";

	private String axisStart = "";

	private String axisRange = "";

	private String beamSizeAtSampleX = "";

	private String beamSizeAtSampleY = "";

	private String resolution = "";

	private String comments = "";

	private String pathJpgCrystal = ""; // Full path to original Images
	
	private String pathJpgCrystal3 = ""; // Third snapshot

	private String pathDiffractionImg1 = "";

	private String pathDiffractionImg2 = "";

	private String pathDiffractionImg3 = "";

	private String pathDNAPredictionImg = "";

	private String diffractionImgNumber1 = "";

	private String diffractionImgNumber2 = "";

	private String diffractionImgNumber3 = "";
	
	private Integer diffractionImgId1 = null;
	
	private Integer diffractionImgId2 = null;
	
	private Integer diffractionImgId3 = null;	

	private String pathJpgCrystal_resized = "";

	private String pathDiffractionImg1_resized = "";

	private String pathDiffractionImg2_resized = "";

	private String pathDiffractionImg3_resized = "";

	private String pathDNAPredictionImg_resized = "";
	
	private String pathEdnaGraph = "";
	
	private String pathAutoProcGraph = "";
	
	private String flux = "";

	// ------------- DNA Lattices ---------------------------
	private String spacegroup = "";

	private String cellA = "";

	private String cellB = "";

	private String cellC = "";

	private String cellAlpha = "";

	private String cellBeta = "";

	private String cellGamma = "";

	private String mosaicity = "";

	private String resObserved = "";

	private String iSigi = "";

	// ------------------ Screening Result ------------------
	private String screeningSuccess = "";

	private String screeningFailure = "";

	private String screeningNotDone = "";
	
	private Byte screeningIndexingSuccess = 0;
	
	private Byte screeningStrategySuccess = 0;

	// -------------- DNA Strategies ------------------------
	private String phiStart = "";

	private String phiEnd = "";

	private String rotation = "";

	private String tranmission = "";

	private String strategy_exposureTime = "";

	private String startegy_resolution = "";

	private String totalExposure = "";

	private String nbImages = "";

	private String program = "";
	
	private String autoProcFastStatus = "";
	
	private String autoProcParallelStatus = "";
	
	private String autoProcEdnaStatus = "";

	private String autoProcAutoPROCStatus = "";

	private String autoProcXia2DialsStatus = "";

	private String autoProcFastDPStatus = "";
	
	// -------------- Strategy Wedge & Strategy SubWedge --------------
	private List<StrategyWedgeInformation> listStrategyWedgeInformation = new ArrayList<StrategyWedgeInformation>();

	// ------------------------------------------------------

	public ScreeningInformation getScreeningInformation() {
		return screeningInformation;
	}

	public void setScreeningInformation(ScreeningInformation screeningInformation) {
		this.screeningInformation = screeningInformation;
	}

	public String getDataCollectionDate() {
		return dataCollectionDate;
	}

	public void setDataCollectionDate(String dataCollectionDate) {
		this.dataCollectionDate = dataCollectionDate;
	}

	public String getImagePrefix() {
		return imagePrefix;
	}

	public void setImagePrefix(String imagePrefix) {
		this.imagePrefix = imagePrefix;
	}

	public String getDataCollectionNumber() {
		return dataCollectionNumber;
	}

	public void setDataCollectionNumber(String dataCollectionNumber) {
		this.dataCollectionNumber = dataCollectionNumber;
	}

	public String getNumberOfImages() {
		return numberOfImages;
	}

	public void setNumberOfImages(String numberOfImages) {
		this.numberOfImages = numberOfImages;
	}

	public String getWavelength() {
		return wavelength;
	}

	public void setWavelength(String wavelength) {
		this.wavelength = wavelength;
	}

	public String getDetectorDistance() {
		return detectorDistance;
	}

	public void setDetectorDistance(String detectorDistance) {
		this.detectorDistance = detectorDistance;
	}

	public String getExposureTime() {
		return exposureTime;
	}

	public void setExposureTime(String exposureTime) {
		this.exposureTime = exposureTime;
	}

	public String getAxisStart() {
		return axisStart;
	}

	public void setAxisStart(String axisStart) {
		this.axisStart = axisStart;
	}

	public String getAxisRange() {
		return axisRange;
	}

	public void setAxisRange(String axisRange) {
		this.axisRange = axisRange;
	}

	public String getBeamSizeAtSampleX() {
		return beamSizeAtSampleX;
	}

	public void setBeamSizeAtSampleX(String beamSizeAtSampleX) {
		this.beamSizeAtSampleX = beamSizeAtSampleX;
	}

	public String getBeamSizeAtSampleY() {
		return beamSizeAtSampleY;
	}

	public void setBeamSizeAtSampleY(String beamSizeAtSampleY) {
		this.beamSizeAtSampleY = beamSizeAtSampleY;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getPathjpgCrystal() {
		return pathJpgCrystal;
	}

	public String getPathDiffractionImg1() {
		return pathDiffractionImg1;
	}

	public void setPathDiffractionImg1(String pathDiffractionImg1) {
		this.pathDiffractionImg1 = pathDiffractionImg1;
	}

	public String getPathDiffractionImg2() {
		return pathDiffractionImg2;
	}

	public void setPathDiffractionImg2(String pathDiffractionImg2) {
		this.pathDiffractionImg2 = pathDiffractionImg2;
	}

	public String getPathDiffractionImg3() {
		return pathDiffractionImg3;
	}

	public void setPathDiffractionImg3(String pathDiffractionImg3) {
		this.pathDiffractionImg3 = pathDiffractionImg3;
	}

	public String getPathDNAPredictionImg() {
		return pathDNAPredictionImg;
	}

	public void setPathDNAPredictionImg(String pathDNAPredictionImg) {
		this.pathDNAPredictionImg = pathDNAPredictionImg;
	}

	public void setPathJpgCrystal(String pathJpgCrystal) {
		this.pathJpgCrystal = pathJpgCrystal;
	}

	public void setPathJpgCrystal_resized(String pathJpgCrystal_resized) {
		this.pathJpgCrystal_resized = pathJpgCrystal_resized;
	}

	public void setPathDiffractionImg1_resized(String pathDiffractionImg1_resized) {
		this.pathDiffractionImg1_resized = pathDiffractionImg1_resized;
	}

	public void setPathDiffractionImg2_resized(String pathDiffractionImg2_resized) {
		this.pathDiffractionImg2_resized = pathDiffractionImg2_resized;
	}

	public void setPathDiffractionImg3_resized(String pathDiffractionImg3_resized) {
		this.pathDiffractionImg3_resized = pathDiffractionImg3_resized;
	}

	public void setPathDNAPredictionImg_resized(String pathDNAPredictionImg_resized) {
		this.pathDNAPredictionImg_resized = pathDNAPredictionImg_resized;
	}

	public String getDiffractionImgNumber1() {
		return diffractionImgNumber1;
	}

	public void setDiffractionImgNumber1(String diffractionImgNumber1) {
		this.diffractionImgNumber1 = diffractionImgNumber1;
	}

	public String getDiffractionImgNumber2() {
		return diffractionImgNumber2;
	}

	public void setDiffractionImgNumber2(String diffractionImgNumber2) {
		this.diffractionImgNumber2 = diffractionImgNumber2;
	}

	public String getDiffractionImgNumber3() {
		return diffractionImgNumber3;
	}

	public void setDiffractionImgNumber3(String diffractionImgNumber3) {
		this.diffractionImgNumber3 = diffractionImgNumber3;
	}

	public InputStream getJpgCrystal() {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(this.pathJpgCrystal_resized);
		} catch (Exception e) {
		}
		return fis;
	}

	public InputStream getJpgDiffractionImg1() {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(this.pathDiffractionImg1_resized);
		} catch (Exception e) {
		}
		return fis;
	}

	public InputStream getJpgDiffractionImg2() {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(this.pathDiffractionImg2_resized);
		} catch (Exception e) {
		}
		return fis;
	}

	public InputStream getJpgDiffractionImg3() {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(this.pathDiffractionImg3_resized);
		} catch (Exception e) {
		}
		return fis;
	}

	public InputStream getJpgDNAPredictionImg() {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(this.pathDNAPredictionImg_resized);
		} catch (Exception e) {
		}
		return fis;
	}

	// ------------- DNA Lattices ---------------------------
	public String getSpacegroup() {
		return spacegroup;
	}

	public void setSpacegroup(String spacegroup) {
		this.spacegroup = spacegroup;
	}

	public String getCellA() {
		return cellA;
	}

	public void setCellA(String cellA) {
		this.cellA = cellA;
	}

	public String getCellB() {
		return cellB;
	}

	public void setCellB(String cellB) {
		this.cellB = cellB;
	}

	public String getCellC() {
		return cellC;
	}

	public void setCellC(String cellC) {
		this.cellC = cellC;
	}

	public String getCellAlpha() {
		return cellAlpha;
	}

	public void setCellAlpha(String cellAlpha) {
		this.cellAlpha = cellAlpha;
	}

	public String getCellBeta() {
		return cellBeta;
	}

	public void setCellBeta(String cellBeta) {
		this.cellBeta = cellBeta;
	}

	public String getCellGamma() {
		return cellGamma;
	}

	public void setCellGamma(String cellGamma) {
		this.cellGamma = cellGamma;
	}

	public String getMosaicity() {
		return mosaicity;
	}

	public void setMosaicity(String mosaicity) {
		this.mosaicity = mosaicity;
	}

	public String getResObserved() {
		return resObserved;
	}

	public void setResObserved(String resObserved) {
		this.resObserved = resObserved;
	}

	public String getISigi() {
		return iSigi;
	}

	public void setISigi(String sigi) {
		this.iSigi = sigi;
	}

	public String getD1() {
		return spacegroup;
	}

	public String getD2() {
		return cellA;
	}

	public String getD3() {
		return cellB;
	}

	public String getD4() {
		return cellC;
	}

	public String getD5() {
		return cellAlpha;
	}

	public String getD6() {
		return cellBeta;
	}

	public String getD7() {
		return cellGamma;
	}

	public String getD8() {
		return mosaicity;
	}

	public String getD9() {
		return resObserved;
	}

	public String getD10() {
		return iSigi;
	}

	// ------------------ Screening Result -----------------
	public String getScreeningSuccess() {
		return screeningSuccess;
	}

	public void setScreeningSuccess(String screeningSuccess) {
		this.screeningSuccess = screeningSuccess;
	}

	public String getScreeningFailure() {
		return screeningFailure;
	}

	public void setScreeningFailure(String screeningFailure) {
		this.screeningFailure = screeningFailure;
	}

	public String getScreeningNotDone() {
		return screeningNotDone;
	}

	public void setScreeningNotDone(String screeningNotDone) {
		this.screeningNotDone = screeningNotDone;
	}

	// -------------- DNA Strategies ------------------------
	public String getPhiStart() {
		return phiStart;
	}

	public void setPhiStart(String phiStart) {
		this.phiStart = phiStart;
	}

	public String getPhiEnd() {
		return phiEnd;
	}

	public void setPhiEnd(String phiEnd) {
		this.phiEnd = phiEnd;
	}

	public String getRotation() {
		return rotation;
	}

	public void setRotation(String rotation) {
		this.rotation = rotation;
	}

	public String getTransmission() {
		return tranmission;
	}

	public void setTransmission(String tranmission) {
		this.tranmission = tranmission;
	}

	public String getStrategy_exposureTime() {
		return strategy_exposureTime;
	}

	public void setStrategy_exposureTime(String strategy_exposureTime) {
		this.strategy_exposureTime = strategy_exposureTime;
	}

	public String getStartegy_resolution() {
		return startegy_resolution;
	}

	public void setStartegy_resolution(String startegy_resolution) {
		this.startegy_resolution = startegy_resolution;
	}

	public String getTotalExposure() {
		return totalExposure;
	}

	public void setTotalExposure(String totalExposure) {
		this.totalExposure = totalExposure;
	}

	public String getNbImages() {
		return nbImages;
	}

	public void setNbImages(String nbImages) {
		this.nbImages = nbImages;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public String getS1() {
		return phiStart;
	}

	public String getS2() {
		return phiEnd;
	}

	public String getS3() {
		return rotation;
	}

	public String getS4() {
		return strategy_exposureTime;
	}

	public String getS5() {
		return startegy_resolution;
	}

	public String getS6() {
		return totalExposure;
	}

	public String getS7() {
		return nbImages;
	}

	public String getS8() {
		return program;
	}

	private boolean hasStrategy = false;

	public void setHasStrategy(boolean hasStrategy) {
		this.hasStrategy = hasStrategy;
	}

	public boolean hasStrategy() {
		return hasStrategy;
	}

	// ------------------ StrategyWedge ------------------------
	public List<StrategyWedgeInformation> getListStrategyWedgeInformation() {
		return this.listStrategyWedgeInformation;
	}

	public void setListStrategyWedgeInformation(List<StrategyWedgeInformation> listStrategyWedgeInformation) {
		this.listStrategyWedgeInformation = listStrategyWedgeInformation;
	}

	// ------------------------------------------------------

	public SampleRankingVO getSampleRankingVO() {
		return this.sampleRankingVO;
	}

	public void setSampleRankingVO(SampleRankingVO sampleRankingVO) {
		this.sampleRankingVO = sampleRankingVO;
	}

	public AutoProcRankingVO getAutoProcRankingVO() {
		return autoProcRankingVO;
	}

	public void setAutoProcRankingVO(AutoProcRankingVO autoProcRankingVO) {
		this.autoProcRankingVO = autoProcRankingVO;
	}

	private boolean hasAutoprocAtt = false;

	public void setHasAutoprocAtt(boolean hasAutoprocAtt) {
		this.hasAutoprocAtt = hasAutoprocAtt;
	}

	public boolean getHasAutoprocAtt() {
		return hasAutoprocAtt;
	}

	public String getPathEdnaGraph() {
		return pathEdnaGraph;
	}

	public void setPathEdnaGraph(String pathEdnaGraph) {
		this.pathEdnaGraph = pathEdnaGraph;
	}

	public String getFlux() {
		return flux;
	}

	public void setFlux(String flux) {
		this.flux = flux;
	}

	public String getPathAutoProcGraph() {
		return pathAutoProcGraph;
	}

	public void setPathAutoProcGraph(String pathAutoProcGraph) {
		this.pathAutoProcGraph = pathAutoProcGraph;
	}

	public Integer getDataCollectionId() {
		return dataCollectionId;
	}

	public void setDataCollectionId(Integer dataCollectionId) {
		this.dataCollectionId = dataCollectionId;
	}

	public String getPathJpgCrystal3() {
		return pathJpgCrystal3;
	}

	public void setPathJpgCrystal3(String pathJpgCrystal3) {
		this.pathJpgCrystal3 = pathJpgCrystal3;
	}

	public String getAutoProcFastStatus() {
		return autoProcFastStatus;
	}

	public void setAutoProcFastStatus(String autoProcFastStatus) {
		this.autoProcFastStatus = autoProcFastStatus;
	}

	public String getAutoProcParallelStatus() {
		return autoProcParallelStatus;
	}

	public void setAutoProcParallelStatus(String autoProcParallelStatus) {
		this.autoProcParallelStatus = autoProcParallelStatus;
	}

	public String getAutoProcEdnaStatus() {
		return autoProcEdnaStatus;
	}

	public void setAutoProcEdnaStatus(String autoProcEdnaStatus) {
		this.autoProcEdnaStatus = autoProcEdnaStatus;
	}


	public String getAutoProcAutoPROCStatus() {
		return autoProcAutoPROCStatus;
	}

	public void setAutoProcAutoPROCStatus(String autoProcAutoPROCStatus) {
		this.autoProcAutoPROCStatus = autoProcAutoPROCStatus;
	}

	public String getAutoProcXia2DialsStatus() {
		return autoProcXia2DialsStatus;
	}

	public void setAutoProcXia2DialsStatus(String autoProcXia2DialsStatus) {
		this.autoProcXia2DialsStatus = autoProcXia2DialsStatus;
	}

	public String getAutoProcFastDPStatus() {
		return autoProcFastDPStatus;
	}

	public void setAutoProcFastDPStatus(String autoProcFastDPStatus) {
		this.autoProcFastDPStatus = autoProcFastDPStatus;
	}

	public Integer getDiffractionImgId1() {
		return diffractionImgId1;
	}

	public void setDiffractionImgId1(Integer diffractionImgId1) {
		this.diffractionImgId1 = diffractionImgId1;
	}

	public Integer getDiffractionImgId2() {
		return diffractionImgId2;
	}

	public void setDiffractionImgId2(Integer diffractionImgId2) {
		this.diffractionImgId2 = diffractionImgId2;
	}

	public Integer getDiffractionImgId3() {
		return diffractionImgId3;
	}

	public void setDiffractionImgId3(Integer diffractionImgId3) {
		this.diffractionImgId3 = diffractionImgId3;
	}

	public Byte getScreeningIndexingSuccess() {
		return screeningIndexingSuccess;
	}

	public void setScreeningIndexingSuccess(Byte screeningIndexingSuccess) {
		this.screeningIndexingSuccess = screeningIndexingSuccess;
	}

	public Byte getScreeningStrategySuccess() {
		return screeningStrategySuccess;
	}

	public void setScreeningStrategySuccess(Byte screeningStrategySuccess) {
		this.screeningStrategySuccess = screeningStrategySuccess;
	}

	

}
