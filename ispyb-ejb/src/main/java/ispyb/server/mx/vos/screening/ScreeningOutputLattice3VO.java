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

package ispyb.server.mx.vos.screening;

import ispyb.server.common.vos.ISPyBValueObject;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * ScreeningOutputLattice3 value object mapping table ScreeningOutputLattice
 * 
 */
@Entity
@Table(name = "ScreeningOutputLattice")
public class ScreeningOutputLattice3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(ScreeningOutputLattice3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "screeningOutputLatticeId")
	protected Integer screeningOutputLatticeId;
	
	@ManyToOne
	@JoinColumn(name = "screeningOutputId")
	private ScreeningOutput3VO screeningOutputVO;
	
	@Column(name = "spaceGroup")
	protected String spaceGroup;
	
	@Column(name = "pointGroup")
	protected String pointGroup;
	
	@Column(name = "bravaisLattice")
	protected String bravaisLattice;
	
	@Column(name = "rawOrientationMatrix_a_x")
	protected Double rawOrientationMatrix_a_x;
	
	@Column(name = "rawOrientationMatrix_a_y")
	protected Double rawOrientationMatrix_a_y;
	
	@Column(name = "rawOrientationMatrix_a_z")
	protected Double rawOrientationMatrix_a_z;
	
	@Column(name = "rawOrientationMatrix_b_x")
	protected Double rawOrientationMatrix_b_x;
	
	@Column(name = "rawOrientationMatrix_b_y")
	protected Double rawOrientationMatrix_b_y;
	
	@Column(name = "rawOrientationMatrix_b_z")
	protected Double rawOrientationMatrix_b_z;
	
	@Column(name = "rawOrientationMatrix_c_x")
	protected Double rawOrientationMatrix_c_x;
	
	@Column(name = "rawOrientationMatrix_c_y")
	protected Double rawOrientationMatrix_c_y;
	
	@Column(name = "rawOrientationMatrix_c_z")
	protected Double rawOrientationMatrix_c_z;

	@Column(name = "unitCell_a")
	protected Double unitCell_a;
	
	@Column(name = "unitCell_b")
	protected Double unitCell_b;
	
	@Column(name = "unitCell_c")
	protected Double unitCell_c;
	
	@Column(name = "unitCell_alpha")
	protected Double unitCell_alpha;
	
	@Column(name = "unitCell_beta")
	protected Double unitCell_beta;
	
	@Column(name = "unitCell_gamma")
	protected Double unitCell_gamma;
	
	@Column(name = "bltimeStamp")
	protected Date timeStamp;
	
	@Column(name = "labelitIndexing")
	protected Boolean labelitIndexing;
	
	public ScreeningOutputLattice3VO(){
		super();
	}
	
	
	public ScreeningOutputLattice3VO(Integer screeningOutputLatticeId,
			ScreeningOutput3VO screeningOutputVO, String spaceGroup,
			String pointGroup, String bravaisLattice,
			Double rawOrientationMatrix_a_x, Double rawOrientationMatrix_a_y,
			Double rawOrientationMatrix_a_z, Double rawOrientationMatrix_b_x,
			Double rawOrientationMatrix_b_y, Double rawOrientationMatrix_b_z,
			Double rawOrientationMatrix_c_x, Double rawOrientationMatrix_c_y,
			Double rawOrientationMatrix_c_z, Double unitCell_a,
			Double unitCell_b, Double unitCell_c, Double unitCell_alpha,
			Double unitCell_beta, Double unitCell_gamma, Date timeStamp, Boolean labelitIndexing) {
		super();
		this.screeningOutputLatticeId = screeningOutputLatticeId;
		this.screeningOutputVO = screeningOutputVO;
		this.spaceGroup = spaceGroup;
		this.pointGroup = pointGroup;
		this.bravaisLattice = bravaisLattice;
		this.rawOrientationMatrix_a_x = rawOrientationMatrix_a_x;
		this.rawOrientationMatrix_a_y = rawOrientationMatrix_a_y;
		this.rawOrientationMatrix_a_z = rawOrientationMatrix_a_z;
		this.rawOrientationMatrix_b_x = rawOrientationMatrix_b_x;
		this.rawOrientationMatrix_b_y = rawOrientationMatrix_b_y;
		this.rawOrientationMatrix_b_z = rawOrientationMatrix_b_z;
		this.rawOrientationMatrix_c_x = rawOrientationMatrix_c_x;
		this.rawOrientationMatrix_c_y = rawOrientationMatrix_c_y;
		this.rawOrientationMatrix_c_z = rawOrientationMatrix_c_z;
		this.unitCell_a = unitCell_a;
		this.unitCell_b = unitCell_b;
		this.unitCell_c = unitCell_c;
		this.unitCell_alpha = unitCell_alpha;
		this.unitCell_beta = unitCell_beta;
		this.unitCell_gamma = unitCell_gamma;
		this.timeStamp = timeStamp;
		this.labelitIndexing = labelitIndexing;
	}

	public ScreeningOutputLattice3VO(ScreeningOutputLattice3VO vo) {
		super();
		this.screeningOutputLatticeId = vo.getScreeningOutputLatticeId();
		this.screeningOutputVO = vo.getScreeningOutputVO();
		this.spaceGroup = vo.getSpaceGroup();
		this.pointGroup = vo.getPointGroup();
		this.bravaisLattice = vo.getBravaisLattice();
		this.rawOrientationMatrix_a_x = vo.getRawOrientationMatrix_a_x();
		this.rawOrientationMatrix_a_y = vo.getRawOrientationMatrix_a_y();
		this.rawOrientationMatrix_a_z = vo.getRawOrientationMatrix_a_z();
		this.rawOrientationMatrix_b_x = vo.getRawOrientationMatrix_b_x();
		this.rawOrientationMatrix_b_y = vo.getRawOrientationMatrix_b_y();
		this.rawOrientationMatrix_b_z = vo.getRawOrientationMatrix_b_z();
		this.rawOrientationMatrix_c_x = vo.getRawOrientationMatrix_c_x();
		this.rawOrientationMatrix_c_y = vo.getRawOrientationMatrix_c_y();
		this.rawOrientationMatrix_c_z = vo.getRawOrientationMatrix_c_z();
		this.unitCell_a = vo.getUnitCell_a();
		this.unitCell_b = vo.getUnitCell_b();
		this.unitCell_c = vo.getUnitCell_c();
		this.unitCell_alpha = vo.getUnitCell_alpha();
		this.unitCell_beta = vo.getUnitCell_beta();
		this.unitCell_gamma = vo.getUnitCell_gamma();
		this.timeStamp = vo.getTimeStamp();
		this.labelitIndexing = vo.getLabelitIndexing();
	}
	
	public void fillVOFromWS(ScreeningOutputLatticeWS3VO vo) {
		this.screeningOutputLatticeId = vo.getScreeningOutputLatticeId();
		this.screeningOutputVO = null;
		this.spaceGroup = vo.getSpaceGroup();
		this.pointGroup = vo.getPointGroup();
		this.bravaisLattice = vo.getBravaisLattice();
		this.rawOrientationMatrix_a_x = vo.getRawOrientationMatrix_a_x();
		this.rawOrientationMatrix_a_y = vo.getRawOrientationMatrix_a_y();
		this.rawOrientationMatrix_a_z = vo.getRawOrientationMatrix_a_z();
		this.rawOrientationMatrix_b_x = vo.getRawOrientationMatrix_b_x();
		this.rawOrientationMatrix_b_y = vo.getRawOrientationMatrix_b_y();
		this.rawOrientationMatrix_b_z = vo.getRawOrientationMatrix_b_z();
		this.rawOrientationMatrix_c_x = vo.getRawOrientationMatrix_c_x();
		this.rawOrientationMatrix_c_y = vo.getRawOrientationMatrix_c_y();
		this.rawOrientationMatrix_c_z = vo.getRawOrientationMatrix_c_z();
		this.unitCell_a = vo.getUnitCell_a();
		this.unitCell_b = vo.getUnitCell_b();
		this.unitCell_c = vo.getUnitCell_c();
		this.unitCell_alpha = vo.getUnitCell_alpha();
		this.unitCell_beta = vo.getUnitCell_beta();
		this.unitCell_gamma = vo.getUnitCell_gamma();
		this.timeStamp = vo.getTimeStamp();
		this.labelitIndexing = vo.getLabelitIndexing();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getScreeningOutputLatticeId() {
		return screeningOutputLatticeId;
	}

	/**
	 * @param pk The pk to set.
	 */
	public void setScreeningOutputLatticeId(Integer screeningOutputLatticeId) {
		this.screeningOutputLatticeId = screeningOutputLatticeId;
	}

	

	public ScreeningOutput3VO getScreeningOutputVO() {
		return screeningOutputVO;
	}



	public void setScreeningOutputVO(ScreeningOutput3VO screeningOutputVO) {
		this.screeningOutputVO = screeningOutputVO;
	}

	public Integer getScreeningOutputVOId() {
		return screeningOutputVO == null ? null : screeningOutputVO.getScreeningOutputId();
	}
	public String getSpaceGroup() {
		return spaceGroup;
	}

	public void setSpaceGroup(String spaceGroup) {
		this.spaceGroup = spaceGroup;
	}

	public String getPointGroup() {
		return pointGroup;
	}

	public void setPointGroup(String pointGroup) {
		this.pointGroup = pointGroup;
	}

	public String getBravaisLattice() {
		return bravaisLattice;
	}

	public void setBravaisLattice(String bravaisLattice) {
		this.bravaisLattice = bravaisLattice;
	}

	public Double getRawOrientationMatrix_a_x() {
		return rawOrientationMatrix_a_x;
	}

	public void setRawOrientationMatrix_a_x(Double rawOrientationMatrix_a_x) {
		this.rawOrientationMatrix_a_x = rawOrientationMatrix_a_x;
	}

	public Double getRawOrientationMatrix_a_y() {
		return rawOrientationMatrix_a_y;
	}

	public void setRawOrientationMatrix_a_y(Double rawOrientationMatrix_a_y) {
		this.rawOrientationMatrix_a_y = rawOrientationMatrix_a_y;
	}

	public Double getRawOrientationMatrix_a_z() {
		return rawOrientationMatrix_a_z;
	}

	public void setRawOrientationMatrix_a_z(Double rawOrientationMatrix_a_z) {
		this.rawOrientationMatrix_a_z = rawOrientationMatrix_a_z;
	}

	public Double getRawOrientationMatrix_b_x() {
		return rawOrientationMatrix_b_x;
	}

	public void setRawOrientationMatrix_b_x(Double rawOrientationMatrix_b_x) {
		this.rawOrientationMatrix_b_x = rawOrientationMatrix_b_x;
	}

	public Double getRawOrientationMatrix_b_y() {
		return rawOrientationMatrix_b_y;
	}

	public void setRawOrientationMatrix_b_y(Double rawOrientationMatrix_b_y) {
		this.rawOrientationMatrix_b_y = rawOrientationMatrix_b_y;
	}

	public Double getRawOrientationMatrix_b_z() {
		return rawOrientationMatrix_b_z;
	}

	public void setRawOrientationMatrix_b_z(Double rawOrientationMatrix_b_z) {
		this.rawOrientationMatrix_b_z = rawOrientationMatrix_b_z;
	}

	public Double getRawOrientationMatrix_c_x() {
		return rawOrientationMatrix_c_x;
	}

	public void setRawOrientationMatrix_c_x(Double rawOrientationMatrix_c_x) {
		this.rawOrientationMatrix_c_x = rawOrientationMatrix_c_x;
	}

	public Double getRawOrientationMatrix_c_y() {
		return rawOrientationMatrix_c_y;
	}

	public void setRawOrientationMatrix_c_y(Double rawOrientationMatrix_c_y) {
		this.rawOrientationMatrix_c_y = rawOrientationMatrix_c_y;
	}

	public Double getRawOrientationMatrix_c_z() {
		return rawOrientationMatrix_c_z;
	}

	public void setRawOrientationMatrix_c_z(Double rawOrientationMatrix_c_z) {
		this.rawOrientationMatrix_c_z = rawOrientationMatrix_c_z;
	}

	public Double getUnitCell_a() {
		return unitCell_a;
	}

	public void setUnitCell_a(Double unitCell_a) {
		this.unitCell_a = unitCell_a;
	}

	public Double getUnitCell_b() {
		return unitCell_b;
	}

	public void setUnitCell_b(Double unitCell_b) {
		this.unitCell_b = unitCell_b;
	}

	public Double getUnitCell_c() {
		return unitCell_c;
	}

	public void setUnitCell_c(Double unitCell_c) {
		this.unitCell_c = unitCell_c;
	}

	public Double getUnitCell_alpha() {
		return unitCell_alpha;
	}

	public void setUnitCell_alpha(Double unitCell_alpha) {
		this.unitCell_alpha = unitCell_alpha;
	}

	public Double getUnitCell_beta() {
		return unitCell_beta;
	}

	public void setUnitCell_beta(Double unitCell_beta) {
		this.unitCell_beta = unitCell_beta;
	}

	public Double getUnitCell_gamma() {
		return unitCell_gamma;
	}

	public void setUnitCell_gamma(Double unitCell_gamma) {
		this.unitCell_gamma = unitCell_gamma;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Boolean getLabelitIndexing() {
		return labelitIndexing;
	}


	public void setLabelitIndexing(Boolean labelitIndexing) {
		this.labelitIndexing = labelitIndexing;
	}


	/**
	 * Checks the values of this value object for correctness and
	 * completeness. Should be done before persisting the data in the DB.
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @throws Exception if the data of the value object is not correct
	 */
	public void checkValues(boolean create) throws Exception {
		//TODO
	}
	
	public String toWSString(){
		String s = "screeningOutputLatticeId="+this.screeningOutputLatticeId +", "+
		"spaceGroup="+this.spaceGroup+", "+
		"pointGroup="+this.pointGroup+", "+
		"bravaisLattice="+this.bravaisLattice+", "+
		"rawOrientationMatrix_a_x="+this.rawOrientationMatrix_a_x+", "+
		"rawOrientationMatrix_a_y="+this.rawOrientationMatrix_a_y+", "+
		"rawOrientationMatrix_a_z="+this.rawOrientationMatrix_a_z+", "+
		"rawOrientationMatrix_b_x="+this.rawOrientationMatrix_b_x+", "+
		"rawOrientationMatrix_b_y="+this.rawOrientationMatrix_b_y+", "+
		"rawOrientationMatrix_b_z="+this.rawOrientationMatrix_b_z+", "+
		"rawOrientationMatrix_c_x="+this.rawOrientationMatrix_c_x+", "+
		"rawOrientationMatrix_c_y="+this.rawOrientationMatrix_c_y+", "+
		"rawOrientationMatrix_c_z="+this.rawOrientationMatrix_c_z+", "+
		"unitCell_a="+this.unitCell_a+", "+
		"unitCell_b="+this.unitCell_b+", "+
		"unitCell_c="+this.unitCell_c+", "+
		"unitCell_alpha="+this.unitCell_alpha+", "+
		"unitCell_beta="+this.unitCell_beta+", "+
		"unitCell_gamma="+this.unitCell_gamma+", "+
		"timeStamp="+this.timeStamp+", "+
		"labelitIndexing="+this.labelitIndexing;
		
		return s;
	}
	
}
