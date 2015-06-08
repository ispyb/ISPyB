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
 * ScreeningStrategySubWedge class for webservices
 * @author BODIN
 *
 */
public class ScreeningStrategySubWedgeWS3VO extends ScreeningStrategySubWedge3VO{
	private static final long serialVersionUID = -2245060386747760257L;
	
	private Integer screeningStrategyWedgeId;

	public ScreeningStrategySubWedgeWS3VO() {
		super();
	}
	
	public ScreeningStrategySubWedgeWS3VO(ScreeningStrategySubWedge3VO vo) {
		super(vo);
	}
	
	public ScreeningStrategySubWedgeWS3VO(Integer screeningStrategySubWedgeId,
			Integer screeningStrategyWedgeId, Integer subWedgeNumber,
			String rotationAxis, Double axisStart, Double axisEnd,
			Double exposureTime, Double transmission, Double oscillationRange,
			Double completeness, Double multiplicity, 
			Double doseTotal, Integer numberOfImages, String comments) {
		super();
		this.screeningStrategySubWedgeId = screeningStrategySubWedgeId;
		this.screeningStrategyWedgeId = screeningStrategyWedgeId;
		this.subWedgeNumber = subWedgeNumber;
		this.rotationAxis = rotationAxis;
		this.axisStart = axisStart;
		this.axisEnd = axisEnd;
		this.exposureTime = exposureTime;
		this.transmission = transmission;
		this.oscillationRange = oscillationRange;
		this.completeness = completeness;
		this.multiplicity = multiplicity;
		this.doseTotal = doseTotal;
		this.numberOfImages = numberOfImages;
		this.comments = comments ;
	}

	public Integer getScreeningStrategyWedgeId() {
		return screeningStrategyWedgeId;
	}

	public void setScreeningStrategyWedgeId(Integer screeningStrategyWedgeId) {
		this.screeningStrategyWedgeId = screeningStrategyWedgeId;
	}
	
	@Override
	public String toWSString(){
		String s= super.toWSString();
		s += ", screeningStrategyWedgeId="+this.screeningStrategyWedgeId;
		return s;
	}
	
}
