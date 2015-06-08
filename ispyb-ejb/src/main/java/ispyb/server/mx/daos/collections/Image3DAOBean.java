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

package ispyb.server.mx.daos.collections;

import ispyb.server.mx.vos.collections.Image3VO;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * <p>
 * The data access object for Image3 objects (rows of table Image).
 * </p>
 * 
 * @see {@link Image3DAO}
 */
@Stateless
public class Image3DAOBean implements Image3DAO {

	private final static Logger LOG = Logger.getLogger(Image3DAOBean.class);

	// Generic HQL request to find instances of Image3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK() {
		return "from Image3VO vo " + "where vo.imageId = :pk";
	}

	// Generic HQL request to find all instances of Image3
	// TODO choose between left/inner join
	private static final String FIND_ALL() {
		return "from Image3VO vo ";
	}

	private static String FIND_BY_IMAGE_PROPOSAL = "SELECT DISTINCT i.imageId, i.dataCollectionId, i.motorPositionId, i.imageNumber, " +
			"i.fileName, i.fileLocation, i.measuredIntensity, i.jpegFileFullPath, i.jpegThumbnailFileFullPath, " +
			"i.temperature, i.cumulativeIntensity, i.synchrotronCurrent, i.comments, i.machineMessage " +
			" FROM Image i, DataCollection dc, DataCollectionGroup dcg, BLSession ses, Proposal pro "
			+ "WHERE  i.imageId  = :imageId AND i.dataCollectionId = dc.dataCollectionId AND " +
			"dc.dataCollectionGroupId = dcg.dataCollectionGroupId AND " + 
			"dcg.sessionId = ses.sessionId AND ses.proposalId = pro.proposalId AND pro.proposalId = :proposalId";
	
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	/* Creation/Update methods ---------------------------------------------- */

	/**
	 * <p>
	 * Insert the given value object. TODO update this comment for insertion details.
	 * </p>
	 */
	public void create(Image3VO vo) throws Exception {
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
	}

	/**
	 * <p>
	 * Update the given value object. TODO update this comment for update details.
	 * </p>
	 */
	public Image3VO update(Image3VO vo) throws Exception {
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
	public void delete(Image3VO vo) {
		entityManager.remove(vo);
	}

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * Returns the Image3VO instance matching the given primary key.
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
	public Image3VO findByPk(Integer pk) {
		try{
			return (Image3VO) entityManager.createQuery(FIND_BY_PK()).setParameter("pk", pk).getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}

	/**
	 * <p>
	 * Returns the Image3VO instances.
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
	public List<Image3VO> findAll() {
		return entityManager.createQuery(FIND_ALL()).getResultList();
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
	private void checkAndCompleteData(Image3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getImageId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getImageId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}

	/**
	 * 
	 * @param fileLocation
	 * @param fileName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Image3VO> findFiltered(String fileLocation, String fileName, Integer dataCollectionId, Integer dataCollectionGroupId) {

		Session session = (Session) this.entityManager.getDelegate();
		Criteria crit = session.createCriteria(Image3VO.class);

		Criteria subCrit = crit.createCriteria("dataCollectionVO");

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (fileLocation != null && !fileLocation.isEmpty()) {
			crit.add(Restrictions.like("fileLocation", fileLocation));
		}

		if (fileName != null && !fileName.isEmpty()) {
			crit.add(Restrictions.like("fileName", fileName));
		}

		if (dataCollectionId != null) {
			subCrit.add(Restrictions.eq("dataCollectionId", dataCollectionId));
		}
		
		if (dataCollectionGroupId != null){
			Criteria dataCollectionGroupCrit = subCrit.createCriteria("dataCollectionGroupVO");
			dataCollectionGroupCrit.add(Restrictions.eq("dataCollectionGroupId", dataCollectionGroupId));
		}

		crit.addOrder(Order.desc("imageId"));

		return crit.list();
	}

	// two variables to guarantee the user fecths only its own images
	@SuppressWarnings("unchecked")
	public List<Image3VO> findByImageIdAndProposalId(Integer imageId, Integer proposalId) {
		String query = FIND_BY_IMAGE_PROPOSAL;
		List<Image3VO> listVOs = this.entityManager.createNativeQuery(query, "imageNativeQuery")
				.setParameter("imageId", imageId).setParameter("proposalId", proposalId).getResultList();
		if (listVOs == null || listVOs.isEmpty())
			return null;
		return listVOs;
	}
}
