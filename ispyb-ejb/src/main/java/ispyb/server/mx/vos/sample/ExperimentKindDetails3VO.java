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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ExperimentKindDetails3 value object mapping table ExperimentKindDetails
 * 
 */
@Entity
@Table(name = "ExperimentKindDetails")
public class ExperimentKindDetails3VO extends ISPyBValueObject implements Cloneable {

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "experimentKindId")
	private Integer experimentKindId;
	
	@Column(name = "diffractionPlanId")
	private Integer diffractionPlanId;
	
	@Column(name = "exposureIndex")
	private Integer exposureIndex;
	
	@Column(name = "dataCollectionType")
	private String dataCollectionType;
	
	@Column(name = "dataCollectionKind")
	private String dataCollectionKind;
	
	@Column(name = "wedgeValue")
	private Float wedgeValue;

	

	public ExperimentKindDetails3VO() {
		super();
	}

	public ExperimentKindDetails3VO(Integer experimentKindId,
			Integer diffractionPlanId, Integer exposureIndex,
			String dataCollectionType, String dataCollectionKind,
			Float wedgeValue) {
		super();
		this.experimentKindId = experimentKindId;
		this.diffractionPlanId = diffractionPlanId;
		this.exposureIndex = exposureIndex;
		this.dataCollectionType = dataCollectionType;
		this.dataCollectionKind = dataCollectionKind;
		this.wedgeValue = wedgeValue;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getExperimentKindId() {
		return experimentKindId;
	}

	/**
	 * @param pk The pk to set.
	 */
	public void setExperimentKindId(Integer experimentKindId) {
		this.experimentKindId = experimentKindId;
	}

	

	public Integer getDiffractionPlanId() {
		return diffractionPlanId;
	}

	public void setDiffractionPlanId(Integer diffractionPlanId) {
		this.diffractionPlanId = diffractionPlanId;
	}

	public Integer getExposureIndex() {
		return exposureIndex;
	}

	public void setExposureIndex(Integer exposureIndex) {
		this.exposureIndex = exposureIndex;
	}

	public String getDataCollectionType() {
		return dataCollectionType;
	}

	public void setDataCollectionType(String dataCollectionType) {
		this.dataCollectionType = dataCollectionType;
	}

	public String getDataCollectionKind() {
		return dataCollectionKind;
	}

	public void setDataCollectionKind(String dataCollectionKind) {
		this.dataCollectionKind = dataCollectionKind;
	}

	public Float getWedgeValue() {
		return wedgeValue;
	}

	public void setWedgeValue(Float wedgeValue) {
		this.wedgeValue = wedgeValue;
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
