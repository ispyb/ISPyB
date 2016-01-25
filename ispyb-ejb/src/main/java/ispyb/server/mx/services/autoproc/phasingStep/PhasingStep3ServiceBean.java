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

package ispyb.server.mx.services.autoproc.phasingStep;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ispyb.server.mx.vos.autoproc.PhasingStepVO;


@Stateless
public class PhasingStep3ServiceBean implements PhasingStep3Service,  PhasingStep3ServiceLocal {

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@Override
	public PhasingStepVO findById(Integer id) {
		try {
			return  entityManager.find(PhasingStepVO.class, id);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public void persist(PhasingStepVO phasingStep) {
		try {
			entityManager.persist(phasingStep);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public PhasingStepVO merge(PhasingStepVO phasingStep) {
		return entityManager.merge(phasingStep);
	}
	
}
