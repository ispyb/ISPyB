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
package ispyb.client.common.shipping;

import java.io.Serializable;
import java.util.List;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

/**
 * @struts.form name="createShippingFileForm"
 */
public class CreateShippingFileForm extends ActionForm implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// list possible values for field separator
	private List<String> listFieldSeparator;
	// list possible values for text separator
	private List<String> listTextSeparator;
	// field separator
	private String fieldSeparator;
	// text separator
	private String textSeparator;
	// file to upload
	private FormFile requestFile= null;
	
	//
	public List<String> getListFieldSeparator() {
		return listFieldSeparator;
	}
	public void setListFieldSeparator(List<String> listFieldSeparator) {
		this.listFieldSeparator = listFieldSeparator;
	}
	public List<String> getListTextSeparator() {
		return listTextSeparator;
	}
	public void setListTextSeparator(List<String> listTextSeparator) {
		this.listTextSeparator = listTextSeparator;
	}
	public String getFieldSeparator() {
		return fieldSeparator;
	}
	public void setFieldSeparator(String fieldSeparator) {
		this.fieldSeparator = fieldSeparator;
	}
	public String getTextSeparator() {
		return textSeparator;
	}
	public void setTextSeparator(String textSeparator) {
		this.textSeparator = textSeparator;
	}
	public FormFile getRequestFile() {
		return requestFile;
	}
	public void setRequestFile(FormFile requestFile) {
		this.requestFile = requestFile;
	}
	
	
}
