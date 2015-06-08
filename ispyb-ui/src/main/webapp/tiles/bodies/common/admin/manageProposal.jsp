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
<%@page import="java.util.Calendar,java.util.GregorianCalendar"%>

<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<%-- Page --%>
<layout:column>
	<layout:panel key="Reassign proposals" align="left" styleClass="PANEL">
		<layout:form action="/user/manageProposalAction.do" reqCode="reassignProposals">	
			<layout:grid cols="2" styleClass="SEARCH_GRID">	
				<layout:column>
					<layout:textarea key="Old Proposals" property="oldProposals" styleClass="FIELD"	mode="E,E,E"	size="10" rows="10"/>
				</layout:column>		
					<layout:textarea key="New Proposal" property="newProposals" styleClass="FIELD"	mode="E,E,E"	size="10" rows="10"/>
				<layout:column>
					
				</layout:column>
				<layout:row>
					<layout:submit reqCode="reassignProposals"><layout:message key="Submit"/></layout:submit>
				</layout:row>
			</layout:grid>
		</layout:form>	
	</layout:panel>

	<%-- Acknowledge Action --%>
	<jsp:include page="/tiles/common/messages/error.jsp" flush="true" />
	<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />
</layout:column>
