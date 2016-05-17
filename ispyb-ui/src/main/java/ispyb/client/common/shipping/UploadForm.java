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
 * UploadForm.java
 * @author ludovic.launer@esrf.fr
 * Dec 14, 2004
 */

package ispyb.client.common.shipping;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

import ispyb.client.mx.sample.ViewCrystalForm;

/**
* @struts.form name="uploadForm"
*/

public class UploadForm extends ActionForm implements Serializable 
{
    static final long serialVersionUID = 0;

    private FormFile			requestFile							= null;
    private ViewCrystalForm		viewCrystalForm 					= new ViewCrystalForm();
	private String populatedTemplateURL = "";
	private String populatedForShipmentTemplageURL = "";
	private int currentShippingId = 0;
	
	private String codeInScDate; // Date when the DM code was entered into the
									// DB
	private String selectedBeamline = null; // Selected beamline to display DM
											// codes
	private List listDMCodes = new ArrayList(); // DM codes detected by the SC

    private String shippingId = "";											// current shippingId

    private boolean deleteAllShipment = true;
	public UploadForm() 
    	{
    	super();
    	}

    //______________________________________________________________________________________________________________________
	public FormFile getRequestFile()					{return requestFile;}
	public void setRequestFile(FormFile requestFile)	{this.requestFile = requestFile;}

	public ViewCrystalForm getViewCrystalForm() 					{return viewCrystalForm;}
	public void setViewCrystalForm(ViewCrystalForm viewCrystalForm) {this.viewCrystalForm = viewCrystalForm;}

	public String getPopulatedTemplateURL() 							{return populatedTemplateURL;}
	public void setPopulatedTemplateURL(String populatedTemplateURL) 	{this.populatedTemplateURL = populatedTemplateURL;}
	
	public String getPopulatedForShipmentTemplageURL() 										{return populatedForShipmentTemplageURL;}
	public void setPopulatedForShipmentTemplageURL(String populatedForShipmentTemplageURL) 	{this.populatedForShipmentTemplageURL = populatedForShipmentTemplageURL;}
	
	public int getCurrentShippingId() 														{return currentShippingId;}
	public void setCurrentShippingId(int currentShippingId) 								{this.currentShippingId = currentShippingId;}
	
	public String getSelectedBeamline() 						{return selectedBeamline;}
	public void setSelectedBeamline(String selectedBeamline) 	{this.selectedBeamline = selectedBeamline;}
	
	public String getCodeInScDate() 							{return codeInScDate;}
	public void setCodeInScDate(String codeInScDate) 			{this.codeInScDate = codeInScDate;}
	
	public List getListDMCodes() 								{return listDMCodes;}
	public void setListDMCodes(List listDMCodes) 				{this.listDMCodes = listDMCodes;}
	
	public String getShippingId() 								{return shippingId; }
	public void setShippingId(String shippingId) 				{this.shippingId = shippingId;}

	public boolean isDeleteAllShipment() {
		return deleteAllShipment;
	}

	public void setDeleteAllShipment(boolean deleteAllShipment) {
		this.deleteAllShipment = deleteAllShipment;
	}
	
	
}
