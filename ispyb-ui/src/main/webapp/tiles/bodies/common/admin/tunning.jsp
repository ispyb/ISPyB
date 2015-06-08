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

<% 
   
%>

<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>


<layout:panel key="Change Proposal" align="left" styleClass="PANEL">
<layout:form action="/user/tunningAction.do" reqCode="changeCurrentProposal">
	<layout:grid cols="2" styleClass="SEARCH_GRID">	
		<layout:column>
			<layout:text key="Proposal Code" 	property="proposalCode" 	styleClass="FIELD" mode="E,E,E" />
			<layout:text key="Proposal Number" 	property="proposalNumber" 	styleClass="FIELD" mode="E,E,E" />
		</layout:column>		
		<layout:column>
			<layout:submit><layout:message key="Save"/></layout:submit>
		</layout:column>
	</layout:grid>
</layout:form>
</layout:panel>
