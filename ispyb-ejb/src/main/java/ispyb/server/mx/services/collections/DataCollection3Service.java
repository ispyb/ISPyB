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

import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.DataCollectionWS3VO;
import ispyb.server.mx.vos.collections.XDSInfo;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

@Remote
public interface DataCollection3Service {

	/**
	 * Create new DataCollection3.
	 * 
	 * @param vo
	 *            the entity to persist
	 * @return the persisted entity
	 */
	public DataCollection3VO create(final DataCollection3VO vo) throws Exception;

	/**
	 * Update the DataCollection3 data.
	 * 
	 * @param vo
	 *            the entity data to update
	 * @return the updated entity
	 */
	public DataCollection3VO update(final DataCollection3VO vo) throws Exception;

	/**
	 * Remove the DataCollection3 from its pk.
	 * 
	 * @param vo
	 *            the entity to remove
	 */
	public void deleteByPk(final Integer pk) throws Exception;

	/**
	 * Remove the DataCollection3.
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final DataCollection3VO vo) throws Exception;

	/**
	 * Finds a DataCollection3 entity by its primary key and set linked value objects if necessary.
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the Scientist value object
	 */
	public DataCollection3VO findByPk(final Integer pk, final boolean withImage,
			final boolean withAutoProcIntegration) throws Exception;

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
			final boolean withAutoProcIntegration) throws Exception;

	/**
	 * Find all DataCollection3 and set linked value objects if necessary.
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	public List<DataCollection3VO> findAll() throws Exception;

	public List<DataCollection3VO> findByShippingId(final Integer shippingId) throws Exception;

	/**
	 * Find the dataCollections for a given sample
	 * 
	 * @param blSampleId
	 * @param detachLight
	 * @return
	 * @throws Exception
	 */
	public DataCollectionWS3VO[] findForWSLastDataCollectionBySample(final Integer blSampleId) throws Exception;

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
			final String imageDirectory, final String imagePrefix, final Integer dataCollectionNumber) throws Exception;

	/**
	 * Find a dataCollection with the image fileLocation and image fileName
	 * 
	 * @param fileLocation
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public DataCollectionWS3VO findForWSDataCollectionIdFromFileLocationAndFileName(final String fileLocation,
			final String fileName) throws Exception;

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
	public List<DataCollection3VO> findFiltered(final String imageDirectory, final String imagePrefix,
			final Integer dataCollectionNumber, final Integer sessionId, final Byte printableForReport,
			final Integer dataCollectionGroupId) throws Exception;

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
	 */
	public List<DataCollection3VO> findByCustomQuery(final Integer proposalId, final String sampleName,
			final String proteinAcronym, final String beamlineName, final Date experimentDateStart,
			final Date experimentDateEnd, final Integer minNumberOfImages, final Integer maxNumberOfImages,
			final String imagePrefix, final Byte onlyPrintableForReport, final Integer maxRecords) throws Exception;

	/**
	 * 
	 * @param proteinAcronym
	 * @param printableForReport
	 * @param proposalId
	 * @return
	 */
	public List<DataCollection3VO> findByProtein(final String proteinAcronym, final Byte printableForReport,
			final Integer proposalId) throws Exception;

	/**
	 * 
	 * @param blSampleId
	 * @param printableForReport
	 * @param proposalId
	 * @return
	 */
	public List<DataCollection3VO> findBySample(final Integer blSampleId, final String sampleName,
			final Byte printableForReport, final Integer proposalId) throws Exception;

	public DataCollection3VO loadEager(DataCollection3VO vo) throws Exception;

	/**
	 * returns the XDSInfo for a given dataCollectionId: a tuple of DataCollection, BeamLineSetup, Detector
	 * 
	 * @param dataCollectionId
	 * @return
	 * @throws Exception
	 */
	public XDSInfo findForWSXDSInfo(final Integer dataCollectionId) throws Exception;

	/**
	 * returns the pdb full path for a given dataCollectionId
	 * 
	 * @param dataCollectionId
	 * @return
	 * @throws Exception
	 */
	public String findPdbFullPath(final Integer dataCollectionId) throws Exception;

	/**
	 * returns the list of dataCollections with startTime > startDate
	 * 
	 * @param startDate
	 * @return
	 * @throws Exception
	 */
	public List<DataCollection3VO> findLastCollect(final Date startDate, final Date endDate, final String[] beamline) throws Exception;

	public DataCollection3VO findForDataCollectionIdFromFileLocationAndFileName(final String fileLocation,
			final String fileName) throws Exception;

	/**
	 * get the number of datacollections which have more than 4 images
	 * 
	 * @param dcgId
	 * @return
	 * @throws Exception
	 */
	public Integer getNbOfCollects(final Integer dcgId) throws Exception;

	/**
	 * get the number of datacollections which have less/or 4 images
	 * 
	 * @param dcgId
	 * @return
	 * @throws Exception
	 */
	public Integer getNbOfTests(final Integer dcgId) throws Exception;
	
	public List<DataCollection3VO> findByProposalId(int proposalId) throws Exception;

	public List<DataCollection3VO> findByProposalId(int proposalId, int dataCollectionId) throws Exception;
}
