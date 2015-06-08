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

import ispyb.server.common.services.admin.AdminActivity3Service;
import ispyb.server.common.services.admin.AdminVar3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.admin.AdminActivity3VO;
import ispyb.server.common.vos.admin.AdminVar3VO;

import java.sql.Timestamp;
import java.util.List;

public class AdminUtils {

	private static final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	public String getValue(String varName) {

		String value = "";

		try {
			AdminVar3Service adminVarService = (AdminVar3Service) ejb3ServiceLocator
					.getLocalService(AdminVar3Service.class);

			List<AdminVar3VO> var = adminVarService.findByName(varName);
			if (var.size() == 0)
				return "unknown var";
			AdminVar3VO myValue = var.get(0);
			if (myValue == null)
				return "unknown var";
			value = myValue.getValue();
		} catch (Exception e) {
			e.printStackTrace();
			return "error in getting var";
		}

		return value;
	}

	public static void logActivity(String username, String action, String comment) {

		try {
			// Timestamp
			java.util.Date today = new java.util.Date();
			Timestamp dateTime = new java.sql.Timestamp(today.getTime());

			// long start = System.currentTimeMillis();

			// Fetch record
			AdminActivity3Service activity = (AdminActivity3Service) ejb3ServiceLocator
					.getLocalService(AdminActivity3Service.class);
			// AdminActivityValue myActivity = activity.findByUsername(username);
			List<AdminActivity3VO> myActivities = (List<AdminActivity3VO>) activity.findByUsername(username);

			if (myActivities != null && myActivities.size() > 0) {
				// Update record
				AdminActivity3VO myActivity = myActivities.get(0);
				myActivity.setAction(action);
				myActivity.setComments(comment);
				myActivity.setDateTime(dateTime);
				activity.update(myActivity);
			} else {
				// Insert record
				AdminActivity3VO newValue = new AdminActivity3VO();
				newValue.setUsername(username);
				newValue.setAction(action);
				newValue.setComments(comment);
				newValue.setDateTime(dateTime);
				activity.create(newValue);
			}

			// long elapsed = System.currentTimeMillis() - start;
			// System.out.println("Elapsed time : "+elapsed+ "ms");

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		return;
	}

}
