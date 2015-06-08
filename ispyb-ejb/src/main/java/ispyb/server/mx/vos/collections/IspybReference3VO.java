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

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import ispyb.server.common.vos.ISPyBValueObject;

import org.apache.log4j.Logger;

/**
 * IspybReference3 value object mapping table IspybReference
 * 
 */
@Entity
@Table(name = "IspybReference")
public class IspybReference3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(IspybReference3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "referenceId")
	protected Integer referenceId;

	@Column(name = "referenceName")
	protected String referenceName;
	
	@Column(name = "referenceUrl")
	protected String referenceUrl;
	
	@Column(name = "referenceBibtext")
	protected Blob referenceBibtext;
	
	@Column(name = "beamline")
	protected String beamline;
	
	
	public IspybReference3VO() {
		super();
	}

	public IspybReference3VO(Integer referenceId, String referenceName,
			String referenceUrl, Blob referenceBibtext, String beamline) {
		super();
		this.referenceId = referenceId;
		this.referenceName = referenceName;
		this.referenceUrl = referenceUrl;
		this.referenceBibtext = referenceBibtext;
		this.beamline = beamline;
	}

	public Integer getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Integer referenceId) {
		this.referenceId = referenceId;
	}

	public String getReferenceName() {
		return referenceName;
	}

	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}

	public String getReferenceUrl() {
		return referenceUrl;
	}

	public void setReferenceUrl(String referenceUrl) {
		this.referenceUrl = referenceUrl;
	}

	public Blob getReferenceBibtext() {
		return referenceBibtext;
	}

	public void setReferenceBibtext(Blob referenceBibtext) {
		this.referenceBibtext = referenceBibtext;
	}

	public String getBeamline() {
		return beamline;
	}

	public void setBeamline(String beamline) {
		this.beamline = beamline;
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

	/**
	 * used to clone an entity to set the linked collectio9ns to null, for webservices
	 */
	@Override
	public IspybReference3VO clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (IspybReference3VO) super.clone();
	}
}
