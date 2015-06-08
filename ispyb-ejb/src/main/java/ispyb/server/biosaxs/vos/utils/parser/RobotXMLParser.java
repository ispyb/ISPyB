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

package ispyb.server.biosaxs.vos.utils.parser;

import ispyb.server.biosaxs.vos.dataAcquisition.Buffer3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Measurement3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Specimen3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.plate.Sampleplate3VO;
import ispyb.server.biosaxs.vos.utils.comparator.MeasurementComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;

public class RobotXMLParser {
	
	public static String toRobotXML(Experiment3VO experiment, List<Integer> samplePlateIds, List<Buffer3VO> buffers ){
		Set<Sampleplate3VO> plates = new HashSet<Sampleplate3VO>();
		for (Integer id : samplePlateIds) {
			plates.add(experiment.getSamplePlateById(id));
		}
		return toRobotXML(experiment, plates, buffers);
	}
	
	public static String toRobotXML(Experiment3VO experiment, Set<Sampleplate3VO> samplePlate, List<Buffer3VO> buffers){
		List<Specimen3VO> specimens = new ArrayList<Specimen3VO>();
		for (Sampleplate3VO plate : samplePlate) {
			Set<Specimen3VO> auxSpecimens = experiment.getSamples();
			for (Specimen3VO specimen3vo : auxSpecimens) {
				if (specimen3vo.getSampleplateposition3VO() != null){
					if (specimen3vo.getSampleplateposition3VO().getSamplePlateId().equals(plate.getSamplePlateId())){
						specimens.add(specimen3vo);
					}
				}
			}
		}
		
		String storageTemperature = new String();
		if (samplePlate.size() > 0){
			storageTemperature = ((Sampleplate3VO)samplePlate.toArray()[0]).getPlategroup3VO().getStorageTemperature();
		}
		
		return toRobotXML(experiment, specimens, storageTemperature, buffers);
	}
	
	private static String getBufferconditionKey(Measurement3VO measurement, Specimen3VO specimen){
		StringBuilder sb = new StringBuilder();
		sb.append(String.valueOf(measurement.getSpecimenId()));
		sb.append("_");
		sb.append(String.valueOf(measurement.getVolumeToLoad()));
		sb.append("_");
		sb.append(String.valueOf(measurement.getTransmission()));
		sb.append("_");
		sb.append(String.valueOf(measurement.getFlow()));
		sb.append("_");
//		sb.append(String.valueOf(measurement.getWaitTime()));
//		sb.append("_");
		sb.append(String.valueOf(measurement.getViscosity()));
		sb.append("_");
		sb.append(String.valueOf(specimen.getSampleplateposition3VO().getSamplePlatePositionId()));
		return sb.toString();
	}
	
	private static String toRobotXML(Experiment3VO experiment, List<Specimen3VO> specimens, String storageTemperature, List<Buffer3VO> buffers){
		try{
			
			/** Splitting between samples and buffers **/
			List<Measurement3VO> measurementSample = new ArrayList<Measurement3VO>();
			List<Measurement3VO> measurementBuffer = new ArrayList<Measurement3VO>();
			HashMap<String, Integer> bufferHash = new  HashMap<String, Integer>();
			for (Specimen3VO specimen : specimens) {
				if (specimen != null){
					if (specimen.getMeasurements() != null){
						for (Measurement3VO measurement : specimen.getMeasurements()) {
							if (specimen.getMacromolecule3VO() == null){
								String bufferKey = RobotXMLParser.getBufferconditionKey(measurement, specimen);
								if (bufferHash.get(bufferKey) == null){
									measurementBuffer.add(measurement);
									bufferHash.put(bufferKey, bufferHash.size() + 1);
//									System.out.println("BUFFER:\t " + bufferHash.get(bufferKey) + "\t" + bufferKey );
								}
							}
							else{
								measurementSample.add(measurement);
							}
						}
					}
				}
			}
			Collections.sort(measurementSample, MeasurementComparator.compare(MeasurementComparator.getComparator(MeasurementComparator.PRIOTIRY_SORT_ASC)));
			StringBuilder sb = new StringBuilder();
			sb.append("<bsxcube>\n");
			
			/** General **/
			sb.append("<general>\n");
				sb.append(RobotXMLParser.addXMLNode("sampleType", "Green"));
				sb.append(RobotXMLParser.addXMLNode("storageTemperature", storageTemperature));
				sb.append(RobotXMLParser.addXMLNode("extraFlowTime", "0"));
				sb.append(RobotXMLParser.addXMLNode("optimitation", "0"));
				sb.append(RobotXMLParser.addXMLNode("initialCleaning", "1"));
				sb.append(RobotXMLParser.addXMLNode("bufferMode", "0"));
			sb.append("</general>\n");
			
			for (Measurement3VO measurement : measurementBuffer) {
				String bufferKey = RobotXMLParser.getBufferconditionKey(measurement, experiment.getSampleById(measurement.getSpecimenId()));
				String backgroundId = new String();
				if (bufferHash.get(bufferKey) != null){
					backgroundId = bufferHash.get(bufferKey).toString();
				}
				sb.append(toRobotXML(experiment, measurement, buffers, backgroundId));
			}
			
			for (Measurement3VO measurement : measurementSample) {
				Measurement3VO bufferBefore = experiment.getMeasurementAfter(measurement.getMeasurementId());
				String bufferKey = null;
				if (bufferBefore != null) {
					bufferKey = RobotXMLParser.getBufferconditionKey(bufferBefore, experiment.getSampleById(bufferBefore.getSpecimenId()));
				}
				String backgroundId = new String();
				if (bufferHash.get(bufferKey) != null){
					backgroundId = bufferHash.get(bufferKey).toString();
				}
				sb.append(toRobotXML(experiment, measurement,  buffers, backgroundId));
			}
			sb.append("</bsxcube>\n");
			return (sb.toString());
		}
		catch(Exception exp){
			exp.printStackTrace();
		}
		return null;
	}
	
	private static String toRobotXML(Experiment3VO experiment, Measurement3VO specimen, List<Buffer3VO> buffers, String backgroundId) throws NamingException{
		StringBuilder sb = new StringBuilder();
		Specimen3VO sample = experiment.getSampleById(specimen.getSpecimenId());
		if (sample.getMacromolecule3VO() == null){
			sb.append("<Buffer>\n");
		}
		else{
			sb.append("<Sample>\n");
		}
		
		/** SampleDesc **/
		sb.append(RobotXMLParser.addSampleDescriptionXMLNode(experiment, specimen, buffers, backgroundId));
		/** CollectParts **/
		sb.append(RobotXMLParser.addCollectParts(experiment, specimen, buffers));
		if (sample.getMacromolecule3VO() == null){
			sb.append("</Buffer>");
		}
		else{
			sb.append("</Sample>");
		}
		return sb.toString();
	}
	
	private static String addSampleDescriptionXMLNode(Experiment3VO experiment, Measurement3VO measurement, List<Buffer3VO> buffers, String backgroundId){
		StringBuilder sb = new StringBuilder();
		sb.append("<sampledesc>\n");
		Specimen3VO sample = experiment.getSampleById(measurement.getSpecimenId());
		
		if (sample.getSampleplateposition3VO() != null){
			sb.append(RobotXMLParser.addXMLNode("plate", experiment.getSamplePlateById(sample.getSampleplateposition3VO().getSamplePlateId()).getSlotPositionColumn()).toString());
			sb.append(RobotXMLParser.addXMLNode("row", sample.getSampleplateposition3VO().getRowNumber()));
			sb.append(RobotXMLParser.addXMLNode("well", sample.getSampleplateposition3VO().getColumnNumber()));
			if (measurement.getFlow() != null){
				if (measurement.getFlow()){
					sb.append(RobotXMLParser.addXMLNode("flow", "True"));
				}
				else{
					sb.append(RobotXMLParser.addXMLNode("flow", "False"));
				}
			}
			else{
				sb.append(RobotXMLParser.addXMLNode("flow", "False"));
			}
			
			/** Looking for a buffer **/
			Specimen3VO specimen = experiment.getSampleById(measurement.getSpecimenId());
//			Buffer3VO buffer = RobotXMLParser.getBufferById(specimen.getBufferId(), buffers);
//			if (buffer != null){
//				sb.append(RobotXMLParser.addXMLNode("buffername", buffer.getAcronym()));
//			}
//			else{
//				sb.append(RobotXMLParser.addXMLNode("buffername", "Not found"));
//			}
			
			sb.append(RobotXMLParser.addXMLNode("buffername", backgroundId));
			
		}
		sb.append("</sampledesc>\n");
		return sb.toString();
	}
	
	
	private static Buffer3VO getBufferById(int bufferId, List<Buffer3VO> buffers){
		for (Buffer3VO buffer3vo : buffers) {
			if(buffer3vo.getBufferId().equals(bufferId)){
				return buffer3vo;
			}
		}
		return null;
	}
	
//	private static boolean isBufferMeasurement(Experiment3VO experiment, Measurement3VO specimen){
//		return experiment.getSampleById(specimen.getSpecimenId()).getMacromolecule3VO() == null;
//	}
	
	private static String getMacromoleculeAcronym(Experiment3VO experiment, Measurement3VO measurement){
		if (experiment.getSampleById(measurement.getSpecimenId()) != null){
			if (experiment.getSampleById(measurement.getSpecimenId()).getMacromolecule3VO() != null){
				return experiment.getSampleById(measurement.getSpecimenId()).getMacromolecule3VO().getAcronym();
			}
		}
		return new String();
	}
	
	
	private static String addCollectParts(Experiment3VO experiment, Measurement3VO measurement, List<Buffer3VO> buffers){
		StringBuilder sb = new StringBuilder();
		sb.append("<collectpars>\n");
			sb.append(RobotXMLParser.addXMLNode("enable", "True"));
			if (measurement.getConcentration(experiment) == null){
				sb.append(RobotXMLParser.addXMLNode("concentration", "0"));
				sb.append(RobotXMLParser.addXMLNode("macromolecule", ""));
			}
			else{
				if ((measurement.getConcentration(experiment).isEmpty() || measurement.getConcentration(experiment).equals("0"))){
					sb.append(RobotXMLParser.addXMLNode("concentration", "0"));
					Buffer3VO buffer = RobotXMLParser.getBufferById(experiment.getSampleById(measurement.getSpecimenId()).getBufferId(), buffers);
					sb.append(RobotXMLParser.addXMLNode("macromolecule", buffer.getAcronym()));
				}
				else{
					sb.append(RobotXMLParser.addXMLNode("concentration", measurement.getConcentration(experiment)));
					sb.append(RobotXMLParser.addXMLNode("macromolecule", RobotXMLParser.getMacromoleculeAcronym(experiment, measurement)));
				}
			}
			sb.append(RobotXMLParser.addXMLNode("comments", measurement.getComment()));
			sb.append(RobotXMLParser.addXMLNode("priority", measurement.getPriority()));
//			if (RobotXMLParser.isBufferMeasurement(experiment, measurement)){
//				sb.append(RobotXMLParser.addXMLNode("code",  ""));
//			}
//			else{
				sb.append(RobotXMLParser.addXMLNode("code",  measurement.getCode()));
//			}
			
			
			if (measurement.getViscosity() != null){
				sb.append(RobotXMLParser.addXMLNode("viscosity", measurement.getViscosity()));
			}
			else{
				sb.append(RobotXMLParser.addXMLNode("viscosity", ""));
			}
			sb.append(RobotXMLParser.addXMLNode("transmission", measurement.getTransmission()));
			sb.append(RobotXMLParser.addXMLNode("volume", measurement.getVolumeToLoad()));
			sb.append(RobotXMLParser.addXMLNode("SEUtemperature", measurement.getExposureTemperature()));
			if (measurement.getFlow() != null){
				if (measurement.getFlow()){
					sb.append(RobotXMLParser.addXMLNode("flow", "True"));
				}
				else{
					sb.append(RobotXMLParser.addXMLNode("flow", "False"));
				}
			}
			else{
				sb.append(RobotXMLParser.addXMLNode("flow", "False"));
			}
			sb.append(RobotXMLParser.addXMLNode("recuperate", "False"));
			sb.append(RobotXMLParser.addXMLNode("waittime", measurement.getWaitTime()));
		sb.append("</collectpars>\n");
		return sb.toString();
	}
	
	private static String addXMLNode(String key, int value){
		return RobotXMLParser.addXMLNode(key, String.valueOf(value));
	}
	
	
	private static String addXMLNode(String key, String value){
		StringBuilder sb = new StringBuilder();
		sb.append("<");
		sb.append(key);
		sb.append(">");
		sb.append(value);
		sb.append("</");
		sb.append(key);
		sb.append(">\n");
		return sb.toString();
	}
}
