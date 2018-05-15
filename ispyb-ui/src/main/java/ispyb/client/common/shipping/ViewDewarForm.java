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
 * ViewDewarForm.java
 * @author ludovic.launer@esrf.fr
 * Dec 17, 2004
 */

package ispyb.client.common.shipping;

import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

/**
 * @struts.form name="viewDewarForm"
 */

public class ViewDewarForm extends ActionForm implements Serializable {
	static final long serialVersionUID = 0;

	private Integer dewarId = new Integer(Integer.MIN_VALUE);

	private String firstExperimentId = new String();
	
	private String code = new String();

	private String comments = new String();

	private Dewar3VO info = new Dewar3VO();

	private List<Dewar3VO> listInfo = new ArrayList<Dewar3VO>();

	private List<Container3VO> listInfoSlave = new ArrayList<Container3VO>();

	private List<CustomSessionBean> listSessions = new ArrayList<CustomSessionBean>();

	// list of dewar status
	private String[] listDewarStatus;
	
	private Integer nbReimbursedDewars;
	
	private Integer currentReimbursedDewars;
	
	private boolean remainingReimbursed;

	private String role;
	
	private String fedexCode;
	
	public ViewDewarForm() {
	}

	// ______________________________________________________________________________________________________________________
	public Dewar3VO getInfo() {
		return info;
	}

	public void setInfo(Dewar3VO info) {
		this.info = info;
	}

	public List<Dewar3VO> getListInfo() {
		return listInfo;
	}

	public void setListInfo(List<Dewar3VO> listInfo) {
		this.listInfo = listInfo;
	}

	public List<Container3VO> getListInfoSlave() {
		return listInfoSlave;
	}

	public void setListInfoSlave(List<Container3VO> listInfoSlave) {
		this.listInfoSlave = listInfoSlave;
	}

	public Integer getDewarId() {
		return dewarId;
	}

	public void setDewarId(Integer dewarId) {
		this.dewarId = dewarId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public List<CustomSessionBean> getListSessions() {
		return listSessions;
	}

	public void setListSessions(List<CustomSessionBean> listSessions) {
		this.listSessions = listSessions;
	}

	public String[] getListDewarStatus() {
		return this.listDewarStatus;
	}

	public void setListDewarStatus(String[] listDewarStatus) {
		this.listDewarStatus = listDewarStatus;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getFirstExperimentId() {
		return firstExperimentId;
	}

	public void setFirstExperimentId(String firstExperimentId) {
		this.firstExperimentId = firstExperimentId;
	}

	public Integer getNbReimbursedDewars() {
		return nbReimbursedDewars;
	}

	public void setNbReimbursedDewars(Integer nbReimbursedDewars) {
		this.nbReimbursedDewars = nbReimbursedDewars;
	}

	public boolean getRemainingReimbursed() {
		return remainingReimbursed;
	}

	public void setRemainingReimbursed(boolean remainingReimbursed) {
		this.remainingReimbursed = remainingReimbursed;
	}

	public String getFedexCode() {
		return fedexCode;
	}

	public void setFedexCode(String fedexCode) {
		this.fedexCode = fedexCode;
	}

	public Integer getCurrentReimbursedDewars() {
		return currentReimbursedDewars;
	}

	public void setCurrentReimbursedDewars(Integer currentReimbursedDewars) {
		this.currentReimbursedDewars = currentReimbursedDewars;
	}
	
}
