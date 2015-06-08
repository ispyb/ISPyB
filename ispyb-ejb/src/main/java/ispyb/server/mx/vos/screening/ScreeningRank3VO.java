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

import ispyb.server.common.vos.ISPyBValueObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * ScreeningRank3 value object mapping table ScreeningRank
 * 
 */
@Entity
@Table(name = "ScreeningRank")
public class ScreeningRank3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(ScreeningRank3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "screeningRankId")
	protected Integer screeningRankId;
	
	@ManyToOne
	@JoinColumn(name = "screeningRankSetId")
	private ScreeningRankSet3VO screeningRankSetVO;
	
	@ManyToOne
	@JoinColumn(name = "screeningId")
	private Screening3VO screeningVO;
	
	@Column(name = "rankValue")
	protected Double rankValue;
	
	@Column(name = "rankInformation")
	protected String rankInformation;
	
	public ScreeningRank3VO(){
		super();
	}
	
	
	
	
	public ScreeningRank3VO(Integer screeningRankId,
			ScreeningRankSet3VO screeningRankSetVO, Screening3VO screeningVO,
			Double rankValue, String rankInformation) {
		super();
		this.screeningRankId = screeningRankId;
		this.screeningRankSetVO = screeningRankSetVO;
		this.screeningVO = screeningVO;
		this.rankValue = rankValue;
		this.rankInformation = rankInformation;
	}
	
	public ScreeningRank3VO(ScreeningRank3VO vo) {
		super();
		this.screeningRankId = vo.getScreeningRankId();
		this.screeningRankSetVO = vo.getScreeningRankSetVO();
		this.screeningVO = vo.getScreeningVO();
		this.rankValue = vo.getRankValue();
		this.rankInformation = vo.getRankInformation();
	}

	public void fillVOFromWS(ScreeningRankWS3VO vo) {
		this.screeningRankId = vo.getScreeningRankId();
		this.screeningRankSetVO = null;
		this.screeningVO = null;
		this.rankValue = vo.getRankValue();
		this.rankInformation = vo.getRankInformation();
	}




	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getScreeningRankId() {
		return screeningRankId;
	}

	/**
	 * @param pk The pk to set.
	 */
	public void setScreeningRankId(Integer screeningRankId) {
		this.screeningRankId = screeningRankId;
	}

	

	

	public ScreeningRankSet3VO getScreeningRankSetVO() {
		return screeningRankSetVO;
	}




	public void setScreeningRankSetVO(ScreeningRankSet3VO screeningRankSetVO) {
		this.screeningRankSetVO = screeningRankSetVO;
	}




	public Screening3VO getScreeningVO() {
		return screeningVO;
	}




	public void setScreeningVO(Screening3VO screeningVO) {
		this.screeningVO = screeningVO;
	}



	public Integer getScreeningRankSetVOId() {
		return screeningRankSetVO == null ? null : screeningRankSetVO.getScreeningRankSetId();
	}

	public Integer getScreeningVOId() {
		return screeningVO == null ? null : screeningVO.getScreeningId();
	}

	public Double getRankValue() {
		return rankValue;
	}

	public void setRankValue(Double rankValue) {
		this.rankValue = rankValue;
	}

	public String getRankInformation() {
		return rankInformation;
	}

	public void setRankInformation(String rankInformation) {
		this.rankInformation = rankInformation;
	}

	/**
	 * Checks the values of this value object for correctness and
	 * completeness. Should be done before persisting the data in the DB.
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @throws Exception if the data of the value object is not correct
	 */
	public void checkValues(boolean create) throws Exception {
		//TODO
	}
	
	public String toWSString(){
		String s = "screeningRankId="+this.screeningRankId +", "+
		"rankValue="+this.rankValue+", "+
		"rankInformation="+this.rankInformation;
		
		return s;
	}
}

