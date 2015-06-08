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


package ispyb.server.common.vos.proposals;

/**
 * Person class for webservices
 * @author 
 *
 */
public class PersonWS3VO extends Person3VO{

	private static final long serialVersionUID = -1177196558815844147L;
	
	private Integer laboratoryId;

	public PersonWS3VO() {
		super();
	}

	public PersonWS3VO(Integer laboratoryId) {
		super();
		this.laboratoryId = laboratoryId;
	}
	
	public PersonWS3VO(Person3VO vo){
		super(vo);
	}

	public Integer getLaboratoryId() {
		return laboratoryId;
	}

	public void setLaboratoryId(Integer laboratoryId) {
		this.laboratoryId = laboratoryId;
	}
	
	
}
