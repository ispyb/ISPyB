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


<%
	String targetSamplesForProtein 		= request.getContextPath() + "/menuSelected.do?leftMenuId=39&topMenuId=10&targetUrl=/user/viewSample.do?reqCode=displayForProtein";
	String targetSamplesForCrystal 		= request.getContextPath() + "/menuSelected.do?leftMenuId=23&topMenuId=10&targetUrl=/user/viewSample.do?reqCode=displayForCrystal";
	String targetEditCrystal 			= request.getContextPath() + "/menuSelected.do?leftMenuId=23&topMenuId=10&targetUrl=/user/viewProteinAndCrystal.do?reqCode=editCrystal";
	String targetDeleteCrystal			= request.getContextPath() + "/menuSelected.do?leftMenuId=23&topMenuId=10&targetUrl=/user/viewProteinAndCrystal.do?reqCode=deleteCrystal";
	String targetNewCrystal 			= request.getContextPath() + "/menuSelected.do?leftMenuId=32&topMenuId=10&targetUrl=/user/createCrystal.do?reqCode=display";
    String targetDataCollectionForProtein = request.getContextPath() + "/menuSelected.do?leftMenuId=61&topMenuId=16&targetUrl=/user/viewDataCollection.do?reqCode=displayForProtein";
    String tabTitle				 		= "Proteins and Crystal Forms List";
    String targetPdbFileUpload = request.getContextPath() + "/menuSelected.do?leftMenuId=23&topMenuId=10&targetUrl=/user/uploadPdbFile.do?reqCode=display";
%>

<%@page import="ispyb.client.mx.sample.ViewProteinAndCrystalAction"%>
<%@page import="ispyb.common.util.Constants"%>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bubble_tooltip.css" />
<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
<%---------------------------------- Define source -------------------------------------%>
<%-- Called from Action --%>
<logic:present name="viewCrystalForm" scope="request">
	<bean:define name="viewCrystalForm" id="myForm" 		type="ispyb.client.mx.sample.ViewCrystalForm"	toScope="request"/>
	<bean:define id="minimalDisplay" value="false"/>
	<bean:define name="viewCrystalForm" property="listProtein" id="myList" type="java.util.List" toScope="request"/>
</logic:present>

<%-- Called from another Page = SubmitPocketSampleInformationAction --%>
<logic:notPresent name="viewCrystalForm" scope="request">
	<bean:define name="uploadForm" property="viewCrystalForm" id="myForm" 		type="ispyb.client.mx.sample.ViewCrystalForm"	toScope="request"/>
	<bean:define id="minimalDisplay" value="true"/>
	<bean:define name="uploadForm" property="viewCrystalForm.listProtein" id="myList" type="java.util.List" toScope="request"/>
	<%
	tabTitle 	= "Proteins previously submited :";
	%>
</logic:notPresent>


<%--------------------------------------------------------------------------------------%>



<%-- To not show a empty table when no proteins exists --%>	
<logic:empty name="myList">
	<%-- Called from Action --%>
	<logic:present name="viewCrystalForm" scope="request">
           <h4>No&nbsp;proteins&nbsp;have&nbsp;been&nbsp;found</h4>
    </logic:present>
</logic:empty>


<logic:notEmpty name="myList">

	<logic:notEmpty name="breadCrumbsForm" property="selectedProteinId">
		<%-- BreadCrumbs bar --%>
		<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />
	</logic:notEmpty>
	
	<logic:notEqual name="minimalDisplay" value="true">
		<h4>Click on the acronym, or on the space group to see the samples attached to corresponding acronym, space group. </h4>
		
	</logic:notEqual>
		
	<layout:grid cols="1"  borderSpacing="10">
	
		<%-- Page --%>
		<layout:column >
			<layout:panel key="<%=tabTitle%>" align="left" styleClass="PANEL">
			
				<layout:collection name="myForm" id="currInfo" property="listProtein" styleClass="LIST">
					<logic:notEqual name="minimalDisplay" value="true">
							<layout:collectionItem title="Protein Name" 	property="name" 	sortable="true">
								<bean:define id="proteinName" 	name="currInfo" property="name"/>
								<bean:define id="proteinId" 	name="currInfo" property="proteinId" type="java.lang.Integer"/>
								<bean:define id="proteinIsCreatedBySampleSheet" 	value="<%= \"\" + ViewProteinAndCrystalAction.proteinIsCreatedBySampleSheet(proteinId.intValue())%>"/>
								<logic:equal name="proteinIsCreatedBySampleSheet" value="1">
									<a href="" class="tt">
										<div class="proteinFromSampleSheet"><%=proteinName%></div>
										<span class="tooltip">
											<span class="top"></span>
											<span class="middle">Protein Created by Sample Sheet</span>
											<span class="bottom"></span>
										</span>
									</a>
								</logic:equal>
								<logic:notEqual name="proteinIsCreatedBySampleSheet" value="1">
										<%=proteinName%>
								</logic:notEqual>
								
								
							</layout:collectionItem>
					</logic:notEqual>
						
					<layout:collectionItem title="Protein Acronym" 	property="acronym" 	sortable="true"
								               href="<%=targetSamplesForProtein%>"
								               paramId="proteinId" paramProperty="proteinId"/>
								               
						<logic:notEqual name="minimalDisplay" value="true">
							<layout:collectionItem title="New Crystal Form" sortable="false" 
													href="<%=targetNewCrystal%>" 
													paramId="proteinId" 
													paramProperty="proteinId">
													
									<img src="<%=request.getContextPath()%>/images/add.png" border="0" onmouseover="return overlib('Create a new Crystal Form.');" onmouseout="return nd();"/>				
							</layout:collectionItem>
						</logic:notEqual>
						
						<layout:nestedCollection property="crystals" id="crystal">	
							<layout:collectionItem title="SpaceGroup" name="crystal" property="spaceGroup" 	sortable="false" 
												   href="<%=targetSamplesForCrystal%>" 
												   paramId="crystalId" paramProperty="crystalId"/>
							<layout:collectionItem title="A" 		name="crystal" property="cellA" 	sortable="false"/>
							<layout:collectionItem title="B" 		name="crystal" property="cellB" 	sortable="false"/>
							<layout:collectionItem title="C" 		name="crystal" property="cellC" 	sortable="false"/>
							<layout:collectionItem title="Alpha" 	name="crystal" property="cellAlpha" sortable="false"/>
							<layout:collectionItem title="Beta" 	name="crystal" property="cellBeta"  sortable="false"/>
							<layout:collectionItem title="Gamma" 	name="crystal" property="cellGamma" sortable="false"/>
							<layout:collectionItem title="Comments" name="crystal" property="comments"  sortable="false"/>			    
						    
					    	<logic:notEqual name="minimalDisplay" value="true">	
						    	<layout:collectionItem title="Edit" name="crystal" sortable="false" href="<%=targetEditCrystal%>" paramId="crystalId" paramProperty="crystalId">
									<logic:present name="crystal">
										<img src="<%=request.getContextPath()%>/images/Edit_16x16_01.png" border="0" onmouseover="return overlib('Edit this crystal.');" onmouseout="return nd();"/>
									</logic:present>
								</layout:collectionItem>
								<%-------- Delete Crystal ---------%>
								<layout:collectionItem title="Delete" name="crystal" sortable="false" href="<%=targetDeleteCrystal%>" paramId="crystalId" paramProperty="crystalId">
									<logic:present name="crystal">
										<bean:define id="crystalId" name="crystal" property="crystalId" type="java.lang.Integer"/>
						    			<bean:define id="crystalHasSample" 	value="<%= \"\" + ViewProteinAndCrystalAction.crystalHasSample(crystalId.intValue())%>"/>
										<logic:equal name="crystalHasSample" value="0">
											<img src="<%=request.getContextPath()%>/images/cancel.png" border="0" onmouseover="return overlib('Delete this crystal (No Sample attached).');" onmouseout="return nd();" onclick="return window.confirm('Do you realy want to delete this Crystal ?\nNo Sample Attached.');"/>
										</logic:equal>
									</logic:present>
								</layout:collectionItem>
							<%-------------------------------%>
							</logic:notEqual>			


							<%-------- Pdb File Upload ---------%>
							<logic:present name="crystal">
								<bean:define id="crystalId" name="crystal" property="crystalId" type="java.lang.Integer"/>
								<layout:collectionItem title="PDB File" href="<%=targetPdbFileUpload%>" name="crystal"  paramId="crystalId" paramProperty="crystalId">
									<logic:present name="crystal" property="pdbFileName">
										<bean:define id="pdbFileName" name="crystal" property="pdbFileName" type="java.lang.String"/>
										<img src="<%=request.getContextPath()%>/images/attach.png" border="0" onmouseover="return overlib('pdb file uploaded: <%=pdbFileName%>');" onmouseout="return nd();" />
									</logic:present>
									Upload
								</layout:collectionItem>	
							</logic:present>
							<logic:notPresent name="crystal">
								<layout:collectionItem title="PDB File" href="<%=targetPdbFileUpload%>"  paramId="proteinAcronym" paramProperty="acronym">
									Upload
								</layout:collectionItem>	
							</logic:notPresent>										
				   	</layout:nestedCollection>
					   
			       <layout:collectionItem title="Data<br>Collections" href="<%=targetDataCollectionForProtein%>" paramId="proteinAcronym" paramProperty="acronym">
						View
				   </layout:collectionItem>	
				   
		
				</layout:collection>	
			</layout:panel>			
		</layout:column>
	
	</layout:grid>
</logic:notEmpty>

<%-- Called from Action --%>
<logic:present name="viewCrystalForm" scope="request">
	<%-- Acknowledge Action --%>
	<jsp:include page="/tiles/common/messages/error.jsp" flush="true" />
	<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />
</logic:present>
