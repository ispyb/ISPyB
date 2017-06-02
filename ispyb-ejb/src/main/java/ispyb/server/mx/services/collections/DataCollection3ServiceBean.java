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

import ispyb.server.common.util.ejb.EJBAccessCallback;
import ispyb.server.common.util.ejb.EJBAccessTemplate;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
<<<<<<< HEAD
import ispyb.server.mx.daos.collections.DataCollection3DAO;
=======

>>>>>>> aa35504f989cd6f3f35a4019958215da79e405e3
import ispyb.server.mx.vos.collections.BeamLineSetup3VO;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.DataCollectionWS3VO;
import ispyb.server.mx.vos.collections.Detector3VO;
import ispyb.server.mx.vos.collections.XDSInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

/**
 * <p>
 * This session bean handles ISPyB DataCollection3.
 * </p>
 */
@Stateless
public class DataCollection3ServiceBean implements DataCollection3Service, DataCollection3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(DataCollection3ServiceBean.class);

	@EJB
	private DataCollection3DAO dao;

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
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (DataCollection3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				dao.create(vo);
				return vo;
			}

		});
	}

	/**
	 * Update the DataCollection3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public DataCollection3VO update(final DataCollection3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);

		return (DataCollection3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				return dao.update(vo);
			}

		});
	}

	/**
	 * Remove the DataCollection3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				DataCollection3VO vo = findByPk(pk, false, false);
				// TODO Edit this business code
				delete(vo);
				return vo;
			}

		});

	}

	/**
	 * Remove the DataCollection3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final DataCollection3VO vo) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				dao.delete(vo);
				return vo;
			}

		});
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the DataCollection3 value object
	 */
	public DataCollection3VO findByPk(final Integer pk, final boolean withImage,
			final boolean withAutoProcIntegration) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (DataCollection3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				// TODO Edit this business code
				DataCollection3VO found = dao.findByPk(pk, withImage, withAutoProcIntegration);
				return found;
			}

		});
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
	public DataCollectionWS3VO findForWSByPk(final Integer pk, final boolean withImage, 
			final boolean withAutoProcIntegration) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (DataCollectionWS3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				checkCreateChangeRemoveAccess();
				DataCollection3VO found = dao.findByPk(pk, withImage, withAutoProcIntegration);
				DataCollectionWS3VO sesLight = getWSDataCollectionVO(found);
				return sesLight;
			}

		});
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

	private DataCollectionWS3VO[] getWSDataCollectionVOs(List<DataCollection3VO> entities)
			throws CloneNotSupportedException {
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
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<DataCollection3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<DataCollection3VO> foundEntities = dao.findAll();
				return foundEntities;
			}

		});
	}

	@SuppressWarnings("unchecked")
	public List<DataCollection3VO> findByShippingId(final Integer shippingId) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<DataCollection3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<DataCollection3VO> foundEntities = dao.findByShippingId(shippingId);
				return foundEntities;
			}

		});
	}

	/**
	 * Check if user has access rights to create, change and remove DataCollection3 entities. If not set rollback only
	 * and throw AccessDeniedException
	 * 
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
				// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
				// to the one checking the needed access rights
				// autService.checkUserRightToChangeAdminData();
				return null;
			}

		});
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
				List<DataCollection3VO> foundEntities = dao.findLastDataCollectionBySample(blSampleId);
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
	private List<DataCollection3VO> getLightDataCollectionVOs(List<DataCollection3VO> entities)
			throws CloneNotSupportedException {
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
	 * Find a dataCollection with the image directory, the image prefix and the dataCollection Number
	 * 
	 * @param imageDirectory
	 * @param imagePrefix
	 * @param dataCollectionNumber
	 * @return
	 * @throws Exception
	 */
	public DataCollectionWS3VO findForWSDataCollectionFromImageDirectoryAndImagePrefixAndNumber(
			final String imageDirectory, final String imagePrefix, final Integer dataCollectionNumber) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (DataCollectionWS3VO) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<DataCollection3VO> foundEntities = dao.findFiltered(imageDirectory, imagePrefix,
						dataCollectionNumber, null, null, null);
				DataCollectionWS3VO[] vos = getWSDataCollectionVOs(foundEntities);
				if (vos == null || vos.length == 0)
					return null;
				return vos[0];
			}

		});
	}

	/**
	 * Find a dataCollection with the image fileLocation and image fileName
	 * 
	 * @param fileLocation
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public DataCollectionWS3VO findForWSDataCollectionIdFromFileLocationAndFileName(final String fileLocation,
			final String fileName) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		EJBAccessCallback callBack = new EJBAccessCallback() {

			public DataCollectionWS3VO[] doInEJBAccess(Object parent) throws Exception {
				List<DataCollection3VO> foundEntities = dao.findByImageFile(fileLocation, fileName);
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
	public DataCollection3VO findForDataCollectionIdFromFileLocationAndFileName(final String fileLocation,
			final String fileName) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		EJBAccessCallback callBack = new EJBAccessCallback() {

			public List<DataCollection3VO> doInEJBAccess(Object parent) throws Exception {
				List<DataCollection3VO> foundEntities = dao.findByImageFile(fileLocation, fileName);
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
	public List<DataCollection3VO> findFiltered(final String imageDirectory, final String imagePrefix,
			final Integer dataCollectionNumber, final Integer sessionId, final Byte printableForReport, final Integer dataCollectionGroupId) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<DataCollection3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<DataCollection3VO> foundEntities = dao.findFiltered(imageDirectory, imagePrefix,
						dataCollectionNumber, sessionId, printableForReport, dataCollectionGroupId);
				return foundEntities;
			}

		});
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
	public List<DataCollection3VO> findByCustomQuery(final Integer proposalId, final String sampleName,
			final String proteinAcronym, final String beamlineName, final Date experimentDateStart,
			final Date experimentDateEnd, final Integer minNumberOfImages, final Integer maxNumberOfImages,
			final String imagePrefix, final Byte onlyPrintableForReport, final Integer maxRecords) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<DataCollection3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<DataCollection3VO> foundEntities = dao.findByCustomQuery(proposalId, sampleName, proteinAcronym,
						beamlineName, experimentDateStart, experimentDateEnd, minNumberOfImages, maxNumberOfImages,
						imagePrefix, onlyPrintableForReport, maxRecords);
				return foundEntities;
			}

		});
	}

	/**
	 * 
	 * @param proteinAcronym
	 * @param printableForReport
	 * @param proposalId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DataCollection3VO> findByProtein(final String proteinAcronym, final Byte printableForReport,
			final Integer proposalId) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<DataCollection3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<DataCollection3VO> foundEntities = dao.findByProtein(proteinAcronym, printableForReport,
						proposalId);
				return foundEntities;
			}

		});
	}

	/**
	 * 
	 * @param blSampleId
	 * @param printableForReport
	 * @param proposalId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DataCollection3VO> findBySample(final Integer blSampleId, final String sampleName,
			final Byte printableForReport, final Integer proposalId) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<DataCollection3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<DataCollection3VO> foundEntities = dao.findBySample(blSampleId, sampleName, printableForReport,
						proposalId);
				return foundEntities;
			}

		});
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
				List foundIds = dao.findXDSInfo(dataCollectionId);
				return foundIds;
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
			BeamLineSetup3Service beamLineSetupService = (BeamLineSetup3Service) ejb3ServiceLocator
					.getLocalService(BeamLineSetup3Service.class);
			BeamLineSetup3VO beamLineSetup = null;
			if (beamLineSetupId != null)
				beamLineSetup = beamLineSetupService.findByPk(beamLineSetupId);
			if (beamLineSetup != null)
				polarisation = beamLineSetup.getPolarisation();

			Double detectorPixelSizeHorizontal = null;
			Double detectorPixelSizeVertical = null;
			String detectorManufacturer = null;
			String detectorModel = null;
			Detector3Service detectorService = (Detector3Service) ejb3ServiceLocator
					.getLocalService(Detector3Service.class);
			Detector3VO detector = null;
			if (detectorId != null)
				detector = detectorService.findByPk(detectorId);
			if (detector != null) {
				detectorPixelSizeHorizontal = detector.getDetectorPixelSizeHorizontal();
				detectorPixelSizeVertical = detector.getDetectorPixelSizeVertical();
				detectorManufacturer = detector.getDetectorManufacturer();
				detectorModel = detector.getDetectorModel();
			}

			xds = new XDSInfo(polarisation, axisRange, axisStart, axisEnd, detectorDistance, fileTemplate,
					imageDirectory, imageSuffix, numberOfImages, startImageNumber, phiStart, kappaStart, wavelength,
					xbeam, ybeam, detectorPixelSizeHorizontal, detectorPixelSizeVertical, detectorManufacturer,
					detectorModel);

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
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (String) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				String foundEntities = dao.findPdbFullPath(dataCollectionId);
				return foundEntities;
			}

		});
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
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (List<DataCollection3VO>) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				List<DataCollection3VO> foundEntities = dao.findLastCollect(startDate, endDate, beamline
						);
				return foundEntities;
			}

		});
	}

	/**
	 * get the number of datcollections which have more then 4 images
	 * 
	 * @param dcgId
	 * @return
	 * @throws Exception
	 */
	public Integer getNbOfCollects(final Integer dcgId) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Integer) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				Integer foundEntities = dao.getNbOfCollects(dcgId);
				return foundEntities;
			}

		});
	}

	/**
	 * get the number of datacollections which have less/or 4 images
	 * 
	 * @param dcgId
	 * @return
	 * @throws Exception
	 */
	public Integer getNbOfTests(final Integer dcgId) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		return (Integer) template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				Integer foundEntities = dao.getNbOfCollects(dcgId);
				return foundEntities;
			}

		});
	}

}