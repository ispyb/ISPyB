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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * IspybAutoProcAttachment3 value object mapping table IspybAutoProcAttachment
 * 
 */
@Entity
@Table(name = "IspybAutoProcAttachment")
public class IspybAutoProcAttachment3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(IspybAutoProcAttachment3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "autoProcAttachmentId")
	private Integer autoProcAttachmentId;
	
	@Column(name = "fileName")
	protected String fileName;
	
	@Column(name = "description")
	protected String description;
	
	@Column(name = "step")
	protected String step;
	
	@Column(name = "fileCategory")
	protected String fileCategory;
	
	@Column(name = "hasGraph")
	protected Boolean hasGraph;

	public IspybAutoProcAttachment3VO() {
		super();
	}

	
	public IspybAutoProcAttachment3VO(Integer autoProcAttachmentId,
			String fileName, String description, String step,
			String fileCategory, Boolean hasGraph) {
		super();
		this.autoProcAttachmentId = autoProcAttachmentId;
		this.fileName = fileName;
		this.description = description;
		this.step = step;
		this.fileCategory = fileCategory;
		this.hasGraph = hasGraph;
	}


	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getAutoProcAttachmentId() {
		return autoProcAttachmentId;
	}

	/**
	 * @param pk The pk to set.
	 */
	public void setAutoProcAttachmentId(Integer autoProcAttachmentId) {
		this.autoProcAttachmentId = autoProcAttachmentId;
	}

	

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public String getFileCategory() {
		return fileCategory;
	}

	public void setFileCategory(String fileCategory) {
		this.fileCategory = fileCategory;
	}

	public Boolean getHasGraph() {
		return hasGraph;
	}

	public void setHasGraph(Boolean hasGraph) {
		this.hasGraph = hasGraph;
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
}
