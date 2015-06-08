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
	String targetViewSampleDetails = request.getContextPath() + "/menuSelected.do?leftMenuId=40&topMenuId=10&targetUrl=/user/viewSample.do?reqCode=displayAllDetails";
%>

<%-- Selected Sample -----------------------------------------------------------------------------%>
<logic:present name="breadCrumbsForm" property="selectedSample" scope="session">
	<bean:define name="breadCrumbsForm" property="selectedSample" 	scope="session" id="selectedSample" 	type="ispyb.server.mx.vos.sample.BLSample3VO"/>
	
	<layout:column styleClass="PANEL_BREAD_CRUMBS">
		<layout:panel key="Selected Sample" align="left" styleClass="PANEL_BREAD_CRUMBS_ITEM">
			<layout:text key="Name" 	name="selectedSample" 	property="name" styleClass="PANEL_BREAD_CRUMBS_FIELD" mode="I,I,I" />
			<tr><th class="PANEL_BREAD_CRUMBS_FIELD" valign="top"></th><td class="PANEL_BREAD_CRUMBS_FIELD" valign="top" style="">
				<html:link href="<%=targetViewSampleDetails%>" paramName="selectedSample" paramId="blSampleId" paramProperty="blSampleId" styleClass="PANEL_BREAD_CRUMBS_FIELD">
					Sample details
				</html:link>
			<tr><th class="PANEL_BREAD_CRUMBS_FIELD" valign="top"></th><td class="PANEL_BREAD_CRUMBS_FIELD" valign="top" style="">
			
			<%-- Link to Crystal details at originating Lab --%>
			<logic:present name="breadCrumbsForm" property="selectedSampleImageURLPrenset" scope="session">
				<logic:equal name="breadCrumbsForm"  property="selectedSampleImageURLPrenset" value="true">
				<bean:define name="breadCrumbsForm" property="selectedSampleImageURL" 	scope="session" id="selectedSampleImageURL" 	type="java.lang.String"/>
					<layout:link href="<%=selectedSampleImageURL%>" styleClass="PANEL_BREAD_CRUMBS_FIELD" target="_new">
					<img src="<%=request.getContextPath()%>/images/crystal.gif" border=0  vspace="2" align="middle">View Crystal details from originating Lab.
					</layout:link>
				</logic:equal>
			</logic:present>
			<%-- ------------------------------------------- --%>
		</layout:panel>
	</layout:column>
</logic:present>
