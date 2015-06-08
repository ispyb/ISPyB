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
String targetCreateContainer	= request.getContextPath() + "/menuSelected.do?leftMenuId=6&topMenuId=5&targetUrl=/user/createPuckAction.do?reqCode=display";
	String targetViewDewars			= request.getContextPath() + "/menuSelected.do?leftMenuId=8&topMenuId=5&targetUrl=/user/viewDewarAction.do?reqCode=display";
	String selectedDewarTitle		= "Component";
	String targetSelectedDewar 		= request.getContextPath() + "/user/viewDewarAction.do?reqCode=displaySlave";
%>
<logic:equal name="breadCrumbsForm" property="userRole" value="<%=Constants.FXMANAGE_ROLE_NAME%>">
	<%	targetSelectedDewar 		= request.getContextPath() + "/user/viewSample.do?reqCode=displayForDewar"; %>
</logic:equal>

<%-- Selected Dewar -----------------------------------------------------------------------------%>
<logic:present name="breadCrumbsForm" 	property="selectedDewar" 	scope="session">
	<bean:define name="breadCrumbsForm" property="selectedDewar" 	scope="session" id="selectedDewar" 	type="ispyb.server.common.vos.shipping.Dewar3VO"/>

	<%-- Selected Dewar Info --%>
	<layout:column  styleClass="PANEL_BREAD_CRUMBS">
		<layout:panel key="<%=selectedDewarTitle%>" align="left" styleClass="PANEL_BREAD_CRUMBS_ITEM">
          <layout:text key="Label" 	name="selectedDewar" 	property="code" 		styleClass="PANEL_BREAD_CRUMBS_FIELD" mode="I,I,I"/>
 		  <%--<layout:text key="Status" 	name="selectedDewar" 	property="dewarStatus" 	styleClass="PANEL_BREAD_CRUMBS_FIELD" mode="I,I,I"/>--%>
 		  <tr><th class="PANEL_BREAD_CRUMBS_FIELD" valign="top"></th><td class="PANEL_BREAD_CRUMBS_FIELD" valign="top" style="">
          	<html:link href="<%=targetSelectedDewar%>" paramName="selectedDewar" paramId="dewarId" paramProperty="dewarId" styleClass="PANEL_BREAD_CRUMBS_FIELD">
				Back to this component
			</html:link>
		  </td></tr>
		</layout:panel>
		
		<%-- Add if shipping is not "Closed" or not "processing" or not fedex manager--%>
		
		<logic:notEqual name="breadCrumbsForm" property="userRole" value="<%=Constants.FXMANAGE_ROLE_NAME%>">
		<logic:notEqual name="breadCrumbsForm" property="selectedShipping.shippingStatus" value="<%=Constants.SHIPPING_STATUS_CLOSED%>">
		<logic:notEqual name="breadCrumbsForm" property="selectedShipping.shippingStatus" value="<%=Constants.SHIPPING_STATUS_PROCESS%>">
			<layout:link href="<%=targetCreateContainer%>" paramName="selectedDewar" paramId="dewarId" paramProperty="dewarId" styleClass="PANEL_BREAD_CRUMBS_FIELD">
				<img src="<%=request.getContextPath()%>/images/BasketAdd_24x24_01.png" border=0  vspace="2" onmouseover="return overlib('Add Container to Dewar');" onmouseout="return nd();">Add Container to Dewar
			</layout:link>
		</logic:notEqual>
		</logic:notEqual>
		</logic:notEqual>
	</layout:column>
</logic:present>
