/*******************************************************************************
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
 ******************************************************************************************************************************/

package ispyb.server.mx.vos.collections;

import ispyb.common.util.StringUtils;
import ispyb.server.common.vos.ISPyBValueObject;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

/**
 * BeamLineSetup value object mapping table BeamLineSetup
 * 
 */
@Entity
@Table(name = "BeamLineSetup")
@SqlResultSetMapping(name = "beamLineSetupNativeQuery", entities = { @EntityResult(entityClass = BeamLineSetup3VO.class) })
public class BeamLineSetup3VO extends ISPyBValueObject implements Cloneable {

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "beamLineSetupId")
	protected Integer beamLineSetupId;
	
	@Column(name = "synchrotronMode")
	protected String synchrotronMode;
	
	@Column(name = "undulatorType1")
	protected String undulatorType1;
	
	@Column(name = "undulatorType2")
	protected String undulatorType2;
	
	@Column(name = "undulatorType3")
	protected String undulatorType3;
	
	@Column(name = "focalSpotSizeAtSample")
	protected Double focalSpotSizeAtSample;
	
	@Column(name = "focusingOptic")
	protected String focusingOptic;
	
	@Column(name = "beamDivergenceHorizontal")
	protected Double beamDivergenceHorizontal;
	
	@Column(name = "beamDivergenceVertical")
	protected Double beamDivergenceVertical;
	
	@Column(name = "polarisation")
	protected Double polarisation;
	
	@Column(name = "monochromatorType")
	protected String monochromatorType;

	@Column(name = "setupDate")
	protected Date setupDate;
	
	@Column(name = "synchrotronName")
	protected String synchrotronName;
	
	@Column(name = "maxExpTimePerDataCollection")
	protected Double maxExpTimePerDataCollection;
	
	@Column(name = "minExposureTimePerImage")
	protected Double minExposureTimePerImage;
	
	@Column(name = "goniostatMaxOscillationSpeed")
	protected Double goniostatMaxOscillationSpeed;
	
	@Column(name = "goniostatMinOscillationWidth")
	protected Double goniostatMinOscillationWidth;
	
	@Column(name = "minTransmission")
	protected Double minTransmission;
	
	@Column(name = "CS")
	protected Float CS;
	

	

	public BeamLineSetup3VO() {
		super();
	}

	public BeamLineSetup3VO(Integer beamLineSetupId, String synchrotronMode,
			String undulatorType1,
			String undulatorType2, String undulatorType3,
			Double focalSpotSizeAtSample, String focusingOptic,
			Double beamDivergenceHorizontal, Double beamDivergenceVertical,
			Double polarisation, String monochromatorType, Date setupDate,
			String synchrotronName, 
			Double maxExpTimePerDataCollection,
			Double minExposureTimePerImage,
			Double goniostatMaxOscillationSpeed,
			Double goniostatMinOscillationWidth, Double minTransmission) {
		super();
		this.beamLineSetupId = beamLineSetupId;
		this.synchrotronMode = synchrotronMode;
		this.undulatorType1 = undulatorType1;
		this.undulatorType2 = undulatorType2;
		this.undulatorType3 = undulatorType3;
		this.focalSpotSizeAtSample = focalSpotSizeAtSample;
		this.focusingOptic = focusingOptic;
		this.beamDivergenceHorizontal = beamDivergenceHorizontal;
		this.beamDivergenceVertical = beamDivergenceVertical;
		this.polarisation = polarisation;
		this.monochromatorType = monochromatorType;
		this.setupDate = setupDate;
		this.synchrotronName = synchrotronName;
		this.maxExpTimePerDataCollection = maxExpTimePerDataCollection;
		this.minExposureTimePerImage = minExposureTimePerImage;
		this.goniostatMaxOscillationSpeed = goniostatMaxOscillationSpeed;
		this.goniostatMinOscillationWidth = goniostatMinOscillationWidth;
		this.minTransmission = minTransmission;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	public Integer getBeamLineSetupId() {
		return beamLineSetupId;
	}

	public void setBeamLineSetupId(Integer beamLineSetupId) {
		this.beamLineSetupId = beamLineSetupId;
	}

	public Date getSetupDate() {
		return setupDate;
	}

	public void setSetupDate(Date setupDate) {
		this.setupDate = setupDate;
	}
	
	public String getSynchrotronName(){
		return this.synchrotronName;
	}
	public void setSynchrotronName(String synchrotronName){
		this.synchrotronName = synchrotronName;
	}
	
	public String getSynchrotronMode(){
		return this.synchrotronMode;
	}
	public void setSynchrotronMode(String synchrotronMode){
		this.synchrotronMode = synchrotronMode;
	}
	
	public String getUndulatorType1(){
		return this.undulatorType1;
	}
	public void setUndulatorType1(String undulatorType1){
		this.undulatorType1 = undulatorType1;
	}
	
	public String getUndulatorType2(){
		return this.undulatorType2;
	}
	public void setUndulatorType2(String undulatorType2){
		this.undulatorType2 = undulatorType2;
	}
	
	public String getUndulatorType3(){
		return this.undulatorType3;
	}
	public void setUndulatorType3(String undulatorType3){
		this.undulatorType3 = undulatorType3;
	}
	
	public Double getFocalSpotSizeAtSample(){
		return this.focalSpotSizeAtSample;
	}
	public void setFocalSpotSizeAtSample(Double focalSpotSizeAtSample){
		this.focalSpotSizeAtSample = focalSpotSizeAtSample;
	}
	
	public String getFocusingOptic(){
		return this.focusingOptic;
	}
	public void setFocusingOptic(String focusingOptic){
		this.focusingOptic = focusingOptic;
	}
	
	public Double getBeamDivergenceHorizontal(){
		return this.beamDivergenceHorizontal;
	}
	public void setBeamDivergenceHorizontal(Double beamDivergenceHorizontal){
		this.beamDivergenceHorizontal = beamDivergenceHorizontal;
	}
	
	public Double getBeamDivergenceVertical(){
		return this.beamDivergenceVertical;
	}
	public void setBeamDivergenceVertical(Double beamDivergenceVertical){
		this.beamDivergenceVertical = beamDivergenceVertical;
	}
	
	public Double getPolarisation(){
		return this.polarisation;
	}
	public void setPolarisation(Double polarisation){
		this.polarisation = polarisation;
	}
	
	public String getMonochromatorType(){
		return this.monochromatorType;
	}
	public void setMonochromatorType(String monochromatorType){
		this.monochromatorType = monochromatorType;
	}
	
	public Double getMaxExpTimePerDataCollection(){
		return this.maxExpTimePerDataCollection;
	}
	public void setMaxExpTimePerDataCollection(Double maxExpTimePerDataCollection){
		this.maxExpTimePerDataCollection = maxExpTimePerDataCollection;
	}
	
	public Double getMinExposureTimePerImage(){
		return this.minExposureTimePerImage;
	}
	public void setMinExposureTimePerImage(Double minExposureTimePerImage){
		this.minExposureTimePerImage = minExposureTimePerImage;
	}
	
	public Double getGoniostatMaxOscillationSpeed(){
		return this.goniostatMaxOscillationSpeed;
	}
	public void setGoniostatMaxOscillationSpeed(Double goniostatMaxOscillationSpeed){
		this.goniostatMaxOscillationSpeed = goniostatMaxOscillationSpeed;
	}
	
	public Double getGoniostatMinOscillationWidth(){
		return this.goniostatMinOscillationWidth;
	}
	public void setGoniostatMinOscillationWidth(Double goniostatMinOscillationWidth){
		this.goniostatMinOscillationWidth = goniostatMinOscillationWidth;
	}
	
	public Double getMinTransmission(){
		return this.minTransmission;
	}
	public void setMinTransmission(Double minTransmission){
		this.minTransmission = minTransmission;
	}

	public Float getCS() {
		return CS;
	}

	public void setCS(Float cS) {
		CS = cS;
	}

	
	/**
	 * Checks the values of this value object for correctness and completeness. Should be done before persisting the
	 * data in the DB.
	 * 
	 * @param create
	 *            should be true if the value object is just being created in the DB, this avoids some checks like
	 *            testing the primary key
	 * @throws VOValidateException
	 *             if the data of the value object is not correct
	 */
	@Override
	public void checkValues(boolean create) throws Exception {
		int maxLengthSynchrotronMode = 255;
		int maxLengthUndulatorType1 = 45;
		int maxLengthUndulatorType2 = 45;
		int maxLengthUndulatorType3 = 45;
		int maxLengthFocusingOptic = 255;
		int maxLengthMonochromatorType = 255;
		int maxLengthSynchrotronName = 255;
		
		// synchrotronMode
		if(!StringUtils.isStringLengthValid(this.synchrotronMode, maxLengthSynchrotronMode))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("BeamLineSetup", "synchrotronMode", maxLengthSynchrotronMode));
		// undulatorType1
		if(!StringUtils.isStringLengthValid(this.undulatorType1, maxLengthUndulatorType1))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("BeamLineSetup", "undulatorType1", maxLengthUndulatorType1));
		// undulatorType2
		if(!StringUtils.isStringLengthValid(this.undulatorType2, maxLengthUndulatorType2))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("BeamLineSetup", "undulatorType2", maxLengthUndulatorType2));
		// undulatorType3
		if(!StringUtils.isStringLengthValid(this.undulatorType3, maxLengthUndulatorType3))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("BeamLineSetup", "undulatorType3", maxLengthUndulatorType3));
		// focusingOptic
		if(!StringUtils.isStringLengthValid(this.focusingOptic, maxLengthFocusingOptic))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("BeamLineSetup", "focusingOptic", maxLengthFocusingOptic));
		// monochromatorType
		if(!StringUtils.isStringLengthValid(this.monochromatorType, maxLengthMonochromatorType))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("BeamLineSetup", "monochromatorType", maxLengthMonochromatorType));
		// synchrotronName
		if(!StringUtils.isStringLengthValid(this.synchrotronName, maxLengthSynchrotronName))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("BeamLineSetup", "synchrotronName", maxLengthSynchrotronName));
		
	}
	
	public String toWSString(){
		String s = "beamLineSetupId="+this.beamLineSetupId +", "+
		"synchrotronMode="+this.synchrotronMode+", "+
		"undulatorType1="+this.undulatorType1+", "+
		"undulatorType2="+this.undulatorType2+", "+
		"undulatorType3="+this.undulatorType3+", "+
		"focalSpotSizeAtSample="+this.focalSpotSizeAtSample+", "+
		"focusingOptic="+this.focusingOptic+", "+
		"beamDivergenceHorizontal="+this.beamDivergenceHorizontal+", "+
		"beamDivergenceVertical="+this.beamDivergenceVertical+", "+
		"polarisation="+this.polarisation+", "+
		"monochromatorType="+this.monochromatorType+", "+
		"setupDate="+this.setupDate+", "+
		"synchrotronName="+this.synchrotronName+", "+
		"maxExpTimePerDataCollection="+this.maxExpTimePerDataCollection+", "+
		"minExposureTimePerImage="+this.minExposureTimePerImage+", "+
		"goniostatMaxOscillationSpeed="+this.goniostatMaxOscillationSpeed+", "+
		"goniostatMinOscillationWidth="+this.goniostatMinOscillationWidth+", "+
		"minTransmission="+this.minTransmission;
		
		return s;
	}
	
	
}
