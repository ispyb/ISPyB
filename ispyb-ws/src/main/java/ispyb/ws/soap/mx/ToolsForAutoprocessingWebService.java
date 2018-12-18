/** This file is part of ISPyB.
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

import ispyb.common.util.Constants;
import ispyb.common.util.StringUtils;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.util.LoggerFormatter;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.autoproc.AutoProc3Service;
import ispyb.server.mx.services.autoproc.AutoProcIntegration3Service;
import ispyb.server.mx.services.autoproc.AutoProcProgram3Service;
import ispyb.server.mx.services.autoproc.AutoProcProgramAttachment3Service;
import ispyb.server.mx.services.autoproc.AutoProcScaling3Service;
import ispyb.server.mx.services.autoproc.AutoProcScalingHasInt3Service;
import ispyb.server.mx.services.autoproc.AutoProcScalingStatistics3Service;
import ispyb.server.mx.services.autoproc.AutoProcStatus3Service;
import ispyb.server.mx.services.autoproc.ImageQualityIndicators3Service;
import ispyb.server.mx.services.autoproc.ModelBuilding3Service;
import ispyb.server.mx.services.autoproc.Phasing3Service;
import ispyb.server.mx.services.autoproc.PhasingAnalysis3Service;
import ispyb.server.mx.services.autoproc.PhasingHasScaling3Service;
import ispyb.server.mx.services.autoproc.PhasingProgramAttachment3Service;
import ispyb.server.mx.services.autoproc.PhasingProgramRun3Service;
import ispyb.server.mx.services.autoproc.PhasingStatistics3Service;
import ispyb.server.mx.services.autoproc.PreparePhasingData3Service;
import ispyb.server.mx.services.autoproc.SpaceGroup3Service;
import ispyb.server.mx.services.autoproc.SubstructureDetermination3Service;
import ispyb.server.mx.services.autoproc.phasingStep.PhasingStep3Service;
import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.server.mx.services.collections.Image3Service;
import ispyb.server.mx.vos.autoproc.AutoProc3VO;
import ispyb.server.mx.vos.autoproc.AutoProcIntegration3VO;
import ispyb.server.mx.vos.autoproc.AutoProcIntegrationWS3VO;
import ispyb.server.mx.vos.autoproc.AutoProcProgram3VO;
import ispyb.server.mx.vos.autoproc.AutoProcProgramAttachment3VO;
import ispyb.server.mx.vos.autoproc.AutoProcProgramAttachmentWS3VO;
import ispyb.server.mx.vos.autoproc.AutoProcScaling3VO;
import ispyb.server.mx.vos.autoproc.AutoProcScalingHasInt3VO;
import ispyb.server.mx.vos.autoproc.AutoProcScalingHasIntWS3VO;
import ispyb.server.mx.vos.autoproc.AutoProcScalingStatistics3VO;
import ispyb.server.mx.vos.autoproc.AutoProcScalingStatisticsWS3VO;
import ispyb.server.mx.vos.autoproc.AutoProcScalingWS3VO;
import ispyb.server.mx.vos.autoproc.AutoProcStatus3VO;
import ispyb.server.mx.vos.autoproc.AutoProcStatusWS3VO;
import ispyb.server.mx.vos.autoproc.AutoProcWS3VO;
import ispyb.server.mx.vos.autoproc.ImageQualityIndicators3VO;
import ispyb.server.mx.vos.autoproc.ImageQualityIndicatorsWS3VO;
import ispyb.server.mx.vos.autoproc.ModelBuilding3VO;
import ispyb.server.mx.vos.autoproc.ModelBuildingWS3VO;
import ispyb.server.mx.vos.autoproc.Phasing3VO;
import ispyb.server.mx.vos.autoproc.PhasingAnalysis3VO;
import ispyb.server.mx.vos.autoproc.PhasingData;
import ispyb.server.mx.vos.autoproc.PhasingHasScaling3VO;
import ispyb.server.mx.vos.autoproc.PhasingHasScalingWS3VO;
import ispyb.server.mx.vos.autoproc.PhasingProgramAttachment3VO;
import ispyb.server.mx.vos.autoproc.PhasingProgramAttachmentWS3VO;
import ispyb.server.mx.vos.autoproc.PhasingProgramRun3VO;
import ispyb.server.mx.vos.autoproc.PhasingStatistics3VO;
import ispyb.server.mx.vos.autoproc.PhasingStatisticsWS3VO;
import ispyb.server.mx.vos.autoproc.PhasingStepVO;
import ispyb.server.mx.vos.autoproc.PhasingWS3VO;
import ispyb.server.mx.vos.autoproc.PreparePhasingData3VO;
import ispyb.server.mx.vos.autoproc.PreparePhasingDataWS3VO;
import ispyb.server.mx.vos.autoproc.SpaceGroup3VO;
import ispyb.server.mx.vos.autoproc.SubstructureDetermination3VO;
import ispyb.server.mx.vos.autoproc.SubstructureDeterminationWS3VO;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.Image3VO;
import ispyb.server.mx.vos.collections.Session3VO;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ws.api.annotation.WebContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Web services for AutoProc & Phasing
 * 
 * @author BODIN
 * 
 */
@WebService(name = "ToolsForAutoprocessingWebService", serviceName = "ispybWS", targetNamespace = "http://ispyb.ejb3.webservices.autoproc")
@SOAPBinding(style = Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Stateless
@RolesAllowed({ "WebService", "User", "Industrial"}) // allow special access roles
@SecurityDomain("ispyb")
@WebContext(authMethod="BASIC",  secureWSDLAccess=false, transportGuarantee="NONE")
public class ToolsForAutoprocessingWebService {
	private final static Logger LOG = Logger.getLogger(ToolsForAutoprocessingWebService.class);

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private final Integer errorCodeFK = null;

	private long now;

	@WebMethod(operationName = "echo")
	@WebResult(name = "echo")
	public String echo(){
		return "echo from server...";
	}
	
	private Integer storeOrUpdateAutoProcValue(AutoProcWS3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdateAutoProcValue");
			if (vo == null)
				return null;

			AutoProc3VO autoProcValue = null;
			AutoProc3Service autoProcService = (AutoProc3Service) ejb3ServiceLocator
					.getLocalService(AutoProc3Service.class);

			Integer autoProcId = vo.getAutoProcId();
			if (vo.getAutoProcProgramId() == null || vo.getAutoProcProgramId() == 0) {
				vo.setAutoProcProgramId(null);
			}

			AutoProc3VO autoProcVO = new AutoProc3VO();
			AutoProcProgram3Service autoProcProgramService = (AutoProcProgram3Service) ejb3ServiceLocator
					.getLocalService(AutoProcProgram3Service.class);
			AutoProcProgram3VO autoProcProgramVO = null;
			if (vo.getAutoProcProgramId() != null && vo.getAutoProcProgramId() > 0)
				autoProcProgramVO = autoProcProgramService.findByPk(vo.getAutoProcProgramId(), false);
			autoProcVO.fillVOFromWS(vo);
			autoProcVO.setAutoProcProgramVO(autoProcProgramVO);
			autoProcVO.setRecordTimeStamp(StringUtils.getCurrentTimeStamp());

			if (autoProcId == null || autoProcId == 0) {
				autoProcVO.setAutoProcId(null);
				autoProcValue = autoProcService.create(autoProcVO);
			} else {
				autoProcValue = autoProcService.update(autoProcVO);
			}
			LOG.debug("storeOrUpdateAutoProcValue " + autoProcValue.getAutoProcId());
			return autoProcValue.getAutoProcId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateAutoProcValue - " + StringUtils.getCurrentDate() + " - "
					+ vo.toWSString());
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "autoProcId")
	public Integer storeOrUpdateAutoProc(Integer autoProcId,
			@WebParam(name = "autoProcProgramId") Integer autoProcProgramId,
			@WebParam(name = "spaceGroup") String spaceGroup, @WebParam(name = "refinedCellA") Float refinedCellA,
			@WebParam(name = "refinedCellB") Float refinedCellB, @WebParam(name = "refinedCellC") Float refinedCellC,
			@WebParam(name = "refinedCellAlpha") Float refinedCellAlpha,
			@WebParam(name = "refinedCellBeta") Float refinedCellBeta,
			@WebParam(name = "refinedCellGamma") Float refinedCellGamma,
			@WebParam(name = "recordTimeStamp") Date recordTimeStamp) throws Exception {
		AutoProcWS3VO vo = new AutoProcWS3VO(autoProcId, autoProcProgramId, spaceGroup, refinedCellA, refinedCellB,
				refinedCellC, refinedCellAlpha, refinedCellBeta, refinedCellGamma, recordTimeStamp);
		return storeOrUpdateAutoProcValue(vo);
	}

	private Integer storeOrUpdateAutoProcScalingValue(AutoProcScalingWS3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdateAutoProcScalingValue");
			if (vo == null)
				return null;

			if (vo.getAutoProcId() == null) {
				LOG.debug("WS PB : AutoProcId is null , could not create autoProcScalingId");
				return errorCodeFK;
			}

			AutoProcScaling3VO autoProcScalingValue = null;
			AutoProcScaling3Service autoProcScalingService = (AutoProcScaling3Service) ejb3ServiceLocator
					.getLocalService(AutoProcScaling3Service.class);

			Integer autoProcScalingId = vo.getAutoProcScalingId();

			AutoProcScaling3VO autoProcScalingVO = new AutoProcScaling3VO();
			AutoProc3Service autoProcService = (AutoProc3Service) ejb3ServiceLocator
					.getLocalService(AutoProc3Service.class);
			AutoProc3VO autoProcVO = null;
			if (vo.getAutoProcId() != null && vo.getAutoProcId() > 0)
				autoProcVO = autoProcService.findByPk(vo.getAutoProcId());
			autoProcScalingVO.fillVOFromWS(vo);
			autoProcScalingVO.setAutoProcVO(autoProcVO);
			autoProcScalingVO.setRecordTimeStamp(StringUtils.getCurrentTimeStamp());

			if (autoProcScalingId == null || autoProcScalingId == 0) {
				autoProcScalingVO.setAutoProcScalingId(null);
				autoProcScalingValue = autoProcScalingService.create(autoProcScalingVO);
			} else {
				autoProcScalingValue = autoProcScalingService.update(autoProcScalingVO);
			}
			// update session
			Session3Service sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
			Session3VO sessionVO = sessionService.findByAutoProcScalingId(autoProcScalingId);
			ToolsForCollectionWebService.updateSession(sessionVO);
			LOG.debug("storeOrUpdateAutoProcScalingValue " + autoProcScalingValue.getAutoProcScalingId());

			return autoProcScalingValue.getAutoProcScalingId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateAutoProcScalingValue - " + StringUtils.getCurrentDate() + " - "
					+ vo.toWSString());
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "autoProcScalingId")
	public Integer storeOrUpdateAutoProcScaling(Integer autoProcScalingId,
			@WebParam(name = "autoProcId") Integer autoProcId, @WebParam(name = "recordTimeStamp") Date recordTimeStamp,
			@WebParam(name = "resolutionEllipsoidAxis11") Float resolutionEllipsoidAxis11, 
			@WebParam(name = "resolutionEllipsoidAxis12") Float resolutionEllipsoidAxis12,
			@WebParam(name = "resolutionEllipsoidAxis13") Float resolutionEllipsoidAxis13,
			@WebParam(name = "resolutionEllipsoidAxis21") Float resolutionEllipsoidAxis21, 
			@WebParam(name = "resolutionEllipsoidAxis22") Float resolutionEllipsoidAxis22,
			@WebParam(name = "resolutionEllipsoidAxis23") Float resolutionEllipsoidAxis23,
			@WebParam(name = "resolutionEllipsoidAxis31") Float resolutionEllipsoidAxis31, 
			@WebParam(name = "resolutionEllipsoidAxis32") Float resolutionEllipsoidAxis32,
			@WebParam(name = "resolutionEllipsoidAxis33") Float resolutionEllipsoidAxis33,
			@WebParam(name = "resolutionEllipsoidValue1") Float resolutionEllipsoidValue1, 
			@WebParam(name = "resolutionEllipsoidValue2") Float resolutionEllipsoidValue2,
			@WebParam(name = "resolutionEllipsoidValue3") Float resolutionEllipsoidValue3			
			)
			throws Exception {
		AutoProcScalingWS3VO vo = new AutoProcScalingWS3VO(autoProcScalingId, autoProcId, recordTimeStamp,
				resolutionEllipsoidAxis11, resolutionEllipsoidAxis12, resolutionEllipsoidAxis13,
				resolutionEllipsoidAxis21, resolutionEllipsoidAxis22, resolutionEllipsoidAxis23,
				resolutionEllipsoidAxis31, resolutionEllipsoidAxis32, resolutionEllipsoidAxis33,
				resolutionEllipsoidValue1, resolutionEllipsoidValue2, resolutionEllipsoidValue3);
		return storeOrUpdateAutoProcScalingValue(vo);
	}

	private Integer storeOrUpdateAutoProcScalingHasIntValue(AutoProcScalingHasIntWS3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdateAutoProcScalingHasIntValue");
			if (vo == null)
				return null;

			if (vo.getAutoProcIntegrationId() == null) {
				LOG.debug("WS PB : AutoProcIntegrationId is null , could not create autoProcScalingHasInt");
				return errorCodeFK;
			}

			if (vo.getAutoProcScalingId() == null) {
				LOG.debug("WS PB : AutoProcScalingId is null , could not create autoProcScalingHasInt");
				return errorCodeFK;
			}
			AutoProcScalingHasInt3VO autoProcScalingHasIntValue = null;
			AutoProcScalingHasInt3Service autoProcScalingHasIntService = (AutoProcScalingHasInt3Service) ejb3ServiceLocator
					.getLocalService(AutoProcScalingHasInt3Service.class);

			Integer autoProcScalingHasIntId = vo.getAutoProcScalingHasIntId();

			AutoProcScalingHasInt3VO autoProcScalingHasIntVO = new AutoProcScalingHasInt3VO();
			AutoProcScaling3Service autoProcScalingService = (AutoProcScaling3Service) ejb3ServiceLocator
					.getLocalService(AutoProcScaling3Service.class);
			AutoProcScaling3VO autoProcScalingVO = null;
			if (vo.getAutoProcScalingId() != null && vo.getAutoProcScalingId() > 0)
				autoProcScalingVO = autoProcScalingService.findByPk(vo.getAutoProcScalingId());
			AutoProcIntegration3Service autoProcIntegrationService = (AutoProcIntegration3Service) ejb3ServiceLocator
					.getLocalService(AutoProcIntegration3Service.class);
			AutoProcIntegration3VO autoProcIntegrationVO = null;
			if (vo.getAutoProcIntegrationId() != null && vo.getAutoProcIntegrationId() > 0)
				autoProcIntegrationVO = autoProcIntegrationService.findByPk(vo.getAutoProcIntegrationId());
			autoProcScalingHasIntVO.fillVOFromWS(vo);
			autoProcScalingHasIntVO.setAutoProcScalingVO(autoProcScalingVO);
			autoProcScalingHasIntVO.setAutoProcIntegrationVO(autoProcIntegrationVO);
			autoProcScalingHasIntVO.setRecordTimeStamp(StringUtils.getCurrentTimeStamp());
			if (autoProcScalingHasIntId == null || autoProcScalingHasIntId == 0) {
				autoProcScalingHasIntVO.setAutoProcScalingHasIntId(null);
				autoProcScalingHasIntValue = autoProcScalingHasIntService.create(autoProcScalingHasIntVO);
			} else {
				autoProcScalingHasIntValue = autoProcScalingHasIntService.update(autoProcScalingHasIntVO);
			}
			LOG.debug("storeOrUpdateAutoProcScalingHasIntValue "
					+ autoProcScalingHasIntValue.getAutoProcScalingHasIntId());
			return autoProcScalingHasIntValue.getAutoProcScalingHasIntId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateAutoProcScalingHasIntValue - " + StringUtils.getCurrentDate() + " - "
					+ vo.toWSString());
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "autoProcScalingHasIntId")
	public Integer storeOrUpdateAutoProcScalingHasInt(Integer autoProcScalingHasIntId,
			@WebParam(name = "autoProcIntegrationId") Integer autoProcIntegrationId,
			@WebParam(name = "autoProcScalingId") Integer autoProcScalingId,
			@WebParam(name = "recordTimeStamp") Date recordTimeStamp) throws Exception {
		AutoProcScalingHasIntWS3VO vo = new AutoProcScalingHasIntWS3VO(autoProcScalingHasIntId, autoProcScalingId,
				autoProcIntegrationId, recordTimeStamp);
		return storeOrUpdateAutoProcScalingHasIntValue(vo);
	}

	private Integer storeOrUpdateAutoProcScalingStatisticValue(AutoProcScalingStatisticsWS3VO vo) throws Exception {
		try {
			LOG.info("storeOrUpdateAutoProcScalingStatisticValue");
			if (vo == null)
				return null;

			if (vo.getAutoProcScalingId() == null) {
				LOG.info("WS PB : AutoProcScalingId is null , could not create autoProcScalingStatistics");
				return errorCodeFK;
			}

			AutoProcScalingStatistics3VO autoProcScalingStatisticValue = null;
			AutoProcScalingStatistics3Service autoProcScalingStatisticsService = (AutoProcScalingStatistics3Service) ejb3ServiceLocator
					.getLocalService(AutoProcScalingStatistics3Service.class);

			Integer autoProcScalingStatisticId = vo.getAutoProcScalingStatisticsId();

			AutoProcScalingStatistics3VO autoProcScalingStatisticVO = new AutoProcScalingStatistics3VO();
			AutoProcScaling3Service autoProcScalingService = (AutoProcScaling3Service) ejb3ServiceLocator
					.getLocalService(AutoProcScaling3Service.class);
			AutoProcScaling3VO autoProcScalingVO = null;
			if (vo.getAutoProcScalingId() != null && vo.getAutoProcScalingId() > 0)
				autoProcScalingVO = autoProcScalingService.findByPk(vo.getAutoProcScalingId());
			autoProcScalingStatisticVO.fillVOFromWS(vo);
			autoProcScalingStatisticVO.setAutoProcScalingVO(autoProcScalingVO);
			autoProcScalingStatisticVO.setRecordTimeStamp(StringUtils.getCurrentTimeStamp());

			if (autoProcScalingStatisticId == null || autoProcScalingStatisticId == 0) {
				autoProcScalingStatisticVO.setAutoProcScalingStatisticsId(null);
				autoProcScalingStatisticValue = autoProcScalingStatisticsService.create(autoProcScalingStatisticVO);
			} else {
				autoProcScalingStatisticValue = autoProcScalingStatisticsService.update(autoProcScalingStatisticVO);
			}
			LOG.info("storeOrUpdateAutoProcScalingStatisticValue "
					+ autoProcScalingStatisticValue.getAutoProcScalingStatisticsId());
			return autoProcScalingStatisticValue.getAutoProcScalingStatisticsId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateAutoProcScalingStatisticValue - " + StringUtils.getCurrentDate() + " - "
					+ vo.toWSString());
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "autoProcScalingStatisticsId")
	public Integer storeOrUpdateAutoProcScalingStatistics(Integer autoProcScalingStatisticsId,
			@WebParam(name = "scalingStatisticsType") String scalingStatisticsType,
			@WebParam(name = "comments") String comments,
			@WebParam(name = "resolutionLimitLow") Float resolutionLimitLow,
			@WebParam(name = "resolutionLimitHigh") Float resolutionLimitHigh, @WebParam(name = "rmerge") Float rmerge,
			@WebParam(name = "rmeasWithinIplusIminus") Float rmeasWithinIplusIminus,
			@WebParam(name = "rmeasAllIplusIminus") Float rmeasAllIplusIminus,
			@WebParam(name = "rpimWithinIplusIminus") Float rpimWithinIplusIminus,
			@WebParam(name = "rpimAllIplusIminus") Float rpimAllIplusIminus,
			@WebParam(name = "fractionalPartialBias") Float fractionalPartialBias,
			@WebParam(name = "nTotalObservations") Integer nTotalObservations,
			@WebParam(name = "nTotalUniqueObservations") Integer nTotalUniqueObservations,
			@WebParam(name = "meanIoverSigI") Float meanIoverSigI, 
			@WebParam(name = "completeness") Float completeness,
			@WebParam(name = "multiplicity") Float multiplicity,
			@WebParam(name = "anomalousCompleteness") Float anomalousCompleteness,
			@WebParam(name = "anomalousMultiplicity") Float anomalousMultiplicity,
			@WebParam(name = "recordTimeStamp") Date recordTimeStamp, @WebParam(name = "anomalous") Boolean anomalous,
			@WebParam(name = "autoProcScalingId") Integer autoProcScalingId, 
			@WebParam(name = "ccHalf") Float ccHalf,
			@WebParam(name = "sigAno") Float sigAno,
			@WebParam(name = "ccAno") Float ccAno,
			@WebParam(name = "isa") Float isa, 
			@WebParam(name = "completenessSpherical") Float completenessSpherical,
			@WebParam(name = "anomalousCompletenessSpherical") Float anomalousCompletenessSpherical, 
			@WebParam(name = "completenessEllipsoidal") Float completenessEllipsoidal,
			@WebParam(name = "anomalousCompletenessEllipsoidal") Float anomalousCompletenessEllipsoidal)
			throws Exception {
		LOG.info("storeOrUpdateAutoProcScalingStatistics");
		
		AutoProcScalingStatisticsWS3VO vo = new AutoProcScalingStatisticsWS3VO(null, autoProcScalingId,
				scalingStatisticsType, comments, resolutionLimitLow, resolutionLimitHigh, rmerge,
				rmeasWithinIplusIminus, rmeasAllIplusIminus, rpimWithinIplusIminus, rpimAllIplusIminus,
				fractionalPartialBias, nTotalObservations, nTotalUniqueObservations, meanIoverSigI, completeness,
				multiplicity, anomalousCompleteness, anomalousMultiplicity, recordTimeStamp, anomalous, ccHalf, sigAno, ccAno, isa
				, completenessSpherical, anomalousCompletenessSpherical,  completenessEllipsoidal, anomalousCompletenessEllipsoidal);
		return storeOrUpdateAutoProcScalingStatisticValue(vo);
	}

	private Integer storeOrUpdateAutoProcIntegrationValue(AutoProcIntegrationWS3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdateAutoProcIntegrationValue");
			if (vo == null)
				return null;

			if (vo.getDataCollectionId() == null) {
				LOG.debug("WS PB : DataCollectionId is null , could not create autoProcIntegration");
				return errorCodeFK;
			}
			// autoProcProgramId can be null
			// if (vo.getAutoProcProgramId() == null) {
			// LOG.debug("WS PB : autoProcProgramId is null , could not create autoProcIntegration");
			// return errorCodeFK;
			// }
			AutoProcIntegration3VO autoProcIntegrationValue = null;
			AutoProcIntegration3Service autoProcIntegrationService = (AutoProcIntegration3Service) ejb3ServiceLocator
					.getLocalService(AutoProcIntegration3Service.class);

			Integer autoProcIntegrationId = vo.getAutoProcIntegrationId();

			AutoProcIntegration3VO autoProcIntegrationVO = new AutoProcIntegration3VO();
			AutoProcProgram3Service autoProcProgramService = (AutoProcProgram3Service) ejb3ServiceLocator
					.getLocalService(AutoProcProgram3Service.class);
			AutoProcProgram3VO autoProcProgramVO = null;
			if (vo.getAutoProcProgramId() != null && vo.getAutoProcProgramId() > 0)
				autoProcProgramVO = autoProcProgramService.findByPk(vo.getAutoProcProgramId(), false);
			DataCollection3Service dataCollectionService = (DataCollection3Service) ejb3ServiceLocator
					.getLocalService(DataCollection3Service.class);
			DataCollection3VO dataCollectionVO = null;
			if (vo.getDataCollectionId() != null && vo.getDataCollectionId() > 0)
				dataCollectionVO = dataCollectionService.findByPk(vo.getDataCollectionId(), false, false);
			// load the object elsewhere there is an error with the childs
			if (vo.getAutoProcIntegrationId() != null && vo.getAutoProcIntegrationId() > 0) {
				autoProcIntegrationVO = autoProcIntegrationService.findByPk(vo.getAutoProcIntegrationId());
			}
			autoProcIntegrationVO.fillVOFromWS(vo);
			autoProcIntegrationVO.setAutoProcProgramVO(autoProcProgramVO);
			autoProcIntegrationVO.setDataCollectionVO(dataCollectionVO);
			autoProcIntegrationVO.setRecordTimeStamp(StringUtils.getCurrentTimeStamp());

			if (autoProcIntegrationId == null || autoProcIntegrationId == 0) {
				autoProcIntegrationVO.setAutoProcIntegrationId(null);
				autoProcIntegrationValue = autoProcIntegrationService.create(autoProcIntegrationVO);
			} else {
				autoProcIntegrationValue = autoProcIntegrationService.update(autoProcIntegrationVO);
			}
			// update session
			if (dataCollectionVO != null)
				ToolsForCollectionWebService.updateSession(dataCollectionVO.getDataCollectionGroupVO().getSessionVO());
			LOG.debug("storeOrUpdateAutoProcIntegrationValue " + autoProcIntegrationValue.getAutoProcIntegrationId());
			return autoProcIntegrationValue.getAutoProcIntegrationId();

		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateAutoProcIntegrationValue - " + StringUtils.getCurrentDate() + " - "
					+ vo.toWSString());
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "autoProcIntegrationId")
	public Integer storeOrUpdateAutoProcIntegration(Integer autoProcIntegrationId,
			@WebParam(name = "autoProcProgramId") Integer autoProcProgramId,
			@WebParam(name = "startImageNumber") Integer startImageNumber,
			@WebParam(name = "endImageNumber") Integer endImageNumber,
			@WebParam(name = "refinedDetectorDistance") Float refinedDetectorDistance,
			@WebParam(name = "refinedXbeam") Float refinedXbeam, @WebParam(name = "refinedYbeam") Float refinedYbeam,
			@WebParam(name = "rotationAxisX") Float rotationAxisX,
			@WebParam(name = "rotationAxisY") Float rotationAxisY,
			@WebParam(name = "rotationAxisZ") Float rotationAxisZ, @WebParam(name = "beamVectorX") Float beamVectorX,
			@WebParam(name = "beamVectorY") Float beamVectorY, @WebParam(name = "beamVectorZ") Float beamVectorZ,
			@WebParam(name = "cellA") Float cellA, @WebParam(name = "cellB") Float cellB,
			@WebParam(name = "cellC") Float cellC, @WebParam(name = "cellAlpha") Float cellAlpha,
			@WebParam(name = "cellBeta") Float cellBeta, @WebParam(name = "cellGamma") Float cellGamma,
			@WebParam(name = "recordTimeStamp") Date recordTimeStamp, @WebParam(name = "anomalous") Boolean anomalous,
			@WebParam(name = "dataCollectionId") Integer dataCollectionId) throws Exception {
		AutoProcIntegrationWS3VO vo = new AutoProcIntegrationWS3VO(autoProcIntegrationId, dataCollectionId,
				autoProcProgramId, startImageNumber, endImageNumber, refinedDetectorDistance, refinedXbeam,
				refinedYbeam, rotationAxisX, rotationAxisY, rotationAxisZ, beamVectorX, beamVectorY, beamVectorZ,
				cellA, cellB, cellC, cellAlpha, cellBeta, cellGamma, recordTimeStamp, anomalous);
		return storeOrUpdateAutoProcIntegrationValue(vo);
	}

	private Integer storeOrUpdateAutoProcProgramAttachmentValue(AutoProcProgramAttachmentWS3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdateAutoProcProgramAttachmentValue");
			AutoProcProgramAttachment3VO autoProcProgramAttachmentValue = null;
			AutoProcProgramAttachment3Service autoProcProgramAttachmentService = (AutoProcProgramAttachment3Service) ejb3ServiceLocator
					.getLocalService(AutoProcProgramAttachment3Service.class);

			if (vo == null)
				return null;

			if (vo.getAutoProcProgramId() == null) {
				LOG.debug("WS PB : autoProcProgramId is null , could not create autoProcProgramAttachment");
				return errorCodeFK;
			}
			
			//default fileType: Result
			if (vo.getFileType() == null || vo.getFileType().trim().equals("")){
				vo.setFileType(Constants.AUTOPROC_ATTACHMENT_DEFAULT_FILETYPE);
			}
			Integer autoProcProgramAttachmentId = vo.getAutoProcProgramAttachmentId();

			AutoProcProgramAttachment3VO autoProcProgramAttachmentVO = new AutoProcProgramAttachment3VO();
			AutoProcProgram3Service autoProcProgramService = (AutoProcProgram3Service) ejb3ServiceLocator
					.getLocalService(AutoProcProgram3Service.class);
			AutoProcProgram3VO autoProcProgramVO = null;
			if (vo.getAutoProcProgramId() != null && vo.getAutoProcProgramId() > 0)
				autoProcProgramVO = autoProcProgramService.findByPk(vo.getAutoProcProgramId(), false);
			autoProcProgramAttachmentVO.fillVOFromWS(vo);
			autoProcProgramAttachmentVO.setAutoProcProgramVO(autoProcProgramVO);
			autoProcProgramAttachmentVO.setRecordTimeStamp(StringUtils.getCurrentTimeStamp());

			autoProcProgramAttachmentVO.checkValues(autoProcProgramAttachmentId == null
					|| autoProcProgramAttachmentId == 0);

			if (autoProcProgramAttachmentId == null || autoProcProgramAttachmentId == 0) {
				autoProcProgramAttachmentVO.setAutoProcProgramAttachmentId(null);
				autoProcProgramAttachmentValue = autoProcProgramAttachmentService.create(autoProcProgramAttachmentVO);
			} else {
				autoProcProgramAttachmentValue = autoProcProgramAttachmentService.update(autoProcProgramAttachmentVO);
			}
			LOG.debug("storeOrUpdateAutoProcProgramAttachmentValue "
					+ autoProcProgramAttachmentValue.getAutoProcProgramAttachmentId());

			return autoProcProgramAttachmentValue.getAutoProcProgramAttachmentId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateAutoProcProgramAttachmentValue - " + StringUtils.getCurrentDate() + " - "
					+ vo.toWSString());
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "autoProcProgramAttachmentId")
	public Integer storeOrUpdateAutoProcProgramAttachment(Integer autoProcProgramAttachmentId,
			@WebParam(name = "fileType") String fileType, @WebParam(name = "fileName") String fileName,
			@WebParam(name = "filePath") String filePath, @WebParam(name = "recordTimeStamp") Date recordTimeStamp,
			@WebParam(name = "autoProcProgramId") Integer autoProcProgramId) throws Exception {
		AutoProcProgramAttachmentWS3VO vo = new AutoProcProgramAttachmentWS3VO(autoProcProgramAttachmentId,
				autoProcProgramId, fileType, fileName, filePath, recordTimeStamp);
		return storeOrUpdateAutoProcProgramAttachmentValue(vo);
	}

	private Integer storeOrUpdateAutoProcProgramValue(AutoProcProgram3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdateAutoProcProgramValue");
			if (vo == null)
				return null;

			AutoProcProgram3VO autoProcProgramValue = null;
			AutoProcProgram3Service autoProcProgramService = (AutoProcProgram3Service) ejb3ServiceLocator
					.getLocalService(AutoProcProgram3Service.class);

			Integer autoProcProgramId = vo.getAutoProcProgramId();
			AutoProcProgram3VO autoProcProgram = new AutoProcProgram3VO();
			// load the object elsewhere there is an error with the childs
			if (autoProcProgramId != null && autoProcProgramId > 0) {
				autoProcProgram = autoProcProgramService.findByPk(autoProcProgramId, false);
			}
			autoProcProgram.setAutoProcProgramId(autoProcProgramId);
			autoProcProgram.setProcessingCommandLine(vo.getProcessingCommandLine());
			autoProcProgram.setProcessingPrograms(vo.getProcessingPrograms());
			autoProcProgram.setProcessingStatus(vo.getProcessingStatus());
			autoProcProgram.setProcessingMessage(vo.getProcessingMessage());
			autoProcProgram.setProcessingStartTime(vo.getProcessingStartTime());
			autoProcProgram.setProcessingEndTime(vo.getProcessingEndTime());
			autoProcProgram.setProcessingEnvironment(vo.getProcessingEnvironment());
			autoProcProgram.setRecordTimeStamp(StringUtils.getCurrentTimeStamp());
			if (autoProcProgramId == null || autoProcProgramId == 0) {
				autoProcProgram.setAutoProcProgramId(null);
				autoProcProgramValue = autoProcProgramService.create(autoProcProgram);
			} else {
				autoProcProgramValue = autoProcProgramService.update(autoProcProgram);
			}
			LOG.debug("storeOrUpdateAutoProcProgramValue " + autoProcProgramValue.getAutoProcProgramId());

			return autoProcProgramValue.getAutoProcProgramId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateAutoProcProgramValue - " + StringUtils.getCurrentDate() + " - "
					+ vo.toWSString());
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "autoProcProgramId")
	public Integer storeOrUpdateAutoProcProgram(Integer autoProcProgramId,
			@WebParam(name = "processingCommandLine") String processingCommandLine,
			@WebParam(name = "processingPrograms") String processingPrograms,
			@WebParam(name = "processingStatus") String processingStatus,
			@WebParam(name = "processingMessage") String processingMessage,
			@WebParam(name = "processingStartTime") Date processingStartTime,
			@WebParam(name = "processingEndTime") Date processingEndTime,
			@WebParam(name = "processingEnvironment") String processingEnvironment,
			@WebParam(name = "recordTimeStamp") Date recordTimeStamp) throws Exception {
		AutoProcProgram3VO vo = new AutoProcProgram3VO(autoProcProgramId, processingCommandLine, processingPrograms,
				processingStatus, processingMessage, processingStartTime, processingEndTime, processingEnvironment,
				recordTimeStamp);
		return storeOrUpdateAutoProcProgramValue(vo);
	}

	@WebMethod
	@WebResult(name = "imageQualityIndicatorsId")
	public Integer storeOrUpdateImageQualityIndicators(ImageQualityIndicatorsWS3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdateImageQualityIndicators");
			if (vo == null)
				return null;

			if (vo.getImageId() == null) {
				LOG.debug("WS PB : ImageId is null , could not update/create ImageQualityIndicators");
				return errorCodeFK;
			}

			if (vo.getAutoProcProgramId() == null) {
				LOG.debug("WS PB : autoProcProgramId is null , could not update/create ImageQualityIndicators");
				return errorCodeFK;
			}

			ImageQualityIndicators3VO imageQualityIndicatorsValue = null;
			ImageQualityIndicators3Service imageQualityIndicatorsService = (ImageQualityIndicators3Service) ejb3ServiceLocator
					.getLocalService(ImageQualityIndicators3Service.class);

			if (vo.getImageQualityIndicatorsId() == null || vo.getImageQualityIndicatorsId() == 0)
				vo.setImageQualityIndicatorsId(null);
			ImageQualityIndicators3VO imageQualityIndicatorsVO = new ImageQualityIndicators3VO();
			Image3Service imageService = (Image3Service) ejb3ServiceLocator.getLocalService(Image3Service.class);
			Image3VO imageVO = null;
			if (vo.getImageId() != null && vo.getImageId() > 0)
				imageVO = imageService.findByPk(vo.getImageId());

			AutoProcProgram3Service autoProcProgramService = (AutoProcProgram3Service) ejb3ServiceLocator
					.getLocalService(AutoProcProgram3Service.class);
			AutoProcProgram3VO autoProcProgramVO = null;
			if (vo.getAutoProcProgramId() != null && vo.getAutoProcProgramId() > 0)
				autoProcProgramVO = autoProcProgramService.findByPk(vo.getAutoProcProgramId(), false);
			imageQualityIndicatorsVO.fillVOFromWS(vo);
			imageQualityIndicatorsVO.setImageVO(imageVO);
			imageQualityIndicatorsVO.setAutoProcProgramVO(autoProcProgramVO);
			imageQualityIndicatorsVO.setRecordTimeStamp(StringUtils.getCurrentTimeStamp());

			Integer imageQualityIndicatorsId = vo.getImageQualityIndicatorsId();
			if (imageQualityIndicatorsId == null || imageQualityIndicatorsId == 0) {
				imageQualityIndicatorsVO.setImageQualityIndicatorsId(null);
				imageQualityIndicatorsValue = imageQualityIndicatorsService.create(imageQualityIndicatorsVO);
			} else {
				imageQualityIndicatorsValue = imageQualityIndicatorsService.update(imageQualityIndicatorsVO);
			}
			LOG.debug("storeOrUpdateImageQualityIndicators "
					+ imageQualityIndicatorsValue.getImageQualityIndicatorsId());

			return imageQualityIndicatorsValue.getImageQualityIndicatorsId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateImageQualityIndicators - " + StringUtils.getCurrentDate() + " - "
					+ vo.toWSString());
			throw e;
		}

	}

	@WebMethod
	@WebResult(name = "ImageQualityIndicatorsId")
	public Integer storeImageQualityIndicators(@WebParam(name = "fileLocation") java.lang.String fileLocation,
			@WebParam(name = "fileName") java.lang.String fileName, @WebParam(name = "imageId") Integer imageId,
			@WebParam(name = "autoProcProgramId") Integer autoProcProgramId,
			@WebParam(name = "spotTotal") Integer spotTotal, @WebParam(name = "inResTotal") Integer inResTotal,
			@WebParam(name = "goodBraggCandidates") Integer goodBraggCandidates,
			@WebParam(name = "iceRings") Integer iceRings, @WebParam(name = "method1res") Float method1res,
			@WebParam(name = "method2res") Float method2res, @WebParam(name = "maxUnitCell") Float maxUnitCell,
			@WebParam(name = "pctSaturationTop50peaks") Float pctSaturationTop50peaks,
			@WebParam(name = "inResolutionOvrlSpots") Integer inResolutionOvrlSpots,
			@WebParam(name = "binPopCutOffMethod2res") Float binPopCutOffMethod2res,
			@WebParam(name = "recordTimeStamp") Date recordTimeStamp, 
			@WebParam(name = "totalIntegratedSignal") Double totalIntegratedSignal, 
			@WebParam(name = "dozor_score") Double dozor_score) throws Exception {
		try {
			if (autoProcProgramId == null || autoProcProgramId == 0)
				autoProcProgramId = null;

			if (imageId == null || imageId == 0) {
				Image3Service imageService = (Image3Service) ejb3ServiceLocator.getLocalService(Image3Service.class);
				List<Image3VO> listImg = imageService.findFiltered(fileLocation, fileName);
				if (listImg != null && listImg.size() > 0)
					imageId = listImg.get(0).getImageId();
				else{
					// try to add or remove the last  / -- it seems that there is no rule  with this.
					if (fileLocation != null && fileLocation.length()> 1){
						char lastCharact = fileLocation.charAt(fileLocation.length() -1);
						if (lastCharact == '/'){
							listImg = imageService.findFiltered(fileLocation.substring(0, fileLocation.length()-1), fileName);
						}else{
							listImg = imageService.findFiltered(fileLocation+"/", fileName);
						}
						if (listImg != null && listImg.size() > 0)
							imageId = listImg.get(0).getImageId();
					}
				}
			}
			if (imageId == null) {
				LOG.debug("WS PB : ImageId is null , could not create ImageQualityIndicators");
				return errorCodeFK;
			}

			if (autoProcProgramId == null) {
				LOG.debug("WS PB : autoProcProgramId is null , could not create ImageQualityIndicators");
				return errorCodeFK;
			}

			return this.storeImageQualityIndicatorsForImageId(imageId, autoProcProgramId, spotTotal, inResTotal,
					goodBraggCandidates, iceRings, method1res, method2res, maxUnitCell, pctSaturationTop50peaks,
					inResolutionOvrlSpots, binPopCutOffMethod2res, totalIntegratedSignal, dozor_score, recordTimeStamp);

		} catch (Exception e) {
			LOG.error("WS ERROR: storeImageQualityIndicators - " + StringUtils.getCurrentDate() + " - " + fileLocation
					+ ", " + fileName + ", " + imageId + ", " + autoProcProgramId + ", " + spotTotal + ", "
					+ inResTotal + ", " + goodBraggCandidates + ", " + iceRings + ", " + method1res + ", " + method2res
					+ ", " + maxUnitCell + ", " + pctSaturationTop50peaks + ", " + inResolutionOvrlSpots + ", "
					+ binPopCutOffMethod2res + ", " + totalIntegratedSignal + ", " +dozor_score + ", " +recordTimeStamp);
			throw e;
		}

	}
	
	@WebMethod
	@WebResult(name = "imageQualityIndicatorsId")
	public Integer storeOrUpdateImageQualityIndicatorsForFileName(@WebParam(name = "fileLocation") java.lang.String fileLocation,
			@WebParam(name = "fileName") java.lang.String fileName, @WebParam(name = "imageId") Integer imageId,
			@WebParam(name = "autoProcProgramId") Integer autoProcProgramId,
			@WebParam(name = "spotTotal") Integer spotTotal, @WebParam(name = "inResTotal") Integer inResTotal,
			@WebParam(name = "goodBraggCandidates") Integer goodBraggCandidates,
			@WebParam(name = "iceRings") Integer iceRings, @WebParam(name = "method1Res") Float method1Res,
			@WebParam(name = "method2Res") Float method2Res, @WebParam(name = "maxUnitCell") Float maxUnitCell,
			@WebParam(name = "pctSaturationTop50Peaks") Float pctSaturationTop50Peaks,
			@WebParam(name = "inResolutionOvrlSpots") Integer inResolutionOvrlSpots,
			@WebParam(name = "binPopCutOffMethod2Res") Float binPopCutOffMethod2Res,
			@WebParam(name = "totalIntegratedSignal") Double totalIntegratedSignal, 
			@WebParam(name = "dozor_score") Double dozor_score) throws Exception {
		try {
			if (autoProcProgramId == null || autoProcProgramId == 0)
				autoProcProgramId = null;

			if (imageId == null || imageId == 0) {
				imageId = null;
				Image3Service imageService = (Image3Service) ejb3ServiceLocator.getLocalService(Image3Service.class);
				List<Image3VO> listImg = imageService.findFiltered(fileLocation, fileName);
				if (listImg != null && listImg.size() > 0)
					imageId = listImg.get(0).getImageId();
				else{
					// try to add or remove the last  / -- it seems that there is no rule  with this.
					if (fileLocation != null && fileLocation.length()> 1){
						char lastCharact = fileLocation.charAt(fileLocation.length() -1);
						if (lastCharact == '/'){
							listImg = imageService.findFiltered(fileLocation.substring(0, fileLocation.length()-1), fileName);
						}else{
							listImg = imageService.findFiltered(fileLocation+"/", fileName);
						}
						if (listImg != null && listImg.size() > 0)
							imageId = listImg.get(0).getImageId();
					}
				}
			}
			if (imageId == null) {
				LOG.debug("WS PB : ImageId is null , could not create ImageQualityIndicators "+fileLocation+", "+fileName);
				return errorCodeFK;
			}

			if (autoProcProgramId == null) {
				LOG.debug("WS PB : autoProcProgramId is null , could not create ImageQualityIndicators");
				return errorCodeFK;
			}
			
			Integer imageQualityIndicatorsId = null;
			ImageQualityIndicators3Service imageQualityIndicatorsService = (ImageQualityIndicators3Service) ejb3ServiceLocator.getLocalService(ImageQualityIndicators3Service.class);

			List<ImageQualityIndicators3VO> listImgQualityInd = imageQualityIndicatorsService.findByImageId(imageId);
			if (listImgQualityInd != null && listImgQualityInd.size() > 0){
				imageQualityIndicatorsId = listImgQualityInd.get(0).getImageQualityIndicatorsId();
			}

			ImageQualityIndicatorsWS3VO vo = new ImageQualityIndicatorsWS3VO(imageQualityIndicatorsId,
					imageId, autoProcProgramId, spotTotal,
					inResTotal, goodBraggCandidates, iceRings,
					method1Res, method2Res, maxUnitCell,
					pctSaturationTop50Peaks, inResolutionOvrlSpots, binPopCutOffMethod2Res,
					StringUtils.getCurrentTimeStamp(), totalIntegratedSignal, dozor_score);

			return storeOrUpdateImageQualityIndicators(vo);

		} catch (Exception e) {
			LOG.error("WS ERROR: storeImageQualityIndicators - " + StringUtils.getCurrentDate() + " - " + fileLocation
					+ ", " + fileName + ", " + imageId + ", " + autoProcProgramId + ", " + spotTotal + ", "
					+ inResTotal + ", " + goodBraggCandidates + ", " + iceRings + ", " + method1Res + ", " + method2Res
					+ ", " + maxUnitCell + ", " + pctSaturationTop50Peaks + ", " + inResolutionOvrlSpots + ", "
					+ binPopCutOffMethod2Res + ", " + totalIntegratedSignal+ ", " + dozor_score);
			throw e;
		}

	}

	private Integer storeImageQualityIndicatorsForImageId(Integer imageId, Integer autoProcProgramId,
			Integer spotTotal, Integer inResTotal, Integer goodBraggCandidates, Integer iceRings, Float method1res,
			Float method2res, Float maxUnitCell, Float pctSaturationTop50peaks, Integer inResolutionOvrlSpots,
			Float binPopCutOffMethod2res, Double totalIntegratedSignal, Double dozor_score, Date recordTimeStamp) throws Exception {

		try {
			if (autoProcProgramId == null || autoProcProgramId == 0)
				autoProcProgramId = null;
			if (autoProcProgramId == null) {
				LOG.debug("WS PB : autoProcProgramId is null , could not create ImageQualityIndicators");
				return -1;
			}
			if (imageId == null) {
				LOG.debug("WS PB : imageId is null , could not create ImageQualityIndicators");
				return -1;
			}
			// do not erase if exists, return error code -1
			ImageQualityIndicators3Service imageQualityIndicatorsService = (ImageQualityIndicators3Service) ejb3ServiceLocator
					.getLocalService(ImageQualityIndicators3Service.class);

			// do not erase if exists, return error code -1
			List<ImageQualityIndicators3VO> listImgQualityInd = imageQualityIndicatorsService.findByImageId(imageId);
			if (listImgQualityInd != null && listImgQualityInd.size() > 0)
				return new Integer(-1);

			ImageQualityIndicatorsWS3VO vo = new ImageQualityIndicatorsWS3VO(null, imageId, autoProcProgramId,
					spotTotal, inResTotal, goodBraggCandidates, iceRings, method1res, method2res, maxUnitCell,
					pctSaturationTop50peaks, inResolutionOvrlSpots, binPopCutOffMethod2res,
					StringUtils.getCurrentTimeStamp(), totalIntegratedSignal, dozor_score);

			return storeOrUpdateImageQualityIndicators(vo);
		} catch (Exception e) {
			LOG.error("WS ERROR: storeImageQualityIndicatorsForImageId - " + StringUtils.getCurrentDate() + " - "
					+ imageId + ", " + autoProcProgramId + ", " + spotTotal + ", " + inResTotal + ", "
					+ goodBraggCandidates + ", " + iceRings + ", " + method1res + ", " + method2res + ", "
					+ maxUnitCell + ", " + pctSaturationTop50peaks + ", " + inResolutionOvrlSpots + ", "
					+ binPopCutOffMethod2res + ", " + totalIntegratedSignal + ", " +dozor_score + ", " +recordTimeStamp);
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "autoProcStatusId")
	public Integer storeOrUpdateAutoProcStatus(java.lang.Integer autoProcStatusId,
			@WebParam(name = "autoProcIntegrationId") java.lang.Integer autoProcIntegrationId,
			@WebParam(name = "step") java.lang.String step, @WebParam(name = "status") java.lang.String status,
			@WebParam(name = "comments") java.lang.String comments, @WebParam(name = "bltimeStamp") Date bltimeStamp)
			throws Exception {
		AutoProcStatusWS3VO vo = new AutoProcStatusWS3VO(autoProcStatusId, autoProcIntegrationId, step, status,
				comments, bltimeStamp);
		return storeOrUpdateAutoProcStatusValue(vo);
	}

	
	private Integer storeOrUpdateAutoProcStatusValue(AutoProcStatusWS3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdateAutoProcStatusValue");
			if (vo == null)
				return null;

			if (vo.getAutoProcIntegrationId() == null) {
				LOG.debug("WS PB : AutoProcIntegrationId is null , could not create autoProcStatus");
				return errorCodeFK;
			}

			AutoProcStatus3VO autoProcStatusValue = null;
			AutoProcStatus3Service autoProcStatusService = (AutoProcStatus3Service) ejb3ServiceLocator
					.getLocalService(AutoProcStatus3Service.class);

			if (vo.getAutoProcIntegrationId() == null || vo.getAutoProcIntegrationId() == 0)
				vo.setAutoProcIntegrationId(null);
			AutoProcStatus3VO autoProcStatusVO = new AutoProcStatus3VO();
			AutoProcIntegration3Service autoProcIntegrationService = (AutoProcIntegration3Service) ejb3ServiceLocator
					.getLocalService(AutoProcIntegration3Service.class);
			AutoProcIntegration3VO autoProcIntegrationVO = null;
			if (vo.getAutoProcIntegrationId() != null && vo.getAutoProcIntegrationId() > 0)
				autoProcIntegrationVO = autoProcIntegrationService.findByPk(vo.getAutoProcIntegrationId());

			Integer autoProcStatusId = vo.getAutoProcStatusId();
			// load the object elsewhere there is an error with the childs
			if (autoProcStatusId != null && autoProcStatusId > 0) {
				autoProcStatusVO = autoProcStatusService.findByPk(autoProcStatusId);
			}
			autoProcStatusVO.fillVOFromWS(vo);
			autoProcStatusVO.setAutoProcIntegrationVO(autoProcIntegrationVO);
			autoProcStatusVO.setBlTimeStamp(StringUtils.getCurrentTimeStamp());

			if (autoProcStatusId == null || autoProcStatusId == 0) {
				autoProcStatusVO.setAutoProcStatusId(null);
				autoProcStatusValue = autoProcStatusService.create(autoProcStatusVO);
			} else {
				autoProcStatusValue = autoProcStatusService.update(autoProcStatusVO);
			}
			LOG.debug("storeOrUpdateAutoProcStatusValue " + autoProcStatusValue.getAutoProcStatusId());
			return autoProcStatusValue.getAutoProcStatusId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateAutoProcStatusValue - " + StringUtils.getCurrentDate() + " - "
					+ vo.toWSString());
			throw e;
		}
	}

	
	@WebMethod
	@WebResult(name = "phasingProgramRunId")
	public Integer storeOrUpdatePhasingProgramRun(PhasingProgramRun3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdatePhasingProgramRun");
			if (vo == null)
				return null;

			PhasingProgramRun3VO phasingProgramRunValue = null;
			PhasingProgramRun3Service phasingProgramRunService = (PhasingProgramRun3Service) ejb3ServiceLocator
					.getLocalService(PhasingProgramRun3Service.class);

			Integer phasingProgramRunId = vo.getPhasingProgramRunId();
			PhasingProgramRun3VO phasingProgramRun = new PhasingProgramRun3VO();
			// load the object elsewhere there is an error with the childs
			if (phasingProgramRunId != null && phasingProgramRunId > 0) {
				phasingProgramRun = phasingProgramRunService.findByPk(phasingProgramRunId, false);
			}
			phasingProgramRun.setPhasingCommandLine(vo.getPhasingCommandLine());
			phasingProgramRun.setPhasingPrograms(vo.getPhasingPrograms());
			phasingProgramRun.setPhasingStatus(vo.getPhasingStatus());
			phasingProgramRun.setPhasingMessage(vo.getPhasingMessage());
			phasingProgramRun.setPhasingStartTime(vo.getPhasingStartTime());
			phasingProgramRun.setPhasingEndTime(vo.getPhasingEndTime());
			phasingProgramRun.setPhasingEnvironment(vo.getPhasingEnvironment());
			phasingProgramRun.setPhasingDirectory(vo.getPhasingDirectory());
			phasingProgramRun.setRecordTimeStamp(StringUtils.getCurrentTimeStamp());
			if (phasingProgramRunId == null || phasingProgramRunId == 0) {
				phasingProgramRun.setPhasingProgramRunId(null);
				phasingProgramRunValue = phasingProgramRunService.create(phasingProgramRun);
			} else {
				phasingProgramRunValue = phasingProgramRunService.update(phasingProgramRun);
			}
			LOG.debug("storeOrUpdatePhasingProgramRun " + phasingProgramRunValue.getPhasingProgramRunId());

			return phasingProgramRunValue.getPhasingProgramRunId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdatePhasingProgramRun - " + StringUtils.getCurrentDate() + " - "
					+ vo.toWSString());
			throw e;
		}
	}
	
	@WebMethod
	@WebResult(name = "phasingAnalysisId")
	public Integer storeOrUpdatePhasingAnalysis(PhasingAnalysis3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdatePhasingAnalysis");
			if (vo == null)
				return null;

			
			PhasingAnalysis3VO phasingAnalysisValue = null;
			PhasingAnalysis3Service phasingAnalysisService = (PhasingAnalysis3Service) ejb3ServiceLocator.getLocalService(PhasingAnalysis3Service.class);

			Integer phasingAnalysisId = vo.getPhasingAnalysisId();
			if (vo.getPhasingAnalysisId() == null || vo.getPhasingAnalysisId() == 0) {
				vo.setPhasingAnalysisId(null);
			}
			vo.setRecordTimeStamp(StringUtils.getCurrentTimeStamp());

			if (phasingAnalysisId == null || phasingAnalysisId == 0) {
				vo.setPhasingAnalysisId(null);
				phasingAnalysisValue = phasingAnalysisService.create(vo);
			} else {
				phasingAnalysisValue = phasingAnalysisService.update(vo);
			}
			LOG.debug("storeOrUpdatePhasingAnalysis " + phasingAnalysisValue.getPhasingAnalysisId());
			return phasingAnalysisValue.getPhasingAnalysisId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdatePhasingAnalysis - " + StringUtils.getCurrentDate() + " - "
					+ vo.toString());
			throw e;
		}
	}
	
	@WebMethod
	@WebResult(name = "preparePhasingDataId")
	public Integer storeOrUpdatePreparePhasingData(PreparePhasingDataWS3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdatePreparePhasingData");
			if (vo == null)
				return null;

			if (vo.getPhasingProgramRunId() == null) {
				LOG.debug("WS PB : PhasingProgramRunId is null , could not create preparePhasingData");
				return errorCodeFK;
			}
			if (vo.getPhasingAnalysisId() == null) {
				LOG.debug("WS PB : PhasingAnalysisId is null , could not create preparePhasingData");
				return errorCodeFK;
			}
			
			PreparePhasingData3VO preparePhasingDataValue = null;
			PreparePhasingData3Service preparePhasingDataService = (PreparePhasingData3Service) ejb3ServiceLocator.getLocalService(PreparePhasingData3Service.class);

			Integer preparePhasingDataId = vo.getPreparePhasingDataId();
			if (vo.getPreparePhasingDataId() == null || vo.getPreparePhasingDataId() == 0) {
				vo.setPreparePhasingDataId(null);
			}

			PreparePhasingData3VO preparePhasingDataVO = new PreparePhasingData3VO();
			PhasingAnalysis3Service phasingAnalysisService = (PhasingAnalysis3Service) ejb3ServiceLocator.getLocalService(PhasingAnalysis3Service.class);
			PhasingAnalysis3VO phasingAnalysisVO = null;
			if (vo.getPhasingAnalysisId() != null && vo.getPhasingAnalysisId() > 0)
				phasingAnalysisVO = phasingAnalysisService.findByPk(vo.getPhasingAnalysisId());
			PhasingProgramRun3Service phasingProgramRunService = (PhasingProgramRun3Service) ejb3ServiceLocator.getLocalService(PhasingProgramRun3Service.class);
			PhasingProgramRun3VO phasingProgramRunVO = null;
			if (vo.getPhasingProgramRunId() != null && vo.getPhasingProgramRunId() > 0)
				phasingProgramRunVO = phasingProgramRunService.findByPk(vo.getPhasingProgramRunId(), false);
			SpaceGroup3Service spaceGroupService = (SpaceGroup3Service) ejb3ServiceLocator.getLocalService(SpaceGroup3Service.class);
			SpaceGroup3VO spaceGroupVO = null;
			if (vo.getSpaceGroupId() != null && vo.getSpaceGroupId() > 0)
				spaceGroupVO = spaceGroupService.findByPk(vo.getSpaceGroupId());
			preparePhasingDataVO.fillVOFromWS(vo);
			preparePhasingDataVO.setPhasingProgramRunVO(phasingProgramRunVO);
			preparePhasingDataVO.setPhasingAnalysisVO(phasingAnalysisVO);
			preparePhasingDataVO.setSpaceGroupVO(spaceGroupVO);
			preparePhasingDataVO.setRecordTimeStamp(StringUtils.getCurrentTimeStamp());

			if (preparePhasingDataId == null || preparePhasingDataId == 0) {
				preparePhasingDataVO.setPreparePhasingDataId(null);
				preparePhasingDataValue = preparePhasingDataService.create(preparePhasingDataVO);
			} else {
				preparePhasingDataValue = preparePhasingDataService.update(preparePhasingDataVO);
			}
			LOG.debug("storeOrUpdatePreparePhasingData " + preparePhasingDataValue.getPreparePhasingDataId());
			return preparePhasingDataValue.getPreparePhasingDataId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdatePreparePhasingData - " + StringUtils.getCurrentDate() + " - "
					+ vo.toWSString());
			throw e;
		}
	}
	
	@WebMethod
	@WebResult(name = "substructureDeterminationId")
	public Integer storeOrUpdateSubstructureDetermination(SubstructureDeterminationWS3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdateSubstructureDetermination");
			if (vo == null)
				return null;

			if (vo.getPhasingProgramRunId() == null) {
				LOG.debug("WS PB : PhasingProgramRunId is null , could not create substructureDetermination");
				return errorCodeFK;
			}
			if (vo.getPhasingAnalysisId() == null) {
				LOG.debug("WS PB : PhasingAnalysisId is null , could not create substructureDetermination");
				return errorCodeFK;
			}
			
			SubstructureDetermination3VO substructureDeterminationValue = null;
			SubstructureDetermination3Service substructureDeterminationService = (SubstructureDetermination3Service) ejb3ServiceLocator.getLocalService(SubstructureDetermination3Service.class);

			Integer substructureDeterminationId = vo.getSubstructureDeterminationId();
			if (vo.getSubstructureDeterminationId() == null || vo.getSubstructureDeterminationId() == 0) {
				vo.setSubstructureDeterminationId(null);
			}

			SubstructureDetermination3VO substructureDeterminationVO = new SubstructureDetermination3VO();
			PhasingAnalysis3Service phasingAnalysisService = (PhasingAnalysis3Service) ejb3ServiceLocator.getLocalService(PhasingAnalysis3Service.class);
			PhasingAnalysis3VO phasingAnalysisVO = null;
			if (vo.getPhasingAnalysisId() != null && vo.getPhasingAnalysisId() > 0)
				phasingAnalysisVO = phasingAnalysisService.findByPk(vo.getPhasingAnalysisId());
			PhasingProgramRun3Service phasingProgramRunService = (PhasingProgramRun3Service) ejb3ServiceLocator.getLocalService(PhasingProgramRun3Service.class);
			PhasingProgramRun3VO phasingProgramRunVO = null;
			if (vo.getPhasingProgramRunId() != null && vo.getPhasingProgramRunId() > 0)
				phasingProgramRunVO = phasingProgramRunService.findByPk(vo.getPhasingProgramRunId(), false);
			SpaceGroup3Service spaceGroupService = (SpaceGroup3Service) ejb3ServiceLocator.getLocalService(SpaceGroup3Service.class);
			SpaceGroup3VO spaceGroupVO = null;
			if (vo.getSpaceGroupId() != null && vo.getSpaceGroupId() > 0)
				spaceGroupVO = spaceGroupService.findByPk(vo.getSpaceGroupId());
			substructureDeterminationVO.fillVOFromWS(vo);
			substructureDeterminationVO.setPhasingProgramRunVO(phasingProgramRunVO);
			substructureDeterminationVO.setPhasingAnalysisVO(phasingAnalysisVO);
			substructureDeterminationVO.setSpaceGroupVO(spaceGroupVO);
			substructureDeterminationVO.setRecordTimeStamp(StringUtils.getCurrentTimeStamp());

			if (substructureDeterminationId == null || substructureDeterminationId == 0) {
				substructureDeterminationVO.setSubstructureDeterminationId(null);
				substructureDeterminationValue = substructureDeterminationService.create(substructureDeterminationVO);
			} else {
				substructureDeterminationValue = substructureDeterminationService.update(substructureDeterminationVO);
			}
			LOG.debug("storeOrUpdateSubstructureDetermination " + substructureDeterminationValue.getSubstructureDeterminationId());
			return substructureDeterminationValue.getSubstructureDeterminationId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateSubstructureDetermination - " + StringUtils.getCurrentDate() + " - "
					+ vo.toWSString());
			throw e;
		}
	}
	
	@WebMethod
	@WebResult(name = "phasingId")
	public Integer storeOrUpdatePhasing(PhasingWS3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdatePhasing");
			if (vo == null)
				return null;

			if (vo.getPhasingProgramRunId() == null) {
				LOG.debug("WS PB : PhasingProgramRunId is null , could not create phasing");
				return errorCodeFK;
			}
			if (vo.getPhasingAnalysisId() == null) {
				LOG.debug("WS PB : PhasingAnalysisId is null , could not create phasing");
				return errorCodeFK;
			}
			
			Phasing3VO phasingValue = null;
			Phasing3Service phasingService = (Phasing3Service) ejb3ServiceLocator.getLocalService(Phasing3Service.class);

			Integer phasingId = vo.getPhasingId();
			if (vo.getPhasingId() == null || vo.getPhasingId() == 0) {
				vo.setPhasingId(null);
			}

			Phasing3VO phasingVO = new Phasing3VO();
			PhasingAnalysis3Service phasingAnalysisService = (PhasingAnalysis3Service) ejb3ServiceLocator.getLocalService(PhasingAnalysis3Service.class);
			PhasingAnalysis3VO phasingAnalysisVO = null;
			if (vo.getPhasingAnalysisId() != null && vo.getPhasingAnalysisId() > 0)
				phasingAnalysisVO = phasingAnalysisService.findByPk(vo.getPhasingAnalysisId());
			PhasingProgramRun3Service phasingProgramRunService = (PhasingProgramRun3Service) ejb3ServiceLocator.getLocalService(PhasingProgramRun3Service.class);
			PhasingProgramRun3VO phasingProgramRunVO = null;
			if (vo.getPhasingProgramRunId() != null && vo.getPhasingProgramRunId() > 0)
				phasingProgramRunVO = phasingProgramRunService.findByPk(vo.getPhasingProgramRunId(), false);
			SpaceGroup3Service spaceGroupService = (SpaceGroup3Service) ejb3ServiceLocator.getLocalService(SpaceGroup3Service.class);
			SpaceGroup3VO spaceGroupVO = null;
			if (vo.getSpaceGroupId() != null && vo.getSpaceGroupId() > 0)
				spaceGroupVO = spaceGroupService.findByPk(vo.getSpaceGroupId());
			phasingVO.fillVOFromWS(vo);
			phasingVO.setPhasingProgramRunVO(phasingProgramRunVO);
			phasingVO.setPhasingAnalysisVO(phasingAnalysisVO);
			phasingVO.setSpaceGroupVO(spaceGroupVO);
			phasingVO.setRecordTimeStamp(StringUtils.getCurrentTimeStamp());

			if (phasingId == null || phasingId == 0) {
				phasingVO.setPhasingId(null);
				phasingValue = phasingService.create(phasingVO);
			} else {
				phasingValue = phasingService.update(phasingVO);
			}
			LOG.debug("storeOrUpdatePhasing " + phasingValue.getPhasingId());
			return phasingValue.getPhasingId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdatePhasing - " + StringUtils.getCurrentDate() + " - "
					+ vo.toWSString());
			throw e;
		}
	}
	
	@WebMethod
	@WebResult(name = "modelBuildingId")
	public Integer storeOrUpdateModelBuilding(ModelBuildingWS3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdateModelBuilding");
			if (vo == null)
				return null;

			if (vo.getPhasingProgramRunId() == null) {
				LOG.debug("WS PB : PhasingProgramRunId is null , could not create modelBuilding");
				return errorCodeFK;
			}
			if (vo.getPhasingAnalysisId() == null) {
				LOG.debug("WS PB : PhasingAnalysisId is null , could not create modelBuilding");
				return errorCodeFK;
			}
			
			ModelBuilding3VO modelBuildingValue = null;
			ModelBuilding3Service modelBuildingService = (ModelBuilding3Service) ejb3ServiceLocator.getLocalService(ModelBuilding3Service.class);

			Integer modelBuildingId = vo.getModelBuildingId();
			if (vo.getModelBuildingId() == null || vo.getModelBuildingId() == 0) {
				vo.setModelBuildingId(null);
			}

			ModelBuilding3VO modelBuildingVO = new ModelBuilding3VO();
			PhasingAnalysis3Service phasingAnalysisService = (PhasingAnalysis3Service) ejb3ServiceLocator.getLocalService(PhasingAnalysis3Service.class);
			PhasingAnalysis3VO phasingAnalysisVO = null;
			if (vo.getPhasingAnalysisId() != null && vo.getPhasingAnalysisId() > 0)
				phasingAnalysisVO = phasingAnalysisService.findByPk(vo.getPhasingAnalysisId());
			PhasingProgramRun3Service phasingProgramRunService = (PhasingProgramRun3Service) ejb3ServiceLocator.getLocalService(PhasingProgramRun3Service.class);
			PhasingProgramRun3VO phasingProgramRunVO = null;
			if (vo.getPhasingProgramRunId() != null && vo.getPhasingProgramRunId() > 0)
				phasingProgramRunVO = phasingProgramRunService.findByPk(vo.getPhasingProgramRunId(), false);
			SpaceGroup3Service spaceGroupService = (SpaceGroup3Service) ejb3ServiceLocator.getLocalService(SpaceGroup3Service.class);
			SpaceGroup3VO spaceGroupVO = null;
			if (vo.getSpaceGroupId() != null && vo.getSpaceGroupId() > 0)
				spaceGroupVO = spaceGroupService.findByPk(vo.getSpaceGroupId());
			modelBuildingVO.fillVOFromWS(vo);
			modelBuildingVO.setPhasingProgramRunVO(phasingProgramRunVO);
			modelBuildingVO.setPhasingAnalysisVO(phasingAnalysisVO);
			modelBuildingVO.setSpaceGroupVO(spaceGroupVO);
			modelBuildingVO.setRecordTimeStamp(StringUtils.getCurrentTimeStamp());

			if (modelBuildingId == null || modelBuildingId == 0) {
				modelBuildingVO.setModelBuildingId(null);
				modelBuildingValue = modelBuildingService.create(modelBuildingVO);
			} else {
				modelBuildingValue = modelBuildingService.update(modelBuildingVO);
			}
			LOG.debug("storeOrUpdateModelBuilding " + modelBuildingValue.getModelBuildingId());
			return modelBuildingValue.getModelBuildingId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateModelBuilding - " + StringUtils.getCurrentDate() + " - "
					+ vo.toWSString());
			throw e;
		}
	}
	
	
	
	@WebMethod
	@WebResult(name = "phasingProgramAttachmentId")
	public Integer storeOrUpdatePhasingProgramAttachment(PhasingProgramAttachmentWS3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdatePhasingProgramAttachment");
			if (vo == null)
				return null;

			if (vo.getPhasingProgramRunId() == null) {
				LOG.debug("WS PB : PhasingProgramRunId is null , could not create phasingAttachment");
				return errorCodeFK;
			}
			
			PhasingProgramAttachment3VO phasingProgramAttachmentValue = null;
			PhasingProgramAttachment3Service phasingProgramAttachmentService = (PhasingProgramAttachment3Service) ejb3ServiceLocator.getLocalService(PhasingProgramAttachment3Service.class);

			Integer phasingProgramAttachmentId = vo.getPhasingProgramAttachmentId();
			if (vo.getPhasingProgramAttachmentId() == null || vo.getPhasingProgramAttachmentId() == 0) {
				vo.setPhasingProgramAttachmentId(null);
			}

			PhasingProgramAttachment3VO phasingProgramAttachmentVO = new PhasingProgramAttachment3VO();
			PhasingProgramRun3Service phasingProgramRunService = (PhasingProgramRun3Service) ejb3ServiceLocator.getLocalService(PhasingProgramRun3Service.class);
			PhasingProgramRun3VO phasingProgramRunVO = null;
			if (vo.getPhasingProgramRunId() != null && vo.getPhasingProgramRunId() > 0)
				phasingProgramRunVO = phasingProgramRunService.findByPk(vo.getPhasingProgramRunId(), false);
			phasingProgramAttachmentVO.fillVOFromWS(vo);
			phasingProgramAttachmentVO.setPhasingProgramRunVO(phasingProgramRunVO);
			phasingProgramAttachmentVO.setRecordTimeStamp(StringUtils.getCurrentTimeStamp());

			if (phasingProgramAttachmentId == null || phasingProgramAttachmentId == 0) {
				phasingProgramAttachmentVO.setPhasingProgramAttachmentId(null);
				phasingProgramAttachmentValue = phasingProgramAttachmentService.create(phasingProgramAttachmentVO);
			} else {
				phasingProgramAttachmentValue = phasingProgramAttachmentService.update(phasingProgramAttachmentVO);
			}
			LOG.debug("storeOrUpdatePhasingProgramAttachment " + phasingProgramAttachmentValue.getPhasingProgramAttachmentId());
			return phasingProgramAttachmentValue.getPhasingProgramAttachmentId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdatePhasingProgramAttachment - " + StringUtils.getCurrentDate() + " - "
					+ vo.toWSString());
			throw e;
		}
	}
	
	@WebMethod
	@WebResult(name = "phasingHasScalingId")
	public Integer storeOrUpdatePhasingHasScaling(PhasingHasScalingWS3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdatePhasingHasScaling");
			if (vo == null)
				return null;

			if (vo.getPhasingAnalysisId() == null) {
				LOG.debug("WS PB : PhasingAnalysisId is null , could not create phasingHasScaling");
				return errorCodeFK;
			}
			if (vo.getAutoProcScalingId() == null) {
				LOG.debug("WS PB : AutoProcScalingId is null , could not create phasingHasScaling");
				return errorCodeFK;
			}
			
			PhasingHasScaling3VO phasingHasScalingValue = null;
			PhasingHasScaling3Service phasingHasScalingService = (PhasingHasScaling3Service) ejb3ServiceLocator.getLocalService(PhasingHasScaling3Service.class);

			Integer phasingHasScalingId = vo.getPhasingHasScalingId();
			if (vo.getPhasingAnalysisId() == null || vo.getPhasingAnalysisId() == 0) {
				vo.setPhasingAnalysisId(null);
			}
			if (vo.getAutoProcScalingId() == null || vo.getAutoProcScalingId() == 0) {
				vo.setAutoProcScalingId(null);
			}

			PhasingHasScaling3VO phasingHasScalingVO = new PhasingHasScaling3VO();
			PhasingAnalysis3Service phasingAnalysisService = (PhasingAnalysis3Service) ejb3ServiceLocator.getLocalService(PhasingAnalysis3Service.class);
			AutoProcScaling3Service autoProcScalingService = (AutoProcScaling3Service) ejb3ServiceLocator.getLocalService(AutoProcScaling3Service.class);
			PhasingAnalysis3VO phasingAnalysisVO = null;
			AutoProcScaling3VO autoProcScalingVO = null;
			if (vo.getPhasingAnalysisId() != null && vo.getPhasingAnalysisId() > 0)
				phasingAnalysisVO = phasingAnalysisService.findByPk(vo.getPhasingAnalysisId());
			if (vo.getAutoProcScalingId() != null && vo.getAutoProcScalingId() > 0)
				autoProcScalingVO = autoProcScalingService.findByPk(vo.getAutoProcScalingId());
			phasingHasScalingVO.fillVOFromWS(vo);
			phasingHasScalingVO.setPhasingAnalysisVO(phasingAnalysisVO);
			phasingHasScalingVO.setAutoProcScalingVO(autoProcScalingVO);
			phasingHasScalingVO.setRecordTimeStamp(StringUtils.getCurrentTimeStamp());

			if (phasingHasScalingId == null || phasingHasScalingId == 0) {
				phasingHasScalingVO.setPhasingHasScalingId(null);
				phasingHasScalingValue = phasingHasScalingService.create(phasingHasScalingVO);
			} else {
				phasingHasScalingValue = phasingHasScalingService.update(phasingHasScalingVO);
			}
			LOG.debug("storeOrUpdatePhasingHasScaling " + phasingHasScalingValue.getPhasingHasScalingId());
			return phasingHasScalingValue.getPhasingHasScalingId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdatePhasingHasScaling - " + StringUtils.getCurrentDate() + " - "
					+ vo.toWSString());
			throw e;
		}
	}
	
	@WebMethod
	@WebResult(name = "phasingStatisticsId")
	public Integer storeOrUpdatePhasingStatistics(PhasingStatisticsWS3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdatePhasingStatistics");
			if (vo == null)
				return null;

			if (vo.getPhasingHasScalingId1() == null) {
				LOG.debug("WS PB : PhasingHasScalingId1 is null , could not create phasingStatistics");
				return errorCodeFK;
			}
			
			
			PhasingStatistics3VO phasingStatisticsValue = null;
			PhasingStatistics3Service phasingStatisticsService = (PhasingStatistics3Service) ejb3ServiceLocator.getLocalService(PhasingStatistics3Service.class);

			Integer phasingStatisticsId = vo.getPhasingStatisticsId();
			if (vo.getPhasingHasScalingId1() == null || vo.getPhasingHasScalingId1() == 0) {
				vo.setPhasingHasScalingId1(null);
			}
			if (vo.getPhasingHasScalingId2() == null || vo.getPhasingHasScalingId2() == 0) {
				vo.setPhasingHasScalingId2(null);
			}

			PhasingStatistics3VO phasingStatisticsVO = new PhasingStatistics3VO();
			PhasingHasScaling3Service phasingHasScalingService = (PhasingHasScaling3Service) ejb3ServiceLocator.getLocalService(PhasingHasScaling3Service.class);
			PhasingHasScaling3VO phasingHasScaling1VO = null;
			PhasingHasScaling3VO phasingHasScaling2VO = null;
			if (vo.getPhasingHasScalingId1() != null && vo.getPhasingHasScalingId1() > 0)
				phasingHasScaling1VO = phasingHasScalingService.findByPk(vo.getPhasingHasScalingId1());
			if (vo.getPhasingHasScalingId2() != null && vo.getPhasingHasScalingId2() > 0)
				phasingHasScaling2VO = phasingHasScalingService.findByPk(vo.getPhasingHasScalingId2());
			phasingStatisticsVO.fillVOFromWS(vo);
			phasingStatisticsVO.setPhasingHasScaling1VO(phasingHasScaling1VO);
			phasingStatisticsVO.setPhasingHasScaling2VO(phasingHasScaling2VO);
			phasingStatisticsVO.setRecordTimeStamp(StringUtils.getCurrentTimeStamp());

			if (phasingStatisticsId == null || phasingStatisticsId == 0) {
				phasingStatisticsVO.setPhasingStatisticsId(null);
				phasingStatisticsValue = phasingStatisticsService.create(phasingStatisticsVO);
			} else {
				phasingStatisticsValue = phasingStatisticsService.update(phasingStatisticsVO);
			}
			LOG.debug("storeOrUpdatePhasingStatistics " + phasingStatisticsValue.getPhasingStatisticsId());
			return phasingStatisticsValue.getPhasingStatisticsId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdatePhasingStatistics - " + StringUtils.getCurrentDate() + " - "
					+ vo.toWSString());
			throw e;
		}
	}
	
	@WebMethod
	@WebResult(name = "spaceGroupId")
	public Integer getSpaceGroupId(String spaceGroupName) throws Exception {
		try {
			LOG.debug("getSpaceGroupId");
			Integer spaceGroupId = null;
			SpaceGroup3Service spaceGroupService = (SpaceGroup3Service) ejb3ServiceLocator.getLocalService(SpaceGroup3Service.class);
			if (spaceGroupName != null){
				String shortSpaceGroup = spaceGroupName.replaceAll(" ", "");
				List<SpaceGroup3VO>listSpaceGroup = spaceGroupService.findBySpaceGroupShortName(shortSpaceGroup);
				if (listSpaceGroup != null && !listSpaceGroup.isEmpty()){
					spaceGroupId = listSpaceGroup.get(0).getSpaceGroupId();
				}
			}
			LOG.debug("getSpaceGroupId " +spaceGroupId);
			return spaceGroupId;
		} catch (Exception e) {
			LOG.error("WS ERROR: getSpaceGroupId - " + StringUtils.getCurrentDate() + " - "
					+ spaceGroupName);
			throw e;
		}
	}
	
	@WebMethod
	@WebResult(name = "autoProcScalingIdList")
	public int[] getAutoProcScalingIdList(Integer autoProcIntegrationId) throws Exception {
		try {
			LOG.debug("getAutoProcScalingIdList");
			int[] autoProcScalingIdList = new int[0];
			AutoProcScalingHasInt3Service autoProcScalingHasIntService = (AutoProcScalingHasInt3Service) ejb3ServiceLocator.getLocalService(AutoProcScalingHasInt3Service.class);
			List<AutoProcScalingHasInt3VO> list = autoProcScalingHasIntService.findFiltered(autoProcIntegrationId);
			if (list != null ){
				autoProcScalingIdList = new int[list.size()];
				int i=0;
				for (Iterator<AutoProcScalingHasInt3VO> iterator = list.iterator(); iterator.hasNext();) {
					AutoProcScalingHasInt3VO autoProcScalingHasInt3VO = (AutoProcScalingHasInt3VO) iterator
							.next();
					autoProcScalingIdList[i++] = autoProcScalingHasInt3VO.getAutoProcScalingVOId();
				}
			}
			LOG.debug("getAutoProcScalingIdList " +autoProcScalingIdList.length);
			return autoProcScalingIdList;
		} catch (Exception e) {
			LOG.error("WS ERROR: getAutoProcScalingIdList - " + StringUtils.getCurrentDate() + " - "
					+ autoProcIntegrationId);
			throw e;
		}
	}
	
	@WebMethod
	@WebResult(name = "imageQualityIndicatorsIds")
	public Integer[] storeOrUpdateImageQualityIndicatorsForFileNames(
			@WebParam(name = "listImageQualityIndicatorsForWS") List<ImageQualityIndicatorsWS3VO> listImageQualityIndicatorsForWS
			) throws Exception {
		try {
			LOG.debug("storeOrUpdateImageQualityIndicatorsForFileNames");
			if (listImageQualityIndicatorsForWS == null){
				LOG.debug("WS PB : listImageQualityIndicatorsForWS is null , could not create storeOrUpdateImageQualityIndicatorsForFileNames");
				return null;
			}
			int nb = listImageQualityIndicatorsForWS.size();
			Integer[] listIds = new Integer[nb];
			Image3Service imageService = (Image3Service) ejb3ServiceLocator.getLocalService(Image3Service.class);
			ImageQualityIndicators3Service imageQualityIndicatorsService = (ImageQualityIndicators3Service) ejb3ServiceLocator.getLocalService(ImageQualityIndicators3Service.class);

			for(int i=0; i<nb; i++){
				ImageQualityIndicatorsWS3VO o = listImageQualityIndicatorsForWS.get(i);
				Integer autoProcProgramId = o.getAutoProcProgramId();
				Integer imageId = o.getImageId();
				String fileLocation = o.getFileLocation();
				String fileName = o.getFileName();
				Integer spotTotal = o.getSpotTotal();
				Integer inResTotal = o.getInResTotal();
				Integer goodBraggCandidates = o.getGoodBraggCandidates();
				Integer iceRings = o.getIceRings();
				Float method1Res = o.getMethod1Res();
				Float method2Res = o.getMethod2Res();
				Float maxUnitCell = o.getMaxUnitCell();
				Float pctSaturationTop50Peaks = o.getPctSaturationTop50Peaks();
				Integer inResolutionOvrlSpots = o.getInResolutionOvrlSpots();
				Float binPopCutOffMethod2Res = o.getBinPopCutOffMethod2Res();
				Double totalIntegratedSignal = o.getTotalIntegratedSignal();
				Double dozor_score = o.getDozor_score();
				
				if (autoProcProgramId == null || autoProcProgramId == 0)
					autoProcProgramId = null;

				if (imageId == null || imageId == 0) {
					imageId = null;
					List<Image3VO> listImg = imageService.findFiltered(fileLocation, fileName);
					if (listImg != null && listImg.size() > 0)
						imageId = listImg.get(0).getImageId();
					else{
						// try to add or remove the last  / -- it seems that there is no rule  with this.
						if (fileLocation != null && fileLocation.length()> 1){
							char lastCharact = fileLocation.charAt(fileLocation.length() -1);
							if (lastCharact == '/'){
								listImg = imageService.findFiltered(fileLocation.substring(0, fileLocation.length()-1), fileName);
							}else{
								listImg = imageService.findFiltered(fileLocation+"/", fileName);
							}
							if (listImg != null && listImg.size() > 0)
								imageId = listImg.get(0).getImageId();
						}
					}
				}
				Integer id = null;
				if (imageId == null) {
					LOG.debug("WS PB : ImageId is null , could not create ImageQualityIndicators "+fileLocation+", "+fileName);
					//return errorCodeFK;
				}

				if (autoProcProgramId == null) {
					LOG.debug("WS PB : autoProcProgramId is null , could not create ImageQualityIndicators");
					//return errorCodeFK;
				}
				if (imageId != null && autoProcProgramId != null){
				
					Integer imageQualityIndicatorsId = null;
					
					List<ImageQualityIndicators3VO> listImgQualityInd = imageQualityIndicatorsService.findByImageId(imageId);
					if (listImgQualityInd != null && listImgQualityInd.size() > 0){
						imageQualityIndicatorsId = listImgQualityInd.get(0).getImageQualityIndicatorsId();
					}

					ImageQualityIndicatorsWS3VO vo = new ImageQualityIndicatorsWS3VO(imageQualityIndicatorsId,
							imageId, autoProcProgramId, spotTotal,
							inResTotal, goodBraggCandidates, iceRings,
							method1Res, method2Res, maxUnitCell,
							pctSaturationTop50Peaks, inResolutionOvrlSpots, binPopCutOffMethod2Res,
							StringUtils.getCurrentTimeStamp(), totalIntegratedSignal, dozor_score);
					id = storeOrUpdateImageQualityIndicators(vo);
					listIds[i] = id;
				}else{
					listIds[i] =errorCodeFK;
				}
			}
			return listIds;

		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateImageQualityIndicatorsForFileNames - " + StringUtils.getCurrentDate() + " - " + (listImageQualityIndicatorsForWS == null?"null":""+listImageQualityIndicatorsForWS.size()));
			throw e;
		}

	}
	
	
	protected Gson getGson() {
		return new GsonBuilder().serializeNulls().excludeFieldsWithModifiers(Modifier.PRIVATE).serializeSpecialFloatingPointValues()
				.create();
	}

	protected PhasingStep3Service getPhasingStep3Service() throws NamingException {
		return (PhasingStep3Service) Ejb3ServiceLocator.getInstance().getLocalService(PhasingStep3Service.class);
	}
	
	protected SpaceGroup3Service getSpaceGroup3Service() throws NamingException {
		return (SpaceGroup3Service) Ejb3ServiceLocator.getInstance().getLocalService(SpaceGroup3Service.class);
	}
	
	protected PhasingProgramRun3Service getPhasingProgramRun3Service() throws NamingException {
		return (PhasingProgramRun3Service) Ejb3ServiceLocator.getInstance().getLocalService(PhasingProgramRun3Service.class);
	}
	
	protected PhasingProgramAttachment3Service getPhasingProgramAttachment3Service() throws NamingException {
		return (PhasingProgramAttachment3Service) Ejb3ServiceLocator.getInstance().getLocalService(PhasingProgramAttachment3Service.class);
	}
	
	protected long logInit(String methodName, Logger logger, Object... args) {
		logger.info("-----------------------");
		this.now = System.currentTimeMillis();
		ArrayList<String> params = new ArrayList<String>();
		for (Object object : args) {
			if (object != null){
				params.add(object.toString());
			}
			else{
				params.add("null");
			}
		}
		logger.info(methodName.toUpperCase());
		LoggerFormatter.log(logger, LoggerFormatter.Package.ISPyB_MX_AUTOPROCESSING, methodName, System.currentTimeMillis(),
				System.currentTimeMillis(), this.getGson().toJson(params));
		return this.now;
	}
	
	

	protected void logFinish(String methodName, long start, Logger logger) {
		logger.debug("### [" + methodName.toUpperCase() + "] Execution time was "
				+ (System.currentTimeMillis() - this.now) + " ms.");
		LoggerFormatter.log(logger, LoggerFormatter.Package.ISPyB_MX_AUTOPROCESSING, methodName, start, System.currentTimeMillis(),
				System.currentTimeMillis() - this.now);

	}

	protected void logError(String methodName, Exception e, long start, Logger logger) {
		e.printStackTrace();
		LoggerFormatter.log(logger, LoggerFormatter.Package.ISPyB_MX_AUTOPROCESSING_ERROR, methodName, start,
				System.currentTimeMillis(), e.getMessage(), e);
		
	}
	
	@WebMethod
	@WebResult(name = "storePhasingAnalysis")
	public String storePhasingAnalysis(String phasingStep, String spaceGroup, String run, String attachments, String phasingStatistics){
		
		
		String methodName = "storePhasingAnalysis";
		long start = this.logInit(methodName, LOG, phasingStep, spaceGroup, run, attachments, phasingStatistics);
		
		try {
			
			
			PhasingStepVO phasingStepVO = this.getGson().fromJson(phasingStep, PhasingStepVO.class);
			SpaceGroup3VO spaceGroup3VO = this.getGson().fromJson(spaceGroup, SpaceGroup3VO.class);
			List<SpaceGroup3VO> spaceGroups = this.getSpaceGroup3Service().findBySpaceGroupShortName(spaceGroup3VO.getSpaceGroupShortName());
			
			if (spaceGroups.size() > 0){
				phasingStepVO.setSpaceGroupVO(spaceGroups.get(0));
			}
			else{
				/** Creating Space Group **/
				phasingStepVO.setSpaceGroupVO(this.getSpaceGroup3Service().create(spaceGroup3VO));
			}

			/** Creating the attachments **/
			List<PhasingProgramAttachment3VO> phasingProgramAttachment3VOs = new ArrayList<PhasingProgramAttachment3VO>();
			if (attachments != null){
				Type listType = new TypeToken<ArrayList<PhasingProgramAttachment3VO>>() {}.getType();
				phasingProgramAttachment3VOs = this.getGson().fromJson(attachments, listType);
			}
			
			/** Program Run **/
			PhasingProgramRun3VO phasingProgramRun3VO =  this.getGson().fromJson(run, PhasingProgramRun3VO.class);
			PhasingProgramRun3VO program = this.getPhasingProgramRun3Service().create(phasingProgramRun3VO);
			phasingStepVO.setPhasingProgramRunVO(program);
			
			
			for (PhasingProgramAttachment3VO phasingProgramAttachment3VO : phasingProgramAttachment3VOs) {
				phasingProgramAttachment3VO.setPhasingProgramRunVO(program);
				this.getPhasingProgramAttachment3Service().create(phasingProgramAttachment3VO);
			}
			phasingStepVO.setRecordTimeStamp(StringUtils.getCurrentTimeStamp());
			phasingStepVO = this.getPhasingStep3Service().merge(phasingStepVO);
			
			/** Statistics **/
			List<PhasingStatistics3VO> phasingStatistics3VOList = new ArrayList<PhasingStatistics3VO>();
			if (phasingStatistics != null){
				Type listType = new TypeToken<ArrayList<PhasingStatistics3VO>>() {}.getType();
				phasingStatistics3VOList = this.getGson().fromJson(phasingStatistics, listType);
			}
			PhasingStatistics3Service phasingStatisticsService = (PhasingStatistics3Service) ejb3ServiceLocator.getLocalService(PhasingStatistics3Service.class);
			if (phasingStatistics3VOList.size() > 0){
				for (PhasingStatistics3VO phasingStatistics3VO : phasingStatistics3VOList) {
					phasingStatistics3VO.setPhasingStepId(phasingStepVO.getPhasingStepId());
					phasingStatistics3VO.setRecordTimeStamp(StringUtils.getCurrentTimeStamp());
					phasingStatisticsService.create(phasingStatistics3VO);
				}
			}
			this.logFinish(methodName, start, LOG);
			return this.getGson().toJson(phasingStepVO);
			
		} catch (Exception e) {
			this.logError(methodName, e, start, LOG);
			return e.getMessage();
		}
		
		
	}
			
	@WebMethod
	@WebResult(name = "phasingDataId")
	public Integer storePhasingData(
			@WebParam(name = "phasingData") PhasingData phasingData
			) throws Exception {
		try {
			LOG.debug("storePhasingData");
			if (phasingData == null) {
				LOG.debug("WS PB : phasingData is null , could not create phasingData");
				return -1;
			}
			//
			// step
			Integer step = phasingData.getStep();
			if (step == null || Arrays.binarySearch(Constants.PHASING_STEPS, step) < 0){
				LOG.debug("WS PB : step for phasingData is not recognized "+step);
				return -1;
			}
			Integer autoProcScalingId = phasingData.getAutoProcScalingId();
			Integer datasetNumber = phasingData.getDatasetNumber();
			String spaceGroup = phasingData.getSpaceGroup();
			Double lowRes = StringUtils.formatDoubleInfinity(phasingData.getLowRes());
			Double highRes = StringUtils.formatDoubleInfinity(phasingData.getHighRes());
			String method = phasingData.getMethod();
			Double solventContent = phasingData.getSolventContent();
			Boolean enantiomorph = phasingData.getEnantiomorph();
			PhasingProgramRun3VO phasingProgramRun = phasingData.getPhasingProgramRun(); 
			List<PhasingProgramAttachmentWS3VO> listPhasingProgramAttachment = phasingData.getListPhasingProgramAttachment();
			List<PhasingStatisticsWS3VO> listPhasingStatistics  = phasingData.getListPhasingStatistics();
			if (autoProcScalingId == null) {
				LOG.debug("WS PB : autoProcScalingId is null , could not create preparePhasingData");
				return errorCodeFK;
			}
			if (phasingProgramRun == null){
				LOG.debug("WS PB : phasingProgramRun is null , could not create preparePhasingData");
				return errorCodeFK;
			}
			// autoProcScaling
			AutoProcScaling3Service autoProcScalingService = (AutoProcScaling3Service) ejb3ServiceLocator.getLocalService(AutoProcScaling3Service.class);
			AutoProcScaling3VO autoProcScalingValue = autoProcScalingService.findByPk(autoProcScalingId);
			// update session
			Session3Service sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
			Session3VO sessionVO = sessionService.findByAutoProcScalingId(autoProcScalingId);
			ToolsForCollectionWebService.updateSession(sessionVO);
			// create PhasingAnalysis
			PhasingAnalysis3VO phasingAnalysisVO = new PhasingAnalysis3VO(null, StringUtils.getCurrentTimeStamp());
			PhasingAnalysis3Service phasingAnalysisService = (PhasingAnalysis3Service) ejb3ServiceLocator.getLocalService(PhasingAnalysis3Service.class);
			PhasingAnalysis3VO phasingAnalysisValue = phasingAnalysisService.create(phasingAnalysisVO);
			// phasingHasScaling
			PhasingHasScaling3VO phasingHasScalingVO = new PhasingHasScaling3VO(null, phasingAnalysisValue, autoProcScalingValue, datasetNumber, StringUtils.getCurrentTimeStamp());
			PhasingHasScaling3Service phasingHasScalingService = (PhasingHasScaling3Service) ejb3ServiceLocator.getLocalService(PhasingHasScaling3Service.class);
			PhasingHasScaling3VO phasingHasScalingValue = phasingHasScalingService.create(phasingHasScalingVO);
			// get Space Group
			SpaceGroup3Service spaceGroupService = (SpaceGroup3Service) ejb3ServiceLocator.getLocalService(SpaceGroup3Service.class);
			List<SpaceGroup3VO> listSpaceGroup = null;
			if (spaceGroup != null){
				listSpaceGroup = spaceGroupService.findBySpaceGroupShortName(spaceGroup);
			}
			SpaceGroup3VO spaceGroupValue = null;
			if (listSpaceGroup != null && !listSpaceGroup.isEmpty()){
				spaceGroupValue = listSpaceGroup.get(0);
			}
			// phasingProgramRun
			phasingProgramRun.setPhasingProgramRunId(null);
			phasingProgramRun.setRecordTimeStamp(StringUtils.getCurrentTimeStamp());
			PhasingProgramRun3Service phasingProgramRunService = (PhasingProgramRun3Service) ejb3ServiceLocator.getLocalService(PhasingProgramRun3Service.class);
			PhasingProgramRun3VO phasingProgramRunValue = phasingProgramRunService.create(phasingProgramRun);
			// phasingProgramAttachment
			PhasingProgramAttachment3Service phasingProgramAttachmentService = (PhasingProgramAttachment3Service) ejb3ServiceLocator.getLocalService(PhasingProgramAttachment3Service.class);
			if (listPhasingProgramAttachment != null){
				for(Iterator<PhasingProgramAttachmentWS3VO> iterator = listPhasingProgramAttachment.iterator(); iterator.hasNext();){
					PhasingProgramAttachmentWS3VO vo = iterator.next();
					PhasingProgramAttachment3VO phasingProgramAttachmentVO = new PhasingProgramAttachment3VO();
					phasingProgramAttachmentVO.fillVOFromWS(vo);
					phasingProgramAttachmentVO.setPhasingProgramRunVO(phasingProgramRunValue);
					phasingProgramAttachmentVO.setRecordTimeStamp(StringUtils.getCurrentTimeStamp());
					phasingProgramAttachmentService.create(phasingProgramAttachmentVO);
				}
			}
			// phasingStatistics
			PhasingStatistics3Service phasingStatisticsService = (PhasingStatistics3Service) ejb3ServiceLocator.getLocalService(PhasingStatistics3Service.class);
			if (listPhasingStatistics != null){
				for(Iterator<PhasingStatisticsWS3VO> iterator = listPhasingStatistics.iterator(); iterator.hasNext();){
					PhasingStatisticsWS3VO vo = iterator.next();
					PhasingStatistics3VO phasingStatisticsVO = new PhasingStatistics3VO();
					phasingStatisticsVO.fillVOFromWS(vo);
					phasingStatisticsVO.setPhasingHasScaling1VO(phasingHasScalingValue);
					phasingStatisticsVO.setPhasingHasScaling2VO(null);
					phasingStatisticsVO.setStatisticsValue(StringUtils.formatDoubleInfinity(vo.getStatisticsValue()));
					phasingStatisticsVO.setLowRes(StringUtils.formatDoubleInfinity(vo.getLowRes()));
					phasingStatisticsVO.setHighRes(StringUtils.formatDoubleInfinity(vo.getHighRes()));
					phasingStatisticsVO.setRecordTimeStamp(StringUtils.getCurrentTimeStamp());
					phasingStatisticsService.create(phasingStatisticsVO);
				}
			}
			// phasing Data: step
			if (step.equals(Constants.PHASING_STEP_PREPARE_PHASING_DATA)){
				PreparePhasingData3Service preparePhasingDataService = (PreparePhasingData3Service) ejb3ServiceLocator.getLocalService(PreparePhasingData3Service.class);
				PreparePhasingData3VO preparePhasingDataVO = new PreparePhasingData3VO();
				preparePhasingDataVO.setPreparePhasingDataId(null);
				preparePhasingDataVO.setPhasingProgramRunVO(phasingProgramRunValue);
				preparePhasingDataVO.setPhasingAnalysisVO(phasingAnalysisValue);
				preparePhasingDataVO.setSpaceGroupVO(spaceGroupValue);
				preparePhasingDataVO.setLowRes(lowRes);
				preparePhasingDataVO.setHighRes(highRes);
				preparePhasingDataVO.setRecordTimeStamp(StringUtils.getCurrentTimeStamp());
				PreparePhasingData3VO preparePhasingDataValue = preparePhasingDataService.create(preparePhasingDataVO);
				return preparePhasingDataValue.getPreparePhasingDataId();
			}else if (step.equals(Constants.PHASING_STEP_SUBSTRUCTURE_DETERMINATION)){
				SubstructureDetermination3Service subStructureDeterminationService = (SubstructureDetermination3Service) ejb3ServiceLocator.getLocalService(SubstructureDetermination3Service.class);
				SubstructureDetermination3VO subStructureDeterminationVO = new SubstructureDetermination3VO();
				subStructureDeterminationVO.setSubstructureDeterminationId(null);
				subStructureDeterminationVO.setPhasingProgramRunVO(phasingProgramRunValue);
				subStructureDeterminationVO.setPhasingAnalysisVO(phasingAnalysisValue);
				subStructureDeterminationVO.setSpaceGroupVO(spaceGroupValue);
				subStructureDeterminationVO.setLowRes(lowRes);
				subStructureDeterminationVO.setHighRes(highRes);
				subStructureDeterminationVO.setMethod(method);
				subStructureDeterminationVO.setRecordTimeStamp(StringUtils.getCurrentTimeStamp());
				SubstructureDetermination3VO subStructureDeterminationValue = subStructureDeterminationService.create(subStructureDeterminationVO);
				return subStructureDeterminationValue.getSubstructureDeterminationId();
			}else if (step.equals(Constants.PHASING_STEP_PHASING)){
				Phasing3Service phasingService = (Phasing3Service) ejb3ServiceLocator.getLocalService(Phasing3Service.class);
				Phasing3VO phasingVO = new Phasing3VO();
				phasingVO.setPhasingId(null);
				phasingVO.setPhasingProgramRunVO(phasingProgramRunValue);
				phasingVO.setPhasingAnalysisVO(phasingAnalysisValue);
				phasingVO.setSpaceGroupVO(spaceGroupValue);
				phasingVO.setLowRes(lowRes);
				phasingVO.setHighRes(highRes);
				phasingVO.setMethod(method);
				phasingVO.setSolventContent(solventContent);
				phasingVO.setEnantiomorph(enantiomorph);
				phasingVO.setRecordTimeStamp(StringUtils.getCurrentTimeStamp());
				Phasing3VO phasingValue = phasingService.create(phasingVO);
				return phasingValue.getPhasingId();
			}else if (step.equals(Constants.PHASING_STEP_MODEL_BUILDING)){
				ModelBuilding3Service modelBuildingService = (ModelBuilding3Service) ejb3ServiceLocator.getLocalService(ModelBuilding3Service.class);
				ModelBuilding3VO modelBuildingVO = new ModelBuilding3VO();
				modelBuildingVO.setModelBuildingId(null);
				modelBuildingVO.setPhasingProgramRunVO(phasingProgramRunValue);
				modelBuildingVO.setPhasingAnalysisVO(phasingAnalysisValue);
				modelBuildingVO.setSpaceGroupVO(spaceGroupValue);
				modelBuildingVO.setLowRes(lowRes);
				modelBuildingVO.setHighRes(highRes);
				modelBuildingVO.setRecordTimeStamp(StringUtils.getCurrentTimeStamp());
				ModelBuilding3VO modelBuildingValue = modelBuildingService.create(modelBuildingVO);
				return modelBuildingValue.getModelBuildingId();
			}
			return -1;
		} catch (Exception e) {
			LOG.error("WS ERROR: storePhasingData - " + StringUtils.getCurrentDate() + " - " + (phasingData == null?"":phasingData.toWSString()) );
			throw e;
		}
	}
}
