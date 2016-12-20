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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ispyb.server.common.vos.ISPyBValueObject;

import org.apache.log4j.Logger;
import org.hibernate.annotations.OrderBy;

/**
 * GeometryClassname3 value object mapping table GeometryClassname
 * 
 */
@Entity
@Table(name = "GeometryClassname")
public class GeometryClassname3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(GeometryClassname3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "geometryClassnameId")
	protected Integer geometryClassnameId;

	@Column(name = "geometryClassname")
	protected String geometryClassname;

	@Column(name = "geometryOrder")
	protected Integer geometryOrder;
	
	@OneToMany
	@JoinColumn(name = "geometryClassnameId")
	@OrderBy(clause = "spaceGroupShortName")
	private Set<SpaceGroup3VO> spaceGroupVOs;

	public GeometryClassname3VO() {
		super();
	}

	public GeometryClassname3VO(Integer geometryClassnameId,
			String geometryClassname, Integer geometryOrder) {
		super();
		this.geometryClassnameId = geometryClassnameId;
		this.geometryClassname = geometryClassname;
		this.geometryOrder = geometryOrder;
	}

	public Integer getGeometryClassnameId() {
		return geometryClassnameId;
	}

	public void setGeometryClassnameId(Integer geometryClassnameId) {
		this.geometryClassnameId = geometryClassnameId;
	}

	public String getGeometryClassname() {
		return geometryClassname;
	}

	public void setGeometryClassname(String geometryClassname) {
		this.geometryClassname = geometryClassname;
	}

	public Integer getGeometryOrder() {
		return geometryOrder;
	}

	public void setGeometryOrder(Integer geometryOrder) {
		this.geometryOrder = geometryOrder;
	}

	public Set<SpaceGroup3VO> getSpaceGroupVOs() {
		return spaceGroupVOs;
	}

	public void setSpaceGroupVOs(Set<SpaceGroup3VO> spaceGroupVOs) {
		this.spaceGroupVOs = spaceGroupVOs;
	}
	
	public SpaceGroup3VO[] getSpaceGroupTab() {
		return this.spaceGroupVOs == null ? null : spaceGroupVOs.toArray(new SpaceGroup3VO[this.spaceGroupVOs.size()]);
	}

	
	public ArrayList<SpaceGroup3VO> getSpaceGroupsList() {
		return this.spaceGroupVOs == null ? null : new ArrayList<SpaceGroup3VO>(Arrays.asList(getSpaceGroupTab()));
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

	/**
	 * used to clone an entity to set the linked collectio9ns to null, for webservices
	 */
	@Override
	public GeometryClassname3VO clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (GeometryClassname3VO) super.clone();
	}
}
