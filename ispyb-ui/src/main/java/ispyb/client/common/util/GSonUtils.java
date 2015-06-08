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
package ispyb.client.common.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GSonUtils {

	
	public static Gson getGson(String dateFormat){
		return new GsonBuilder().setDateFormat(dateFormat).excludeFieldsWithModifiers(Modifier.PRIVATE).serializeNulls().create();
	}
	
	public static String getErrorMessage(Exception exp){
		HashMap<String, String> error = new HashMap<String, String>();
		StringWriter sw = new StringWriter();
		exp.printStackTrace(new PrintWriter(sw));
		error.put("trace", sw.toString());
		if (exp.getMessage() != null){
			error.put("message", exp.getMessage());
		}
		return new Gson().toJson(error);
	};
	
	public static void sendToJs(HttpServletResponse response, Object o){
		sendToJs(response, o, "dd-MM-yyyy"); 
	}
	
	public static void sendToJs(HttpServletResponse response, Object o, String dateFormat){

		try {
			Gson gson = getGson(dateFormat);
			String json = gson.toJson(o);
			response.setContentType("application/json; charset=UTF-8");
			response.setHeader("Cache-Control","no-cache");
			response.getWriter().flush();
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
			try {
				response.getWriter().write( getErrorMessage(e));
				response.setStatus(500);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
		
	}
}
