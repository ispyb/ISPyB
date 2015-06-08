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

package ispyb.client.mx.ranking.autoProcRanking;

import ispyb.client.mx.collection.AutoProcShellWrapper;
import ispyb.client.mx.collection.ViewDataCollectionAction;
import ispyb.common.util.StringUtils;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.autoproc.AutoProcScalingStatistics3Service;
import ispyb.server.mx.vos.autoproc.AutoProc3VO;
import ispyb.server.mx.vos.autoproc.AutoProcScalingStatistics3VO;
import ispyb.server.mx.vos.collections.DataCollection3VO;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class AutoProcRanking {
	private final static int NB_CRITERION = 3;

	private final static int CRITERION_COL_OverallRFactor = 0;

	private final static int CRITERION_COL_HighestResolution = 1;

	private final static int CRITERION_COL_Completeness = 2;

	/*
	 * Weights
	 */
	private AutoProcRankingCriteriaVO criteriaVO = null;

	/**
	 * Set weights
	 * 
	 * @param criteriaVO
	 */
	public void setWeights(AutoProcRankingCriteriaVO criteriaVO) {

		this.criteriaVO = criteriaVO;
	}

	/**
	 * Rank samples
	 */
	public List<AutoProcRankingVO> rank(List<DataCollection3VO> dataCollectionList) {
		// Extract values
		List<AutoProcRankingVO> autoProcRankingValues = getAutoProcValues(dataCollectionList);
		// Rank each criterion
		rankOverallRFactor(autoProcRankingValues);
		rankHighestResolution(autoProcRankingValues);
		rankCompleteness(autoProcRankingValues);

		// Decision matrix
		Float decisionMatrix[][] = buildDecisionMatrix(autoProcRankingValues);
		// plotMatrix("Init",decisionMatrix, "0000.00");
		maximizeMatrix(decisionMatrix);
		// plotMatrix("Maximized",decisionMatrix, "0000.00");
		normalizeMatrix(decisionMatrix);
		// plotMatrix("Normalized",decisionMatrix, "0.000");
		Float[] weightVector = buildWeightVector();
		weightMatrix(weightVector, decisionMatrix);
		// plotMatrix("Weighted",decisionMatrix, "0.000");
		setScore(autoProcRankingValues, decisionMatrix);

		// Rank total
		rankTotal(autoProcRankingValues);
		// plotScore("Ranked", sampleRankingValues);

		return autoProcRankingValues;
	}

	public List<AutoProcRankingVO> getAutoProcValues(List<DataCollection3VO> dataCollectionList) {
		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		AutoProcScalingStatistics3Service apssService;
		List<AutoProcRankingVO> autoProcRankingValues = new ArrayList<AutoProcRankingVO>();
		try {
			apssService = (AutoProcScalingStatistics3Service) ejb3ServiceLocator
					.getLocalService(AutoProcScalingStatistics3Service.class);

			int nb = dataCollectionList.size();

			double rMerge = 10.0;
			double iSigma = 1.0;
			AutoProcShellWrapper wrapper = ViewDataCollectionAction.getAutoProcStatistics(dataCollectionList, rMerge, iSigma);
			AutoProc3VO[] autoProcs = wrapper.getAutoProcs();

			for (int i = 0; i < nb; i++) {

				DataCollection3VO dataCollection = dataCollectionList.get(i);
				AutoProcRankingVO autoProcRankingValue = new AutoProcRankingVO();

				AutoProc3VO autoProc = autoProcs[i];
				AutoProcScalingStatistics3VO apssv_overall = apssService.getBestAutoProcScalingStatistic(apssService.findByAutoProcId(
						autoProc.getAutoProcId(), "overall"));

				// Set dataCollectionId
				autoProcRankingValue.setDataCollectionId(dataCollection.getDataCollectionId());
				autoProcRankingValue.setImagePrefix(dataCollection.getImagePrefix());
				autoProcRankingValue.setDataCollectionNumber(dataCollection.getDataCollectionNumber());
				autoProcRankingValue.setStartTime(StringUtils.dateToTimestamp(dataCollection.getStartTime()));
				// Not ranked values
				if (autoProc != null) {
					autoProcRankingValue.setSpaceGroup(autoProc.getSpaceGroup());
					autoProcRankingValue.setUnitCell_a(autoProc.getRefinedCellA());
					autoProcRankingValue.setUnitCell_b(autoProc.getRefinedCellB());
					autoProcRankingValue.setUnitCell_c(autoProc.getRefinedCellC());
				}
				// Overall RFactor
				try {
					autoProcRankingValue.setOverallRFactorValue(apssv_overall.getRmerge());
				} catch (Exception e) {
					autoProcRankingValue.setOverallRFactorValue(null);
				}

				// Highest resolution
				try {
					autoProcRankingValue.setHighestResolutionValue(apssv_overall.getResolutionLimitHigh());
				} catch (Exception e) {
					autoProcRankingValue.setHighestResolutionValue(null);
				}

				// Completeness
				try {
					autoProcRankingValue.setCompletenessValue(apssv_overall.getCompleteness());
				} catch (Exception e) {
					autoProcRankingValue.setCompletenessValue(null);
				}

				// Add to result list
				autoProcRankingValues.add(autoProcRankingValue);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return autoProcRankingValues;
	}

	/**
	 * Weight matrix
	 * 
	 * @param weightVector
	 * @param decisionMatrix
	 */
	private void weightMatrix(Float[] weightVector, Float[][] decisionMatrix) {

		for (int j = 0; j < decisionMatrix[0].length; j++) {
			for (int i = 0; i < decisionMatrix.length; i++) {
				if (decisionMatrix[i][j] != null)
					decisionMatrix[i][j] = decisionMatrix[i][j] * weightVector[j];
			}
		}
	}

	/**
	 * Maximize decision matrix
	 * 
	 * @param sampleRankingValues
	 * @return
	 */
	private void maximizeMatrix(Float[][] decisionMatrix) {
		maximizeColumn(decisionMatrix, CRITERION_COL_OverallRFactor);
		maximizeColumn(decisionMatrix, CRITERION_COL_HighestResolution);
		maximizeColumn(decisionMatrix, CRITERION_COL_Completeness);
	}

	/**
	 * Maximize column
	 * 
	 * @param matrix
	 * @param numColumn
	 */
	private void maximizeColumn(Float[][] decisionMatrix, int numColumn) {

		Float maxVal = null;
		for (int i = 0; i < decisionMatrix.length; i++) {
			if (decisionMatrix[i][numColumn] != null) {
				if (maxVal == null)
					maxVal = decisionMatrix[i][numColumn];
				if (decisionMatrix[i][numColumn] > maxVal)
					maxVal = decisionMatrix[i][numColumn];
			}
		}
		for (int i = 0; i < decisionMatrix.length; i++) {
			if (decisionMatrix[i][numColumn] != null && decisionMatrix[i][numColumn] != 0) {
				// SAW
				// decisionMatrix[i][numColumn] = maxVal - decisionMatrix[i][numColumn];
				// TODO This one is more accurate for top ranked samples
				decisionMatrix[i][numColumn] = maxVal / decisionMatrix[i][numColumn];
			}
		}
	}

	/**
	 * Normalize Matrix
	 * 
	 * @param decisionMatrix
	 */
	private void normalizeMatrix(Float[][] decisionMatrix) {

		for (int j = 0; j < decisionMatrix[0].length; j++) {
			Float maxVal = null;
			Float minVal = null;
			for (int i = 0; i < decisionMatrix.length; i++) {
				if (decisionMatrix[i][j] != null) {
					if (maxVal == null)
						maxVal = decisionMatrix[i][j];
					if (minVal == null)
						minVal = decisionMatrix[i][j];
					if (decisionMatrix[i][j] > maxVal)
						maxVal = decisionMatrix[i][j];
					if (decisionMatrix[i][j] < minVal)
						minVal = decisionMatrix[i][j];
				}
			}
			for (int i = 0; i < decisionMatrix.length; i++) {
				if (decisionMatrix[i][j] != null)
					// SAW
					// decisionMatrix[i][j] = (decisionMatrix[i][j] - minVal) / (maxVal - minVal);
					// TODO This one as better accuracy for poor samples
					decisionMatrix[i][j] = decisionMatrix[i][j] / maxVal;
			}
		}
	}

	/**
	 * Build weight vector
	 * 
	 * @return
	 */
	private Float[] buildWeightVector() {

		Float weightedCoef[] = new Float[NB_CRITERION];
		weightedCoef[0] = criteriaVO.getWeightOverallRFactor().floatValue();
		weightedCoef[1] = criteriaVO.getWeightHighestResolution().floatValue();
		weightedCoef[2] = criteriaVO.getWeightCompleteness().floatValue();
		Float coef = new Float(0);
		for (int j = 0; j < NB_CRITERION; j++) {
			coef += weightedCoef[j];
		}
		for (int j = 0; j < NB_CRITERION; j++) {
			weightedCoef[j] = weightedCoef[j] / coef;
		}

		return weightedCoef;
	}

	/**
	 * Rank total
	 */
	private static void rankTotal(List<AutoProcRankingVO> autoProcRankingValues) {
		Float previousVal = null;
		int currentRank = 1;
		Collections.sort(autoProcRankingValues, AutoProcRankingComparator.ORDER_SCORE_Total_DESC);
		for (int i = 0; i < autoProcRankingValues.size(); i++) {
			Float currentVal = autoProcRankingValues.get(i).getTotalScore();
			if (previousVal != null && currentVal != null && !currentVal.equals(previousVal))
				currentRank++;
			else if (previousVal != null && currentVal == null)
				currentRank++;
			autoProcRankingValues.get(i).setTotalRank(currentRank);
			previousVal = currentVal;
		}
	}

	/**
	 * Build decision matrix
	 * 
	 * @param sampleRankingValues
	 * @return
	 */
	private Float[][] buildDecisionMatrix(List<AutoProcRankingVO> autoProcRankingValues) {

		Float decisionMatrix[][] = new Float[autoProcRankingValues.size()][NB_CRITERION];
		for (int i = 0; i < autoProcRankingValues.size(); i++) {
			AutoProcRankingVO autoProcRankingVO = autoProcRankingValues.get(i);
			Float overallRFactorValue = null;
			Float highestResolutionValue = null;
			Float completenessValue = null;

			if (autoProcRankingVO.getOverallRFactorValue() != null)
				overallRFactorValue = autoProcRankingVO.getOverallRFactorValue();
			if (autoProcRankingVO.getHighestResolutionValue() != null)
				highestResolutionValue = autoProcRankingVO.getHighestResolutionValue();
			if (autoProcRankingVO.getCompletenessValue() != null)
				completenessValue = autoProcRankingVO.getCompletenessValue();

			decisionMatrix[i][CRITERION_COL_OverallRFactor] = overallRFactorValue;
			decisionMatrix[i][CRITERION_COL_HighestResolution] = highestResolutionValue;
			decisionMatrix[i][CRITERION_COL_Completeness] = completenessValue;
		}

		return decisionMatrix;
	}

	/**
	 * Set total score
	 * 
	 * @param autoProcRankingValues
	 * @param decisionMatrix
	 */
	private void setScore(List<AutoProcRankingVO> autoProcRankingValues, Float[][] decisionMatrix) {

		for (int i = 0; i < decisionMatrix.length; i++) {
			Float value = new Float(0.0);
			for (int j = 0; j < decisionMatrix[i].length; j++) {
				if (decisionMatrix[i][j] != null)
					value += decisionMatrix[i][j];
			}
			autoProcRankingValues.get(i).setTotalScore(value * 100);
			if (decisionMatrix[i][CRITERION_COL_OverallRFactor] != null)
				autoProcRankingValues.get(i).setOverallRFactorScore(decisionMatrix[i][CRITERION_COL_OverallRFactor] * 100);
			else
				autoProcRankingValues.get(i).setOverallRFactorScore((float) 0);
			if (decisionMatrix[i][CRITERION_COL_HighestResolution] != null)
				autoProcRankingValues.get(i).setHighestResolutionScore(decisionMatrix[i][CRITERION_COL_HighestResolution] * 100);
			else
				autoProcRankingValues.get(i).setHighestResolutionScore((float) 0);
			if (decisionMatrix[i][CRITERION_COL_Completeness] != null)
				autoProcRankingValues.get(i).setCompletenessScore(decisionMatrix[i][CRITERION_COL_Completeness] * 100);
			else
				autoProcRankingValues.get(i).setCompletenessScore((float) 0.0);

		}
	}

	/**
	 * Round Double according to format
	 * 
	 * @param d
	 * @param format
	 * @return
	 */
	private static Float roundFloat(Float d, String format) {
		if (d == null)
			return null;
		Locale.setDefault(Locale.US);
		DecimalFormat doubleForm = new DecimalFormat(format);
		return Float.valueOf(doubleForm.format(d));
	}

	/**
	 * Rank by OverallRFactor
	 * 
	 * @param autoProcRankingValues
	 */
	private static void rankOverallRFactor(List<AutoProcRankingVO> autoProcRankingValues) {
		Float previousVal = null;
		int currentRank = 1;
		Collections.sort(autoProcRankingValues, AutoProcRankingComparator.ORDER_VALUE_OverallRFactor_ASC);
		for (int i = 0; i < autoProcRankingValues.size(); i++) {
			Float currentVal = roundFloat(autoProcRankingValues.get(i).getOverallRFactorValue(), "#.##");
			if (previousVal != null && currentVal != null && !currentVal.equals(previousVal))
				currentRank++;
			else if (previousVal != null && currentVal == null)
				currentRank++;
			autoProcRankingValues.get(i).setOverallRFactorRank(currentRank);
			previousVal = currentVal;
		}
	}

	/**
	 * Rank by HighestResolution
	 * 
	 * @param autoProcRankingValues
	 */
	private static void rankHighestResolution(List<AutoProcRankingVO> autoProcRankingValues) {
		Float previousVal = null;
		int currentRank = 1;
		Collections.sort(autoProcRankingValues, AutoProcRankingComparator.ORDER_VALUE_HighestResolution_ASC);
		for (int i = 0; i < autoProcRankingValues.size(); i++) {
			Float currentVal = roundFloat(autoProcRankingValues.get(i).getHighestResolutionValue(), "#.##");
			if (previousVal != null && currentVal != null && !currentVal.equals(previousVal))
				currentRank++;
			else if (previousVal != null && currentVal == null)
				currentRank++;
			autoProcRankingValues.get(i).setHighestResolutionRank(currentRank);
			previousVal = currentVal;
		}
	}

	/**
	 * Rank by Completeness
	 * 
	 * @param autoProcRankingValues
	 */
	private static void rankCompleteness(List<AutoProcRankingVO> autoProcRankingValues) {
		Float previousVal = null;
		int currentRank = 1;
		Collections.sort(autoProcRankingValues, AutoProcRankingComparator.ORDER_VALUE_Completeness_ASC);
		for (int i = 0; i < autoProcRankingValues.size(); i++) {
			Float currentVal = roundFloat(autoProcRankingValues.get(i).getCompletenessValue(), "#.##");
			if (previousVal != null && currentVal != null && !currentVal.equals(previousVal))
				currentRank++;
			else if (previousVal != null && currentVal == null)
				currentRank++;
			autoProcRankingValues.get(i).setCompletenessRank(currentRank);
			previousVal = currentVal;
		}
	}

}
