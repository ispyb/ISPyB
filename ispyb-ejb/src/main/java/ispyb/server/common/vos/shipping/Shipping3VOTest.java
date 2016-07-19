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

package ispyb.server.common.vos.shipping;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import ispyb.server.common.services.shipping.Shipping3Service;
import ispyb.server.common.vos.EJB3Test;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class Shipping3VOTest extends EJB3Test {

	private List<Shipping3VO> shippings = new ArrayList<Shipping3VO>();

	private Shipping3Service shipping3Service;

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		this.shipping3Service = (Shipping3Service) serviceLocator.getRemoteService(Shipping3Service.class, properties);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void findByProposalCode() throws Exception {
		String proposalCode = "FX";

		this.shippings = shipping3Service.findByProposalCode(proposalCode, true);

		for (Shipping3VO shipping3VO : shippings) {
			assertTrue(shipping3VO.getProposalVO().getCode().toUpperCase().equals(proposalCode));
			if (shipping3VO.getProposalVO().getNumber().equals("1"))
				assertTrue(shipping3VO.getDewarVOs() != null);
		}
		assertTrue(this.shippings.size() >= 1);
	}

	@Test
	public void findFilteredByMainProposer() throws Exception {
		String mp = "gordon";

		this.shippings = shipping3Service.findFiltered(null, null, null,null,  mp, null, null, null, false);

		for (Shipping3VO shipping3VO : shippings) {
			assertTrue(shipping3VO.getProposalVO().getPersonVO().getFamilyName().toLowerCase().equals(mp));
			// if (shipping3VO.getProposalVO().getNumber() == 1)
			// assertTrue(shipping3VO.getDewarVOs() != null);
		}
		assertTrue(this.shippings.size() >= 1);
	}

	@Test
	public void findFilteredByProposalCode() throws Exception {
		String proposalCode = "FX";

		this.shippings = shipping3Service.findFiltered(null, null, proposalCode, null, null, null, null, null, false);

		for (Shipping3VO shipping3VO : shippings) {
			assertTrue(shipping3VO.getProposalVO().getCode().equals(proposalCode));
			// if (shipping3VO.getProposalVO().getNumber() == 1)
			// assertTrue(shipping3VO.getDewarVOs() != null);
		}
		assertTrue(this.shippings.size() >= 1);
	}

	@Test
	public void findAll() throws Exception {
		this.shippings = shipping3Service.findAll();
		assertTrue(this.shippings.size() > 1);
	}

	@Test
	public void findByPk() throws Exception {
		Shipping3VO shipping = shipping3Service.findByPk(1000, false);
		assertFalse(1000 != shipping.getShippingId());
	}

	@Test
	public void findByStatus() throws Exception {
		String status = "opened";
		this.shippings = shipping3Service.findByStatus(status, null, false);
		for (Shipping3VO Shipping3VO : shippings) {
			assertTrue(Shipping3VO.getShippingStatus().equals(status));
		}
		assertTrue(this.shippings.size() > 1);
	}

	@Test
	public void findByProposalId() throws Exception {
		int proposalId = 1170;
		this.shippings = shipping3Service.findByProposal(proposalId, false);
		for (Shipping3VO shipping3VO : shippings) {
			assertTrue(shipping3VO.getProposalVOId() == proposalId);
		}
		assertTrue(this.shippings.size() >= 1);
	}

	@Test
	public void findByCreationData() throws Exception {
		this.shippings = shipping3Service.findByCreationDate(getDate(17, 7, 2008), false);
	}

	@Test
	public void findByCreationDataInterval() throws Exception {
		this.shippings = shipping3Service.findByCreationDateInterval(getDate(17, 7, 2008), getDate(17, 7, 2012));
		assertTrue(this.shippings.size() >= 1);
	}

	@Test
	public void findByIsStorageShipping() throws Exception {
		this.shippings = shipping3Service.findByIsStorageShipping(false);
		System.out.println(this.shippings.size());
		assertTrue(this.shippings.size() >= 1);
	}

}
