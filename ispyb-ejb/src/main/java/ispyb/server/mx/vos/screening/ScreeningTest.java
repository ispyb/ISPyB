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

package ispyb.server.mx.vos.screening;

import static org.junit.Assert.assertFalse;
import ispyb.server.common.vos.EJB3Test;


import ispyb.server.mx.services.screening.Screening3Service;
import ispyb.server.mx.services.screening.ScreeningOutput3Service;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class ScreeningTest extends EJB3Test {

	
	private Screening3Service screening3Service;

	private ScreeningOutput3Service screeningOutput3Service;

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		this.screening3Service = (Screening3Service) serviceLocator.getRemoteService(Screening3Service.class,
				properties);
		this.screeningOutput3Service = (ScreeningOutput3Service) serviceLocator.getRemoteService(
				ScreeningOutput3Service.class, properties);
	}

	@After
	public void tearDown() throws Exception {
	}

	// @Test
	// public void findAll() throws Exception {
	// this.screenings = screening3Service.findAll(false, false, false);
	// assertTrue(this.screenings.size() > 1);
	// }

	@Test
	public void findByPk() throws Exception {
		Integer screeningId = 80796;
		Screening3VO Screening3VO = screening3Service.findByPk(screeningId, false,  false);
		assertFalse(80796 != Screening3VO.getScreeningId());
	}

	@Test
	public void findScreeningOutputByPk() throws Exception {
		Integer id = 80738;
		ScreeningOutput3VO vo = screeningOutput3Service.findByPk(id, false, false);
		assertFalse(80738 != vo.getScreeningOutputId());
	}

	// @Test
	// public void findByProposalId() throws Exception {
	// int proposalId = 1170;
	// this.screenings = screening3Service.findByProposalId(proposalId);
	// for (Screening3VO Screening3VO : screenings) {
	// System.out.println(Screening3VO.getComments());
	// }
	//
	// assertTrue(this.screenings.size() > 1);
	// }

}
