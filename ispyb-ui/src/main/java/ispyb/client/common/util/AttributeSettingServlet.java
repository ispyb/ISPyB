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

import ispyb.common.util.Constants;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

/**
 * This servlet is only used to setup context attributes on startup.
 * 
 * Please configure it in the applications servlets.xml
 * 
 */
public class AttributeSettingServlet extends HttpServlet {
	
	private final static Logger LOG = Logger.getLogger(AttributeSettingServlet.class);

    /**
     * Initializes the SITE_ATTRIBUTE attribute based on the SITE_PROPERTY property.
     */
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        context.setAttribute(Constants.SITE_ATTRIBUTE,Constants.getProperty(Constants.SITE_PROPERTY));
        LOG.info("Site is: " + Constants.getProperty(Constants.SITE_PROPERTY));
        
        context.setAttribute(Constants.SITE_AUTHENTICATION_METHOD_ATTRIBUTE,Constants.SITE_AUTHENTICATION_METHOD);
        context.setAttribute(Constants.PROPOSAL_LIST_DISPLAY_ATTRIBUTE, Constants.PROPOSAL_LIST_DISPLAY);
        context.setAttribute(Constants.BCM_ATTRIBUTE, Constants.BCM);
    }
}
