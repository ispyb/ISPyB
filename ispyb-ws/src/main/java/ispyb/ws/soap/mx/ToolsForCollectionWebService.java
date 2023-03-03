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

package ispyb.ws.soap.mx;

import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ws.api.annotation.WebContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ispyb.common.util.Constants;
import ispyb.common.util.SendMailUtils;
import ispyb.common.util.StringUtils;
import ispyb.common.util.beamlines.ESRFBeamlineEnum;
import ispyb.server.biosaxs.services.core.structure.Structure3Service;
import ispyb.server.biosaxs.vos.assembly.Structure3VO;
import ispyb.server.common.services.proposals.LabContact3Service;
import ispyb.server.common.services.proposals.Person3Service;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.services.robot.RobotAction3Service;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.services.shipping.Dewar3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.LabContact3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.robot.RobotAction3VO;
import ispyb.server.common.vos.robot.RobotActionWS3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.server.mx.services.collections.BeamLineSetup3Service;
import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.server.mx.services.collections.DataCollectionGroup3Service;
import ispyb.server.mx.services.collections.Detector3Service;
import ispyb.server.mx.services.collections.EnergyScan3Service;
import ispyb.server.mx.services.collections.GridInfo3Service;
import ispyb.server.mx.services.collections.Image3Service;
import ispyb.server.mx.services.collections.Position3Service;
import ispyb.server.mx.services.collections.Workflow3Service;
import ispyb.server.mx.services.collections.WorkflowMesh3Service;
import ispyb.server.mx.services.collections.XFEFluorescenceSpectrum3Service;
import ispyb.server.mx.services.collections.workflowStep.WorkflowStep3Service;
import ispyb.server.mx.services.sample.BLSample3Service;
import ispyb.server.mx.services.sample.BLSubSample3Service;
import ispyb.server.mx.services.screening.ScreeningStrategySubWedge3Service;
import ispyb.server.mx.vos.collections.BeamLineSetup3VO;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
import ispyb.server.mx.vos.collections.DataCollectionGroupWS3VO;
import ispyb.server.mx.vos.collections.DataCollectionInfo;
import ispyb.server.mx.vos.collections.DataCollectionPosition;
import ispyb.server.mx.vos.collections.DataCollectionWS3VO;
import ispyb.server.mx.vos.collections.Detector3VO;
import ispyb.server.mx.vos.collections.EnergyScan3VO;
import ispyb.server.mx.vos.collections.EnergyScanWS3VO;
import ispyb.server.mx.vos.collections.GridInfo3VO;
import ispyb.server.mx.vos.collections.GridInfoWS3VO;
import ispyb.server.mx.vos.collections.Image3VO;
import ispyb.server.mx.vos.collections.ImageCreation;
import ispyb.server.mx.vos.collections.ImagePosition;
import ispyb.server.mx.vos.collections.ImageWS3VO;
import ispyb.server.mx.vos.collections.Position3VO;
import ispyb.server.mx.vos.collections.PositionWS3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.collections.SessionWS3VO;
import ispyb.server.mx.vos.collections.Workflow3VO;
import ispyb.server.mx.vos.collections.WorkflowMesh3VO;
import ispyb.server.mx.vos.collections.WorkflowMeshWS3VO;
import ispyb.server.mx.vos.collections.WorkflowStep3VO;
import ispyb.server.mx.vos.collections.XDSInfo;
import ispyb.server.mx.vos.collections.XFEFluorescenceSpectrum3VO;
import ispyb.server.mx.vos.collections.XFEFluorescenceSpectrumWS3VO;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.BLSampleWS3VO;
import ispyb.server.mx.vos.sample.BLSubSample3VO;
import ispyb.server.mx.vos.screening.ScreeningStrategySubWedge3VO;
import ispyb.server.smis.UpdateFromSMIS;

/**
 * Web services for Collection
 * 
 * @author BODIN
 * 
 */

@WebService(name = "ToolsForCollectionWebService", serviceName = "ispybWS", targetNamespace = "http://ispyb.ejb3.webservices.collection")
@SOAPBinding(style = Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Stateless
@RolesAllowed({ "WebService", "User", "Industrial" })
// allow special access roles
@SecurityDomain("ispyb")
@WebContext(authMethod = "BASIC", secureWSDLAccess = false, transportGuarantee = "NONE")
public class ToolsForCollectionWebService {
	private final static Logger LOG = Logger.getLogger(ToolsForCollectionWebService.class);

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private final Integer errorCodeFK = null;// -1;

	@WebMethod(operationName = "echo")
	@WebResult(name = "echo")
	public String echo()  {
		return "echo from server...";
	}

	/**
	 * 
	 * @param beamLineSetup
	 * @return
	 * @throws Exception
	 */
	@WebMethod
	@WebResult(name = "beamLineSetupId")
	public Integer storeOrUpdateBeamLineSetup(@WebParam(name = "beamLineSetup")
	BeamLineSetup3VO beamLineSetup) throws Exception {
		try {
			LOG.debug("storeOrUpdateBeamLineSetup");
			BeamLineSetup3Service blsetupService = (BeamLineSetup3Service) ejb3ServiceLocator
					.getLocalService(BeamLineSetup3Service.class);

			if (beamLineSetup != null) {
				Integer beamLineSetupId = beamLineSetup.getBeamLineSetupId();
				BeamLineSetup3VO vo = null;
				if (beamLineSetupId == null || beamLineSetupId == 0) {
					beamLineSetup.setBeamLineSetupId(null);
					vo = blsetupService.create(beamLineSetup);
					beamLineSetupId = vo.getBeamLineSetupId();
					LOG.debug("BeamLineSetup created " + beamLineSetupId);
				} else {
					vo = blsetupService.update(beamLineSetup);
					LOG.debug("BeamLineSetup updated " + beamLineSetupId);
				}
				return vo.getBeamLineSetupId();
			}
			return null;
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateBeamLineSetup - " + StringUtils.getCurrentDate() + " - " + beamLineSetup.toWSString());
			throw e;
		}
	}

	/**
	 * returns the session for a proposal Code and Number and with the endDate > today or null
	 * 
	 * @param code
	 * @param number
	 * @return
	 * @throws Exception
	 */
	@WebMethod
	@WebResult(name = "Sessions")
	public SessionWS3VO[] findSessionsByProposalAndBeamLine(@WebParam(name = "code")
	String code, @WebParam(name = "number")
	String number, @WebParam(name = "beamLineName")
	String beamLineName) throws Exception {

		try {
			LOG.debug("findSessionsByProposalAndBeamLine : code= " + code + ", number= " + number + ", beamlineName= " + beamLineName);
			long startTime = System.currentTimeMillis();
			Session3Service sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);

			SessionWS3VO[] ret = sessionService.findForWSByProposalCodeAndNumber(StringUtils.getProposalCode(code), number,
					beamLineName);
			if (Constants.SITE_IS_ESRF() || Constants.SITE_IS_SOLEIL()) {
				if (ret == null || ret.length <1){
					//no sessions found, try to update DB				
					LOG.debug("findSessionsByProposalAndBeamLine : no sessions found, try to update from SMIS ") ;
					UpdateFromSMIS.updateProposalFromSMIS(code, number);
					ret = sessionService.findForWSByProposalCodeAndNumber(StringUtils.getProposalCode(code), number,
						beamLineName);
				}
			}

			long endTime = System.currentTimeMillis();
			long duration = endTime - startTime;
			LOG.debug("findSessionsByProposalAndBeamLine : code= " + code + ", number= " + number + ", beamlineName= " + beamLineName
					+ " time = " + duration + " ms");
			return ret;
		} catch (Exception e) {
			LOG.error("WS ERROR: findSessionsByProposalAndBeamLine - " + StringUtils.getCurrentDate() + " - " + code + ", " + number
					+ ", " + beamLineName);
			throw e;
		}
	}
	
	/**
	 * returns the sessions  to be protected afetr a dealy and in a particular window
	 * this WS is only used for tests on ESRF site
	 * 
	 * @param delay
	 * @param window
	 * @return
	 * @throws Exception
	 */
	@WebMethod
	@WebResult(name = "Sessions")
	public SessionWS3VO[] findSessionsToBeProtected(@WebParam(name = "delay") Integer delay, 
			@WebParam(name = "window") Integer window) throws Exception {

		try {
			LOG.debug("findSessionsToBeProtected( : delay= " + delay + ", window= " + window);
			long startTime = System.currentTimeMillis();
			Session3Service sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);

			SessionWS3VO[] ret = sessionService.findForWSToBeProtected(delay, window);
			
			if (ret == null || ret.length <1) {			
					LOG.debug("findSessionsToBeProtected no sessions found ") ;					
			}

			long endTime = System.currentTimeMillis();
			long duration = endTime - startTime;
			LOG.debug("findSessionsToBeProtected(delay= " + delay + ", window= " + window
					+ " time = " + duration + " ms");
			return ret;
			
		} catch (Exception e) {
			LOG.error("WS ERROR: findSessionsToBeProtected - " + StringUtils.getCurrentDate() + " - " + delay + ", " + window);
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "session")
	public SessionWS3VO findSession(@WebParam(name = "sessionId")
	Integer sessionId) throws Exception {
		try {
			LOG.debug("findSession : id= " + sessionId);
			long startTime = System.currentTimeMillis();
			Session3Service sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);

			SessionWS3VO session = sessionService.findForWSByPk(sessionId, false, false, false);
			long endTime = System.currentTimeMillis();
			long duration = endTime - startTime;
			LOG.debug("findSession : id= " + sessionId + " time = " + duration + " ms");

			return session;
		} catch (Exception e) {
			LOG.error("WS ERROR: findSession - " + StringUtils.getCurrentDate() + " - " + sessionId);
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "sessionId")
	public Integer storeOrUpdateSession(@WebParam(name = "session")
	SessionWS3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdateSession");
			// if vo is null we return null, no creation
			if (vo == null)
				return null;

			// if proposalId is null we return null, no creation of Session
			if (vo.getProposalId() == null || vo.getProposalId() <= 0) {

				LOG.debug(" WS PB : ProposalId is null, session not stored");
				return errorCodeFK;

			}

			Session3VO sessionValue = null;
			Session3Service sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);

			Integer sessionId = vo.getSessionId();
			BeamLineSetup3Service beamLineSetupService = (BeamLineSetup3Service) ejb3ServiceLocator
					.getLocalService(BeamLineSetup3Service.class);
			BeamLineSetup3VO beamLineSetup = null;
			if (vo.getBeamLineSetupId() != null && vo.getBeamLineSetupId() > 0)
				beamLineSetup = beamLineSetupService.findByPk(vo.getBeamLineSetupId());
			Proposal3Service proposalService = (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);
			Proposal3VO proposal = null;
			if (vo.getProposalId() != null && vo.getProposalId() > 0)
				proposal = proposalService.findByPk(vo.getProposalId());

			Session3VO session = new Session3VO();
			// load the object elsewhere there is an error with the childs
			if (sessionId != null && sessionId > 0) {
				session = sessionService.findByPk(sessionId, false, false, false);
			}
			session.fillVOFromWS(vo);
			session.setBeamLineSetupVO(beamLineSetup);
			session.setProposalVO(proposal);

			session.setTimeStamp(StringUtils.getCurrentTimeStamp());

			if (sessionId == null || sessionId == 0) {
				session.setSessionId(null);
				Integer nbShifts = 3;
				
				// if dates sent by BCM then take them
				if (vo.getStartDate() != null  && vo.getEndDate() != null) {
					LOG.debug("Session to be created with : startdate = " + vo.getStartDate() + " enddate = " + vo.getEndDate());
					session.setStartDate(vo.getStartDate());
					session.setEndDate(vo.getEndDate());
					if (vo.getNbShifts() != null) 
						session.setNbShifts(nbShifts);
					else {
						nbShifts = (int) (vo.getEndDate().getTime() - vo.getStartDate().getTime())/(1000*60*60*8 );
						session.setNbShifts(nbShifts);
					}
				}
				// if only start date sent by BCM then take it and put 3 shifts
				else if (vo.getStartDate() != null ) {	
					LOG.debug("Session to be created with : startdate = " + vo.getStartDate() + " enddate = null ");
						session.setStartDate(vo.getStartDate());
						Calendar endDateCal = Calendar.getInstance();
						Integer daysToAdd = 1;
						endDateCal.setTime(vo.getStartDate());
						endDateCal.add(Calendar.DATE, daysToAdd);	
						Timestamp endDate = new Timestamp(endDateCal.getTimeInMillis());
						nbShifts = 3;
						session.setEndDate(endDate);	
						session.setNbShifts(nbShifts);
				}	
				else {
					// force startDate, endDate and nbShifts=3
					Calendar startDateCal = Calendar.getInstance();
					Timestamp startDate = new Timestamp(startDateCal.getTimeInMillis());
					if (Constants.SITE_IS_MAXIV()) {
						startDateCal.set(Calendar.HOUR_OF_DAY, 8);
						startDateCal.set(Calendar.MINUTE, 0);
						startDateCal.set(Calendar.SECOND, 0);
						startDate = new Timestamp(startDateCal.getTimeInMillis());
					}
					session.setStartDate(startDate);
					nbShifts = 3;
					session.setNbShifts(nbShifts);
					// Integer daysToAdd = nbShifts / 3 + 1;
					Integer daysToAdd = 1;
					Calendar endDateCal = Calendar.getInstance();
					endDateCal.setTime(startDate);
					endDateCal.add(Calendar.DATE, daysToAdd);
					Timestamp endDate = new Timestamp(endDateCal.getTimeInMillis());
					if (Constants.SITE_IS_MAXIV()) {
						endDateCal.set(Calendar.HOUR_OF_DAY, 7);
						endDateCal.set(Calendar.MINUTE, 59);
						endDateCal.set(Calendar.SECOND, 59);
						endDate = new Timestamp(endDateCal.getTimeInMillis());
					}
					session.setEndDate(endDate);	
				}

				session.setLastUpdate(session.getEndDate());
				sessionValue = sessionService.create(session);
				sessionId = sessionValue.getSessionId();
				LOG.debug("Session created: " + sessionId + " startdate = " + sessionValue.getStartDate() + " enddate = " + sessionValue.getEndDate());
			} else {
				sessionValue = sessionService.update(session);
				LOG.debug("Session updated " + sessionId);
			}
			return sessionValue.getSessionId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateSession - " + StringUtils.getCurrentDate() + " - " + vo.toWSString());
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "dataCollectionId")
	public Integer storeOrUpdateDataCollection(@WebParam(name = "dataCollection")
	DataCollectionWS3VO vo) throws Exception {
		try {			
			LOG.debug("storeOrUpdateDataCollection");
			// if vo is null we return null, no creation
			if (vo == null)
				return null;

			// if datacollectionGroupId is null we return null, no creation of dataCollection
			if (vo.getDataCollectionGroupId() == null || vo.getDataCollectionGroupId() <= 0) {

				LOG.debug(" WS PB : datacollectionGroupId is null, dc not stored");
				return errorCodeFK;

			}

			LOG.debug(" storeOrUpdateDataCollection with gaps " + vo.getUndulatorGap1() + ", " + vo.getUndulatorGap2() + ", "
					+ vo.getUndulatorGap3());

			DataCollection3VO dataCollectionValue = null;
			DataCollection3Service dataCollectionService = (DataCollection3Service) ejb3ServiceLocator
					.getLocalService(DataCollection3Service.class);

			Integer dataCollectionId = vo.getDataCollectionId();

			DataCollectionGroup3Service dataCollectionGroupService = (DataCollectionGroup3Service) ejb3ServiceLocator
					.getLocalService(DataCollectionGroup3Service.class);
			DataCollectionGroup3VO dataCollectionGroupVO = null;
			if (vo.getDataCollectionGroupId() != null && vo.getDataCollectionGroupId() > 0)
				dataCollectionGroupVO = dataCollectionGroupService.findByPk(vo.getDataCollectionGroupId(), false, false);

			ScreeningStrategySubWedge3Service screeningStrategySubWedgeService = (ScreeningStrategySubWedge3Service) ejb3ServiceLocator
					.getLocalService(ScreeningStrategySubWedge3Service.class);
			ScreeningStrategySubWedge3VO strategySubWedgeOrigVO = null;
			if (vo.getStrategySubWedgeOrigId() != null && vo.getStrategySubWedgeOrigId() > 0)
				strategySubWedgeOrigVO = screeningStrategySubWedgeService.findByPk(vo.getStrategySubWedgeOrigId());

			Detector3Service detectorService = (Detector3Service) ejb3ServiceLocator.getLocalService(Detector3Service.class);
			Detector3VO detectorVO = null;
			if (vo.getDetectorId() != null && vo.getDetectorId() > 0)
				detectorVO = detectorService.findByPk(vo.getDetectorId());

			BLSubSample3Service blSubSampleService = (BLSubSample3Service) ejb3ServiceLocator
					.getLocalService(BLSubSample3Service.class);
			BLSubSample3VO blSubSampleVO = null;
			if (vo.getBlSubSampleId() != null && vo.getBlSubSampleId() > 0)
				blSubSampleVO = blSubSampleService.findByPk(vo.getBlSubSampleId());

			DataCollection3VO dataCollection = new DataCollection3VO();

			if (dataCollectionId != null && dataCollectionId > 0) {

				// load the object elsewhere there is an error with the childs
				dataCollection = dataCollectionService.findByPk(dataCollectionId, false, false);
			}
			dataCollection.fillVOFromWS(vo);
			dataCollection.setDataCollectionGroupVO(dataCollectionGroupVO);
			dataCollection.setStrategySubWedgeOrigVO(strategySubWedgeOrigVO);
			dataCollection.setDetectorVO(detectorVO);
			dataCollection.setBlSubSampleVO(blSubSampleVO);
			// printableForReport default 1
			if (dataCollection.getPrintableForReport() == null)
				dataCollection.setPrintableForReport(new Byte("1"));
			if (dataCollection.getNumberOfImages() == null || dataCollection.getNumberOfImages() < 0 || dataCollection.getNumberOfImages() > 1000000000) {
				LOG.debug("WS STORE OR UPDATE DATACOLLECTION with dataCollectionId='" + dataCollectionId + "' wrong numberOfImages='"
						+ dataCollection.getNumberOfImages() + "'");
				dataCollection.setNumberOfImages(0);
			}
			// Issue 1511: dates set by ispyb
			if (dataCollectionId == null || dataCollectionId == 0) {
				// creation: startTime = endTime= currentTime
				dataCollection.setStartTime(StringUtils.getCurrentTimeStamp());
				dataCollection.setEndTime(StringUtils.getCurrentTimeStamp());
			} else {
				// update: endTime = currentTime
				dataCollection.setEndTime(StringUtils.getCurrentTimeStamp());
			}

			dataCollection.checkValues(dataCollectionId == null || dataCollectionId == 0);

			if (dataCollectionId == null || dataCollectionId == 0) {
				dataCollection.setDataCollectionId(null);
				dataCollectionValue = dataCollectionService.create(dataCollection);
				dataCollectionId = dataCollectionValue.getDataCollectionId();
				LOG.debug("created DataCollection : " + dataCollectionId);
			} else {
				dataCollectionValue = dataCollectionService.update(dataCollection);
				LOG.debug("Updated DataCollection : " + dataCollectionId);
			}
			// update session
			updateSession(dataCollection.getDataCollectionGroupVO().getSessionVO());
			return dataCollectionId;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("WS ERROR: storeOrUpdateDataCollection - " + StringUtils.getCurrentDate() + " - " + vo.toWSString());
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "dataCollectionId")
	public Integer updateDataCollectionStatus(@WebParam(name = "dataCollectionId") Integer dataCollectionId, 
			@WebParam(name = "runStatus") String runStatus) throws Exception {
		try {
			LOG.debug("updateDataCollectionStatus");
			DataCollection3Service dataCollectionService = (DataCollection3Service) ejb3ServiceLocator
					.getLocalService(DataCollection3Service.class);
			if (dataCollectionId != null && dataCollectionId > 0) {
				DataCollection3VO dataCollection = dataCollectionService.findByPk(dataCollectionId, false, false);
				if (dataCollection != null) {
					dataCollection.setRunStatus(runStatus);
					dataCollection.setEndTime(StringUtils.getCurrentTimeStamp());
					dataCollection = dataCollectionService.update(dataCollection);
					LOG.debug("updateDataCollectionStatus done");
					return dataCollection.getDataCollectionId();
				} else {
					LOG.debug(" WS PB : updateDataCollectionStatus dataCollection not found: " + dataCollectionId);
					return -1;
				}
			} else {
				LOG.debug(" WS PB : updateDataCollectionStatus dataCollectionId: " + dataCollectionId);
				return -1;
			}
		} catch (Exception e) {
			LOG.error("WS ERROR: updateDataCollectionStatus - " + StringUtils.getCurrentDate() + " - " + dataCollectionId + ", "
					+ runStatus);
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "dataCollection")
	public DataCollectionWS3VO findDataCollectionFromFileLocationAndFileName(@WebParam(name = "fileLocation")
	String fileLocation, @WebParam(name = "fileName")
	String fileName) throws Exception {

		try {
			LOG.debug("findDataCollectionFromFileLocationAndFileName");
			DataCollectionWS3VO dc = null;
			DataCollection3Service dataCollectionService = (DataCollection3Service) ejb3ServiceLocator
					.getLocalService(DataCollection3Service.class);
			dc = dataCollectionService.findForWSDataCollectionIdFromFileLocationAndFileName(fileLocation, fileName);
			if (dc == null) {
				// try to add or remove the last / -- it seems that there is no rule with this.
				if (fileLocation != null && fileLocation.length() > 1) {
					char lastCharact = fileLocation.charAt(fileLocation.length() - 1);
					if (lastCharact == '/') {
						dc = dataCollectionService.findForWSDataCollectionIdFromFileLocationAndFileName(
								fileLocation.substring(0, fileLocation.length() - 1), fileName);
						return dc;
					} else {
						dc = dataCollectionService.findForWSDataCollectionIdFromFileLocationAndFileName(fileLocation + "/", fileName);
						return dc;
					}
				}
			}
			return dc;
		} catch (Exception e) {
			LOG.error("WS ERROR: findDataCollectionFromFileLocationAndFileName - " + StringUtils.getCurrentDate() + " - "
					+ fileLocation + ", " + fileName);
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "dataCollection")
	public DataCollectionWS3VO findDataCollectionFromImageDirectoryAndImagePrefixAndNumber(@WebParam(name = "imageDirectory")
	String imageDirectory, @WebParam(name = "imagePrefix")
	String imagePrefix, @WebParam(name = "dataCollectionNumber")
	Integer dataCollectionNumber) throws Exception {
		try {
			LOG.debug("findDataCollectionFromImageDirectoryAndImagePrefixAndNumber");
			DataCollectionWS3VO dataCollectionValue = null;
			DataCollection3Service dataCollectionService = (DataCollection3Service) ejb3ServiceLocator
					.getLocalService(DataCollection3Service.class);

			dataCollectionValue = dataCollectionService.findForWSDataCollectionFromImageDirectoryAndImagePrefixAndNumber(
					imageDirectory, imagePrefix, dataCollectionNumber);
			return dataCollectionValue;
		} catch (Exception e) {
			LOG.error("WS ERROR: findDataCollectionFromImageDirectoryAndImagePrefixAndNumber - " + StringUtils.getCurrentDate()
					+ " - " + imageDirectory + ", " + imagePrefix + ", " + dataCollectionNumber);
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "dataCollectionGroup")
	public DataCollectionGroupWS3VO findDataCollectionGroup(@WebParam(name = "dataCollectionGroupId")
	Integer dataCollectionGroupId) throws Exception {
		try {
			LOG.debug("findDataCollectionGroup");
			DataCollectionGroupWS3VO dataCollectionGroupValue = null;
			DataCollectionGroup3Service dataCollectionGroupService = (DataCollectionGroup3Service) ejb3ServiceLocator
					.getLocalService(DataCollectionGroup3Service.class);

			dataCollectionGroupValue = dataCollectionGroupService.findForWSByPk(dataCollectionGroupId, false, false);
			if (dataCollectionGroupValue != null)
				LOG.debug("findDataCollectionGroup found :" + dataCollectionGroupId);
			return dataCollectionGroupValue;
		} catch (Exception e) {
			LOG.error("WS ERROR: findDataCollectionGroup - " + StringUtils.getCurrentDate() + " - " + dataCollectionGroupId);
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "dataCollectionGroupId")
	public Integer storeOrUpdateDataCollectionGroup(@WebParam(name = "dataCollectionGroup")
	DataCollectionGroupWS3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdateDataCollectionGroup");
			// if vo is null we return null, no creation
			if (vo == null)
				return null;

			// if sessionId is null we return null, no creation of dataCollectionGroup
			if (vo.getSessionId() == null || vo.getSessionId() <= 0) {

				LOG.debug(" WS PB : sessionId is null, dcGroup not stored");
				return null;

			}
			LOG.debug("...with dataCollectionGroupId = " + vo.getDataCollectionGroupId());
			LOG.debug("...with sampleId = " + vo.getBlSampleId());
			DataCollectionGroup3VO dataCollectionGroupValue = null;
			DataCollectionGroup3Service dataCollectionGroupService = (DataCollectionGroup3Service) ejb3ServiceLocator
					.getLocalService(DataCollectionGroup3Service.class);

			Integer dataCollectionGroupId = vo.getDataCollectionGroupId();

			BLSample3Service blSampleService = (BLSample3Service) ejb3ServiceLocator.getLocalService(BLSample3Service.class);
			BLSample3VO blSampleVO = null;
			if (vo.getBlSampleId() != null && vo.getBlSampleId() > 0)
				blSampleVO = blSampleService.findByPk(vo.getBlSampleId(), false, false, false);

			Session3Service sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
			Session3VO sessionVO = null;
			if (vo.getSessionId() != null && vo.getSessionId() > 0)
				sessionVO = sessionService.findByPk(vo.getSessionId(), true, false, false);

			Workflow3Service workflowService = (Workflow3Service) ejb3ServiceLocator.getLocalService(Workflow3Service.class);
			Workflow3VO workflowVO = null;
			if (vo.getWorkflowId() != null && vo.getWorkflowId() > 0)
				workflowVO = workflowService.findByPk(vo.getWorkflowId());

			DataCollectionGroup3VO dataCollectionGroup = new DataCollectionGroup3VO();
			String oldComments = "";
			Workflow3VO oldWorkflow = null;
			// load the object elsewhere there is an error with the childs
			if (dataCollectionGroupId != null && dataCollectionGroupId > 0) {
				dataCollectionGroup = dataCollectionGroupService.findByPk(dataCollectionGroupId, false, false);
				oldComments = dataCollectionGroup.getComments();
				oldWorkflow = dataCollectionGroup.getWorkflowVO();
			}
			dataCollectionGroup.fillVOFromWS(vo);
			dataCollectionGroup.setComments(oldComments + (vo.getComments() == null ? "" : " " + vo.getComments()));
			dataCollectionGroup.setBlSampleVO(blSampleVO);
			dataCollectionGroup.setSessionVO(sessionVO);
			// The workflow is updated by EDNA (updateDataCollectionGroupWorkflowId), it's not filled by mxcube, so we don't replace
			// the worklfow with the mxcube value
			dataCollectionGroup.setWorkflowVO(oldWorkflow == null ? workflowVO : oldWorkflow);

			if (dataCollectionGroup.getActualSampleSlotInContainer() != null
					&& (dataCollectionGroup.getActualSampleSlotInContainer() < 0 || dataCollectionGroup
							.getActualSampleSlotInContainer() > 1000000000)) {
				LOG.debug("WS STORE OR UPDATE DATACOLLECTIONGROUP with dataCollectionGroupId='" + dataCollectionGroupId
						+ "' wrong actualSampleSlotInContainer='" + dataCollectionGroup.getActualSampleSlotInContainer() + "'");
				dataCollectionGroup.setActualSampleSlotInContainer(0);
			}
			if (dataCollectionGroup.getActualContainerSlotInSC() != null
					&& (dataCollectionGroup.getActualContainerSlotInSC() < 0 || dataCollectionGroup.getActualContainerSlotInSC() > 1000000000)) {
				LOG.debug("WS STORE OR UPDATE DATACOLLECTIONGROUP with dataCollectionGroupId='" + dataCollectionGroupId
						+ "' wrong actualContainerSlotInSC='" + dataCollectionGroup.getActualContainerSlotInSC() + "'");
				dataCollectionGroup.setActualContainerSlotInSC(0);
			}
			if (dataCollectionGroup.getExperimentType() == null) {
				LOG.debug("WS STORE OR UPDATE DATACOLLECTIONGROUP with dataCollectionGroupId='" + dataCollectionGroupId
						+ "' experimentType is null ");
				return null;
			}
			// Issue 1511: dates set by ispyb
			if (dataCollectionGroupId == null || dataCollectionGroupId == 0) {
				// creation: startTime = endTime= currentTime
				dataCollectionGroup.setStartTime(StringUtils.getCurrentTimeStamp());
				dataCollectionGroup.setEndTime(StringUtils.getCurrentTimeStamp());
			} else {
				// update: endTime = currentTime
				dataCollectionGroup.setEndTime(StringUtils.getCurrentTimeStamp());
			}

			dataCollectionGroup.checkValues(dataCollectionGroupId == null || dataCollectionGroupId == 0);

			if (dataCollectionGroupId == null || dataCollectionGroupId == 0) {
				dataCollectionGroup.setDataCollectionGroupId(null);
				dataCollectionGroupValue = dataCollectionGroupService.create(dataCollectionGroup);
				dataCollectionGroupId = dataCollectionGroupValue.getDataCollectionGroupId();
				
				LOG.debug("create DataCollectionGroup : " + dataCollectionGroupId);
				// Issue 1728: email notification for ID30
				if ((Constants.SITE_IS_ESRF() && ESRFBeamlineEnum.isBeamlineEmailNotification(dataCollectionGroup.getSessionVO()
						.getBeamlineName())) || Constants.SITE_IS_SOLEIL()) {
					
					Session3VO ses = sessionService.findByPk(vo.getSessionId(), true /*withDataCollectionGroup*/, true/*withEnergyScan*/, true /*withXFESpectrum*/);
					
					if (ses.getDataCollectionGroupsList() == null || 
							ses.getDataCollectionGroupsList().size() == 1) {
						
						Proposal3VO proposal = dataCollectionGroup.getSessionVO().getProposalVO();
						String beamline = dataCollectionGroup.getSessionVO().getBeamlineName();
						SimpleDateFormat dateStandard = new SimpleDateFormat(Constants.DATE_FORMAT);
						String experimentDateFormated = dateStandard.format(dataCollectionGroup.getSessionVO().getStartDate());
						Shipping3VO shipping = null;
						Dewar3VO dewar = null;
						if (dataCollectionGroup.getBlSampleVO() != null
								&& dataCollectionGroup.getBlSampleVO().getContainerVO() != null
								&& dataCollectionGroup.getBlSampleVO().getContainerVO().getDewarVO() != null) {
							dewar = dataCollectionGroup.getBlSampleVO().getContainerVO().getDewarVO();
						} else {
							Dewar3Service dewarService = (Dewar3Service) ejb3ServiceLocator.getLocalService(Dewar3Service.class);
							List<Dewar3VO> listDewar = dewarService.findByExperiment(
									dataCollectionGroup.getSessionVO().getSessionId(), Constants.DEWAR_STATUS_PROCESS);
							if (listDewar != null && listDewar.size() > 0) {
								dewar = listDewar.get(0);
							}
						}
						if (dewar != null) {
							shipping = dewar.getShippingVO();
						}
						String sendingLabContact = "";
						if (shipping != null && shipping.getSendingLabContactVO() != null) {
							sendingLabContact = shipping.getSendingLabContactVO().getCardName();
						}
						String returnLabContact = "";
						if (shipping != null && shipping.getReturnLabContactVO() != null) {
							returnLabContact = shipping.getReturnLabContactVO().getCardName();
						}

						SimpleDateFormat dateTimeStandard = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
						String collectTime = dateTimeStandard.format(dataCollectionGroup.getStartTime());

						// mail
						String from = Constants.getProperty("mail.from");
						String to = "";
						if (shipping != null) {
							Person3Service person3Service = (Person3Service) ejb3ServiceLocator.getLocalService(Person3Service.class);
							LabContact3Service labContact3Service = (LabContact3Service) ejb3ServiceLocator
									.getLocalService(LabContact3Service.class);
							LabContact3VO labContactS = labContact3Service.findByPk(shipping.getSendingLabContactId());
							if (labContactS != null)
								to = person3Service.findByPk(labContactS.getPersonVOId()).getEmailAddress();
							LabContact3VO labContactR = labContact3Service.findByPk(shipping.getReturnLabContactId());
							if (labContactS != null && labContactR != null
									&& !labContactS.getLabContactId().equals(labContactR.getLabContactId()))
								to += ", " + person3Service.findByPk(labContactR.getPersonVOId()).getEmailAddress();
						}
						LOG.debug("test Create first collection on " + beamline + " : sendMail to " + to);
						// to = Constants.getProperty("mail.notification.collect.cc");
						String cc = Constants.getProperty("mail.notification.collect.cc");
						String subject = proposal == null ? "" : (proposal.getCode() + proposal.getNumber() + ": ");
						subject += "Collect started on " + beamline + "";
						String body = "Shipment name: " + (shipping == null ? "" : shipping.getShippingName()) + "\n"
								+ "Sending LabContact: " + sendingLabContact + "\n" + "Return LabContact: " + returnLabContact + "\n"
								+ "Proposal number: " + (proposal == null ? "" : (proposal.getCode() + proposal.getNumber())) + "\n"
								+ "Exp. date: " + experimentDateFormated + " & Beamline:" + beamline + "\n"
								+ "---------------------------------------------------\n" + "Collect started at " + collectTime + "\n";

						if (to != "")
							SendMailUtils.sendMail(from, to, cc, subject, body);
						LOG.debug("Create first collection on " + beamline + " : sendMail to " + to);
					}
				}
			} else {
				dataCollectionGroupValue = dataCollectionGroupService.update(dataCollectionGroup);
				LOG.debug("update DataCollectionGroup : " + dataCollectionGroupId);
			}

			// update session
			updateSession(dataCollectionGroup.getSessionVO());

			return dataCollectionGroupId;

		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateDataCollectionGroup - " + StringUtils.getCurrentDate() + " - " + vo.toWSString());
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "dataCollection")
	public DataCollectionWS3VO findDataCollection(@WebParam(name = "dataCollectionId")
	Integer dataCollectionId) throws Exception {
		try {
			LOG.debug("findDataCollection");
			DataCollectionWS3VO dataCollectionValue = null;
			DataCollection3Service dataCollectionService = (DataCollection3Service) ejb3ServiceLocator
					.getLocalService(DataCollection3Service.class);

			dataCollectionValue = dataCollectionService.findForWSByPk(dataCollectionId, false, false);
			if (dataCollectionValue != null)
				LOG.debug("findDataCollection found :" + dataCollectionId);
			return dataCollectionValue;
		} catch (Exception e) {
			LOG.error("WS ERROR: findDataCollection - " + StringUtils.getCurrentDate() + " - " + dataCollectionId);
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "xfeFluorescenceSpectrumId")
	public Integer storeOrUpdateXFEFluorescenceSpectrum(@WebParam(name = "xfeFluorescenceSpectrum")
	XFEFluorescenceSpectrumWS3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdateXFEFluorescenceSpectrum ");
			// if vo is null we return null, no creation
			if (vo == null) {
				LOG.debug("storeOrUpdateXFEFluorescenceSpectrum : NULL ");
				return null;

			}

			// if sessionId is null we return null, no creation of dataCollectionGroup
			if (vo.getSessionId() == null || vo.getSessionId() <= 0) {

				LOG.debug(" WS PB : sessionId is null, XFE not stored");
				return errorCodeFK;

			}

			XFEFluorescenceSpectrum3VO xfeFluorescenceSpectrumValue = null;
			XFEFluorescenceSpectrum3Service xfeFluorescenceSpectrumService = (XFEFluorescenceSpectrum3Service) ejb3ServiceLocator
					.getLocalService(XFEFluorescenceSpectrum3Service.class);

			Integer xfeFluorescenceSpectrumId = vo.getXfeFluorescenceSpectrumId();

			BLSample3Service blSampleService = (BLSample3Service) ejb3ServiceLocator.getLocalService(BLSample3Service.class);
			BLSample3VO blSampleVO = null;
			LOG.debug(" with sample " + vo.getBlSampleId());
			if (vo.getBlSampleId() != null && vo.getBlSampleId() > 0)
				blSampleVO = blSampleService.findByPk(vo.getBlSampleId(), false, false, false);

			Session3Service sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
			Session3VO sessionVO = null;
			if (vo.getSessionId() != null && vo.getSessionId() > 0)
				sessionVO = sessionService.findByPk(vo.getSessionId(), false, false, false);

			XFEFluorescenceSpectrum3VO xfe = new XFEFluorescenceSpectrum3VO();
			if (xfeFluorescenceSpectrumId != null && xfeFluorescenceSpectrumId > 0) {
				// load the object elsewhere there is an error with the childs
				xfe = xfeFluorescenceSpectrumService.findByPk(xfeFluorescenceSpectrumId);
			}
			xfe.fillVOFromWS(vo);
			xfe.setBlSampleVO(blSampleVO);
			xfe.setSessionVO(sessionVO);

			// Issue 1511: dates set by ispyb
			if (xfeFluorescenceSpectrumId == null || xfeFluorescenceSpectrumId == 0) {
				// creation: startTime = endTime= currentTime
				xfe.setStartTime(StringUtils.getCurrentTimeStamp());
				xfe.setEndTime(StringUtils.getCurrentTimeStamp());
			} else {
				// update: endTime = currentTime
				xfe.setEndTime(StringUtils.getCurrentTimeStamp());
			}

			if (xfeFluorescenceSpectrumId == null || xfeFluorescenceSpectrumId == 0) {
				xfe.setXfeFluorescenceSpectrumId(null);
				xfeFluorescenceSpectrumValue = xfeFluorescenceSpectrumService.create(xfe);
			} else {
				xfeFluorescenceSpectrumValue = xfeFluorescenceSpectrumService.update(xfe);
			}
			xfeFluorescenceSpectrumId = xfeFluorescenceSpectrumValue.getXfeFluorescenceSpectrumId();
			LOG.debug("storeOrUpdateXFEFluorescenceSpectrum " + xfeFluorescenceSpectrumId);

			// update session
			updateSession(xfe.getSessionVO());

			return xfeFluorescenceSpectrumId;

		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateXFEFluorescenceSpectrum - " + StringUtils.getCurrentDate() + " - " + vo.toWSString());
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "energyScanId")
	public Integer storeOrUpdateEnergyScan(@WebParam(name = "energyScan")
	EnergyScanWS3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdateEnergyScan - " +vo.toWSString());
			// if vo is null we return null, no creation
			if (vo == null)
				return null;

			// if sessionId is null we return null, no creation of dataCollectionGroup
			if (vo.getSessionId() == null || vo.getSessionId() <= 0) {

				LOG.debug(" WS PB : sessionId is null, energyScan not stored");
				return errorCodeFK;
			}

			EnergyScan3VO energyScanValue = null;
			EnergyScan3Service energyScanService = (EnergyScan3Service) ejb3ServiceLocator.getLocalService(EnergyScan3Service.class);

			Integer energyScanId = vo.getEnergyScanId();

			Session3Service sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
			Session3VO sessionVO = null;
			if (vo.getSessionId() != null && vo.getSessionId() > 0) {
				sessionVO = sessionService.findByPk(vo.getSessionId(), false, false, false);
			}
			
			BLSample3Service blSampleService = (BLSample3Service) ejb3ServiceLocator.getLocalService(BLSample3Service.class);
			BLSample3VO blSampleVO = null;
			if (vo.getBlSampleId() != null && vo.getBlSampleId() > 0) {
				blSampleVO = blSampleService.findByPk(vo.getBlSampleId(), false, false, false);
				LOG.debug(" with sample " + blSampleVO.toString());
			}


			EnergyScan3VO energyScan = new EnergyScan3VO();
			// load the object elsewhere there is an error with the childs
			if (energyScanId != null && energyScanId > 0) {
				energyScan = energyScanService.findByPk(energyScanId);
			}
			energyScan.fillVOFromLight(vo);
			energyScan.setSessionVO(sessionVO);
			energyScan.setBlSampleVO(blSampleVO);
			
			// Issue 1511: dates set by ispyb
			if (energyScanId == null || energyScanId == 0) {
				// creation: startTime = endTime= currentTime
				energyScan.setStartTime(StringUtils.getCurrentTimeStamp());
				energyScan.setEndTime(StringUtils.getCurrentTimeStamp());
			} else {
				// update: endTime = currentTime
				energyScan.setEndTime(StringUtils.getCurrentTimeStamp());
			}

			if (energyScanId == null || energyScanId == 0) {
				energyScan.setEnergyScanId(null);
				energyScanValue = energyScanService.create(energyScan);
			} else {
				energyScanValue = energyScanService.update(energyScan);
			}
			energyScanId = energyScanValue.getEnergyScanId();
			LOG.debug("storeOrUpdateEnergyScan " + energyScanId);

			// update session
			updateSession(energyScan.getSessionVO());

			return energyScanId;
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateEnergyScan - " + StringUtils.getCurrentDate() + " - " + vo.toWSString());
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "imageId")
	public Integer storeOrUpdateImage(@WebParam(name = "image")
	ImageWS3VO vo) throws Exception {
		try {
			// LOG.debug("storeOrUpdateImage");
			// if vo is null we return null, no creation
			if (vo == null)
				return null;

			// if datacollectionGroupId is null we return null, no creation of dataCollection
			if (vo.getDataCollectionId() == null || vo.getDataCollectionId() <= 0) {

				LOG.debug(" WS PB : datacollectionId is null, image not stored");
				return errorCodeFK;

			}

			Image3VO imageValue = null;
			Image3Service imageService = (Image3Service) ejb3ServiceLocator.getLocalService(Image3Service.class);

			Integer imageId = vo.getImageId();
			DataCollection3Service dataCollectionService = (DataCollection3Service) ejb3ServiceLocator
					.getLocalService(DataCollection3Service.class);
			DataCollection3VO dataCollectionVO = null;
			dataCollectionVO = dataCollectionService.findByPk(vo.getDataCollectionId(), false, false);

			Image3VO image = new Image3VO();
			image.fillVOFromWS(vo);
			image.setDataCollectionVO(dataCollectionVO);

			image.checkValues(imageId == null || imageId == 0);
			if (imageId == null || imageId == 0) {
				image.setImageId(null);
				imageValue = imageService.create(image);
				// LOG.debug("storeOrUpdateImage : image created " + imageValue.getImageId());
			} else {
				imageValue = imageService.update(image);
				// LOG.debug("storeOrUpdateImage : image updated " + imageValue.getImageId());
			}
			return imageValue.getImageId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateImage - " + StringUtils.getCurrentDate() + " - " + vo.toWSString());
			throw e;
		}
	}

	/**
	 * Returns xds information for a given dataCollectionId
	 * 
	 * @param dataCollectionId
	 * @return
	 * @throws Exception
	 */
	@WebMethod
	@WebResult(name = "XDSInfo")
	public XDSInfo getXDSInfo(@WebParam(name = "dataCollectionId")
	Integer dataCollectionId) throws Exception {
		try {
			LOG.debug("getXDSInfo : dataCollectionId=" + dataCollectionId);
			long startTime = System.currentTimeMillis();
			DataCollection3Service dataCollectionService = (DataCollection3Service) ejb3ServiceLocator
					.getLocalService(DataCollection3Service.class);

			XDSInfo xdsInfo = null;
			xdsInfo = dataCollectionService.findForWSXDSInfo(dataCollectionId);
			long endTime = System.currentTimeMillis();
			long duration = endTime - startTime;
			LOG.debug("getXDSInfo : dataCollectionId=" + dataCollectionId + " time = " + duration + " ms");
			return xdsInfo;
		} catch (Exception e) {
			LOG.error("WS ERROR: getXDSInfo - " + StringUtils.getCurrentDate() + " - " + dataCollectionId);
			throw e;
		}
	}

	/**
	 * Find a detector for the given characteristics
	 * 
	 * @param detectorId
	 * @return
	 */
	@WebMethod
	@WebResult(name = "Detector")
	public Detector3VO findDetectorByParam(@WebParam(name = "detectorType")
	String detectorType, @WebParam(name = "detectorManufacturer")
	String detectorManufacturer, @WebParam(name = "detectorModel")
	String detectorModel, @WebParam(name = "detectorMode")
	String detectorMode) throws Exception {
		try {
			LOG.debug("findDetectorByParam");
			long startTime = System.currentTimeMillis();
			Detector3VO detectorValue = null;
			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Detector3Service detectorService = (Detector3Service) ejb3ServiceLocator.getLocalService(Detector3Service.class);
			// no need to have the detectorType to retrieve the detector,it's even a problem because the
			// detector type in mxcube is not the detectorType defined in ispyb
			detectorValue = detectorService.findDetector(null, detectorManufacturer, detectorModel, detectorMode);
			long endTime = System.currentTimeMillis();
			long duration = endTime - startTime;
			LOG.debug("findDetectorByParam : detectorType=" + detectorType + " ,detectorManufacturer= " + detectorManufacturer
					+ " ,detectorModel= " + detectorModel + " ,detectorMode= " + detectorMode + " ,time = " + duration + " ms");
			return detectorValue;
		} catch (Exception e) {
			LOG.error("WS ERROR: findDetectorByParam - " + StringUtils.getCurrentDate() + " - " + detectorType + ", "
					+ detectorManufacturer + ", " + detectorModel + ", " + detectorMode);
			throw e;
		}
	}

	/**
	 * returns the pdb filePath for a given dataCollectionId
	 * 
	 * @param code
	 * @param number
	 * @return
	 * @throws Exception
	 */
	@WebMethod
	@WebResult(name = "PdbFilePath")
	public String getPdbFilePath(@WebParam(name = "dataCollectionId")
	Integer dataCollectionId) throws Exception {

		try {
			LOG.debug("getPdbFilePath : dataCollectionId= " + dataCollectionId);
			long startTime = System.currentTimeMillis();
			String pdbFilePath = "";
			DataCollection3Service dataCollectionService = (DataCollection3Service) ejb3ServiceLocator
					.getLocalService(DataCollection3Service.class);
			pdbFilePath = dataCollectionService.findPdbFullPath(dataCollectionId);
			long endTime = System.currentTimeMillis();
			long duration = endTime - startTime;
			LOG.debug("getPdbFilePath : dataCollectionId= " + dataCollectionId + ",  time = " + duration + " ms");
			return pdbFilePath;
		} catch (Exception e) {
			LOG.error("WS ERROR: getPdbFilePath - " + StringUtils.getCurrentDate() + " - " + dataCollectionId);
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "positionId")
	public Integer storeOrUpdatePosition(@WebParam(name = "position")
	PositionWS3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdatePosition");
			// if vo is null we return null, no creation
			if (vo == null)
				return null;

			Position3VO positionValue = null;
			Position3Service positionService = (Position3Service) ejb3ServiceLocator.getLocalService(Position3Service.class);

			Integer positionId = vo.getPositionId();

			Position3VO position = new Position3VO();
			// load the object elsewhere there is an error with the childs
			if (positionId != null && positionId > 0) {
				position = positionService.findByPk(positionId);
			}
			Position3VO relativePositionVO = null;
			if (vo.getRelativePositionId() != null && vo.getRelativePositionId() > 0)
				relativePositionVO = positionService.findByPk(vo.getRelativePositionId());

			position.fillVOFromWS(vo);
			position.setRelativePositionVO(relativePositionVO);
			position.setRecordTimeStamp(StringUtils.getCurrentTimeStamp());

			if (positionId == null || positionId == 0) {
				position.setPositionId(null);
				positionValue = positionService.create(position);
				positionId = positionValue.getPositionId();
				LOG.debug("Position created " + positionId);
			} else {
				positionValue = positionService.update(position);
				LOG.debug("Position updated " + positionId);
			}
			return positionValue.getPositionId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdatePosition - " + StringUtils.getCurrentDate() + " - " + vo.toWSString());
			throw e;
		}
	}
	
	protected Gson getGson(String ignore) {
		// put a fake parameter to avoid the Undertow request failed HttpServerExchange{ POST  .. error
		return new GsonBuilder().serializeNulls().excludeFieldsWithModifiers(Modifier.PRIVATE).serializeSpecialFloatingPointValues()
				.create();
	}
	
	@WebMethod
	@WebResult(name = "workflowId")
	public Integer storeWorkflowStep(
			@WebParam(name = "workflowStep") String workflowStep) throws Exception {
		try {
			LOG.info("storeWorkflowStep");
			LOG.info("workflowStep");
			LOG.info(workflowStep);
			
			WorkflowStep3VO workflowStep3VO = this.getGson("ignore").fromJson(workflowStep, WorkflowStep3VO.class);
			WorkflowStep3Service workflowStep3Service = (WorkflowStep3Service) ejb3ServiceLocator.getLocalService(WorkflowStep3Service.class);
			workflowStep3VO = workflowStep3Service.merge(workflowStep3VO);
			
			return workflowStep3VO.getWorkflowStepId();
			
		} catch (Exception e) {
			LOG.error("WS ERROR: storeWorkflowStep - " + StringUtils.getCurrentDate());
			throw e;
		}
		
	}
	
	
	@WebMethod
	@WebResult(name = "workflowId")
	public Integer storeOrUpdateWorkflow(@WebParam(name = "workflow")
	Workflow3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdateWorkflow");
			// if vo is null we return null, no creation
			if (vo == null)
				return null;

			Workflow3VO workflowValue = null;
			Workflow3Service workflowService = (Workflow3Service) ejb3ServiceLocator.getLocalService(Workflow3Service.class);

			Integer workflowId = vo.getWorkflowId();
			if (vo.getWorkflowType() == null)
				vo.setWorkflowType("Undefined");
			vo.setRecordTimeStamp(StringUtils.getCurrentTimeStamp());

			if (workflowId == null || workflowId == 0) {
				vo.setWorkflowId(null);
				workflowValue = workflowService.create(vo);
				workflowId = workflowValue.getWorkflowId();
				LOG.debug("Workflow created " + workflowId);
			} else {
				workflowValue = workflowService.update(vo);
				LOG.debug("Workflow updated " + workflowId);
			}
			return workflowValue.getWorkflowId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateWorkflow - " + StringUtils.getCurrentDate() + " - " + vo.toWSString());
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "dataCollectionGroupId")
	public Integer updateDataCollectionGroupWorkflowId(@WebParam(name = "fileLocation")
	String fileLocation, @WebParam(name = "fileName")
	String fileName, @WebParam(name = "workflowId")
	Integer workflowId) throws Exception {
		try {
			LOG.info("updateDataCollectionGroupWorkflowId : workflowId=" + workflowId + ", fileLocation= " + fileLocation
					+ ", fileName= " + fileName);
			// retrieve the datacollection from fileLocation and fileName
			DataCollection3VO dc = null;
			DataCollection3Service dataCollectionService = (DataCollection3Service) ejb3ServiceLocator
					.getLocalService(DataCollection3Service.class);
			dc = dataCollectionService.findForDataCollectionIdFromFileLocationAndFileName(fileLocation, fileName);
			if (dc == null) {
				// try to add or remove the last / -- it seems that there is no rule with this.
				if (fileLocation != null && fileLocation.length() > 1) {
					char lastCharact = fileLocation.charAt(fileLocation.length() - 1);
					if (lastCharact == '/') {
						dc = dataCollectionService.findForDataCollectionIdFromFileLocationAndFileName(
								fileLocation.substring(0, fileLocation.length() - 1), fileName);
					} else {
						dc = dataCollectionService.findForDataCollectionIdFromFileLocationAndFileName(fileLocation + "/", fileName);
					}
				}
			}
			// retrieve dataCollectionGroupId
			Integer dataCollectionGroupId = null;
			if (dc != null)
				dataCollectionGroupId = dc.getDataCollectionGroupVOId();
			// update workflowId
			if (dataCollectionGroupId != null) {
				DataCollectionGroup3Service dataCollectionGroupService = (DataCollectionGroup3Service) ejb3ServiceLocator
						.getLocalService(DataCollectionGroup3Service.class);
				DataCollectionGroup3VO dataCollectionGroupVO = dataCollectionGroupService.findByPk(dataCollectionGroupId, false, false);
				Workflow3Service workflowService = (Workflow3Service) ejb3ServiceLocator.getLocalService(Workflow3Service.class);
				Workflow3VO workflowVO = null;
				if (workflowId != null && workflowId > 0)
					workflowVO = workflowService.findByPk(workflowId);
				dataCollectionGroupVO.setWorkflowVO(workflowVO);
				dataCollectionGroupService.update(dataCollectionGroupVO);
			}
			return dataCollectionGroupId;
		} catch (Exception e) {
			LOG.error("WS ERROR: updateDataCollectionGroupWorkflowId - " + StringUtils.getCurrentDate() + " - " + fileLocation + ", "
					+ fileName + ", " + workflowId);
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "dataCollectionIds")
	public Integer[] groupDataCollections(@WebParam(name = "dataCollectionGroupId")
	Integer dataCollectionGroupId, @WebParam(name = "arrayOfFileLocation")
	String[] arrayOfFileLocation, @WebParam(name = "arrayOfFileName")
	String[] arrayOfFileName) throws Exception {
		try {
			LOG.debug("groupDataCollections for " + dataCollectionGroupId);
			// find the collect and update the dataCollectionGroup
			if (dataCollectionGroupId == null || dataCollectionGroupId == -1) {
				LOG.debug(" WS PB : dataCollectionGroup is null, dc not updated");
				return null;
			}
			if (arrayOfFileName == null || arrayOfFileLocation == null) {
				LOG.debug(" No collect to group");
				return null;
			}
			if (arrayOfFileName.length != arrayOfFileLocation.length) {
				LOG.debug(" WS PB: groupDataCollections - length of filename and fileLocation don't fit ");
				return null;
			}
			// get the dataCollectionGroup
			DataCollectionGroup3Service dataCollectionGroupService = (DataCollectionGroup3Service) ejb3ServiceLocator
					.getLocalService(DataCollectionGroup3Service.class);
			DataCollectionGroup3VO dataCollectionGroupVO = dataCollectionGroupService.findByPk(dataCollectionGroupId, false, false);
			DataCollection3Service dataCollectionService = (DataCollection3Service) ejb3ServiceLocator
					.getLocalService(DataCollection3Service.class);
			// for each collect
			int n = arrayOfFileName.length;
			Integer[] dcIdUpdated = new Integer[n];
			for (int i = 0; i < n; i++) {
				DataCollection3VO dc = dataCollectionService.findForDataCollectionIdFromFileLocationAndFileName(
						arrayOfFileLocation[i], arrayOfFileName[i]);
				if (dc != null) {
					Integer oldDataCollectionGroupId = dc.getDataCollectionGroupVOId();
					dc.setDataCollectionGroupVO(dataCollectionGroupVO);
					dataCollectionService.update(dc);
					dcIdUpdated[i] = dc.getDataCollectionId();
					LOG.debug(" Group dataCollection with dc = " + dc.getDataCollectionId() + ": old id = " + oldDataCollectionGroupId
							+ " to new id " + dataCollectionGroupId);
				}
			}
			return dcIdUpdated;
		} catch (Exception e) {
			String t = "[";
			if (arrayOfFileName != null && arrayOfFileLocation != null) {
				for (int i = 0; i < arrayOfFileName.length; i++) {
					t += arrayOfFileLocation[i] + " - " + arrayOfFileName[i] + "\n, ";
				}
			}
			t += "]";
			LOG.error("WS ERROR: groupDataCollections - " + StringUtils.getCurrentDate() + " - " + dataCollectionGroupId + ", " + t);
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "workflowId")
	public Integer updateWorkflowStatus(java.lang.Integer workflowId, @WebParam(name = "status")
	String status) throws Exception {
		try {
			LOG.debug("updateWorkflowStatus");
			Workflow3Service workflowService = (Workflow3Service) ejb3ServiceLocator.getLocalService(Workflow3Service.class);
			if (workflowId != null && workflowId > 0) {
				Workflow3VO workflow = workflowService.findByPk(workflowId);
				if (workflow != null) {
					workflow.setStatus(status);
					workflow = workflowService.update(workflow);
					LOG.debug("updateWorkflowStatus done");
					return workflow.getWorkflowId();
				} else {
					LOG.debug(" WS PB : updateWorkflowStatus workflow not found: " + workflowId);
					return -1;
				}
			} else {
				LOG.debug(" WS PB : updateWorkflowStatus workflowId: " + workflowId);
				return -1;
			}
		} catch (Exception e) {
			LOG.error("WS ERROR: updateWorkflowStatus - " + StringUtils.getCurrentDate() + " - " + workflowId + ", " + status);
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "dataCollectionId")
	public Integer setDataCollectionPosition(@WebParam(name = "fileLocation") String fileLocation, 
			@WebParam(name = "fileName")
	String fileName ) throws Exception {
		try {
			LOG.debug("setDataCollectionPosition");
			// retrieve the datacollection from fileLocation and fileName
			DataCollection3VO dc = null;
			DataCollection3Service dataCollectionService = (DataCollection3Service) ejb3ServiceLocator
					.getLocalService(DataCollection3Service.class);
			dc = dataCollectionService.findForDataCollectionIdFromFileLocationAndFileName(fileLocation, fileName);
			if (dc == null) {
				// try to add or remove the last / -- it seems that there is no rule with this.
				if (fileLocation != null && fileLocation.length() > 1) {
					char lastCharact = fileLocation.charAt(fileLocation.length() - 1);
					if (lastCharact == '/') {
						dc = dataCollectionService.findForDataCollectionIdFromFileLocationAndFileName(
								fileLocation.substring(0, fileLocation.length() - 1), fileName);
					} else {
						dc = dataCollectionService.findForDataCollectionIdFromFileLocationAndFileName(fileLocation + "/", fileName);
					}
				}
			}

			DataCollection3VO vodc = null;
			if (dc != null) {
				vodc = dataCollectionService.findByPk(dc.getDataCollectionId(), false, false);
			}
			return (vodc == null ? null : vodc.getDataCollectionId());
		} catch (Exception e) {
			LOG.error("WS ERROR: setDataCollectionPosition - " + StringUtils.getCurrentDate() + " - " + fileLocation + ", " + fileName);
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "gridInfoId")
	public Integer storeOrUpdateGridInfo(@WebParam(name = "gridInfo")
	GridInfoWS3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdateGridInfo");
			// if vo is null we return null, no creation
			if (vo == null)
				return null;
			if (vo.getWorkflowMeshId() == null || vo.getWorkflowMeshId() <= 0) {

				LOG.debug(" WS PB : workflowMeshId is null, gridInfo not stored");
				return errorCodeFK;

			}

			GridInfo3VO gridInfoValue = null;
			GridInfo3Service gridInfoService = (GridInfo3Service) ejb3ServiceLocator.getLocalService(GridInfo3Service.class);
			WorkflowMesh3Service workflowMeshService = (WorkflowMesh3Service) ejb3ServiceLocator
					.getLocalService(WorkflowMesh3Service.class);

			Integer gridInfoId = vo.getGridInfoId();

			GridInfo3VO gridInfo = new GridInfo3VO();
			// load the object elsewhere there is an error with the childs
			if (gridInfoId != null && gridInfoId > 0) {
				gridInfo = gridInfoService.findByPk(gridInfoId);
			}
			WorkflowMesh3VO workflowMeshVO = null;
			if (vo.getWorkflowMeshId() != null && vo.getWorkflowMeshId() > 0)
				workflowMeshVO = workflowMeshService.findByPk(vo.getWorkflowMeshId());

			gridInfo.fillVOFromWS(vo);
			gridInfo.setWorkflowMeshVO(workflowMeshVO);

			if (gridInfoId == null || gridInfoId == 0) {
				gridInfo.setGridInfoId(null);
				gridInfoValue = gridInfoService.create(gridInfo);
				gridInfoId = gridInfoValue.getGridInfoId();
				LOG.debug("GridInfo created " + gridInfoId);
			} else {
				gridInfoValue = gridInfoService.update(gridInfo);
				LOG.debug("GridInfo updated " + gridInfoId);
			}
			return gridInfoValue.getGridInfoId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateGridInfo - " + StringUtils.getCurrentDate() + " - " + vo.toWSString());
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "workflowMeshId")
	public Integer storeOrUpdateWorkflowMesh(@WebParam(name = "workflowMesh")
	WorkflowMeshWS3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdateWorkflowMesh");
			// if vo is null we return null, no creation
			if (vo == null)
				return null;
			if (vo.getWorkflowId() == null || vo.getWorkflowId() <= 0) {
				LOG.debug(" WS PB : workflowId is null, workflowMesh not stored");
				return errorCodeFK;
			}

			WorkflowMesh3VO workflowMeshValue = null;
			Workflow3Service workflowService = (Workflow3Service) ejb3ServiceLocator.getLocalService(Workflow3Service.class);
			WorkflowMesh3Service workflowMeshService = (WorkflowMesh3Service) ejb3ServiceLocator
					.getLocalService(WorkflowMesh3Service.class);
			Image3Service imageService = (Image3Service) ejb3ServiceLocator.getLocalService(Image3Service.class);

			Integer workflowMeshId = vo.getWorkflowMeshId();

			WorkflowMesh3VO workflowMesh = new WorkflowMesh3VO();
			// load the object elsewhere there is an error with the childs
			if (workflowMeshId != null && workflowMeshId > 0) {
				workflowMesh = workflowMeshService.findByPk(workflowMeshId);
			}
			Workflow3VO workflowVO = null;
			if (vo.getWorkflowId() != null && vo.getWorkflowId() > 0)
				workflowVO = workflowService.findByPk(vo.getWorkflowId());

			Image3VO bestImageVO = null;
			if (vo.getBestImageId() != null && vo.getBestImageId() > 0)
				bestImageVO = imageService.findByPk(vo.getBestImageId());

			workflowMesh.fillVOFromWS(vo);
			workflowMesh.setWorkflowVO(workflowVO);
			workflowMesh.setBestImageVO(bestImageVO);

			if (workflowMeshId == null || workflowMeshId == 0) {
				workflowMesh.setWorkflowMeshId(null);
				workflowMeshValue = workflowMeshService.create(workflowMesh);
				workflowMeshId = workflowMeshValue.getWorkflowMeshId();
				LOG.debug("WorkflowMesh created " + workflowMeshId);
			} else {
				workflowMeshValue = workflowMeshService.update(workflowMesh);
				LOG.debug("WorkflowMesh updated " + workflowMeshId);
			}
			return workflowMeshValue.getWorkflowMeshId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateWorkflowMesh - " + StringUtils.getCurrentDate() + " - " + vo.toWSString());
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "dataCollectionIds")
	public Integer[] setDataCollectionsPositions(@WebParam(name = "listDataCollectionPosition")
	List<DataCollectionPosition> listDataCollectionPosition) throws Exception {
		try {
			LOG.debug("setDataCollectionsPositions");

			int nb = listDataCollectionPosition.size();
			DataCollection3Service dataCollectionService = (DataCollection3Service) ejb3ServiceLocator
					.getLocalService(DataCollection3Service.class);
			Integer[] listIds = new Integer[nb];
			for (int i = 0; i < nb; i++) {
				try {
					DataCollectionPosition o = listDataCollectionPosition.get(i);

					String fileLocation = o.getFileLocation();
					String fileName = o.getFileName();
					// retrieve the datacollection from fileLocation and fileName
					DataCollection3VO dc = null;
					dc = dataCollectionService.findForDataCollectionIdFromFileLocationAndFileName(fileLocation, fileName);
					if (dc == null) {
						// try to add or remove the last / -- it seems that there is no rule with this.
						if (fileLocation != null && fileLocation.length() > 1) {
							char lastCharact = fileLocation.charAt(fileLocation.length() - 1);
							if (lastCharact == '/') {
								dc = dataCollectionService.findForDataCollectionIdFromFileLocationAndFileName(
										fileLocation.substring(0, fileLocation.length() - 1), fileName);
							} else {
								dc = dataCollectionService.findForDataCollectionIdFromFileLocationAndFileName(fileLocation + "/",
										fileName);
							}
						}
					}
					DataCollection3VO vodc = null;
					if (dc != null) {
						vodc = dataCollectionService.findByPk(dc.getDataCollectionId(), false, false);
					}
					listIds[i] = (vodc == null ? null : vodc.getDataCollectionId());
				} catch (Exception e) {

				}
			}

			return listIds;
		} catch (Exception e) {
			LOG.error("WS ERROR: setDataCollectionsPositions - " + StringUtils.getCurrentDate() + " - "
					+ (listDataCollectionPosition == null ? "null" : listDataCollectionPosition.size()));
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "dataCollectionInfo")
	public DataCollectionInfo getDataCollectionInfo(@WebParam(name = "dataCollectionId")
	Integer dataCollectionId) throws Exception {
		try {
			LOG.debug("getDataCollectionInfo");
			DataCollectionWS3VO dataCollectionValue = null;
			DataCollection3Service dataCollectionService = (DataCollection3Service) ejb3ServiceLocator
					.getLocalService(DataCollection3Service.class);

			dataCollectionValue = dataCollectionService.findForWSByPk(dataCollectionId, false, false);
			if (dataCollectionValue != null)
				LOG.debug("getDataCollectionInfo found :" + dataCollectionId);
			DataCollection3VO vo = dataCollectionService.findByPk(dataCollectionId, false, false);
			if (vo != null) {
				DataCollectionGroup3Service dataCollectionGroupService = (DataCollectionGroup3Service) ejb3ServiceLocator
						.getLocalService(DataCollectionGroup3Service.class);
				DataCollectionGroup3VO group = dataCollectionGroupService.findByPk(vo.getDataCollectionGroupVOId(), false, false);
				Session3VO sessionVO = group.getSessionVO();
				String localContact = "";
				if (sessionVO != null && sessionVO.getBeamlineOperator() != null)
					localContact = sessionVO.getBeamlineOperator();
				String localContactEmail = "";
				if (sessionVO != null)
					localContactEmail = sessionVO.getBeamLineOperatorEmail();
				BLSampleWS3VO blSampleVO = null;
				BLSample3VO sampleFromDB = null;
				if (vo.getDataCollectionGroupVO() != null && vo.getDataCollectionGroupVO().getBlSampleVO() != null) {
					BLSample3Service blSampleService = (BLSample3Service) ejb3ServiceLocator.getLocalService(BLSample3Service.class);
					blSampleVO = blSampleService.findForWSByPk(vo.getDataCollectionGroupVO().getBlSampleVOId(), false, false, false);
					sampleFromDB = blSampleService.findByPk(vo.getDataCollectionGroupVO().getBlSampleVOId(), false, false, false);
				}
				String spaceGroup = "";
				Double cellA = null;
				Double cellB = null;
				Double cellC = null;
				Double cellAlpha = null;
				Double cellBeta = null;
				Double cellGamma = null;

				if (sampleFromDB != null && sampleFromDB.getCrystalVO() != null) {
					spaceGroup = sampleFromDB.getCrystalVO().getSpaceGroup();
					cellA = sampleFromDB.getCrystalVO().getCellA();
					cellB = sampleFromDB.getCrystalVO().getCellB();
					cellC = sampleFromDB.getCrystalVO().getCellC();
					cellAlpha = sampleFromDB.getCrystalVO().getCellAlpha();
					cellBeta = sampleFromDB.getCrystalVO().getCellBeta();
					cellGamma = sampleFromDB.getCrystalVO().getCellGamma();
				}

				String pdbFilePath = "";
				pdbFilePath = dataCollectionService.findPdbFullPath(dataCollectionId);

				if (dataCollectionValue != null) {
					DataCollectionInfo info = new DataCollectionInfo(dataCollectionValue, localContact, localContactEmail, blSampleVO,
							spaceGroup, cellA, cellB, cellC, cellAlpha, cellBeta,
							cellGamma, pdbFilePath);
					return info;
				} else {
					return null;
				}
			} else
				return null;
		} catch (Exception e) {
			LOG.error("WS ERROR: getDataCollectionInfo - " + StringUtils.getCurrentDate() + " - " + dataCollectionId);
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "arrayImageIdsAndIsCreated")
	public ImageCreation[] setImagesPositions(@WebParam(name = "listImagePosition")
	List<ImagePosition> listImagePosition) throws Exception {
		try {
			LOG.debug("setImagesPositions");
			if (listImagePosition == null) {
				return null;
			}
			int nb = listImagePosition.size();
			Image3Service imageService = (Image3Service) ejb3ServiceLocator.getLocalService(Image3Service.class);
			DataCollection3Service dataCollectionService = (DataCollection3Service) ejb3ServiceLocator
					.getLocalService(DataCollection3Service.class);
			ImageCreation[] listIds = new ImageCreation[nb];
			Integer dataCollectionId = null;
			DataCollection3VO dataCollectionVO = null;
			Integer imageNumber = 1;
			for (int i = 0; i < nb; i++) {
				try {
					ImagePosition o = listImagePosition.get(i);

					String fileLocation = o.getFileLocation();
					String fileName = o.getFileName();
					String jpegFileFullPath = o.getJpegFileFullPath();
					;
					String jpegThumbnailFileFullPath = o.getJpegThumbnailFileFullPath();
					// retrieve the image from fileLocation and fileName
					Image3VO image = null;
					Boolean isCreated = false;
					List<Image3VO> listImages = null;
					listImages = imageService.findFiltered(fileLocation, fileName);
					if (listImages == null || listImages.size() == 0) {
						// try to add or remove the last / -- it seems that there is no rule with this.
						if (fileLocation != null && fileLocation.length() > 1) {
							char lastCharact = fileLocation.charAt(fileLocation.length() - 1);
							if (lastCharact == '/') {
								listImages = imageService.findFiltered(fileLocation.substring(0, fileLocation.length() - 1), fileName);
							} else {
								listImages = imageService.findFiltered(fileLocation + "/", fileName);
							}
						}
					}

					// image exists already: only set the motor position
					if (listImages != null && listImages.size() > 0) {
						image = listImages.get(0);
						dataCollectionId = image.getDataCollectionVOId();
						isCreated = false;
						imageNumber++;
					} else { // image does not exist: creation
						if (dataCollectionId != null) {
							if (dataCollectionVO == null) {
								dataCollectionVO = dataCollectionService.findByPk(dataCollectionId, false, false);
							}
							Image3VO imagevo = new Image3VO(null, dataCollectionVO, imageNumber, fileName, fileLocation, null,
									jpegFileFullPath, jpegThumbnailFileFullPath, null, null, null, null, null);
							image = imageService.create(imagevo);
							isCreated = true;
							imageNumber++;
						} else {
							LOG.error("Error in setImagesPositions -- no dataCollectionId retrieved- " + fileLocation + ", "
									+ fileName);
						}
					}
					Integer imageId = (image == null ? null : image.getImageId());
					listIds[i] = new ImageCreation(imageId, isCreated, fileLocation, fileName);
				} catch (Exception e) {
					LOG.error("WS ERROR: setImagesPositions - " + StringUtils.getCurrentDate() + " - " + i + ", "
							+ (listImagePosition == null ? "null" : listImagePosition.size()));
					throw e;
				}
			}

			return listIds;
		} catch (Exception e) {
			LOG.error("WS ERROR: setImagesPositions - " + StringUtils.getCurrentDate() + " - "
					+ (listImagePosition == null ? "null" : listImagePosition.size()));
			throw e;
		}
	}

	public static Integer updateSession(Session3VO vo) throws Exception {
		try {
			LOG.debug("updateSession");
			// if vo is null we return null
			if (vo == null)
				return null;

			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Session3VO sessionValue = null;
			Session3Service sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
			Timestamp currentTimeStamp = StringUtils.getCurrentTimeStamp();
			Timestamp lastUpdate = new Timestamp(vo.getLastUpdate().getTime());
			if (currentTimeStamp.after(lastUpdate)) {
				vo.setLastUpdate(currentTimeStamp);
				sessionValue = sessionService.update(vo);
				LOG.debug("Session updated " + sessionValue.getSessionId());
				return sessionValue.getSessionId();
			}

			return null;
		} catch (Exception e) {
			LOG.error("WS ERROR: updateSession - " + StringUtils.getCurrentDate() + " - " + vo.toWSString());
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "dataCollectionId")
	public Integer setBestWilsonPlotPath(Integer dataCollectionId, @WebParam(name = "bestWilsonPlotPath")
	String bestWilsonPlotPath) throws Exception {
		try {
			LOG.debug("setBestWilsonPlotPath for dataCollectionId = " + dataCollectionId);
			// retrieve the datacollection
			DataCollection3VO dc = null;
			DataCollection3Service dataCollectionService = (DataCollection3Service) ejb3ServiceLocator.getLocalService(DataCollection3Service.class);
			dc = dataCollectionService.findByPk(dataCollectionId, false, false);

			if (dc != null) {
				dc.setBestWilsonPlotPath(bestWilsonPlotPath);
				dataCollectionService.update(dc);
			}
			return (dc == null ? null : dc.getDataCollectionId());
		} catch (Exception e) {
			LOG.error("WS ERROR: setBestWilsonPlotPath - " + StringUtils.getCurrentDate() + " - dcId=" + dataCollectionId
					+ ", bestWilsonPlotPath=" + bestWilsonPlotPath);
			throw e;
		}
	}
	
	
	@WebMethod
	@WebResult(name = "dataCollectionId")
	public Integer setImageQualityIndicatorsPlot(
			Integer dataCollectionId, 
			@WebParam(name = "imageQualityIndicatorsPlotPath")	String imageQualityIndicatorsPlotPath,
			@WebParam(name = "imageQualityIndicatorsCSVPath")	String imageQualityIndicatorsCSVPath) throws Exception {
		try {
			LOG.debug("setImageQualityIndicatorsPlot for dataCollectionId = " + dataCollectionId);
			// retrieve the datacollection
			DataCollection3VO dc = null;
			DataCollection3Service dataCollectionService = (DataCollection3Service) ejb3ServiceLocator.getLocalService(DataCollection3Service.class);
			dc = dataCollectionService.findByPk(dataCollectionId, false, false);

			if (dc != null) {
				dc.setImageQualityIndicatorsPlotPath(imageQualityIndicatorsPlotPath);
				dc.setImageQualityIndicatorsCSVPath(imageQualityIndicatorsCSVPath);
				dataCollectionService.update(dc);
			}
			return (dc == null ? null : dc.getDataCollectionId());
		} catch (Exception e) {
			LOG.error("WS ERROR: setBestWilsonPlotPath - " + StringUtils.getCurrentDate() + " - dcId=" + dataCollectionId
					+ ", imageQualityIndicatorsPlotPath=" + imageQualityIndicatorsPlotPath);
			throw e;
		}
	}
	
	@WebMethod
	@WebResult(name = "structures")
	public String getLigandsByDataCollectionId(
	@WebParam(name = "dataCollectionId")Integer dataCollectionId) throws Exception {
		try {
			Structure3Service structure3service = (Structure3Service) ejb3ServiceLocator.getLocalService(Structure3Service.class);
			List<Structure3VO> structures = structure3service.getLigandsByDataCollectionId(dataCollectionId);
			if (structures != null){
				return convertStructuresToCSV(structures);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String convertStructuresToCSV(List<Structure3VO> structures) {

		try {
			StringBuilder sb = new StringBuilder();
			for (Structure3VO structure3vo : structures) {
				sb.append(structure3vo.getGroupName());
				sb.append(",");
				sb.append(structure3vo.getType());
				sb.append(",");
				sb.append(structure3vo.getFilePath());
				sb.append(",");
				sb.append(structure3vo.getMultiplicity());
				sb.append(",");
				sb.append(structure3vo.getUniprotId());
				sb.append("\n");
			}
			return sb.toString();
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@WebMethod
	@WebResult(name = "robotActionId")
	public Integer storeRobotAction(@WebParam(name = "robotAction")
	RobotActionWS3VO vo) throws Exception {
		try {
			LOG.debug("storeRobotAction");
			// if vo is null we return null, no creation
			if (vo == null)
				return null;

			// if robotActionId is not null we return null, no creation there is a problem we don't plan to update a robotAction
			if (vo.getRobotActionId() != null ) {
				LOG.debug(" WS PB : vo.getRobotActionId()Id is not null, robotAction not stored");
				return null;
			}

			// if sessionId is null we return null, no creation 
			if (vo.getSessionId() == null || vo.getSessionId() <= 0) {

				LOG.debug(" WS PB : sessionId is null, robotAction not stored");
				return null;

			}
			
			LOG.debug("...with sampleId = " + vo.getBlSampleId());			
			RobotAction3VO value = new RobotAction3VO();
			RobotAction3Service robotActionService = (RobotAction3Service) ejb3ServiceLocator
					.getLocalService(RobotAction3Service.class);


			BLSample3Service blSampleService = (BLSample3Service) ejb3ServiceLocator.getLocalService(BLSample3Service.class);
			BLSample3VO blSampleVO = null;
			if (vo.getBlSampleId() != null && vo.getBlSampleId() > 0)
				blSampleVO = blSampleService.findByPk(vo.getBlSampleId(), false, false, false);

			Session3Service sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
			Session3VO sessionVO = null;
			if (vo.getSessionId() != null && vo.getSessionId() > 0)
				sessionVO = sessionService.findByPk(vo.getSessionId(), true, false, false);

			value.fillVOFromWS(vo);
			value.setBlSampleVO(blSampleVO);
			value.setSessionVO(sessionVO);
			
			RobotAction3VO newValue = robotActionService.merge(value);

			// TODO: update session and blSample if oneToMany links active

			return newValue.getRobotActionId();

		} catch (Exception e) {
			LOG.error("WS ERROR: storeRobotAction - " + StringUtils.getCurrentDate() + " - " + vo.toString());
			throw e;
		}
	}
}
