/****************************************************************************************************
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
 *****************************************************************************************************/
package ispyb.client.mx.collection;

/**
 * represents information for a file needed by the GUI: the fileName, the path, the content, if the file exists or not
 * @author BODIN
 *
 */
public class FileInformation {
	protected String fileName;
	protected boolean existFile;
	protected String fileFullPath;
	protected String fullFileContent;
	protected String directoryName;
	
	public FileInformation() {
		super();
	}
	
	public FileInformation(String fileName, boolean existFile,
			String fileFullPath, String fullFileContent, String directoryName) {
		super();
		this.fileName = fileName;
		this.existFile = existFile;
		this.fileFullPath = fileFullPath;
		this.fullFileContent = fullFileContent;
		this.directoryName = directoryName;
	}

	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public boolean isExistFile() {
		return existFile;
	}
	
	public void setExistFile(boolean existFile) {
		this.existFile = existFile;
	}
	public String getFileFullPath() {
		return fileFullPath;
	}
	
	public void setFileFullPath(String fileFullPath) {
		this.fileFullPath = fileFullPath;
	}
	
	public String getFullFileContent() {
		return fullFileContent;
	}
	
	public void setFullFileContent(String fullFileContent) {
		this.fullFileContent = fullFileContent;
	}

	public String getDirectoryName() {
		return directoryName;
	}

	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}
	
}
