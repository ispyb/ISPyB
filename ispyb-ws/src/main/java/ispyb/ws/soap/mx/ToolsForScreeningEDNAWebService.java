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

import java.util.Date;

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

import ispyb.common.util.StringUtils;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.collections.DataCollectionGroup3Service;
import ispyb.server.mx.services.sample.DiffractionPlan3Service;
import ispyb.server.mx.services.screening.Screening3Service;
import ispyb.server.mx.services.screening.ScreeningInput3Service;
import ispyb.server.mx.services.screening.ScreeningOutput3Service;
import ispyb.server.mx.services.screening.ScreeningOutputLattice3Service;
import ispyb.server.mx.services.screening.ScreeningRank3Service;
import ispyb.server.mx.services.screening.ScreeningRankSet3Service;
import ispyb.server.mx.services.screening.ScreeningStrategy3Service;
import ispyb.server.mx.services.screening.ScreeningStrategySubWedge3Service;
import ispyb.server.mx.services.screening.ScreeningStrategyWedge3Service;
import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
import ispyb.server.mx.vos.sample.DiffractionPlan3VO;
import ispyb.server.mx.vos.screening.Screening3VO;
import ispyb.server.mx.vos.screening.ScreeningInput3VO;
import ispyb.server.mx.vos.screening.ScreeningInputWS3VO;
import ispyb.server.mx.vos.screening.ScreeningOutput3VO;
import ispyb.server.mx.vos.screening.ScreeningOutputLattice3VO;
import ispyb.server.mx.vos.screening.ScreeningOutputLatticeWS3VO;
import ispyb.server.mx.vos.screening.ScreeningOutputWS3VO;
import ispyb.server.mx.vos.screening.ScreeningRank3VO;
import ispyb.server.mx.vos.screening.ScreeningRankSet3VO;
import ispyb.server.mx.vos.screening.ScreeningRankWS3VO;
import ispyb.server.mx.vos.screening.ScreeningStrategy3VO;
import ispyb.server.mx.vos.screening.ScreeningStrategySubWedge3VO;
import ispyb.server.mx.vos.screening.ScreeningStrategySubWedgeWS3VO;
import ispyb.server.mx.vos.screening.ScreeningStrategyWS3VO;
import ispyb.server.mx.vos.screening.ScreeningStrategyWedge3VO;
import ispyb.server.mx.vos.screening.ScreeningStrategyWedgeWS3VO;
import ispyb.server.mx.vos.screening.ScreeningWS3VO;

/**
 * Web services for Screening
 * 
 * @author BODIN
 * 
 */
@WebService(name = "ToolsForScreeningEDNAWebService", serviceName = "ispybWS", targetNamespace = "http://ispyb.ejb3.webservices.screening")
@SOAPBinding(style = Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Stateless
@RolesAllowed({ "WebService", "User", "Industrial"}) // allow special access roles
@SecurityDomain("ispyb")
@WebContext(authMethod="BASIC",  secureWSDLAccess=false, transportGuarantee="NONE")
public class ToolsForScreeningEDNAWebService {
	private final static Logger LOG = Logger.getLogger(ToolsForScreeningEDNAWebService.class);

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private final Integer errorCodeFK = null;


	@WebMethod(operationName = "echo")
	@WebResult(name = "echo")
	public String echo(){
		return "echo from server...";
	}
	
	private Integer storeOrUpdateScreeningValue(ScreeningWS3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdateScreeningValue");
			if (vo == null)
				return null;

			if (vo.getDataCollectionGroupId() == null) {
				LOG.debug("WS PB : DataCollectionId is null , could not update/create Screening");
				return errorCodeFK;
			}

			Screening3VO screeningValue = null;
			Screening3Service screeningService = (Screening3Service) ejb3ServiceLocator
					.getLocalService(Screening3Service.class);

			if (vo.getDataCollectionGroupId() == null || vo.getDataCollectionGroupId() == 0)
				vo.setDataCollectionGroupId(null);
			Screening3VO screeningVO = new Screening3VO();
			DataCollectionGroup3Service DataCollectionGroupService = (DataCollectionGroup3Service) ejb3ServiceLocator
					.getLocalService(DataCollectionGroup3Service.class);
			DataCollectionGroup3VO DataCollectionGroupVO = null;
			if (vo.getDataCollectionGroupId() != null && vo.getDataCollectionGroupId() > 0)
				DataCollectionGroupVO = DataCollectionGroupService.findByPk(vo.getDataCollectionGroupId(), false,false) ;
			DiffractionPlan3Service diffractionPlanService = (DiffractionPlan3Service) ejb3ServiceLocator
					.getLocalService(DiffractionPlan3Service.class);
			DiffractionPlan3VO diffractionPlanVO = null;
			if (vo.getDiffractionPlanId() != null && vo.getDiffractionPlanId() > 0)
				diffractionPlanVO = diffractionPlanService.findByPk(vo.getDiffractionPlanId(), false, false);

			Integer screeningId = vo.getScreeningId();
			// load the object elsewhere there is an error with the childs
			if (screeningId != null && screeningId > 0) {
				screeningVO = screeningService.findByPk(screeningId, false, false);
			}
			screeningVO.fillVOFromWS(vo);
			screeningVO.setDataCollectionGroupVO(DataCollectionGroupVO);
			screeningVO.setDiffractionPlanVO(diffractionPlanVO);
			screeningVO.setTimeStamp(StringUtils.getCurrentTimeStamp());

			if (screeningId == null || screeningId == 0) {
				screeningVO.setScreeningId(null);
				screeningValue = screeningService.create(screeningVO);
			} else {
				screeningValue = screeningService.update(screeningVO);
			}
			LOG.debug("storeOrUpdateScreeningValue " + screeningValue.getScreeningId());
			return screeningValue.getScreeningId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateScreeningValue - " + StringUtils.getCurrentDate() + " - "
					+ vo.toWSString());
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "screeningId")
	public Integer storeOrUpdateScreening(java.lang.Integer screeningId,
			@WebParam(name = "dataCollectionGroupId") java.lang.Integer dataCollectionGroupId,
			@WebParam(name = "diffractionPlanId") java.lang.Integer diffractionPlanId,
			@WebParam(name = "recordTimeStamp") Date recordTimeStamp,
			@WebParam(name = "programVersion") java.lang.String programVersion,
			@WebParam(name = "comments") java.lang.String comments,
			@WebParam(name = "shortComments") java.lang.String shortComments,
			@WebParam(name = "xmlSampleInformation") java.lang.String xmlSampleInformation) throws Exception {
		ScreeningWS3VO vo = new ScreeningWS3VO(screeningId, dataCollectionGroupId, diffractionPlanId, recordTimeStamp,
				programVersion, comments, shortComments, xmlSampleInformation);
		return storeOrUpdateScreeningValue(vo);
	}
	
	@WebMethod
	@WebResult(name = "screeningRankId")
	public Integer storeOrUpdateScreeningRankValue(@WebParam(name = "screeningRank") ScreeningRankWS3VO vo)
			throws Exception {
		try {

			if (vo == null)
				return null;

			if (vo.getScreeningId() == null) {
				LOG.debug("WS PB : ScreeningId is null , could not update/create ScreeningRank");
				return errorCodeFK;
			}

			if (vo.getScreeningRankSetId() == null) {
				LOG.debug("WS PB : ScreeningRankSetId is null , could not update/create ScreeningRank");
				return errorCodeFK;
			}

			ScreeningRank3VO screeningRankValue = null;
			ScreeningRank3Service screeningRankService = (ScreeningRank3Service) ejb3ServiceLocator
					.getLocalService(ScreeningRank3Service.class);

			if (vo.getScreeningId() == null || vo.getScreeningId() == 0)
				vo.setScreeningId(null);
			if (vo.getScreeningRankSetId() != null && vo.getScreeningRankSetId() == 0)
				vo.setScreeningRankSetId(null);

			ScreeningRank3VO screeningRankVO = new ScreeningRank3VO();
			ScreeningRankSet3Service screeningRankSetService = (ScreeningRankSet3Service) ejb3ServiceLocator
					.getLocalService(ScreeningRankSet3Service.class);
			ScreeningRankSet3VO screeningRankSetVO = null;
			if (vo.getScreeningRankSetId() != null && vo.getScreeningRankSetId() > 0)
				screeningRankSetVO = screeningRankSetService.findByPk(vo.getScreeningRankSetId(), false);
			Screening3Service screeningService = (Screening3Service) ejb3ServiceLocator
					.getLocalService(Screening3Service.class);
			Screening3VO screeningVO = null;
			if (vo.getScreeningId() != null && vo.getScreeningId() > 0)
				screeningVO = screeningService.findByPk(vo.getScreeningId(), false, false);
			Integer screeningRankId = vo.getScreeningRankId();
			screeningRankVO.fillVOFromWS(vo);
			screeningRankVO.setScreeningRankSetVO(screeningRankSetVO);
			screeningRankVO.setScreeningVO(screeningVO);

			if (screeningRankId == null || screeningRankId == 0) {
				screeningRankVO.setScreeningRankId(null);
				screeningRankValue = screeningRankService.create(screeningRankVO);
			} else {
				screeningRankValue = screeningRankService.update(screeningRankVO);
			}
			return screeningRankValue.getScreeningRankId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateScreeningRankValue - " + StringUtils.getCurrentDate() + " - "
					+ vo.toWSString());
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "screeningRankId")
	public Integer storeOrUpdateScreeningRank(java.lang.Integer screeningRankId,
			@WebParam(name = "screeningRankSetId") Integer screeningRankSetId,
			@WebParam(name = "screeningId") Integer screeningId, @WebParam(name = "rankValue") Double rankValue,
			@WebParam(name = "rankInformation") String rankInformation) throws Exception {
		ScreeningRankWS3VO vo = new ScreeningRankWS3VO(screeningRankId, screeningRankSetId, screeningId, rankValue,
				rankInformation);
		return storeOrUpdateScreeningRankValue(vo);
	}

	@WebMethod
	@WebResult(name = "screeningRankSetId")
	public Integer storeOrUpdateScreeningRankSetValue(@WebParam(name = "screeningRankSet") ScreeningRankSet3VO vo)
			throws Exception {
		try {

			ScreeningRankSet3VO screeningRankSetValue = null;
			ScreeningRankSet3Service screeningRankSetService = (ScreeningRankSet3Service) ejb3ServiceLocator
					.getLocalService(ScreeningRankSet3Service.class);

			if (vo != null) {
				Integer screeningRankSetId = vo.getScreeningRankSetId();
				ScreeningRankSet3VO screeningRankSet = new ScreeningRankSet3VO();
				// load the object elsewhere there is an error with the childs
				if (screeningRankSetId != null && screeningRankSetId > 0) {
					screeningRankSet = screeningRankSetService.findByPk(screeningRankSetId, false);
				}
				screeningRankSet.setScreeningRankSetId(screeningRankSetId);
				screeningRankSet.setRankEngine(vo.getRankEngine());
				screeningRankSet.setRankingProjectFileName(vo.getRankingProjectFileName());
				screeningRankSet.setRankingSummaryFileName(vo.getRankingSummaryFileName());
				if (screeningRankSetId == null || screeningRankSetId == 0) {
					screeningRankSet.setScreeningRankSetId(null);
					screeningRankSetValue = screeningRankSetService.create(screeningRankSet);
				} else {
					screeningRankSetValue = screeningRankSetService.update(screeningRankSet);
				}
				return screeningRankSetValue.getScreeningRankSetId();
			}
			return null;
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateScreeningRankSetValue - " + StringUtils.getCurrentDate() + " - "
					+ vo.toWSString());
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "screeningRankSetId")
	public Integer storeOrUpdateScreeningRankSet(java.lang.Integer screeningRankSetId,
			@WebParam(name = "rankEngine") String rankEngine,
			@WebParam(name = "rankingProjectFileName") String rankingProjectFileName,
			@WebParam(name = "rankingSummaryFileName") String rankingSummaryFileName) throws Exception {
		ScreeningRankSet3VO vo = new ScreeningRankSet3VO(screeningRankSetId, rankEngine, rankingProjectFileName,
				rankingSummaryFileName);
		return storeOrUpdateScreeningRankSetValue(vo);
	}

	private Integer storeOrUpdateScreeningOutputValue(ScreeningOutputWS3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdateScreeningOutputValue");
			if (vo == null)
				return null;

			if (vo.getScreeningId() == null) {
				LOG.debug("WS PB : ScreeningId is null , could not update/create ScreeningOutput");
				return errorCodeFK;
			}

			ScreeningOutput3VO screeningOutputValue = null;
			ScreeningOutput3Service screeningOutputService = (ScreeningOutput3Service) ejb3ServiceLocator
					.getLocalService(ScreeningOutput3Service.class);

			if (vo.getScreeningId() == null || vo.getScreeningId() == 0)
				vo.setScreeningId(null);

			ScreeningOutput3VO screeningOutputVO = new ScreeningOutput3VO();
			Screening3Service screeningService = (Screening3Service) ejb3ServiceLocator
					.getLocalService(Screening3Service.class);
			Screening3VO screeningVO = null;
			if (vo.getScreeningId() != null && vo.getScreeningId() > 0)
				screeningVO = screeningService.findByPk(vo.getScreeningId(), false, false);
			Integer screeningOutputId = vo.getScreeningOutputId();
			// load the object elsewhere there is an error with the childs
			if (screeningOutputId != null && screeningOutputId > 0) {
				screeningOutputVO = screeningOutputService.findByPk(screeningOutputId, false, false);
			}
			screeningOutputVO.fillVOFromWS(vo);
			screeningOutputVO.setScreeningVO(screeningVO);

			if (screeningOutputId == null || screeningOutputId == 0) {
				screeningOutputVO.setScreeningOutputId(null);
				screeningOutputValue = screeningOutputService.create(screeningOutputVO);
			} else {
				screeningOutputValue = screeningOutputService.update(screeningOutputVO);
			}
			LOG.debug("storeOrUpdateScreeningOutputValue " + screeningOutputValue.getScreeningOutputId());

			return screeningOutputValue.getScreeningOutputId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateScreeningOutputValue - " + StringUtils.getCurrentDate() + " - "
					+ vo.toWSString());
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "screeningOutputId")
	public Integer storeOrUpdateScreeningOutput(java.lang.Integer screeningOutputId,
			@WebParam(name = "screeningId") Integer screeningId,
			@WebParam(name = "statusDescription") String statusDescription,
			@WebParam(name = "rejectedReflections") Integer rejectedReflections,
			@WebParam(name = "resolutionObtained") Double resolutionObtained,
			@WebParam(name = "spotDeviationR") Double spotDeviationR,
			@WebParam(name = "spotDeviationTheta") Double spotDeviationTheta,
			@WebParam(name = "beamShiftX") Double beamShiftX, @WebParam(name = "beamShiftY") Double beamShiftY,
			@WebParam(name = "numSpotsFound") Integer numSpotsFound,
			@WebParam(name = "numSpotsUsed") Integer numSpotsUsed,
			@WebParam(name = "numSpotsRejected") Integer numSpotsRejected,
			@WebParam(name = "mosaicity") Double mosaicity, @WebParam(name = "ioverSigma") Double ioverSigma,
			@WebParam(name = "diffractionRings") Byte diffractionRings,
			@WebParam(name = "strategySuccess") Byte strategySuccess,
			@WebParam(name = "indexingSuccess") Byte indexingSuccess,
			@WebParam(name = "mosaicityEstimated") Byte mosaicityEstimated,
			@WebParam(name = "rankingResolution") Double rankingResolution, @WebParam(name = "program") String program,
			@WebParam(name = "doseTotal") Double doseTotal,
			@WebParam(name = "totalExposureTime") Double totalExposureTime,
			@WebParam(name = "totalRotationRange") Double totalRotationRange,
			@WebParam(name = "totalNumberOfImages") Integer totalNumberOfImages,
			@WebParam(name = "rFriedel") Double rFriedel) throws Exception {
		ScreeningOutputWS3VO vo = new ScreeningOutputWS3VO(screeningOutputId, screeningId, statusDescription,
				rejectedReflections, resolutionObtained, spotDeviationR, spotDeviationTheta, beamShiftX, beamShiftY,
				numSpotsFound, numSpotsUsed, numSpotsRejected, mosaicity, ioverSigma, diffractionRings,
				strategySuccess, indexingSuccess, mosaicityEstimated, rankingResolution, program, doseTotal,
				totalExposureTime, totalRotationRange, totalNumberOfImages, rFriedel);
		return storeOrUpdateScreeningOutputValue(vo);
	}

	private Integer storeOrUpdateScreeningOutputLatticeValue(ScreeningOutputLatticeWS3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdateScreeningOutputLatticeValue");
			if (vo == null)
				return null;

			if (vo.getScreeningOutputId() == null) {
				LOG.debug("WS PB : ScreeningOutputId is null , could not update/create ScreeningOutputLattice");
				return errorCodeFK;
			}

			ScreeningOutputLattice3VO screeningOutputLatticeValue = null;
			ScreeningOutputLattice3Service screeningOutputLatticeService = (ScreeningOutputLattice3Service) ejb3ServiceLocator
					.getLocalService(ScreeningOutputLattice3Service.class);

			if (vo.getScreeningOutputId() == null || vo.getScreeningOutputId() == 0)
				vo.setScreeningOutputId(null);

			ScreeningOutputLattice3VO screeningOutputLatticeVO = new ScreeningOutputLattice3VO();
			ScreeningOutput3Service screeningOutputService = (ScreeningOutput3Service) ejb3ServiceLocator
					.getLocalService(ScreeningOutput3Service.class);
			ScreeningOutput3VO screeningOutputVO = null;
			if (vo.getScreeningOutputId() != null && vo.getScreeningOutputId() > 0)
				screeningOutputVO = screeningOutputService.findByPk(vo.getScreeningOutputId(), false, false);
			Integer screeningOutputLatticeId = vo.getScreeningOutputLatticeId();

			screeningOutputLatticeVO.fillVOFromWS(vo);
			screeningOutputLatticeVO.setScreeningOutputVO(screeningOutputVO);
			screeningOutputLatticeVO.setTimeStamp(StringUtils.getCurrentTimeStamp());
			if (screeningOutputLatticeId == null || screeningOutputLatticeId == 0) {
				screeningOutputLatticeVO.setScreeningOutputLatticeId(null);
				screeningOutputLatticeValue = screeningOutputLatticeService.create(screeningOutputLatticeVO);
			} else {
				screeningOutputLatticeValue = screeningOutputLatticeService.update(screeningOutputLatticeVO);
			}
			LOG.debug("storeOrUpdateScreeningOutputLatticeValue "
					+ screeningOutputLatticeValue.getScreeningOutputLatticeId());

			return screeningOutputLatticeValue.getScreeningOutputLatticeId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateScreeningOutputLatticeValue - " + StringUtils.getCurrentDate() + " - "
					+ vo.toWSString());
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "screeningOutputLatticeId")
	public Integer storeOrUpdateScreeningOutputLattice(java.lang.Integer screeningOutputLatticeId,
			@WebParam(name = "screeningOutputId") Integer screeningOutputId,
			@WebParam(name = "spaceGroup") String spaceGroup, @WebParam(name = "pointGroup") String pointGroup,
			@WebParam(name = "bravaisLattice") String bravaisLattice,
			@WebParam(name = "rawOrientationMatrix_a_x") Double rawOrientationMatrix_a_x,
			@WebParam(name = "rawOrientationMatrix_a_y") Double rawOrientationMatrix_a_y,
			@WebParam(name = "rawOrientationMatrix_a_z") Double rawOrientationMatrix_a_z,
			@WebParam(name = "rawOrientationMatrix_b_x") Double rawOrientationMatrix_b_x,
			@WebParam(name = "rawOrientationMatrix_b_y") Double rawOrientationMatrix_b_y,
			@WebParam(name = "rawOrientationMatrix_b_z") Double rawOrientationMatrix_b_z,
			@WebParam(name = "rawOrientationMatrix_c_x") Double rawOrientationMatrix_c_x,
			@WebParam(name = "rawOrientationMatrix_c_y") Double rawOrientationMatrix_c_y,
			@WebParam(name = "rawOrientationMatrix_c_z") Double rawOrientationMatrix_c_z,
			@WebParam(name = "unitCell_a") Double unitCell_a, @WebParam(name = "unitCell_b") Double unitCell_b,
			@WebParam(name = "unitCell_c") Double unitCell_c, @WebParam(name = "unitCell_alpha") Double unitCell_alpha,
			@WebParam(name = "unitCell_beta") Double unitCell_beta,
			@WebParam(name = "unitCell_gamma") Double unitCell_gamma,
			@WebParam(name = "recordTimeStamp") Date recordTimeStamp,
			@WebParam(name = "labelitIndexing") Boolean labelitIndexing) throws Exception {
		ScreeningOutputLatticeWS3VO vo = new ScreeningOutputLatticeWS3VO(screeningOutputLatticeId, screeningOutputId,
				spaceGroup, pointGroup, bravaisLattice, rawOrientationMatrix_a_x, rawOrientationMatrix_a_y,
				rawOrientationMatrix_a_z, rawOrientationMatrix_b_x, rawOrientationMatrix_b_y, rawOrientationMatrix_b_z,
				rawOrientationMatrix_c_x, rawOrientationMatrix_c_y, rawOrientationMatrix_c_z, unitCell_a, unitCell_b,
				unitCell_c, unitCell_alpha, unitCell_beta, unitCell_gamma, recordTimeStamp, labelitIndexing);
		return storeOrUpdateScreeningOutputLatticeValue(vo);
	}

	private Integer storeOrUpdateScreeningStrategyValue(ScreeningStrategyWS3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdateScreeningStrategyValue");
			if (vo == null)
				return null;

			if (vo.getScreeningOutputId() == null) {
				LOG.debug("WS PB : ScreeningOutputId is null , could not update/create ScreeningStrategy");
				return errorCodeFK;
			}

			ScreeningStrategy3VO screeningStrategyValue = null;
			ScreeningStrategy3Service screeningStrategyService = (ScreeningStrategy3Service) ejb3ServiceLocator
					.getLocalService(ScreeningStrategy3Service.class);

			if (vo.getScreeningOutputId() == null || vo.getScreeningOutputId() == 0)
				vo.setScreeningOutputId(null);

			ScreeningStrategy3VO screeningStrategyVO = new ScreeningStrategy3VO();
			ScreeningOutput3Service screeningOutputService = (ScreeningOutput3Service) ejb3ServiceLocator
					.getLocalService(ScreeningOutput3Service.class);
			ScreeningOutput3VO screeningOutputVO = null;
			if (vo.getScreeningOutputId() != null && vo.getScreeningOutputId() > 0)
				screeningOutputVO = screeningOutputService.findByPk(vo.getScreeningOutputId(), false, false);
			Integer screeningStrategyId = vo.getScreeningStrategyId();
			// load the object elsewhere there is an error with the childs
			if (screeningStrategyId != null && screeningStrategyId > 0) {
				screeningStrategyVO = screeningStrategyService.findByPk(screeningStrategyId, false);
			}
			screeningStrategyVO.fillVOFromWS(vo);
			screeningStrategyVO.setScreeningOutputVO(screeningOutputVO);

			if (screeningStrategyId == null || screeningStrategyId == 0) {
				screeningStrategyVO.setScreeningStrategyId(null);
				screeningStrategyValue = screeningStrategyService.create(screeningStrategyVO);
			} else {
				screeningStrategyValue = screeningStrategyService.update(screeningStrategyVO);
			}
			LOG.debug("storeOrUpdateScreeningStrategyValue " + screeningStrategyValue.getScreeningStrategyId());

			return screeningStrategyValue.getScreeningStrategyId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateScreeningStrategyValue - " + StringUtils.getCurrentDate() + " - "
					+ vo.toWSString());
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "screeningStrategyId")
	public Integer storeOrUpdateScreeningStrategy(java.lang.Integer screeningStrategyId,
			@WebParam(name = "screeningOutputId") Integer screeningOutputId,
			@WebParam(name = "phiStart") Double phiStart, @WebParam(name = "phiEnd") Double phiEnd,
			@WebParam(name = "rotation") Double rotation, @WebParam(name = "exposureTime") Double exposureTime,
			@WebParam(name = "resolution") Double resolution, @WebParam(name = "completeness") Double completeness,
			@WebParam(name = "multiplicity") Double multiplicity, @WebParam(name = "anomalous") Byte anomalous,
			@WebParam(name = "program") String program, @WebParam(name = "rankingResolution") Double rankingResolution,
			@WebParam(name = "transmission") Double transmission) throws Exception {
		ScreeningStrategyWS3VO vo = new ScreeningStrategyWS3VO(screeningStrategyId, screeningOutputId, phiStart,
				phiEnd, rotation, exposureTime, resolution, completeness, multiplicity, anomalous, program,
				rankingResolution, transmission);
		return storeOrUpdateScreeningStrategyValue(vo);
	}

	private Integer storeOrUpdateScreeningStrategyWedgeValue(ScreeningStrategyWedgeWS3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdateScreeningStrategyWedgeValue");

			if (vo == null)
				return null;

			ScreeningStrategyWedge3VO screeningStrategyWedgeValue = null;
			ScreeningStrategyWedge3Service screeningStrategyWedgeService = (ScreeningStrategyWedge3Service) ejb3ServiceLocator
					.getLocalService(ScreeningStrategyWedge3Service.class);

			if (vo.getScreeningStrategyId() == null || vo.getScreeningStrategyId() == 0)
				vo.setScreeningStrategyId(null);

			if (vo.getScreeningStrategyId() == null) {
				LOG.debug("WS PB : ScreeningStrategyId is null , could not update/create ScreeningStrategyWedge");
				return errorCodeFK;
			}
			Integer screeningStrategyWedgeId = vo.getScreeningStrategyWedgeId();

			ScreeningStrategyWedge3VO screeningStrategyWedgeVO = new ScreeningStrategyWedge3VO();
			ScreeningStrategy3Service screeningStrategyService = (ScreeningStrategy3Service) ejb3ServiceLocator
					.getLocalService(ScreeningStrategy3Service.class);
			ScreeningStrategy3VO screeningStrategyVO = null;
			if (vo.getScreeningStrategyId() != null && vo.getScreeningStrategyId() > 0)
				screeningStrategyVO = screeningStrategyService.findByPk(vo.getScreeningStrategyId(), false);
			// load the object elsewhere there is an error with the childs
			if (screeningStrategyWedgeId != null && screeningStrategyWedgeId > 0) {
				screeningStrategyWedgeVO = screeningStrategyWedgeService.findByPk(screeningStrategyWedgeId, false);
			}
			screeningStrategyWedgeVO.fillVOFromWS(vo);
			screeningStrategyWedgeVO.setScreeningStrategyVO(screeningStrategyVO);

			if (screeningStrategyWedgeId == null || screeningStrategyWedgeId == 0) {
				screeningStrategyWedgeVO.setScreeningStrategyWedgeId(null);
				screeningStrategyWedgeValue = screeningStrategyWedgeService.create(screeningStrategyWedgeVO);
			} else {
				screeningStrategyWedgeValue = screeningStrategyWedgeService.update(screeningStrategyWedgeVO);
			}
			LOG.debug("storeOrUpdateScreeningStrategyWedgeValue "
					+ screeningStrategyWedgeValue.getScreeningStrategyWedgeId());

			return screeningStrategyWedgeValue.getScreeningStrategyWedgeId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateScreeningStrategyWedgeValue - " + StringUtils.getCurrentDate() + " - "
					+ vo.toWSString());
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "screeningStrategyWedgeId")
	public Integer storeOrUpdateScreeningStrategyWedge(java.lang.Integer screeningStrategyWedgeId,
			@WebParam(name = "screeningStrategyId") Integer screeningStrategyId,
			@WebParam(name = "wedgeNumber") Integer wedgeNumber, @WebParam(name = "resolution") Double resolution,
			@WebParam(name = "completeness") Double completeness, @WebParam(name = "multiplicity") Double multiplicity,
			@WebParam(name = "doseTotal") Double doseTotal, @WebParam(name = "numberOfImages") Integer numberOfImages,
			@WebParam(name = "phi") Double phi, @WebParam(name = "kappa") Double kappa, @WebParam(name = "chi") Double chi,
			@WebParam(name = "comments") String comments, @WebParam(name = "wavelength") Double wavelength)
			throws Exception {
		ScreeningStrategyWedgeWS3VO vo = new ScreeningStrategyWedgeWS3VO(screeningStrategyWedgeId, screeningStrategyId,
				wedgeNumber, resolution, completeness, multiplicity, doseTotal, numberOfImages, phi, kappa, chi, comments,
				wavelength);
		return storeOrUpdateScreeningStrategyWedgeValue(vo);
	}

	private Integer storeOrUpdateScreeningStrategySubWedgeValue(ScreeningStrategySubWedgeWS3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdateScreeningStrategySubWedgeValue");
			if (vo == null)
				return null;

			ScreeningStrategySubWedge3VO screeningStrategySubWedgeValue = null;
			ScreeningStrategySubWedge3Service screeningStrategySubWedgeService = (ScreeningStrategySubWedge3Service) ejb3ServiceLocator
					.getLocalService(ScreeningStrategySubWedge3Service.class);

			if (vo.getScreeningStrategyWedgeId() == null || vo.getScreeningStrategyWedgeId() == 0)
				vo.setScreeningStrategyWedgeId(null);

			if (vo.getScreeningStrategyWedgeId() == null) {
				LOG.debug("WS PB : ScreeningStrategyWedgeId is null , could not update/create ScreeningStrategySubWedge");
				return errorCodeFK;
			}

			Integer screeningStrategySubWedgeId = vo.getScreeningStrategySubWedgeId();

			ScreeningStrategySubWedge3VO screeningStrategySubWedgeVO = new ScreeningStrategySubWedge3VO();
			ScreeningStrategyWedge3Service screeningStrategyWedgeService = (ScreeningStrategyWedge3Service) ejb3ServiceLocator
					.getLocalService(ScreeningStrategyWedge3Service.class);
			ScreeningStrategyWedge3VO screeningStrategyWedgeVO = null;
			if (vo.getScreeningStrategyWedgeId() != null && vo.getScreeningStrategyWedgeId() > 0)
				screeningStrategyWedgeVO = screeningStrategyWedgeService.findByPk(vo.getScreeningStrategyWedgeId(),
						false);
			screeningStrategySubWedgeVO.fillVOFromWS(vo);
			screeningStrategySubWedgeVO.setScreeningStrategyWedgeVO(screeningStrategyWedgeVO);

			if (screeningStrategySubWedgeId == null || screeningStrategySubWedgeId == 0) {
				screeningStrategySubWedgeVO.setScreeningStrategySubWedgeId(null);
				screeningStrategySubWedgeValue = screeningStrategySubWedgeService.create(screeningStrategySubWedgeVO);
			} else {
				screeningStrategySubWedgeValue = screeningStrategySubWedgeService.update(screeningStrategySubWedgeVO);
			}
			LOG.debug("storeOrUpdateScreeningStrategySubWedgeValue "
					+ screeningStrategySubWedgeValue.getScreeningStrategySubWedgeId());

			return screeningStrategySubWedgeValue.getScreeningStrategySubWedgeId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateScreeningStrategySubWedgeValue - " + StringUtils.getCurrentDate() + " - "
					+ vo.toWSString());
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "screeningStrategySubWedgeId")
	public Integer storeOrUpdateScreeningStrategySubWedge(java.lang.Integer screeningStrategySubWedgeId,
			@WebParam(name = "screeningStrategyWedgeId") Integer screeningStrategyWedgeId,
			@WebParam(name = "subWedgeNumber") Integer subWedgeNumber,
			@WebParam(name = "rotationAxis") String rotationAxis, @WebParam(name = "axisStart") Double axisStart,
			@WebParam(name = "axisEnd") Double axisEnd, @WebParam(name = "exposureTime") Double exposureTime,
			@WebParam(name = "transmission") Double transmission,
			@WebParam(name = "oscillationRange") Double oscillationRange,
			@WebParam(name = "completeness") Double completeness, @WebParam(name = "multiplicity") Double multiplicity,
			@WebParam(name = "doseTotal") Double doseTotal, @WebParam(name = "numberOfImages") Integer numberOfImages,
			@WebParam(name = "comments") String comments) throws Exception {
		ScreeningStrategySubWedgeWS3VO vo = new ScreeningStrategySubWedgeWS3VO(screeningStrategySubWedgeId,
				screeningStrategyWedgeId, subWedgeNumber, rotationAxis, axisStart, axisEnd, exposureTime, transmission,
				oscillationRange, completeness, multiplicity, doseTotal, numberOfImages, comments);
		return storeOrUpdateScreeningStrategySubWedgeValue(vo);
	}

	private Integer storeOrUpdateScreeningInputValue(ScreeningInputWS3VO vo) throws Exception {

		LOG.debug("storeOrUpdateScreeningInputValue");
		if (vo == null)
			return null;

		if (vo.getScreeningId() == null) {
			LOG.debug("WS PB : ScreeningId is null , could not update/create ScreeningInput");
			return errorCodeFK;
		}

		ScreeningInput3VO screeningInputValue = null;
		ScreeningInput3Service screeningInputService = (ScreeningInput3Service) ejb3ServiceLocator
				.getLocalService(ScreeningInput3Service.class);

		if (vo.getScreeningId() == null || vo.getScreeningId() == 0)
			vo.setScreeningId(null);
		Integer screeningInputId = vo.getScreeningInputId();

		ScreeningInput3VO screeningInputVO = new ScreeningInput3VO();
		Screening3Service screeningService = (Screening3Service) ejb3ServiceLocator
				.getLocalService(Screening3Service.class);
		Screening3VO screeningVO = null;
		if (vo.getScreeningId() != null && vo.getScreeningId() > 0)
			screeningVO = screeningService.findByPk(vo.getScreeningId(), false, false);
		DiffractionPlan3Service diffractionPlanService = (DiffractionPlan3Service) ejb3ServiceLocator
				.getLocalService(DiffractionPlan3Service.class);
		DiffractionPlan3VO diffractionPlanVO = null;
		if (vo.getDiffractionPlanId() != null && vo.getDiffractionPlanId() > 0)
			diffractionPlanVO = diffractionPlanService.findByPk(vo.getDiffractionPlanId(), false, false);
		screeningInputVO.fillVOFromWS(vo);
		screeningInputVO.setScreeningVO(screeningVO);
		screeningInputVO.setDiffractionPlanVO(diffractionPlanVO);

		if (screeningInputId == null || screeningInputId == 0) {
			screeningInputVO.setScreeningInputId(null);
			screeningInputValue = screeningInputService.create(screeningInputVO);
		} else {
			screeningInputValue = screeningInputService.update(screeningInputVO);
		}
		LOG.debug("storeOrUpdateScreeningInputValue " + screeningInputValue.getScreeningInputId());
		return screeningInputValue.getScreeningInputId();
	}

	@WebMethod
	@WebResult(name = "screeningInputId")
	public Integer storeOrUpdateScreeningInput(java.lang.Integer screeningInputId,
			@WebParam(name = "screeningId") Integer screeningId,
			@WebParam(name = "diffractionPlanId") Integer diffractionPlanId, @WebParam(name = "beamX") Double beamX,
			@WebParam(name = "beamY") Double beamY, @WebParam(name = "rmsErrorLimits") Double rmsErrorLimits,
			@WebParam(name = "minimumFractionIndexed") Double minimumFractionIndexed,
			@WebParam(name = "maximumFractionRejected") Double maximumFractionRejected,
			@WebParam(name = "minimumSignalToNoise") Double minimumSignalToNoise,
			@WebParam(name = "xmlSampleInformation") String xmlSampleInformation) throws Exception {
		ScreeningInputWS3VO vo = new ScreeningInputWS3VO(screeningInputId, screeningId, diffractionPlanId, beamX,
				beamY, rmsErrorLimits, minimumFractionIndexed, maximumFractionRejected, minimumSignalToNoise,
				xmlSampleInformation);
		return storeOrUpdateScreeningInputValue(vo);
	}

}
