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

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import ispyb.server.common.vos.ISPyBValueObject;
import ispyb.server.common.vos.proposals.Person3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.sample.BLSample3VO;

/**
 * Container3 value object mapping table Container
 * 
 */
@Entity
@Table(name = "Container")
public class Container3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(Container3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "containerId")
	protected Integer containerId;

	@ManyToOne
	@JoinColumn(name = "dewarId")
	private Dewar3VO dewarVO;

	@Column(name = "code")
	protected String code;

	@Column(name = "containerType")
	protected String containerType;

	@Column(name = "capacity")
	protected Integer capacity;

	@Column(name = "beamlineLocation")
	protected String beamlineLocation;

	@Column(name = "sampleChangerLocation")
	protected String sampleChangerLocation;

	@Column(name = "containerStatus")
	protected String containerStatus;

	@Column(name = "bltimeStamp")
	private Date timeStamp;
	
	@Column(name = "barcode")
	protected String barcode;

	@ManyToOne
	@JoinColumn(name = "sessionId")
	private Session3VO sessionVO;

	@ManyToOne
	@JoinColumn(name = "ownerId")
	private Person3VO personVO;

	@Fetch(value = FetchMode.SELECT)
	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE })
	@JoinColumn(name = "containerId")
	protected Set<BLSample3VO> sampleVOs;

	public Container3VO() {
		super();
	}

	public Container3VO(Integer containerId, Dewar3VO dewarVO, String code, String containerType, Integer capacity,
			String beamlineLocation, String sampleChangerLocation, String containerStatus, Date timeStamp, String barcode, Session3VO sessionVO, Person3VO personVO) {
		super();
		this.containerId = containerId;
		this.dewarVO = dewarVO;
		this.code = code;
		this.containerType = containerType;
		this.capacity = capacity;
		this.beamlineLocation = beamlineLocation;
		this.sampleChangerLocation = sampleChangerLocation;
		this.containerStatus = containerStatus;
		this.timeStamp = timeStamp;
		this.barcode = barcode;
		this.sessionVO = sessionVO;
		this.personVO = personVO;
	}

	public Container3VO(Container3VO vo) {
		this.containerId = vo.containerId;
		this.dewarVO = vo.dewarVO;
		this.code = vo.code;
		this.containerType = vo.containerType;
		this.capacity = vo.capacity;
		this.beamlineLocation = vo.beamlineLocation;
		this.sampleChangerLocation = vo.sampleChangerLocation;
		this.containerStatus = vo.containerStatus;
		this.barcode = vo.barcode;
		this.sessionVO = vo.sessionVO;
		this.personVO = vo.personVO;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getContainerId() {
		return containerId;
	}

	/**
	 * @param pk
	 *            The pk to set.
	 */
	public void setContainerId(Integer containerId) {
		this.containerId = containerId;
	}

	public Dewar3VO getDewarVO() {
		return dewarVO;
	}

	public void setDewarVO(Dewar3VO dewarVO) {
		this.dewarVO = dewarVO;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getContainerType() {
		return containerType;
	}

	public void setContainerType(String containerType) {
		this.containerType = containerType;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public String getBeamlineLocation() {
		return beamlineLocation;
	}

	public void setBeamlineLocation(String beamlineLocation) {
		this.beamlineLocation = beamlineLocation;
	}

	public String getSampleChangerLocation() {
		return sampleChangerLocation;
	}

	public void setSampleChangerLocation(String sampleChangerLocation) {
		this.sampleChangerLocation = sampleChangerLocation;
	}

	public String getContainerStatus() {
		return containerStatus;
	}

	public void setContainerStatus(String containerStatus) {
		this.containerStatus = containerStatus;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Set<BLSample3VO> getSampleVOs() {
		return sampleVOs;
	}

	public void setSampleVOs(Set<BLSample3VO> sampleVOs) {
		this.sampleVOs = sampleVOs;
	}

	public Integer getSamplesNumber() {
		Integer nb = 0;
		if (this.sampleVOs != null)
			nb = this.sampleVOs.size();
		return nb;
	}

	public Integer getDewarVOId() {
		return dewarVO == null ? null : dewarVO.getDewarId();
	}

	
	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Session3VO getSessionVO() {
		return sessionVO;
	}

	public Integer getSessionVOId() {
		return sessionVO == null ? null : sessionVO.getSessionId();
	}
	
	public void setSessionVO(Session3VO sessionVO) {
		this.sessionVO = sessionVO;
	}

	public Person3VO getPersonVO() {
		return personVO;
	}
	
	public Integer getPersonVOId() {
		return personVO == null ? null : personVO.getPersonId();
	}

	public void setPersonVO(Person3VO personVO) {
		this.personVO = personVO;
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
