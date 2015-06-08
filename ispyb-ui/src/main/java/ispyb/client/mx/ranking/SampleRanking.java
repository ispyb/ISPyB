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
 * SampleRanking.java
 * @author patrice.brenchereau@esrf.fr
 * March 09, 2009
 * 
 * This is an example...
 * 
 */

package ispyb.client.mx.ranking;

import ispyb.client.mx.ranking.SampleScreeningVO.ScreeningOutputLatticeVO;
import ispyb.client.mx.ranking.SampleScreeningVO.ScreeningOutputVO;
import ispyb.client.mx.ranking.SampleScreeningVO.ScreeningStrategyVO;
import ispyb.client.mx.ranking.SampleScreeningVO.ScreeningVO;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class SampleRanking {
	
	private final static int NB_CRITERION = 5;
	
	private final static int CRITERION_COL_TheoreticalResolution = 0;
	private final static int CRITERION_COL_TotalExposureTime = 1;
	private final static int CRITERION_COL_Mosaicity= 2;
	private final static int CRITERION_COL_NumberOfSpotsIndexed = 3;
	private final static int CRITERION_COL_NumberOfImages = 4;
	
	/*
	 * Weights
	 */
	private SampleRankingCriteriaVO criteriaVO = null;
	
	/**
	 * Set weights
	 * @param criteriaVO
	 */
	public void setWeights(SampleRankingCriteriaVO criteriaVO)  {
		
		this.criteriaVO = criteriaVO;
	}
	
	/**
	 * Rank samples 
	 * @param sampleRankingValues
	 */
	public List<SampleRankingVO>  rank(List<SampleScreeningVO> sampleScreeningVOList)  {
		
		// Extract values from SampleScreening
		List<SampleRankingVO> sampleRankingValues = getSampleScreeningValues(sampleScreeningVOList);

        // Rank each criterion
        rankTheoreticalResolution(sampleRankingValues);
        rankTotalExposureTime(sampleRankingValues);
        rankMosaicity(sampleRankingValues);
        rankNumberOfSpotsIndexed(sampleRankingValues);
        rankNumberOfImages(sampleRankingValues);
        
        // Decision matrix
        Double decisionMatrix [][] = buildDecisionMatrix(sampleRankingValues);
        //plotMatrix("Init",decisionMatrix, "0000.00");
        maximizeMatrix(decisionMatrix);
        //plotMatrix("Maximized",decisionMatrix, "0000.00");
        normalizeMatrix(decisionMatrix);
        //plotMatrix("Normalized",decisionMatrix, "0.000");
        Double [] weightVector = buildWeightVector();
        weightMatrix(weightVector,decisionMatrix);
        //plotMatrix("Weighted",decisionMatrix, "0.000");
        setScore(sampleRankingValues, decisionMatrix);
        
        // Rank total
        rankTotal(sampleRankingValues);
        //plotScore("Ranked", sampleRankingValues);
        
		return sampleRankingValues;           
	}
	
	/**
	 * @param sampleScreeningVOList
	 * @return
	 */
	public List<SampleRankingVO>  getSampleScreeningValues(List<SampleScreeningVO> sampleScreeningVOList)  {
		
		List<SampleRankingVO> sampleRankingValues = new ArrayList<SampleRankingVO>();

		// Get SampleScreeningVO values and populate SampleRankingVO values
		for ( int i=0; i<sampleScreeningVOList.size(); i++) {

			SampleScreeningVO sampleScreeningVO = sampleScreeningVOList.get(i);
			SampleRankingVO sampleRankingValue = new SampleRankingVO();
			
			// Set dataCollectionId
			sampleRankingValue.setDataCollectionId(sampleScreeningVO.getDataCollectionId());
			
			// Take the last ScreeningVO and ScreeningStrategyVO (assume that they are the best...)
			ScreeningVO screeningVO =  null;
			ScreeningOutputVO screeningOutputVO =  null;
			ScreeningOutputLatticeVO screeningOutputLatticeVO = null;
			ScreeningStrategyVO screeningStrategyVO = null;
			int j;
			// Get last ScreeningVO that has ScreeningVO.ScreeningOutput.screeningSuccess == 1
			if ( sampleScreeningVO != null && sampleScreeningVO.getScreeningVOList() != null ) {
				j = sampleScreeningVO.getScreeningVOList().size()-1;
				while ( screeningVO == null && j >=0 ) {
					ScreeningVO screeningVOtmp = sampleScreeningVO.getScreeningVOList().get(j);
					if ( screeningVOtmp.getScreeningOutputVO() != null && screeningVOtmp.getScreeningOutputVO().getIndexingSuccess() == 1 ) 
						screeningVO = screeningVOtmp;
					j = j-1;
				}
			}
			// Get ScreeningOutputVO
			if ( screeningVO != null && screeningVO.getScreeningOutputVO() != null ) 
				screeningOutputVO = screeningVO.getScreeningOutputVO();
			// Get ScreeningOutputLattice
			if ( screeningOutputVO != null ) 
				screeningOutputLatticeVO = screeningOutputVO.getScreeningOutputLatticeVO();
			// Get last ScreeningStrategyVO that has ScreeningStrategyVO.program="BEST*"
			if ( screeningOutputVO != null && screeningOutputVO.getScreeningStrategyVOList() != null ) {
				j = screeningOutputVO.getScreeningStrategyVOList().size()-1;
				while ( screeningStrategyVO == null && j >=0 ) {
					ScreeningStrategyVO screeningStrategyVOtmp = screeningOutputVO.getScreeningStrategyVOList().get(j);
					if ( screeningStrategyVOtmp != null && screeningStrategyVOtmp.getProgram() != null && screeningStrategyVOtmp.getProgram().startsWith("BEST")  ) 
						screeningStrategyVO = screeningStrategyVOtmp;
					j = j-1;
				}
			}
			
			// RankingResolution
			try {
				sampleRankingValue.setTheoreticalResolutionValue(screeningOutputVO.getRankingResolution());
			}
			catch (Exception e) {
				sampleRankingValue.setTheoreticalResolutionValue(null);
		    }

			// NumberOfImages and ExposureTime
			try {
				NumberFormat  nf1 = NumberFormat.getIntegerInstance();
				DecimalFormat df1 = (DecimalFormat)NumberFormat.getInstance(Locale.US);
			    df1.applyPattern("#####0.0");	   
			    
				Double nbImagesdb = null;
				Double rotation = screeningStrategyVO.getRotation();
				Double phiS		= screeningStrategyVO.getPhiStart();
				Double phiE		= screeningStrategyVO.getPhiEnd();
				Double exposureT= screeningStrategyVO.getExposureTime();
				Double newExposureT = screeningOutputVO.getTotalExposureTime();
				Integer newNumberOfImages = screeningOutputVO.getTotalNumberOfImages();

				// NumberOfImages: now totalNumberOfImages is recorded in ScreeningOutput table
				// for existing data, we keep the old number of images calculation (ScreeningStrategy) 
				Integer totNbImages = null;
				if(newNumberOfImages != null)
					totNbImages = newNumberOfImages ;
				else{
					if (phiE != null && phiS != null && rotation != null && rotation.compareTo(new Double(0))!=0) {
						nbImagesdb = (phiE - phiS) / rotation;
						totNbImages = new Integer(nf1.format(nbImagesdb));
					}
				}
				sampleRankingValue.setNumberOfImagesValue(totNbImages);
				
				// ExposureTime: now totalExposureTime is recorded in ScreeningOutput table
				// for existing data, we keep the old exposure time calculation (ScreeningStrategy) 
				Double totExposureTime = null;
				if (newExposureT != null)
					totExposureTime = new Double(df1.format(newExposureT));
				if (exposureT != null) {
					totExposureTime = new Double(df1.format(nbImagesdb * exposureT));
				}
				sampleRankingValue.setTotalExposureTimeValue(totExposureTime);
			}
			catch (Exception e) {
				sampleRankingValue.setNumberOfImagesValue(null);
				sampleRankingValue.setTotalExposureTimeValue(null);
		    }
			
			// NumberOfSpotsIndexedValue
			try {
				int numberOfSpotsIndexed = screeningOutputVO.getNumSpotsFound();
				sampleRankingValue.setNumberOfSpotsIndexedValue(numberOfSpotsIndexed);
			}
			catch (Exception e) {
				sampleRankingValue.setNumberOfSpotsIndexedValue(null);
		    }
			
			// Mosaicity
			try {
				double mosaicity = screeningOutputVO.getMosaicity();
				sampleRankingValue.setMosaicityValue(mosaicity);	
			}
			catch (Exception e) {
				sampleRankingValue.setMosaicityValue(null);
		    }
			
			// Not ranked values
			if ( screeningOutputLatticeVO != null ) {
				sampleRankingValue.setSpaceGroup(screeningOutputLatticeVO.getSpaceGroup());
				sampleRankingValue.setUnitCell_a(screeningOutputLatticeVO.getUnitCellA());
				sampleRankingValue.setUnitCell_b(screeningOutputLatticeVO.getUnitCellB());
				sampleRankingValue.setUnitCell_c(screeningOutputLatticeVO.getUnitCellC());	
			}

			// Save selected VOs
			sampleRankingValue.setValue_ScreeningVO(screeningVO);
			sampleRankingValue.setValue_ScreeningOutputVO(screeningOutputVO);
			sampleRankingValue.setValue_ScreeningOutputLatticeVO(screeningOutputLatticeVO);
			sampleRankingValue.setValue_ScreeningStrategyVO(screeningStrategyVO);
			
			// Add to result list
			sampleRankingValues.add(sampleRankingValue);
		}
        
		return sampleRankingValues;           
	}
	
	/**
	 * Rank by TheoreticalResolution
	 * @param sampleRankingValues
	 */
	private static void rankTheoreticalResolution (List <SampleRankingVO> sampleRankingValues) {

		Double previousVal = null;
        int currentRank = 1;
	    Collections.sort(sampleRankingValues,SampleRankingComparator.ORDER_VALUE_TheoreticalResolution_ASC);
	    for (int i = 0; i < sampleRankingValues.size(); i++) {
	    	Double currentVal = roundDouble(sampleRankingValues.get(i).getTheoreticalResolutionValue(),"#.##");
	    	if ( previousVal != null && currentVal != null && !currentVal.equals(previousVal) ) currentRank ++;
	    	else if ( previousVal != null && currentVal == null ) currentRank ++;
	    	sampleRankingValues.get(i).setTheoreticalResolutionRank(currentRank);
	    	previousVal = currentVal;
	    }
	}
	
	/**
	 * Rank by TotalExposureTime
	 * @param sampleRankingValues
	 */
	private static void rankTotalExposureTime (List <SampleRankingVO> sampleRankingValues) {
		
		Double previousVal = null;
        int currentRank = 1;
        Collections.sort(sampleRankingValues,SampleRankingComparator.ORDER_VALUE_TotalExposureTime_ASC);
        for (int i = 0; i < sampleRankingValues.size(); i++) {
	    	Double currentVal = roundDouble(sampleRankingValues.get(i).getTotalExposureTimeValue(),"#.##");
	    	if ( previousVal != null && currentVal != null && !currentVal.equals(previousVal) ) currentRank ++;
	    	else if ( previousVal != null && currentVal == null ) currentRank ++;
        	sampleRankingValues.get(i).setTotalExposureTimeRank(currentRank);
        	previousVal = currentVal;
        }
	}
	
	/**
	 * Rank by Mosaicity
	 * @param sampleRankingValues
	 */
	private static void rankMosaicity (List <SampleRankingVO> sampleRankingValues) {

		Double previousVal = null;
        int currentRank = 1;
        Collections.sort(sampleRankingValues,SampleRankingComparator.ORDER_VALUE_Mosaicity_ASC);
        for (int i = 0; i < sampleRankingValues.size(); i++) {
        	Double currentVal = roundDouble(sampleRankingValues.get(i).getMosaicityValue(),"#.##");
        	if ( previousVal != null && currentVal != null && !currentVal.equals(previousVal) ) currentRank ++;
        	else if ( previousVal != null && currentVal == null ) currentRank ++;
        	sampleRankingValues.get(i).setMosaicityRank(currentRank);
        	previousVal = currentVal;
        }
	}
	
	/**
	 * Rank by NumberOfSpotsIndexed
	 * @param sampleRankingValues
	 */
	private static void rankNumberOfSpotsIndexed (List <SampleRankingVO> sampleRankingValues) {

		Double previousVal = null;
        int currentRank = 1;
        Collections.sort(sampleRankingValues,SampleRankingComparator.ORDER_VALUE_NumberOfSpotsIndexed_DESC);
        for (int i = 0; i < sampleRankingValues.size(); i++) {
        	Double currentVal = null;
        	if ( sampleRankingValues.get(i).getNumberOfImagesValue() != null ) currentVal = sampleRankingValues.get(i).getNumberOfImagesValue().doubleValue();
        	if ( previousVal != null && currentVal != null && !currentVal.equals(previousVal) ) currentRank ++;
        	else if ( previousVal != null && currentVal == null ) currentRank ++;
        	sampleRankingValues.get(i).setNumberOfSpotsIndexedRank(currentRank);
        	previousVal = currentVal;
        }
	}
	
	/**
	 * Rank by NumberOfImages
	 * @param sampleRankingValues
	 */
	private static void rankNumberOfImages (List <SampleRankingVO> sampleRankingValues) {
		
		Double previousVal = null;
        int currentRank = 1;
        Collections.sort(sampleRankingValues,SampleRankingComparator.ORDER_VALUE_NumberOfImages_ASC);
        for (int i = 0; i < sampleRankingValues.size(); i++) {
        	Double currentVal = null;
        	if ( sampleRankingValues.get(i).getNumberOfImagesValue() != null ) currentVal = sampleRankingValues.get(i).getNumberOfImagesValue().doubleValue();
        	if ( previousVal != null && currentVal != null && !currentVal.equals(previousVal) ) currentRank ++;
        	else if ( previousVal != null && currentVal == null ) currentRank ++;
        	sampleRankingValues.get(i).setNumberOfImagesRank(currentRank);
        	previousVal = currentVal;
        }
	}
	
	/**
	 * Rank total
	 * @param sampleRankingValues
	 */
	private static void rankTotal(List <SampleRankingVO> sampleRankingValues) {
		
		Double previousVal = null;
        int currentRank = 1;
        Collections.sort(sampleRankingValues,SampleRankingComparator.ORDER_SCORE_Total_DESC);
        for (int i = 0; i < sampleRankingValues.size(); i++) {
        	Double currentVal = sampleRankingValues.get(i).getTotalScore();
        	if ( previousVal != null && currentVal != null && !currentVal.equals(previousVal) ) currentRank ++;
        	else if ( previousVal != null && currentVal == null ) currentRank ++;
        	sampleRankingValues.get(i).setTotalRank(currentRank);
        	previousVal = currentVal;
        }
	}
	
	/**
	 * @param sampleRankingValues
	 */
	private static void plotScore(String message, List <SampleRankingVO> sampleRankingValues) {
		
		String trace = "";;
		trace += message+"\n";
		int scaleFactor = 10;
        for (int i = 0; i < sampleRankingValues.size(); i++) {
        	SampleRankingVO sampleRankingVO = sampleRankingValues.get(i);
        	trace += "["+i+"]";
        	long rankingResolution = Math.round(sampleRankingVO.getTheoreticalResolutionScore()*scaleFactor);
			long exposureTime = Math.round(sampleRankingVO.getTotalExposureTimeScore()*scaleFactor);
			long mosaicity = Math.round(sampleRankingVO.getMosaicityScore()*scaleFactor);
			long numberOfSpots = Math.round(sampleRankingVO.getNumberOfSpotsIndexedScore()*scaleFactor);
			long numberOfImages = Math.round(sampleRankingVO.getNumberOfImagesScore()*scaleFactor);
        	
        	trace += rankingResolution+ " ";
        	trace += exposureTime + " ";
        	trace += mosaicity + " ";
        	trace += numberOfSpots + " ";
        	trace += numberOfImages + " ";
        	trace += "\n";
        }
        System.out.println(trace);
	}
	
	
	
	/**
	 * Build decision matrix
	 * @param sampleRankingValues
	 * @return
	 */
	private Double [][] buildDecisionMatrix (List <SampleRankingVO> sampleRankingValues) {
		
		Double decisionMatrix [][] = new Double[sampleRankingValues.size()][NB_CRITERION];
        for (int i = 0; i < sampleRankingValues.size(); i++) {
        	SampleRankingVO sampleRankingVO = sampleRankingValues.get(i);
        	Double theoreticalResolutionValue = null;
        	Double totalExposureTimeValue = null;
        	Double mosaicityValue = null;
        	Double numberOfSpotsIndexedValue = null;
        	Double numberOfImagesValue = null;
        	
        	if (sampleRankingVO.getTheoreticalResolutionValue() != null) theoreticalResolutionValue = sampleRankingVO.getTheoreticalResolutionValue();
        	if (sampleRankingVO.getTotalExposureTimeValue() != null) totalExposureTimeValue = sampleRankingVO.getTotalExposureTimeValue();
        	if (sampleRankingVO.getMosaicityValue() != null) mosaicityValue = sampleRankingVO.getMosaicityValue();
        	if (sampleRankingVO.getNumberOfSpotsIndexedValue() != null) numberOfSpotsIndexedValue = sampleRankingVO.getNumberOfSpotsIndexedValue().doubleValue();
        	if (sampleRankingVO.getNumberOfImagesValue() != null) numberOfImagesValue = sampleRankingVO.getNumberOfImagesValue().doubleValue();
        	
        	decisionMatrix [i][CRITERION_COL_TheoreticalResolution] = theoreticalResolutionValue;
        	decisionMatrix [i][CRITERION_COL_TotalExposureTime] 	= totalExposureTimeValue;
        	decisionMatrix [i][CRITERION_COL_Mosaicity] 			= mosaicityValue;
        	decisionMatrix [i][CRITERION_COL_NumberOfSpotsIndexed] 	= numberOfSpotsIndexedValue;
        	decisionMatrix [i][CRITERION_COL_NumberOfImages] 		= numberOfImagesValue;
        }
        
        return decisionMatrix;
	}
	
	/**
	 * Maximize decision matrix
	 * @param sampleRankingValues
	 * @return
	 */
	private void maximizeMatrix (Double [][] decisionMatrix) {      
	    
	    maximizeColumn(decisionMatrix,CRITERION_COL_TheoreticalResolution);
	    maximizeColumn(decisionMatrix,CRITERION_COL_TotalExposureTime);
	    maximizeColumn(decisionMatrix,CRITERION_COL_Mosaicity);
	    // Already maximized maximizeColumn(decisionMatrix,CRITERION_COL_NumberOfSpotsIndexed);
	    maximizeColumn(decisionMatrix,CRITERION_COL_NumberOfImages);
	}
	
	/**
	 * Maximize column
	 * @param matrix
	 * @param numColumn
	 */
	private void maximizeColumn (Double [][] decisionMatrix, int numColumn) {      

	    Double maxVal = null;
    	for (int i = 0; i < decisionMatrix.length; i++) {
    		if ( decisionMatrix[i][numColumn] != null ) {
	    		if ( maxVal == null ) maxVal = decisionMatrix[i][numColumn];
	    		if ( decisionMatrix[i][numColumn] > maxVal ) maxVal = decisionMatrix[i][numColumn];
    		}
    	}
    	for (int i = 0; i < decisionMatrix.length; i++) {
    		if ( decisionMatrix[i][numColumn] != null && decisionMatrix[i][numColumn] != 0 ) {
    			// SAW
    			//decisionMatrix[i][numColumn] = maxVal - decisionMatrix[i][numColumn];
    			// TODO This one is more accurate for top ranked samples
    			decisionMatrix[i][numColumn] = maxVal / decisionMatrix[i][numColumn];
    		}
    	}
	}
	
	/**
	 * Normalize Matrix
	 * @param decisionMatrix
	 */
	private void normalizeMatrix (Double [][] decisionMatrix) {      
        
	    for (int j = 0; j < decisionMatrix[0].length; j++) {
	    	Double maxVal = null;
	    	Double minVal = null;
	    	for (int i = 0; i < decisionMatrix.length; i++) {
	    		if ( decisionMatrix[i][j] != null ) {
		    		if ( maxVal == null ) maxVal = decisionMatrix[i][j];
		    		if ( minVal == null ) minVal = decisionMatrix[i][j];
		    		if ( decisionMatrix[i][j] > maxVal ) maxVal = decisionMatrix[i][j];
		    		if ( decisionMatrix[i][j] < minVal ) minVal = decisionMatrix[i][j];
	    		}
        	}
	    	for (int i = 0; i < decisionMatrix.length; i++) {
	    		if ( decisionMatrix[i][j] != null )
	    			// SAW
	    			// decisionMatrix[i][j] = (decisionMatrix[i][j] - minVal) / (maxVal - minVal);
	    			// TODO This one as better accuracy for poor samples
	    			decisionMatrix[i][j] = decisionMatrix[i][j] /  maxVal ;
        	}
        }
	}
	
	/**
	 * Build weight vector
	 * @return
	 */
	private Double [] buildWeightVector () {

	    Double weightedCoef[] = new Double [NB_CRITERION];
	    weightedCoef[0] = criteriaVO.getWeightRankingResolution().doubleValue();
	    weightedCoef[1] = criteriaVO.getWeightExposureTime().doubleValue();
	    weightedCoef[2] = criteriaVO.getWeightMosaicity().doubleValue();
	    weightedCoef[3] = criteriaVO.getWeightNumberOfSpots().doubleValue();
	    weightedCoef[4] = criteriaVO.getWeightNumberOfImages().doubleValue();
	    Double coef = new Double(0);
	    for (int j = 0; j < NB_CRITERION; j++) {
	    	coef += weightedCoef[j];
	    }
	    for (int j = 0; j < NB_CRITERION; j++) {
	    	weightedCoef[j]= weightedCoef[j] / coef;
	    }
	    
	    return weightedCoef;
	}
	
	
	/**
	 * Weight matrix
	 * @param weightVector
	 * @param decisionMatrix
	 */
	private void weightMatrix(Double [] weightVector,Double [][]decisionMatrix) {
		
		for (int j = 0; j < decisionMatrix[0].length; j++) {
	    	for (int i = 0; i < decisionMatrix.length; i++) {
	    		if ( decisionMatrix[i][j] != null )
	    			decisionMatrix[i][j] = decisionMatrix[i][j] * weightVector[j];
        	}
        }
	}
	
	/**
	 * Set total score
	 * @param sampleRankingValues
	 * @param decisionMatrix
	 */
	private void setScore(List <SampleRankingVO> sampleRankingValues, Double [][] decisionMatrix) {
		
		for (int i = 0; i < decisionMatrix.length; i++) {
			Double value = new Double(0.0);
        	for (int j = 0; j < decisionMatrix[i].length; j++) {
        		if ( decisionMatrix[i][j] != null )
        			value += decisionMatrix[i][j];	
        	}
        	sampleRankingValues.get(i).setTotalScore(value*100);
        	if ( decisionMatrix [i][CRITERION_COL_TheoreticalResolution] != null )
        		sampleRankingValues.get(i).setTheoreticalResolutionScore(decisionMatrix [i][CRITERION_COL_TheoreticalResolution]*100);
        	else
        		sampleRankingValues.get(i).setTheoreticalResolutionScore(0.0);
        	if ( decisionMatrix [i][CRITERION_COL_TotalExposureTime] != null )
        		sampleRankingValues.get(i).setTotalExposureTimeScore(decisionMatrix [i][CRITERION_COL_TotalExposureTime]*100);
        	else
        		sampleRankingValues.get(i).setTotalExposureTimeScore(0.0);
        	if ( decisionMatrix [i][CRITERION_COL_Mosaicity] != null )
        		sampleRankingValues.get(i).setMosaicityScore(decisionMatrix [i][CRITERION_COL_Mosaicity]*100);
        	else
        		sampleRankingValues.get(i).setMosaicityScore(0.0);
        	if ( decisionMatrix [i][CRITERION_COL_NumberOfSpotsIndexed] != null )
        		sampleRankingValues.get(i).setNumberOfSpotsIndexedScore(decisionMatrix [i][CRITERION_COL_NumberOfSpotsIndexed]*100);
        	else
        		sampleRankingValues.get(i).setNumberOfSpotsIndexedScore(0.0);
        	if ( decisionMatrix [i][CRITERION_COL_NumberOfImages] != null )
        		sampleRankingValues.get(i).setNumberOfImagesScore(decisionMatrix [i][CRITERION_COL_NumberOfImages]*100);
        	else
        		sampleRankingValues.get(i).setNumberOfImagesScore(0.0);
        }
	}
	
	/**
	 * Plot matrix
	 * @param decisionMatrix
	 * @param format
	 */
	private void plotMatrix (String message, Double decisionMatrix [][], String format) {
		
	    DecimalFormat df1 = (DecimalFormat)NumberFormat.getInstance(Locale.US);
	    df1.applyPattern(format);
	    
	    System.out.println(message);
        for (int i = 0; i < decisionMatrix.length; i++) {
        	String str = "";
        	for (int j = 0; j < decisionMatrix[i].length; j++) {
        		if ( decisionMatrix[i][j] == null ) str += "<null> ";
        		else str += df1.format(decisionMatrix[i][j])+" ";
        	}
        	System.out.println("["+i+"] "+str);
        }
	}
	
	
	/**
	 * Round Double according to format
	 * @param d
	 * @param format
	 * @return
	 */
	private static Double roundDouble(Double d, String format) {
		if ( d == null ) return null;
		Locale.setDefault(Locale.US);
		DecimalFormat doubleForm = new DecimalFormat(format);
    	return Double.valueOf(doubleForm.format(d));
	}
	
	/**
	 * For developing, testing and debugging
	 * @param filePath
	 * @param sampleScreeningVOList
	 */
	public static void writeSampleScreeningVOList(String filePath, List<SampleScreeningVO> sampleScreeningVOList) {
		
		System.out.println("writeSampleScreeningVOList to file "+filePath);
		try {
			FileOutputStream fstream = new FileOutputStream(filePath);
			try {
	            XMLEncoder ostream = new XMLEncoder(fstream);
	            try {
	            	ostream.writeObject(sampleScreeningVOList);
	            	ostream.flush();
	            } finally {
	            	ostream.close();
	            }
	        } 
			finally {
	            fstream.close();
	        }
	    } 
		catch(Exception ex) {
	         ex.printStackTrace();
	    }
		
	}
	
	/**
	 * For developing, testing and debugging
	 * @param filePath
	 * @return
	 */
	public static List<SampleScreeningVO> readSampleScreeningVOList(String filePath) {
		
		System.out.println("readSampleScreeningVOList from file "+filePath);
		List<SampleScreeningVO> sampleScreeningVOList=null;
	    try {
		  FileInputStream fos = new FileInputStream(filePath);
		  XMLDecoder xdec = new XMLDecoder(fos);
		  sampleScreeningVOList = (List<SampleScreeningVO>) xdec.readObject();    
	    } 
	    catch(Exception ex) {
	    	ex.printStackTrace();
	    }
		
		return sampleScreeningVOList;
		
	}
	
	/**
	 * For testing and debuging
	 * @param args
	 */
	public static void main(String[] args) {

	}
	
}
