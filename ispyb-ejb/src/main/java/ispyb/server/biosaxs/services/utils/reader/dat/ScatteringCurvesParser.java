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

package ispyb.server.biosaxs.services.utils.reader.dat;

import ispyb.common.util.PathUtils;
import ispyb.server.biosaxs.vos.advanced.FitStructureToExperimentalData3VO;
import ispyb.server.biosaxs.vos.datacollection.Frame3VO;
import ispyb.server.biosaxs.vos.datacollection.Merge3VO;
import ispyb.server.biosaxs.vos.datacollection.Subtraction3VO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;

/**
 * This class reads a list of frame3VO and return their json representation
 *
 */
public class ScatteringCurvesParser {
	
	public static String curveParser(List<Frame3VO> frames) throws Exception{
		/** Getting data from files **/
		List<HashMap<String, List<List<String>>>> data = ScatteringCurvesParser.readFiles(frames);
		return new Gson().toJson(data);
	}
	
	private static List<HashMap<String, List<List<String>>>> readFiles(List<Frame3VO> frames) throws Exception{
		List< HashMap<String, List<List<String>>>> data = new ArrayList< HashMap<String, List<List<String>>>>();
		for (Frame3VO frame : frames) {
			data.add(ScatteringCurvesParser.readFile(frame.getFrameId(), frame.getFilePath()));
		}
		return data;
	}
	
	
	
	private static List<HashMap<String, List<List<String>>>> parseMerges(List<Merge3VO> merges) throws Exception{
		List< HashMap<String, List<List<String>>>> data = new ArrayList< HashMap<String, List<List<String>>>>();
		for (Merge3VO merge : merges) {
			data.add(ScatteringCurvesParser.readFile(merge.getMergeId(), merge.getAverageFilePath()));
		}
		return data;
	}
	
	private static Collection<? extends HashMap<String, List<List<String>>>> parseSubtractrions(List<Subtraction3VO> subtractions) throws Exception {
		List< HashMap<String, List<List<String>>>> data = new ArrayList< HashMap<String, List<List<String>>>>();
		for (Subtraction3VO subtraction : subtractions) {
			if (subtraction.getSubstractedFilePath() != null){
				File file = new File(PathUtils.getPath(subtraction.getSubstractedFilePath().trim())); 
				if (file.exists()){
					String key = subtraction.getSubtractionId() + "*SUBTRACTION*" + file.getName();
					data.add(ScatteringCurvesParser.readFile(key, subtraction.getSubstractedFilePath().trim()));
				}
			}
		}
		return data;
	}
	
	private static Collection<? extends HashMap<String, List<List<String>>>> parseSubtractrionSampleAveraged(Subtraction3VO subtraction) throws Exception {
		List< HashMap<String, List<List<String>>>> data = new ArrayList< HashMap<String, List<List<String>>>>();
		if (subtraction.getSampleAverageFilePath()!= null){
			File file = new File(PathUtils.getPath(subtraction.getSampleAverageFilePath().trim())); 
			if (file.exists()){
				String key = subtraction.getSubtractionId() + "*SAMPLE*AVERAGE*" + file.getName(); 
				data.add(ScatteringCurvesParser.readFile(key, subtraction.getSampleAverageFilePath().trim()));
			}
		}
		return data;
	}
	
	private static Collection<? extends HashMap<String, List<List<String>>>> parseSubtractrionBufferAveraged(Subtraction3VO subtraction) throws Exception {
		List< HashMap<String, List<List<String>>>> data = new ArrayList< HashMap<String, List<List<String>>>>();
		if (subtraction.getBufferAverageFilePath()!= null){
			File file = new File(PathUtils.getPath(subtraction.getBufferAverageFilePath().trim())); 
			if (file.exists()){
				String key = subtraction.getSubtractionId() + "*BUFFER*AVERAGE*" + file.getName(); 
				data.add(ScatteringCurvesParser.readFile(key, subtraction.getBufferAverageFilePath().trim()));
			}
		}
		return data;
	}

	
	private static HashMap<String, List<List<String>>> readFile(Integer id, String filePath) throws Exception{
		return readFile(String.valueOf(id), filePath);
	}
	
	private static HashMap<String, List<List<String>>> readFile(String id, String filePath) throws Exception{
		return readFile(id, filePath, 3);
	}
	
	private static HashMap<String, List<List<String>>> readFile(String id, String filePath, int expectedColumnsNumber) throws IOException {
		HashMap<String, List<List<String>>> dat = new HashMap<String, List<List<String>>>();
		List<List<String>> data = new ArrayList<List<String>>();
		if (!new File(filePath.trim()).exists()){
			filePath = PathUtils.getPath(filePath.trim());
		}
		if (new File(filePath.trim()).exists()){
			BufferedReader br = new BufferedReader(new FileReader(filePath.trim()));
			String line;
			while ((line = br.readLine()) != null) {
				/** Skipping blank lines **/
				if (line.toString().trim().length() != 0){
					if (!line.startsWith("#")){
						if ((!line.matches(".*[a-d]+.*") && (!line.matches(".*[A-D]+.*")))){
							if (!line.matches(".*[f-z]+.*") && (!line.matches(".*[F-Z]+.*"))){
								List<String> x = Arrays.asList(line.trim().split(" +"));
								/** If doesn't contains expectedColumnsNumber lines removed **/
//								if (x.size() == expectedColumnsNumber){
								boolean isValidColumn = true;
								for (String string : x) {
									if (!string.matches("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?")){
										isValidColumn = false;
									}
								}
								if (isValidColumn){
									data.add(x);
								}
//								}
							}
						}
					}
				}
			}
			br.close();
		}
		dat.put(id, data);
		return dat;
	}

	

	public static String curveParser(List<Frame3VO> frames, List<Merge3VO> merges) throws Exception {
		return new Gson().toJson(getData(frames, merges));
	}
	
	private static List<HashMap<String, List<List<String>>>> getData(List<Frame3VO> frames, List<Merge3VO> merges) throws Exception {
		List<HashMap<String, List<List<String>>>> data = ScatteringCurvesParser.readFiles(frames);
		data.addAll(ScatteringCurvesParser.parseMerges(merges));
		return data;
	}
	
	private static List<HashMap<String, List<List<String>>>> getData(List<Frame3VO> frames, List<Merge3VO> merges, List<Subtraction3VO> subtractions) throws Exception {
		List<HashMap<String, List<List<String>>>> data = ScatteringCurvesParser.getData(frames, merges);
		data.addAll(ScatteringCurvesParser.parseSubtractrions(subtractions));
		/** For the subtraction we need to retrieve not only the subtrated file but also:
		 * 1) Sample Average
		 * 2) Buffer Average
		 * 3) Sample 1d files
		 * 4) Buffer 1d files
		 */
		for (Subtraction3VO subtraction : subtractions) {
			data.addAll(ScatteringCurvesParser.parseSubtractrionSampleAveraged(subtraction));
			data.addAll(ScatteringCurvesParser.parseSubtractrionBufferAveraged(subtraction));
		}
		
		return data;
	}
	
	private static List<HashMap<String, List<List<String>>>> getData(List<Frame3VO> frames, List<Merge3VO> merges, List<Subtraction3VO> subtractions, List<FitStructureToExperimentalData3VO> fits) throws Exception {
		List<HashMap<String, List<List<String>>>> data = ScatteringCurvesParser.getData(frames, merges, subtractions);
		data.addAll(ScatteringCurvesParser.parseFits(fits));
		return data;
	}

	private static Collection<? extends HashMap<String, List<List<String>>>> parseFits(List<FitStructureToExperimentalData3VO> fits) throws Exception {
		List< HashMap<String, List<List<String>>>> data = new ArrayList< HashMap<String, List<List<String>>>>();
		for (FitStructureToExperimentalData3VO fit : fits) {
			if (fit.getFitFilePath() != null){
				File file = new File(PathUtils.getPath(fit.getFitFilePath().trim())); 
				if (file.exists()){
					String key = fit.getFitStructureToExperimentalDataId() + "_FIT";
					data.add(ScatteringCurvesParser.readFile(key, fit.getFitFilePath().trim()));
				}
			}
		}
		return data;
	}

	public static String curveParser(List<Frame3VO> frames, List<Merge3VO> merges, List<Subtraction3VO> subtractions) throws Exception {
		List<HashMap<String, List<List<String>>>> data = getData(frames, merges, subtractions);
		return new Gson().toJson(data);
	}

	public static String curveParserByFilePath(List<String> filePathList) throws Exception{
		List< HashMap<String, List<List<String>>>> data = new ArrayList< HashMap<String, List<List<String>>>>();
		for (String filePath : filePathList) {
			data.add(ScatteringCurvesParser.readFile(filePath, filePath));
		}
		return new Gson().toJson(data);
	}

	public static String curveParserByFilePath(List<String> filePathList, int expectedColumnsNumber) throws Exception{
		List< HashMap<String, List<List<String>>>> data = new ArrayList< HashMap<String, List<List<String>>>>();
		for (String filePath : filePathList) {
			data.add(ScatteringCurvesParser.readFile(filePath, filePath, expectedColumnsNumber));
		}
		return new Gson().toJson(data);
	}

	public static String curveParser(List<Frame3VO> frames, List<Merge3VO> merges, List<Subtraction3VO> subtractions, List<FitStructureToExperimentalData3VO> fits) throws Exception {
		List<HashMap<String, List<List<String>>>> data = ScatteringCurvesParser.getData(frames, merges, subtractions, fits);
		return new Gson().toJson(data);
	}



}
