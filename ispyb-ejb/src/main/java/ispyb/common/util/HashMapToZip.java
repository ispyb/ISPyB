package ispyb.common.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

public class HashMapToZip {
	
	private final static Logger LOG = Logger.getLogger("HashMapToZip");
	
	/**
	 * 
	 * @param files
	 * 			  ["Path on the zip file", "absolute file path on the disk, where the file actually is"]
	 *            ["frames/file.dat" : "/pyarch29/data/file.dat"]
	 * @return
	 */
	public static byte[] doZip(HashMap<String, String> files) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ZipOutputStream zipfile = new ZipOutputStream(bos);
		try {
			
			/** Checking for PathUtils **/
			for (String key : files.keySet()) {
				files.put(key, PathUtils.getPath(files.get(key)));
			}
			ZipEntry zipentry = null;
			for (String filePath : files.keySet()) {
				if (new File(files.get(filePath)).exists()){
					zipentry = new ZipEntry(filePath);
					zipfile.putNextEntry(zipentry);
					zipfile.write(fileToBytes(files.get(filePath)));
				}
				else{
					LOG.warn(files.get(filePath) + " doesn't exist");
				}
			}
		
		} catch (IOException ioe) {
			ioe.printStackTrace();
			LOG.error("Error creating zip file: " + ioe);
		}
		finally{
			try {
				zipfile.close();
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bos.toByteArray();
	}

	/**
	 * Converts a filepath to an array of bytes
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	private static byte[] fileToBytes(String filePath) throws IOException {
		FileInputStream fis = new FileInputStream(filePath);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		byte[] bytes;
		try {
			for (int readNum; (readNum = fis.read(buf)) != -1;) {
				bos.write(buf, 0, readNum); // no doubt here is 0
			}
			bytes = bos.toByteArray();
		} catch (IOException ex) {
			ex.printStackTrace();
			LOG.error("Error creating zip file: " +  ex.getMessage());
			throw ex;
		}
		finally{
			fis.close();
			bos.flush();
			bos.close();
		}

//		// below is the different part
//		File someFile = new File("java2.pdf");
//		FileOutputStream fos = new FileOutputStream(someFile);
//		fos.write(bytes);
//		fos.flush();
//		fos.close();
		return bytes;
	}
}
