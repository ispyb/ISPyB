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
@Table(name = "MixtureToStructure")
public class MixtureToStructure implements java.io.Serializable{

	protected Integer fitToStructureId;
	protected Integer structureId;
	protected Integer mixtureId;
	protected String volumeFraction;
	protected Date creationDate;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "fitToStructureId", unique = true, nullable = false)
	public Integer getFitToStructureId() {
		return fitToStructureId;
	}


	public void setFitToStructureId(Integer fitToStructureId) {
		this.fitToStructureId = fitToStructureId;
	}
	
	@Column(name = "structureId")
	public Integer getStructureId() {
		return structureId;
	}
	
	public void setStructureId(Integer structureId) {
		this.structureId = structureId;
	}

	@Column(name = "mixtureId")
	public Integer getMixtureId() {
		return mixtureId;
	}


	public void setMixtureId(Integer mixtureId) {
		this.mixtureId = mixtureId;
	}

	@Column(name = "volumeFraction")
	public String getVolumeFraction() {
		return volumeFraction;
	}


	public void setVolumeFraction(String volumeFraction) {
		this.volumeFraction = volumeFraction;
	}

	@Column(name = "creationDate")
	public Date getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}


	
	
}



