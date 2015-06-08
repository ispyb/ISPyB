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

package ispyb.client.biosaxs.pdf;

import ispyb.client.biosaxs.pdf.utils.DoubleUtils;
import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Buffer3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Measurement3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Specimen3VO;
import ispyb.server.biosaxs.vos.datacollection.MeasurementTodataCollection3VO;
import ispyb.server.biosaxs.vos.datacollection.Merge3VO;
import ispyb.server.biosaxs.vos.datacollection.SaxsDataCollection3VO;
import ispyb.server.biosaxs.vos.datacollection.Subtraction3VO;
import ispyb.server.biosaxs.vos.utils.comparator.MeasurementComparator;
import ispyb.server.common.vos.proposals.Proposal3VO;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;



public class DataAcquisitionReport {
	protected  static DecimalFormat df=new DecimalFormat("0.00");
	protected String[] COLUMNS = {"ID", "MOLECULE","BUFFER", "CON. (mg/ml)", "Frames Avg.", "Rg", "Points", "Quality", "IO", "Aggregated", "Rg", "Total", "Dmax", "Volume", "MM(kD) Vol. Est." };
	protected String[] MEASUREMENT_COLUMNS = {"ID", "MACROMOLECULE","BUFFER", "CONCENTRATION", "VOLUME", "EXP. TEMP.", "TRANS.", "VISCOSITY",  "WAIT TIME", "FLOW", "ENERGY", "FRAMES", "TIME", "PREFIX"};
	protected Experiment3VO experiment;
	protected List<Buffer3VO> buffers;
	protected Proposal3VO proposal;

	

	private String getAcronymBuffer(Specimen3VO specimen, Experiment3VO experiment, List<Buffer3VO> buffers){
		 int bufferId = specimen.getBufferId();
		 for (int i = 0; i < buffers.size(); i++) {
			if (buffers.get(i).getBufferId().equals(bufferId)){
				return buffers.get(i).getAcronym();
			}
		}
		 return new String();
	}
	
	private ArrayList<String> getMeasurementData( Measurement3VO measurement, Experiment3VO experiment, List<Buffer3VO> buffers) {
		 String order = new String();
		 String macromolecule = new String();
		 String buffer = new String();
		 String concentration = new String();
		 String volume = new String();
		 String transmission = new String();
		 String viscosity = new String();
		 String energy = new String();
		 String realTemp = new String();
		 String waitTime = new String();
		 String flow = new String();
		 String time = new String();
		 String numberFrames = new String();
		 String prefix = new String();
		 
		 if (measurement.getPriority() != null){
			 order = measurement.getPriority().toString();
		 }
		 
		 Specimen3VO specimen =  experiment.getSampleById(measurement.getSpecimenId());
		 Macromolecule3VO macromolecule3VO = specimen.getMacromolecule3VO();
		 if (macromolecule3VO != null){
			 macromolecule = macromolecule3VO.getAcronym();
		 }
		 
		 if (specimen != null){
					buffer = this.getAcronymBuffer(specimen, experiment, buffers);
		 }
		 
		 concentration = specimen.getConcentration();
		 volume = measurement.getVolumeToLoad();
		 realTemp = measurement.getExposureTemperature();
		 transmission = measurement.getTransmission();
		 viscosity = measurement.getViscosity();
		 flow = measurement.getFlow().toString();
		 
		 if (measurement.getRun3VO() != null){
			 energy = fixDecimals(measurement.getRun3VO().getEnergy());
			 realTemp = fixDecimals(measurement.getRun3VO().getExposureTemperature());
			 time = measurement.getRun3VO().getTimeStart();
			 
		 }
		 
		 if(measurement.getMerge3VOs() != null){
			 if (measurement.getMerge3VOs().size() > 0){
				 Iterator<Merge3VO> iterator = measurement.getMerge3VOs().iterator();
				 Merge3VO merge = iterator.next();
				 if (merge.getAverageFilePath() != null){
					 prefix = new File(merge.getAverageFilePath()).getName();
				 }
			 }
		 }
		 
		 waitTime = measurement.getWaitTime();
		 ArrayList<String> measurementList = new ArrayList<String>();
		 measurementList.add(order);
		 measurementList.add(macromolecule);
		 measurementList.add(buffer);
		 measurementList.add(concentration);
		 measurementList.add(volume);
		 measurementList.add(realTemp);
		 
		 measurementList.add(transmission);
		 measurementList.add(viscosity);
		 measurementList.add(waitTime);
		 measurementList.add(flow);
		 measurementList.add(energy);
		 measurementList.add(numberFrames);
		 measurementList.add(time);
		 measurementList.add(prefix);
		 
		 return measurementList;
	}
	
	private String fixDecimals(String value){
		if (value != null){
	 		 if (Pattern.matches(DoubleUtils.fpRegex, value)){
	 			return df.format(Double.parseDouble(value));
	 		 }
	 	}
		return new String();
	}
	
	private String fixDecimals(double d) {
		return df.format(d);
	}
	
	
	protected ArrayList<String> addAnalysisRow( Measurement3VO measurement, Experiment3VO experiment, List<Buffer3VO> buffers) {
		
		 String order = new String();
		 String macromolecule = new String();
		 String buffer = new String();
		 String concentration = new String();
		 
		 String frames = new String();
		 /** Guinier **/
		 String rgGuinier = new String();
		 String points = new String();
		 String quality = new String();
		 String guinierIO = new String();
		 String aggregated = new String();
		 /** Gnom **/
		 String rg = new String();
		 String total = new String();
		 String dmax = new String();
		 
		 /** Porod **/
		 String volume = new String();
		 String estimated = new String();
		 
		 if (measurement.getPriority() != null){
			 order = measurement.getPriority().toString();
		 }
		 
		 Specimen3VO specimen =  experiment.getSampleById(measurement.getSpecimenId());
		 Macromolecule3VO macromolecule3VO = specimen.getMacromolecule3VO();
		 if (macromolecule3VO != null){
			 macromolecule = macromolecule3VO.getAcronym();
		 }
		 
		 if (specimen != null){
			 int bufferId = specimen.getBufferId();
			 for (int i = 0; i < buffers.size(); i++) {
				if (buffers.get(i).getBufferId().equals(bufferId)){
					buffer = buffers.get(i).getAcronym();
				}
			}
		 }
		 concentration = fixDecimals(specimen.getConcentration());

		 SaxsDataCollection3VO dataCollection = experiment.getDataCollectionByMeasurementId(measurement.getMeasurementId());
		 /** GUINIER **/
		 if (measurement.getMerge3VOs() != null){
			 if (measurement.getMerge3VOs().size() > 0){
				 frames = ((Merge3VO)(measurement.getMerge3VOs().toArray()[0])).getFramesMerge() + "/"+ ((Merge3VO)(measurement.getMerge3VOs().toArray()[0])).getFramesCount();
				 if (dataCollection != null){
					 List<Object> list = Arrays.asList(dataCollection.getMeasurementtodatacollection3VOs().toArray());
					 
					 if (list.get(0) != null){
						 MeasurementTodataCollection3VO measurementToDatacollectionBefore = (MeasurementTodataCollection3VO )list.get(0);
						 Measurement3VO measurementBefore = experiment.getMeasurementBefore(measurementToDatacollectionBefore.getMeasurementId());
						 String before = "";
						 if (measurementBefore.getMerge3VOs() != null){
							 if (measurementBefore.getMerge3VOs().size() > 0){
							 before = ((Merge3VO)(measurementBefore.getMerge3VOs().toArray()[0])).getFramesMerge() + "/"+ ((Merge3VO)(measurementBefore.getMerge3VOs().toArray()[0])).getFramesCount();
							 }
						 }
						 frames = before + " " + frames;
					 }
					 
					 if (list.get(2) != null){
						 MeasurementTodataCollection3VO measurementToDatacollectionAfter = (MeasurementTodataCollection3VO )list.get(2);
						 Measurement3VO measurementAfter = experiment.getMeasurementBefore(measurementToDatacollectionAfter.getMeasurementId());
						 String after = "";
						 if (measurementAfter.getMerge3VOs() != null){
							 if (measurementAfter.getMerge3VOs().size() > 0){
								 after = ((Merge3VO)(measurementAfter.getMerge3VOs().toArray()[0])).getFramesMerge() + "/"+ ((Merge3VO)(measurementAfter.getMerge3VOs().toArray()[0])).getFramesCount();
							 }
						 }
						 frames = frames + " " + after;
					 }
					 
				 }
			 }
			 
		 }
		 
		 if (dataCollection != null){
			 if (dataCollection.getSubstraction3VOs() != null){
				 if (dataCollection.getSubstraction3VOs().size() > 0){
					Subtraction3VO substraction = ((Subtraction3VO)(dataCollection.getSubstraction3VOs().toArray()[0]));
				 	rgGuinier = fixDecimals(substraction.getRgGuinier());
				 	points = substraction.getFirstPointUsed() + " - " + substraction.getLastPointUsed();
				 	quality = fixDecimals(substraction.getQuality());
				 	guinierIO = fixDecimals(substraction.getI0()) + " \u00B1 " + fixDecimals(substraction.getI0stdev());
				 	aggregated = substraction.getIsagregated();
				 	
				 	rg = fixDecimals(substraction.getRgGnom());
				 	total = fixDecimals(substraction.getTotal());
				 	dmax = fixDecimals(substraction.getDmax());
				 	
				 	volume = fixDecimals(substraction.getVolume());
				 	try{
				 		if (substraction.getVolume() != null){
				 		 if (Pattern.matches(DoubleUtils.fpRegex, substraction.getVolume()))
				 			estimated = fixDecimals(Double.parseDouble(substraction.getVolume())/2) + " - " + fixDecimals(Double.parseDouble(substraction.getVolume())/1.5);
				 		}
				 		
				 	}catch(Exception e){
				 		e.printStackTrace();
				 	}
				 }
				 
			 }
		 }
		 
		 
		 ArrayList<String> measurementList = new ArrayList<String>();
		 measurementList.add(order);
		 measurementList.add(macromolecule);
		 measurementList.add(buffer);
		 measurementList.add(concentration);
		 measurementList.add(frames);
		 /** GUINIER **/
		 measurementList.add(rgGuinier);
		 measurementList.add(points);
		 measurementList.add(quality);
		 measurementList.add(guinierIO);
		 measurementList.add(aggregated);
		 /** GNOM **/
		 measurementList.add(rg);
		 measurementList.add(total);
		 measurementList.add(dmax);
		 
		 /** POROD **/
		 measurementList.add(volume);
		 measurementList.add(estimated);
		 return measurementList;
	}
	
	

	
	
	
	/**
	 * @param experiment
	 * @param experiment2
	 * @param buffers
	 * @return
	 */
	protected List<List<String>> getExperimentMesurementData(Experiment3VO experiment,List<Buffer3VO> buffers) {
		List<List<String>> data = new ArrayList<List<String>>();
		
		List<Measurement3VO> measurements = experiment.getMeasurements();
		Collections.sort(measurements, MeasurementComparator.compare(MeasurementComparator.getComparator(MeasurementComparator.PRIOTIRY_SORT_ASC)));
		for (int x = 0; x < measurements.size(); x++) {
			Measurement3VO measurement = measurements.get(x);
			data.add(this.getMeasurementData( measurement, experiment, buffers));
		}
		return data;	
	}
	
	/**
	 * @param experiment
	 * @param experiment2
	 * @param buffers
	 * @return
	 */
	protected List<List<String>> getExperimentAnalysisData(Experiment3VO experiment,List<Buffer3VO> buffers) {
		List<List<String>> data = new ArrayList<List<String>>();
		List<Measurement3VO> measurements = experiment.getMeasurements();
		Collections.sort(measurements, MeasurementComparator.compare(MeasurementComparator.getComparator(MeasurementComparator.PRIOTIRY_SORT_ASC)));
		for (int x = 0; x < measurements.size(); x++) {
			Measurement3VO measurement = measurements.get(x);
			if (experiment.getSampleById(measurement.getSpecimenId()).getMacromolecule3VO() != null){
					data.add(this.addAnalysisRow( measurement, experiment, buffers));
			}
		}
		return data;	
	}

	public void setUp(Experiment3VO experiment, List<Buffer3VO> buffers, Proposal3VO proposal) {
		this.experiment = experiment;
		this.buffers = buffers;
		this.proposal = proposal;
	}

	public boolean checkParameters() {
		return !((this.experiment == null)&&(this.buffers == null) && (this.proposal == null));
	}

}
