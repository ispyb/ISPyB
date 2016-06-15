/*************************************************************************************************
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
 ****************************************************************************************************/
package ispyb.server.mx.vos.collections;

/**
 * represents data needed to store image  position for webservices
 * @author 
 *
 */
public class ImagePosition {
	
	
	private String fileLocation;
	
	private String fileName ;
	
	private String jpegFileFullPath;
	
	private String jpegThumbnailFileFullPath;

	
	
	public ImagePosition() {
		super();
	}

	

	public ImagePosition(String fileLocation, String fileName,
			String jpegFileFullPath,
			String jpegThumbnailFileFullPath) {
		super();
		this.fileLocation = fileLocation;
		this.fileName = fileName;
		this.jpegFileFullPath = jpegFileFullPath;
		this.jpegThumbnailFileFullPath = jpegThumbnailFileFullPath;
	}



	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getJpegFileFullPath() {
		return jpegFileFullPath;
	}

	public void setJpegFileFullPath(String jpegFileFullPath) {
		this.jpegFileFullPath = jpegFileFullPath;
	}

	public String getJpegThumbnailFileFullPath() {
		return jpegThumbnailFileFullPath;
	}

	public void setJpegThumbnailFileFullPath(String jpegThumbnailFileFullPath) {
		this.jpegThumbnailFileFullPath = jpegThumbnailFileFullPath;
	}
	
	
}
