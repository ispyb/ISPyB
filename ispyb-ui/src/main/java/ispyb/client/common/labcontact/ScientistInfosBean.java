/*******************************************************************************
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
 ******************************************************************************/
package ispyb.client.common.labcontact;

import ispyb.server.common.vos.proposals.Laboratory3VO;
import ispyb.server.common.vos.proposals.Person3VO;

public class ScientistInfosBean {

	private Person3VO person = null;
	private Laboratory3VO laboratory = null;
	
	public ScientistInfosBean(Person3VO person, Laboratory3VO currentLabo)
	{
		this.person = person;
		
		this.laboratory = currentLabo;
	}

	public Person3VO getPerson() {
		return person;
	}

	public void setPerson(Person3VO person) {
		this.person = person;
	}

	public Laboratory3VO getLaboratory() {
		return laboratory;
	}

	public void setLaboratory(Laboratory3VO laboratory) {
		this.laboratory = laboratory;
	}
	
}
