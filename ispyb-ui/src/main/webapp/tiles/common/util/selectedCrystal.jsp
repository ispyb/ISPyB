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
	String targetCrystal = request.getContextPath() + "/menuSelected.do?leftMenuId=23&topMenuId=10&targetUrl=/user/viewSample.do?reqCode=displayForCrystal";
	String targetProtein = request.getContextPath() + "/menuSelected.do?leftMenuId=23&topMenuId=10&targetUrl=/user/viewSample.do?reqCode=displayForProtein";
%>

<%-- Selected Crystal and protein-----------------------------------------------------------------------------%>
<logic:present name="breadCrumbsForm" property="selectedCrystal" scope="session">
	<bean:define name="breadCrumbsForm" property="selectedCrystal" 	scope="session" id="selectedCrystal" 	type="ispyb.server.mx.vos.sample.Crystal3VO"/>
	<bean:define name="breadCrumbsForm" property="selectedProtein" 	scope="session" id="selectedProtein" 	type="ispyb.server.mx.vos.sample.Protein3VO"/>

	<%-- Selected Session Info --%>
	<layout:column styleClass="PANEL_BREAD_CRUMBS">
		<layout:panel key="Protein and Crystal Form" align="left" styleClass="PANEL_BREAD_CRUMBS_ITEM">
			<layout:text key="Acronym" 	name="selectedProtein" 	property="acronym" styleClass="PANEL_BREAD_CRUMBS_FIELD" mode="I,I,I" />
			<layout:link href="<%=targetProtein%>" paramName="selectedProtein" paramId="proteinId" paramProperty="proteinId" styleClass="PANEL_BREAD_CRUMBS_FIELD">
				Back to this protein
			</layout:link>
			<layout:text key="Space Group" 	name="selectedCrystal" 	property="spaceGroup" styleClass="PANEL_BREAD_CRUMBS_FIELD" mode="I,I,I" />
			
			<%-- Link to Crystal details at originating Lab --%>
			<logic:present name="breadCrumbsForm" property="selectedCrystalImageURLPresent" scope="session">
				<logic:equal name="breadCrumbsForm"  property="selectedCrystalImageURLPresent" value="true">
				<bean:define name="breadCrumbsForm"  property="selectedCrystalImageURL" 	scope="session" id="selectedCrystalImageURL" 	type="java.lang.String"/>
					<layout:link href="<%=selectedCrystalImageURL%>" styleClass="PANEL_BREAD_CRUMBS_FIELD" target="_new">
						<img src="<%=request.getContextPath()%>/images/crystal.gif" border=0  vspace="2" align="middle">View Crystal details from originating Lab.
					</layout:link>
				</logic:equal>
			</logic:present>
			<%-- ------------------------------------------- --%>
			
			<layout:link href="<%=targetCrystal%>" paramName="selectedCrystal" paramId="crystalId" paramProperty="crystalId" styleClass="PANEL_BREAD_CRUMBS_FIELD">
				Back to this crystal form
			</layout:link>
			
		</layout:panel>

<%--
	<layout:panel key="Protein and Crystal Form" align="left" styleClass="PANEL">
		<layout:column styleClass="PANEL_BREAD_CRUMBS_FIELD" >
			
			<layout:link href="<%=targetProtein%>" paramName="selectedProtein" paramId="proteinId" paramProperty="proteinId" styleClass="PANEL_BREAD_CRUMBS_FIELD">
				<layout:write name="selectedProtein" 	property="acronym" styleClass="PANEL_BREAD_CRUMBS_FIELD" />
			</layout:link>		
			<layout:link href="<%=targetCrystal%>" paramName="selectedCrystal" paramId="crystalId" paramProperty="crystalId" styleClass="PANEL_BREAD_CRUMBS_FIELD">
				<layout:write name="selectedCrystal" 	property="spaceGroup" styleClass="PANEL_BREAD_CRUMBS_FIELD" />
			</layout:link>
		</layout:column>
	</layout:panel>
--%>



	</layout:column>
</logic:present>
