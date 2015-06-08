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

import java.util.ArrayList;
import java.util.Arrays;
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

/**
 * ScreeningOutput3 value object mapping table ScreeningOutput
 * 
 */
@Entity
@Table(name = "ScreeningOutput")
public class ScreeningOutput3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(ScreeningOutput3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "screeningOutputId")
	protected Integer screeningOutputId;

	@ManyToOne
	@JoinColumn(name = "screeningId")
	private Screening3VO screeningVO;

	@Column(name = "statusDescription")
	protected String statusDescription;

	@Column(name = "rejectedReflections")
	protected Integer rejectedReflections;

	@Column(name = "resolutionObtained")
	protected Double resolutionObtained;

	@Column(name = "spotDeviationR")
	protected Double spotDeviationR;

	@Column(name = "spotDeviationTheta")
	protected Double spotDeviationTheta;

	@Column(name = "beamShiftX")
	protected Double beamShiftX;

	@Column(name = "beamShiftY")
	protected Double beamShiftY;

	@Column(name = "numSpotsFound")
	protected Integer numSpotsFound;

	@Column(name = "numSpotsUsed")
	protected Integer numSpotsUsed;

	@Column(name = "numSpotsRejected")
	protected Integer numSpotsRejected;

	@Column(name = "mosaicity")
	protected Double mosaicity;

	@Column(name = "ioverSigma")
	protected Double ioverSigma;

	@Column(name = "diffractionRings")
	protected Byte diffractionRings;

	@Column(name = "strategySuccess")
	protected Byte strategySuccess;
	
	@Column(name = "indexingSuccess")
	protected Byte indexingSuccess;

	@Column(name = "mosaicityEstimated")
	protected Byte mosaicityEstimated;
	
	@Column(name = "rankingResolution")
	protected Double rankingResolution;
	
	@Column(name = "program")
	protected String program;
	
	@Column(name = "doseTotal")
	protected Double doseTotal;
	
	@Column(name = "totalExposureTime")
	protected Double totalExposureTime;
	
	@Column(name = "totalRotationRange")
	protected Double totalRotationRange;
	
	@Column(name = "totalNumberOfImages")
	protected Integer totalNumberOfImages;
	
	@Column(name = "rFriedel")
	protected Double rFriedel;

	@Fetch(value = FetchMode.SELECT)
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "screeningOutputId")
	private Set<ScreeningStrategy3VO> screeningStrategyVOs;

	@Fetch(value = FetchMode.SELECT)
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "screeningOutputId")
	private Set<ScreeningOutputLattice3VO> screeningOutputLatticeVOs;

	public ScreeningOutput3VO() {
		super();
	}
	
	public ScreeningOutput3VO(Integer screeningOutputId,
			Screening3VO screeningVO, String statusDescription,
			Integer rejectedReflections, Double resolutionObtained,
			Double spotDeviationR, Double spotDeviationTheta,
			Double beamShiftX, Double beamShiftY, Integer numSpotsFound,
			Integer numSpotsUsed, Integer numSpotsRejected, Double mosaicity,
			Double ioverSigma, Byte diffractionRings, Byte strategySuccess,
			Byte indexingSuccess, Byte mosaicityEstimated,
			Double rankingResolution, String program, Double doseTotal,
			Double totalExposureTime, Double totalRotationRange,
			Integer totalNumberOfImages, Double rFriedel) {
		super();
		this.screeningOutputId = screeningOutputId;
		this.screeningVO = screeningVO;
		this.statusDescription = statusDescription;
		this.rejectedReflections = rejectedReflections;
		this.resolutionObtained = resolutionObtained;
		this.spotDeviationR = spotDeviationR;
		this.spotDeviationTheta = spotDeviationTheta;
		this.beamShiftX = beamShiftX;
		this.beamShiftY = beamShiftY;
		this.numSpotsFound = numSpotsFound;
		this.numSpotsUsed = numSpotsUsed;
		this.numSpotsRejected = numSpotsRejected;
		this.mosaicity = mosaicity;
		this.ioverSigma = ioverSigma;
		this.diffractionRings = diffractionRings;
		this.strategySuccess = strategySuccess;
		this.indexingSuccess = indexingSuccess;
		this.mosaicityEstimated = mosaicityEstimated;
		this.rankingResolution = rankingResolution;
		this.program = program;
		this.doseTotal = doseTotal;
		this.totalExposureTime = totalExposureTime;
		this.totalRotationRange = totalRotationRange;
		this.totalNumberOfImages = totalNumberOfImages;
		this.rFriedel = rFriedel;
	}





	public ScreeningOutput3VO(ScreeningOutput3VO vo) {
		super();
		this.screeningOutputId = vo.getScreeningOutputId();
		this.screeningVO = vo.getScreeningVO();
		this.statusDescription = vo.getStatusDescription();
		this.rejectedReflections = vo.getRejectedReflections();
		this.resolutionObtained = vo.getResolutionObtained();
		this.spotDeviationR = vo.getSpotDeviationR();
		this.spotDeviationTheta = vo.getSpotDeviationTheta();
		this.beamShiftX = vo.getBeamShiftX();
		this.beamShiftY = vo.getBeamShiftY();
		this.numSpotsFound = vo.getNumSpotsFound();
		this.numSpotsUsed = vo.getNumSpotsUsed();
		this.numSpotsRejected = vo.getNumSpotsRejected();
		this.mosaicity = vo.getMosaicity();
		this.ioverSigma = vo.getIoverSigma();
		this.diffractionRings = vo.getDiffractionRings();
		this.strategySuccess = vo.getStrategySuccess();
		this.indexingSuccess = vo.getIndexingSuccess();
		this.mosaicityEstimated = vo.getMosaicityEstimated();
		this.rankingResolution = vo.getRankingResolution();
		this.program = vo.getProgram();
		this.doseTotal = vo.getDoseTotal();
		this.totalExposureTime = vo.getTotalExposureTime();
		this.totalRotationRange = vo.getTotalRotationRange();
		this.totalNumberOfImages = vo.getTotalNumberOfImages();
		this.rFriedel = vo.getrFriedel();
	}

	public void fillVOFromWS(ScreeningOutputWS3VO vo) {
		this.screeningOutputId = vo.getScreeningOutputId();
		this.screeningVO = null;
		this.statusDescription = vo.getStatusDescription();
		this.rejectedReflections = vo.getRejectedReflections();
		this.resolutionObtained = vo.getResolutionObtained();
		this.spotDeviationR = vo.getSpotDeviationR();
		this.spotDeviationTheta = vo.getSpotDeviationTheta();
		this.beamShiftX = vo.getBeamShiftX();
		this.beamShiftY = vo.getBeamShiftY();
		this.numSpotsFound = vo.getNumSpotsFound();
		this.numSpotsUsed = vo.getNumSpotsUsed();
		this.numSpotsRejected = vo.getNumSpotsRejected();
		this.mosaicity = vo.getMosaicity();
		this.ioverSigma = vo.getIoverSigma();
		this.diffractionRings = vo.getDiffractionRings();
		this.strategySuccess = vo.getStrategySuccess();
		this.indexingSuccess = vo.getIndexingSuccess();
		this.mosaicityEstimated = vo.getMosaicityEstimated();
		this.rankingResolution = vo.getRankingResolution();
		this.program = vo.getProgram();
		this.doseTotal = vo.getDoseTotal();
		this.totalExposureTime = vo.getTotalExposureTime();
		this.totalRotationRange = vo.getTotalRotationRange();
		this.totalNumberOfImages = vo.getTotalNumberOfImages();
		this.rFriedel = vo.getrFriedel();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getScreeningOutputId() {
		return screeningOutputId;
	}

	/**
	 * @param pk
	 *            The pk to set.
	 */
	public void setScreeningOutputId(Integer screeningOutputId) {
		this.screeningOutputId = screeningOutputId;
	}

	public Screening3VO getScreeningVO() {
		return screeningVO;
	}

	public void setScreeningVO(Screening3VO screeningVO) {
		this.screeningVO = screeningVO;
	}

	public Integer getScreeningVOId() {
		return screeningVO == null ? null : screeningVO.getScreeningId();
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	public Integer getRejectedReflections() {
		return rejectedReflections;
	}

	public void setRejectedReflections(Integer rejectedReflections) {
		this.rejectedReflections = rejectedReflections;
	}

	public Double getResolutionObtained() {
		return resolutionObtained;
	}

	public void setResolutionObtained(Double resolutionObtained) {
		this.resolutionObtained = resolutionObtained;
	}

	public Double getSpotDeviationR() {
		return spotDeviationR;
	}

	public void setSpotDeviationR(Double spotDeviationR) {
		this.spotDeviationR = spotDeviationR;
	}

	public Double getSpotDeviationTheta() {
		return spotDeviationTheta;
	}

	public void setSpotDeviationTheta(Double spotDeviationTheta) {
		this.spotDeviationTheta = spotDeviationTheta;
	}

	public Double getBeamShiftX() {
		return beamShiftX;
	}

	public void setBeamShiftX(Double beamShiftX) {
		this.beamShiftX = beamShiftX;
	}

	public Double getBeamShiftY() {
		return beamShiftY;
	}

	public void setBeamShiftY(Double beamShiftY) {
		this.beamShiftY = beamShiftY;
	}

	public Integer getNumSpotsFound() {
		return numSpotsFound;
	}

	public void setNumSpotsFound(Integer numSpotsFound) {
		this.numSpotsFound = numSpotsFound;
	}

	public Integer getNumSpotsUsed() {
		return numSpotsUsed;
	}

	public void setNumSpotsUsed(Integer numSpotsUsed) {
		this.numSpotsUsed = numSpotsUsed;
	}

	public Integer getNumSpotsRejected() {
		return numSpotsRejected;
	}

	public void setNumSpotsRejected(Integer numSpotsRejected) {
		this.numSpotsRejected = numSpotsRejected;
	}

	public Double getMosaicity() {
		return mosaicity;
	}

	public void setMosaicity(Double mosaicity) {
		this.mosaicity = mosaicity;
	}

	public Double getIoverSigma() {
		return ioverSigma;
	}

	public void setIoverSigma(Double ioverSigma) {
		this.ioverSigma = ioverSigma;
	}

	public Byte getDiffractionRings() {
		return diffractionRings;
	}

	public void setDiffractionRings(Byte diffractionRings) {
		this.diffractionRings = diffractionRings;
	}
	
	public Byte getStrategySuccess() {
		return strategySuccess;
	}
	
	public void setStrategySuccess(Byte strategySuccess) {
		this.strategySuccess = strategySuccess;
	}
	
	public Byte getIndexingSuccess() {
		return indexingSuccess;
	}
	
	public void setIndexingSuccess(Byte indexingSuccess) {
		this.indexingSuccess = indexingSuccess;
	}
	
	public Integer getTotalNumberOfImages() {
		return totalNumberOfImages;
	}

	public void setTotalNumberOfImages(Integer totalNumberOfImages) {
		this.totalNumberOfImages = totalNumberOfImages;
	}

	public Byte getMosaicityEstimated() {
		return mosaicityEstimated;
	}

	public void setMosaicityEstimated(Byte mosaicityEstimated) {
		this.mosaicityEstimated = mosaicityEstimated;
	}
	

	public Double getRankingResolution() {
		return rankingResolution;
	}


	public void setRankingResolution(Double rankingResolution) {
		this.rankingResolution = rankingResolution;
	}


	public String getProgram() {
		return program;
	}


	public void setProgram(String program) {
		this.program = program;
	}

	public Double getDoseTotal() {
		return doseTotal;
	}


	public void setDoseTotal(Double doseTotal) {
		this.doseTotal = doseTotal;
	}


	public Double getTotalExposureTime() {
		return totalExposureTime;
	}


	public void setTotalExposureTime(Double totalExposureTime) {
		this.totalExposureTime = totalExposureTime;
	}


	public Double getTotalRotationRange() {
		return totalRotationRange;
	}


	public void setTotalRotationRange(Double totalRotationRange) {
		this.totalRotationRange = totalRotationRange;
	}


	public Double getrFriedel() {
		return rFriedel;
	}


	public void setrFriedel(Double rFriedel) {
		this.rFriedel = rFriedel;
	}


	public Set<ScreeningStrategy3VO> getScreeningStrategyVOs() {
		return screeningStrategyVOs;
	}

	public void setScreeningStrategyVOs(Set<ScreeningStrategy3VO> screeningStrategyVOs) {
		this.screeningStrategyVOs = screeningStrategyVOs;
	}

	public ScreeningStrategy3VO[] getScreeningStrategysTab() {
		return this.screeningStrategyVOs == null ? null : screeningStrategyVOs
				.toArray(new ScreeningStrategy3VO[this.screeningStrategyVOs.size()]);
	}

	public ArrayList<ScreeningStrategy3VO> getScreeningStrategysList() {
		return this.screeningStrategyVOs == null ? null : new ArrayList<ScreeningStrategy3VO>(
				Arrays.asList(getScreeningStrategysTab()));
	}

	public Set<ScreeningOutputLattice3VO> getScreeningOutputLatticeVOs() {
		return screeningOutputLatticeVOs;
	}

	public void setScreeningOutputLatticeVOs(Set<ScreeningOutputLattice3VO> screeningOutputLatticeVOs) {
		this.screeningOutputLatticeVOs = screeningOutputLatticeVOs;
	}

	public ScreeningOutputLattice3VO[] getScreeningOutputLatticesTab() {
		return this.screeningOutputLatticeVOs == null ? null : screeningOutputLatticeVOs
				.toArray(new ScreeningOutputLattice3VO[this.screeningOutputLatticeVOs.size()]);
	}

	public ArrayList<ScreeningOutputLattice3VO> getScreeningOutputLatticesList() {
		return this.screeningOutputLatticeVOs == null ? null : new ArrayList<ScreeningOutputLattice3VO>(
				Arrays.asList(getScreeningOutputLatticesTab()));
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
		String s = "screeningOutputId="+this.screeningOutputId +", "+
		"statusDescription="+this.statusDescription+", "+
		"rejectedReflections="+this.rejectedReflections+", "+
		"resolutionObtained="+this.resolutionObtained+", "+
		"spotDeviationR="+this.spotDeviationR+", "+
		"spotDeviationTheta="+this.spotDeviationTheta+", "+
		"beamShiftX="+this.beamShiftX+", "+
		"beamShiftY="+this.beamShiftY+", "+
		"numSpotsFound="+this.numSpotsFound+", "+
		"numSpotsUsed="+this.numSpotsUsed+", "+
		"numSpotsRejected="+this.numSpotsRejected+", "+
		"mosaicity="+this.mosaicity+", "+
		"ioverSigma="+this.ioverSigma+", "+
		"diffractionRings="+this.diffractionRings+", "+
		"strategySuccess="+this.strategySuccess+", "+
		"indexingSuccess="+this.indexingSuccess+", "+
		"mosaicityEstimated="+this.mosaicityEstimated+", "+
		"rankingResolution="+this.rankingResolution+", "+
		"program="+this.program+", "+
		"doseTotal="+this.doseTotal+", "+
		"totalExposureTime="+this.totalExposureTime+", "+
		"totalRotationRange="+this.totalRotationRange+", "+
		"totalNumberOfImages="+this.totalNumberOfImages+", "+
		"rFriedel="+this.rFriedel;
		
		return s;
	}
	
}
