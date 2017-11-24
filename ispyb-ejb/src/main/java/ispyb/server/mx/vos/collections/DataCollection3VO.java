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
import ispyb.server.mx.vos.autoproc.AutoProcIntegration3VO;
import ispyb.server.mx.vos.sample.BLSubSample3VO;
import ispyb.server.mx.vos.screening.Screening3VO;
import ispyb.server.mx.vos.screening.ScreeningStrategySubWedge3VO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
 * DataCollection3 value object mapping table DataCollection
 * 
 */
@Entity
@Table(name = "DataCollection")
@SqlResultSetMapping(name = "dataCollectionNativeQuery", entities = { @EntityResult(entityClass = DataCollection3VO.class) })
public class DataCollection3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(DataCollection3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "dataCollectionId")
	protected Integer dataCollectionId;

	@ManyToOne
	@JoinColumn(name = "dataCollectionGroupId")
	private DataCollectionGroup3VO dataCollectionGroupVO;

	@ManyToOne
	@JoinColumn(name = "strategySubWedgeOrigId")
	private ScreeningStrategySubWedge3VO strategySubWedgeOrigVO;

	@ManyToOne
	@JoinColumn(name = "detectorId")
	private Detector3VO detectorVO;

	@ManyToOne
	@JoinColumn(name = "blSubSampleId")
	private BLSubSample3VO blSubSampleVO;

	@Column(name = "dataCollectionNumber")
	protected Integer dataCollectionNumber;

	@Column(name = "startTime")
	protected Date startTime;

	@Column(name = "endTime")
	protected Date endTime;

	@Column(name = "runStatus")
	protected String runStatus;

	@Column(name = "axisStart")
	protected Double axisStart;

	@Column(name = "axisEnd")
	protected Double axisEnd;

	@Column(name = "axisRange")
	protected Double axisRange;

	@Column(name = "overlap")
	protected Double overlap;

	@Column(name = "numberOfImages")
	protected Integer numberOfImages;

	@Column(name = "startImageNumber")
	protected Integer startImageNumber;

	@Column(name = "numberOfPasses")
	protected Integer numberOfPasses;

	@Column(name = "exposureTime")
	protected Double exposureTime;

	@Column(name = "imageDirectory")
	protected String imageDirectory;

	@Column(name = "imagePrefix")
	protected String imagePrefix;

	@Column(name = "imageSuffix")
	protected String imageSuffix;

	@Column(name = "fileTemplate")
	protected String fileTemplate;

	@Column(name = "wavelength")
	protected Double wavelength;

	@Column(name = "resolution")
	protected Double resolution;

	@Column(name = "detectorDistance")
	protected Double detectorDistance;

	@Column(name = "xBeam")
	protected Double xbeam;

	@Column(name = "yBeam")
	protected Double ybeam;

	@Column(name = "xBeamPix")
	protected Double xbeamPix;

	@Column(name = "yBeamPix")
	protected Double ybeamPix;

	@Column(name = "comments")
	protected String comments;

	@Column(name = "printableForReport")
	protected Byte printableForReport;

	@Column(name = "slitGapVertical")
	protected Double slitGapVertical;

	@Column(name = "slitGapHorizontal")
	protected Double slitGapHorizontal;

	@Column(name = "transmission")
	protected Double transmission;

	@Column(name = "synchrotronMode")
	protected String synchrotronMode;

	@Column(name = "xtalSnapshotFullPath1")
	protected String xtalSnapshotFullPath1;

	@Column(name = "xtalSnapshotFullPath2")
	protected String xtalSnapshotFullPath2;

	@Column(name = "xtalSnapshotFullPath3")
	protected String xtalSnapshotFullPath3;

	@Column(name = "xtalSnapshotFullPath4")
	protected String xtalSnapshotFullPath4;

	@Column(name = "rotationAxis")
	protected String rotationAxis;

	@Column(name = "phiStart")
	protected Double phiStart;

	@Column(name = "kappaStart")
	protected Double kappaStart;

	@Column(name = "omegaStart")
	protected Double omegaStart;

	@Column(name = "resolutionAtCorner")
	protected Double resolutionAtCorner;

	@Column(name = "detector2theta")
	protected Double detector2theta;

	@Column(name = "undulatorGap1")
	protected Double undulatorGap1;

	@Column(name = "undulatorGap2")
	protected Double undulatorGap2;

	@Column(name = "undulatorGap3")
	protected Double undulatorGap3;

	@Column(name = "beamSizeAtSampleX")
	protected Double beamSizeAtSampleX;

	@Column(name = "beamSizeAtSampleY")
	protected Double beamSizeAtSampleY;

	@Column(name = "centeringMethod")
	protected String centeringMethod;

	@Column(name = "averageTemperature")
	protected Double averageTemperature;

	@Column(name = "actualCenteringPosition")
	protected String actualCenteringPosition;

	@Column(name = "beamShape")
	protected String beamShape;

	@Column(name = "flux")
	protected Double flux;

	@Column(name = "flux_end")
	protected Double flux_end;

	@Column(name = "totalAbsorbedDose")
	protected Double totalAbsorbedDose;

	@Column(name = "bestWilsonPlotPath")
	protected String bestWilsonPlotPath;

	@Column(name = "imageQualityIndicatorsPlotPath")
	protected String imageQualityIndicatorsPlotPath;
	
	@Column(name = "imageQualityIndicatorsCSVPath")
	protected String imageQualityIndicatorsCSVPath;
	
	@Column(name = "magnification")
	protected Integer magnification;
	
	@Column(name = "voltage")
	protected Float voltage;
	
	
	
	@OneToMany
	@JoinColumn(name = "dataCollectionId")
	private Set<Image3VO> imageVOs;

//	@OneToMany
//	@JoinColumn(name = "dataCollectionId")
//	private Set<Screening3VO> screeningVOs;

	@OneToMany
	@JoinColumn(name = "dataCollectionId")
	private Set<AutoProcIntegration3VO> autoProcIntegrationVOs;

	public DataCollection3VO() {
		super();
	}

	public DataCollection3VO(Integer dataCollectionId, DataCollectionGroup3VO dataCollectionGroupVO,
			ScreeningStrategySubWedge3VO strategySubWedgeOrigVO, Detector3VO detectorVO, BLSubSample3VO blSubSampleVO,
			Integer dataCollectionNumber,
			Date startTime, Date endTime, String runStatus, Double axisStart, Double axisEnd, Double axisRange,
			Double overlap, Integer numberOfImages, Integer startImageNumber, Integer numberOfPasses,
			Double exposureTime, String imageDirectory, String imagePrefix, String imageSuffix, String fileTemplate,
			Double wavelength, Double resolution, Double detectorDistance, Double xbeam, Double ybeam, Double xbeamPix, Double ybeamPix, String comments,
			Byte printableForReport, Double slitGapVertical, Double slitGapHorizontal, Double transmission,
			String synchrotronMode, String xtalSnapshotFullPath1, String xtalSnapshotFullPath2,
			String xtalSnapshotFullPath3, String xtalSnapshotFullPath4, String rotationAxis, Double phiStart,
			Double kappaStart, Double omegaStart, Double resolutionAtCorner, Double detector2theta,
			Double undulatorGap1, Double undulatorGap2, Double undulatorGap3, Double beamSizeAtSampleX,
			Double beamSizeAtSampleY, String centeringMethod, Double averageTemperature,
			String actualCenteringPosition, String beamShape, Double flux, Double flux_end, Double totalAbsorbedDose,
			String bestWilsonPlotPath) {
		super();
		this.dataCollectionId = dataCollectionId;
		this.dataCollectionGroupVO = dataCollectionGroupVO;
		this.strategySubWedgeOrigVO = strategySubWedgeOrigVO;
		this.detectorVO = detectorVO;
		this.blSubSampleVO = blSubSampleVO;
		this.dataCollectionNumber = dataCollectionNumber;
		this.startTime = startTime;
		this.endTime = endTime;
		this.runStatus = runStatus;
		this.axisStart = axisStart;
		this.axisEnd = axisEnd;
		this.axisRange = axisRange;
		this.overlap = overlap;
		this.numberOfImages = numberOfImages;
		this.startImageNumber = startImageNumber;
		this.numberOfPasses = numberOfPasses;
		this.exposureTime = exposureTime;
		this.imageDirectory = imageDirectory;
		this.imagePrefix = imagePrefix;
		this.imageSuffix = imageSuffix;
		this.fileTemplate = fileTemplate;
		this.wavelength = wavelength;
		this.resolution = resolution;
		this.detectorDistance = detectorDistance;
		this.xbeam = xbeam;
		this.ybeam = ybeam;
		this.xbeamPix = xbeamPix;
		this.ybeamPix = ybeamPix;
		this.comments = comments;
		this.printableForReport = printableForReport;
		this.slitGapVertical = slitGapVertical;
		this.slitGapHorizontal = slitGapHorizontal;
		this.transmission = transmission;
		this.synchrotronMode = synchrotronMode;
		this.xtalSnapshotFullPath1 = xtalSnapshotFullPath1;
		this.xtalSnapshotFullPath2 = xtalSnapshotFullPath2;
		this.xtalSnapshotFullPath3 = xtalSnapshotFullPath3;
		this.xtalSnapshotFullPath4 = xtalSnapshotFullPath4;
		this.rotationAxis = rotationAxis;
		this.phiStart = phiStart;
		this.kappaStart = kappaStart;
		this.omegaStart = omegaStart;
		this.resolutionAtCorner = resolutionAtCorner;
		this.detector2theta = detector2theta;
		this.undulatorGap1 = undulatorGap1;
		this.undulatorGap2 = undulatorGap2;
		this.undulatorGap3 = undulatorGap3;
		this.beamSizeAtSampleX = beamSizeAtSampleX;
		this.beamSizeAtSampleY = beamSizeAtSampleY;
		this.centeringMethod = centeringMethod;
		this.averageTemperature = averageTemperature;
		this.actualCenteringPosition = actualCenteringPosition;
		this.beamShape = beamShape;
		this.flux = flux;
		this.flux_end = flux_end;
		this.totalAbsorbedDose = totalAbsorbedDose;
		this.bestWilsonPlotPath = bestWilsonPlotPath;
	}

	public DataCollection3VO(DataCollection3VO vo) {
		super();
		this.dataCollectionId = vo.getDataCollectionId();
		this.dataCollectionGroupVO = vo.getDataCollectionGroupVO();
		this.strategySubWedgeOrigVO = vo.getStrategySubWedgeOrigVO();
		this.detectorVO = vo.getDetectorVO();
		this.blSubSampleVO = vo.getBlSubSampleVO();
		this.dataCollectionNumber = vo.getDataCollectionNumber();
		this.startTime = vo.getStartTime();
		this.endTime = vo.getEndTime();
		this.runStatus = vo.getRunStatus();
		this.axisStart = vo.getAxisStart();
		this.axisEnd = vo.getAxisEnd();
		this.axisRange = vo.getAxisRange();
		this.overlap = vo.getOverlap();
		this.numberOfImages = vo.getNumberOfImages();
		this.startImageNumber = vo.getStartImageNumber();
		this.numberOfPasses = vo.getNumberOfPasses();
		this.exposureTime = vo.getExposureTime();
		this.imageDirectory = vo.getImageDirectory();
		this.imagePrefix = vo.getImagePrefix();
		this.imageSuffix = vo.getImageSuffix();
		this.fileTemplate = vo.getFileTemplate();
		this.wavelength = vo.getWavelength();
		this.resolution = vo.getResolution();
		this.detectorDistance = vo.getDetectorDistance();
		this.xbeam = vo.getXbeam();
		this.ybeam = vo.getYbeam();
		this.xbeamPix = vo.getXbeamPix();
		this.ybeamPix = vo.getYbeamPix();
		this.comments = vo.getComments();
		this.printableForReport = vo.getPrintableForReport();
		this.slitGapVertical = vo.getSlitGapVertical();
		this.slitGapHorizontal = vo.getSlitGapHorizontal();
		this.transmission = vo.getTransmission();
		this.synchrotronMode = vo.getSynchrotronMode();
		this.xtalSnapshotFullPath1 = vo.getXtalSnapshotFullPath1();
		this.xtalSnapshotFullPath2 = vo.getXtalSnapshotFullPath2();
		this.xtalSnapshotFullPath3 = vo.getXtalSnapshotFullPath3();
		this.xtalSnapshotFullPath4 = vo.getXtalSnapshotFullPath4();
		this.rotationAxis = vo.getRotationAxis();
		this.phiStart = vo.getPhiStart();
		this.kappaStart = vo.getKappaStart();
		this.omegaStart = vo.getOmegaStart();
		this.resolutionAtCorner = vo.getResolutionAtCorner();
		this.detector2theta = vo.getDetector2theta();
		this.undulatorGap1 = vo.getUndulatorGap1();
		this.undulatorGap2 = vo.getUndulatorGap2();
		this.undulatorGap3 = vo.getUndulatorGap3();
		this.beamSizeAtSampleX = vo.getBeamSizeAtSampleX();
		this.beamSizeAtSampleY = vo.getBeamSizeAtSampleY();
		this.centeringMethod = vo.getCenteringMethod();
		this.averageTemperature = vo.getAverageTemperature();
		this.actualCenteringPosition = vo.getActualCenteringPosition();
		this.beamShape = vo.getBeamShape();
		this.flux = vo.getFlux();
		this.flux_end = vo.getFlux_end();
		this.totalAbsorbedDose = vo.getTotalAbsorbedDose();
		this.bestWilsonPlotPath = vo.getBestWilsonPlotPath();
	}

	public void fillVOFromWS(DataCollectionWS3VO vo) {
		this.dataCollectionId = vo.getDataCollectionId();
		this.dataCollectionNumber = vo.getDataCollectionNumber();
		// this.startTime = vo.getStartTime();
		// this.endTime = vo.getEndTime();
		this.runStatus = vo.getRunStatus();
		this.axisStart = vo.getAxisStart();
		this.axisEnd = vo.getAxisEnd();
		this.axisRange = vo.getAxisRange();
		this.overlap = vo.getOverlap();
		this.numberOfImages = vo.getNumberOfImages();
		this.startImageNumber = vo.getStartImageNumber();
		this.numberOfPasses = vo.getNumberOfPasses();
		this.exposureTime = vo.getExposureTime();
		this.imageDirectory = vo.getImageDirectory();
		this.imagePrefix = vo.getImagePrefix();
		this.imageSuffix = vo.getImageSuffix();
		this.fileTemplate = vo.getFileTemplate();
		this.wavelength = vo.getWavelength();
		this.resolution = vo.getResolution();
		this.detectorDistance = vo.getDetectorDistance();
		this.xbeam = vo.getXbeam();
		this.ybeam = vo.getYbeam();
		this.xbeamPix = vo.getXbeamPix();
		this.ybeamPix = vo.getYbeamPix();
		this.comments = vo.getComments();
		this.printableForReport = vo.getPrintableForReport();
		this.slitGapVertical = vo.getSlitGapVertical();
		this.slitGapHorizontal = vo.getSlitGapHorizontal();
		this.transmission = vo.getTransmission();
		this.synchrotronMode = vo.getSynchrotronMode();
		this.xtalSnapshotFullPath1 = vo.getXtalSnapshotFullPath1();
		this.xtalSnapshotFullPath2 = vo.getXtalSnapshotFullPath2();
		this.xtalSnapshotFullPath3 = vo.getXtalSnapshotFullPath3();
		this.xtalSnapshotFullPath4 = vo.getXtalSnapshotFullPath4();
		this.rotationAxis = vo.getRotationAxis();
		this.phiStart = vo.getPhiStart();
		this.kappaStart = vo.getKappaStart();
		this.omegaStart = vo.getOmegaStart();
		this.resolutionAtCorner = vo.getResolutionAtCorner();
		this.detector2theta = vo.getDetector2theta();
		this.undulatorGap1 = vo.getUndulatorGap1();
		this.undulatorGap2 = vo.getUndulatorGap2();
		this.undulatorGap3 = vo.getUndulatorGap3();
		this.beamSizeAtSampleX = vo.getBeamSizeAtSampleX();
		this.beamSizeAtSampleY = vo.getBeamSizeAtSampleY();
		this.centeringMethod = vo.getCenteringMethod();
		this.averageTemperature = vo.getAverageTemperature();
		this.actualCenteringPosition = vo.getActualCenteringPosition();
		this.beamShape = vo.getBeamShape();
		this.flux = vo.getFlux();
		this.flux_end = vo.getFlux_end();
		this.totalAbsorbedDose = vo.getTotalAbsorbedDose();
		this.bestWilsonPlotPath = vo.getBestWilsonPlotPath();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getDataCollectionId() {
		return dataCollectionId;
	}

	/**
	 * @param pk
	 *            The pk to set.
	 */
	public void setDataCollectionId(Integer dataCollectionId) {
		this.dataCollectionId = dataCollectionId;
	}

	public DataCollectionGroup3VO getDataCollectionGroupVO() {
		return dataCollectionGroupVO;
	}

	public Integer getDataCollectionGroupVOId() {
		return dataCollectionGroupVO == null ? null : dataCollectionGroupVO.getDataCollectionGroupId();
	}

	public void setDataCollectionGroupVO(DataCollectionGroup3VO dataCollectionGroupVO) {
		this.dataCollectionGroupVO = dataCollectionGroupVO;
	}

	public ScreeningStrategySubWedge3VO getStrategySubWedgeOrigVO() {
		return strategySubWedgeOrigVO;
	}

	public void setStrategySubWedgeOrigVO(ScreeningStrategySubWedge3VO strategySubWedgeOrigVO) {
		this.strategySubWedgeOrigVO = strategySubWedgeOrigVO;
	}

	public Integer getStrategySubWedgeOrigVOId() {
		return strategySubWedgeOrigVO == null ? null : strategySubWedgeOrigVO.getScreeningStrategySubWedgeId();
	}

	public Detector3VO getDetectorVO() {
		return detectorVO;
	}

	public Integer getDetectorVOId() {
		return detectorVO == null ? null : detectorVO.getDetectorId();
	}

	public void setDetectorVO(Detector3VO detectorVO) {
		this.detectorVO = detectorVO;
	}

	public BLSubSample3VO getBlSubSampleVO() {
		return blSubSampleVO;
	}

	public void setBlSubSampleVO(BLSubSample3VO blSubSampleVO) {
		this.blSubSampleVO = blSubSampleVO;
	}

	public Integer getBlSubSampleVOId() {
		return blSubSampleVO == null ? null : blSubSampleVO.getBlSubSampleId();
	}

	public Integer getDataCollectionNumber() {
		return dataCollectionNumber;
	}

	public void setDataCollectionNumber(Integer dataCollectionNumber) {
		this.dataCollectionNumber = dataCollectionNumber;
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

	public String getRunStatus() {
		return runStatus;
	}

	public void setRunStatus(String runStatus) {
		this.runStatus = runStatus;
	}

	public Double getAxisStart() {
		return axisStart;
	}

	public void setAxisStart(Double axisStart) {
		this.axisStart = axisStart;
	}

	public Double getAxisEnd() {
		return axisEnd;
	}

	public void setAxisEnd(Double axisEnd) {
		this.axisEnd = axisEnd;
	}

	public Double getAxisRange() {
		return axisRange;
	}

	public void setAxisRange(Double axisRange) {
		this.axisRange = axisRange;
	}

	public Double getOverlap() {
		return overlap;
	}

	public void setOverlap(Double overlap) {
		this.overlap = overlap;
	}

	public Integer getNumberOfImages() {
		return numberOfImages;
	}

	public void setNumberOfImages(Integer numberOfImages) {
		this.numberOfImages = numberOfImages;
	}

	public Integer getStartImageNumber() {
		return startImageNumber;
	}

	public void setStartImageNumber(Integer startImageNumber) {
		this.startImageNumber = startImageNumber;
	}

	public Integer getNumberOfPasses() {
		return numberOfPasses;
	}

	public void setNumberOfPasses(Integer numberOfPasses) {
		this.numberOfPasses = numberOfPasses;
	}

	public Double getExposureTime() {
		return exposureTime;
	}

	public void setExposureTime(Double exposureTime) {
		this.exposureTime = exposureTime;
	}

	public String getImageDirectory() {
		return imageDirectory;
	}

	public void setImageDirectory(String imageDirectory) {
		this.imageDirectory = imageDirectory;
	}

	public String getImagePrefix() {
		return imagePrefix;
	}

	public void setImagePrefix(String imagePrefix) {
		this.imagePrefix = imagePrefix;
	}

	public String getImageSuffix() {
		return imageSuffix;
	}

	public void setImageSuffix(String imageSuffix) {
		this.imageSuffix = imageSuffix;
	}

	public String getFileTemplate() {
		return fileTemplate;
	}

	public void setFileTemplate(String fileTemplate) {
		this.fileTemplate = fileTemplate;
	}

	public Double getWavelength() {
		return wavelength;
	}

	public void setWavelength(Double wavelength) {
		this.wavelength = wavelength;
	}

	public Double getResolution() {
		return resolution;
	}

	public void setResolution(Double resolution) {
		this.resolution = resolution;
	}

	public Double getDetectorDistance() {
		return detectorDistance;
	}

	public void setDetectorDistance(Double detectorDistance) {
		this.detectorDistance = detectorDistance;
	}

	public Double getXbeam() {
		return xbeam;
	}

	public void setXbeam(Double xbeam) {
		this.xbeam = xbeam;
	}

	public Double getYbeam() {
		return ybeam;
	}

	public void setYbeam(Double ybeam) {
		this.ybeam = ybeam;
	}

	public Double getXbeamPix() {
		return xbeamPix;
	}

	public void setXbeamPix(Double xBeamPix) {
		this.xbeamPix = xBeamPix;
	}

	public Double getYbeamPix() {
		return ybeamPix;
	}

	public void setYbeamPix(Double yBeamPix) {
		this.ybeamPix = yBeamPix;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Byte getPrintableForReport() {
		return printableForReport;
	}

	public void setPrintableForReport(Byte printableForReport) {
		this.printableForReport = printableForReport;
	}

	public Double getSlitGapVertical() {
		return slitGapVertical;
	}

	public void setSlitGapVertical(Double slitGapVertical) {
		this.slitGapVertical = slitGapVertical;
	}

	public Double getSlitGapHorizontal() {
		return slitGapHorizontal;
	}

	public void setSlitGapHorizontal(Double slitGapHorizontal) {
		this.slitGapHorizontal = slitGapHorizontal;
	}

	public Double getTransmission() {
		return transmission;
	}

	public void setTransmission(Double transmission) {
		this.transmission = transmission;
	}

	public String getSynchrotronMode() {
		return synchrotronMode;
	}

	public void setSynchrotronMode(String synchrotronMode) {
		this.synchrotronMode = synchrotronMode;
	}

	public String getXtalSnapshotFullPath1() {
		return xtalSnapshotFullPath1;
	}

	public void setXtalSnapshotFullPath1(String xtalSnapshotFullPath1) {
		this.xtalSnapshotFullPath1 = xtalSnapshotFullPath1;
	}

	public String getXtalSnapshotFullPath2() {
		return xtalSnapshotFullPath2;
	}

	public void setXtalSnapshotFullPath2(String xtalSnapshotFullPath2) {
		this.xtalSnapshotFullPath2 = xtalSnapshotFullPath2;
	}

	public String getXtalSnapshotFullPath3() {
		return xtalSnapshotFullPath3;
	}

	public void setXtalSnapshotFullPath3(String xtalSnapshotFullPath3) {
		this.xtalSnapshotFullPath3 = xtalSnapshotFullPath3;
	}

	public String getXtalSnapshotFullPath4() {
		return xtalSnapshotFullPath4;
	}

	public void setXtalSnapshotFullPath4(String xtalSnapshotFullPath4) {
		this.xtalSnapshotFullPath4 = xtalSnapshotFullPath4;
	}

	public String getRotationAxis() {
		return rotationAxis;
	}

	public void setRotationAxis(String rotationAxis) {
		this.rotationAxis = rotationAxis;
	}

	public Double getPhiStart() {
		return phiStart;
	}

	public void setPhiStart(Double phiStart) {
		this.phiStart = phiStart;
	}

	public Double getKappaStart() {
		return kappaStart;
	}

	public void setKappaStart(Double kappaStart) {
		this.kappaStart = kappaStart;
	}

	public Double getOmegaStart() {
		return omegaStart;
	}

	public void setOmegaStart(Double omegaStart) {
		this.omegaStart = omegaStart;
	}

	public Double getResolutionAtCorner() {
		return resolutionAtCorner;
	}

	public void setResolutionAtCorner(Double resolutionAtCorner) {
		this.resolutionAtCorner = resolutionAtCorner;
	}

	public Double getDetector2theta() {
		return detector2theta;
	}

	public void setDetector2theta(Double detector2theta) {
		this.detector2theta = detector2theta;
	}

	public Double getUndulatorGap1() {
		return undulatorGap1;
	}

	public void setUndulatorGap1(Double undulatorGap1) {
		this.undulatorGap1 = undulatorGap1;
	}

	public Double getUndulatorGap2() {
		return undulatorGap2;
	}

	public void setUndulatorGap2(Double undulatorGap2) {
		this.undulatorGap2 = undulatorGap2;
	}

	public Double getUndulatorGap3() {
		return undulatorGap3;
	}

	public void setUndulatorGap3(Double undulatorGap3) {
		this.undulatorGap3 = undulatorGap3;
	}

	public Double getBeamSizeAtSampleX() {
		return beamSizeAtSampleX;
	}

	public void setBeamSizeAtSampleX(Double beamSizeAtSampleX) {
		this.beamSizeAtSampleX = beamSizeAtSampleX;
	}

	public Double getBeamSizeAtSampleY() {
		return beamSizeAtSampleY;
	}

	public void setBeamSizeAtSampleY(Double beamSizeAtSampleY) {
		this.beamSizeAtSampleY = beamSizeAtSampleY;
	}

	public String getCenteringMethod() {
		return centeringMethod;
	}

	public void setCenteringMethod(String centeringMethod) {
		this.centeringMethod = centeringMethod;
	}

	public Double getAverageTemperature() {
		return averageTemperature;
	}

	public void setAverageTemperature(Double averageTemperature) {
		this.averageTemperature = averageTemperature;
	}

	public String getActualCenteringPosition() {
		return actualCenteringPosition;
	}

	public void setActualCenteringPosition(String actualCenteringPosition) {
		this.actualCenteringPosition = actualCenteringPosition;
	}

	public String getBeamShape() {
		return beamShape;
	}

	public void setBeamShape(String beamShape) {
		this.beamShape = beamShape;
	}

	public Double getFlux() {
		return flux;
	}

	public void setFlux(Double flux) {
		this.flux = flux;
	}

	public Double getFlux_end() {
		return flux_end;
	}

	public void setFlux_end(Double flux_end) {
		this.flux_end = flux_end;
	}

	public Double getTotalAbsorbedDose() {
		return totalAbsorbedDose;
	}

	public void setTotalAbsorbedDose(Double totalAbsorbedDose) {
		this.totalAbsorbedDose = totalAbsorbedDose;
	}

	public String getBestWilsonPlotPath() {
		return bestWilsonPlotPath;
	}

	public void setBestWilsonPlotPath(String bestWilsonPlotPath) {
		this.bestWilsonPlotPath = bestWilsonPlotPath;
	}

	public Set<Image3VO> getImageVOs() {
		return imageVOs;
	}

	public void setImageVOs(Set<Image3VO> imageVOs) {
		this.imageVOs = imageVOs;
	}

//	public Set<Screening3VO> getScreeningVOs() {
//		return screeningVOs;
//	}
//
//	public void setScreeningVOs(Set<Screening3VO> screeningVOs) {
//		this.screeningVOs = screeningVOs;
//	}
//
//	public Screening3VO[] getScreeningsTab() {
//		return this.screeningVOs == null ? null : screeningVOs.toArray(new Screening3VO[this.screeningVOs.size()]);
//	}
//
//	public ArrayList<Screening3VO> getScreeningsList() {
//		return this.screeningVOs == null ? null : new ArrayList<Screening3VO>(Arrays.asList(getScreeningsTab()));
//	}

	public Set<AutoProcIntegration3VO> getAutoProcIntegrationVOs() {
		return autoProcIntegrationVOs;
	}

	public void setAutoProcIntegrationVOs(Set<AutoProcIntegration3VO> autoProcIntegrationVOs) {
		this.autoProcIntegrationVOs = autoProcIntegrationVOs;
	}

	/**
	 * Checks the values of this value object for correctness and completeness. Should be done before persisting the
	 * data in the DB.
	 * 
	 * @param create
	 *            should be true if the value object is just being created in the DB, this avoids some checks like
	 *            testing the primary key
	 * @throws Exception
	 *             if the data of the value object is not correct
	 */
	@Override
	public void checkValues(boolean create) throws Exception {
		int maxLengthRunStatus = 45;
		int maxLengthImageDirectory = 255;
		int maxLengthImagePrefix = 100;
		int maxLengthImageSuffix = 45;
		int maxLengthFileTemplate = 255;
		int maxLengthComments = 1024;
		int maxLengthSynchrotronMode = 20;
		int maxLengthXtalSnapshotFullPath1 = 255;
		int maxLengthXtalSnapshotFullPath2 = 255;
		int maxLengthXtalSnapshotFullPath3 = 255;
		int maxLengthXtalSnapshotFullPath4 = 255;
		int maxLengthCenteringMethod = 255;
		int maxLengthActualCenteringPosition = 255;
		int maxLengthBeamShape = 45;
		int maxLengthBestWilsonPlotPath = 255;

		String[] listRotationAxis = { "Omega", "Kappa", "Phi" };
		// dataCollectionGroup
		if (dataCollectionGroupVO == null)
			throw new IllegalArgumentException(StringUtils.getMessageRequiredField("DataCollection",
					"dataCollectionGroupVO"));
		// runStatus
		if (!StringUtils.isStringLengthValid(this.runStatus, maxLengthRunStatus))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("DataCollection", "runStatus",
					maxLengthRunStatus));
		// imageDirectory
		if (!StringUtils.isStringLengthValid(this.imageDirectory, maxLengthImageDirectory))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("DataCollection", "imageDirectory",
					maxLengthImageDirectory));
		// imagePrefix
		if (!StringUtils.isStringLengthValid(this.imagePrefix, maxLengthImagePrefix))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("DataCollection", "imagePrefix",
					maxLengthImagePrefix));
		// imageSuffix
		if (!StringUtils.isStringLengthValid(this.imageSuffix, maxLengthImageSuffix))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("DataCollection", "imageSuffix",
					maxLengthImageSuffix));
		// fileTemplate
		if (!StringUtils.isStringLengthValid(this.fileTemplate, maxLengthFileTemplate))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("DataCollection", "fileTemplate",
					maxLengthFileTemplate));
		// comments
		if (!StringUtils.isStringLengthValid(this.comments, maxLengthComments))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("DataCollection", "comments",
					maxLengthComments));
		// printableForReport
		if (!StringUtils.isBoolean("" + this.printableForReport, true))
			throw new IllegalArgumentException(StringUtils.getMessageBooleanField("DataCollection",
					"printableForReport"));
		// synchrotronMode
		if (!StringUtils.isStringLengthValid(this.synchrotronMode, maxLengthSynchrotronMode))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("DataCollection",
					"synchrotronMode", maxLengthSynchrotronMode));
		// xtalSnapshotFullPath1
		if (!StringUtils.isStringLengthValid(this.xtalSnapshotFullPath1, maxLengthXtalSnapshotFullPath1))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("DataCollection",
					"xtalSnapshotFullPath1", maxLengthXtalSnapshotFullPath1));
		// xtalSnapshotFullPath2
		if (!StringUtils.isStringLengthValid(this.xtalSnapshotFullPath2, maxLengthXtalSnapshotFullPath2))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("DataCollection",
					"xtalSnapshotFullPath2", maxLengthXtalSnapshotFullPath2));
		// xtalSnapshotFullPath3
		if (!StringUtils.isStringLengthValid(this.xtalSnapshotFullPath3, maxLengthXtalSnapshotFullPath3))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("DataCollection",
					"xtalSnapshotFullPath3", maxLengthXtalSnapshotFullPath3));
		// xtalSnapshotFullPath4
		if (!StringUtils.isStringLengthValid(this.xtalSnapshotFullPath4, maxLengthXtalSnapshotFullPath4))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("DataCollection",
					"xtalSnapshotFullPath4", maxLengthXtalSnapshotFullPath4));
		// rotationAxis
		if (!StringUtils.isStringInPredefinedList(this.rotationAxis, listRotationAxis, true))
			throw new IllegalArgumentException(StringUtils.getMessageErrorPredefinedList("DataCollection",
					"rotationAxis", listRotationAxis));
		// centeringMethod
		if (!StringUtils.isStringLengthValid(this.centeringMethod, maxLengthCenteringMethod))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("DataCollection",
					"centeringMethod", maxLengthCenteringMethod));
		// actualCenteringPosition
		if (!StringUtils.isStringLengthValid(this.actualCenteringPosition, maxLengthActualCenteringPosition))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("DataCollection",
					"actualCenteringPosition", maxLengthActualCenteringPosition));
		// beamShape
		if (!StringUtils.isStringLengthValid(this.beamShape, maxLengthBeamShape))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("DataCollection", "beamShape",
					maxLengthBeamShape));
		// bestWilsonPlotPath
		if (!StringUtils.isStringLengthValid(this.bestWilsonPlotPath, maxLengthBestWilsonPlotPath))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("DataCollection",
					"bestWilsonPlotPath", maxLengthBestWilsonPlotPath));

	}

	/**
	 * returns the acronym of the associated protein
	 * 
	 * @return
	 */
	public String getAcronym() {
		if (this.dataCollectionGroupVO != null && dataCollectionGroupVO.getBlSampleVO() != null
				&& dataCollectionGroupVO.getBlSampleVO().getCrystalVO() != null
				&& dataCollectionGroupVO.getBlSampleVO().getCrystalVO().getProteinVO() != null) {
			return this.dataCollectionGroupVO.getBlSampleVO().getCrystalVO().getProteinVO().getAcronym();
		}
		return null;
	}

	public String toWSString() {
		String s = "dataCollectionId=" + this.dataCollectionId + ", " + "dataCollectionNumber="
				+ this.dataCollectionNumber + ", " + "startTime=" + this.startTime + ", " + "endTime=" + this.endTime
				+ ", " + "runStatus=" + this.runStatus + ", " + "axisStart=" + this.axisStart + ", " + "axisEnd="
				+ this.axisEnd + ", " + "axisRange=" + this.axisRange + ", " + "overlap=" + this.overlap + ", "
				+ "numberOfImages=" + this.numberOfImages + ", " + "startImageNumber=" + this.startImageNumber + ", "
				+ "numberOfPasses=" + this.numberOfPasses + ", " + "exposureTime=" + this.exposureTime + ", "
				+ "imageDirectory=" + this.imageDirectory + ", " + "imagePrefix=" + this.imagePrefix + ", "
				+ "imageSuffix=" + this.imageSuffix + ", " + "fileTemplate=" + this.fileTemplate + ", " + "wavelength="
				+ this.wavelength + ", " + "resolution=" + this.resolution + ", " + "detectorDistance="
				+ this.detectorDistance + ", " + "xbeam=" + this.xbeam + ", " + "ybeam=" + this.ybeam + ", "
				+ "comments=" + this.comments + ", " + "printableForReport=" + this.printableForReport + ", "
				+ "slitGapVertical=" + this.slitGapVertical + ", " + "slitGapHorizontal=" + this.slitGapHorizontal
				+ ", " + "transmission=" + this.transmission + ", " + "synchrotronMode=" + this.synchrotronMode + ", "
				+ "xtalSnapshotFullPath1=" + this.xtalSnapshotFullPath1 + ", " + "xtalSnapshotFullPath2="
				+ this.xtalSnapshotFullPath2 + ", " + "xtalSnapshotFullPath3=" + this.xtalSnapshotFullPath3 + ", "
				+ "xtalSnapshotFullPath4=" + this.xtalSnapshotFullPath4 + ", " + "rotationAxis=" + this.rotationAxis
				+ ", " + "phiStart=" + this.phiStart + ", " + "kappaStart=" + this.kappaStart + ", " + "omegaStart="
				+ this.omegaStart + ", " + "resolutionAtCorner=" + this.resolutionAtCorner + ", " + "detector2theta="
				+ this.detector2theta + ", " + "undulatorGap1=" + this.undulatorGap1 + ", " + "undulatorGap2="
				+ this.undulatorGap2 + ", " + "undulatorGap3=" + this.undulatorGap3 + ", " + "beamSizeAtSampleX="
				+ this.beamSizeAtSampleX + ", " + "beamSizeAtSampleY=" + this.beamSizeAtSampleY + ", "
				+ "centeringMethod=" + this.centeringMethod + ", " + "averageTemperature=" + this.averageTemperature
				+ ", " + "actualCenteringPosition=" + this.actualCenteringPosition + ", " + "beamShape="
				+ this.beamShape + ", " + "flux=" + this.flux + ", " + "flux_end=" + this.flux_end + ", "
				+ "totalAbsorbedDose=" + this.totalAbsorbedDose + ", " + "bestWilsonPlotPath="
				+ this.bestWilsonPlotPath;

		return s;
	}

	public AutoProcIntegration3VO[] getAutoProcIntegrationsTab() {
		return this.autoProcIntegrationVOs == null ? null : autoProcIntegrationVOs
				.toArray(new AutoProcIntegration3VO[this.autoProcIntegrationVOs.size()]);
	}

	public ArrayList<AutoProcIntegration3VO> getAutoProcIntegrationsList() {
		return this.autoProcIntegrationVOs == null ? null : new ArrayList<AutoProcIntegration3VO>(
				Arrays.asList(getAutoProcIntegrationsTab()));
	}

	public Double getTotalExposureTime() {
		if (this.numberOfImages != null && this.exposureTime != null) {
			return this.exposureTime * this.numberOfImages;
		} else {
			return null;
		}
	}

	public String getSamplePosition() {
		String samplePosition = "";
		if (this.dataCollectionGroupVO.getActualContainerSlotInSC() != null) {
			String s = "";
			if (this.dataCollectionGroupVO.getActualSampleSlotInContainer() != null) {
				s = "" + this.dataCollectionGroupVO.getActualSampleSlotInContainer();
			}
			samplePosition += this.dataCollectionGroupVO.getActualContainerSlotInSC() + " - " + s;
		}
		return samplePosition;
	}

	public String getImageQualityIndicatorsPlotPath() {
		return imageQualityIndicatorsPlotPath;
	}

	public void setImageQualityIndicatorsPlotPath(String imageQualityIndicatorsPlotPath) {
		this.imageQualityIndicatorsPlotPath = imageQualityIndicatorsPlotPath;
	}

	public String getImageQualityIndicatorsCSVPath() {
		return imageQualityIndicatorsCSVPath;
	}

	public void setImageQualityIndicatorsCSVPath(String imageQualityIndicatorsCSVPath) {
		this.imageQualityIndicatorsCSVPath = imageQualityIndicatorsCSVPath;
	}

	public Integer getMagnification() {
		return magnification;
	}

	public void setMagnification(Integer magnification) {
		this.magnification = magnification;
	}

	public Float getVoltage() {
		return voltage;
	}

	public void setVoltage(Float voltage) {
		this.voltage = voltage;
	}
}
