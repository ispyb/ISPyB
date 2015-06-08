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
 * ViewDewarHistoryForm.java
 * @author Phil PASCAL
 */

package ispyb.client.common.shipping;

import ispyb.server.common.vos.proposals.LabContact3VO;
import ispyb.server.common.vos.proposals.Laboratory3VO;
import ispyb.server.common.vos.proposals.Person3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.DewarTransportHistory3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

/**
 * @struts.form name="viewDewarHistoryForm"
 */

public class ViewDewarHistoryForm extends ActionForm implements Serializable {

	static final long serialVersionUID = 0;

	private Dewar3VO dewar = new Dewar3VO();

	private List<DewarTransportHistory3VO> dewarEvents = new ArrayList<DewarTransportHistory3VO>();

	private Shipping3VO shipping = new Shipping3VO();

	private Integer parcelsNumber = new Integer(-1);

	private LabContact3VO sendingLabContact = new LabContact3VO();

	private LabContact3VO returnLabContact = new LabContact3VO();

	private Person3VO sendingPerson = new Person3VO();

	private Person3VO returnPerson = new Person3VO();

	private Laboratory3VO sendingLaboratory = new Laboratory3VO();

	private Laboratory3VO returnLaboratory = new Laboratory3VO();

	// ______________________________________________________________________________________________________________________

	public ViewDewarHistoryForm() {
	}

	public Dewar3VO getDewar() {
		return dewar;
	}

	public void setDewar(Dewar3VO dewar) {
		this.dewar = dewar;
	}

	public Shipping3VO getShipping() {
		return shipping;
	}

	public void setShipping(Shipping3VO shipping) {
		this.shipping = shipping;
	}

	public Integer getParcelsNumber() {
		return parcelsNumber;
	}

	public void setParcelsNumber(Integer parcelsNumber) {
		this.parcelsNumber = parcelsNumber;
	}

	public LabContact3VO getSendingLabContact() {
		return sendingLabContact;
	}

	public void setSendingLabContact(LabContact3VO sendingLabContact) {
		this.sendingLabContact = sendingLabContact;
	}

	public LabContact3VO getReturnLabContact() {
		return returnLabContact;
	}

	public void setReturnLabContact(LabContact3VO returnLabContact) {
		this.returnLabContact = returnLabContact;
	}

	public Person3VO getSendingPerson() {
		return sendingPerson;
	}

	public void setSendingPerson(Person3VO sendingPerson) {
		this.sendingPerson = sendingPerson;
	}

	public Person3VO getReturnPerson() {
		return returnPerson;
	}

	public void setReturnPerson(Person3VO returnPerson) {
		this.returnPerson = returnPerson;
	}

	public Laboratory3VO getSendingLaboratory() {
		return sendingLaboratory;
	}

	public void setSendingLaboratory(Laboratory3VO sendingLaboratory) {
		this.sendingLaboratory = sendingLaboratory;
	}

	public Laboratory3VO getReturnLaboratory() {
		return returnLaboratory;
	}

	public void setReturnLaboratory(Laboratory3VO returnLaboratory) {
		this.returnLaboratory = returnLaboratory;
	}

	public List<DewarTransportHistory3VO> getDewarEvents() {
		return dewarEvents;
	}

	public void setDewarEvents(List<DewarTransportHistory3VO> dewarEvents) {
		this.dewarEvents = dewarEvents;
	}
}
