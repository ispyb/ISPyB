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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

/***
 * This class defines the methods to manage files tar in ISpyB
 * 
 * @author BODIN
 *
 */
public final class IspybFileUtils {

	private static final int BUFFER_SIZE = 1024;

	public static byte[] getFile(String filePath) throws IOException {
		File file = new File(filePath);
		if (file.exists()){
			byte[] fileInBytes = new byte[(int) file.length()];
			InputStream inputStream = null;
			try {
				inputStream = new FileInputStream(file);
				inputStream.read(fileInBytes);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw e;
			} finally {
				inputStream.close();
			}
			return fileInBytes;
		}
		return null;
	}

	/**
	 * returns the list of files in the given directoryFile (and sub-directories) with the given fileName
	 * 
	 * @param directoryFile
	 * @param fileName
	 * @return
	 */
	public static List<File> getListFile(File directoryFile, String fileName) {
		List<File> resultFilesList = new ArrayList<File>();
		if (directoryFile == null)
			return resultFilesList;
		// File[] list = directoryFile.listFiles();
		List<File> listFile = Arrays.asList(directoryFile.listFiles());
		Collections.sort(listFile, Collections.reverseOrder());
		if (listFile != null) {
			for (int k = 0; k < listFile.size(); k++) {
				if (listFile.get(k).isFile() && listFile.get(k).getName().equals(fileName)) {
					resultFilesList.add(listFile.get(k));
				} else if (listFile.get(k).isDirectory()) {
					List<File> subList = getListFile(listFile.get(k), fileName);
					resultFilesList.addAll(subList);
				}
			}
		}
		return resultFilesList;
	}
	
	

}
