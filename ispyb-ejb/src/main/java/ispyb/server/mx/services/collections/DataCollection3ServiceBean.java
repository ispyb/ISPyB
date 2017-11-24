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
package ispyb.server.mx.services.collections;

import ispyb.common.util.Constants;
import ispyb.common.util.StringUtils;
import ispyb.server.common.util.ejb.EJBAccessCallback;
import ispyb.server.common.util.ejb.EJBAccessTemplate;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.vos.collections.BeamLineSetup3VO;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.DataCollectionWS3VO;
import ispyb.server.mx.vos.collections.Detector3VO;
import ispyb.server.mx.vos.collections.XDSInfo;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * <p>
 * This session bean handles ISPyB DataCollection3.
 * </p>
 */
@Stateless
public class DataCollection3ServiceBean implements DataCollection3Service, DataCollection3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(DataCollection3ServiceBean.class);

	// Generic HQL request to find instances of DataCollection3 by pk
	private static final String FIND_BY_PK(boolean fetchImage, boolean fetchAutoProcIntegration) {
		return "from DataCollection3VO vo " + (fetchImage ? "left join fetch vo.imageVOs " : "")

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
			+ "DataCollectionGroup.blSampleId = BLSample.blSampleId  " + " and BLSample.blSampleId = :blSampleId ORDER BY DataCollection.endTime ASC ";

	private static final String FIND_BY_IMAGE_FILE = "select * from DataCollection, Image "
			+ " where DataCollection.dataCollectionId = Image.dataCollectionId  "
			+ " and Image.fileLocation like :fileLocation AND Image.fileName like :fileName ORDER BY DataCollection.endTime ASC ";

	private static final String FIND_PDB_PATH = "SELECT c.pdbFileName, c.pdbFilePath " + "FROM DataCollection d, DataCollectionGroup g, BLSample s, Crystal c "
			+ "WHERE d.dataCollectionId = :dataCollectionId AND " + "d.dataCollectionGroupId = g.dataCollectionGroupId AND "
			+ "g.blSampleId = s.blSampleId AND " + "s.crystalId = c.crystalId ";

	private final static String NB_OF_COLLECTS_FOR_GROUP = "SELECT count(*) FROM DataCollection "
			+ " WHERE  DataCollection.numberOfImages >4 and DataCollection.dataCollectionGroupId = :dcGroupId ";

	private final static String NB_OF_TESTS_FOR_GROUP = "SELECT count(*) FROM DataCollection "
			+ " WHERE  DataCollection.numberOfImages <=4 and DataCollection.dataCollectionGroupId = :dcGroupId ";

	private static final String FIND_BY_PROPOSALID = "select * from DataCollection, DataCollectionGroup, BLSession, Proposal "
			+ " where DataCollection.dataCollectionGroupId = DataCollectionGroup.dataCollectionGroupId AND BLSession.sessionId = DataCollectionGroup.sessionId"
			+ " and BLSession.proposalId = Proposal.proposalId and Proposal.proposalId = :proposalId ";

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@Resource
	private SessionContext context;

	public DataCollection3ServiceBean() {
	};

	/**
	 * Create new DataCollection3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public DataCollection3VO create(final DataCollection3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the DataCollection3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public DataCollection3VO update(final DataCollection3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the DataCollection3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		checkCreateChangeRemoveAccess();
		DataCollection3VO vo = findByPk(pk, false, false);
		// TODO Edit this business code
		delete(vo);
	}

	/**
	 * Remove the DataCollection3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final DataCollection3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		entityManager.remove(entityManager.merge(vo));
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects
	 * if necessary
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the DataCollection3 value object
	 */
	public DataCollection3VO findByPk(final Integer pk, final boolean withImage, final boolean withAutoProcIntegration) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		try {
			return (DataCollection3VO) entityManager.createQuery(FIND_BY_PK(withImage, withAutoProcIntegration)).setParameter("pk", pk).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Find a dataCollection by its primary key -- webservices object
	 * 
	 * @param pk
	 * @param withLink1
	 * @param withLink2
	 * @return
	 * @throws Exception
	 */
	public DataCollectionWS3VO findForWSByPk(final Integer pk, final boolean withImage, final boolean withAutoProcIntegration) throws Exception {

		checkCreateChangeRemoveAccess();
		try {
			DataCollection3VO found = (DataCollection3VO) entityManager.createQuery(FIND_BY_PK(withImage, withAutoProcIntegration)).setParameter("pk", pk)
					.getSingleResult();
			DataCollectionWS3VO sesLight = getWSDataCollectionVO(found);
			return sesLight;
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private DataCollection3VO getLightDataCollection3VO(DataCollection3VO vo) throws CloneNotSupportedException {
		if (vo == null)
			return null;
		DataCollection3VO otherVO = (DataCollection3VO) vo.clone();
		// otherVO.set(null);
		return otherVO;
	}

	private DataCollectionWS3VO[] getWSDataCollectionVOs(List<DataCollection3VO> entities) throws CloneNotSupportedException {
		if (entities == null)
			return null;
		ArrayList<DataCollectionWS3VO> results = new ArrayList<DataCollectionWS3VO>(entities.size());
		for (DataCollection3VO vo : entities) {
			DataCollectionWS3VO otherVO = getWSDataCollectionVO(vo);
			if (otherVO != null)
				results.add(otherVO);
		}
		DataCollectionWS3VO[] tmpResults = new DataCollectionWS3VO[results.size()];
		return results.toArray(tmpResults);
	}

	private DataCollectionWS3VO getWSDataCollectionVO(DataCollection3VO vo) throws CloneNotSupportedException {
		if (vo != null) {
			DataCollection3VO otherVO = getLightDataCollection3VO(vo);
			Integer dataCollectionGroupId = null;
			Integer strategySubWedgeOrigId = null;
			Integer detectorId = null;
			Integer blSubSampleId = null;
			dataCollectionGroupId = otherVO.getDataCollectionGroupVOId();
			strategySubWedgeOrigId = otherVO.getStrategySubWedgeOrigVOId();
			detectorId = otherVO.getDetectorVOId();
			blSubSampleId = otherVO.getBlSubSampleVOId();
			otherVO.setDataCollectionGroupVO(null);
			otherVO.setStrategySubWedgeOrigVO(null);
			otherVO.setDetectorVO(null);
			otherVO.setBlSubSampleVO(null);
			DataCollectionWS3VO wsDataCollection = new DataCollectionWS3VO(otherVO);
			wsDataCollection.setDataCollectionGroupId(dataCollectionGroupId);
			wsDataCollection.setStrategySubWedgeOrigId(strategySubWedgeOrigId);
			wsDataCollection.setDetectorId(detectorId);
			wsDataCollection.setBlSubSampleId(blSubSampleId);
			return wsDataCollection;
		} else {
			return null;
		}
	}

	/**
	 * Find all DataCollection3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<DataCollection3VO> findAll() throws Exception {

		List<DataCollection3VO> foundEntities = entityManager.createQuery(FIND_ALL()).getResultList();
		return foundEntities;
	}

	@SuppressWarnings("unchecked")
	public List<DataCollection3VO> findByShippingId(final Integer shippingId) throws Exception {

		String query = FIND_BY_SHIPPING_ID;
		List<DataCollection3VO> col = this.entityManager.createNativeQuery(query, "dataCollectionNativeQuery").setParameter("shippingId", shippingId)
				.getResultList();
		return col;
	}

	/**
	 * Check if user has access rights to create, change and remove
	 * DataCollection3 entities. If not set rollback only and throw
	 * AccessDeniedException
	 * 
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);
		// // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
	}

	/**
	 * Find the dataCollections for a given sample
	 * 
	 * @param blSampleId
	 * @param detachLight
	 * @return
	 * @throws Exception
	 */
	public DataCollectionWS3VO[] findForWSLastDataCollectionBySample(final Integer blSampleId) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		EJBAccessCallback callBack = new EJBAccessCallback() {

			public DataCollectionWS3VO[] doInEJBAccess(Object parent) throws Exception {
				String query = FIND_BY_SAMPLE;
				List<DataCollection3VO> listVOs = entityManager.createNativeQuery(query, "dataCollectionNativeQuery").setParameter("blSampleId", blSampleId)
						.getResultList();
				if (listVOs == null || listVOs.isEmpty())
					listVOs = null;
				List<DataCollection3VO> foundEntities = listVOs;
				DataCollectionWS3VO[] vos = getWSDataCollectionVOs(foundEntities);
				return vos;
			};
		};
		DataCollectionWS3VO[] ret = (DataCollectionWS3VO[]) template.execute(callBack);

		return ret;
	}

	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	@SuppressWarnings("unused")
	private List<DataCollection3VO> getLightDataCollectionVOs(List<DataCollection3VO> entities) throws CloneNotSupportedException {
		List<DataCollection3VO> results = new ArrayList<DataCollection3VO>(entities.size());
		for (DataCollection3VO vo : entities) {
			DataCollection3VO otherVO = getLightDataCollectionVO(vo);
			results.add(otherVO);
		}
		return results;
	}

	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private DataCollection3VO getLightDataCollectionVO(DataCollection3VO vo) throws CloneNotSupportedException {
		DataCollection3VO otherVO = (DataCollection3VO) vo.clone();
		otherVO.setImageVOs(null);
		otherVO.setAutoProcIntegrationVOs(null);
		return otherVO;
	}

	/**
	 * Find a dataCollection with the image directory, the image prefix and the
	 * dataCollection Number
	 * 
	 * @param imageDirectory
	 * @param imagePrefix
	 * @param dataCollectionNumber
	 * @return
	 * @throws Exception
	 */
	public DataCollectionWS3VO findForWSDataCollectionFromImageDirectoryAndImagePrefixAndNumber(final String imageDirectory, final String imagePrefix,
			final Integer dataCollectionNumber) throws Exception {

		Session session = (Session) this.entityManager.getDelegate();
		Criteria crit = session.createCriteria(DataCollection3VO.class);
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT
		if (imageDirectory != null)
			crit.add(Restrictions.like("imageDirectory", imageDirectory));

		if (imagePrefix != null)
			crit.add(Restrictions.like("imagePrefix", imagePrefix));

		if (dataCollectionNumber != null)
			crit.add(Restrictions.eq("dataCollectionNumber", dataCollectionNumber));

		crit.addOrder(Order.desc("startTime"));

		List<DataCollection3VO> foundEntities = crit.list();
		DataCollectionWS3VO[] vos = getWSDataCollectionVOs(foundEntities);
		if (vos == null || vos.length == 0)
			return null;
		return vos[0];
	}

	/**
	 * Find a dataCollection with the image fileLocation and image fileName
	 * 
	 * @param fileLocation
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public DataCollectionWS3VO findForWSDataCollectionIdFromFileLocationAndFileName(final String fileLocation, final String fileName) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		EJBAccessCallback callBack = new EJBAccessCallback() {

			public DataCollectionWS3VO[] doInEJBAccess(Object parent) throws Exception {
				String query = FIND_BY_IMAGE_FILE;
				List<DataCollection3VO> listVOs = entityManager.createNativeQuery(query, "dataCollectionNativeQuery")
						.setParameter("fileLocation", fileLocation).setParameter("fileName", fileName).getResultList();
				if (listVOs == null || listVOs.isEmpty())
					listVOs = null;

				List<DataCollection3VO> foundEntities = listVOs;
				DataCollectionWS3VO[] vos = getWSDataCollectionVOs(foundEntities);
				return vos;
			};
		};
		DataCollectionWS3VO[] ret = (DataCollectionWS3VO[]) template.execute(callBack);
		if (ret == null || ret.length == 0)
			return null;
		return ret[0];
	}

	/**
	 * Find a dataCollection with the image fileLocation and image fileName
	 * 
	 * @param fileLocation
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public DataCollection3VO findForDataCollectionIdFromFileLocationAndFileName(final String fileLocation, final String fileName) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		EJBAccessCallback callBack = new EJBAccessCallback() {

			public List<DataCollection3VO> doInEJBAccess(Object parent) throws Exception {
				String query = FIND_BY_IMAGE_FILE;
				List<DataCollection3VO> listVOs = entityManager.createNativeQuery(query, "dataCollectionNativeQuery")
						.setParameter("fileLocation", fileLocation).setParameter("fileName", fileName).getResultList();
				if (listVOs == null || listVOs.isEmpty())
					listVOs = null;

				List<DataCollection3VO> foundEntities = listVOs;
				return foundEntities;
			};
		};
		List<DataCollection3VO> ret = (List<DataCollection3VO>) template.execute(callBack);
		if (ret == null || ret.size() == 0)
			return null;
		return ret.get(0);
	}

	/**
	 * 
	 * @param imageDirectory
	 * @param imagePrefix
	 * @param dataCollectionNumber
	 * @param sessionId
	 * @param printableForReport
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<DataCollection3VO> findFiltered(final String imageDirectory, final String imagePrefix, final Integer dataCollectionNumber,
			final Integer sessionId, final Byte printableForReport, final Integer dataCollectionGroupId) throws Exception {

		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(DataCollection3VO.class);
		Criteria subCritgroup = crit.createCriteria("dataCollectionGroupVO");

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT
																	// RESULTS !

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

		crit.addOrder(Order.desc("startTime"));
		List<DataCollection3VO> foundEntities = crit.list();
		return foundEntities;
	}

	/**
	 * 
	 * @param proposalId
	 * @param sampleName
	 * @param proteinAcronym
	 * @param beamlineName
	 * @param experimentDateStart
	 * @param experimentDateEnd
	 * @param minNumberOfImages
	 * @param maxNumberOfImages
	 * @param onlyPrintableForReport
	 * @param maxRecords
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<DataCollection3VO> findByCustomQuery(final Integer proposalId, final String sampleName, final String proteinAcronym, final String beamlineName,
			final Date experimentDateStart, final Date experimentDateEnd, final Integer minNumberOfImages, final Integer maxNumberOfImages,
			final String imagePrefix, final Byte onlyPrintableForReport, final Integer maxRecords) throws Exception {

		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(DataCollection3VO.class);

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT
																	// RESULTS !
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
				String Na;
				Na = sampleName.replace('*', '%');
				subCritSample.add(Restrictions.like("name", Na));
			}
		}

		if (proteinAcronym != null) {
			if (!StringUtils.isEmpty(proteinAcronym)) {
				if (subCritSample == null)
					subCritSample = subCritgroup.createCriteria("blSampleVO");
				Criteria subsubCritCrystal = subCritSample.createCriteria("crystalVO");
				Criteria subsubsubCritProtein = subsubCritCrystal.createCriteria("proteinVO");
				String a;
				a = proteinAcronym.replace('*', '%');
				subsubsubCritProtein.add(Restrictions.like("acronym", a));
			}
		}

		if (beamlineName != null) {
			if (!StringUtils.isEmpty(beamlineName)) {
				String n;
				n = beamlineName.replace('*', '%');
				subCritSession.add(Restrictions.like("beamlineName", n));
			}
		}

		if (experimentDateStart != null) {
			if (Constants.DATABASE_IS_ORACLE()) {
				// Number of days between 01.01.1970 and creationDateStart =
				// msecs divided by the number of msecs per
				// day
				String days = String.valueOf(experimentDateStart.getTime() / (24 * 60 * 60 * 1000));
				crit.add(Restrictions.sqlRestriction("startTime >= to_date('19700101', 'yyyymmdd') + " + days));
				// query +=
				// " AND o.startTime >= to_date('19700101', 'yyyymmdd') + " +
				// days;
			} else if (Constants.DATABASE_IS_MYSQL())
				// query += " AND o.startTime >= '" + experimentDateStart + "'";
				crit.add(Restrictions.ge("startTime", experimentDateStart));
			else
				LOG.error("Database type not set.");
		}

		if (experimentDateEnd != null) {
			if (Constants.DATABASE_IS_ORACLE()) {
				// Number of days between 01.01.1970 and creationDateEnd = msecs
				// divided by the number of msecs per day
				String days = String.valueOf(experimentDateEnd.getTime() / (24 * 60 * 60 * 1000));
				// query +=
				// " AND o.startTime <= to_date('19700101', 'yyyymmdd') + " +
				// days;
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
				String p;
				p = imagePrefix.replace('*', '%');
				crit.add(Restrictions.ilike("imagePrefix", p));
			}
		}

		if (onlyPrintableForReport != null) {
			crit.add(Restrictions.eq("printableForReport", onlyPrintableForReport));
		}

		if (maxRecords != null) {
			crit.setMaxResults(maxRecords);
		}

		crit.addOrder(Order.desc("startTime"));

		List<DataCollection3VO> foundEntities = crit.list();
		return foundEntities;
	}

	/**
	 * 
	 * @param proteinAcronym
	 * @param printableForReport
	 * @param proposalId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DataCollection3VO> findByProtein(String proteinAcronym, final Byte printableForReport, final Integer proposalId) throws Exception {

		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(DataCollection3VO.class);
		Criteria subCritgroup = crit.createCriteria("dataCollectionGroupVO");

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT
																	// RESULTS !

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

		crit.addOrder(Order.desc("startTime"));

		List<DataCollection3VO> foundEntities = crit.list();
		return foundEntities;
	}

	/**
	 * 
	 * @param blSampleId
	 * @param printableForReport
	 * @param proposalId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DataCollection3VO> findBySample(final Integer blSampleId, String sampleName, final Byte printableForReport, final Integer proposalId)
			throws Exception {

		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(DataCollection3VO.class);
		Criteria subCritgroup = crit.createCriteria("dataCollectionGroupVO");

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT
																	// RESULTS !
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

		crit.addOrder(Order.desc("startTime"));

		List<DataCollection3VO> foundEntities = crit.list();
		return foundEntities;
	}

	public DataCollection3VO loadEager(DataCollection3VO vo) throws Exception {
		DataCollection3VO newVO = this.findByPk(vo.getDataCollectionId(), true, true);
		return newVO;
	}

	private static Double getDoubleValue(Float f) {
		if (f != null)
			return Double.parseDouble(f.toString());
		return null;
	}

	/**
	 * returns the XDSInfo for a given dataCollectionId
	 * 
	 * @param dataCollectionId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public XDSInfo findForWSXDSInfo(final Integer dataCollectionId) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		List orders = (List) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				Query q = entityManager.createNativeQuery("SELECT BLSession.beamLineSetupId, DataCollection.detectorId,  "
						+ "DataCollection.axisRange, DataCollection.axisStart, DataCollection.axisEnd, DataCollection.detectorDistance, "
						+ "DataCollection.fileTemplate, DataCollection.imageDirectory, DataCollection.imageSuffix, "
						+ "DataCollection.numberOfImages, DataCollection.startImageNumber, " + "DataCollection.phiStart, DataCollection.kappaStart, "
						+ "DataCollection.wavelength, DataCollection.xbeam, DataCollection.ybeam " + "FROM DataCollection, DataCollectionGroup, BLSession "
						+ "WHERE DataCollection.dataCollectionId = " + dataCollectionId + " AND "
						+ "DataCollection.dataCollectionGroupId = DataCollectionGroup.dataCollectionGroupId AND "
						+ "DataCollectionGroup.sessionId = BLSession.sessionId ");
				List orders = q.getResultList();
				return orders;

			}
		});

		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		XDSInfo xds = null;

		if (orders == null || orders.size() < 1)
			return null;
		int nb = orders.size();
		for (int i = 0; i < nb; i++) {
			Object[] o = (Object[]) orders.get(i);
			int j = 0;
			Integer beamLineSetupId = (Integer) o[j++];
			Integer detectorId = (Integer) o[j++];
			Double axisRange = getDoubleValue((Float) o[j++]);
			Double axisStart = getDoubleValue((Float) o[j++]);
			Double axisEnd = getDoubleValue((Float) o[j++]);
			Double detectorDistance = getDoubleValue((Float) o[j++]);
			String fileTemplate = (String) o[j++];
			String imageDirectory = (String) o[j++];
			String imageSuffix = (String) o[j++];
			Integer numberOfImages = (Integer) o[j++];
			Integer startImageNumber = (Integer) o[j++];
			Double phiStart = getDoubleValue((Float) o[j++]);
			Double kappaStart = getDoubleValue((Float) o[j++]);
			Double wavelength = getDoubleValue((Float) o[j++]);
			Double xbeam = getDoubleValue((Float) o[j++]);
			Double ybeam = getDoubleValue((Float) o[j++]);

			Double polarisation = null;
			BeamLineSetup3Service beamLineSetupService = (BeamLineSetup3Service) ejb3ServiceLocator.getLocalService(BeamLineSetup3Service.class);
			BeamLineSetup3VO beamLineSetup = null;
			if (beamLineSetupId != null)
				beamLineSetup = beamLineSetupService.findByPk(beamLineSetupId);
			if (beamLineSetup != null)
				polarisation = beamLineSetup.getPolarisation();

			Double detectorPixelSizeHorizontal = null;
			Double detectorPixelSizeVertical = null;
			String detectorManufacturer = null;
			String detectorModel = null;
			Detector3Service detectorService = (Detector3Service) ejb3ServiceLocator.getLocalService(Detector3Service.class);
			Detector3VO detector = null;
			if (detectorId != null)
				detector = detectorService.findByPk(detectorId);
			if (detector != null) {
				detectorPixelSizeHorizontal = detector.getDetectorPixelSizeHorizontal();
				detectorPixelSizeVertical = detector.getDetectorPixelSizeVertical();
				detectorManufacturer = detector.getDetectorManufacturer();
				detectorModel = detector.getDetectorModel();
			}

			xds = new XDSInfo(polarisation, axisRange, axisStart, axisEnd, detectorDistance, fileTemplate, imageDirectory, imageSuffix, numberOfImages,
					startImageNumber, phiStart, kappaStart, wavelength, xbeam, ybeam, detectorPixelSizeHorizontal, detectorPixelSizeVertical,
					detectorManufacturer, detectorModel);

		}

		return xds;
	}

	/**
	 * returns the pdb full path for a given dataCollectionId
	 * 
	 * @param dataCollectionId
	 * @return
	 * @throws Exception
	 */
	public String findPdbFullPath(final Integer dataCollectionId) throws Exception {

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
		String foundEntities = res;
		return foundEntities;
	}

	/**
	 * returns the list of dataCollections with startTime > startDate
	 * 
	 * @param startDate
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<DataCollection3VO> findLastCollect(final Date startDate, final Date endDate, final String[] beamline) throws Exception {

		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(DataCollection3VO.class);

		if (startDate != null) {
			crit.add(Restrictions.ge("startTime", startDate));
		}

		if (endDate != null) {
			crit.add(Restrictions.le("endTime", endDate));
		}
		if (beamline != null && beamline.length > 0) {
			Criteria subCritgroup = crit.createCriteria("dataCollectionGroupVO");
			Criteria subCritSession = subCritgroup.createCriteria("sessionVO");
			subCritSession.add(Restrictions.in("beamlineName", beamline));
		}
		crit.addOrder(Order.desc("startTime"));

		List<DataCollection3VO> foundEntities = crit.list();
		return foundEntities;
	}

	/**
	 * get the number of datcollections which have more then 4 images
	 * 
	 * @param dcgId
	 * @return
	 * @throws Exception
	 */
	public Integer getNbOfCollects(final Integer dcgId) throws Exception {

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

	/**
	 * get the number of datacollections which have less/or 4 images
	 * 
	 * @param dcgId
	 * @return
	 * @throws Exception
	 */
	public Integer getNbOfTests(final Integer dcgId) throws Exception {

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

	/* Private methods ------------------------------------------------------ */

	/**
	 * Checks the data for integrity. E.g. if references and categories exist.
	 * 
	 * @param vo
	 *            the data to check
	 * @param create
	 *            should be true if the value object is just being created in
	 *            the DB, this avoids some checks like testing the primary key
	 * @exception VOValidateException
	 *                if data is not correct
	 */
	private void checkAndCompleteData(DataCollection3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getDataCollectionId() != null) {
				throw new IllegalArgumentException("Primary key is already set! This must be done automatically. Please, set it to null!");
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

	@SuppressWarnings("unchecked")
	public List<DataCollection3VO> findByProposalId(int proposalId) throws Exception {
		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(DataCollection3VO.class).createCriteria("dataCollectionGroupVO").createCriteria("sessionVO")
				.createCriteria("proposalVO").add(Restrictions.eq("proposalId", proposalId));

		List<DataCollection3VO> foundEntities = crit.list();
		return foundEntities;
	}
	
	@SuppressWarnings("unchecked")
	public List<DataCollection3VO> findByProposalId(int proposalId, int dataCollectionId) throws Exception {
		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(DataCollection3VO.class).add(Restrictions.eq("dataCollectionId", dataCollectionId));
		crit.createCriteria("dataCollectionGroupVO").createCriteria("sessionVO")
				.createCriteria("proposalVO").add(Restrictions.eq("proposalId", proposalId));

		List<DataCollection3VO> foundEntities = crit.list();
		return foundEntities;
	}

}