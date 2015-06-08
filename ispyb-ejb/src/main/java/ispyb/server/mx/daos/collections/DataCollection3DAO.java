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

import ispyb.server.mx.vos.collections.DataCollection3VO;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

/**
 * <p>
 * The data access object for DataCollection3 objects (rows of table DataCollection).
 * </p>
 */
@Local
public interface DataCollection3DAO {

	/* Creation/Update methods ---------------------------------------------- */

	/**
	 * <p>
	 * Insert the given value object. TODO update this comment for insertion details.
	 * </p>
	 */
	public void create(DataCollection3VO vo) throws Exception;

	/**
	 * <p>
	 * Update the given value object. TODO update this comment for update details.
	 * </p>
	 * 
	 * @throws VOValidateException
	 *             if the value object data integrity is not ok.
	 */
	public DataCollection3VO update(DataCollection3VO vo) throws Exception;

	/* Deletion methods ----------------------------------------------------- */

	/**
	 * <p>
	 * Deletes the given value object.
	 * </p>
	 * 
	 * @param vo
	 *            the value object to delete.
	 */
	public void delete(DataCollection3VO vo);

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * Returns the DataCollection3VO instance matching the given primary key.
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
	public DataCollection3VO findByPk(Integer pk, boolean fetchImage, boolean fetchScreening,
			boolean fetchAutoProcIntegration);

	/**
	 * <p>
	 * Returns the DataCollection3VO instances.
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
	public List<DataCollection3VO> findAll();

	public List<DataCollection3VO> findByShippingId(Integer shippingId);

	/**
	 * Find the dataCollections for a given sample
	 * 
	 * @param blSampleId
	 * @return
	 */
	public List<DataCollection3VO> findLastDataCollectionBySample(Integer blSampleId);

	public List<DataCollection3VO> findFiltered(String imageDirectory, String imagePrefix,
			Integer dataCollectionNumber, Integer sessionId, Byte printableForReport, boolean withScreenings,
			Integer dataCollectionId);

	/**
	 * find dataCollection with the given image fileLocation and the given image fileName
	 * 
	 * @param fileLocation
	 * @param fileName
	 * @return
	 */
	public List<DataCollection3VO> findByImageFile(String fileLocation, String fileName);

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
	public List<DataCollection3VO> findByCustomQuery(Integer proposalId, String sampleName, String proteinAcronym,
			String beamlineName, Date experimentDateStart, Date experimentDateEnd, Integer minNumberOfImages,
			Integer maxNumberOfImages, String imagePrefix, Byte onlyPrintableForReport, Integer maxRecords,
			boolean withScreenings);

	/**
	 * 
	 * @param proteinAcronym
	 * @param printableForReport
	 * @param proposalId
	 * @return
	 */
	public List<DataCollection3VO> findByProtein(String proteinAcronym, Byte printableForReport, Integer proposalId,
			boolean withScreenings);

	/**
	 * 
	 * @param blSampleId
	 * @param printableForReport
	 * @param proposalId
	 * @return
	 */
	public List<DataCollection3VO> findBySample(Integer blSampleId, String sampleName, Byte printableForReport,
			Integer proposalId, boolean withScreenings);

	/**
	 * returns a XDSInfo for a given dataCollectionId
	 * 
	 * @param dataCollectionId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List findXDSInfo(Integer dataCollectionId);

	/**
	 * returns the pdb full path for a given dataCollectionId
	 * 
	 * @param dataCollectionId
	 * @return
	 * @throws Exception
	 */
	public String findPdbFullPath(Integer dataCollectionId);

	/**
	 * returns the list of dataCollections with startTime > startDate
	 * 
	 * @param startDate
	 * @return
	 * @throws Exception
	 */
	public List<DataCollection3VO> findLastCollect(Date startDate, Date endDate, String[] beamline,
			boolean withScreenings);

	public Integer getNbOfCollects(Integer dcgId) throws Exception;

	public Integer getNbOfTests(Integer dcgId) throws Exception;

}
