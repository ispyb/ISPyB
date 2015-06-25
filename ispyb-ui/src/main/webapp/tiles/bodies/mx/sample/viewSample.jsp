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
<%@ page import="ispyb.common.util.Constants"%>

<%@ page isELIgnored="false" %>

<%@ page import="ispyb.client.mx.sample.ViewSampleAction"%>

<%
	String targetSample 		= request.getContextPath() + "/menuSelected.do?leftMenuId=11&topMenuId=10&targetUrl="+ispyb.common.util.Constants.PAGE_SAMPLE_EDIT;
    String targetDataCollections 	= request.getContextPath() + "/menuSelected.do?leftMenuId=61&topMenuId=16&targetUrl=/user/viewDataCollection.do?reqCode=displayForSample";
    String targetDataCollectionGroups 	= request.getContextPath() + "/menuSelected.do?leftMenuId=61&topMenuId=16&targetUrl=/user/viewDataCollectionGroup.do?reqCode=displayForSample";
	String targetViewSampleDetails 	= request.getContextPath() + "/user/viewSample.do?reqCode=displayAllDetails";
	String targetViewContainer	= request.getContextPath() + "/user/viewSample.do?reqCode=displayForContainer";
	String targetDewar 		= request.getContextPath() + "/user/viewDewarAction.do?reqCode=display";
	
	String targetremoveSampleFromContainer	= request.getContextPath() + "/user/removeSampleAction.do?reqCode=removeSampleFromContainer";
	String targetDeleteSample		= request.getContextPath() + "/user/removeSampleAction.do?reqCode=deleteSample";
	
    String targetExportPDF 			= request.getContextPath() + "/user/viewSample.do?reqCode=exportAsPdf";
    String targetExportPDF2 		= request.getContextPath() + "/user/viewSample.do?reqCode=exportAsPdf&sortView=2";

	String selectProperty	= "";
	String selectName		= "";
	String selectType		= "";
%>
<layout:skin includeScript="true"/>
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bubble_tooltip.css" /> 

<logic:present name="viewSampleForm" scope="request">
	<bean:define name="viewSampleForm" id="myForm" type="ispyb.client.mx.sample.ViewSampleForm" toScope="request"/>
	<bean:define name="viewSampleForm" property="listInfo" id="myList" type="java.util.List" toScope="request"/>
</logic:present>
<%----------------------------------------------------------------%>

<%-- View Sample list --%>

<%-- BreadCrumbs bar --%>
<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />


<%-- To not show a empty table when no samples exists --%>	
<logic:present name="myList">
<logic:empty name="myList">
           <h4>No&nbsp;samples&nbsp;have&nbsp;been&nbsp;found</h4>
</logic:empty>
</logic:present>


<logic:present name="myList">
<logic:notEmpty name="myList">


<layout:grid cols="3"  borderSpacing="10">
	<%-- display dewar comments only if view samples for Dewar   --%>	
	<logic:present 	name="breadCrumbsForm" 	property="selectedDewar" scope="session">
		<logic:notPresent 	name="breadCrumbsForm" 	property="selectedContainer" scope="session">
			<layout:column>
				<logic:notEmpty 	name="breadCrumbsForm" 	property="selectedDewar.comments" scope="session">
						<layout:panel key="Dewar Comments" align="left" styleClass="PANEL">
							<p><bean:write name="breadCrumbsForm" property="selectedDewar.comments"/></p>
						</layout:panel>	
				</logic:notEmpty>
			</layout:column>
	
			<%-- Link to export to pdf only for dewar: sort by dewar/cont/location --%>
			<layout:column>
			<layout:link title="Export to PDF (sort by container/location)" href="<%= targetExportPDF2 %>" paramName="viewSampleForm" >
				<font class="FIELD">
				<img src="<%=request.getContextPath()%>/images/pdf.png" border="0" onmouseover="return overlib('Export this table as PDF');" onmouseout="return nd();"/>
				Export as PDF(sort by container/location)
				</font>
			</layout:link>
			</layout:column>
		</logic:notPresent>
	</logic:present>
	
	<%-- Link to export to pdf sorted by position only for shipment --%>
	<logic:notPresent 	name="breadCrumbsForm" 	property="selectedDewar" scope="session">
	<layout:column>
		<layout:link title="Export to PDF (sort by dewar/container/location)" href="<%= targetExportPDF2 %>" paramName="viewSampleForm" >
			<font class="FIELD">
			<img src="<%=request.getContextPath()%>/images/pdf.png" border="0" onmouseover="return overlib('Export this table as PDF');" onmouseout="return nd();"/>
			Export as PDF(sort by dewar/container/location)
			</font>
		</layout:link>
	</layout:column>
	</logic:notPresent>
	
	<%-- Link to export to pdf common to all --%>
	<layout:column>
		<layout:link title="Export to PDF (sort by acronym/sample name)" href="<%= targetExportPDF %>" paramName="viewSampleForm" >
			<font class="FIELD">
			<img src="<%=request.getContextPath()%>/images/pdf.png" border="0" onmouseover="return overlib('Export this table as PDF');" onmouseout="return nd();"/>
			Export as PDF(sort by acronym/sample name)
			</font>
		</layout:link>
	</layout:column>
	
</layout:grid>

<layout:grid cols="1"  borderSpacing="10">
	<%-- Page --%>
	<layout:panel key="Samples" align="left" styleClass="PANEL">
	
		<logic:present name="myList">
		<layout:collection 	name="myList" 
					styleClass="LIST"
					id="currInfo"
					selectProperty="<%=selectProperty%>"  selectName="<%=selectName%>" selectType="<%=selectType%>"
					sortAction="client"
					offsetIndexId="index"
					offset="0"
					>

				<layout:collectionItem title="Protein" property="crystalVO.proteinVO.acronym" sortable="true">
					<bean:define id="crystalId" 			name="currInfo" property="crystalVO.crystalId" type="java.lang.Integer"/>
					<bean:define id="isCrystalImageURLOK" 		value="<%= \" \" + ViewSampleAction.isCrystalImageURLOK(crystalId)%>"/>
					
					<%-- Link to Crystal Information --%>
					<logic:equal name="isCrystalImageURLOK" value="1">
						<bean:define id="crystalImageURL" 	value="<%= ViewSampleAction.getCrystalImageURL(crystalId)%>"/>
						<html:link href="<%= crystalImageURL %>"  styleClass="LIST" target="_new">
							<bean:write name="currInfo" property="crystalVO.proteinVO.acronym"/>
						</html:link>
					</logic:equal>
					<logic:equal name="isCrystalImageURLOK" value="0">
						<bean:write name="currInfo" property="crystalVO.proteinVO.acronym"/>
					</logic:equal>
				</layout:collectionItem>
				
				<layout:collectionItem title="Space Group" 			property="crystalVO.spaceGroup" 				sortable="true"/>																																							
				<layout:collectionItem title="Sample<br>name" property="name" sortable="true" href="<%=targetViewSampleDetails%>" paramId="blSampleId" paramProperty="blSampleId" width="50"/>
				<layout:collectionItem title="Smp<br>code" property="code" sortable="true" href="<%=targetViewSampleDetails%>" paramId="blSampleId" paramProperty="blSampleId"/>
				
				<logic:notPresent 	name="breadCrumbsForm" 	property="selectedDewar" scope="session">
					<%-- ------------------------------------------------ Shipment Name ------------------------------------- --%>
					<layout:collectionItem title="Shipment" sortable="false" href="<%=targetDewar%>" paramId="shippingIdFromcontainerId" paramProperty="containerVO.containerId">
						<logic:present name="currInfo" property="containerVO.containerId">
						<bean:define id="containerId" 	name="currInfo" property="containerVO.containerId" type="java.lang.Integer"/>
						<%= ViewSampleAction.getShipping(containerId).getShippingName()%>
						</logic:present>
					</layout:collectionItem>
					<%-- ------------------------------------------------Dewar Comments-------------------------------------- --%>
					<layout:collectionItem title="Dewar" property="containerVO.dewarVO.code" sortable="true">
					
						<logic:present name="currInfo" property="containerVO.dewarVO.comments">
							<a href="" class="tt">
								<bean:write name="currInfo" property="containerVO.dewarVO.code"/>
								<span class="tooltip"><span class="top"></span>
								<span class="middle">
									<bean:write name="currInfo" property="containerVO.dewarVO.comments"/>
								</span>
								<span class="bottom"></span></span>
							</a>
						</logic:present>
						<logic:notPresent name="currInfo" property="containerVO.dewarVO.comments">
						<logic:present name="currInfo" property="containerVO.dewarVO.code">
							<bean:write name="currInfo" property="containerVO.dewarVO.code"/>
							</logic:present>
						</logic:notPresent>
					</layout:collectionItem>
					<%-- ---------------------------------------------------------------------------------------------------- --%>			
				</logic:notPresent>

				<logic:notPresent 	name="breadCrumbsForm" 	property="selectedContainer" scope="session">
					<layout:collectionItem title="Container" property="containerVO.code" sortable="true" href="<%=targetViewContainer%>" paramId="containerId" paramProperty="containerVO.containerId"/>
				</logic:notPresent>
				
				<layout:collectionItem title="Loc.<br>in<br>cont." property="location" 	sortable="true"/>
				<layout:collectionItem title="Cell a" 				property="crystalVO.cellA" 					sortable="false"/>																																					
				<layout:collectionItem title="Cell b" 				property="crystalVO.cellB" 					sortable="false"/>																																					
				<layout:collectionItem title="Cell c" 				property="crystalVO.cellC" 					sortable="false"/>																																					
				<layout:collectionItem title="Cell<br>alpha" 			property="crystalVO.cellAlpha" 					sortable="false"/>																																					
				<layout:collectionItem title="Cell beta" 			property="crystalVO.cellBeta" 					sortable="false"/>																																					
				<layout:collectionItem title="Cell gamma"			property="crystalVO.cellGamma" 					sortable="false"/>																																					
				<layout:collectionItem title="Crystal<br>comments" 		property="crystalVO.comments" 				sortable="false"/>
				<logic:present name="currInfo"  property="diffractionPlanVO" >
					<layout:collectionItem title="Exp. Type" 			property="diffractionPlanVO.experimentKind" 					sortable="false"/>	
					<layout:collectionItem title="Already<br>observed<br>resol." 	property="diffractionPlanVO.observedResolution" mathPattern="#.##" 	sortable="false"/>
					<layout:collectionItem title="Required<br>resol." 		property="diffractionPlanVO.requiredResolution" mathPattern="#.##" 	sortable="false"/>
				</logic:present>
				<logic:notPresent name="currInfo"  property="diffractionPlanVO" >
					<layout:collectionItem title="Exp. Type" 			property="crystalVO.diffractionPlanVO.experimentKind" 					sortable="false"/>	
					<layout:collectionItem title="Already<br>observed<br>resol." 	property="crystalVO.diffractionPlanVO.observedResolution" mathPattern="#.##" 	sortable="false"/>
					<layout:collectionItem title="Required<br>resol." 		property="crystalVO.diffractionPlanVO.requiredResolution" mathPattern="#.##" 	sortable="false"/>
				</logic:notPresent>
				<layout:collectionItem title="Minimal<br>resol." 		property="crystalVO.diffractionPlanVO.minimalResolution" mathPattern="#.##" 	sortable="false"/>
				<layout:collectionItem title="Sample<br>comments" 		property="comments" 					sortable="false"/>		        
				
				<%-- Actions : edit/delete/unassign -----------%>	
				<layout:collectionItem title="&nbsp;Edit&nbsp;sample&nbsp;">									

					<%-- Actions : edit/cancel only available if sample not in processing state --%>
					<logic:present name="breadCrumbsForm" property="selectedShipping" scope="session">
						<logic:present name="breadCrumbsForm" property="selectedShipping.shippingStatus" scope="session">
							<bean:define id="shippingStatus" name="breadCrumbsForm" property="selectedShipping.shippingStatus"/>
						</logic:present>
					</logic:present>
					<logic:present name="breadCrumbsForm" property="selectedContainer">
						<logic:present name="breadCrumbsForm" property="selectedContainer.containerStatus">
							<bean:define id="containerStatus" name="breadCrumbsForm" property="selectedContainer.containerStatus"/>
						</logic:present>
					</logic:present>

					<logic:notEqual name="shippingStatus" value="<%=Constants.SHIPPING_STATUS_PROCESS%>">
						<logic:notEqual name="containerStatus" value="<%=Constants.CONTAINER_STATUS_PROCESS%>">
							<logic:notEqual name="currInfo" property="blSampleStatus" value="<%=Constants.SAMPLE_STATUS_PROCESS%>">
								<html:link href="<%=targetSample%>" paramName="currInfo" paramId="blSampleId" paramProperty="blSampleId" styleClass="LIST">
                  					<img src="<%=request.getContextPath()%>/images/Edit_16x16_01.png" border="0" onmouseover="return overlib('Edit Sample');" onmouseout="return nd();"/>
								</html:link>
                				<html:link href="<%=targetDeleteSample%>" paramName="currInfo" paramId="blSampleId" paramProperty="blSampleId" onclick="return window.confirm('Do you really want to delete this Sample?');" styleClass="LIST">
									<img src="<%=request.getContextPath()%>/images/cancel.png" border="0" onmouseover="return overlib('Delete the Sample');" onmouseout="return nd();">
								</html:link>					
							</logic:notEqual>
						</logic:notEqual>
					</logic:notEqual>

					<%-- Actions : remove from container only possible if shipment is not in processing state  
					
					<logic:present 	name="breadCrumbsForm" 	property="selectedShipping" scope="session">
						<logic:notEqual name="breadCrumbsForm" 	property="selectedShipping.shippingStatus" value="<%=Constants.SHIPPING_STATUS_PROCESS%>">						
							<html:link href="<%=targetremoveSampleFromContainer%>" paramName="currInfo" paramId="blSampleId" paramProperty="blSampleId" onclick="return window.confirm('Do you really want to remove this sample from its container?\\n(The sample will go back to the list of Unassigned Samples)');"  styleClass="LIST">
								<img src="<%=request.getContextPath()%>/images/RemoveFromContainer_16x16_01.png" border="0" onmouseover="return overlib('Remove Sample from its container (i.e.: unassigned sample).');" onmouseout="return nd();"/>
							</html:link>
					    </logic:notEqual>
					</logic:present>
					--%>	
															
				</layout:collectionItem>
				
				<%-- Sample status --%>
				<layout:collectionItem title="Sample<br>status" property="blSampleStatus" sortable="true"/>
									
				<%-- Data Collection --%>			
				<layout:collectionItem title="Data<br>Collections" href="<%=targetDataCollections%>" paramId="name,blSampleId" paramProperty="name,blSampleId">
						<bean:define name="index" id="myIndex" />
						<bean:define name="viewSampleForm" property="nbSamples" id="numberofsamples" />
<c:if test="${SITE_ATTRIBUTE eq 'ESRF' or SITE_ATTRIBUTE eq 'EMBL' or SITE_ATTRIBUTE eq 'SOLEIL'}">
						<logic:equal name="viewSampleForm" property="<%= \"hasDataCollection[\" + myIndex +\"]\" %>" value="true" >
							<logic:equal name="viewSampleForm" property="<%= \"hasSnapshot[\" + myIndex + \"]\" %>" value="true" >
							   <img src="<%=request.getContextPath()%>/images/camera_24.png" border="0" onmouseover="return overlib('View Data Collections<br/>(this sample has a crystal snapshot).');" onmouseout="return nd();">
							</logic:equal>
							<logic:notEqual name="viewSampleForm" property="<%= \"hasSnapshot[\" + myIndex + \"]\" %>" value="true" >
								<img src="<%=request.getContextPath()%>/images/magnif_16.png" border="0" onmouseover="return overlib('View Data Collections');" onmouseout="return nd();"/>
							</logic:notEqual>
						</logic:equal>
</c:if>

<c:if test="${SITE_ATTRIBUTE eq 'MAXIV'}">
						<logic:equal name="viewSampleForm" property="<%= \"hasDataCollection[\" + myIndex +\"]\" %>" value="true" >
							<logic:equal name="viewSampleForm" property="<%= \"hasSnapshot[\" + myIndex + \"]\" %>" value="true" >
							   <img src="<%=request.getContextPath()%>/images/camera_24.png" border="0" onmouseover="return overlib('View Data Collections<br/>(this sample has a crystal snapshot).');" onmouseout="return nd();">
							</logic:equal>
							<logic:notEqual name="viewSampleForm" property="<%= \"hasSnapshot[\" + myIndex + \"]\" %>" value="true" >
								<img src="<%=request.getContextPath()%>/images/magnif_16.png" border="0" onmouseover="return overlib('View Data Collections');" onmouseout="return nd();"/>
							</logic:notEqual>
						</logic:equal>
</c:if>

<c:if test="${SITE_ATTRIBUTE eq 'DLS'}">
							<logic:equal name="viewSampleForm" property="<%= \"hasDataCollection[\" + myIndex +\"]\" %>" value="true" >
								<logic:equal name="viewSampleForm" property="<%= \"hasSnapshot[\" + myIndex + \"]\" %>" value="true" >
							   		<img src="<%=request.getContextPath()%>/images/camera_24.png" border="0" onmouseover="return overlib('View Data Collections<BR/>(this sample has a crystal snapshot).');" onmouseout="return nd();">
								</logic:equal>
								<logic:notEqual name="viewSampleForm" property="<%= \"hasSnapshot[\" + myIndex + \"]\" %>" value="true" >
									<img src="<%=request.getContextPath()%>/images/magnif_16.png" border="0" onmouseover="return overlib('View Data Collections');" onmouseout="return nd();"/>
								</logic:notEqual>
							</logic:equal>

</c:if>
				</layout:collectionItem>
				
				<layout:collectionItem title="Data<br>CollectionGroups" href="<%=targetDataCollectionGroups%>" paramId="name,blSampleId" paramProperty="name,blSampleId">
						<bean:define name="index" id="myIndex" />
						<bean:define name="viewSampleForm" property="nbSamples" id="numberofsamples" />
						<logic:equal name="viewSampleForm" property="<%= \"hasDataCollectionGroup[\" + myIndex +\"]\" %>" value="true" >
							 <img src="<%=request.getContextPath()%>/images/magnif_16.png" border="0" onmouseover="return overlib('View Data Collection Groups, energys Scans, XRFSpectra.');" onmouseout="return nd();">
						</logic:equal>
			</layout:collectionItem>
	
                			
			</layout:collection>
			</logic:present>

	</layout:panel>
</layout:grid>
<h4><bean:write name="myForm" property="nbSamples"/>&nbsp;samples&nbsp;have&nbsp;been&nbsp;found </h4>
</logic:notEmpty>
</logic:present>

<%-- Errors --%>
<jsp:include page="/tiles/common/messages/error.jsp" flush="true" />

<%-- Acknowledge Action --%>
<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />
