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
package ispyb.server.security;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class EmployeeVO implements Serializable {
	/**
    *
    */
	private static final long serialVersionUID = -6489532899167593439L;

	private final static Logger LOG = Logger.getLogger(EmployeeVO.class);

	private final String roomNumber;

	private final String postOfficeBox;

	private final String givenName;

	private final String departmentNumber;

	private final String esrfEndDate;

	private final String persCategory;

	private final String uid;

	private final String uidNumber;

	private final String mail;

	private final String cn;

	private final String esrfManagerUid;

	private final String telephoneNumber;

	private final String esrfSmallPhotoURL;

	private final String esrfAlternateMail;

	private final String sambaPwdLastSet;

	private final String sambaAcctFlags;

	private final String employeeNumber;

	private final String esrfStartDate;

	private final String sambaPwdCanChange;

	private final String sambaSID;

	private final String description;

	private final String secretaryTelephoneNumber;

	private final String esrfPhotoURL;

	private final String sn;

	private final String esrfMisClient;

	private final String siteNumber;

	private boolean obsolete = false;

	public EmployeeVO(Map<String, String> attributesMap) {
				
		this.roomNumber = attributesMap.get("roomNumber");
		this.postOfficeBox = attributesMap.get("postOfficeBox");
		this.givenName = attributesMap.get("givenName");
		this.departmentNumber = attributesMap.get("departmentNumber");
		this.esrfEndDate = attributesMap.get("esrfEndDate");		
		this.uid = attributesMap.get("uid");
		this.uidNumber = attributesMap.get("uidNumber");
		this.mail = attributesMap.get("mail");
		this.cn = attributesMap.get("cn");
		this.esrfManagerUid = attributesMap.get("esrfManagerUid");
		this.telephoneNumber = attributesMap.get("telephoneNumber");
		this.esrfSmallPhotoURL = attributesMap.get("esrfSmallPhotoURL");
		this.esrfAlternateMail = attributesMap.get("esrfAlternateMail");
		this.sambaPwdLastSet = attributesMap.get("sambaPwdLastSet");
		this.sambaAcctFlags = attributesMap.get("sambaAcctFlags");
		this.esrfStartDate = attributesMap.get("esrfStartDate");
		this.sambaPwdCanChange = attributesMap.get("sambaPwdCanChange");
		this.sambaSID = attributesMap.get("sambaSID");
		this.description = attributesMap.get("description");
		this.esrfPhotoURL = attributesMap.get("esrfPhotoURL");
		this.sn = attributesMap.get("sn");
		this.esrfMisClient = attributesMap.get("esrfMisClient");
		
		// search for esrf prefix 
		//TODO small trick to be removed end of 2017 and use final ldap attribute at ESRF
		this.persCategory = getEsrfAttribute("persCategory", (HashMap<String, String>)attributesMap);
		this.siteNumber = getEsrfAttribute("siteNumber", (HashMap<String, String>)attributesMap);
		this.secretaryTelephoneNumber = getEsrfAttribute("secretaryTelephoneNumber", (HashMap<String, String>)attributesMap);
		this.employeeNumber = getEsrfAttribute("employeeNumber", (HashMap<String, String>)attributesMap);

	}

	public String getCn() {
		return cn;
	}

	public String getDepartmentNumber() {
		return departmentNumber;
	}

	public String getDescription() {
		return description;
	}

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public String getEsrfAlternateMail() {
		return esrfAlternateMail;
	}

	public String getEsrfEndDate() {
		return esrfEndDate;
	}

	public String getEsrfManagerUid() {
		return esrfManagerUid;
	}

	public String getEsrfMisClient() {
		return esrfMisClient;
	}

	public String getEsrfPhotoURL() {
		return esrfPhotoURL;
	}

	public String getEsrfSmallPhotoURL() {
		return esrfSmallPhotoURL;
	}

	public String getEsrfStartDate() {
		return esrfStartDate;
	}

	public String getGivenName() {
		return givenName;
	}

	public String getMail() {
		return mail;
	}

	public String getPersCategory() {
		return persCategory;
	}

	public String getPostOfficeBox() {
		return postOfficeBox;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public String getSambaAcctFlags() {
		return sambaAcctFlags;
	}

	public String getSambaPwdCanChange() {
		return sambaPwdCanChange;
	}

	public String getSambaPwdLastSet() {
		return sambaPwdLastSet;
	}

	public String getSambaSID() {
		return sambaSID;
	}

	public String getSecretaryTelephoneNumber() {
		return secretaryTelephoneNumber;
	}

	public String getSn() {
		return sn;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public String getUid() {
		return uid;
	}

	public String getUidNumber() {
		return uidNumber;
	}

	public boolean isObsolete() {
		return obsolete;
	}

	public void setObsolete(boolean obsolete) {
		this.obsolete = obsolete;
	}

	public String getSiteNumber() {
		return siteNumber;
	}

	private String getEsrfAttribute(String key, HashMap<String, String> attributesMap){
		
		if ((attributesMap).containsKey(key)) {
			return attributesMap.get(key);
		} else {
			return attributesMap.get("esrf"+ key.substring(0, 1).toUpperCase() + key.substring(1));
		}

	}
}
