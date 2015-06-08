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

<h3> Protein coming from samplesheets entered in User Office db </h3>

	<layout:grid cols="1"  borderSpacing="10">
		<%-- Page --%>
		<layout:panel key="All proteins from SampleSheets" align="left" styleClass="PANEL">
				<layout:collection 	name="sampleSheetForm" property="sampleSheetInfo" id="list" styleClass="LIST">
					<layout:collectionStyle name="sampleSheetInfo"  property="color"  value="#ff0000"  matchStyle="LISTRED" >
       				<layout:collectionStyle name="sampleSheetInfo"  property="color"  value="#8FBC8B"  matchStyle="LISTGREEN" >

						
						<layout:collectionItem title="Protein Acronym" 	property="acronym" 	sortable="true"/>
						                      					    		
						    <layout:collectionItem title="SpaceGroup"  property="spaceGroup" sortable="false"/>
						    <layout:collectionItem title="Description"  property="description" sortable="false"/>
						    <%-- <layout:collectionItem title="Color"  property="color" sortable="false" /> --%>
						    <layout:collectionItem title="A" 	property="cellA" 		sortable="false"/>
						    <layout:collectionItem title="B" 	 property="cellB" 		sortable="false"/>
						    <layout:collectionItem title="C" 		property="cellC" 		sortable="false"/>
						    <layout:collectionItem title="alpha" 	property="cellAlpha" 	sortable="false"/>
						    <layout:collectionItem title="beta" 	property="cellBeta" 		sortable="false"/>
						    <layout:collectionItem title="gama" 	 property="cellGamma" 	sortable="false"/>
				       </layout:collectionStyle>  
				       </layout:collectionStyle>  
				</layout:collection>
		</layout:panel>
	</layout:grid>

<%----------------------------- Protein not entered in ISPyB db ----------------------------------------%>
<logic:notEmpty name="sampleSheetForm" property="sampleSheetNewList">
	<layout:grid cols="1"  borderSpacing="10">
		<%-- Page --%>
		<layout:column >
		<layout:panel key="Proteins from SampleSheets not existing in ISPyB database" align="left" styleClass="PANEL">
			<layout:form action="/user/viewProteinFromSS.do" reqCode="save">	
				<layout:collection 	name="sampleSheetForm" property="sampleSheetNewList" styleClass="LIST">
				
						
						<layout:collectionItem title="Protein Acronym" 	property="acronym" 	sortable="true"/>
						                       
							<layout:text key="save" 	property="saveNewAcronyms" 	value="yes"  styleClass="FIELD"	mode="H,H,H"/>
						    <layout:collectionItem title="SpaceGroup"  property="spaceGroup" sortable="false"/>
						    <layout:collectionItem title="Description"  property="description" sortable="false"/>

						    <layout:collectionItem title="A" 	property="cellA" 		sortable="false"/>
						    <layout:collectionItem title="B" 	 property="cellB" 		sortable="false"/>
						    <layout:collectionItem title="C" 		property="cellC" 		sortable="false"/>
						    <layout:collectionItem title="alpha" 	property="cellAlpha" 	sortable="false"/>
						    <layout:collectionItem title="beta" 	property="cellBeta" 		sortable="false"/>
						    <layout:collectionItem title="gama" 	 property="cellGamma" 	sortable="false"/>
				  
				</layout:collection>
				<layout:row>
										<layout:submit reqCode="save"><layout:message key="Update ISPyB database"/></layout:submit>
				</layout:row>
			</layout:form>
		</layout:panel>
		</layout:column>
	</layout:grid>
</logic:notEmpty>
<logic:empty name="sampleSheetForm" property="sampleSheetNewList">
<h4> All proteins approved by Safety have been entered in ISPyB database</h4> 
</logic:empty>

