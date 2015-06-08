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
 * AutoProc class for webservices
 * @author BODIN
 *
 */
public class AutoProcWS3VO extends AutoProc3VO{
	private static final long serialVersionUID = 7970650800043764836L;
	
	private Integer autoProcProgramId;

	public AutoProcWS3VO() {
		super();
	}

	public AutoProcWS3VO(Integer autoProcProgramId) {
		super();
		this.autoProcProgramId = autoProcProgramId;
	}
	
	public AutoProcWS3VO(Integer autoProcId,
			Integer autoProcProgramId, String spaceGroup,
			Float refinedCellA, Float refinedCellB, Float refinedCellC,
			Float refinedCellAlpha, Float refinedCellBeta,
			Float refinedCellGamma, Date recordTimeStamp) {
		super();
		this.autoProcId = autoProcId;
		this.autoProcProgramId = autoProcProgramId;
		this.spaceGroup = spaceGroup;
		this.refinedCellA = refinedCellA;
		this.refinedCellB = refinedCellB;
		this.refinedCellC = refinedCellC;
		this.refinedCellAlpha = refinedCellAlpha;
		this.refinedCellBeta = refinedCellBeta;
		this.refinedCellGamma = refinedCellGamma;
		this.recordTimeStamp = recordTimeStamp;
	}

	public Integer getAutoProcProgramId() {
		return this.autoProcProgramId;
	}

	public void setAutoProcProgramId(Integer autoProcProgramId) {
		this.autoProcProgramId = autoProcProgramId;
	}
	
	@Override
	public String toWSString(){
		String s= super.toWSString();
		s += ", autoProcProgramId="+this.autoProcProgramId;
		return s;
	}
	
	
	
}
