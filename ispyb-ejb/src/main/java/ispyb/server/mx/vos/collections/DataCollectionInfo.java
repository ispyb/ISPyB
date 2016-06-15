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
 ******************************************************************************************************************************/

package ispyb.server.mx.vos.collections;

import ispyb.server.mx.vos.sample.BLSampleWS3VO;

public class DataCollectionInfo extends DataCollection3VO{
	
	private static final long serialVersionUID = -7528595032943495303L;
	
	
	private String localContact;
	private String localContactEmail;
	private BLSampleWS3VO blSampleVO;
	
	// crystal
	private String spaceGroup;
	private Double cellA;
	private Double cellB;
	private Double cellC;
	private Double cellAlpha;
	private Double cellBeta;
	private Double cellGamma;
	
	// pdbFilePath
	private String pdbFilePath;
	
	
	
	public DataCollectionInfo(DataCollection3VO vo, String localContact,
			String localContactEmail, BLSampleWS3VO blSampleVO, 
			String spaceGroup, Double cellA, Double cellB, Double cellC, Double cellAlpha, Double cellBeta, 
			Double cellGamma, String pdbFilePath) {
		super(vo);
		this.localContact = localContact;
		this.localContactEmail = localContactEmail;
		this.blSampleVO = blSampleVO;
		this.spaceGroup = spaceGroup;
		this.cellA = cellA;
		this.cellB = cellB ; 
		this.cellC = cellC;
		this.cellAlpha = cellAlpha ; 
		this.cellBeta = cellBeta ;
		this.cellGamma = cellGamma ;
		this.pdbFilePath = pdbFilePath ;
	}

	public DataCollectionInfo() {
		super();
	}

	public String getLocalContact() {
		return localContact;
	}

	public void setLocalContact(String localContact) {
		this.localContact = localContact;
	}

	public String getLocalContactEmail() {
		return localContactEmail;
	}

	public void setLocalContactEmail(String localContactEmail) {
		this.localContactEmail = localContactEmail;
	}

	public BLSampleWS3VO getBlSampleVO() {
		return blSampleVO;
	}

	public void setBlSampleVO(BLSampleWS3VO blSampleVO) {
		this.blSampleVO = blSampleVO;
	}

	public String getSpaceGroup() {
		return spaceGroup;
	}

	public void setSpaceGroup(String spaceGroup) {
		this.spaceGroup = spaceGroup;
	}

	public Double getCellA() {
		return cellA;
	}

	public void setCellA(Double cellA) {
		this.cellA = cellA;
	}

	public Double getCellB() {
		return cellB;
	}

	public void setCellB(Double cellB) {
		this.cellB = cellB;
	}

	public Double getCellC() {
		return cellC;
	}

	public void setCellC(Double cellC) {
		this.cellC = cellC;
	}

	public Double getCellAlpha() {
		return cellAlpha;
	}

	public void setCellAlpha(Double cellAlpha) {
		this.cellAlpha = cellAlpha;
	}

	public Double getCellBeta() {
		return cellBeta;
	}

	public void setCellBeta(Double cellBeta) {
		this.cellBeta = cellBeta;
	}

	public Double getCellGamma() {
		return cellGamma;
	}

	public void setCellGamma(Double cellGamma) {
		this.cellGamma = cellGamma;
	}

	public String getPdbFilePath() {
		return pdbFilePath;
	}

	public void setPdbFilePath(String pdbFilePath) {
		this.pdbFilePath = pdbFilePath;
	}
	
	
}
