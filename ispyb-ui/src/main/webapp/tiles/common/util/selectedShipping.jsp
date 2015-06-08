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
<%@page import="ispyb.client.common.BreadCrumbsForm"%>

<%
	String targetCreateDewar 	= request.getContextPath() + "/menuSelected.do?leftMenuId=6&topMenuId=5&targetUrl=/user/createDewarAction.do?reqCode=display";
	String targetViewShippings	= request.getContextPath() + "/menuSelected.do?leftMenuId=7&topMenuId=5&targetUrl=/user/viewShippingAction.do?reqCode=display";
	String selectedShippingTitle	= "Shipment";
	String targetSelectedShipping 	= request.getContextPath() + "/reader/viewDewarAction.do?reqCode=readOnly";
	String targetUploadShipping 	= request.getContextPath() + "/user/submitPocketSampleInformationAction.do?reqCode=displayAfterDewarTracking";
	String targetExportShipping 	= request.getContextPath() + "/user/submitPocketSampleInformationAction.do?reqCode=exportShipping";
	String targetFillShipment 	= request.getContextPath() + "/user/fillShipmentAction.do?reqCode=display";
	
	String isInSubmitPocketSampleInformation = request.getParameter("isInSubmitPocketSampleInformation");
	String isInFillShipment = request.getParameter("isInFillShipment");
%>

<logic:equal name="breadCrumbsForm" property="userRole" value="<%=Constants.FXMANAGE_ROLE_NAME%>">
	<%	targetSelectedShipping 		= request.getContextPath() + "/user/viewSample.do?reqCode=displayForShipping"; %>
</logic:equal>

<%-- Selected Shipping -----------------------------------------------------------------------------%>

<logic:present name="breadCrumbsForm" property="selectedShipping" 		scope="session">
	<bean:define name="breadCrumbsForm" property="selectedShipping" 	scope="session" id="selectedShipping" 	type="ispyb.server.common.vos.shipping.Shipping3VO"/>

	<%-- Selected Shipping Info --%>
	<layout:column styleClass="PANEL_BREAD_CRUMBS">
		<layout:panel key="<%=selectedShippingTitle%>" align="left" styleClass="PANEL_BREAD_CRUMBS_ITEM">
			<layout:text key="Name"          name="selectedShipping" 	property="shippingName"   styleClass="PANEL_BREAD_CRUMBS_FIELD" mode="I,I,I"/>
			<layout:text key="Creation date" name="selectedShipping" 	property="creationDate"  type="date"  styleClass="PANEL_BREAD_CRUMBS_FIELD" mode="I,I,I"/>
			<layout:text key="Status"        name="selectedShipping" 	property="shippingStatus" styleClass="PANEL_BREAD_CRUMBS_FIELD" mode="I,I,I"/>

			<%--<logic:present name="breadCrumbsForm" property="selectedDewar" scope="session">--%>	
				<tr><th class="PANEL_BREAD_CRUMBS_FIELD" valign="top"></th><td class="PANEL_BREAD_CRUMBS_FIELD" valign="top" style="">
	            	<html:link href="<%=targetSelectedShipping%>" paramName="selectedShipping" paramId="shippingId" paramProperty="shippingId" styleClass="PANEL_BREAD_CRUMBS_FIELD">
						Back to this Shipment
					</html:link>
				</td></tr>
			<%--</logic:present>--%>

		</layout:panel>
			
	<%-- If not fedexManager--%>
	<logic:notEqual name="breadCrumbsForm" property="userRole" value="<%=Constants.FXMANAGE_ROLE_NAME%>">
		
		<%-- If Dewar Tracking--%>
		<logic:equal name="breadCrumbsForm" property="selectedShipping.shippingType" value="<%=Constants.DEWAR_TRACKING_SHIPPING_TYPE%>">
		
			<%-- If Shipping opened: Add a component to shipment --%>
			<logic:equal name="breadCrumbsForm" property="selectedShipping.shippingStatus" value="<%=Constants.SHIPPING_STATUS_OPENED%>">
				<% if (isInFillShipment==null) {%>
				<layout:link href="<%=targetCreateDewar%>" paramName="selectedShipping" paramId="shippingId" paramProperty="shippingId" styleClass="PANEL_BREAD_CRUMBS_FIELD">
					<img src="<%=request.getContextPath()%>/images/componentAdd.png" border="0"  vspace="2" onmouseover="return overlib('Add a Component to Shipment');" onmouseout="return nd();">Add a Component to this shipment
				</layout:link>
				<%} %>
			</logic:equal>
			
			<%-- If Shipping not processing, sent to user and not closed --%>
			<logic:notEqual name="breadCrumbsForm" property="selectedShipping.shippingStatus" value="<%=Constants.SHIPPING_STATUS_PROCESS%>">	
			<logic:notEqual name="breadCrumbsForm" property="selectedShipping.shippingStatus" value="<%=Constants.SHIPPING_STATUS_SENT_TO_USER%>">	
			<logic:notEqual name="breadCrumbsForm" property="selectedShipping.shippingStatus" value="<%=Constants.SHIPPING_STATUS_CLOSED%>">	
			
				
				<layout:grid cols="3"  borderSpacing="10">
				
				<%-- If Shipping opened: file upload --%>
				<logic:equal name="breadCrumbsForm" property="selectedShipping.shippingStatus" value="<%=Constants.SHIPPING_STATUS_OPENED%>">	
					<% if (isInSubmitPocketSampleInformation==null) {%>
					<layout:column>
					<layout:link href="<%=targetUploadShipping%>" paramName="selectedShipping" paramId="shippingId" paramProperty="shippingId" styleClass="PANEL_BREAD_CRUMBS_FIELD"
								onclick="document.body.style.cursor = 'wait';"
								onmouseover="return overlib('Upload Shipment from Excel file');" 
								onmouseout="return nd();" >
						<img src="<%=request.getContextPath()%>/images/Excel_24x24_01.png" border="0" vspace="2" />Upload Shipment from Excel file
					</layout:link>	
					</layout:column>
					<%} %>
				</logic:equal>
				<%-- Else: file upload with warning --%>
				<logic:notEqual name="breadCrumbsForm" property="selectedShipping.shippingStatus" value="<%=Constants.SHIPPING_STATUS_OPENED%>">	
					<% if (isInSubmitPocketSampleInformation==null) {%>
					<layout:column>
					<layout:link href="<%=targetUploadShipping%>" paramName="selectedShipping" paramId="shippingId" paramProperty="shippingId" styleClass="PANEL_BREAD_CRUMBS_FIELD" 
								onclick="alert('WARNING: your shipment has been sent to the facility or localised at the facility. Uploading a new file will delete existing Samples and Pucks.');document.body.style.cursor = 'wait';" 
								onmouseover="return overlib('<DIV align=center>Upload Shipment from Excel file.<BR><B><FONT color=red>WARNING</FONT></B><BR>Your shipment has been sent to the facility or localised at the facility. Uploading a new file <B><FONT color=red>will delete existing Samples and Pucks</FONT></B>.</DIV>');" 
								onmouseout="return nd();" >
						<img src="<%=request.getContextPath()%>/images/Excel_24x24_01.png" border="0" vspace="2" />Upload Shipment from Excel file
					</layout:link>	
					</layout:column>
					
					<%} %>
				</logic:notEqual>
				
				
				
				
				<%-- Fill shipment --%>
				<% if (isInFillShipment==null) {%>
					<% if (isInSubmitPocketSampleInformation==null) {%>
					<layout:column>
						<font color="black">OR</font>
					</layout:column>
					<%} %>
				<layout:column>
				<layout:link href="<%=targetFillShipment%>" paramName="selectedShipping" paramId="shippingId" paramProperty="shippingId" styleClass="PANEL_BREAD_CRUMBS_FIELD" 
							onmouseover="return overlib('Fill Shipment online: edit dewars and pucks');"
								onmouseout="return nd();" >
					<img src="<%=request.getContextPath()%>/images/shipment.png" border="0"  vspace="2" onmouseover="return overlib('Edit dewars and pucks');" onmouseout="return nd();" />Fill Shipment online
				</layout:link>
				</layout:column>
				<%} %>
			</layout:grid>
			</logic:notEqual>
			</logic:notEqual>
			</logic:notEqual>
			
		</logic:equal>
		
		
		<%-- If not Dewar Tracking and opened --%>
		<logic:notEqual name="breadCrumbsForm" property="selectedShipping.shippingType" value="<%=Constants.DEWAR_TRACKING_SHIPPING_TYPE%>">
			<logic:equal name="breadCrumbsForm" property="selectedShipping.shippingStatus" value="<%=Constants.SHIPPING_STATUS_OPENED%>">
				<layout:link href="<%=targetExportShipping%>" paramName="selectedShipping" paramId="shippingId" paramProperty="shippingId" styleClass="PANEL_BREAD_CRUMBS_FIELD">
					<img src="<%=request.getContextPath()%>/images/excel_export.gif" border="0"  vspace="2" onmouseover="return overlib('Export Shipping to Excel file');" onmouseout="return nd();"
						width="25px" height="30px" />Export Shipment to Excel file
				</layout:link>
			</logic:equal>
		</logic:notEqual>
	</logic:notEqual>

	</layout:column>
</logic:present>
		
