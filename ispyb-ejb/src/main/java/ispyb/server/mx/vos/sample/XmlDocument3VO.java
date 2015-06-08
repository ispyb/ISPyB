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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * XmlDocument3 value object mapping table XmlDocument
 * 
 */
@Entity
@Table(name = "XmlDocument")
public class XmlDocument3VO extends ISPyBValueObject implements Cloneable {


	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "xmlDocumentId")
	private Integer xmlDocumentId;

	@Column(name = "xmlSchemaId")
	private Integer xmlSchemaId;
	
	@Column(name = "xmldata")
	private String xmldata;

	
	public XmlDocument3VO() {
		super();
	}

	public XmlDocument3VO(Integer xmlDocumentId, Integer xmlSchemaId,
			String xmldata) {
		super();
		this.xmlDocumentId = xmlDocumentId;
		this.xmlSchemaId = xmlSchemaId;
		this.xmldata = xmldata;
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getXmlDocumentId() {
		return xmlDocumentId;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @param pk The pk to set.
	 */
	public void setXmlDocumentId(Integer xmlDocumentId) {
		this.xmlDocumentId = xmlDocumentId;
	}

	

	public Integer getXmlSchemaId() {
		return xmlSchemaId;
	}

	public void setXmlSchemaId(Integer xmlSchemaId) {
		this.xmlSchemaId = xmlSchemaId;
	}

	public String getXmldata() {
		return xmldata;
	}

	public void setXmldata(String xmldata) {
		this.xmldata = xmldata;
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
		String s = "xmlDocumentId="+this.xmlDocumentId +", "+
		"xmlSchemaId="+this.xmlSchemaId+", "+
		"xmldata="+this.xmldata;
		
		return s;
	}
}
