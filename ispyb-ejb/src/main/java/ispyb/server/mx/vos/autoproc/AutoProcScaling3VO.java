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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * AutoProcScaling3 value object mapping table AutoProcScaling
 * 
 */
@Entity
@Table(name = "AutoProcScaling")
public class AutoProcScaling3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(AutoProcScaling3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "autoProcScalingId")
	protected Integer autoProcScalingId;
	
	@ManyToOne
	@JoinColumn(name = "autoProcId")
	private AutoProc3VO autoProcVO;
	
	@Column(name = "recordTimeStamp")
	protected Date recordTimeStamp;
	

	public AutoProcScaling3VO(){
		super();
	}
	
	
	public AutoProcScaling3VO(Integer autoProcScalingId,
			AutoProc3VO autoProcVO, Date recordTimeStamp) {
		super();
		this.autoProcScalingId = autoProcScalingId;
		this.autoProcVO = autoProcVO;
		this.recordTimeStamp = recordTimeStamp;
	}
	
	public AutoProcScaling3VO(AutoProcScaling3VO vo) {
		super();
		this.autoProcScalingId = vo.getAutoProcScalingId();
		this.autoProcVO = vo.getAutoProcVO();
		this.recordTimeStamp = vo.getRecordTimeStamp();
	}
	
	public void fillVOFromWS(AutoProcScalingWS3VO vo) {
		this.autoProcScalingId = vo.getAutoProcScalingId();
		this.autoProcVO = null;
		this.recordTimeStamp = vo.getRecordTimeStamp();
	}



	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	/**
	 * @return Returns the pk.
	 */
	public Integer getAutoProcScalingId() {
		return autoProcScalingId;
	}

	/**
	 * @param pk The pk to set.
	 */
	public void setAutoProcScalingId(Integer autoProcScalingId) {
		this.autoProcScalingId = autoProcScalingId;
	}

	

	public AutoProc3VO getAutoProcVO() {
		return autoProcVO;
	}




	public void setAutoProcVO(AutoProc3VO autoProcVO) {
		this.autoProcVO = autoProcVO;
	}




	public Date getRecordTimeStamp() {
		return recordTimeStamp;
	}

	public void setRecordTimeStamp(Date recordTimeStamp) {
		this.recordTimeStamp = recordTimeStamp;
	}

	public Integer getAutoProcVOId() {
		return autoProcVO == null ? null: autoProcVO.getAutoProcId();
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
		String s = "autoProcScalingId="+this.autoProcScalingId +", "+
		"recordTimeStamp="+this.recordTimeStamp;
		
		return s;
	}
	
	
	

}

