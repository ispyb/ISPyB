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

<%@ page isELIgnored="false" %>

<%
	String targetSubmitShipping 	= request.getContextPath() + "/user/submitShippingAction.do?reqCode=display";
	
	String targetContainer 			= request.getContextPath() + "/menuSelected.do?leftMenuId=11&topMenuId=10&targetUrl=/user/viewSample.do?reqCode=displayForContainer";
	String targetDewar 				= request.getContextPath() + "/menuSelected.do?leftMenuId=71&topMenuId=5&targetUrl=/user/viewDewarAction.do?reqCode=displaySlave";
	String targetDewarHistory		= request.getContextPath() + "/reader/viewDewarHistoryAction.do?reqCode=display";
	
	String targetDewarLabels		= 	(Constants.SITE_IS_ESRF())?	request.getContextPath() + "/reader/viewDewarAction.do?reqCode=generateLabels":
										(Constants.SITE_IS_DLS())?	request.getContextPath() + "/user/viewDewarAction.do?reqCode=generateLabels":
										(Constants.SITE_IS_EMBL())?	request.getContextPath() + "/reader/viewDewarAction.do?reqCode=generateLabels":	
										(Constants.SITE_IS_MAXIV())? request.getContextPath() + "/reader/viewDewarAction.do?reqCode=generateLabels":	
										"";
	
	String targetSample 			= request.getContextPath() + "/menuSelected.do?leftMenuId=11&topMenuId=10&targetUrl=/user/viewSample.do?reqCode=displayForDewar";
	String targetCreateDewar 		= request.getContextPath() + "/menuSelected.do?leftMenuId=6&topMenuId=5&targetUrl=/user/createDewarAction.do?reqCode=display";
	String targetCreateContainer	= request.getContextPath() + "/menuSelected.do?leftMenuId=6&topMenuId=5&targetUrl=/user/createPuckAction.do?reqCode=display";
	
	String targetUpdateContainer	= request.getContextPath() + "/menuSelected.do?leftMenuId=6&topMenuId=5&targetUrl=/user/createContainerAction.do?reqCode=updateDisplay";
	String targetCreateSample		= request.getContextPath() + "/menuSelected.do?leftMenuId=6&topMenuId=5&targetUrl=/user/createSampleAction.do?reqCode=display";
	String targetDeleteContainer	= request.getContextPath() + "/menuSelected.do?leftMenuId=6&topMenuId=5&targetUrl=/user/createContainerAction.do?reqCode=delete";
	String targetCloneContainer		= request.getContextPath() + "/menuSelected.do?leftMenuId=8&topMenuId=5&targetUrl=/user/createContainerAction.do?reqCode=clone";
	
	String targetUpdateDewar		= request.getContextPath() + "/menuSelected.do?leftMenuId=6&topMenuId=5&targetUrl=/user/createDewarAction.do?reqCode=updateDisplay";
	String targetDeleteDewar		= request.getContextPath() + "/menuSelected.do?leftMenuId=6&topMenuId=5&targetUrl=/user/createDewarAction.do?reqCode=delete";
	String targetGetReimbursed 	= request.getContextPath() + "/reader/viewDewarAction.do?reqCode=getReimbursed";
	
	String targetViewShippings		= request.getContextPath() + "/reader/genericShippingAction.do?reqCode=display";	
	String targetViewDewars			= request.getContextPath() + "/menuSelected.do?leftMenuId=8&topMenuId=5&targetUrl=/user/viewDewarAction.do?reqCode=display";
	String selectedDewarTitle		= 	"<a href=\"" + targetViewDewars + "\">" +
										"<img src=\"" + request.getContextPath() + "/images/up.gif\" border=0 onmouseover=\"return overlib('View Dewars');\" onmouseout=\"return nd();\">" +
										"</a>" +
										"Selected Dewar";
	String dewarPanelTitle			= "All Components in Shipment : ";
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
	dewarPanelTitle 		= "Selected Component : " + selectedDewarCode;
	containerPanelTitle		+= selectedDewarCode;
	%>
</logic:present>

<%-- BreadCrumbs bar --%>
<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />
<%-------------------------%>

<%-- Errors --%>
<jsp:include page="/tiles/common/messages/error.jsp" flush="true" />
<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />

<%--Warning message  (only if shippingType is equal to DewarTracking) 
<logic:equal name="breadCrumbsForm" property="selectedShipping.shippingType" value="<%=Constants.DEWAR_TRACKING_SHIPPING_TYPE%>">
	<logic:equal name="breadCrumbsForm" property="selectedShipping.shippingStatus" value="<%=Constants.SHIPPING_STATUS_OPENED%>">
		<font color="red">1) Please print your component labels & stick them on each transport box & component prior to sending.</font>
	</logic:equal>
</logic:equal>
--%>



<%-------------------------------------------------------- "View all" links ------------------------------------------------------%>
<layout:row>

		<layout:link href="<%=targetViewShippings%>" styleClass="PANEL_BREAD_CRUMBS">
			<img src="<%=request.getContextPath()%>/images/up.gif" border=0 onmouseover="return overlib('View All Shipments');" onmouseout="return nd();">
			View all Shipments
		</layout:link>

	<%-- Link visible only when a dewar is selected --%>
	<logic:present name="breadCrumbsForm" property="selectedDewar" scope="session">	
		&nbsp;&nbsp;&nbsp;&nbsp;
		
		<layout:link href="<%=targetViewDewars%>" styleClass="PANEL_BREAD_CRUMBS">
			<img src="<%=request.getContextPath()%>/images/up.gif" border="0" onmouseover="return overlib('View All Dewars in Shipment');" onmouseout="return nd();">
			View all Components in this Shipment
		</layout:link>
	</logic:present>

</layout:row>
	
<%-------------------------------------------------------- Define titles ------------------------------------------------------%>
	<%-- To not show a empty table when no dewar exists --%>
<layout:grid cols="1"  borderSpacing="10">
	<logic:present name="breadCrumbsForm" property="selectedShipping" scope="session">	
		<logic:empty name="viewDewarForm" property="listInfo">
	        <layout:row>
	           <h4>There&nbsp;are&nbsp;no&nbsp;dewars&nbsp;in&nbsp;this&nbsp;shipment, you may add a dewar by clicking on the above link</h4>
	        </layout:row>
	    </logic:empty>
	    
		
		
	</logic:present>
	<%-------------------------------------------------------- Dewars ------------------------------------------------------%>
	<c:set var="nbReimb"  value="${viewDewarForm.nbReimbursedDewars}" scope="request" />
	<c:set var="currentReimb"  value="${viewDewarForm.currentReimbursedDewars}" scope="request"/>
	<c:if test="${nbReimb > 0 || currentReimb > 0}">
		<c:if test="${nbReimb >= currentReimb}">
			<br><font color="green">You have selected ${currentReimb} reimbursed dewars out of ${nbReimb} allowed.</font>
		</c:if>						
		<c:if test="${nbReimb < currentReimb}">
			<br><font color="red">You have selected ${currentReimb} reimbursed dewars out of ${nbReimb} allowed! Please modify.</font>
		</c:if>
	</c:if>


	<%-- To not show a empty table when no dewar exists --%>
	<logic:empty  name="viewDewarForm" property="listInfo">
           <h4>No&nbsp;Dewar&nbsp;have&nbsp;been&nbsp;found</h4>
	</logic:empty>
	<logic:notEmpty name="viewDewarForm" property="listInfo">
      	 <layout:row>

		<layout:panel key="<%=dewarPanelTitle%>" align="left" styleClass="PANEL" width="100%">
				
				<%-- Shipping comments visible only if there is no dewar selected --%>
				<logic:present name="breadCrumbsForm" property="selectedShipping" scope="session">
				<logic:notPresent name="breadCrumbsForm" property="selectedDewar" scope="session">	
					<layout:cell>
						<span class="FIELD">Shipment comments :<br/>
							<i><bean:write name="breadCrumbsForm" property="selectedShipping.comments"/></i>
						</span>
					</layout:cell>
				</logic:notPresent>
				</logic:present>
				<logic:greaterThan name="viewDewarForm" property="nbReimbursedDewars" value="0">	
					<layout:cell>
						<font color="red" >According to the A-form for this experiment, you are allowed to have 
						<bean:write name="viewDewarForm" property="nbReimbursedDewars"/> dewars reimbursed by the ESRF. 
						Please use the euro icon to select/unselect the dewars to be reimbursed.
						<br>Your FedEx Reference for this shipment:</font> <bean:write name="viewDewarForm" property="fedexCode"/>
												
					</layout:cell>
				</logic:greaterThan>
				
				<%-- COLLECTION --%>
				<layout:collection 	name="viewDewarForm" 
									property="listInfo" 
									styleClass="LIST" 
									id="dewar" 
									styleClass2="LIST2">
										
					<%-- Highlight selected Dewar --%>
					<layout:collectionStyle name="dewar" property="dewarId" valueName="viewDewarForm" valueProperty="dewarId" matchStyleClass="HIGHLIGHT"> 
						
						<%-- Type --%>
						<layout:collectionItem title="Type" property="type" sortable="true">
							<center>
								<logic:equal name="dewar" property="type" value="<%=Constants.PARCEL_TOOLBOX_TYPE%>">
									<img title="Toolbox" alt="Toolbox" src="<%=request.getContextPath()%>/images/toolbox.png" border="0">
								</logic:equal>
								<logic:equal name="dewar" property="type" value="<%=Constants.PARCEL_DEWAR_TYPE%>">
									<img title="Dewar" alt="Dewar" src="<%=request.getContextPath()%>/images/Dewar_32x32_01.png" border="0">
								</logic:equal>
								<logic:equal name="dewar"  property="isReimbursed" value="true">
								(R)
								</logic:equal>
							</center>
						</layout:collectionItem>
						
						<%-- Name --%>
						<logic:equal name="breadCrumbsForm"  property="userOrIndus" value="true">
							<layout:collectionItem title="Comp. name" 	property="code" paramId="dewarId" paramProperty="dewarId" href="<%=targetDewar%>" sortable="true"/>
						</logic:equal>
						<logic:notEqual name="breadCrumbsForm"  property="userOrIndus" value="true">
							<layout:collectionItem title="Comp. name" 	property="code" sortable="true"/>
						</logic:notEqual>
						
						<%-- Comment --%>
						<layout:collectionItem title="Comments" property="comments" sortable="false"/>
						
						<%-- Barcode --%>
						<logic:equal name="breadCrumbsForm" property="selectedShipping.shippingType" value="<%=Constants.DEWAR_TRACKING_SHIPPING_TYPE%>">
							<layout:collectionItem title="Barcode" property="barCode" sortable="false"/>
						</logic:equal>
						<logic:notEqual name="breadCrumbsForm" property="selectedShipping.shippingType" value="<%=Constants.DEWAR_TRACKING_SHIPPING_TYPE%>">
							<layout:collectionItem title="Barcode" sortable="false">
								<center>Not tracked dewar</center>
							</layout:collectionItem>
						</logic:notEqual>
						
						<%-- Exp. and Beamline --%>
						<layout:collectionItem title="Exp. date and beamline" sortable="false">				
						<c:if test="${SITE_ATTRIBUTE eq 'ESRF'}">
							<logic:empty name="dewar" property="sessionVO">
							   <html:link href="<%=targetUpdateDewar%>" paramName="dewar" paramId="dewarId" paramProperty="dewarId">
								    <center><img src="<%=request.getContextPath()%>/images/warning_01.png" border="0" onmouseover="return overlib('You must assign a beamline to this dewar');" onmouseout="return nd()""></center>
							   </html:link>
              				</logic:empty>
              			</c:if>
              			<c:if test="${SITE_ATTRIBUTE eq 'EMBL'}">
							<logic:empty name="dewar" property="sessionVO">
							   <html:link href="<%=targetUpdateDewar%>" paramName="dewar" paramId="dewarId" paramProperty="dewarId">
								    <center><img src="<%=request.getContextPath()%>/images/warning_01.png" border="0" onmouseover="return overlib('You must assign a beamline to this dewar');" onmouseout="return nd()""></center>
							   </html:link>
              				</logic:empty>
              			</c:if>
              			<c:if test="${SITE_ATTRIBUTE eq 'MAXIV'}">
							<logic:empty name="dewar" property="sessionVO">
							   <html:link href="<%=targetUpdateDewar%>" paramName="dewar" paramId="dewarId" paramProperty="dewarId">
								    <center><img src="<%=request.getContextPath()%>/images/warning_01.png" border="0" onmouseover="return overlib('You must assign a beamline to this dewar');" onmouseout="return nd()""></center>
							   </html:link>
              				</logic:empty>
              			</c:if>
							<logic:notEmpty name="dewar" property="sessionVO">
									<bean:write name="dewar" property="sessionVO.startDate" format="dd/MM/yyyy"/><br>
									<bean:write name="dewar" property="sessionVO.beamlineName"/>
							</logic:notEmpty>
						</layout:collectionItem>
						
						<%-- Tracking numbers TO Synchrotron --%>
						<layout:collectionItem title="tracking # TO Synchrotron" sortable="false">
							<center>
							<bean:write name="breadCrumbsForm" property="selectedShipping.deliveryAgentAgentName"/><br>
							<bean:write name="dewar" property="trackingNumberToSynchrotron"/><br>
							<logic:present name="breadCrumbsForm" property="selectedShipping.deliveryAgentAgentName">
							<logic:present name="dewar" property="trackingNumberToSynchrotron">
								<!--%--Fedex--%-->
								<logic:equal name="breadCrumbsForm" property="selectedShipping.deliveryAgentAgentName" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_FEDEX%>">
									<html:link href="<%=Constants.TRACKING_URL_FEDEX%>" paramName="dewar" paramId="tracknumbers" paramProperty="trackingNumberToSynchrotron" target="_new">
										<img src="<%=request.getContextPath()%>/images/fedex_express_logo.gif" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
									</html:link>
								</logic:equal>
								<!--%--TNT--%-->
								<logic:equal name="breadCrumbsForm" property="selectedShipping.deliveryAgentAgentName" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_TNT%>">
									<html:link href="<%=Constants.TRACKING_URL_TNT%>" paramName="dewar" paramId="cons" paramProperty="trackingNumberToSynchrotron" target="_new">
										<img src="<%=request.getContextPath()%>/images/tnt_logo.gif" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
									</html:link>
								</logic:equal>
								<!--%--DHL--%-->
								<logic:equal name="breadCrumbsForm" property="selectedShipping.deliveryAgentAgentName" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_DHL%>">
									<html:link href="<%=Constants.TRACKING_URL_DHL%>" paramName="dewar" paramId="AWB" paramProperty="trackingNumberToSynchrotron" target="_new">
										<img src="<%=request.getContextPath()%>/images/dhl_logo.gif" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
									</html:link>
								</logic:equal>
								<!--%--UPS--%-->
								<logic:equal name="breadCrumbsForm" property="selectedShipping.deliveryAgentAgentName" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_UPS%>">
									<html:link href="<%=Constants.TRACKING_URL_UPS%>" paramName="dewar" paramId="trackNums" paramProperty="trackingNumberToSynchrotron" target="_new">
										<img src="<%=request.getContextPath()%>/images/ups_logo.gif" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
									</html:link>
								</logic:equal>
								<!--%--WorldCourier--%-->
								<logic:equal name="breadCrumbsForm" property="selectedShipping.deliveryAgentAgentName" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_WORLDCOURIER%>">
									<html:link href="<%=Constants.TRACKING_URL_WORLD_COURIER%>" paramName="dewar" paramId="hwb" paramProperty="trackingNumberToSynchrotron" target="_new">
										<img src="<%=request.getContextPath()%>/images/worldCourier_logo.jpg" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
									</html:link>
								</logic:equal>
								<!--%--Chronopost--%-->
						<logic:equal name="breadCrumbsForm" property="selectedShipping.deliveryAgentAgentName" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_CHRONOPOST%>">
							<html:link href="<%=Constants.TRACKING_URL_CHRONOPOST%>" paramName="dewar" paramId="hwb" paramProperty="trackingNumberToSynchrotron" target="_new">
								<img src="<%=request.getContextPath()%>/images/chronopost_logo.png" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
							</html:link>
						</logic:equal>
							</logic:present>
							</logic:present>
							</center>
						</layout:collectionItem>
						
						<%-- Tracking numbers FROM Synchrotron --%>
						<layout:collectionItem title="Tracking # FROM Synchrotron" sortable="false">
							<center>
							<bean:write name="breadCrumbsForm" property="selectedShipping.returnCourier"/><br>
							<bean:write name="dewar" property="trackingNumberFromSynchrotron"/><br>	
							<logic:present name="breadCrumbsForm" property="selectedShipping.returnCourier">
							<logic:present name="dewar" property="trackingNumberFromSynchrotron">
								<!--%--Fedex--%-->
								<logic:equal name="breadCrumbsForm" property="selectedShipping.returnCourier" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_FEDEX%>">
									<html:link href="<%=Constants.TRACKING_URL_FEDEX%>" paramName="dewar" paramId="tracknumbers" paramProperty="trackingNumberFromSynchrotron" target="_new">
										<img src="<%=request.getContextPath()%>/images/fedex_express_logo.gif" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
									</html:link>
								</logic:equal>
								<!--%--TNT--%-->
								<logic:equal name="breadCrumbsForm" property="selectedShipping.returnCourier" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_TNT%>">
									<html:link href="<%=Constants.TRACKING_URL_TNT%>" paramName="dewar" paramId="cons" paramProperty="trackingNumberFromSynchrotron" target="_new">
										<img src="<%=request.getContextPath()%>/images/tnt_logo.gif" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
									</html:link>
								</logic:equal>
								<!--%--DHL--%-->
								<logic:equal name="breadCrumbsForm" property="selectedShipping.returnCourier" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_DHL%>">
									<html:link href="<%=Constants.TRACKING_URL_DHL%>" paramName="dewar" paramId="AWB" paramProperty="trackingNumberFromSynchrotron" target="_new">
										<img src="<%=request.getContextPath()%>/images/dhl_logo.gif" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
									</html:link>
								</logic:equal>
								<!--%--UPS--%-->
								<logic:equal name="breadCrumbsForm" property="selectedShipping.returnCourier" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_UPS%>">
									<html:link href="<%=Constants.TRACKING_URL_UPS%>" paramName="dewar" paramId="trackNums" paramProperty="trackingNumberFromSynchrotron" target="_new">
										<img src="<%=request.getContextPath()%>/images/ups_logo.gif" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
									</html:link>
								</logic:equal>
								<!--%--WorldCourier--%-->
								<logic:equal name="breadCrumbsForm" property="selectedShipping.returnCourier" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_WORLDCOURIER%>">
									<html:link href="<%=Constants.TRACKING_URL_WORLD_COURIER%>" paramName="dewar" paramId="hwb" paramProperty="trackingNumberFromSynchrotron" target="_new">
										<img src="<%=request.getContextPath()%>/images/worldCourier_logo.jpg" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
									</html:link>
								</logic:equal>
								<!--%--Chronopost--%-->
								<logic:equal name="breadCrumbsForm" property="selectedShipping.returnCourier" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_CHRONOPOST%>">
									<html:link href="<%=Constants.TRACKING_URL_CHRONOPOST%>" paramName="dewar" paramId="hwb" paramProperty="trackingNumberFromSynchrotron" target="_new">
										<img src="<%=request.getContextPath()%>/images/chronopost_logo.png" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
									</html:link>
								</logic:equal>
	
							</logic:present>
							</logic:present>
							</center>
						</layout:collectionItem>
						
						<%-- Status and Location --%>
						<layout:collectionItem title="Comp. status"  property="dewarStatus"   sortable="true"/>
						<layout:collectionItem title="${SITE_ATTRIBUTE} Location" property="storageLocation" sortable="true"/>
						
                        <%-- View containers and samples (User only) + managers (Isssue 1789) --%>
						<logic:present name="breadCrumbsForm"  property="userOrIndus">
		        		<logic:equal name="breadCrumbsForm"  property="userOrIndus" value="true">
							<layout:collectionItem title="View Containers" property="dewarId" paramId="dewarId" paramProperty="dewarId" href="<%=targetDewar%>" sortable="false">
								<center>
									<logic:equal name="dewar" property="type" value="<%=Constants.PARCEL_TOOLBOX_TYPE%>">
										<img src="<%=request.getContextPath()%>/images/blank.gif" border="0">	
									</logic:equal>
									<logic:equal name="dewar" property="type" value="<%=Constants.PARCEL_DEWAR_TYPE%>">
										<img src="<%=request.getContextPath()%>/images/BasketView_24x24_01.png" border="0" onmouseover="return overlib('View Pucks in dewar');" onmouseout="return nd();">
									</logic:equal>
								</center>
							</layout:collectionItem>
							
							<layout:collectionItem title="View Samples"	paramId="dewarId" paramProperty="dewarId" sortable="false" href="<%=targetSample%>">
								<center>
									<logic:equal name="dewar" property="type" value="<%=Constants.PARCEL_TOOLBOX_TYPE%>">
										<img src="<%=request.getContextPath()%>/images/blank.gif" border="0">	
									</logic:equal>
									<logic:equal name="dewar" property="type" value="<%=Constants.PARCEL_DEWAR_TYPE%>">
										<img src="<%=request.getContextPath()%>/images/SampleHolderView_24x24_01.png" border="0" onmouseover="return overlib('View Samples in Dewar.');" onmouseout="return nd();">
									</logic:equal>
								</center>
							</layout:collectionItem>
						</logic:equal>
						</logic:present>
						
						<logic:equal name="viewDewarForm" property="role" value="<%=Constants.ROLE_MANAGER%>">
							<layout:collectionItem title="View Containers" property="dewarId" paramId="dewarId" paramProperty="dewarId" href="<%=targetDewar%>" sortable="false">
								<center>
									<logic:equal name="dewar" property="type" value="<%=Constants.PARCEL_TOOLBOX_TYPE%>">
										<img src="<%=request.getContextPath()%>/images/blank.gif" border="0">	
									</logic:equal>
									<logic:equal name="dewar" property="type" value="<%=Constants.PARCEL_DEWAR_TYPE%>">
										<img src="<%=request.getContextPath()%>/images/BasketView_24x24_01.png" border="0" onmouseover="return overlib('View Pucks in dewar');" onmouseout="return nd();">
									</logic:equal>
								</center>
							</layout:collectionItem>
							
							<layout:collectionItem title="View Samples"	paramId="dewarId" paramProperty="dewarId" sortable="false" href="<%=targetSample%>">
								<center>
									<logic:equal name="dewar" property="type" value="<%=Constants.PARCEL_TOOLBOX_TYPE%>">
										<img src="<%=request.getContextPath()%>/images/blank.gif" border="0">	
									</logic:equal>
									<logic:equal name="dewar" property="type" value="<%=Constants.PARCEL_DEWAR_TYPE%>">
										<img src="<%=request.getContextPath()%>/images/SampleHolderView_24x24_01.png" border="0" onmouseover="return overlib('View Samples in Dewar.');" onmouseout="return nd();">
									</logic:equal>
								</center>
							</layout:collectionItem>
						</logic:equal>
						
						<%-- History --%>
						<layout:collectionItem title="History" 	paramId="dewarId" paramProperty="dewarId" sortable="false" width="80" href="<%=targetDewarHistory%>">
							<%-- Visible only if shippingType = 'DewarTracking' --%>
							<logic:equal name="breadCrumbsForm" property="selectedShipping.shippingType" value="<%=Constants.DEWAR_TRACKING_SHIPPING_TYPE%>">
								<center><img src="<%=request.getContextPath()%>/images/Info_16x16_01.png" border="0" onmouseover="return overlib('View Dewar history');" onmouseout="return nd();"></center>
							</logic:equal>
						</layout:collectionItem>
										
						<%-- Actions --%>	
						<logic:present name="breadCrumbsForm" property="selectedShipping" scope="session">
						
						<logic:notEqual name="breadCrumbsForm" property="selectedShipping.shippingStatus" value="<%=Constants.SHIPPING_STATUS_CLOSED%>">
						<%-- Print labels --%>
							<layout:collectionItem title="Comp. labels" width="80">
								<%-- Visible only if shippingType = 'DewarTracking' --%>
						<c:if test="${SITE_ATTRIBUTE eq 'ESRF'}">
								<logic:equal name="breadCrumbsForm" property="selectedShipping.shippingType" value="<%=Constants.DEWAR_TRACKING_SHIPPING_TYPE%>">
									<logic:notEmpty name="dewar" property="sessionVO">
										<logic:equal name="dewar" property="dewarStatus" value="<%=Constants.SHIPPING_STATUS_OPENED%>">
	                    					<html:link href="<%=targetDewarLabels%>" paramName="dewar" paramId="dewarId" paramProperty="dewarId">
	  											<center><img src="<%=request.getContextPath()%>/images/print_warning.png" border="0" onclick="alert('Please print your component labels and stick them on your shipment components prior to sending (see instructions on first page).');this.src='<%=request.getContextPath()%>/images/print.png';" onmouseover="return overlib('You must print component labels');" onmouseout="return nd();"></center>
	  										</html:link>
  										</logic:equal>
  										<logic:notEqual name="dewar" property="dewarStatus" value="<%=Constants.SHIPPING_STATUS_OPENED%>">
  										<logic:notEqual name="dewar" property="dewarStatus" value="<%=Constants.SHIPPING_STATUS_SENT_TO_USER%>">
  										  <logic:empty name="dewar" property="dewarStatus">
	                    					<html:link href="<%=targetDewarLabels%>" paramName="dewar" paramId="dewarId" paramProperty="dewarId">
	  											<center><img src="<%=request.getContextPath()%>/images/print_warning.png" border="0" onclick="alert('Please print your component labels and stick them on your shipment components prior to sending (see instructions on first page).');this.src='<%=request.getContextPath()%>/images/print.png';" onmouseover="return overlib('You must print component labels');" onmouseout="return nd();"></center>
	  										</html:link>
	  									  </logic:empty>
	  									  <logic:notEmpty name="dewar" property="dewarStatus">
	                    					<html:link href="<%=targetDewarLabels%>" paramName="dewar" paramId="dewarId" paramProperty="dewarId">
	  											<center><img src="<%=request.getContextPath()%>/images/print.png" border="0" onclick="alert('Please print your component labels and stick them on your shipment components prior to sending (see instructions on first page).');this.src='<%=request.getContextPath()%>/images/print.png';" onmouseover="return overlib('Print component labels');" onmouseout="return nd();"></center>
	  										</html:link>
	  									  </logic:notEmpty>
  										</logic:notEqual>
  										</logic:notEqual>
									</logic:notEmpty>
									
                  					<logic:empty name="dewar" property="sessionVO">
                    					<html:link href="<%=targetDewarLabels%>" paramName="dewar" paramId="dewarId" paramProperty="dewarId">
  											<center><img src="<%=request.getContextPath()%>/images/print_warning.png" border="0" onclick="alert('Please print your component labels and stick them on your shipment components prior to sending (see instructions on first page).\n\nWARNING: your labels won\'t have Beamline, Experiment Date and Local Contact (you should setup an experiment for this dewar).');this.src='<%=request.getContextPath()%>/images/print.png';" onmouseover="return overlib('Print component labels (incomplete)');" onmouseout="return nd();"></center>
  										</html:link>
									</logic:empty>
								</logic:equal>
</c:if>
<c:if test="${SITE_ATTRIBUTE eq 'EMBL'}">
								<logic:equal name="breadCrumbsForm" property="selectedShipping.shippingType" value="<%=Constants.DEWAR_TRACKING_SHIPPING_TYPE%>">
									<logic:notEmpty name="dewar" property="sessionVO">
										<logic:equal name="dewar" property="dewarStatus" value="<%=Constants.SHIPPING_STATUS_OPENED%>">
	                    					<html:link href="<%=targetDewarLabels%>" paramName="dewar" paramId="dewarId" paramProperty="dewarId">
	  											<center><img src="<%=request.getContextPath()%>/images/print_warning.png" border="0" onclick="alert('Please print your component labels and stick them on your shipment components prior to sending (see instructions on first page).');this.src='<%=request.getContextPath()%>/images/print.png';" onmouseover="return overlib('You must print component labels');" onmouseout="return nd();"></center>
	  										</html:link>
  										</logic:equal>
  										<logic:notEqual name="dewar" property="dewarStatus" value="<%=Constants.SHIPPING_STATUS_OPENED%>">
  										<logic:notEqual name="dewar" property="dewarStatus" value="<%=Constants.SHIPPING_STATUS_SENT_TO_USER%>">
  										  <logic:empty name="dewar" property="dewarStatus">
	                    					<html:link href="<%=targetDewarLabels%>" paramName="dewar" paramId="dewarId" paramProperty="dewarId">
	  											<center><img src="<%=request.getContextPath()%>/images/print_warning.png" border="0" onclick="alert('Please print your component labels and stick them on your shipment components prior to sending (see instructions on first page).');this.src='<%=request.getContextPath()%>/images/print.png';" onmouseover="return overlib('You must print component labels');" onmouseout="return nd();"></center>
	  										</html:link>
	  									  </logic:empty>
	  									  <logic:notEmpty name="dewar" property="dewarStatus">
	                    					<html:link href="<%=targetDewarLabels%>" paramName="dewar" paramId="dewarId" paramProperty="dewarId">
	  											<center><img src="<%=request.getContextPath()%>/images/print.png" border="0" onclick="alert('Please print your component labels and stick them on your shipment components prior to sending (see instructions on first page).');this.src='<%=request.getContextPath()%>/images/print.png';" onmouseover="return overlib('Print component labels');" onmouseout="return nd();"></center>
	  										</html:link>
	  									  </logic:notEmpty>
  										</logic:notEqual>
  										</logic:notEqual>
									</logic:notEmpty>
									
                  					<logic:empty name="dewar" property="sessionVO">
                    					<html:link href="<%=targetDewarLabels%>" paramName="dewar" paramId="dewarId" paramProperty="dewarId">
  											<center><img src="<%=request.getContextPath()%>/images/print_warning.png" border="0" onclick="alert('Please print your component labels and stick them on your shipment components prior to sending (see instructions on first page).\n\nWARNING: your labels won\'t have Beamline, Experiment Date and Local Contact (you should setup an experiment for this dewar).');this.src='<%=request.getContextPath()%>/images/print.png';" onmouseover="return overlib('Print component labels (incomplete)');" onmouseout="return nd();"></center>
  										</html:link>
									</logic:empty>
								</logic:equal>
</c:if>
<c:if test="${SITE_ATTRIBUTE eq 'DLS'}">
								<logic:equal name="breadCrumbsForm" property="selectedShipping.shippingType" value="<%=Constants.DEWAR_TRACKING_SHIPPING_TYPE%>">
									<html:link href="<%=targetDewarLabels%>" paramName="dewar" paramId="dewarId" paramProperty="dewarId">
										<center><img src="<%=request.getContextPath()%>/images/print.png" border="0" onclick="alert('Please print your component labels and stick them on the transport box.')" onmouseover="return overlib('Print component labels');" onmouseout="return nd();"></center>
									</html:link>
								</logic:equal>
</c:if>
<c:if test="${SITE_ATTRIBUTE eq 'MAXIV'}">
								<logic:equal name="breadCrumbsForm" property="selectedShipping.shippingType" value="<%=Constants.DEWAR_TRACKING_SHIPPING_TYPE%>">
									<logic:notEmpty name="dewar" property="sessionVO">
										<logic:equal name="dewar" property="dewarStatus" value="<%=Constants.SHIPPING_STATUS_OPENED%>">
	                    					<html:link href="<%=targetDewarLabels%>" paramName="dewar" paramId="dewarId" paramProperty="dewarId">
	  											<center><img src="<%=request.getContextPath()%>/images/print_warning.png" border="0" onclick="alert('Please print your component labels and stick them on your shipment components prior to sending (see instructions on first page).');this.src='<%=request.getContextPath()%>/images/print.png';" onmouseover="return overlib('You must print component labels');" onmouseout="return nd();"></center>
	  										</html:link>
  										</logic:equal>
  										<logic:notEqual name="dewar" property="dewarStatus" value="<%=Constants.SHIPPING_STATUS_OPENED%>">
  										<logic:notEqual name="dewar" property="dewarStatus" value="<%=Constants.SHIPPING_STATUS_SENT_TO_USER%>">
  										  <logic:empty name="dewar" property="dewarStatus">
	                    					<html:link href="<%=targetDewarLabels%>" paramName="dewar" paramId="dewarId" paramProperty="dewarId">
	  											<center><img src="<%=request.getContextPath()%>/images/print_warning.png" border="0" onclick="alert('Please print your component labels and stick them on your shipment components prior to sending (see instructions on first page).');this.src='<%=request.getContextPath()%>/images/print.png';" onmouseover="return overlib('You must print component labels');" onmouseout="return nd();"></center>
	  										</html:link>
	  									  </logic:empty>
	  									  <logic:notEmpty name="dewar" property="dewarStatus">
	                    					<html:link href="<%=targetDewarLabels%>" paramName="dewar" paramId="dewarId" paramProperty="dewarId">
	  											<center><img src="<%=request.getContextPath()%>/images/print.png" border="0" onclick="alert('Please print your component labels and stick them on your shipment components prior to sending (see instructions on first page).');this.src='<%=request.getContextPath()%>/images/print.png';" onmouseover="return overlib('Print component labels');" onmouseout="return nd();"></center>
	  										</html:link>
	  									  </logic:notEmpty>
  										</logic:notEqual>
  										</logic:notEqual>
									</logic:notEmpty>
									
                  					<logic:empty name="dewar" property="sessionVO">
                    					<html:link href="<%=targetDewarLabels%>" paramName="dewar" paramId="dewarId" paramProperty="dewarId">
  											<center><img src="<%=request.getContextPath()%>/images/print_warning.png" border="0" onclick="alert('Please print your component labels and stick them on your shipment components prior to sending (see instructions on first page).\n\nWARNING: your labels won\'t have Beamline, Experiment Date and Local Contact (you should setup an experiment for this dewar).');this.src='<%=request.getContextPath()%>/images/print.png';" onmouseover="return overlib('Print component labels (incomplete)');" onmouseout="return nd();"></center>
  										</html:link>
									</logic:empty>
								</logic:equal>
</c:if>
							</layout:collectionItem>
						<logic:notEqual name="breadCrumbsForm" property="selectedShipping.shippingStatus" value="<%=Constants.SHIPPING_STATUS_PROCESS%>">
							
							<%-- Edit/delete Actions only if shipment not closed or not processing --%>
							<layout:collectionItem title="Edit Comp. parameters" width="80">
								<%-- Visible only if shippingType = 'DewarTracking' --%>
								<logic:equal name="breadCrumbsForm" property="selectedShipping.shippingType" value="<%=Constants.DEWAR_TRACKING_SHIPPING_TYPE%>">
									<center>
									<html:link href="<%=targetUpdateDewar%>" paramName="dewar" paramId="dewarId" paramProperty="dewarId">
										<img src="<%=request.getContextPath()%>/images/Edit_16x16_01.png" border="0" onmouseover="return overlib('Edit Dewar');" onmouseout="return nd();"/>
									</html:link>
									<%--
									<logic:notEqual name="breadCrumbsForm" property="selectedShipping.shippingStatus" value="<%=Constants.SHIPPING_STATUS_OPENED%>">
										*
									</logic:notEqual>
									--%>
									</center>
								</logic:equal>
							</layout:collectionItem>
							
							
							
							<%-- Others actions (User only) --%>
							<logic:equal name="breadCrumbsForm"  property="userOrIndus" value="true">
								<layout:collectionItem title="Other actions" width="120">
									<center>
										<%-- If dewar status = opened then edit, delete else delete --%>
										<logic:equal name="breadCrumbsForm" property="selectedShipping.shippingStatus" value="<%=Constants.SHIPPING_STATUS_OPENED%>">
											<logic:equal name="dewar" property="type" value="<%=Constants.PARCEL_DEWAR_TYPE%>">
												<html:link href="<%=targetCreateContainer%>" paramName="dewar" paramId="dewarId" paramProperty="dewarId">
													<img src="<%=request.getContextPath()%>/images/BasketAdd_24x24_01.png" border="0" onmouseover="return overlib('Add Container to Dewar');" onmouseout="return nd();">
												</html:link>
											</logic:equal>
											<logic:equal name="dewar" property="type" value="<%=Constants.PARCEL_TOOLBOX_TYPE%>">
												<img src="<%=request.getContextPath()%>/images/blank.gif" border="0" width="26" />
											</logic:equal>
										</logic:equal>
										&nbsp;
										<logic:present name="dewar" property="sessionVO.nbReimbDewars">
										
										  <logic:equal name="dewar" property="isReimbursed" value="true">
										  	<html:link href="<%=targetGetReimbursed%>" paramName="dewar" paramId="dewarId" paramProperty="dewarId" >
												<img src="<%=request.getContextPath()%>/images/euro.gif" border="0" onmouseover="return overlib('Set/Unset Dewar Reimbursement');" onmouseout="return nd();">
											</html:link>
										</logic:equal>
										
										 <logic:notEqual name="dewar" property="isReimbursed" value="true">											
										  <logic:equal name="viewDewarForm" property="remainingReimbursed" value="true">
											<html:link href="<%=targetGetReimbursed%>" paramName="dewar" paramId="dewarId" paramProperty="dewarId" >
												<img src="<%=request.getContextPath()%>/images/euro.gif" border="0" onmouseover="return overlib('Set/Unset Dewar Reimbursement');" onmouseout="return nd();">
											</html:link>
											</logic:equal>
										</logic:notEqual>
										
										
										</logic:present>
										&nbsp;
										<html:link href="<%=targetDeleteDewar%>" paramName="dewar" paramId="dewarId" paramProperty="dewarId" onclick="return window.confirm('Do you really want to delete this Dewar?');">
											<img src="<%=request.getContextPath()%>/images/cancel.png" border="0" onmouseover="return overlib('Delete the Dewar');" onmouseout="return nd();">
										</html:link>
									</center>
								</layout:collectionItem>
							</logic:equal>
	
						</logic:notEqual>
						</logic:notEqual>
						</logic:present>						


					</layout:collectionStyle>
				</layout:collection>
				
				
			<%-- Submit the current shipping (only if shippingType is equal to DewarTracking) --%>
			<logic:equal name="breadCrumbsForm" property="selectedShipping.shippingType" value="<%=Constants.DEWAR_TRACKING_SHIPPING_TYPE%>">
				<logic:present name="breadCrumbsForm" property="selectedShipping" scope="session">
				<logic:notPresent name="breadCrumbsForm" property="selectedDewar" scope="session">
					<bean:define name="breadCrumbsForm" property="selectedShipping.shippingStatus" id="selectedShippingStatus" 	type="java.lang.String"/>
					<logic:equal name="breadCrumbsForm" property="selectedShipping.shippingStatus" value="<%=Constants.SHIPPING_STATUS_OPENED%>">
					<c:if test="${SITE_ATTRIBUTE eq 'ESRF' or SITE_ATTRIBUTE eq 'EMBL' or SITE_ATTRIBUTE eq 'MAXIV'}">
						<layout:cell>
							<font color="red" class="small"><b>1)</b> Please print (printer icon) your component labels and stick them on your shipment components prior to sending (see instructions on first page).</font>
						</layout:cell>
					</c:if>
						<layout:cell>
						<c:if test="${SITE_ATTRIBUTE eq 'ESRF'}">
							<font color="red" class="small"><b>2)</b> When sending your components, don't forget to click on "Send Shipment to ESRF".</font>
						</c:if>
						<c:if test="${SITE_ATTRIBUTE eq 'EMBL'}">
							<font color="red" class="small"><b>2)</b> When sending your components, don't forget to click on "Send Shipment to EMBL".</font>
						</c:if>
						<c:if test="${SITE_ATTRIBUTE eq 'MAXIV'}">
							<font color="red" class="small"><b>2)</b> When sending your components, don't forget to click on "Send Shipment to MAXIV".</font>
						</c:if>						
						<c:if test="${SITE_ATTRIBUTE eq 'DLS'}">
						<font color="red" class="small"><%-- <b>2)</b>--%> When sending your components, don't forget to click on "Send Shipment to DLS".</font>
						</c:if>
						</layout:cell>
						<layout:cell>
							<html:link href="<%=targetSubmitShipping%>" paramName="breadCrumbsForm" paramId="shippingId" paramProperty="selectedShipping.shippingId">
								<img src="<%=request.getContextPath()%>/images/SendShipment_32x32_01.png" border="0"/><font class="small">Send Shipment to <%=Constants.SITE_NAME%></font>
							</html:link>
						</layout:cell>
					</logic:equal>
				</logic:notPresent>
				</logic:present>
			</logic:equal>
			
				
		</layout:panel>
	  </layout:row>
	</logic:notEmpty>
		
	<%----------------------------------------------- Containers -----------------------------------------------------------%>		
	<logic:present name="breadCrumbsForm" property="selectedShipping" scope="session">
		<logic:present name="breadCrumbsForm" property="selectedDewar" scope="session">
			<logic:empty name="viewDewarForm" property="listInfoSlave">
		      <logic:greaterThan name="viewDewarForm" property="dewarId" value="1">
		        <layout:row>
		           <h4>There&nbsp;are&nbsp;no&nbsp;containers&nbsp;in&nbsp;this&nbsp;dewar,&nbsp;you&nbsp;may&nbsp;add&nbsp;a&nbsp;container&nbsp;by&nbsp;clicking&nbsp;on&nbsp;the&nbsp;above&nbsp;link</h4>
		        </layout:row>
		       </logic:greaterThan>
		    </logic:empty>
	    </logic:present>
	</logic:present>
	
	<%-- Containers --%>
	<logic:present name="viewDewarForm" scope="request">
	<logic:notEmpty name="viewDewarForm" 	property="listInfoSlave">
		<layout:row>
				<layout:panel key="<%=containerPanelTitle%>" align="center" styleClass="PANEL" width="100%">
					<jsp:include page="viewContainerList.jsp" flush="true" />
				</layout:panel>
		</layout:row>
	</logic:notEmpty>
	</logic:present>
	
</layout:grid>																	 
			
