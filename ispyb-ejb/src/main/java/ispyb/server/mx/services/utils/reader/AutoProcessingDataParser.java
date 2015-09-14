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
package ispyb.server.mx.services.utils.reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;


/**
 * data to be plotted - auto processing graphs
 * 3 different graphes are available: 
 * from XSCALE.LP file: resolution vs completeness /or R-Factor or I/Sigl or CC/2 or SigAno
 * wilson plot
 * cumultive intensity distribution
 * @author BODIN
 *
 */
public class AutoProcessingDataParser {

	private List<List<AutoProcessingData>> autoProcessingDataListList = new ArrayList<List<AutoProcessingData>>();

	public AutoProcessingDataParser(List<List<AutoProcessingData>> autoProcessingDataList) {
		this.autoProcessingDataListList = autoProcessingDataList;
	}

	
	public List<Double> getResolutionLimitSorted(){
		ArrayList<Double> resolutionLimit = new ArrayList<Double>();
		for (List<AutoProcessingData> file : this.autoProcessingDataListList) {
			Collections.sort(file, new AutoProcessingDataCompartor());
			for (AutoProcessingData autoProcessingData : file) {
				resolutionLimit.add(autoProcessingData.getResolutionLimit());
			}
		}
		Collections.sort(resolutionLimit);
		resolutionLimit = new ArrayList<Double>(new LinkedHashSet<Double>(resolutionLimit));
		return resolutionLimit;
	}
	
	
	public List<Double> getResolutionSorted(){
		List<AutoProcessingData> file = this.autoProcessingDataListList.get(0);
		Collections.sort(file, new AutoProcessingDataCompartor());
		
		ArrayList<Double> resolutionLimit = new ArrayList<Double>();
		for (AutoProcessingData autoProcessingData : file) {
			resolutionLimit.add(autoProcessingData.getResolution());
			System.out.println(autoProcessingData.getResolution());
		}
		return resolutionLimit;
	}
	
	public String getCompletenessByResolution(double resolution, List<AutoProcessingData> file){
		for (AutoProcessingData autoProcessingData : file) {
			if (autoProcessingData.getResolutionLimit().equals(resolution)){
				return String.valueOf(autoProcessingData.getCompleteness());
			}
		}
		return "";
	}
	
	public String getRfactorByResolution(double resolution, List<AutoProcessingData> file){
		for (AutoProcessingData autoProcessingData : file) {
			if (autoProcessingData.getResolutionLimit().equals(resolution)){
				return String.valueOf(autoProcessingData.getrFactorObserved());
			}
		}
		return "";
	}
	
	public String getSigmaByResolution(double resolution, List<AutoProcessingData> file){
		for (AutoProcessingData autoProcessingData : file) {
			if (autoProcessingData.getResolutionLimit().equals(resolution)){
				return String.valueOf(autoProcessingData.getSigAno());
			}
		}
		return "";
	}
	
	
	public String getCC2ByResolution(double resolution, List<AutoProcessingData> file){
		for (AutoProcessingData autoProcessingData : file) {
			if (autoProcessingData.getResolutionLimit().equals(resolution)){
				return String.valueOf(autoProcessingData.getCc2());
			}
		}
		return "";
	}
	
	public String getISigmaByResolution(double resolution, List<AutoProcessingData> file){
		for (AutoProcessingData autoProcessingData : file) {
			if (autoProcessingData.getResolutionLimit().equals(resolution)){
				return String.valueOf(autoProcessingData.getiSigma());
			}
		}
		return "";
	}
	

	public String getAnomCorrectionByResolution(double resolution, List<AutoProcessingData> file){
		for (AutoProcessingData autoProcessingData : file) {
			if (autoProcessingData.getResolutionLimit().equals(resolution)){
				return String.valueOf(autoProcessingData.getAnomalCorr());
			}
		}
		return "";
	}
	
	public String getWilsonByResolution(double resolution, List<AutoProcessingData> file){
		for (AutoProcessingData autoProcessingData : file) {
			if (autoProcessingData.getResolution() != null){
				if (autoProcessingData.getResolution().equals(resolution)){
					return String.valueOf(autoProcessingData.getWilsonPlot());
				}
			}
		}
		return "";
	}
	
	
	public String parseCompleteness(){
		List<Double> resolutionLimitArray = this.getResolutionLimitSorted();
		
		StringBuilder sb = new StringBuilder();
		if (this.autoProcessingDataListList != null){
			/** Header **/
			sb.append("Resolution");
			for (List<AutoProcessingData> file : this.autoProcessingDataListList) {
				sb.append(",");
				sb.append(file.get(0).getAutoProcProgramAttachmentId());
			}
			sb.append("\n");

			/** Getting X (resolution) **/
			for (Double resolutionLimit : resolutionLimitArray) {
				sb.append(resolutionLimit);
				for (List<AutoProcessingData> file : this.autoProcessingDataListList) {
					sb.append(",");
					String value = this.getCompletenessByResolution(resolutionLimit, file);
					String error = "0";
					sb.append(value);
					sb.append(",");
					sb.append(error);
				}
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	public String parseRfactor(){
		List<Double> resolutionLimitArray = this.getResolutionLimitSorted();
		
		StringBuilder sb = new StringBuilder();
		if (this.autoProcessingDataListList != null){
			/** Header **/
			sb.append("Resolution");
			for (List<AutoProcessingData> file : this.autoProcessingDataListList) {
				sb.append(",");
				sb.append(file.get(0).getAutoProcProgramAttachmentId());
			}
			sb.append("\n");

			/** Getting X (resolution) **/
			for (Double resolutionLimit : resolutionLimitArray) {
				sb.append(resolutionLimit);
				for (List<AutoProcessingData> file : this.autoProcessingDataListList) {
					sb.append(",");
					String value = this.getRfactorByResolution(resolutionLimit, file);
					String error = "0";
					sb.append(value);
					sb.append(",");
					sb.append(error);
				}
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	public String parseSigmaAno(){
		List<Double> resolutionLimitArray = this.getResolutionLimitSorted();
		
		StringBuilder sb = new StringBuilder();
		if (this.autoProcessingDataListList != null){
			/** Header **/
			sb.append("Resolution");
			for (List<AutoProcessingData> file : this.autoProcessingDataListList) {
				sb.append(",");
				sb.append(file.get(0).getAutoProcProgramAttachmentId());
			}
			sb.append("\n");

			/** Getting X (resolution) **/
			for (Double resolutionLimit : resolutionLimitArray) {
				sb.append(resolutionLimit);
				for (List<AutoProcessingData> file : this.autoProcessingDataListList) {
					sb.append(",");
					String value = this.getSigmaByResolution(resolutionLimit, file);
					String error = "0";
					sb.append(value);
					sb.append(",");
					sb.append(error);
				}
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	public String parseISigma(){
		List<Double> resolutionLimitArray = this.getResolutionLimitSorted();
		
		StringBuilder sb = new StringBuilder();
		if (this.autoProcessingDataListList != null){
			/** Header **/
			sb.append("Resolution");
			for (List<AutoProcessingData> file : this.autoProcessingDataListList) {
				sb.append(",");
				sb.append(file.get(0).getAutoProcProgramAttachmentId());
			}
			sb.append("\n");

			/** Getting X (resolution) **/
			for (Double resolutionLimit : resolutionLimitArray) {
				sb.append(resolutionLimit);
				for (List<AutoProcessingData> file : this.autoProcessingDataListList) {
					sb.append(",");
					String value = this.getISigmaByResolution(resolutionLimit, file);
					String error = "0";
					sb.append(value);
					sb.append(",");
					sb.append(error);
				}
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	public String parsecc2(){
		List<Double> resolutionLimitArray = this.getResolutionLimitSorted();
		
		StringBuilder sb = new StringBuilder();
		if (this.autoProcessingDataListList != null){
			/** Header **/
			sb.append("Resolution");
			for (List<AutoProcessingData> file : this.autoProcessingDataListList) {
				sb.append(",");
				sb.append(file.get(0).getAutoProcProgramAttachmentId());
			}
			sb.append("\n");

			/** Getting X (resolution) **/
			for (Double resolutionLimit : resolutionLimitArray) {
				sb.append(resolutionLimit);
				for (List<AutoProcessingData> file : this.autoProcessingDataListList) {
					sb.append(",");
					String value = this.getCC2ByResolution(resolutionLimit, file);
					String error = "0";
					sb.append(value);
					sb.append(",");
					sb.append(error);
				}
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	
	public String parseAnomCorrection(){
		List<Double> resolutionLimitArray = this.getResolutionLimitSorted();
		
		StringBuilder sb = new StringBuilder();
		if (this.autoProcessingDataListList != null){
			/** Header **/
			sb.append("Resolution");
			for (List<AutoProcessingData> file : this.autoProcessingDataListList) {
				sb.append(",");
				sb.append(file.get(0).getAutoProcProgramAttachmentId());
			}
			sb.append("\n");

			/** Getting X (resolution) **/
			for (Double resolutionLimit : resolutionLimitArray) {
				sb.append(resolutionLimit);
				for (List<AutoProcessingData> file : this.autoProcessingDataListList) {
					sb.append(",");
					String value = this.getAnomCorrectionByResolution(resolutionLimit, file);
					String error = "0";
					sb.append(value);
					sb.append(",");
					sb.append(error);
				}
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	public String parseWilson(){
		List<Double> resolutionArray = this.getResolutionSorted();
		
		StringBuilder sb = new StringBuilder();
		if (this.autoProcessingDataListList != null){
			/** Header **/
			sb.append("Resolution");
			for (List<AutoProcessingData> file : this.autoProcessingDataListList) {
				sb.append(",");
				sb.append(file.get(0).getAutoProcProgramAttachmentId());
			}
			sb.append("\n");

			/** Getting X (resolution) **/
			for (Double resolution : resolutionArray) {
				sb.append(resolution);
				for (List<AutoProcessingData> file : this.autoProcessingDataListList) {
					sb.append(",");
					String value = this.getWilsonByResolution(resolution, file);
					String error = "0";
					sb.append(value);
					sb.append(",");
					sb.append(error);
				}
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
}
