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
 * ShipmentCourrierSuggestAction.java
 */

package ispyb.client.common.ajax;

import ispyb.common.util.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ShipmentCourrierSuggestAction extends fr.improve.struts.taglib.layout.suggest.SuggestAction {

	@Override
	public Collection<String> getSuggestionList(javax.servlet.http.HttpServletRequest in_request, String in_word) {

		// Get all the courrier names
		Collection<String> allCourriers = new ArrayList<String>();
		allCourriers.add(Constants.SHIPPING_DELIVERY_AGENT_NAME_FEDEX);
		allCourriers.add(Constants.SHIPPING_DELIVERY_AGENT_NAME_TNT);
		allCourriers.add(Constants.SHIPPING_DELIVERY_AGENT_NAME_CIBLEX);
		allCourriers.add(Constants.SHIPPING_DELIVERY_AGENT_NAME_DHL);
		allCourriers.add(Constants.SHIPPING_DELIVERY_AGENT_NAME_FASTRANS);
		allCourriers.add(Constants.SHIPPING_DELIVERY_AGENT_NAME_TAT);
		allCourriers.add(Constants.SHIPPING_DELIVERY_AGENT_NAME_UPS);
		allCourriers.add(Constants.SHIPPING_DELIVERY_AGENT_NAME_WORLDCOURIER);
		allCourriers.add(Constants.SHIPPING_DELIVERY_AGENT_NAME_CHRONOPOST);

		// Start to build the suggestions list
		ArrayList<String> suggestions = new ArrayList<String>();

		if (in_word != null && in_word.length() > 0) {
			Iterator<String> iter = allCourriers.iterator();

			while (iter.hasNext()) {
				String currentWord = iter.next();

				if (currentWord.toLowerCase().startsWith(in_word.toLowerCase()))
					suggestions.add(currentWord);
			}
		}

		return suggestions;
	}
}
