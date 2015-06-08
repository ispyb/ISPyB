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
	String dewarPanelTitle			= "All Dewars in Shipment : ";
%>

<layout:skin includeScript="true"/>
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<%-- Retrieve a few things from the Beans --%>
<logic:present name="breadCrumbsForm" property="selectedShipping" scope="session">	
	<bean:define name="breadCrumbsForm" property="selectedShipping.shippingName" id="selectedShippingCode" 	type="java.lang.String"/>
	<% dewarPanelTitle += selectedShippingCode; %>
</logic:present>

<%-- BreadCrumbs bar --%>
<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />

<%-- "View all" links --%>
<layout:row>

		<layout:link href="<%=targetViewShippings%>" styleClass="PANEL_BREAD_CRUMBS">
			<img src="<%=request.getContextPath()%>/images/up.gif" border=0 onmouseover="return overlib('View All Shipments');" onmouseout="return nd();">
			View all Shipments
		</layout:link>

</layout:row>
	
<%-- History --%>
<layout:form action="/reader/viewShippingHistoryAction" reqCode="display">

	<layout:tabs width="600">
		
		<%-- Shipment --%>
		<layout:tab key="Shipment Information" width="200">
			
			<layout:grid cols="1"  borderSpacing="5">
				<layout:panel key="Details" align="left" styleClass="PANEL">
					<layout:text key="Label"     property="shipping.shippingName" styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:text key="Number of components" property="parcelsNumber" styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:text key="Creation date" property="shipping.creationDate" type="date" styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:text key="Sending date" property="shipping.deliveryAgentShippingDate" type="date" styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:text key="Expected arrival date" property="shipping.deliveryAgentDeliveryDate" type="date" styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:text key="Return date"  property="shipping.dateOfShippingToUser" type="date" styleClass="FIELD_INFO" mode="I,I,I"/>
				</layout:panel>
			</layout:grid>
			
			<layout:grid cols="2"  borderSpacing="5">
				<layout:panel key="Lab-contact for sending" align="left" styleClass="PANEL">
					<layout:text key="Name"        property="sendingPerson.familyName" 	styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:text key="Firstname"   property="sendingPerson.givenName" 	styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:text key="Phone"       property="sendingPerson.phoneNumber" styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:text key="Fax"         property="sendingPerson.faxNumber" 	styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:text key="Email"       property="sendingPerson.emailAddress" styleClass="FIELD_INFO" mode="I,I,I"/>
					
					<layout:text key="Lab name"    property="sendingLaboratory.name" 	styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:textarea key="Lab address" property="sendingLaboratory.address" styleClass="FIELD_INFO" mode="I,I,I" cols="20"/>
				</layout:panel>
				
				<layout:panel key="Lab-contact for return" align="left" styleClass="PANEL">
					<layout:text key="Name"        property="returnPerson.familyName" 	styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:text key="Firstname"   property="returnPerson.givenName" 	styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:text key="Phone"       property="returnPerson.phoneNumber"  styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:text key="Fax"         property="returnPerson.faxNumber" 	styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:text key="Email"       property="returnPerson.emailAddress" styleClass="FIELD_INFO" mode="I,I,I"/>
					
					<layout:text key="Lab name"        property="returnLaboratory.name"    styleClass="FIELD_INFO" mode="I,I,I"/>
					<layout:textarea key="Lab address" property="returnLaboratory.address" styleClass="FIELD_INFO" mode="I,I,I" cols="20"/>
				</layout:panel>
			</layout:grid>
			
		</layout:tab>
		
		<%-- Dewars --%>
		<layout:tab key="Dewar History" width="200">
			<layout:grid cols="2"  borderSpacing="5">	
								
					<logic:empty name="viewShippingHistoryForm" property="dewars">
				        <layout:row>
				           <h4>No dewar found for this shipping.</h4>
				        </layout:row>
				    </logic:empty>
					
					<logic:notEmpty name="viewShippingHistoryForm" property="dewars">
					
						<%-- LOOP ON THE DEWARS --%>
						<logic:iterate name="viewShippingHistoryForm" property="dewars" id="dewar" indexId="index" type="ispyb.server.common.vos.shipping.Dewar3VO">
							
							<%  //int numero = new Integer(index) + 1;
								String panelTitle = dewar.getCode()+" history view";
							%>
							<layout:panel key="<%=panelTitle%>" align="left" styleClass="PANEL">
								
								<layout:text key="Label"   name="dewar" property="code"    styleClass="FIELD_INFO" mode="I,I,I"/>
								<layout:text key="Barcode" name="dewar" property="barCode" styleClass="FIELD_INFO" mode="I,I,I"/>
								<layout:text key="Average customs values (Euro)"   name="dewar" property="customsValue" styleClass="FIELD_INFO" mode="I,I,I"/>
								<layout:text key="Average transport values (Euro)" name="dewar" property="transportValue" styleClass="FIELD_INFO" mode="I,I,I"/>
								<layout:space/>
								<layout:text key="Outbound courier" property="shipping.deliveryAgentAgentName" styleClass="FIELD_INFO" mode="I,I,I"/>
								<layout:text key="Outbound tracking number" name="dewar" property="trackingNumberToSynchrotron" styleClass="FIELD_INFO" mode="I,I,I"/>
								
								<logic:present name="viewShippingHistoryForm" property="shipping.deliveryAgentAgentName">
								<logic:present name="dewar" property="trackingNumberToSynchrotron">
								<logic:notEmpty name="dewar" property="trackingNumberToSynchrotron">
									<!--%--Fedex--%-->
									<logic:equal name="viewShippingHistoryForm" property="shipping.deliveryAgentAgentName" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_FEDEX%>">
										<layout:link href="<%=Constants.TRACKING_URL_FEDEX%>" paramName="dewar" paramId="tracknumbers"
											 paramProperty="trackingNumberToSynchrotron" target="_new">
											<img src="<%=request.getContextPath()%>/images/fedex_express_logo.gif" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
										</layout:link>
									</logic:equal>
									<!--%--TNT--%-->
									<logic:equal name="viewShippingHistoryForm" property="shipping.deliveryAgentAgentName" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_TNT%>">
										<layout:link href="<%=Constants.TRACKING_URL_TNT%>" paramName="dewar" paramId="cons"
											 paramProperty="trackingNumberToSynchrotron" target="_new">
											<img src="<%=request.getContextPath()%>/images/tnt_logo.gif" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
										</layout:link>
									</logic:equal>
									<!--%--DHL--%-->
									<logic:equal name="viewShippingHistoryForm" property="shipping.deliveryAgentAgentName" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_DHL%>">
										<layout:link href="<%=Constants.TRACKING_URL_DHL%>" paramName="dewar" paramId="AWB"
										 	paramProperty="trackingNumberToSynchrotron" target="_new">
											<img src="<%=request.getContextPath()%>/images/dhl_logo.gif" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
										</layout:link>
									</logic:equal>
									<!--%--UPS--%-->
									<logic:equal name="viewShippingHistoryForm" property="shipping.deliveryAgentAgentName" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_UPS%>">
										<layout:link href="<%=Constants.TRACKING_URL_UPS%>" paramName="dewar" paramId="trackNums"
											 paramProperty="trackingNumberToSynchrotron" target="_new">
											<img src="<%=request.getContextPath()%>/images/ups_logo.gif" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
										</layout:link>
									</logic:equal>
									<!--%--WorldCourier--%-->
									<logic:equal name="viewShippingHistoryForm" property="shipping.deliveryAgentAgentName" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_WORLDCOURIER%>">
										<layout:link href="<%=Constants.TRACKING_URL_WORLD_COURIER%>" paramName="dewar" paramId="hwb"
											 paramProperty="trackingNumberToSynchrotron" target="_new">
											<img src="<%=request.getContextPath()%>/images/worldCourier_logo.jpg" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
										</layout:link>
									</logic:equal>
									<!--%--Chronopost--%-->
									<logic:equal name="viewShippingHistoryForm" property="shipping.deliveryAgentAgentName" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_CHRONOPOST%>">
										<layout:link href="<%=Constants.TRACKING_URL_CHRONOPOST%>" paramName="dewar" paramId="hwb"
											 paramProperty="trackingNumberToSynchrotron" target="_new">
											<img src="<%=request.getContextPath()%>/images/chronopost_logo.jpg" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
										</layout:link>
									</logic:equal>
									
								</logic:notEmpty>
								</logic:present>
								</logic:present>
								
								
								<%-- ** EVENTS ** --%>
								<logic:empty name="dewar" property="dewarTransportHistoryVOs">
							        <layout:row>
							           <h4>No event found for this dewar.</h4>
							        </layout:row>
							    </logic:empty>
								
								<logic:notEmpty name="dewar" property="dewarTransportHistoryVOs">
									<layout:collection 	name="dewar" property="dewarTransportHistoryVOs" 
													id="dewarEvent" styleClass="LIST" styleClass2="LIST2">
													
										<layout:collectionItem title="Date" sortable="false">
											<bean:write name="dewarEvent" property="arrivalDate" format="dd-MM-yyyy HH:mm"/>
										</layout:collectionItem>
										<layout:collectionItem title="Status"   name="dewarEvent" property="dewarStatus" sortable="false"/>
										<layout:collectionItem title="Location" name="dewarEvent" property="storageLocation" sortable="false"/>
									</layout:collection>
								</logic:notEmpty>
								<%-- ** End of EVENTS ** --%>
								<layout:space/>
								
								<layout:text key="Return courier" property="shipping.returnCourier" styleClass="FIELD_INFO" mode="I,I,I"/>
								<layout:text key="Return tracking number" name="dewar" property="trackingNumberFromSynchrotron" styleClass="FIELD_INFO" mode="I,I,I"/>
								
								<logic:present name="viewShippingHistoryForm" property="shipping.returnCourier">
								<logic:present name="dewar" property="trackingNumberFromSynchrotron">
								<logic:notEmpty name="dewar" property="trackingNumberFromSynchrotron">
									<!--%--Fedex--%-->
									<logic:equal name="viewShippingHistoryForm" property="shipping.returnCourier" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_FEDEX%>">
										<layout:link href="<%=Constants.TRACKING_URL_FEDEX%>" paramName="dewar" paramId="tracknumbers"
											 paramProperty="trackingNumberFromSynchrotron" target="_new">
											<img src="<%=request.getContextPath()%>/images/fedex_express_logo.gif" border="0" onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
										</layout:link>
									</logic:equal>
									<!--%--TNT--%-->
									<logic:equal name="viewShippingHistoryForm" property="shipping.returnCourier" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_TNT%>">
										<layout:link href="<%=Constants.TRACKING_URL_TNT%>" paramName="dewar" paramId="cons"
											 paramProperty="trackingNumberFromSynchrotron" target="_new">
											<img src="<%=request.getContextPath()%>/images/tnt_logo.gif" border="0" onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
										</layout:link>
									</logic:equal>
									<!--%--DHL--%-->
									<logic:equal name="viewShippingHistoryForm" property="shipping.returnCourier" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_DHL%>">
										<layout:link href="<%=Constants.TRACKING_URL_DHL%>" paramName="dewar" paramId="AWB"
											 paramProperty="trackingNumberFromSynchrotron" target="_new">
											<img src="<%=request.getContextPath()%>/images/dhl_logo.gif" border="0" onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
										</layout:link>
									</logic:equal>
									<!--%--UPS--%-->
									<logic:equal name="viewShippingHistoryForm" property="shipping.returnCourier" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_UPS%>">
										<layout:link href="<%=Constants.TRACKING_URL_UPS%>" paramName="dewar" paramId="trackNums"
											 paramProperty="trackingNumberFromSynchrotron" target="_new">
											<img src="<%=request.getContextPath()%>/images/ups_logo.gif" border="0" onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
										</layout:link>
									</logic:equal>
									<!--%--WorldCourier--%-->
									<logic:equal name="viewShippingHistoryForm" property="shipping.returnCourier" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_WORLDCOURIER%>">
										<layout:link href="<%=Constants.TRACKING_URL_WORLD_COURIER%>" paramName="dewar" paramId="hwb"
											 paramProperty="trackingNumberFromSynchrotron" target="_new">
											<img src="<%=request.getContextPath()%>/images/worldCourier_logo.jpg" border="0" onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
										</layout:link>
									</logic:equal>
									<!--%--Chronopost--%-->
									<logic:equal name="viewShippingHistoryForm" property="shipping.returnCourier" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_CHRONOPOST%>">
										<layout:link href="<%=Constants.TRACKING_URL_CHRONOPOST%>" paramName="dewar" paramId="hwb"
											 paramProperty="trackingNumberFromSynchrotron" target="_new">
											<img src="<%=request.getContextPath()%>/images/chronopost_logo.jpg" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
										</layout:link>
									</logic:equal>
									
								</logic:notEmpty>
								</logic:present>
								</logic:present>
								
							</layout:panel>
							
						</logic:iterate>
						
					</logic:notEmpty>
					
			</layout:grid>
		</layout:tab>
		
	</layout:tabs>
	
</layout:form>
	
