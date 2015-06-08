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
 * AutoProcStatus class for webservices
 * @author BODIN
 *
 */
public class AutoProcStatusWS3VO extends AutoProcStatus3VO{
	private static final long serialVersionUID = 824768172921463811L;

	private Integer autoProcIntegrationId ;

	
	public AutoProcStatusWS3VO() {
		super();
	}
	
	

	public AutoProcStatusWS3VO(Integer autoProcStatusId,
			Integer autoProcIntegrationId, String step,
			String status, String comments, Date blTimeStamp) {
		super();
		this.autoProcIntegrationId = autoProcIntegrationId;
		this.autoProcStatusId = autoProcStatusId;
		this.step = step;
		this.status = status;
		this.comments = comments;
		this.blTimeStamp = blTimeStamp;
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
		s += ", autoProcIntegrationId="+this.autoProcIntegrationId;
		return s;
	}
	
	
}
