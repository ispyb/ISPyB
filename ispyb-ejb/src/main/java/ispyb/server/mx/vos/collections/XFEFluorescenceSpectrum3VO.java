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
 * XFEFluorescenceSpectrum3 value object mapping table XFEFluorescenceSpectrum
 * 
 */
@Entity
@Table(name = "XFEFluorescenceSpectrum")
public class XFEFluorescenceSpectrum3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(XFEFluorescenceSpectrum3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "xfeFluorescenceSpectrumId")
	protected Integer xfeFluorescenceSpectrumId;
	
	@ManyToOne
	@JoinColumn(name = "sessionId")
	private Session3VO sessionVO;
	
	@ManyToOne
	@JoinColumn(name = "blSampleId")
	private BLSample3VO blSampleVO;
	
	@Column(name = "fittedDataFileFullPath")
	protected String fittedDataFileFullPath;
	
	@Column(name = "scanFileFullPath")
	protected String scanFileFullPath;
	
	@Column(name = "jpegScanFileFullPath")
	protected String jpegScanFileFullPath;
	
	@Column(name = "startTime")
	protected Date startTime;
	
	@Column(name = "endTime")
	protected Date endTime;
	
	@Column(name = "filename")
	protected String filename;
	
	@Column(name = "energy")
	protected Float energy;
	
	@Column(name = "exposureTime")
	protected Float exposureTime;
	
	@Column(name = "beamTransmission")
	protected Float beamTransmission;
	
	@Column(name = "annotatedPymcaXfeSpectrum")
	protected String annotatedPymcaXfeSpectrum;
	
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
	
	@ManyToOne
	@JoinColumn(name = "blSubSampleId")
	private BLSubSample3VO blSubSampleVO;
	

	public XFEFluorescenceSpectrum3VO() {
		super();
	}

	
	
	public XFEFluorescenceSpectrum3VO(Integer xfeFluorescenceSpectrumId,
			Session3VO sessionVO, BLSample3VO blSampleVO,
			String fittedDataFileFullPath, String scanFileFullPath,
			String jpegScanFileFullPath, Date startTime, Date endTime,
			String filename, Float energy, Float exposureTime,
			Float beamTransmission, String annotatedPymcaXfeSpectrum,
			Float beamSizeVertical, Float beamSizeHorizontal,
			String crystalClass, String comments, Double flux, Double flux_end,
			String workingDirectory) {
		super();
		this.xfeFluorescenceSpectrumId = xfeFluorescenceSpectrumId;
		this.sessionVO = sessionVO;
		this.blSampleVO = blSampleVO;
		this.fittedDataFileFullPath = fittedDataFileFullPath;
		this.scanFileFullPath = scanFileFullPath;
		this.jpegScanFileFullPath = jpegScanFileFullPath;
		this.startTime = startTime;
		this.endTime = endTime;
		this.filename = filename;
		this.energy = energy;
		this.exposureTime = exposureTime;
		this.beamTransmission = beamTransmission;
		this.annotatedPymcaXfeSpectrum = annotatedPymcaXfeSpectrum;
		this.beamSizeVertical = beamSizeVertical;
		this.beamSizeHorizontal = beamSizeHorizontal;
		this.crystalClass = crystalClass;
		this.comments = comments;
		this.flux = flux;
		this.flux_end = flux_end;
		this.workingDirectory = workingDirectory;
	}

	public XFEFluorescenceSpectrum3VO(XFEFluorescenceSpectrum3VO vo) {
		super();
		this.xfeFluorescenceSpectrumId = vo.getXfeFluorescenceSpectrumId();
		this.sessionVO = vo.getSessionVO();
		this.blSampleVO = vo.getBlSampleVO();
		this.fittedDataFileFullPath = vo.getFittedDataFileFullPath();
		this.scanFileFullPath = vo.getScanFileFullPath();
		this.jpegScanFileFullPath = vo.getJpegScanFileFullPath();
		this.startTime = vo.getStartTime();
		this.endTime = vo.getEndTime();
		this.filename = vo.getFilename();
		this.energy = vo.getEnergy();
		this.exposureTime = vo.getExposureTime();
		this.beamTransmission = vo.getBeamTransmission();
		this.annotatedPymcaXfeSpectrum = vo.getAnnotatedPymcaXfeSpectrum();
		this.beamSizeVertical = vo.getBeamSizeVertical();
		this.beamSizeHorizontal = vo.getBeamSizeHorizontal();
		this.crystalClass = vo.getCrystalClass();
		this.comments = vo.getComments();
		this.flux = vo.getFlux();
		this.flux_end = vo.getFlux_end();
		this.workingDirectory = vo.getWorkingDirectory();
	}
	
	
	public void fillVOFromWS(XFEFluorescenceSpectrumWS3VO vo) {
		this.xfeFluorescenceSpectrumId = vo.getXfeFluorescenceSpectrumId();
		this.sessionVO = null;
		this.blSampleVO = null;
		this.fittedDataFileFullPath = vo.getFittedDataFileFullPath();
		this.scanFileFullPath = vo.getScanFileFullPath();
		this.jpegScanFileFullPath = vo.getJpegScanFileFullPath();
		//this.startTime = vo.getStartTime();
		//this.endTime = vo.getEndTime();
		this.filename = vo.getFilename();
		this.energy = vo.getEnergy();
		this.exposureTime = vo.getExposureTime();
		this.beamTransmission = vo.getBeamTransmission();
		this.annotatedPymcaXfeSpectrum = vo.getAnnotatedPymcaXfeSpectrum();
		this.beamSizeVertical = vo.getBeamSizeVertical();
		this.beamSizeHorizontal = vo.getBeamSizeHorizontal();
		this.crystalClass = vo.getCrystalClass();
		this.comments = vo.getComments();
		this.flux = vo.getFlux();
		this.flux_end = vo.getFlux_end();
		this.workingDirectory = vo.getWorkingDirectory();
	}



	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getXfeFluorescenceSpectrumId() {
		return xfeFluorescenceSpectrumId;
	}

	/**
	 * @param pk The pk to set.
	 */
	public void setXfeFluorescenceSpectrumId(Integer xfeFluorescenceSpectrumId) {
		this.xfeFluorescenceSpectrumId = xfeFluorescenceSpectrumId;
	}
			
	public Session3VO getSessionVO() {
		return sessionVO;
	}

	public void setSessionVO(Session3VO sessionVO) {
		this.sessionVO = sessionVO;
	}

	public BLSample3VO getBlSampleVO() {
		return blSampleVO;
	}

	public void setBlSampleVO(BLSample3VO blSampleVO) {
		this.blSampleVO = blSampleVO;
	}

	public String getFittedDataFileFullPath() {
		return fittedDataFileFullPath;
	}
	public void setFittedDataFileFullPath(String fittedDataFileFullPath) {
		this.fittedDataFileFullPath = fittedDataFileFullPath;
	}
	
	public String getScanFileFullPath() {
		return scanFileFullPath;
	}
	public void setScanFileFullPath(String scanFileFullPath) {
		this.scanFileFullPath = scanFileFullPath;
	}
	
	public String getJpegScanFileFullPath() {
		return jpegScanFileFullPath;
	}
	public void setJpegScanFileFullPath(String jpegScanFileFullPath) {
		this.jpegScanFileFullPath = jpegScanFileFullPath;
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
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public Float getEnergy() {
		return energy;
	}
	public void setEnergy(Float energy) {
		this.energy = energy;
	}
	
	public Float getExposureTime() {
		return exposureTime;
	}
	public void setExposureTime(Float exposureTime) {
		this.exposureTime = exposureTime;
	}
	
	public Float getBeamTransmission() {
		return beamTransmission;
	}
	public void setBeamTransmission(Float beamTransmission) {
		this.beamTransmission = beamTransmission;
	}
	
	public String getAnnotatedPymcaXfeSpectrum() {
		return annotatedPymcaXfeSpectrum;
	}
	public void setAnnotatedPymcaXfeSpectrum(String annotatedPymcaXfeSpectrum) {
		this.annotatedPymcaXfeSpectrum = annotatedPymcaXfeSpectrum;
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
	
	public Integer getSessionVOId() {
		return sessionVO == null ? null : sessionVO.getSessionId();
	}
	public Integer getBlSampleVOId(){
		return blSampleVO == null ? null : blSampleVO.getBlSampleId();
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

	public BLSubSample3VO getBlSubSampleVO() {
		return blSubSampleVO;
	}

	public void setBlSubSampleVO(BLSubSample3VO blSubSampleVO) {
		this.blSubSampleVO = blSubSampleVO;
	}

	public Integer getBlSubSampleVOId(){
		return blSubSampleVO == null ? null : blSubSampleVO.getBlSubSampleId();
	}


	/**
	 * Checks the values of this value object for correctness and
	 * completeness. Should be done before persisting the data in the DB.
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @throws Exception if the data of the value object is not correct
	 */
	public void checkValues(boolean create) throws Exception {
		int maxLengthFittedDataFileFullPath = 255;
		int maxLengthScanFileFullPath = 255;
		int maxLengthJpegScanFileFullPath = 255;
		int maxLengthFilename = 255;
		int maxLengthAnnotatedPymcaXfeSpectrum = 255;
		int maxLengthCrystalClass = 20;
		int maxLengthComments = 1024;
		
		//session
		if(sessionVO == null)
			throw new IllegalArgumentException(StringUtils.getMessageRequiredField("XFEFluorescenceSpectrum", "session"));
		// fittedDataFileFullPath
		if(!StringUtils.isStringLengthValid(this.fittedDataFileFullPath, maxLengthFittedDataFileFullPath))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("XFEFluorescenceSpectrum", "fittedDataFileFullPath", maxLengthFittedDataFileFullPath));
		// scanFileFullPath
		if(!StringUtils.isStringLengthValid(this.scanFileFullPath, maxLengthScanFileFullPath))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("XFEFluorescenceSpectrum", "scanFileFullPath", maxLengthScanFileFullPath));
		// jpegScanFileFullPath
		if(!StringUtils.isStringLengthValid(this.jpegScanFileFullPath, maxLengthJpegScanFileFullPath))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("XFEFluorescenceSpectrum", "jpegScanFileFullPath", maxLengthJpegScanFileFullPath));
		// filename
		if(!StringUtils.isStringLengthValid(this.filename, maxLengthFilename))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("XFEFluorescenceSpectrum", "filename", maxLengthFilename));
		// annotatedPymcaXfeSpectrum
		if(!StringUtils.isStringLengthValid(this.annotatedPymcaXfeSpectrum, maxLengthAnnotatedPymcaXfeSpectrum))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("XFEFluorescenceSpectrum", "annotatedPymcaXfeSpectrum", maxLengthAnnotatedPymcaXfeSpectrum));
		// crystalClass
		if(!StringUtils.isStringLengthValid(this.crystalClass, maxLengthCrystalClass))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("XFEFluorescenceSpectrum", "crystalClass", maxLengthCrystalClass));
		// comments
		if(!StringUtils.isStringLengthValid(this.comments, maxLengthComments))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("XFEFluorescenceSpectrum", "comments", maxLengthComments));
		
	}
	
	public String toWSString(){
		String s = "xfeFluorescenceSpectrumId="+this.xfeFluorescenceSpectrumId +", "+
		"fittedDataFileFullPath="+this.fittedDataFileFullPath+", "+
		"scanFileFullPath="+this.scanFileFullPath+", "+
		"jpegScanFileFullPath="+this.jpegScanFileFullPath+", "+
		"startTime="+this.startTime+", "+
		"endTime="+this.endTime+", "+
		"filename="+this.filename+", "+
		"energy="+this.energy+", "+
		"exposureTime="+this.exposureTime+", "+
		"beamTransmission="+this.beamTransmission+", "+
		"annotatedPymcaXfeSpectrum="+this.annotatedPymcaXfeSpectrum+", "+
		"beamSizeVertical="+this.beamSizeVertical+", "+
		"crystalClass="+this.crystalClass+", "+
		"comments="+this.comments+", "+
		"flux="+this.flux+", "+
		"flux_end="+this.flux_end;
		
		return s;
	}



	public String getWorkingDirectory() {
		return workingDirectory;
	}



	public void setWorkingDirectory(String workingDirectory) {
		this.workingDirectory = workingDirectory;
	}
}
