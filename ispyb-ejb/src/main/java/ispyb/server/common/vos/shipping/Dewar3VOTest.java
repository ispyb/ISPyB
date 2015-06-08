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
import ispyb.server.common.services.shipping.Dewar3Service;
import ispyb.server.common.vos.EJB3Test;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class Dewar3VOTest extends EJB3Test {

	private List<Dewar3VO> dewars = new ArrayList<Dewar3VO>();

	private Dewar3Service dewar3Service;

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		this.dewar3Service = (Dewar3Service) serviceLocator.getRemoteService(Dewar3Service.class, properties);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void findAll() throws Exception {
		this.dewars = dewar3Service.findAll(false, false);
		assertTrue(this.dewars.size() > 1);
	}

	@Test
	public void findByPk() throws Exception {
		Dewar3VO dewar3VO = dewar3Service.findByPk(1000, false, false);
		assertFalse(1000 != dewar3VO.getDewarId());
	}

	@Test
	public void findByProposalId() throws Exception {
		int proposalId = 1170;
		this.dewars = dewar3Service.findByProposalId(proposalId);
		for (Dewar3VO dewar3VO : dewars) {
			System.out.println(dewar3VO.getComments());
		}

		assertTrue(this.dewars.size() > 1);
	}

	@Test
	public void findByCode() throws Exception {
		String code = "nodata";
		this.dewars = dewar3Service.findByCode(code);
		System.out.println(this.dewars.size());
		for (Dewar3VO dewar3VO : dewars) {
			System.out.println(dewar3VO.getDewarId());
		}

		assertTrue(this.dewars.size() >= 1);
	}

	@Test
	public void findByStorageLocation() throws Exception {
		String code = "ID14";
		this.dewars = dewar3Service.findByStorageLocation(code);
		System.out.println(this.dewars.size());
		for (Dewar3VO dewar3VO : dewars) {
			System.out.println(dewar3VO.getDewarId());
		}
		assertTrue(this.dewars.size() >= 1);
	}

	@Test
	public void findByExperimentDate() throws Exception {
		this.dewars = dewar3Service.findByExperimentDate(getDate(17, 7, 2008));
		System.out.println(this.dewars.size());
		for (Dewar3VO dewar3VO : dewars) {
			System.out.println("findByExperimentDate " + dewar3VO.getShippingVO().getCreationDate());
		}
		assertTrue(this.dewars.size() >= 1);
	}

	@Test
	public void findByStatus() throws Exception {
		String status = "nostorage";
		this.dewars = dewar3Service.findByStatus(status);
		for (Dewar3VO dewar3VO : dewars) {
			assertTrue(dewar3VO.getDewarStatus().equals(status));
		}
		assertTrue(this.dewars.size() >= 1);
	}

	@Test
	public void findFiltered() throws Exception {
		this.dewars = dewar3Service.findFiltered(null, null, null, "a", null, null, null, null, null, false, false);
		for (Dewar3VO dewar3VO : dewars) {
			System.out.println(dewar3VO.getCode());
		}
		assertTrue(this.dewars.size() >= 1);
	}

	@Test
	public void findFilteredDewar() throws Exception {
		this.dewars = dewar3Service.findFiltered(null, null, null, "a", null, null, null, null, null, 1000, false, false);
		for (Dewar3VO dewar3VO : dewars) {
			System.out.println(dewar3VO.getCode());
		}
		assertTrue(this.dewars.size() >= 1);
	}

	@Test
	public void findFilteredDewarFetchSession() throws Exception {
		this.dewars = dewar3Service.findFiltered(null, null, null, null, null, null, null, null, null, null, 303267,
				null, true, false, false);
		for (Dewar3VO dewar3VO : dewars) {
			System.out.println(dewar3VO.getDewarStatus());
			System.out.println(dewar3VO.getSessionVO().getSessionId());
		}
		assertTrue(this.dewars.size() >= 1);
	}

}
