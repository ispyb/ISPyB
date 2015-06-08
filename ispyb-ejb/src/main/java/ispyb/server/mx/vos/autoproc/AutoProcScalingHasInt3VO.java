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
 * AutoProcScalingHasInt3 value object mapping table AutoProcScaling_has_Int
 * 
 */
@Entity
@Table(name = "AutoProcScaling_has_Int")
public class AutoProcScalingHasInt3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(AutoProcScalingHasInt3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "autoProcScaling_has_IntId")
	protected Integer autoProcScalingHasIntId;
	
	@ManyToOne
	@JoinColumn(name = "autoProcScalingId")
	private AutoProcScaling3VO autoProcScalingVO;
	
	@ManyToOne
	@JoinColumn(name = "autoProcIntegrationId")
	private AutoProcIntegration3VO autoProcIntegrationVO;
	
	@Column(name = "recordTimeStamp")
	protected Date recordTimeStamp;

	public AutoProcScalingHasInt3VO(){
		super();
	}

	public AutoProcScalingHasInt3VO(Integer autoProcScalingHasIntId,
			AutoProcScaling3VO autoProcScalingVO,
			AutoProcIntegration3VO autoProcIntegrationVO, Date recordTimeStamp) {
		super();
		this.autoProcScalingHasIntId = autoProcScalingHasIntId;
		this.autoProcScalingVO = autoProcScalingVO;
		this.autoProcIntegrationVO = autoProcIntegrationVO;
		this.recordTimeStamp = recordTimeStamp;
	}
	
	public AutoProcScalingHasInt3VO(AutoProcScalingHasInt3VO vo) {
		super();
		this.autoProcScalingHasIntId = vo.getAutoProcScalingHasIntId();
		this.autoProcScalingVO = vo.getAutoProcScalingVO();
		this.autoProcIntegrationVO = vo.getAutoProcIntegrationVO();
		this.recordTimeStamp = vo.getRecordTimeStamp();
	}

	public void fillVOFromWS(AutoProcScalingHasIntWS3VO vo) {
		this.autoProcScalingHasIntId = vo.getAutoProcScalingHasIntId();
		this.autoProcScalingVO = null;
		this.autoProcIntegrationVO = null;
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
	public Integer getAutoProcScalingHasIntId() {
		return autoProcScalingHasIntId;
	}

	/**
	 * @param pk The pk to set.
	 */
	public void setAutoProcScalingHasIntId(Integer autoProcScalingHasIntId) {
		this.autoProcScalingHasIntId = autoProcScalingHasIntId;
	}

	

	public AutoProcScaling3VO getAutoProcScalingVO() {
		return autoProcScalingVO;
	}


	public void setAutoProcScalingVO(AutoProcScaling3VO autoProcScalingVO) {
		this.autoProcScalingVO = autoProcScalingVO;
	}

	public AutoProcIntegration3VO getAutoProcIntegrationVO() {
		return autoProcIntegrationVO;
	}


	public void setAutoProcIntegrationVO(
			AutoProcIntegration3VO autoProcIntegrationVO) {
		this.autoProcIntegrationVO = autoProcIntegrationVO;
	}
	
	public Date getRecordTimeStamp() {
		return recordTimeStamp;
	}

	public void setRecordTimeStamp(Date recordTimeStamp) {
		this.recordTimeStamp = recordTimeStamp;
	}
	
	public Integer getAutoProcScalingVOId() {
		return autoProcScalingVO == null ? null : autoProcScalingVO.getAutoProcScalingId();
	}

	public Integer getAutoProcIntegrationVOId() {
		return autoProcIntegrationVO == null ? null : autoProcIntegrationVO.getAutoProcIntegrationId();
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
		String s = "autoProcScalingHasIntId="+this.autoProcScalingHasIntId +", "+
		"recordTimeStamp="+this.recordTimeStamp;
		
		return s;
	}
}

