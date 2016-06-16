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

import ispyb.common.util.Constants;
import ispyb.common.util.StringUtils;
import ispyb.server.mx.vos.collections.DataCollection3VO;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * <p>
 * The data access object for DataCollection3 objects (rows of table DataCollection).
 * </p>
 * 
 * @see {@link DataCollection3DAO}
 */
@Stateless
public class DataCollection3DAOBean implements DataCollection3DAO {

	private final static Logger LOG = Logger.getLogger(DataCollection3DAOBean.class);

	// Generic HQL request to find instances of DataCollection3 by pk
	private static final String FIND_BY_PK(boolean fetchImage, boolean fetchScreening, boolean fetchAutoProcIntegration) {
		return "from DataCollection3VO vo " + (fetchImage ? "left join fetch vo.imageVOs " : "")
				+ (fetchScreening ? "left join fetch vo.screeningVOs " : "")
				+ (fetchAutoProcIntegration ? "left join fetch vo.autoProcIntegrationVOs " : "") + "where vo.dataCollectionId = :pk";
	}

	// Generic HQL request to find all instances of DataCollection3
	private static final String FIND_ALL() {
		return "from DataCollection3VO vo ";
	}

	private static final String FIND_BY_SHIPPING_ID = "select * from DataCollection, DataCollectionGroup, BLSample, Container, Dewar"
			+ " where DataCollection.dataCollectionGroupId = DataCollectionGroup.dataCollectionGroupId AND "
			+ "DataCollectionGroup.blSampleId = BLSample.blSampleId  " + " and BLSample.containerId = Container.containerId "
			+ " and Container.dewarId = Dewar.dewarId " + " and Dewar.shippingId = :shippingId ";

	private static final String FIND_BY_SAMPLE = "select * from DataCollection, DataCollectionGroup, BLSample "
			+ " where DataCollection.dataCollectionGroupId = DataCollectionGroup.dataCollectionGroupId AND "
			+ "DataCollectionGroup.blSampleId = BLSample.blSampleId  "
			+ " and BLSample.blSampleId = :blSampleId ORDER BY DataCollection.endTime ASC ";

	private static final String FIND_BY_IMAGE_FILE = "select * from DataCollection, Image "
			+ " where DataCollection.dataCollectionId = Image.dataCollectionId  "
			+ " and Image.fileLocation like :fileLocation AND Image.fileName like :fileName ORDER BY DataCollection.endTime ASC ";

	private static final String FIND_PDB_PATH = "SELECT c.pdbFileName, c.pdbFilePath "
			+ "FROM DataCollection d, DataCollectionGroup g, BLSample s, Crystal c "
			+ "WHERE d.dataCollectionId = :dataCollectionId AND " + "d.dataCollectionGroupId = g.dataCollectionGroupId AND "
			+ "g.blSampleId = s.blSampleId AND " + "s.crystalId = c.crystalId ";

	private final static String NB_OF_COLLECTS_FOR_GROUP = "SELECT count(*) FROM DataCollection "
			+ " WHERE  DataCollection.numberOfImages >4 and DataCollection.dataCollectionGroupId = :dcGroupId ";

	private final static String NB_OF_TESTS_FOR_GROUP = "SELECT count(*) FROM DataCollection "
			+ " WHERE  DataCollection.numberOfImages <=4 and DataCollection.dataCollectionGroupId = :dcGroupId ";

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	/* Creation/Update methods ---------------------------------------------- */

	/**
	 * <p>
	 * Insert the given value object. TODO update this comment for insertion details.
	 * </p>
	 */
	public void create(DataCollection3VO vo) throws Exception {
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
	}

	/**
	 * <p>
	 * Update the given value object. TODO update this comment for update details.
	 * </p>
	 */
	public DataCollection3VO update(DataCollection3VO vo) throws Exception {
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
	public void delete(DataCollection3VO vo) {
		entityManager.remove(entityManager.merge(vo));
	}

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * Returns the DataCollection3VO instance matching the given primary key.
	 * </p>
	 * <p>
	 * <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u> if the value object has to be used out the
	 * EJB container.
	 * </p>
	 * 
	 * @param pk
	 *            the primary key of the object to load.
	 * @param fetchRelation1
	 *            if true, the linked instances by the relation "relation1" will be set.
	 * @param fetchRelation2
	 *            if true, the linked instances by the relation "relation2" will be set.
	 */
	public DataCollection3VO findByPk(Integer pk, boolean fetchImage, boolean fetchScreening, boolean fetchAutoProcIntegration) {
		try {
			return (DataCollection3VO) entityManager.createQuery(FIND_BY_PK(fetchImage, fetchScreening, fetchAutoProcIntegration))
					.setParameter("pk", pk).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * <p>
	 * Returns the DataCollection3VO instances.
	 * </p>
	 * <p>
	 * <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u> if the value object has to be used out the
	 * EJB container.
	 * </p>
	 * 
	 * @param fetchRelation1
	 *            if true, the linked instances by the relation "relation1" will be set.
	 * @param fetchRelation2
	 *            if true, the linked instances by the relation "relation2" will be set.
	 */
	@SuppressWarnings("unchecked")
	public List<DataCollection3VO> findAll() {
		return entityManager.createQuery(FIND_ALL()).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<DataCollection3VO> findByShippingId(Integer shippingId) {
		String query = FIND_BY_SHIPPING_ID;
		List<DataCollection3VO> col = this.entityManager.createNativeQuery(query, "dataCollectionNativeQuery")
				.setParameter("shippingId", shippingId).getResultList();
		return col;
	}

	/* Private methods ------------------------------------------------------ */

	/**
	 * Checks the data for integrity. E.g. if references and categories exist.
	 * 
	 * @param vo
	 *            the data to check
	 * @param create
	 *            should be true if the value object is just being created in the DB, this avoids some checks like testing the primary
	 *            key
	 * @exception VOValidateException
	 *                if data is not correct
	 */
	private void checkAndCompleteData(DataCollection3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getDataCollectionId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getDataCollectionId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}

	/**
	 * Find the dataCollections for a given sample
	 * 
	 * @param blSampleId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DataCollection3VO> findLastDataCollectionBySample(Integer blSampleId) {
		String query = FIND_BY_SAMPLE;
		List<DataCollection3VO> listVOs = this.entityManager.createNativeQuery(query, "dataCollectionNativeQuery")
				.setParameter("blSampleId", blSampleId).getResultList();
		if (listVOs == null || listVOs.isEmpty())
			return null;
		return listVOs;
	}

	@SuppressWarnings("unchecked")
	public List<DataCollection3VO> findFiltered(String imageDirectory, String imagePrefix, Integer dataCollectionNumber,
			Integer sessionId, Byte printableForReport, boolean withScreenings, Integer dataCollectionGroupId) {

		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(DataCollection3VO.class);
		Criteria subCritgroup = crit.createCriteria("dataCollectionGroupVO");

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (sessionId != null) {
			Criteria subCritSession = subCritgroup.createCriteria("sessionVO");
			subCritSession.add(Restrictions.eq("sessionId", sessionId));
		}

		if (dataCollectionGroupId != null) {
			subCritgroup.add(Restrictions.eq("dataCollectionGroupId", dataCollectionGroupId));
		}

		if (imageDirectory != null)
			crit.add(Restrictions.like("imageDirectory", imageDirectory));

		if (imagePrefix != null)
			crit.add(Restrictions.like("imagePrefix", imagePrefix));

		if (dataCollectionNumber != null)
			crit.add(Restrictions.eq("dataCollectionNumber", dataCollectionNumber));

		if (printableForReport != null) {
			crit.add(Restrictions.eq("printableForReport", printableForReport));
		}

		if (withScreenings) {
			crit.setFetchMode("screeningVOs", FetchMode.JOIN);
		}

		crit.addOrder(Order.desc("startTime"));

		return crit.list();
	}

	/**
	 * find dataCollection with the given image fileLocation and the given image fileName
	 * 
	 * @param fileLocation
	 * @param fileName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DataCollection3VO> findByImageFile(String fileLocation, String fileName) {
		String query = FIND_BY_IMAGE_FILE;
		List<DataCollection3VO> listVOs = this.entityManager.createNativeQuery(query, "dataCollectionNativeQuery")
				.setParameter("fileLocation", fileLocation).setParameter("fileName", fileName).getResultList();
		if (listVOs == null || listVOs.isEmpty())
			return null;
		return listVOs;
	}

	@SuppressWarnings("unchecked")
	public List<DataCollection3VO> findByCustomQuery(Integer proposalId, String sampleName, String proteinAcronym,
			String beamlineName, Date experimentDateStart, Date experimentDateEnd, Integer minNumberOfImages,
			Integer maxNumberOfImages, String imagePrefix, Byte onlyPrintableForReport, Integer maxRecords, boolean withScreenings) {

		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(DataCollection3VO.class);

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !
		Criteria subCritgroup = crit.createCriteria("dataCollectionGroupVO");
		Criteria subCritSample = null;

		Criteria subCritSession = subCritgroup.createCriteria("sessionVO");

		if (proposalId != null) {
			Criteria subsubCritProposal = subCritSession.createCriteria("proposalVO");
			subsubCritProposal.add(Restrictions.eq("proposalId", proposalId));
		}

		if (sampleName != null) {
			if (!StringUtils.isEmpty(sampleName)) {
				subCritSample = subCritgroup.createCriteria("blSampleVO");
				sampleName = sampleName.replace('*', '%');
				subCritSample.add(Restrictions.like("name", sampleName));
			}
		}

		if (proteinAcronym != null) {
			if (!StringUtils.isEmpty(proteinAcronym)) {
				if (subCritSample == null)
					subCritSample = subCritgroup.createCriteria("blSampleVO");
				Criteria subsubCritCrystal = subCritSample.createCriteria("crystalVO");
				Criteria subsubsubCritProtein = subsubCritCrystal.createCriteria("proteinVO");
				proteinAcronym = proteinAcronym.replace('*', '%');
				subsubsubCritProtein.add(Restrictions.like("acronym", proteinAcronym));
			}
		}

		if (beamlineName != null) {
			if (!StringUtils.isEmpty(beamlineName)) {
				beamlineName = beamlineName.replace('*', '%');
				subCritSession.add(Restrictions.like("beamlineName", beamlineName));
			}
		}

		if (experimentDateStart != null) {
			if (Constants.DATABASE_IS_ORACLE()) {
				// Number of days between 01.01.1970 and creationDateStart = msecs divided by the number of msecs per
				// day
				String days = String.valueOf(experimentDateStart.getTime() / (24 * 60 * 60 * 1000));
				crit.add(Restrictions.sqlRestriction("startTime >= to_date('19700101', 'yyyymmdd') + " + days));
				// query += " AND o.startTime >= to_date('19700101', 'yyyymmdd') + " + days;
			} else if (Constants.DATABASE_IS_MYSQL())
				// query += " AND o.startTime >= '" + experimentDateStart + "'";
				crit.add(Restrictions.ge("startTime", experimentDateStart));
			else
				LOG.error("Database type not set.");
		}

		if (experimentDateEnd != null) {
			if (Constants.DATABASE_IS_ORACLE()) {
				// Number of days between 01.01.1970 and creationDateEnd = msecs divided by the number of msecs per day
				String days = String.valueOf(experimentDateEnd.getTime() / (24 * 60 * 60 * 1000));
				// query += " AND o.startTime <= to_date('19700101', 'yyyymmdd') + " + days;
				crit.add(Restrictions.sqlRestriction("startTime <= to_date('19700101', 'yyyymmdd') + " + days));
			} else if (Constants.DATABASE_IS_MYSQL())
				// query += " AND o.startTime <= '" + experimentDateEnd + "'";
				crit.add(Restrictions.le("startTime", experimentDateEnd));
			else
				LOG.error("Database type not set.");
		}

		if (minNumberOfImages != null) {
			crit.add(Restrictions.ge("numberOfImages", minNumberOfImages));
		}

		if (maxNumberOfImages != null) {
			crit.add(Restrictions.le("numberOfImages", maxNumberOfImages));
		}

		if (imagePrefix != null) {
			if (!StringUtils.isEmpty(imagePrefix)) {
				imagePrefix = imagePrefix.replace('*', '%');
				crit.add(Restrictions.ilike("imagePrefix", imagePrefix));
			}
		}

		if (onlyPrintableForReport != null) {
			crit.add(Restrictions.eq("printableForReport", onlyPrintableForReport));
		}

		if (maxRecords != null) {
			crit.setMaxResults(maxRecords);
		}

		if (withScreenings) {
			crit.setFetchMode("screeningVOs", FetchMode.JOIN);
		}

		crit.addOrder(Order.desc("startTime"));

		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public List<DataCollection3VO> findByProtein(String proteinAcronym, Byte printableForReport, Integer proposalId,
			boolean withScreenings) {
		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(DataCollection3VO.class);
		Criteria subCritgroup = crit.createCriteria("dataCollectionGroupVO");

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (proposalId != null) {
			Criteria subCritSession = subCritgroup.createCriteria("sessionVO");
			Criteria subsubCritProposal = subCritSession.createCriteria("proposalVO");
			subsubCritProposal.add(Restrictions.eq("proposalId", proposalId));
		}

		if (proteinAcronym != null) {
			Criteria subCritSample = subCritgroup.createCriteria("blSampleVO");
			Criteria subsubCritCrystal = subCritSample.createCriteria("crystalVO");
			Criteria subsubsubCritProtein = subsubCritCrystal.createCriteria("proteinVO");

			if (!StringUtils.isEmpty(proteinAcronym))
				proteinAcronym = proteinAcronym.replace('*', '%');
			subsubsubCritProtein.add(Restrictions.like("acronym", proteinAcronym));
		}

		if (printableForReport != null) {
			crit.add(Restrictions.eq("printableForReport", printableForReport));
		}

		if (withScreenings) {
			crit.setFetchMode("screeningVOs", FetchMode.JOIN);
		}

		crit.addOrder(Order.desc("startTime"));

		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public List<DataCollection3VO> findBySample(Integer blSampleId, String sampleName, Byte printableForReport, Integer proposalId,
			boolean withScreenings) {
		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(DataCollection3VO.class);
		Criteria subCritgroup = crit.createCriteria("dataCollectionGroupVO");

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !
		Criteria subCritSample = subCritgroup.createCriteria("blSampleVO");

		if (proposalId != null) {
			Criteria subCritSession = subCritgroup.createCriteria("sessionVO");
			Criteria subsubCritProposal = subCritSession.createCriteria("proposalVO");
			subsubCritProposal.add(Restrictions.eq("proposalId", proposalId));
		}

		if (blSampleId != null) {
			subCritSample.add(Restrictions.eq("blSampleId", blSampleId));
		}

		if (sampleName != null) {
			if (!StringUtils.isEmpty(sampleName))
				sampleName = sampleName.replace('*', '%');
			subCritSample.add(Restrictions.like("name", sampleName));
		}

		if (printableForReport != null) {
			crit.add(Restrictions.eq("printableForReport", printableForReport));
		}

		if (withScreenings) {
			crit.setFetchMode("screeningVOs", FetchMode.JOIN);
		}

		crit.addOrder(Order.desc("startTime"));

		return crit.list();
	}

	/**
	 * returns a XDSInfo for a given dataCollectionId
	 * 
	 * @param dataCollectionId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List findXDSInfo(Integer dataCollectionId) {
		Query q = this.entityManager
				.createNativeQuery("SELECT BLSession.beamLineSetupId, DataCollection.detectorId,  "
						+ "DataCollection.axisRange, DataCollection.axisStart, DataCollection.axisEnd, DataCollection.detectorDistance, "
						+ "DataCollection.fileTemplate, DataCollection.imageDirectory, DataCollection.imageSuffix, "
						+ "DataCollection.numberOfImages, DataCollection.startImageNumber, "
						+ "DataCollection.phiStart, DataCollection.kappaStart, "
						+ "DataCollection.wavelength, DataCollection.xbeam, DataCollection.ybeam "
						+ "FROM DataCollection, DataCollectionGroup, BLSession " + "WHERE DataCollection.dataCollectionId = "
						+ dataCollectionId + " AND "
						+ "DataCollection.dataCollectionGroupId = DataCollectionGroup.dataCollectionGroupId AND "
						+ "DataCollectionGroup.sessionId = BLSession.sessionId ");
		List orders = q.getResultList();

		return orders;
	}

	/**
	 * returns the pdb full path for a given dataCollectionId
	 * 
	 * @param dataCollectionId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String findPdbFullPath(Integer dataCollectionId) {
		Query query = entityManager.createNativeQuery(FIND_PDB_PATH).setParameter("dataCollectionId", dataCollectionId);
		List orders = query.getResultList();
		if (orders == null || orders.size() < 1)
			return null;
		int nb = orders.size();
		String res = null;
		for (int i = 0; i < nb; i++) {
			Object[] o = (Object[]) orders.get(i);
			int j = 0;
			String fileName = (String) o[j++];
			String filePath = (String) o[j++];
			if (fileName != null && filePath != null)
				res = filePath + fileName;
		}
		return res;
	}

	/**
	 * returns the list of dataCollections with startTime > startDate
	 * 
	 * @param startDate
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<DataCollection3VO> findLastCollect(Date startDate, Date endDate, String[] beamlineName, boolean withScreenings) {
		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(DataCollection3VO.class);

		if (startDate != null) {
			crit.add(Restrictions.ge("startTime", startDate));
		}

		if (endDate != null) {
			crit.add(Restrictions.le("endTime", endDate));
		}
		if (beamlineName != null && beamlineName.length > 0) {
			Criteria subCritgroup = crit.createCriteria("dataCollectionGroupVO");
			Criteria subCritSession = subCritgroup.createCriteria("sessionVO");
			subCritSession.add(Restrictions.in("beamlineName", beamlineName));
		}

		if (withScreenings) {
			crit.setFetchMode("screeningVOs", FetchMode.JOIN);
		}
		crit.addOrder(Order.desc("startTime"));

		return crit.list();
	}

	public Integer getNbOfCollects(Integer dcgId) throws Exception {

		Query query = entityManager.createNativeQuery(NB_OF_COLLECTS_FOR_GROUP).setParameter("dcGroupId", dcgId);
		try {
			BigInteger res = (BigInteger) query.getSingleResult();

			return new Integer(res.intValue());
		} catch (NoResultException e) {
			System.out.println("ERROR in getNbOfCollects - NoResultException: " + dcgId);
			e.printStackTrace();
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public Integer getNbOfTests(Integer dcgId) throws Exception {

		Query query = entityManager.createNativeQuery(NB_OF_TESTS_FOR_GROUP).setParameter("dcGroupId", dcgId);
		try {
			BigInteger res = (BigInteger) query.getSingleResult();

			return new Integer(res.intValue());
		} catch (NoResultException e) {
			System.out.println("ERROR in getNbOfTests - NoResultException: " + dcgId);
			e.printStackTrace();
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
