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

import java.util.ArrayList;
import java.util.List;

public class StrategyWedgeInformation {
	
	private String wedgeNumber 		= "";
	private String resolution 		= "";
	private String completeness		= "";
	private String multiplicity		= "";
	private String doseTotal		= "";
	private String numberOfImages 	= "";
	private String phi			 	= "";
	private String kappa		 	= "";
	private String wavelength 		= "";
	private String comments			= "";
	
	private List<StrategySubWedgeInformation> listStrategySubWedgeInformation= new ArrayList<StrategySubWedgeInformation>();
	
	
	public String getWedgeNumber() 								{return wedgeNumber;}
	public void setWedgeNumber(String wedgeNumber) 				{this.wedgeNumber = wedgeNumber;}
	public String getResolution() 								{return resolution;}
	public void setResolution(String resolution) 				{this.resolution = resolution;}
	public String getCompleteness() 							{return completeness;}
	public void setCompleteness(String completeness) 			{this.completeness = completeness;}
	public String getMultiplicity() 							{return multiplicity;}
	public void setMultiplicity(String multiplicity) 			{this.multiplicity = multiplicity;}
	public String getDoseTotal() 								{return doseTotal;}
	public void setDoseTotal(String doseTotal) 					{this.doseTotal = doseTotal;}
	public String getNumberOfImages() 							{return numberOfImages;}
	public void setNumberOfImages(String numberOfImages) 		{this.numberOfImages = numberOfImages;}
	public String getPhi() 										{return phi;}
	public void setPhi(String phi) 								{this.phi = phi;}
	public String getKappa() 									{return kappa;}
	public void setKappa(String kappa) 							{this.kappa = kappa;}
	public String getWavelength() 								{return wavelength;}
	public void setWavelength(String wavelength) 				{this.wavelength = wavelength;}
	public String getComments() 								{return comments;}
	public void setComments(String comments) 					{this.comments = comments;}
	
	public List<StrategySubWedgeInformation> getListStrategySubWedgeInformation() {return listStrategySubWedgeInformation;}
	public void setListStrategySubWedgeInformation(List<StrategySubWedgeInformation> listStrategySubWedgeInformation) {this.listStrategySubWedgeInformation = listStrategySubWedgeInformation;}
}
