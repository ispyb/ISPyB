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
 * 
 * ViewResultsForm.java
 */

package ispyb.client.mx.results;

import ispyb.common.util.Constants;
import ispyb.server.mx.vos.autoproc.AutoProc3VO;
import ispyb.server.mx.vos.autoproc.AutoProcStatus3VO;
import ispyb.server.mx.vos.collections.BeamLineSetup3VO;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.Image3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.DiffractionPlan3VO;
import ispyb.server.mx.vos.sample.Protein3VO;
import ispyb.server.mx.vos.screening.Screening3VO;
import ispyb.server.mx.vos.screening.ScreeningInput3VO;
import ispyb.server.mx.vos.screening.ScreeningOutput3VO;
import ispyb.server.mx.vos.screening.ScreeningOutputLattice3VO;
import ispyb.server.mx.vos.screening.ScreeningRank3VO;
import ispyb.server.mx.vos.screening.ScreeningRankSet3VO;
import ispyb.server.mx.vos.screening.ScreeningStrategy3VO;
import ispyb.server.mx.vos.screening.ScreeningStrategySubWedge3VO;
import ispyb.server.mx.vos.screening.ScreeningStrategyWedge3VO;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.struts.action.ActionForm;

/**
 * @struts.form name="viewResultsForm"
 */

public class ViewResultsForm extends ActionForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<ImageValueInfo> listInfo = new ArrayList<ImageValueInfo>();

	private Integer dataCollectionId;

	private DataCollection3VO dataCollection = new DataCollection3VO();

	private Crystal3VO crystal = new Crystal3VO();

	private Protein3VO protein = new Protein3VO();

	private BLSample3VO sample = new BLSample3VO();

	private DiffractionPlan3VO difPlan = new DiffractionPlan3VO();

	private BeamLineSetup3VO beamLine = new BeamLineSetup3VO();

	private Session3VO session = new Session3VO();

	private Screening3VO[] screeningList;

	private Screening3VO screening = new Screening3VO();

	private ScreeningInput3VO screeningInput = new ScreeningInput3VO();

	private ScreeningRank3VO screeningRank = new ScreeningRank3VO();

	private ScreeningOutput3VO screeningOutput = new ScreeningOutput3VO();

	private ScreeningStrategy3VO screeningStrategy = new ScreeningStrategy3VO();

	private ScreeningStrategy3VO[] screeningStrategyList;

	private List<ScreeningStrategyValueInfo> listStrategiesInfo = new ArrayList<ScreeningStrategyValueInfo>();

	// screening strategy wedge
	private ScreeningStrategyWedge3VO screeningStrategyWedge = new ScreeningStrategyWedge3VO();

	private ScreeningStrategyWedge3VO[] screeningStrategyWedgeList;

	private List<ScreeningStrategyWedgeValueInfo> listStrategiesWedgeInfo;

	
	// screening strategy sub wedge
	private ScreeningStrategySubWedge3VO screeningStrategySubWedge = new ScreeningStrategySubWedge3VO();

	private ScreeningStrategySubWedge3VO[][] screeningStrategySubWedgeListAll;

	private List<ScreeningStrategySubWedgeValueInfo>[] listStrategiesSubWedgeInfoAll;
	
	private ScreeningStrategySubWedge3VO[] screeningStrategySubWedgeList;

	private List<ScreeningStrategySubWedgeValueInfo> listStrategiesSubWedgeInfo;

	private ScreeningOutputLattice3VO screeningOutputLattice = new ScreeningOutputLattice3VO();

	private ScreeningRankSet3VO screeningRankSet = new ScreeningRankSet3VO();

	private Integer nbImagesHorizontal = Constants.IMAGE_WALL_DEFAULT_NB_IMAGES_HOR;

	private Integer nbImagesVertical = Constants.IMAGE_WALL_DEFAULT_NB_IMAGES_VERT;

	private String imageWidth = Constants.IMAGE_WALL_DEFAULT_IMAGE_WIDTH;

	private String imageDir;

	private String imagePath;

	private boolean displayOutputParam;

	private boolean DNAContentPresent;

	private boolean EDNAContentPresent;

	private boolean dna_logContentPresent;

	private boolean mosflm_triclintContentPresent;

	private boolean scala_logContentPresent;

	private boolean pointlessContentPresent;

	private boolean dataProcessingContentPresent;

	private boolean integrationContentPresent;

	private boolean strategyContentPresent;

	private boolean DNARankingProjectFilePresent;

	private boolean DNARankingSummaryFilePresent;

	private String DNAContent;

	private String DNASelectedFile;

	private boolean DenzonContentPresent;

	private String DenzoContent;

	private String expectedDenzoPath;

	private boolean displayDenzoContent = false;

	private String expectedSnapshotPath;

	private List<SnapshotInfo> listSnapshots = new ArrayList<SnapshotInfo>();

	private SnapshotInfo snapshotInfo;

	private ImageValueInfo image = new ImageValueInfo(new Image3VO());

	private Integer targetImageNumber = null;

	private Integer totalImageNumber = null;

	private Double energy = null;

	private String undulatorTypes;

	private String undulatorGaps;

	private String axisStartLabel;

	private String beamShape;

	// parameters used on the applet:
	private String imageUrl;

	private String detectorType;

	// params with specific formating
	private Integer transmissionInt = null;

	private Integer slitGapHorizontalMicro = null;

	private Integer slitGapVerticalMicro = null;

	private Integer beamSizeAtSampleXMicro = null;

	private Integer beamSizeAtSampleYMicro = null;

	private Double detectorPixelSizeHorizontalMicro = null;

	private Double detectorPixelSizeVerticalMicro = null;

	private Integer beamDivergenceHorizontalInt = null;

	private Integer beamDivergenceVerticalInt = null;
	
	private Double totalExposureTime = null;

	private Double predictedResolution = null;

	private String autoProcProgramAttachmentId = null;

	private String autoProcAttachmentContent = null;

	private String autoProcAttachmentName = null;

	private final java.util.Collection<AutoProc3VO> autoProcs = null;

	private String selectedAutoProcLabel;

	private String selectedAutoProc;

	private String rmerge;

	private String isigma;

	private String currentImageId;
	
	private String htmlFileContent;
	
	private List<List<AutoProcStatus3VO>>interruptedAutoProcEvents;
	
	

	public String getSelectedAutoProc() {
		return selectedAutoProc;
	}

	public void setSelectedAutoProc(String selectedAutoProc) {
		this.selectedAutoProc = selectedAutoProc;
	}

	public List<ImageValueInfo> getListInfo() {
		return listInfo;
	}

	public void setListInfo(List<ImageValueInfo> listInfo) {
		this.listInfo = listInfo;
	}

	public Integer getDataCollectionId() {
		return dataCollectionId;
	}

	public void setDataCollectionId(Integer dataCollectionId) {
		this.dataCollectionId = dataCollectionId;
	}

	public String getImageDir() {
		return imageDir;
	}

	public void setImageDir(String imageDir) {
		this.imageDir = imageDir;
	}

	public Integer getNbImagesHorizontal() {
		return nbImagesHorizontal;
	}

	public void setNbImagesHorizontal(Integer nbImagesHorizontal) {
		this.nbImagesHorizontal = nbImagesHorizontal;
	}

	public Integer getNbImagesVertical() {
		return nbImagesVertical;
	}

	public void setNbImagesVertical(Integer nbImagesVertical) {
		this.nbImagesVertical = nbImagesVertical;
	}

	public String getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(String imageWidth) {
		this.imageWidth = imageWidth;
	}

	public ViewResultsForm() {
		super();
	}

	public boolean isDisplayOutputParam() {
		return displayOutputParam;
	}

	public void setDisplayOutputParam(boolean displayOutputParam) {
		this.displayOutputParam = displayOutputParam;
	}

	public boolean isDNAContentPresent() {
		return DNAContentPresent;
	}

	public void setDNAContentPresent(boolean contentPresent) {
		DNAContentPresent = contentPresent;
	}

	public boolean isEDNAContentPresent() {
		return EDNAContentPresent;
	}

	public void setEDNAContentPresent(boolean contentPresent) {
		EDNAContentPresent = contentPresent;
	}

	public boolean isDna_logContentPresent() {
		return dna_logContentPresent;
	}

	public void setDna_logContentPresent(boolean adna_logContentPresent) {
		dna_logContentPresent = adna_logContentPresent;
	}

	public boolean isMosflm_triclintContentPresent() {
		return mosflm_triclintContentPresent;
	}

	public void setMosflm_triclintContentPresent(boolean amosflm_triclintContentPresent) {
		mosflm_triclintContentPresent = amosflm_triclintContentPresent;
	}

	public boolean isScala_logContentPresent() {
		return scala_logContentPresent;
	}

	public void setScala_logContentPresent(boolean ascala_logContentPresent) {
		scala_logContentPresent = ascala_logContentPresent;
	}

	public boolean isPointlessContentPresent() {
		return pointlessContentPresent;
	}

	public void setPointlessContentPresent(boolean pointlessContentPresent) {
		this.pointlessContentPresent = pointlessContentPresent;
	}

	public boolean isDataProcessingContentPresent() {
		return dataProcessingContentPresent;
	}

	public void setDataProcessingContentPresent(boolean contentPresent) {
		dataProcessingContentPresent = contentPresent;
	}

	public boolean isIntegrationContentPresent() {
		return integrationContentPresent;
	}

	public void setIntegrationContentPresent(boolean contentPresent) {
		integrationContentPresent = contentPresent;
	}

	public boolean isStrategyContentPresent() {
		return strategyContentPresent;
	}

	public void setStrategyContentPresent(boolean contentPresent) {
		strategyContentPresent = contentPresent;
	}

	public boolean isDNARankingProjectFilePresent() {
		return DNARankingProjectFilePresent;
	}

	public void setDNARankingProjectFilePresent(boolean contentPresent) {
		DNARankingProjectFilePresent = contentPresent;
	}

	public boolean isDNARankingSummaryFilePresent() {
		return DNARankingSummaryFilePresent;
	}

	public void setDNARankingSummaryFilePresent(boolean contentPresent) {
		DNARankingSummaryFilePresent = contentPresent;
	}

	public String getDNAContent() {
		return DNAContent;
	}

	public void setDNAContent(String dNAContent) {
		DNAContent = dNAContent;
	}

	public String getDNASelectedFile() {
		return DNASelectedFile;
	}

	public void setDNASelectedFile(String DNASelectedFile) {
		this.DNASelectedFile = DNASelectedFile;
	}

	public String getDenzoContent() {
		return DenzoContent;
	}

	public void setDenzoContent(String denzoContent) {
		DenzoContent = denzoContent;
	}

	public boolean isDenzonContentPresent() {
		return DenzonContentPresent;
	}

	public void setDenzonContentPresent(boolean denzonContentPresent) {
		DenzonContentPresent = denzonContentPresent;
	}

	public String getExpectedDenzoPath() {
		return expectedDenzoPath;
	}

	public void setExpectedDenzoPath(String expectedDenzoPath) {
		this.expectedDenzoPath = expectedDenzoPath;
	}

	public boolean isDisplayDenzoContent() {
		return displayDenzoContent;
	}

	public void setDisplayDenzoContent(boolean displayDenzoContent) {
		this.displayDenzoContent = displayDenzoContent;
	}

	public String getExpectedSnapshotPath() {
		return expectedSnapshotPath;
	}

	public void setExpectedSnapshotPath(String expectedSnapshotPath) {
		this.expectedSnapshotPath = expectedSnapshotPath;
	}

	public List<SnapshotInfo> getListSnapshots() {
		return listSnapshots;
	}

	public void setListSnapshots(List<SnapshotInfo> listSnapshots) {
		this.listSnapshots = listSnapshots;
	}

	public SnapshotInfo getSnapshotInfo() {
		return snapshotInfo;
	}

	public void setSnapshotInfo(SnapshotInfo snapshotInfo) {
		this.snapshotInfo = snapshotInfo;
	}

	public Integer getTargetImageNumber() {
		return targetImageNumber;
	}

	public void setTargetImageNumber(Integer targetImageNumber) {
		this.targetImageNumber = targetImageNumber;
	}

	public Integer getTotalImageNumber() {
		return totalImageNumber;
	}

	public void setTotalImageNumber(Integer totalImageNumber) {
		this.totalImageNumber = totalImageNumber;
	}

	public Integer getSlitGapHorizontalMicro() {
		return slitGapHorizontalMicro;
	}

	public void setSlitGapHorizontalMicro(Integer slitGapHorizontalMicro) {
		this.slitGapHorizontalMicro = slitGapHorizontalMicro;
	}

	public Integer getSlitGapVerticalMicro() {
		return slitGapVerticalMicro;
	}

	public void setSlitGapVerticalMicro(Integer slitGapVerticalMicro) {
		this.slitGapVerticalMicro = slitGapVerticalMicro;
	}

	public Integer getBeamSizeAtSampleXMicro() {
		return beamSizeAtSampleXMicro;
	}

	public void setBeamSizeAtSampleXMicro(Integer beamSizeAtSampleXMicro) {
		this.beamSizeAtSampleXMicro = beamSizeAtSampleXMicro;
	}

	public Integer getBeamSizeAtSampleYMicro() {
		return beamSizeAtSampleYMicro;
	}

	public void setBeamSizeAtSampleYMicro(Integer beamSizeAtSampleYMicro) {
		this.beamSizeAtSampleYMicro = beamSizeAtSampleYMicro;
	}

	public Double getDetectorPixelSizeHorizontalMicro() {
		return detectorPixelSizeHorizontalMicro;
	}

	public void setDetectorPixelSizeHorizontalMicro(Double detectorPixelSizeHorizontalMicro) {
		this.detectorPixelSizeHorizontalMicro = detectorPixelSizeHorizontalMicro;
	}

	public Double getDetectorPixelSizeVerticalMicro() {
		return detectorPixelSizeVerticalMicro;
	}

	public void setDetectorPixelSizeVerticalMicro(Double detectorPixelSizeVerticalMicro) {
		this.detectorPixelSizeVerticalMicro = detectorPixelSizeVerticalMicro;
	}

	public Integer getTransmissionInt() {
		return transmissionInt;
	}

	public void setTransmissionInt(Integer transmissionInt) {
		this.transmissionInt = transmissionInt;
	}

	public Integer getBeamDivergenceHorizontalInt() {
		return beamDivergenceHorizontalInt;
	}

	public void setBeamDivergenceHorizontalInt(Integer beamDivergenceHorizontalInt) {
		this.beamDivergenceHorizontalInt = beamDivergenceHorizontalInt;
	}

	public Integer getBeamDivergenceVerticalInt() {
		return beamDivergenceVerticalInt;
	}

	public void setBeamDivergenceVerticalInt(Integer beamDivergenceVerticalInt) {
		this.beamDivergenceVerticalInt = beamDivergenceVerticalInt;
	}

	public String getBeamShape() {
		return beamShape;
	}

	public void setBeamShape(String beamShape) {
		this.beamShape = beamShape;
	}

	public Double getPredictedResolution() {
		return predictedResolution;
	}

	public void setPredictedResolution(Double predictedResolution) {
		DecimalFormat df2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df2.applyPattern("#####0.00");

		if (predictedResolution != null) {
			Double resolution = new Double(df2.format(predictedResolution));
			this.predictedResolution = resolution;
		}
	}

	public String getDetectorType() {
		return detectorType;
	}

	public void setDetectorType(String detectorType) {
		this.detectorType = detectorType;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public ImageValueInfo getImage() {
		return image;
	}

	public void setImage(ImageValueInfo image) {
		this.image = image;
	}

	public Crystal3VO getCrystal() {
		return crystal;
	}

	public void setCrystal(Crystal3VO crystal) {
		this.crystal = crystal;
	}

	public DataCollection3VO getDataCollection() {
		return dataCollection;
	}

	public void setDataCollection(DataCollection3VO dataCollection) {

		// NumberFormat nf1 = new DecimalFormat("#");
		DecimalFormat nf1 = (DecimalFormat) NumberFormat.getInstance(Locale.UK);
		nf1.applyPattern("#");
		DecimalFormat df1 = (DecimalFormat) NumberFormat.getInstance(Locale.UK);
		df1.applyPattern("#####0.0");
		DecimalFormat df2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df2.applyPattern("#####0.00");
		DecimalFormat df3 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df3.applyPattern("#####0.000");
		DecimalFormat df4 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df4.applyPattern("#####0.0000");
		DecimalFormat df5 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df5.applyPattern("#####0.00000");

		if (dataCollection.getAxisRange() != null) {
			Double axisRange = new Double(df2.format(dataCollection.getAxisRange()));
			dataCollection.setAxisRange(axisRange);
		}

		if (dataCollection.getExposureTime() != null) {
			Double exposure = new Double(df3.format(dataCollection.getExposureTime()));
			dataCollection.setExposureTime(exposure);
			if (dataCollection.getNumberOfImages() != null){
				setTotalExposureTime(new Double(df3.format(dataCollection.getExposureTime() * dataCollection.getNumberOfImages())));
			}
		}

		if (dataCollection.getWavelength() != null) {
			Double wavelength = new Double(df4.format(dataCollection.getWavelength()));
			dataCollection.setWavelength(wavelength);
		}

		if (dataCollection.getDetectorDistance() != null) {
			Double distance = new Double(df1.format(dataCollection.getDetectorDistance()));
			dataCollection.setDetectorDistance(distance);
		}

		if (dataCollection.getXbeam() != null) {
			Double xbeam = new Double(df2.format(dataCollection.getXbeam()));
			dataCollection.setXbeam(xbeam);
		}

		if (dataCollection.getYbeam() != null) {
			Double ybeam = new Double(df2.format(dataCollection.getYbeam()));
			dataCollection.setYbeam(ybeam);
		}

		if (dataCollection.getResolution() != null) {
			Double resol = new Double(df2.format(dataCollection.getResolution()));
			dataCollection.setResolution(resol);
		}

		if (dataCollection.getResolutionAtCorner() != null) {
			Double resol = new Double(df2.format(dataCollection.getResolutionAtCorner()));
			dataCollection.setResolutionAtCorner(resol);
		}

		if (dataCollection.getKappaStart() != null) {
			Double kappa = new Double(df2.format(dataCollection.getKappaStart()));
			dataCollection.setKappaStart(kappa);
		}

		if (dataCollection.getPhiStart() != null) {
			Double phi = new Double(df2.format(dataCollection.getPhiStart()));
			dataCollection.setPhiStart(phi);
		}

		if (dataCollection.getAxisStart() != null) {
			Double axis = new Double(df1.format(dataCollection.getAxisStart()));
			dataCollection.setAxisStart(axis);
		}

		if (dataCollection.getTransmission() != null) {
			transmissionInt = new Integer(nf1.format(dataCollection.getTransmission()));
		}

		if (dataCollection.getSlitGapHorizontal() != null) {
			// in DB beamsize unit is mm, display is in micrometer => conversion
			slitGapHorizontalMicro = new

			Integer(nf1.format(dataCollection.getSlitGapHorizontal().doubleValue() * 1000));
		}

		if (dataCollection.getSlitGapVertical() != null) {
			// in DB beamsize unit is mm, display is in micrometer => conversion
			slitGapVerticalMicro = new

			Integer(nf1.format(dataCollection.getSlitGapVertical().doubleValue() * 1000));
		}

		if (dataCollection.getBeamSizeAtSampleX() != null) {
			// in DB beamsize unit is mm, display is in micrometer => conversion
			beamSizeAtSampleXMicro = new

			Integer(nf1.format(dataCollection.getBeamSizeAtSampleX().doubleValue() * 1000));
		}

		if (dataCollection.getBeamSizeAtSampleY() != null) {
			// in DB beamsize unit is mm, display is in micrometer => conversion
			beamSizeAtSampleYMicro = new

			Integer(nf1.format(dataCollection.getBeamSizeAtSampleY().doubleValue() * 1000));
		}

		if (dataCollection.getDetectorVO() != null &&
				 dataCollection.getDetectorVO().getDetectorPixelSizeHorizontal() != null) {
			// in DB pixel size unit is mm, 
			detectorPixelSizeHorizontalMicro = new

			Double(df5.format(dataCollection.getDetectorVO().getDetectorPixelSizeHorizontal()));
		}

		if (dataCollection.getDetectorVO() != null &&
				dataCollection.getDetectorVO().getDetectorPixelSizeVertical() != null) {
			// in DB pixel size unit is mm, display is in micrometer => conversion
			detectorPixelSizeVerticalMicro = new

			Double(df5.format(dataCollection.getDetectorVO().getDetectorPixelSizeVertical()));
		}
		
		

		if (beamLine.getBeamDivergenceHorizontal() != null) {
			beamDivergenceHorizontalInt = new

			Integer(nf1.format(beamLine.getBeamDivergenceHorizontal()));
		}

		if (beamLine.getBeamDivergenceVertical() != null) {
			beamDivergenceVerticalInt = new Integer(nf1.format(beamLine.getBeamDivergenceVertical()));
		}

		this.dataCollection = dataCollection;
	}

	public DiffractionPlan3VO getDifPlan() {
		return difPlan;
	}

	public void setDifPlan(DiffractionPlan3VO difPlan) {
		this.difPlan = difPlan;
	}

	public Protein3VO getProtein() {
		return protein;
	}

	public void setProtein(Protein3VO protein) {
		this.protein = protein;
	}

	public BLSample3VO getSample() {
		return sample;
	}

	public void setSample(BLSample3VO sample) {
		this.sample = sample;
	}

	public void setBeamLine(BeamLineSetup3VO beamLine) {
		NumberFormat nf1 = NumberFormat.getIntegerInstance();
		DecimalFormat df2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df2.applyPattern("#####0.00");

		
		if (beamLine.getPolarisation() != null) {
			Double polarisation = new Double(df2.format(beamLine.getPolarisation()));
			beamLine.setPolarisation(polarisation);
		}

		if (beamLine.getBeamDivergenceHorizontal() != null) {
			Double beamDivergenceHorizontal = new

			Double(nf1.format(beamLine.getBeamDivergenceHorizontal()));
			beamLine.setBeamDivergenceHorizontal(beamDivergenceHorizontal);
		}

		if (beamLine.getBeamDivergenceVertical() != null) {
			Double beamDivergenceVertical = new

			Double(nf1.format(beamLine.getBeamDivergenceVertical()));
			beamLine.setBeamDivergenceVertical(beamDivergenceVertical);
		}

		this.beamLine = beamLine;
	}

	public BeamLineSetup3VO getBeamLine() {
		return beamLine;
	}

	public void setSession(Session3VO sessionlv) {
		this.session = sessionlv;
	}

	public Session3VO getSession() {
		return session;
	}

	public Screening3VO[] getScreeningList() {
		return screeningList;
	}

	public void setScreeningList(Screening3VO[] screeningList) {
		this.screeningList = screeningList;
	}

	public Screening3VO getScreening() {
		return screening;
	}

	public void setScreening(Screening3VO screening) {
		this.screening = screening;
	}

	public ScreeningInput3VO getScreeningInput() {
		return screeningInput;
	}

	public void setScreeningInput(ScreeningInput3VO screeningInput) {
		this.screeningInput = screeningInput;
	}

	public ScreeningRank3VO getScreeningRank() {
		return screeningRank;
	}

	public void setScreeningRank(ScreeningRank3VO screeningRank) {
		this.screeningRank = screeningRank;
	}

	public ScreeningRankSet3VO getScreeningRankSet() {
		return screeningRankSet;
	}

	public void setScreeningRankSet(ScreeningRankSet3VO screeningRankSet) {
		this.screeningRankSet = screeningRankSet;
	}

	public ScreeningOutput3VO getScreeningOutput() {
		return screeningOutput;
	}

	public void setScreeningOutput(ScreeningOutput3VO screeningOutput) {

		DecimalFormat df1 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df1.applyPattern("#####0.0");
		DecimalFormat df2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df2.applyPattern("#####0.00");
		DecimalFormat df3 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df3.applyPattern("#####0.000");

		if (screeningOutput.getMosaicity() != null) {
			Double mosaicity = new Double(df2.format(screeningOutput.getMosaicity()));
			screeningOutput.setMosaicity(mosaicity);
		}
		if (screeningOutput.getResolutionObtained() != null) {
			Double resolutionObtained = new Double(df2.format(screeningOutput.getResolutionObtained()));
			screeningOutput.setResolutionObtained(resolutionObtained);
		}
		if (screeningOutput.getIoverSigma() != null) {
			Double iOverSigma = new Double(df1.format(screeningOutput.getIoverSigma()));
			screeningOutput.setIoverSigma(iOverSigma);
		}
		if (screeningOutput.getSpotDeviationR() != null) {
			Double spotDeviationR = new Double(df3.format(screeningOutput.getSpotDeviationR()));
			screeningOutput.setSpotDeviationR(spotDeviationR);
		}
		if (screeningOutput.getSpotDeviationTheta() != null) {
			Double spotDeviationTheta = new Double(df3.format(screeningOutput.getSpotDeviationTheta()));
			screeningOutput.setSpotDeviationTheta(spotDeviationTheta);
		}
		if (screeningOutput.getBeamShiftX() != null) {
			Double beamShiftX = new Double(df3.format(screeningOutput.getBeamShiftX()));
			screeningOutput.setBeamShiftX(beamShiftX);
		}
		if (screeningOutput.getBeamShiftY() != null) {
			Double beamShiftY = new Double(df3.format(screeningOutput.getBeamShiftY()));
			screeningOutput.setBeamShiftY(beamShiftY);
		}

		this.screeningOutput = screeningOutput;
	}

	/**
	 * @return Returns the screeningOutputLattice.
	 */
	public ScreeningOutputLattice3VO getScreeningOutputLattice() {
		return screeningOutputLattice;
	}

	/**
	 * @param screeningOutputLattice
	 *            The screeningOutputLattice to set.
	 */
	public void setScreeningOutputLattice(ScreeningOutputLattice3VO screeningOutputLattice) {

		DecimalFormat df3 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df3.applyPattern("#####0.000");

		if (screeningOutputLattice.getUnitCell_a() != null) {
			Double unitCellA = new Double(df3.format(screeningOutputLattice.getUnitCell_a()));
			screeningOutputLattice.setUnitCell_a(unitCellA);
		}
		if (screeningOutputLattice.getUnitCell_b() != null) {
			Double unitCellB = new Double(df3.format(screeningOutputLattice.getUnitCell_b()));
			screeningOutputLattice.setUnitCell_b(unitCellB);
		}
		if (screeningOutputLattice.getUnitCell_c() != null) {
			Double unitCellC = new Double(df3.format(screeningOutputLattice.getUnitCell_c()));
			screeningOutputLattice.setUnitCell_c(unitCellC);
		}
		if (screeningOutputLattice.getUnitCell_alpha() != null) {
			Double unitCellAlpha = new Double(df3.format(screeningOutputLattice.getUnitCell_alpha()));
			screeningOutputLattice.setUnitCell_alpha(unitCellAlpha);
		}
		if (screeningOutputLattice.getUnitCell_beta() != null) {
			Double unitCellBeta = new Double(df3.format(screeningOutputLattice.getUnitCell_beta()));
			screeningOutputLattice.setUnitCell_beta(unitCellBeta);
		}
		if (screeningOutputLattice.getUnitCell_gamma() != null) {
			Double unitCellGamma = new Double(df3.format(screeningOutputLattice.getUnitCell_gamma()));
			screeningOutputLattice.setUnitCell_gamma(unitCellGamma);
		}

		this.screeningOutputLattice = screeningOutputLattice;
	}

	/**
	 * @return Returns the screeningStrategy.
	 */
	public ScreeningStrategy3VO getScreeningStrategy() {
		return screeningStrategy;
	}

	/**
	 * @param screeningStrategy
	 *            The screeningStrategy to set.
	 */
	public void setScreeningStrategy(ScreeningStrategy3VO screeningStrategy) {
		this.screeningStrategy = screeningStrategy;
	}

	/**
	 * @return Returns the screeningStrategyList.
	 */
	public ScreeningStrategy3VO[] getScreeningStrategyList() {
		return screeningStrategyList;
	}

	/**
	 * @param screeningStrategyList
	 *            The screeningStrategyList to set.
	 */
	public void setScreeningStrategyList(ScreeningStrategy3VO[] screeningStrategyList) {
		this.screeningStrategyList = screeningStrategyList;

		for (int i = screeningStrategyList.length - 1; i >= 0; i--) {
			ScreeningStrategy3VO sslv = screeningStrategyList[i];

			if (sslv.getRankingResolution() != null) {
				this.setPredictedResolution(sslv.getRankingResolution());
				break;
			}
			// TODO get rid of this commented code if the following trick no more useful
			// check with Olof for EDNA
			//
			// if (sslv.getProgram().toUpperCase().indexOf("BEST") != -1) {
			// this.setPredictedResolution(sslv.getRankingResolution());
			// break;
			// }
		}

	}

	/**
	 * @return Returns the list of DNA strategies + data
	 */
	public List<ScreeningStrategyValueInfo> getListStrategiesInfo() {
		return listStrategiesInfo;
	}

	/**
	 * @param listStrategiesInfo
	 *            The listStrategiesInfo to set.
	 */
	public void setListStrategiesInfo(List<ScreeningStrategyValueInfo> listStrategiesInfo) {
		this.listStrategiesInfo = listStrategiesInfo;
	}

	/**
	 * @return Returns the screeningStrategyWedge.
	 */
	public ScreeningStrategyWedge3VO getScreeningStrategyWedge() {
		return screeningStrategyWedge;
	}

	/**
	 * @param screeningStrategyWedge
	 *            The screeningStrategyWedge to set.
	 */
	public void setScreeningStrategyWedge(ScreeningStrategyWedge3VO screeningStrategyWedge) {
		this.screeningStrategyWedge = screeningStrategyWedge;
	}

	
	public ScreeningStrategyWedge3VO[] getScreeningStrategyWedgeList() {
		return screeningStrategyWedgeList;
	}

	public void setScreeningStrategyWedgeList(
			ScreeningStrategyWedge3VO[] screeningStrategyWedgeList) {
		this.screeningStrategyWedgeList = screeningStrategyWedgeList;
	}

	public List<ScreeningStrategyWedgeValueInfo> getListStrategiesWedgeInfo() {
		return listStrategiesWedgeInfo;
	}

	public void setListStrategiesWedgeInfo(
			List<ScreeningStrategyWedgeValueInfo> listStrategiesWedgeInfo) {
		this.listStrategiesWedgeInfo = listStrategiesWedgeInfo;
	}

	/**
	 * @return Returns the energy.
	 */
	public Double getEnergy() {
		return energy;
	}

	/**
	 * @param energy
	 *            The energy to set.
	 */
	public void setEnergy(Double wavelength) {
		DecimalFormat df3 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df3.applyPattern("#####0.000");
		Double energy = null;
		if (wavelength != null && wavelength.compareTo(new Double(0)) != 0)
			energy = new Double(df3.format(Constants.WAVELENGTH_TO_ENERGY_CONSTANT / wavelength));
		this.energy = energy;
	}

	/**
	 * @return Returns the undulatorGaps.
	 */
	public String getUndulatorGaps() {
		return undulatorGaps;
	}

	/**
	 * @param undulatorGaps
	 *            The undulatorGaps to set.
	 */
	public void setUndulatorGaps(Double undulatorGap1, Double undulatorGap2, Double undulatorGap3) {
		// Number format Exception if number > 999 or < -999
		DecimalFormat df2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		// DecimalFormat df2 = new DecimalFormat("##0.##");
		df2.applyPattern("##0.##");
		StringBuffer buf = new StringBuffer();
		// if no type then there is no meaningful value
		// if no undulator 1 then no 2 and no 3
		if (this.beamLine.getUndulatorType1() != null && this.beamLine.getUndulatorType1().length() > 0) {
			if (undulatorGap1 != null && !undulatorGap1.equals(Constants.SILLY_NUMBER)) {
				Double gap1 = new Double(df2.format(undulatorGap1));
				buf.append(gap1.toString()).append(" mm ");
			}
			if (this.beamLine.getUndulatorType2() != null && this.beamLine.getUndulatorType2().length()

			> 0) {
				if (undulatorGap2 != null && !undulatorGap2.equals(Constants.SILLY_NUMBER)) {
					Double gap2 = new Double(df2.format(undulatorGap2));
					buf.append(gap2.toString()).append(" mm ");
				}
				if (this.beamLine.getUndulatorType3() != null &&

				this.beamLine.getUndulatorType3().length() > 0) {
					if (undulatorGap3 != null && !undulatorGap3.equals(Constants.SILLY_NUMBER))

					{
						Double gap3 = new Double(df2.format(undulatorGap3));
						buf.append(gap3.toString()).append(" mm ");
					}
				}
			}
		}
		this.undulatorGaps = buf.toString();
	}

	/**
	 * @return Returns the undulatorTypes.
	 */
	public String getUndulatorTypes() {
		return undulatorTypes;
	}

	/**
	 * @param undulatorTypes
	 *            The undulatorTypes to set.
	 */
	public void setUndulatorTypes(String undulatorType1, String undulatorType2, String undulatorType3) {

		StringBuffer buf = new StringBuffer();
		if (undulatorType1 != null) {
			buf.append(undulatorType1).append(" ");
		}
		if (undulatorType2 != null) {
			buf.append(undulatorType2).append(" ");
		}
		if (undulatorType3 != null) {
			buf.append(undulatorType3).append(" ");
		}

		this.undulatorTypes = buf.toString();
	}

	/**
	 * @return Returns the axisStartLabel (= rotationAxis + " start"').
	 */
	public String getAxisStartLabel() {
		return axisStartLabel;
	}

	/**
	 * @param axisStartLabel
	 *            The axisStartLabel to set.
	 */
	public void setAxisStartLabel(String axisRotation) {

		StringBuffer buf = new StringBuffer();
		if (axisRotation != null) {
			buf.append(axisRotation).append("&nbsp;start");
		}

		this.axisStartLabel = buf.toString();
	}

	/**
	 * @return Returns the kappaStr.
	 */
	public String getKappaStr() {
		Double kappa = this.dataCollection.getKappaStart();
		if (kappa == null || kappa.equals(Constants.SILLY_NUMBER))
			//return new String("N/A");
			return new String("0");
		return new String(kappa.toString());
	}

	/**
	 * @return Returns the phiStr.
	 */
	public String getPhiStr() {
		Double phi = this.dataCollection.getPhiStart();
		if (phi == null || phi.equals(Constants.SILLY_NUMBER))
			//return new String("N/A");
			return new String("0");
		return new String(phi.toString());
	}


	public java.util.Collection<AutoProc3VO> getAutoProcs() {
		return this.autoProcs;
	}

	/*
	 * public void setAutoProcs(java.util.Collection<AutoProc3VO> autoProcs) { this.autoProcs = autoProcs;
	 * updateAutoProcLabels(); updateAutoProcIdLabels(); }
	 * 
	 * public java.util.Collection<String> getAutoProcLabels() { return autoProcLabels; }
	 * 
	 * public void setAutoProcLabels(java.util.Collection<String> autoProcLabels) { this.autoProcLabels =
	 * autoProcLabels; }
	 * 
	 * private void updateAutoProcLabels() { this.autoProcLabels = new ArrayList<String>();
	 * 
	 * if (autoProcs == null) return;
	 * 
	 * for (java.util.Iterator i = this.autoProcs.iterator(); i.hasNext();) {
	 * 
	 * AutoProc3VO apv = (AutoProc3VO) i.next(); double refinedCellA = ((double) ((int) (apv.getRefinedCellA() * 10))) /
	 * 10; // round to 1 dp double refinedCellB = ((double) ((int) (apv.getRefinedCellB() * 10))) / 10; // round to 1 dp
	 * double refinedCellC = ((double) ((int) (apv.getRefinedCellC() * 10))) / 10; // round to 1 dp
	 * 
	 * autoProcLabels.add(apv.getSpaceGroup() + ": " + refinedCellA + ", " + refinedCellB + ", " + refinedCellC + " (" +
	 * apv.getRefinedCellAlpha() + ", " + apv.getRefinedCellBeta() + ", " + apv.getRefinedCellGamma() + ")"); }
	 * 
	 * }
	 * 
	 * public java.util.Collection<String> getAutoProcIdLabels() { return autoProcIdLabels; }
	 * 
	 * public void setAutoProcIdLabels(java.util.Collection<String> autoProcIdLabels) { this.autoProcIdLabels =
	 * autoProcIdLabels; }
	 * 
	 * private void updateAutoProcIdLabels() { this.autoProcIdLabels = new ArrayList<String>();
	 * 
	 * if (autoProcs == null) return;
	 * 
	 * for (java.util.Iterator i = this.autoProcs.iterator(); i.hasNext();) {
	 * 
	 * AutoProc3VO apv = (AutoProc3VO) i.next(); autoProcIdLabels.add("" + apv.getAutoProcId()); }
	 * 
	 * }
	 */

	public String getAutoProcProgramAttachmentId() {
		return autoProcProgramAttachmentId;
	}

	public void setAutoProcProgramAttachmentId(String autoProcProgramAttachmentId) {
		this.autoProcProgramAttachmentId = autoProcProgramAttachmentId;
	}

	public String getAutoProcAttachmentContent() {
		return autoProcAttachmentContent;
	}

	public void setAutoProcAttachmentContent(String autoProcAttachmentContent) {
		this.autoProcAttachmentContent = autoProcAttachmentContent;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getSelectedAutoProcLabel() {
		return selectedAutoProcLabel;
	}

	public void setSelectedAutoProcLabel(String selectedAutoProcLabel) {
		this.selectedAutoProcLabel = selectedAutoProcLabel;
	}

	public String getAutoProcAttachmentName() {
		return autoProcAttachmentName;
	}

	public void setAutoProcAttachmentName(String autoProcAttachmentName) {
		this.autoProcAttachmentName = autoProcAttachmentName;
	}

	public String getRmerge() {
		return rmerge;
	}

	public void setRmerge(String merge) {
		rmerge = merge;
	}

	public String getIsigma() {
		return isigma;
	}

	public void setIsigma(String sigma) {
		isigma = sigma;
	}

	public String getCurrentImageId() {
		return currentImageId;
	}

	public void setCurrentImageId(String currentImageId) {
		this.currentImageId = currentImageId;
	}
	
	/**
	 * @param screeningStrategySubWedge
	 *            The screeningStrategySubWedge to set.
	 */
	public void setScreeningStrategySubWedge(ScreeningStrategySubWedge3VO screeningStrategySubWedge) {
		this.screeningStrategySubWedge = screeningStrategySubWedge;
	}
	
	/**
	 * @return Returns the screeningStrategySubWedgeList.
	 */
	public ScreeningStrategySubWedge3VO[][] getScreeningStrategySubWedgeListAll() {
		return screeningStrategySubWedgeListAll;
	}

	/**
	 * @param screeningStrategySubWedgeList
	 *            The screeningStrategyWedgeList to set.
	 */
	public void setScreeningStrategySubWedgeListAll(ScreeningStrategySubWedge3VO[][] screeningStrategySubWedgeListAll) {
		this.screeningStrategySubWedgeListAll = screeningStrategySubWedgeListAll;
	}
	
	/**
	 * @return Returns the list of DNA strategies sub wedge+ data
	 */
	public List<ScreeningStrategySubWedgeValueInfo>[] getListStrategiesSubWedgeInfoAll() {
		return listStrategiesSubWedgeInfoAll;
	}

	/**
	 * @param listStrategiesSubWedgeInfo
	 *            The listStrategiesSubWedgeInfo to set.
	 */
	public void setListStrategiesSubWedgeInfoAll(List<ScreeningStrategySubWedgeValueInfo>[] listStrategiesSubWedgeInfoAll) {
		this.listStrategiesSubWedgeInfoAll = listStrategiesSubWedgeInfoAll;
	}
	
	/**
	 * @return Returns the screeningStrategySubWedgeList.
	 */
	public ScreeningStrategySubWedge3VO[] getScreeningStrategySubWedgeList() {
		return screeningStrategySubWedgeList;
	}

	/**
	 * @param screeningStrategySubWedgeList
	 *            The screeningStrategyWedgeList to set.
	 */
	public void setScreeningStrategySubWedgeList(ScreeningStrategySubWedge3VO[] screeningStrategySubWedgeList) {
		this.screeningStrategySubWedgeList = screeningStrategySubWedgeList;
	}
	
	/**
	 * @return Returns the list of DNA strategies sub wedge+ data
	 */
	public List<ScreeningStrategySubWedgeValueInfo> getListStrategiesSubWedgeInfo() {
		return listStrategiesSubWedgeInfo;
	}

	/**
	 * @param listStrategiesSubWedgeInfo
	 *            The listStrategiesSubWedgeInfo to set.
	 */
	public void setListStrategiesSubWedgeInfo(List<ScreeningStrategySubWedgeValueInfo> listStrategiesSubWedgeInfo) {
		this.listStrategiesSubWedgeInfo = listStrategiesSubWedgeInfo;
	}

	public ScreeningStrategySubWedge3VO getScreeningStrategySubWedge() {
		return screeningStrategySubWedge;
	}

	public Double getTotalExposureTime() {
		return totalExposureTime;
	}

	public void setTotalExposureTime(Double totalExposureTime) {
		this.totalExposureTime = totalExposureTime;
	}

	public List<List<AutoProcStatus3VO>> getInterruptedAutoProcEvents() {
		return interruptedAutoProcEvents;
	}

	public void setInterruptedAutoProcEvents(
			List<List<AutoProcStatus3VO>> interruptedAutoProcEvents) {
		this.interruptedAutoProcEvents = interruptedAutoProcEvents;
	}

	public String getHtmlFileContent() {
		return htmlFileContent;
	}

	public void setHtmlFileContent(String htmlFileContent) {
		this.htmlFileContent = htmlFileContent;
	}

}
