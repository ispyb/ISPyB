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

package ispyb.server.biosaxs.services.core.analysis.primaryDataProcessing;


import ispyb.server.biosaxs.services.core.measurementToDataCollection.MeasurementToDataCollection3Service;
import ispyb.server.biosaxs.services.sql.SQLQueryKeeper;
import ispyb.server.biosaxs.vos.dataAcquisition.Measurement3VO;
import ispyb.server.biosaxs.vos.datacollection.Frame3VO;
import ispyb.server.biosaxs.vos.datacollection.Framelist3VO;
import ispyb.server.biosaxs.vos.datacollection.Frametolist3VO;
import ispyb.server.biosaxs.vos.datacollection.MeasurementTodataCollection3VO;
import ispyb.server.biosaxs.vos.datacollection.Merge3VO;
import ispyb.server.biosaxs.vos.datacollection.Run3VO;
import ispyb.server.biosaxs.vos.datacollection.SaxsDataCollection3VO;
import ispyb.server.biosaxs.vos.datacollection.Subtraction3VO;
import ispyb.server.common.util.LoggerFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.google.gson.Gson;



@Stateless
public class PrimaryDataProcessing3ServiceBean implements PrimaryDataProcessing3Service, PrimaryDataProcessing3ServiceLocal {

	private final static Logger LOG = Logger.getLogger("PrimaryDataProcessing3ServiceBean");

	private static long now = 0;
	
	@EJB
	private MeasurementToDataCollection3Service measurementToDataCollection3Service;
	
	/** The entity manager. */
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	/** The now. */
	protected static Calendar NOW;

	private Date getNow(){
		 NOW = GregorianCalendar.getInstance();
		 return NOW.getTime();
	}
	

	
	
	/******************************************************************************************
	 * MERGE
	 * 
	 * Given a measurementId add all the information about the run
	 * Curves: "/data/pyarch/bm29/opd29/600/1d/tata_006_00001.dat, /data/pyarch/bm29/opd29/600/1d/tata_006_00002.dat, /data/pyarch/bm29/opd29/600/1d/tata_006_00003.dat, /data/pyarch/bm29/opd29/600/1d/tata_006_ave.dat"
	 ******************************************************************************************/
	@Override
	public void addMerge(int measurementId, int averagedCount, int framesCount, String oneDimensionalDataFilePathArray, String discardedCurves) {
		this.addMerge(measurementId, averagedCount, framesCount, oneDimensionalDataFilePathArray, discardedCurves, this.getAverageFilePath(Arrays.asList(oneDimensionalDataFilePathArray.split(","))));
	}
	
	@Override
	public void addMerge(int measurementId, int averagedCount, int framesCount, String oneDimensionalDataFilePathArray, String discardedCurves, String averageFilePath) {
		List<String> curveList = Arrays.asList(oneDimensionalDataFilePathArray.split(","));
		Framelist3VO frameList = this.getFrameListFromOneDimesionalDataFilePathArray(curveList);
		Merge3VO merge = new Merge3VO();
		merge.setMeasurementId(measurementId);
		merge.setFramelist3VO(frameList);
		merge.setFramesMerge(String.valueOf(averagedCount));
		merge.setFramesCount(String.valueOf(framesCount));
		if (averageFilePath != null){
			merge.setAverageFilePath(averageFilePath.toString().trim());
		}
		merge.setCreationDate(getNow());
		entityManager.merge(merge);
	}
	

	public Framelist3VO getFrameListFromOneDimesionalDataFilePathArray(List<String> curveList){
		Framelist3VO frameList = new Framelist3VO();
		frameList = this.entityManager.merge(frameList);
		for (String curve : curveList) {
//			if (!curve.endsWith("_ave.dat") && !curve.endsWith("_sub.dat") && !curve.endsWith("_sub.out")  && !curve.endsWith("_ave_averbuffer.dat")){
			if (!curve.endsWith("_ave.dat")){
				Frame3VO frame = new Frame3VO();
				frame.setFilePath(curve.trim());
				frame.setCreationDate(getNow());
				frame = this.entityManager.merge(frame);
				
				Frametolist3VO frameToList = new Frametolist3VO();
				frameToList.setFrame3VO(frame);
				frameToList.setFrameListId(frameList.getFrameListId());
				this.entityManager.merge(frameToList);
			}
		}
		return frameList;
	}
	
	/** Given a list of file path return path which finish with  _ave.dat **/
	public String getAverageFilePath(List<String> curveList){
		try{
			for (String curve : curveList) {
				if (curve.endsWith("_ave.dat")){
					return curve;
				}
			}
		}
		catch(Exception e){
			LOG.error(e.getMessage());
			e.printStackTrace();
		}
		return new String();
	}
	
	/**
	 * Example of curveList
	 * 
	 * /data/pyarch/bm29/opd29/340/1d/titi_014_00001.dat, 
	 * /data/pyarch/bm29/opd29/340/1d/titi_014_00002.dat, 
	 * /data/pyarch/bm29/opd29/340/1d/titi_014_00003.dat, 
	 * /data/pyarch/bm29/opd29/340/1d/titi_014_00004.dat, 
	 * /data/pyarch/bm29/opd29/340/1d/titi_014_00005.dat, 
	 * /data/pyarch/bm29/opd29/340/1d/titi_014_00006.dat, 
	 * /data/pyarch/bm29/opd29/340/1d/titi_014_00007.dat, 
	 * /data/pyarch/bm29/opd29/340/1d/titi_014_00008.dat, 
	 * /data/pyarch/bm29/opd29/340/1d/titi_014_00009.dat, 
	 * /data/pyarch/bm29/opd29/340/1d/titi_014_00010.dat, 
	 * /data/pyarch/bm29/opd29/340/1d/titi_014_ave.dat, 
	 * /data/pyarch/bm29/opd29/340/1d/titi_013_sub.dat, 
	 * /data/pyarch/bm29/opd29/340/1d/titi_013_sub.out, 
	 * /data/pyarch/bm29/opd29/340/1d/titi_013_ave_averbuffer.dat
	 * 
	 * We only want to log here to .dat files
	 */
	@Override
	public Framelist3VO addFrameList(List<String> curveList){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("curveList", Arrays.asList(curveList).toString());
		
		long start = this.logInit("addFrameList", new Gson().toJson(params));
		Framelist3VO frameList = new Framelist3VO();
		frameList = this.entityManager.merge(frameList);
		for (String curve : curveList) {
				Frame3VO frame = this.getFrame(curve.trim());
				if (frame != null){
					Frametolist3VO frameToList = new Frametolist3VO();
					frameToList.setFrame3VO(frame);
					frameToList.setFrameListId(frameList.getFrameListId());
					this.entityManager.merge(frameToList);
				}
		}
		this.logFinish("addFRamesList", start);
		
		return frameList;
	}
	
	/**
	 * this checks if the frame exists on the database otherwise it will create a new one
	 * @param filePath
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Frame3VO getFrame(String filePath){
		
		/** Checking if it exists **/
		try{
			String query = SQLQueryKeeper.getFrame3VOByFilePath(filePath);
			Query EJBQuery = this.entityManager.createQuery(query);
			List<Frame3VO> frames =  EJBQuery.getResultList();
			if (frames.size() > 0){
				return frames.get(0);
			}
		}
		catch(Exception exp){
			exp.printStackTrace();
		}
		
		/** Creating a new one **/
		try{
			Frame3VO frame = new Frame3VO();
			frame.setFilePath(filePath);
			frame.setCreationDate(getNow());
			frame = this.entityManager.merge(frame);
			return frame;
		}
		catch(Exception exp){
			exp.printStackTrace();
		}
		return null;
	}
	
	
	/******************************************************************************************
	 * SUBTRACTION
	 * 
	 * Given a measurementId add all the information about the run
	 ******************************************************************************************/
	@Override
	public void addSubstraction(int dataCollectionId, 
			String rg, String rgStdev, String i0, String i0Stdev,
			String firstPointUsed, String lastPointUsed, String quality,
			String isagregated, String code, String concentration,
			String gnomFile, String rgGuinier, String rgGnom, String dmax,
			String total, String volume, String scatteringFilePath,
			String guinierFilePath, String kratkyFilePath, String densityPlot, String substractedFilePath, String gnomFilePathOutput) {
		
		Subtraction3VO substraction = new Subtraction3VO();
		substraction.setDataCollectionId(dataCollectionId);
		substraction.setRg(rg);
		substraction.setRgStdev(rgStdev);
		substraction.setI0(i0);
		substraction.setI0stdev(i0Stdev);
		substraction.setFirstPointUsed(firstPointUsed);
		substraction.setLastPointUsed(lastPointUsed);
		substraction.setQuality(quality);
		substraction.setIsagregated(isagregated);
		substraction.setConcentration(concentration);
		substraction.setGnomFilePath(densityPlot);
		substraction.setRgGuinier(rgGuinier);
		substraction.setRgGnom(rgGnom);
		substraction.setDmax(dmax);
		substraction.setTotal(total);
		substraction.setVolume(volume);
		substraction.setCreationTime(getNow());
		substraction.setKratkyFilePath(kratkyFilePath);
		substraction.setGuinierFilePath(guinierFilePath);
		substraction.setScatteringFilePath(scatteringFilePath);
		substraction.setGnomFilePath(densityPlot);
		substraction.setSubstractedFilePath(substractedFilePath);
		substraction.setGnomFilePathOutput(gnomFilePathOutput);
		this.entityManager.persist(substraction);
		
	}

	private Measurement3VO getMeasurementById(String measurementId){
			String query = SQLQueryKeeper.getMeasurementById(Integer.parseInt(measurementId));//"SELECT measurement FROM Measurement3VO measurement where measurement.measurementId = :specimenId" ;
			Query EJBQuery = this.entityManager.createQuery(query);
			Measurement3VO measurement = (Measurement3VO) EJBQuery.getSingleResult();	
			return measurement;
	}
	/******************************************************************************************
	 * RUN
	 * 
	 * Given a measurementId add all the information about the run
	 ******************************************************************************************/
	@Override
	@Deprecated
	public void addRun(
			Integer experimentId, 
			String measurementId, 
			String exposureTemperature, 
			String storageTemperature,
			String timePerFrame, 
			String timeStart, 
			String timeEnd,
			String energy, 
			String detectorDistance, 
			String edfFileArray,
			String snapshotCapillary, 
			String currentMachine, 
			String beamCenterX, 
			String beamCenterY,
			String radiationRelative, 
			String radiationAbsolute,
			String pixelSizeX, 
			String pixelSizeY, 
			String normalization,
			String transmission, 
			ArrayList<String> curves) {

		Measurement3VO measurement = getMeasurementById(measurementId);
		
		
		Run3VO run = new Run3VO();
		run.setTimePerFrame(timePerFrame);
		run.setTimeStart(timeStart);
		run.setTimeEnd(timeEnd);
		run.setStorageTemperature(storageTemperature);
		run.setExposureTemperature(exposureTemperature);
		run.setEnergy(energy);
		run.setTransmission(transmission);
		run.setBeamCenterX(beamCenterX);
		run.setBeamCenterY(beamCenterY);
		run.setPixelSizeX(pixelSizeX);
		run.setPixelSizeY(pixelSizeY);
		run.setRadiationAbsolute(radiationAbsolute);
		run.setRadiationRelative(radiationRelative);
		run.setExposureTemperature(exposureTemperature);
		run.setNormalization(normalization);
		run.setCreationDate(getNow());
		
		
		run = this.entityManager.merge(run);
		
		measurement.setRun3VO(run);
		measurement = this.entityManager.merge(measurement);
	}

	@Override
	public void addRun(
			int experimentId, 
			int measurementId, 
			String exposureTemperature, 
			String storageTemperature,
			String timePerFrame, 
			String timeStart, 
			String timeEnd,
			String energy, 
			String detectorDistance, 
			String snapshotCapillary, 
			String currentMachine, 
			String beamCenterX, 
			String beamCenterY,
			String radiationRelative, 
			String radiationAbsolute,
			String pixelSizeX, 
			String pixelSizeY, 
			String normalization,
			String transmission) {

			this.addRun(experimentId, 
					String.valueOf(measurementId), exposureTemperature, storageTemperature, timePerFrame, timeStart, timeEnd, energy, detectorDistance, null,
					snapshotCapillary, currentMachine, beamCenterX, beamCenterY, radiationRelative, radiationAbsolute, pixelSizeX, pixelSizeY, 
					normalization, 
					transmission, null);
	}
	
	@Override
	public void addRun(
			String measurementId, 
			String exposureTemperature, 
			String storageTemperature,
			String timePerFrame, 
			String timeStart, 
			String timeEnd,
			String energy, 
			String detectorDistance, 
			String snapshotCapillary, 
			String currentMachine, 
			String beamCenterX, 
			String beamCenterY,
			String radiationRelative, 
			String radiationAbsolute,
			String pixelSizeX, 
			String pixelSizeY, 
			String normalization,
			String transmission) {

			this.addRun(null, 
					measurementId, exposureTemperature, storageTemperature, timePerFrame, timeStart, timeEnd, energy, detectorDistance, null,
					snapshotCapillary, currentMachine, beamCenterX, beamCenterY, radiationRelative, radiationAbsolute, pixelSizeX, pixelSizeY, 
					normalization, transmission, null);
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Frame3VO> getFramesByIdList(List<Integer> frameIdList) {
		if (frameIdList.size() > 0){
			String query = "SELECT frames FROM Frame3VO frames where frames.frameId IN :frameIdList ";
			Query EJBQuery = this.entityManager.createQuery(query).setParameter("frameIdList", frameIdList);
			return (List<Frame3VO>)EJBQuery.getResultList();
		}
		else{
			return new  ArrayList<Frame3VO>();
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List test(String query) {
		Query EJBQuery = this.entityManager.createQuery(query);
		return EJBQuery.getResultList();
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Merge3VO> getMergesByIdsList(List<Integer> mergeIdList) {
		String query = SQLQueryKeeper.getMergesByIdsList();
		Query EJBQuery = this.entityManager.createQuery( query.toString()).setParameter("mergeIdList", mergeIdList);
		return (List<Merge3VO>)EJBQuery.getResultList();
		
	}

	@Override
	public Subtraction3VO getSubstractionById(int subtractionId) {
		return entityManager.find(Subtraction3VO.class, subtractionId);
	}
	
	@Override
	public List<Subtraction3VO> getSubstractionsByMeasurementId(int measurementId) {
		List<MeasurementTodataCollection3VO> measurementToDataCollection = measurementToDataCollection3Service.findByMeasurementId(measurementId);
		List<Subtraction3VO> result = new ArrayList<Subtraction3VO>();
		for (MeasurementTodataCollection3VO measurementTodataCollection3VO : measurementToDataCollection) {
			SaxsDataCollection3VO dataCollection = measurementToDataCollection3Service.findDataCollectionById(measurementTodataCollection3VO.getDataCollectionId());
			result.add(this.getSubtractionByDataCollectionId(dataCollection.getDataCollectionId()));
		}
		return result;
	}
	
	private Subtraction3VO getSubtractionByDataCollectionId(Integer dataCollectionId) {
		String query = "select s from Subtraction3VO s where s.dataCollectionId=:dataCollectionId";
		Query EJBQuery = this.entityManager.createQuery(query).setParameter("dataCollectionId", dataCollectionId);
		return (Subtraction3VO)EJBQuery.getSingleResult();
	}




	@Override
	public List<Merge3VO> findByMeasurementId(int measurementId) {
		String query = "select c from Merge3VO c where c.measurementId=:measurementId";
		Query EJBQuery = this.entityManager.createQuery(query).setParameter("measurementId", measurementId);
		return (List<Merge3VO>)EJBQuery.getResultList();
	}
	
	
	

	


	/**
	* ESRF
	*/ 
	@Override
	public void addRun(int experimentId, String measurementId, String runNumber, String exposureTemperature,
			String storageTemperature, String timePerFrame, String timeStart, String timeEnd, String energy, String detectorDistance,
			String edfFileArray, String snapshotCapillary, String currentMachine, String tocollect, String pars, String beamCenterX,
			String beamCenterY, String radiationRelative, String radiationAbsolute, String pixelSizeX, String pixelSizeY,
			String normalization, String transmission, ArrayList<String> curves) {

		Measurement3VO measurement = getMeasurementById(measurementId);
		
		Run3VO run = new Run3VO();
		run.setTimePerFrame(timePerFrame);
		run.setTimeStart(timeStart);
		run.setTimeEnd(timeEnd);
		run.setStorageTemperature(storageTemperature);
		run.setExposureTemperature(exposureTemperature);
		run.setEnergy(energy);
		run.setTransmission(transmission);
		run.setBeamCenterX(beamCenterX);
		run.setBeamCenterY(beamCenterY);
		run.setPixelSizeX(pixelSizeX);
		run.setPixelSizeY(pixelSizeY);
		run.setRadiationAbsolute(radiationAbsolute);
		run.setRadiationRelative(radiationRelative);
		run.setExposureTemperature(exposureTemperature);
		run.setNormalization(normalization);
		run.setCreationDate(getNow());
		run = this.entityManager.merge(run);
		
		/** Update runNumber **/
		measurement.setCode(runNumber);
		measurement.setRun3VO(run);
		measurement = this.entityManager.merge(measurement);			
		
	}




	private void logFinish(String methodName, long id) {
		LOG.debug("### [" + methodName.toUpperCase() + "] Execution time was " + (System.currentTimeMillis() - this.now) + " ms.");
		LoggerFormatter.log(LOG, LoggerFormatter.Package.BIOSAXS_WS, methodName, id, System.currentTimeMillis(),
				System.currentTimeMillis() - this.now);

	}

	protected long logInit(String methodName, String params) {
		LOG.info("-----------------------");
		this.now = System.currentTimeMillis();
		LOG.info(methodName.toUpperCase());
		LoggerFormatter.log(LOG, LoggerFormatter.Package.BIOSAXS_WS, methodName, System.currentTimeMillis(),
				System.currentTimeMillis(), params);
		return this.now;
	}
	
	
}
