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
 ******************************************************************************************************************************/

package ispyb.server.biosaxs.services.core.specimen;

import ispyb.server.biosaxs.services.sql.SQLQueryKeeper;
import ispyb.server.biosaxs.vos.dataAcquisition.Specimen3VO;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class Specimen3ServiceBean implements Specimen3Service, Specimen3ServiceLocal {

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@Override
	public Specimen3VO findSpecimenById(int specimenId) {
		String query = SQLQueryKeeper.getSpecimenById(specimenId);
		Query EJBQuery = this.entityManager.createQuery(query);
		Specimen3VO Specimen3VO = (Specimen3VO) EJBQuery.getSingleResult();	
		return Specimen3VO;
	}

	@Override
	public void remove(Specimen3VO specimen3vo) {
		this.entityManager.remove(this.findSpecimenById(specimen3vo.getSpecimenId()));
	}
	
	@Override
	public Specimen3VO merge(Specimen3VO specimen3vo) {
		return entityManager.merge(specimen3vo);
	}
}
