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

<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>


<%
	String targetDewar 		 		= request.getContextPath() + "/menuSelected.do?leftMenuId=71&topMenuId=5&targetUrl=/user/viewDewarAction.do?reqCode=displaySlave";
	String targetDewarHistory		= request.getContextPath() + "/menuSelected.do?leftMenuId=71&topMenuId=5&targetUrl=/reader/viewDewarHistoryAction.do?reqCode=display";
	
	String targetShipping	 		= request.getContextPath() + "/menuSelected.do?leftMenuId=71&topMenuId=5&targetUrl=/reader/viewDewarAction.do?reqCode=readOnly";
	
	String targetLabContact 		= request.getContextPath() + "/reader/createLabContactAction.do?reqCode=readOnly";
	String targetLabContactUpdate 	= request.getContextPath() + "/user/createLabContactAction.do?reqCode=updateDisplay";

	//String targetContainer 			= request.getContextPath() + "/menuSelected.do?leftMenuId=11&topMenuId=10&targetUrl=/user/viewSample.do?reqCode=displayForContainer";
	String targetContainer 			= request.getContextPath() + "/menuSelected.do?leftMenuId=71&topMenuId=5&targetUrl=/user/viewDewarAction.do?reqCode=displaySlave";
	//String targetDewarLabels		= request.getContextPath() + "/user/viewDewarAction.do?reqCode=generateLabels";
	String targetDewarLabels		= request.getContextPath() + "/reader/viewDewarAction.do?reqCode=generateLabels";
	String targetSample 			= request.getContextPath() + "/menuSelected.do?topMenuId=10&targetUrl=/user/viewSample.do?reqCode=displayForDewar";
		
	String FedexTrackingURL			= "http://www.fedex.com/Tracking?ascend_header=1&clienttype=dotcomreg&cntry_code=gb&language=english";
	String TntTrackingURL			= "http://www.tnt.com/webtracker/tracking.do?requestType=GEN&searchType=CON&navigation=1&respLang=en&respCountry=GB&genericSiteIdent=";
	String DhlTrackingURL			= "http://www.dhl.com/publish/g0/en/eshipping/track.high.html?pageToInclude=RESULTS&type=fasttrack";
	String UpsTrackingURL			= "http://wwwapps.ups.com/WebTracking/track";
	String WorldCourierTrackingURL	= "http://www.worldcouriercrc.com/CRC/FTRetry.cfm?pickupD=";
%>

<script language="javascript"> 
	function setValue(myfield,value)
	{
		myfield.value = value;
		return false;
	}
</script> 

<script TYPE="text/javascript">
	function changeRowStyle( tag , newStyle) {
	
		  var child = tag.firstChild;
		  while (child != null) {
		   		if ( child.className != 'LISTORANGE' ) child.className = newStyle;
		    	child = child.nextSibling;
		  }
	}
</script>


<%-- Display messages --%>
<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />

<%-------------------------------------------%>
<%-- Prepare your experiment 			   --%>
<%-------------------------------------------%>
<h4>Prepare your experiment:</h4>  
<p align="justify">

	<%-- 1) Select shipment --%>
	<b>
		1- Select the dewar</a> you want for processing.<br>
	</b>

	<%-- 2) Fill Sample changer --%>
	<span class="greyText">
	<c:if test="${SITE_ATTRIBUTE eq 'ESRF' or SITE_ATTRIBUTE eq 'EMBL'}"> 
		2- Fill the sample changer</a>: assign a location for your containers (only required if not using Damatrix codes)<br>
	</c:if>
	<c:if test="${SITE_ATTRIBUTE eq 'DLS'}">
		2- Fill the sample changer</a>: assign a location for your containers<br>
	</c:if> 
	<c:if test="${SITE_ATTRIBUTE eq 'MAXIV'}">
		2- Fill the sample changer</a>: assign a location for your containers<br>
	</c:if>
	<c:if test="${SITE_ATTRIBUTE eq 'SOLEIL'}">
		2- Fill the sample changer</a>: assign a location for your containers<br>
	</c:if>
	</span>

	<%-- 3) Link crystals --%>
	<span class="greyText">
	<c:if test="${SITE_ATTRIBUTE ne 'ESRF' and SITE_ATTRIBUTE ne 'EMBL'}">
		3- Associate data collections to samples in <%=Constants.BCM_NAME%>.<br>
	</c:if> 
	<c:if test="${SITE_ATTRIBUTE eq 'ESRF' or SITE_ATTRIBUTE eq 'EMBL'}">
		3- In MxCuBe, link the samples in the container to the crystals in the Sample Changer.<br>
	</c:if>
	<c:if test="${SITE_ATTRIBUTE eq 'SOLEIL'}"> 
		3- In MxCuBe, link the samples in the container to the crystals in the Sample Changer.<br>
	</c:if>

	</span>
	
	<%-- Steps --%>
	<br>
	<a class="noUnderline" href="<%=request.getContextPath()%>/helpPrepareGuestPage.do" title="Prepare your experiment">
	<img src="<%=request.getContextPath()%>/images/Previous-16x16.png" border=0>
	</a>
	<a href="<%=request.getContextPath()%>/user/fillSampleChanger.do?reqCode=display" title="Fill the sample changer">
	<img src="<%=request.getContextPath()%>/images/Next-16x16.png" border=0></a>
	<input type=button value='Next step: Fill the sample changer' onclick="parent.location='<%=request.getContextPath()%>/user/fillSampleChanger.do?reqCode=display'"">	

</p>

<%-------------------------------------------%>
<%-- Select Dewar                          --%>
<%-------------------------------------------%>

<h4>Note that the dewar shipments will be set in "processing" state and will not be editable any more.</h4>

<%-- To not show a empty table when no dewar exists --%>
<logic:empty  name="genericViewDewarForm" property="listDewars">
          <h4>No&nbsp;Dewar&nbsp;have&nbsp;been&nbsp;found</h4>
</logic:empty>

<logic:notEmpty name="genericViewDewarForm" property="listDewars">
  <layout:grid cols="1"  borderSpacing="10">

	<layout:panel key="Select dewars for processing" align="left" styleClass="PANEL" width="85%">
			
		<layout:pager maxPageItems="20" styleClass="LIST">
		
		<%-- Form --%>
		<html:form action="/user/prepareExp.do">
		<html:hidden property="reqCode" value="updateDewar"/>
		<html:hidden property="actionName" value="none"/>
		<html:hidden property="dewarId" value="none"/>
		
		<%-- Collection --%>
		<layout:collection 	name="genericViewDewarForm" property="listDewars"  styleClass="LIST" id="dewar" indexId="index" onRowMouseOver="changeRowStyle(this,'LIST_OVER')" onRowMouseOut="changeRowStyle(this,'LIST')">			
			
			<layout:collectionStyle name="dewar" property="dewarStatus" value="<%=Constants.DEWAR_STATUS_PROCESS%>" matchStyleClass="LISTORANGE"> 

				<%-- Shipping name --%>
				<layout:collectionItem title="Ship. name" name="dewar" property="shippingVO.shippingName" sortable="true"/>
				
				<%-- Shipping creationDate --%>
				<layout:collectionItem title="Creation<br>date" name= "dewar" property="shippingVO.creationDate" sortable="true" type="date"/>
				
				<%-- Dewar name --%>
				<logic:equal name="genericViewDewarForm" property="userOrIndus" value="true">
					<layout:collectionItem title="Comp. name" 	property="code" sortable="true" href="<%=targetDewar%>"  paramId="dewarId" paramProperty="dewarId"/>
				</logic:equal>
				<logic:notEqual name="genericViewDewarForm" property="userOrIndus" value="true">
					<layout:collectionItem title="Comp. name" 	property="code" sortable="true" href="<%=targetShipping%>"  paramId="shippingId,proposalId" paramProperty="shippingId,proposalId"/>
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
				
				<%-- Comments--%>
				<logic:equal name="genericViewDewarForm" property="userOrIndus" value="true">
					<layout:collectionItem title="Comments" 	property="comments" sortable="false"/>
				</logic:equal>
				
				<%-- Barcode --%>
				<layout:collectionItem title="Barcode" property="barCode" sortable="false"/>
				
				
				<%-- Exp. date --%>
				<layout:collectionItem title="Exp. date" name="dewar"  property="sessionVO.startDate" sortable="true" type="date"/>
				
				<%-- Beamline --%>
				<layout:collectionItem title="Beamline" name="dewar"   property="sessionVO.beamlineName" sortable="true"/>		
				
				<%-- Nb containers/samples --%>
				<layout:collectionItem title="#&nbsp;containers<BR>(&nbsp;#&nbsp;samples&nbsp;)" sortable="false">
					<bean:define id="containerVOs" name="dewar" property="containerVOs" type="java.util.Set"/>
					<%=Integer.toString(containerVOs.size())%>&nbsp;
						<logic:equal name="dewar"	property="samplesNumber" value ="0">
							<font color="#ff0000" >(&nbsp;<bean:write name="dewar"	property="samplesNumber"/>&nbsp;)</font>
						</logic:equal>
						<logic:notEqual name="dewar"	property="samplesNumber" value ="0">
							(&nbsp;<bean:write name="dewar"	property="samplesNumber"/>&nbsp;)
						</logic:notEqual>
				</layout:collectionItem>
					
				<%-- Local contact --%>
				<layout:collectionItem title="Local contact" name="dewar"   property="sessionVO.beamlineOperator" sortable="false"/>
				
				
				<%-- Shipping date --%>
				<logic:notEqual name="genericViewDewarForm" property="userOrIndus" value="true">
				<layout:collectionItem title="Creation date" name="dewar"   property="shippingVO.creationDate" sortable="true" type="date"/>
				</logic:notEqual>
								
				<%-- Status --%>
				<layout:collectionItem title="Dewar status"  property="dewarStatus"   sortable="true" />
				
				<%-- Location --%>
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
				
				<%-- Lab-contact for return 
				<logic:equal name="genericViewDewarForm" property="userOrIndus" value="true">
				<layout:collectionItem title="Return details" 	property="lcReturnCardName" 	
															sortable="true" href="<%=targetLabContactUpdate%>"
															paramId="labContactId" paramProperty="lcReturnId"
															/>
				</logic:equal>
				<logic:notEqual name="genericViewDewarForm" property="userOrIndus" value="true">
				<layout:collectionItem title="Return details" 	property="lcReturnCardName" 	
															sortable="true" href="<%=targetLabContact%>"
															paramId="labContactId" paramProperty="lcReturnId"/>
				</logic:notEqual>
				--%>
				
				<%-- Date of shipping to user --%>
				<logic:notEqual name="genericViewDewarForm" property="userOrIndus" value="true">
					<layout:collectionItem title="Date of shipping to user" property="dateOfShippingToUser" type="date" sortable="true"/>
				</logic:notEqual>
								
				<%-- <%-- History 
				<layout:collectionItem title="History" 	paramId="dewarId" paramProperty="dewarId" sortable="false" width="80" href="<%=targetDewarHistory%>">
					<logic:equal name="dewar" property="shippingVO.shippingType" value="<%=Constants.DEWAR_TRACKING_SHIPPING_TYPE%>">
						<logic:empty name="dewar" property="dewarTransportHistoryVOs">
							<center><img src="<%=request.getContextPath()%>/images/magnif.png" border=0 onmouseover="return overlib('View Dewar history');" onmouseout="return nd();"></center>
						</logic:empty>
						<logic:notEmpty name="dewar" property="dewarTransportHistoryVOs" >
							<center><img src="<%=request.getContextPath()%>/images/magnif2.png" border=0 onmouseover="return overlib('View Dewar history (Dewar Tracking events)');" onmouseout="return nd();"></center>
						</logic:notEmpty>
					</logic:equal>
				</layout:collectionItem>
				--%>
				
				<%-- <%-- Print labels 
				<layout:collectionItem title="Comp. labels" width="80">
					<%-- Visible only if shippingType = 'DewarTracking' --%>
					<%-- <logic:equal name="dewar" property="shippingVO.shippingType" value="<%=Constants.DEWAR_TRACKING_SHIPPING_TYPE%>">
						
						<logic:notEmpty name="dewar" property="sessionVO">
						<logic:notEmpty name="dewar" property="sessionVO.startDate">
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
						
               			<logic:empty name="dewar" property="sessionVO.startDate">
                 			<html:link href="<%=targetDewarLabels%>" paramName="dewar" paramId="dewarId" paramProperty="dewarId">
								<center><img src="<%=request.getContextPath()%>/images/print_warning.png" border="0" onclick="alert('Please print your component labels and stick them on your shipment components prior to sending (see instructions on first page).\n\nWARNING: your labels won\'t have Beamline, Experiment Date and Local Contact (you should setup an experiment for this dewar).');this.src='<%=request.getContextPath()%>/images/print.png';" onmouseover="return overlib('Print component labels (incomplete)');" onmouseout="return nd();"></center>
							</html:link>
						</logic:empty>
						
						</logic:notEmpty>
					</logic:equal>
				</layout:collectionItem>
				--%>
				
				
				<%-- Action --%>
				<layout:collectionItem title="Select for<br>processing">
					<logic:notEqual name="dewar" property="dewarStatus" value="<%=Constants.DEWAR_STATUS_PROCESS%>">
						<input name="Action" type="submit" value="Select" onclick="setValue(this.form.actionName,'setdewar');setValue(this.form.dewarId,'<bean:write name="dewar" property="dewarId"/>');" onmouseover="return overlib('Set dewar to processing');" onmouseout="return nd();">
					</logic:notEqual>
					<logic:equal name="dewar" property="dewarStatus" value="<%=Constants.DEWAR_STATUS_PROCESS%>">
						<input name="Action" type="submit" value="Deselect" onclick="setValue(this.form.actionName,'unsetdewar');setValue(this.form.dewarId,'<bean:write name="dewar" property="dewarId"/>');" onmouseover="return overlib('Deselect dewar');" onmouseout="return nd();">
					</logic:equal>
				</layout:collectionItem>		
			</layout:collectionStyle>
		</layout:collection>
		</html:form>
		</layout:pager>	
		
		
	</layout:panel>
  </layout:grid>
  
  <bean:define id="results" name="genericViewDewarForm" property="listDewars" type="java.util.ArrayList"/>
  <bean:define id="nbDays" name="genericViewDewarForm" property="nbDays" />
   <h4><%=Integer.toString(results.size())%>&nbsp;results&nbsp;found <logic:notEqual name="genericViewDewarForm" property="nbDays" value="0">in the last <%=nbDays%> days </logic:notEqual></h4>
</logic:notEmpty>






