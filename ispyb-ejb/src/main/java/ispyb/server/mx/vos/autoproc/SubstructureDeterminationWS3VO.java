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

public class SubstructureDeterminationWS3VO extends SubstructureDetermination3VO{

	private static final long serialVersionUID = 1L;

	protected Integer phasingAnalysisId;
	
	protected Integer phasingProgramRunId;
	
	protected Integer spaceGroupId;

	public SubstructureDeterminationWS3VO() {
		super();
	}

	
	public SubstructureDeterminationWS3VO(Integer phasingAnalysisId, Integer phasingProgramRunId, Integer spaceGroupId) {
		super();
		this.phasingAnalysisId = phasingAnalysisId;
		this.phasingProgramRunId = phasingProgramRunId;
		this.spaceGroupId = spaceGroupId;
	}


	public SubstructureDeterminationWS3VO(Integer substructureDeterminationId,
			Integer phasingAnalysisId,
			Integer phasingProgramRunId,
			Integer spaceGroupId, String method, Double lowRes, Double highRes, Date recordTimeStamp) {
		super();
		this.substructureDeterminationId = substructureDeterminationId;
		this.phasingAnalysisId = phasingAnalysisId;
		this.phasingProgramRunId = phasingProgramRunId;
		this.spaceGroupId = spaceGroupId;
		this.method = method;
		this.lowRes = lowRes;
		this.highRes = highRes;
		this.recordTimeStamp = recordTimeStamp;
	}

	public Integer getPhasingProgramRunId() {
		return phasingProgramRunId;
	}

	public void setPhasingProgramRunId(Integer phasingProgramRunId) {
		this.phasingProgramRunId = phasingProgramRunId;
	}
	
	public Integer getSpaceGroupId() {
		return spaceGroupId;
	}

	public void setSpaceGroupId(Integer spaceGroupId) {
		this.spaceGroupId = spaceGroupId;
	}


	public Integer getPhasingAnalysisId() {
		return phasingAnalysisId;
	}


	public void setPhasingAnalysisId(Integer phasingAnalysisId) {
		this.phasingAnalysisId = phasingAnalysisId;
	}


	@Override
	public String toWSString(){
		String s= super.toWSString();
		s += ", phasingAnalysisId="+this.phasingAnalysisId+", "+
		", phasingProgramRunId="+this.phasingProgramRunId+", "+
		", spaceGroupId="+this.spaceGroupId;
		return s;
	}
	

}
