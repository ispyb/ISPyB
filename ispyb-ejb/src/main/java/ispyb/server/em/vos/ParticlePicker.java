package ispyb.server.em.vos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@SuppressWarnings("serial")
@Entity
@Table(name = "ParticlePicker")
public class ParticlePicker implements Serializable{
	
	@Id
	@GeneratedValue
	@Column(name = "particlePickerId")
	protected Integer particlePickerId;
	
	@Column(name = "autoProcProgramId")
	protected Integer autoProcProgramId;
	
	@Column(name = "particlePickingTemplate")
	protected String particlePickingTemplate;
	
	@Column(name = "particleDiameter")
	protected String particleDiameter;
	
	@Column(name = "numberOfParticles")
	protected String numberOfParticles;

	public Integer getParticlePickerid() {
		return particlePickerId;
	}

	public void setParticlePickerId(Integer particlePickerId) {
		this.particlePickerId = particlePickerId;
	}

	public Integer getAutoProcProgramId() {
		return autoProcProgramId;
	}

	public void setAutoProcProgramId(Integer autoProcProgramId) {
		this.autoProcProgramId = autoProcProgramId;
	}

	public String getParticlePickingTemplate() {
		return particlePickingTemplate;
	}

	public void setParticlePickingTemplate(
			String particlePickingTemplate) {
		this.particlePickingTemplate = particlePickingTemplate;
	}

	public String getParticleDiameter() {
		return particleDiameter;
	}

	public void setParticleDiameter(String particleDiameter) {
		this.particleDiameter = particleDiameter;
	}

	public String getNumberOfParticles() {
		return numberOfParticles;
	}

	public void setNumberOfParticles(String numberOfParticles) {
		this.numberOfParticles = numberOfParticles;
	}
}
