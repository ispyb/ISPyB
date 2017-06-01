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
package ispyb.server.mx.daos.collections;

import ispyb.server.mx.vos.collections.BeamLineSetup3VO;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

/**
 * <p>
 * The data access object for BeamLineSetup objects (rows of table BeamLineSetup).
 * </p>
 * 
 * @see {@link BeamLineSetup3DAO}
 */
@Stateless
public class BeamLineSetup3DAOBean implements BeamLineSetup3DAO {

	private final static Logger LOG = Logger.getLogger(BeamLineSetup3DAOBean.class);

	// Generic HQL request to find instances of BeamLineSetup by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK(Integer pk) {
		return "from BeamLineSetup3VO vo  where vo.beamLineSetupId = :pk";
	}

	// Generic HQL request to find all instances of BeamLineSetup
	// TODO choose between left/inner join
	private static final String FIND_ALL() {
		return "from BeamLineSetup3VO vo ";
	}

	private static final String FIND_BY_SCREENING_INPUT = "SELECT * "
			+ "FROM BeamLineSetup bls, ScreeningInput si, Screening s, DataCollection dc, DataCollectionGroup g, BLSession ses  "
			+ " WHERE si.screeningInputId = :screeningInputId AND si.screeningId = s.screeningId "
			+ "AND s.dataCollectionId = dc.dataCollectionId AND "
			+ "dc.dataCollectionGroupId = g.dataCollectionGroupId AND "
			+ "g.sessionId = ses.sessionId AND ses.beamLineSetupId = bls.beamLineSetupId";

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public BeamLineSetup3DAOBean() {
	};

	/* Creation/Update methods ---------------------------------------------- */

	/**
	 * <p>
	 * Insert the given value object. TODO update this comment for insertion details.
	 * </p>
	 * 
	 * @throws VOValidateException
	 *             if the value object data integrity is not ok.
	 */
	public void create(BeamLineSetup3VO vo) throws Exception {
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
	}

	/**
	 * <p>
	 * Update the given value object. TODO update this comment for update details.
	 * </p>
	 * 
	 * @throws VOValidateException
	 *             if the value object data integrity is not ok.
	 */
	public BeamLineSetup3VO update(BeamLineSetup3VO vo) throws Exception {
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/* Deletion methods ----------------------------------------------------- */

	/**
	 * <p>
	 * Deletes the given value object.
	 * </p>
	 * 
	 * @param vo
	 *            the value object to delete.
	 */
	public void delete(BeamLineSetup3VO vo) {
		entityManager.remove(vo);
	}

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * Returns the BeamLineSetupVO instance matching the given primary key.
	 * </p>
	 * <p>
	 * <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u> if the value object has to be
	 * used out the EJB container.
	 * </p>
	 * 
	 * @param pk
	 *            the primary key of the object to load.
	 * @param fetchRelation1
	 *            if true, the linked instances by the relation "relation1" will be set.
	 * @param fetchRelation2
	 *            if true, the linked instances by the relation "relation2" will be set.
	 */
	public BeamLineSetup3VO findByPk(Integer pk) {
		Query query = entityManager.createQuery(FIND_BY_PK(pk)).setParameter("pk", pk);
		List listVOs = query.getResultList();
		if (listVOs == null || listVOs.isEmpty())
			return null;
		return (BeamLineSetup3VO) listVOs.toArray()[0];
	}

	/**
	 * <p>
	 * Returns the BeamLineSetupVO instances.
	 * </p>
	 * <p>
	 * <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u> if the value object has to be
	 * used out the EJB container.
	 * </p>
	 * 
	 * @param fetchRelation1
	 *            if true, the linked instances by the relation "relation1" will be set.
	 * @param fetchRelation2
	 *            if true, the linked instances by the relation "relation2" will be set.
	 */
	@SuppressWarnings("unchecked")
	public List<BeamLineSetup3VO> findAll() {
		Query query = entityManager.createQuery(FIND_ALL());
		List<BeamLineSetup3VO> vos = query.getResultList();
		return vos;
	}

	/* Private methods ------------------------------------------------------ */

	/**
	 * Checks the data for integrity. E.g. if references and categories exist.
	 * 
	 * @param vo
	 *            the data to check
	 * @param create
	 *            should be true if the value object is just being created in the DB, this avoids some checks like
	 *            testing the primary key
	 * @exception VOValidateException
	 *                if data is not correct
	 */
	private void checkAndCompleteData(BeamLineSetup3VO vo, boolean create) throws Exception {
		if (create) {
			if (vo.getBeamLineSetupId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getBeamLineSetupId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}

	/**
	 * Find a beamLineSetup for a given screeningInput
	 * 
	 * @param screeningInputId
	 * @return
	 */
	public BeamLineSetup3VO findByScreeningInputId(Integer screeningInputId) {
		String query = FIND_BY_SCREENING_INPUT;
		List<BeamLineSetup3VO> listVOs = this.entityManager.createNativeQuery(query, "beamLineSetupNativeQuery")
				.setParameter("screeningInputId", screeningInputId).getResultList();
		if (listVOs == null || listVOs.isEmpty())
			return null;
		return (BeamLineSetup3VO) listVOs.toArray()[0];
	}
}
