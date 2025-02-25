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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ws.api.annotation.WebContext;

import ispyb.common.util.Constants;
import ispyb.common.util.StringUtils;
import ispyb.common.util.beamlines.EMBLBeamlineEnum;
import ispyb.common.util.beamlines.ESRFBeamlineEnum;
import ispyb.common.util.beamlines.MAXIVBeamlineEnum;
import ispyb.common.util.beamlines.SOLEILBeamlineEnum;
import ispyb.server.biosaxs.services.core.structure.Structure3Service;
import ispyb.server.biosaxs.vos.assembly.Structure3VO;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.services.shipping.Container3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.mx.services.collections.Position3Service;
import ispyb.server.mx.services.sample.BLSample3Service;
import ispyb.server.mx.services.sample.BLSampleImage3Service;
import ispyb.server.mx.services.sample.BLSubSample3Service;
import ispyb.server.mx.services.sample.Crystal3Service;
import ispyb.server.mx.services.sample.DiffractionPlan3Service;
import ispyb.server.mx.vos.collections.Position3VO;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.BLSampleImage3VO;
import ispyb.server.mx.vos.sample.BLSampleImageWS3VO;
import ispyb.server.mx.vos.sample.BLSampleWS3VO;
import ispyb.server.mx.vos.sample.BLSubSample3VO;
import ispyb.server.mx.vos.sample.BLSubSampleWS3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.DiffractionPlan3VO;
import ispyb.server.mx.vos.sample.DiffractionPlanWS3VO;
import ispyb.server.mx.vos.sample.Protein3VO;
import ispyb.server.mx.vos.sample.SampleInfo;

/**
 * Web services for BLSample
 * 
 * @author BODIN
 * 
 */
@WebService(name = "ToolsForBLSampleWebService", serviceName = "ispybWS", targetNamespace = "http://ispyb.ejb3.webservices.sample")
@SOAPBinding(style = Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Stateless
@RolesAllowed({ "WebService", "User", "Industrial" })
// allow special access roles
@SecurityDomain("ispyb")
@WebContext(authMethod = "BASIC", secureWSDLAccess = false, transportGuarantee = "NONE")
public class ToolsForBLSampleWebService {
	private final static Logger LOG = Logger.getLogger(ToolsForBLSampleWebService.class);

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private final Integer errorCodeFK = null;// -1;

	private long now;

	@Resource
	private SessionContext sessionContext;

	@WebMethod(operationName = "echo")
	@WebResult(name = "echo")
	public String echo() {
		return "echo from server...";
	}

	private Integer storeOrUpdateDiffractionPlanValue(DiffractionPlanWS3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdateDiffractionPlan");
			long startTime = System.currentTimeMillis();

			if (vo == null)
				return null;

			DiffractionPlan3VO diffractionPlanValue = new DiffractionPlan3VO();
			DiffractionPlan3Service diffractionPlanService = (DiffractionPlan3Service) ejb3ServiceLocator
					.getLocalService(DiffractionPlan3Service.class);

			if (vo.getExperimentKind() == null || vo.getExperimentKind().trim().equals("")) {
				vo.setExperimentKind(Constants.DEFAULT_DIFFRACTION_PLAN_EXPERIMENT_KIND);
			}
			Integer diffractionPlanId = vo.getDiffractionPlanId();
			// load the object elsewhere there is an error with the childs
			if (diffractionPlanId != null && diffractionPlanId > 0) {
				diffractionPlanValue = diffractionPlanService.findByPk(diffractionPlanId, false, false);
			}
			diffractionPlanValue.fillVOFromWS(vo);

			if (diffractionPlanId == null || diffractionPlanId == 0) {
				diffractionPlanValue.setDiffractionPlanId(null);
				diffractionPlanValue = diffractionPlanService.create(diffractionPlanValue);
			} else {
				diffractionPlanValue = diffractionPlanService.update(diffractionPlanValue);
			}
			diffractionPlanId = diffractionPlanValue.getDiffractionPlanId();
			long duration = System.currentTimeMillis() - startTime;
			LOG.debug("storeOrUpdateDiffractionPlan : " + diffractionPlanId + " time = " + duration + " ms");
			return diffractionPlanId;
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateDiffractionPlanValue - " + StringUtils.getCurrentDate() + " - " + vo.toWSString());
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "diffractionPlanId")
	public Integer storeOrUpdateDiffractionPlan(java.lang.Integer diffractionPlanId, 
	@WebParam(name = "experimentKind")
	String experimentKind, 
	@WebParam(name = "observedResolution")
	Double observedResolution, 
	@WebParam(name = "minimalResolution")
	Double minimalResolution, 
	@WebParam(name = "exposureTime")
	Double exposureTime, 
	@WebParam(name = "oscillationRange")
	Double oscillationRange, 
	@WebParam(name = "maximalResolution")
	Double maximalResolution, 
	@WebParam(name = "screeningResolution")
	Double screeningResolution, 
	@WebParam(name = "radiationSensitivity")
	Double radiationSensitivity, 
	@WebParam(name = "anomalousScatterer")
	String anomalousScatterer, 
	@WebParam(name = "preferredBeamSizeX")
	Double preferredBeamSizeX, 
	@WebParam(name = "preferredBeamSizeY")
	Double preferredBeamSizeY,
	@WebParam(name = "preferredBeamDiameter") Double preferredBeamDiameter,
	@WebParam(name = "comments") String comments, 
	@WebParam(name = "aimedCompleteness")
	Double aimedCompleteness, @WebParam(name = "aimedIOverSigmaAtHighestRes")
	Double aimedIOverSigmaAtHighestRes, @WebParam(name = "aimedMultiplicity")
	Double aimedMultiplicity, @WebParam(name = "aimedResolution")
	Double aimedResolution, @WebParam(name = "anomalousData")
	Boolean anomalousData, @WebParam(name = "complexity")
	String complexity, @WebParam(name = "estimateRadiationDamage")
	Boolean estimateRadiationDamage, @WebParam(name = "forcedSpaceGroup")
	String forcedSpaceGroup, @WebParam(name = "requiredCompleteness")
	Double requiredCompleteness, @WebParam(name = "requiredMultiplicity")
	Double requiredMultiplicity, @WebParam(name = "requiredResolution")
	Double requiredResolution, @WebParam(name = "strategyOption")
	String strategyOption, @WebParam(name = "kappaStrategyOption")
	String kappaStrategyOption, @WebParam(name = "numberOfPositions")
	Integer numberOfPositions, @WebParam(name = "minDimAccrossSpindleAxis")
	Double minDimAccrossSpindleAxis, @WebParam(name = "maxDimAccrossSpindleAxis")
	Double maxDimAccrossSpindleAxis, @WebParam(name = "radiationSensitivityBeta")
	Double radiationSensitivityBeta, 
	@WebParam(name = "radiationSensitivityGamma") Double radiationSensitivityGamma, 
	@WebParam(name = "minOscWidth") Double minOscWidth,
	@WebParam(name = "axisRange") Double axisRange
	) throws Exception {
		DiffractionPlanWS3VO vo = new DiffractionPlanWS3VO(diffractionPlanId,  experimentKind, observedResolution,
				minimalResolution, exposureTime, oscillationRange, maximalResolution, screeningResolution, radiationSensitivity,
				anomalousScatterer, preferredBeamSizeX, preferredBeamSizeY, preferredBeamDiameter, comments, aimedCompleteness, aimedIOverSigmaAtHighestRes,
				aimedMultiplicity, aimedResolution, anomalousData, complexity, estimateRadiationDamage, forcedSpaceGroup,
				requiredCompleteness, requiredMultiplicity, requiredResolution, strategyOption, kappaStrategyOption,
				numberOfPositions, minDimAccrossSpindleAxis, maxDimAccrossSpindleAxis, radiationSensitivityBeta,
				radiationSensitivityGamma,minOscWidth, axisRange);
		return storeOrUpdateDiffractionPlanValue(vo);
	}

	@WebMethod
	@WebResult(name = "blSample")
	public BLSampleWS3VO findBLSample(@WebParam(name = "blSampleId")
	Integer blSampleId) throws Exception {
		try {
			LOG.debug("findBLSample");
			long startTime = System.currentTimeMillis();
			BLSampleWS3VO blSampleValue = null;
			BLSample3Service blSampleService = (BLSample3Service) ejb3ServiceLocator.getLocalService(BLSample3Service.class);

			blSampleValue = blSampleService.findForWSByPk(blSampleId, false, false, false);
			long duration = System.currentTimeMillis() - startTime;
			if (blSampleValue != null)
				LOG.debug("findBLSample : " + blSampleValue.getBlSampleId() + " time = " + duration + " ms");
			else
				LOG.debug("findBLSample : no sample found - time = " + duration + " ms");
			return blSampleValue;
		} catch (Exception e) {
			LOG.error("WS ERROR: findBLSample - " + StringUtils.getCurrentDate() + " - " + blSampleId);
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "blSampleId")
	public Integer storeOrUpdateBLSample(@WebParam(name = "blSample")
	BLSampleWS3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdateBLSample");
			long startTime = System.currentTimeMillis();
			if (vo == null)
				return null;

			if (vo.getDiffractionPlanId() == null || vo.getDiffractionPlanId() == 0) {
				vo.setDiffractionPlanId(null);
			}
			if (vo.getCrystalId() == null || vo.getCrystalId() == 0) {
				vo.setCrystalId(null);
			}
			if (vo.getContainerId() == null || vo.getContainerId() == 0) {
				vo.setContainerId(null);
			}

			if (vo.getCrystalId() == null) {
				LOG.debug("WS PB : CrystalId is null , could not create blSample");
				return errorCodeFK;
			}

			BLSample3Service blSampleService = (BLSample3Service) ejb3ServiceLocator.getLocalService(BLSample3Service.class);
			Crystal3Service crystalService = (Crystal3Service) ejb3ServiceLocator.getLocalService(Crystal3Service.class);
			DiffractionPlan3Service diffractionPlanService = (DiffractionPlan3Service) ejb3ServiceLocator
					.getLocalService(DiffractionPlan3Service.class);
			Container3Service containerService = (Container3Service) ejb3ServiceLocator.getLocalService(Container3Service.class);

			BLSample3VO blSampleVO = new BLSample3VO();

			Crystal3VO crystalVO = null;
			if (vo.getCrystalId() != null && vo.getCrystalId() > 0)
				crystalVO = crystalService.findByPk(vo.getCrystalId(), false);
			DiffractionPlan3VO diffractionPlanVO = null;
			if (vo.getDiffractionPlanId() != null && vo.getDiffractionPlanId() > 0)
				diffractionPlanVO = diffractionPlanService.findByPk(vo.getDiffractionPlanId(), false, false);
			Container3VO containerVO = null;
			if (vo.getContainerId() != null && vo.getContainerId() > 0)
				containerVO = containerService.findByPk(vo.getContainerId(), false);
			Integer blSampleId = vo.getBlSampleId();
			// load the object elsewhere there is an error with the childs
			if (blSampleId != null && blSampleId > 0) {
				blSampleVO = blSampleService.findByPk(blSampleId, false, false,false);
			}
			blSampleVO.fillVOFromWS(vo);
			blSampleVO.setCrystalVO(crystalVO);
			blSampleVO.setDiffractionPlanVO(diffractionPlanVO);
			blSampleVO.setContainerVO(containerVO);

			if (blSampleId == null || blSampleId == 0) {
				vo.setBlSampleId(null);
				blSampleVO = blSampleService.create(blSampleVO);
			} else {
				blSampleVO = blSampleService.update(blSampleVO);
			}
			blSampleId = blSampleVO.getBlSampleId();
			long duration = System.currentTimeMillis() - startTime;
			LOG.debug("storeOrUpdateBLSample : " + blSampleId + " time = " + duration + " ms");

			return blSampleVO.getBlSampleId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateBLSample - " + StringUtils.getCurrentDate() + " - " + vo.toWSString());
			throw e;
		}
	}

	/**
	 * Returns a Tuple of SampleInfo for a given proposalId and a specified container.beamlineLocation and with the blSampleStatus or
	 * containerStatus is processing
	 * 
	 * @param proposalId
	 * @param beamlineLocation
	 * @return
	 * @throws Exception
	 */
	@WebMethod
	@WebResult(name = "SampleInfos")
	public SampleInfo[] findSampleInfoLightForProposal(@WebParam(name = "proposalId")
	Integer proposalId, @WebParam(name = "beamlineLocation")
	String beamlineLocation) throws Exception {
		try {
			LOG.debug("findSampleInfoLightForProposal");
			long startTime = System.currentTimeMillis();

			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			BLSample3Service blSampleService = (BLSample3Service) ejb3ServiceLocator.getLocalService(BLSample3Service.class);

			SampleInfo[] sampleInfos = null;
			String status = Constants.PROCESSING_STATUS;
			String beamlineLoc = beamlineLocation;
			if (Constants.SITE_IS_ESRF()) {
				ESRFBeamlineEnum bl = ESRFBeamlineEnum.retrieveBeamlineWithName(beamlineLocation);
				beamlineLoc = bl.getBeamlineName();
			}
			if (Constants.SITE_IS_EMBL()) {
				EMBLBeamlineEnum bl = EMBLBeamlineEnum.retrieveBeamlineWithName(beamlineLocation);
				beamlineLoc = bl.getBeamlineName();
			}
			if (Constants.SITE_IS_MAXIV()) {
				MAXIVBeamlineEnum bl = MAXIVBeamlineEnum.retrieveBeamlineWithName(beamlineLocation);
				beamlineLoc = bl.getBeamlineName();
			}
			if (Constants.SITE_IS_SOLEIL()) {
				SOLEILBeamlineEnum bl = SOLEILBeamlineEnum.retrieveBeamlineWithName(beamlineLocation);
				beamlineLoc = bl.getBeamlineName();
			}
			// if (bl != null)
			// beamlineLoc = bl.getBeamlineName();
			sampleInfos = blSampleService.findForWSSampleInfoLight(proposalId, null, beamlineLoc, status);
			long endTime = System.currentTimeMillis();
			long duration = endTime - startTime;
			if (sampleInfos != null)
				LOG.debug("findSampleInfoLightForProposal : " + proposalId + " nb samples = " + sampleInfos.length + " time = "
						+ duration + " ms");
			else
				LOG.debug("findSampleInfoLightForProposal : " + proposalId + " no samples " + " time = " + duration + " ms");
			return sampleInfos;
		} catch (Exception e) {
			LOG.error("WS ERROR: findSampleInfoLightForProposal - " + StringUtils.getCurrentDate() + " - " + proposalId + ", "
					+ beamlineLocation);
			throw e;
		}
	}

	/**
	 * Returns a Tuple of SampleInfo for a given proposalCode and proposalNumber and a specified container.beamlineLocation and with
	 * the blSampleStatus or containerStatus is processing
	 * 
	 * @param proposalId
	 * @param beamlineLocation
	 * @return
	 * @throws Exception
	 */
	@WebMethod
	@WebResult(name = "SampleInfos")
	public SampleInfo[] findSampleInfoLightForProposalCodeAndNumber(@WebParam(name = "code")
	String code, @WebParam(name = "number")
	String number, @WebParam(name = "beamlineLocation")
	String beamlineLocation) throws Exception {
		try {
			LOG.debug("findSampleInfoLightForProposal");
			long startTime = System.currentTimeMillis();

			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			BLSample3Service blSampleService = (BLSample3Service) ejb3ServiceLocator.getLocalService(BLSample3Service.class);

			Proposal3Service proposalService = (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);

			Proposal3VO proposalValue = proposalService.findForWSByCodeAndNumber(StringUtils.getProposalCode(code), number);
			Integer proposalId = null;
			if (proposalValue != null)
				proposalId = proposalValue.getProposalId();
			SampleInfo[] sampleInfos = null;
			String status = Constants.PROCESSING_STATUS;
			String beamlineLoc = beamlineLocation;
			ESRFBeamlineEnum bl = ESRFBeamlineEnum.retrieveBeamlineWithName(beamlineLocation);
			if (bl != null)
				beamlineLoc = bl.getBeamlineName();
			if (Constants.SITE_IS_SOLEIL()) {
				SOLEILBeamlineEnum sbl = SOLEILBeamlineEnum.retrieveBeamlineWithName(beamlineLocation);
				beamlineLoc = sbl != null ? sbl.getBeamlineName() : beamlineLocation;
			}
			sampleInfos = blSampleService.findForWSSampleInfoLight(proposalId, null, beamlineLoc, status);
			long endTime = System.currentTimeMillis();
			long duration = endTime - startTime;
			if (sampleInfos != null)
				LOG.debug("findSampleInfoLightForProposal : " + code + "" + number + " nb samples = " + sampleInfos.length
						+ " time = " + duration + " ms");
			else
				LOG.debug("findSampleInfoLightForProposal : " + code + "" + number + " no samples " + " time = " + duration + " ms");
			return sampleInfos;
		} catch (Exception e) {
			LOG.error("WS ERROR: findSampleInfoLightForProposal - " + StringUtils.getCurrentDate() + " - " + code + "" + number + ", "
					+ beamlineLocation);
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
	public String getPdbFilePath(@WebParam(name = "proteinAcronym")
	String proteinAcronym, @WebParam(name = "spaceGroup")
	String spaceGroup) throws Exception {

		try {
			LOG.debug("getPdbFilePath : proteinAcronym= " + proteinAcronym + ", spaceGroup= " + spaceGroup);
			long startTime = System.currentTimeMillis();
			String pdbFilePath = "";
			Crystal3Service crystalService = (Crystal3Service) ejb3ServiceLocator.getLocalService(Crystal3Service.class);
			pdbFilePath = crystalService.findPdbFullPath(proteinAcronym, spaceGroup);
			long endTime = System.currentTimeMillis();
			long duration = endTime - startTime;
			LOG.debug("getPdbFilePath : proteinAcronym= " + proteinAcronym + ",, spaceGroup= " + spaceGroup + ",  time = " + duration
					+ " ms");
			return pdbFilePath;
		} catch (Exception e) {
			LOG.error("WS ERROR: getPdbFilePath - " + StringUtils.getCurrentDate() + " - " + proteinAcronym + ", " + spaceGroup);
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
	@WebResult(name = "structures")
	public String getStructuresByDataCollectionId(
	@WebParam(name = "dataCollectionId")Integer dataCollectionId) throws Exception {
		try {
			Structure3Service structure3service = (Structure3Service) ejb3ServiceLocator.getLocalService(Structure3Service.class);
			List<Structure3VO> structures = structure3service.getStructuresByDataCollectionId(dataCollectionId);
			return convertStructuresToCSV(structures);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * returns the pdb filePath for all crystals linked to a protein associated to a given dataCollectionId
	 * 
	 * @param code
	 * @param number
	 * @return
	 * @throws Exception
	 */
	@WebMethod
	@WebResult(name = "structures")
	public String getProteinStructuresByDataCollectionId(
	@WebParam(name = "dataCollectionId")Integer dataCollectionId) throws Exception {
		try {
			Structure3Service structure3service = (Structure3Service) ejb3ServiceLocator.getLocalService(Structure3Service.class);
			List<Structure3VO> structures = structure3service.getProteinStructuresByDataCollectionId(dataCollectionId);
			LOG.debug("getProteinStructuresByDataCollectionId. dataCollectionId=" + dataCollectionId + " structures=" + structures.size());
			return convertStructuresToCSV(structures);
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
	

	/**
	 * Returns the list of protein acronyms for a given proposal
	 * 
	 * @param proposalCode
	 * @param proposalNumber
	 * @return
	 * @throws Exception
	 */
	@WebMethod
	@WebResult(name = "listProteinAcronyms")
	public List<String> findProteinAcronymsForProposal(@WebParam(name = "proposalCode")
	String proposalCode, @WebParam(name = "proposalNumber")
	String proposalNumber) throws Exception {
		try {
			LOG.debug("findProteinAcronymsForProposal");
			long startTime = System.currentTimeMillis();

			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Proposal3Service proposalService = (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);

			List<String> listProteinAcronyms = new ArrayList<String>();

			List<Proposal3VO> proposals = proposalService.findByCodeAndNumber(proposalCode, proposalNumber, false, true, false);
			if (proposals != null && proposals.size() > 0) {
				Proposal3VO proposal3VO = proposals.get(0);
				Set<Protein3VO> proteinVOs = proposal3VO.getProteinVOs();
				if (proteinVOs != null) {
					List<Protein3VO> listProtein = new ArrayList<Protein3VO>(Arrays.asList(proteinVOs
							.toArray(new Protein3VO[proteinVOs.size()])));
					for (Iterator<Protein3VO> p = listProtein.iterator(); p.hasNext();) {
						listProteinAcronyms.add(p.next().getAcronym());
					}
				}

			}
			long endTime = System.currentTimeMillis();
			long duration = endTime - startTime;
			LOG.debug("findProteinAcronymsForProposal : " + listProteinAcronyms.size() + " time = " + duration + " ms");
			return listProteinAcronyms;
		} catch (Exception e) {
			LOG.error("WS ERROR: findProteinAcronymsForProposal - " + StringUtils.getCurrentDate() + " - " + proposalCode + ", "
					+ proposalNumber);
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "blSubSampleId")
	public Integer storeOrUpdateBLSubSample(@WebParam(name = "blSubSample")
	BLSubSampleWS3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdateBLSubSample");
			long startTime = System.currentTimeMillis();
			if (vo == null)
				return null;

			if (vo.getDiffractionPlanId() == null || vo.getDiffractionPlanId() == 0) {
				vo.setDiffractionPlanId(null);
			}
			if (vo.getBlSampleId() == null || vo.getBlSampleId() == 0) {
				vo.setBlSampleId(null);
			}
			if (vo.getPositionId() == null || vo.getPositionId() == 0) {
				vo.setPositionId(null);
			}
//			if (vo.getMotorPositionId() == null || vo.getMotorPositionId() == 0) {
//				vo.setMotorPositionId(null);
//			}

			if (vo.getBlSampleId() == null) {
				LOG.debug("WS PB : BLSampleId is null , could not create blSubSample");
				return errorCodeFK;
			}

			BLSubSample3Service blSubSampleService = (BLSubSample3Service) ejb3ServiceLocator
					.getLocalService(BLSubSample3Service.class);
			BLSample3Service blsampleService = (BLSample3Service) ejb3ServiceLocator.getLocalService(BLSample3Service.class);
			DiffractionPlan3Service diffractionPlanService = (DiffractionPlan3Service) ejb3ServiceLocator
					.getLocalService(DiffractionPlan3Service.class);
			Position3Service positionService = (Position3Service) ejb3ServiceLocator.getLocalService(Position3Service.class);

			BLSubSample3VO blSubSampleVO = new BLSubSample3VO();

			BLSample3VO sampleVO = null;
			if (vo.getBlSampleId() != null && vo.getBlSampleId() > 0)
				sampleVO = blsampleService.findByPk(vo.getBlSampleId(), false, false, false);
			DiffractionPlan3VO diffractionPlanVO = null;
			if (vo.getDiffractionPlanId() != null && vo.getDiffractionPlanId() > 0)
				diffractionPlanVO = diffractionPlanService.findByPk(vo.getDiffractionPlanId(), false, false);
			Position3VO positionVO = null;
			if (vo.getPositionId() != null && vo.getPositionId() > 0)
				positionVO = positionService.findByPk(vo.getPositionId());
			Integer blSubSampleId = vo.getBlSubSampleId();
			// load the object elsewhere there is an error with the childs
			if (blSubSampleId != null && blSubSampleId > 0) {
				blSubSampleVO = blSubSampleService.findByPk(blSubSampleId);
			}
			blSubSampleVO.fillVOFromWS(vo);
			blSubSampleVO.setBlSampleVO(sampleVO);
			blSubSampleVO.setDiffractionPlanVO(diffractionPlanVO);
			blSubSampleVO.setPositionVO(positionVO);

			if (blSubSampleId == null || blSubSampleId == 0) {
				vo.setBlSampleId(null);
				blSubSampleVO = blSubSampleService.create(blSubSampleVO);
			} else {
				blSubSampleVO = blSubSampleService.update(blSubSampleVO);
			}
			blSubSampleId = blSubSampleVO.getBlSubSampleId();
			long duration = System.currentTimeMillis() - startTime;
			LOG.debug("storeOrUpdateBLSubSample : " + blSubSampleId + " time = " + duration + " ms");

			return blSubSampleVO.getBlSubSampleId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateBLSubSample - " + StringUtils.getCurrentDate() + " - " + vo.toWSString());
			throw e;
		}
	}
	
	@WebMethod
	@WebResult(name = "blSubSampleId")
	public Integer storeOrUpdateBLSampleImage(@WebParam(name = "blSampleImage")
	BLSampleImageWS3VO vo) throws Exception {
		try {
			LOG.debug("storeOrUpdateBLSampleImage");
			long startTime = System.currentTimeMillis();
			if (vo == null)
				return null;

			if (vo.getBlSampleId() == null || vo.getBlSampleId() == 0) {
				vo.setBlSampleId(null);
			}

			if (vo.getBlSampleId() == null) {
				LOG.debug("WS PB : BLSampleId is null , could not create blSampleImage");
				return errorCodeFK;
			}

			BLSampleImage3Service blSampleImageService = (BLSampleImage3Service) ejb3ServiceLocator
					.getLocalService(BLSampleImage3Service.class);
			BLSample3Service blsampleService = (BLSample3Service) ejb3ServiceLocator.getLocalService(BLSample3Service.class);

			BLSampleImage3VO sampleImage = new BLSampleImage3VO();

			BLSample3VO sampleVO = null;
			if (vo.getBlSampleId() != null && vo.getBlSampleId() > 0)
				sampleVO = blsampleService.findByPk(vo.getBlSampleId(), false, false, false);
			Integer blSampleImageId = vo.getBlSampleImageId();
			
			// load the object elsewhere there is an error with the childs
			if (blSampleImageId != null && blSampleImageId > 0) {
				sampleImage = blSampleImageService.findByPk(blSampleImageId);
			}
			sampleImage.fillVOFromWS(vo);
			sampleImage.setBlsampleVO(sampleVO);

			if (blSampleImageId == null || blSampleImageId == 0) {
				vo.setBlSampleId(null);
				sampleImage = blSampleImageService.create(sampleImage);
			} else {
				sampleImage = blSampleImageService.update(sampleImage);
			}
			blSampleImageId = sampleImage.getBlSampleImageId();
			long duration = System.currentTimeMillis() - startTime;
			LOG.debug("storeOrUpdateBLSampleImage : " + blSampleImageId + " time = " + duration + " ms");

			return sampleImage.getBlSampleImageId();
		} catch (Exception e) {
			LOG.error("WS ERROR: storeOrUpdateBLSampleImage - " + StringUtils.getCurrentDate() + " - " + vo.toWSString());
			throw e;
		}
	}


	@WebMethod
	@WebResult(name = "SampleInfo")
	public SampleInfo getSampleInformation(@WebParam(name = "sampleId")
	Integer sampleId) throws Exception {
		try {
			LOG.debug("getSampleInformation");
			long startTime = System.currentTimeMillis();
			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			BLSample3Service blSampleService = (BLSample3Service) ejb3ServiceLocator.getLocalService(BLSample3Service.class);
			SampleInfo sampleInfo = blSampleService.findForWSSampleInfo(sampleId);

			long duration = System.currentTimeMillis() - startTime;
			LOG.debug("getSampleInformation : " + sampleId + " time = " + duration + " ms");
			return sampleInfo;
		} catch (Exception e) {
			LOG.error("WS ERROR: getSampleInformation - " + StringUtils.getCurrentDate() + " - " + sampleId);
			throw e;
		}
	}

}