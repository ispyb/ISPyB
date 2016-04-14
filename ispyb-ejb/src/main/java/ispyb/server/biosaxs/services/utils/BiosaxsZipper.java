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

package ispyb.server.biosaxs.services.utils;

import ispyb.common.util.HashMapToZip;
import ispyb.server.biosaxs.services.core.analysis.Analysis3Service;
import ispyb.server.biosaxs.services.core.analysis.abInitioModelling.AbInitioModelling3Service;
import ispyb.server.biosaxs.services.core.analysis.primaryDataProcessing.PrimaryDataProcessing3Service;
import ispyb.server.biosaxs.vos.datacollection.Frametolist3VO;
import ispyb.server.biosaxs.vos.datacollection.Merge3VO;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;



/**
 * Gets a zip file with all the files filtered by either experimentId or macromolecule
 * 
 */
public class BiosaxsZipper {

	private final ArrayList<String> alphabet;

	private final HashMap<String, Integer> concentrationLetterIds;

	private final Analysis3Service analysis3Service;

	private final AbInitioModelling3Service abInitioModelling3Service;

	private final PrimaryDataProcessing3Service primaryDataProcessing3Service;

	private final static Logger LOG = Logger.getLogger("BiosaxsZipper");

	public BiosaxsZipper(Analysis3Service analysis3Service, AbInitioModelling3Service abInitioModelling3Service, PrimaryDataProcessing3Service primaryDataProcessing3Service) {
		this.analysis3Service = analysis3Service;
		this.abInitioModelling3Service = abInitioModelling3Service;
		this.primaryDataProcessing3Service = primaryDataProcessing3Service;

		this.alphabet = new ArrayList<String>();
		this.concentrationLetterIds = new HashMap<String, Integer>();

		for (char alphabet = 'a'; alphabet <= 'z'; alphabet++) {
			this.alphabet.add(String.valueOf(alphabet));
		}
		for (char alphabet = 'A'; alphabet <= 'Z'; alphabet++) {
			this.alphabet.add(String.valueOf(alphabet));
		}
		
		for (int i = 1; i < 10; i++) {
			for (char alphabet = 'a'; alphabet <= 'z'; alphabet++) {
				this.alphabet.add(String.valueOf(alphabet)+ i);
			}
		}
	}

	public byte[] getFilesByMacromolecule(int macromoleculeId, int proposalId) {
		HashMap<String, String> files = new HashMap<String, String>();
		List<Map<String, Object>> data = this.analysis3Service.getAllByMacromolecule(macromoleculeId, proposalId);
		for (Map<String, Object> map : data) {
			if (map.get("bufferBeforeMergeId") != null) {
				files.putAll(this.getFilesByDataCollectionRecord(map));
			}
		}
		return HashMapToZip.doZip(files);
	}

	/**
	 * The idea of working with getAnalysisInformationByExperimentId which returns a List<Map> is because is much faster than ejb3 objects.
	 * @param experimentId
	 * @return
	 */
	public byte[] getFilesByExperimentId(Integer experimentId) {
		HashMap<String, String> files = new HashMap<String, String>();
		List<Map<String, Object>> data = this.abInitioModelling3Service.getAnalysisInformationByExperimentId(experimentId);
		/** Data is a data structure as:
		 * [{
		 * "total":"0.700573329996",
		 * "averagedModelId":303403,
		 * "bufferBeforeMeasurementId":53986,
		 * "sampleMergeId":30247,
		 * "I0":"0.0",
		 * "proposalCode":"MX",
		 * "averagedModel":"/data/pyarch/bm29/mx1525/3165/53987/damaver.pdb",
		 * "bufferAfterMergeId":30248,
		 * "framesCount":"10",
		 * "bufferBeforeMergeId":30246,
		 * "averageFilePath":" /data/pyarch/bm29/mx1525/3165/1d/Feldman_285_ave.dat",
		 * "bufferAfterMeasurementId":53988,
		 * "rgGuinier":"21.7018539871",
		 * "subtractedFilePath":" /data/pyarch/bm29/mx1525/3165/1d/Feldman_285_sub.dat",
		 * "firstPointUsed":"0",
		 * "chi2RgFilePath":"/data/pyarch/bm29/mx1525/3165/53987/chi2_R.png",
		 * "abInitioModelId":6794,"macromoleculeId":10806,
		 * "code":"RhodopsinBlack_1.0","transmission":"30.0",
		 * "timeStart":"2014-04-23 23:07:15.214215","sessionId":36498,
		 * "bufferAcronym":"BufferR","quality":"0.0",
		 * "shapeDeterminationModelId":303405,
		 * "expermientComments":"[BsxCube] Generated from BsxCube",
		 * "bufferId":1787,"isagregated":"False","dmax":"110.136908985",
		 * "bufferBeforeAverageFilePath":" /data/pyarch/bm29/mx1525/3165/1d/Feldman_284_ave.dat",
		 * "exposureTemperature":"20.0",
		 * "subtractionId":12629,
		 * "rapidShapeDeterminationModel":"/data/pyarch/bm29/mx1525/3165/53987/damfilt.pdb","conc":"1.0",
		 * "experimentCreationDate":"Apr 23, 2014 11:05:20 PM",
		 * "lastPointUsed":"0","modelListId":8199,"framesMerge":"2",
		 * "rapidShapeDeterminationModelId":303404,
		 * "bufferAfterAverageFilePath":" /data/pyarch/bm29/mx1525/3165/1d/Feldman_286_ave.dat",
		 * "nsdFilePath":"/data/pyarch/bm29/mx1525/3165/53987/nsd.png",
		 * "bufferAfterFramesMerged":"10","experimentId":3165,
		 * "experimentType":"STATIC",
		 * "macromoleculeAcronym":"RhodopsinBlack",
		 * "i0stdev":"0.0","sampleMeasurementId":53987,
		 * "measurementComments":"[1]",
		 * "priorityLevelId":2,
		 * "bufferBeforeFramesMerged":"10","substractionCreationTime":"Apr 23, 2014 11:09:34 PM",
		 * "proposalNumber":"1525",
		 * "rgGnom":"33.8280332203",
		 * "volume":"30522.6",
		 * "shapeDeterminationModel":"/data/pyarch/bm29/mx1525/3165/53987/dammin.pdb",
		 * "comments":null}]
		 */
		for (Map<String, Object> map : data) {
			files.putAll(this.getFilesByDataCollectionRecord(map));
		}
		return HashMapToZip.doZip(files);
	}

	
	private HashMap<String, String> getFilesByDataCollectionRecord(Map<String, Object> map) {
		HashMap<String, String> files = new HashMap<String, String>();
		String macromolecule = map.get("macromoleculeAcronym").toString().trim();
		String buffer = map.get("bufferAcronym").toString().trim();

		String conc = this.getConcentration(String.valueOf(Float.parseFloat(map.get("conc").toString().trim())));

		String folder = macromolecule + "_" + buffer;// + "\\" + macromolecule + "_" + buffer + "_" + conc + "mgml_" + exposureTemperature + "C_" + subtractionId;
		/** Frames **/
		files.putAll(this.getFramesByDataCollectionRecord(map, folder + "\\frames\\" + conc + " mgml", conc));
		/** Average **/
		files.putAll(this.getAverageFrameByDataCollectionRecord(map, folder + "\\average", conc));
		/** subtraction **/
		files.putAll(this.getSubtractionByDataCollectionRecord(map, folder + "\\subtraction", conc));
		/** models **/
		files.putAll(this.getModels(map, folder + "\\models"));
		return files;
	}

	/**
	 * @param valueOf
	 * @return
	 */
	private String getConcentration(String concentration) {
		if (this.concentrationLetterIds.get(concentration) == null) {
			this.concentrationLetterIds.put(concentration, -1);
		}
		this.concentrationLetterIds.put(concentration, this.concentrationLetterIds.get(concentration) + 1);

		if (this.concentrationLetterIds.get(concentration) == 0) {
			return concentration;
		}
		return concentration + "_" + this.alphabet.get(this.concentrationLetterIds.get(concentration) - 1);
	}

	private HashMap<String, String> getFramesByMergeId(List<Merge3VO> merge3VOs, String folderName, String concentration) {
		HashMap<String, String> files = new HashMap<String, String>();
		for (Merge3VO merge3VO : merge3VOs) {
			Iterator<Frametolist3VO> iterator = merge3VO.getFramelist3VO().getFrametolist3VOs().iterator();
			while (iterator.hasNext()) {
				Frametolist3VO frameToList3VO = iterator.next();
				if (frameToList3VO.getFrame3VO().getFilePath() != null) {
					if (!frameToList3VO.getFrame3VO().getFilePath().endsWith("_ave.dat")
							&& !frameToList3VO.getFrame3VO().getFilePath().endsWith("_sub.dat")
							&& !frameToList3VO.getFrame3VO().getFilePath().endsWith("_sub.out")
							&& !frameToList3VO.getFrame3VO().getFilePath().endsWith("_ave_averbuffer.dat")) {
							files.put(folderName + "\\" + concentration + "_mgml_" + new File(frameToList3VO.getFrame3VO().getFilePath()).getName(), frameToList3VO.getFrame3VO().getFilePath());
					}
				}
			}
		}
		return files;
	}
	
	private HashMap<String, String> getFramesByMergeId(int mergeId, String folderName, String concentration) {
		ArrayList<Integer> frameListIds = new ArrayList<Integer>();
		frameListIds.add(mergeId);
		return getFramesByMergeId(this.primaryDataProcessing3Service.getMergesByIdsList(frameListIds), folderName, concentration);

	}

	private HashMap<String, String> getAverageFrameByFile(String filePath, String folderName, String concentration) {
		HashMap<String, String> files = new HashMap<String, String>();
		if (filePath != null) {
				files.put(folderName + "\\" + concentration + "_mgml_" + new File(filePath.toString().trim()).getName(), filePath.toString().trim());
		}
		return files;
	}

	private HashMap<String, String> getAverageFrameByDataCollectionRecord(Map<String, Object> map, String folderName,
			String concentration) {
		HashMap<String, String> files = new HashMap<String, String>();
		if (map.get("bufferBeforeAverageFilePath") != null) {
			files.putAll(this.getAverageFrameByFile(map.get("bufferBeforeAverageFilePath").toString(), folderName,
					concentration));
		}
		if (map.get("averageFilePath") != null) {
			files.putAll(this.getAverageFrameByFile(map.get("averageFilePath").toString(), folderName, concentration));
		}
		if (map.get("bufferAfterAverageFilePath") != null) {
			files.putAll(this.getAverageFrameByFile(map.get("bufferAfterAverageFilePath").toString(), folderName,
					concentration));
		}
		return files;
	}

	private HashMap<String, String> getFramesByDataCollectionRecord(Map<String, Object> map, String folderName,
			String concentration) {
		HashMap<String, String> files = new HashMap<String, String>();

		if (map.get("bufferBeforeMergeId") != null) {
			int mergeId = Integer.parseInt(map.get("bufferBeforeMergeId").toString());
			files.putAll(this.getFramesByMergeId(mergeId, folderName, concentration));
		}
		if (map.get("sampleMergeId") != null) {
			int mergeId = Integer.parseInt(map.get("sampleMergeId").toString());
			files.putAll(this.getFramesByMergeId(mergeId, folderName, concentration));
		}
		if (map.get("bufferAfterMergeId") != null) {
			int mergeId = Integer.parseInt(map.get("bufferAfterMergeId").toString());
			files.putAll(this.getFramesByMergeId(mergeId, folderName, concentration));
		}
		return files;
	}

	/**
	 * Get the filepath where the subtraction is and it adds to the HashMap
	 * @param map
	 * @param folderName
	 * @return
	 */
	private HashMap<String, String> getSubtractionByDataCollectionRecord(Map<String, Object> map, String folderName,
			String concentration) {
		HashMap<String, String> files = new HashMap<String, String>();
		String key = "subtractedFilePath";
		/** Subtraction **/
		if (map.get(key) != null) {
			if (!map.get(key).toString().isEmpty()){
				files.put(folderName + "\\" + concentration + "_mgml_" + new File(map.get(key).toString().trim()).getName(), map.get(key).toString().trim());
			}
		}
		return files;
	}

	/**
	 * Get the filepath where the models are and it adds to the HashMap
	 * @param map
	 * @param folderName
	 * @return
	 */
	private HashMap<String, String> getModels(Map<String, Object> map, String folderName) {
		HashMap<String, String> files = new HashMap<String, String>();
		List<String> keys = new ArrayList<String>();
		keys.add("averagedModel");
		keys.add("rapidShapeDeterminationModel");
		keys.add("shapeDeterminationModel");
		/** Models **/
		for (String key : keys) {
			if (map.get(key) != null) {
				files.put(folderName + "\\" + new File(map.get(key).toString().trim()).getName(), map.get(key).toString().trim());
			}
		}
		return files;
	}

	

}
