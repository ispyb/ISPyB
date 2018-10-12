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

import ispyb.common.util.Constants;
import ispyb.common.util.StringUtils;
import ispyb.server.biosaxs.vos.assembly.Structure3VO;
import ispyb.server.common.vos.ISPyBValueObject;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Crystal3 value object mapping table Crystal
 * 
 */
@Entity
@Table(name = "Crystal")
public class Crystal3VO extends ISPyBValueObject implements Cloneable {

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "crystalId")
	protected Integer crystalId;

	@ManyToOne
	@JoinColumn(name = "diffractionPlanId")
	protected DiffractionPlan3VO diffractionPlanVO;

	@ManyToOne
	@JoinColumn(name = "proteinId")
	protected Protein3VO proteinVO;

	@Column(name = "crystalUUID")
	private String crystalUUID;

	@Column(name = "name")
	protected String name;

	@Column(name = "spaceGroup")
	protected String spaceGroup;

	@Column(name = "morphology")
	protected String morphology;

	@Column(name = "color")
	protected String color;

	@Column(name = "size_X")
	protected Double sizeX;

	@Column(name = "size_Y")
	protected Double sizeY;

	@Column(name = "size_Z")
	protected Double sizeZ;

	@Column(name = "cell_a")
	protected Double cellA;

	@Column(name = "cell_b")
	protected Double cellB;

	@Column(name = "cell_c")
	protected Double cellC;

	@Column(name = "cell_alpha")
	protected Double cellAlpha;

	@Column(name = "cell_beta")
	protected Double cellBeta;

	@Column(name = "cell_gamma")
	protected Double cellGamma;

	@Column(name = "comments")
	protected String comments;

	@Column(name = "pdbFileName")
	protected String pdbFileName;
	
	@Column(name = "pdbFilePath")
	protected String pdbFilePath;

	@OneToMany
	@JoinColumn(name = "crystalId")
	private Set<BLSample3VO> sampleVOs;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
	@JoinColumn(name = "crystalId")
	protected Set<Structure3VO> structure3VOs;

	public Crystal3VO() {
		super();
	}

	public Crystal3VO(Integer crystalId, DiffractionPlan3VO diffractionPlanVO, Protein3VO proteinVO, String crystalUUID,
			String name, String spaceGroup, String morphology, String color, Double sizeX, Double sizeY, Double sizeZ,
			Double cellA, Double cellB, Double cellC, Double cellAlpha, Double cellBeta, Double cellGamma,
			String comments, String pdbFileName, String pdbFilePath) {
		super();
		this.crystalId = crystalId;
		this.diffractionPlanVO = diffractionPlanVO;
		this.proteinVO = proteinVO;
		this.crystalUUID = crystalUUID;
		this.name = name;
		this.spaceGroup = spaceGroup;
		this.morphology = morphology;
		this.color = color;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.sizeZ = sizeZ;
		this.cellA = cellA;
		this.cellB = cellB;
		this.cellC = cellC;
		this.cellAlpha = cellAlpha;
		this.cellBeta = cellBeta;
		this.cellGamma = cellGamma;
		this.comments = comments;
		this.pdbFileName = pdbFileName;
		this.pdbFilePath = pdbFilePath;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getCrystalId() {
		return crystalId;
	}

	/**
	 * @param pk
	 *            The pk to set.
	 */
	public void setCrystalId(Integer crystalId) {
		this.crystalId = crystalId;
	}

	

	public DiffractionPlan3VO getDiffractionPlanVO() {
		return diffractionPlanVO;
	}

	public void setDiffractionPlanVO(DiffractionPlan3VO diffractionPlanVO) {
		this.diffractionPlanVO = diffractionPlanVO;
	}

	public Protein3VO getProteinVO() {
		return proteinVO;
	}

	public void setProteinVO(Protein3VO proteinVO) {
		this.proteinVO = proteinVO;
	}

	public String getCrystalUUID() {
		return crystalUUID;
	}

	public void setCrystalUUID(String crystalUUID) {
		this.crystalUUID = crystalUUID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpaceGroup() {
		return spaceGroup;
	}

	public void setSpaceGroup(String spaceGroup) {
		this.spaceGroup = spaceGroup;
	}

	public String getMorphology() {
		return morphology;
	}

	public void setMorphology(String morphology) {
		this.morphology = morphology;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Double getSizeX() {
		return sizeX;
	}

	public void setSizeX(Double sizeX) {
		this.sizeX = sizeX;
	}

	public Double getSizeY() {
		return sizeY;
	}

	public void setSizeY(Double sizeY) {
		this.sizeY = sizeY;
	}

	public Double getSizeZ() {
		return sizeZ;
	}

	public void setSizeZ(Double sizeZ) {
		this.sizeZ = sizeZ;
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

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getPdbFileName() {
		return pdbFileName;
	}

	public void setPdbFileName(String pdbFileName) {
		this.pdbFileName = pdbFileName;
	}

	public String getPdbFilePath() {
		return pdbFilePath;
	}

	public void setPdbFilePath(String pdbFilePath) {
		this.pdbFilePath = pdbFilePath;
	}

	public Set<BLSample3VO> getSampleVOs() {
		return sampleVOs;
	}

	public void setSampleVOs(Set<BLSample3VO> sampleVOs) {
		this.sampleVOs = sampleVOs;
	}

	public Integer getProteinVOId() {
		return proteinVO == null ? null : proteinVO.getProteinId();
	}

	public String getDesignation() {
		return this.proteinVO.getAcronym() + " - " + this.spaceGroup;
	}

	public Integer getDiffractionPlanVOId() {
		return diffractionPlanVO == null ? null : diffractionPlanVO.getDiffractionPlanId();
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
		int maxLengthCrystalUUID = 45;
		int maxLengthName = 255;
		int maxLengthSpaceGroup = 20;
		int maxLengthMorphology = 255;
		int maxLengthColor = 45;
		int maxLengthComments = 255;
		int maxLengthPdbFileName = 255;
		int maxLengthPdbFilePath = 1024;
		
		//protein
		if(proteinVO == null)
			throw new IllegalArgumentException(StringUtils.getMessageRequiredField("Crystal", "protein"));
		// crystalUUID
		if(!StringUtils.isStringLengthValid(this.crystalUUID, maxLengthCrystalUUID))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("Crystal", "crystalUUID", maxLengthCrystalUUID));
		// name
		if(!StringUtils.isStringLengthValid(this.name, maxLengthName))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("Crystal", "name", maxLengthName));
		// spaceGroup
		if(!StringUtils.isStringLengthValid(this.spaceGroup, maxLengthSpaceGroup))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("Crystal", "spaceGroup", maxLengthSpaceGroup));
		// morphology
		if(!StringUtils.isStringLengthValid(this.morphology, maxLengthMorphology))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("Crystal", "morphology", maxLengthMorphology));
		// color
		if(!StringUtils.isStringLengthValid(this.color, maxLengthColor))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("Crystal", "color", maxLengthColor));
		// comments
		if(!StringUtils.isStringLengthValid(this.comments, maxLengthComments))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("Crystal", "comments", maxLengthComments));
		// pdbFileName
		if(!StringUtils.isStringLengthValid(this.pdbFileName, maxLengthPdbFileName))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("Crystal", "pdbFileName", maxLengthPdbFileName));
		// pdbFilePath
		if(!StringUtils.isStringLengthValid(this.pdbFilePath, maxLengthPdbFilePath))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("Crystal", "pdbFilePath", maxLengthPdbFilePath));
		
	}
	
	/**
	 * return true if the cell values are filled, if one of them are empty or =0, returns false
	 * @return
	 */
	public boolean hasCellInfo(){
		return (this.cellA != null && !this.cellA.equals(new Double(0)) &&
				this.cellB != null && !this.cellB.equals(new Double(0)) &&
				this.cellC != null && !this.cellC.equals(new Double(0)) &&
				this.cellAlpha != null && !this.cellAlpha.equals(new Double(0)) &&
				this.cellBeta != null && !this.cellBeta.equals(new Double(0)) &&
				this.cellGamma != null && !this.cellGamma.equals(new Double(0)));
	}
	
	public boolean hasPdbFile(){
		return this.pdbFileName != null;
	}
	
	public String getAcronymSpaceGroup(){
		String acronym = this.proteinVO.getAcronym();
		// Replace database empty values by 'Undefined'
		if (getSpaceGroup() != null && !getSpaceGroup().equals("")) {
			acronym += Constants.PROTEIN_ACRONYM_SPACE_GROUP_SEPARATOR + getSpaceGroup();
		} else {
			acronym += Constants.PROTEIN_ACRONYM_SPACE_GROUP_SEPARATOR + "Undefined";
		}
		return acronym;
	}

	public Set<Structure3VO> getStructure3VOs() {
		return structure3VOs;
	}

	public void setStructure3VOs(Set<Structure3VO> structure3vOs) {
		structure3VOs = structure3vOs;
	}

}
