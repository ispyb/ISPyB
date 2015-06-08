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
@Table(name = "RigidBodyModeling")
public class RigidBodyModeling3VO {

	protected Integer rigidBodyModelingId;
	protected Integer subtractionId;
	
	protected String fitFilePath;
	protected String rigidBodyModelFilePath;
	protected String logFilePath;
	protected String curveConfigFilePath;
	protected String subUnitConfigFilePath;
	protected String crossCorrConfigFilePath;
	protected String contactDescriptionFilePath;
	protected String symmetry;
	protected Date creationDate;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "rigidBodyModelingId", unique = true, nullable = false)
	public Integer getRigidBodyModelingId() {
		return rigidBodyModelingId;
	}
	public void setRigidBodyModelingId(Integer rigidBodyModelingId) {
		this.rigidBodyModelingId = rigidBodyModelingId;
	}
	
	@Column(name = "subtractionId")
	public Integer getSubtractionId() {
		return subtractionId;
	}
	public void setSubtractionId(Integer subtractionId) {
		this.subtractionId = subtractionId;
	}
	@Column(name = "fitFilePath")
	public String getFitFilePath() {
		return fitFilePath;
	}
	public void setFitFilePath(String fitFilePath) {
		this.fitFilePath = fitFilePath;
	}
	@Column(name = "rigidBodyModelFilePath")
	public String getRigidBodyModelFilePath() {
		return rigidBodyModelFilePath;
	}
	public void setRigidBodyModelFilePath(String rigidBodyModelFilePath) {
		this.rigidBodyModelFilePath = rigidBodyModelFilePath;
	}
	@Column(name = "logFilePath")
	public String getLogFilePath() {
		return logFilePath;
	}
	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}
	@Column(name = "curveConfigFilePath")
	public String getCurveConfigFilePath() {
		return curveConfigFilePath;
	}
	public void setCurveConfigFilePath(String curveConfigFilePath) {
		this.curveConfigFilePath = curveConfigFilePath;
	}
	@Column(name = "subUnitConfigFilePath")
	public String getSubUnitConfigFilePath() {
		return subUnitConfigFilePath;
	}
	public void setSubUnitConfigFilePath(String subUnitConfigFilePath) {
		this.subUnitConfigFilePath = subUnitConfigFilePath;
	}
	@Column(name = "crossCorrConfigFilePath")
	public String getCrossCorrConfigFilePath() {
		return crossCorrConfigFilePath;
	}
	public void setCrossCorrConfigFilePath(String crossCorrConfigFilePath) {
		this.crossCorrConfigFilePath = crossCorrConfigFilePath;
	}
	@Column(name = "contactDescriptionFilePath")
	public String getContactDescriptionFilePath() {
		return contactDescriptionFilePath;
	}
	public void setContactDescriptionFilePath(String contactDescriptionFilePath) {
		this.contactDescriptionFilePath = contactDescriptionFilePath;
	}
	@Column(name = "symmetry")
	public String getSymmetry() {
		return symmetry;
	}
	public void setSymmetry(String symmetry) {
		this.symmetry = symmetry;
	}
	@Column(name = "creationDate")
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	
	
}