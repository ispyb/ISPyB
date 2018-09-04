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
	
	@Column(name = "resolutionEllipsoidAxis11")
	protected Float resolutionEllipsoidAxis11;
	
	@Column(name = "resolutionEllipsoidAxis12")
	protected Float resolutionEllipsoidAxis12;
	
	@Column(name = "resolutionEllipsoidAxis13")
	protected Float resolutionEllipsoidAxis13;
	
	@Column(name = "resolutionEllipsoidAxis21")
	protected Float resolutionEllipsoidAxis21;
	
	@Column(name = "resolutionEllipsoidAxis22")
	protected Float resolutionEllipsoidAxis22;
	
	@Column(name = "resolutionEllipsoidAxis23")
	protected Float resolutionEllipsoidAxis23;
	
	@Column(name = "resolutionEllipsoidAxis31")
	protected Float resolutionEllipsoidAxis31;
	
	@Column(name = "resolutionEllipsoidAxis32")
	protected Float resolutionEllipsoidAxis32;
	
	@Column(name = "resolutionEllipsoidAxis33")
	protected Float resolutionEllipsoidAxis33;
	
	@Column(name = "resolutionEllipsoidValue1")
	protected Float resolutionEllipsoidValue1;
	
	@Column(name = "resolutionEllipsoidValue2")
	protected Float resolutionEllipsoidValue2;
	
	@Column(name = "resolutionEllipsoidValue3")
	protected Float resolutionEllipsoidValue3;	

	public AutoProcScaling3VO(){
		super();
	}
		
	public AutoProcScaling3VO(Integer autoProcScalingId,
			AutoProc3VO autoProcVO, Date recordTimeStamp, 
			Float resolutionEllipsoidAxis11, Float resolutionEllipsoidAxis12, Float resolutionEllipsoidAxis13, 
			Float resolutionEllipsoidAxis21, Float resolutionEllipsoidAxis22, Float resolutionEllipsoidAxis23, 
			Float resolutionEllipsoidAxis31, Float resolutionEllipsoidAxis32, Float resolutionEllipsoidAxis33,
			Float resolutionEllipsoidValue1, Float resolutionEllipsoidValue2, Float resolutionEllipsoidValue3) {
		super();
		this.autoProcScalingId = autoProcScalingId;
		this.autoProcVO = autoProcVO;
		this.recordTimeStamp = recordTimeStamp;
		this.resolutionEllipsoidAxis11 = resolutionEllipsoidAxis11;
		this.resolutionEllipsoidAxis12 = resolutionEllipsoidAxis12;
		this.resolutionEllipsoidAxis13 = resolutionEllipsoidAxis13;
		this.resolutionEllipsoidAxis21 = resolutionEllipsoidAxis21;
		this.resolutionEllipsoidAxis22 = resolutionEllipsoidAxis22;
		this.resolutionEllipsoidAxis23 = resolutionEllipsoidAxis23;
		this.resolutionEllipsoidAxis31 = resolutionEllipsoidAxis31;
		this.resolutionEllipsoidAxis32 = resolutionEllipsoidAxis32;
		this.resolutionEllipsoidAxis33 = resolutionEllipsoidAxis33;
		this.resolutionEllipsoidValue1 =  resolutionEllipsoidValue1;
		this.resolutionEllipsoidValue2 =  resolutionEllipsoidValue2;
		this.resolutionEllipsoidValue3 =  resolutionEllipsoidValue3;
	}
	
	public AutoProcScaling3VO(AutoProcScaling3VO vo) {
		super();
		this.autoProcScalingId = vo.getAutoProcScalingId();
		this.autoProcVO = vo.getAutoProcVO();
		this.recordTimeStamp = vo.getRecordTimeStamp();
		this.resolutionEllipsoidAxis11 = vo.getResolutionEllipsoidAxis11();
		this.resolutionEllipsoidAxis12 = vo.getResolutionEllipsoidAxis12();
		this.resolutionEllipsoidAxis13 = vo.getResolutionEllipsoidAxis13();
		this.resolutionEllipsoidAxis21 = vo.getResolutionEllipsoidAxis21();
		this.resolutionEllipsoidAxis22 = vo.getResolutionEllipsoidAxis22();
		this.resolutionEllipsoidAxis23 = vo.getResolutionEllipsoidAxis23();
		this.resolutionEllipsoidAxis31 = vo.getResolutionEllipsoidAxis31();
		this.resolutionEllipsoidAxis32 = vo.getResolutionEllipsoidAxis32();
		this.resolutionEllipsoidAxis33 = vo.getResolutionEllipsoidAxis33();
		this.resolutionEllipsoidValue1 =  vo.getResolutionEllipsoidValue1();
		this.resolutionEllipsoidValue2 =  vo.getResolutionEllipsoidValue2();
		this.resolutionEllipsoidValue3 =  vo.getResolutionEllipsoidValue3();

	}
	
	public void fillVOFromWS(AutoProcScalingWS3VO vo) {
		this.autoProcScalingId = vo.getAutoProcScalingId();
		this.autoProcVO = null;
		this.recordTimeStamp = vo.getRecordTimeStamp();
		this.resolutionEllipsoidAxis11 = vo.getResolutionEllipsoidAxis11();
		this.resolutionEllipsoidAxis12 = vo.getResolutionEllipsoidAxis12();
		this.resolutionEllipsoidAxis13 = vo.getResolutionEllipsoidAxis13();
		this.resolutionEllipsoidAxis21 = vo.getResolutionEllipsoidAxis21();
		this.resolutionEllipsoidAxis22 = vo.getResolutionEllipsoidAxis22();
		this.resolutionEllipsoidAxis23 = vo.getResolutionEllipsoidAxis23();
		this.resolutionEllipsoidAxis31 = vo.getResolutionEllipsoidAxis31();
		this.resolutionEllipsoidAxis32 = vo.getResolutionEllipsoidAxis32();
		this.resolutionEllipsoidAxis33 = vo.getResolutionEllipsoidAxis33();
		this.resolutionEllipsoidValue1 =  vo.getResolutionEllipsoidValue1();
		this.resolutionEllipsoidValue2 =  vo.getResolutionEllipsoidValue2();
		this.resolutionEllipsoidValue3 =  vo.getResolutionEllipsoidValue3();

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

	public Float getResolutionEllipsoidAxis11() {
		return resolutionEllipsoidAxis11;
	}

	public void setResolutionEllipsoidAxis11(Float resolutionEllipsoidAxis11) {
		this.resolutionEllipsoidAxis11 = resolutionEllipsoidAxis11;
	}

	public Float getResolutionEllipsoidAxis12() {
		return resolutionEllipsoidAxis12;
	}

	public void setResolutionEllipsoidAxis12(Float resolutionEllipsoidAxis12) {
		this.resolutionEllipsoidAxis12 = resolutionEllipsoidAxis12;
	}

	public Float getResolutionEllipsoidAxis13() {
		return resolutionEllipsoidAxis13;
	}

	public void setResolutionEllipsoidAxis13(Float resolutionEllipsoidAxis13) {
		this.resolutionEllipsoidAxis13 = resolutionEllipsoidAxis13;
	}

	public Float getResolutionEllipsoidAxis21() {
		return resolutionEllipsoidAxis21;
	}

	public void setResolutionEllipsoidAxis21(Float resolutionEllipsoidAxis21) {
		this.resolutionEllipsoidAxis21 = resolutionEllipsoidAxis21;
	}

	public Float getResolutionEllipsoidAxis22() {
		return resolutionEllipsoidAxis22;
	}

	public void setResolutionEllipsoidAxis22(Float resolutionEllipsoidAxis22) {
		this.resolutionEllipsoidAxis22 = resolutionEllipsoidAxis22;
	}

	public Float getResolutionEllipsoidAxis23() {
		return resolutionEllipsoidAxis23;
	}

	public void setResolutionEllipsoidAxis23(Float resolutionEllipsoidAxis23) {
		this.resolutionEllipsoidAxis23 = resolutionEllipsoidAxis23;
	}

	public Float getResolutionEllipsoidAxis31() {
		return resolutionEllipsoidAxis31;
	}

	public void setResolutionEllipsoidAxis31(Float resolutionEllipsoidAxis31) {
		this.resolutionEllipsoidAxis31 = resolutionEllipsoidAxis31;
	}

	public Float getResolutionEllipsoidAxis32() {
		return resolutionEllipsoidAxis32;
	}

	public void setResolutionEllipsoidAxis32(Float resolutionEllipsoidAxis32) {
		this.resolutionEllipsoidAxis32 = resolutionEllipsoidAxis32;
	}

	public Float getResolutionEllipsoidAxis33() {
		return resolutionEllipsoidAxis33;
	}

	public void setResolutionEllipsoidAxis33(Float resolutionEllipsoidAxis33) {
		this.resolutionEllipsoidAxis33 = resolutionEllipsoidAxis33;
	}

	public Float getResolutionEllipsoidValue1() {
		return resolutionEllipsoidValue1;
	}

	public void setResolutionEllipsoidValue1(Float resolutionEllipsoidValue1) {
		this.resolutionEllipsoidValue1 = resolutionEllipsoidValue1;
	}

	public Float getResolutionEllipsoidValue2() {
		return resolutionEllipsoidValue2;
	}

	public void setResolutionEllipsoidValue2(Float resolutionEllipsoidValue2) {
		this.resolutionEllipsoidValue2 = resolutionEllipsoidValue2;
	}

	public Float getResolutionEllipsoidValue3() {
		return resolutionEllipsoidValue3;
	}

	public void setResolutionEllipsoidValue3(Float resolutionEllipsoidValue3) {
		this.resolutionEllipsoidValue3 = resolutionEllipsoidValue3;
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

