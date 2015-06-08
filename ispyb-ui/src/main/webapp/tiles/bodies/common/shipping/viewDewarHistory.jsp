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
	String targetViewShippings		= request.getContextPath() + "/reader/genericShippingAction.do?reqCode=display";	
	String targetViewDewars			= request.getContextPath() + "/user/viewDewarAction.do?reqCode=display";
	
	String dewarPanelTitle			= "All Dewars in Shipment : ";
	String containerPanelTitle		= "All Containers in Dewar : ";

%>

<layout:skin includeScript="true"/>
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<%-- Retrieve a few things from the Beans --%>
<logic:present name="breadCrumbsForm" property="selectedShipping" scope="session">	
	<bean:define name="breadCrumbsForm" property="selectedShipping.shippingName" id="selectedShippingCode" 	type="java.lang.String"/>
	<% dewarPanelTitle += selectedShippingCode; %>
</logic:present>

<logic:present name="breadCrumbsForm" property="selectedDewar" scope="session">	
	<bean:define name="breadCrumbsForm" property="selectedDewar.code" id="selectedDewarCode" 	type="java.lang.String"/>
	<% 
	dewarPanelTitle 		= "Selected Dewar : " + selectedDewarCode;
	containerPanelTitle		+= selectedDewarCode;
	%>
</logic:present>

<%-- BreadCrumbs bar --%>
<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />

<%-- "View all" links --%>
<layout:row>

	<layout:link href="<%=targetViewShippings%>" styleClass="PANEL_BREAD_CRUMBS">
		<img src="<%=request.getContextPath()%>/images/up.gif" border=0 onmouseover="return overlib('View All Shipments');" onmouseout="return nd();">
		View all Shipments
	</layout:link>

	<%-- Link visible only when a dewar is selected --%>
	<logic:present name="breadCrumbsForm" property="selectedDewar" scope="session">	
		<layout:link href="<%=targetViewDewars%>" styleClass="PANEL_BREAD_CRUMBS">
			<img src="<%=request.getContextPath()%>/images/up.gif" border="0" onmouseover="return overlib('View All Dewars in Shipment');" onmouseout="return nd();">
			View all Dewars in Shipment
		</layout:link>
	</logic:present>

</layout:row>


<%-- History --%>
<layout:form action="/reader/viewDewarHistoryAction" reqCode="display">

	<layout:tabs width="600">
		
		<%-- Dewar --%>
		<layout:tab key="Dewar Information" width="200">
			
			<layout:grid cols="2"  borderSpacing="5">
				<layout:panel key="Details" align="left" styleClass="PANEL">
					<layout:text key="Label" 	 property="dewar.code"    styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:text key="Barcode" property="dewar.barCode" styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:space/>
					<layout:text key="Average customs values (Euro)"   property="dewar.customsValue" styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:text key="Average transport values (Euro)" property="dewar.transportValue" styleClass="FIELD_INFO" mode="I,I,I"/>
				</layout:panel>
	
				<layout:panel key="Dewar location" align="left" styleClass="PANEL">
					<layout:text key="Creation date" property="shipping.creationDate" type="date" styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:text key="Sending date" property="shipping.deliveryAgentShippingDate" type="date" styleClass="FIELD_INFO" mode="I,I,I"/>
					
					<layout:text key="Outbound courier" property="shipping.deliveryAgentAgentName" styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:text key="Outbound tracking number" property="dewar.trackingNumberToSynchrotron" styleClass="FIELD_INFO" mode="I,I,I"/>
					
					<logic:present name="viewDewarHistoryForm" property="shipping.deliveryAgentAgentName">
					<logic:present name="viewDewarHistoryForm" property="dewar.trackingNumberToSynchrotron">
						<!--%--Fedex--%-->
						<logic:equal name="viewDewarHistoryForm" property="shipping.deliveryAgentAgentName" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_FEDEX%>">
							<layout:link href="<%=Constants.TRACKING_URL_FEDEX%>" paramName="viewDewarHistoryForm" paramId="tracknumbers" paramProperty="dewar.trackingNumberToSynchrotron" target="_new">
								<img src="<%=request.getContextPath()%>/images/fedex_express_logo.gif" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
							</layout:link>
						</logic:equal>
						<!--%--TNT--%-->
						<logic:equal name="viewDewarHistoryForm" property="shipping.deliveryAgentAgentName" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_TNT%>">
							<layout:link href="<%=Constants.TRACKING_URL_TNT%>" paramName="viewDewarHistoryForm" paramId="cons" paramProperty="dewar.trackingNumberToSynchrotron" target="_new">
								<img src="<%=request.getContextPath()%>/images/tnt_logo.gif" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
							</layout:link>
						</logic:equal>
						<!--%--DHL--%-->
						<logic:equal name="viewDewarHistoryForm" property="shipping.deliveryAgentAgentName" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_DHL%>">
							<layout:link href="<%=Constants.TRACKING_URL_DHL%>" paramName="viewDewarHistoryForm" paramId="AWB" paramProperty="dewar.trackingNumberToSynchrotron" target="_new">
								<img src="<%=request.getContextPath()%>/images/dhl_logo.gif" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
							</layout:link>
						</logic:equal>
						<!--%--UPS--%-->
						<logic:equal name="viewDewarHistoryForm" property="shipping.deliveryAgentAgentName" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_UPS%>">
							<layout:link href="<%=Constants.TRACKING_URL_UPS%>" paramName="viewDewarHistoryForm" paramId="trackNums" paramProperty="dewar.trackingNumberToSynchrotron" target="_new">
								<img src="<%=request.getContextPath()%>/images/ups_logo.gif" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
							</layout:link>
						</logic:equal>
						<!--%--WorldCourier--%-->
						<logic:equal name="viewDewarHistoryForm" property="shipping.deliveryAgentAgentName" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_WORLDCOURIER%>">
							<layout:link href="<%=Constants.TRACKING_URL_WORLD_COURIER%>" paramName="viewDewarHistoryForm" paramId="hwb" paramProperty="dewar.trackingNumberToSynchrotron" target="_new">
								<img src="<%=request.getContextPath()%>/images/worldCourier_logo.jpg" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
							</layout:link>
						</logic:equal>
						<!--%--Chronopost--%-->
						<logic:equal name="viewDewarHistoryForm" property="shipping.deliveryAgentAgentName" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_CHRONOPOST%>">
							<html:link href="<%=Constants.TRACKING_URL_CHRONOPOST%>" paramName="dewar" paramId="hwb" paramProperty="trackingNumberToSynchrotron" target="_new">
								<img src="<%=request.getContextPath()%>/images/chronopost_logo.png" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
							</html:link>
						</logic:equal>
						
					</logic:present>
					</logic:present>
					
					<layout:text key="Expected arrival date" property="shipping.deliveryAgentDeliveryDate" type="date" styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:space/>
					
					<%-- ** EVENTS ** --%>
					<logic:empty name="viewDewarHistoryForm" property="dewarEvents">
				        <layout:row>
				           <h4>No event found for this dewar.</h4>
				        </layout:row>
				    </logic:empty>
					
					<logic:notEmpty name="viewDewarHistoryForm" property="dewarEvents">
						<layout:collection 	name="viewDewarHistoryForm" property="dewarEvents" 
										id="dewarEvent" styleClass="LIST" styleClass2="LIST2">
										
							<layout:collectionItem title="Date" sortable="false">
								<bean:write name="dewarEvent" property="arrivalDate" format="dd-MM-yyyy HH:mm"/>
							</layout:collectionItem>
							<layout:collectionItem title="Status"   property="dewarStatus" sortable="false"/>
							<layout:collectionItem title="Location" property="storageLocation" sortable="false"/>
						</layout:collection>
					</logic:notEmpty>
					<%-- ** End of EVENTS ** --%>
					
					<layout:space/>
					<layout:text key="Return date"  property="shipping.dateOfShippingToUser" type="date" styleClass="FIELD_INFO" mode="I,I,I"/>
					
					<layout:text key="Return courier" property="shipping.returnCourier" styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:text key="Return tracking number" property="dewar.trackingNumberFromSynchrotron" styleClass="FIELD_INFO" mode="I,I,I"/>
					
					<logic:present name="viewDewarHistoryForm" property="shipping.returnCourier">
					<logic:present name="viewDewarHistoryForm" property="dewar.trackingNumberFromSynchrotron">
						<!--%--Fedex--%-->
						<logic:equal name="viewDewarHistoryForm" property="shipping.returnCourier" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_FEDEX%>">
							<layout:link href="<%=Constants.TRACKING_URL_FEDEX%>" paramName="viewDewarHistoryForm" paramId="tracknumbers" paramProperty="dewar.trackingNumberFromSynchrotron" target="_new">
								<img src="<%=request.getContextPath()%>/images/fedex_express_logo.gif" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
							</layout:link>
						</logic:equal>
						<!--%--TNT--%-->
						<logic:equal name="viewDewarHistoryForm" property="shipping.returnCourier" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_TNT%>">
							<layout:link href="<%=Constants.TRACKING_URL_TNT%>" paramName="viewDewarHistoryForm" paramId="cons" paramProperty="dewar.trackingNumberFromSynchrotron" target="_new">
								<img src="<%=request.getContextPath()%>/images/tnt_logo.gif" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
							</layout:link>
						</logic:equal>
						<!--%--DHL--%-->
						<logic:equal name="viewDewarHistoryForm" property="shipping.returnCourier" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_DHL%>">
							<layout:link href="<%=Constants.TRACKING_URL_DHL%>" paramName="viewDewarHistoryForm" paramId="AWB" paramProperty="dewar.trackingNumberFromSynchrotron" target="_new">
								<img src="<%=request.getContextPath()%>/images/dhl_logo.gif" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
							</layout:link>
						</logic:equal>
						<!--%--UPS--%-->
						<logic:equal name="viewDewarHistoryForm" property="shipping.returnCourier" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_UPS%>">
							<layout:link href="<%=Constants.TRACKING_URL_UPS%>" paramName="viewDewarHistoryForm" paramId="trackNums" paramProperty="dewar.trackingNumberFromSynchrotron" target="_new">
								<img src="<%=request.getContextPath()%>/images/ups_logo.gif" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
							</layout:link>
						</logic:equal>
						<!--%--WorldCourier--%-->
						<logic:equal name="viewDewarHistoryForm" property="shipping.returnCourier" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_WORLDCOURIER%>">
							<layout:link href="<%=Constants.TRACKING_URL_WORLD_COURIER%>" paramName="viewDewarHistoryForm" paramId="hwb" paramProperty="dewar.trackingNumberFromSynchrotron" target="_new">
								<img src="<%=request.getContextPath()%>/images/worldCourier_logo.jpg" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
							</layout:link>
						</logic:equal>
						<!--%--Chronopost--%-->
						<logic:equal name="viewDewarHistoryForm" property="shipping.returnCourier" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_CHRONOPOST%>">
							<layout:link href="<%=Constants.TRACKING_URL_CHRONOPOST%>" paramName="viewDewarHistoryForm" paramId="hwb" paramProperty="dewar.trackingNumberFromSynchrotron" target="_new">
								<img src="<%=request.getContextPath()%>/images/chronopost_logo.png" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
							</layout:link>
						</logic:equal>
						
						
					</logic:present>
					</logic:present>
					
				</layout:panel>
			</layout:grid>
		
		</layout:tab>
		
		<%-- Shipment --%>	
		<layout:tab key="Shipment Information" width="200">
		
			<layout:grid cols="1"  borderSpacing="5">
				<layout:panel key="Shipment information" align="left" styleClass="PANEL">
					<layout:text key="Shipment name"     property="shipping.shippingName" styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:text key="Number of components" property="parcelsNumber" styleClass="FIELD_INFO" mode="I,I,I"/>
				</layout:panel>
			</layout:grid>
			
			<layout:grid cols="2"  borderSpacing="5">	
				<layout:panel key="Lab-contact for sending" align="left" styleClass="PANEL">
				<logic:present name="viewDewarHistoryForm" property="sendingPerson">
					<layout:text key="Name"        property="sendingPerson.familyName" 	styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:text key="Firstname"   property="sendingPerson.givenName" 	styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:text key="Phone"       property="sendingPerson.phoneNumber" styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:text key="Fax"         property="sendingPerson.faxNumber" 	styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:text key="Email"       property="sendingPerson.emailAddress" styleClass="FIELD_INFO" mode="I,I,I"/>
				</logic:present>
				<logic:present name="viewDewarHistoryForm" property="sendingLaboratory">
					<layout:text key="Lab name"    property="sendingLaboratory.name" 	styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:textarea key="Lab address" property="sendingLaboratory.address" styleClass="FIELD_INFO" mode="I,I,I" cols="20"/>
				</logic:present>
				</layout:panel>
	
				<layout:panel key="Lab-contact for return" align="left" styleClass="PANEL">
				<logic:present name="viewDewarHistoryForm" property="returnPerson">
					<layout:text key="Name"        property="returnPerson.familyName" 	styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:text key="Firstname"   property="returnPerson.givenName" 	styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:text key="Phone"       property="returnPerson.phoneNumber"  styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:text key="Fax"         property="returnPerson.faxNumber" 	styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:text key="Email"       property="returnPerson.emailAddress" styleClass="FIELD_INFO" mode="I,I,I"/>
				</logic:present>
				<logic:present name="viewDewarHistoryForm" property="returnLaboratory">
					<layout:text key="Lab name"        property="returnLaboratory.name"    styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:textarea key="Lab address" property="returnLaboratory.address" styleClass="FIELD_INFO" mode="I,I,I" cols="20"/>
				</logic:present>
				</layout:panel>
			</layout:grid>
			
		</layout:tab>

	</layout:tabs>
	
</layout:form>
