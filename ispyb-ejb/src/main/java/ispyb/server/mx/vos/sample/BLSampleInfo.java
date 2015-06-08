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

package ispyb.server.mx.vos.sample;

import ispyb.server.common.vos.shipping.Container3VO;

import java.io.Serializable;

public class BLSampleInfo implements Serializable, Cloneable{
	
	private static final long serialVersionUID = 5309905960982412232L;
	
	
	private BLSampleWS3VO blSample;
	private Protein3VO protein;
	private Crystal3VO crystal;
	private DiffractionPlan3VO diffractionPlan;
	private Container3VO container;
	
	
	public BLSampleInfo() {
		super();
	}
	
	public BLSampleInfo(BLSampleWS3VO blSample, Protein3VO protein,
			Crystal3VO crystal, DiffractionPlan3VO diffractionPlan,
			Container3VO container) {
		super();
		this.blSample = blSample;
		this.protein = protein;
		this.crystal = crystal;
		this.diffractionPlan = diffractionPlan;
		this.container = container;
	}

	public BLSampleWS3VO getBlSample() {
		return blSample;
	}

	public void setBlSample(BLSampleWS3VO blSample) {
		this.blSample = blSample;
	}

	public Protein3VO getProtein() {
		return protein;
	}

	public void setProtein(Protein3VO protein) {
		this.protein = protein;
	}

	public Crystal3VO getCrystal() {
		return crystal;
	}

	public void setCrystal(Crystal3VO crystal) {
		this.crystal = crystal;
	}

	public DiffractionPlan3VO getDiffractionPlan() {
		return diffractionPlan;
	}

	public void setDiffractionPlan(DiffractionPlan3VO diffractionPlan) {
		this.diffractionPlan = diffractionPlan;
	}

	public Container3VO getContainer() {
		return container;
	}

	public void setContainer(Container3VO container) {
		this.container = container;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		super.clone();
		BLSampleInfo o = new BLSampleInfo();
		o.setBlSample((BLSampleWS3VO)this.getBlSample().clone());
		o.setContainer((Container3VO)this.getContainer().clone());
		o.setCrystal((Crystal3VO)this.getCrystal().clone());
		o.setProtein((Protein3VO)this.getProtein().clone());
		o.setDiffractionPlan((DiffractionPlan3VO)this.getDiffractionPlan().clone());
		return o;	
	}

	
}
