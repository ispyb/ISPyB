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
 ******************************************************************************************************************************/

package ispyb.client.biosaxs.dataAdapter;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import generated.ws.smis.SMISWebService;
import ispyb.client.biosaxs.pdf.IPDFReport;
import ispyb.client.biosaxs.pdf.PDFReportType;
import ispyb.client.biosaxs.pdf.SaxsReportFactory;
import ispyb.common.util.Constants;
import ispyb.common.util.PathUtils;
import ispyb.server.biosaxs.services.core.ExperimentScope;
import ispyb.server.biosaxs.services.core.analysis.Analysis3Service;
import ispyb.server.biosaxs.services.core.analysis.abInitioModelling.AbInitioModelling3Service;
import ispyb.server.biosaxs.services.core.analysis.advanced.AdvancedAnalysis3Service;
import ispyb.server.biosaxs.services.core.analysis.primaryDataProcessing.PrimaryDataProcessing3Service;
import ispyb.server.biosaxs.services.core.experiment.Experiment3Service;
import ispyb.server.biosaxs.services.core.measurementToDataCollection.MeasurementToDataCollection3Service;
import ispyb.server.biosaxs.services.core.plateType.PlateType3Service;
import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3Service;
import ispyb.server.biosaxs.services.core.robot.Robot3Service;
import ispyb.server.biosaxs.services.core.samplePlate.Sampleplate3Service;
import ispyb.server.biosaxs.services.core.specimen.Specimen3Service;
import ispyb.server.biosaxs.services.utils.BiosaxsZipper;
import ispyb.server.biosaxs.services.utils.reader.zip.SAXSZipper;
import ispyb.server.biosaxs.services.webUserInterface.WebUserInterfaceService;
import ispyb.server.biosaxs.vos.advanced.FitStructureToExperimentalData3VO;
import ispyb.server.biosaxs.vos.advanced.RigidBodyModeling3VO;
import ispyb.server.biosaxs.vos.advanced.Superposition3VO;
import ispyb.server.biosaxs.vos.assembly.Assembly3VO;
import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.biosaxs.vos.assembly.Structure3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Buffer3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Measurement3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Specimen3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.StockSolution3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.plate.Platetype3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.plate.Sampleplate3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.plate.Sampleplateposition3VO;
import ispyb.server.biosaxs.vos.datacollection.AbInitioModel3VO;
import ispyb.server.biosaxs.vos.datacollection.Frame3VO;
import ispyb.server.biosaxs.vos.datacollection.MeasurementTodataCollection3VO;
import ispyb.server.biosaxs.vos.datacollection.Merge3VO;
import ispyb.server.biosaxs.vos.datacollection.Model3VO;
import ispyb.server.biosaxs.vos.datacollection.ModelList3VO;
import ispyb.server.biosaxs.vos.datacollection.Subtraction3VO;
import ispyb.server.biosaxs.vos.datacollection.SubtractiontoAbInitioModel3VO;
import ispyb.server.biosaxs.vos.utils.comparator.SaxsDataCollectionComparator;
import ispyb.server.biosaxs.vos.utils.comparator.SpecimenComparator;
import ispyb.server.common.hdf5.HDF5FileReader;
import ispyb.server.common.services.proposals.LabContact3Service;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.services.shipping.Dewar3Service;
import ispyb.server.common.services.shipping.Shipping3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.LabContact3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.server.mx.vos.collections.InputParameterWorkflow;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.collections.Workflow3VO;
import ispyb.server.smis.UpdateFromSMIS;

public class BiosaxsActions {

	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Experiment3Service experiment3Service;

	private SaxsProposal3Service saxsproposal3Service;

	private Analysis3Service analysis3Service;

	private LabContact3Service labContactService;

	private Shipping3Service shippingService;

	private Proposal3Service proposal3Service;

	private Session3Service session3Service;

	private Robot3Service robot3Service;

	// private Priority3Service priority3Service;
	private WebUserInterfaceService biosaxsWeb3Service;

	private MeasurementToDataCollection3Service measurementToDataCollection3Service;

	private Dewar3Service dewarService;

	private Sampleplate3Service samplePlate3Service;

	private Specimen3Service specimen3Service;

	private AbInitioModelling3Service abInitioModelling3Service;

	private PrimaryDataProcessing3Service primaryDataProcessingService;

	private PlateType3Service plateType3Service;

	private AdvancedAnalysis3Service advancedAnalysis;

	public BiosaxsActions() {
		try {
			this.experiment3Service = (Experiment3Service) ejb3ServiceLocator.getLocalService(Experiment3Service.class);
			this.saxsproposal3Service = (SaxsProposal3Service) ejb3ServiceLocator.getLocalService(SaxsProposal3Service.class);
			this.analysis3Service = (Analysis3Service) ejb3ServiceLocator.getLocalService(Analysis3Service.class);
			this.abInitioModelling3Service = (AbInitioModelling3Service) ejb3ServiceLocator
					.getLocalService(AbInitioModelling3Service.class);
			this.primaryDataProcessingService = (PrimaryDataProcessing3Service) ejb3ServiceLocator
					.getLocalService(PrimaryDataProcessing3Service.class);
			this.robot3Service = (Robot3Service) ejb3ServiceLocator.getLocalService(Robot3Service.class);
			this.biosaxsWeb3Service = (WebUserInterfaceService) ejb3ServiceLocator.getLocalService(WebUserInterfaceService.class);
			this.measurementToDataCollection3Service = (MeasurementToDataCollection3Service) ejb3ServiceLocator
					.getLocalService(MeasurementToDataCollection3Service.class);
			this.samplePlate3Service = (Sampleplate3Service) ejb3ServiceLocator.getLocalService(Sampleplate3Service.class);
			this.specimen3Service = (Specimen3Service) ejb3ServiceLocator.getLocalService(Specimen3Service.class);
			this.plateType3Service = (PlateType3Service) ejb3ServiceLocator.getLocalService(PlateType3Service.class);
			this.advancedAnalysis = (AdvancedAnalysis3Service) ejb3ServiceLocator.getLocalService(AdvancedAnalysis3Service.class);
			/** Reusing MX services **/
			this.labContactService = (LabContact3Service) ejb3ServiceLocator.getLocalService(LabContact3Service.class);
			this.shippingService = (Shipping3Service) ejb3ServiceLocator.getLocalService(Shipping3Service.class);
			this.proposal3Service = (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);
			this.session3Service = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
			this.dewarService = (Dewar3Service) ejb3ServiceLocator.getLocalService(Dewar3Service.class);

		} catch (NamingException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param experimentScope
	 * @param proposalId
	 * @param parseInt
	 * @return
	 */
	public Experiment3VO getExperimentById(Integer experimentId, ExperimentScope experimentScope, Integer proposalId) {
		return this.experiment3Service.findById(experimentId, experimentScope, proposalId);
	}

	/**
	 * @param name
	 * @param comments
	 * @return
	 */
	public Experiment3VO createExperiment(Integer proposalId, Integer sessionId, String name, String comments, Date createTime) {
		Experiment3VO experiment = new Experiment3VO();
		experiment = experiment3Service.initPlates(experiment);
		experiment.setCreationDate(createTime);
		experiment.setComments(comments);
		experiment.setName(name);
		experiment.setProposalId(proposalId);
		experiment.setSessionId(sessionId);
		experiment = experiment3Service.merge(experiment);
		Integer experimentId = experiment.getExperimentId();
		// return
		// this.experiment3Service.findAllById(Integer.valueOf(experimentId));
		return experiment3Service.findById(Integer.valueOf(experimentId), ExperimentScope.MEDIUM);
	}

	/**
	 * @param proposalId
	 * @param minimal
	 * @return
	 */
	public List<Experiment3VO> findExperimentsByProposalId(Integer proposalId, ExperimentScope scope) {
		return this.experiment3Service.findByProposalId(proposalId, scope);
	}

	/**
	 * @param proposalId
	 * @return list of Macromolecules belonging to the proposal
	 */
	public List<Macromolecule3VO> findMacromoleculesByProposalId(Integer proposalId) {
		return this.saxsproposal3Service.findMacromoleculesByProposalId(proposalId);
	}

	/**
	 * @param proposalId
	 * @return list of buffers belonging to the proposal
	 */
	public List<Buffer3VO> findBuffersByProposalId(Integer proposalId) {
		return this.saxsproposal3Service.findBuffersByProposalId(proposalId);
	}

	/**
	 * @param proposalId
	 * @return list of assemblies belonging to the proposal
	 */
	public List<Assembly3VO> findAssembliesByProposalId(Integer proposalId) {
		return this.saxsproposal3Service.findAssembliesByProposalId(proposalId);
	}

	/**
	 * @param frameIdList
	 * @return
	 */
	public List<Frame3VO> getFramesByIds(List<Integer> frameIdList) {
		List<Frame3VO> frames = new ArrayList<Frame3VO>();
		if (frameIdList.size() > 0) {
			frames = this.primaryDataProcessingService.getFramesByIdList(frameIdList);
		}
		return frames;

	}

	/**
	 * @param mergeIdList
	 * @return
	 */
	public List<Merge3VO> getMergeByIds(List<Integer> mergeIdList) {
		List<Merge3VO> merges = new ArrayList<Merge3VO>();
		if (mergeIdList.size() > 0) {
			merges = this.primaryDataProcessingService.getMergesByIdsList(mergeIdList);
		}
		return merges;
	}

	/**
	 * @param proposalId
	 * @return
	 */
	public List<StockSolution3VO> findStockSolutionsByProposalId(Integer proposalId) {
		return this.saxsproposal3Service.findStockSolutionsByProposalId(proposalId);
	}

	public List<Platetype3VO> findPlateTypes() {
		return this.plateType3Service.findAll();
	}

	/**
	 * @param buffer3vo
	 * @return
	 */
	public Buffer3VO saveBuffer(Buffer3VO buffer3vo) {
		return this.saxsproposal3Service.merge(buffer3vo);

	}

	/**
	 * @param macromolecule3vo
	 * @return
	 */
	public Macromolecule3VO saveMacromolecule(Macromolecule3VO macromolecule3vo) {
		return this.saxsproposal3Service.merge(macromolecule3vo);

	}

	/**
	 * @param assemblyId
	 * @param macromolecules
	 */
	public void createAssembly(Integer assemblyId, List<Integer> macromolecules) {
		this.saxsproposal3Service.createAssembly(assemblyId, macromolecules);

	}

	/**
	 * @param proposalId
	 * @return
	 * @throws Exception
	 */
	public List<LabContact3VO> findLabContactsByProposal(Integer proposalId) throws Exception {
		return this.labContactService.findFiltered(proposalId, null);
	}

	/**
	 * @param shippingId
	 * @param name
	 * @param comments
	 * @param sendingLabContactId
	 * @param returnLabContactId
	 * @param returnCourier
	 * @param courierAccount
	 * @param billingReference
	 * @param dewarAvgCustomsValue
	 * @param dewarAvgTransportValue
	 * @return
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	public Shipping3VO saveShipment(String shippingId, String name, String comments, String sendingLabContactId,
			String returnLabContactId, String returnCourier, String courierAccount, String billingReference,
			String dewarAvgCustomsValue, String dewarAvgTransportValue, Integer proposalId) throws NumberFormatException, Exception {

		Shipping3VO shipping3VO = new Shipping3VO();

		if ((!shippingId.equals("null"))) {
			shipping3VO = this.shippingService.findByPk(Integer.parseInt(shippingId), true);
		}

		shipping3VO.setShippingName(name);
		shipping3VO.setShippingStatus(Constants.SHIPPING_STATUS_OPENED);
		shipping3VO.setComments(comments);
		shipping3VO.setProposalVO(proposal3Service.findByPk(proposalId));
		shipping3VO.setCreationDate(new Date());
		shipping3VO.setTimeStamp(new Date());
		shipping3VO.setShippingType(Constants.DEWAR_TRACKING_SHIPPING_TYPE);
		// shipping3VO.setReturnCourier(returnCourier);

		/** Lab contacts **/
		LabContact3VO sendingLabContact = this.labContactService.findByPk(Integer.parseInt(sendingLabContactId));
		shipping3VO.setSendingLabContactVO(sendingLabContact);

		LabContact3VO returnLabContact = this.labContactService.findByPk(Integer.parseInt(returnLabContactId));
		returnLabContact.setBillingReference(billingReference);
		returnLabContact.setCourierAccount(courierAccount);
		returnLabContact.setDewarAvgCustomsValue(Integer.parseInt(dewarAvgCustomsValue));
		returnLabContact.setDewarAvgTransportValue(Integer.parseInt(dewarAvgTransportValue));

		returnLabContact.setDefaultCourrierCompany(returnCourier);
		this.labContactService.update(returnLabContact);

		shipping3VO.setReturnLabContactVO(returnLabContact);

		if (!shippingId.equals("null")) {
			this.shippingService.update(shipping3VO);
		} else {
			shipping3VO = this.shippingService.create(shipping3VO);
		}
		return this.shippingService.findByPk(shipping3VO.getShippingId(), true);
	}

	/**
	 * @param proposalId
	 * @return
	 * @throws Exception
	 */
	public List<Shipping3VO> findShipmentsByProposal(Integer proposalId) throws Exception {
		//return this.shippingService.findByProposal(proposalId, true);
		return this.shippingService.findByProposal(proposalId, true, true, true);
	}

	/**
	 * @param stockSolution3VO
	 * @return
	 */
	public StockSolution3VO saveStockSolution(StockSolution3VO stockSolution3VO) {
		return this.saxsproposal3Service.merge(stockSolution3VO);
	}

	public Subtraction3VO getSubtractionById(Integer substractionId) {
		return this.primaryDataProcessingService.getSubstractionById(substractionId);
	}

	public List<Subtraction3VO> getSubtractionByIds(List<Integer> subtractionIds) {
		List<Subtraction3VO> result = new ArrayList<Subtraction3VO>();
		for (Integer subtractionId : subtractionIds) {
			result.add(this.getSubtractionById(subtractionId));
		}
		return result;
	}

	/**
	 * @param type
	 * @param parseInt
	 * @return
	 */
	public String getSubstractedFileById(String type, Integer substractionId) {
		Subtraction3VO substraction = this.getSubtractionById(substractionId);
		if (type.equals("guinier")) {
			return substraction.getGuinierFilePath();
		}
		if (type.equals("gnom")) {
			return substraction.getGnomFilePath();
		}
		if (type.equals("kratky")) {
			return substraction.getKratkyFilePath();
		}
		if (type.equals("scattering")) {
			return substraction.getScatteringFilePath();
		}
		return null;
	}

	// /**
	// * @param macromoleculeIds
	// */
	// public List<Experiment3VO> getExperimentByMacromolecules(List<Integer>
	// macromoleculeIds) {
	// return
	// this.experiment3Service.getExperimentByMacromolecules(macromoleculeIds,
	// ExperimentScope.MINIMAL);
	// }
	/**
	 * @param filePath
	 * @return
	 */
	public static String checkFilePathForDevelopment(String filePath) {
		return PathUtils.getPath(filePath);
	}

	// public String getCSVReport(Integer proposalId, Integer experimentId, CSVReportType type) throws Exception {
	// Experiment3VO experiment = this.experiment3Service.findById(experimentId, ExperimentScope.REPORT, proposalId);
	// List<Buffer3VO> buffers = saxsproposal3Service.findBuffersByProposalId(experiment.getProposalId());
	// Proposal3VO proposal = proposal3Service.findByPk(proposalId);
	//
	// /** PDF **/
	// SaxsReportFactory factory = new SaxsReportFactory();
	// ICSVReport report = factory.getCSVReport(type);
	// report.setUp(experiment, buffers, proposal);
	// return report.run();
	// }

	public ByteArrayOutputStream getPDFReport(Integer proposalId, Integer experimentId, PDFReportType type) throws Exception {
		Experiment3VO experiment = this.experiment3Service.findById(experimentId, ExperimentScope.MEDIUM, proposalId);
		List<Buffer3VO> buffers = saxsproposal3Service.findBuffersByProposalId(experiment.getProposalId());
		Proposal3VO proposal = proposal3Service.findByPk(proposalId);

		/** PDF **/
		SaxsReportFactory factory = new SaxsReportFactory();
		IPDFReport report = factory.getPDFReport(type);
		report.setUp(experiment, buffers, proposal);
		return report.run();

	}

	/**
	 * @param macromoleculeId
	 * @return
	 */
	public List<Map<String, Object>> getAllInformationByMacromoleculeId(Integer macromoleculeId, Integer proposalId) {
		return this.analysis3Service.getAllByMacromolecule(macromoleculeId, proposalId);
	}

	/**
	 * @param macromoleculeId
	 * @param bufferId
	 * @return
	 */
	public List<Map<String, Object>> getAllInformationByMacromoleculeId(Integer macromoleculeId, Integer bufferId, Integer proposalId) {
		return this.analysis3Service.getAllByMacromolecule(macromoleculeId, bufferId, proposalId);
	}

	/**
	 * @param proposalId
	 * @return
	 */
	public List<Map<String, Object>> getAllInformationByProposalId(Integer proposalId) {
		return this.abInitioModelling3Service.getAllByProposalId(proposalId);
	}

	/**
	 * @param macromoleculeId
	 * @return
	 */
	public List<Map<String, Object>> getAnalysisInformationByExperimentId(Integer experimentId) {
		return this.abInitioModelling3Service.getAnalysisInformationByExperimentId(experimentId);
	}

	/**
	 * @param proposalId
	 * @return
	 */
	public List<Map<String, Object>> getExperimentInformationByProposalId(Integer proposalId) {
		return this.analysis3Service.getExperimentListByProposalId(proposalId);
	}

	// /**
	// * @param experimentId
	// * @return
	// */
	// public List<Map<String, Object>> getAllInformationByExperimentId(Integer
	// experimentId) {
	// return
	// this.analysis3Service.getAllInformationByExperimentId(experimentId);
	// }
	/**
	 * @param sessionId
	 * @return
	 */
	public Proposal3VO getProposalBySessionId(Integer sessionId) {
		try {
			return this.session3Service.findByPk(sessionId, false, false, false).getProposalVO();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param sessionId
	 * @return
	 */
	public List<Session3VO> getSessionByProposalId(Integer proposalId) {
		try {
			return this.session3Service.findFiltered(proposalId, null, null, null, null, null, false, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param specimen3vo
	 * @return
	 */
	public Specimen3VO merge(Specimen3VO specimen3vo) {
		return this.biosaxsWeb3Service.merge(specimen3vo);
		// Sampleplateposition3VO samplePlatePosition = null;
		// if (specimen3vo.getSampleplateposition3VO() != null) {
		// samplePlatePosition = this.merge(specimen3vo.getSampleplateposition3VO());
		// specimen3vo.setSampleplateposition3VO(samplePlatePosition);
		// }
		// specimen3vo = this.specimen3Service.merge(specimen3vo);
		// specimen3vo.setSampleplateposition3VO(samplePlatePosition);
		// return specimen3vo;
	}

	/**
	 * @param sampleplateposition3vo
	 * @return
	 */
	public Sampleplateposition3VO merge(Sampleplateposition3VO sampleplateposition3vo) {
		return this.samplePlate3Service.merge(sampleplateposition3vo);
	}

	/**
	 * @param experimentId
	 * @param proposalId
	 * @param samples
	 * @return
	 */
	public Experiment3VO addMeasurementsToExperiment(Integer experimentId, Integer proposalId,
			ArrayList<HashMap<String, String>> samples) {
		try {
			this.robot3Service.addMeasurementsToExperiment(experimentId, proposalId, samples);
			return this.biosaxsWeb3Service.setPriorities(experimentId, proposalId, SaxsDataCollectionComparator.defaultComparator);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Experiment3VO createTemplate(Integer proposalId, String name, ArrayList<HashMap<String, String>> samples) {
		try {
			Boolean optimize = false;
			String comments = "";
			// TODO replace by correct sessionId
			Integer sessionId = null;
			Experiment3VO experiment = this.robot3Service.createExperimentFromRobotParams(samples, sessionId, proposalId,
					"BeforeAndAfter", "0", "0", "TEMPLATE", null, name, optimize, comments);
			return this.biosaxsWeb3Service.setPriorities(experiment.getExperimentId(), proposalId,
					SaxsDataCollectionComparator.defaultComparator);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * These measurement only can be a sample!! ------------------------------------------------------------------------- If conditions
	 * have changed we also change the conditions of the buffers of the same data collection. These conditions are: - Exp. Temp - Vol.
	 * Load. - Transmission - Flow
	 * 
	 * @throws Exception
	 */
	public Measurement3VO merge(Measurement3VO measurement3vo) throws Exception {
		return this.biosaxsWeb3Service.merge(measurement3vo);
	}

	/**
	 * @param parseInt
	 * @throws Exception
	 */
	public void removeExperiment(Integer experimentId) throws Exception {
		this.biosaxsWeb3Service.removeMeasurementsByExperimentId(experimentId);
		this.biosaxsWeb3Service.removeSpecimensByExperimentId(experimentId);
		this.biosaxsWeb3Service.removePlates(experimentId);
		this.experiment3Service.remove(this.experiment3Service.findById(experimentId, ExperimentScope.MINIMAL));
	}

	/**
	 * @param measurement3vo
	 * @return
	 * @throws Exception
	 */
	public void remove(Measurement3VO measurement3vo) throws Exception {
		List<MeasurementTodataCollection3VO> measurementsDC = this.measurementToDataCollection3Service
				.findByMeasurementId(measurement3vo.getMeasurementId());

		/** Getting specimens involved in this datacollection **/
		List<Specimen3VO> specimens = new ArrayList<Specimen3VO>();
		for (MeasurementTodataCollection3VO measurementTodataCollection3VO : measurementsDC) {
			specimens = this.biosaxsWeb3Service.getSpecimenByDataCollectionId(measurementTodataCollection3VO.getDataCollectionId());
		}
		/** Removing data collection, MeasurementtoDc and Measurement **/
		this.biosaxsWeb3Service.removeDataCollectionByMeasurement(measurement3vo);

		/** Removing specimen if possible measurementCount == 0 **/
		for (Specimen3VO specimen3vo : specimens) {
			this.biosaxsWeb3Service.remove(specimen3vo);
		}

	}

	/**
	 * @param experimentId
	 * @return
	 */
	public List<AbInitioModel3VO> getAbinitioModelsByExperimentId(Integer experimentId) {
		return this.abInitioModelling3Service.getAbinitioModelsByExperimentId(experimentId);
	}

	/**
	 * @param type
	 * @param parseInt
	 * @return
	 */
	public String getModelListFileById(String type, Integer modelListId) {
		ModelList3VO modelList = this.abInitioModelling3Service.getModelListById(modelListId);

		if (type.equals("NSD")) {
			return modelList.getNsdFilePath();
		}
		if (type.equals("CHI2")) {
			return modelList.getChi2rgFilePath();
		}
		return null;
	}

	/**
	 * @param type
	 * @param parseInt
	 * @return
	 */
	public String getModelFileById(String type, Integer modelId) {
		Model3VO model = this.abInitioModelling3Service.getModelById(modelId);

		if (type.equals("FIT")) {
			return model.getFitFile();
		}
		if (type.equals("FIR")) {
			return model.getFirFile();
		}
		if (type.equals("PDB")) {
			return model.getPdbFile();
		}
		return null;
	}

	/**
	 * @param type
	 * @param parseInt
	 * @return
	 */
	public String getAbnitioPDBFile(String type, Integer abinitioModelId) {
		AbInitioModel3VO abInitioModel3VO = this.abInitioModelling3Service.getAbinitioModelsById(abinitioModelId);
		if (type.equals("AVERAGED")) {
			return abInitioModel3VO.getAveragedModel().getPdbFile();
		}
		if (type.equals("RAPIDSHAPEDETERMINATIONMODEL")) {
			return abInitioModel3VO.getRapidShapeDeterminationModel().getPdbFile();
		}
		if (type.equals("SHAPEDETERMINATIONMODEL")) {
			return abInitioModel3VO.getShapeDeterminationModel().getPdbFile();
		}
		return null;
	}

	/**
	 * @param parseInt
	 * @return
	 */
	public String getTemplateRobotFile(Integer experimentId, Integer proposalId, final SaxsDataCollectionComparator... multipleOptions) {
		return this.experiment3Service.toRobotXML(experimentId, proposalId, multipleOptions);
	}

	public String getTemplateRobotFile(Integer experimentId, Integer proposalId) {
		return this.experiment3Service.toRobotXML(experimentId, proposalId);
	}

	/**
	 * @param proposalId
	 * @throws Exception
	 */
	public void updateSMISDataBase(Integer proposalId) throws Exception {

		UpdateFromSMIS.updateProposalFromSMIS(proposalId);

	}

	/**
	 * @param proposalId
	 * @return
	 * @throws Exception
	 */
	public Proposal3VO getProposalById(Integer proposalId) throws Exception {
		return this.proposal3Service.findByPk(proposalId);

	}

	/**
	 * @param measurementIds
	 * @param parameter
	 * @param value
	 */
	public void setMeasurementParameters(List<Integer> measurementIds, String parameter, String value) {
		/**
		 * parameter: ["Exp. Temp.", "Volume To Load", "Transmission", "Wait Time", "Viscosity", "macromoleculeId", "bufferId"]
		 **/
		this.biosaxsWeb3Service.setMeasurementParameters(measurementIds, parameter, value);

	}

	/**
	 * @param targetId
	 * @param specimenId
	 * @return
	 */
	public Experiment3VO mergeSpecimens(Integer targetId, Integer specimenId) {
		Specimen3VO specimen = this.biosaxsWeb3Service.mergeSpecimens(targetId, specimenId);
		return this.experiment3Service.findById(specimen.getExperimentId(), ExperimentScope.MEDIUM);
	}

	/**
	 * @param proposalBySessionId
	 * @return
	 */
	public List<Map<String, Object>> getAnalysisCalibrationByProposalId(Integer proposalId) {
		return this.abInitioModelling3Service.getAnalysisCalibrationByProposalId(proposalId);
	}

	
//	public String getAlignedPDBContentBySuperpositionList(ArrayList<HashMap<String, String>> propertiesList) throws IOException {
//		int count = 0;
//		StringBuilder content = new StringBuilder();
//
//		for (int i = 0; i < propertiesList.size(); i++) {
//			Superposition3VO superposition = this.advancedAnalysis.getSuperpositionById(Integer.parseInt(propertiesList.get(i).get("superpositionId")));
//			if (superposition != null) {
//				String filePath = superposition.getAlignedPdbFilePath();
//				return PDBParser.getPDBContent(filePath, propertiesList.get(i));
////				content.append(result.get("content"));
////				count = count + Integer.parseInt(result.get("count"));
//			}
//		}
////		return count + "\n" + "ISPyB Generated File\n" + content.toString();
//		return null;
//	}
//	public String getAbinitioPDBContentBySuperpositionList(ArrayList<HashMap<String, String>> propertiesList) throws IOException {
//		int count = 0;
//		StringBuilder content = new StringBuilder();
//
//		for (int i = 0; i < propertiesList.size(); i++) {
//			Superposition3VO superposition = this.advancedAnalysis.getSuperpositionById(Integer.parseInt(propertiesList.get(i).get("superpositionId")));
//			if (superposition != null) {
//				String filePath = superposition.getAbinitioModelPdbFilePath();
//				return PDBParser.getPDBContent(filePath, propertiesList.get(i));
////				content.append(result.get("content"));
////				count = count + Integer.parseInt(result.get("count"));
//			}
//		}
////		return count + "\n" + "ISPyB Generated File\n" + content.toString();
//		return null;
//	}
//	/**
//	 * @param propertiesList
//	 * @return
//	 * @throws Exception 
//	 */
//	public String getPDBContentByModelList(ArrayList<HashMap<String, String>> propertiesList) throws Exception {
//		int count = 0;
//		StringBuilder content = new StringBuilder();
//
//		for (int i = 0; i < propertiesList.size(); i++) {
//			Model3VO model = this.abInitioModelling3Service.getModelById(Integer.parseInt(propertiesList.get(i).get("modelId")));
//			if (model != null) {
//				String filePath = model.getPdbFile();
//				return PDBParser.getPDBContent(filePath, propertiesList.get(i));
////				content.append(result.get("content"));
////				count = count + Integer.parseInt(result.get("count"));
//			}
//		}
////		return count + "\n" + "ISPyB Generated File\n" + content.toString();
//		return null;
//		
//	}

	/**
	 * @param shippingId
	 * @return Shipping3VO
	 * @throws Exception
	 */
	public Shipping3VO getShipmentById(Integer shippingId) throws Exception {
		return this.shippingService.findByPk(shippingId, true, true);
	}

	/**
	 * @param shipping3vo
	 * @return
	 * @throws Exception
	 */
	public Dewar3VO createDewar(Integer shipmentId) throws Exception {
		Shipping3VO shipping = this.getShipmentById(shipmentId);
		Dewar3VO dewar3VO = new Dewar3VO();
		dewar3VO.setShippingVO(shipping);
		dewar3VO.setType("Dewar");
		dewar3VO.setDewarStatus(Constants.SHIPPING_STATUS_OPENED);
		Dewar3VO dewar = this.dewarService.create(dewar3VO);

		return dewar;
	}

	/**
	 * @param dewarId
	 * @param dewarMap
	 * @throws Exception
	 */
	public void saveCase(String dewarId, Map<String, String> dewarMap) throws Exception {
		Dewar3VO dewar3VO = this.dewarService.findByPk(Integer.parseInt(dewarId), false, false);
		dewar3VO.setCode(dewarMap.get("code"));

		if (dewarMap.get("transportValue") != null) {
			dewar3VO.setTransportValue(Integer.parseInt(dewarMap.get("transportValue")));
		}
		dewar3VO.setStorageLocation(dewarMap.get("storageLocation"));
		dewar3VO.setDewarStatus(dewarMap.get("dewarStatus"));

		if (dewarMap.containsKey("firstExperimentId")) {
			System.out.println(dewarMap.get("firstExperimentId"));
			if (dewarMap.get("firstExperimentId") != null) {
				dewar3VO.setSessionVO(this.session3Service.findByPk(Integer.parseInt(dewarMap.get("firstExperimentId")), false, false,
						false));
			}
		}

		dewar3VO.setComments(dewarMap.get("comments"));
		dewar3VO.setTrackingNumberToSynchrotron(dewarMap.get("trackingNumberToSynchrotron"));
		dewar3VO.setTrackingNumberFromSynchrotron(dewarMap.get("trackingNumberFromSynchrotron"));

		this.dewarService.update(dewar3VO);

	}

	/**
	 * @param proposalId
	 * @param dewarId
	 * @param shippingId
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	public void removeCase(Integer proposalId, String dewarId, String shippingId) throws NumberFormatException, Exception {
		List<Sampleplate3VO> sampleplate3VOs = this.samplePlate3Service.getSamplePlatesByBoxId(dewarId);
		for (Sampleplate3VO plate : sampleplate3VOs) {
			if (plate.getBoxId() != null) {
				if (plate.getBoxId() == Integer.parseInt(dewarId)) {
					plate.setBoxId(null);
					this.samplePlate3Service.merge(plate);
				}
			}
		}
		List<StockSolution3VO> stockSolution3VOs = this.saxsproposal3Service.findStockSolutionsByBoxId(dewarId);
		for (StockSolution3VO stockSolution3VO : stockSolution3VOs) {
			if (stockSolution3VO.getBoxId() != null) {
				if (stockSolution3VO.getBoxId() == Integer.parseInt(dewarId)) {
					stockSolution3VO.setBoxId(null);
					this.saxsproposal3Service.merge(stockSolution3VO);
				}
			}
		}
		this.dewarService.deleteByPk(Integer.parseInt(dewarId));
	}

	/**
	 * @param samplePlate3VO
	 */
	public void merge(Sampleplate3VO samplePlate3VO) {
		this.samplePlate3Service.merge(samplePlate3VO);

	}

	/**
	 * @param samplePlate3VOs
	 */
	public void merge(ArrayList<Sampleplate3VO> samplePlate3VOs) {
		for (Sampleplate3VO sampleplate3vo : samplePlate3VOs) {
			this.merge(sampleplate3vo);
		}

	}

	/**
	 * @param proposalId
	 * @return
	 */
	// public List<Sampleplate3VO> findSamplePlatesByProposalId(Integer
	// proposalId) {
	// return this.samplePlate3Service.findSamplePlatesByProposalId(proposalId);
	// }
	// /**
	// * @param proposalId
	// */
	// public void serializeSamplePlates(Integer proposalId) {
	// List<Sampleplate3VO> sampleplate3VOs =
	// this.samplePlate3Service.findSamplePlatesByProposalId(proposalId);
	// Gson gson = new GsonBuilder().serializeNulls().setExclusionStrategies(new
	// ProposalExclusionStrategy()).excludeFieldsWithModifiers(Modifier.PRIVATE).create();
	// for (Sampleplate3VO sampleplate3vo : sampleplate3VOs) {
	// System.out.println(sampleplate3vo);
	// System.out.println(gson.toJson(sampleplate3vo));
	// }
	//
	// }

	/**
	 * @param parseInt
	 * @return
	 */
	public StockSolution3VO getStockSolutionById(Integer stockSolutionId) {
		return this.saxsproposal3Service.findStockSolutionById(stockSolutionId);

	}

	/**
	 * @param parseInt
	 */
	public void remove(StockSolution3VO stockSolution3VO) {
		this.saxsproposal3Service.remove(stockSolution3VO);

	}

	/**
	 * @param parseInt
	 * @throws Exception
	 */
	public void removeShippingById(Integer shippingId) throws Exception {
		this.shippingService.deleteByPk(shippingId);
	}

	/**
	 * @param macromoleculeId
	 * @param proposalId
	 * @return
	 */
	public byte[] getZipFileByMacromoleculeId(Integer macromoleculeId, Integer proposalId) {
		BiosaxsZipper zipper = new BiosaxsZipper(analysis3Service, abInitioModelling3Service, primaryDataProcessingService);
		return zipper.getFilesByMacromolecule(macromoleculeId, proposalId);

	}

	/**
	 * @param experimentId
	 * @param proposalId
	 * @return
	 */
	public byte[] getZipFileByExperimentId(Integer experimentId, Integer proposalId) {
		BiosaxsZipper zipper = new BiosaxsZipper(analysis3Service, abInitioModelling3Service, primaryDataProcessingService);
		return zipper.getFilesByExperimentId(experimentId);
	}

	public byte[] getZipFileByAverageList(List<Merge3VO> merges, List<Subtraction3VO> subtractions) {
		List<SubtractiontoAbInitioModel3VO> abinitio = new ArrayList<SubtractiontoAbInitioModel3VO>();
		for (Subtraction3VO subtraction3vo : subtractions) {
			List<Subtraction3VO> subtractionsWithAbinitios = this.abInitioModelling3Service.getAbinitioModelsBySubtractionId(subtraction3vo.getSubtractionId());
			for (Subtraction3VO subtraction3vo2 : subtractionsWithAbinitios) {
				abinitio.addAll(subtraction3vo2.getSubstractionToAbInitioModel3VOs());
			}
		}
		
		return SAXSZipper.zip(merges, subtractions, abinitio);
	}

	/**
	 * @param experimentId
	 * @param name
	 * @param type
	 * @param comments
	 * @param proposalBySessionId
	 * @throws Exception
	 */
	public void saveExperiment(Integer experimentId, String name, String type, String comments, Integer proposalId) throws Exception {
		Experiment3VO experiment = this.experiment3Service.findById(experimentId, ExperimentScope.MINIMAL, proposalId);
		if (experiment != null) {
			experiment.setComments(comments);
			experiment.setName(name);
			experiment.setType(type);
			this.experiment3Service.merge(experiment);
		} else {
			throw new Exception("Experiment: " + experimentId + " proposalId: " + proposalId + " not found");
		}

	}

	public void emptyPlate(Integer samplePlateId, Integer experimentId) {
		this.biosaxsWeb3Service.emptyPlate(samplePlateId, experimentId);
	}

	public void autoFillPlate(Integer samplePlateId, Integer experimentId) {
		this.biosaxsWeb3Service.autoFillPlate(samplePlateId, experimentId, SpecimenComparator.MACROMOLECULE,
				SpecimenComparator.BUFFER, SpecimenComparator.CONCENTRATION);

	}

	/**
	 * @param specimensArrayIds
	 * @param proposalBySessionId
	 * @return
	 */
	public List<Map<String, Object>> getAllInformationByMacromoleculeId(ArrayList<HashMap<String, String>> specimensArrayIds,
			Integer proposalBySessionId) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

		for (HashMap<String, String> map : specimensArrayIds) {
			Integer macromoleculeId = Integer.parseInt(map.get("macromoleculeId"));
			if (map.containsKey("bufferId")) {
				Integer bufferId = Integer.parseInt(map.get("bufferId"));
				data.addAll(this.getAllInformationByMacromoleculeId(macromoleculeId, bufferId, proposalBySessionId));
			} else {
				data.addAll(this.getAllInformationByMacromoleculeId(macromoleculeId, proposalBySessionId));
			}
		}
		return data;
	}

	/**
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public String getContentFileByPath(String filePath) throws Exception {
		filePath = PathUtils.getPath(filePath);
		if (new File(filePath).exists()) {
			String line;
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			StringBuilder content = new StringBuilder();
			while ((line = br.readLine()) != null) {
				content.append(line + "\n");
			}
			br.close();
			return content.toString();
		} else {
			throw new Exception("Not file found " + filePath);
		}
	}

	/**
	 * @param parseInt
	 * @param frames
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, ArrayList<String>> getH5FrameScattering(Integer experimentId, Integer frame, Integer proposalId)
			throws Exception {
		String filePath = this.getH5FilePathByExperimentId(experimentId, proposalId);
		HDF5FileReader reader = new HDF5FileReader(filePath);
		boolean includeSubtraction = true;
		boolean includeBufferAverage = true;
		return reader.getH5FrameScattering(frame, includeSubtraction, includeBufferAverage);
	}

	public HashMap<String, float[]> getH5ParametersByExperimentId(Integer experimentId, List<String> parameters, Integer proposalId)
			throws Exception {
		System.out.println("getH5ParametersByExperimentId");
		String filePath = this.getH5FilePathByExperimentId(experimentId, proposalId);
		System.out.println("getH5ParametersByExperimentId filePath = '" + filePath + "' \n parameters = '" + parameters + "'");
		HDF5FileReader reader = new HDF5FileReader(filePath);
		return reader.getH5ParametersByExperimentId(parameters);
	}

	public String getH5FilePathByExperimentId(Integer experimentId, Integer proposalId) {
		Experiment3VO experiment = this.experiment3Service.findById(experimentId, ExperimentScope.MINIMAL, proposalId);
		if (experiment != null) {
			String filePath = experiment.getDataAcquisitionFilePath();
			return BiosaxsActions.checkFilePathForDevelopment(filePath);
		}
		return null;
	}

	/**
	 * @param parseInt
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, List<List<String>>> getSampleAverages(Integer experimentId, Integer proposalId) throws Exception {
		String filePath = this.getH5FilePathByExperimentId(experimentId, proposalId);
		HDF5FileReader reader = new HDF5FileReader(filePath);
		return reader.getSampleAverages();
	}

	/**
	 * @param experimentId
	 * @param proposalId
	 * @param startFrame
	 * @param endFrame
	 * @return
	 * @throws Exception
	 */
	public byte[] getZipFileH5ByFramesRange(Integer experimentId, Integer proposalId, Integer startFrame, Integer endFrame)
			throws Exception {
		String filePath = this.getH5FilePathByExperimentId(experimentId, proposalId);
		HDF5FileReader reader = new HDF5FileReader(filePath);
		return reader.getH5ZipFileByteArrayByFrameRange(startFrame, endFrame);
	}

	public Experiment3VO setPriorities(int experimentId, Integer proposalId, SaxsDataCollectionComparator[] options) {
		return this.experiment3Service.setPriorities(experimentId, proposalId, options);
	}

	public void saveStructure(Integer macromoleculeId, String fileName, String filePath, String type) {
		this.saveStructure(macromoleculeId, fileName, filePath, type, "P1", "1");
	}

	public void saveStructure(Integer macromoleculeId, String fileName, String filePath, String type, String symmetry,
			String multiplicity) {
		this.experiment3Service.saveStructure(macromoleculeId, fileName, filePath, type, symmetry, multiplicity);
	}

	public void saveStructre(Structure3VO structure3vo) {
		this.experiment3Service.saveStructure(structure3vo);

	}

	public void removeStructure(int structureId) {
		this.experiment3Service.removeStructure(structureId);

	}

	public Structure3VO getStructureById(int structureId) {
		return this.experiment3Service.findStructureById(structureId);
	}

	public void removeStoichiometry(int stoichiometryId) {
		this.experiment3Service.removeStoichiometry(stoichiometryId);

	}

	public void saveStoichiometry(int macromoleculeId, int hostmacromoleculeId, String ratio, String comments) {
		this.experiment3Service.saveStoichiometry(macromoleculeId, hostmacromoleculeId, ratio, comments);

	}

	public List<Map<String, Object>> getCompactAnalysisInformationByExperimentId(Integer experimentId) {
		return this.analysis3Service.getCompactAnalysisByExperimentId(experimentId);
	}
	
	public String getCountCompactAnalysisByProposalId(Integer proposalId) {
		return this.analysis3Service.getCountCompactAnalysisByExperimentId(proposalId).toString();
	}

	public List<Map<String, Object>> getCompactAnalysisBySubtractionId(String subtractionId) {
		return this.analysis3Service.getCompactAnalysisBySubtractionId(subtractionId);
	}

	public List<Map<String, Object>> getCompactAnalysisByProposalId(Integer proposalId, Integer limit) {
		return this.analysis3Service.getCompactAnalysisByProposalId(proposalId, limit);
	}
	
	public List<Map<String, Object>> getCompactAnalysisByProposalId(Integer proposalId, Integer start, Integer limit) {
		return this.analysis3Service.getCompactAnalysisByProposalId(proposalId, start, limit);
	}
	
	

	public AbInitioModel3VO getAbinitioById(Integer abinitioId) {
		return this.abInitioModelling3Service.getAbinitioModelsById(abinitioId);
	}

	public List<AbInitioModel3VO> getAbinitioById(List<Integer> abinitioIds) {
		System.out.println("Abinnitios: " + Arrays.asList(abinitioIds).toString());
		List<AbInitioModel3VO> result = new ArrayList<AbInitioModel3VO>();
		for (Integer abinitioId : abinitioIds) {
			result.add(this.getAbinitioById(abinitioId));
		}
		System.out.println(result.size());
		return result;
	}

	public FitStructureToExperimentalData3VO getFitStructureToExperimentDatal(int fitStructureToExperimentalDataId) {
		return this.advancedAnalysis.getFitStructureToExperimentDatal(fitStructureToExperimentalDataId);
	}

	public Workflow3VO save(Workflow3VO workflow, ArrayList<InputParameterWorkflow> inputs) {
		return this.advancedAnalysis.merge(workflow, inputs);

	}

	public FitStructureToExperimentalData3VO merge(FitStructureToExperimentalData3VO fitStructureToExperimentalData) {
		return this.advancedAnalysis.merge(fitStructureToExperimentalData);
	}

	public List<FitStructureToExperimentalData3VO> getFitStructureToExperimentalDataBySubtractionId(Integer subtractionId) {
		return this.advancedAnalysis.getFitStructureToExperimentalDataBySubtractionId(subtractionId);
	}

	public Macromolecule3VO findMacromoleculesById(Integer macromoleculeId) {
		return this.saxsproposal3Service.findMacromoleculesById(macromoleculeId);
	}

	public List<RigidBodyModeling3VO> getRigidBodyModelingBySubtractionId(int subtractionId) {
		return this.advancedAnalysis.getRigidBodyModelingBySubtractionId(subtractionId);
	}

	public List<Superposition3VO> getSuperpositionBySubtractionId(int subtractionId) {
		return this.advancedAnalysis.getSuperpositionBySubtractionId(subtractionId);
	}

	public List<FitStructureToExperimentalData3VO> getFitStructureToExperimentalDataByIds(List<Integer> fitIds) {
		if (fitIds != null){
			if (fitIds.size() > 0){
				return this.advancedAnalysis.getFitStructureToExperimentalDataByIds(fitIds);
			}
		}
		return new ArrayList<FitStructureToExperimentalData3VO>();
	}

	public List<Subtraction3VO> getAbinitioModelsBySubtractionId(List<Integer> subtractionIds) {
		List<Subtraction3VO> result = new ArrayList<Subtraction3VO>();
		if (subtractionIds != null){
			for (Integer subtractionId : subtractionIds) {
				result.addAll(this.abInitioModelling3Service.getAbinitioModelsBySubtractionId(subtractionId));
			}
		}
		return result;
	}



	

	

}
