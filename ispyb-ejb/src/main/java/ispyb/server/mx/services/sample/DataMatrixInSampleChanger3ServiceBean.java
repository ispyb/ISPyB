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

package ispyb.server.mx.services.sample;

// Generated Mar 20, 2012 2:49:54 PM by Hibernate Tools 3.4.0.CR1

import ispyb.server.mx.vos.sample.DataMatrixInSampleChanger3VO;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * Home object for domain model class Datamatrixinsamplechanger3VO.
 * 
 * @see .Datamatrixinsamplechanger3VO
 * @author Hibernate Tools
 */
@Stateless
public class DataMatrixInSampleChanger3ServiceBean implements DataMatrixInSampleChanger3Service,
		DataMatrixInSampleChanger3ServiceLocal {

	private static final Logger LOG = Logger.getLogger(DataMatrixInSampleChanger3ServiceBean.class);

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ispyb.server.mx.services.sample.DataMatrixInSampleChanger3Service#persist(ispyb.server.mx.vos.sample
	 * .Datamatrixinsamplechanger3VO)
	 */
	@Override
	public void persist(DataMatrixInSampleChanger3VO transientInstance) {
		LOG.debug("persisting Datamatrixinsamplechanger3VO instance");
		try {
			entityManager.persist(transientInstance);
			LOG.debug("persist successful");
		} catch (RuntimeException re) {
			LOG.error("persist failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ispyb.server.mx.services.sample.DataMatrixInSampleChanger3Service#remove(ispyb.server.mx.vos.sample
	 * .Datamatrixinsamplechanger3VO)
	 */
	@Override
	public void remove(DataMatrixInSampleChanger3VO persistentInstance) {
		LOG.debug("removing Datamatrixinsamplechanger3VO instance");
		try {
			entityManager.remove(persistentInstance);
			LOG.debug("remove successful");
		} catch (RuntimeException re) {
			LOG.error("remove failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ispyb.server.mx.services.sample.DataMatrixInSampleChanger3Service#merge(ispyb.server.mx.vos.sample
	 * .Datamatrixinsamplechanger3VO)
	 */
	@Override
	public DataMatrixInSampleChanger3VO merge(DataMatrixInSampleChanger3VO detachedInstance) {
		LOG.debug("merging Datamatrixinsamplechanger3VO instance");
		try {
			DataMatrixInSampleChanger3VO result = entityManager.merge(detachedInstance);
			LOG.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			LOG.error("merge failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ispyb.server.mx.services.sample.DataMatrixInSampleChanger3Service#findById(java.lang.Integer)
	 */
	@Override
	public DataMatrixInSampleChanger3VO findById(Integer id) {
		LOG.debug("getting Datamatrixinsamplechanger3VO instance with id: " + id);
		try {
			DataMatrixInSampleChanger3VO instance = entityManager.find(DataMatrixInSampleChanger3VO.class, id);
			LOG.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			LOG.error("get failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DataMatrixInSampleChanger3VO> findByProposalIdAndBeamlineName(Integer proposalId, String beamLineName) {
		Session session = (Session) this.entityManager.getDelegate();
		return session.createCriteria(DataMatrixInSampleChanger3VO.class)
				.add(Restrictions.eq("proposalId", proposalId)).add(Restrictions.eq("beamLineName", beamLineName))
				.addOrder(Order.desc("proposalId")).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DataMatrixInSampleChanger3VO> findByProposalId(Integer proposalId) {
		Session session = (Session) this.entityManager.getDelegate();
		return session.createCriteria(DataMatrixInSampleChanger3VO.class)
				.add(Restrictions.eq("proposalId", proposalId)).addOrder(Order.desc("proposalId")).list();
	}

	@Override
	public List<String> findBLNamesByProposalId(Integer proposalId) {
		List<DataMatrixInSampleChanger3VO> list = this.findByProposalId(proposalId);
		List<String> blNames = new ArrayList<String>();
		for (DataMatrixInSampleChanger3VO dataMatrixInSampleChanger3VO : list) {
			if (!blNames.contains(dataMatrixInSampleChanger3VO.getBeamLineName()))
				blNames.add(dataMatrixInSampleChanger3VO.getBeamLineName());
		}
		return blNames;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DataMatrixInSampleChanger3VO> findByBeamLineName(String beamLineName) {
		Session session = (Session) this.entityManager.getDelegate();
		return session.createCriteria(DataMatrixInSampleChanger3VO.class)
				.add(Restrictions.eq("beamLineName", beamLineName)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.list();
	}

	public DataMatrixInSampleChanger3VO create(DataMatrixInSampleChanger3VO vo) {
		try {
			entityManager.persist(vo);
			return vo;
		} catch (RuntimeException re) {
			LOG.error("create failed", re);
			throw re;
		}
	}

	// Error while updating this object
	// Row was updated or deleted by another transaction (or unsaved-value mapping was incorrect)
	public DataMatrixInSampleChanger3VO update(DataMatrixInSampleChanger3VO vo) {
		return merge(vo);
	}

	@SuppressWarnings("unchecked")
	public List<DataMatrixInSampleChanger3VO> findBySampleProposalIdAndBeamlineName(Integer proposalId,
			String beamLineName, Integer locationInContainer, Integer containerLocationInSC) {

		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(DataMatrixInSampleChanger3VO.class);

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (proposalId != null) {
			crit.add(Restrictions.eq("proposalId", proposalId));
		}

		if (beamLineName != null)
			crit.add(Restrictions.like("beamLineName", beamLineName));

		if (locationInContainer != null)
			crit.add(Restrictions.eq("locationInContainer", locationInContainer));

		if (containerLocationInSC != null)
			crit.add(Restrictions.eq("containerLocationInSC", containerLocationInSC));

		crit.addOrder(Order.desc("proposalId"));

		return crit.list();
	}
}
