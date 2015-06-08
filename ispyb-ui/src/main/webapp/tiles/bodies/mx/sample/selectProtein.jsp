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
	String targetSamplesForProtein = request.getContextPath() + "/menuSelected.do?leftMenuId=11&topMenuId=10&targetUrl=/user/viewSample.do?reqCode=displayForProtein";
	String targetSamplesForCrystal = request.getContextPath() + "/menuSelected.do?leftMenuId=11&topMenuId=10&targetUrl=/user/viewSample.do?reqCode=displayForCrystal";
%>


<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<%----------------------------- Select a protein ----------------------------------------%>
<logic:notPresent name="viewCrystalForm" property="exportedContent">
	<layout:grid cols="1"  borderSpacing="10">
		<%-- Page --%>
		<layout:column >
		<layout:panel key="Proteins and Crystal Forms" align="left" styleClass="PANEL">
			<layout:form action="/user/selectProteinAction.do" reqCode="display">	
				<layout:collection 	name="viewCrystalForm" property="listProtein" styleClass="LIST"
									selectType="checkbox" selectProperty="proteinId" selectName="selectedProteins"
				>
				
						<layout:collectionItem title="Protein Name" 	property="name" 	sortable="true"/>
						<layout:collectionItem title="Protein Acronym" 	property="acronym" 	sortable="true"
								               href="<%=targetSamplesForProtein%>"
								               paramId="proteinId" paramProperty="proteinId"/>
						                       
						<layout:nestedCollection  property="crystals" id="crystal">
				
						    <layout:collectionItem title="SpaceGroup" name="crystal" property="spaceGroup" sortable="false"
						                           href="<%=targetSamplesForCrystal%>"
						                           paramId="crystalId" paramProperty="crystalId"/>
						    <layout:collectionItem title="X" 					name="crystal" property="sizeX" 		sortable="false"/>
						    <layout:collectionItem title="Y" 					name="crystal" property="sizeY" 		sortable="false"/>
						    <layout:collectionItem title="Z" 					name="crystal" property="sizeZ" 		sortable="false"/>
						    <layout:collectionItem title="A" 					name="crystal" property="cellA" 		sortable="false"/>
						    <layout:collectionItem title="B" 					name="crystal" property="cellB" 		sortable="false"/>
						    <layout:collectionItem title="C" 					name="crystal" property="cellC" 		sortable="false"/>
						    <layout:collectionItem title="alpha" 				name="crystal" property="cellAlpha" 	sortable="false"/>
						    <layout:collectionItem title="beta" 				name="crystal" property="cellBeta" 		sortable="false"/>
						    <layout:collectionItem title="gama" 				name="crystal" property="cellGamma" 	sortable="false"/>
				       </layout:nestedCollection>
				</layout:collection>
				<layout:row>
					<layout:reset/>
					<layout:submit reqCode="exportForPocketSample"><layout:message key="Export for PocketSample"/></layout:submit>
				</layout:row>
				
			</layout:form>
		</layout:panel>
		</layout:column>
	</layout:grid>
</logic:notPresent>
<%----------------------------- Display Result ----------------------------------------%>
<logic:present name="viewCrystalForm" property="exportedContent">
Export the following information to PocketSample:
<layout:row>
<layout:panel key="Information to export to PocketSample" align="center" styleClass="PANEL" >
	<layout:form action="/user/selectProteinAction.do" reqCode="display" width="100%">	
		<layout:textarea key=""	property="exportedContent" mode="R,R,R" styleClass="XML"/>
	</layout:form>
	
	<bean:define name="viewCrystalForm" property="exportedContentUrl" id="exportedContentUrl" type="java.lang.String"/>
	<h2>
	File available at: <a href="<%=exportedContentUrl%>"><%=exportedContentUrl%></a>
	</h2>
	<h2>
	File Format is:<br>
	Protein_Acronym - Space_Group
	</h2>
</layout:panel>

</layout:row>
</logic:present>
