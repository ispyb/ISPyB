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
 * This class represents a beamline at MAXIV. 
 * A beamline has a name, a name for the directory where data is stored, and a phone number
 * The beamline name can have others names associated (eg ID14-1 & ID14 1)
 *
 */
public enum MAXIVBeamlineEnum{
	BIOMAX("BioMAX", "idBioMAX", new String[]{"BioMAX"}, "<font style='color:#6888A8;font-size:90%'>+46702906618<font style='color:#0000FF;font-weight:bold;'>312</font></font>", null, true, true),
	MX("MX", "idi911_3", new String[]{"i911-3 (MX)"}, "<font style='color:#000080;font-size:90%'>+46462229793<font style='color:#0000FF;font-weight:bold;'>313</font></font>", null, true, false),
	;
	
	private MAXIVBeamlineEnum(String beamlineName, String directoryName, String[] associatedName, String phoneNumber, String[] correctionFiles, boolean emailNotification, boolean inActivity){
		this.beamlineName = beamlineName;
		this.directoryName = directoryName;
		this.associatedName = associatedName;
		this.phoneNumber = phoneNumber;
		this.correctionFiles = correctionFiles;
		this.emailNotification = emailNotification;
		this.inActivity = inActivity;
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
	 * @return
	 */
	private final String[] correctionFiles;
	
	/**
	 * emailNotification: if true, an automatic email will be send to the labcontact when the first dataCollection is created for a session -- Issue 1728
	 */
	private final boolean emailNotification;
	
	/**
	 * inActivity: true if the beamline is currently in activity. I false, the beamline is not proposed in the list in the prepare experiment tab.
	 */
	private final boolean inActivity;
	
	public String getBeamlineName(){
		return this.beamlineName;
	}
	
	public String getDirectoryName(){
		return this.directoryName;
	}
	
	public String getPhoneNumber(){
		return this.phoneNumber;
	}
	
	public String[] getCorrectionFiles() {
		return correctionFiles;
	}

	public String[] getAssociatedName(){
		return this.associatedName;
	}
	
	public boolean isEmailNotification() {
		return emailNotification;
	}

	public boolean isInActivity() {
		return inActivity;
	}

	public static MAXIVBeamlineEnum retrieveBeamlineWithName(String aName){
		for(MAXIVBeamlineEnum b:MAXIVBeamlineEnum.values()){
			if(b.getBeamlineName().equals(aName)){
				return b;
			}
			if(b.getAssociatedName() != null){
				for(int i=0;i<b.getAssociatedName().length;i++){
					if(b.getAssociatedName()[i].equals(aName)){
						return b;
					}
				}
			}
		}
		return null;
	}
	
	public static List<String> getAllBeamlinesForName(String aName){
		for(MAXIVBeamlineEnum b:MAXIVBeamlineEnum.values()){
			if(b.getBeamlineName().equals(aName)){
				List<String> list = new ArrayList<String>();
				list.add(b.getBeamlineName());
				if(b.getAssociatedName() != null){
					for(int i=0;i<b.getAssociatedName().length;i++){
						list.add(b.getAssociatedName()[i]);
					}
				}
				return list;
			}
		}
		return null;
	}
	
	public static String retrieveDirectoryNameWithName(String aName){
		MAXIVBeamlineEnum beamline = retrieveBeamlineWithName(aName);
		if(beamline != null){
			return beamline.getDirectoryName();
		}
		return null;
	}
	
	public static String retrievePhoneNumberWithName(String aName){
		if(aName == null)
			return "";
		MAXIVBeamlineEnum beamline = retrieveBeamlineWithName(aName);
		if(beamline != null){
			return beamline.getPhoneNumber();
		}
		return "";
	}
	
	public static String[] getBeamlineNames(){
		String[] list = new String[MAXIVBeamlineEnum.values().length];
		int i=0;
		for(MAXIVBeamlineEnum b:MAXIVBeamlineEnum.values()){
			list[i++] = b.getBeamlineName();
		}
		return list;
	}
	
	public static String[] getBeamlineNamesInActivity(){
		List<String> listBeamlinesInActivity = new ArrayList<String>();
		for(MAXIVBeamlineEnum b:MAXIVBeamlineEnum.values()){
			if (b.inActivity)
				listBeamlinesInActivity.add(b.getBeamlineName());
		}
		String[] list = listBeamlinesInActivity.toArray(new String[listBeamlinesInActivity.size()]);
		
		return list;
	}
	
	public static String[] retrieveCorrectionFilesNameWithName(String aName){
		MAXIVBeamlineEnum beamline = retrieveBeamlineWithName(aName);
		if(beamline != null){
			return beamline.getCorrectionFiles();
		}
		return null;
	}
	
	public static boolean isBeamlineEmailNotification(String aName){
		MAXIVBeamlineEnum beamline = retrieveBeamlineWithName(aName);
		if(beamline != null){
			return beamline.isEmailNotification();
		}
		return false;
	}
	
}
