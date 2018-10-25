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

package ispyb.server.mx.services.sample;

import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.BLSampleInfo;
import ispyb.server.mx.vos.sample.BLSampleWS3VO;
import ispyb.server.mx.vos.sample.SampleInfo;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface BLSample3Service {

	/**
	 * Create new BLSample3.
	 * 
	 * @param vo
	 *            the entity to persist
	 * @return the persisted entity
	 */
	public BLSample3VO create(final BLSample3VO vo) throws Exception;

	/**
	 * Update the BLSample3 data.
	 * 
	 * @param vo
	 *            the entity data to update
	 * @return the updated entity
	 */
	public BLSample3VO update(final BLSample3VO vo) throws Exception;

	/**
	 * Remove the BLSample3 from its pk.
	 * 
	 * @param vo
	 *            the entity to remove
	 */
	public void deleteByPk(final Integer pk) throws Exception;

	/**
	 * Remove the BLSample3.
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final BLSample3VO vo) throws Exception;

	/**
	 * Finds a BLSample3 entity by its primary key and set linked value objects if necessary.
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the Scientist value object
	 */
	public BLSample3VO findByPk(final Integer pk, final boolean withEnergyScan, final boolean withSubSamples, final boolean withSampleImages) throws Exception;

	/**
	 * Find all BLSample3 and set linked value objects if necessary.
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	public List<BLSample3VO> findAll(final boolean withEnergyScan, final boolean withSubSamples) throws Exception;

	public List<BLSample3VO> findByProposalIdAndBlSampleStatus(final Integer proposalId, final String status)
			throws Exception;

	public List<BLSample3VO> findByCrystalNameCode(final Integer crystalId, final String name, final String code)
			throws Exception;

	/**
	 * find the sample info (Tuple of BLSample, Container, Crystal, Protein, DiffractionPlan) for a given blsampleId
	 * 
	 * @param proposalId
	 * @param beamlineLocation
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public BLSampleInfo findSampleInfoForPk(final Integer blSampleId) throws Exception;

	/**
	 * find all sample info (Tuple of BLSample, Container, Crystal, Protein, DiffractionPlan) for a specified proposal
	 * and a specified beamlineLocation and a given status(blSample or container)
	 * 
	 * @param proposalId
	 * @param beamlineLocation
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public BLSampleInfo[] findSampleInfoForProposal(final Integer proposalId, final String beamlineLocation,
			final String status) throws Exception;

	/**
	 * find all sample info (Tuple of BLSample, Container, Crytsal, Protein, DiffractionPlan) for a specified proposal
	 * and a specified beamlineLocation and a given status(blSample or container)
	 * 
	 * @param proposalId
	 * @param beamlineLocation
	 * @param status
	 * @param detachLight
	 * @return
	 * @throws Exception
	 */
	public BLSampleInfo[] findForWSSampleInfoForProposal(final Integer proposalId, final String beamlineLocation,
			final String status) throws Exception;

	/**
	 * find only sample info for a specified proposal and a specified beamlineLocation and a given status(blSample or
	 * container)
	 * 
	 * @param proposalId
	 * @param beamlineLocation
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public BLSampleWS3VO[] findForWSSampleInfoForProposalLight(final Integer proposalId, final String beamlineLocation,
			final String status) throws Exception;

	/**
	 * find all sample info (Tuple of SampleInfo) for a specified proposal
	 * and a specified beamlineLocation and a given status(blSample or container)
	 * if a crystalId is specified then gives all the list of samples linked to this crystal
	 * if location and status is null then gives all for the proposal
	 * 
	 * @param proposalId
	 * @param beamlineLocation
	 * @param status
	 * @param crystalId
	 * @return
	 * @throws Exception
	 */
	public SampleInfo[] findForWSSampleInfoLight(final Integer proposalId, final Integer crystalId, final String beamlineLocation,
			final String status) throws Exception;
	
	/**
	 * Find a sample by its primary key -- webservices object
	 * 
	 * @param pk
	 * @param withLink1
	 * @param withLink2
	 * @return
	 * @throws Exception
	 */
	public BLSample3VO loadEager(BLSample3VO vo) throws Exception;

	public BLSampleWS3VO findForWSByPk(final Integer pk, final boolean withEnergyScan, final boolean withSubSamples, final boolean withSampleImages) throws Exception;

	public List<BLSample3VO> findByCrystalId(final Integer crystalId) throws Exception;

	public List<BLSample3VO> findByName(final String name) throws Exception;

	public List<BLSample3VO> findByCode(final String code) throws Exception;

	public List<BLSample3VO> findByStatus(final String status) throws Exception;
	
	public List<BLSample3VO> findFiltered(final Integer proposalId, final Integer proteinId, final String acronym,
			final Integer crystalId, final String name, final String code, final String statusByte,
			final Byte isInSampleChanger, final Integer shippingId, final String sortType) throws Exception;

	public List<BLSample3VO> findByProposalIdAndDewarNull(final Integer proposalId) throws Exception;

	public List<BLSample3VO> findByProposalId(final Integer proposalId) throws Exception;

	public List<BLSample3VO> findByNameAndCodeAndProposalId(final String name, final String code,
			final Integer proposalId) throws Exception;

	public List<BLSample3VO> findByShippingId(final Integer shippingId, final Integer sortView) throws Exception;
	
	public List<BLSample3VO> findByShippingIdOrder(final Integer shippingId, final Integer sortView) throws Exception;

	public List<BLSample3VO> findByDewarId(final List<Integer> dewarIds, final Integer sortView) throws Exception;
	
	public List<BLSample3VO> findByDewarId(final Integer dewarId, final Integer sortView) throws Exception;

	public List<BLSample3VO> findByContainerId(final Integer containerId) throws Exception;

	public List<BLSample3VO> findByProteinId(final Integer proteinId) throws Exception;

	public List<BLSample3VO> findByProposalIdAndIsInSampleChanger(final Integer proposalId, final Byte isInSampleChanger)
			throws Exception;

	public List<BLSample3VO> findByAcronymAndProposalId(final String acronym, final Integer proposalId, final String sortType)
			throws Exception;

	public List<BLSample3VO> findByNameAndProteinId(final String name, final Integer proteinId, final Integer shippingId)
			throws Exception;

	public List<BLSample3VO> findByCodeAndShippingId(final String dmCode, final Integer shippingId) throws Exception;

	public List<BLSample3VO> findByCodeAndProposalId(final String dmCode, final Integer proposalId) throws Exception;

	public void resetIsInSampleChanger(final Integer proposalId) throws Exception;
	
	public SampleInfo findForWSSampleInfo(final Integer sampleId) throws Exception ;


}
