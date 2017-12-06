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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import ispyb.server.common.vos.ISPyBValueObject;


/**
 * ProposalHasPerson value object mapping table ProposalHasPerson
 * 
 */
@Entity
@Table(name = "ProposalHasPerson")
public class ProposalHasPerson3VO extends ISPyBValueObject implements Cloneable {

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "proposalHasPersonId")
	protected Integer proposalHasPersonId;
	
	@Column(name = "personId")
	protected Integer personId;
	
	@Column(name = "proposalId")
	protected Integer proposalId;
	
	public ProposalHasPerson3VO() {
		super();
	}
	
	public ProposalHasPerson3VO(Integer proposalHasPersonId, Integer personId, Integer proposalId) {
		super();
		this.proposalId = proposalId;
		this.personId = personId;
		this.proposalHasPersonId = proposalHasPersonId;
	}
	
	public ProposalHasPerson3VO(ProposalHasPerson3VO vo) {
		super();
		this.proposalHasPersonId = vo.getProposalHasPersonId();
		this.proposalId = vo.getProposalId();
		this.personId = vo.getPersonId();
		
	}
	
	public Integer getProposalId() {
		return proposalId;
	}

	public void setProposalId(Integer proposalId) {
		this.proposalId = proposalId;
	}

	public Integer getProposalHasPersonId() {
		return proposalHasPersonId;
	}

	public void setProposalHasPersonId(Integer proposalHasPersonId) {
		this.proposalHasPersonId = proposalHasPersonId;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	@Override
	public void checkValues(boolean create) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
