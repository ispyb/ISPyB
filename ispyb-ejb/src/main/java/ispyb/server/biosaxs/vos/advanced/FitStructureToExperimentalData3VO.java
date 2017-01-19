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


package ispyb.server.biosaxs.vos.advanced;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "FitStructureToExperimentalData")
public class FitStructureToExperimentalData3VO implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Integer fitStructureToExperimentalDataId;
	protected Integer structureId;
	protected Integer subtractionId;
	protected Integer workflowId;
	protected String fitFilePath;
	protected String logFilePath;
	protected String outputFilePath;
	protected String comments;
	protected Date creationDate;
	
	protected Set<MixtureToStructure> mixtureToStructure3VOs = new HashSet<MixtureToStructure>(0);
	
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "fitStructureToExperimentalDataId", unique = true, nullable = false)
	public Integer getFitStructureToExperimentalDataId() {
		return fitStructureToExperimentalDataId;
	}
	public void setFitStructureToExperimentalDataId(Integer fitStructureToExperimentalDataId) {
		this.fitStructureToExperimentalDataId = fitStructureToExperimentalDataId;
	}
	
	
	@Column(name = "structureId")
	public Integer getStructureId() {
		return structureId;
	}
	public void setStructureId(Integer structureId) {
		this.structureId = structureId;
	}
	
	@Column(name = "subtractionId")
	public Integer getSubtractionId() {
		return subtractionId;
	}
	public void setSubtractionId(Integer subtractionId) {
		this.subtractionId = subtractionId;
	}
	
	@Column(name = "workflowId")
	public Integer getWorkflowId() {
		return workflowId;
	}
	public void setWorkflowId(Integer workflowId) {
		this.workflowId = workflowId;
	}
	
	@Column(name = "fitFilePath")
	public String getFitFilePath() {
		return fitFilePath;
	}
	public void setFitFilePath(String fitFilePath) {
		this.fitFilePath = fitFilePath;
	}
	
	@Column(name = "logFilePath")
	public String getLogFilePath() {
		return logFilePath;
	}
	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}
	
	@Column(name = "outputFilePath")
	public String getOutputFilePath() {
		return outputFilePath;
	}
	public void setOutputFilePath(String outputFilePath) {
		this.outputFilePath = outputFilePath;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creationDate", length = 0)
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	@Column(name = "comments")
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "mixtureId")
	public Set<MixtureToStructure> getMixtureToStructure3VOs() {
		return mixtureToStructure3VOs;
	}

	public void setMixtureToStructure3VOs(Set<MixtureToStructure> mixtureToStructure3VOs) {
		this.mixtureToStructure3VOs = mixtureToStructure3VOs;
	}
	
	
	
	
	
}



