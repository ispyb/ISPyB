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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="ispyb.common.util.Constants"%>
<%@page import="ispyb.common.util.beamlines.ESRFBeamlineEnum"%>
<%@page import="ispyb.common.util.beamlines.EMBLBeamlineEnum"%>
<%@page import="ispyb.common.util.beamlines.MAXIVBeamlineEnum"%>
<%@page import="ispyb.common.util.beamlines.SOLEILBeamlineEnum"%>

<% 
   String targetSelectedSession = request.getContextPath() + "/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/viewSessionSummary.do?reqCode=display";
   String targetSessions = request.getContextPath() + "/user/viewSession.do?reqCode=displayLast";
%>

<%-- Selected Session -----------------------------------------------------------------------------%>
<logic:present name="breadCrumbsForm" property="selectedSession" scope="session">
	<bean:define name="breadCrumbsForm" property="selectedSession" 	scope="session" id="selectedSession" 	type="ispyb.server.mx.vos.collections.Session3VO"/>	<%-- Selected Session Info --%>
	
	<layout:column styleClass="PANEL_BREAD_CRUMBS">
		<layout:panel key="<%=Constants.SELECTED_SESSION_VISIT%>" align="left" styleClass="PANEL_BREAD_CRUMBS_ITEM">
			<layout:text key="Proposal" 	name="selectedSession" 	property="proposalVO.proposalAccount" styleClass="PANEL_BREAD_CRUMBS_FIELD" mode="I,I,I" />
		
			<layout:text key="Start Date" 	name="selectedSession" 	property="startDate" styleClass="PANEL_BREAD_CRUMBS_FIELD" mode="I,I,I" type="date" />
						
			<layout:text key="BeamLine" 	name="selectedSession" 	property="beamlineName" styleClass="PANEL_BREAD_CRUMBS_FIELD" mode="I,I,I">
			<c:if test="${SITE_ATTRIBUTE eq 'ESRF'}">
				<%=ESRFBeamlineEnum.retrievePhoneNumberWithName(selectedSession.getBeamlineName())%>
			</c:if>
			<c:if test="${SITE_ATTRIBUTE eq 'EMBL'}">
				<%=EMBLBeamlineEnum.retrievePhoneNumberWithName(selectedSession.getBeamlineName())%>
			</c:if>
			<c:if test="${SITE_ATTRIBUTE eq 'MAXIV'}">
				<%=MAXIVBeamlineEnum.retrievePhoneNumberWithName(selectedSession.getBeamlineName())%>
			</c:if>					
			<c:if test="${SITE_ATTRIBUTE eq 'SOLEIL'}">
				<%=SOLEILBeamlineEnum.retrievePhoneNumberWithName(selectedSession.getBeamlineName())%>
			</c:if>
			</layout:text>
			<tr><th class="PANEL_BREAD_CRUMBS_FIELD" valign="top"></th><td class="PANEL_BREAD_CRUMBS_FIELD" valign="top" style="">
	            <html:link href="<%=targetSelectedSession%>" paramName="selectedSession" paramId="sessionId" paramProperty="sessionId" styleClass="PANEL_BREAD_CRUMBS_FIELD">
					<%=Constants.BACK_TO_THIS_SESSION_VISIT%>
				</html:link>
			</td></tr>
			<tr><th class="PANEL_BREAD_CRUMBS_FIELD" valign="top"></th><td class="PANEL_BREAD_CRUMBS_FIELD" valign="top" style="">
	            <html:link href="<%=targetSessions%>" styleClass="PANEL_BREAD_CRUMBS_FIELD">
					Back to <%=Constants.SESSIONS%>
				</html:link>
			</td></tr>
		</layout:panel>
	</layout:column>
</logic:present>
