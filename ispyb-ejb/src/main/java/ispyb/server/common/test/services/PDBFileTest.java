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


//import ispyb.client.bx.dataAdapter.BiosaxsActions;
import ispyb.server.biosaxs.services.core.analysis.Analysis3Service;
import ispyb.server.biosaxs.services.core.analysis.abInitioModelling.AbInitioModelling3Service;
import ispyb.server.biosaxs.vos.datacollection.Model3VO;
import ispyb.server.common.test.SaxsEJB3Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

public class PDBFileTest extends SaxsEJB3Test {

	

	/** The now. */
	protected static Calendar NOW;
	private Analysis3Service analysis3Service;
	private AbInitioModelling3Service abInitioModelling3Service;
	
	@Before
	public void setUp() throws Exception {
		analysis3Service = (Analysis3Service) serviceLocator.getRemoteService(Analysis3Service.class, properties);
//		abInitioModelling3Service = (AbInitioModelling3Service) serviceLocator.getRemoteService(AbInitioModelling3Service.class);
	}
	@Test
	public void te3st() {
		BufferedWriter writer = null;
		try
		{
		    writer = new BufferedWriter( new FileWriter( "c:\\dataming.json"));
		    writer.write("var data = " + new Gson().toJson(analysis3Service.getAllAnalysisInformation()));

		}
		catch ( IOException e)
		{
		}
		finally
		{
		    try
		    {
		        if ( writer != null)
		        writer.close( );
		    }
		    catch ( IOException e)
		    {
		    }
		}
		
		
	}
	public void parseFile() throws Exception {
		String filePath = "C:\\Users\\demariaa\\Downloads\\damminTest.pdb";
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line;
		int count = 0;
		StringBuilder content = new StringBuilder();
		while ((line = br.readLine()) != null) {
		   if (line.startsWith("ATOM")){
			   List<String> list = Arrays.asList(line.split("\\s+"));
			   String x = list.get(5);
			   String y = list.get(6);
			   String z = list.get(7);
			   content.append("H" + "\t" + x + "\t" + y + "\t" + z + "\n");
			   count ++;
		   }
		}
		br.close();
		System.out.println(count + "\n" + "ISPyB Generated File\n" + content.toString());
	}
	
	@Test
	public void test() throws Exception{
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(5036);
		ids.add(5035);
		ids.add(5034);
		
		List<String> colors = new ArrayList<String>();
		colors.add("blue");
		colors.add("red");
		colors.add("green");
		System.out.println(this.getPDBContentByModelList(ids, colors));	
	}
	
	
	public String getPDBContentByModelList(List<Integer> modelListId,List<String> colors) throws Exception {
		int count = 0;
		StringBuilder content = new StringBuilder();
		
		HashSet<String> keys = new HashSet<String>();
		for (int i = 0; i < modelListId.size(); i++) {
			String color = colors.get(i);
			Model3VO model = this.abInitioModelling3Service.getModelById(modelListId.get(i));
			if (model != null){
				String filePath = model.getPdbFile();
				if (new File(filePath).exists()){
					String line;
					BufferedReader br = new BufferedReader(new FileReader(filePath));
					while ((line = br.readLine()) != null) {
					   if (line.startsWith("ATOM")){
						   List<String> list = Arrays.asList(line.split("\\s+"));
						   String x = list.get(5);
						   String y = list.get(6);
						   String z = list.get(7);
						   
						   DecimalFormat df = new DecimalFormat("#.##");
						   String key = df.format(Float.parseFloat(x)) + "_" + df.format(Float.parseFloat(y)) + "_" + df.format(Float.parseFloat(z));
						   if (!keys.contains(key)){
							   keys.add(key);
							   if (color != null){
								   content.append("H" + "\t" + x + "\t" + y + "\t" + z + "\t" + color + "\n");
							   }
							   else{
								   content.append("H" + "\t" + x + "\t" + y + "\t" + z + "\n");
							   }
							   count ++;
						   }
						   
						 
					   }
					}
					br.close();
					System.out.println("keys" + keys.size());
				}
			}
		}
		return count + "\n" + "ISPyB Generated File\n" + content.toString();
		
	}
}