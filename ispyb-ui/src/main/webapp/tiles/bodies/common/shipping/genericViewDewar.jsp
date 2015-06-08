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
	String targetDewar 		 	= request.getContextPath() + "/user/viewDewarAction.do?reqCode=displaySlave";
	String targetDewarHistory	= request.getContextPath() + "/reader/viewDewarHistoryAction.do?reqCode=display";
	
	String targetShipping	 	= request.getContextPath() + "/reader/viewDewarAction.do?reqCode=readOnly";
	
	String targetLabContact 		= request.getContextPath() + "/menuSelected.do?leftMenuId=70&topMenuId=65&targetUrl=/reader/createLabContactAction.do?reqCode=readOnly";
	String targetLabContactUpdate 	= request.getContextPath() + "/menuSelected.do?leftMenuId=70&topMenuId=65&targetUrl=/user/createLabContactAction.do?reqCode=updateDisplay";

	String targetContainer 			= request.getContextPath() + "/user/viewDewarAction.do?reqCode=displaySlave";
	String targetDewarLabels		= request.getContextPath() + "/reader/viewDewarAction.do?reqCode=generateLabels";
	String targetSample 			= request.getContextPath() + "/menuSelected.do?topMenuId=10&targetUrl=/user/viewSample.do?reqCode=displayForDewar";

%>

<layout:skin includeScript="true"/>
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

	
<%-------------------------------------------------------- Dewars ------------------------------------------------------%>
<%-- To not show a empty table when no dewar exists --%>
<logic:empty  name="genericViewDewarForm" property="listDewars">
          <h4>No&nbsp;Dewar&nbsp;have&nbsp;been&nbsp;found</h4>
</logic:empty>

<logic:notEqual  name="genericViewDewarForm" property="dewarMsg" value="">
          <h4><bean:write name="genericViewDewarForm" property="dewarMsg"/></h4>
</logic:notEqual>

<logic:notEmpty name="genericViewDewarForm" property="listDewars">
  <layout:grid cols="1"  borderSpacing="10">

	<layout:panel key="Components" align="left" styleClass="PANEL" width="85%">
			
		<layout:pager maxPageItems="20" styleClass="LIST">
		
		<%-- COLLECTION --%>
		<layout:collection 	name="genericViewDewarForm" 
							property="listDewars" 
							styleClass="LIST" 
							id="dewar" 
							styleClass2="LIST2"
							indexId="index">	
				
				<%-- Dewar name --%>
				<logic:equal name="genericViewDewarForm" property="userOrIndus" value="true">
				<layout:collectionItem title="Comp. name" 	property="code"
															sortable="true" href="<%=targetDewar%>" 
															paramId="dewarId" paramProperty="dewarId"/>
				</logic:equal>
				<logic:notEqual name="genericViewDewarForm" property="userOrIndus" value="true">
				
				<layout:collectionItem title="Comp. name" 	property="code"
															sortable="true" href="<%=targetShipping%>" 
															paramId="shippingId,proposalId" paramProperty="shippingVO.shippingId,shippingVO.proposalVO.proposalId"/>
				</logic:notEqual>
				
				<%-- Type (for the User view) --%>
				<logic:equal name="genericViewDewarForm" property="userOrIndus" value="true">
					<layout:collectionItem title="Type" property="type" sortable="false">
						<logic:equal name="dewar" property="shippingVO.shippingType" value="<%=Constants.DEWAR_TRACKING_SHIPPING_TYPE%>">
							<center>
								<logic:equal name="dewar" property="type" value="<%=Constants.PARCEL_TOOLBOX_TYPE%>">
									<img title="Toolbox" alt="Toolbox" src="<%=request.getContextPath()%>/images/toolbox.png" border="0"/>
								</logic:equal>
								<logic:equal name="dewar" property="type" value="<%=Constants.PARCEL_DEWAR_TYPE%>">
									<img title="Dewar" alt="Dewar" src="<%=request.getContextPath()%>/images/Dewar_24x24_01.png" border="0"/>
								</logic:equal>
							</center>
						</logic:equal>
					</layout:collectionItem>
				</logic:equal>
				
				<%-- Shipping name --%>
				<layout:collectionItem title="Ship. name" property="shippingVO.shippingName" sortable="true"/>
				
				<%-- Comments--%>
				<logic:equal name="genericViewDewarForm" property="userOrIndus" value="true">
					<layout:collectionItem title="Comments" 	property="comments" sortable="false"/>
				</logic:equal>
				
				<%-- Barcode --%>
				<layout:collectionItem title="Barcode" property="barCode" sortable="false"/>
				
				<%-- Proposal number --%>
				<logic:notEqual name="genericViewDewarForm" property="userOrIndus" value="true">
					<layout:collectionItem title="Proposal number" property="shippingVO.proposalVO.number" sortable="false"/>
				</logic:notEqual>
				
				<%-- Lab-contact for sending --%>
				<logic:equal name="genericViewDewarForm" property="userOrIndus" value="true">
				<layout:collectionItem title="Sending details" 	property="shippingVO.sendingLabContactVO.personVO.familyName" 	
															sortable="true" href="<%=targetLabContactUpdate%>"
															paramId="labContactId" paramProperty="shippingVO.sendingLabContactVO.labContactId"/>
				</logic:equal>
				<logic:notEqual name="genericViewDewarForm" property="userOrIndus" value="true">
				<layout:collectionItem title="Sending details" 	property="shippingVO.sendingLabContactVO.personVO.familyName" 	
															sortable="true" href="<%=targetLabContact%>"
															paramId="labContactId" paramProperty="shippingVO.sendingLabContactVO.labContactId"/>
				</logic:notEqual>
				
				<%-- Exp. date --%>
				<layout:collectionItem title="Exp. date" property="shippingVO.firstExpDate" sortable="true" type="date"/>
				
				<%-- Beamline --%>
				<layout:collectionItem title="Beamline" property="shippingVO.firstExpBl" sortable="true"/>			
				<%-- Nb samples --%>	
				
				<layout:collectionItem title="#<BR>samples" sortable="false">
					<logic:equal name="dewar" property="type" value="<%=Constants.PARCEL_DEWAR_TYPE%>">
						<bean:write name="genericViewDewarForm"	property="listNbSamplesPerDewar[${index}]"/>
					</logic:equal>
					
					
				</layout:collectionItem>	
					
				<%-- Local contact --%>
				<logic:equal name="genericViewDewarForm" property="role" value="<%=Constants.ROLE_BLOM%>">
					<layout:collectionItem title="Local contact" property="shippingVO.beamlineOperator" sortable="false"/>
				</logic:equal>
				<logic:equal name="genericViewDewarForm" property="role" value="<%=Constants.ROLE_MANAGER%>">
					<layout:collectionItem title="Local contact" property="shippingVO.beamlineOperator" sortable="false"/>
				</logic:equal>
				
				<%-- Shipping date --%>
				<logic:notEqual name="genericViewDewarForm" property="userOrIndus" value="true">
				<layout:collectionItem title="Creation date" property="shippingVO.creationDate" sortable="true" type="date"/>
				</logic:notEqual>
				
				<%-- Tracking numbers TO Synchrotron --%>
				<layout:collectionItem title="Courier/ tracking # TO ${SITE_ATTRIBUTE}" sortable="false">
					<bean:write name="dewar"	property="shippingVO.deliveryAgentAgentName"/><br/>
					<bean:write name="dewar"	property="trackingNumberToSynchrotron"/><br/>
					<logic:present name="dewar" property="shippingVO.deliveryAgentAgentName">
					<logic:present name="dewar" property="trackingNumberToSynchrotron">
						<!--%--Fedex--%-->
						<logic:equal name="dewar" property="shippingVO.deliveryAgentAgentName" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_FEDEX%>">
							<html:link href="<%=Constants.TRACKING_URL_FEDEX%>" paramName="dewar" paramId="tracknumbers" paramProperty="trackingNumberToSynchrotron" target="_new">
								<img src="<%=request.getContextPath()%>/images/fedex_express_logo.gif" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
							</html:link>
						</logic:equal>
						<!--%--TNT--%-->
						<logic:equal name="dewar" property="shippingVO.deliveryAgentAgentName" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_TNT%>">
							<html:link href="<%=Constants.TRACKING_URL_TNT%>" paramName="dewar" paramId="cons" paramProperty="trackingNumberToSynchrotron" target="_new">
								<img src="<%=request.getContextPath()%>/images/tnt_logo.gif" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
							</html:link>
						</logic:equal>
						<!--%--DHL--%-->
						<logic:equal name="dewar" property="shippingVO.deliveryAgentAgentName" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_DHL%>">
							<html:link href="<%=Constants.TRACKING_URL_DHL%>" paramName="dewar" paramId="AWB" paramProperty="trackingNumberToSynchrotron" target="_new">
								<img src="<%=request.getContextPath()%>/images/dhl_logo.gif" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
							</html:link>
						</logic:equal>
						<!--%--UPS--%-->
						<logic:equal name="dewar" property="shippingVO.deliveryAgentAgentName" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_UPS%>">
							<html:link href="<%=Constants.TRACKING_URL_UPS%>" paramName="dewar" paramId="trackNums" paramProperty="trackingNumberToSynchrotron" target="_new">
								<img src="<%=request.getContextPath()%>/images/ups_logo.gif" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
							</html:link>
						</logic:equal>
						<!--%--WorldCourier--%-->
						<logic:equal name="dewar" property="shippingVO.deliveryAgentAgentName" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_WORLDCOURIER%>">
							<html:link href="<%=Constants.TRACKING_URL_WORLD_COURIER%>" paramName="dewar" paramId="hwb" paramProperty="trackingNumberToSynchrotron" target="_new">
								<img src="<%=request.getContextPath()%>/images/worldCourier_logo.jpg" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
							</html:link>
						</logic:equal>
						<!--%--Chronopost--%-->
						<logic:equal name="dewar" property="shippingVO.deliveryAgentAgentName" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_CHRONOPOST%>">
							<html:link href="<%=Constants.TRACKING_URL_CHRONOPOST%>" paramName="dewar" paramId="hwb" paramProperty="trackingNumberToSynchrotron" target="_new">
								<img src="<%=request.getContextPath()%>/images/chronopost_logo.png" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
							</html:link>
						</logic:equal>
					</logic:present>
					</logic:present>
				</layout:collectionItem>
				
				<%-- Status and Location --%>
				<layout:collectionItem title="Component status"  property="dewarStatus"   sortable="true"/>
				<layout:collectionItem title="Location" property="storageLocation" sortable="true"/>
				
				<%-- View containers (User only) --%>
				<logic:equal name="genericViewDewarForm" property="userOrIndus" value="true">
					<layout:collectionItem title="View Containers" sortable="false">
						<center>
							<logic:equal name="dewar" property="type" value="<%=Constants.PARCEL_DEWAR_TYPE%>">
								<layout:link href="<%=targetContainer%>" paramName="dewar" paramId="dewarId" paramProperty="dewarId">
								<img src="<%=request.getContextPath()%>/images/BasketView_24x24_01.png" border=0 onmouseover="return overlib('View Pucks in dewar');" onmouseout="return nd();">
								</layout:link>
							</logic:equal>
						</center>
					</layout:collectionItem>
				</logic:equal>
				
				<%-- View samples (User only) --%>
				<logic:equal name="genericViewDewarForm" property="userOrIndus" value="true">
					<layout:collectionItem title="View Samples" sortable="false">
						<center>
							<logic:equal name="dewar" property="type" value="<%=Constants.PARCEL_DEWAR_TYPE%>">
								<layout:link href="<%=targetSample%>" paramName="dewar" paramId="dewarId" paramProperty="dewarId">
								<img src="<%=request.getContextPath()%>/images/SampleHolderView_24x24_01.png" border="0" onmouseover="return overlib('View Samples in Dewar.');" onmouseout="return nd();">
								</layout:link>
							</logic:equal>
						</center>
					</layout:collectionItem>
				</logic:equal>
				
				<%-- Lab-contact for return --%>
				<logic:equal name="genericViewDewarForm" property="userOrIndus" value="true">
				<layout:collectionItem title="Return details" 	property="shippingVO.returnLabContactVO.personVO.familyName" 	
															sortable="true" href="<%=targetLabContactUpdate%>"
															paramId="labContactId" paramProperty="shippingVO.returnLabContactVO.labContactId"
															/>
				</logic:equal>
				<logic:notEqual name="genericViewDewarForm" property="userOrIndus" value="true">
				<layout:collectionItem title="Return details" 	property="shippingVO.returnLabContactVO.personVO.familyName" 	
															sortable="true" href="<%=targetLabContact%>"
															paramId="labContactId" paramProperty="shippingVO.returnLabContactVO.labContactId"/>
				</logic:notEqual>
				
				<%-- Date of shipping to user --%>
				<logic:notEqual name="genericViewDewarForm" property="userOrIndus" value="true">
					<layout:collectionItem title="Date of shipping to user" property="shippingVO.dateOfShippingToUser" type="date" sortable="true"/>
				</logic:notEqual>
				
				<%-- Tracking numbers FROM Synchrotron --%>
				<layout:collectionItem title="Courier/ tracking # FROM ${SITE_ATTRIBUTE}" sortable="false">
					<bean:write name="dewar" property="shippingVO.returnCourier"/><br>
					<bean:write name="dewar" property="trackingNumberFromSynchrotron"/><br>	
					<logic:present name="dewar" property="shippingVO.returnCourier">
					<logic:present name="dewar" property="trackingNumberFromSynchrotron">
						<!--%--Fedex--%-->
						<logic:equal name="dewar" property="shippingVO.returnCourier" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_FEDEX%>">
							<html:link href="<%=Constants.TRACKING_URL_FEDEX%>" paramName="dewar" paramId="tracknumbers" paramProperty="trackingNumberFromSynchrotron" target="_new">
								<img src="<%=request.getContextPath()%>/images/fedex_express_logo.gif" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
							</html:link>
						</logic:equal>
						<!--%--TNT--%-->
						<logic:equal name="dewar" property="shippingVO.returnCourier" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_TNT%>">
							<html:link href="<%=Constants.TRACKING_URL_TNT%>" paramName="dewar" paramId="cons" paramProperty="trackingNumberFromSynchrotron" target="_new">
								<img src="<%=request.getContextPath()%>/images/tnt_logo.gif" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
							</html:link>
						</logic:equal>
						<!--%--DHL--%-->
						<logic:equal name="dewar" property="shippingVO.returnCourier" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_DHL%>">
							<html:link href="<%=Constants.TRACKING_URL_DHL%>" paramName="dewar" paramId="AWB" paramProperty="trackingNumberFromSynchrotron" target="_new">
								<img src="<%=request.getContextPath()%>/images/dhl_logo.gif" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
							</html:link>
						</logic:equal>
						<!--%--UPS--%-->
						<logic:equal name="dewar" property="shippingVO.returnCourier" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_UPS%>">
							<html:link href="<%=Constants.TRACKING_URL_UPS%>" paramName="dewar" paramId="trackNums" paramProperty="trackingNumberFromSynchrotron" target="_new">
								<img src="<%=request.getContextPath()%>/images/ups_logo.gif" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
							</html:link>
						</logic:equal>
						<!--%--WorldCourier--%-->
						<logic:equal name="dewar" property="shippingVO.returnCourier" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_WORLDCOURIER%>">
							<html:link href="<%=Constants.TRACKING_URL_WORLD_COURIER%>" paramName="dewar" paramId="hwb" paramProperty="trackingNumberFromSynchrotron" target="_new">
								<img src="<%=request.getContextPath()%>/images/worldCourier_logo.jpg" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
							</html:link>
						</logic:equal>
						<!--%--Chronopost--%-->
						<logic:equal name="dewar" property="shippingVO.deliveryAgentAgentName" value="<%=Constants.SHIPPING_DELIVERY_AGENT_NAME_CHRONOPOST%>">
							<html:link href="<%=Constants.TRACKING_URL_CHRONOPOST%>" paramName="dewar" paramId="hwb" paramProperty="trackingNumberToSynchrotron" target="_new">
								<img src="<%=request.getContextPath()%>/images/chronopost_logo.png" border=0 onmouseover="return overlib('Track the Shipping');" onmouseout="return nd();">
							</html:link>
						</logic:equal>
					</logic:present>
					</logic:present>
				</layout:collectionItem>
				
				<%-- History --%>
				<layout:collectionItem title="History" 	paramId="dewarId" paramProperty="dewarId" sortable="false" width="80" href="<%=targetDewarHistory%>">
					<logic:empty name="dewar" property="dewarTransportHistoryVOs">
							<center><img src="<%=request.getContextPath()%>/images/Info_16x16_01.png" border=0 onmouseover="return overlib('View Dewar history');" onmouseout="return nd();"></center>
						</logic:empty>
						<logic:notEmpty name="dewar" property="dewarTransportHistoryVOs" >
							<center><img src="<%=request.getContextPath()%>/images/Info_16x16_02.png" border=0 onmouseover="return overlib('View Dewar history (Dewar Tracking events)');" onmouseout="return nd();"></center>
						</logic:notEmpty>
				</layout:collectionItem>
				
				<%-- Print labels --%>
				<layout:collectionItem title="Comp. labels" width="80">
					<%-- Visible only if shippingType = 'DewarTracking' --%>
					<logic:equal name="dewar" property="shippingVO.shippingType" value="<%=Constants.DEWAR_TRACKING_SHIPPING_TYPE%>">
						
						<logic:notEmpty name="dewar" property="shippingVO.firstExpDate">
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
						
               			<logic:empty name="dewar" property="shippingVO.firstExpDate">
                 			<html:link href="<%=targetDewarLabels%>" paramName="dewar" paramId="dewarId" paramProperty="dewarId">
								<center><img src="<%=request.getContextPath()%>/images/print_warning.png" border="0" onclick="alert('Please print your component labels and stick them on your shipment components prior to sending (see instructions on first page).\n\nWARNING: your labels won\'t have Beamline, Experiment Date and Local Contact (you should setup an experiment for this dewar).');this.src='<%=request.getContextPath()%>/images/print.png';" onmouseover="return overlib('Print component labels (incomplete)');" onmouseout="return nd();"></center>
							</html:link>
						</logic:empty>
					</logic:equal>
					
				</layout:collectionItem>
				
				<%-- Type (for the Store view) --%>
				<logic:equal name="genericViewDewarForm" property="role" value="<%=Constants.ROLE_STORE%>">
					<layout:collectionItem title="Type" property="type" sortable="false">
						<logic:equal name="dewar" property="shippingVO.shippingType" value="<%=Constants.DEWAR_TRACKING_SHIPPING_TYPE%>">
							<center>
								<logic:equal name="dewar" property="type" value="<%=Constants.PARCEL_TOOLBOX_TYPE%>">
									<img title="Toolbox" alt="Toolbox" src="<%=request.getContextPath()%>/images/toolbox.png" border="0"/>
								</logic:equal>
								<logic:equal name="dewar" property="type" value="<%=Constants.PARCEL_DEWAR_TYPE%>">
									<img title="Dewar" alt="Dewar"  src="<%=request.getContextPath()%>/images/Dewar_24x24_01.png" border="0"/>
								</logic:equal>
							</center>
						</logic:equal>
					</layout:collectionItem>
				</logic:equal>
					
			
		</layout:collection>
		</layout:pager>	
		
		
	</layout:panel>
  </layout:grid>
  
  <bean:define id="results" name="genericViewDewarForm" property="listDewars" type="java.util.ArrayList"/>
  <h4><%=Integer.toString(results.size())%>&nbsp;results&nbsp;found </h4>
</logic:notEmpty>
