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

<%
	String targetProtein = request.getContextPath() + "/menuSelected.do?leftMenuId=39&topMenuId=10&targetUrl=/user/viewSample.do?reqCode=displayForProtein";
	String targetProteins = request.getContextPath() + "/menuSelected.do?leftMenuId=23&topMenuId=10&targetUrl=/user/viewProteinAndCrystal.do?reqCode=display";
%>

<%-- Selected Protein only displayed if not crystal form selected   -----------------------------------------------%>

<logic:notPresent name="breadCrumbsForm" property="selectedCrystal" scope="session">
<logic:present name="breadCrumbsForm" property="selectedProtein" scope="session">
	<bean:define name="breadCrumbsForm" property="selectedProtein" 	scope="session" id="selectedProtein" 	type="ispyb.server.mx.vos.sample.Protein3VO"/>
	<%-- Selected Protein Info --%>
	<layout:column styleClass="PANEL_BREAD_CRUMBS">
		<layout:panel key="Selected Protein" align="left" styleClass="PANEL_BREAD_CRUMBS_ITEM">
			<layout:text key="Name" 	name="selectedProtein" 	property="name" styleClass="PANEL_BREAD_CRUMBS_FIELD" mode="I,I,I" />
			<layout:text key="Acronym" 	name="selectedProtein" 	property="acronym" styleClass="PANEL_BREAD_CRUMBS_FIELD" mode="I,I,I" />
			<tr><th class="PANEL_BREAD_CRUMBS_FIELD" valign="top"></th><td class="PANEL_BREAD_CRUMBS_FIELD" valign="top" style="">
				<html:link href="<%=targetProtein%>" paramName="selectedProtein" paramId="proteinId" paramProperty="proteinId" styleClass="PANEL_BREAD_CRUMBS_FIELD">
					Back to this protein
				</html:link>
			</td></tr>
			<tr><th class="PANEL_BREAD_CRUMBS_FIELD" valign="top"></th><td class="PANEL_BREAD_CRUMBS_FIELD" valign="top" style="">
	            <html:link href="<%=targetProteins%>" styleClass="PANEL_BREAD_CRUMBS_FIELD">
					Back to proteins
				</html:link>	
			</td></tr>		
		</layout:panel>
	</layout:column>
</logic:present>
</logic:notPresent>
