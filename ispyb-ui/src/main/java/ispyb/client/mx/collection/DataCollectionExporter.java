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
package ispyb.client.mx.collection;

import java.io.File;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import ispyb.client.common.util.FileUtil;
import ispyb.client.mx.ranking.SampleRankingVO;
import ispyb.client.mx.ranking.autoProcRanking.AutoProcRankingVO;
import ispyb.client.mx.results.ScreeningStrategySubWedgeValueInfo;
import ispyb.client.mx.results.ScreeningStrategyValueInfo;
import ispyb.client.mx.results.ScreeningStrategyWedgeValueInfo;
import ispyb.client.mx.results.SnapshotInfo;
import ispyb.common.util.Constants;
import ispyb.common.util.Formatter;
import ispyb.common.util.PathUtils;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.autoproc.AutoProcIntegration3Service;
import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.server.mx.services.collections.DataCollectionGroup3Service;
import ispyb.server.mx.services.collections.Image3Service;
import ispyb.server.mx.services.screening.Screening3Service;
import ispyb.server.mx.services.screening.ScreeningOutput3Service;
import ispyb.server.mx.services.screening.ScreeningStrategy3Service;
import ispyb.server.mx.services.screening.ScreeningStrategyWedge3Service;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
import ispyb.server.mx.vos.collections.Image3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.screening.Screening3VO;
import ispyb.server.mx.vos.screening.ScreeningOutput3VO;
import ispyb.server.mx.vos.screening.ScreeningOutputLattice3VO;
import ispyb.server.mx.vos.screening.ScreeningStrategy3VO;
import ispyb.server.mx.vos.screening.ScreeningStrategySubWedge3VO;
import ispyb.server.mx.vos.screening.ScreeningStrategyWedge3VO;

/**
 * loads the dataCollection information for the reports - builds a DataCollectionInformation object
 * 
 * @author Marjolaine Bodin
 * 
 */
public class DataCollectionExporter {

	public final static int MIN_NB_IMAGES_COLLECT = 5;

	DecimalFormat df2;

	DecimalFormat df3;

	DecimalFormat decimalFormat0 = new DecimalFormat("0");

	DecimalFormat decimalFormat1 = new DecimalFormat("0.0");

	DecimalFormat decimalFormat2 = new DecimalFormat("0.00");

	DecimalFormat decimalFormat3 = new DecimalFormat("0.000");

	HttpServletRequest mRequest;

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private DataCollection3Service dataCollectionService;
	
	private DataCollectionGroup3Service dataCollectionGroupService;

	private Screening3Service screeningService;

	private ScreeningOutput3Service screeningOutputService;

	private ScreeningStrategy3Service screeningStrategyService;

	private ScreeningStrategyWedge3Service screeningStrategyWedgeService;

	private AutoProcIntegration3Service autoProcIntegrationService;

	// proposal
	String proposalCode;

	String proposalNumber;

	// --- Information we want --------------------------------------------------------------------------------------
	String mImagePrefix = "";

	String mDataCollectionNumber = "";

	String mNumberOfImages = "";

	String mWavelength = "";

	String mDetectorDistance = "";

	String mExposureTime = "";

	String mAxisStart = "";

	String mAxisRange = "";

	String mBeamSizeAtSampleX = "";

	String mBeamSizeAtSampleY = "";

	String mResolution = "";

	String mComments = "";

	Byte mStrategySuccess = 0;

	Byte mIndexingSuccess = 0;

	String mStatusDescription = "";

	ScreeningOutputLattice3VO mScreeningOutputLattice = new ScreeningOutputLattice3VO();

	ScreeningOutput3VO mScreeningOutPut = new ScreeningOutput3VO();

	boolean mScreeningsExist = false;

	ArrayList<ScreeningStrategyValueInfo> mScreeningStrategyList = new ArrayList<ScreeningStrategyValueInfo>();

	ArrayList<ArrayList<ScreeningStrategyWedgeValueInfo>> mScreeningStrategyWedgeList = new ArrayList<ArrayList<ScreeningStrategyWedgeValueInfo>>();

	ArrayList<ArrayList<ArrayList<ScreeningStrategySubWedgeValueInfo>>> mScreeningStrategySubWedgeList = new ArrayList<ArrayList<ArrayList<ScreeningStrategySubWedgeValueInfo>>>();

	Timestamp mDataCollectionDate = null;

	String mPathJpgCrystal = "";

	String mPathJpgCrystal3 = "";

	String mPathJpgDiffractionImg1 = "";

	String mPathJpgDiffractionImg2 = "";

	String mPathJpgDiffractionImg3 = "";

	String mPathJpgDNAPredictionImg = "";

	String mDiffractionImgNumber1 = "";

	String mDiffractionImgNumber2 = "";

	String mDiffractionImgNumber3 = "";

	Integer mDiffractionImgId1 = null;

	Integer mDiffractionImgId2 = null;

	Integer mDiffractionImgId3 = null;

	// --- DNA prediction Image --------------
	// int mDataCollectionId = -1;

	String mFullDNAPath = null;

	boolean mDNAContentPresent = false;

	boolean mDNAPredictionExist = false;

	boolean mPredictionImageFound = false;

	// --- DNA Lattices -------------------------
	String mSpacegroup = "";

	String mCellA = "";

	String mCellB = "";

	String mCellC = "";

	String mCellAlpha = "";

	String mCellBeta = "";

	String mCellGamma = "";

	String mMosaicity = "";

	String mResObserved = "";

	String mISigi = "";

	// --- Strategy ----------------------------
	String phiStart = "";

	String phiEnd = "";

	String rotation = "";

	String transmission = "";

	String strategy_exposureTime = "";

	String strategy_resolution = "";

	String totalExposure = "";

	String nbImages = "";

	String program = "";

	String mFlux = "";

	String mTotalExposureTime = "";

	// --- StrategyWedge & SubWedge
	List<StrategyWedgeInformation> strategyWedgeInformationList = new ArrayList<StrategyWedgeInformation>();

	// -----END of Information

	public DataCollectionExporter(DecimalFormat df2, DecimalFormat df3, String proposalCode, String proposalNumber,
			HttpServletRequest mRequest) throws CreateException, NamingException {
		this.df2 = df2;
		this.df3 = df3;
		this.proposalCode = proposalCode;
		this.proposalNumber = proposalNumber;
		this.mRequest = mRequest;
		init();
	}

	private void init() throws CreateException, NamingException {
		decimalFormat0 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		decimalFormat0.applyPattern("0");
		decimalFormat1 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		decimalFormat1.applyPattern("0.0");
		decimalFormat2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		decimalFormat2.applyPattern("0.00");

		this.dataCollectionService = (DataCollection3Service) ejb3ServiceLocator.getLocalService(DataCollection3Service.class);
		this.dataCollectionGroupService = (DataCollectionGroup3Service) ejb3ServiceLocator.getLocalService(DataCollectionGroup3Service.class);
		this.screeningService = (Screening3Service) ejb3ServiceLocator.getLocalService(Screening3Service.class);
		this.screeningOutputService = (ScreeningOutput3Service) ejb3ServiceLocator.getLocalService(ScreeningOutput3Service.class);
		this.screeningStrategyService = (ScreeningStrategy3Service) ejb3ServiceLocator
				.getLocalService(ScreeningStrategy3Service.class);
		this.screeningStrategyWedgeService = (ScreeningStrategyWedge3Service) ejb3ServiceLocator
				.getLocalService(ScreeningStrategyWedge3Service.class);
		this.autoProcIntegrationService = (AutoProcIntegration3Service) ejb3ServiceLocator
				.getLocalService(AutoProcIntegration3Service.class);

	}

	public DataCollectionInformation getDataCollectionInformation(DataCollection3VO dcValue, SampleRankingVO sampleRankingVO,
			AutoProcRankingVO autoProcRankingVO) throws Exception {
		DataCollectionInformation info = new DataCollectionInformation();
		ScreeningInformation screeningInfo = new ScreeningInformation();

		// Integer dataCollectionId = dcValue.getDataCollectionId();
		boolean isScreening = isDataCollectionScreening(dcValue);
		boolean isDataCollection = !isScreening;
		// String mFullDNAPath = PathUtils.getFullDNAPath(dcValue);
		// boolean mDNAContentPresent = (new File(mFullDNAPath + Constants.DNA_FILES_INDEX_FILE)).exists();
		// -----------------------------

		// Session3VO slv = sessionService.findByPk(dcValue.getSessionId(), false, false, false);
		Session3VO slv = dcValue.getDataCollectionGroupVO().getSessionVO();

		if (dcValue.getImagePrefix() != null)
			mImagePrefix = dcValue.getImagePrefix();
		if (dcValue.getDataCollectionNumber() != null)
			mDataCollectionNumber = dcValue.getDataCollectionNumber().toString();
		if (dcValue.getNumberOfImages() != null)
			mNumberOfImages = dcValue.getNumberOfImages().toString();
		if (dcValue.getWavelength() != null)
			mWavelength = df3.format(dcValue.getWavelength());
		if (dcValue.getDetectorDistance() != null)
			mDetectorDistance = df2.format(dcValue.getDetectorDistance());
		if (dcValue.getExposureTime() != null)
			mExposureTime = df3.format(dcValue.getExposureTime());
		if (dcValue.getAxisStart() != null)
			mAxisStart = df2.format(dcValue.getAxisStart());
		if (dcValue.getAxisRange() != null)
			mAxisRange = df2.format(dcValue.getAxisRange());
		if (dcValue.getBeamSizeAtSampleX() != null)
			mBeamSizeAtSampleX = df2.format(dcValue.getBeamSizeAtSampleX());
		if (dcValue.getBeamSizeAtSampleY() != null)
			mBeamSizeAtSampleY = df2.format(dcValue.getBeamSizeAtSampleY());
		if (dcValue.getResolution() != null)
			mResolution = df2.format(dcValue.getResolution());
		if (dcValue.getComments() != null)
			mComments = dcValue.getComments();
		if (dcValue.getFlux() != null)
			mFlux = dcValue.getFlux().toString();
		if (dcValue.getTotalExposureTime() != null)
			mTotalExposureTime = df2.format(dcValue.getTotalExposureTime());

		info.setDataCollectionId(dcValue.getDataCollectionId());
		info.setImagePrefix(mImagePrefix);
		info.setDataCollectionNumber(mDataCollectionNumber);
		info.setNumberOfImages(mNumberOfImages);
		info.setWavelength(mWavelength);
		info.setDetectorDistance(mDetectorDistance);
		info.setExposureTime(mExposureTime);
		info.setAxisStart(mAxisStart);
		info.setAxisRange(mAxisRange);
		info.setBeamSizeAtSampleX(mBeamSizeAtSampleX);
		info.setBeamSizeAtSampleY(mBeamSizeAtSampleY);
		info.setResolution(mResolution);
		info.setComments(mComments);
		info.setFlux(mFlux);
		info.setTotalExposure(mTotalExposureTime);
		// --- Screening Information ---
		screeningInfo.setProposalCodeAndNumber(proposalCode + proposalNumber);
		screeningInfo.setBeamLineName(slv.getBeamlineName());
		screeningInfo.setSessionStartDate(Formatter.formatDate(slv.getStartDate()));
		screeningInfo.setSessionComments(slv.getComments());
		// ---------------------------------------------- DNA indexing result
		// ---------------------------------------------

		DataCollection3VO dataCollectionVO = dataCollectionService.findByPk(dcValue.getDataCollectionId(), true, true);
		DataCollectionGroup3VO dataCollectionGroupVO = dataCollectionGroupService.findByPk(dataCollectionVO.getDataCollectionGroupVOId(), true, true);
		// DataCollectionValue dataCollection = _dataCollection.findByPrimaryKey(dcValue.getDataCollectionId());
		// DataCollectionLightValue dataCollectionLight = _dataCollection.findByPrimaryKeyLight(dcValue
		// .getDataCollectionId());
		Screening3VO[] mLstScreenings = dataCollectionGroupVO.getScreeningsTab();

		// ------------------------------------ DataCollection Date and Time -----------------------------------
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		info.setDataCollectionDate(formatter.format(dataCollectionVO.getStartTime()));
		// ------------------------------------- Screening Success / Failure ------------------------------------
		if (mLstScreenings.length > 0 && isScreening) // Get Screening Success - Failure message
		{
			mScreeningsExist = true;
			// Screening3VO lscreening = mLstScreenings[0];
			// Screening3VO screening = _screening.findByPrimaryKey(lscreening.getScreeningId());
			Screening3VO screening = mLstScreenings[0];
			screening = screeningService.loadEager(screening);

			ScreeningOutput3VO[] screeningOutputs = screening.getScreeningOutputsTab();
			if (screeningOutputs.length > 0) {
				ScreeningOutput3VO screeningOutputLight = screeningOutputs[0];
				screeningOutputLight = screeningOutputService.loadEager(screeningOutputLight);
				// mScreeningOutPut = _screeningOutput.findByPrimaryKey(screeningOutputLight.getPrimaryKey());
				mScreeningOutPut = screeningOutputLight;

				// ScreeningOutputLattice3VO[] screeningOutputLattices = mScreeningOutPut
				// .getScreeningOutputLattices();
				ScreeningOutputLattice3VO[] screeningOutputLattices = mScreeningOutPut.getScreeningOutputLatticesTab();

				// --- Lattices ---
				if (screeningOutputLattices.length > 0) {
					mScreeningOutputLattice = screeningOutputLattices[0];
				}

				// --- Screening Success / Failure ---
				mStrategySuccess = screeningOutputLight.getStrategySuccess();
				mIndexingSuccess = screeningOutputLight.getIndexingSuccess();
				mStatusDescription = screeningOutputLight.getStatusDescription();

				// --- DNA Strategies ---
				// ScreeningStrategy3VO[] screeningStrategys = mScreeningOutPut.getScreeningStrategys();
				ScreeningStrategy3VO[] screeningStrategys = mScreeningOutPut.getScreeningStrategysTab();
				if (screeningStrategys.length > 0) {
					for (int s = 0; s < screeningStrategys.length; s++) {
						// ScreeningStrategy3VO screeningStrategyValue = _screeningStrategy
						// .findByPrimaryKey(screeningStrategys[s].getPrimaryKey());
						ScreeningStrategy3VO screeningStrategyValue = screeningStrategys[s];
						screeningStrategyValue = screeningStrategyService.loadEager(screeningStrategyValue);
						ScreeningStrategyValueInfo ssvi = new ScreeningStrategyValueInfo(screeningStrategyValue);
						ssvi.setProgramLog(dataCollectionVO);
						mScreeningStrategyList.add(ssvi);
						// --- strategy wedge ---
						ArrayList<ScreeningStrategyWedgeValueInfo> ascreeningStrategyWedgeValueInfo = new ArrayList<ScreeningStrategyWedgeValueInfo>();
						ArrayList<ArrayList<ScreeningStrategySubWedgeValueInfo>> strategyScreeningStrategySubWedgeValueInfo = new ArrayList<ArrayList<ScreeningStrategySubWedgeValueInfo>>();
						// ScreeningStrategyWedge3VO[] screeningStrategysWedge = ssvi.getScreeningStrategyWedges();
						ScreeningStrategyWedge3VO[] screeningStrategysWedge = screeningStrategyValue.getScreeningStrategyWedgesTab();
						if (screeningStrategysWedge != null && screeningStrategysWedge.length > 0) {
							for (int w = 0; w < screeningStrategysWedge.length; w++) {
								// ScreeningStrategyWedge3VO screeningStrategyWedgeValue = _screeningStrategyWedge
								// .findByPrimaryKey(screeningStrategysWedge[w].getPrimaryKey());
								ScreeningStrategyWedge3VO screeningStrategyWedgeValue = screeningStrategysWedge[w];
								screeningStrategyWedgeValue = screeningStrategyWedgeService.loadEager(screeningStrategyWedgeValue);
								ScreeningStrategyWedgeValueInfo sswvi = new ScreeningStrategyWedgeValueInfo(
										screeningStrategyWedgeValue);
								ascreeningStrategyWedgeValueInfo.add(sswvi);
								// --- strategy sub wedge ---
								ArrayList<ScreeningStrategySubWedgeValueInfo> aScreeningStrategySubWedgeValueInfo = new ArrayList<ScreeningStrategySubWedgeValueInfo>();
								ScreeningStrategySubWedge3VO[] screeningStrategysSubWedge = screeningStrategyWedgeValue
										.getScreeningStrategySubWedgesTab();
								if (screeningStrategysSubWedge.length > 0) {
									for (int sw = 0; sw < screeningStrategysSubWedge.length; sw++) {
										// ScreeningStrategySubWedge3VO screeningStrategySubWedgeValue = _screeningStrategySubWedge
										// .findByPrimaryKey(screeningStrategysSubWedge[sw].getPrimaryKey());
										ScreeningStrategySubWedge3VO screeningStrategySubWedgeValue = screeningStrategysSubWedge[sw];
										ScreeningStrategySubWedgeValueInfo ssswvi = new ScreeningStrategySubWedgeValueInfo(
												screeningStrategySubWedgeValue);
										aScreeningStrategySubWedgeValueInfo.add(ssswvi);
									}
								}
								strategyScreeningStrategySubWedgeValueInfo.add(aScreeningStrategySubWedgeValueInfo);
							}
						}
						mScreeningStrategyWedgeList.add(ascreeningStrategyWedgeValueInfo);
						mScreeningStrategySubWedgeList.add(strategyScreeningStrategySubWedgeValueInfo);
					}
				}
			}
		}

		// ---
		if (!mScreeningsExist)
			info.setScreeningNotDone("No Indexing available");
		if (mIndexingSuccess == 1)
			info.setScreeningSuccess(mStatusDescription);
		if (mIndexingSuccess == 0)
			info.setScreeningFailure(mStatusDescription);
		info.setScreeningIndexingSuccess(mIndexingSuccess);
		info.setScreeningStrategySuccess(mStrategySuccess);
		// ------------------------------------ Strategy -------------------------------------------------------------
		// --- Keep latest strategy ---
		if (mScreeningStrategyList.size() > 0 && isScreening) {
			ScreeningStrategyValueInfo ssvi = mScreeningStrategyList.get(mScreeningStrategyList.size() - 1);
			phiStart = (ssvi.getPhiStart() != null) ? decimalFormat1.format(ssvi.getPhiStart()) : "";
			phiEnd = (ssvi.getPhiEnd() != null) ? decimalFormat1.format(ssvi.getPhiEnd()) : "";
			rotation = (ssvi.getRotation() != null) ? decimalFormat1.format(ssvi.getRotation()) : "";
			transmission = (ssvi.getTransmission() != null) ? decimalFormat1.format(ssvi.getTransmission()) : "";
			strategy_exposureTime = (ssvi.getExposureTime() != null) ? decimalFormat1.format(ssvi.getExposureTime()) : "";
			strategy_resolution = (ssvi.getResolution() != null) ? decimalFormat2.format(ssvi.getResolution()) : "";
			totalExposure = (ssvi.getTotExposureTime() != null) ? decimalFormat1.format(ssvi.getTotExposureTime()) : "";
			nbImages = (ssvi.getTotNbImages() != null) ? decimalFormat0.format(ssvi.getTotNbImages()) : "";
			program = (ssvi.getProgram() != null) ? ssvi.getProgram() : "";
			info.setHasStrategy(true);
			info.setPhiStart(phiStart);
			info.setPhiEnd(phiEnd);
			info.setRotation(rotation);
			info.setTransmission(transmission);
			info.setStrategy_exposureTime(strategy_exposureTime);
			info.setStartegy_resolution(strategy_resolution);
			info.setTotalExposure(totalExposure);
			info.setNbImages(nbImages);
			info.setProgram(program);

			// strategy wedge
			// info
			ArrayList<ScreeningStrategyWedgeValueInfo> aScreeningStrategyWedgeValueInfo = mScreeningStrategyWedgeList
					.get(mScreeningStrategyList.size() - 1);

			List<StrategyWedgeInformation> listStrategyWedgeInformation = new ArrayList<StrategyWedgeInformation>();
			int nb = aScreeningStrategyWedgeValueInfo.size();
			for (int i = 0; i < nb; i++) {
				StrategyWedgeInformation swi = new StrategyWedgeInformation();
				ScreeningStrategyWedgeValueInfo sswvi = aScreeningStrategyWedgeValueInfo.get(i);

				swi.setWedgeNumber((sswvi.getWedgeNumber() != null) ? decimalFormat0.format(sswvi.getWedgeNumber()) : "");
				swi.setResolution((sswvi.getResolution() != null) ? decimalFormat1.format(sswvi.getResolution()) : "");
				swi.setCompleteness((sswvi.getCompleteness() != null) ? decimalFormat1.format(sswvi.getCompleteness()) : "");
				swi.setMultiplicity((sswvi.getMultiplicity() != null) ? decimalFormat1.format(sswvi.getMultiplicity()) : "");
				swi.setDoseTotal((sswvi.getDoseTotal() != null) ? decimalFormat1.format(sswvi.getDoseTotal()) : "");
				swi.setNumberOfImages((sswvi.getNumberOfImages() != null) ? decimalFormat0.format(sswvi.getNumberOfImages()) : "");
				swi.setPhi((sswvi.getPhi() != null) ? decimalFormat1.format(sswvi.getPhi()) : "");
				swi.setKappa((sswvi.getKappa() != null) ? decimalFormat1.format(sswvi.getKappa()) : "");
				swi.setWavelength((sswvi.getWavelength() != null) ? decimalFormat1.format(sswvi.getWavelength()) : "");
				swi.setComments((sswvi.getComments() != null) ? sswvi.getComments() : "");
				List<StrategySubWedgeInformation> listStrategySubWedgeInformation = new ArrayList<StrategySubWedgeInformation>();

				ArrayList<ScreeningStrategySubWedgeValueInfo> aScreeningStrategySubWedgeValueInfo = mScreeningStrategySubWedgeList
						.get(mScreeningStrategyList.size() - 1).get(i);
				for (Iterator<ScreeningStrategySubWedgeValueInfo> j = aScreeningStrategySubWedgeValueInfo.iterator(); j.hasNext();) {
					ScreeningStrategySubWedgeValueInfo ssswvi = j.next();
					// info
					StrategySubWedgeInformation sswi = new StrategySubWedgeInformation();
					sswi.setSubWedgeNumber((ssswvi.getSubWedgeNumber() != null) ? decimalFormat0.format(ssswvi.getSubWedgeNumber())
							: "");
					sswi.setRotationAxis((ssswvi.getRotationAxis() != null) ? ssswvi.getRotationAxis() : "");
					sswi.setAxisStart((ssswvi.getAxisStart() != null) ? decimalFormat1.format(ssswvi.getAxisStart()) : "");
					sswi.setAxisEnd((ssswvi.getAxisEnd() != null) ? decimalFormat1.format(ssswvi.getAxisEnd()) : "");
					sswi.setExposureTime((ssswvi.getExposureTime() != null) ? decimalFormat3.format(ssswvi.getExposureTime()) : "");
					sswi.setTransmission((ssswvi.getTransmission() != null) ? decimalFormat1.format(ssswvi.getTransmission()) : "");
					sswi.setOscillationRange((ssswvi.getOscillationRange() != null) ? decimalFormat1.format(ssswvi
							.getOscillationRange()) : "");
					sswi.setCompleteness((ssswvi.getCompleteness() != null) ? decimalFormat1.format(ssswvi.getCompleteness()) : "");
					sswi.setMultiplicity((ssswvi.getMultiplicity() != null) ? decimalFormat1.format(ssswvi.getMultiplicity()) : "");
					sswi.setDoseTotal((ssswvi.getDoseTotal() != null) ? decimalFormat1.format(ssswvi.getDoseTotal()) : "");
					sswi.setNumberOfImages((ssswvi.getNumberOfImages() != null) ? decimalFormat0.format(ssswvi.getNumberOfImages())
							: "");
					sswi.setComments((ssswvi.getComments() != null) ? ssswvi.getComments() : "");
					listStrategySubWedgeInformation.add(sswi);
				}
				swi.setListStrategySubWedgeInformation(listStrategySubWedgeInformation);
				listStrategyWedgeInformation.add(swi);
			}

			info.setListStrategyWedgeInformation(listStrategyWedgeInformation);
			strategyWedgeInformationList = listStrategyWedgeInformation;
		}
		// ------------------------------------ DNA Lattices ---------------------------------------------------------
		if (isScreening) {
			mSpacegroup = (mScreeningOutputLattice.getSpaceGroup() != null) ? mScreeningOutputLattice.getSpaceGroup() : "";
			mCellA = (mScreeningOutputLattice.getUnitCell_a() != null) ? decimalFormat2
					.format(mScreeningOutputLattice.getUnitCell_a()) : "";
			mCellB = (mScreeningOutputLattice.getUnitCell_b() != null) ? decimalFormat2
					.format(mScreeningOutputLattice.getUnitCell_b()) : "";
			mCellC = (mScreeningOutputLattice.getUnitCell_c() != null) ? decimalFormat2
					.format(mScreeningOutputLattice.getUnitCell_c()) : "";
			mCellAlpha = (mScreeningOutputLattice.getUnitCell_alpha() != null) ? decimalFormat2.format(mScreeningOutputLattice
					.getUnitCell_alpha()) : "";
			mCellBeta = (mScreeningOutputLattice.getUnitCell_beta() != null) ? decimalFormat2.format(mScreeningOutputLattice
					.getUnitCell_beta()) : "";
			mCellGamma = (mScreeningOutputLattice.getUnitCell_gamma() != null) ? decimalFormat2.format(mScreeningOutputLattice
					.getUnitCell_gamma()) : "";
			mMosaicity = (mScreeningOutPut.getMosaicity() != null) ? decimalFormat2.format(mScreeningOutPut.getMosaicity()) : "";
			mResObserved = (mScreeningOutPut.getRankingResolution() != null) ? decimalFormat2.format(mScreeningOutPut
					.getRankingResolution()) : "";
			mISigi = (mScreeningOutPut.getIoverSigma() != null) ? decimalFormat2.format(mScreeningOutPut.getIoverSigma()) : "";

			info.setSpacegroup(mSpacegroup);
			info.setCellA(mCellA);
			info.setCellB(mCellB);
			info.setCellC(mCellC);
			info.setCellAlpha(mCellAlpha);
			info.setCellBeta(mCellBeta);
			info.setCellGamma(mCellGamma);
			info.setMosaicity(mMosaicity);
			info.setResObserved(mResObserved);
			info.setISigi(mISigi);
		}
		// ------------------------------------ Crystal Image -------------------------------------------------------
		ArrayList<SnapshotInfo> xtalSnaphsots = FileUtil.GetFullSnapshotPath(dataCollectionVO);
		boolean crystalImageExist = false;
		String pathJpgCrystal = "";

		if (xtalSnaphsots.size() != 0) {
			SnapshotInfo snapshot1 = xtalSnaphsots.get(0);
			if (!snapshot1.isFilePresent())
				crystalImageExist = false;
			else {
				pathJpgCrystal = snapshot1.getFileLocation();
				crystalImageExist = true;
			}

		}

		if (!crystalImageExist) {
			// LOG.debug("[PdfExporterScreening::exportAsPdf] does not exist: " + pathJpgCrystal
			// );
			pathJpgCrystal = mRequest.getRealPath(Constants.IMAGE_NO_XTAL_THUMBNAILS_RELATIVE_PATH);
		}

		this.mPathJpgCrystal = pathJpgCrystal;
		info.setPathJpgCrystal(pathJpgCrystal);

		// snaphsot3 for MXpressO report
		boolean crystalImageExist3 = false;
		String pathJpgCrystal3 = "";

		if (xtalSnaphsots.size() != 0 && xtalSnaphsots.size() > 2) {
			SnapshotInfo snapshot3 = xtalSnaphsots.get(2);
			if (!snapshot3.isFilePresent())
				crystalImageExist3 = false;
			else {
				pathJpgCrystal3 = snapshot3.getFileLocation();
				crystalImageExist3 = true;
			}

		}

		if (!crystalImageExist3) {
			// LOG.debug("[PdfExporterScreening::exportAsPdf] does not exist: " + pathJpgCrystal
			// );
			pathJpgCrystal3 = mRequest.getRealPath(Constants.IMAGE_NO_XTAL_THUMBNAILS_RELATIVE_PATH);
		}

		this.mPathJpgCrystal3 = pathJpgCrystal3;
		info.setPathJpgCrystal3(pathJpgCrystal3);

		// ------------------------------------ Diffraction Image ---------------------------------------------------
		// List<String> lstImagesScreening = null;
		List<String> lstImagesDataCollection = null;
		List<String> lstImages = null;

		// lstImages = FileUtil.getImageJpgThumbList(dcValue.getDataCollectionId());

		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		Image3Service imageService = (Image3Service) ejb3ServiceLocator.getLocalService(Image3Service.class);
		List<Image3VO> tmp_imageList = imageService.findByDataCollectionId(dcValue.getDataCollectionId());
		// List tmp_imageList = (List) image.findByDataCollectionId(dataCollectionId);

		List<String> imageList = new ArrayList<String>(tmp_imageList.size());
		List<Integer> imageIdList = new ArrayList<Integer>(tmp_imageList.size());

		for (int i = 0; i < tmp_imageList.size(); i++) {
			Image3VO imgValue = tmp_imageList.get(i);
			String jpgThumbFullPath = imgValue.getJpegThumbnailFileFullPath();
			imageList.add(jpgThumbFullPath);
			imageIdList.add(imgValue.getImageId());
		}
		lstImages = imageList;

		int nbImages = lstImages.size();

		// if (isScreening)
		// lstImagesScreening = null;

		int firstIndex = -1;
		int lastIndex = -1;
		int middleIndex = -1;
		if (isDataCollection && nbImages > 0) {
			lstImagesDataCollection = new ArrayList<String>(Constants.NB_IMAGES_DATA_COLLECTION - 1);

			// --- Get Image Indexes ---
			firstIndex = 0;
			lastIndex = (nbImages > 0) ? nbImages - 1 : 0;
			middleIndex = Math.round(nbImages / 2);
			if (nbImages == 3)
				middleIndex = 2;

			lstImagesDataCollection.add(0, lstImages.get(firstIndex));
			lstImagesDataCollection.add(1, lstImages.get(middleIndex));
			lstImagesDataCollection.add(Constants.NB_IMAGES_DATA_COLLECTION - 1, lstImages.get(lastIndex));
			lstImages = lstImagesDataCollection;
		}
		mDiffractionImgNumber1 = (firstIndex == -1) ? "0" : Integer.toString(firstIndex + 1);
		mDiffractionImgNumber2 = (middleIndex == -1) ? "0" : Integer.toString(middleIndex);
		mDiffractionImgNumber3 = (lastIndex == -1) ? "0" : Integer.toString(lastIndex + 1);
		int imgNumber = 1;
		// for (String jpgThumbFullPath : lstImages) {
		for (int i = 0; i < lstImages.size(); i++) {
			String jpgThumbFullPath = lstImages.get(i);
			Integer imageId = null;
			if (i < imageIdList.size() && imageIdList.get(i) != null) {
				imageId = imageIdList.get(i);
			}
			File imageFile = null;
			// --- Check File exists ---
			boolean fileDoesNotExist = false; // Check File name
			if (jpgThumbFullPath != null) // null
			{
				if (jpgThumbFullPath.trim().equals("")) // ""
					fileDoesNotExist = true;
			} else
				fileDoesNotExist = true;

			if (!fileDoesNotExist) // File name ok..check it's there
			{
				jpgThumbFullPath = PathUtils.FitPathToOS(jpgThumbFullPath);
				imageFile = new File(jpgThumbFullPath);

				if (!imageFile.exists()) {
					fileDoesNotExist = true;
				}
			}
			// -------------------------
			if (fileDoesNotExist) {
				// LOG.debug("[PdfExporterScreening::exportAsPdf] does not exist: " +
				// jpgThumbFullPath);
				jpgThumbFullPath = mRequest.getRealPath(Constants.IMAGE_NO_DIFFRACTION_THUMBNAILS_RELATIVE_PATH);
			}
			switch (imgNumber) {
			case 1:
				mPathJpgDiffractionImg1 = jpgThumbFullPath;
				mDiffractionImgId1 = imageId;
				break;
			case 2:
				mPathJpgDiffractionImg2 = jpgThumbFullPath;
				mDiffractionImgId2 = imageId;
				break;
			case 3:
				mPathJpgDiffractionImg3 = jpgThumbFullPath;
				mDiffractionImgId3 = imageId;
				break;
			}
			imgNumber++;
		}
		if (mPathJpgDiffractionImg1.equals(""))
			mPathJpgDiffractionImg1 = mRequest.getRealPath(Constants.IMAGE_NO_DIFFRACTION_THUMBNAILS_RELATIVE_PATH);
		if (mPathJpgDiffractionImg2.equals(""))
			mPathJpgDiffractionImg2 = mRequest.getRealPath(Constants.IMAGE_NO_DIFFRACTION_THUMBNAILS_RELATIVE_PATH);
		if (mPathJpgDiffractionImg3.equals(""))
			mPathJpgDiffractionImg3 = mRequest.getRealPath(Constants.IMAGE_NO_DIFFRACTION_THUMBNAILS_RELATIVE_PATH);

		info.setDiffractionImgNumber1(mDiffractionImgNumber1);
		info.setDiffractionImgNumber2(mDiffractionImgNumber2);
		info.setDiffractionImgNumber3(mDiffractionImgNumber3);
		info.setPathDiffractionImg1(mPathJpgDiffractionImg1);
		info.setPathDiffractionImg2(mPathJpgDiffractionImg2);
		info.setPathDiffractionImg3(mPathJpgDiffractionImg3);
		info.setDiffractionImgId1(mDiffractionImgId1);
		info.setDiffractionImgId2(mDiffractionImgId2);
		info.setDiffractionImgId3(mDiffractionImgId3);

		// ------------------------------------Edna graph ---------------------------------------------------
		String pathEdnaGraph = "";
		if (dataCollectionVO.getBestWilsonPlotPath() != null) {
			String bestWilsonPlotPath = dataCollectionVO.getBestWilsonPlotPath();
			pathEdnaGraph = PathUtils.FitPathToOS(bestWilsonPlotPath);
		}
		info.setPathEdnaGraph(pathEdnaGraph);

		// ------------------------------------AutoProc results graph ---------------------------------------------------
		String pathAutoprocGraph = "";

		info.setPathAutoProcGraph(pathAutoprocGraph);

		// ------------------------------------AutoProc results ---------------------------------------------------
		String autoProcFastStatus = mRequest.getRealPath(Constants.IMAGE_FAILED);
		String autoProcParallelStatus = mRequest.getRealPath(Constants.IMAGE_FAILED);
		String autoProcEdnaStatus = mRequest.getRealPath(Constants.IMAGE_FAILED);
		String autoProcAutoPROCStatus = mRequest.getRealPath(Constants.IMAGE_FAILED);
		String autoProcXia2DialsStatus = mRequest.getRealPath(Constants.IMAGE_FAILED);
		String autoProcFastDPStatus = mRequest.getRealPath(Constants.IMAGE_FAILED);


		Boolean hasFastproc = this.autoProcIntegrationService.getAutoProcStatus(dataCollectionVO.getDataCollectionId(),
				Constants.AUTOPROC_FASTPROC);
		if (hasFastproc) {
			autoProcFastStatus = mRequest.getRealPath(Constants.IMAGE_SUCCESS);
		}

		Boolean hasParallelproc = this.autoProcIntegrationService.getAutoProcStatus(dataCollectionVO.getDataCollectionId(),
				Constants.AUTOPROC_PARALLELPROC);
		if (hasParallelproc) {
			autoProcParallelStatus = mRequest.getRealPath(Constants.IMAGE_SUCCESS);
		}

		Boolean hasEdnaproc = this.autoProcIntegrationService.getAutoProcStatus(dataCollectionVO.getDataCollectionId(),
				Constants.AUTOPROC_EDNAPROC);
		if (hasEdnaproc) {
			autoProcEdnaStatus = mRequest.getRealPath(Constants.IMAGE_SUCCESS);
		}

		Boolean hasAutoPROC = this.autoProcIntegrationService.getAutoProcStatus(dataCollectionVO.getDataCollectionId(),
				Constants.AUTOPROC_AUTOPROC);
		if (hasAutoPROC) {
			autoProcAutoPROCStatus = mRequest.getRealPath(Constants.IMAGE_SUCCESS);
		}

		Boolean hasXia2Dails = this.autoProcIntegrationService.getAutoProcXia2DialsStatus(dataCollectionVO.getDataCollectionId(),
				Constants.AUTOPROC_XIA2_DIALS);
		if (hasXia2Dails) {
			autoProcXia2DialsStatus = mRequest.getRealPath(Constants.IMAGE_SUCCESS);
		}

		Boolean hasFastDP = this.autoProcIntegrationService.getAutoProcFastDPStatus(dataCollectionVO.getDataCollectionId(),
				Constants.AUTOPROC_FASTDP);
		if (hasFastDP) {
			autoProcFastDPStatus = mRequest.getRealPath(Constants.IMAGE_SUCCESS);
		}


		info.setAutoProcFastStatus(autoProcFastStatus);
		info.setAutoProcParallelStatus(autoProcParallelStatus);
		info.setAutoProcEdnaStatus(autoProcEdnaStatus);
		info.setAutoProcAutoPROCStatus(autoProcAutoPROCStatus);
		info.setAutoProcXia2DialsStatus(autoProcXia2DialsStatus);
		info.setAutoProcFastDPStatus(autoProcFastDPStatus);

		info.setScreeningInformation(screeningInfo);
		info.setSampleRankingVO(sampleRankingVO);
		info.setAutoProcRankingVO(autoProcRankingVO);
		return info;
	}

	public static boolean isDataCollectionScreening(DataCollection3VO dcValue) {
		return dcValue.getNumberOfImages() == null ? true : dcValue.getNumberOfImages() <= MIN_NB_IMAGES_COLLECT;
	}

}
