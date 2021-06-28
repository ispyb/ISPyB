package ispyb.server.em.vos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@SuppressWarnings("serial")
@Entity
@Table(name = "ParticleClassification")
public class ParticleClassification implements Serializable{
	
	@Id
	@GeneratedValue
	@Column(name = "particleClassificationId")
	protected Integer particleClassificationId;
	
	@Column(name = "particleClassificationGroupId")
	protected Integer particleClassificationGroupId;
	
	@Column(name = "classNumber")
	protected String classNumber;

	@Column(name = "classImageFullPath")
	protected String classImageFullPath;

	@Column(name = "particlesPerClass")
	protected String particlesPerClass;

	@Column(name = "classDistribution")
	protected String classDistribution;

	@Column(name = "rotationAccuracy")
	protected String rotationAccuracy;

	@Column(name = "translationAccuracy")
	protected String translationAccuracy;

	@Column(name = "estimatedResolution")
	protected String estimatedResolution;

	@Column(name = "overallFourierCompleteness")
	protected String overallFourierCompleteness;

	public Integer getParticleClassificationId() {
		return particleClassificationId;
	}

	public void setParticleClassificationId(Integer particleClassificationId) {
		this.particleClassificationId = particleClassificationId;
	}

	public Integer getParticleClassificationGroupId() {
		return particleClassificationGroupId;
	}

	public void setParticleClassificationGroupId(Integer particleClassificationGroupId) {
		this.particleClassificationGroupId = particleClassificationGroupId;
	}

	public String getClassNumber() {
		return classNumber;
	}

	public void setClassNumber(String classNumber) {
		this.classNumber = classNumber;
	}

	public String getClassImageFullPath() {
		return classImageFullPath;
	}

	public void setClassImageFullPath(String classImageFullPath) {
		this.classImageFullPath = classImageFullPath;
	}

	public String getParticlesPerClass() {
		return particlesPerClass;
	}

	public void setParticlesPerClass(String particlesPerClass) {
		this.particlesPerClass = particlesPerClass;
	}

	public String getClassDistribution() {
		return classDistribution;
	}

	public void setClassDistribution(String classDistribution) {
		this.classDistribution = classDistribution;
	}

	public String getRotationAccuracy() {
		return rotationAccuracy;
	}

	public void setRotationAccuracy(String rotationAccuracy) {
		this.rotationAccuracy = rotationAccuracy;
	}

	public String getTranslationAccuracy() {
		return translationAccuracy;
	}

	public void setTranslationAccuracy(String translationAccuracy) {
		this.translationAccuracy = translationAccuracy;
	}

	public String getEstimatedResolution() {
		return estimatedResolution;
	}

	public void setEstimatedResolution(String estimatedResolution) {
		this.estimatedResolution = estimatedResolution;
	}

	public String getOverallFourierCompleteness() {
		return overallFourierCompleteness;
	}

	public void setOverallFourierCompleteness(String overallFourierCompleteness) {
		this.overallFourierCompleteness = overallFourierCompleteness;
	}


}
