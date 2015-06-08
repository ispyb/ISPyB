<%--------------------------------------------------------------------------------------------------
This file is part of ISPyB.

ISPyB is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

ISPyB is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with ISPyB.  If not, see <http://www.gnu.org/licenses/>.

Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau, M. Bodin, A. De Maria Antolinos
--------------------------------------------------------------------------------------------------%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.application-servers.com/layout" prefix="layout" %>

<%@page import="ispyb.common.util.Constants"%>


<%-- BreadCrumbs bar --%>
<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />

<layout:skin />

<bean:define name="viewResultsForm" id="image" type="ispyb.client.mx.results.ViewResultsForm"/>

<layout:grid cols="1"  borderSpacing="10">
	<layout:column>
		<layout:panel key="Java Based Image Display for Protein Crystallography" align="center" styleClass="PANEL">

			<jsp:plugin type="applet" 
				code="MainApplet.class" 
				codebase="<%=request.getContextPath()%>/ispyb/applet"
				archive="<%=request.getContextPath()%>/applet/lib/image.jar" 
				jreversion="1.4" 
			  	align="center" height="570" width="433"
			  	nspluginurl="http://www.java.com/en/download/" 
			  	iepluginurl="http://www.java.com/en/download/windows_automatic.jsp" >
			   <jsp:params>	
			      	<jsp:param name="imageUrl" value="<%=image.getImageUrl()%>" />	
			      	<jsp:param name="detectorType" value="<%=image.getDetectorType()%>" />
			   </jsp:params>	
			   <jsp:fallback>	
			      <p>Unable to load applet</p>	
			   </jsp:fallback>	
			</jsp:plugin>

		</layout:panel>
	</layout:column>

</layout:grid>

	<p>This facility is a Java Applet courtesy of <i>John W. Campbell</i>&nbsp;<img src="http://www.ccp4.ac.uk/jwc/image_applet/jwc.gif" align="middle"/>
	</p>
	<p><img src="http://www.ccp4.ac.uk/jwc/image_applet/jdlimg.gif" align="middle"/>&nbsp;  For more details visit the web page of <a href="http://www.ccp4.ac.uk/jwc/image_applet/ImageDisplay_ccp4.html">Java Based Image Display for Protein Crystallography</a>
	</p>
