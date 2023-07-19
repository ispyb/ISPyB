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

package ispyb.common.util.beamlines;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a beamline at ESRF. A beamline has a name, a name for the directory where data is stored, and a phone number
 * The beamline name can have others names associated (eg ID14-1 & ID14 1)
 * 
 */
public enum ESRFBeamlineEnum {
	BM07("BM07", "bm07", new String[] { "BM07" },
			"<font style='color:#6888A8;font-size:90%'>+3347688<font style='color:#0000FF;font-weight:bold;'>2323</font></font>",
			null, false, false, false), 
	ID14_1("ID14-1", "id14eh1", new String[] { "ID14 1" },
			"<font style='color:#6888A8;font-size:90%'>+3347688<font style='color:#0000FF;font-weight:bold;'>2323</font></font>",
			null, false, false, false), 
	ID14_2("ID14-2", "id14eh2", new String[] { "ID14 2" },
			"<font style='color:#000080;font-size:90%'>+3347688<font style='color:#0000FF;font-weight:bold;'>2565</font></font>",
			null, false, false, false), 
	ID14_3("ID14-3", "id14eh3", new String[] { "ID14 3" },
			"<font style='color:#000080;font-size:90%'>+3347688<font style='color:#0000FF;font-weight:bold;'>2786</font></font>",
			null, false, false, false), 
	ID14_4("ID14-4", "id14eh4", new String[] { "ID14 4" },
			"<font style='color:#000080;font-size:90%'>+3347688<font style='color:#0000FF;font-weight:bold;'>2322</font></font>",
			null, false, false, false), 
	ID23_1("ID23-1", "id23eh1", new String[] { "ID23 1" },
			"<font style='color:#000080;font-size:90%'>+3347688<font style='color:#0000FF;font-weight:bold;'>2261</font></font>",
			new String[] { "x_geo_corr.cbf", "y_geo_corr.cbf" }, false, true, true), 
	ID23_2("ID23-2", "id23eh2", new String[] { "ID23 2" },
			"<font style='color:#000080;font-size:90%'>+3347688<font style='color:#0000FF;font-weight:bold;'>2590</font></font>",
			new String[] { "x_geo_corr.cbf", "y_geo_corr.cbf" }, false, true, true), 
	ID29("ID29", "id29", new String[] { "ID29" },
			"<font style='color:#000080;font-size:90%'>+3347688<font style='color:#0000FF;font-weight:bold;'>2805</font></font>",
			new String[] { "x_geo_corr.cbf", "y_geo_corr.cbf" }, false, true, true), 
	ID30A1("ID30A-1", "id30a1", new String[] { "ID30A1" },
			"<font style='color:#000080;font-size:90%'>+3347688<font style='color:#0000FF;font-weight:bold;'>****</font></font>",
			null, true, true, true), 
	ID30A2("ID30A-2", "id30a2", new String[] { "ID30A2" },
			"<font style='color:#000080;font-size:90%'>+3347688<font style='color:#0000FF;font-weight:bold;'>****</font></font>",
			null, true, true, true), 
	ID30A3("ID30A-3", "id30a3", new String[] { "ID30A3" },
			"<font style='color:#000080;font-size:90%'>+3347688<font style='color:#0000FF;font-weight:bold;'>****</font></font>",
			null, true, true, true), 
	ID30B("ID30B", "id30b", null,
			"<font style='color:#000080;font-size:90%'>+3347688<font style='color:#0000FF;font-weight:bold;'>****</font></font>",
			null, true, true, true), 
	BM14("BM14", "bm14", new String[] { "BM14U" },
			"<font style='color:#000080;font-size:90%'>+3347688<font style='color:#0000FF;font-weight:bold;'>2703</font></font>",
			null, false, true, false), 
	BM16("BM16", "bm16", null,
			"<font style='color:#000080;font-size:90%'>+3347688<font style='color:#0000FF;font-weight:bold;'>2614</font></font>",
			null, false, false, false), 
	BM29("BM29", "bm29", null,
			"<font style='color:#000080;font-size:90%'>+3347688<font style='color:#0000FF;font-weight:bold;'>2628</font></font>",
			null, false, true, false), 
	BM30A("BM30A", "bm30a", null,
			"<font style='color:#000080;font-size:90%'>+3347688<font style='color:#0000FF;font-weight:bold;'>2787</font></font>",
			null, false, true, false), 
	ID19("ID19", "id19", null,
			"<font style='color:#000080;font-size:90%'>+3347688<font style='color:#0000FF;font-weight:bold;'>2700</font></font>",
			null, false, true, false),
	CM01("CM01", "cm01", null,"<font style='color:#000080;font-size:90%'>+3347688<font style='color:#0000FF;font-weight:bold;'>****</font></font>", null, false, true, false);

	private ESRFBeamlineEnum(String beamlineName, String directoryName, String[] associatedName, String phoneNumber,
			String[] correctionFiles, boolean emailNotification, boolean inActivity, boolean toBeProtected) {
		this.beamlineName = beamlineName;
		this.directoryName = directoryName;
		this.associatedName = associatedName;
		this.phoneNumber = phoneNumber;
		this.correctionFiles = correctionFiles;
		this.emailNotification = emailNotification;
		this.inActivity = inActivity;
		this.toBeProtected = toBeProtected;
	}

	/**
	 * beamline official name
	 */
	private final String beamlineName;

	/**
	 * name of the directory where data is stored on pyarch
	 */
	private final String directoryName;

	/**
	 * associated names for the beamline
	 */
	private final String[] associatedName;

	/**
	 * phone number
	 */
	private final String phoneNumber;

	/**
	 * correctionFiles: x_geo_corr.cbf & y_geo_corr.cbf
	 * 
	 * @return
	 */
	private final String[] correctionFiles;

	/**
	 * emailNotification: if true, an automatic email will be send to the labcontact when the first dataCollection is created for a
	 * session -- Issue 1728
	 */
	private final boolean emailNotification;

	/**
	 * inActivity: true if the beamline is currently in activity. I false, the beamline is not proposed in the list in the prepare
	 * experiment tab.
	 */
	private final boolean inActivity;
	
	/**
	 * toBeProtected: true if the beamline data shall be protected by calling the dataprotection webservice.
	 */
	private final boolean toBeProtected;

	public String getBeamlineName() {
		return this.beamlineName;
	}

	public String getDirectoryName() {
		return this.directoryName;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public String[] getCorrectionFiles() {
		return correctionFiles;
	}

	public String[] getAssociatedName() {
		return this.associatedName;
	}

	public boolean isEmailNotification() {
		return emailNotification;
	}

	public boolean isInActivity() {
		return inActivity;
	}
	
	public boolean isToBeProtected() {
		return toBeProtected;
	}

	public static ESRFBeamlineEnum retrieveBeamlineWithName(String aName) {
		for (ESRFBeamlineEnum b : ESRFBeamlineEnum.values()) {
			if (b.getBeamlineName().equals(aName)) {
				return b;
			}
			if (b.getAssociatedName() != null) {
				for (int i = 0; i < b.getAssociatedName().length; i++) {
					if (b.getAssociatedName()[i].equals(aName)) {
						return b;
					}
				}
			}
		}
		return null;
	}

	public static List<String> getAllBeamlinesForName(String aName) {
		for (ESRFBeamlineEnum b : ESRFBeamlineEnum.values()) {
			if (b.getBeamlineName().equals(aName)) {
				List<String> list = new ArrayList<String>();
				list.add(b.getBeamlineName());
				if (b.getAssociatedName() != null) {
					for (int i = 0; i < b.getAssociatedName().length; i++) {
						list.add(b.getAssociatedName()[i]);
					}
				}
				return list;
			}
		}
		return null;
	}

	public static String retrieveDirectoryNameWithName(String aName) {
		ESRFBeamlineEnum beamline = retrieveBeamlineWithName(aName);
		if (beamline != null) {
			return beamline.getDirectoryName();
		}
		return null;
	}

	public static String retrievePhoneNumberWithName(String aName) {
		if (aName == null)
			return "";
		ESRFBeamlineEnum beamline = retrieveBeamlineWithName(aName);
		if (beamline != null) {
			return beamline.getPhoneNumber();
		}
		return "";
	}

	public static String[] getBeamlineNames() {
		String[] list = new String[ESRFBeamlineEnum.values().length];
		int i = 0;
		for (ESRFBeamlineEnum b : ESRFBeamlineEnum.values()) {
			list[i++] = b.getBeamlineName();
		}
		return list;
	}

	public static String[] getBeamlineNamesInActivity() {
		List<String> listBeamlinesInActivity = new ArrayList<String>();
		for (ESRFBeamlineEnum b : ESRFBeamlineEnum.values()) {
			if (b.inActivity)
				listBeamlinesInActivity.add(b.getBeamlineName());
		}
		String[] list = listBeamlinesInActivity.toArray(new String[listBeamlinesInActivity.size()]);

		return list;
	}

	public static String[] retrieveCorrectionFilesNameWithName(String aName) {
		ESRFBeamlineEnum beamline = retrieveBeamlineWithName(aName);
		if (beamline != null) {
			return beamline.getCorrectionFiles();
		}
		return null;
	}

	public static boolean isBeamlineEmailNotification(String aName) {
		ESRFBeamlineEnum beamline = retrieveBeamlineWithName(aName);
		if (beamline != null) {
			return beamline.isEmailNotification();
		}
		return false;
	}
	
	public static String[] getBeamlineNamesToBeProtected() {
		List<String> listBeamlinesToBeProtected = new ArrayList<String>();
		for (ESRFBeamlineEnum b : ESRFBeamlineEnum.values()) {
			if (b.inActivity && b.toBeProtected)
				listBeamlinesToBeProtected.add(b.getBeamlineName());
		}
		String[] list = listBeamlinesToBeProtected.toArray(new String[listBeamlinesToBeProtected.size()]);

		return list;
	}

}
