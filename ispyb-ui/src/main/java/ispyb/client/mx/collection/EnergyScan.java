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

import ispyb.server.mx.vos.collections.EnergyScan3VO;

/**
 * information about EnergyScan to be displayed
 * @author BODIN
 *
 */
public class EnergyScan extends EnergyScan3VO{
	private static final long serialVersionUID = 1L;
	
	protected boolean scanFileFullPathExists;
	
	protected String shortFileName;

	protected boolean choochExists;
	
	protected String jpegChoochFileFitPath;
	
	protected String sampleName;
	
	public EnergyScan() {
		super();
	}

	public EnergyScan(EnergyScan3VO vo, boolean scanFileFullPathExists,
			String shortFileName, boolean choochExists,
			String jpegChoochFileFitPath, String sampleName) {
		super(vo);
		this.scanFileFullPathExists = scanFileFullPathExists;
		this.shortFileName = shortFileName;
		this.choochExists = choochExists;
		this.jpegChoochFileFitPath = jpegChoochFileFitPath;
		this.sampleName = sampleName;
	}

	public boolean isScanFileFullPathExists() {
		return scanFileFullPathExists;
	}

	public void setScanFileFullPathExists(boolean scanFileFullPathExists) {
		this.scanFileFullPathExists = scanFileFullPathExists;
	}

	public String getShortFileName() {
		return shortFileName;
	}

	public void setShortFileName(String shortFileName) {
		this.shortFileName = shortFileName;
	}

	public boolean isChoochExists() {
		return choochExists;
	}

	public void setChoochExists(boolean choochExists) {
		this.choochExists = choochExists;
	}

	public String getJpegChoochFileFitPath() {
		return jpegChoochFileFitPath;
	}

	public void setJpegChoochFileFitPath(String jpegChoochFileFitPath) {
		this.jpegChoochFileFitPath = jpegChoochFileFitPath;
	}

	public String getSampleName() {
		return sampleName;
	}

	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}

	
	

}
