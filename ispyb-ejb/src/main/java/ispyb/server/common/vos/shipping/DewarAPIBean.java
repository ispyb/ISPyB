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
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.LabContact3VO;
import ispyb.server.common.vos.proposals.Person3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.DewarLocation3VO;
import ispyb.server.common.vos.shipping.DewarLocationList3VO;
import ispyb.server.common.vos.shipping.DewarTransportHistory3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.server.mx.services.collections.Session3Service;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.MotorPosition3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.sample.BLSampleWS3VO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

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