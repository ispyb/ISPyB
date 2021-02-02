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
	
	@Column(name = "particlePickerId")
	protected Integer particlePickerId;
	
	@Column(name = "type")
	protected String type;
	
	@Column(name = "batchNumber")
	protected String batchNumber;
	
	@Column(name = "classNumber")
	protected String classNumber;

	@Column(name = "numberOfParticlesPerBatch")
	protected String numberOfParticlesPerBatch;

	@Column(name = "numberOfClassesPerBatch")
	protected String numberOfClassesPerBatch;

	@Column(name = "particlesPerClass")
	protected String particlesPerClass;

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

	public Integer getParticlePickerId() {
		return particlePickerId;
	}
	public void setParticlePickerId(Integer particlePickerId) {
		this.particlePickerId = particlePickerId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public String getClassNumber() {
		return classNumber;
	}

	public void setClassNumber(String classNumber) {
		this.classNumber = classNumber;
	}

	public String getNumberOfParticlesPerBatch() {
		return numberOfParticlesPerBatch;
	}

	public void setNumberOfParticlesPerBatch(String numberOfParticlesPerBatch) {
		this.numberOfParticlesPerBatch = numberOfParticlesPerBatch;
	}

	public String getNumberOfClassesPerBatch() {
		return numberOfClassesPerBatch;
	}

	public void setNumberOfClassesPerBatch(String numberOfClassesPerBatch) {
		this.numberOfClassesPerBatch = numberOfClassesPerBatch;
	}

	public String getParticlesPerClass() {
		return particlesPerClass;
	}

	public void setParticlesPerClass(String particlesPerClass) {
		this.particlesPerClass = particlesPerClass;
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
