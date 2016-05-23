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

import org.apache.log4j.Logger;

/**
 * <p>
 * This bean handles ISPyB DewarAPI.
 * </p>
 */
public class DewarAPIBean extends Dewar3VO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger LOG = Logger.getLogger(DewarAPIBean.class);

	private String parcelName;

	private String sendingLabContactEmail;

	private String returnLabContactEmail;

	private String shippingName;

	private Date startDate;

	private String beamLineName;

	private String proposalTitle;

	private String proposalCode;

	private String proposalNumber;

	private String localContact;
	
	public DewarAPIBean(Dewar3VO vo, String localContact,String parcelName,
			String sendingLabContactEmail,String returnLabContactEmail,String shippingName,
			Date startDate,String beamLineName,String proposalTitle,String proposalCode,String proposalNumber) {
		super(vo);
		this.localContact = localContact;
		this.parcelName= parcelName;
		this.sendingLabContactEmail = sendingLabContactEmail;
		this.returnLabContactEmail = returnLabContactEmail;
		this.shippingName = shippingName ;
		this.startDate = startDate;
		this.beamLineName = beamLineName;
		this.proposalTitle = proposalTitle ; 
		this.proposalCode = proposalCode;
		this.proposalNumber = proposalNumber ; 
	}

	public DewarAPIBean() {
		super();
	}

	public String getParcelName() {
		return parcelName;
	}

	public void setParcelName(String parcelName) {
		this.parcelName = parcelName;
	}

	public String getSendingLabContactEmail() {
		return sendingLabContactEmail;
	}

	public void setSendingLabContactEmail(String sendingLabContactEmail) {
		this.sendingLabContactEmail = sendingLabContactEmail;
	}

	public String getReturnLabContactEmail() {
		return returnLabContactEmail;
	}

	public void setReturnLabContactEmail(String returnLabContactEmail) {
		this.returnLabContactEmail = returnLabContactEmail;
	}

	public String getShippingName() {
		return shippingName;
	}

	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getBeamLineName() {
		return beamLineName;
	}

	public void setBeamLineName(String beamLineName) {
		this.beamLineName = beamLineName;
	}

	public String getProposalTitle() {
		return proposalTitle;
	}

	public void setProposalTitle(String proposalTitle) {
		this.proposalTitle = proposalTitle;
	}

	public String getProposalCode() {
		return proposalCode;
	}

	public void setProposalCode(String proposalCode) {
		this.proposalCode = proposalCode;
	}

	public String getProposalNumber() {
		return proposalNumber;
	}

	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}

	public String getLocalContact() {
		return localContact;
	}

	public void setLocalContact(String localContact) {
		this.localContact = localContact;
	}


}