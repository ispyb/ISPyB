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
 ******************************************************************************************************************************/

package ispyb.server.biosaxs.vos.dataAcquisition;

// Generated Sep 18, 2012 9:07:31 AM by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;
import ispyb.server.biosaxs.vos.dataAcquisition.plate.InstructionSet3VO;

import java.util.HashSet;
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
import javax.xml.bind.annotation.XmlTransient;

@SuppressWarnings("serial")
@Entity
@Table(name = "StockSolution")
public class StockSolution3VO implements java.io.Serializable {

	protected Integer stockSolutionId;
	protected int proposalId;
	protected Integer macromoleculeId;
	protected int bufferId;
	protected InstructionSet3VO instructionSet3VO;
	protected Integer boxId;
	protected String storageTemperature;
	protected String volume;
	protected String concentration;
	protected String comments;
	protected String name;
	protected Set<Specimen3VO> samples = new HashSet<Specimen3VO>(0);

	public StockSolution3VO() {
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "stockSolutionId", unique = true, nullable = false)
	public Integer getStockSolutionId() {
		return this.stockSolutionId;
	}

	public void setStockSolutionId(Integer stockSolutionId) {
		this.stockSolutionId = stockSolutionId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "instructionSetId")
	public InstructionSet3VO getInstructionSet3VO() {
		return this.instructionSet3VO;
	}

	public void setInstructionSet3VO(InstructionSet3VO instructionSet3VO) {
		this.instructionSet3VO = instructionSet3VO;
	}

	@Column(name = "bufferId", nullable = false)
	public int getBufferId() {
		return this.bufferId;
	}

	public void setBufferId(int bufferId) {
		this.bufferId = bufferId;
	}

	@Column(name = "proposalId", nullable = false)
	public int getProposalId() {
		return this.proposalId;
	}

	public void setProposalId(int proposalId) {
		this.proposalId = proposalId;
	}

	@Column(name = "boxId")
	public Integer getBoxId() {
		return this.boxId;
	}

	public void setBoxId(Integer boxId) {
		this.boxId = boxId;
	}

	@Column(name = "storageTemperature")
	public String getStorageTemperature() {
		return this.storageTemperature;
	}

	public void setStorageTemperature(String storageTemperature) {
		this.storageTemperature = storageTemperature;
	}

	@Column(name = "volume")
	public String getVolume() {
		return this.volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	@Column(name = "concentration")
	public String getConcentration() {
		return this.concentration;
	}

	public void setConcentration(String concentration) {
		this.concentration = concentration;
	}

	@Column(name = "macromoleculeId", nullable=true)
	public Integer getMacromoleculeId() {
		return macromoleculeId;
	}

	public void setMacromoleculeId(Integer macromoleculeId) {
		this.macromoleculeId = macromoleculeId;
	}

	@Column(name = "comments", nullable=true)
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "stockSolutionId")
	public Set<Specimen3VO> getSamples() {
		return samples;
	}

	public void setSamples(Set<Specimen3VO> samples) {
		this.samples = samples;
	}
	
	@Column(name = "name", nullable=true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

