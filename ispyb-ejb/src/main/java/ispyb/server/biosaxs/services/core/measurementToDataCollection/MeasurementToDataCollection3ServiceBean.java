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

package ispyb.server.biosaxs.services.core.measurementToDataCollection;


import java.util.List;

import ispyb.server.biosaxs.services.sql.SQLQueryKeeper;
import ispyb.server.biosaxs.vos.datacollection.MeasurementTodataCollection3VO;
import ispyb.server.biosaxs.vos.datacollection.SaxsDataCollection3VO;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

@Stateless
public class MeasurementToDataCollection3ServiceBean implements MeasurementToDataCollection3Service, MeasurementToDatacollection3ServiceLocal {

	private final static Logger log = Logger.getLogger(MeasurementToDataCollection3ServiceBean.class);
	
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public void persist(MeasurementTodataCollection3VO transientInstance) {
		log.debug("persisting Sampleplatewell3VO instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}


	@Override
	public MeasurementTodataCollection3VO merge(MeasurementTodataCollection3VO detachedInstance) {
		log.debug("merging Sampleplatewell3VO instance");
		try {
			MeasurementTodataCollection3VO result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	@Override
	public MeasurementTodataCollection3VO findById(int id) {
		log.debug("getting Sampleplatewell3VO instance with id: " + id);
		try {
			MeasurementTodataCollection3VO instance = entityManager.find(MeasurementTodataCollection3VO.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	@Override
	public List<MeasurementTodataCollection3VO> findByMeasurementId(int measurementId) {
		try {
			String querySQL = SQLQueryKeeper.getMeasurementToDataCollectionByMeasurementId(measurementId);
			TypedQuery<MeasurementTodataCollection3VO> query = entityManager.createQuery(querySQL, MeasurementTodataCollection3VO.class);
			return query.getResultList();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	


	@Override
	public SaxsDataCollection3VO findDataCollectionById(int dataCollectionId) {
		try {
			SaxsDataCollection3VO instance = entityManager.find(SaxsDataCollection3VO.class, dataCollectionId);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<MeasurementTodataCollection3VO> findMeasurementToDataCollectionByDataCollectionId(Integer dataCollectionId) {
		try {
			String querySQL = SQLQueryKeeper.findMeasurementToDataCollectionByDataCollectionId(dataCollectionId);
			TypedQuery<MeasurementTodataCollection3VO> query = entityManager.createQuery(querySQL, MeasurementTodataCollection3VO.class);
			return query.getResultList();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@Override
	public void remove(MeasurementTodataCollection3VO detachedInstance) {
		try {
			MeasurementTodataCollection3VO result = entityManager.merge(detachedInstance);
			this.entityManager.remove(result);
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@Override
	public void remove(SaxsDataCollection3VO dataCollection) {
		SaxsDataCollection3VO result = entityManager.merge(dataCollection);
		this.entityManager.remove(result);
	}
}
