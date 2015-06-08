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

import ispyb.server.mx.vos.collections.XFEFluorescenceSpectrum3VO;

/**
 * XRFSpectra information for the display
 * @author BODIN
 *
 */
public class XFESpectra extends XFEFluorescenceSpectrum3VO{
	private static final long serialVersionUID = 1L;
	
	protected boolean jpegScanExists;
	
	protected String jpegScanFileFullPathParser;
	
	protected boolean annotatedPymcaXfeSpectrumExists;
	
	protected String shortFileNameAnnotatedPymcaXfeSpectrum;
	
	protected String sampleName ;

	public XFESpectra() {
		super();
	}

	public XFESpectra(XFEFluorescenceSpectrum3VO vo, boolean jpegScanExists,
			String jpegScanFileFullPathParser,
			boolean annotatedPymcaXfeSpectrumExists,
			String shortFileNameAnnotatedPymcaXfeSpectrum, String sampleName) {
		super(vo);
		this.jpegScanExists = jpegScanExists;
		this.jpegScanFileFullPathParser = jpegScanFileFullPathParser;
		this.annotatedPymcaXfeSpectrumExists = annotatedPymcaXfeSpectrumExists;
		this.shortFileNameAnnotatedPymcaXfeSpectrum = shortFileNameAnnotatedPymcaXfeSpectrum;
		this.sampleName = sampleName;
	}

	public boolean isJpegScanExists() {
		return jpegScanExists;
	}

	public void setJpegScanExists(boolean jpegScanExists) {
		this.jpegScanExists = jpegScanExists;
	}

	public String getJpegScanFileFullPathParser() {
		return jpegScanFileFullPathParser;
	}

	public void setJpegScanFileFullPathParser(String jpegScanFileFullPathParser) {
		this.jpegScanFileFullPathParser = jpegScanFileFullPathParser;
	}

	public boolean isAnnotatedPymcaXfeSpectrumExists() {
		return annotatedPymcaXfeSpectrumExists;
	}

	public void setAnnotatedPymcaXfeSpectrumExists(
			boolean annotatedPymcaXfeSpectrumExists) {
		this.annotatedPymcaXfeSpectrumExists = annotatedPymcaXfeSpectrumExists;
	}

	public String getShortFileNameAnnotatedPymcaXfeSpectrum() {
		return shortFileNameAnnotatedPymcaXfeSpectrum;
	}

	public void setShortFileNameAnnotatedPymcaXfeSpectrum(
			String shortFileNameAnnotatedPymcaXfeSpectrum) {
		this.shortFileNameAnnotatedPymcaXfeSpectrum = shortFileNameAnnotatedPymcaXfeSpectrum;
	}

	public String getSampleName() {
		return sampleName;
	}

	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}
	
	

}
