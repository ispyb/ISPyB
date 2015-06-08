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
 * AutoProcScalingHasInt class for webservices
 * @author BODIN
 *
 */
public class AutoProcScalingHasIntWS3VO extends AutoProcScalingHasInt3VO{
	private static final long serialVersionUID = -6225028064305858252L;
	
	private Integer autoProcScalingId;
	private Integer autoProcIntegrationId;
	
	public AutoProcScalingHasIntWS3VO() {
		super();
	}
	
	public AutoProcScalingHasIntWS3VO(Integer autoProcScalingId,
			Integer autoProcIntegrationId) {
		super();
		this.autoProcScalingId = autoProcScalingId;
		this.autoProcIntegrationId = autoProcIntegrationId;
	}
	
	public AutoProcScalingHasIntWS3VO(Integer autoProcScalingHasIntId,
			Integer autoProcScalingId, Integer autoProcIntegrationId,
			Date recordTimeStamp) {
		super();
		this.autoProcScalingHasIntId = autoProcScalingHasIntId;
		this.autoProcScalingId = autoProcScalingId;
		this.autoProcIntegrationId = autoProcIntegrationId;
		this.recordTimeStamp = recordTimeStamp;
	}
	
	public Integer getAutoProcScalingId() {
		return autoProcScalingId;
	}
	public void setAutoProcScalingId(Integer autoProcScalingId) {
		this.autoProcScalingId = autoProcScalingId;
	}
	public Integer getAutoProcIntegrationId() {
		return autoProcIntegrationId;
	}
	public void setAutoProcIntegrationId(Integer autoProcIntegrationId) {
		this.autoProcIntegrationId = autoProcIntegrationId;
	}
	
	@Override
	public String toWSString(){
		String s= super.toWSString();
		s += ", autoProcScalingId="+this.autoProcScalingId+", "+
		"autoProcIntegrationId="+this.autoProcIntegrationId;
		return s;
	}
	
	
}
