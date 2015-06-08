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
import ispyb.server.biosaxs.services.core.specimen.Specimen3Service;
import ispyb.server.biosaxs.services.utils.BiosaxsZipper;
import ispyb.server.biosaxs.vos.dataAcquisition.Specimen3VO;
import ispyb.server.biosaxs.vos.utils.comparator.SaxsDataCollectionComparator;
import ispyb.server.biosaxs.vos.utils.serializer.SpecimenExclusionStrategy;
import ispyb.server.common.test.SaxsEJB3Test;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import org.apache.commons.io.IOUtils;

public class ZipperTest extends SaxsEJB3Test {
	
	private Analysis3Service analysis3Service;
	private AbInitioModelling3Service abInitioModelling3Service;
	private PrimaryDataProcessing3Service primaryDataProcessing3Service;
	private Specimen3Service measurement3Service;

	@Before
	public void setUp() throws Exception {
		this.analysis3Service = (Analysis3Service) serviceLocator.getRemoteService(Analysis3Service.class, properties);
		this.abInitioModelling3Service = (AbInitioModelling3Service) serviceLocator.getRemoteService(AbInitioModelling3Service.class, properties);
		this.primaryDataProcessing3Service = (PrimaryDataProcessing3Service) serviceLocator.getRemoteService(PrimaryDataProcessing3Service.class, properties);
		this.measurement3Service = (Specimen3Service) serviceLocator.getRemoteService(Specimen3Service.class, properties);
		
	}
//	@Test
	public void read() throws Exception {
//		/data/pyarch/bm29/opd29/2471/1d/wat_002_ave_averbuffer.dat NOT Contains /data/pyarch/
		String DATA_FILEPATH = "/data/pyarch/";
		String LOCAL = "/work/ademaria/data/pyarch/";
		String origPath = "/data/pyarch/bm29/opd29/2471/1d/wat_002_ave_averbuffer.dat";
		System.out.println(origPath);
		System.out.println(DATA_FILEPATH);
		System.out.println(LOCAL);
		System.out.println(origPath.replace(DATA_FILEPATH, LOCAL));
		
		System.out.println(origPath.contains(DATA_FILEPATH));
//		HDF5FileReader reader = new HDF5FileReader("C:\\testing\\opd29\\HPLC\\2192\\test_006.h5");
//		reader.getH5FrameScattering(20);
	}
	
	@Test
	public void zip() throws Exception {
		System.out.println("test");
		
		BiosaxsZipper zipper = new BiosaxsZipper(analysis3Service, abInitioModelling3Service, primaryDataProcessing3Service);
//		byte[] bytes = zipper.getFilesByExperimentId(3170);
		
		byte[] bytes = zipper.getFilesByExperimentId(1768);
		FileOutputStream output = new FileOutputStream(new File("/tmp/zips/experim.zip"));
//		IOUtils.write(bytes, output);
	}
//	@Test
	public void defaultCom() throws Exception {
		SaxsDataCollectionComparator[] defaultComparator2 = {
				SaxsDataCollectionComparator.BY_EXPOSURE_TEMPERATURE, 
				SaxsDataCollectionComparator.BY_BUFFER, 
//				SaxsDataCollectionComparator.BY_TRANSMISSION,
//				SaxsDataCollectionComparator.BY_VISCOSITY,
//				SaxsDataCollectionComparator.BY_FLOW,
				SaxsDataCollectionComparator.BY_MACROMOLECULE,
				SaxsDataCollectionComparator.BY_CONCENTRATION
		};
//		System.out.println(defaultComparator2);
		Gson gson = new GsonBuilder().setExclusionStrategies(new SpecimenExclusionStrategy()).create();
		Specimen3VO m = this.measurement3Service.findSpecimenById(28454);
		System.out.println(gson.toJson(m));
		SaxsDataCollectionComparator[] options2 = new SaxsDataCollectionComparator[]{SaxsDataCollectionComparator.BY_BUFFER};
		this.TEST(options2);
		
	}
	public void TEST(SaxsDataCollectionComparator... multipleOptions){
		System.out.println(new Gson().toJson(multipleOptions));
	}
	
	
}