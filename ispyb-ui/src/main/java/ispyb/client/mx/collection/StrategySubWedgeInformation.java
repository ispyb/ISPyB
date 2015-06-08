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


public class StrategySubWedgeInformation {
	
	private String subWedgeNumber 		= "";
	private String rotationAxis 		= "";
	private String axisStart 			= "";
	private String axisEnd 				= "";
	private String exposureTime 		= "";
	private String transmission 		= "";
	private String oscillationRange 	= "";
	private String completeness			= "";
	private String multiplicity			= "";
	private String resolution 			= "";
	private String doseTotal			= "";
	private String numberOfImages 		= "";
	private String comments		 		= "";
	
	
	public String getSubWedgeNumber() 							{return subWedgeNumber;}
	public void setSubWedgeNumber(String subWedgeNumber) 		{this.subWedgeNumber = subWedgeNumber;}
	public String getRotationAxis() 							{return rotationAxis;}
	public void setRotationAxis(String rotationAxis) 			{this.rotationAxis = rotationAxis;}
	public String getAxisStart() 								{return axisStart;}
	public void setAxisStart(String axisStart) 					{this.axisStart = axisStart;}
	public String getAxisEnd() 									{return axisEnd;}
	public void setAxisEnd(String axisEnd) 						{this.axisEnd = axisEnd;}
	public String getExposureTime() 							{return exposureTime;}
	public void setExposureTime(String exposureTime) 			{this.exposureTime = exposureTime;}
	public String getTransmission() 							{return transmission;}
	public void setTransmission(String transmission) 			{this.transmission = transmission;}
	public String getOscillationRange() 						{return oscillationRange;}
	public void setOscillationRange(String oscillationRange) 	{this.oscillationRange = oscillationRange;}
	public String getCompleteness() 							{return completeness;}
	public void setCompleteness(String completeness) 			{this.completeness = completeness;}
	public String getMultiplicity() 							{return multiplicity;}
	public void setMultiplicity(String multiplicity) 			{this.multiplicity = multiplicity;}
	public String getResolution() 								{return resolution;}
	public void setResolution(String resolution) 				{this.resolution = resolution;}
	public String getDoseTotal() 								{return doseTotal;}
	public void setDoseTotal(String doseTotal) 					{this.doseTotal = doseTotal;}
	public String getNumberOfImages() 							{return numberOfImages;}
	public void setNumberOfImages(String numberOfImages) 		{this.numberOfImages = numberOfImages;}
	public String getComments() 								{return comments;}
	public void setComments(String comments) 					{this.comments = comments;}
	
}
