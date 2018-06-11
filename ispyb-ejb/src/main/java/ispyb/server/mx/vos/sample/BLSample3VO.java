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

package ispyb.server.mx.vos.sample;

import ispyb.server.common.vos.ISPyBValueObject;
import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.mx.vos.collections.EnergyScan3VO;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * BLSample3 value object mapping table BLSample
 * 
 */
@Entity
@Table(name = "BLSample")
@SqlResultSetMapping(name = "blSampleNativeQuery", entities = { @EntityResult(entityClass = BLSample3VO.class) })
public class BLSample3VO extends ISPyBValueObject implements Cloneable {

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "blSampleId")
	protected Integer blSampleId;

	@ManyToOne
	@JoinColumn(name = "diffractionPlanId")
	protected DiffractionPlan3VO diffractionPlanVO;

	@ManyToOne
	@JoinColumn(name = "crystalId")
	protected Crystal3VO crystalVO;

	@ManyToOne
	@JoinColumn(name = "containerId")
	private Container3VO containerVO;

	@Column(name = "name")
	protected String name;

	@Column(name = "code")
	protected String code;

	@Column(name = "location")
	protected String location;

	@Column(name = "holderLength")
	protected Double holderLength;

	@Column(name = "loopLength")
	protected Double loopLength;

	@Column(name = "loopType")
	protected String loopType;

	@Column(name = "wireWidth")
	protected Double wireWidth;

	@Column(name = "comments")
	protected String comments;

	@Column(name = "completionStage")
	protected String completionStage;

	@Column(name = "structureStage")
	protected String structureStage;

	@Column(name = "publicationStage")
	protected String publicationStage;

	@Column(name = "publicationComments")
	protected String publicationComments;

	@Column(name = "blSampleStatus")
	protected String blSampleStatus;

	@Column(name = "isInSampleChanger")
	protected Byte isInSampleChanger;

	@Column(name = "lastKnownCenteringPosition")
	protected String lastKnownCenteringPosition;
	
	@Column(name = "SMILES")
	protected String smiles;
	
	@Column(name = "lastImageURL")
	protected String lastImageURL;

	@OneToMany
	@JoinColumn(name = "blSampleId")
	private Set<EnergyScan3VO> energyScanVOs;
	
	@Fetch(value = FetchMode.SELECT)
	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE })
	@JoinColumn(name = "blSampleId")
	protected Set<BLSubSample3VO> blSubSampleVOs;
	
	@Fetch(value = FetchMode.SELECT)
	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE })
	@JoinColumn(name = "blSampleId")
	protected Set<BLSampleImage3VO> blsampleImageVOs;


	public BLSample3VO() {
		super();
	}

	public BLSample3VO(Integer blSampleId, DiffractionPlan3VO diffractionPlanVO, Crystal3VO crystalVO,
			Container3VO containerVO, String name, String code, String location, Double holderLength,
			Double loopLength, String loopType, Double wireWidth, String comments, String completionStage,
			String structureStage, String publicationStage, String publicationComments, String blSampleStatus,
			Byte isInSampleChanger, String lastKnownCenteringPosition, String SMILES,String lastImageURL) {
		super();
		this.blSampleId = blSampleId;
		this.diffractionPlanVO = diffractionPlanVO;
		this.crystalVO = crystalVO;
		this.containerVO = containerVO;
		this.name = name;
		this.code = code;
		this.location = location;
		this.holderLength = holderLength;
		this.loopLength = loopLength;
		this.loopType = loopType;
		this.wireWidth = wireWidth;
		this.comments = comments;
		this.completionStage = completionStage;
		this.structureStage = structureStage;
		this.publicationStage = publicationStage;
		this.publicationComments = publicationComments;
		this.blSampleStatus = blSampleStatus;
		this.isInSampleChanger = isInSampleChanger;
		this.lastKnownCenteringPosition = lastKnownCenteringPosition;
		this.smiles = SMILES;
		this.lastImageURL = lastImageURL;
	}

	public BLSample3VO(BLSample3VO vo) {
		this.blSampleId = vo.getBlSampleId();
		this.diffractionPlanVO = vo.getDiffractionPlanVO();
		this.crystalVO = vo.getCrystalVO();
		this.containerVO = vo.getContainerVO();
		this.name = vo.getName();
		this.code = vo.getCode();
		this.location = vo.getLocation();
		this.holderLength = vo.getHolderLength();
		this.loopLength = vo.getLoopLength();
		this.loopType = vo.getLoopType();
		this.wireWidth = vo.getWireWidth();
		this.comments = vo.getComments();
		this.completionStage = vo.getCompletionStage();
		this.structureStage = vo.getStructureStage();
		this.publicationStage = vo.getPublicationStage();
		this.publicationComments = vo.getPublicationComments();
		this.blSampleStatus = vo.getBlSampleStatus();
		this.isInSampleChanger = vo.getIsInSampleChanger();
		this.lastKnownCenteringPosition = vo.getLastKnownCenteringPosition();
		this.smiles = vo.getSmiles();
		this.lastImageURL = vo.getLastImageURL();
	}

	public void fillVOFromWS(BLSampleWS3VO vo) {
		this.blSampleId = vo.getBlSampleId();
		this.diffractionPlanVO = null;
		this.crystalVO = null;
		this.containerVO = null;
		this.name = vo.getName();
		this.code = vo.getCode();
		this.location = vo.getLocation();
		this.holderLength = vo.getHolderLength();
		this.loopLength = vo.getLoopLength();
		this.loopType = vo.getLoopType();
		this.wireWidth = vo.getWireWidth();
		this.comments = vo.getComments();
		this.completionStage = vo.getCompletionStage();
		this.structureStage = vo.getStructureStage();
		this.publicationStage = vo.getPublicationStage();
		this.publicationComments = vo.getPublicationComments();
		this.blSampleStatus = vo.getBlSampleStatus();
		this.isInSampleChanger = vo.getIsInSampleChanger();
		this.lastKnownCenteringPosition = vo.getLastKnownCenteringPosition();
		this.smiles = vo.getSmiles();
		this.lastImageURL = vo.getLastImageURL();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getBlSampleId() {
		return blSampleId;
	}

	/**
	 * @param pk
	 *            The pk to set.
	 */
	public void setBlSampleId(Integer blSampleId) {
		this.blSampleId = blSampleId;
	}

	public DiffractionPlan3VO getDiffractionPlanVO() {
		return diffractionPlanVO;
	}

	public void setDiffractionPlanVO(DiffractionPlan3VO diffractionPlanVO) {
		this.diffractionPlanVO = diffractionPlanVO;
	}

	public Crystal3VO getCrystalVO() {
		return crystalVO;
	}

	public void setCrystalVO(Crystal3VO crystalVO) {
		this.crystalVO = crystalVO;
	}

	public Integer getContainerVOId() {
		if (this.containerVO == null)
			return null;
		return this.containerVO.getContainerId();
	}

	public Container3VO getContainerVO() {
		return containerVO;
	}

	public void setContainerVO(Container3VO containerVO) {
		this.containerVO = containerVO;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Double getHolderLength() {
		return holderLength;
	}

	public void setHolderLength(Double holderLength) {
		this.holderLength = holderLength;
	}

	public Double getLoopLength() {
		return loopLength;
	}

	public void setLoopLength(Double loopLength) {
		this.loopLength = loopLength;
	}

	public String getLoopType() {
		return loopType;
	}

	public void setLoopType(String loopType) {
		this.loopType = loopType;
	}

	public Double getWireWidth() {
		return wireWidth;
	}

	public void setWireWidth(Double wireWidth) {
		this.wireWidth = wireWidth;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCompletionStage() {
		return completionStage;
	}

	public void setCompletionStage(String completionStage) {
		this.completionStage = completionStage;
	}

	public String getStructureStage() {
		return structureStage;
	}

	public void setStructureStage(String structureStage) {
		this.structureStage = structureStage;
	}

	public String getPublicationStage() {
		return publicationStage;
	}

	public void setPublicationStage(String publicationStage) {
		this.publicationStage = publicationStage;
	}

	public String getPublicationComments() {
		return publicationComments;
	}

	public void setPublicationComments(String publicationComments) {
		this.publicationComments = publicationComments;
	}

	public String getBlSampleStatus() {
		return blSampleStatus;
	}

	public void setBlSampleStatus(String blSampleStatus) {
		this.blSampleStatus = blSampleStatus;
	}

	public Byte getIsInSampleChanger() {
		return isInSampleChanger;
	}

	public void setIsInSampleChanger(Byte isInSampleChanger) {
		this.isInSampleChanger = isInSampleChanger;
	}

	public String getLastKnownCenteringPosition() {
		return lastKnownCenteringPosition;
	}

	public void setLastKnownCenteringPosition(String lastKnownCenteringPosition) {
		this.lastKnownCenteringPosition = lastKnownCenteringPosition;
	}

	public String getSmiles() {
		return smiles;
	}

	public void setSmiles(String smiles) {
		this.smiles = smiles;
	}

	public String getLastImageURL() {
		return lastImageURL;
	}

	public void setLastImageURL(String lastImageURL) {
		this.lastImageURL = lastImageURL;
	}

	public Integer getCrystalVOId() {
		return crystalVO == null ? null : crystalVO.getCrystalId();
	}

	public Set<EnergyScan3VO> getEnergyScanVOs() {
		return energyScanVOs;
	}

	public void setEnergyScanVOs(Set<EnergyScan3VO> energyScanVOs) {
		this.energyScanVOs = energyScanVOs;
	}

	public EnergyScan3VO[] getEnergyScansTab() {
		return this.energyScanVOs == null ? null : energyScanVOs.toArray(new EnergyScan3VO[this.energyScanVOs.size()]);
	}

	public Integer getDiffractionPlanVOId() {
		if (this.diffractionPlanVO != null)
			return this.diffractionPlanVO.getDiffractionPlanId();
		else
			return null;
	}

	public Set<BLSubSample3VO> getBlSubSampleVOs() {
		return blSubSampleVOs;
	}

	public void setBlSubSampleVOs(Set<BLSubSample3VO> blSubSampleVOs) {
		this.blSubSampleVOs = blSubSampleVOs;
	}
	
	public BLSubSample3VO[] getBLSubSamplesTab() {
		return this.blSubSampleVOs == null ? null : blSubSampleVOs.toArray(new BLSubSample3VO[this.blSubSampleVOs.size()]);
	}
		
	public Set<BLSampleImage3VO> getBlsampleImageVOs() {
		return blsampleImageVOs;
	}

	public void setBlsampleImageVOs(Set<BLSampleImage3VO> blsampleImageVOs) {
		this.blsampleImageVOs = blsampleImageVOs;
	}
	
	// if eager loaded and if we suppose only 1 BLsampleImage loaded, returns the imagePath 
	public String getBlsampleImagePath() {
		if (this.blsampleImageVOs != null && !this.blsampleImageVOs.isEmpty()) {
			return ((BLSampleImage3VO) this.blsampleImageVOs.toArray()[0]).getImageFullPath();
		}
		return null;
	}

	/**
	 * Checks the values of this value object for correctness and completeness. Should be done before persisting the
	 * data in the DB.
	 * 
	 * @param create
	 *            should be true if the value object is just being created in the DB, this avoids some checks like
	 *            testing the primary key
	 * @throws Exception
	 *             if the data of the value object is not correct
	 */
	@Override
	public void checkValues(boolean create) throws Exception {
		// TODO
		return;
	}
	
	@Override
	public String toString(){
		String s = "blSampleId="+this.blSampleId +", "+
		"name="+this.name;		
		return s;
	}
	
	public String toWSString(){
		String s = "blSampleId="+this.blSampleId +", "+
		"name="+this.name+", "+
		"code="+this.code+", "+
		"location="+this.location+", "+
		"holderLength="+this.holderLength+", "+
		"loopLength="+this.loopLength+", "+
		"loopType="+this.loopType+", "+
		"wireWidth="+this.wireWidth+", "+
		"comments="+this.comments+", "+
		"completionStage="+this.completionStage+", "+
		"structureStage="+this.structureStage+", "+
		"publicationStage="+this.publicationStage+", "+
		"publicationComments="+this.publicationComments+", "+
		"blSampleStatus="+this.blSampleStatus+", "+
		"isInSampleChanger="+this.isInSampleChanger+", "+
		"lastKnownCenteringPosition="+this.lastKnownCenteringPosition+", "+
		"smiles="+this.smiles + ", "+ "lastImageURL="+this.lastImageURL;
		
		return s;
	}
	
}
