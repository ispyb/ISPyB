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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.log4j.Logger;

import ispyb.common.util.StringUtils;
import ispyb.server.common.vos.ISPyBValueObject;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.BLSubSample3VO;

/**
 * EnergyScan3 value object mapping table EnergyScan
 * 
 */
@Entity
@Table(name = "EnergyScan")
public class EnergyScan3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(EnergyScan3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "energyScanId")
	protected Integer energyScanId;
	
	@ManyToOne
	@JoinColumn(name = "sessionId")
	private Session3VO sessionVO;
	
	@ManyToOne
	@JoinColumn(name = "blSampleId")
	private BLSample3VO blSampleVO;
		
	@Column(name = "fluorescenceDetector")
	protected String fluorescenceDetector;
	
	@Column(name = "scanFileFullPath")
	protected String scanFileFullPath;
	
	@Column(name = "choochFileFullPath")
	protected String choochFileFullPath;
	
	@Column(name = "jpegChoochFileFullPath")
	protected String jpegChoochFileFullPath;
	
	@Column(name = "element")
	protected String element;
	
	@Column(name = "startEnergy")
	protected Double startEnergy;
	
	@Column(name = "endEnergy")
	protected Double endEnergy;
	
	@Column(name = "transmissionFactor")
	protected Double transmissionFactor;
	
	@Column(name = "exposureTime")
	protected Double exposureTime;
	
	@Column(name = "synchrotronCurrent")
	protected Double synchrotronCurrent;
	
	@Column(name = "temperature")
	protected Double temperature;
	
	@Column(name = "peakEnergy")
	protected Double peakEnergy;
	
	@Column(name = "peakFPrime")
	protected Double peakFPrime;
	
	@Column(name = "peakFDoublePrime")
	protected Double peakFDoublePrime;
	
	@Column(name = "inflectionEnergy")
	protected Double inflectionEnergy;
	
	@Column(name = "inflectionFPrime")
	protected Double inflectionFPrime;
	
	@Column(name = "inflectionFDoublePrime")
	protected Double inflectionFDoublePrime;
	
	@Column(name = "xrayDose")
	protected Double xrayDose;
	
	@Column(name = "startTime")
	protected Date startTime;
	
	@Column(name = "endTime")
	protected Date endTime;
	
	@Column(name = "edgeEnergy")
	protected String edgeEnergy;
	
	@Column(name = "filename")
	protected String filename;
	
	@Column(name = "beamSizeVertical")
	protected Float beamSizeVertical;
	
	@Column(name = "beamSizeHorizontal")
	protected Float beamSizeHorizontal;
	
	@Column(name = "crystalClass")
	protected String crystalClass;
	
	@Column(name = "comments")
	protected String comments;
	
	@Column(name = "flux")
	protected Double flux;
	
	@Column(name = "flux_end")
	protected Double flux_end;
	
	@Column(name = "workingDirectory")
	protected String workingDirectory;

	@Column(name = "remoteEnergy")
	protected Double remoteEnergy;

	@Column(name = "remoteFPrime")
	protected Double remoteFPrime;

	@Column(name = "remoteFDoublePrime")
	protected Double remoteFDoublePrime;
	
	@ManyToOne
	@JoinColumn(name = "blSubSampleId")
	private BLSubSample3VO blSubSampleVO;
	
	public EnergyScan3VO() {
		super();
	}	

	public EnergyScan3VO(Integer energyScanId, Session3VO sessionVO, BLSample3VO sampleVO,
			String fluorescenceDetector, String scanFileFullPath,
			String choochFileFullPath, String jpegChoochFileFullPath,
			String element, Double startEnergy, Double endEnergy,
			Double transmissionFactor, Double exposureTime,
			Double synchrotronCurrent, Double temperature, Double peakEnergy,
			Double peakFPrime, Double peakFDoublePrime,
			Double inflectionEnergy, Double inflectionFPrime,
			Double inflectionFDoublePrime, Double xrayDose, Date startTime,
			Date endTime, String edgeEnergy, String filename,
			Float beamSizeVertical, Float beamSizeHorizontal,
			String crystalClass, String comments, Double flux, Double flux_end,
			Double remoteEnergy, Double remoteFPrime, Double remoteFDoublePrime) {
		super();
		this.energyScanId = energyScanId;
		this.sessionVO = sessionVO;
		this.blSampleVO = sampleVO;
		this.fluorescenceDetector = fluorescenceDetector;
		this.scanFileFullPath = scanFileFullPath;
		this.choochFileFullPath = choochFileFullPath;
		this.jpegChoochFileFullPath = jpegChoochFileFullPath;
		this.element = element;
		this.startEnergy = startEnergy;
		this.endEnergy = endEnergy;
		this.transmissionFactor = transmissionFactor;
		this.exposureTime = exposureTime;
		this.synchrotronCurrent = synchrotronCurrent;
		this.temperature = temperature;
		this.peakEnergy = peakEnergy;
		this.peakFPrime = peakFPrime;
		this.peakFDoublePrime = peakFDoublePrime;
		this.inflectionEnergy = inflectionEnergy;
		this.inflectionFPrime = inflectionFPrime;
		this.inflectionFDoublePrime = inflectionFDoublePrime;
		this.xrayDose = xrayDose;
		this.startTime = startTime;
		this.endTime = endTime;
		this.edgeEnergy = edgeEnergy;
		this.filename = filename;
		this.beamSizeVertical = beamSizeVertical;
		this.beamSizeHorizontal = beamSizeHorizontal;
		this.crystalClass = crystalClass;
		this.comments = comments;
		this.flux = flux;
		this.flux_end = flux_end;
		this.remoteEnergy = remoteEnergy;
		this.remoteFPrime = remoteFPrime;
		this.remoteFDoublePrime = remoteFDoublePrime;
	}
	
	
	
	public EnergyScan3VO(EnergyScan3VO vo) {
		this.energyScanId = vo.getEnergyScanId();
		this.sessionVO = vo.getSessionVO();
		this.blSampleVO = vo.getBlSampleVO();
		this.fluorescenceDetector = vo.getFluorescenceDetector();
		this.scanFileFullPath = vo.getScanFileFullPath();
		this.choochFileFullPath = vo.getChoochFileFullPath();
		this.jpegChoochFileFullPath = vo.getJpegChoochFileFullPath();
		this.element = vo.getElement();
		this.startEnergy = vo.getStartEnergy();
		this.endEnergy = vo.getEndEnergy();
		this.transmissionFactor = vo.getTransmissionFactor();
		this.exposureTime = vo.getExposureTime();
		this.synchrotronCurrent = vo.getSynchrotronCurrent();
		this.temperature = vo.getTemperature();
		this.peakEnergy = vo.getPeakEnergy();
		this.peakFPrime = vo.getPeakFPrime();
		this.peakFDoublePrime = vo.getPeakFDoublePrime();
		this.inflectionEnergy = vo.getInflectionEnergy();
		this.inflectionFPrime = vo.getInflectionFPrime();
		this.inflectionFDoublePrime = vo.getInflectionFDoublePrime();
		this.xrayDose = vo.getXrayDose();
		this.startTime = vo.getStartTime();
		this.endTime = vo.getEndTime();
		this.edgeEnergy = vo.getEdgeEnergy();
		this.filename = vo.getFilename();
		this.beamSizeVertical = vo.getBeamSizeVertical();
		this.beamSizeHorizontal = vo.getBeamSizeHorizontal();
		this.crystalClass = vo.getCrystalClass();
		this.comments = vo.getComments();
		this.flux = vo.getFlux();
		this.flux_end = vo.getFlux_end();
		this.remoteEnergy = vo.getRemoteEnergy();
		this.remoteFPrime = vo.getRemoteFPrime();
		this.remoteFDoublePrime = vo.getRemoteFDoublePrime();
	}



	public void fillVOFromLight(EnergyScanWS3VO vo) {
		this.energyScanId = vo.getEnergyScanId();
		this.sessionVO = null;
		this.blSampleVO = null;
		this.fluorescenceDetector = vo.getFluorescenceDetector();
		this.scanFileFullPath = vo.getScanFileFullPath();
		this.choochFileFullPath = vo.getChoochFileFullPath();
		this.jpegChoochFileFullPath = vo.getJpegChoochFileFullPath();
		this.element = vo.getElement();
		this.startEnergy = vo.getStartEnergy();
		this.endEnergy = vo.getEndEnergy();
		this.transmissionFactor = vo.getTransmissionFactor();
		this.exposureTime = vo.getExposureTime();
		this.synchrotronCurrent = vo.getSynchrotronCurrent();
		this.temperature = vo.getTemperature();
		this.peakEnergy = vo.getPeakEnergy();
		this.peakFPrime = vo.getPeakFPrime();
		this.peakFDoublePrime = vo.getPeakFDoublePrime();
		this.inflectionEnergy = vo.getInflectionEnergy();
		this.inflectionFPrime = vo.getInflectionFPrime();
		this.inflectionFDoublePrime = vo.getInflectionFDoublePrime();
		this.xrayDose = vo.getXrayDose();
		//this.startTime = vo.getStartTime();
		//this.endTime = vo.getEndTime();
		this.edgeEnergy = vo.getEdgeEnergy();
		this.filename = vo.getFilename();
		this.beamSizeVertical = vo.getBeamSizeVertical();
		this.beamSizeHorizontal = vo.getBeamSizeHorizontal();
		this.crystalClass = vo.getCrystalClass();
		this.comments = vo.getComments();
		this.flux = vo.getFlux();
		this.flux_end = vo.getFlux_end();
		this.remoteEnergy = vo.getRemoteEnergy();
		this.remoteFPrime = vo.getRemoteFPrime();
		this.remoteFDoublePrime = vo.getRemoteFDoublePrime();
	}



	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the energyScanId.
	 */
	public Integer getEnergyScanId() {
		return energyScanId;
	}

	/**
	 * @param pk The energyScanId to set.
	 */
	public void setEnergyScanId(Integer energyScanId) {
		this.energyScanId = energyScanId;
	}
	
	public Integer getSessionVOId() {
		if(sessionVO ==  null)
			return null;
		return this.sessionVO.getSessionId();
	}
	
	public Session3VO getSessionVO() {
		return sessionVO;
	}

	public void setSessionVO(Session3VO sessionVO) {
		this.sessionVO = sessionVO;
	}

	public Integer getBlSampleVOId(){
		return blSampleVO == null ? null : blSampleVO.getBlSampleId();
	}

	public BLSample3VO getBlSampleVO() {
		return blSampleVO;
	}

	public void setBlSampleVO(BLSample3VO blSampleVO) {
		this.blSampleVO = blSampleVO;
	}

	public Integer getBlSubSampleVOId(){
		return blSubSampleVO == null ? null : blSubSampleVO.getBlSubSampleId();
	}
	public BLSubSample3VO getBlSubSampleVO() {
		return blSubSampleVO;
	}

	public void setBlSubSampleVO(BLSubSample3VO blSubSampleVO) {
		this.blSubSampleVO = blSubSampleVO;
	}

	public String getFluorescenceDetector() {
		return fluorescenceDetector;
	}
	public void setFluorescenceDetector(String fluorescenceDetector) {
		this.fluorescenceDetector = fluorescenceDetector;
	}
	
	public String getScanFileFullPath() {
		return scanFileFullPath;
	}
	public void setScanFileFullPath(String scanFileFullPath) {
		this.scanFileFullPath = scanFileFullPath;
	}
	
	public String getChoochFileFullPath() {
		return choochFileFullPath;
	}
	public void setChoochFileFullPath(String choochFileFullPath) {
		this.choochFileFullPath = choochFileFullPath;
	}
	
	public String getJpegChoochFileFullPath() {
		return jpegChoochFileFullPath;
	}
	public void setJpegChoochFileFullPath(String jpegChoochFileFullPath) {
		this.jpegChoochFileFullPath = jpegChoochFileFullPath;
	}
	
	public String getElement() {
		return element;
	}
	public void setElement(String element) {
		this.element = element;
	}
	
	public Double getStartEnergy() {
		return startEnergy;
	}
	public void setStartEnergy(Double startEnergy) {
		this.startEnergy = startEnergy;
	}
	
	public Double getEndEnergy() {
		return endEnergy;
	}
	public void setEndEnergy(Double endEnergy) {
		this.endEnergy = endEnergy;
	}
	
	public Double getTransmissionFactor() {
		return transmissionFactor;
	}
	public void setTransmissionFactor(Double transmissionFactor) {
		this.transmissionFactor = transmissionFactor;
	}
	
	public Double getExposureTime() {
		return exposureTime;
	}
	public void setExposureTime(Double exposureTime) {
		this.exposureTime = exposureTime;
	}
	
	public Double getSynchrotronCurrent() {
		return synchrotronCurrent;
	}
	public void setSynchrotronCurrent(Double synchrotronCurrent) {
		this.synchrotronCurrent = synchrotronCurrent;
	}
	
	public Double getTemperature() {
		return temperature;
	}
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
	
	public Double getPeakEnergy() {
		return peakEnergy;
	}
	public void setPeakEnergy(Double peakEnergy) {
		this.peakEnergy = peakEnergy;
	}
	
	
	
	public Double getPeakFPrime() {
		return peakFPrime;
	}



	public void setPeakFPrime(Double peakFPrime) {
		this.peakFPrime = peakFPrime;
	}



	public Double getPeakFDoublePrime() {
		return peakFDoublePrime;
	}



	public void setPeakFDoublePrime(Double peakFDoublePrime) {
		this.peakFDoublePrime = peakFDoublePrime;
	}



	public Double getInflectionFPrime() {
		return inflectionFPrime;
	}



	public void setInflectionFPrime(Double inflectionFPrime) {
		this.inflectionFPrime = inflectionFPrime;
	}



	public Double getInflectionFDoublePrime() {
		return inflectionFDoublePrime;
	}



	public void setInflectionFDoublePrime(Double inflectionFDoublePrime) {
		this.inflectionFDoublePrime = inflectionFDoublePrime;
	}



	public Double getInflectionEnergy() {
		return inflectionEnergy;
	}
	public void setInflectionEnergy(Double inflectionEnergy) {
		this.inflectionEnergy = inflectionEnergy;
	}
	
	public Double getXrayDose() {
		return xrayDose;
	}
	public void setXrayDose(Double xrayDose) {
		this.xrayDose = xrayDose;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public String getEdgeEnergy() {
		return edgeEnergy;
	}
	public void setEdgeEnergy(String edgeEnergy) {
		this.edgeEnergy = edgeEnergy;
	}
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public Float getBeamSizeVertical() {
		return beamSizeVertical;
	}
	public void setBeamSizeVertical(Float beamSizeVertical) {
		this.beamSizeVertical = beamSizeVertical;
	}
	
	public Float getBeamSizeHorizontal() {
		return beamSizeHorizontal;
	}
	public void setBeamSizeHorizontal(Float beamSizeHorizontal) {
		this.beamSizeHorizontal = beamSizeHorizontal;
	}
	
	public String getCrystalClass() {
		return crystalClass;
	}
	public void setCrystalClass(String crystalClass) {
		this.crystalClass = crystalClass;
	}
	
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

	public Double getFlux() {
		return flux;
	}

	public void setFlux(Double flux) {
		this.flux = flux;
	}
	
	public Double getFlux_end() {
		return flux_end;
	}
	
	public void setFlux_end(Double flux_end) {
		this.flux_end = flux_end;
	}

	public Double getRemoteEnergy() { return remoteEnergy; }
	public void setRemoteEnergy(Double remoteEnergy) {
		this.remoteEnergy = remoteEnergy;
	}

	public Double getRemoteFPrime() {
		return remoteFPrime;
	}
	public void setRemoteFPrime(Double remoteFPrime) {
		this.remoteFPrime = remoteFPrime;
	}

	public Double getRemoteFDoublePrime() {
		return remoteFDoublePrime;
	}
	public void setRemoteFDoublePrime(Double remoteFDoublePrime) {
		this.remoteFDoublePrime = remoteFDoublePrime;
	}


	/**
	 * Checks the values of this value object for correctness and
	 * completeness. Should be done before persisting the data in the DB.
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @throws Exception if the data of the value object is not correct
	 */
	public void checkValues(boolean create) throws Exception {
		int maxLengthFluorescenceDetector = 255;
		int maxLengthScanFileFullPath = 255;
		int maxLengthChoochFileFullPath = 255;
		int maxLengthJpegChoochFileFulPath = 255;
		int maxLengthElement = 45;
		int maxLengthEdgeEnergy = 255;
		int maxLengthFileName = 255;
		int maxLengthCrystalClass = 20;
		int maxLengthComments = 1024;
		
		//session
		if(sessionVO == null)
			throw new IllegalArgumentException(StringUtils.getMessageRequiredField("EnergyScan", "session"));
		// fluorescenceDetector
		if(!StringUtils.isStringLengthValid(this.fluorescenceDetector, maxLengthFluorescenceDetector))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("EnergyScan", "fluorescenceDetector", maxLengthFluorescenceDetector));
		// scanFileFullPath
		if(!StringUtils.isStringLengthValid(this.scanFileFullPath, maxLengthScanFileFullPath))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("EnergyScan", "scanFileFullPath", maxLengthScanFileFullPath));
		// choochFileFullPath
		if(!StringUtils.isStringLengthValid(this.choochFileFullPath, maxLengthChoochFileFullPath))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("EnergyScan", "choochFileFullPath", maxLengthChoochFileFullPath));
		// jpegChoochFileFullPath
		if(!StringUtils.isStringLengthValid(this.jpegChoochFileFullPath, maxLengthJpegChoochFileFulPath))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("EnergyScan", "jpegChoochFileFullPath", maxLengthJpegChoochFileFulPath));
		// element
		if(!StringUtils.isStringLengthValid(this.element, maxLengthElement))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("EnergyScan", "element", maxLengthElement));
		// edgeEnergy
		if(!StringUtils.isStringLengthValid(this.edgeEnergy, maxLengthEdgeEnergy))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("EnergyScan", "edgeEnergy", maxLengthEdgeEnergy));
		// filename
		if(!StringUtils.isStringLengthValid(this.filename, maxLengthFileName))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("EnergyScan", "filename", maxLengthFileName));
		// crystalClass
		if(!StringUtils.isStringLengthValid(this.crystalClass, maxLengthCrystalClass))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("EnergyScan", "crystalClass", maxLengthCrystalClass));
		// comments
		if(!StringUtils.isStringLengthValid(this.comments, maxLengthComments))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("EnergyScan", "comments", maxLengthComments));
		
	}
	
	public String toWSString(){
		String s = "energyScanId="+this.energyScanId +", "+
		"fluorescenceDetector="+this.fluorescenceDetector+", "+
		"scanFileFullPath="+this.scanFileFullPath+", "+
		"choochFileFullPath="+this.choochFileFullPath+", "+
		"jpegChoochFileFullPath="+this.jpegChoochFileFullPath+", "+
		"element="+this.element+", "+
		"startEnergy="+this.startEnergy+", "+
		"endEnergy="+this.endEnergy+", "+
		"transmissionFactor="+this.transmissionFactor+", "+
		"exposureTime="+this.exposureTime+", "+
		"synchrotronCurrent="+this.synchrotronCurrent+", "+
		"temperature="+this.temperature+", "+
		"peakEnergy="+this.peakEnergy+", "+
		"peakFPrime="+this.peakFPrime+", "+
		"peakFDoublePrime="+this.peakFDoublePrime+", "+
		"inflectionEnergy="+this.inflectionEnergy+", "+
		"inflectionFPrime="+this.inflectionFPrime+", "+
		"inflectionFDoublePrime="+this.inflectionFDoublePrime+", "+
		"inflectionFDoublePrime="+this.inflectionFDoublePrime+", "+
		"xrayDose="+this.xrayDose+", "+
		"startTime="+this.startTime+", "+
		"endTime="+this.endTime+", "+
		"edgeEnergy="+this.edgeEnergy+", "+
		"filename="+this.filename+", "+
		"beamSizeVertical="+this.beamSizeVertical+", "+
		"beamSizeHorizontal="+this.beamSizeHorizontal+", "+
		"crystalClass="+this.crystalClass+", "+
		"comments="+this.comments+", "+
		"flux="+this.flux+", "+
		"flux_end="+this.flux_end+", "+
		"remoteEnergy="+this.remoteEnergy+", "+
		"remoteFPrime="+this.remoteFPrime+", "+
		"remoteFDoublePrime="+this.remoteFDoublePrime;
		
		return s;
	}



	public String getWorkingDirectory() {
		return workingDirectory;
	}



	public void setWorkingDirectory(String workingDirectory) {
		this.workingDirectory = workingDirectory;
	}
}
