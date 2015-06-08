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
 * imageId and boolean to indicate if the image has been created or not: use for web service workflow setImagesPositions
 * @author 
 *
 */
public class ImageCreation {
	
	
	private Integer imageId;
	
	private Boolean isCreated;
	
	private String fileLocation;
	
	private String fileName;
	
	
	public ImageCreation() {
		super();
	}


	
	public ImageCreation(Integer imageId, Boolean isCreated,
			String fileLocation, String fileName) {
		super();
		this.imageId = imageId;
		this.isCreated = isCreated;
		this.fileLocation = fileLocation;
		this.fileName = fileName;
	}



	public Integer getImageId() {
		return imageId;
	}


	public void setImageId(Integer imageId) {
		this.imageId = imageId;
	}


	public Boolean getIsCreated() {
		return isCreated;
	}


	public void setIsCreated(Boolean isCreated) {
		this.isCreated = isCreated;
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

		
}
