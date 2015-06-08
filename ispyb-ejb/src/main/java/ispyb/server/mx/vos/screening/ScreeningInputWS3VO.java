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
 * ScreeningInput class for webservices
 * @author BODIN
 *
 */
public class ScreeningInputWS3VO extends ScreeningInput3VO{
	private static final long serialVersionUID = 4588802987416265580L;
	
	private Integer screeningId;
	private Integer diffractionPlanId;
	
	public ScreeningInputWS3VO() {
		super();
	}
	
	public ScreeningInputWS3VO(ScreeningInput3VO vo) {
		super(vo);
	}

	
	public ScreeningInputWS3VO(Integer screeningInputId, Integer screeningId,
			Integer diffractionPlanId, Double beamX, Double beamY,
			Double rmsErrorLimits, Double minimumFractionIndexed,
			Double maximumFractionRejected, Double minimumSignalToNoise,
			String xmlSampleInformation) {
		super();
		this.screeningInputId = screeningInputId;
		this.screeningId = screeningId;
		this.diffractionPlanId = diffractionPlanId;
		this.beamX = beamX;
		this.beamY = beamY;
		this.rmsErrorLimits = rmsErrorLimits;
		this.minimumFractionIndexed = minimumFractionIndexed;
		this.maximumFractionRejected = maximumFractionRejected;
		this.minimumSignalToNoise = minimumSignalToNoise;
		this.xmlSampleInformation = xmlSampleInformation;
	}
	

	public Integer getScreeningId() {
		return screeningId;
	}

	public void setScreeningId(Integer screeningId) {
		this.screeningId = screeningId;
	}

	public Integer getDiffractionPlanId() {
		return diffractionPlanId;
	}

	public void setDiffractionPlanId(Integer diffractionPlanId) {
		this.diffractionPlanId = diffractionPlanId;
	}
	
	
	
	
}
