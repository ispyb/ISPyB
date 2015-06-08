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
 * viewShippingForm.java
 * @author ludovic.launer@esrf.fr
 * Dec 14, 2004
 */

package ispyb.client.common.shipping;

import ispyb.server.common.vos.proposals.LabContact3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

/**
 * @struts.form name="viewShippingForm"
 */

public class ViewShippingForm extends ActionForm implements Serializable {
	private static final long serialVersionUID = 1L;

	private String shippingName = new String();

	private String shippingDateStart = new String();

	private String shippingDateEnd = new String();

	private String deliveryDate = new String();

	private Integer nbDewars;

	private Integer nbOtherComponents;

	private Boolean isIdenticalReturnAddress = false;

	private Integer sendingLabContactId;

	private Integer returnLabContactId;

	// TODO : collection
	private Integer[] experimentsScheduled = null;

	private Shipping3VO info = new Shipping3VO();

	private List<Shipping3VO> listInfo = new ArrayList<Shipping3VO>();

	private List<LabContact3VO> listLabContacts = new ArrayList<LabContact3VO>();

	private List<CustomSessionBean> listSessions = new ArrayList<CustomSessionBean>();

	private FormFile requestFile = null;

	private String fromPage;

	private Boolean selectForExp;

	private Integer theShippingId;

	/**
	 * Lab contact info
	 */
	private LabContact3VO labContact = new LabContact3VO();

	private List<String> listDefaultCourierCompany = new ArrayList<String>();

	private List<String> listCourierAccount = new ArrayList<String>();

	private List<String> listBillingReference = new ArrayList<String>();

	private List<String> listDewarAvgCustomsValue = new ArrayList<String>();

	private List<String> listDewarAvgTransportValue = new ArrayList<String>();

	// Used to search the shipping by machine run
	private Integer machineRunId;

	// ////////////////////////////////////////////

	public ViewShippingForm() {
		super();
	}

	// ______________________________________________________________________________________________________________________

	public Shipping3VO getInfo() {
		return info;
	}

	public void setInfo(Shipping3VO info) {
		this.info = info;
	}

	public FormFile getRequestFile() {
		return requestFile;
	}

	public void setRequestFile(FormFile requestFile) {
		this.requestFile = requestFile;
	}

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

	/**
	 * @return Returns the selectForExp.
	 */
	public Boolean getSelectForExp() {
		return selectForExp;
	}

	/**
	 * @param selectForExp
	 *            The selectForExp to set.
	 */
	public void setSelectForExp(Boolean selectForExp) {
		this.selectForExp = selectForExp;
	}

	/**
	 * @return Returns the theShippingId.
	 */
	public Integer getTheShippingId() {
		return theShippingId;
	}

	/**
	 * @param theShippingId
	 *            The theShippingId to set.
	 */
	public void setTheShippingId(Integer theShippingId) {
		this.theShippingId = theShippingId;
	}

	/**
	 * @return Returns the machineRunId.
	 */
	public Integer getMachineRunId() {
		return machineRunId;
	}

	/**
	 * @param machineRunId
	 *            The machineRunId to set.
	 */
	public void setMachineRunId(Integer machineRunId) {
		this.machineRunId = machineRunId;
	}

	public Integer[] getExperimentsScheduled() {
		return experimentsScheduled;
	}

	public void setExperimentsScheduled(Integer[] experimentsScheduled) {
		this.experimentsScheduled = experimentsScheduled;
	}

	public Boolean getIsIdenticalReturnAddress() {
		return isIdenticalReturnAddress;
	}

	public void setIsIdenticalReturnAddress(Boolean isIdenticalReturnAddress) {
		this.isIdenticalReturnAddress = isIdenticalReturnAddress;
	}

	public List<LabContact3VO> getListLabContacts() {
		return listLabContacts;
	}

	public void setListLabContacts(List<LabContact3VO> listLabContacts) {
		this.listLabContacts = listLabContacts;
	}

	public List<CustomSessionBean> getListSessions() {
		return listSessions;
	}

	public void setListSessions(List<CustomSessionBean> listSessions) {
		this.listSessions = listSessions;
	}

	public List<Shipping3VO> getListInfo() {
		return listInfo;
	}

	public void setListInfo(List<Shipping3VO> listInfo) {
		this.listInfo = listInfo;
	}

	public String getShippingName() {
		return shippingName;
	}

	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}

	public String getShippingDateStart() {
		return shippingDateStart;
	}

	public void setShippingDateStart(String shippingDateStart) {
		this.shippingDateStart = shippingDateStart;
	}

	public String getShippingDateEnd() {
		return shippingDateEnd;
	}

	public void setShippingDateEnd(String shippingDateEnd) {
		this.shippingDateEnd = shippingDateEnd;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Integer getNbOtherComponents() {
		return nbOtherComponents;
	}

	public void setNbOtherComponents(Integer nbOtherComponents) {
		this.nbOtherComponents = nbOtherComponents;
	}

	public Integer getNbDewars() {
		return nbDewars;
	}

	public void setNbDewars(Integer nbDewars) {
		this.nbDewars = nbDewars;
	}

	public LabContact3VO getLabContact() {
		return labContact;
	}

	public void setLabContact(LabContact3VO labContact) {
		this.labContact = labContact;
	}

	public List<String> getListDefaultCourierCompany() {
		return this.listDefaultCourierCompany;
	}

	public void setListDefaultCourierCompany(List<String> listDefaultCourierCompany) {
		this.listDefaultCourierCompany = listDefaultCourierCompany;
	}

	public List<String> getListCourierAccount() {
		return this.listCourierAccount;
	}

	public void setListCourierAccount(List<String> listCourierAccount) {
		this.listCourierAccount = listCourierAccount;
	}

	public List<String> getListBillingReference() {
		return this.listBillingReference;
	}

	public void setListBillingReference(List<String> listBillingReference) {
		this.listBillingReference = listBillingReference;
	}

	public List<String> getListDewarAvgCustomsValue() {
		return this.listDewarAvgCustomsValue;
	}

	public void setListDewarAvgCustomsValue(List<String> listDewarAvgCustomsValue) {
		this.listDewarAvgCustomsValue = listDewarAvgCustomsValue;
	}

	public List<String> getListDewarAvgTransportValue() {
		return this.listDewarAvgTransportValue;
	}

	public void setListDewarAvgTransportValue(List<String> listDewarAvgTransportValue) {
		this.listDewarAvgTransportValue = listDewarAvgTransportValue;
	}

	public Integer getSendingLabContactId() {
		return sendingLabContactId;
	}

	public void setSendingLabContactId(Integer sendingLabContactId) {
		this.sendingLabContactId = sendingLabContactId;
	}

	public Integer getReturnLabContactId() {
		return returnLabContactId;
	}

	public void setReturnLabContactId(Integer returnLabContactId) {
		this.returnLabContactId = returnLabContactId;
	}

}
