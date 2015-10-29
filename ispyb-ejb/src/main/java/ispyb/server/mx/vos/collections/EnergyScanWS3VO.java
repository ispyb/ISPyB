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
package ispyb.server.mx.vos.collections;

/**
 * EnergyScan class used for webservices
 * @author BODIN
 *
 */
public class EnergyScanWS3VO extends EnergyScan3VO{

	private static final long serialVersionUID = 7080709203057256940L;
	
	private Integer sessionId;
	
	private Integer blSampleId;

	public EnergyScanWS3VO() {
		super();
	}

	public EnergyScanWS3VO(Integer sessionId, Integer blSampleId) {
		super();
		this.sessionId = sessionId;
		this.blSampleId = blSampleId;
	}
	
	public EnergyScanWS3VO(EnergyScan3VO vo) {
		super(vo);
	}

	public Integer getSessionId() {
		return sessionId;
	}

	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}

	public Integer getBlSampleId() {
		return blSampleId;
	}

	public void setBlSampleId(Integer blSampleId) {
		this.blSampleId = blSampleId;
	}
	
	@Override
	public String toWSString(){
		String s= super.toWSString();
		s += ", sessionId="+this.sessionId;
		return s;
	}
	
}
