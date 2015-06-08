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
package ispyb.client.common.reference;

import ispyb.server.mx.vos.collections.IspybReference3VO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

/**
 * @struts.form name="viewReferenceForm"
 */

public class ViewReferenceForm extends ActionForm implements Serializable {
    public static final long serialVersionUID = 0;

    private List<IspybReference3VO> listReferences = new ArrayList<IspybReference3VO>();

	public ViewReferenceForm() {
		super();
	}

	public List<IspybReference3VO> getListReferences() {
		return listReferences;
	}

	public void setListReferences(List<IspybReference3VO> listReferences) {
		this.listReferences = listReferences;
	}
    
    
}
