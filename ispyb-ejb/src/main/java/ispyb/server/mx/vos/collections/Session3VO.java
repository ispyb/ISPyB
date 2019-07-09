/*************************************************************************************************
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
 ****************************************************************************************************/
package ispyb.server.mx.vos.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OrderBy;

import ispyb.common.util.Constants;
import ispyb.common.util.StringUtils;
import ispyb.server.common.vos.ISPyBValueObject;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.security.LdapConnection;

/**
 * Session3 value object mapping table Session
 * 
 */
@Entity
@Table(name = "BLSession")
@SqlResultSetMapping(name = "sessionNativeQuery", entities = { @EntityResult(entityClass = Session3VO.class) })
public class Session3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(Session3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "sessionId")
	protected Integer sessionId;

	@Column(name = "expSessionPk")
	protected Long expSessionPk;

	@ManyToOne
	@JoinColumn(name = "proposalId")
	protected Proposal3VO proposalVO;

	@ManyToOne
	@JoinColumn(name = "beamLineSetupId")
	protected BeamLineSetup3VO beamLineSetupVO;

	@Column(name = "projectCode")
	protected String projectCode;

	@Column(name = "startDate")
	protected Date startDate;

	@Column(name = "endDate")
	protected Date endDate;

	@Column(name = "beamLineName")
	protected String beamlineName;

	@Column(name = "scheduled")
	protected Byte scheduled;

	@Column(name = "nbShifts")
	protected Integer nbShifts;

	@Column(name = "comments")
	protected String comments;

	@Column(name = "beamlineOperator")
	protected String beamlineOperator;

	@Column(name = "usedFlag")
	protected Byte usedFlag;

	@Column(name = "sessionTitle")
	protected String sessionTitle;

	@Column(name = "structureDeterminations")
	protected Float structureDeterminations;

	@Column(name = "dewarTransport")
	protected Float dewarTransport;

	@Column(name = "databackupEurope")
	protected Float databackupFrance;

	@Column(name = "databackupFrance")
	protected Float databackupEurope;

	@Column(name = "visit_number")
	protected Integer visit_number;

	@Column(name = "operatorSiteNumber")
	protected String operatorSiteNumber;

	@Column(name = "bltimeStamp")
	protected Date timeStamp;
	
	@Column(name = "lastUpdate")
	protected Date lastUpdate;
	
	@Column(name = "protectedData")
	protected String protectedData;

	@OneToMany//(fetch = FetchType.LAZY)
	@JoinColumn(name = "sessionId")
	private Set<DataCollectionGroup3VO> dataCollectionGroupVOs;

	@OneToMany//(fetch = FetchType.LAZY)
	@JoinColumn(name = "sessionId")
	@OrderBy(clause = "startTime DESC")
	private Set<XFEFluorescenceSpectrum3VO> xfeSpectrumVOs;

	@OneToMany//(fetch = FetchType.LAZY)
	@JoinColumn(name = "sessionId")
	@OrderBy(clause = "startTime DESC")
	private Set<EnergyScan3VO> energyScanVOs;
	
	@Column(name = "externalId")
	protected Integer externalId;
	
	@Column(name = "nbReimbDewars")
	protected Integer nbReimbDewars;


	// this link is not bidirectional so the following should not be declared
	// @ManyToMany
	// @JoinTable(name = "ShippingHasSession", joinColumns = { @JoinColumn(name = "sessionId", referencedColumnName =
	// "sessionId") }, inverseJoinColumns = { @JoinColumn(name = "shippingId", referencedColumnName = "shippingId") })
	// private Set<Shipping3VO> shippingVOs;

	public Session3VO() {
	};

	public Session3VO(Integer sessionId, Long expSessionPk, Proposal3VO proposalVO, BeamLineSetup3VO beamLineSetupVO,
			String projectCode, Date startDate, Date endDate, String beamlineName, Byte scheduled, Integer nbShifts,
			String comments, String beamlineOperator, Byte usedFlag, String sessionTitle,
			Float structureDeterminations, Float dewarTransport, Float databackupFrance, Float databackupEurope,
			Integer visit_number, String operatorSiteNumber, Date timeStamp, Date lastUpdate, String protectedData, Integer externalId) {
		super();
		this.sessionId = sessionId;
		this.expSessionPk = expSessionPk;
		this.proposalVO = proposalVO;
		this.beamLineSetupVO = beamLineSetupVO;
		this.projectCode = projectCode;
		this.startDate = startDate;
		this.endDate = endDate;
		this.beamlineName = beamlineName;
		this.scheduled = scheduled;
		this.nbShifts = nbShifts;
		this.comments = comments;
		this.beamlineOperator = beamlineOperator;
		this.usedFlag = usedFlag;
		this.sessionTitle = sessionTitle;
		this.structureDeterminations = structureDeterminations;
		this.dewarTransport = dewarTransport;
		this.databackupFrance = databackupFrance;
		this.databackupEurope = databackupEurope;
		this.visit_number = visit_number;
		this.timeStamp = timeStamp;
		this.operatorSiteNumber = operatorSiteNumber;
		this.lastUpdate = lastUpdate;
		this.protectedData = protectedData;
		this.externalId = externalId;
	}

	public Session3VO(Session3VO vo) {
		this.beamlineName = vo.getBeamlineName();
		this.beamlineOperator = vo.getBeamlineOperator();
		this.beamLineSetupVO = vo.getBeamLineSetupVO();
		this.comments = vo.getComments();
		this.databackupEurope = vo.getDatabackupEurope();
		this.databackupFrance = vo.getDatabackupFrance();
		this.dewarTransport = vo.getDewarTransport();
		this.startDate = vo.getStartDate();
		this.endDate = vo.getEndDate();
		this.visit_number = vo.getVisit_number();
		this.nbShifts = vo.getNbShifts();
		this.projectCode = vo.getProjectCode();
		this.proposalVO = vo.getProposalVO();
		this.scheduled = vo.getScheduled();
		this.sessionId = vo.getSessionId();
		this.sessionTitle = vo.getSessionTitle();
		this.expSessionPk = vo.getExpSessionPk();
		this.structureDeterminations = vo.getStructureDeterminations();
		this.usedFlag = vo.getUsedFlag();
		this.operatorSiteNumber = vo.getOperatorSiteNumber();
		this.timeStamp = vo.getTimeStamp();
		this.lastUpdate = vo.getLastUpdate();
		this.protectedData = vo.getProtectedData();
		this.externalId = vo.getExternalId();
	}

	public void fillVOFromWS(SessionWS3VO vo) {
		this.beamlineName = vo.getBeamlineName();
		this.beamlineOperator = vo.getBeamlineOperator();
		this.comments = vo.getComments();
		this.databackupEurope = vo.getDatabackupEurope();
		this.databackupFrance = vo.getDatabackupFrance();
		this.dewarTransport = vo.getDewarTransport();
		this.startDate = vo.getStartDate();
		this.endDate = vo.getEndDate();
		this.visit_number = vo.getVisit_number();
		this.nbShifts = vo.getNbShifts();
		this.projectCode = vo.getProjectCode();
		this.scheduled = vo.getScheduled();
		this.sessionId = vo.getSessionId();
		this.expSessionPk = vo.getExpSessionPk();
		this.sessionTitle = vo.getSessionTitle();
		this.structureDeterminations = vo.getStructureDeterminations();
		this.usedFlag = vo.getUsedFlag();
		this.timeStamp = vo.getTimeStamp();
		this.operatorSiteNumber = vo.getOperatorSiteNumber();
		this.lastUpdate = vo.getLastUpdate();
		this.protectedData = vo.getProtectedData();
		this.externalId = vo.getExternalId();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	public Integer getSessionId() {
		return sessionId;
	}

	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}

	public Long getExpSessionPk() {
		return expSessionPk;
	}

	public void setExpSessionPk(Long expSessionPk) {
		this.expSessionPk = expSessionPk;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public BeamLineSetup3VO getBeamLineSetupVO() {
		return beamLineSetupVO;
	}

	public void setBeamLineSetupVO(BeamLineSetup3VO beamLineSetupVO) {
		this.beamLineSetupVO = beamLineSetupVO;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getBeamlineName() {
		return beamlineName;
	}

	public void setBeamlineName(String beamlineName) {
		this.beamlineName = beamlineName;
	}

	public Byte getScheduled() {
		return scheduled;
	}

	public void setScheduled(Byte scheduled) {
		this.scheduled = scheduled;
	}

	public Integer getNbShifts() {
		return nbShifts;
	}

	public void setNbShifts(Integer nbShifts) {
		this.nbShifts = nbShifts;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getBeamlineOperator() {
		return beamlineOperator;
	}

	public void setBeamlineOperator(String beamlineOperator) {
		this.beamlineOperator = beamlineOperator;
	}

	public Byte getUsedFlag() {
		return usedFlag;
	}

	public void setUsedFlag(Byte usedFlag) {
		this.usedFlag = usedFlag;
	}

	public String getSessionTitle() {
		return sessionTitle;
	}

	public void setSessionTitle(String sessionTitle) {
		this.sessionTitle = sessionTitle;
	}

	public Float getStructureDeterminations() {
		return structureDeterminations;
	}

	public void setStructureDeterminations(Float structureDeterminations) {
		this.structureDeterminations = structureDeterminations;
	}

	public Float getDewarTransport() {
		return dewarTransport;
	}

	public void setDewarTransport(Float dewarTransport) {
		this.dewarTransport = dewarTransport;
	}

	public Float getDatabackupFrance() {
		return databackupFrance;
	}

	public void setDatabackupFrance(Float databackupFrance) {
		this.databackupFrance = databackupFrance;
	}

	public Float getDatabackupEurope() {
		return databackupEurope;
	}

	public void setDatabackupEurope(Float databackupEurope) {
		this.databackupEurope = databackupEurope;
	}

	public Integer getVisit_number() {
		return visit_number;
	}

	public void setVisit_number(Integer visit_number) {
		this.visit_number = visit_number;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getProtectedData() {
		return protectedData;
	}

	public void setProtectedData(String protectedData) {
		this.protectedData = protectedData;
	}

	public String getOperatorSiteNumber() {
		return operatorSiteNumber;
	}

	public void setOperatorSiteNumber(String operatorSiteNumber) {
		this.operatorSiteNumber = operatorSiteNumber;
	}

	public Proposal3VO getProposalVO() {
		return proposalVO;
	}

	public void setProposalVO(Proposal3VO proposalVO) {
		this.proposalVO = proposalVO;
	}

	public Integer getBeamLineSetupVOId() {
		return beamLineSetupVO == null ? null : beamLineSetupVO.getBeamLineSetupId();
	}

	public Integer getProposalVOId() {
		return proposalVO == null ? null : proposalVO.getProposalId();
	}

	public Set<DataCollectionGroup3VO> getDataCollectionGroupVOs() {
		return dataCollectionGroupVOs;
	}

	public void setDataCollectionGroupVOs(Set<DataCollectionGroup3VO> dataCollectionGroupVOs) {
		this.dataCollectionGroupVOs = dataCollectionGroupVOs;
	}

	public DataCollectionGroup3VO[] getDataCollectionGroupTab() {
		return this.dataCollectionGroupVOs == null ? null : dataCollectionGroupVOs
				.toArray(new DataCollectionGroup3VO[this.dataCollectionGroupVOs.size()]);
	}

	public ArrayList<DataCollectionGroup3VO> getDataCollectionGroupsList() {
		return this.dataCollectionGroupVOs == null ? null : new ArrayList<DataCollectionGroup3VO>(
				Arrays.asList(getDataCollectionGroupTab()));
	}

	public Set<XFEFluorescenceSpectrum3VO> getXfeSpectrumVOs() {
		return xfeSpectrumVOs;
	}

	public void setXfeSpectrumVOs(Set<XFEFluorescenceSpectrum3VO> xfeSpectrumVOs) {
		this.xfeSpectrumVOs = xfeSpectrumVOs;
	}

	public XFEFluorescenceSpectrum3VO[] getXfeSpectrumTab() {
		return this.xfeSpectrumVOs == null ? null : xfeSpectrumVOs
				.toArray(new XFEFluorescenceSpectrum3VO[this.xfeSpectrumVOs.size()]);
	}

	public ArrayList<XFEFluorescenceSpectrum3VO> getXfeSpectrumsList() {
		return this.xfeSpectrumVOs == null ? null : new ArrayList<XFEFluorescenceSpectrum3VO>(
				Arrays.asList(getXfeSpectrumTab()));
	}

	public Set<EnergyScan3VO> getEnergyScanVOs() {
		return energyScanVOs;
	}

	public void setEnergyScanVOs(Set<EnergyScan3VO> energyScanVOs) {
		this.energyScanVOs = energyScanVOs;
	}

	public EnergyScan3VO[] getEnergyScanTab() {
		return this.energyScanVOs == null ? null : energyScanVOs.toArray(new EnergyScan3VO[this.energyScanVOs.size()]);
	}

	public ArrayList<EnergyScan3VO> getEnergyScansList() {
		return this.energyScanVOs == null ? null : new ArrayList<EnergyScan3VO>(Arrays.asList(getEnergyScanTab()));
	}

	public Integer getExternalId() {
		return externalId;
	}

	public void setExternalId(Integer externalId) {
		this.externalId = externalId;
	}

	public Integer getNbReimbDewars() {
		return nbReimbDewars;
	}

	public void setNbReimbDewars(Integer nbReimbDewars) {
		this.nbReimbDewars = nbReimbDewars;
	}

	/**
	 * Checks the values of this value object for correctness and completeness. Should be done before persisting the
	 * data in the DB.
	 * 
	 * @param create
	 *            should be true if the value object is just being created in the DB, this avoids some checks like
	 *            testing the primary key
	 * @throws Exception
	 *             if the data of the value object is not correct
	 */
	@Override
	public void checkValues(boolean create) throws Exception {
		int maxLengthProjectCode = 45;
		int maxLengthBeamLineName = 45;
		int maxLengthComments = 2000;
		int maxLengthBeamLineOperator = 45;
		int maxLengthSessionTitle = 255;
		int maxLengthProtectedData = 1024;

		// proposalVO
		if (proposalVO == null)
			throw new IllegalArgumentException(StringUtils.getMessageRequiredField("Session", "proposal"));
		// projectCode
		if (!StringUtils.isStringLengthValid(this.projectCode, maxLengthProjectCode))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("Session", "projectCode",
					maxLengthProjectCode));
		// beamlineName
		if (!StringUtils.isStringLengthValid(this.beamlineName, maxLengthBeamLineName))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("Session", "beamlineName",
					maxLengthBeamLineName));
		// scheduled
		if (!StringUtils.isBoolean("" + this.scheduled, true))
			throw new IllegalArgumentException(StringUtils.getMessageBooleanField("Session", "scheduled"));
		// comments
		if (!StringUtils.isStringLengthValid(this.comments, maxLengthComments))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("Session", "comments",
					maxLengthComments));
		// beamlineOperator
		if (!StringUtils.isStringLengthValid(this.beamlineOperator, maxLengthBeamLineOperator))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("Session", "beamlineOperator",
					maxLengthBeamLineOperator));
		// usedFlag
		if (!StringUtils.isBoolean("" + this.usedFlag, true))
			throw new IllegalArgumentException(StringUtils.getMessageBooleanField("Session", "usedFlag"));
		// sessionTitle
		if (!StringUtils.isStringLengthValid(this.sessionTitle, maxLengthSessionTitle))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("Session", "sessionTitle",
					maxLengthSessionTitle));
		// protectedData
		if (!StringUtils.isStringLengthValid(this.protectedData, maxLengthProtectedData))
			throw new IllegalArgumentException(StringUtils.getMessageErrorMaxLength("Session", "protectedData",
					maxLengthProtectedData));
	}

	public String[] getBeamlineOperatorLastNameAndFirstNameLetter() {
		// Get first letter of firstName + *
		String firstNameLetter = null;
		String lastName = this.getBeamlineOperator();
		if (this.getBeamlineOperator() != null && !this.getBeamlineOperator().equals("")) {
			if (this.getBeamlineOperator()
					.substring(this.getBeamlineOperator().length() - 2, this.getBeamlineOperator().length() - 1)
					.equals(" "))
				firstNameLetter = this.getBeamlineOperator().substring(this.getBeamlineOperator().length() - 1,
						this.getBeamlineOperator().length())
						+ "*";

			if (firstNameLetter != null && firstNameLetter.length() == 2) {
				// Get lastName without first letter of firstName in case there was a firstname letter
				lastName = this.getBeamlineOperator().substring(0, this.getBeamlineOperator().length() - 2);
				if (lastName.endsWith(" "))
					lastName = lastName.substring(0, lastName.length() - 1);
			}
		}
		String[] tab = new String[2];
		tab[0] = lastName;
		tab[1] = firstNameLetter;
		return tab;
	}

	public String toWSString() {
		String s = "sessionId=" + this.sessionId + ", " + "projectCode=" + this.projectCode + ", " + "startDate="
				+ this.startDate + ", " + "endDate=" + this.endDate + ", " + "beamlineName=" + this.beamlineName + ", "
				+ "scheduled=" + this.scheduled + ", " + "nbShifts=" + this.nbShifts + ", " + "comments="
				+ this.comments + ", " + "beamlineOperator=" + this.beamlineOperator + ", " + "usedFlag="
				+ this.usedFlag + ", " + "sessionTitle=" + this.sessionTitle + ", " + "structureDeterminations="
				+ this.structureDeterminations + ", " + "dewarTransport=" + this.dewarTransport + ", "
				+ "databackupFrance=" + this.databackupFrance + ", " + "databackupEurope=" + this.databackupEurope
				+ ", " + "visit_number=" + this.visit_number + ", " + "timeStamp=" + this.timeStamp
				+ ", " + "lastUpdate=" + this.lastUpdate + ", " + "protectedData=" + this.protectedData;

		return s;
	}
	
	
	public  String getBeamLineOperatorEmail(){
		String beamLineOperatorEmail = "";
		if (Constants.SITE_IS_ESRF() || Constants.SITE_IS_MAXIV()) { // connection to ldap only for the esrf
			
			if (this.getBeamlineOperator() != null && !this.getBeamlineOperator().equals("")) {
				
				String lastName = this.getBeamlineOperator();				
				String firstNameLetter = "*";
				if (this.getBeamlineOperator()
						.substring(this.getBeamlineOperator().length() - 2, this.getBeamlineOperator().length() - 1).equals(" ")) {
					// Get first letter of firstName + *
					firstNameLetter = this.getBeamlineOperator().substring(this.getBeamlineOperator().length() - 1, this.getBeamlineOperator().length()) + "*";
					// Get lastName without first letter of firstName in case there was a firstname letter
					lastName = this.getBeamlineOperator().substring(0, this.getBeamlineOperator().length() - 2);
					if (lastName.endsWith(" "))
						lastName = lastName.substring(0, lastName.length() - 1);
				}
				
				lastName = lastName.replace(' ', '*');
				if(lastName.toLowerCase().startsWith("mc")){
					lastName = "mc*"+lastName.substring(2);
				}
				// Get local contact email
				String email = null;
				if (Constants.SITE_IS_ESRF()) {
					email = LdapConnection.getLocalContactEmail(lastName, firstNameLetter);
					if (email != null && !email.equals(""))
						beamLineOperatorEmail = email;
					LOG.debug("LocalContact email: " + lastName + "/" + firstNameLetter + " = " + beamLineOperatorEmail);
				} else {
					LOG.debug("LocalContact " + lastName + "/" + firstNameLetter);
				}
			} else {
				LOG.debug("Local contact is empty.");
			}
		}
		return beamLineOperatorEmail;
	}
}
