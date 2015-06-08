/*
 * CourierForm.java
 * @author patrice.brenchereau@esrf.fr
 * July 8, 2008
 */

package bcr.client.tracking;

import java.io.Serializable;

import org.apache.struts.action.ActionForm;

/**
 * @struts.form name="courierForm"
 */

public class CourierForm extends ActionForm implements Serializable {
	
	private String courierName ;
	private String currentDewarNumber;
	private String currentTrackingNumber;
	private String dewarList;
	private int nbDewars = 0;
	
	private String message;

	public String getCourierName() {
		return courierName;
	}

	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}

	public String getCurrentDewarNumber() {
		return currentDewarNumber;
	}

	public void setCurrentDewarNumber(String currentDewarNumber) {
		this.currentDewarNumber = currentDewarNumber;
	}

	public String getCurrentTrackingNumber() {
		return currentTrackingNumber;
	}

	public void setCurrentTrackingNumber(String currentTrackingNumber) {
		this.currentTrackingNumber = currentTrackingNumber;
	}

	public int getNbDewars() {
		return nbDewars;
	}

	public void setNbDewars(int nbDewars) {
		this.nbDewars = nbDewars;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDewarList() {
		return dewarList;
	}

	public void setDewarList(String dewarList) {
		this.dewarList = dewarList;
	}
	

}