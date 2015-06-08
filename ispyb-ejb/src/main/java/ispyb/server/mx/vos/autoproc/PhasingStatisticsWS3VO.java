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

public class PhasingStatisticsWS3VO extends PhasingStatistics3VO{

	private static final long serialVersionUID = 1L;

	protected Integer phasingHasScalingId1;
	protected Integer phasingHasScalingId2;

	public PhasingStatisticsWS3VO() {
		super();
	}
	
	public PhasingStatisticsWS3VO(Integer phasingHasScalingId1,
			Integer phasingHasScalingId2) {
		super();
		this.phasingHasScalingId1 = phasingHasScalingId1;
		this.phasingHasScalingId2 = phasingHasScalingId2;
	}

	public PhasingStatisticsWS3VO(Integer phasingStatisticsId,
			Integer phasingHasScalingId1,
			Integer phasingHasScalingId2, Integer numberOfBins,
			Integer binNumber, Double lowRes, Double highRes, String metric,
			Double statisticsValue, Integer nReflections,  Date recordTimeStamp) {
		super();
		this.phasingStatisticsId = phasingStatisticsId;
		this.phasingHasScalingId1 = phasingHasScalingId1;
		this.phasingHasScalingId2 = phasingHasScalingId2;
		this.numberOfBins = numberOfBins;
		this.binNumber = binNumber;
		this.lowRes = lowRes;
		this.highRes = highRes;
		this.metric = metric;
		this.statisticsValue = statisticsValue;
		this.nReflections = nReflections;
		this.recordTimeStamp = recordTimeStamp;
	}

	
	public Integer getPhasingHasScalingId1() {
		return phasingHasScalingId1;
	}

	public void setPhasingHasScalingId1(Integer phasingHasScalingId1) {
		this.phasingHasScalingId1 = phasingHasScalingId1;
	}

	public Integer getPhasingHasScalingId2() {
		return phasingHasScalingId2;
	}

	public void setPhasingHasScalingId2(Integer phasingHasScalingId2) {
		this.phasingHasScalingId2 = phasingHasScalingId2;
	}

	@Override
	public String toWSString(){
		String s= super.toWSString();
		s += ", phasingHasScalingId1="+this.phasingHasScalingId1+", "+
		", phasingHasScalingId2="+this.phasingHasScalingId2;
		return s;
	}
	

}
