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
 * viewLabContactForm.java
 * @author philippe.pascal@esrf.fr
 * Apr 15, 2008
 */

package ispyb.client.common.labcontact;

import ispyb.server.common.vos.proposals.LabContact3VO;
import ispyb.server.common.vos.proposals.Laboratory3VO;
import ispyb.server.common.vos.proposals.Person3VO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

/**
 * @struts.form name="viewLabContactForm"
 */

public class ViewLabContactForm extends ActionForm implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Lab contact info
	 */
	private LabContact3VO labContact = new LabContact3VO();

	/**
	 * Laboratory
	 */
	private Laboratory3VO laboratory = new Laboratory3VO();

	/**
	 * Person
	 */
	private Person3VO person = new Person3VO();
	
	private String name;
	
	private String firstName;

	/**
	 * Used for ViewAll screen
	 */
	private List<LabContact3VO> listOfLabContacts = new ArrayList<LabContact3VO>();
	
	/* indicates if the labcontact can be deleted or not: nbShipping ==0 */
	private List<Integer> listLabContactDeleted = new ArrayList<Integer>();

	/**
	 * Used for Login screen
	 */
	private List<ScientistInfosBean> listOfScientists = new ArrayList<ScientistInfosBean>();

	private String fromPage;

	private String shippingId;

	public ViewLabContactForm() {
		super();
	}

	// ______________________________________________________________________________________________________________________

	/**
	 * @return Returns the fromPage.
	 */
	public String getFromPage() {
		return fromPage;
	}

	/**
	 * @param fromPage
	 *            The fromPage to set.
	 */
	public void setFromPage(String fromPage) {
		this.fromPage = fromPage;
	}

	public LabContact3VO getLabContact() {
		return labContact;
	}

	public void setLabContact(LabContact3VO labContact) {
		this.labContact = labContact;
	}

	public Laboratory3VO getLaboratory() {
		return laboratory;
	}

	public void setLaboratory(Laboratory3VO laboratory) {
		this.laboratory = laboratory;
	}

	public Person3VO getPerson() {
		return person;
	}

	public void setPerson(Person3VO person) {
		this.person = person;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public List<LabContact3VO> getListOfLabContacts() {
		return listOfLabContacts;
	}

	public void setListOfLabContacts(List<LabContact3VO> listOfLabContacts) {
		this.listOfLabContacts = listOfLabContacts;
	}

	public List<ScientistInfosBean> getListOfScientists() {
		return listOfScientists;
	}

	public void setListOfScientists(List<ScientistInfosBean> listOfScientists) {
		this.listOfScientists = listOfScientists;
	}

	public String getShippingId() {
		return shippingId;
	}

	public void setShippingId(String shippingId) {
		this.shippingId = shippingId;
	}

	public List<Integer> getListLabContactDeleted() {
		return listLabContactDeleted;
	}

	public void setListLabContactDeleted(List<Integer> listLabContactDeleted) {
		this.listLabContactDeleted = listLabContactDeleted;
	}

}
