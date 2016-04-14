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
/**
 * ViewSessionForm
 */

package ispyb.client.mx.collection;

import ispyb.common.util.Constants;
import ispyb.common.util.beamlines.EMBLBeamlineEnum;
import ispyb.common.util.beamlines.ESRFBeamlineEnum;
import ispyb.common.util.beamlines.MAXIVBeamlineEnum;
import ispyb.common.util.beamlines.SOLEILBeamlineEnum;
import ispyb.server.mx.vos.collections.Session3VO;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

/**
 * @struts.form name="viewSessionForm"
 */

public class ViewSessionForm extends ActionForm implements Serializable {
    static final long serialVersionUID = 0;

    private List<Session3VO> listInfo = new ArrayList<Session3VO>();
    private List<SessionInformation> listSessionInformation = new ArrayList<SessionInformation>();
    private Timestamp 	startDate;
    private String 		startDatest;
    private String 		endDatest;
    private String 		beamLineName;
    private String 		beamLineOperator;
    private String 		beamLineOperatorEmail; // local contact email address
    private String 		comments;
    private String 		urole;
    private String 		sessionsTitle = "";
    private Integer 	theSessionId;
 // session fields for fx accounts
	private String sessionTitle;
	private String structureDeterminations;
	private String dewarTransport;
	private String dataBackupFrance;
	private String dataBackupEurope;
    
    private int			nbSessionsToDisplay;
    private boolean 	isAllowedToSubmitReport = false;
    
    private String[] beamlineList;
    private String 		beamLineNameFromList = "";
    
    
 // DLS #####
    private int 		visitNumber;
    
    public ViewSessionForm()
    {
    	super();
    	sessionsTitle = Constants.ALLSESSIONS;
    }
    	
    /**
	 * @return Returns the visitNumber.
	 */
	public int getVisitNumber() {
		return visitNumber;
	}
	/**
	 * @param beamlineName The visitNumber to set.
	 */
	public void setVisitNumber(int visitNumber) {
		this.visitNumber = visitNumber;
	}
	
	/**
	 * @return Returns the beamlineName.
	 */
	public String getBeamLineName() {
		return beamLineName;
	}
	/**
	 * @param beamlineName The beamlineName to set.
	 */
	public void setBeamLineName(String beamLineName) {
		this.beamLineName = beamLineName;
	}
	/**
	 * @return Returns the startDatest.
	 */
	public String getStartDatest() {
		return startDatest;
	}
	/**
	 * @param startDatest The startDatest to set.
	 */
	public void setStartDatest(String startDatest) {
		this.startDatest = startDatest;
	}
	
	/**
	 * @return Returns the endDatest.
	 */
	public String getEndDatest() {
		return endDatest;
	}
	/**
	 * @param endDatest The endDatest to set.
	 */
	public void setEndDatest(String endDatest) {
		this.endDatest = endDatest;
	}
	
   public List<Session3VO> getListInfo() {
        return listInfo;
    }

    public void setListInfo(List<Session3VO> listInfo) {
        this.listInfo = listInfo;
    }

	/**
	 * @return Returns the startDate.
	 */
	public Timestamp getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate The startDate to set.
	 */
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	
	
	/**
	 * @return Returns the beamLineOperator.
	 */
	public String getBeamLineOperator() {
		return beamLineOperator;
	}
	/**
	 * @param beamLineOperator The beamLineOperator to set.
	 */
	public void setBeamLineOperator(String beamLineOperator) {
		this.beamLineOperator = beamLineOperator;
	}
	/**
	 * @return Returns the comments.
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * @param comments The comments to set.
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}	
	
	/**
	 * @return Returns the urole.
	 */
	public String getUrole() {
		return urole;
	}
	/**
	 * @param urole The urole to set.
	 */
	public void setUrole(String urole) {
		this.urole = urole;
	}
	/**
	 * @return Returns the theSessionId.
	 */
	public Integer getTheSessionId() {
		return theSessionId;
	}
	/**
	 * @param theSessionId The theSessionId to set.
	 */
	public void setTheSessionId(Integer theSessionId) {
		this.theSessionId = theSessionId;
	}
	
	/**
	 * @return Returns the sessionsTitle.
	 */
	public String getSessionsTitle() {
		return sessionsTitle;
	}
	/**
	 * @param sessionsTitle The sessionsTitle to set.
	 */
	public void setSessionsTitle(String sessionsTitle) {
		this.sessionsTitle = sessionsTitle;
	}
	/**
	 * @return Returns the nbSessionsToDisplay.
	 */
	public int getNbSessionsToDisplay() {
		return nbSessionsToDisplay;
	}
	/**
	 * @param nbSessionsToDisplay The nbSessionsToDisplay to set.
	 */
	public void setNbSessionsToDisplay(int nbSessionsToDisplay) {
		this.nbSessionsToDisplay = nbSessionsToDisplay;
	}
	/**
	 * 
	 * @return
	 */
	public boolean isAllowedToSubmitReport()									{return isAllowedToSubmitReport;}
	public void setAllowedToSubmitReport(boolean isAllowedToSubmitReport)		{this.isAllowedToSubmitReport = isAllowedToSubmitReport;}
	
	public void setListSessionInformation(List<SessionInformation> listSessionInformation){
		this.listSessionInformation = listSessionInformation;
	}
	
	public List<SessionInformation> getListSessionInformation(){
		return this.listSessionInformation;
	}
	
	/**
	 * @return Returns the beamLineOperatorEmail.
	 */
	public String getBeamLineOperatorEmail() {
		return beamLineOperatorEmail;
	}

	/**
	 * @param beamLineOperatorEmail
	 *            The beamLineOperatorEmail to set.
	 */
	public void setBeamLineOperatorEmail(String beamLineOperatorEmail) {
		this.beamLineOperatorEmail = beamLineOperatorEmail;
	}
	
	/**
	 * @return Returns the sessionTitle
	 */
	public String getSessionTitle() {
		return sessionTitle;
	}

	/**
	 * @param sessionTitle
	 *            The sessionTitle to set.
	 */
	public void setSessionTitle(String sessionTitle) {
		this.sessionTitle = sessionTitle;
	}
	
	/**
	 * @return Returns the structureDeterminations
	 */
	public String getStructureDeterminations() {
		return this.structureDeterminations;
	}

	/**
	 * @param structureDeterminations
	 *            The structureDeterminations to set.
	 */
	public void setStructureDeterminations(String structureDeterminations) {
		this.structureDeterminations = structureDeterminations;
	}
	
	/**
	 * @return Returns the dewarTransport
	 */
	public String getDewarTransport() {
		return dewarTransport;
	}

	/**
	 * @param dewarTransport
	 *            The dewarTransport to set.
	 */
	public void setDewarTransport(String dewarTransport) {
		this.dewarTransport = dewarTransport;
	}
	
	/**
	 * @return Returns the data backup & express delivery France
	 */
	public String getDataBackupFrance() {
		return dataBackupFrance;
	}

	/**
	 * @param dataBackupFrance
	 *            The dataBackupFrance to set.
	 */
	public void setDataBackupFrance(String dataBackupFrance) {
		this.dataBackupFrance = dataBackupFrance;
	}
	
	/**
	 * @return Returns the data backup & express delivery Europe
	 */
	public String getDataBackupEurope() {
		return dataBackupEurope;
	}

	/**
	 * @param dataBackupEurope
	 *            The dataBackupEurope to set.
	 */
	public void setDataBackupEurope(String dataBackupEurope) {
		this.dataBackupEurope = dataBackupEurope;
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] getBeamlineList(){
		return this.beamlineList;
	}
	
	/**
	 * 
	 * @param beamlineList
	 */
	public void setBeamlineList(String[] beamlineList){
		this.beamlineList = beamlineList;
	}
	
	public void setBeamLineList(){
		this.beamlineList = Constants.BEAMLINE_LOCATION;
		if(Constants.SITE_IS_ESRF()){
			beamlineList = ESRFBeamlineEnum.getBeamlineNames();
		}
		if(Constants.SITE_IS_EMBL()){
			beamlineList = EMBLBeamlineEnum.getBeamlineNames();
		}
        if (Constants.SITE_IS_MAXIV()){
			beamlineList = MAXIVBeamlineEnum.getBeamlineNames();
        }
        if(Constants.SITE_IS_SOLEIL()){
			beamlineList = SOLEILBeamlineEnum.getBeamlineNames();
		}
	}
	
	/**
	 * @return Returns the beamlineName.
	 */
	public String getBeamLineNameFromList() {
		return beamLineNameFromList;
	}
	/**
	 * @param beamlineName The beamlineNameFromList to set.
	 */
	public void setBeamLineNameFromList(String beamLineNameFromList) {
		this.beamLineNameFromList = beamLineNameFromList;
	}

}
