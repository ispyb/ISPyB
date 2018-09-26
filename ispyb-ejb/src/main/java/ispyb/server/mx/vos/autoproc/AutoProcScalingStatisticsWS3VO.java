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

package ispyb.server.mx.vos.autoproc;

import java.util.Date;

/**
 * AutoProcScalingStatistics class for webservices
 * @author BODIN
 *
 */
public class AutoProcScalingStatisticsWS3VO extends AutoProcScalingStatistics3VO{
	private static final long serialVersionUID = 5281759556326477140L;
	private Integer autoProcScalingId;

	public AutoProcScalingStatisticsWS3VO() {
		super();
	}

	public AutoProcScalingStatisticsWS3VO(Integer autoProcScalingId) {
		super();
		this.autoProcScalingId = autoProcScalingId;
	}
	
	public AutoProcScalingStatisticsWS3VO(Integer autoProcScalingStatisticsId,
			Integer autoProcScalingId, String scalingStatisticsType,
			String comments, Float resolutionLimitLow,
			Float resolutionLimitHigh, Float rmerge,
			Float rmeasWithinIplusIminus, Float rmeasAllIplusIminus,
			Float rpimWithinIplusIminus, Float rpimAllIplusIminus,
			Float fractionalPartialBias, Integer nTotalObservations,
			Integer nTotalUniqueObservations, Float meanIoverSigI,
			Float completeness, Float multiplicity,
			Float anomalousCompleteness, Float anomalousMultiplicity,
			Date recordTimeStamp, Boolean anomalous, Float ccHalf, Float sigAno, Float ccAno, Float ISa,
			Float completenessSpherical, Float anomalousCompletenessSpherical,
			Float completenessEllipsoidal, Float anomalousCompletenessEllipsoidal) {
		super();
		this.autoProcScalingStatisticsId = autoProcScalingStatisticsId;
		this.autoProcScalingId = autoProcScalingId;
		this.scalingStatisticsType = scalingStatisticsType;
		this.comments = comments;
		this.resolutionLimitLow = resolutionLimitLow;
		this.resolutionLimitHigh = resolutionLimitHigh;
		this.rmerge = rmerge;
		this.rmeasWithinIplusIminus = rmeasWithinIplusIminus;
		this.rmeasAllIplusIminus = rmeasAllIplusIminus;
		this.rpimWithinIplusIminus = rpimWithinIplusIminus;
		this.rpimAllIplusIminus = rpimAllIplusIminus;
		this.fractionalPartialBias = fractionalPartialBias;
		this.nTotalObservations = nTotalObservations;
		this.nTotalUniqueObservations = nTotalUniqueObservations;
		this.meanIoverSigI = meanIoverSigI;
		this.completeness = completeness;
		this.multiplicity = multiplicity;
		this.anomalousCompleteness = anomalousCompleteness;
		this.anomalousMultiplicity = anomalousMultiplicity;
		this.recordTimeStamp = recordTimeStamp;
		this.anomalous = anomalous;
		this.ccHalf = ccHalf;
		this.anomalous = anomalous;
		this.ccHalf = ccHalf;
		this.ccAno = ccAno;
		this.sigAno = sigAno;
		this.ISa = ISa;
		this.completenessSpherical = completenessSpherical;
		this.anomalousCompletenessSpherical = anomalousCompletenessSpherical;
		this.completenessEllipsoidal = completenessEllipsoidal;
		this.anomalousCompletenessEllipsoidal = anomalousCompletenessEllipsoidal;

	}

	public Integer getAutoProcScalingId() {
		return autoProcScalingId;
	}

	public void setAutoProcScalingId(Integer autoProcScalingId) {
		this.autoProcScalingId = autoProcScalingId;
	}
	
	@Override
	public String toWSString(){
		String s= super.toWSString();
		s += ", autoProcScalingId="+this.autoProcScalingId;
		return s;
	}
	
}
