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

package ispyb.client.biosaxs.dataAdapter;

import ispyb.server.biosaxs.services.BiosaxsServices;
import ispyb.server.common.util.LoggerFormatter;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


	

	/**
	 * @struts.action name="MobileBiosaxsDataAdapterAction" path="/mobiledataadapter"
	 *                type="ispyb.client.biosaxs.dataAdapter.MobileBiosaxsDataAdapterAction"  validate="false"
	 *                parameter="reqCode" scope="request"
	 *                
     * @struts.action-forward name="goToEditProject" path="/user/editProjectJS.do?reqCode=display"
	 * 
	 */
	public class MobileBiosaxsDataAdapterAction extends org.apache.struts.actions.DispatchAction {
		private final static Logger LOGGER = Logger.getLogger(MobileBiosaxsDataAdapterAction.class);
		protected static Calendar NOW = GregorianCalendar.getInstance();
		

		/** This class contains all the business logic **/
		BiosaxsActions biosaxsActions = new BiosaxsActions();
		private BiosaxsServices biosaxsWebServiceActions = new BiosaxsServices();
		
		private void initServices() {
		}

		
		@Override
		public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
				final HttpServletResponse response) throws Exception {
			this.initServices();
			return super.execute(mapping, form, request, response);
		}
		
		private void logEnd(String methodName, long start) {
			long time = System.currentTimeMillis();
			 LoggerFormatter.log(LOGGER, LoggerFormatter.Package.BIOSAXS_MOBILE, methodName, start, time,  time - start);
		}

		private Boolean checkSecurity(HttpServletRequest request){
			String user = request.getParameter("user");
			String password = request.getParameter("password");
			return (user.equals("opd29")&&(password.equals("0a2bc9f9ffcc617c6c37b8615bd4d0a497470c5c")));
		}
		/**
		 * @param info
		 */
		public ActionForward info(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws IOException {
			long start = BiosaxsDataAdapterCommon.logMethod("getAnalysisInformationByExperimentId", request, LoggerFormatter.Package.BIOSAXS_MOBILE);
			try{
				if (checkSecurity(request)){
					int limit = Integer.parseInt(request.getParameter("limit"));
					if (limit > 300){
						limit = 300;
					}
					
					List<Map<String, Object>> result =  this.biosaxsWebServiceActions.getAnalysisInformation(limit);
					Gson gson = new GsonBuilder().serializeNulls().create();
					this.logEnd("getAnalysisInformation", start);
					BiosaxsDataAdapterCommon.sendResponseToclient(response, "callback("+gson.toJson(result)+")");
				}
			}
			catch(Exception exp){
				BiosaxsDataAdapterCommon.logError("getAnalysisInformationByExperimentId", start, exp);
				response.getWriter().write(BiosaxsDataAdapterCommon.getErrorMessage(exp));
				response.setStatus(500);
				return null;
			}
			return null;	
		}
		
		
		/**
		 * getImage
		 * 
		 * @param mapping
		 * @param actForm
		 * @param request
		 * @param in_reponse
		 * @return
		 * @throws Exception 
		 */
		public ActionForward getImage(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
			long start = BiosaxsDataAdapterCommon.logMethod("getImage", request, LoggerFormatter.Package.BIOSAXS_MOBILE);
			try{
				if (this.checkSecurity(request)){
					String type							= request.getParameter("type");
					String dataCollectionId				= request.getParameter("dataCollectionId");
					String filePath = this.biosaxsActions.getSubstractedFileById(type, Integer.parseInt(dataCollectionId));
					if (filePath != null){
						BiosaxsDataAdapterCommon.sendImageToClient(filePath, response);
					}
					this.logEnd("getImage", start);
				}
			}
			catch(Exception exp){
				response.setStatus(500);
				return null;
			}
			return null;
		};
}
