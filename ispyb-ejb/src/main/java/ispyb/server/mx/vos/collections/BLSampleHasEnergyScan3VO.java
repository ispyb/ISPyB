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

package ispyb.server.mx.vos.collections;

import ispyb.common.util.StringUtils;
import ispyb.server.common.vos.ISPyBValueObject;
import ispyb.server.mx.vos.sample.BLSample3VO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * BLSampleHasEnergyScan3 value object mapping table BLSample_has_EnergyScan
 * 
 */
@Entity
@Table(name = "BLSample_has_EnergyScan")
public class BLSampleHasEnergyScan3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(BLSampleHasEnergyScan3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "blSampleHasEnergyScanId")
	protected Integer blSampleHasEnergyScanId;
	
	@ManyToOne
	@JoinColumn(name = "blSampleId")
	private BLSample3VO blSampleVO;
	
	@ManyToOne
	@JoinColumn(name = "energyScanId")
	private EnergyScan3VO energyScanVO;

	

	public BLSampleHasEnergyScan3VO() {
		super();
	}

	

	public BLSampleHasEnergyScan3VO(Integer blSampleHasEnergyScanId,
			BLSample3VO blSampleVO, EnergyScan3VO energyScanVO) {
		super();
		this.blSampleHasEnergyScanId = blSampleHasEnergyScanId;
		this.blSampleVO = blSampleVO;
		this.energyScanVO = energyScanVO;
	}
	
	public BLSampleHasEnergyScan3VO(BLSampleHasEnergyScan3VO vo) {
		super();
		this.blSampleHasEnergyScanId = vo.getBlSampleHasEnergyScanId();
		this.blSampleVO = vo.getBlSampleVO();
		this.energyScanVO = vo.getEnergyScanVO();
	}
	
	public void fillVOFromWS(BLSampleHasEnergyScanWS3VO vo) {
		this.blSampleHasEnergyScanId = vo.getBlSampleHasEnergyScanId();
		this.blSampleVO = null;
		this.energyScanVO = null;
	}




	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
	
	/**
	 * @return Returns the pk.
	 */
	public Integer getBlSampleHasEnergyScanId() {
		return blSampleHasEnergyScanId;
	}

	/**
	 * @param pk The pk to set.
	 */
	public void setBlSampleHasEnergyScanId(Integer blSampleHasEnergyScanId) {
		this.blSampleHasEnergyScanId = blSampleHasEnergyScanId;
	}

	

	public BLSample3VO getBlSampleVO() {
		return blSampleVO;
	}



	public void setBlSampleVO(BLSample3VO blSampleVO) {
		this.blSampleVO = blSampleVO;
	}



	public EnergyScan3VO getEnergyScanVO() {
		return energyScanVO;
	}



	public void setEnergyScanVO(EnergyScan3VO energyScanVO) {
		this.energyScanVO = energyScanVO;
	}



	public Integer getEnergyScanVOId(){
		return energyScanVO == null ? null:energyScanVO.getEnergyScanId();
	}
	
	public Integer getBlSampleVOId(){
		return blSampleVO == null ? null:blSampleVO.getBlSampleId();
	}
	
	/**
	 * Checks the values of this value object for correctness and
	 * completeness. Should be done before persisting the data in the DB.
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @throws Exception if the data of the value object is not correct
	 */
	public void checkValues(boolean create) throws Exception {
		//blSample
		if(this.blSampleVO == null)
			throw new IllegalArgumentException(StringUtils.getMessageRequiredField("BLSampleHasEnergyScan", "blSample"));
		//energyScan
		if(this.energyScanVO == null)
			throw new IllegalArgumentException(StringUtils.getMessageRequiredField("BLSampleHasEnergyScan", "energyScan"));
		
	}

	
}
