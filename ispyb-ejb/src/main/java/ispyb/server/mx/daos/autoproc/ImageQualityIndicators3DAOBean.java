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

package ispyb.server.mx.daos.autoproc;

import ispyb.server.mx.vos.autoproc.ImageQualityIndicators3VO;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * <p>
 * The data access object for ImageQualityIndicators3 objects (rows of table TableName).
 * </p>
 * 
 * @see {@link ImageQualityIndicators3DAO}
 */
@Stateless
public class ImageQualityIndicators3DAOBean implements ImageQualityIndicators3DAO {

	private final static Logger LOG = Logger.getLogger(ImageQualityIndicators3DAOBean.class);

	// Generic HQL request to find instances of ImageQualityIndicators3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK() {
		return "from ImageQualityIndicators3VO vo " + "where vo.imageQualityIndicatorsId = :pk";
	}

	// Generic HQL request to find all instances of ImageQualityIndicators3
	// TODO choose between left/inner join
	private static final String FIND_ALL() {
		return "from ImageQualityIndicators3VO vo " ;
	}

	private static String FIND_BY_DATACOLLECTION_ID = "select * " + " FROM ImageQualityIndicators q, Image i "
			+ "WHERE q.imageId = i.imageId  AND  i.dataCollectionId = :dataCollectionId ";

	private static String FIND_BY_IMAGE_ID = "select * FROM ImageQualityIndicators q " + "WHERE q.imageId = :imageId ";

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	/* Creation/Update methods ---------------------------------------------- */

	/**
	 * <p>
	 * Insert the given value object. TODO update this comment for insertion details.
	 * </p>
	 */
	public void create(ImageQualityIndicators3VO vo) throws Exception {
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
	}

	/**
	 * <p>
	 * Update the given value object. TODO update this comment for update details.
	 * </p>
	 */
	public ImageQualityIndicators3VO update(ImageQualityIndicators3VO vo) throws Exception {
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
	public void delete(ImageQualityIndicators3VO vo) {
		entityManager.remove(vo);
	}

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * Returns the ImageQualityIndicators3VO instance matching the given primary key.
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
	public ImageQualityIndicators3VO findByPk(Integer pk) {
		try{
		return (ImageQualityIndicators3VO) entityManager.createQuery(FIND_BY_PK())
				.setParameter("pk", pk).getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}

	/**
	 * <p>
	 * Returns the ImageQualityIndicators3VO instances.
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
	public List<ImageQualityIndicators3VO> findAll() {
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
	private void checkAndCompleteData(ImageQualityIndicators3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getImageQualityIndicatorsId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getImageQualityIndicatorsId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}

	/**
	 * 
	 * @param dataCollectionId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ImageQualityIndicators3VO> findByDataCollectionId(Integer dataCollectionId) {
		String query = FIND_BY_DATACOLLECTION_ID;
		List<ImageQualityIndicators3VO> listVOs = this.entityManager
				.createNativeQuery(query, "imageQualityIndicatorsNativeQuery")
				.setParameter("dataCollectionId", dataCollectionId).getResultList();
		if (listVOs == null || listVOs.isEmpty())
			return null;
		return listVOs;
	}

	/**
	 * 
	 * @param dataCollectionId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ImageQualityIndicators3VO> findByImageId(Integer imageId) {
		String query = FIND_BY_IMAGE_ID;
		List<ImageQualityIndicators3VO> listVOs = this.entityManager
				.createNativeQuery(query, "imageQualityIndicatorsNativeQuery").setParameter("imageId", imageId)
				.getResultList();
		if (listVOs == null || listVOs.isEmpty())
			return null;
		return listVOs;
	}

}
