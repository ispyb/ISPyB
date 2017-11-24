package ispyb.server.em.vos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@SuppressWarnings("serial")
@Entity
@Table(name = "CTF")
public class CTF implements Serializable{
	
	@Id
	@GeneratedValue
	@Column(name = "CTFid")
	protected Integer CTFid;
	
	@Column(name = "motionCorrectionId")
	protected Integer motionCorrectionId;
	
	@Column(name = "spectraImageThumbnailFullPath")
	protected String spectraImageThumbnailFullPath;
	
	@Column(name = "spectraImageFullPath")
	protected String spectraImageFullPath;
	
	@Column(name = "defocusU")
	protected String defocusU;
	
	@Column(name = "defocusV")
	protected String defocusV;
	
	@Column(name = "angle")
	protected String angle;
	
	@Column(name = "crossCorrelationCoefficient")
	protected String crossCorrelationCoefficient;
	
	@Column(name = "resolutionLimit")
	protected String resolutionLimit;
	
	@Column(name = "estimatedBfactor")
	protected String estimatedBfactor;
	
	@Column(name = "logFilePath")
	protected String logFilePath;

	public Integer getCTFid() {
		return CTFid;
	}

	public void setCTFid(Integer cTFid) {
		CTFid = cTFid;
	}

	public Integer getMotionCorrectionId() {
		return motionCorrectionId;
	}

	public void setMotionCorrectionId(Integer motionCorrectionId) {
		this.motionCorrectionId = motionCorrectionId;
	}

	public String getSpectraImageThumbnailFullPath() {
		return spectraImageThumbnailFullPath;
	}

	public void setSpectraImageThumbnailFullPath(
			String spectraImageThumbnailFullPath) {
		this.spectraImageThumbnailFullPath = spectraImageThumbnailFullPath;
	}

	public String getSpectraImageFullPath() {
		return spectraImageFullPath;
	}

	public void setSpectraImageFullPath(String spectraImageFullPath) {
		this.spectraImageFullPath = spectraImageFullPath;
	}

	public String getDefocusU() {
		return defocusU;
	}

	public void setDefocusU(String defocusU) {
		this.defocusU = defocusU;
	}

	public String getDefocusV() {
		return defocusV;
	}

	public void setDefocusV(String defocusV) {
		this.defocusV = defocusV;
	}

	public String getAngle() {
		return angle;
	}

	public void setAngle(String angle) {
		this.angle = angle;
	}

	public String getCrossCorrelationCoefficient() {
		return crossCorrelationCoefficient;
	}

	public void setCrossCorrelationCoefficient(String crossCorrelationCoefficient) {
		this.crossCorrelationCoefficient = crossCorrelationCoefficient;
	}

	public String getResolutionLimit() {
		return resolutionLimit;
	}

	public void setResolutionLimit(String resolutionLimit) {
		this.resolutionLimit = resolutionLimit;
	}

	public String getEstimatedBfactor() {
		return estimatedBfactor;
	}

	public void setEstimatedBfactor(String estimatedBfactor) {
		this.estimatedBfactor = estimatedBfactor;
	}

	public String getLogFilePath() {
		return logFilePath;
	}

	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}
	
	
}
