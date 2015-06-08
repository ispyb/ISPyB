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

package ispyb.client.mx.collection;

import ispyb.server.mx.vos.collections.Session3VO;

/**
 * represents the session information value to include the beamLine Operator Email
 * 
 * 
 */
public class SessionInformation extends Session3VO {
	
	private static final long serialVersionUID = 1L;
	
	protected String beamLineOperatorEmail;
	
	protected boolean hasDataCollectionGroup;
	
	protected String proposalCodeNumber;
	
	protected Integer nbCollect;
	
	protected Integer nbTest;
	
	protected Integer nbEnergyScans;
	
	protected Integer nbXRFSpectra;

	public SessionInformation(Session3VO sv, String beamLineOperatorEmail, boolean hasDataCollectionGroup) {
		super(sv);
		this.beamLineOperatorEmail = beamLineOperatorEmail;
		this.hasDataCollectionGroup = hasDataCollectionGroup;
		setProposalCodeNumber();
	}
	
	public void setBeamLineOperatorEmail(String beamLineOperatorEmail){
		this.beamLineOperatorEmail = beamLineOperatorEmail;
	}
	
	public String getBeamLineOperatorEmail(){
		return this.beamLineOperatorEmail;
	}
	
	public void setHasDataCollectionGroup(boolean hasDataCollectionGroup){
		this.hasDataCollectionGroup = hasDataCollectionGroup;
	}
	
	public boolean getHasDataCollectionGroup(){
		return this.hasDataCollectionGroup;
	}

	public String getProposalCodeNumber() {
		return proposalCodeNumber;
	}

	public void setProposalCodeNumber(String proposalCodeNumber) {
		this.proposalCodeNumber = proposalCodeNumber;
	}
	
	private void setProposalCodeNumber(){
		this.proposalCodeNumber = getProposalVO().getCode()+getProposalVO().getNumber();
	}

	public Integer getNbCollect() {
		return nbCollect;
	}

	public void setNbCollect(Integer nbCollect) {
		this.nbCollect = nbCollect;
	}

	public Integer getNbTest() {
		return nbTest;
	}

	public void setNbTest(Integer nbTest) {
		this.nbTest = nbTest;
	}

	public Integer getNbEnergyScans() {
		return nbEnergyScans;
	}

	public void setNbEnergyScans(Integer nbEnergyScans) {
		this.nbEnergyScans = nbEnergyScans;
	}

	public Integer getNbXRFSpectra() {
		return nbXRFSpectra;
	}

	public void setNbXRFSpectra(Integer nbXRFSpectra) {
		this.nbXRFSpectra = nbXRFSpectra;
	}

}
