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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Superposition")
public class Superposition3VO {

	protected Integer superpositionId;
	protected Integer subtractionId;
	
	protected String abinitioModelPdbFilePath;
	protected String aprioriPdbFilePath;
	protected String alignedPdbFilePath;
	protected Date creationDate;
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "superpositionId", unique = true, nullable = false)
	public Integer getSuperpositionId() {
		return superpositionId;
	}
	public void setSuperpositionId(Integer superpositionId) {
		this.superpositionId = superpositionId;
	}
	@Column(name = "subtractionId")
	public Integer getSubtractionId() {
		return subtractionId;
	}
	public void setSubtractionId(Integer subtractionId) {
		this.subtractionId = subtractionId;
	}
	@Column(name = "abinitioModelPdbFilePath")
	public String getAbinitioModelPdbFilePath() {
		return abinitioModelPdbFilePath;
	}
	public void setAbinitioModelPdbFilePath(String abinitioModelPdbFilePath) {
		this.abinitioModelPdbFilePath = abinitioModelPdbFilePath;
	}
	@Column(name = "aprioriPdbFilePath")
	public String getAprioriPdbFilePath() {
		return aprioriPdbFilePath;
	}
	public void setAprioriPdbFilePath(String aprioriPdbFilePath) {
		this.aprioriPdbFilePath = aprioriPdbFilePath;
	}
	@Column(name = "alignedPdbFilePath")
	public String getAlignedPdbFilePath() {
		return alignedPdbFilePath;
	}
	public void setAlignedPdbFilePath(String alignedPdbFilePath) {
		this.alignedPdbFilePath = alignedPdbFilePath;
	}
	@Column(name = "creationDate")
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	

	
}