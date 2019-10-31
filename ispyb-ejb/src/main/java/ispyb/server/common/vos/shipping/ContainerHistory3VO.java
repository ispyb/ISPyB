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

package ispyb.server.common.vos.shipping;

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
 * DewarTransportHistory3 value object mapping table DewarTransportHistory
 * 
 */
@Entity
@Table(name = "ContainerHistory")
public class ContainerHistory3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(ContainerHistory3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "containerHistoryId")
	private Integer containerHistoryId;

	@ManyToOne
	@JoinColumn(name = "containerId")
	private Container3VO containerVO;

	@Column(name = "location")
	private String containerLocation;
	
	@Column(name = "status")
	private String containerStatus;

	public ContainerHistory3VO() {
		super();
	}

	public ContainerHistory3VO(Container3VO containerVO, 
			String containerLocation, String containerStatus) {
		super();
		
		this.containerVO = containerVO;
		this.containerStatus = containerStatus;
		this.containerLocation = containerLocation;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	public Integer getContainerHistoryId() {
		return containerHistoryId;
	}

	public void setContainerHistoryId(Integer containerHistoryId) {
		this.containerHistoryId = containerHistoryId;
	}

	public Container3VO getContainerVO() {
		return containerVO;
	}

	public void setContainerVO(Container3VO containerVO) {
		this.containerVO = containerVO;
	}

	public String getContainerLocation() {
		return containerLocation;
	}

	public void setContainerLocation(String containerLocation) {
		this.containerLocation = containerLocation;
	}

	public String getContainerStatus() {
		return containerStatus;
	}

	public void setContainerStatus(String containerStatus) {
		this.containerStatus = containerStatus;
	}

	/**
	 * Checks the values of this value object for correctness and completeness. Should be done before persisting the
	 * data in the DB.
	 * 
	 * @param create
	 *            should be true if the value object is just being created in the DB, this avoids some checks like
	 *            testing the primary key
	 * @throws Exception
	 *             if the data of the value object is not correct
	 */
	@Override
	public void checkValues(boolean create) throws Exception {
		// TODO
	}
}
