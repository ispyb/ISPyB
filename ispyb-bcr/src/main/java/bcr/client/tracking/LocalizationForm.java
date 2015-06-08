/*
 * LocalizationForm.java
 * @author patrice.brenchereau@esrf.fr
 * July 8, 2008
 */

package bcr.client.tracking;

import java.io.Serializable;

import org.apache.struts.action.ActionForm;

/**
 * @struts.form name="localizationForm"
 */

public class LocalizationForm extends ActionForm implements Serializable {
	
	private String location ;
	private String currentDewarNumber;
	private String dewarList;
	private int nbDewars = 0;
	
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDewarList() {
		return dewarList;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setDewarList(String dewarList) {
		this.dewarList = dewarList;
	}

	public String getCurrentDewarNumber() {
		return currentDewarNumber;
	}

	public void setCurrentDewarNumber(String currentDewarNumber) {
		this.currentDewarNumber = currentDewarNumber;
	}

	public int getNbDewars() {
		return nbDewars;
	}

	public void setNbDewars(int nbDewars) {
		this.nbDewars = nbDewars;
	}



}