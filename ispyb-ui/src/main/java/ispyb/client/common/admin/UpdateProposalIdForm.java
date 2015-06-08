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
/**
 * ViewProposalForm
 */

package ispyb.client.common.admin;

import java.io.Serializable;

import org.apache.struts.action.ActionForm;

/**
 * @struts.form name="updateProposalIdForm"
 */

public class UpdateProposalIdForm extends ActionForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer oldPropId;
	private Integer newPropId;
	

    /**
	 * @return Returns the newPropId.
	 */
	public Integer getNewPropId() {
		return newPropId;
	}

	/**
	 * @param newPropId The newPropId to set.
	 */
	public void setNewPropId(Integer newPropId) {
		this.newPropId = newPropId;
	}

	/**
	 * @return Returns the oldPropId.
	 */
	public Integer getOldPropId() {
		return oldPropId;
	}
	/**
	 * @param oldPropId The oldPropId to set.
	 */
	public void setOldPropId(Integer oldPropId) {
		this.oldPropId = oldPropId;
	}
	public UpdateProposalIdForm() {
        super();
    }

}
