package ispyb.server.em.vos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@SuppressWarnings("serial")
@Entity
@Table(name = "ParticleClassificationGroup")
public class ParticleClassificationGroup implements Serializable{
	
	@Id
	@GeneratedValue
	@Column(name = "particleClassificationGroupId")
	protected Integer particleClassificationGroupId;
	
	@Column(name = "particlePickerId")
	protected Integer particlePickerId;
	
	@Column(name = "programId")
	protected Integer programId;

	@Column(name = "type")
	protected String type;

	@Column(name = "batchNumber")
	protected String batchNumber;

	@Column(name = "numberOfParticlesPerBatch")
	protected String numberOfParticlesPerBatch;

	@Column(name = "numberOfClassesPerBatch")
	protected String numberOfClassesPerBatch;

	@Column(name = "symmetry")
	protected String symmetry;

	public Integer getParticleClassificationGroupId() {
		return particleClassificationGroupId;
	}

	public void setParticleClassificationGroupId(Integer particleClassificationGroupId) {
		this.particleClassificationGroupId = particleClassificationGroupId;
	}

	public Integer getParticlePickerId() {
		return particlePickerId;
	}

	public void setParticlePickerId(Integer particlePickerId) {
		this.particlePickerId = particlePickerId;
	}

	public Integer getProgramId() {
		return programId;
	}

	public void setProgramId(Integer programId) {
		this.programId = programId;
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

	public String getSymmetry() {
		return symmetry;
	}

	public void setSymmetry(String symmetry) {
		this.symmetry = symmetry;
	}


}
