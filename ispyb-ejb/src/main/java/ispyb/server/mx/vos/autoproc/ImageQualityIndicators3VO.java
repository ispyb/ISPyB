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
import ispyb.server.mx.vos.collections.Image3VO;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * ImageQualityIndicators3 value object mapping table ImageQualityIndicators
 * 
 */
@Entity
@Table(name = "ImageQualityIndicators")
@SqlResultSetMapping(name = "imageQualityIndicatorsNativeQuery", entities = { @EntityResult(entityClass = ImageQualityIndicators3VO.class) })
public class ImageQualityIndicators3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(ImageQualityIndicators3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "imageQualityIndicatorsId")
	protected Integer imageQualityIndicatorsId;
	
	@ManyToOne
	@JoinColumn(name = "imageId")
	private Image3VO imageVO;
	
	@ManyToOne
	@JoinColumn(name = "autoProcProgramId")
	private AutoProcProgram3VO autoProcProgramVO;
	
	@Column(name = "spotTotal")
	protected Integer spotTotal;
	
	@Column(name = "inResTotal")
	protected Integer inResTotal;
	
	@Column(name = "goodBraggCandidates")
	protected Integer goodBraggCandidates;
	
	@Column(name = "iceRings")
	protected Integer iceRings;
	
	@Column(name = "method1Res")
	protected Float method1Res;
	
	@Column(name = "method2Res")
	protected Float method2Res;
	
	@Column(name = "maxUnitCell")
	protected Float maxUnitCell;
	
	@Column(name = "pctSaturationTop50Peaks")
	protected Float pctSaturationTop50Peaks;
	
	@Column(name = "inResolutionOvrlSpots")
	protected Integer inResolutionOvrlSpots;

	@Column(name = "binPopCutOffMethod2Res")
	protected Float binPopCutOffMethod2Res;
	
	@Column(name = "recordTimeStamp")
	protected Date recordTimeStamp;
	
	@Column(name = "totalIntegratedSignal")
	protected Double totalIntegratedSignal;
	
	@Column(name = "dozor_score")
	protected Double dozor_score;
	
	public ImageQualityIndicators3VO() {
		super();
	}

	
	
	public ImageQualityIndicators3VO(Integer imageQualityIndicatorsId,
			Image3VO imageVO, AutoProcProgram3VO autoProcProgramVO,
			Integer spotTotal, Integer inResTotal, Integer goodBraggCandidates,
			Integer iceRings, Float method1Res, Float method2Res,
			Float maxUnitCell, Float pctSaturationTop50Peaks,
			Integer inResolutionOvrlSpots, Float binPopCutOffMethod2Res,
			Date recordTimeStamp, Double totalIntegratedSignal, Double dozor_score) {
		super();
		this.imageQualityIndicatorsId = imageQualityIndicatorsId;
		this.imageVO = imageVO;
		this.autoProcProgramVO = autoProcProgramVO;
		this.spotTotal = spotTotal;
		this.inResTotal = inResTotal;
		this.goodBraggCandidates = goodBraggCandidates;
		this.iceRings = iceRings;
		this.method1Res = method1Res;
		this.method2Res = method2Res;
		this.maxUnitCell = maxUnitCell;
		this.pctSaturationTop50Peaks = pctSaturationTop50Peaks;
		this.inResolutionOvrlSpots = inResolutionOvrlSpots;
		this.binPopCutOffMethod2Res = binPopCutOffMethod2Res;
		this.recordTimeStamp = recordTimeStamp;
		this.totalIntegratedSignal = totalIntegratedSignal;
		this.dozor_score = dozor_score;
	}



	public ImageQualityIndicators3VO(ImageQualityIndicators3VO vo) {
		super();
		this.imageQualityIndicatorsId = vo.getImageQualityIndicatorsId();
		this.imageVO = vo.getImageVO();
		this.autoProcProgramVO = vo.getAutoProcProgramVO();
		this.spotTotal = vo.getSpotTotal();
		this.inResTotal = vo.getInResTotal();
		this.goodBraggCandidates = vo.getGoodBraggCandidates();
		this.iceRings = vo.getIceRings();
		this.method1Res = vo.getMethod1Res();
		this.method2Res = vo.getMethod2Res();
		this.maxUnitCell = vo.getMaxUnitCell();
		this.pctSaturationTop50Peaks = vo.getPctSaturationTop50Peaks();
		this.inResolutionOvrlSpots = vo.getInResolutionOvrlSpots();
		this.binPopCutOffMethod2Res = vo.getBinPopCutOffMethod2Res();
		this.recordTimeStamp = vo.getRecordTimeStamp();
		this.totalIntegratedSignal = vo.getTotalIntegratedSignal();
		this.dozor_score = vo.getDozor_score();
	}
	
	public void fillVOFromWS(ImageQualityIndicatorsWS3VO vo) {
		this.imageQualityIndicatorsId = vo.getImageQualityIndicatorsId();
		this.imageVO = null;
		this.autoProcProgramVO = null;
		this.spotTotal = vo.getSpotTotal();
		this.inResTotal = vo.getInResTotal();
		this.goodBraggCandidates = vo.getGoodBraggCandidates();
		this.iceRings = vo.getIceRings();
		this.method1Res = vo.getMethod1Res();
		this.method2Res = vo.getMethod2Res();
		this.maxUnitCell = vo.getMaxUnitCell();
		this.pctSaturationTop50Peaks = vo.getPctSaturationTop50Peaks();
		this.inResolutionOvrlSpots = vo.getInResolutionOvrlSpots();
		this.binPopCutOffMethod2Res = vo.getBinPopCutOffMethod2Res();
		this.recordTimeStamp = vo.getRecordTimeStamp();
		this.totalIntegratedSignal = vo.getTotalIntegratedSignal();
		this.dozor_score = vo.getDozor_score();
	}


	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
	
	/**
	 * @return Returns the pk.
	 */
	public Integer getImageQualityIndicatorsId() {
		return imageQualityIndicatorsId;
	}

	/**
	 * @param pk The pk to set.
	 */
	public void setImageQualityIndicatorsId(Integer imageQualityIndicatorsId) {
		this.imageQualityIndicatorsId = imageQualityIndicatorsId;
	}

	

	public Image3VO getImageVO() {
		return imageVO;
	}

	public void setImageVO(Image3VO imageVO) {
		this.imageVO = imageVO;
	}


	public AutoProcProgram3VO getAutoProcProgramVO() {
		return autoProcProgramVO;
	}

	public void setAutoProcProgramVO(AutoProcProgram3VO autoProcProgramVO) {
		this.autoProcProgramVO = autoProcProgramVO;
	}

	public Integer getImageVOId() {
		return imageVO == null ? null : imageVO.getImageId();
	}
	public Integer getAutoProcProgramVOId() {
		return  autoProcProgramVO == null ? null : autoProcProgramVO.getAutoProcProgramId();  
	}

	public Integer getSpotTotal() {
		return spotTotal;
	}

	public void setSpotTotal(Integer spotTotal) {
		this.spotTotal = spotTotal;
	}

	public Integer getInResTotal() {
		return inResTotal;
	}

	public void setInResTotal(Integer inResTotal) {
		this.inResTotal = inResTotal;
	}

	public Integer getGoodBraggCandidates() {
		return goodBraggCandidates;
	}

	public void setGoodBraggCandidates(Integer goodBraggCandidates) {
		this.goodBraggCandidates = goodBraggCandidates;
	}

	public Integer getIceRings() {
		return iceRings;
	}

	public void setIceRings(Integer iceRings) {
		this.iceRings = iceRings;
	}

	public Float getMethod1Res() {
		return method1Res;
	}

	public void setMethod1Res(Float method1Res) {
		this.method1Res = method1Res;
	}

	public Float getMethod2Res() {
		return method2Res;
	}

	public void setMethod2Res(Float method2Res) {
		this.method2Res = method2Res;
	}

	public Float getMaxUnitCell() {
		return maxUnitCell;
	}

	public void setMaxUnitCell(Float maxUnitCell) {
		this.maxUnitCell = maxUnitCell;
	}

	public Float getPctSaturationTop50Peaks() {
		return pctSaturationTop50Peaks;
	}

	public void setPctSaturationTop50Peaks(Float pctSaturationTop50Peaks) {
		this.pctSaturationTop50Peaks = pctSaturationTop50Peaks;
	}

	public Integer getInResolutionOvrlSpots() {
		return inResolutionOvrlSpots;
	}



	public void setInResolutionOvrlSpots(Integer inResolutionOvrlSpots) {
		this.inResolutionOvrlSpots = inResolutionOvrlSpots;
	}



	public Float getBinPopCutOffMethod2Res() {
		return binPopCutOffMethod2Res;
	}

	public void setBinPopCutOffMethod2Res(Float binPopCutOffMethod2Res) {
		this.binPopCutOffMethod2Res = binPopCutOffMethod2Res;
	}

	public Date getRecordTimeStamp() {
		return recordTimeStamp;
	}

	public void setRecordTimeStamp(Date recordTimeStamp) {
		this.recordTimeStamp = recordTimeStamp;
	}

	public Double getTotalIntegratedSignal() {
		return totalIntegratedSignal;
	}

	public void setTotalIntegratedSignal(Double totalIntegratedSignal) {
		this.totalIntegratedSignal = totalIntegratedSignal;
	}


	public Double getDozor_score() {
		return dozor_score;
	}

	public void setDozor_score(Double dozor_score) {
		this.dozor_score = dozor_score;
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
		String s = "imageQualityIndicatorsId="+this.imageQualityIndicatorsId +", "+
		"spotTotal="+this.spotTotal+", "+
		"inResTotal="+this.inResTotal+", "+
		"goodBraggCandidates="+this.goodBraggCandidates+", "+
		"iceRings="+this.iceRings+", "+
		"method1Res="+this.method1Res+", "+
		"method2Res="+this.method2Res+", "+
		"maxUnitCell="+this.maxUnitCell+", "+
		"pctSaturationTop50Peaks="+this.pctSaturationTop50Peaks+", "+
		"inResolutionOvrlSpots="+this.inResolutionOvrlSpots+", "+
		"binPopCutOffMethod2Res="+this.binPopCutOffMethod2Res+", "+
		"dozor_score="+this.dozor_score+", "+
		"recordTimeStamp="+this.recordTimeStamp;
		
		return s;
	}
	
}
