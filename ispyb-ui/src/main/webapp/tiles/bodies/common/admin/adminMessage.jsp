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

<style type="text/css">
  .rowAlignCenter  {
    margin-left: auto;
    margin-right: auto;
    }
</style>

<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<%-- Errors --%>
<jsp:include page="/tiles/common/messages/error.jsp" flush="true" />

<BR>
<%-- Page --%>
<layout:grid cols="1"  borderSpacing="3" align="center">
<%-- Shipping --%>
<layout:panel key="User messages" align="left" styleClass="PANEL">
	<layout:form action="/manager/adminSite.do" reqCode="submitMessages">	
		<layout:column>
			<layout:textarea	key="Warning message" 	property="warningMessage"		mode="E,E,E"	size="75" rows="3"		styleClass="FIELD"/>
			<layout:textarea	key="Welcome message" 	property="infoMessage"			mode="E,E,E"	size="75" rows="7"		styleClass="FIELD"/>
		</layout:column>
		
		<layout:row styleClass="rowAlignCenter">
			<layout:reset/>
			<layout:submit reqCode="submitMessages"><layout:message key="Submit"/></layout:submit>
			<layout:submit reqCode="display"><layout:message key="Cancel"/></layout:submit>
		</layout:row>
	</layout:form>
</layout:panel>	

<%-- Acknowledge Action --%>
<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />

</layout:grid>
