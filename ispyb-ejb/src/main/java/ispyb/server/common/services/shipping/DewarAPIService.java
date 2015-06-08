/*************************************************************************************************
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
 ****************************************************************************************************/

package ispyb.server.common.services.shipping;

import ispyb.server.common.vos.shipping.DewarAPIBean;

import java.util.ArrayList;
import java.util.Date;

import javax.ejb.Remote;

@Remote
public interface DewarAPIService {

	/**
	 * @param dewarBarCode
	 * @return
	 * 
	 */
	public boolean dewarExists(String dewarBarCode);

	/**
	 * Add event entry in DewarLocation table
	 * 
	 * @param dewarBarCode
	 * @param username
	 * @param dateTime
	 * @param location
	 * @param courierName
	 * @param trackingNumber
	 * @return
	 * 
	 */
	public boolean addDewarLocation(String dewarBarCode, String username, Date dateTime, String location,
			String courierName, String trackingNumber);

	/**
	 * Get possible dewar locations values from DewarLocationList table
	 * 
	 * @return
	 * 
	 * 
	 */
	public ArrayList<String> getDewarLocations();

	/**
	 * Fetch dewarAPI info
	 * 
	 * @param dewarBarCode
	 * @return
	 * 
	 */
	public DewarAPIBean fetchDewar(String dewarBarCode);

	/**
	 * Update Dewar table (dewarStatus, storageLocation, trackingNumberFromESRF) Update Shipping table (shippingStatus,
	 * return Courier, dateOfShippingToUser) Add entry in DewarTransportHistory table
	 * 
	 * @param dewarBarCode
	 * @param location
	 * @param courierName
	 * @param TrackingNumber
	 * @return
	 * 
	 */
	public boolean updateDewar(String dewarBarCode, String location, String courierName, String TrackingNumber);

}