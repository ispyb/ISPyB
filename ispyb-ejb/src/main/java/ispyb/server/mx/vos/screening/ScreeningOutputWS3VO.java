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
 * ScreeningOutput class for webservices
 * @author BODIN
 *
 */
public class ScreeningOutputWS3VO extends ScreeningOutput3VO{
	private static final long serialVersionUID = -7618678094184722662L;
	private Integer screeningId;

	
	public ScreeningOutputWS3VO() {
		super();
	}

	public ScreeningOutputWS3VO(ScreeningOutput3VO vo) {
		super(vo);
	}
	
	public ScreeningOutputWS3VO(Integer screeningOutputId, Integer screeningId,
			String statusDescription,
			Integer rejectedReflections, Double resolutionObtained,
			Double spotDeviationR, Double spotDeviationTheta,
			Double beamShiftX, Double beamShiftY, Integer numSpotsFound,
			Integer numSpotsUsed, Integer numSpotsRejected, Double mosaicity,
			Double ioverSigma, Byte diffractionRings, Byte strategySuccess,
			Byte indexingSuccess, Byte mosaicityEstimated,
			Double rankingResolution, String program, Double doseTotal,
			Double totalExposureTime, Double totalRotationRange,
			Integer totalNumberOfImages, Double rFriedel) {
		super();
		this.screeningOutputId = screeningOutputId;
		this.screeningId = screeningId;
		this.statusDescription = statusDescription;
		this.rejectedReflections = rejectedReflections;
		this.resolutionObtained = resolutionObtained;
		this.spotDeviationR = spotDeviationR;
		this.spotDeviationTheta = spotDeviationTheta;
		this.beamShiftX = beamShiftX;
		this.beamShiftY = beamShiftY;
		this.numSpotsFound = numSpotsFound;
		this.numSpotsUsed = numSpotsUsed;
		this.numSpotsRejected = numSpotsRejected;
		this.mosaicity = mosaicity;
		this.ioverSigma = ioverSigma;
		this.diffractionRings = diffractionRings;
		this.strategySuccess = strategySuccess;
		this.indexingSuccess = indexingSuccess;
		this.mosaicityEstimated = mosaicityEstimated;
		this.rankingResolution = rankingResolution;
		this.program = program;
		this.doseTotal = doseTotal;
		this.totalExposureTime = totalExposureTime;
		this.totalRotationRange = totalRotationRange;
		this.totalNumberOfImages = totalNumberOfImages;
		this.rFriedel = rFriedel;
	}


	public ScreeningOutputWS3VO(Integer screeningId) {
		super();
		this.screeningId = screeningId;
	}


	public Integer getScreeningId() {
		return screeningId;
	}


	public void setScreeningId(Integer screeningId) {
		this.screeningId = screeningId;
	}
	
	@Override
	public String toWSString(){
		String s= super.toWSString();
		s += ", screeningId="+this.screeningId;
		return s;
	}
	
	
}
