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
	
	String targetDewar 				= request.getContextPath() + "/user/viewDewarAction.do?reqCode=display";
	String targetSample 			= request.getContextPath() + "/user/viewSample.do?reqCode=displayForShipping";
	String targetSampleForDewar 			= request.getContextPath() + "/user/viewSample.do?reqCode=displayForDewar";
	String targetDeleteShipping 	= request.getContextPath() + "/user/viewShippingAction.do?reqCode=deleteShipping";	
	String targetEditDewar		= request.getContextPath() + "/user/editDewar.do?reqCode=display";

%>

<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<jsp:useBean id="viewShippingAction" scope="request" class="ispyb.client.common.shipping.ViewShippingAction"/>

<h2> View all FX shipments </h2>
<layout:grid cols="1"  borderSpacing="10">
	<%-- Search Panel --%>
	<layout:panel key="Search Shipping" align="left" styleClass="PANEL">
		<layout:form action="/user/viewShippingAction.do" reqCode="displayFXByRun">		
			<layout:grid cols="1" styleClass="SEARCH_GRID">	
				<layout:column>
					<layout:select key="Choose Run #:" property="machineRunId" styleClass="FIELD" size="1" isRequired="false">
						<layout:option value="1"/>   
		                <layout:option value="2"/>   
		                <layout:option value="3"/>
		                <layout:option value="4"/>
		                <layout:option value="5"/>
					</layout:select>							
				</layout:column>		
					
				<layout:row>
					<layout:reset/>
					<layout:submit><layout:message key="Search"/></layout:submit>
				</layout:row>
			</layout:grid>		
		</layout:form>	
	</layout:panel>
	
	<%-- Page --%>
	<layout:panel key="Shipments" align="left" styleClass="PANEL">
	<layout:collection 	name="viewShippingForm" 
								property="listInfo"
								styleClass="LIST" 
								id="currInfo"
								>

        <layout:collectionItem title="Proposal" property="proposalId" sortable="true" >                      			
							<logic:notEmpty name="currInfo" property="proposalVO">
				        		FX<layout:write  name="currInfo" property="proposalVO.number"/>						    
							</logic:notEmpty>					
		</layout:collectionItem>

		<layout:collectionItem title="Shipment Name" 	property="shippingName"  	sortable="false"
					href="<%=targetSample%>" 	paramId="shippingId" paramProperty="shippingId"/>
					
		<layout:collectionItem title="Shipping Date" property="deliveryAgentShippingDate" sortable="true" type="date"/>
																	
		<layout:collectionItem title="Courrier Name" property="deliveryAgentAgentName" sortable="false"/>

		<layout:collectionItem title="Tracking Number" 	property="deliveryAgentAgentCode" sortable="false"/>
																											
		<%-- View samples --%>

		<layout:collectionItem title="View Samples" sortable="false" 
								href="<%=targetSample%>" paramId="shippingId" paramProperty="shippingId" width="80" >
															
			<center><img src="<%=request.getContextPath()%>/images/SampleHolderView_24x24_01.png" border=0 onmouseover="return overlib('View Samples in shipping.');" onmouseout="return nd();"></center>
		</layout:collectionItem>
																						
		<%-- Actions --%>												
		<layout:collectionItem title="Delete<br>shipment" property="shippingStatus" width="120">

		  <%-- Shipping with processing status --%>
		  <logic:equal name="currInfo" property="shippingStatus" value="<%=Constants.SHIPPING_STATUS_PROCESS%>">
				<img src="<%=request.getContextPath()%>/images/lock.gif" border=0>
		  </logic:equal>

		  <%-- Shipping with other status --%>
		  <logic:notEqual name="currInfo" property="shippingStatus" value="<%=Constants.SHIPPING_STATUS_PROCESS%>">
				<%-- Delete --%>
				<html:link href="<%=targetDeleteShipping%>" paramName="currInfo" paramId="shippingId" paramProperty="shippingId" onclick="return window.confirm('Do you really want to delete this shipping?');">
					<img src="<%=request.getContextPath()%>/images/cancel.png" border=0 onmouseover="return overlib('Delete the Shipping');" onmouseout="return nd();">
				</html:link>
			</logic:notEqual>
					  
		</layout:collectionItem>	

		<%-- View Dewars  --%>	
		<layout:nestedCollection  property="dewars" id="DewarData" >
		    <layout:collectionItem title="Dewars:<br>name" 	name="DewarData" property="code" href="<%=targetSampleForDewar%>" 	paramId="dewarId" paramProperty="dewarId"/>
			<layout:collectionItem title="Dewars:<br>comments" 	name="DewarData"	property="comments" 	sortable="false"/>
			<layout:collectionItem title="Dewars:<br>storage Location" name="DewarData"	property="storageLocation" 	sortable="false"/>		
			<layout:collectionItem title="Dewars:<br>status" name="DewarData"	property="dewarStatus" 	sortable="false"/>		
			<layout:collectionItem title="Edit Dewar" width="120">
				<%-- error ????
					<html:link href="<%=targetEditDewar%>" paramName="DewarData" paramId="dewarId" paramProperty="dewarId">
						<img src="<%=request.getContextPath()%>/images/Edit_16x16_01.png" border="0" onmouseover="return overlib('Edit Dewar');" onmouseout="return nd();"/>Edit
					</html:link> 
				--%>
			</layout:collectionItem>	
		</layout:nestedCollection>								
		
	</layout:collection>
	</layout:panel>

</layout:grid>
