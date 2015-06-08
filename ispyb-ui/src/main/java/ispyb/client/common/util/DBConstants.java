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

package ispyb.client.common.util;

/**
 * contains all constants linked to the database (field length)
 * @author BODIN
 *
 */
public final class DBConstants {
	// Session table: beamLineOperator
	public static final int MAX_LENGTH_SESSION_BEAMLINEOPERATOR = 45;
	// Session table: comments
	public static final int MAX_LENGTH_SESSION_COMMENTS = 2000;
	// Session table: sessionTitle
	public static final int MAX_LENGTH_SESSION_SESSIONTITLE = 255;
	
	// LabContact table: cardName
	public static final int MAX_LENGTH_LABCONTACT_CARDNAME = 40;
	// LabContact table: defaultCourrierCompany
	public static final int MAX_LENGTH_LABCONTACT_DEFAULTCOURRIERCOMPANY = 45;
	// LabContact table: courierAccount
	public static final int MAX_LENGTH_LABCONTACT_COURIERACCOUNT = 45;
	// LabContact table: billingReference
	public static final int MAX_LENGTH_LABCONTACT_BILLINGREFERENCE = 45;
	
	// Person table: phone number
	public static final int MAX_LENGTH_PERSON_PHONENUMBER = 45;
	// Person table: fax number
	public static final int MAX_LENGTH_PERSON_FAXNUMBER = 45;
	// Person table: email address
	public static final int MAX_LENGTH_PERSON_EMAILADRESS = 45;
	
	// Laboratory table: name
	public static final int MAX_LENGTH_LABORATORY_NAME = 45;
	// Laboratory table: address
	public static final int MAX_LENGTH_LABORATORY_ADDRESS = 255;
	
	// Shipping table: name
	public static final int MAX_LENGTH_SHIPPING_SHIPPINGNAME = 45;
	// Shipping table: comments
	public static final int MAX_LENGTH_SHIPPING_COMMENTS = 255;
	
	// Dewar table: code
	public static final int MAX_LENGTH_DEWAR_CODE = 45;
	// Dewar table: courier trackingNumberToSynchrotron
	public static final int MAX_LENGTH_DEWAR_TRACKINGNUMBERTOSYNCHROTRON = 30;
	
	// Sample table: name
	public static final int MAX_LENGTH_SAMPLE_NAME = 45;
	// Sample table: code
	public static final int MAX_LENGTH_SAMPLE_CODE = 45;
	// Sample table: location
	public static final int MAX_LENGTH_SAMPLE_LOCATION = 45;
	// Sample table: comments
	public static final int MAX_LENGTH_SAMPLE_COMMENTS = 1024;
	// Sample table: blSampleStatus
	public static final int MAX_LENGTH_SAMPLE_BLSAMPLESTATUS = 20;
	
	// DiffractionPlan table: anomalousScatterrer
	public static final int MAX_LENGTH_DIFFRACTIONPLAN_ANOMALOUSSCATTERRER = 255;
	// DiffractionPlan table: comments
	public static final int MAX_LENGTH_DIFFRACTIONPLAN_COMMENTS = 1024;
	
	// Crystal table: morphology
	public static final int MAX_LENGTH_CRYSTAL_MORPHOLOGY = 255;
	// Crystal table: color
	public static final int MAX_LENGTH_CRYSTAL_COLOR = 45;
	// Crystal table: comments
	public static final int MAX_LENGTH_CRYSTAL_COMMENTS = 255;
	
}
