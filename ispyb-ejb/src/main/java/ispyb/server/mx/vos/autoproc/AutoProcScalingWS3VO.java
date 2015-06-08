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
 * AutoProcScaling class for webservices
 * @author BODIN
 *
 */
public class AutoProcScalingWS3VO extends AutoProcScaling3VO{
	private static final long serialVersionUID = -7883075490328211423L;
	private Integer autoProcId;

	public AutoProcScalingWS3VO() {
		super();
	}

	public AutoProcScalingWS3VO(Integer autoProcId) {
		super();
		this.autoProcId = autoProcId;
	}
	
	public AutoProcScalingWS3VO(Integer autoProcScalingId, Integer autoProcId,
			Date recordTimeStamp) {
		super();
		this.autoProcScalingId = autoProcScalingId;
		this.autoProcId = autoProcId;
		this.recordTimeStamp = recordTimeStamp;
	}

	public Integer getAutoProcId() {
		return autoProcId;
	}

	public void setAutoProcId(Integer autoProcId) {
		this.autoProcId = autoProcId;
	}
	
	@Override
	public String toWSString(){
		String s= super.toWSString();
		s += ", autoProcId="+this.autoProcId;
		return s;
	}
	
	
	
}
