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

package ispyb.client.common.shipping;

import ispyb.client.common.util.MISArrayUtils;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

/**
 * @struts.form name="submitShippingForm"
 */

public class SubmitShippingForm extends ActionForm implements Serializable {
	private static final long serialVersionUID = 1L;

	private String sendingDate = new String();

	private String expectedEsrfArrivalDate = new String();

	private Shipping3VO shipping = new Shipping3VO();

	// the list of transaction equipment selected
	private List<Dewar3VO> listDewars = new ArrayList<Dewar3VO>();

	// the list of transaction equipment selected
	private String[] trackingNumbers = new String[1];

	public SubmitShippingForm() {
		super();
	}

	// __________________________________________________________________________________

	public Shipping3VO getShipping() {
		return shipping;
	}

	public void setShipping(Shipping3VO shipping) {
		this.shipping = shipping;
	}

	public List<Dewar3VO> getListDewars() {
		return listDewars;
	}

	public void setListDewars(List<Dewar3VO> listDewars) {
		this.listDewars = listDewars;
	}

	public void setTrackingNumbers(int index, String nb) {
		if (trackingNumbers == null || trackingNumbers.length == 0) {
			trackingNumbers = new String[index + 1];
		}
		trackingNumbers = (String[]) MISArrayUtils.setElement(trackingNumbers, nb, index);
	}

	public String getTrackingNumbers(int index) {
		if (index >= trackingNumbers.length) {
			trackingNumbers = (String[]) MISArrayUtils.expandArray(trackingNumbers, index - trackingNumbers.length + 1);
		}
		return trackingNumbers[index];
	}

	public String[] getTrackingNumbers() {
		return trackingNumbers;
	}

	public void setTrackingNumbers(String[] trackingNumbers) {
		this.trackingNumbers = trackingNumbers;
	}

	public String getSendingDate() {
		return sendingDate;
	}

	public void setSendingDate(String sendingDate) {
		this.sendingDate = sendingDate;
	}

	public String getExpectedEsrfArrivalDate() {
		return expectedEsrfArrivalDate;
	}

	public void setExpectedEsrfArrivalDate(String expectedEsrfArrivalDate) {
		this.expectedEsrfArrivalDate = expectedEsrfArrivalDate;
	}

}
