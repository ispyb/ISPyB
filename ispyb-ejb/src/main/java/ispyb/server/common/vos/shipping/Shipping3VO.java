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
import ispyb.server.common.vos.proposals.LabContact3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.mx.vos.collections.Session3VO;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Shipping3 value object mapping table Shipping
 * 
 */
@Entity
@Table(name = "Shipping")
public class Shipping3VO extends ISPyBValueObject implements Cloneable {

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "shippingId")
	protected Integer shippingId;

	@ManyToOne
	@JoinColumn(name = "proposalId")
	private Proposal3VO proposalVO;

	@Column(name = "shippingName")
	protected String shippingName;

	@Column(name = "deliveryAgent_agentName")
	protected String deliveryAgentAgentName;

	@Column(name = "deliveryAgent_shippingDate")
	protected Date deliveryAgentShippingDate;

	@Column(name = "deliveryAgent_deliveryDate")
	protected Date deliveryAgentDeliveryDate;

	@Column(name = "deliveryAgent_agentCode")
	protected String deliveryAgentAgentCode;

	@Column(name = "deliveryAgent_flightCode")
	protected String deliveryAgentFlightCode;

	@Column(name = "shippingStatus")
	protected String shippingStatus;

	@Column(name = "bltimeStamp")
	protected Date timeStamp;

	@Column(name = "laboratoryId")
	protected Integer laboratoryId;

	@Column(name = "isStorageShipping")
	protected Boolean isStorageShipping;

	@Column(name = "creationDate")
	protected Date creationDate;

	@Column(name = "comments")
	protected String comments;

	@ManyToOne
	@JoinColumn(name = "sendingLabContactId")
	protected LabContact3VO sendingLabContactVO;

	@ManyToOne
	@JoinColumn(name = "returnLabContactId")
	protected LabContact3VO returnLabContactVO;

	@Column(name = "returnCourier")
	protected String returnCourier;

	@Column(name = "dateOfShippingToUser")
	protected Date dateOfShippingToUser;

	@Column(name = "shippingType")
	protected String shippingType;
	
	@OneToMany(cascade = { CascadeType.REMOVE })
	@JoinColumn(name = "shippingId")
	protected Set<Dewar3VO> dewarVOs;

	@Fetch(value = FetchMode.SELECT)
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ShippingHasSession", joinColumns = { @JoinColumn(name = "shippingId", referencedColumnName = "shippingId") }, inverseJoinColumns = { @JoinColumn(name = "sessionId", referencedColumnName = "sessionId") })
	protected Set<Session3VO> sessions;

	public Shipping3VO() {
		super();
	}

	public Shipping3VO(Integer shippingId, Proposal3VO proposalVO, String shippingName, String deliveryAgentAgentName,
			Date deliveryAgentShippingDate, Date deliveryAgentDeliveryDate, String deliveryAgentAgentCode,
			String deliveryAgentFlightCode, String shippingStatus, Date timeStamp, Integer laboratoryId,
			Boolean isStorageShipping, Date creationDate, String comments, LabContact3VO sendingLabContactVO,
			LabContact3VO returnLabContactVO, String returnCourier, Date dateOfShippingToUser, String shippingType,
			Set<Dewar3VO> dewarVOs, Set<Session3VO> sessions) {
		super();
		this.shippingId = shippingId;
		this.proposalVO = proposalVO;
		this.shippingName = shippingName;
		this.deliveryAgentAgentName = deliveryAgentAgentName;
		this.deliveryAgentShippingDate = deliveryAgentShippingDate;
		this.deliveryAgentDeliveryDate = deliveryAgentDeliveryDate;
		this.deliveryAgentAgentCode = deliveryAgentAgentCode;
		this.deliveryAgentFlightCode = deliveryAgentFlightCode;
		this.shippingStatus = shippingStatus;
		this.timeStamp = timeStamp;
		this.laboratoryId = laboratoryId;
		this.isStorageShipping = isStorageShipping;
		this.creationDate = creationDate;
		this.comments = comments;
		this.sendingLabContactVO = sendingLabContactVO;
		this.returnLabContactVO = returnLabContactVO;
		this.returnCourier = returnCourier;
		this.dateOfShippingToUser = dateOfShippingToUser;
		this.shippingType = shippingType;
		this.dewarVOs = dewarVOs;
		this.sessions = sessions;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getShippingId() {
		return shippingId;
	}

	/**
	 * @param pk
	 *            The pk to set.
	 */
	public void setShippingId(Integer shippingId) {
		this.shippingId = shippingId;
	}

	public Proposal3VO getProposalVO() {
		return proposalVO;
	}

	public void setProposalVO(Proposal3VO proposalVO) {
		this.proposalVO = proposalVO;
	}

	public String getShippingName() {
		return shippingName;
	}

	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}

	public String getDeliveryAgentAgentName() {
		return deliveryAgentAgentName;
	}

	public void setDeliveryAgentAgentName(String deliveryAgentAgentName) {
		this.deliveryAgentAgentName = deliveryAgentAgentName;
	}

	public Date getDeliveryAgentShippingDate() {
		return deliveryAgentShippingDate;
	}

	public void setDeliveryAgentShippingDate(Date deliveryAgentShippingDate) {
		this.deliveryAgentShippingDate = deliveryAgentShippingDate;
	}

	public Date getDeliveryAgentDeliveryDate() {
		return deliveryAgentDeliveryDate;
	}

	public void setDeliveryAgentDeliveryDate(Date deliveryAgentDeliveryDate) {
		this.deliveryAgentDeliveryDate = deliveryAgentDeliveryDate;
	}

	public String getDeliveryAgentAgentCode() {
		return deliveryAgentAgentCode;
	}

	public void setDeliveryAgentAgentCode(String deliveryAgentAgentCode) {
		this.deliveryAgentAgentCode = deliveryAgentAgentCode;
	}

	public String getDeliveryAgentFlightCode() {
		return deliveryAgentFlightCode;
	}

	public void setDeliveryAgentFlightCode(String deliveryAgentFlightCode) {
		this.deliveryAgentFlightCode = deliveryAgentFlightCode;
	}

	public String getShippingStatus() {
		return shippingStatus;
	}

	public void setShippingStatus(String shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Integer getLaboratoryId() {
		return laboratoryId;
	}

	public void setLaboratoryId(Integer laboratoryId) {
		this.laboratoryId = laboratoryId;
	}

	public Boolean getIsStorageShipping() {
		return isStorageShipping;
	}

	public void setIsStorageShipping(Boolean isStorageShipping) {
		this.isStorageShipping = isStorageShipping;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public String getCreationDateStr() {
		if (creationDate == null)
			return null;
		return creationDate.toString();
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public LabContact3VO getSendingLabContactVO() {
		return sendingLabContactVO;
	}

	public void setSendingLabContactVO(LabContact3VO sendingLabContactVO) {
		this.sendingLabContactVO = sendingLabContactVO;
	}

	public LabContact3VO getReturnLabContactVO() {
		return returnLabContactVO;
	}

	public void setReturnLabContactVO(LabContact3VO returnLabContactVO) {
		this.returnLabContactVO = returnLabContactVO;
	}

	public String getReturnCourier() {
		return returnCourier;
	}

	public void setReturnCourier(String returnCourier) {
		this.returnCourier = returnCourier;
	}

	public Date getDateOfShippingToUser() {
		return dateOfShippingToUser;
	}

	public void setDateOfShippingToUser(Date dateOfShippingToUser) {
		this.dateOfShippingToUser = dateOfShippingToUser;
	}

	public String getShippingType() {
		return shippingType;
	}

	public void setShippingType(String shippingType) {
		this.shippingType = shippingType;
	}

	public Set<Dewar3VO> getDewarVOs() {
		return dewarVOs;
	}

	public void setDewarVOs(Set<Dewar3VO> dewarVOs) {
		this.dewarVOs = dewarVOs;
	}

	public Integer getProposalVOId() {
		return proposalVO == null ? null : proposalVO.getProposalId();
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

	public Set<Session3VO> getSessions() {
		return sessions;
	}

	public void setSessions(Set<Session3VO> sessions) {
		this.sessions = sessions;
	}

	public void addSession(Session3VO ses) {
		if (sessions == null) {
			sessions = new HashSet<Session3VO>();
		}
		if (ses != null && !sessions.contains(ses)) {
			sessions.add(ses);
		}
	}

	public void clearSessions() {
		this.sessions.clear();

	}

	public Dewar3VO[] getDewars() {
		Set<Dewar3VO> set = this.dewarVOs;
		int len = set.size();
		return set.toArray(new Dewar3VO[len]);
	}

	public Integer getSendingLabContactId() {
		if (this.sendingLabContactVO == null)
			return null;
		return this.sendingLabContactVO.getLabContactId();
	}

	public Integer getReturnLabContactId() {
		if (this.returnLabContactVO == null)
			return null;
		return this.returnLabContactVO.getLabContactId();
	}

	public Session3VO getFirstExp() {
		Session3VO ses = null;
		if (this.sessions != null) {
			for (Iterator<Session3VO> iterator = sessions.iterator(); iterator.hasNext();) {
				// TODO select the first
				ses = iterator.next();
			}
		}
		return ses;
	}

	public Date getFirstExpDate() {
		// why today date? 
		//Date expDate = new Date();
		Date expDate = null;
		if (this.getFirstExp() != null) {
			expDate = this.getFirstExp().getStartDate();
		}
		return expDate;
	}

	public String getFirstExpBl() {
		String bl = null;
		if (this.getFirstExp() != null) {
			bl = this.getFirstExp().getBeamlineName();
		}
		return bl;
	}

	public String getBeamlineOperator() {
		String bl = null;
		if (this.getFirstExp() != null) {
			bl = this.getFirstExp().getBeamlineOperator();
		}
		return bl;
	}

}
