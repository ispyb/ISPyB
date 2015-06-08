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

<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>


<% 
	String targetViewSampleDetails = request.getContextPath() + "/user/viewSample.do?reqCode=displayAllDetails";
	
	String targetDewar 		 		= request.getContextPath() + "/reader/viewDewarAction.do?reqCode=readOnly";
	String targetDewarUser 			= request.getContextPath() + "/user/viewDewarAction.do?reqCode=display";
	String targetSample 			= request.getContextPath() + "/user/viewSample.do?reqCode=displayForShipping";
	String targetShippingHistory	= request.getContextPath() + "/reader/viewShippingHistoryAction.do?reqCode=display";


%>


<%-- to display messages --%>
<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />

<%-------------------------------------------%>
<%-- Prepare your experiment 			   --%>
<%-------------------------------------------%>

<h4>Prepare your experiment:</h4>  
<p align="justify">

	<%-- 1) Select shipment --%>
	<b>
		1- Select the shipments</a> you want for processing.<br>
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
		3- In MxCuBe, link the samples in the shipment to the crystals in the Sample Changer.<br>
	</span>
	
	<%-- Steps --%>
	<br>
	<a class="noUnderline" href="<%=request.getContextPath()%>/helpPrepareGuestPage.do" title="Prepare your experiment">
	<img src="<%=request.getContextPath()%>/images/Previous-16x16.png" border=0>
	</a>
	<a href="<%=request.getContextPath()%>/user/fillSampleChanger.do?reqCode=display" title="Fill the sample changer">
	<img src="<%=request.getContextPath()%>/images/Next-16x16.png" border=0>
	Next step: Fill the sample changer
	</a>
	
</p>

<%-------------------------------------------%>
<%-- Select Shipment                          --%>
<%-------------------------------------------%>

<h4>Note that the selected shipments will be set in "processing" state and will not be editable any more.</h4>

<%-- To not show a empty page when no closed shipment exists --%>	
<logic:empty name="viewShippingForm" property="listInfo">
           <h4>No&nbsp;shipments&nbsp;have&nbsp;been&nbsp;found.</h4>
</logic:empty>


<%-- Struts table with editable cells --%>

<layout:grid cols="1"  borderSpacing="1">
<layout:column>

<layout:panel key="Select shipments for processing" align="center" styleClass="PANEL">
<table cellspacing="1" cellpadding="3" border="0" width="100%" align="CENTER" class="LIST">
	<tr>
		<th class="LIST">Name</th>
		<c:if test="${SITE_ATTRIBUTE eq 'ESRF' or SITE_ATTRIBUTE eq 'SOLEIL'}"> 
			<th class="LIST">Creation date</th>
		</c:if>
		<%--
		<th class="LIST">Shipping date</th>
		<th class="LIST">Courier name</th>
		--%>
		<th class="LIST">#&nbsp;components<BR>(&nbsp;#&nbsp;samples&nbsp;)</th>
		<th class="LIST">View Dewars</th>
		<th class="LIST">View Samples</th>
		<th class="LIST">Comments</th>
		<th class="LIST">Shipment status</th>
		<th class="LIST">History</th>
		<th class="LIST">Select for experiment</th>
		<th class="LIST">Save</th>
	</tr>
	<logic:iterate name="viewShippingForm" property="listInfo" id="item" type="ispyb.server.common.vos.shipping.Shipping3VO">
		<html:form action="/user/prepareExp.do">
			<html:hidden property="reqCode" value="updateShipmentStatus"/>
			<html:hidden property="theShippingId" value="<%=item.getShippingId().toString()%>"/>
			<tr>
			
				<%-- Shipping Name --%>
				<td class="LIST_LEFT">
					<html:link href="<%=targetDewarUser + "&shippingId=" + item.getShippingId()%>" styleClass="LIST">
						<bean:write name="item" property="shippingName" />
					</html:link>
				</td>
					
				<%-- Creation date --%>
				<c:if test="${SITE_ATTRIBUTE eq 'ESRF' or SITE_ATTRIBUTE eq 'SOLEIL'}"> 
					<td class="LIST"><bean:write name="item" property="creationDate" format="dd-MM-yyyy"/></td>
				</c:if>
				
				<%-- Delivery --%>
				<%--
				<td class="LIST"><bean:write name="item" property="deliveryAgentShippingDate"/></td>
				<td class="LIST"><bean:write name="item" property="deliveryAgentAgentName"/></td>
				--%>
				
				<%-- Content --%>
				<td class="LIST">
					<bean:write name="item"	property="parcelsNumber"/>&nbsp;(&nbsp;<bean:write name="item"	property="samplesNumber"/>&nbsp;)
				</td>
				
				<%-- View Dewars --%>
				<td class="LIST">
					<html:link href="<%=targetDewarUser + "&shippingId=" + item.getShippingId()%>" styleClass="LIST">
						<center><img src="<%=request.getContextPath()%>/images/DewarView_24x24_01.png" border=0 onmouseover="return overlib('View Dewars in Shipping');" onmouseout="return nd();"></center>
					</html:link>
				</td>
				
				<%-- View Samples --%>
				<td class="LIST">
					<html:link href="<%=targetSample + "&shippingId=" + item.getShippingId()%>" styleClass="LIST">
						<center><img src="<%=request.getContextPath()%>/images/SampleHolderView_24x24_01.png" border=0 onmouseover="return overlib('View Samples in shipping.');" onmouseout="return nd();"></center>
					</html:link>
				</td>
				
				<%-- Comments --%>
				<td class="LIST">
					<bean:write name="item" property="comments"/>
				</td>
				
				<%-- Shipping status --%>
				<logic:notEqual name="item" property="shippingStatus" value="<%=Constants.SHIPPING_STATUS_PROCESS%>">
					<td class="LIST"><bean:write name="item" property="shippingStatus"/></td>
				</logic:notEqual>
				<logic:equal name="item" property="shippingStatus" value="<%=Constants.SHIPPING_STATUS_PROCESS%>">
					<td class="LIST"><font color=red><bean:write name="item" property="shippingStatus"/></font></td>
				</logic:equal>
				
				<%-- History --%>
				<td class="LIST">
					<logic:equal name="item" property="shippingType" value="<%=Constants.DEWAR_TRACKING_SHIPPING_TYPE%>">
						<html:link href="<%=targetShippingHistory + "&shippingId=" + item.getShippingId()%>" styleClass="LIST">
							<logic:equal name="item" property="eventsNumber" value="0">
								<center><img src="<%=request.getContextPath()%>/images/magnif.png" border=0 onmouseover="return overlib('View Dewar history');" onmouseout="return nd();"></center>
							</logic:equal>
							<logic:notEqual name="item" property="eventsNumber" value="0">
								<center><img src="<%=request.getContextPath()%>/images/magnif2.png" border=0 onmouseover="return overlib('View Dewar history (with Dewar Tracking events)');" onmouseout="return nd();"></center>
							</logic:notEqual>
						</html:link>
					</logic:equal>
				</td>

				<%-- Select check box  --%>
				<%
					((ispyb.client.common.shipping.ViewShippingForm)request.getAttribute("viewShippingForm")).setSelectForExp(Boolean.FALSE);
				%>
				<logic:equal name="item" property="shippingStatus" value="<%=Constants.SHIPPING_STATUS_PROCESS%>">
					<%
						((ispyb.client.common.shipping.ViewShippingForm)request.getAttribute("viewShippingForm")).setSelectForExp(Boolean.TRUE);
					%>
				</logic:equal>
				<td class="LIST"><html:checkbox property="selectForExp" value="1" /></td>
			
				<%-- Save button --%>
			    <td class="LIST"><html:submit value="Select"/></td>
			    
			</tr>
		</html:form>
	</logic:iterate>
</table>
</layout:panel>

<layout:space/>



</layout:column>
</layout:grid>






