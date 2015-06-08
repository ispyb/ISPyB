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
 * ScreeningStrategy class for webservices
 * @author BODIN
 *
 */
public class ScreeningStrategyWS3VO extends ScreeningStrategy3VO{
	
	private static final long serialVersionUID = 7372667149748760698L;
	
	private Integer screeningOutputId;

	public ScreeningStrategyWS3VO() {
		super();
	}
	
	public ScreeningStrategyWS3VO(ScreeningStrategy3VO vo) {
		super(vo);
	}
	
	public ScreeningStrategyWS3VO(Integer screeningStrategyId,
			Integer screeningOutputId, Double phiStart, Double phiEnd,
			Double rotation, Double exposureTime, Double resolution,
			Double completeness, Double multiplicity, Byte anomalous,
			String program, Double rankingResolution, Double transmission) {
		super();
		this.screeningStrategyId = screeningStrategyId;
		this.screeningOutputId = screeningOutputId;
		this.phiStart = phiStart;
		this.phiEnd = phiEnd;
		this.rotation = rotation;
		this.exposureTime = exposureTime;
		this.resolution = resolution;
		this.completeness = completeness;
		this.multiplicity = multiplicity;
		this.anomalous = anomalous;
		this.program = program;
		this.rankingResolution = rankingResolution;
		this.transmission = transmission;
	}

	public Integer getScreeningOutputId() {
		return screeningOutputId;
	}

	public void setScreeningOutputId(Integer screeningOutputId) {
		this.screeningOutputId = screeningOutputId;
	}
	
	@Override
	public String toWSString(){
		String s= super.toWSString();
		s += ", screeningOutputId="+this.screeningOutputId;
		return s;
	}
	
	
	
}
