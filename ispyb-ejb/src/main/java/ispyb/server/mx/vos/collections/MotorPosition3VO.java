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

package ispyb.server.mx.vos.collections;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import ispyb.server.common.vos.ISPyBValueObject;

import org.apache.log4j.Logger;

/**
 * MotorPosition3 value object mapping table MotorPosition
 * 
 */
@Entity
@Table(name="MotorPosition")
public class MotorPosition3VO extends ISPyBValueObject implements Cloneable {
	
	private final static Logger LOG = Logger.getLogger(MotorPosition3VO.class);
	
	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;
	
	@Id
	@GeneratedValue
	@Column(name = "motorPositionId")
	protected Integer motorPositionId;

	@Column(name = "phiX")
	protected Double phiX;
	
	@Column(name = "phiY")
	protected Double phiY;
	
	@Column(name = "phiZ")
	protected Double phiZ;
	
	@Column(name = "sampX")
	protected Double sampX;
	
	@Column(name = "sampY")
	protected Double sampY;
	
	@Column(name = "omega")
	protected Double omega;
	
	@Column(name = "kappa")
	protected Double kappa;
	
	@Column(name = "phi")
	protected Double phi;
	
	@Column(name = "chi")
	protected Double chi;
	
	@Column(name = "gridIndexY")
	protected Integer gridIndexY;
	
	@Column(name = "gridIndexZ")
	protected Integer gridIndexZ;
	
	public MotorPosition3VO() {
		super();
	}

	public MotorPosition3VO(Integer motorPositionId, Double phiX, Double phiY,
			Double phiZ, Double sampX, Double sampY, Double omega,
			Double kappa, Double phi, Double chi, Integer gridIndexY, Integer gridIndexZ) {
		super();
		this.motorPositionId = motorPositionId;
		this.phiX = phiX;
		this.phiY = phiY;
		this.phiZ = phiZ;
		this.sampX = sampX;
		this.sampY = sampY;
		this.omega = omega;
		this.kappa = kappa;
		this.phi = phi;
		this.chi = chi;
		this.gridIndexY = gridIndexY;
		this.gridIndexZ = gridIndexZ;
	}
	
	public MotorPosition3VO(MotorPosition3VO vo) {
		super();
		this.motorPositionId = vo.getMotorPositionId();
		this.phiX = vo.getPhiX();
		this.phiY = vo.getPhiY();
		this.phiZ = vo.getPhiZ();
		this.sampX = vo.getSampX();
		this.sampY = vo.getSampY();
		this.omega = vo.getOmega();
		this.kappa = vo.getKappa();
		this.phi = vo.getPhi();
		this.chi = vo.getChi();
		this.gridIndexY = vo.getGridIndexY();
		this.gridIndexZ = vo.getGridIndexZ();
	}

	public Integer getMotorPositionId() {
		return motorPositionId;
	}

	public void setMotorPositionId(Integer motorPositionId) {
		this.motorPositionId = motorPositionId;
	}

	public Double getPhiX() {
		return phiX;
	}

	public void setPhiX(Double phiX) {
		this.phiX = phiX;
	}

	public Double getPhiY() {
		return phiY;
	}

	public void setPhiY(Double phiY) {
		this.phiY = phiY;
	}

	public Double getPhiZ() {
		return phiZ;
	}

	public void setPhiZ(Double phiZ) {
		this.phiZ = phiZ;
	}

	public Double getSampX() {
		return sampX;
	}

	public void setSampX(Double sampX) {
		this.sampX = sampX;
	}

	public Double getSampY() {
		return sampY;
	}

	public void setSampY(Double sampY) {
		this.sampY = sampY;
	}

	public Double getOmega() {
		return omega;
	}

	public void setOmega(Double omega) {
		this.omega = omega;
	}

	public Double getKappa() {
		return kappa;
	}

	public void setKappa(Double kappa) {
		this.kappa = kappa;
	}

	public Double getPhi() {
		return phi;
	}

	public Double getChi() {
		return chi;
	}

	public void setChi(Double chi) {
		this.chi = chi;
	}

	public void setPhi(Double phi) {
		this.phi = phi;
	}

	public Integer getGridIndexY() {
		return gridIndexY;
	}

	public void setGridIndexY(Integer gridIndexY) {
		this.gridIndexY = gridIndexY;
	}

	public Integer getGridIndexZ() {
		return gridIndexZ;
	}

	public void setGridIndexZ(Integer gridIndexZ) {
		this.gridIndexZ = gridIndexZ;
	}

	/**
	 * Checks the values of this value object for correctness and
	 * completeness. Should be done before persisting the data in the DB.
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @throws Exception if the data of the value object is not correct
	 */
	public void checkValues(boolean create) throws Exception {
		
	}
	
	/**
	 * used to clone an entity to set the linked collectio9ns to null, for webservices
	 */
	@Override
	public MotorPosition3VO clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (MotorPosition3VO) super.clone();
	}
	
	public String toWSString(){
		String s = "motorPositionId="+this.motorPositionId +", "+
		"phiX="+this.phiX+", "+
		"phiY="+this.phiY+", "+
		"phiZ="+this.phiZ+", "+
		"sampX="+this.sampX+", "+
		"sampY="+this.sampY+", "+
		"omega="+this.omega+", "+
		"kappa="+this.kappa+", "+
		"phi="+this.phi+", "+
		"chi="+this.chi+", "+
		"gridIndexY="+this.gridIndexY+", "+
		"gridIndexZ="+this.gridIndexZ;
		
		return s;
	}
	
}
