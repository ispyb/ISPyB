/*******************************************************************************
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
 ******************************************************************************/
/*
 * ProposalAndSessionAndProteinForm
 */

package ispyb.client.common.proposal;

import java.io.Serializable;

import org.apache.struts.action.ActionForm;

/**
 * @struts.form name="proposalAndSessionAndProteinForm"
 */

public class ProposalAndSessionAndProteinForm extends ActionForm implements Serializable
{
static final long			serialVersionUID	= 0;

private String startDate;
private String endDate;
private String proposalCode;
private String proposalNumberSt;

private boolean userIsManager = true;


	public ProposalAndSessionAndProteinForm() {
		super();
	}
	

/**
 * @return Returns the startDate.
 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            The startDate to set.
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	
/**
	 * @return Returns the endDate.
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate The endDate to set.
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

/**
 * @return Returns the proposalCode.
 */
public String getProposalCode() {
	return proposalCode;
}
/**
 * @param proposalCode The proposalCode to set.
 */
public void setProposalCode(String proposalCode) {
	this.proposalCode = proposalCode;
}
/**
 * @return Returns the proposalNumberSt.
 */
public String getProposalNumberSt() {
	return (proposalNumberSt==null?"":proposalNumberSt);
}
/**
 * @param proposalNumberSt The proposalNumberSt to set.
 */
public void setProposalNumberSt(String proposalNumberSt) {
	this.proposalNumberSt = proposalNumberSt;
}


/**
 * @return Returns the userIsManager.
 */
public boolean isUserIsManager() {
	return userIsManager;
}
/**
 * @param userIsManager The userIsManager to set.
 */
public void setUserIsManager(boolean userIsManager) {
	this.userIsManager = userIsManager;
}
}
