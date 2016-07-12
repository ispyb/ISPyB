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


package ispyb.server.mx.vos.screening;

/**
 * ScreeningStrategyWedge class for webservices
 * @author BODIN
 *
 */
public class ScreeningStrategyWedgeWS3VO extends ScreeningStrategyWedge3VO{
	
	private static final long serialVersionUID = 2406103593492195563L;
	
	private Integer screeningStrategyId;

	public ScreeningStrategyWedgeWS3VO() {
		super();
	}
	
	public ScreeningStrategyWedgeWS3VO(ScreeningStrategyWedge3VO vo) {
		super(vo);
	}
	
	public ScreeningStrategyWedgeWS3VO(Integer screeningStrategyWedgeId,
			Integer screeningStrategyId, Integer wedgeNumber,
			Double resolution, Double completeness, Double multiplicity,
			Double doseTotal, Integer numberOfImages, Double phi, Double kappa,
			Double chi, String comments, Double wavelength) {
		super();
		this.screeningStrategyWedgeId = screeningStrategyWedgeId;
		this.screeningStrategyId = screeningStrategyId;
		this.wedgeNumber = wedgeNumber;
		this.resolution = resolution;
		this.completeness = completeness;
		this.multiplicity = multiplicity;
		this.doseTotal = doseTotal;
		this.numberOfImages = numberOfImages;
		this.phi = phi;
		this.kappa = kappa;
		this.chi = chi;
		this.comments = comments ;
		this.wavelength = wavelength ;
	}

	public Integer getScreeningStrategyId() {
		return screeningStrategyId;
	}

	public void setScreeningStrategyId(Integer screeningStrategyId) {
		this.screeningStrategyId = screeningStrategyId;
	}
	
	@Override
	public String toWSString(){
		String s= super.toWSString();
		s += ", screeningStrategyId="+this.screeningStrategyId;
		return s;
	}
	
}
