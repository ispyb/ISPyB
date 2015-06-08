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
package ispyb.client.mx.results;


import ispyb.server.mx.vos.sample.BLSample3VO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

/**
 * @struts.form name="analyseSampleForm"
 */

public class AnalyseSampleForm extends ActionForm implements Serializable {
    static final long serialVersionUID = 0;

    private BLSample3VO info = new BLSample3VO(); //sample
        
    private List listInfo = new ArrayList(); //samples

    public AnalyseSampleForm() {
        super();
    }

    /**
     * @return Returns the serialVersionUID.
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @return Returns the info.
     */
    public BLSample3VO getInfo() {
        return info;
    }

    /**
     * @param info
     *            The info to set.
     */
    public void setInfo(BLSample3VO info) {
        this.info = info;
    }

    /**
     * @return Returns the listInfo.
     */
    public List getListInfo() {
        return listInfo;
    }

    /**
     * @param listInfo
     *            The listInfo to set.
     */
    public void setListInfo(List listInfo) {
        this.listInfo = listInfo;
    }
}
