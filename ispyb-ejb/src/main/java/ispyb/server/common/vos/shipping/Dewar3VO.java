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

import ispyb.common.util.Constants;
import ispyb.server.common.vos.ISPyBValueObject;
import ispyb.server.mx.vos.collections.Session3VO;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import org.hibernate.annotations.OrderBy;




/**
 * Dewar3 value object mapping table Dewar
 * 
 */
@Entity
@Table(name = "Dewar")
@SqlResultSetMapping(name = "dewarNativeQuery", entities = { @EntityResult(entityClass = Dewar3VO.class) })
public class Dewar3VO extends ISPyBValueObject implements Cloneable {


	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "dewarId")
	protected Integer dewarId;

	@ManyToOne
	@JoinColumn(name = "shippingId")
	private Shipping3VO shippingVO;

	@Column(name = "code")
	protected String code;

	@Column(name = "comments")
	protected String comments;

	@Column(name = "storageLocation")
	protected String storageLocation;

	@Column(name = "dewarStatus")
	protected String dewarStatus;

	@Column(name = "bltimeStamp")
	protected Date timeStamp;

	@Column(name = "isStorageDewar")
	protected Boolean isStorageDewar;

	@Column(name = "barCode")
	protected String barCode;

//	@Column(name = "firstExperimentId")
//	private Integer firstExperimentId;

	@Column(name = "customsValue")
	protected Integer customsValue;

	@Column(name = "transportValue")
	protected Integer transportValue;

	@Column(name = "trackingNumberToSynchrotron")
	protected String trackingNumberToSynchrotron;

	@Column(name = "trackingNumberFromSynchrotron")
	protected String trackingNumberFromSynchrotron;
	
	@Column(name = "facilityCode")
	protected String facilityCode;

	@Column(name = "type")
	protected String type;
	
	@Column(name = "isReimbursed")
	protected Boolean isReimbursed;

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE })
	@JoinColumn(name = "dewarId")
	protected Set<Container3VO> containerVOs;
	
	@OneToMany(cascade = { CascadeType.REMOVE })
	@JoinColumn(name = "dewarId")
	@OrderBy(clause = "dewarTransportHistoryId")
	private Set<DewarTransportHistory3VO> dewarTransportHistoryVOs;
	
	@ManyToOne
    @JoinColumn(name="firstExperimentId")
	protected Session3VO sessionVO;
	 

	public Integer getShippingVOId() {
		return shippingVO == null ? null : shippingVO.getShippingId();
	}

	public Dewar3VO() {
		super();
	}

	public Dewar3VO(Integer dewarId, Shipping3VO shippingVO, String code, String comments, String storageLocation,
			String dewarStatus, Date timeStamp, Boolean isStorageDewar, String barCode,
//			Integer firstExperimentId,
			Integer customsValue, Integer transportValue, String trackingNumberToSynchrotron,
			String trackingNumberFromSynchrotron, String facilityCode, String type, Boolean isReimbursed) {
		super();
		this.dewarId = dewarId;
		this.shippingVO = shippingVO;
		this.code = code;
		this.comments = comments;
		this.storageLocation = storageLocation;
		this.dewarStatus = dewarStatus;
		this.timeStamp = timeStamp;
		this.isStorageDewar = isStorageDewar;
		this.barCode = barCode;
//		this.firstExperimentId = firstExperimentId;
		this.customsValue = customsValue;
		this.transportValue = transportValue;
		this.trackingNumberToSynchrotron = trackingNumberToSynchrotron;
		this.trackingNumberFromSynchrotron = trackingNumberFromSynchrotron;
		this.facilityCode = facilityCode;
		this.type = type;
		this.isReimbursed = isReimbursed;
	}
	
	public Dewar3VO(Dewar3VO vo){
		super();
		this.dewarId = vo.getDewarId();
		this.shippingVO = vo.getShippingVO();
		this.code = vo.getCode();
		this.comments = vo.getComments();
		this.storageLocation = vo.getStorageLocation();
		this.dewarStatus = vo.getDewarStatus();
		this.timeStamp = vo.getTimeStamp();
		this.isStorageDewar = vo.getIsStorageDewar();
		this.barCode = vo.getBarCode();
		this.customsValue = vo.getCustomsValue();
		this.transportValue = vo.getTransportValue();
		this.trackingNumberToSynchrotron = vo.getTrackingNumberToSynchrotron();
		this.trackingNumberFromSynchrotron = vo.getTrackingNumberFromSynchrotron();
		this.facilityCode = vo.getFacilityCode();
		this.type = vo.getType();
		this.isReimbursed = vo.getIsReimbursed();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getDewarId() {
		return dewarId;
	}

	/**
	 * @param pk
	 *            The pk to set.
	 */
	public void setDewarId(Integer dewarId) {
		this.dewarId = dewarId;
	}

	public Shipping3VO getShippingVO() {
		return shippingVO;
	}

	public void setShippingVO(Shipping3VO shippingVO) {
		this.shippingVO = shippingVO;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getStorageLocation() {
		return storageLocation;
	}

	public void setStorageLocation(String storageLocation) {
		this.storageLocation = storageLocation;
	}

	public String getDewarStatus() {
		return dewarStatus;
	}

	public void setDewarStatus(String dewarStatus) {
		this.dewarStatus = dewarStatus;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Boolean getIsStorageDewar() {
		return isStorageDewar;
	}

	public void setIsStorageDewar(Boolean isStorageDewar) {
		this.isStorageDewar = isStorageDewar;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

//	public Integer getFirstExperimentId() {
//		return firstExperimentId;
//	}
//
//	public void setFirstExperimentId(Integer firstExperimentId) {
//		this.firstExperimentId = firstExperimentId;
//	}

	public Integer getCustomsValue() {
		return customsValue;
	}

	public void setCustomsValue(Integer customsValue) {
		this.customsValue = customsValue;
	}

	public Integer getTransportValue() {
		return transportValue;
	}

	public void setTransportValue(Integer transportValue) {
		this.transportValue = transportValue;
	}

	public String getTrackingNumberToSynchrotron() {
		return trackingNumberToSynchrotron;
	}

	public void setTrackingNumberToSynchrotron(String trackingNumberToSynchrotron) {
		this.trackingNumberToSynchrotron = trackingNumberToSynchrotron;
	}

	public String getTrackingNumberFromSynchrotron() {
		return trackingNumberFromSynchrotron;
	}

	public void setTrackingNumberFromSynchrotron(String trackingNumberFromSynchrotron) {
		this.trackingNumberFromSynchrotron = trackingNumberFromSynchrotron;
	}

	public String getFacilityCode() {
		return facilityCode;
	}

	public void setFacilityCode(String facilityCode) {
		this.facilityCode = facilityCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getIsReimbursed() {
		return isReimbursed;
	}

	public void setIsReimbursed(Boolean isReimbursed) {
		this.isReimbursed = isReimbursed;
	}

	public Set<Container3VO> getContainerVOs() {
		return containerVOs;
	}

	public void setContainerVOs(Set<Container3VO> containerVOs) {
		this.containerVOs = containerVOs;
	}

	public Container3VO[] getContainers() {
		Set<Container3VO> set = this.containerVOs;
		if (set != null) {
			int len = set.size();
			return set.toArray(new Container3VO[len]);
		} else
			return null;
	}

	public Integer getSamplesNumber() {
		Integer nb = 0;
		Container3VO[] tab = this.getContainers();
		if (tab != null)
			for (int i = 0; i < tab.length; i++) {
				nb = nb + tab[i].getSamplesNumber();
			}
		return nb;
	}

	public Set<DewarTransportHistory3VO> getDewarTransportHistoryVOs() {
		return dewarTransportHistoryVOs;
	}

	public void setDewarTransportHistoryVOs(
			Set<DewarTransportHistory3VO> dewarTransportHistoryVOs) {
		this.dewarTransportHistoryVOs = dewarTransportHistoryVOs;
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

	public Session3VO getSessionVO() {
		return sessionVO;
	}

	public void setSessionVO(Session3VO sessionVO) {
		this.sessionVO = sessionVO;
	}

	public void initBarcode(){
		if (Constants.SITE_IS_ESRF() && dewarId != null) {
			String barCode = "ESRF";
			if (getDewarId() < 1000000)
				barCode = barCode + "0";
			barCode = barCode + getDewarId().toString();
			setBarCode(barCode);
		}
	}

}
