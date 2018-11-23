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


import java.io.Serializable;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

/**
 * @struts.form name="uploadXDSInputFileForm"
 */

public class UploadXDSInputFileForm extends ActionForm implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String type = null;
	private Integer collectionId = null;
	private FormFile requestFile = null;
	private String	fileFullPath = null;
	private String fileContent = null;
	private boolean fileContentPresent = false;
	
	public UploadXDSInputFileForm(){
    	super();
    }

	public Integer getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(Integer collectionId) {
		this.collectionId = collectionId;
	}
	
	public FormFile getRequestFile() {
		return requestFile;
	}

	public void setRequestFile(FormFile requestFile) {
		this.requestFile = requestFile;
	}

	public String getFileFullPath() {
		return fileFullPath;
	}

	public void setFileFullPath(String fileFullPath) {
		this.fileFullPath = fileFullPath;
	}

	public String getFileContent() {
		return fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}

	public boolean isFileContentPresent() {
		return fileContentPresent;
	}

	public void setFileContentPresent(boolean fileContentPresent) {
		this.fileContentPresent = fileContentPresent;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
