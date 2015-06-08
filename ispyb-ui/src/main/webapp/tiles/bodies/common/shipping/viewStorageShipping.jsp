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
	String targetSample 			= request.getContextPath() + "/menuSelected.do?leftMenuId=11&topMenuId=10&targetUrl=/user/viewSample.do?reqCode=displayForShipping";
	String targetCreateDewar 		= request.getContextPath() + "/user/createDewarAction.do?reqCode=display";
	String targetDeleteShipping 	= request.getContextPath() + "/user/viewShippingAction.do?reqCode=deleteShipping";
	String targetUpdateShipping 	= request.getContextPath() + "/user/createShippingAction.do?reqCode=updateDisplay";
	
%>

<jsp:useBean id="viewShippingAction" scope="request" class="ispyb.client.common.shipping.ViewShippingAction"/>
<bean:define name="viewShippingForm" property="listInfo" id="myList" type="java.util.List"	toScope="request"/>

<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>


<%-- To not show a empty table when no shipping exists --%>	
<logic:empty name="myList">
           <h4>No&nbsp;shipment&nbsp;have&nbsp;been&nbsp;found</h4>
</logic:empty>


<logic:notEmpty name="myList">	
	<layout:grid cols="1"  borderSpacing="10">
	
		<%-- Page --%>
		<layout:panel key="Shipment" align="left" styleClass="PANEL">
		<layout:collection 	name="viewShippingForm" 
									property="listInfo"
									styleClass="LIST" styleClass2="LIST2"
									id="currInfo"
									>
	
			<layout:collectionItem title="Name" 				property="shippingName" 	
																sortable="true"
																href="<%=targetDewar%>" 															
																paramId="shippingId" paramProperty="shippingId"
																/>
			<layout:collectionItem title="Shipping Date" 		property="deliveryAgentShippingDate" 	
																sortable="true"
																type="date"
																/>
			<layout:collectionItem title="Courrier Name" 		property="deliveryAgentAgentName" 	
																sortable="false"/>
															
			<layout:collectionItem title="Tracking Number" 		property="deliveryAgentAgentCode" 	
																sortable="false">
				
				<%------- Track the Shipping -------%>
				<logic:present name="currInfo" property="deliveryAgentAgentName">
				<bean:define name="currInfo" property="deliveryAgentAgentName" id="currentDeliveryAgentAgentName"/>
				
				<%--Fedex--%>
				<logic:equal name="currInfo" property="deliveryAgentAgentName" value="<%=viewShippingAction.retrieveDeliveryAgentName((String)currentDeliveryAgentAgentName, Constants.SHIPPING_DELIVERY_AGENT_NAME_FEDEX)%>">
					<html:link href="<%=Constants.TRACKING_URL_FEDEX%>" paramName="currInfo" paramId="tracknumbers" paramProperty="deliveryAgentAgentCode" target="_new">
						<img src="<%=request.getContextPath()%>/images/fedex_express_logo.gif" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
						<bean:write name="currInfo"	property="deliveryAgentAgentCode"/> 
					</html:link>
				</logic:equal>
				<%--TNT--%>
				<logic:equal name="currInfo" property="deliveryAgentAgentName" value="<%=viewShippingAction.retrieveDeliveryAgentName((String)currentDeliveryAgentAgentName, Constants.SHIPPING_DELIVERY_AGENT_NAME_TNT)%>">
					<html:link href="<%=Constants.TRACKING_URL_TNT%>" paramName="currInfo" paramId="cons" paramProperty="deliveryAgentAgentCode" target="_new">
						<img src="<%=request.getContextPath()%>/images/tnt_logo.gif" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
						<bean:write name="currInfo"	property="deliveryAgentAgentCode"/> 
					</html:link>
				</logic:equal>
				</logic:present>
				<%---------------------------------%>		
								
			</layout:collectionItem>
														
			<%-- View Dewars and samples --%>
			<layout:collectionItem title="View Dewars"
																sortable="false"
																href="<%=targetDewar%>" 															
																paramId="shippingId" paramProperty="shippingId"
																width="80"
																>
				
				<center><img src="<%=request.getContextPath()%>/images/DewarView_24x24_01.png" border=0 onmouseover="return overlib('View Dewars in Shipping');" onmouseout="return nd();"></center>
			</layout:collectionItem>
			<layout:collectionItem title="View Samples"
																sortable="false"
																href="<%=targetSample%>"					 
																paramId="shippingId" paramProperty="shippingId"
																width="80"
																>
				<center><img src="<%=request.getContextPath()%>/images/SampleHolderView_24x24_01.png" border=0 onmouseover="return overlib('View Samples in shipping.');" onmouseout="return nd();"></center>
			</layout:collectionItem>
	
	
	
			<%-- Actions --%>												
			<layout:collectionItem title="" width="120">
	
			  <%-- Shipping with processing status --%>
			  <logic:equal name="currInfo" property="shippingStatus" value="<%=Constants.SHIPPING_STATUS_PROCESS%>">
					<font color="#ff0000" >Processing</font>
					<img src="<%=request.getContextPath()%>/images/blank.gif" border=0>
			  </logic:equal>
	
			  <%-- Shipping with closed status --%>
			  <logic:equal name="currInfo" property="shippingStatus" value="<%=Constants.SHIPPING_STATUS_CLOSED%>">
					<font color="#ff0000" >Closed</font>
					<img src="<%=request.getContextPath()%>/images/blank.gif" border=0>
			  </logic:equal>
			  
		    <%-- Shipping with open status : edit, close, delete --%>
				<logic:equal name="currInfo" property="shippingStatus" value="<%=Constants.SHIPPING_STATUS_OPENED%>">
					<%-- Edit --%>
					<html:link href="<%=targetUpdateShipping%>" paramName="currInfo" paramId="shippingId" paramProperty="shippingId">
					<img src="<%=request.getContextPath()%>/images/Edit_16x16_01.png" border="0" onmouseover="return overlib('Edit Shipping');" onmouseout="return nd();"/>Edit
					</html:link>
					<%-- Add Dewar --%>
					<html:link href="<%=targetCreateDewar%>" paramName="currInfo" paramId="shippingId" paramProperty="shippingId">
						<img src="<%=request.getContextPath()%>/images/DewarAdd_24x24_01.png" border=0 onmouseover="return overlib('Add Dewar to Shipping');" onmouseout="return nd();">
					</html:link>
					<%-- Close 
					<html:link href="<%=targetCloseShipping%>" paramName="currInfo" paramId="shippingId" paramProperty="shippingId" onclick="return window.confirm('Do you really want to close this shipping?');">
						<img src="<%=request.getContextPath()%>/images/lock.gif" border=0 onmouseover="return overlib('Close the Shipping');" onmouseout="return nd();">
					</html:link>
					--%>
					<%-- Delete --%>
					<html:link href="<%=targetDeleteShipping%>" paramName="currInfo" paramId="shippingId" paramProperty="shippingId" onclick="return window.confirm('Do you really want to delete this shipping?');">
						<img src="<%=request.getContextPath()%>/images/cancel.png" border=0 onmouseover="return overlib('Delete the Shipping');" onmouseout="return nd();">
					</html:link>
				</logic:equal>
						  
	
			</layout:collectionItem>										
			
		</layout:collection>
		</layout:panel>
	
	</layout:grid>
</logic:notEmpty>

