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
/*
 * FileUtil.java
 * 
 * Created on 6 avr. 2005
 *
 */
package ispyb.client.common.util;

import ispyb.client.mx.results.ImageValueInfo;
import ispyb.client.mx.results.SnapshotInfo;
import ispyb.common.util.Constants;
import ispyb.common.util.PathUtils;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.collections.Image3Service;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.Image3VO;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 * Class with utils to manage the image files
 * 
 */
public class FileUtil {

	/**
     *  
     */
	public FileUtil() {

	}

	private static final int SNAPSHOT_EXPECTED_NUMBER = 4;

	private final static Logger LOG = Logger.getLogger(FileUtil.class);

	/**
	 * Gunzip a local file
	 * 
	 * @param sourceFileName
	 * @return
	 */

	public static byte[] readBytes(String sourceFileName) {

		ByteArrayOutputStream outBuffer = null;
		FileInputStream inFile = null;
		BufferedInputStream bufInputStream = null;

		try {
			outBuffer = new ByteArrayOutputStream();
			inFile = new FileInputStream(sourceFileName);
			bufInputStream = new BufferedInputStream(inFile);

			byte[] tmpBuffer = new byte[8 * 1024];
			int n = 0;
			while ((n = bufInputStream.read(tmpBuffer)) >= 0)
				outBuffer.write(tmpBuffer, 0, n);
		} catch (FileNotFoundException fnf) {
			LOG.error("[readBytes] File not found :" + fnf.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inFile != null) {
				try {
					inFile.close();
				} catch (IOException ioex) {
					// ignore
				}
			}
			if (outBuffer != null) {
				try {
					outBuffer.close();
				} catch (IOException ioex) {
					// ignore
				}
			}
			if (bufInputStream != null) {
				try {
					bufInputStream.close();
				} catch (IOException ioex) {
					// ignore
				}
			}
		}
		return outBuffer.toByteArray();
	}

	public static String fileToString(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        return sb.toString();
	    } finally {
	        br.close();
	    }
	}
		
	public static String fileToString(String sourceFileName) {
		
		String output = "nofile";
		try {
			if (sourceFileName != null) {
				File file = new File(sourceFileName);
				if (file.exists()) {
					output =fileToString(file);
				} 
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}
	
	//TODO remove the following method if previous OK
	public static String fileToStringOld(String sourceFileName) {
		BufferedReader inFile = null;
		String output = new String();// = null;

		try {

			// 1. Reading input by lines:
			inFile = new BufferedReader(new FileReader(sourceFileName));
			String s = new String();
			while ((s = inFile.readLine()) != null)
				output += s + "\n";
			inFile.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inFile != null) {
				try {
					inFile.close();
				} catch (IOException ioex) {
					// ignore
					output = "nofile";
					return output;
				}
			}

		}
		return output;
	}

	/**
	 * downloadFile
	 * 
	 * @param fullFilePath
	 * @param mimeType
	 * @param response
	 */
	public static void DownloadFile(String fullFilePath, String mimeType, String attachmentFilename,
			HttpServletResponse response) {
		try {
			byte[] imageBytes = FileUtil.readBytes(fullFilePath);
			response.setContentLength(imageBytes.length);
			ServletOutputStream out = response.getOutputStream();
			response.setHeader("Pragma", "public");
			response.setHeader("Cache-Control", "max-age=0");
			response.setContentType(mimeType);
			response.setHeader("Content-Disposition", "attachment; filename=" + attachmentFilename);

			out.write(imageBytes);
			out.flush();
			out.close();

		} catch (FileNotFoundException fnf) {
			LOG.debug("[DownloadFile] File not found: " + fullFilePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean isJpgForImage(Image3VO imageVO, HttpServletRequest request) throws Exception {

		boolean isJpg = false;

		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
		if (proposalId == null) {
			try {
				proposalId = new Integer(request.getParameter(Constants.PROPOSAL_ID));
			} catch (NumberFormatException e) {
				if (Confidentiality.isManager(request) || Confidentiality.isLocalContact(request)){
					// isLocalContact or isManager = true;
					String sourceFileName = imageVO.getJpegFileFullPath();
					sourceFileName = PathUtils.FitPathToOS(sourceFileName);

					isJpg = FileUtil.fileExists(sourceFileName);
					return isJpg;
				}
				return false;
			}
		}

		// Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		// Image3Service imageService = (Image3Service) ejb3ServiceLocator.getLocalService(Image3Service.class);
		// two variables to guarantee the user fecths only its own images
		// ArrayList imageFetchedList = (ArrayList) imgFacade.findByImageIdAndProposalId(imageId, proposalId);
		// List<Image3VO> imageFetchedList = imageService.findByImageIdAndProposalId(imageId, proposalId);

		// Image3VO imgValue = imageService.findByPk(imageId);
		Integer proposalIdFromImage = imageVO.getDataCollectionVO().getDataCollectionGroupVO().getSessionVO()
				.getProposalVOId();

		if (proposalIdFromImage.equals(proposalId)) {

			String sourceFileName = imageVO.getJpegFileFullPath();
			sourceFileName = PathUtils.FitPathToOS(sourceFileName);

			isJpg = FileUtil.fileExists(sourceFileName);
		}

		return isJpg;

	}

	/**
	 * returns false if file does not exist
	 * 
	 * @param sourceFileName
	 * @return
	 */

	public static boolean fileExists(String sourceFileName) {

		if (sourceFileName == null)
			return false;

		FileInputStream inFile = null;
		boolean fileExists = true;

		try {
			inFile = new FileInputStream(sourceFileName);
		} catch (FileNotFoundException fnf) {
			fileExists = false;
		}
		return fileExists;
	}

	/**
	 * GetImageList
	 * 
	 * @param dataCollectionId
	 * @return
	 * @throws Exception
	 */
	public static List<ImageValueInfo> GetImageList(Integer dataCollectionId, Integer extract_ImageId,
			Integer extract_ImageNumber, Integer nbImagesHorizontal, Integer nbImagesVertical,
			HttpServletRequest request) throws Exception {

		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		Image3Service imageService = (Image3Service) ejb3ServiceLocator.getLocalService(Image3Service.class);
		List<Image3VO> tmp_imageList = imageService.findByDataCollectionId(dataCollectionId);
		return getImageList(tmp_imageList, extract_ImageId, extract_ImageNumber, nbImagesHorizontal, nbImagesVertical, request);
	}

	private static List<ImageValueInfo> getImageList(List<Image3VO> tmp_imageList, Integer extract_ImageId,
			Integer extract_ImageNumber, Integer nbImagesHorizontal, Integer nbImagesVertical,
			HttpServletRequest request) throws Exception {
		Collections.reverse(tmp_imageList);
		List<ImageValueInfo> imageList = new ArrayList<ImageValueInfo>(tmp_imageList.size());

		int imgNumberHorizontal = 0;
		int imgNumberVertical = 0;

		int nb = tmp_imageList.size();

		for (int i = 0; i < nb; i++) {

			Image3VO imgValue = tmp_imageList.get(i);
			// TODO check if fileExists
			// we add the image only if jpeg are present on disk
			if (isJpgForImage(imgValue, request)) {

				imgNumberHorizontal++;

				ImageValueInfo imageValueInfo = new ImageValueInfo(imgValue);
				// --- Populate ImageValueInf0
				Integer currId = imgValue.getImageId();
				Integer prevId = (i > 0) ? (tmp_imageList.get(i - 1)).getImageId() : currId;
				Integer nextId = (i < (tmp_imageList.size() - 1)) ? (tmp_imageList.get(i + 1)).getImageId() : currId;
				Integer imageNumber = new Integer(i + 1);
				boolean first = (i == 0) ? true : false;
				boolean last = (i == tmp_imageList.size() - 1) ? true : false;

				imageValueInfo.setCurrentImageId(currId);
				imageValueInfo.setPreviousImageId(prevId);
				imageValueInfo.setNextImageId(nextId);
				imageValueInfo.setImageNumberInfo(imageNumber);
				imageValueInfo.setFirst(first);
				imageValueInfo.setLast(last);
				imageValueInfo.setSynchrotronCurrent(imgValue.getSynchrotronCurrent());
				imageValueInfo.setFormattedData();
				imageValueInfo.setLastImageHorizontal(false);
				imageValueInfo.setLastImageVertical(false);

				// --- Tag last Horizontal and Vertical Image ---
				if (nbImagesHorizontal != null && imgNumberHorizontal == nbImagesHorizontal && nbImagesHorizontal != 0) {
					imageValueInfo.setLastImageHorizontal(true);
					imgNumberHorizontal = 0;
					imgNumberVertical++;
				}

				if (nbImagesVertical != null && imgNumberVertical == nbImagesVertical && nbImagesVertical != 0) {
					imageValueInfo.setLastImageVertical(true);
					imgNumberVertical = 0;
				}

				imageList.add(imageValueInfo);
			}

		}
		// update the nextId value
		int nbImages = imageList.size();
		for (int i = 0; i < nbImages; i++) {
			Integer id = new Integer(i + 1);
			ImageValueInfo v = imageList.get(i);
			if (i < nbImages - 1) {
				v.setNextImageId(imageList.get(id).getCurrentImageId());
			}
			v.setImageNumberInfo(id);

		}
		// --- Extract specific Image by adding it at position 0
		if (extract_ImageId != null) {
			ImageValueInfo v = getImageValueInfo(extract_ImageId, imageList);
			if (v != null)
				imageList.add(0, v);
		} else {
			if (extract_ImageNumber != null) {
				ImageValueInfo v = getImageValueInfo(extract_ImageNumber, imageList);
				if (v != null)
					imageList.add(0, v);
			}
		}
		return imageList;
	}
	/**
	 * GetImageList
	 * 
	 * @param dataCollectionGroupId
	 * @return
	 * @throws Exception
	 */
	public static List<ImageValueInfo> getImageListForDataCollectionGroup(Integer dataCollectionGroupId, Integer extract_ImageId,
			Integer extract_ImageNumber, Integer nbImagesHorizontal, Integer nbImagesVertical,
			HttpServletRequest request) throws Exception {

		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		Image3Service imageService = (Image3Service) ejb3ServiceLocator.getLocalService(Image3Service.class);
		List<Image3VO> tmp_imageList = imageService.findByDataCollectionGroupId(dataCollectionGroupId);
		return getImageList(tmp_imageList, extract_ImageId, extract_ImageNumber, nbImagesHorizontal, nbImagesVertical, request);
	}
	private static ImageValueInfo getImageValueInfo(int id, List<ImageValueInfo> imageList) {
		int nbImages = imageList.size();
		for (int i = 0; i < nbImages; i++) {
			if (imageList.get(i).getCurrentImageId().equals(id)) {
				return imageList.get(i);
			}
		}
		return null;
	}

	public static List<String> getImageJpgThumbList(Integer dataCollectionId) throws Exception {
		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		Image3Service imageService = (Image3Service) ejb3ServiceLocator.getLocalService(Image3Service.class);
		List<Image3VO> tmp_imageList = imageService.findByDataCollectionId(dataCollectionId);
		// List tmp_imageList = (List) image.findByDataCollectionId(dataCollectionId);

		List<String> imageList = new ArrayList<String>(tmp_imageList.size());

		for (int i = 0; i < tmp_imageList.size(); i++) {
			Image3VO imgValue = tmp_imageList.get(i);
			String jpgThumbFullPath = imgValue.getJpegThumbnailFileFullPath();
			imageList.add(jpgThumbFullPath);
		}
		return imageList;
	}

	public static List<String> getImageJpgThumbList(Integer dataCollectionId, HttpServletRequest request)
			throws Exception {

		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		Image3Service imageService = (Image3Service) ejb3ServiceLocator.getLocalService(Image3Service.class);
		List<Image3VO> tmp_imageList = imageService.findByDataCollectionId(dataCollectionId);

		List<String> imageList = new ArrayList<String>();

		for (int i = 0; i < tmp_imageList.size(); i++) {
			Image3VO imgValue = tmp_imageList.get(i);
			// add the link only if jpeg exists
			if (FileUtil.isJpgThumbForImage(imgValue.getImageId(), request)) {
				String jpgThumbFullPath = imgValue.getJpegThumbnailFileFullPath();
				imageList.add(jpgThumbFullPath);
			}
		}
		return imageList;
	}

	public static boolean isJpgThumbForImage(Integer imageId, HttpServletRequest request) throws Exception {

		boolean isJpg = false;

		Integer proposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
		if (proposalId == null) {
			proposalId = new Integer(request.getParameter(Constants.PROPOSAL_ID));
		}

		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		Image3Service imageService = (Image3Service) ejb3ServiceLocator.getLocalService(Image3Service.class);
		// two variables to guarantee the user fecths only its own images
		List<Image3VO> imageFetchedList = imageService.findByImageIdAndProposalId(imageId, proposalId);

		if (imageFetchedList.size() == 1) {

			Image3VO imgValue = imageFetchedList.get(0);

			String sourceFileName = imgValue.getJpegThumbnailFileFullPath();
			sourceFileName = PathUtils.FitPathToOS(sourceFileName);

			isJpg = FileUtil.fileExists(sourceFileName);
		}

		return isJpg;

	}

	public static ArrayList<SnapshotInfo> GetFullSnapshotPath(DataCollection3VO dcValue) throws Exception {
		ArrayList<SnapshotInfo> snapshotPath = new ArrayList<SnapshotInfo>();
		// boolean isWindows = (System.getProperty("os.name").indexOf("Win") != -1) ? true : false;

		String expectedSnapShotPath = "";
		String fullSnapshotPath = "";
		for (int s = 0; s < SNAPSHOT_EXPECTED_NUMBER; s++) {
			switch (s + 1) {
			case 1:
				fullSnapshotPath = dcValue.getXtalSnapshotFullPath1();
				// --- Expected Snapshot path ---
				expectedSnapShotPath = fullSnapshotPath;
				if (expectedSnapShotPath == null)
					expectedSnapShotPath = "";
				expectedSnapShotPath = PathUtils.FitPathToOS(expectedSnapShotPath);
				break;
			case 2:
				fullSnapshotPath = dcValue.getXtalSnapshotFullPath2();
				break;
			case 3:
				fullSnapshotPath = dcValue.getXtalSnapshotFullPath3();
				break;
			case 4:
				fullSnapshotPath = dcValue.getXtalSnapshotFullPath4();
				break;
			}
			if (fullSnapshotPath == null)
				fullSnapshotPath = "";
			fullSnapshotPath = PathUtils.FitPathToOS(fullSnapshotPath);

			File foundFile = new File(fullSnapshotPath);
			boolean fileExists = (foundFile.exists() && foundFile.isFile());
			SnapshotInfo snapshotInfo = new SnapshotInfo(fullSnapshotPath, fileExists);
			snapshotPath.add(snapshotInfo);
		}

		SnapshotInfo snapshotInfo = new SnapshotInfo(expectedSnapShotPath, false);
		snapshotPath.add(SNAPSHOT_EXPECTED_NUMBER, snapshotInfo);

		return snapshotPath;
	}

	/**
	 * FileCleaner
	 * 
	 * @author launer Delete a file after a certain delay
	 */
	public static class FileCleaner extends Thread {
		long waitBeforeRun = 5000;

		String fileToDelete = "";

		public FileCleaner(long _waitBeforeRun, String _fileToDelete) {
			this.waitBeforeRun = _waitBeforeRun;
			this.fileToDelete = _fileToDelete;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(this.waitBeforeRun);
				File targetFile = new File(fileToDelete);
				targetFile.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static String GetFullDenzoPath(DataCollection3VO dcValue) throws Exception {

		String imgPrefix = dcValue.getImagePrefix();

		// reformat imgPrefix to erase "ref-" at the beginning
		while (imgPrefix.startsWith("ref-")) {
			imgPrefix = imgPrefix.substring(4);
		}

		String imageDir = dcValue.getImageDirectory() + "/";
		imageDir = imageDir.trim(); // Reformat imageDir to ensure no space at the beginning

		String fullDenzoPath = imageDir + Constants.IMG_DENZO_URL_PREFIX;
		String archive_fullDenzoPath = PathUtils.GetArchiveEquivalentPath(fullDenzoPath);

		fullDenzoPath = PathUtils.FitPathToOS(fullDenzoPath);
		archive_fullDenzoPath = PathUtils.FitPathToOS(archive_fullDenzoPath);

		File file_archive_fullDenzoPath = new File(archive_fullDenzoPath);

		if (file_archive_fullDenzoPath.exists()) // Archive path exists
		{
			fullDenzoPath = archive_fullDenzoPath;
		} else // Archive path does not exist -> create
		{
			// ----- File exist, try to Sync first = copy to <...>/archive/<...> -----
			File originalFilePath = new File(fullDenzoPath);
			File archiveFilePath = new File(archive_fullDenzoPath);
			try {
				FileUtils.copyDirectory(originalFilePath, archiveFilePath);
			} catch (Exception e) {
				LOG.debug("SyncAndCopyDenzoFiles : Error when trying to Sync: \n originalFilePath=" + originalFilePath
						+ "\narchiveFilePath=" + archiveFilePath);
				// e.printStackTrace();
			}
		}

		// LOG.warn("full Denzo path = " + fullDenzoPath );

		return fullDenzoPath;
	}
	
	/**
	 * returns the value of the give param in the request
	 * try to fix character '+' in the request (filename)
	 * @param request
	 * @param param
	 * @return
	 */
	public static String getRequestParameter(HttpServletRequest request, String param){
		String value = request.getParameter(param);
		String query = request.getQueryString();
		int id = query.indexOf(param);
		if (id != -1){
			String paramValue = query.substring(1+id+param.length());
			int idA = paramValue.indexOf('&');
			if (idA != -1){
				paramValue = paramValue.substring(0,idA);
			}
			value = paramValue;
		}
		return value;
	}

}
