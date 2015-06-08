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
<%@ page isELIgnored="false" %>
<%@page import="ispyb.common.util.Constants"%>


<%	
	String targetDewar 		 = request.getContextPath() + "/menuSelected.do?leftMenuId=71&topMenuId=5&targetUrl=/reader/viewDewarAction.do?reqCode=readOnly";
	String targetDewarUser = request.getContextPath() + "/user/viewDewarAction.do?reqCode=display";
	
	String targetLabContact = request.getContextPath() + "/menuSelected.do?leftMenuId=70&topMenuId=65&targetUrl=/reader/createLabContactAction.do?reqCode=readOnly";
	String targetLabContactUpdate = request.getContextPath() + "/menuSelected.do?leftMenuId=70&topMenuId=65&targetUrl=/user/createLabContactAction.do?reqCode=updateDisplay";
	
	String targetSubmitShipping = request.getContextPath() + "/user/submitShippingAction.do?reqCode=display";
	String targetShippingHistory		= request.getContextPath() + "/reader/viewShippingHistoryAction.do?reqCode=display";
	
	String targetSample 		= request.getContextPath() + "/menuSelected.do?topMenuId=10&targetUrl=/user/viewSample.do?reqCode=displayForShipping";
	String targetCreateDewar 	= request.getContextPath() + "/user/createDewarAction.do?reqCode=display";
	String targetDeleteShipping 	= request.getContextPath() + "/user/viewShippingAction.do?reqCode=deleteShipping";
	String targetUpdateShipping 	= request.getContextPath() + "/user/createShippingAction.do?reqCode=updateDisplay";
	String targetExportShipping 	= request.getContextPath() + "/user/submitPocketSampleInformationAction.do?reqCode=exportShipping";
	
	String targetUploadShipping 	= request.getContextPath() + "/user/submitPocketSampleInformationAction.do?reqCode=displayAfterDewarTracking";
	
	String targetOpenShipping =  request.getContextPath() + "/user/fillShipmentAction.do?reqCode=display";

	String FedexTrackingURL		= "http://www.fedex.com/Tracking?ascend_header=1&clienttype=dotcomreg&cntry_code=gb&language=english";
	String TntTrackingURL		= "http://www.tnt.com/webtracker/tracking.do?requestType=GEN&searchType=CON&navigation=1&respLang=en&respCountry=GB&genericSiteIdent=";
%>

<jsp:useBean id="genericViewShippingAction" scope="request" class="ispyb.client.common.shipping.GenericViewShippingAction"/>
<bean:define name="genericViewShippingForm" property="listShippings" id="myList" type="java.util.List"	toScope="request"/>

<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<%-- Errors --%>
<jsp:include page="/tiles/common/messages/error.jsp" flush="true" />
<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />

<%-- To not show a empty table when no shipping exists --%>	
<logic:empty name="myList">
           <h4>No&nbsp;shipment&nbsp;have&nbsp;been&nbsp;found</h4>
</logic:empty>

<logic:notEqual  name="genericViewShippingForm" property="shipmentMsg" value="">
          <h4><bean:write name="genericViewShippingForm" property="shipmentMsg"/></h4>
</logic:notEqual>


<logic:notEmpty name="myList">	
	<layout:grid cols="1"  borderSpacing="10">
	
		<%-- Page --%>
		<layout:panel key="Shipments" align="left" styleClass="PANEL">
		
		<%-- COLLECTION --%>
		<layout:pager maxPageItems="20" styleClass="LIST">
		<layout:collection 	name="genericViewShippingForm" 
							property="listShippings"
							styleClass="LIST" styleClass2="LIST2"
							id="currInfo"
							indexId="index">
							
			<%-- Actions (Users only) --%>
			<logic:equal name="genericViewShippingForm" property="userOrIndus" value="true">										
				<layout:collectionItem title="Edit" >
				<div style="white-space: nowrap; text-align: center">
						  
  				<%-- Shipping with not closed and not processing status : edit, delete --%> 
  				<logic:notEqual name="currInfo" property="shippingStatus" value="<%=Constants.SHIPPING_STATUS_PROCESS%>">
  				    <logic:notEqual name="currInfo" property="shippingStatus" value="<%=Constants.SHIPPING_STATUS_CLOSED%>">
  					
  						<%-- Edit (only if type==DewarTracking) --%>
  						<logic:equal name="currInfo" property="shippingType" value="<%=Constants.DEWAR_TRACKING_SHIPPING_TYPE%>">
  							<html:link href="<%=targetUpdateShipping%>" paramName="currInfo" paramId="shippingId" paramProperty="shippingId">
  								<img src="<%=request.getContextPath()%>/images//Edit_16x16_01.png" border="0" onmouseover="return overlib('Edit Shipping');" onmouseout="return nd();"/>
  							</html:link>
  						</logic:equal>
  						
  					</logic:notEqual>
  				</logic:notEqual>
  				
				</div>
				</layout:collectionItem>
			</logic:equal>
	
			<%-- Shipping Name --%>
			<logic:notEqual name="genericViewShippingForm" property="userOrIndus" value="true">
				<layout:collectionItem title="Shipment Name" 	property="shippingName" sortable="true" href="<%=targetDewar%>" paramId="shippingId,proposalVO.proposalId" paramProperty="shippingId,proposalVO.proposalId"/>
			</logic:notEqual>
			<logic:equal name="genericViewShippingForm" property="userOrIndus" value="true">
				<layout:collectionItem title="Shipment Name" 	property="shippingName" sortable="true" href="<%=targetDewarUser%>" paramId="shippingId" paramProperty="shippingId"/>
			</logic:equal>
			
			<%-- Dewar Tracking indicator 
			<layout:collectionItem title="" 	paramId="shippingId" paramProperty="shippingId" sortable="false"  >
				<logic:equal name="currInfo" property="shippingType" value="<%=Constants.DEWAR_TRACKING_SHIPPING_TYPE%>">
					<center><layout:link href="" title="Dewar Tracking features enabled" styleClass="NO_UNDERLINE">T</layout:link></center>
				</logic:equal>
			</layout:collectionItem>
			--%>
			
			<%-- Shipping Date --%>
			<layout:collectionItem title="Creation date" property="creationDate" sortable="true" type="date" />
			
			<%-- Expected arrival date --%>
			<c:choose>
			<c:when test="${SITE_ATTRIBUTE eq 'ESRF'}">
				<layout:collectionItem title="Expected at ESRF"	property="deliveryAgentDeliveryDate" sortable="true" type="date"/>
			</c:when>
			<c:when test="${SITE_ATTRIBUTE eq 'EMBL'}">
				<layout:collectionItem title="Expected at EMBL"	property="deliveryAgentDeliveryDate" sortable="true" type="date"/>
			</c:when>
			<c:when test="${SITE_ATTRIBUTE eq 'DLS'}">
				<layout:collectionItem title="Expected at DLS"	property="deliveryAgentDeliveryDate" sortable="true" type="date"/>
			</c:when>
			<c:when test="${SITE_ATTRIBUTE eq 'MAXIV'}">
				<layout:collectionItem title="Expected at MAXIV" property="deliveryAgentDeliveryDate" sortable="true" type="date"/>
			</c:when>
			<c:otherwise>
				<layout:collectionItem title="Expected at site"	property="deliveryAgentDeliveryDate" sortable="true" type="date"/>
			</c:otherwise>
			</c:choose>
			<%-- Proposal number --%>
			<logic:notEqual name="genericViewShippingForm" property="userOrIndus" value="true">
				<layout:collectionItem title="Proposal" sortable="false">
					<bean:write  name="currInfo" property="proposalVO.code"></bean:write>
					<bean:write name="currInfo"  property="proposalVO.number"></bean:write>
				</layout:collectionItem>
				
			</logic:notEqual>
			
			<c:if test="${SITE_ATTRIBUTE eq 'ESRF'}">
				<%-- Main proposer --%>
				<logic:notEqual name="genericViewShippingForm" property="userOrIndus" value="true">
					<logic:notEqual name="genericViewShippingForm" property="role" value="<%=Constants.ROLE_STORE%>">
						<layout:collectionItem title="Main proposer" property="proposalVO.personVO.givenName" sortable="false"/>
					</logic:notEqual>
				</logic:notEqual>
			</c:if>

			<c:if test="${SITE_ATTRIBUTE eq 'EMBL'}">
				<%-- Main proposer --%>
				<logic:notEqual name="genericViewShippingForm" property="userOrIndus" value="true">
					<logic:notEqual name="genericViewShippingForm" property="role" value="<%=Constants.ROLE_STORE%>">
						<layout:collectionItem title="Main proposer" property="proposalVO.personVO.givenName" sortable="false"/>
					</logic:notEqual>
				</logic:notEqual>
			</c:if>
			<c:if test="${SITE_ATTRIBUTE eq 'MAXIV'}">
				<%-- Main proposer --%>
				<logic:notEqual name="genericViewShippingForm" property="userOrIndus" value="true">
					<logic:notEqual name="genericViewShippingForm" property="role" value="<%=Constants.ROLE_STORE%>">
						<layout:collectionItem title="Main proposer" property="proposalVO.personVO.givenName" sortable="false"/>
					</logic:notEqual>
				</logic:notEqual>
			</c:if>
			
			<%-- Lab-contact for sending --%>
			<%--<logic:equal name="genericViewShippingForm" property="userOrIndus" value="true">
				 <layout:collectionItem title="Sending details" 	property="lcSendingCardName" sortable="true" href="<%=targetLabContactUpdate%>" paramId="labContactId" paramProperty="lcSendingId"/> 
			</logic:equal> 
			<logic:notEqual name="genericViewShippingForm" property="userOrIndus" value="true">
				<layout:collectionItem title="Sending details" 	property="lcSendingCardName" sortable="true" href="<%=targetLabContact%>" paramId="labContactId" paramProperty="lcSendingId"/>
			</logic:notEqual>
			--%>
			<logic:equal name="genericViewShippingForm" property="userOrIndus" value="true">
				<layout:collectionItem title="Sending details" 	property="sendingLabContactVO.cardName" sortable="true" href="<%=targetLabContactUpdate%>" paramId="labContactId" paramProperty="sendingLabContactVO.labContactId"/> 
			</logic:equal>
			<logic:notEqual name="genericViewShippingForm" property="userOrIndus" value="true">
				<layout:collectionItem title="Sending details" 	property="sendingLabContactVO.cardName" sortable="true" href="<%=targetLabContact%>" paramId="labContactId" paramProperty="sendingLabContactVO.labContactId"/>
			</logic:notEqual>
			
			
			
			<%-- Lab-contact for return --%>
			<%--<logic:equal name="genericViewShippingForm"   property="userOrIndus" value="true">
				<layout:collectionItem title="Return details" property="lcReturnCardName" sortable="true" href="<%=targetLabContactUpdate%>" paramId="labContactId" paramProperty="lcReturnId"/>
			</logic:equal>
			<logic:notEqual name="genericViewShippingForm" property="userOrIndus" value="true">
				<layout:collectionItem title="Return details"  property="lcReturnCardName" sortable="true" href="<%=targetLabContact%>" paramId="labContactId" paramProperty="lcReturnId"/>
			</logic:notEqual>
			--%>
			
			<logic:equal name="genericViewShippingForm"   property="userOrIndus" value="true">
				<layout:collectionItem title="Return details" property="returnLabContactVO.cardName" sortable="true" href="<%=targetLabContactUpdate%>" paramId="labContactId" paramProperty="returnLabContactVO.labContactId"/>
			</logic:equal>
			<logic:notEqual name="genericViewShippingForm" property="userOrIndus" value="true">
				<layout:collectionItem title="Return details"  property="returnLabContactVO.cardName" sortable="true" href="<%=targetLabContact%>" paramId="labContactId" paramProperty="returnLabContactVO.labContactId"/>
			</logic:notEqual>
			
			
			
			
			<%-- Nb parcels/samples 
			<layout:collectionItem title="#&nbsp;components<BR>(&nbsp;#&nbsp;samples&nbsp;)" sortable="false">
				<bean:write name="currInfo"	property="dewarVOs.size"/>&nbsp;(&nbsp;<bean:write name="currInfo" />&nbsp;)
			</layout:collectionItem> --%>
			<layout:collectionItem title="#&nbsp;components<BR>(&nbsp;#&nbsp;samples&nbsp;)" sortable="false">
				<bean:define id="dewarVOs" name="currInfo" property="dewarVOs" type="java.util.Set"/>
					<%=Integer.toString(dewarVOs.size())%>&nbsp;(&nbsp;<bean:write name="genericViewShippingForm" property="listNbSamplesPerShipping[${index}]" /> &nbsp;)
			</layout:collectionItem>
																																						
			<%-- View Dewars (Users only) + managers (Issue 1789) --%>
			<logic:equal name="genericViewShippingForm" property="userOrIndus" value="true">
				<layout:collectionItem title="View<br>Components" 	paramId="shippingId" paramProperty="shippingId" sortable="false" width="40" href="<%=targetDewar%>">
					<center><img src="<%=request.getContextPath()%>/images/componentView.png" border=0 onmouseover="return overlib('View Components in Shipping');" onmouseout="return nd();"></center>
				</layout:collectionItem>
			</logic:equal>
			<logic:equal name="genericViewShippingForm" property="role" value="<%=Constants.ROLE_MANAGER%>">
				<layout:collectionItem title="View<br>Components" 	paramId="shippingId" paramProperty="shippingId" sortable="false" width="40" href="<%=targetDewar%>">
					<center><img src="<%=request.getContextPath()%>/images/componentView.png" border=0 onmouseover="return overlib('View Components in Shipping');" onmouseout="return nd();"></center>
				</layout:collectionItem>
			</logic:equal>
			
			<%-- View Samples (Users only) + managers (Issue 1789) --%>
			<logic:equal name="genericViewShippingForm" property="userOrIndus" value="true">
				<layout:collectionItem title="View<br>Samples"	paramId="shippingId" paramProperty="shippingId" sortable="false" width="40" href="<%=targetSample%>">
					<center><img src="<%=request.getContextPath()%>/images/SampleHolderView_24x24_01.png" border=0 onmouseover="return overlib('View Samples in shipping.');" onmouseout="return nd();"></center>
				</layout:collectionItem>
			</logic:equal>
			<logic:equal name="genericViewShippingForm" property="role" value="<%=Constants.ROLE_MANAGER%>">
				<layout:collectionItem title="View<br>Samples"	paramId="shippingId" paramProperty="shippingId" sortable="false" width="40" href="<%=targetSample%>">
					<center><img src="<%=request.getContextPath()%>/images/SampleHolderView_24x24_01.png" border=0 onmouseover="return overlib('View Samples in shipping.');" onmouseout="return nd();"></center>
				</layout:collectionItem>
			</logic:equal>

			<%-- Comments (Users only) --%>
			<logic:equal name="genericViewShippingForm" property="userOrIndus" value="true">
				<layout:collectionItem title="Comments" sortable="false">
				 	<div align="left"><bean:write name="currInfo"	property="comments"/></div>
				</layout:collectionItem>
			</logic:equal>
			
			<%-- Status --%>
			<%-- <layout:collectionItem title="Status" property="shippingStatus" sortable="true" /> --%>
			<layout:collectionItem title="Status" sortable="false" >
				<logic:equal name="currInfo" property="shippingStatus" value="<%=Constants.SHIPPING_STATUS_OPENED%>">
					<center>			
						<logic:equal name="genericViewShippingForm" property="userOrIndus" value="true">
							<html:link href="<%=targetDewarUser%>" paramName="currInfo" paramId="shippingId" paramProperty="shippingId">
							<img src="<%=request.getContextPath()%>/images/Error_16x16_01.png" border="0" onmouseover="return overlib('Your shipment setup is not complete.');" onmouseout="return nd()"">
							</html:link>
						</logic:equal>
						<logic:notEqual name="genericViewShippingForm" property="userOrIndus" value="true">
							<center><bean:write name="currInfo" property="shippingStatus"/></center>
						</logic:notEqual>
					</center>
				</logic:equal>
				<logic:notEqual name="currInfo" property="shippingStatus" value="<%=Constants.SHIPPING_STATUS_OPENED%>">
					<center><bean:write name="currInfo" property="shippingStatus"/></center>
				</logic:notEqual>
			</layout:collectionItem>
			
			<%-- History --%>
			<layout:collectionItem title="History" 	paramId="shippingId" paramProperty="shippingId" sortable="false" width="40" href="<%=targetShippingHistory%>">
				<logic:equal name="currInfo" property="shippingType" value="<%=Constants.DEWAR_TRACKING_SHIPPING_TYPE%>">
					<logic:equal name="genericViewShippingForm" property="listNbDewarHistoryPerShipping[${index}]"  value="0">
						<center><img src="<%=request.getContextPath()%>/images/Info_16x16_01.png" border=0 onmouseover="return overlib('View Dewar history');" onmouseout="return nd();"></center>
					</logic:equal>
					<logic:notEqual name="genericViewShippingForm" property="listNbDewarHistoryPerShipping[${index}]" value="0">
						<center><img src="<%=request.getContextPath()%>/images/Info_16x16_02.png" border=0 onmouseover="return overlib('View Dewar history (with Dewar Tracking events)');" onmouseout="return nd();"></center>
					</logic:notEqual>
				</logic:equal>
			</layout:collectionItem>
			
	
			<%-- Actions (Users only) --%>
			<logic:equal name="genericViewShippingForm" property="userOrIndus" value="true">										
				<layout:collectionItem title="Actions" >
				<div style="white-space: nowrap; text-align: left">
				&nbsp;
		
  				<%-- Shipping with processing status --%>
  				<logic:equal name="currInfo" property="shippingStatus" value="<%=Constants.SHIPPING_STATUS_PROCESS%>">
  					<font color="#ff0000" >Processing</font>
  				</logic:equal>
  		 
  				<%-- Shipping with closed status --%>
  				<logic:equal name="currInfo" property="shippingStatus" value="<%=Constants.SHIPPING_STATUS_CLOSED%>">
  					<font color="#ff0000" >Closed</font>
  				</logic:equal>
  				
  				<%-- Shipping with open status : add dewar, export  --%>
  				<logic:equal name="currInfo" property="shippingStatus" value="<%=Constants.SHIPPING_STATUS_OPENED%>">
  					
  					<%-- Add Dewar --%>
  					<logic:equal name="currInfo" property="shippingType" value="<%=Constants.DEWAR_TRACKING_SHIPPING_TYPE%>">
  						<html:link href="<%=targetCreateDewar%>" paramName="currInfo" paramId="shippingId" paramProperty="shippingId">
  							<img src="<%=request.getContextPath()%>/images/DewarAdd_24x24_01.png" border=0 onmouseover="return overlib('Add Dewar to Shipping');" onmouseout="return nd();"/>
  						</html:link>
  					</logic:equal>
  					
  					<%-- Export (only if type!=DewarTracking) --%>
  					<logic:notEqual name="currInfo" property="shippingType" value="<%=Constants.DEWAR_TRACKING_SHIPPING_TYPE%>">
  						<html:link href="<%=targetExportShipping%>" paramName="currInfo" paramId="shippingId" paramProperty="shippingId">
  							<img src="<%=request.getContextPath()%>/images/Excel_16x16_01.png" border="0" onmouseover="return overlib('Export Shipping to Excel file');" onmouseout="return nd();"/>
  						</html:link>
  					</logic:notEqual>
  				
  				</logic:equal>
  				<logic:notEqual name="currInfo" property="shippingStatus" value="<%=Constants.SHIPPING_STATUS_OPENED%>">
  					<img src="<%=request.getContextPath()%>/images/Blank_16x16_01.png" border=0 "/>
  				</logic:notEqual>
  				
  				<%-- If Shipping not processing, sent to user and not closed --%>
				<logic:notEqual name="currInfo" property="shippingStatus" value="<%=Constants.SHIPPING_STATUS_PROCESS%>">	
				<logic:notEqual name="currInfo" property="shippingStatus" value="<%=Constants.SHIPPING_STATUS_SENT_TO_USER%>">	
				<logic:notEqual name="currInfo" property="shippingStatus" value="<%=Constants.SHIPPING_STATUS_CLOSED%>">	
				
					<%-- Upload (only if type==DewarTracking) --%>
  					<logic:equal name="currInfo" property="shippingType" value="<%=Constants.DEWAR_TRACKING_SHIPPING_TYPE%>">
				
						<%-- If Shipping opened: file upload --%>
						<logic:equal name="currInfo" property="shippingStatus" value="<%=Constants.SHIPPING_STATUS_OPENED%>">	
							<html:link href="<%=targetUploadShipping%>" paramName="currInfo" paramId="shippingId" paramProperty="shippingId" styleClass="FIELD"
									onmouseover="return overlib('Upload Shipment from Excel file');" 
									onmouseout="return nd();" >
								<img src="<%=request.getContextPath()%>/images/Excel_16x16_01.png" border="0"/>
							</html:link>	
						</logic:equal>
						<%-- Else: file upload with warning --%>
						<logic:notEqual name="currInfo" property="shippingStatus" value="<%=Constants.SHIPPING_STATUS_OPENED%>">	
							<html:link href="<%=targetUploadShipping%>" paramName="currInfo" paramId="shippingId" paramProperty="shippingId" styleClass="FIELD" 
										onclick="alert('WARNING: your shipment has been sent to the facility or localised at the facility. Uploading a new file will delete existing Samples and Baskets.');" 
										onmouseover="return overlib('<DIV align=center>Upload Shipment from Excel file.<BR><B><FONT color=red>WARNING</FONT></B><BR>Your shipment has been sent to the facility or localised at the facility. Uploading a new file <B><FONT color=red>will delete existing Samples and Baskets</FONT></B>.</DIV>');" 
										onmouseout="return nd();" >
		 						<img src="<%=request.getContextPath()%>/images/Excel_16x16_01.png" border="0"/>
	 						</html:link>	
						</logic:notEqual>
					
					</logic:equal>
					
				<html:link href="<%=targetOpenShipping%>" paramName="currInfo" paramId="shippingId" paramProperty="shippingId" styleClass="FIELD"
									onmouseover="return overlib('Edit Dewars and Pucks');" 
									onmouseout="return nd();" >
								<img src="<%=request.getContextPath()%>/images/shipment16x16.png" border="0"/>
					</html:link>
				
				</logic:notEqual>
				</logic:notEqual>
				</logic:notEqual>
  					
  				<%-- Shipping with open status : send to ESRF  --%>
  				<logic:equal name="currInfo" property="shippingStatus" value="<%=Constants.SHIPPING_STATUS_OPENED%>">
  					
  					<%-- Send to ESRF (only if type==DewarTracking) --%>
  					<logic:equal name="currInfo" property="shippingType" value="<%=Constants.DEWAR_TRACKING_SHIPPING_TYPE%>">
  						<html:link href="<%=targetSubmitShipping%>" paramName="currInfo" paramId="shippingId" paramProperty="shippingId">
  							<img src="<%=request.getContextPath()%>/images/SendShipment_16x16_01.png" border="0" onmouseover="return overlib('Send Shipment to <%=Constants.ESRF_DLS%>');" onmouseout="return nd();"/>
  						</html:link>
  					</logic:equal>
  								
  					<%-- Close 
  					<html:link href="<%=targetCloseShipping%>" paramName="currInfo" paramId="shippingId" paramProperty="shippingId" onclick="return window.confirm('Do you really want to close this shipping?');">
  						<img src="<%=request.getContextPath()%>/images/lock.gif" border=0 onmouseover="return overlib('Close the Shipping');" onmouseout="return nd();">
  					</html:link>
  					--%>
  	
  				</logic:equal>
				
			&nbsp;
			</div>
			</layout:collectionItem>	
        								
			<layout:collectionItem title="" >
        	<div style="white-space: nowrap; text-align: center">
			&nbsp;
				
  				<%-- Shipping with not closed and not processing status : delete --%> 
  				<logic:notEqual name="currInfo" property="shippingStatus" value="<%=Constants.SHIPPING_STATUS_PROCESS%>">
  				    <logic:notEqual name="currInfo" property="shippingStatus" value="<%=Constants.SHIPPING_STATUS_CLOSED%>">
  						
  						<%-- Delete --%>
  						<html:link href="<%=targetDeleteShipping%>" paramName="currInfo" paramId="shippingId" paramProperty="shippingId" onclick="return window.confirm('Do you really want to delete this shipping and samples associated?');">
  							<img src="<%=request.getContextPath()%>/images/cancel.png" border=0 onmouseover="return overlib('Delete the Shipping');" onmouseout="return nd();"/>
  						</html:link>
  						
  					</logic:notEqual>
  				</logic:notEqual>
  				
  			&nbsp;
			</div>
			</layout:collectionItem>
			</logic:equal>
			
		</layout:collection>
		</layout:pager>
		
		</layout:panel>
	
	</layout:grid>
	
	<bean:define id="results" name="genericViewShippingForm" property="listShippings" type="java.util.ArrayList"/>
	<h4><%=Integer.toString(results.size())%>&nbsp;results&nbsp;found </h4>
</logic:notEmpty>

