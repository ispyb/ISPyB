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

package ispyb.client.mx.collection;

import ispyb.server.mx.vos.autoproc.AutoProc3VO;
import ispyb.server.mx.vos.autoproc.AutoProcScalingStatistics3VO;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.screening.ScreeningOutput3VO;
import ispyb.server.mx.vos.screening.ScreeningOutputLattice3VO;

/**
 * information regarding a collect to be displayed
 * 
 * @author BODIN
 * 
 */
public class DataCollectionBean extends DataCollection3VO {

	private static final long serialVersionUID = 3907662702130564883L;

	/* session beamLineName */
	protected java.lang.String beamLineName;

	/* screening screeningId */
	protected java.lang.Integer screeningId;

	/* screeningOutput screeningOutputId */
	protected java.lang.Integer screeningOutputId;

	/* screeningOutput indexingSuccess */
	protected Byte indexingSuccess;

	/* screeningOutput strategySuccess */
	protected Byte strategySuccess;

	/* EnergyScan energyScanId */
	protected java.lang.Integer energyScanId;

	/* Protein acronym */
	protected java.lang.String acronym;

	/* PDB file name */
	protected java.lang.String pdbFileName;

	/* BLSample name */
	protected java.lang.String sampleName;

	/* screeningStrategy screeningStrategyId */
	protected java.lang.Integer screeningStrategyId;

	/* ScreeningOutputLattice.spaceGroup */
	protected String spaceGroup;

	/* ScreeningOutputLattice.untiCell_* -- format */
	protected Double unitCell_a;

	protected Double unitCell_b;

	protected Double unitCell_c;

	protected Double unitCell_alpha;

	protected Double unitCell_beta;

	protected Double unitCell_gamma;

	/* ScreeningOutput.rankingResolution */
	protected Double rankingResolution;

	/* AutoProcStatus.step */
	protected String autoprocessingStep;

	/* AutoProcStatus.status */
	protected String autoprocessingStatus;

	/* has at least 1 autoproc attachment */
	// private Byte hasAttachment;

	protected String proposal;

	// protected java.lang.String pdbFileName;

	protected String experimentType;

	protected boolean hasSnapshot;

	protected AutoProc3VO autoProc;

	protected AutoProcScalingStatistics3VO autoProcStatisticsOverall;

	protected AutoProcScalingStatistics3VO autoProcStatisticsInner;

	protected AutoProcScalingStatistics3VO autoProcStatisticsOuter;

	protected ScreeningOutputLattice3VO lattice;

	protected ScreeningOutput3VO screeningOutput;

	// protected String autoprocessingStatus;

	// protected String autoprocessingStep;

	protected Double energy;

	protected String axisStartLabel;

	protected Double totalExposureTime;

	protected String kappa;

	protected String phi;

	protected String undulatorGaps;

	protected Integer slitGapHorizontalMicro;

	protected Integer slitGapVerticalMicro;

	protected Double detectorPixelSizeHorizontalMicro;

	protected Double detectorPixelSizeVerticalMicro;

	protected Integer beamSizeAtSampleXMicro;

	protected Integer beamSizeAtSampleYMicro;

	protected Integer beamDivergenceHorizontalInt;

	protected Integer beamDivergenceVerticalInt;

	protected boolean hasAutoProcAttachment;

	protected boolean skipForReport;

	protected boolean selectedToRank;

	public DataCollectionBean() {
		super();
	}

	public DataCollectionBean(DataCollection3VO vo) {
		super(vo);
		// this.beamLineName = null;
		// this.screeningId = null;
		// this.screeningOutputId = null;
		// this.indexingSuccess = null;
		// this.strategySuccess = null;
		// this.energyScanId = null;
		// this.acronym = null;
		// this.pdbFileName = null;
		// this.sampleName = null;
		// this.screeningStrategyId = null;
		// this.spaceGroup = null;
		// this.unitCell_a = null;
		// this.unitCell_b = null;
		// this.unitCell_c = null;
		// this.unitCell_alpha = null;
		// this.unitCell_beta = null;
		// this.unitCell_gamma = null;
		// this.rankingResolution = null;
		// this.autoprocessingStatus = null;
		// this.autoprocessingStep = null;
	}

	// keep this finder for old DataCollection and remove later when merged with other constructor
	public DataCollectionBean(DataCollection3VO vo, String beamLineName, String proposal, String proteinAcronym, String pdbFileName,
			String experimentType, boolean hasSnapshot, AutoProc3VO autoProc, AutoProcScalingStatistics3VO autoProcStatisticsOverall,
			AutoProcScalingStatistics3VO autoProcStatisticsInner, AutoProcScalingStatistics3VO autoProcStatisticsOuter,
			ScreeningOutput3VO screeningOutput, ScreeningOutputLattice3VO lattice, String autoprocessingStatus,
			String autoprocessingStep, boolean hasAttachment) {
		super(vo);
		this.beamLineName = beamLineName;
		this.proposal = proposal;
		this.acronym = proteinAcronym;
		this.pdbFileName = pdbFileName;
		this.experimentType = experimentType;
		this.hasSnapshot = hasSnapshot;
		this.autoProc = autoProc;
		this.autoProcStatisticsOverall = autoProcStatisticsOverall;
		this.autoProcStatisticsInner = autoProcStatisticsInner;
		this.autoProcStatisticsOuter = autoProcStatisticsOuter;
		this.screeningOutput = screeningOutput;
		this.lattice = lattice;
		this.autoprocessingStatus = autoprocessingStatus;
		this.autoprocessingStep = autoprocessingStep;
		this.hasAutoProcAttachment = hasAttachment;
		this.skipForReport = false;
		this.selectedToRank = false;
		if (vo.getPrintableForReport() != null && vo.getPrintableForReport().intValue() == 0)
			this.skipForReport = true;
	}

	// keep this finder for old DataCollectionGUI and remove later when merged with other constructor
	public DataCollectionBean(String beamLineName, Integer screeningId, Integer screeningOutputId, Byte indexingSuccess,
			Byte strategySuccess, Integer energyScanId, String acronym, String pdbFileName, String sampleName,
			Integer screeningStrategyId, String spaceGroup, Double unitCell_a, Double unitCell_b, Double unitCell_c,
			Double unitCell_alpha, Double unitCell_beta, Double unitCell_gamma, Double rankingResolution, String autoprocessingStatus,
			String autoprocessingStep, boolean hasAttachment) {
		super();
		this.beamLineName = beamLineName;
		this.screeningId = screeningId;
		this.screeningOutputId = screeningOutputId;
		this.indexingSuccess = indexingSuccess;
		this.strategySuccess = strategySuccess;
		this.energyScanId = energyScanId;
		this.acronym = acronym;
		this.pdbFileName = pdbFileName;
		this.sampleName = sampleName;
		this.screeningStrategyId = screeningStrategyId;
		this.spaceGroup = spaceGroup;
		this.unitCell_a = unitCell_a;
		this.unitCell_b = unitCell_b;
		this.unitCell_c = unitCell_c;
		this.unitCell_alpha = unitCell_alpha;
		this.unitCell_beta = unitCell_beta;
		this.unitCell_gamma = unitCell_gamma;
		this.rankingResolution = rankingResolution;
		this.autoprocessingStatus = autoprocessingStatus;
		this.autoprocessingStep = autoprocessingStep;
		this.hasAutoProcAttachment = hasAttachment;
		this.skipForReport = false;
		if (printableForReport != null && printableForReport.intValue() == 0)
			this.skipForReport = true;
		this.selectedToRank = false;
	}

	public DataCollectionBean(String beamLineName, Integer screeningId, Integer screeningOutputId, Byte indexingSuccess,
			Byte strategySuccess, Integer energyScanId, String acronym, String pdbFileName, String sampleName,
			Integer screeningStrategyId, String spaceGroup, Double unitCell_a, Double unitCell_b, Double unitCell_c,
			Double unitCell_alpha, Double unitCell_beta, Double unitCell_gamma, Double rankingResolution, String autoprocessingStatus,
			String autoprocessingStep, boolean hasAttachment, String proposal, String experimentType, boolean hasSnapshot,
			AutoProc3VO autoProc, AutoProcScalingStatistics3VO autoProcStatisticsOverall,
			AutoProcScalingStatistics3VO autoProcStatisticsInner, AutoProcScalingStatistics3VO autoProcStatisticsOuter,
			ScreeningOutput3VO screeningOutput, ScreeningOutputLattice3VO lattice) {
		super();
		this.beamLineName = beamLineName;
		this.screeningId = screeningId;
		this.screeningOutputId = screeningOutputId;
		this.indexingSuccess = indexingSuccess;
		this.strategySuccess = strategySuccess;
		this.energyScanId = energyScanId;
		this.acronym = acronym;
		this.pdbFileName = pdbFileName;
		this.sampleName = sampleName;
		this.screeningStrategyId = screeningStrategyId;
		this.spaceGroup = spaceGroup;
		this.unitCell_a = unitCell_a;
		this.unitCell_b = unitCell_b;
		this.unitCell_c = unitCell_c;
		this.unitCell_alpha = unitCell_alpha;
		this.unitCell_beta = unitCell_beta;
		this.unitCell_gamma = unitCell_gamma;
		this.rankingResolution = rankingResolution;
		this.autoprocessingStatus = autoprocessingStatus;
		this.autoprocessingStep = autoprocessingStep;
		this.proposal = proposal;
		this.experimentType = experimentType;
		this.hasSnapshot = hasSnapshot;
		this.autoProc = autoProc;
		this.autoProcStatisticsOverall = autoProcStatisticsOverall;
		this.autoProcStatisticsInner = autoProcStatisticsInner;
		this.autoProcStatisticsOuter = autoProcStatisticsOuter;
		this.screeningOutput = screeningOutput;
		this.lattice = lattice;
		this.hasAutoProcAttachment = hasAttachment;
		this.skipForReport = false;
		if (printableForReport != null && printableForReport.intValue() == 0)
			this.skipForReport = true;
		this.selectedToRank = false;
	}

	public java.lang.String getBeamLineName() {
		return beamLineName;
	}

	public void setBeamLineName(java.lang.String beamLineName) {
		this.beamLineName = beamLineName;
	}

	public java.lang.Integer getScreeningId() {
		return screeningId;
	}

	public void setScreeningId(java.lang.Integer screeningId) {
		this.screeningId = screeningId;
	}

	public java.lang.Integer getScreeningOutputId() {
		return screeningOutputId;
	}

	public void setScreeningOutputId(java.lang.Integer screeningOutputId) {
		this.screeningOutputId = screeningOutputId;
	}

	public java.lang.Integer getEnergyScanId() {
		return energyScanId;
	}

	public void setEnergyScanId(java.lang.Integer energyScanId) {
		this.energyScanId = energyScanId;
	}

	@Override
	public java.lang.String getAcronym() {
		return acronym;
	}

	public void setAcronym(java.lang.String acronym) {
		this.acronym = acronym;
	}

	public java.lang.String getPdbFileName() {
		return pdbFileName;
	}

	public void setPdbFileName(java.lang.String pdbFileName) {
		this.pdbFileName = pdbFileName;
	}

	public java.lang.String getSampleName() {
		return sampleName;
	}

	public void setSampleName(java.lang.String sampleName) {
		this.sampleName = sampleName;
	}

	public java.lang.Integer getScreeningStrategyId() {
		return screeningStrategyId;
	}

	public void setScreeningStrategyId(java.lang.Integer screeningStrategyId) {
		this.screeningStrategyId = screeningStrategyId;
	}

	public Byte getIndexingSuccess() {
		return indexingSuccess;
	}

	public void setIndexingSuccess(Byte indexingSuccess) {
		this.indexingSuccess = indexingSuccess;
	}

	public Byte getStrategySuccess() {
		return strategySuccess;
	}

	public void setStrategySuccess(Byte strategySuccess) {
		this.strategySuccess = strategySuccess;
	}

	public String getSpaceGroup() {
		return spaceGroup;
	}

	public void setSpaceGroup(String spaceGroup) {
		this.spaceGroup = spaceGroup;
	}

	public Double getUnitCell_a() {
		return unitCell_a;
	}

	public void setUnitCell_a(Double unitCell_a) {
		this.unitCell_a = unitCell_a;
	}

	public Double getUnitCell_b() {
		return unitCell_b;
	}

	public void setUnitCell_b(Double unitCell_b) {
		this.unitCell_b = unitCell_b;
	}

	public Double getUnitCell_c() {
		return unitCell_c;
	}

	public void setUnitCell_c(Double unitCell_c) {
		this.unitCell_c = unitCell_c;
	}

	public Double getUnitCell_alpha() {
		return unitCell_alpha;
	}

	public void setUnitCell_alpha(Double unitCell_alpha) {
		this.unitCell_alpha = unitCell_alpha;
	}

	public Double getUnitCell_beta() {
		return unitCell_beta;
	}

	public void setUnitCell_beta(Double unitCell_beta) {
		this.unitCell_beta = unitCell_beta;
	}

	public Double getUnitCell_gamma() {
		return unitCell_gamma;
	}

	public void setUnitCell_gamma(Double unitCell_gamma) {
		this.unitCell_gamma = unitCell_gamma;
	}

	public Double getRankingResolution() {
		return rankingResolution;
	}

	public void setRankingResolution(Double rankingResolution) {
		this.rankingResolution = rankingResolution;
	}

	public String getAutoprocessingStep() {
		return autoprocessingStep;
	}

	public void setAutoprocessingStep(String autoprocessingStep) {
		this.autoprocessingStep = autoprocessingStep;
	}

	public String getAutoprocessingStatus() {
		return autoprocessingStatus;
	}

	public void setAutoprocessingStatus(String autoprocessingStatus) {
		this.autoprocessingStatus = autoprocessingStatus;
	}

	public Byte getHasAttachment() {
		if (this.hasAutoProcAttachment)
			return new Byte((byte) 1);
		else
			return new Byte((byte) 0);
	}

	public String getProposal() {
		return proposal;
	}

	public void setProposal(String proposal) {
		this.proposal = proposal;
	}

	public String getExperimentType() {
		return experimentType;
	}

	public void setExperimentType(String experimentType) {
		this.experimentType = experimentType;
	}

	public boolean isHasSnapshot() {
		return hasSnapshot;
	}

	public void setHasSnapshot(boolean hasSnapshot) {
		this.hasSnapshot = hasSnapshot;
	}

	public AutoProc3VO getAutoProc() {
		return autoProc;
	}

	public void setAutoProc(AutoProc3VO autoProc) {
		this.autoProc = autoProc;
	}

	public AutoProcScalingStatistics3VO getAutoProcStatisticsOverall() {
		return autoProcStatisticsOverall;
	}

	public void setAutoProcStatisticsOverall(AutoProcScalingStatistics3VO autoProcStatisticsOverall) {
		this.autoProcStatisticsOverall = autoProcStatisticsOverall;
	}

	public AutoProcScalingStatistics3VO getAutoProcStatisticsInner() {
		return autoProcStatisticsInner;
	}

	public void setAutoProcStatisticsInner(AutoProcScalingStatistics3VO autoProcStatisticsInner) {
		this.autoProcStatisticsInner = autoProcStatisticsInner;
	}

	public AutoProcScalingStatistics3VO getAutoProcStatisticsOuter() {
		return autoProcStatisticsOuter;
	}

	public void setAutoProcStatisticsOuter(AutoProcScalingStatistics3VO autoProcStatisticsOuter) {
		this.autoProcStatisticsOuter = autoProcStatisticsOuter;
	}

	public ScreeningOutputLattice3VO getLattice() {
		return lattice;
	}

	public void setLattice(ScreeningOutputLattice3VO lattice) {
		this.lattice = lattice;
	}

	public ScreeningOutput3VO getScreeningOutput() {
		return screeningOutput;
	}

	public void setScreeningOutput(ScreeningOutput3VO screeningOutput) {
		this.screeningOutput = screeningOutput;
	}

	public Double getEnergy() {
		return energy;
	}

	public void setEnergy(Double energy) {
		this.energy = energy;
	}

	public String getAxisStartLabel() {
		return axisStartLabel;
	}

	public void setAxisStartLabel(String axisStartLabel) {
		this.axisStartLabel = axisStartLabel;
	}

	@Override
	public Double getTotalExposureTime() {
		return totalExposureTime;
	}

	public void setTotalExposureTime(Double totalExposureTime) {
		this.totalExposureTime = totalExposureTime;
	}

	public String getKappa() {
		return kappa;
	}

	public void setKappa(String kappa) {
		this.kappa = kappa;
	}

	public String getPhi() {
		return phi;
	}

	public void setPhi(String phi) {
		this.phi = phi;
	}

	public String getUndulatorGaps() {
		return undulatorGaps;
	}

	public void setUndulatorGaps(String undulatorGaps) {
		this.undulatorGaps = undulatorGaps;
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

	public boolean isHasAutoProcAttachment() {
		return hasAutoProcAttachment;
	}

	public void setHasAutoProcAttachment(boolean hasAutoProcAttachment) {
		this.hasAutoProcAttachment = hasAutoProcAttachment;
	}

	public boolean getSkipForReport() {
		return skipForReport;
	}

	public void setSkipForReport(boolean skipForReport) {
		this.skipForReport = skipForReport;
	}

	public boolean getSelectedToRank() {
		return selectedToRank;
	}

	public void setSelectedToRank(boolean selectedToRank) {
		this.selectedToRank = selectedToRank;
	}

}
