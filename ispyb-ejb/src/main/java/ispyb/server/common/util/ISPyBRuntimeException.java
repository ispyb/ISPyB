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
package ispyb.server.common.util;

public class ISPyBRuntimeException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ISPyBRuntimeException(String message) {super(message);}
	
	public String toString()
	{
		String outString = getHeader();
		outString += this.getMessage() + "\n";
		outString += "***********************************************************************************\n";
		outString += "\n" + super.toString() +"\n";
		outString += "***********************************************************************************\n";
		return outString;
	}
	
	public static String getHeader()
	{
		String outString = "";
		
		outString += "\n";
		outString += "*********************\n";
		outString += "* [ISPyB Exception] *\n";
		outString += "*********************\n";
		
		
		return outString;
	}
		
}
