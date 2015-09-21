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

package ispyb.server.common.test.services;

//import ispyb.client.biosaxs.hdf5.HDF5FileReader;
import ispyb.server.biosaxs.services.core.analysis.Analysis3Service;
import ispyb.server.biosaxs.services.core.analysis.abInitioModelling.AbInitioModelling3Service;
import ispyb.server.biosaxs.services.core.analysis.primaryDataProcessing.PrimaryDataProcessing3Service;
import ispyb.server.biosaxs.services.sql.SQLQueryKeeper;
import ispyb.server.biosaxs.services.utils.BiosaxsZipper;
import ispyb.server.biosaxs.services.utils.reader.dat.FramesReaderFactory;
import ispyb.server.biosaxs.services.utils.reader.dat.OneDimensionalFileReader;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.services.shipping.Shipping3Service;
import ispyb.server.common.services.shipping.external.External3Service;
import ispyb.server.common.test.SaxsEJB3Test;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.server.mx.services.sample.BLSample3Service;
import ispyb.server.mx.services.sample.Crystal3Service;
import ispyb.server.mx.vos.collections.Workflow3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import org.apache.commons.io.IOUtils;

public class CrimsTest extends SaxsEJB3Test {

	private External3Service external3Service;
	private Crystal3Service crystalService;
	
	private AbInitioModelling3Service abinitio;

	@Before
	public void setUp() throws Exception {
		this.external3Service = (External3Service) serviceLocator.getRemoteService(External3Service.class, properties);
		this.crystalService = (Crystal3Service) serviceLocator.getRemoteService(Crystal3Service.class, properties);
		
		this.abinitio = (AbInitioModelling3Service) serviceLocator.getRemoteService(AbInitioModelling3Service.class, properties);
	}

	
	@Test
	public void readFile() throws Exception{
//		OneDimensionalFileReader reader = new OneDimensionalFileReader("/data/pyarch/bm29/opd29/56/1d/GMP2_421_00019.dat");
//		System.out.println(new Gson().toJson(reader.readFile()));
		int x = 662593;
		List<Integer> list = new ArrayList<Integer>();
		list.add(x);
		FramesReaderFactory factory = new FramesReaderFactory();
		System.out.println(new Gson().toJson(factory.getData(list)));
	}
	
//	@Test
	public void TestCrystal() throws Exception{
		Crystal3VO crustal= this.crystalService.findByPk(367507, false);
		new Gson().toJson(crustal);
	}
	
	public class TestExclStrat implements ExclusionStrategy {

		public boolean shouldSkipClass(Class<?> arg0) {
			return false;
		}

		public boolean shouldSkipField(FieldAttributes f) {
			if (f.getName().equals("diffractionPlanVO")) {
				return true;
			}
			if (f.getName().equals("proposalVOs")) {
				return true;
			}
			if (f.getName().equals("sessionVOs")) {
				return true;
			}
			if (f.getName().equals("proteinVOs")) {
				return true;
			}
			if (f.getName().equals("shippingVOs")) {
				return true;
			}
			if (f.getName().equals("crystalVOs")) {
				return true;
			}
			if (f.getName().equals("sampleVOs")) {
				return true;
			}
			if (f.getName().equals("dewarVOs")) {
				return true;
			}
			if (f.getName().equals("dataCollectionGroupVOs")) {
				return true;
			}
			if (f.getName().equals("xfeSpectrumVOs")) {
				return true;
			}
			if (f.getName().equals("energyScanVOs")) {
				return true;
			}
			if (f.getName().equals("containerVOs")) {
				return true;
			}
			if (f.getName().equals("dewarTransportHistoryVOs")) {
				return true;
			}
			if (f.getName().equals("blSubSampleVOs")) {
				return true;
			}
			return false;
		}

	}
	
//	@Test
	public void getData(){
		List<Map<String, Object>> result = this.external3Service.getDataCollectionByProposal("mx", "415");
		System.out.println(result.toString());
	}
	
	@Test
	public void setWorkflow(){
		String json = "{workflowTitle: 'BioSAXS Post Processing' }";
		Workflow3VO workflow = new Gson().fromJson(json, Workflow3VO.class);
	}

}