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

import org.apache.log4j.Logger;

/**
 * XmlSchema3 value object mapping table XmlSchema
 * 
 */
@Entity
@Table(name = "XmlSchema")
public class XmlSchema3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(XmlSchema3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "xmlSchemaId")
	private Integer xmlSchemaId;

	@Column(name = "description")
	private String description;
	
	@Column(name = "schemaxml")
	private String schemaxml;

	
	
	public XmlSchema3VO() {
		super();
	}

	public XmlSchema3VO(Integer xmlSchemaId, String description,
			String schemaxml) {
		super();
		this.xmlSchemaId = xmlSchemaId;
		this.description = description;
		this.schemaxml = schemaxml;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	/**
	 * @return Returns the pk.
	 */
	public Integer getXmlSchemaId() {
		return xmlSchemaId;
	}

	/**
	 * @param pk The pk to set.
	 */
	public void setXmlSchemaId(Integer xmlSchemaId) {
		this.xmlSchemaId = xmlSchemaId;
	}

	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSchemaxml() {
		return schemaxml;
	}

	public void setSchemaxml(String schemaxml) {
		this.schemaxml = schemaxml;
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
		String s = "xmlSchemaId="+this.xmlSchemaId +", "+
		"description="+this.description+", "+
		"schemaxml="+this.schemaxml;
		
		return s;
	}
}
