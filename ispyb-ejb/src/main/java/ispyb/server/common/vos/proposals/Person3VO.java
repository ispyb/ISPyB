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

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Person3 value object mapping table Person
 * 
 */
@Entity
@Table(name = "Person")
@SqlResultSetMapping(name = "personNativeQuery", entities = { @EntityResult(entityClass = Person3VO.class) })
public class Person3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(Person3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "personId")
	protected Integer personId;

	@ManyToOne
	@JoinColumn(name = "laboratoryId")
	protected Laboratory3VO laboratoryVO;

	@Column(name = "siteId")
	protected String siteId;

	@Column(name = "personUUID")
	protected String personUUID;
	
	@Column(name = "familyName")
	protected String familyName;

	@Column(name = "givenName")
	protected String givenName;

	@Column(name = "title")
	protected String title;

	@Column(name = "emailAddress")
	protected String emailAddress;

	@Column(name = "phoneNumber")
	protected String phoneNumber;

	@Column(name = "login")
	protected String login;

	@Column(name = "faxNumber")
	protected String faxNumber;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "personId")
	private Set<Proposal3VO> proposalDirectVOs;

	@Fetch(value = FetchMode.SELECT)
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ProposalHasPerson", joinColumns = { @JoinColumn(name = "personId", referencedColumnName = "personId") }, inverseJoinColumns = { @JoinColumn(name = "proposalId", referencedColumnName = "proposalId") })
	private Set<Proposal3VO> proposalVOs;

	@Column(name = "externalId")
	protected Integer externalId;

	public Person3VO() {
		super();
	}

	public Person3VO(Integer personId, Laboratory3VO laboratoryVO, String siteId, String personUUID, String familyName, String givenName,
			String title, String emailAddress, String phoneNumber, String login, String faxNumber,
			Set<Proposal3VO> proposalVOs, Integer externalId) {
		super();
		this.personId = personId;
		this.laboratoryVO = laboratoryVO;
		this.personUUID = personUUID;
		this.siteId = siteId;
		this.familyName = familyName;
		this.givenName = givenName;
		this.title = title;
		this.emailAddress = emailAddress;
		this.phoneNumber = phoneNumber;
		this.login = login;
		this.faxNumber = faxNumber;
		this.proposalVOs = proposalVOs;
		this.externalId = externalId;
	}

	public Person3VO(Person3VO vo) {
		super();
		this.personId = vo.getPersonId();
		this.laboratoryVO = vo.getLaboratoryVO();
		this.siteId = vo.getSiteId();
		this.personUUID = vo.getPersonUUID();
		this.familyName = vo.getFamilyName();
		this.givenName = vo.getGivenName();
		this.title = vo.getTitle();
		this.emailAddress = vo.getEmailAddress();
		this.phoneNumber = vo.getPhoneNumber();
		this.login = vo.getLogin();
		this.faxNumber = vo.getFaxNumber();
		this.proposalVOs = vo.getProposalVOs();
		this.externalId = vo.getExternalId();
	}

	public void fillVOFromLight(PersonWS3VO vo) {
		this.personId = vo.getPersonId();
		this.laboratoryVO = null;
		this.siteId = vo.getSiteId();
		this.personUUID = vo.getPersonUUID();
		this.familyName = vo.getFamilyName();
		this.givenName = vo.getGivenName();
		this.title = vo.getTitle();
		this.emailAddress = vo.getEmailAddress();
		this.phoneNumber = vo.getPhoneNumber();
		this.login = vo.getLogin();
		this.faxNumber = vo.getFaxNumber();
		this.proposalVOs = null;
		this.externalId = vo.getExternalId();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getPersonId() {
		return personId;
	}

	/**
	 * @param pk
	 *            The pk to set.
	 */
	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public Laboratory3VO getLaboratoryVO() {
		return laboratoryVO;
	}

	public void setLaboratoryVO(Laboratory3VO laboratoryVO) {
		this.laboratoryVO = laboratoryVO;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getPersonUUID() {
		return personUUID;
	}

	public void setPersonUUID(String personUUID) {
		this.personUUID = personUUID;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public Set<Proposal3VO> getProposalVOs() {
		return proposalVOs;
	}

	public void setProposalVOs(Set<Proposal3VO> proposalVOs) {
		this.proposalVOs = proposalVOs;
	}

	public Set<Proposal3VO> getProposalDirectVOs() {
		return proposalDirectVOs;
	}

	public void setProposalDirectVOs(Set<Proposal3VO> proposalDirectVOs) {
		this.proposalDirectVOs = proposalDirectVOs;
	}

	public Integer getLaboratoryVOId() {
		return laboratoryVO == null ? null : laboratoryVO.getLaboratoryId();
	}

	
	public Integer getExternalId() {
		return externalId;
	}

	public void setExternalId(Integer externalId) {
		this.externalId = externalId;
	}

	/**
	 * Checks the values of this value object for correctness and completeness. Should be done before persisting the data in the DB.
	 * 
	 * @param create
	 *            should be true if the value object is just being created in the DB, this avoids some checks like testing the primary
	 *            key
	 * @throws Exception
	 *             if the data of the value object is not correct
	 */
	@Override
	public void checkValues(boolean create) throws Exception {
		// TODO
	}
}
