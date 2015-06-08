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
package ispyb.client.mx.collection;

import java.io.Serializable;

import org.apache.struts.action.ActionForm;

/**
 * @struts.form name="exportDataCollectionForm"
 */

public class ExportDataCollectionForm extends ActionForm implements Serializable {
    static final long serialVersionUID = 0;

    private String rmergeCutoff;
    private String isigmaCutoff;
    
    
	public String getRmergeCutoff() {
		return rmergeCutoff;
	}
	public void setRmergeCutoff(String mergeCutoff) {
		rmergeCutoff = mergeCutoff;
	}
	public String getIsigmaCutoff() {
		return isigmaCutoff;
	}
	public void setIsigmaCutoff(String sigmaCutoff) {
		isigmaCutoff = sigmaCutoff;
	}
}
