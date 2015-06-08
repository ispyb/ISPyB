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
 * SendReportForm.java
 * @author ludovic.launer@esrf.fr
 * Jan 9, 2006
 */

package ispyb.client.common.help;

import java.io.Serializable;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

/**
* @struts.form name="sendReportForm"
*/

public class SendReportForm extends ActionForm implements Serializable
{
	static final long serialVersionUID = 0;

	private FormFile	uploadedFile;
	
	private String	fileFullPath							= null;

	public FormFile getUploadedFile()						{return uploadedFile;}
	public void setUploadedFile(FormFile uploadedFile)		{this.uploadedFile = uploadedFile;}
	
	public String getFileFullPath()							{return fileFullPath;}
	public void setFileFullPath(String fileFullPath)		{this.fileFullPath = fileFullPath;}
}
