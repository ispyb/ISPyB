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

package ispyb.server.biosaxs.services.core.samplePlate;

import ispyb.server.biosaxs.vos.dataAcquisition.plate.PlateGroup3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.plate.Sampleplate3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.plate.Sampleplateposition3VO;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

/**
 * Home object for domain model class Sampleplate3VO.
 * 
 * @see ispyb.server.biosaxs.vos.saxs.dataAcquisition.plate.bx.project.Sampleplate3VO
 * @author Hibernate Tools
 */
@Stateless
public class Sampleplate3ServiceBean implements Sampleplate3Service, Sampleplate3ServiceLocal {

	private final static Logger log = Logger.getLogger(Sampleplate3ServiceBean.class);

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@Override
	public Sampleplate3VO merge(Sampleplate3VO detachedInstance) {
		log.debug("merging Sampleplate3VO instance");
		try {
			Sampleplate3VO result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	@Override
	public Sampleplateposition3VO merge(Sampleplateposition3VO sampleplateposition3vo) {
		if (sampleplateposition3vo != null) {
			return this.entityManager.merge(sampleplateposition3vo);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Sampleplate3VO> getSamplePlatesByBoxId(String dewarId) {
		String query = "SELECT plate FROM Sampleplate3VO plate where plate.boxId = " + dewarId;// SQLQueryKeeper.getSamplePlatesByBoxId(dewarId);
		Query EJBQuery = this.entityManager.createQuery(query);
		List<Sampleplate3VO> samplePlates = EJBQuery.getResultList();
		return samplePlates;
	}

	@Override
	public PlateGroup3VO merge(PlateGroup3VO plateGroup) {
		try {
			PlateGroup3VO result = entityManager.merge(plateGroup);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

}
