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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import ispyb.server.common.vos.ISPyBValueObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * ScreeningRankSet3 value object mapping table ScreeningRankSet
 * 
 */
@Entity
@Table(name = "ScreeningRankSet")
public class ScreeningRankSet3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(ScreeningRankSet3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "screeningRankSetId")
	private Integer screeningRankSetId;
	
	@Column(name = "rankEngine")
	private String rankEngine;
	
	@Column(name = "rankingProjectFileName")
	private String rankingProjectFileName;
	
	@Column(name = "rankingSummaryFileName")
	private String rankingSummaryFileName;
	
	@OneToMany
	@JoinColumn(name = "screeningRankSetId")
	private Set<ScreeningRank3VO> screeningRankVOs;
	
	public ScreeningRankSet3VO(){
		super();
	}

	public ScreeningRankSet3VO(Integer screeningRankSetId, String rankEngine,
			String rankingProjectFileName, String rankingSummaryFileName) {
		super();
		this.screeningRankSetId = screeningRankSetId;
		this.rankEngine = rankEngine;
		this.rankingProjectFileName = rankingProjectFileName;
		this.rankingSummaryFileName = rankingSummaryFileName;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getScreeningRankSetId() {
		return screeningRankSetId;
	}

	/**
	 * @param pk The pk to set.
	 */
	public void setScreeningRankSetId(Integer screeningRankSetId) {
		this.screeningRankSetId = screeningRankSetId;
	}

	

	public String getRankEngine() {
		return rankEngine;
	}

	public void setRankEngine(String rankEngine) {
		this.rankEngine = rankEngine;
	}

	public String getRankingProjectFileName() {
		return rankingProjectFileName;
	}

	public void setRankingProjectFileName(String rankingProjectFileName) {
		this.rankingProjectFileName = rankingProjectFileName;
	}

	public String getRankingSummaryFileName() {
		return rankingSummaryFileName;
	}

	public void setRankingSummaryFileName(String rankingSummaryFileName) {
		this.rankingSummaryFileName = rankingSummaryFileName;
	}

	
	public Set<ScreeningRank3VO> getScreeningRankVOs() {
		return screeningRankVOs;
	}

	public void setScreeningRankVOs(Set<ScreeningRank3VO> screeningRankVOs) {
		this.screeningRankVOs = screeningRankVOs;
	}

	public ScreeningRank3VO[] getScreeningRanksTab(){
		return this.screeningRankVOs == null ? null : screeningRankVOs.toArray(new ScreeningRank3VO[this.screeningRankVOs.size()]);
	}
	
	public ArrayList<ScreeningRank3VO> getScreeningRankSetsList(){
		return this.screeningRankVOs == null ? null : new ArrayList<ScreeningRank3VO>(Arrays.asList(getScreeningRanksTab()));
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
		String s = "screeningRankSetId="+this.screeningRankSetId +", "+
		"rankEngine="+this.rankEngine+", "+
		"rankingProjectFileName="+this.rankingProjectFileName+", "+
		"rankingSummaryFileName="+this.rankingSummaryFileName;
		
		return s;
	}
	
}
