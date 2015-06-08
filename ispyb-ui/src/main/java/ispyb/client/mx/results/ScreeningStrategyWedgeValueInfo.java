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
package ispyb.client.mx.results;


import ispyb.server.mx.vos.screening.ScreeningStrategyWedge3VO;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class ScreeningStrategyWedgeValueInfo extends ScreeningStrategyWedge3VO{
	private static final long serialVersionUID = 0;
	
	public ScreeningStrategyWedgeValueInfo(ScreeningStrategyWedge3VO screeningStrategyWedgeValue)
	{
		super(screeningStrategyWedgeValue);
		try
		{			
			NumberFormat  nf1 = NumberFormat.getIntegerInstance();
			DecimalFormat df1 = (DecimalFormat)NumberFormat.getInstance(Locale.US);
		    df1.applyPattern("#####0.0");	   
			DecimalFormat df2 = (DecimalFormat)NumberFormat.getInstance(Locale.US);
		    df2.applyPattern("#####0.00");	   

			
				
			// Format data
		    Double resolutionf = null;
		    Double completenessf = null;
		    Double multiplicityf  = null; 
		    Double doseTotalf = null;
		    Double phif  = null;
		    Double kappaf = null;
		    Double wavelengthf = null;
		    
			if (this.getResolution() != null)
				resolutionf = new Double(df2.format(this.getResolution()));
			if (this.getMultiplicity() != null)
		    	multiplicityf = new Double(df1.format(this.getMultiplicity()));
		    if (this.getCompleteness() != null)
		    	completenessf = new Double(df1.format(this.getCompleteness().doubleValue()*100));
		    if (this.getDoseTotal() != null)
		    	doseTotalf = new Double(df1.format(this.getDoseTotal()));
		    if (this.getPhi() != null)
		    	phif = new Double(df1.format(this.getPhi()));
		    if (this.getKappa() != null)
		    	kappaf = new Double(df1.format(this.getKappa()));
		    if (this.getWavelength() != null)
		    	wavelengthf = new Double(df1.format(this.getWavelength()));
		    
		    
		    this.setResolution(resolutionf);
		    this.setMultiplicity(multiplicityf);
		    this.setCompleteness(completenessf);
		    this.setDoseTotal(doseTotalf);
		    this.setPhi(phif);
		    this.setKappa(kappaf);
		    this.setWavelength(wavelengthf);
		   
		    
		}
	catch (Exception e) {}		
	}

	
	
}
