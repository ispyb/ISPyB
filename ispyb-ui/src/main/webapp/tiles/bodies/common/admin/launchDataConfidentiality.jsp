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

<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<h3> Launch data confidentiality</h3>

<layout:row>

<layout:panel key="Data confidentiality " align="left" styleClass="PANEL">
<layout:form action="/manager/launchDataConfidentiality.do" reqCode="launch">
	
	<layout:grid cols="1" styleClass="SEARCH_GRID">	
		<layout:column>
					<layout:text key="date From (dd/mm/yyyy)" 	property="dateFrom" 	styleClass="FIELD" 	mode="E,E,E"/>
					<layout:text key="date To (dd/mm/yyyy)" 	property="dateTo" 	styleClass="FIELD" 	mode="E,E,E"/>
		</layout:column>		
			
		<layout:row>
			<layout:reset/>
			<layout:submit><layout:message key="Launch"/></layout:submit>
		</layout:row>
	</layout:grid>
	
</layout:form>	
</layout:panel>


</layout:row>

<%-- Acknowledge Action --%>
<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />

<%-- Errors --%>
<jsp:include page="/tiles/common/messages/error.jsp" flush="true" />
