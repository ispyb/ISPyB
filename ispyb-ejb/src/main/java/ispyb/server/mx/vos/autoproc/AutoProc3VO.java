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
import javax.persistence.EntityResult;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * AutoProc3 value object mapping table AutoProc
 * 
 */
@Entity
@Table(name = "AutoProc")
@SqlResultSetMapping(name = "autoProcNativeQuery", entities = { @EntityResult(entityClass = AutoProc3VO.class) })
public class AutoProc3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(AutoProc3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "autoProcId")
	protected Integer autoProcId;

	@ManyToOne
	@JoinColumn(name = "autoProcProgramId")
	private AutoProcProgram3VO autoProcProgramVO;

	@Column(name = "spaceGroup")
	protected String spaceGroup;

	@Column(name = "refinedCell_a")
	protected Float refinedCellA;

	@Column(name = "refinedCell_b")
	protected Float refinedCellB;

	@Column(name = "refinedCell_c")
	protected Float refinedCellC;

	@Column(name = "refinedCell_alpha")
	protected Float refinedCellAlpha;

	@Column(name = "refinedCell_beta")
	protected Float refinedCellBeta;

	@Column(name = "refinedCell_gamma")
	protected Float refinedCellGamma;

	@Column(name = "recordTimeStamp")
	protected Date recordTimeStamp;

	public AutoProc3VO() {
		super();
	}

	public AutoProc3VO(Integer autoProcId, AutoProcProgram3VO autoProcProgramVO, String spaceGroup, Float refinedCellA,
			Float refinedCellB, Float refinedCellC, Float refinedCellAlpha, Float refinedCellBeta,
			Float refinedCellGamma, Date recordTimeStamp) {
		super();
		this.autoProcId = autoProcId;
		this.autoProcProgramVO = autoProcProgramVO;
		this.spaceGroup = spaceGroup;
		this.refinedCellA = refinedCellA;
		this.refinedCellB = refinedCellB;
		this.refinedCellC = refinedCellC;
		this.refinedCellAlpha = refinedCellAlpha;
		this.refinedCellBeta = refinedCellBeta;
		this.refinedCellGamma = refinedCellGamma;
		this.recordTimeStamp = recordTimeStamp;
	}

	public AutoProc3VO(AutoProc3VO vo) {
		super();
		this.autoProcId = vo.getAutoProcId();
		this.autoProcProgramVO = vo.getAutoProcProgramVO();
		this.spaceGroup = vo.getSpaceGroup();
		this.refinedCellA = vo.getRefinedCellA();
		this.refinedCellB = vo.getRefinedCellB();
		this.refinedCellC = vo.getRefinedCellC();
		this.refinedCellAlpha = vo.getRefinedCellAlpha();
		this.refinedCellBeta = vo.getRefinedCellBeta();
		this.refinedCellGamma = vo.getRefinedCellGamma();
		this.recordTimeStamp = vo.getRecordTimeStamp();
	}

	public void fillVOFromWS(AutoProcWS3VO vo) {
		this.autoProcId = vo.getAutoProcId();
		this.autoProcProgramVO = null;
		this.spaceGroup = vo.getSpaceGroup();
		this.refinedCellA = vo.getRefinedCellA();
		this.refinedCellB = vo.getRefinedCellB();
		this.refinedCellC = vo.getRefinedCellC();
		this.refinedCellAlpha = vo.getRefinedCellAlpha();
		this.refinedCellBeta = vo.getRefinedCellBeta();
		this.refinedCellGamma = vo.getRefinedCellGamma();
		this.recordTimeStamp = vo.getRecordTimeStamp();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getAutoProcId() {
		return autoProcId;
	}

	/**
	 * @param pk
	 *            The pk to set.
	 */
	public void setAutoProcId(Integer autoProcId) {
		this.autoProcId = autoProcId;
	}

	public AutoProcProgram3VO getAutoProcProgramVO() {
		return autoProcProgramVO;
	}

	public void setAutoProcProgramVO(AutoProcProgram3VO autoProcProgramVO) {
		this.autoProcProgramVO = autoProcProgramVO;
	}

	public String getSpaceGroup() {
		return spaceGroup;
	}

	public void setSpaceGroup(String spaceGroup) {
		this.spaceGroup = spaceGroup;
	}

	public Float getRefinedCellA() {
		return refinedCellA;
	}

	public void setRefinedCellA(Float refinedCellA) {
		this.refinedCellA = refinedCellA;
	}

	public Float getRefinedCellB() {
		return refinedCellB;
	}

	public void setRefinedCellB(Float refinedCellB) {
		this.refinedCellB = refinedCellB;
	}

	public Float getRefinedCellC() {
		return refinedCellC;
	}

	public void setRefinedCellC(Float refinedCellC) {
		this.refinedCellC = refinedCellC;
	}

	public Float getRefinedCellAlpha() {
		return refinedCellAlpha;
	}

	public void setRefinedCellAlpha(Float refinedCellAlpha) {
		this.refinedCellAlpha = refinedCellAlpha;
	}

	public Float getRefinedCellBeta() {
		return refinedCellBeta;
	}

	public void setRefinedCellBeta(Float refinedCellBeta) {
		this.refinedCellBeta = refinedCellBeta;
	}

	public Float getRefinedCellGamma() {
		return refinedCellGamma;
	}

	public void setRefinedCellGamma(Float refinedCellGamma) {
		this.refinedCellGamma = refinedCellGamma;
	}

	public Date getRecordTimeStamp() {
		return recordTimeStamp;
	}

	public void setRecordTimeStamp(Date recordTimeStamp) {
		this.recordTimeStamp = recordTimeStamp;
	}

	public Integer getAutoProcProgramVOId() {
		return autoProcProgramVO == null ? null : autoProcProgramVO.getAutoProcProgramId();
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
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
	public String toWSString(){
		String s = "autoProcId="+this.autoProcId +", "+
		"spaceGroup="+this.spaceGroup+", "+
		"refinedCellA="+this.refinedCellA+", "+
		"refinedCellB="+this.refinedCellB+", "+
		"refinedCellC="+this.refinedCellC+", "+
		"refinedCellAlpha="+this.refinedCellAlpha+", "+
		"refinedCellBeta="+this.refinedCellBeta+", "+
		"refinedCellGamma="+this.refinedCellGamma+", "+
		"recordTimeStamp="+this.recordTimeStamp;
		
		return s;
	}

}
