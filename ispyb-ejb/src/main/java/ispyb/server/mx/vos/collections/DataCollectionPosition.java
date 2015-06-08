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
 * represents data needed to store dataCollection position for webservices
 * @author 
 *
 */
public class DataCollectionPosition {
	
	
	private String fileLocation;
	
	private String fileName ;
	
	private MotorPosition3VO startPosition;
	
	private MotorPosition3VO endPosition;

	
	
	public DataCollectionPosition() {
		super();
	}

	public DataCollectionPosition(String fileLocation, String fileName,
			MotorPosition3VO startPosition, MotorPosition3VO endPosition) {
		super();
		this.fileLocation = fileLocation;
		this.fileName = fileName;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
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

	public MotorPosition3VO getStartPosition() {
		return startPosition;
	}
	
	public void setStartPosition(MotorPosition3VO startPosition) {
		this.startPosition = startPosition;
	}
	
	public MotorPosition3VO getEndPosition() {
		return endPosition;
	}

	public void setEndPosition(MotorPosition3VO endPosition) {
		this.endPosition = endPosition;
	}
}
