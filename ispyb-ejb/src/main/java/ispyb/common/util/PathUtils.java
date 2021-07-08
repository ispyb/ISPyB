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
 ******************************************************************************/
package ispyb.common.util;

import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.server.mx.vos.collections.DataCollection3VO;

import java.io.File;

/**
 * 
 * Paths creation used in several methods
 * 
 * 
 */
public class PathUtils {


	public static String getImageDirPath(DataCollection3VO dcValue) throws Exception {

		String imgPrefix = dcValue.getImagePrefix();

		// several cases ref-bla, bla, or postref-bla

		// reformat imgPrefix to erase "ref-" at the beginning
		while (imgPrefix.startsWith("ref-")) {
			imgPrefix = imgPrefix.substring(4);
		}

		// reformat imgPrefix to erase "postref-" at the beginning
		while (imgPrefix.startsWith("postref-")) {
			imgPrefix = imgPrefix.substring(8);
		}

		String imageDir = mapFilePath(dcValue.getImageDirectory());
		if (imageDir != null && imageDir.charAt(imageDir.length() - 1) != '/')
			imageDir += "/";
		imageDir += imgPrefix + "_" + dcValue.getDataCollectionNumber().toString() + "_";

		// reformat imageDir to ensure no space at the beginning
		while (imageDir.startsWith(" ")) {
			imageDir = imageDir.substring(1);
		}

		return imageDir;
	}

	public static String getFullDNAPath(DataCollection3VO dataCollectionVO) throws Exception {
		boolean isWindows = (System.getProperty("os.name").indexOf("Win") != -1) ? true : false;

		String imageDir = getImageDirPath(dataCollectionVO);
		if (Constants.SITE_IS_MAXIV()) {
			imageDir = imageDir.replace("/data/visitors/","/data/staff/ispybstorage/visitors/");
		}
		String fullDNAPath = imageDir + Constants.IMG_DNA_URL_SUFIX;
		if (isWindows) {
			fullDNAPath = fullDNAPath.replace(Constants.DATA_FILEPATH_START, Constants.DATA_FILEPATH_WINDOWS_MAPPING);
		}
		return fullDNAPath;
	}

	public static String getPath(String origPath) {
		boolean isWindows = (System.getProperty("os.name").indexOf("Win") != -1) ? true : false;

		if (isWindows) {
			return origPath.replace(Constants.DATA_FILEPATH_START, Constants.DATA_FILEPATH_WINDOWS_MAPPING);
		}

		/** In case of development on a linux machine **/
		if (origPath != null){
			/** Trimming **/
			origPath = origPath.trim();
			if (!origPath.isEmpty()){
				if (new File(origPath).exists() == false){
					/** In case of being upload folder **/ 
					String path = origPath.replace(Constants.DATA_FILEPATH_START.trim(), Constants.getDataFilePathForDevelopmentOnLinuxMapping().trim());
					path = origPath.replace(Constants.DATA_PDB_FILEPATH_START.trim(), Constants.getDataFilePathForDevelopmentOnLinuxMapping().trim());
					return path;
				}
			}
		}
		return origPath;
	}

	public static String getFullEDNAPath(DataCollection3VO dataCollectionVO) throws Exception {
		String fullEDNAPath = null;
		if (!Constants.SITE_IS_DLS()) {
			fullEDNAPath = getFullDNAPath(dataCollectionVO) + Constants.EDNA_FILES_SUFIX;
		} else {
			String imgPrefix = dataCollectionVO.getImagePrefix();
			Integer dataCollectionNumber = dataCollectionVO.getDataCollectionNumber();
			String imgDir = dataCollectionVO.getImageDirectory();
			int i = imgDir.indexOf('/', 19); // get the index of '/' after /dls/i0x/data/yyyy/proposal-visitNumber
			String processedDir = imgDir.substring(0, i + 1) + "processed/";
			if (i + 1 < imgDir.length())
				processedDir += imgDir.substring(i + 1);

			fullEDNAPath = processedDir + imgPrefix + "_" + dataCollectionNumber.toString() + "_/edna/";
		}

		return fullEDNAPath;
	}

	public static String GetFullLogPath(DataCollection3VO dataCollectionVO) throws Exception {
		boolean isWindows = (System.getProperty("os.name").indexOf("Win") != -1) ? true : false;

		String imageDir = getImageDirPath(dataCollectionVO);
		String fullLogPath = imageDir + Constants.DNA_LOG_FILES_SUFIX;
		if (isWindows) {
			fullLogPath = fullLogPath.replace(Constants.DATA_FILEPATH_START, Constants.DATA_FILEPATH_WINDOWS_MAPPING);
		}

		return fullLogPath;
	}

	public static String GetFullDNARankingPath(Integer dataCollectionId) throws Exception {
		boolean isWindows = (System.getProperty("os.name").indexOf("Win") != -1) ? true : false;

		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		DataCollection3Service dataCollectionService = (DataCollection3Service) ejb3ServiceLocator
				.getLocalService(DataCollection3Service.class);
		DataCollection3VO dcValue = dataCollectionService.findByPk(dataCollectionId, false, false);

		String imageDir = mapFilePath(dcValue.getImageDirectory());
		if (imageDir != null && imageDir.charAt(imageDir.length() - 1) != '/')
			imageDir += "/";
		// reformat imageDir to ensure no space at the beginning
		while (imageDir.startsWith(" ")) {
			imageDir = imageDir.substring(1);
		}

		String fullDNARankingPath = imageDir;
		if (isWindows) {
			fullDNARankingPath = fullDNARankingPath.replace(Constants.DATA_FILEPATH_START,
					Constants.DATA_FILEPATH_WINDOWS_MAPPING);
		}

		return fullDNARankingPath;
	}

	public static String GetFullDataProcessingPath(DataCollection3VO dataCollectionVO) throws Exception {
		boolean isWindows = (System.getProperty("os.name").indexOf("Win") != -1) ? true : false;

		String imageDir = getImageDirPath(dataCollectionVO);
		String fullDataProcessingPath = imageDir + Constants.DNA_FILES_DATA_PROC_SUFIX;
		if (isWindows) {
			fullDataProcessingPath = fullDataProcessingPath.replace(Constants.DATA_FILEPATH_START,
					Constants.DATA_FILEPATH_WINDOWS_MAPPING);
		}

		return fullDataProcessingPath;
	}

	public static String GetFullIntegrationPath(DataCollection3VO dataCollectionVO) throws Exception {
		boolean isWindows = (System.getProperty("os.name").indexOf("Win") != -1) ? true : false;

		String imageDir = getImageDirPath(dataCollectionVO);
		String fullIntegrationPath = imageDir + Constants.DNA_FILES_INTEGRATION_SUFIX;
		if (isWindows) {
			fullIntegrationPath = fullIntegrationPath.replace(Constants.DATA_FILEPATH_START,
					Constants.DATA_FILEPATH_WINDOWS_MAPPING);
		}

		return fullIntegrationPath;
	}

	public static String GetFullStrategyPath(DataCollection3VO dataCollectionVO) throws Exception {
		boolean isWindows = (System.getProperty("os.name").indexOf("Win") != -1) ? true : false;

		String imageDir = getImageDirPath(dataCollectionVO);
		String GetFullStrategyPath = imageDir + Constants.DNA_FILES_STRATEGY_SUFIX;
		if (isWindows) {
			GetFullStrategyPath = GetFullStrategyPath.replace(Constants.DATA_FILEPATH_START,
					Constants.DATA_FILEPATH_WINDOWS_MAPPING);
		}

		return GetFullStrategyPath;
	}

	// TODO check if it is still used at ESRF (used only for DENZO) ?
	public static String GetArchiveEquivalentPath(String originalPath) {
		String archivePath = originalPath;
		archivePath = mapFilePath(archivePath);

		// Reformat imageDir to ensure no space at the beginning
		while (archivePath.startsWith(" ")) {
			archivePath = archivePath.substring(1);
		}

		return archivePath;
	}

	private static String mapFilePath(String filePathIn) {

		String filePathOut = filePathIn;
		if (Constants.PATH_MAPPING_STYLE.equals("BASE")) {
			if (filePathOut.endsWith("/"))
				filePathOut = filePathOut.substring(0, filePathOut.length() - 1);
		}
		// ESRF ####
		else if (Constants.PATH_MAPPING_STYLE.equals("ESRF")) {
			// on some beamlines data are stored on /data/gz
			filePathOut = filePathOut.replace("/gz/", "/");
			if (filePathOut.startsWith("/data/visitor")) {
				// Visitor: "/data/visitor/mx415/id14he2/..." mapped to "/data/pyarch/id14eh2/mx415/..."
				String[] filePathDir = filePathOut.split("/");
				if (filePathDir.length > 4) {
					String accountName = "/" + filePathDir[3];
					String beamlineName = "/" + filePathDir[4];
					// Swap account and beamline
					filePathOut = filePathOut.replaceAll(accountName, "_accountName_");
					filePathOut = filePathOut.replaceAll(beamlineName, "_beamlineName_");
					filePathOut = filePathOut.replaceAll("_accountName_", beamlineName);
					filePathOut = filePathOut.replaceAll("_beamlineName_", accountName);
				}
				
				filePathOut = filePathOut.replaceAll("/data", "/data/pyarch").replaceAll("/visitor", "");
			} else {
				// Inhouse and External: "/data/id14eh2/inhouse/mx415" mapped to "/data/pyarch/id14eh2/mx415/..."
				filePathOut = filePathOut.replaceAll("/data", "/data/pyarch").replaceAll("/external", "")
						.replaceAll("/inhouse", "");
			}
		}
		// MAXIV ####
		else if (Constants.PATH_MAPPING_STYLE.equals("MAXIV")) {
			if (filePathOut.startsWith("/data/data1/visitor")) {
				// Visitor: "/data/visitor/mx415/id14he2/..." mapped to "/data/pyarch/id14eh2/mx415/..."
//				String[] filePathDir = filePathOut.split("/");
//				if (filePathDir.length > 4) {
//					String accountName = "/" + filePathDir[3];
//					String beamlineName = "/" + filePathDir[4];
//					// Swap account and beamline
//					filePathOut = filePathOut.replaceAll(accountName, "_accountName_");
//					filePathOut = filePathOut.replaceAll(beamlineName, "_beamlineName_");
//					filePathOut = filePathOut.replaceAll("_accountName_", beamlineName);
//					filePathOut = filePathOut.replaceAll("_beamlineName_", accountName);
//				}
				filePathOut = filePathOut.replaceAll("/data/data1/", "/data/ispyb/").replaceAll("/visitor", "");;
			} else {
				// Inhouse and External: "/data/id14eh2/inhouse/mx415" mapped to "/data/pyarch/id14eh2/mx415/..."
				filePathOut = filePathOut.replaceAll("/data/data1/", "/data/ispyb/").replaceAll("/external", "").replaceAll("/inhouse", "");
			}
		}

		return filePathOut;
	}
	
	/**
	 * FitPathToOS
	 * 
	 * @param fullFilePath
	 * @return
	 */
	public static String FitPathToOS(String fullFilePath) {
		String newPath = fullFilePath;
		boolean isWindows = (System.getProperty("os.name").indexOf("Win") != -1) ? true : false;

		if (isWindows && newPath != null) {
			// newPath = fullFilePath.replace("/data/", "W://");
			newPath = newPath.replace(Constants.DATA_FILEPATH_START, Constants.DATA_FILEPATH_WINDOWS_MAPPING);
			newPath = newPath.replace(Constants.DATA_PDB_FILEPATH_START, Constants.DATA_PDB_FILEPATH_WINDOWS_MAPPING);
		}
		return newPath;
	}
	
	/**
	 * fileExists
	 * 
	 * @param fillFullPath
	 * @return
	 */
	public static int fileExists(String fillFullPath) {
		boolean fileExist = false;
		try {
			String targetPath = FitPathToOS(fillFullPath);
			File targetFile = new File(targetPath);
			fileExist = (targetFile.exists() && targetFile.isFile());
		} catch (Exception e) {
		}
		return (fileExist) ? 1 : 0;
	}

	public static int directoryExists(String fillFullPath) {
		boolean dirExist = false;
		try {
			String targetPath = FitPathToOS(fillFullPath);
			File targetFile = new File(targetPath);
			dirExist = (targetFile.exists() && targetFile.isDirectory());
		} catch (Exception e) {
		}
		return (dirExist) ? 1 : 0;
	}

}
