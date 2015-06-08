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

import static org.junit.Assert.assertTrue;
import ispyb.server.common.services.shipping.DewarTransportHistory3Service;
import ispyb.server.common.vos.EJB3Test;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class DewarTransportHistory3VOTest extends EJB3Test {

	
	private List<DewarTransportHistory3VO> dewarTransportHistory3VOs = new ArrayList<DewarTransportHistory3VO>();
	private DewarTransportHistory3Service dewarTransportHistory3Service; 
	

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		this.dewarTransportHistory3Service = (DewarTransportHistory3Service)serviceLocator.getRemoteService(DewarTransportHistory3Service.class, properties);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void findAll() throws Exception {
		this.dewarTransportHistory3VOs = dewarTransportHistory3Service.findAll(false, false);
		assertTrue(this.dewarTransportHistory3VOs.size() > 1);
	}
	
	@Test
	public void findByDewarId() throws Exception {
		int dewarId = 301548;
		this.dewarTransportHistory3VOs = dewarTransportHistory3Service.findByDewarId(dewarId);
		for (DewarTransportHistory3VO dewarTransportHistory3VO : dewarTransportHistory3VOs) {
			assertTrue(dewarTransportHistory3VO.getDewarVO().getDewarId() == dewarId);
		}
		assertTrue(this.dewarTransportHistory3VOs.size() > 1);
	}
	
}
