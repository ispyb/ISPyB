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

package ispyb.server.mx.vos.autoproc;

import ispyb.server.common.vos.ISPyBValueObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * SpaceGroup3 value object mapping table SpaceGroup
 * 
 */
@Entity
@Table(name = "SpaceGroup")
public class SpaceGroup3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(SpaceGroup3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "spaceGroupId")
	protected Integer spaceGroupId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "geometryClassnameId")
	protected GeometryClassname3VO geometryClassnameVO;

	@Column(name = "spaceGroupNumber")
	protected Integer spaceGroupNumber;
	
	@Column(name = "spaceGroupShortName")
	protected String spaceGroupShortName;
	
	@Column(name = "spaceGroupName")
	protected String spaceGroupName;
	
	@Column(name = "bravaisLattice")
	protected String bravaisLattice;
	
	@Column(name = "bravaisLatticeName")
	protected String bravaisLatticeName;
	
	@Column(name = "pointGroup")
	protected String pointGroup;
	
	@Column(name = "MX_used")
	protected Integer mxUsed;
	

	
	public SpaceGroup3VO() {
		super();
	}

	
	
	public SpaceGroup3VO(Integer spaceGroupId,
			GeometryClassname3VO geometryClassnameVO, Integer spaceGroupNumber,
			String spaceGroupShortName, String spaceGroupName,
			String bravaisLattice, String bravaisLatticeName,
			String pointGroup, Integer mxUsed) {
		super();
		this.spaceGroupId = spaceGroupId;
		this.geometryClassnameVO = geometryClassnameVO;
		this.spaceGroupNumber = spaceGroupNumber;
		this.spaceGroupShortName = spaceGroupShortName;
		this.spaceGroupName = spaceGroupName;
		this.bravaisLattice = bravaisLattice;
		this.bravaisLatticeName = bravaisLatticeName;
		this.pointGroup = pointGroup;
		this.mxUsed = mxUsed;
	}



	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getSpaceGroupId() {
		return spaceGroupId;
	}

	/**
	 * @param pk The pk to set.
	 */
	public void setSpaceGroupId(Integer spaceGroupId) {
		this.spaceGroupId = spaceGroupId;
	}


	public GeometryClassname3VO getGeometryClassnameVO() {
		return geometryClassnameVO;
	}

	public void setGeometryClassnameVO(GeometryClassname3VO geometryClassnameVO) {
		this.geometryClassnameVO = geometryClassnameVO;
	}

	public Integer getMxUsed() {
		return mxUsed;
	}
	
	public void setMxUsed(Integer mxUsed) {
		this.mxUsed = mxUsed;
	}

	public Integer getSpaceGroupNumber() {
		return spaceGroupNumber;
	}

	public void setSpaceGroupNumber(Integer spaceGroupNumber) {
		this.spaceGroupNumber = spaceGroupNumber;
	}

	public String getSpaceGroupShortName() {
		return spaceGroupShortName;
	}

	public void setSpaceGroupShortName(String spaceGroupShortName) {
		this.spaceGroupShortName = spaceGroupShortName;
	}

	public String getSpaceGroupName() {
		return spaceGroupName;
	}

	public void setSpaceGroupName(String spaceGroupName) {
		this.spaceGroupName = spaceGroupName;
	}

	public String getBravaisLattice() {
		return bravaisLattice;
	}

	public void setBravaisLattice(String bravaisLattice) {
		this.bravaisLattice = bravaisLattice;
	}

	public String getBravaisLatticeName() {
		return bravaisLatticeName;
	}

	public void setBravaisLatticeName(String bravaisLatticeName) {
		this.bravaisLatticeName = bravaisLatticeName;
	}

	public String getPointGroup() {
		return pointGroup;
	}

	public void setPointGroup(String pointGroup) {
		this.pointGroup = pointGroup;
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
}

