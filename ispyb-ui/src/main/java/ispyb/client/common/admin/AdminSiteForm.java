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
 * AdminSiteForm.java
 * @author patrice.brenchereau@esrf.fr
 * August 18, 2008
 */

package ispyb.client.common.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

/**
 * @struts.form name="adminSiteForm"
 */

public class AdminSiteForm extends ActionForm implements Serializable {
	
	// User messages form
	
	private String warningMessage;
	private String infoMessage;
	
	public String getWarningMessage() {
		return warningMessage;
	}
	public void setWarningMessage(String warningMessage) {
		this.warningMessage = warningMessage;
	}
	public String getInfoMessage() {
		return infoMessage;
	}
	public void setInfoMessage(String infoMessage) {
		this.infoMessage = infoMessage;
	}
	
	// Who's online form
	
	private List listInfo1 = new ArrayList();

	public List getListInfo1() {
		return listInfo1;
	}

	public void setListInfo1(List listInfo1) {
		this.listInfo1 = listInfo1;
	}
	
	private List listInfo2 = new ArrayList();

	public List getListInfo2() {
		return listInfo2;
	}

	public void setListInfo2(List listInfo2) {
		this.listInfo2 = listInfo2;
	}
	
	
	// Chart reports
	
	private String reportDataUrl;
	private String chartTitle;
	

	public String getReportDataUrl() {
		return reportDataUrl;
	}
	public void setReportDataUrl(String reportDataUrl) {
		this.reportDataUrl = reportDataUrl;
	}
	public String getChartTitle() {
		return chartTitle;
	}
	public void setChartTitle(String chartTitle) {
		this.chartTitle = chartTitle;
	}


}
