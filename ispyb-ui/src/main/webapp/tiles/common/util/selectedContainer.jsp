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
	String targetCreateSample		= request.getContextPath() + "/menuSelected.do?leftMenuId=6&topMenuId=5&targetUrl=/user/createSampleAction.do?reqCode=display";
	String targetSelectedContainer 	= request.getContextPath() + "/user/viewSample.do?reqCode=displayForContainer";
	String selectedContainerTitle	= "Container";
%>

<%-- Selected Container -----------------------------------------------------------------------------%>
<logic:present 	name="breadCrumbsForm" 	property="selectedContainer" scope="session">
	<bean:define name="breadCrumbsForm" property="selectedContainer" id="selectedContainer" 	type="ispyb.server.common.vos.shipping.Container3VO"/>

	<%-- Selected Container Info --%>
	<layout:column  styleClass="PANEL_BREAD_CRUMBS">
		<layout:panel key="<%=selectedContainerTitle%>" align="left" styleClass="PANEL_BREAD_CRUMBS_ITEM">
			<layout:text key="Label" 		name="selectedContainer" 	property="code" 					styleClass="PANEL_BREAD_CRUMBS_FIELD" mode="I,I,I"/>
			<layout:text key="Type" 		name="selectedContainer" 	property="containerType" 			styleClass="PANEL_BREAD_CRUMBS_FIELD" mode="I,I,I"/>
			
			<logic:present name="breadCrumbsForm" 	property="selectedContainer" 	scope="session">
				<bean:define name="breadCrumbsForm" property="selectedContainer" 	scope="session" id="selectedContainer" 	type="ispyb.server.common.vos.shipping.Container3VO"/>
             		<tr><th class="PANEL_BREAD_CRUMBS_FIELD" valign="top"></th><td class="PANEL_BREAD_CRUMBS_FIELD" valign="top" style="">
	             		<html:link href="<%=targetSelectedContainer%>" paramName="selectedContainer" paramId="containerId" paramProperty="containerId" styleClass="PANEL_BREAD_CRUMBS_FIELD">
							Back to this Container
						</html:link>
					</td></tr>
			</logic:present>
		</layout:panel>
		
		<%-- Add if shipping is not "Closed" or "processing" or not fedexManager--%>
<logic:notEqual name="breadCrumbsForm" property="userRole" value="<%=Constants.FXMANAGE_ROLE_NAME%>">
<logic:notEmpty name="breadCrumbsForm" property="selectedShipping" 		scope="session">
		<logic:notEqual name="breadCrumbsForm" property="selectedShipping.shippingStatus" value="<%=Constants.SHIPPING_STATUS_CLOSED%>">
		<logic:notEqual name="breadCrumbsForm" property="selectedShipping.shippingStatus" value="<%=Constants.SHIPPING_STATUS_PROCESS%>">

			<layout:link href="<%=targetCreateSample%>" paramName="selectedContainer" paramId="containerId" paramProperty="containerId" styleClass="PANEL_BREAD_CRUMBS_FIELD">
				<img src="<%=request.getContextPath()%>/images/SampleHolderAdd_24x24_01.png" border=0  vspace="2" onmouseover="return overlib('Add Sample to Container');" onmouseout="return nd();">Add Sample to Container
			</layout:link>
		</logic:notEqual>
		</logic:notEqual>
</logic:notEmpty>
</logic:notEqual>

	</layout:column>
</logic:present>
