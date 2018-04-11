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

package ispyb.server.mx.vos.sample;

import ispyb.server.common.vos.ISPyBValueObject;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * DiffractionPlan3 value object mapping table DiffractionPlan
 * 
 */
@Entity
@Table(name = "DiffractionPlan")
public class DiffractionPlan3VO extends ISPyBValueObject implements Cloneable {

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "diffractionPlanId")
	protected Integer diffractionPlanId;

	@Column(name = "experimentKind")
	protected String experimentKind;

	@Column(name = "observedResolution")
	protected Double observedResolution;

	@Column(name = "minimalResolution")
	protected Double minimalResolution;

	@Column(name = "exposureTime")
	protected Double exposureTime;

	@Column(name = "oscillationRange")
	protected Double oscillationRange;

	@Column(name = "maximalResolution")
	protected Double maximalResolution;

	@Column(name = "screeningResolution")
	protected Double screeningResolution;

	@Column(name = "radiationSensitivity")
	protected Double radiationSensitivity;

	@Column(name = "anomalousScatterer")
	protected String anomalousScatterer;

	@Column(name = "preferredBeamSizeX")
	protected Double preferredBeamSizeX;

	@Column(name = "preferredBeamSizeY")
	protected Double preferredBeamSizeY;
	
	@Column(name = "preferredBeamDiameter")
	protected Double preferredBeamDiameter;

	@Column(name = "comments")
	protected String comments;

	@Column(name = "aimedCompleteness")
	protected Double aimedCompleteness;

	@Column(name = "aimedIOverSigmaAtHighestRes")
	protected Double aimedIOverSigmaAtHighestRes;

	@Column(name = "aimedMultiplicity")
	protected Double aimedMultiplicity;

	@Column(name = "aimedResolution")
	protected Double aimedResolution;

	@Column(name = "anomalousData")
	protected Boolean anomalousData;

	@Column(name = "complexity")
	protected String complexity;

	@Column(name = "estimateRadiationDamage")
	protected Boolean estimateRadiationDamage;

	@Column(name = "forcedSpaceGroup")
	protected String forcedSpaceGroup;

	@Column(name = "requiredCompleteness")
	protected Double requiredCompleteness;

	@Column(name = "requiredMultiplicity")
	protected Double requiredMultiplicity;

	@Column(name = "requiredResolution")
	protected Double requiredResolution;

	@Column(name = "strategyOption")
	protected String strategyOption;

	@Column(name = "kappaStrategyOption")
	protected String kappaStrategyOption;

	@Column(name = "numberOfPositions")
	protected Integer numberOfPositions;
	
	@Column(name = "minDimAccrossSpindleAxis")
	protected Double minDimAccrossSpindleAxis;
	
	@Column(name = "maxDimAccrossSpindleAxis")
	protected Double maxDimAccrossSpindleAxis;
	
	@Column(name = "radiationSensitivityBeta")
	protected Double radiationSensitivityBeta;
	
	@Column(name = "radiationSensitivityGamma")
	protected Double radiationSensitivityGamma;
	
	@Column(name = "minOscWidth")
	protected Double minOscWidth;
	
	@Column(name = "axisRange")
	protected Double axisRange;
	

	@Fetch(value = FetchMode.SELECT)
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "diffractionPlanId")
	private Set<ExperimentKindDetails3VO> experimentKindVOs;


	public DiffractionPlan3VO() {
		super();
	}

	public DiffractionPlan3VO(Integer diffractionPlanId, String experimentKind,
			Double observedResolution, Double minimalResolution, Double exposureTime, Double oscillationRange,
			Double maximalResolution, Double screeningResolution, Double radiationSensitivity,
			String anomalousScatterer, Double preferredBeamSizeX, Double preferredBeamSizeY, Double preferredBeamDiameter,String comments,
			Double aimedCompleteness, Double aimedIOverSigmaAtHighestRes, Double aimedMultiplicity,
			Double aimedResolution, Boolean anomalousData, String complexity, Boolean estimateRadiationDamage,
			String forcedSpaceGroup, Double requiredCompleteness, Double requiredMultiplicity,
			Double requiredResolution, String strategyOption, String kappaStrategyOption, Integer numberOfPositions, 
			Double minDimAccrossSpindleAxis, Double maxDimAccrossSpindleAxis, Double radiationSensitivityBeta, 
			Double radiationSensitivityGamma, Double minOscWidth, Double axisRange) {
		super();
		this.diffractionPlanId = diffractionPlanId;
		this.experimentKind = experimentKind;
		this.observedResolution = observedResolution;
		this.minimalResolution = minimalResolution;
		this.exposureTime = exposureTime;
		this.oscillationRange = oscillationRange;
		this.maximalResolution = maximalResolution;
		this.screeningResolution = screeningResolution;
		this.radiationSensitivity = radiationSensitivity;
		this.anomalousScatterer = anomalousScatterer;
		this.preferredBeamSizeX = preferredBeamSizeX;
		this.preferredBeamSizeY = preferredBeamSizeY;
		this.preferredBeamDiameter = preferredBeamDiameter;
		this.comments = comments;
		this.aimedCompleteness = aimedCompleteness;
		this.aimedIOverSigmaAtHighestRes = aimedIOverSigmaAtHighestRes;
		this.aimedMultiplicity = aimedMultiplicity;
		this.aimedResolution = aimedResolution;
		this.anomalousData = anomalousData;
		this.complexity = complexity;
		this.estimateRadiationDamage = estimateRadiationDamage;
		this.forcedSpaceGroup = forcedSpaceGroup;
		this.requiredCompleteness = requiredCompleteness;
		this.requiredMultiplicity = requiredMultiplicity;
		this.requiredResolution = requiredResolution;
		this.strategyOption = strategyOption;
		this.kappaStrategyOption = kappaStrategyOption;
		this.numberOfPositions = numberOfPositions;
		this.minDimAccrossSpindleAxis = minDimAccrossSpindleAxis;
		this.maxDimAccrossSpindleAxis = maxDimAccrossSpindleAxis ;
		this.radiationSensitivityBeta = radiationSensitivityBeta;
		this.radiationSensitivityGamma = radiationSensitivityGamma;
		this.minOscWidth = minOscWidth;
		this.axisRange = axisRange;
		
	}

	public DiffractionPlan3VO(DiffractionPlan3VO vo) {
		this.diffractionPlanId = vo.getDiffractionPlanId();
		this.experimentKind = vo.getExperimentKind();
		this.observedResolution = vo.getObservedResolution();
		this.minimalResolution = vo.getMinimalResolution();
		this.exposureTime = vo.getExposureTime();
		this.oscillationRange = vo.getOscillationRange();
		this.maximalResolution = vo.getMaximalResolution();
		this.screeningResolution = vo.getScreeningResolution();
		this.radiationSensitivity = vo.getRadiationSensitivity();
		this.anomalousScatterer = vo.getAnomalousScatterer();
		this.preferredBeamSizeX = vo.getPreferredBeamSizeX();
		this.preferredBeamSizeY = vo.getPreferredBeamSizeY();
		this.preferredBeamDiameter = vo.getPreferredBeamDiameter();
		this.comments = vo.getComments();
		this.aimedCompleteness = vo.getAimedCompleteness();
		this.aimedIOverSigmaAtHighestRes = vo.getAimedIOverSigmaAtHighestRes();
		this.aimedMultiplicity = vo.getAimedMultiplicity();
		this.aimedResolution = vo.getAimedResolution();
		this.anomalousData = vo.getAnomalousData();
		this.complexity = vo.getComplexity();
		this.estimateRadiationDamage = vo.getEstimateRadiationDamage();
		this.forcedSpaceGroup = vo.getForcedSpaceGroup();
		this.requiredCompleteness = vo.getRequiredCompleteness();
		this.requiredMultiplicity = vo.getRequiredMultiplicity();
		this.requiredResolution = vo.getRequiredResolution();
		this.strategyOption = vo.getStrategyOption();
		this.kappaStrategyOption = vo.getKappaStrategyOption();
		this.numberOfPositions = vo.getNumberOfPositions();
		this.minDimAccrossSpindleAxis = vo.getMinDimAccrossSpindleAxis();
		this.maxDimAccrossSpindleAxis = vo.getMaxDimAccrossSpindleAxis();
		this.radiationSensitivityBeta = vo.getRadiationSensitivityBeta();
		this.radiationSensitivityGamma = vo.getRadiationSensitivityGamma();
		this.minOscWidth = vo.getMinOscWidth();
		this.axisRange = vo.getAxisRange();
	}

	public void fillVOFromWS(DiffractionPlanWS3VO vo) {
		this.diffractionPlanId = vo.getDiffractionPlanId();
		this.experimentKind = vo.getExperimentKind();
		this.observedResolution = vo.getObservedResolution();
		this.minimalResolution = vo.getMinimalResolution();
		this.exposureTime = vo.getExposureTime();
		this.oscillationRange = vo.getOscillationRange();
		this.maximalResolution = vo.getMaximalResolution();
		this.screeningResolution = vo.getScreeningResolution();
		this.radiationSensitivity = vo.getRadiationSensitivity();
		this.anomalousScatterer = vo.getAnomalousScatterer();
		this.preferredBeamSizeX = vo.getPreferredBeamSizeX();
		this.preferredBeamSizeY = vo.getPreferredBeamSizeY();
		this.preferredBeamDiameter = vo.getPreferredBeamDiameter();
		this.comments = vo.getComments();
		this.aimedCompleteness = vo.getAimedCompleteness();
		this.aimedIOverSigmaAtHighestRes = vo.getAimedIOverSigmaAtHighestRes();
		this.aimedMultiplicity = vo.getAimedMultiplicity();
		this.aimedResolution = vo.getAimedResolution();
		this.anomalousData = vo.getAnomalousData();
		this.complexity = vo.getComplexity();
		this.estimateRadiationDamage = vo.getEstimateRadiationDamage();
		this.forcedSpaceGroup = vo.getForcedSpaceGroup();
		this.requiredCompleteness = vo.getRequiredCompleteness();
		this.requiredMultiplicity = vo.getRequiredMultiplicity();
		this.requiredResolution = vo.getRequiredResolution();
		this.strategyOption = vo.getStrategyOption();
		this.kappaStrategyOption = vo.getKappaStrategyOption();
		this.numberOfPositions = vo.getNumberOfPositions();
		this.minDimAccrossSpindleAxis = vo.getMinDimAccrossSpindleAxis();
		this.maxDimAccrossSpindleAxis = vo.getMaxDimAccrossSpindleAxis();
		this.radiationSensitivityBeta = vo.getRadiationSensitivityBeta();
		this.radiationSensitivityGamma = vo.getRadiationSensitivityGamma();
		this.minOscWidth = vo.getMinOscWidth();
		this.axisRange = vo.getAxisRange();

	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getDiffractionPlanId() {
		return diffractionPlanId;
	}

	/**
	 * @param pk
	 *            The pk to set.
	 */
	public void setDiffractionPlanId(Integer diffractionPlanId) {
		this.diffractionPlanId = diffractionPlanId;
	}

	public String getExperimentKind() {
		return experimentKind;
	}

	public void setExperimentKind(String experimentKind) {
		this.experimentKind = experimentKind;
	}

	public Double getObservedResolution() {
		return observedResolution;
	}

	public void setObservedResolution(Double observedResolution) {
		this.observedResolution = observedResolution;
	}

	public Double getMinimalResolution() {
		return minimalResolution;
	}

	public void setMinimalResolution(Double minimalResolution) {
		this.minimalResolution = minimalResolution;
	}

	public Double getExposureTime() {
		return exposureTime;
	}

	public void setExposureTime(Double exposureTime) {
		this.exposureTime = exposureTime;
	}

	public Double getOscillationRange() {
		return oscillationRange;
	}

	public void setOscillationRange(Double oscillationRange) {
		this.oscillationRange = oscillationRange;
	}

	public Double getMaximalResolution() {
		return maximalResolution;
	}

	public void setMaximalResolution(Double maximalResolution) {
		this.maximalResolution = maximalResolution;
	}

	public Double getScreeningResolution() {
		return screeningResolution;
	}

	public void setScreeningResolution(Double screeningResolution) {
		this.screeningResolution = screeningResolution;
	}

	public Double getRadiationSensitivity() {
		return radiationSensitivity;
	}

	public void setRadiationSensitivity(Double radiationSensitivity) {
		this.radiationSensitivity = radiationSensitivity;
	}

	public String getAnomalousScatterer() {
		return anomalousScatterer;
	}

	public void setAnomalousScatterer(String anomalousScatterer) {
		this.anomalousScatterer = anomalousScatterer;
	}

	public Double getPreferredBeamSizeX() {
		return preferredBeamSizeX;
	}

	public void setPreferredBeamSizeX(Double preferredBeamSizeX) {
		this.preferredBeamSizeX = preferredBeamSizeX;
	}

	public Double getPreferredBeamSizeY() {
		return preferredBeamSizeY;
	}

	public void setPreferredBeamSizeY(Double preferredBeamSizeY) {
		this.preferredBeamSizeY = preferredBeamSizeY;
	}

	public Double getPreferredBeamDiameter() {
		return preferredBeamDiameter;
	}

	public void setPreferredBeamDiameter(Double preferredBeamDiameter) {
		this.preferredBeamDiameter = preferredBeamDiameter;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Double getAimedCompleteness() {
		return aimedCompleteness;
	}

	public void setAimedCompleteness(Double aimedCompleteness) {
		this.aimedCompleteness = aimedCompleteness;
	}

	public Double getAimedIOverSigmaAtHighestRes() {
		return aimedIOverSigmaAtHighestRes;
	}

	public void setAimedIOverSigmaAtHighestRes(Double aimedIOverSigmaAtHighestRes) {
		this.aimedIOverSigmaAtHighestRes = aimedIOverSigmaAtHighestRes;
	}

	public Double getAimedMultiplicity() {
		return aimedMultiplicity;
	}

	public void setAimedMultiplicity(Double aimedMultiplicity) {
		this.aimedMultiplicity = aimedMultiplicity;
	}

	public Double getAimedResolution() {
		return aimedResolution;
	}

	public void setAimedResolution(Double aimedResolution) {
		this.aimedResolution = aimedResolution;
	}

	public Boolean getAnomalousData() {
		return anomalousData;
	}

	public void setAnomalousData(Boolean anomalousData) {
		this.anomalousData = anomalousData;
	}

	public String getComplexity() {
		return complexity;
	}

	public void setComplexity(String complexity) {
		this.complexity = complexity;
	}

	public Boolean getEstimateRadiationDamage() {
		return estimateRadiationDamage;
	}

	public void setEstimateRadiationDamage(Boolean estimateRadiationDamage) {
		this.estimateRadiationDamage = estimateRadiationDamage;
	}

	public String getForcedSpaceGroup() {
		return forcedSpaceGroup;
	}

	public void setForcedSpaceGroup(String forcedSpaceGroup) {
		this.forcedSpaceGroup = forcedSpaceGroup;
	}

	public Double getRequiredCompleteness() {
		return requiredCompleteness;
	}

	public void setRequiredCompleteness(Double requiredCompleteness) {
		this.requiredCompleteness = requiredCompleteness;
	}

	public Double getRequiredMultiplicity() {
		return requiredMultiplicity;
	}

	public void setRequiredMultiplicity(Double requiredMultiplicity) {
		this.requiredMultiplicity = requiredMultiplicity;
	}

	public Double getRequiredResolution() {
		return requiredResolution;
	}

	public void setRequiredResolution(Double requiredResolution) {
		this.requiredResolution = requiredResolution;
	}

	public String getStrategyOption() {
		return strategyOption;
	}

	public void setStrategyOption(String strategyOption) {
		this.strategyOption = strategyOption;
	}

	public String getKappaStrategyOption() {
		return kappaStrategyOption;
	}

	public void setKappaStrategyOption(String kappaStrategyOption) {
		this.kappaStrategyOption = kappaStrategyOption;
	}

	public Integer getNumberOfPositions() {
		return numberOfPositions;
	}

	public void setNumberOfPositions(Integer numberOfPositions) {
		this.numberOfPositions = numberOfPositions;
	}

	public Set<ExperimentKindDetails3VO> getExperimentKindVOs() {
		return experimentKindVOs;
	}

	public void setExperimentKindVOs(Set<ExperimentKindDetails3VO> experimentKindVOs) {
		this.experimentKindVOs = experimentKindVOs;
	}

	public Double getMinDimAccrossSpindleAxis() {
		return minDimAccrossSpindleAxis;
	}

	public void setMinDimAccrossSpindleAxis(Double minDimAccrossSpindleAxis) {
		this.minDimAccrossSpindleAxis = minDimAccrossSpindleAxis;
	}

	public Double getMaxDimAccrossSpindleAxis() {
		return maxDimAccrossSpindleAxis;
	}

	public void setMaxDimAccrossSpindleAxis(Double maxDimAccrossSpindleAxis) {
		this.maxDimAccrossSpindleAxis = maxDimAccrossSpindleAxis;
	}

	public Double getRadiationSensitivityBeta() {
		return radiationSensitivityBeta;
	}

	public void setRadiationSensitivityBeta(Double radiationSensitivityBeta) {
		this.radiationSensitivityBeta = radiationSensitivityBeta;
	}

	public Double getRadiationSensitivityGamma() {
		return radiationSensitivityGamma;
	}

	public void setRadiationSensitivityGamma(Double radiationSensitivityGamma) {
		this.radiationSensitivityGamma = radiationSensitivityGamma;
	}	

	public Double getMinOscWidth() {
		return minOscWidth;
	}

	public void setMinOscWidth(Double minOscWidth) {
		this.minOscWidth = minOscWidth;
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
		// TODO
	}
	
	public String toWSString(){
		String s = "diffractionPlanId="+this.diffractionPlanId +", "+
		"experimentKind="+this.experimentKind+", "+
		"observedResolution="+this.observedResolution+", "+
		"minimalResolution="+this.minimalResolution+", "+
		"exposureTime="+this.exposureTime+", "+
		"oscillationRange="+this.oscillationRange+", "+
		"maximalResolution="+this.maximalResolution+", "+
		"screeningResolution="+this.screeningResolution+", "+
		"radiationSensitivity="+this.radiationSensitivity+", "+
		"anomalousScatterer="+this.anomalousScatterer+", "+
		"preferredBeamSizeX="+this.preferredBeamSizeX+", "+
		"comments="+this.comments+", "+
		"aimedCompleteness="+this.aimedCompleteness+", "+
		"aimedIOverSigmaAtHighestRes="+this.aimedIOverSigmaAtHighestRes+", "+
		"aimedMultiplicity="+this.aimedMultiplicity+", "+
		"aimedResolution="+this.aimedResolution+", "+
		"anomalousData="+this.anomalousData+", "+
		"complexity="+this.complexity+", "+
		"estimateRadiationDamage="+this.estimateRadiationDamage+", "+
		"forcedSpaceGroup="+this.forcedSpaceGroup+", "+
		"requiredCompleteness="+this.requiredCompleteness+", "+
		"requiredMultiplicity="+this.requiredMultiplicity+", "+
		"requiredResolution="+this.requiredResolution+", "+
		"strategyOption="+this.strategyOption+", "+
		"kappaStrategyOption="+this.kappaStrategyOption+", "+
		"numberOfPositions="+this.numberOfPositions+", "+
		"minDimAccrossSpindleAxis="+this.minDimAccrossSpindleAxis+", "+
		"maxDimAccrossSpindleAxis="+this.maxDimAccrossSpindleAxis+", "+
		"radiationSensitivityBeta="+this.radiationSensitivityBeta+", "+
		"radiationSensitivityGamma="+this.radiationSensitivityGamma;
		
		
		return s;
	}

	public Double getAxisRange() {
		return axisRange;
	}

	public void setAxisRange(Double axisRange) {
		this.axisRange = axisRange;
	}
}
