package ispyb.server.em.vos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@SuppressWarnings("serial")
@Entity
@Table(name = "MotionCorrection")
public class MotionCorrection implements Serializable{
	
	@Id
	@GeneratedValue
	@Column(name = "motionCorrectionId")
	protected Integer motionCorrectionId;
	
	@Column(name = "movieId")
	protected Integer movieId;
	
	@Column(name = "firstFrame")
	protected String firstFrame;
	
	@Column(name = "lastFrame")
	protected String lastFrame;
	
	@Column(name = "dosePerFrame")
	protected String dosePerFrame;
	
	@Column(name = "doseWeight")
	protected String doseWeight;
	
	@Column(name = "totalMotion")
	protected String totalMotion;
	
	@Column(name = "averageMotionPerFrame")
	protected String averageMotionPerFrame;
	
	@Column(name = "driftPlotFullPath")
	protected String driftPlotFullPath;
	
	@Column(name = "micrographFullPath")
	protected String micrographFullPath;
	
	@Column(name = "micrographSnapshotFullPath")
	protected String micrographSnapshotFullPath;
	
	@Column(name = "correctedDoseMicrographFullPath")
	protected String correctedDoseMicrographFullPath;
	
	@Column(name = "patchesUsed")
	protected String patchesUsed;
	
	@Column(name = "logFileFullPath")
	protected String logFileFullPath;

	public Integer getMotionCorrectionId() {
		return motionCorrectionId;
	}

	public void setMotionCorrectionId(Integer motionCorrectionId) {
		this.motionCorrectionId = motionCorrectionId;
	}

	public Integer getMovieId() {
		return movieId;
	}

	public void setMovieId(Integer movieId) {
		this.movieId = movieId;
	}

	public String getFirstFrame() {
		return firstFrame;
	}

	public void setFirstFrame(String firstFrame) {
		this.firstFrame = firstFrame;
	}

	public String getLastFrame() {
		return lastFrame;
	}

	public void setLastFrame(String lastFrame) {
		this.lastFrame = lastFrame;
	}

	public String getDosePerFrame() {
		return dosePerFrame;
	}

	public void setDosePerFrame(String dosePerFrame) {
		this.dosePerFrame = dosePerFrame;
	}

	public String getDoseWeight() {
		return doseWeight;
	}

	public void setDoseWeight(String doseWeight) {
		this.doseWeight = doseWeight;
	}

	public String getTotalMotion() {
		return totalMotion;
	}

	public void setTotalMotion(String totalMotion) {
		this.totalMotion = totalMotion;
	}

	public String getAverageMotionPerFrame() {
		return averageMotionPerFrame;
	}

	public void setAverageMotionPerFrame(String averageMotionPerFrame) {
		this.averageMotionPerFrame = averageMotionPerFrame;
	}

	public String getDriftPlotFullPath() {
		return driftPlotFullPath;
	}

	public void setDriftPlotFullPath(String driftPlotFullPath) {
		this.driftPlotFullPath = driftPlotFullPath;
	}

	public String getMicrographFullPath() {
		return micrographFullPath;
	}

	public void setMicrographFullPath(String micrographFullPath) {
		this.micrographFullPath = micrographFullPath;
	}

	public String getMicrographSnapshotFullPath() {
		return micrographSnapshotFullPath;
	}

	public void setMicrographSnapshotFullPath(String micrographSnapshotFullPath) {
		this.micrographSnapshotFullPath = micrographSnapshotFullPath;
	}

	public String getCorrectedDoseMicrographFullPath() {
		return correctedDoseMicrographFullPath;
	}

	public void setCorrectedDoseMicrographFullPath(
			String correctedDoseMicrographFullPath) {
		this.correctedDoseMicrographFullPath = correctedDoseMicrographFullPath;
	}

	public String getPatchesUsed() {
		return patchesUsed;
	}

	public void setPatchesUsed(String patchesUsed) {
		this.patchesUsed = patchesUsed;
	}

	public String getLogFileFullPath() {
		return logFileFullPath;
	}

	public void setLogFileFullPath(String logFileFullPath) {
		this.logFileFullPath = logFileFullPath;
	}
	
}
