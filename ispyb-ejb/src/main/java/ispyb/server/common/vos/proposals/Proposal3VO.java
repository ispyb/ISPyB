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

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

import ispyb.server.common.vos.ISPyBValueObject;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.sample.Protein3VO;


/**
 * BeamLineSetup value object mapping table BeamLineSetup
 * 
 */
@Entity
@Table(name = "Proposal")
public class Proposal3VO extends ISPyBValueObject implements Cloneable {

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "proposalId")
	protected Integer proposalId;

	@ManyToOne
	@JoinColumn(name = "personId")
	private Person3VO personVO;

	@Column(name = "title")
	protected String title;

	@Column(name = "proposalCode")
	protected String code;

	@Column(name = "proposalNumber")
	protected String number;

	@Column(name = "proposalType")
	protected String type;

	@OneToMany
	@JoinColumn(name = "proposalId")
	private Set<Session3VO> sessionVOs;

	@OneToMany
	@JoinColumn(name = "proposalId")
	private Set<Protein3VO> proteinVOs;

	@OneToMany
	@JoinColumn(name = "proposalId")
	private Set<Shipping3VO> shippingVOs;
	
	@Column(name = "externalId")
	protected Integer externalId;

	@Column(name = "bltimeStamp")
	protected Date timeStamp;

	//@Fetch(value = FetchMode.SELECT)
	@ManyToMany//(fetch = FetchType.EAGER)
	//@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "ProposalHasPerson", joinColumns = { @JoinColumn(name = "proposalId", referencedColumnName = "proposalId") }, inverseJoinColumns = { @JoinColumn(name = "personId", referencedColumnName = "personId") })
	//protected Set<Person3VO> participants;
	private Set<Person3VO> participants;
	
	public Proposal3VO() {
		super();
	}
	
	public Proposal3VO(Integer proposalId, Person3VO personVO, String title,
			String code, String number, String type,
			Set<Session3VO> sessionVOs, Set<Protein3VO> proteinVOs,
			Set<Shipping3VO> shippingVOs, Integer externalId) {
		super();
		this.proposalId = proposalId;
		this.personVO = personVO;
		this.title = title;
		this.code = code;
		this.number = number;
		this.type = type;
		this.sessionVOs = sessionVOs;
		this.proteinVOs = proteinVOs;
		this.shippingVOs = shippingVOs;
		this.externalId = externalId;
		this.participants = null;
	}
	
	public Proposal3VO(Proposal3VO vo) {
		super();
		this.proposalId = vo.getProposalId();
		this.personVO = vo.getPersonVO();
		this.title = vo.getTitle();
		this.code = vo.getCode();
		this.number = vo.getNumber();
		this.type = vo.getType();
		this.sessionVOs = vo.getSessionVOs();
		this.proteinVOs = vo.getProteinVOs();
		this.shippingVOs = vo.getShippingVOs();
		this.externalId = vo.getExternalId();
		this.participants = vo.getParticipants();
	}
	
	public void fillVOFromLight(ProposalWS3VO vo) {
		this.proposalId = vo.getProposalId();
		this.personVO = null;
		this.title = vo.getTitle();
		this.code = vo.getCode();
		this.number = vo.getNumber();
		this.type = vo.getType();
		this.sessionVOs =  null;
		this.proteinVOs =  null;
		this.shippingVOs =  null;
		this.externalId = vo.getExternalId();
		this.participants = null;
	}


	public Integer getProposalId() {
		return proposalId;
	}

	public void setProposalId(Integer proposalId) {
		this.proposalId = proposalId;
	}


	public Person3VO getPersonVO() {
		return personVO;
	}

	public String getLogin() {
		return personVO == null ? null : personVO.getLogin();
	}

	public void setPersonVO(Person3VO personVO) {
		this.personVO = personVO;
	}


	public Set<Protein3VO> getProteinVOs() {
		return proteinVOs;
	}

	public void setProteinVOs(Set<Protein3VO> proteinVOs) {
		this.proteinVOs = proteinVOs;
	}

	public Set<Session3VO> getSessionVOs() {
		return sessionVOs;
	}

	public void setSessionVOs(Set<Session3VO> sessionVOs) {
		this.sessionVOs = sessionVOs;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Set<Shipping3VO> getShippingVOs() {
		return shippingVOs;
	}

	public void setShippingVOs(Set<Shipping3VO> shippingVOs) {
		this.shippingVOs = shippingVOs;
	}
	
	public Integer getPersonVOId() {
		return personVO == null ? null : personVO.getPersonId();
	}


	public Integer getExternalId() {
		return externalId;
	}


	public void setExternalId(Integer externalId) {
		this.externalId = externalId;
	}

	public Set<Person3VO> getParticipants() {
		return this.participants;
	}

	public void setParticipants(Set<Person3VO> participants) {
		this.participants = participants;
	}

	public void addParticipant(Person3VO participant) {
		if (this.participants == null) {
			this.participants = new HashSet<Person3VO>();
		}
		if (participant != null && !this.participants.contains(participant)) {
			this.participants.add(participant);
		}
	}

	public void clearParticipants() {
		this.participants.clear();

	}
	
	/**
	 * Checks the values of this value object for correctness and completeness. Should be done before persisting the
	 * data in the DB.
	 * 
	 * @param create
	 *            should be true if the value object is just being created in the DB, this avoids some checks like
	 *            testing the primary key
	 * @throws VOValidateException
	 *             if the data of the value object is not correct
	 */
	@Override
	public void checkValues(boolean create) throws Exception {
		//
	}

	/**
	 * used to clone an entity to set the linked collectio9ns to null, for webservices
	 */
	@Override
	public Proposal3VO clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (Proposal3VO) super.clone();
	}
	
	public String getProposalAccount(){
		return (this.code+this.number).toLowerCase();
	}

	public Date getTimeStamp() {
		return this.timeStamp;	
	}
}
