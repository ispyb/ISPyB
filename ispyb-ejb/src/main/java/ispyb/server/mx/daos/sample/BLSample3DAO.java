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

package ispyb.server.mx.daos.sample;

import ispyb.server.mx.vos.sample.BLSample3VO;

import java.util.List;

import javax.ejb.Local;

/**
 * <p>
 * The data access object for BLSample3 objects (rows of table BLSample).
 * </p>
 */
@Local
public interface BLSample3DAO {

	/* Creation/Update methods ---------------------------------------------- */

	/**
	 * <p>
	 * Insert the given value object. TODO update this comment for insertion details.
	 * </p>
	 */
	public void create(BLSample3VO vo) throws Exception;

	/**
	 * <p>
	 * Update the given value object. TODO update this comment for update details.
	 * </p>
	 * 
	 * @throws VOValidateException
	 *             if the value object data integrity is not ok.
	 */
	public BLSample3VO update(BLSample3VO vo) throws Exception;

	/* Deletion methods ----------------------------------------------------- */

	/**
	 * <p>
	 * Deletes the given value object.
	 * </p>
	 * 
	 * @param vo
	 *            the value object to delete.
	 */
	public void delete(BLSample3VO vo);

	/* Find methods --------------------------------------------------------- */

	/**
	 * <p>
	 * Returns the BLSample3VO instance matching the given primary key.
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
	public BLSample3VO findByPk(Integer pk, boolean fetchEnergyScan, boolean fetchSubSamples);

	/**
	 * <p>
	 * Returns the BLSample3VO instances.
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
	public List<BLSample3VO> findAll(boolean fetchEnergyScan, boolean fetchSubSamples);

	/**
	 * 
	 * @param proposalId
	 * @param crystalId
	 * @param name
	 * @param code
	 * @param status
	 * @param containerId
	 * @return
	 */
	public List<BLSample3VO> findFiltered(Integer proposalId, Integer proteinId, String acronym, Integer crystalId,
			String name, String code, String status, Byte isInSampleChanger, Integer shippingId);

	/**
	 * find all sample info (Tuple of BLSample, Container, Crystal, Protein, DiffractionPlan) for a specified proposal
	 * and a specified beamlineLocation and a given status(blSample or container)
	 * 
	 * @param proposalId
	 * @param beamlineLocation
	 * @param status
	 * @return
	 */
	public List<BLSample3VO> findSampleInfoForProposal(Integer proposalId, String beamlineLocation, String status)
			throws Exception;
	
	public List findSampleInfoLightForProposal(Integer proposalId, String beamlineLocation, String status) throws Exception ;

	public List<BLSample3VO> findByProposalIdAndDewarNull(Integer proposalId);

	public List<BLSample3VO> findByShippingDewarContainer(Integer shippingId, Integer dewarId, Integer containerId,
			String dmCode, Integer sortView);

	public List<BLSample3VO> findByShippingId(Integer shippingId);

	public List<BLSample3VO> findByContainerId(Integer containerId);
	
	public List<BLSample3VO> findByShippingIdOrder(Integer shippingId, Integer sortView) ;
	
	public List findSampleInfo(Integer sampleId) throws Exception ;

}
