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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.annotations.OrderBy;

/**
 * ScreeningStrategy3 value object mapping table ScreeningStrategy
 * 
 */
@Entity
@Table(name = "ScreeningStrategy")
public class ScreeningStrategy3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(ScreeningStrategy3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "screeningStrategyId")
	protected Integer screeningStrategyId;
	
	@ManyToOne
	@JoinColumn(name = "screeningOutputId")
	private ScreeningOutput3VO screeningOutputVO;
	
	@Column(name = "phiStart")
	protected Double phiStart;
	
	@Column(name = "phiEnd")
	protected Double phiEnd;
	
	@Column(name = "rotation")
	protected Double rotation;
	
	@Column(name = "exposureTime")
	protected Double exposureTime;
	
	@Column(name = "resolution")
	protected Double resolution;
	
	@Column(name = "completeness")
	protected Double completeness;
	
	@Column(name = "multiplicity")
	protected Double multiplicity;
	
	@Column(name = "anomalous")
	protected Byte anomalous;
	
	@Column(name = "program")
	protected String program;
	
	@Column(name = "rankingResolution")
	protected Double rankingResolution;
	
	@Column(name = "transmission")
	protected Double transmission;
	
	@OneToMany
	@JoinColumn(name = "screeningStrategyId")
	@OrderBy(clause = "screeningStrategyWedgeId")
	private Set<ScreeningStrategyWedge3VO> screeningStrategyWedgeVOs;
	
	public ScreeningStrategy3VO(){
		super();
	}
	   
	
	public ScreeningStrategy3VO(Integer screeningStrategyId,
			ScreeningOutput3VO screeningOutputVO, Double phiStart,
			Double phiEnd, Double rotation, Double exposureTime,
			Double resolution, Double completeness, Double multiplicity,
			Byte anomalous, String program, Double rankingResolution,
			Double transmission) {
		super();
		this.screeningStrategyId = screeningStrategyId;
		this.screeningOutputVO = screeningOutputVO;
		this.phiStart = phiStart;
		this.phiEnd = phiEnd;
		this.rotation = rotation;
		this.exposureTime = exposureTime;
		this.resolution = resolution;
		this.completeness = completeness;
		this.multiplicity = multiplicity;
		this.anomalous = anomalous;
		this.program = program;
		this.rankingResolution = rankingResolution;
		this.transmission = transmission;
	}

	public ScreeningStrategy3VO(ScreeningStrategy3VO vo) {
		super();
		this.screeningStrategyId = vo.getScreeningStrategyId();
		this.screeningOutputVO = vo.getScreeningOutputVO();
		this.phiStart = vo.getPhiStart();
		this.phiEnd = vo.getPhiEnd();
		this.rotation = vo.getRotation();
		this.exposureTime = vo.getExposureTime();
		this.resolution = vo.getResolution();
		this.completeness = vo.getCompleteness();
		this.multiplicity = vo.getMultiplicity();
		this.anomalous = vo.getAnomalous();
		this.program = vo.getProgram();
		this.rankingResolution = vo.getRankingResolution();
		this.transmission = vo.getTransmission();
	}

	public void fillVOFromWS(ScreeningStrategy3VO vo) {
		this.screeningStrategyId = vo.getScreeningStrategyId();
		this.screeningOutputVO = null;
		this.phiStart = vo.getPhiStart();
		this.phiEnd = vo.getPhiEnd();
		this.rotation = vo.getRotation();
		this.exposureTime = vo.getExposureTime();
		this.resolution = vo.getResolution();
		this.completeness = vo.getCompleteness();
		this.multiplicity = vo.getMultiplicity();
		this.anomalous = vo.getAnomalous();
		this.program = vo.getProgram();
		this.rankingResolution = vo.getRankingResolution();
		this.transmission = vo.getTransmission();
	}


	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getScreeningStrategyId() {
		return screeningStrategyId;
	}

	/**
	 * @param pk The pk to set.
	 */
	public void setScreeningStrategyId(Integer screeningStrategyId) {
		this.screeningStrategyId = screeningStrategyId;
	}

	

	public ScreeningOutput3VO getScreeningOutputVO() {
		return screeningOutputVO;
	}




	public void setScreeningOutputVO(ScreeningOutput3VO screeningOutputVO) {
		this.screeningOutputVO = screeningOutputVO;
	}


	public Integer getScreeningOutputVOId() {
		return screeningOutputVO == null ? null : screeningOutputVO.getScreeningOutputId();
	}

	public Double getPhiStart() {
		return phiStart;
	}

	public void setPhiStart(Double phiStart) {
		this.phiStart = phiStart;
	}

	public Double getPhiEnd() {
		return phiEnd;
	}

	public void setPhiEnd(Double phiEnd) {
		this.phiEnd = phiEnd;
	}

	public Double getRotation() {
		return rotation;
	}

	public void setRotation(Double rotation) {
		this.rotation = rotation;
	}

	public Double getExposureTime() {
		return exposureTime;
	}

	public void setExposureTime(Double exposureTime) {
		this.exposureTime = exposureTime;
	}

	public Double getResolution() {
		return resolution;
	}

	public void setResolution(Double resolution) {
		this.resolution = resolution;
	}

	public Double getCompleteness() {
		return completeness;
	}

	public void setCompleteness(Double completeness) {
		this.completeness = completeness;
	}

	public Double getMultiplicity() {
		return multiplicity;
	}

	public void setMultiplicity(Double multiplicity) {
		this.multiplicity = multiplicity;
	}

	public Byte getAnomalous() {
		return anomalous;
	}

	public void setAnomalous(Byte anomalous) {
		this.anomalous = anomalous;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public Double getRankingResolution() {
		return rankingResolution;
	}

	public void setRankingResolution(Double rankingResolution) {
		this.rankingResolution = rankingResolution;
	}

	public Double getTransmission() {
		return transmission;
	}

	public void setTransmission(Double transmission) {
		this.transmission = transmission;
	}

	public Set<ScreeningStrategyWedge3VO> getScreeningStrategyWedgeVOs() {
		return screeningStrategyWedgeVOs;
	}

	public void setScreeningStrategyWedgeVOs(
			Set<ScreeningStrategyWedge3VO> screeningStrategyWedgeVOs) {
		this.screeningStrategyWedgeVOs = screeningStrategyWedgeVOs;
	}

	public ScreeningStrategyWedge3VO[] getScreeningStrategyWedgesTab(){
		return this.screeningStrategyWedgeVOs == null ? null : screeningStrategyWedgeVOs.toArray(new ScreeningStrategyWedge3VO[this.screeningStrategyWedgeVOs.size()]);
	}
	
	public ArrayList<ScreeningStrategyWedge3VO> getScreeningStrategyWedgesList(){
		return this.screeningStrategyWedgeVOs == null ? null : new ArrayList<ScreeningStrategyWedge3VO>(Arrays.asList(getScreeningStrategyWedgesTab()));
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
		String s = "screeningStrategyId="+this.screeningStrategyId +", "+
		"phiStart="+this.phiStart+", "+
		"phiEnd="+this.phiEnd+", "+
		"rotation="+this.rotation+", "+
		"exposureTime="+this.exposureTime+", "+
		"resolution="+this.resolution+", "+
		"completeness="+this.completeness+", "+
		"multiplicity="+this.multiplicity+", "+
		"anomalous="+this.anomalous+", "+
		"program="+this.program+", "+
		"rankingResolution="+this.rankingResolution+", "+
		"transmission="+this.transmission;
		
		return s;
	}
	
}

