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
 * SampleRankingLoadValue.java
 * @author patrice.brenchereau@esrf.fr
 * March 09, 2009
 */

package ispyb.client.mx.ranking;

import ispyb.client.mx.ranking.SampleScreeningVO.ScreeningOutputLatticeVO;
import ispyb.client.mx.ranking.SampleScreeningVO.ScreeningOutputVO;
import ispyb.client.mx.ranking.SampleScreeningVO.ScreeningStrategyVO;
import ispyb.client.mx.ranking.SampleScreeningVO.ScreeningStrategyWedgeVO;
import ispyb.client.mx.ranking.SampleScreeningVO.ScreeningVO;
import ispyb.common.util.StringUtils;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.server.mx.services.screening.Screening3Service;
import ispyb.server.mx.services.screening.ScreeningOutput3Service;
import ispyb.server.mx.services.screening.ScreeningStrategy3Service;
import ispyb.server.mx.services.screening.ScreeningStrategyWedge3Service;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.screening.Screening3VO;
import ispyb.server.mx.vos.screening.ScreeningOutput3VO;
import ispyb.server.mx.vos.screening.ScreeningOutputLattice3VO;
import ispyb.server.mx.vos.screening.ScreeningStrategy3VO;
import ispyb.server.mx.vos.screening.ScreeningStrategyWedge3VO;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class SampleScreeningUtils {

	private final static Logger LOG = Logger.getLogger(SampleScreeningUtils.class);

	/**
	 * Loads SampleScreeningVO of dataCollectionId
	 * 
	 * @param dataCollectionId
	 * @return
	 * @throws Exception
	 */
	public static SampleScreeningVO load(int dataCollectionId) throws Exception {

		// LOG.debug("Loading screening records for dataCollection "+dataCollectionId);

		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		DataCollection3Service dataCollectionService = (DataCollection3Service) ejb3ServiceLocator
				.getLocalService(DataCollection3Service.class);
		Screening3Service screeningService = (Screening3Service) ejb3ServiceLocator
				.getLocalService(Screening3Service.class);
		ScreeningOutput3Service screeningOutputService = (ScreeningOutput3Service) ejb3ServiceLocator
				.getLocalService(ScreeningOutput3Service.class);
		ScreeningStrategy3Service screeningStrategyService = (ScreeningStrategy3Service) ejb3ServiceLocator
				.getLocalService(ScreeningStrategy3Service.class);
		ScreeningStrategyWedge3Service screeningStrategyWedgeService = (ScreeningStrategyWedge3Service) ejb3ServiceLocator
				.getLocalService(ScreeningStrategyWedge3Service.class);

		// DataCollection
		DataCollection3VO dataCollectionDb = dataCollectionService.findByPk(dataCollectionId, false, false);
		if (dataCollectionDb == null) {
			LOG.debug("SampleRanking: there is no dataCollectionId=" + dataCollectionId);
			return null;
		}
		SampleScreeningVO sampleScreeningVO = new SampleScreeningVO();
		copySampleScreeningVO(dataCollectionDb, sampleScreeningVO);

		// Screening
		List<Screening3VO> screeningDbList = screeningService.findFiltered(dataCollectionId);
		if (screeningDbList.size() > 0) {
			List<ScreeningVO> screeningVOList = new ArrayList<ScreeningVO>();
			for (int i = 0; i < screeningDbList.size(); i++) {
				Screening3VO screeningDb = screeningDbList.get(i);
				screeningDb = screeningService.loadEager(screeningDb);
				ScreeningVO screeningVO = sampleScreeningVO.new ScreeningVO();
				copyScreeningVO(screeningDb, screeningVO);

				// ScreeningInput
				// List<ScreeningInput3VO> screeningInputDbList = screeningDb.getScreeningInputsList();
				// //List<ScreeningInput3VO> screeningInputDbList = (List)
				// screeningInputs.findByScreeningId(screeningDb.getScreeningId());
				// if ( screeningInputDbList.size() > 0 ) {
				// ScreeningInput3VO screeningInputDb = screeningInputDbList.get(0);
				// ScreeningInputVO screeningInputVO = sampleScreeningVO.new ScreeningInputVO();
				// copyScreeningInputVO(screeningInputDb,screeningInputVO);
				// screeningVO.setScreeningInputVO(screeningInputVO);
				// }

				// ScreeningOutput
				List<ScreeningOutput3VO> screeningOutputDbList = screeningDb.getScreeningOutputsList();
				// List<ScreeningOutput3VO> screeningOutputDbList = (List)
				// screeningOutputs.findByScreeningId(screeningDb.getScreeningId());
				if (screeningOutputDbList.size() > 0) {
					ScreeningOutput3VO screeningOutputDb = screeningOutputDbList.get(0);
					screeningOutputDb = screeningOutputService.loadEager(screeningOutputDb);
					ScreeningOutputVO screeningOutputVO = sampleScreeningVO.new ScreeningOutputVO();
					copyScreeningOutputVO(screeningOutputDb, screeningOutputVO);
					{
						// ScreeningOutputLattice
						// List<ScreeningOutputLattice3VO> screeningOutputLatticeDbList = (List)
						// screeningOutputLattices.findByScreeningOutputId(screeningOutputDb.getScreeningOutputId());
						List<ScreeningOutputLattice3VO> screeningOutputLatticeDbList = screeningOutputDb
								.getScreeningOutputLatticesList();
						if (screeningOutputLatticeDbList.size() > 0) {
							ScreeningOutputLattice3VO screeningOutputLatticeDb = screeningOutputLatticeDbList.get(0);
							ScreeningOutputLatticeVO screeningOutputLatticeVO = sampleScreeningVO.new ScreeningOutputLatticeVO();
							copyScreeningOutputLatticeVO(screeningOutputLatticeDb, screeningOutputLatticeVO);
							screeningOutputVO.setScreeningOutputLatticeVO(screeningOutputLatticeVO);
						}

						// ScreeningStrategy
						// List<ScreeningStrategy3VO> screeningStrategyDbList = (List)
						// screeningStrategies.findByScreeningOutputId(screeningOutputDb.getScreeningOutputId());
						List<ScreeningStrategy3VO> screeningStrategyDbList = screeningOutputDb
								.getScreeningStrategysList();
						if (screeningStrategyDbList.size() > 0) {
							List<ScreeningStrategyVO> screeningStrategyVOList = new ArrayList<ScreeningStrategyVO>();
							for (int j = 0; j < screeningStrategyDbList.size(); j++) {
								ScreeningStrategy3VO screeningStrategyDb = screeningStrategyDbList.get(j);
								screeningStrategyDb = screeningStrategyService.loadEager(screeningStrategyDb);
								ScreeningStrategyVO screeningStrategyVO = sampleScreeningVO.new ScreeningStrategyVO();
								copyScreeningStrategyVO(screeningStrategyDb, screeningStrategyVO);
								// ScreeningStrategyWedge
								// ScreeningStrategy
								List<ScreeningStrategyWedge3VO> screeningStrategyWedgeDbList = screeningStrategyDb
										.getScreeningStrategyWedgesList();
								// List<ScreeningStrategyWedge3VO> screeningStrategyWedgeDbList = (List)
								// screeningStrategiesWedge.findByScreeningStrategyId(screeningStrategyDb.getScreeningStrategyId());
								if (screeningStrategyWedgeDbList.size() > 0) {
									List<ScreeningStrategyWedgeVO> screeningStrategyWedgeVOList = new ArrayList<ScreeningStrategyWedgeVO>();
									for (int k = 0; k < screeningStrategyWedgeDbList.size(); k++) {
										ScreeningStrategyWedge3VO screeningStrategyWedgeDb = screeningStrategyWedgeDbList
												.get(k);
										screeningStrategyWedgeDb = screeningStrategyWedgeService
												.loadEager(screeningStrategyWedgeDb);
										ScreeningStrategyWedgeVO screeningStrategyWedgeVO = sampleScreeningVO.new ScreeningStrategyWedgeVO();
										copyScreeningStrategyWedgeVO(screeningStrategyWedgeDb, screeningStrategyWedgeVO);
										screeningStrategyWedgeVOList.add(screeningStrategyWedgeVO);
									}
									screeningOutputVO.setScreeningStrategyVOList(screeningStrategyVOList);
								}
								screeningStrategyVOList.add(screeningStrategyVO);
							}
							screeningOutputVO.setScreeningStrategyVOList(screeningStrategyVOList);
						}
					}
					screeningVO.setScreeningOutputVO(screeningOutputVO);
				}
				screeningVOList.add(screeningVO);
			}
			sampleScreeningVO.setScreeningVOList(screeningVOList);
		}

		return sampleScreeningVO;
	}

	private static void copySampleScreeningVO(DataCollection3VO dataCollectionDb, SampleScreeningVO sampleScreeningVO) {

		sampleScreeningVO.setDataCollectionId(dataCollectionDb.getDataCollectionId());
	}

	private static void copyScreeningVO(Screening3VO screeningDb, ScreeningVO screeningVO) {

		screeningVO.setScreeningId(screeningDb.getScreeningId());
		screeningVO.setTimeStamp(StringUtils.dateToTimestamp(screeningDb.getTimeStamp()));
		screeningVO.setProgramVersion(screeningVO.getProgramVersion());
	}

	private static void copyScreeningOutputVO(ScreeningOutput3VO screeningOutputDb, ScreeningOutputVO screeningOutputVO) {

		screeningOutputVO.setScreeningOutputId(screeningOutputDb.getScreeningOutputId());
		screeningOutputVO.setStatusDescription(screeningOutputDb.getStatusDescription());
		screeningOutputVO.setRejectedReflections(screeningOutputDb.getRejectedReflections());
		screeningOutputVO.setResolutionObtained(screeningOutputDb.getResolutionObtained());
		screeningOutputVO.setSpotDeviationR(screeningOutputDb.getSpotDeviationR());
		screeningOutputVO.setSpotDeviationTheta(screeningOutputDb.getSpotDeviationTheta());
		screeningOutputVO.setBeamShiftX(screeningOutputDb.getBeamShiftX());
		screeningOutputVO.setBeamShiftY(screeningOutputDb.getBeamShiftY());
		screeningOutputVO.setNumSpotsFound(screeningOutputDb.getNumSpotsFound());
		screeningOutputVO.setNumSpotsUsed(screeningOutputDb.getNumSpotsUsed());
		screeningOutputVO.setNumSpotsRejected(screeningOutputDb.getNumSpotsRejected());
		screeningOutputVO.setMosaicity(screeningOutputDb.getMosaicity());
		screeningOutputVO.setIoverSigma(screeningOutputDb.getIoverSigma());
		screeningOutputVO.setDiffractionRings(screeningOutputDb.getDiffractionRings());
		screeningOutputVO.setIndexingSuccess(screeningOutputDb.getIndexingSuccess());
		screeningOutputVO.setStrategySuccess(screeningOutputDb.getStrategySuccess());
		screeningOutputVO.setMosaicityEstimated(screeningOutputDb.getMosaicityEstimated());
		screeningOutputVO.setRankingResolution(screeningOutputDb.getRankingResolution());
		screeningOutputVO.setProgram(screeningOutputDb.getProgram());
		screeningOutputVO.setDoseTotal(screeningOutputDb.getDoseTotal());
		screeningOutputVO.setTotalExposureTime(screeningOutputDb.getTotalExposureTime());
		screeningOutputVO.setTotalRotationRange(screeningOutputDb.getTotalRotationRange());
		screeningOutputVO.setTotalNumberOfImages(screeningOutputDb.getTotalNumberOfImages());
		screeningOutputVO.setrFriedel(screeningOutputDb.getrFriedel());
	}

	private static void copyScreeningOutputLatticeVO(ScreeningOutputLattice3VO screeningOutputLatticeDb,
			ScreeningOutputLatticeVO screeningOutputLatticeVO) {

		screeningOutputLatticeVO.setScreeningOutputLatticeId(screeningOutputLatticeDb.getScreeningOutputLatticeId());
		screeningOutputLatticeVO.setSpaceGroup(screeningOutputLatticeDb.getSpaceGroup());
		screeningOutputLatticeVO.setPointGroup(screeningOutputLatticeDb.getPointGroup());
		screeningOutputLatticeVO.setBravaisLattice(screeningOutputLatticeDb.getBravaisLattice());
		screeningOutputLatticeVO.setRawOrientationMatrixAX(screeningOutputLatticeDb.getRawOrientationMatrix_a_x());
		screeningOutputLatticeVO.setRawOrientationMatrixAY(screeningOutputLatticeDb.getRawOrientationMatrix_a_y());
		screeningOutputLatticeVO.setRawOrientationMatrixAZ(screeningOutputLatticeDb.getRawOrientationMatrix_a_z());
		screeningOutputLatticeVO.setRawOrientationMatrixBX(screeningOutputLatticeDb.getRawOrientationMatrix_b_x());
		screeningOutputLatticeVO.setRawOrientationMatrixBY(screeningOutputLatticeDb.getRawOrientationMatrix_b_y());
		screeningOutputLatticeVO.setRawOrientationMatrixBZ(screeningOutputLatticeDb.getRawOrientationMatrix_b_z());
		screeningOutputLatticeVO.setRawOrientationMatrixCX(screeningOutputLatticeDb.getRawOrientationMatrix_c_x());
		screeningOutputLatticeVO.setRawOrientationMatrixCY(screeningOutputLatticeDb.getRawOrientationMatrix_c_y());
		screeningOutputLatticeVO.setRawOrientationMatrixCZ(screeningOutputLatticeDb.getRawOrientationMatrix_c_z());
		screeningOutputLatticeVO.setUnitCellA(screeningOutputLatticeDb.getUnitCell_a());
		screeningOutputLatticeVO.setUnitCellB(screeningOutputLatticeDb.getUnitCell_b());
		screeningOutputLatticeVO.setUnitCellC(screeningOutputLatticeDb.getUnitCell_c());
		screeningOutputLatticeVO.setUnitCellAlpha(screeningOutputLatticeDb.getUnitCell_alpha());
		screeningOutputLatticeVO.setUnitCellBeta(screeningOutputLatticeDb.getUnitCell_beta());
		screeningOutputLatticeVO.setUnitCellGamma(screeningOutputLatticeDb.getUnitCell_gamma());
		screeningOutputLatticeVO.setTimeStamp(StringUtils.dateToTimestamp(screeningOutputLatticeDb.getTimeStamp()));
	}

	private static void copyScreeningStrategyVO(ScreeningStrategy3VO screeningStrategyDb,
			ScreeningStrategyVO screeningStrategyVO) {

		screeningStrategyVO.setScreeningStrategyId(screeningStrategyDb.getScreeningStrategyId());
		screeningStrategyVO.setPhiStart(screeningStrategyDb.getPhiStart());
		screeningStrategyVO.setPhiEnd(screeningStrategyDb.getPhiEnd());
		screeningStrategyVO.setRotation(screeningStrategyDb.getRotation());
		screeningStrategyVO.setExposureTime(screeningStrategyDb.getExposureTime());
		screeningStrategyVO.setResolution(screeningStrategyDb.getResolution());
		screeningStrategyVO.setCompleteness(screeningStrategyDb.getCompleteness());
		screeningStrategyVO.setMultiplicity(screeningStrategyDb.getMultiplicity());
		screeningStrategyVO.setAnomalous(screeningStrategyDb.getAnomalous());
		screeningStrategyVO.setProgram(screeningStrategyDb.getProgram());
		screeningStrategyVO.setRankingResolution(screeningStrategyDb.getRankingResolution());
	}

	private static void copyScreeningStrategyWedgeVO(ScreeningStrategyWedge3VO screeningStrategyWedgeDb,
			ScreeningStrategyWedgeVO screeningStrategyWedgeVO) {

		screeningStrategyWedgeVO.setScreeningStrategyWedgeId(screeningStrategyWedgeDb.getScreeningStrategyWedgeId());
		screeningStrategyWedgeVO.setWedgeNumber(screeningStrategyWedgeDb.getWedgeNumber());
		screeningStrategyWedgeVO.setResolution(screeningStrategyWedgeDb.getResolution());
		screeningStrategyWedgeVO.setCompleteness(screeningStrategyWedgeDb.getCompleteness());
		screeningStrategyWedgeVO.setMultiplicity(screeningStrategyWedgeDb.getMultiplicity());
		screeningStrategyWedgeVO.setDoseTotal(screeningStrategyWedgeDb.getDoseTotal());
		screeningStrategyWedgeVO.setNumberOfImages(screeningStrategyWedgeDb.getNumberOfImages());
		screeningStrategyWedgeVO.setPhi(screeningStrategyWedgeDb.getPhi());
		screeningStrategyWedgeVO.setKappa(screeningStrategyWedgeDb.getKappa());
		screeningStrategyWedgeVO.setComments(screeningStrategyWedgeDb.getComments());
		screeningStrategyWedgeVO.setWavelength(screeningStrategyWedgeDb.getWavelength());
	}
}
