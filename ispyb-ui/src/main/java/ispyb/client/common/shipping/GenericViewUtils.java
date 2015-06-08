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
 * ViewSampleAction.java
 */

package ispyb.client.common.shipping;


public class GenericViewUtils {

	// public static String getBeamlineOperatorSqlLike(HttpServletRequest request) {
	// // Retrieve the user family name;local contact name = beamline operator
	// String beamlineOperatorLastName = ((String) request.getSession().getAttribute(Constants.LDAP_LastName))
	// .toUpperCase();
	// String beamlineOperatorFirstName = ((String) request.getSession().getAttribute(Constants.LDAP_GivenName))
	// .toUpperCase();
	// beamlineOperatorLastName = beamlineOperatorLastName.replace(' ', '%');
	// beamlineOperatorLastName = beamlineOperatorLastName.replace('-', '%');
	// String beamlineOperatorNameSqlLike = beamlineOperatorLastName;
	// if (beamlineOperatorFirstName != null && beamlineOperatorFirstName.length() > 0) {
	// // Just because there are 1 or 2 black spaces between lastname and firstname in the database...
	// beamlineOperatorNameSqlLike += " %" + beamlineOperatorFirstName.substring(0, 1);
	//
	// }
	// return beamlineOperatorNameSqlLike;
	// }

}
