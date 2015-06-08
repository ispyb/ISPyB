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
 * genericViewShippingForm.java
 * 
 */

package ispyb.client.common.shipping;

import ispyb.server.common.vos.shipping.Shipping3VO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

/**
 * @struts.form name="genericViewShippingForm"
 */

public class GenericViewShippingForm extends ActionForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Shipping3VO> listShippings = new ArrayList<Shipping3VO>();
	
	private List<Integer> listNbSamplesPerShipping = new ArrayList<Integer>();
	
	private List<Integer> listNbDewarHistoryPerShipping = new ArrayList<Integer>();

	private String role;

	private Boolean userOrIndus = false;

	// field used to store a msg for a specific search
	private String shipmentMsg = new String();

	// fields used for the Search page
	private String shippingName = new String();

	private String creationDateStart = new String();

	private String creationDateEnd = new String();

	private String mainProposer = new String();

	private String proposalCodeNumber = new String();

	// __________________________________________________________________________________________
	public GenericViewShippingForm() {
		super();
	}

	// __________________________________________________________________________________________

	public List<Shipping3VO> getListShippings() {
		return listShippings;
	}

	public void setListShippings(List<Shipping3VO> listShippings) {
		this.listShippings = listShippings;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getShippingName() {
		return shippingName;
	}

	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}

	public String getCreationDateStart() {
		return creationDateStart;
	}

	public void setCreationDateStart(String creationDateStart) {
		this.creationDateStart = creationDateStart;
	}

	public String getCreationDateEnd() {
		return creationDateEnd;
	}

	public void setCreationDateEnd(String creationDateEnd) {
		this.creationDateEnd = creationDateEnd;
	}

	public String getMainProposer() {
		return mainProposer;
	}

	public void setMainProposer(String mainProposer) {
		this.mainProposer = mainProposer;
	}

	public String getProposalCodeNumber() {
		return proposalCodeNumber;
	}

	public void setProposalCodeNumber(String proposalCodeNumber) {
		this.proposalCodeNumber = proposalCodeNumber;
	}

	public Boolean getUserOrIndus() {
		return userOrIndus;
	}

	public void setUserOrIndus(Boolean userOrIndus) {
		this.userOrIndus = userOrIndus;
	}

	public String getShipmentMsg() {
		return this.shipmentMsg;
	}

	public void setShipmentMsg(String shipmentMsg) {
		this.shipmentMsg = shipmentMsg;
	}

	public List<Integer> getListNbSamplesPerShipping() {
		return listNbSamplesPerShipping;
	}

	public void setListNbSamplesPerShipping(List<Integer> listNbSamplesPerShipping) {
		this.listNbSamplesPerShipping = listNbSamplesPerShipping;
	}

	public List<Integer> getListNbDewarHistoryPerShipping() {
		return listNbDewarHistoryPerShipping;
	}

	public void setListNbDewarHistoryPerShipping(
			List<Integer> listNbDewarHistoryPerShipping) {
		this.listNbDewarHistoryPerShipping = listNbDewarHistoryPerShipping;
	}

}
