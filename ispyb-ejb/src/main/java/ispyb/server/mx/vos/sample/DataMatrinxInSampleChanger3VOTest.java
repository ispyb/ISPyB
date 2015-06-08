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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import ispyb.server.common.vos.EJB3Test;
import ispyb.server.mx.services.sample.DataMatrixInSampleChanger3Service;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class DataMatrinxInSampleChanger3VOTest extends EJB3Test {

	private List<DataMatrixInSampleChanger3VO> dataMatrixInSampleChanger3VOs = new ArrayList<DataMatrixInSampleChanger3VO>();

	private DataMatrixInSampleChanger3Service dataMatrixInSampleChanger3Service;

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		this.dataMatrixInSampleChanger3Service = (DataMatrixInSampleChanger3Service) serviceLocator.getRemoteService(DataMatrixInSampleChanger3Service.class, properties);
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void findByPk() throws Exception {
		DataMatrixInSampleChanger3VO dataMatrixInSampleChanger3VO = dataMatrixInSampleChanger3Service.findById(1);
		assertFalse(1 != dataMatrixInSampleChanger3VO.getDatamatrixInSampleChangerId());
	}
	
	@Test
	public void findByProposalIdAndBeamlineName() throws Exception {
		dataMatrixInSampleChanger3VOs = dataMatrixInSampleChanger3Service.findByProposalIdAndBeamlineName(2276, "My_office");
		System.out.println(dataMatrixInSampleChanger3VOs.get(0).getDatamatrixInSampleChangerId());
		assertTrue(dataMatrixInSampleChanger3VOs.size() > 0);
	}

	
}
