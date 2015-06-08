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

<p>If you have any questions of comments, feel free to submit your feedback:</p>

<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>


<%-- Page --%>
<layout:grid cols="1"  borderSpacing="5">
<%-- Shipping --%>
<layout:panel key="Submit your feedback" align="left" styleClass="PANEL">
	<layout:form action="/user/SendMailAction.do" reqCode="sendMail">	
		<layout:column>
			<layout:text		key="Your email" 	property="senderEmail"	mode="E,E,E"	size="50" 				styleClass="FIELD"/>
			<layout:textarea	key="" 				property="body"			mode="E,E,E"	size="50" rows="10"		styleClass="FIELD"/>
		</layout:column>
		
		<layout:row>
			<layout:reset/>
			<layout:submit reqCode="sendMail"><layout:message key="Submit your feedback"/></layout:submit>
		</layout:row>
	</layout:form>
</layout:panel>	
	
	
	
	
</layout:grid>
