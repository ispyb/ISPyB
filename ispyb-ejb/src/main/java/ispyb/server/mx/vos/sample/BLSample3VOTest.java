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
 ******************************************************************************************************************************/

package ispyb.server.mx.vos.sample;

import static org.junit.Assert.assertTrue;
import ispyb.common.util.Constants;
import ispyb.server.common.vos.EJB3Test;
import ispyb.server.mx.services.sample.BLSample3Service;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class BLSample3VOTest extends EJB3Test {

	private List<BLSample3VO> blSamples3VOs = new ArrayList<BLSample3VO>();

	private BLSample3Service blSample3Service;

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		this.blSample3Service = (BLSample3Service) serviceLocator.getRemoteService(BLSample3Service.class, properties);
	}

	@After
	public void tearDown() throws Exception {
	}

	// @Test
	// public void findAll() throws Exception {
	// this.blSamples3VOs = blSample3Service.findAll(false);
	// assertTrue(this.blSamples3VOs.size() > 1);
	// }

	@Test
	public void findByPk() throws Exception {
		BLSample3VO blSample3VO = blSample3Service.findByPk(3120, false, false, false);
		assertTrue(blSample3VO != null);
	}

	@Test
	public void findByCode() throws Exception {
		String code = "None";
		this.blSamples3VOs = blSample3Service.findByCode(code);
		for (BLSample3VO blSample3VO : this.blSamples3VOs) {
			assertTrue(blSample3VO.getCode().equalsIgnoreCase(code));
		}
	}

	@Test
	public void findByName() throws Exception {
		String name = "test";
		this.blSamples3VOs = blSample3Service.findByName(name);
		for (BLSample3VO blSample3VO : this.blSamples3VOs) {
			assertTrue(blSample3VO.getName().equalsIgnoreCase(name));
		}
	}

	@Test
	public void findByContainerId() throws Exception {
		Integer containerId = 858;
		this.blSamples3VOs = blSample3Service.findByContainerId(containerId);
		for (BLSample3VO blSample3VO : this.blSamples3VOs) {
			assertTrue(blSample3VO.getContainerVOId().equals(containerId));
		}

	}

	@Test
	public void findByProposalIdAndDewarNullTest() throws Exception {
		List<BLSample3VO> list = blSample3Service.findByProposalIdAndDewarNull(1170);
		for (BLSample3VO blSample3VO : list) {
			System.out.println(blSample3VO.getName());
		}
	}

	@Test
	public void findByShippingId() throws Exception {
		int shippingCode = 1020;
		List<BLSample3VO> list = blSample3Service.findByShippingId(shippingCode, null);
		for (BLSample3VO blSample3VO : list) {
			assertTrue(blSample3VO.getContainerVO().getDewarVO().getShippingVO().getShippingId().equals(shippingCode));
		}
		System.out.println("findByShippingId " + list.size());
	}

	@Test
	public void findByProteinIdTest() throws Exception {
		int proteinId = 681;
		List<BLSample3VO> list = blSample3Service.findByProteinId(proteinId);
		for (BLSample3VO blSample3VO : list) {
			assertTrue(blSample3VO.getCrystalVO().getProteinVO().getProteinId().equals(proteinId));
		}
		System.out.println("findByProteinId " + list.size());
	}

	@Test
	public void findByProposalAndBeamlineAndStatusLight() throws Exception {

		Integer proposalId = 3114;// MX1428
		String beamlineLocation = "ID23-1";
		String status = Constants.PROCESSING_STATUS;
		int length = 0;

		BLSampleWS3VO[] tab = blSample3Service
				.findForWSSampleInfoForProposalLight(proposalId, status, beamlineLocation);
		if (tab != null) {
			length = tab.length;
			for (int i = 0; i < tab.length; i++) {
				assertTrue(tab[i].getCrystalId() != null);
			}
		}

		System.out.println("findByProposalAndBeamlineAndStatus " + length);
	}

	@Test
	public void findByProposalAndBeamlineAndStatus() throws Exception {

		Integer proposalId = 3114;// MX1428
		String beamlineLocation = "ID23-1";
		String status = Constants.PROCESSING_STATUS;
		int length = 0;

		BLSampleInfo[] tab = blSample3Service.findForWSSampleInfoForProposal(proposalId, status, beamlineLocation);
		if (tab != null) {
			length = tab.length;
			// for (int i = 0; i < tab.length; i++) {
			// assertTrue(tab[i].getCrystal() != null);
			// }
		}

		System.out.println("findByProposalAndBeamlineAndStatus " + length);
	}

	@Test
	public void findSampleInfo() throws Exception {

		String beamline = "ID23-1";
		BLSampleInfo info = new BLSampleInfo();

		Integer blSampleId = 406154;

		for (int i = 0; i < 10; i++) {
			info = blSample3Service.findSampleInfoForPk(blSampleId);
			if (info != null) {
				assertTrue(info.getContainer().getBeamlineLocation().equals(beamline));
			}
			blSampleId = blSampleId + 1;
		}

		System.out.println("findSampleInfo() finished");
	}
}
