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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ispyb.common.util.StringUtils;
import ispyb.server.common.vos.ISPyBValueObject;

import org.apache.log4j.Logger;

/**
 * AutoProcStatus3 value object mapping table AutoProcStatus
 * 
 */
@Entity
@Table(name = "AutoProcStatus")
public class AutoProcStatus3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(AutoProcStatus3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "autoProcStatusId")
	protected Integer autoProcStatusId;

	@ManyToOne
	@JoinColumn(name = "autoProcIntegrationId")
	private AutoProcIntegration3VO autoProcIntegrationVO; 
	
	@Column(name = "step")
	protected String step;
	
	@Column(name = "status")
	protected String status;
	
	@Column(name = "comments")
	protected String comments;
	
	@Column(name = "bltimeStamp")
	protected Date blTimeStamp;

	

	public AutoProcStatus3VO() {
		super();
	}
	

	public AutoProcStatus3VO(Integer autoProcStatusId,
			AutoProcIntegration3VO autoProcIntegrationVO, String step,
			String status, String comments, Date blTimeStamp) {
		super();
		this.autoProcStatusId = autoProcStatusId;
		this.autoProcIntegrationVO = autoProcIntegrationVO;
		this.step = step;
		this.status = status;
		this.comments = comments;
		this.blTimeStamp = blTimeStamp;
	}
	
	public AutoProcStatus3VO(AutoProcStatus3VO vo) {
		super();
		this.autoProcStatusId = vo.getAutoProcStatusId();
		this.autoProcIntegrationVO = vo.getAutoProcIntegrationVO();
		this.step = vo.getStep();
		this.status = vo.getStatus();
		this.comments = vo.getComments();
		this.blTimeStamp = vo.getBlTimeStamp();
	}

	public void fillVOFromWS(AutoProcStatusWS3VO vo) {
		this.autoProcStatusId = vo.getAutoProcStatusId();
		this.autoProcIntegrationVO = null;
		this.step = vo.getStep();
		this.status = vo.getStatus();
		this.comments = vo.getComments();
		this.blTimeStamp = vo.getBlTimeStamp();
	}

	public Integer getAutoProcStatusId() {
		return autoProcStatusId;
	}

	public void setAutoProcStatusId(Integer autoProcStatusId) {
		this.autoProcStatusId = autoProcStatusId;
	}

	public AutoProcIntegration3VO getAutoProcIntegrationVO() {
		return autoProcIntegrationVO;
	}

	public void setAutoProcIntegrationVO(
			AutoProcIntegration3VO autoProcIntegrationVO) {
		this.autoProcIntegrationVO = autoProcIntegrationVO;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getBlTimeStamp() {
		return blTimeStamp;
	}

	public void setBlTimeStamp(Date blTimeStamp) {
		this.blTimeStamp = blTimeStamp;
	}

	/**
	 * Checks the values of this value object for correctness and
	 * completeness. Should be done before persisting the data in the DB.
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @throws Exception if the data of the value object is not correct
	 */
	public void checkValues(boolean create) throws Exception {
		int maxLengthComments = 1024;
		
		String[] listStep = {"Indexing", "Integration", "Correction", "Scaling", "Importing"};
		String[] listStatus = {"Launched", "Successful", "Failed"};
		// autoProcIntegrationVO
		if(this.autoProcIntegrationVO == null)
			throw new IllegalArgumentException(StringUtils.getMessageRequiredField("AutoProcStatus", "autoProcIntegration"));
		// step
		if(!StringUtils.isStringInPredefinedList(this.step, listStep, false))
			throw new IllegalArgumentException(StringUtils.getMessageErrorPredefinedList("AutoProcStatus", "step", listStep));
		// status
		if(!StringUtils.isStringInPredefinedList(this.status, listStatus, false))
			throw new IllegalArgumentException(StringUtils.getMessageErrorPredefinedList("AutoProcStatus", "status", listStatus));
		// comments
		if(!StringUtils.isStringLengthValid(this.comments, maxLengthComments))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("AutoProcStatus", "comments", maxLengthComments));
		
	}

	/**
	 * used to clone an entity to set the linked collectio9ns to null, for webservices
	 */
	@Override
	public AutoProcStatus3VO clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (AutoProcStatus3VO) super.clone();
	}
	
	public String toWSString(){
		String s = "autoProcStatusId="+this.autoProcStatusId +", "+
		"step="+this.step+", "+
		"status="+this.status+", "+
		"comments="+this.comments+", "+
		"blTimeStamp="+this.blTimeStamp;
		
		return s;
	}
	
}
