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

import ispyb.server.mx.vos.screening.ScreeningStrategySubWedge3VO;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class ScreeningStrategySubWedgeValueInfo extends ScreeningStrategySubWedge3VO {
	private static final long serialVersionUID = 0;
	
	public ScreeningStrategySubWedgeValueInfo(ScreeningStrategySubWedge3VO screeningStrategySubWedgeValue)
	{
		super(screeningStrategySubWedgeValue);
		try
		{			
			NumberFormat  nf1 = NumberFormat.getIntegerInstance();
			DecimalFormat df1 = (DecimalFormat)NumberFormat.getInstance(Locale.US);
		    df1.applyPattern("#####0.0");	   
			DecimalFormat df2 = (DecimalFormat)NumberFormat.getInstance(Locale.US);
		    df2.applyPattern("#####0.00");	   
		    DecimalFormat df3 = (DecimalFormat)NumberFormat.getInstance(Locale.US);
		    df3.applyPattern("#####0.000");	   

			
				
			// Format data
		    Double axisStartf = null;
		    Double axisEndf = null;
		    Double exposureTimef = null;
		    Double transmissionf = null;
		    Double oscillationRangef = null;
		    Double completenessf = null;
		    Double multiplicityf  = null; 
		    Double doseTotalf = null;
		    
		    if (this.getAxisStart() != null)
		    	axisStartf = new Double(df1.format(this.getAxisStart()));
		    if (this.getAxisEnd() != null)
		    	axisEndf = new Double(df1.format(this.getAxisEnd()));
		    if (this.getExposureTime() != null)
		    	exposureTimef = new Double(df3.format(this.getExposureTime()));
		    if (this.getTransmission() != null)
		    	transmissionf = new Double(df1.format(this.getTransmission()));
		    if (this.getOscillationRange() != null)
		    	oscillationRangef = new Double(df2.format(this.getOscillationRange()));
			if (this.getMultiplicity() != null)
		    	multiplicityf = new Double(df1.format(this.getMultiplicity()));
		    if (this.getCompleteness() != null)
		    	completenessf = new Double(df1.format(this.getCompleteness().doubleValue()*100));
		    if (this.getDoseTotal() != null)
		    	doseTotalf = new Double(df1.format(this.getDoseTotal()));
		    
		    this.setAxisStart(axisStartf);
		    this.setAxisEnd(axisEndf);
		    this.setExposureTime(exposureTimef);
		    this.setTransmission(transmissionf);
		    this.setOscillationRange(oscillationRangef);
		    this.setMultiplicity(multiplicityf);
		    this.setCompleteness(completenessf);
		    this.setDoseTotal(doseTotalf);
		   
		    
		}
	catch (Exception e) {}		
	}

	
	
}
