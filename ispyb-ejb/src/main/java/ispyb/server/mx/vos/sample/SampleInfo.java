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

import java.io.Serializable;

/**
 * Sample info needed for mxCuBE to display the list of samples (on a beamline, with the 'processing' status)
 * @author BODIN
 *
 */
public class SampleInfo implements Serializable, Cloneable{

	private static final long serialVersionUID = -3203393739645665888L;
	
	// sample info
	protected Integer sampleId;
	protected String sampleName;
	protected String code;
	protected Double holderLength;
	protected String sampleLocation;
	protected String smiles;
	
	// protein
	protected String proteinAcronym;
	
	// crystal
	protected Integer crystalId;
	protected String crystalSpaceGroup;
	protected Double cellA;
	protected Double cellB;
	protected Double cellC;
	protected Double cellAlpha;
	protected Double cellBeta;
	protected Double cellGamma;
	
	// diffractionPlan
	protected Double minimalResolution;
	protected String experimentType;
	protected DiffractionPlanWS3VO diffractionPlan;
	
	// container
	protected String containerSampleChangerLocation;
	protected String containerCode;

	
	public SampleInfo() {
		super();
	}

	public SampleInfo(Integer sampleId, String sampleName, String code,
			Double holderLength, String sampleLocation, String smiles, String proteinAcronym, Integer crystalId,
			String crystalSpaceGroup, Double cellA, Double cellB, Double cellC, Double cellAlpha, 
			Double cellBeta, Double cellGamma, Double minimalResolution, String experimentType,
			String containerSampleChangerLocation, String containerCode, DiffractionPlanWS3VO diffractionPlan) {
		super();
		this.sampleId = sampleId;
		this.sampleName = sampleName;
		this.code = code;
		this.holderLength = holderLength;
		this.sampleLocation = sampleLocation;
		this.containerCode = containerCode;
		this.smiles = smiles;
		this.proteinAcronym = proteinAcronym;
		this.crystalId=crystalId;
		this.crystalSpaceGroup = crystalSpaceGroup;
		this.cellA = cellA;
		this.cellB = cellB;
		this.cellC = cellC;
		this.cellAlpha = cellAlpha;
		this.cellBeta = cellBeta;
		this.cellGamma = cellGamma;
		this.diffractionPlan = diffractionPlan ;
		this.minimalResolution = minimalResolution;
		this.experimentType = experimentType;
		this.containerSampleChangerLocation = containerSampleChangerLocation;
	}
	
	

	public Integer getSampleId() {
		return sampleId;
	}

	public void setSampleId(Integer sampleId) {
		this.sampleId = sampleId;
	}

	public String getSampleName() {
		return sampleName;
	}

	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getHolderLength() {
		return holderLength;
	}

	public void setHolderLength(Double holderLength) {
		this.holderLength = holderLength;
	}

	public String getSampleLocation() {
		return sampleLocation;
	}

	public void setSampleLocation(String sampleLocation) {
		this.sampleLocation = sampleLocation;
	}

	public String getSmiles() {
		return smiles;
	}

	public void setSmiles(String smiles) {
		this.smiles = smiles;
	}

	public String getProteinAcronym() {
		return proteinAcronym;
	}

	public void setProteinAcronym(String proteinAcronym) {
		this.proteinAcronym = proteinAcronym;
	}

	public Integer getCrystalId() {
		return crystalId;
	}

	public void setCrystalId(Integer crystalId) {
		this.crystalId = crystalId;
	}

	public String getCrystalSpaceGroup() {
		return crystalSpaceGroup;
	}

	public void setCrystalSpaceGroup(String crystalSpaceGroup) {
		this.crystalSpaceGroup = crystalSpaceGroup;
	}

	public Double getCellA() {
		return cellA;
	}

	public void setCellA(Double cellA) {
		this.cellA = cellA;
	}

	public Double getCellB() {
		return cellB;
	}

	public void setCellB(Double cellB) {
		this.cellB = cellB;
	}

	public Double getCellC() {
		return cellC;
	}

	public void setCellC(Double cellC) {
		this.cellC = cellC;
	}

	public Double getCellAlpha() {
		return cellAlpha;
	}

	public void setCellAlpha(Double cellAlpha) {
		this.cellAlpha = cellAlpha;
	}

	public Double getCellBeta() {
		return cellBeta;
	}

	public void setCellBeta(Double cellBeta) {
		this.cellBeta = cellBeta;
	}

	public Double getCellGamma() {
		return cellGamma;
	}

	public void setCellGamma(Double cellGamma) {
		this.cellGamma = cellGamma;
	}

	public Double getMinimalResolution() {
		return minimalResolution;
	}

	public void setMinimalResolution(Double minimalResolution) {
		this.minimalResolution = minimalResolution;
	}

	public String getExperimentType() {
		return experimentType;
	}

	public void setExperimentType(String experimentType) {
		this.experimentType = experimentType;
	}

	public String getContainerSampleChangerLocation() {
		return containerSampleChangerLocation;
	}

	public void setContainerSampleChangerLocation(
			String containerSampleChangerLocation) {
		this.containerSampleChangerLocation = containerSampleChangerLocation;
	}

	public String getContainerCode() {
		return containerCode;
	}

	public void setContainerCode(String containerCode) {
		this.containerCode = containerCode;
	}

	public DiffractionPlanWS3VO getDiffractionPlan() {
		return diffractionPlan;
	}

	public void setDiffractionPlan(DiffractionPlanWS3VO diffractionPlan) {
		this.diffractionPlan = diffractionPlan;
	}
	
	
	
	

}
