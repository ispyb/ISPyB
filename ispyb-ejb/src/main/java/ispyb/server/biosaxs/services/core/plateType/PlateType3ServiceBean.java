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

package ispyb.server.biosaxs.services.core.plateType;


import ispyb.server.biosaxs.vos.dataAcquisition.plate.Platetype3VO;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


@Stateless
public class PlateType3ServiceBean implements PlateType3Service,  PlateType3ServiceLocal {

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@Override
	public Platetype3VO findById(Integer id) {
		try {
			Platetype3VO instance = entityManager.find(Platetype3VO.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<Platetype3VO> findAll() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Platetype3VO> criteria = builder.createQuery(Platetype3VO.class);
		Root<Platetype3VO> projectRoot = criteria.from(Platetype3VO.class);
		criteria.select(projectRoot);
		criteria.orderBy(builder.desc(projectRoot.get("plateTypeId")));
		return this.entityManager.createQuery(criteria).getResultList();
	}
}
