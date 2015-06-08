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
 * Session class used for webservices
 * @author BODIN
 *
 */
public class PositionWS3VO extends Position3VO {

	private static final long serialVersionUID = 4267300167791508231L;
	
	private Integer relativePositionId;

	public PositionWS3VO() {
		super();
	}
	
	public PositionWS3VO(Position3VO vo) {
		super(vo);
	}


	public Integer getRelativePositionId() {
		return relativePositionId;
	}

	public void setRelativePositionId(Integer relativePositionId) {
		this.relativePositionId = relativePositionId;
	}
	

	@Override
	public String toWSString(){
		String s= super.toWSString();
		s += ", relativePositionId="+this.relativePositionId;
		return s;
	}
}
