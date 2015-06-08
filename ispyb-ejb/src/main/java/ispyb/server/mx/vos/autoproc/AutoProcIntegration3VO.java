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

package ispyb.server.mx.vos.autoproc;

import ispyb.server.common.vos.ISPyBValueObject;
import ispyb.server.mx.vos.collections.DataCollection3VO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OrderBy;

/**
 * AutoProcIntegration3 value object mapping table AutoProcIntegration
 * 
 */
@Entity
@Table(name = "AutoProcIntegration")
@SqlResultSetMapping(name = "autoProcIntegrationNativeQuery", entities = { @EntityResult(entityClass = AutoProcIntegration3VO.class) })
public class AutoProcIntegration3VO extends ISPyBValueObject implements Cloneable  {

	private final static Logger LOG = Logger.getLogger(AutoProcIntegration3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "autoProcIntegrationId")
	protected Integer autoProcIntegrationId;
	
	@ManyToOne
	@JoinColumn(name = "dataCollectionId")
	private DataCollection3VO dataCollectionVO;
	
	@ManyToOne
	@JoinColumn(name = "autoProcProgramId")
	private AutoProcProgram3VO autoProcProgramVO;
	
	@Column(name = "startImageNumber")
	protected Integer startImageNumber;
	
	@Column(name = "endImageNumber")
	protected Integer endImageNumber;
	
	@Column(name = "refinedDetectorDistance")
	protected Float refinedDetectorDistance;
	
	@Column(name = "refinedXbeam")
	protected Float refinedXbeam;
	
	@Column(name = "refinedYbeam")
	protected Float refinedYbeam;
	
	@Column(name = "rotationAxisX")
	protected Float rotationAxisX;
	
	@Column(name = "rotationAxisY")
	protected Float rotationAxisY;
	
	@Column(name = "rotationAxisZ")
	protected Float rotationAxisZ;
	
	@Column(name = "beamVectorX")
	protected Float beamVectorX;
	
	@Column(name = "beamVectorY")
	protected Float beamVectorY;
	
	@Column(name = "beamVectorZ")
	protected Float beamVectorZ;
	
	@Column(name = "cell_a")
	protected Float cellA;
	
	@Column(name = "cell_b")
	protected Float cellB;
	
	@Column(name = "cell_c")
	protected Float cellC;
	
	@Column(name = "cell_alpha")
	protected Float cellAlpha;
	
	@Column(name = "cell_beta")
	protected Float cellBeta;
	
	@Column(name = "cell_gamma")
	protected Float cellGamma;
	
	@Column(name = "recordTimeStamp")
	protected Date recordTimeStamp;
	
	@Column(name = "anomalous")
	protected Boolean anomalous;
	
	@Fetch(value = FetchMode.SELECT)
	@OneToMany(fetch = FetchType.EAGER)
	@OrderBy(clause = "autoProcStatusId")
	@JoinColumn(name = "autoProcIntegrationId")
	private Set<AutoProcStatus3VO> autoProcStatusVOs;
 
	public AutoProcIntegration3VO(){
		super();
	}

	
	
	public AutoProcIntegration3VO(Integer autoProcIntegrationId,
			DataCollection3VO dataCollectionVO,
			AutoProcProgram3VO autoProcProgramVO, Integer startImageNumber,
			Integer endImageNumber, Float refinedDetectorDistance,
			Float refinedXbeam, Float refinedYbeam, Float rotationAxisX,
			Float rotationAxisY, Float rotationAxisZ, Float beamVectorX,
			Float beamVectorY, Float beamVectorZ, Float cellA, Float cellB,
			Float cellC, Float cellAlpha, Float cellBeta, Float cellGamma,
			Date recordTimeStamp, Boolean anomalous) {
		super();
		this.autoProcIntegrationId = autoProcIntegrationId;
		this.dataCollectionVO = dataCollectionVO;
		this.autoProcProgramVO = autoProcProgramVO;
		this.startImageNumber = startImageNumber;
		this.endImageNumber = endImageNumber;
		this.refinedDetectorDistance = refinedDetectorDistance;
		this.refinedXbeam = refinedXbeam;
		this.refinedYbeam = refinedYbeam;
		this.rotationAxisX = rotationAxisX;
		this.rotationAxisY = rotationAxisY;
		this.rotationAxisZ = rotationAxisZ;
		this.beamVectorX = beamVectorX;
		this.beamVectorY = beamVectorY;
		this.beamVectorZ = beamVectorZ;
		this.cellA = cellA;
		this.cellB = cellB;
		this.cellC = cellC;
		this.cellAlpha = cellAlpha;
		this.cellBeta = cellBeta;
		this.cellGamma = cellGamma;
		this.recordTimeStamp = recordTimeStamp;
		this.anomalous = anomalous;
	}
	
	public AutoProcIntegration3VO(AutoProcIntegration3VO vo) {
		super();
		this.autoProcIntegrationId = vo.getAutoProcIntegrationId();
		this.dataCollectionVO = vo.getDataCollectionVO();
		this.autoProcProgramVO = vo.getAutoProcProgramVO();
		this.startImageNumber = vo.getStartImageNumber();
		this.endImageNumber = vo.getEndImageNumber();
		this.refinedDetectorDistance = vo.getRefinedDetectorDistance();
		this.refinedXbeam = vo.getRefinedXbeam();
		this.refinedYbeam = vo.getRefinedYbeam();
		this.rotationAxisX = vo.getRotationAxisX();
		this.rotationAxisY = vo.getRotationAxisY();
		this.rotationAxisZ = vo.getRotationAxisZ();
		this.beamVectorX = vo.getBeamVectorX();
		this.beamVectorY = vo.getBeamVectorY();
		this.beamVectorZ = vo.getBeamVectorZ();
		this.cellA = vo.getCellA();
		this.cellB = vo.getCellB();
		this.cellC = vo.getCellC();
		this.cellAlpha = vo.getCellAlpha();
		this.cellBeta = vo.getCellBeta();
		this.cellGamma = vo.getCellGamma();
		this.recordTimeStamp = vo.getRecordTimeStamp();
		this.anomalous = vo.getAnomalous();
		this.autoProcStatusVOs = vo.getAutoProcStatusVOs();
	}
	
	public void  fillVOFromWS(AutoProcIntegrationWS3VO vo) {
		this.autoProcIntegrationId = vo.getAutoProcIntegrationId();
		this.dataCollectionVO = null;
		this.autoProcProgramVO = null;
		this.startImageNumber = vo.getStartImageNumber();
		this.endImageNumber = vo.getEndImageNumber();
		this.refinedDetectorDistance = vo.getRefinedDetectorDistance();
		this.refinedXbeam = vo.getRefinedXbeam();
		this.refinedYbeam = vo.getRefinedYbeam();
		this.rotationAxisX = vo.getRotationAxisX();
		this.rotationAxisY = vo.getRotationAxisY();
		this.rotationAxisZ = vo.getRotationAxisZ();
		this.beamVectorX = vo.getBeamVectorX();
		this.beamVectorY = vo.getBeamVectorY();
		this.beamVectorZ = vo.getBeamVectorZ();
		this.cellA = vo.getCellA();
		this.cellB = vo.getCellB();
		this.cellC = vo.getCellC();
		this.cellAlpha = vo.getCellAlpha();
		this.cellBeta = vo.getCellBeta();
		this.cellGamma = vo.getCellGamma();
		this.recordTimeStamp = vo.getRecordTimeStamp();
		this.anomalous = vo.getAnomalous();
	}



	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getAutoProcIntegrationId() {
		return autoProcIntegrationId;
	}

	/**
	 * @param pk The pk to set.
	 */
	public void setAutoProcIntegrationId(Integer autoProcIntegrationId) {
		this.autoProcIntegrationId = autoProcIntegrationId;
	}

	public DataCollection3VO getDataCollectionVO() {
		return dataCollectionVO;
	}



	public void setDataCollectionVO(DataCollection3VO dataCollectionVO) {
		this.dataCollectionVO = dataCollectionVO;
	}



	public AutoProcProgram3VO getAutoProcProgramVO() {
		return autoProcProgramVO;
	}



	public void setAutoProcProgramVO(AutoProcProgram3VO autoProcProgramVO) {
		this.autoProcProgramVO = autoProcProgramVO;
	}



	public Integer getStartImageNumber() {
		return startImageNumber;
	}

	public void setStartImageNumber(Integer startImageNumber) {
		this.startImageNumber = startImageNumber;
	}

	public Integer getEndImageNumber() {
		return endImageNumber;
	}

	public void setEndImageNumber(Integer endImageNumber) {
		this.endImageNumber = endImageNumber;
	}

	public Float getRefinedDetectorDistance() {
		return refinedDetectorDistance;
	}

	public void setRefinedDetectorDistance(Float refinedDetectorDistance) {
		this.refinedDetectorDistance = refinedDetectorDistance;
	}

	public Float getRefinedXbeam() {
		return refinedXbeam;
	}

	public void setRefinedXbeam(Float refinedXbeam) {
		this.refinedXbeam = refinedXbeam;
	}

	public Float getRefinedYbeam() {
		return refinedYbeam;
	}

	public void setRefinedYbeam(Float refinedYbeam) {
		this.refinedYbeam = refinedYbeam;
	}

	public Float getRotationAxisX() {
		return rotationAxisX;
	}

	public void setRotationAxisX(Float rotationAxisX) {
		this.rotationAxisX = rotationAxisX;
	}

	public Float getRotationAxisY() {
		return rotationAxisY;
	}

	public void setRotationAxisY(Float rotationAxisY) {
		this.rotationAxisY = rotationAxisY;
	}

	public Float getRotationAxisZ() {
		return rotationAxisZ;
	}

	public void setRotationAxisZ(Float rotationAxisZ) {
		this.rotationAxisZ = rotationAxisZ;
	}

	public Float getBeamVectorX() {
		return beamVectorX;
	}

	public void setBeamVectorX(Float beamVectorX) {
		this.beamVectorX = beamVectorX;
	}

	public Float getBeamVectorY() {
		return beamVectorY;
	}

	public void setBeamVectorY(Float beamVectorY) {
		this.beamVectorY = beamVectorY;
	}

	public Float getBeamVectorZ() {
		return beamVectorZ;
	}

	public void setBeamVectorZ(Float beamVectorZ) {
		this.beamVectorZ = beamVectorZ;
	}

	public Float getCellA() {
		return cellA;
	}

	public void setCellA(Float cellA) {
		this.cellA = cellA;
	}

	public Float getCellB() {
		return cellB;
	}

	public void setCellB(Float cellB) {
		this.cellB = cellB;
	}

	public Float getCellC() {
		return cellC;
	}

	public void setCellC(Float cellC) {
		this.cellC = cellC;
	}

	public Float getCellAlpha() {
		return cellAlpha;
	}

	public void setCellAlpha(Float cellAlpha) {
		this.cellAlpha = cellAlpha;
	}

	public Float getCellBeta() {
		return cellBeta;
	}

	public void setCellBeta(Float cellBeta) {
		this.cellBeta = cellBeta;
	}

	public Float getCellGamma() {
		return cellGamma;
	}

	public void setCellGamma(Float cellGamma) {
		this.cellGamma = cellGamma;
	}

	public Date getRecordTimeStamp() {
		return recordTimeStamp;
	}

	public void setRecordTimeStamp(Date recordTimeStamp) {
		this.recordTimeStamp = recordTimeStamp;
	}

	public Boolean getAnomalous() {
		return anomalous;
	}

	public void setAnomalous(Boolean anomalous) {
		this.anomalous = anomalous;
	}
	
	public Integer getDataCollectionVOId(){
		return dataCollectionVO == null ? null : dataCollectionVO.getDataCollectionId();
	}
	
	public Integer getAutoProcProgramVOId(){
		return autoProcProgramVO == null ? null : autoProcProgramVO.getAutoProcProgramId();
	}
	
	public Set<AutoProcStatus3VO> getAutoProcStatusVOs() {
		return autoProcStatusVOs;
	}

	public void setAutoProcStatusVOs(Set<AutoProcStatus3VO> autoProcStatusVOs) {
		this.autoProcStatusVOs = autoProcStatusVOs;
	}
	
	public AutoProcStatus3VO[] getAutoProcStatusTab() {
		return this.autoProcStatusVOs == null ? null : autoProcStatusVOs
				.toArray(new AutoProcStatus3VO[this.autoProcStatusVOs.size()]);
	}

	public ArrayList<AutoProcStatus3VO> getAutoProcStatusList() {
		return this.autoProcStatusVOs == null ? null : new ArrayList<AutoProcStatus3VO>(
				Arrays.asList(getAutoProcStatusTab()));
	}



	/**
	 * Checks the values of this value object for correctness and
	 * completeness. Should be done before persisting the data in the DB.
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @throws Exception if the data of the value object is not correct
	 */
	public void checkValues(boolean create) throws Exception {
		//TODO
	}
	
	public String toWSString(){
		String s = "autoProcIntegrationId="+this.autoProcIntegrationId +", "+
		"startImageNumber="+this.startImageNumber+", "+
		"endImageNumber="+this.endImageNumber+", "+
		"refinedDetectorDistance="+this.refinedDetectorDistance+", "+
		"refinedXbeam="+this.refinedXbeam+", "+
		"refinedYbeam="+this.refinedYbeam+", "+
		"rotationAxisX="+this.rotationAxisX+", "+
		"rotationAxisY="+this.rotationAxisY+", "+
		"rotationAxisZ="+this.rotationAxisZ+", "+
		"beamVectorX="+this.beamVectorX+", "+
		"beamVectorY="+this.beamVectorY+", "+
		"beamVectorZ="+this.beamVectorZ+", "+
		"cellA="+this.cellA+", "+
		"cellB="+this.cellB+", "+
		"cellC="+this.cellC+", "+
		"cellAlpha="+this.cellAlpha+", "+
		"cellBeta="+this.cellBeta+", "+
		"cellGamma="+this.cellGamma+", "+
		"recordTimeStamp="+this.recordTimeStamp+", "+
		"anomalous="+this.anomalous;
		
		return s;
	}
}

