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

package ispyb.server.common.vos.proposals;

import ispyb.server.common.vos.ISPyBValueObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * LabContact3 value object mapping table LabContact
 * 
 */
@Entity
@Table(name = "LabContact")
public class LabContact3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(LabContact3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "labContactId")
	protected Integer labContactId;

	@ManyToOne
	@JoinColumn(name = "personId")
	protected Person3VO personVO;

	@Column(name = "cardName")
	protected String cardName;

	@ManyToOne
	@JoinColumn(name = "proposalId")
	private Proposal3VO proposalVO;

	@Column(name = "defaultCourrierCompany")
	protected String defaultCourrierCompany;

	@Column(name = "courierAccount")
	protected String courierAccount;

	@Column(name = "billingReference")
	protected String billingReference;

	@Column(name = "dewarAvgCustomsValue")
	protected Integer dewarAvgCustomsValue;

	@Column(name = "dewarAvgTransportValue")
	protected Integer dewarAvgTransportValue;

	public LabContact3VO() {
		super();
	}

	public LabContact3VO(Integer labContactId, Person3VO personVO, String cardName, Proposal3VO proposalVO,
			String defaultCourrierCompany, String courierAccount, String billingReference,
			Integer dewarAvgCustomsValue, Integer dewarAvgTransportValue) {
		super();
		this.labContactId = labContactId;
		this.personVO = personVO;
		this.cardName = cardName;
		this.proposalVO = proposalVO;
		this.defaultCourrierCompany = defaultCourrierCompany;
		this.courierAccount = courierAccount;
		this.billingReference = billingReference;
		this.dewarAvgCustomsValue = dewarAvgCustomsValue;
		this.dewarAvgTransportValue = dewarAvgTransportValue;
	}

	public LabContact3VO(LabContact3VO vo) {
		super();
		this.labContactId = vo.getLabContactId();
		this.personVO = vo.getPersonVO();
		this.cardName = vo.getCardName();
		this.proposalVO = vo.getProposalVO();
		this.defaultCourrierCompany = vo.getDefaultCourrierCompany();
		this.courierAccount = vo.getCourierAccount();
		this.billingReference = vo.getBillingReference();
		this.dewarAvgCustomsValue = vo.getDewarAvgCustomsValue();
		this.dewarAvgTransportValue = vo.getDewarAvgTransportValue();
	}

	public void fillVOFromLight(LabContactWS3VO vo) {
		this.labContactId = vo.getLabContactId();
		this.personVO = null;
		this.cardName = vo.getCardName();
		this.proposalVO = null;
		this.defaultCourrierCompany = vo.getDefaultCourrierCompany();
		this.courierAccount = vo.getCourierAccount();
		this.billingReference = vo.getBillingReference();
		this.dewarAvgCustomsValue = vo.getDewarAvgCustomsValue();
		this.dewarAvgTransportValue = vo.getDewarAvgTransportValue();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getLabContactId() {
		return labContactId;
	}

	/**
	 * @param pk
	 *            The pk to set.
	 */
	public void setLabContactId(Integer labContactId) {
		this.labContactId = labContactId;
	}

	public Person3VO getPersonVO() {
		return personVO;
	}

	public void setPersonVO(Person3VO personVO) {
		this.personVO = personVO;
	}

	public Proposal3VO getProposalVO() {
		return proposalVO;
	}

	public void setProposalVO(Proposal3VO proposalVO) {
		this.proposalVO = proposalVO;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getDefaultCourrierCompany() {
		return defaultCourrierCompany;
	}

	public void setDefaultCourrierCompany(String defaultCourrierCompany) {
		this.defaultCourrierCompany = defaultCourrierCompany;
	}

	public String getCourierAccount() {
		return courierAccount;
	}

	public void setCourierAccount(String courierAccount) {
		this.courierAccount = courierAccount;
	}

	public String getBillingReference() {
		return billingReference;
	}

	public void setBillingReference(String billingReference) {
		this.billingReference = billingReference;
	}

	public Integer getDewarAvgCustomsValue() {
		return dewarAvgCustomsValue;
	}

	public void setDewarAvgCustomsValue(Integer dewarAvgCustomsValue) {
		this.dewarAvgCustomsValue = dewarAvgCustomsValue;
	}

	public Integer getDewarAvgTransportValue() {
		return dewarAvgTransportValue;
	}

	public void setDewarAvgTransportValue(Integer dewarAvgTransportValue) {
		this.dewarAvgTransportValue = dewarAvgTransportValue;
	}

	public Integer getPersonVOId() {
		return personVO == null ? null : personVO.getPersonId();
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

}
