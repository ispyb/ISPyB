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

package ispyb.server.mx.vos.screening;

/**
 * ScreeningRank class for webservices
 * @author BODIN
 *
 */
public class ScreeningRankWS3VO extends ScreeningRank3VO{
	private static final long serialVersionUID = -1772029029241675999L;
	
	private Integer screeningRankSetId;
	private Integer screeningId;
	
	public ScreeningRankWS3VO() {
		super();
	}
	
	public ScreeningRankWS3VO(ScreeningRank3VO vo) {
		super(vo);
	}
	
	public ScreeningRankWS3VO(Integer screeningRankId,
			Integer screeningRankSetId, Integer screeningId, Double rankValue,
			String rankInformation) {
		super();
		this.screeningRankId = screeningRankId;
		this.screeningRankSetId = screeningRankSetId;
		this.screeningId = screeningId;
		this.rankValue = rankValue;
		this.rankInformation = rankInformation;
	}

	public Integer getScreeningRankSetId() {
		return screeningRankSetId;
	}

	public void setScreeningRankSetId(Integer screeningRankSetId) {
		this.screeningRankSetId = screeningRankSetId;
	}

	public Integer getScreeningId() {
		return screeningId;
	}

	public void setScreeningId(Integer screeningId) {
		this.screeningId = screeningId;
	}
	
	@Override
	public String toWSString(){
		String s= super.toWSString();
		s += ", screeningRankSetId="+this.screeningRankSetId+", "+
		"screeningId="+this.screeningId;
		return s;
	}
	
	
}
