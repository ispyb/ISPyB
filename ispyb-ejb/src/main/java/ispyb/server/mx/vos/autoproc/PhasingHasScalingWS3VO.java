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

public class PhasingHasScalingWS3VO extends PhasingHasScaling3VO{

	private static final long serialVersionUID = 1L;

	protected Integer phasingAnalysisId;
	protected Integer autoProcScalingId;

	public PhasingHasScalingWS3VO() {
		super();
	}
	
	public PhasingHasScalingWS3VO(Integer phasingAnalysisId, Integer autoProcScalingId) {
		super();
		this.phasingAnalysisId = phasingAnalysisId;
		this.autoProcScalingId = autoProcScalingId;
	}

	public PhasingHasScalingWS3VO(Integer phasingHasScalingId,
			Integer phasingAnalysisId, Integer autoProcScalingId,
			Integer datasetNumber, Date recordTimeStamp) {
		super();
		this.phasingHasScalingId = phasingHasScalingId;
		this.phasingAnalysisId = phasingAnalysisId;
		this.autoProcScalingId = autoProcScalingId;
		this.datasetNumber = datasetNumber;
		this.recordTimeStamp = recordTimeStamp;
	}

	

	public Integer getPhasingAnalysisId() {
		return phasingAnalysisId;
	}

	public void setPhasingAnalysisId(Integer phasingAnalysisId) {
		this.phasingAnalysisId = phasingAnalysisId;
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
		s += ", phasingAnalysisId="+this.phasingAnalysisId+", "+
		"autoProcScalingId="+this.autoProcScalingId;
		return s;
	}
	

}
