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
package ispyb.client.common.shipping;

import ispyb.server.mx.vos.sample.BLSample3VO;

/**
 * data used for the creation of a simple puck.
 * @author BODIN
 *
 */
public class SamplePuck {
	protected String sampleId;
	protected String position;
	protected String sampleName;
	protected String proteinAcronym;
	protected String spaceGroup;
	protected String pinBarcode;
	protected String preObservedResolution;
	protected String neededResolution;
	protected String preferredBeamDiameter;
	protected String experimentType;
	protected String numberOfPositions;
	protected String radiationSensitivity;
	protected String aimedCompleteness;
	protected String aimedMultiplicity;
	protected String unitCellA;
	protected String unitCellB;
	protected String unitCellC;
	protected String unitCellAlpha;
	protected String unitCellBeta;
	protected String unitCellGamma;
	protected String smiles;
	protected String comments;
	protected String minOscWidth;
	protected String axisRange;
	
	public SamplePuck() {
		super();
	}

	public SamplePuck(String sampleId, String position, String sampleName,
			String proteinAcronym, String spaceGroup,
			String preObservedResolution, String neededResolution,
			String preferredBeamDiameter, String experimentType, 
			String numberOfPositions,
			String radiationSensitivity,
			String aimedCompleteness,
			String aimedMultiplicity,
			String unitCellA,
			String unitCellB, String unitCellC, String unitCellAlpha,
			String unitCellBeta, String unitCellGamma, String smiles, String comments, String pinBarcode, String minOscWidth, String axisRange) {
		super();
		this.sampleId = sampleId;
		this.position = position;
		this.sampleName = sampleName;
		this.proteinAcronym = proteinAcronym;
		this.spaceGroup = spaceGroup;
		this.preObservedResolution = preObservedResolution;
		this.neededResolution = neededResolution;
		this.preferredBeamDiameter = preferredBeamDiameter;
		this.numberOfPositions = numberOfPositions;
		this.radiationSensitivity = radiationSensitivity;
		this.aimedCompleteness = aimedCompleteness;
		this.aimedMultiplicity = aimedMultiplicity;
		this.experimentType = experimentType;
		this.unitCellA = unitCellA;
		this.unitCellB = unitCellB;
		this.unitCellC = unitCellC;
		this.unitCellAlpha = unitCellAlpha;
		this.unitCellBeta = unitCellBeta;
		this.unitCellGamma = unitCellGamma;
		this.smiles = smiles;
		this.comments = comments;
		this.pinBarcode = pinBarcode;
		this.minOscWidth= minOscWidth;
		this.axisRange = axisRange;
	}
	
	public SamplePuck(BLSample3VO blSample3VO) {
		super();
		this.sampleId = blSample3VO.getBlSampleId().toString();
		this.position = blSample3VO.getLocation();
		this.sampleName = blSample3VO.getName();
		this.proteinAcronym = blSample3VO.getCrystalVO().getAcronymSpaceGroup();
		this.spaceGroup = blSample3VO.getCrystalVO().getSpaceGroup();
		this.preObservedResolution = "";
		if (blSample3VO.getDiffractionPlanVO() != null && blSample3VO.getDiffractionPlanVO().getObservedResolution() != null){
			this.preObservedResolution = blSample3VO.getDiffractionPlanVO().getObservedResolution().toString();
		}
		this.neededResolution = "";
		if (blSample3VO.getDiffractionPlanVO() != null && blSample3VO.getDiffractionPlanVO().getRequiredResolution() != null){
			this.neededResolution = blSample3VO.getDiffractionPlanVO().getRequiredResolution().toString();
		}
		this.preferredBeamDiameter = "";
		if (blSample3VO.getDiffractionPlanVO() != null && blSample3VO.getDiffractionPlanVO().getPreferredBeamDiameter() != null){
			this.preferredBeamDiameter = blSample3VO.getDiffractionPlanVO().getPreferredBeamDiameter().toString();
		}
		this.experimentType = "";
		if(blSample3VO.getDiffractionPlanVO() != null){
			this.experimentType = blSample3VO.getDiffractionPlanVO().getExperimentKind();
		}
		this.numberOfPositions = "";
		if(blSample3VO.getDiffractionPlanVO() != null && blSample3VO.getDiffractionPlanVO().getNumberOfPositions() != null){
			this.numberOfPositions = blSample3VO.getDiffractionPlanVO().getNumberOfPositions().toString();
		}
		this.radiationSensitivity = "";
		if(blSample3VO.getDiffractionPlanVO() != null && blSample3VO.getDiffractionPlanVO().getRadiationSensitivity() != null){
			this.radiationSensitivity = blSample3VO.getDiffractionPlanVO().getRadiationSensitivity().toString();
		}
		this.aimedMultiplicity = "";
		if(blSample3VO.getDiffractionPlanVO() != null && blSample3VO.getDiffractionPlanVO().getAimedMultiplicity() != null){
			this.aimedMultiplicity = blSample3VO.getDiffractionPlanVO().getAimedMultiplicity().toString();
		}
		this.aimedCompleteness = "";
		if(blSample3VO.getDiffractionPlanVO() != null && blSample3VO.getDiffractionPlanVO().getAimedCompleteness() != null){
			this.aimedCompleteness = blSample3VO.getDiffractionPlanVO().getAimedCompleteness().toString();
		}
		this.minOscWidth = "";
		if(blSample3VO.getDiffractionPlanVO() != null && blSample3VO.getDiffractionPlanVO().getMinOscWidth() != null){
			this.minOscWidth = blSample3VO.getDiffractionPlanVO().getMinOscWidth().toString();
		}
		this.axisRange = "";
		if(blSample3VO.getDiffractionPlanVO() != null && blSample3VO.getDiffractionPlanVO().getAxisRange() != null){
			this.axisRange = blSample3VO.getDiffractionPlanVO().getAxisRange().toString();
		}
		
		this.unitCellA = "";
		if ( blSample3VO.getCrystalVO().getCellA() != null){
			this.unitCellA =blSample3VO.getCrystalVO().getCellA().toString();
		}
		this.unitCellB = "";
		if ( blSample3VO.getCrystalVO().getCellB() != null){
			this.unitCellB =blSample3VO.getCrystalVO().getCellB().toString();
		}
		this.unitCellC = "";
		if ( blSample3VO.getCrystalVO().getCellC() != null){
			this.unitCellC =blSample3VO.getCrystalVO().getCellC().toString();
		}
		this.unitCellAlpha = "";
		if ( blSample3VO.getCrystalVO().getCellAlpha() != null){
			this.unitCellAlpha =blSample3VO.getCrystalVO().getCellAlpha().toString();
		}
		this.unitCellBeta = "";
		if ( blSample3VO.getCrystalVO().getCellBeta() != null){
			this.unitCellBeta =blSample3VO.getCrystalVO().getCellBeta().toString();
		}
		this.unitCellGamma = "";
		if ( blSample3VO.getCrystalVO().getCellGamma() != null){
			this.unitCellGamma =blSample3VO.getCrystalVO().getCellGamma().toString();
		}
		this.comments = blSample3VO.getComments();
		this.smiles = blSample3VO.getSmiles() ;
		this.pinBarcode  =blSample3VO.getCode();
	}

	public String getSampleId() {
		return sampleId;
	}

	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getSampleName() {
		return sampleName;
	}

	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}

	public String getProteinAcronym() {
		return proteinAcronym;
	}

	public void setProteinAcronym(String proteinAcronym) {
		this.proteinAcronym = proteinAcronym;
	}

	public String getSpaceGroup() {
		return spaceGroup;
	}

	public void setSpaceGroup(String spaceGroup) {
		this.spaceGroup = spaceGroup;
	}

	public String getPreObservedResolution() {
		return preObservedResolution;
	}

	public void setPreObservedResolution(String preObservedResolution) {
		this.preObservedResolution = preObservedResolution;
	}

	public String getNeededResolution() {
		return neededResolution;
	}

	public void setNeededResolution(String neededResolution) {
		this.neededResolution = neededResolution;
	}

	public String getPreferredBeamDiameter() {
		return preferredBeamDiameter;
	}

	public void setPreferredBeamDiameter(String preferredBeamDiameter) {
		this.preferredBeamDiameter = preferredBeamDiameter;
	}

	public String getExperimentType() {
		return experimentType;
	}
	
	public void setExperimentType(String experimentType) {
		this.experimentType = experimentType;
	}

	public String getNumberOfPositions() {
		return numberOfPositions;
	}

	public void setNumberOfPositions(String numberOfPositions) {
		this.numberOfPositions = numberOfPositions;
	}

	public String getRadiationSensitivity() {
		return radiationSensitivity;
	}
	public void setRadiationSensitivity(String radiationSensitivity) {
		this.radiationSensitivity = radiationSensitivity;
	}

	public String getAimedCompleteness() {
		return aimedCompleteness;
	}

	public void setAimedCompleteness(String aimedCompleteness) {
		this.aimedCompleteness = aimedCompleteness;
	}

	public String getAimedMultiplicity() {
		return aimedMultiplicity;
	}

	public void setAimedMultiplicity(String aimedMultiplicity) {
		this.aimedMultiplicity = aimedMultiplicity;
	}

	public String getAxisRange() {
		return axisRange;
	}

	public void setAxisRange(String axisRange) {
		this.axisRange = axisRange;
	}

	public String getUnitCellA() {
		return unitCellA;
	}

	public void setUnitCellA(String unitCellA) {
		this.unitCellA = unitCellA;
	}

	public String getUnitCellB() {
		return unitCellB;
	}

	public void setUnitCellB(String unitCellB) {
		this.unitCellB = unitCellB;
	}
	
	public String getUnitCellC() {
		return unitCellC;
	}
	
	public void setUnitCellC(String unitCellC) {
		this.unitCellC = unitCellC;
	}
	
	public String getUnitCellAlpha() {
		return unitCellAlpha;
	}
	
	public void setUnitCellAlpha(String unitCellAlpha) {
		this.unitCellAlpha = unitCellAlpha;
	}
	
	public String getUnitCellBeta() {
		return unitCellBeta;
	}
	
	public void setUnitCellBeta(String unitCellBeta) {
		this.unitCellBeta = unitCellBeta;
	}
	
	public String getUnitCellGamma() {
		return unitCellGamma;
	}

	public void setUnitCellGamma(String unitCellGamma) {
		this.unitCellGamma = unitCellGamma;
	}
	
	public String getComments() {
		return comments;
	}
	
	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getSmiles() {
		return smiles;
	}
	
	public void setSmiles(String smiles) {
		this.smiles = smiles;
	}

	public String getPinBarcode() {
		return pinBarcode;
	}

	public void setPinBarcode(String pinBarcode) {
		this.pinBarcode = pinBarcode;
	}

	public String getMinOscWidth() {
		return minOscWidth;
	}

	public void setMinOscWidth(String minOscWidth) {
		this.minOscWidth = minOscWidth;
	}
	
}
