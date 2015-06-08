package ispyb.server.biosaxs.services.utils.reader.pdb;

import ispyb.common.util.PathUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import com.google.gson.Gson;

public class PDBParser {
	public static String fileToString(String sourceFileName) throws IOException {
		if (sourceFileName != null) {
			File file = new File(sourceFileName);
			if (file.exists()) {
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
		}
		return null;
	}

	private static String getPDBContent(String filePath, String suffix) throws IOException {
		StringBuilder content = new StringBuilder();
		int count = 0;
		filePath = PathUtils.getPath(filePath);
		if (new File(filePath).exists()) {
			String line;
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			while ((line = br.readLine()) != null) {
				if (line.startsWith("ATOM")) {
					/*
					 * PDB FORMAT COLUMNS DATA TYPE FIELD DEFINITION
					 * ------------
					 * ----------------------------------------------
					 * --------------------------- 1 - 6 Record name "ATOM  " 7
					 * - 11 Integer serial Atom serial number. 13 - 16 Atom name
					 * Atom name. 17 Character altLoc Alternate location
					 * indicator. 18 - 20 Residue name resName Residue name. 22
					 * Character chainID Chain identifier. 23 - 26 Integer
					 * resSeq Residue sequence number. 27 AChar iCode Code for
					 * insertion of residues. 31 - 38 Real(8.3) x Orthogonal
					 * coordinates for X in Angstroms. 39 - 46 Real(8.3) y
					 * Orthogonal coordinates for Y in Angstroms. 47 - 54
					 * Real(8.3) z Orthogonal coordinates for Z in Angstroms. 55
					 * - 60 Real(6.2) occupancy Occupancy. 61 - 66 Real(6.2)
					 * tempFactor Temperature factor. 77 - 78 LString(2) element
					 * Element symbol, right-justified. 79 - 80 LString(2)
					 * charge Charge on the atom.
					 */
					if (line.length() >= 54) {
						String x = line.substring(31, 38).trim();
						String y = line.substring(39, 46).trim();
						String z = line.substring(47, 54).trim();
						content.append("H" + "\t" + x + "\t" + y + "\t" + z + "\t" + suffix + "\n");
					}
					count++;
				}
			}
			br.close();
		}
		HashMap<String, String> result = new HashMap<String, String>();
		result.put("content", content.toString());
		result.put("count", String.valueOf(count));
		return count + "\n" + "ISPyB Generated File\n" + content.toString();
	}

	public static String getPDBContent(String filePath, HashMap<String, String> property) throws IOException {
		return PDBParser.getPDBContent(filePath, new Gson().toJson(property));
	}

	public static String getPDBContent(String filePath) throws IOException {
		return PDBParser.getPDBContent(filePath, "");
	}
}
