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
 * genericViewDewarForm.java
 * 
 */

package ispyb.client.common.shipping;

import ispyb.server.common.vos.shipping.Dewar3VO;

import java.io.Serializable;
import java.util.List;

import org.apache.struts.action.ActionForm;

/**
 * @struts.form name="genericViewDewarForm"
 */

public class GenericViewDewarForm extends ActionForm implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<Dewar3VO> listDewars;

	private List<Integer> listNbSamplesPerDewar;

	private String role;

	private Boolean userOrIndus;

	// field used to store a msg for a specific search
	private String dewarMsg = new String();

	// fields used for the Search page
	private String dewarName = new String();

	private String comments = new String();

	private String barCode = new String();

	private String experimentDateStart = new String();

	private String experimentDateEnd = new String();

	private String dewarStatus = new String();

	private String storageLocation = new String();

	private int nbDays;

	// ____________________________________________________________________________

	public GenericViewDewarForm() {
		super();
	}

	// ____________________________________________________________________________

	public String getRole() {
		return role;
	}

	public List<Dewar3VO> getListDewars() {
		return listDewars;
	}

	public void setListDewars(List<Dewar3VO> listDewars) {
		this.listDewars = listDewars;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDewarName() {
		return dewarName;
	}

	public void setDewarName(String dewarName) {
		this.dewarName = dewarName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getExperimentDateStart() {
		return experimentDateStart;
	}

	public void setExperimentDateStart(String experimentDateStart) {
		this.experimentDateStart = experimentDateStart;
	}

	public String getExperimentDateEnd() {
		return experimentDateEnd;
	}

	public void setExperimentDateEnd(String experimentDateEnd) {
		this.experimentDateEnd = experimentDateEnd;
	}

	public String getDewarStatus() {
		return dewarStatus;
	}

	public void setDewarStatus(String dewarStatus) {
		this.dewarStatus = dewarStatus;
	}

	public String getStorageLocation() {
		return storageLocation;
	}

	public void setStorageLocation(String storageLocation) {
		this.storageLocation = storageLocation;
	}

	public Boolean getUserOrIndus() {
		return userOrIndus;
	}

	public void setUserOrIndus(Boolean userOrIndus) {
		this.userOrIndus = userOrIndus;
	}

	public String getDewarMsg() {
		return this.dewarMsg;
	}

	public void setDewarMsg(String dewarMsg) {
		this.dewarMsg = dewarMsg;
	}

	public List<Integer> getListNbSamplesPerDewar() {
		return listNbSamplesPerDewar;
	}

	public void setListNbSamplesPerDewar(List<Integer> listNbSamplesPerDewar) {
		this.listNbSamplesPerDewar = listNbSamplesPerDewar;
	}

	public int getNbDays() {
		return nbDays;
	}

	public void setNbDays(int nbDays) {
		this.nbDays = nbDays;
	}

}
