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

public class SnapshotInfo {
	protected  String 		fileLocation;
	protected boolean		filePresent = true;
	
	public SnapshotInfo(String fileLocation)
		{
		this.fileLocation = fileLocation;
		}
	
	public SnapshotInfo(String fileLocation, boolean filePresent)
		{
		this.fileLocation 	= fileLocation;
		this.filePresent	= filePresent;
		}
	
	public String getFileLocation() 					{return fileLocation;}
	public void setFileLocation(String fileLocation) 	{this.fileLocation = fileLocation;}
	public boolean isFilePresent() 						{return filePresent;}
	public void setFilePresent(boolean filePresent) 	{this.filePresent = filePresent;}
}
