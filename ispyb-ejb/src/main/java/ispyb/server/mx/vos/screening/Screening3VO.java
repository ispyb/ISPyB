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
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import ispyb.server.common.vos.ISPyBValueObject;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
import ispyb.server.mx.vos.sample.DiffractionPlan3VO;

/**
 * Screening3 value object mapping table Screening
 * 
 */
@Entity
@Table(name = "Screening")
public class Screening3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(Screening3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "screeningId")
	protected Integer screeningId;

	@ManyToOne
	@JoinColumn(name = "dataCollectionGroupId")
	protected DataCollectionGroup3VO dataCollectionGroupVO;
	
	@ManyToOne
	@JoinColumn(name = "diffractionPlanId")
	private DiffractionPlan3VO diffractionPlanVO;

	@Column(name = "bltimeStamp")
	protected Date timeStamp;

	@Column(name = "programVersion")
	protected String programVersion;

	@Column(name = "comments")
	protected String comments;

	@Column(name = "shortComments")
	protected String shortComments;
	
	@Column(name = "xmlSampleInformation")
	protected String xmlSampleInformation;

	@Fetch(value = FetchMode.SELECT)
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "screeningId")
	private Set<ScreeningRank3VO> screeningRankVOs;

	@Fetch(value = FetchMode.SELECT)
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "screeningId")
	private Set<ScreeningOutput3VO> screeningOutputVOs;


	public Screening3VO() {
		super();
	}

	
	public Screening3VO(Integer screeningId,
			DataCollectionGroup3VO dataCollectionGroupVO,
			DiffractionPlan3VO diffractionPlanVO, Date timeStamp,
			String programVersion, String comments, String shortComments,
			String xmlSampleInformation) {
		super();
		this.screeningId = screeningId;
		this.dataCollectionGroupVO = dataCollectionGroupVO;
		this.diffractionPlanVO = diffractionPlanVO;
		this.timeStamp = timeStamp;
		this.programVersion = programVersion;
		this.comments = comments;
		this.shortComments = shortComments;
		this.xmlSampleInformation = xmlSampleInformation;
	}


	public Screening3VO(Screening3VO vo) {
		super();
		this.screeningId = vo.getScreeningId();
		this.dataCollectionGroupVO = vo.getDataCollectionGroupVO();
		this.diffractionPlanVO = vo.getDiffractionPlanVO();
		this.timeStamp = vo.getTimeStamp();
		this.programVersion = vo.getProgramVersion();
		this.comments = vo.getComments();
		this.shortComments = vo.getShortComments();
		this.xmlSampleInformation = vo.getXmlSampleInformation();
	}

	public void fillVOFromWS(ScreeningWS3VO vo) {
		this.screeningId = vo.getScreeningId();
		this.dataCollectionGroupVO = null;
		this.diffractionPlanVO =null;
		this.timeStamp = vo.getTimeStamp();
		this.programVersion = vo.getProgramVersion();
		this.comments = vo.getComments();
		this.shortComments = vo.getShortComments();
		this.xmlSampleInformation = vo.getXmlSampleInformation();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getScreeningId() {
		return screeningId;
	}

	/**
	 * @param pk
	 *            The pk to set.
	 */
	public void setScreeningId(Integer screeningId) {
		this.screeningId = screeningId;
	}


	public DataCollectionGroup3VO getDataCollectionGroupVO() {
		return dataCollectionGroupVO;
	}


	public void setDataCollectionGroupVO(DataCollectionGroup3VO dataCollectionGroupVO) {
		this.dataCollectionGroupVO = dataCollectionGroupVO;
	}


	public Integer getDataCollectionGroupVOId() {
		return dataCollectionGroupVO == null ? null : dataCollectionGroupVO.getDataCollectionGroupId();
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getProgramVersion() {
		return programVersion;
	}

	public void setProgramVersion(String programVersion) {
		this.programVersion = programVersion;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getShortComments() {
		return shortComments;
	}

	public void setShortComments(String shortComments) {
		this.shortComments = shortComments;
	}

	public DiffractionPlan3VO getDiffractionPlanVO() {
		return diffractionPlanVO;
	}

	public void setDiffractionPlanVO(DiffractionPlan3VO diffractionPlanVO) {
		this.diffractionPlanVO = diffractionPlanVO;
	}
	
	public Integer getDiffractionPlanVOId() {
		return diffractionPlanVO == null ?  null : diffractionPlanVO.getDiffractionPlanId();
	}

	public String getXmlSampleInformation() {
		return xmlSampleInformation;
	}

	public void setXmlSampleInformation(String xmlSampleInformation) {
		this.xmlSampleInformation = xmlSampleInformation;
	}

	public Set<ScreeningRank3VO> getScreeningRankVOs() {
		return screeningRankVOs;
	}

	public void setScreeningRankVOs(Set<ScreeningRank3VO> screeningRankVOs) {
		this.screeningRankVOs = screeningRankVOs;
	}

	public Set<ScreeningOutput3VO> getScreeningOutputVOs() {
		return screeningOutputVOs;
	}

	public void setScreeningOutputVOs(Set<ScreeningOutput3VO> screeningOutputVOs) {
		this.screeningOutputVOs = screeningOutputVOs;
	}


	public ScreeningRank3VO[] getScreeningRanksTab() {
		return this.screeningRankVOs == null ? null : screeningRankVOs
				.toArray(new ScreeningRank3VO[this.screeningRankVOs.size()]);
	}

	public ArrayList<ScreeningRank3VO> getScreeningRanksList() {
		return this.screeningRankVOs == null ? null : new ArrayList<ScreeningRank3VO>(
				Arrays.asList(getScreeningRanksTab()));
	}

	public ScreeningOutput3VO[] getScreeningOutputsTab() {
		return this.screeningOutputVOs == null ? null : screeningOutputVOs
				.toArray(new ScreeningOutput3VO[this.screeningOutputVOs.size()]);
	}

	public ArrayList<ScreeningOutput3VO> getScreeningOutputsList() {
		return this.screeningOutputVOs == null ? null : new ArrayList<ScreeningOutput3VO>(
				Arrays.asList(getScreeningOutputsTab()));
	}

	/**
	 * Checks the values of this value object for correctness and completeness. Should be done before persisting the
	 * data in the DB.
	 * 
	 * @param create
	 *            should be true if the value object is just being created in the DB, this avoids some checks like
	 *            testing the primary key
	 * @throws Exception
	 *             if the data of the value object is not correct
	 */
	@Override
	public void checkValues(boolean create) throws Exception {
		// TODO
	}
	
	public String toWSString(){
		String s = "screeningId="+this.screeningId +", "+
		"timeStamp="+this.timeStamp+", "+
		"programVersion="+this.programVersion+", "+
		"comments="+this.comments+", "+
		"shortComments="+this.shortComments+", "+
		"xmlSampleInformation="+this.xmlSampleInformation;
		
		return s;
	}
	
}
