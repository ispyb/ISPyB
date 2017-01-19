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
import ispyb.server.common.vos.proposals.Proposal3VO;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Protein3 value object mapping table Protein
 * 
 */
@Entity
@Table(name = "Protein")
public class Protein3VO extends ISPyBValueObject implements Cloneable {


	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "proteinId")
	protected Integer proteinId;

	@ManyToOne
	@JoinColumn(name = "proposalId")
	private Proposal3VO proposalVO;

	@Column(name = "name")
	protected String name;

	@Column(name = "acronym")
	protected String acronym;
	
	@Column(name = "safetyLevel")
	protected String safetyLevel;

	@Column(name = "molecularMass")
	protected Double molecularMass;

	@Column(name = "proteinType")
	protected String proteinType;

	@Column(name = "sequence")
	protected String sequence;

	@Column(name = "personId")
	protected Integer personId;

	@Column(name = "bltimeStamp")
	protected Date timeStamp;

	@Column(name = "isCreatedBySampleSheet")
	protected Byte isCreatedBySampleSheet;

	@OneToMany
	@JoinColumn(name = "proteinId")
	private Set<Crystal3VO> crystalVOs;

	@Column(name = "externalId")
	protected Integer externalId;

	public Protein3VO() {
		super();
	}

	public Protein3VO(Integer proteinId, Proposal3VO proposalVO, String name, String acronym, String safetyLevel, Double molecularMass,
			String proteinType, String sequence, Integer personId, Date timeStamp, Byte isCreatedBySampleSheet, Integer externalId) {
		super();
		this.proteinId = proteinId;
		this.proposalVO = proposalVO;
		this.name = name;
		this.acronym = acronym;
		this.safetyLevel  = safetyLevel;
		this.molecularMass = molecularMass;
		this.proteinType = proteinType;
		this.sequence = sequence;
		this.personId = personId;
		this.timeStamp = timeStamp;
		this.isCreatedBySampleSheet = isCreatedBySampleSheet;
		this.externalId = externalId;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getProteinId() {
		return proteinId;
	}

	/**
	 * @param pk
	 *            The pk to set.
	 */
	public void setProteinId(Integer proteinId) {
		this.proteinId = proteinId;
	}

	public Proposal3VO getProposalVO() {
		return proposalVO;
	}

	public void setProposalVO(Proposal3VO proposalVO) {
		this.proposalVO = proposalVO;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public String getSafetyLevel() {
		return safetyLevel;
	}

	public void setSafetyLevel(String safetyLevel) {
		this.safetyLevel = safetyLevel;
	}

	public Double getMolecularMass() {
		return molecularMass;
	}

	public void setMolecularMass(Double molecularMass) {
		this.molecularMass = molecularMass;
	}

	public String getProteinType() {
		return proteinType;
	}

	public void setProteinType(String proteinType) {
		this.proteinType = proteinType;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Byte getIsCreatedBySampleSheet() {
		return isCreatedBySampleSheet;
	}

	public void setIsCreatedBySampleSheet(Byte isCreatedBySampleSheet) {
		this.isCreatedBySampleSheet = isCreatedBySampleSheet;
	}

	public Set<Crystal3VO> getCrystalVOs() {
		return crystalVOs;
	}

	public void setCrystalVOs(Set<Crystal3VO> crystalVOs) {
		this.crystalVOs = crystalVOs;
	}

	public Integer getProposalVOId() {
		return proposalVO == null ? null : proposalVO.getProposalId();
	}

	public Crystal3VO[] getCrystals() {
		Set<Crystal3VO> set = this.crystalVOs;
		int len = set.size();
		Crystal3VO[] tab = set.toArray(new Crystal3VO[len]);
		return tab;
	}

	public Integer getExternalId() {
		return externalId;
	}

	public void setExternalId(Integer externalId) {
		this.externalId = externalId;
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
}
